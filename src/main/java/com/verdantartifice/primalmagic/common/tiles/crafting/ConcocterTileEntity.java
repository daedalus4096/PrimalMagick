package com.verdantartifice.primalmagic.common.tiles.crafting;

import java.util.UUID;

import javax.annotation.Nullable;

import com.verdantartifice.primalmagic.common.blocks.crafting.ConcocterBlock;
import com.verdantartifice.primalmagic.common.capabilities.IManaStorage;
import com.verdantartifice.primalmagic.common.capabilities.ManaStorage;
import com.verdantartifice.primalmagic.common.capabilities.PrimalMagicCapabilities;
import com.verdantartifice.primalmagic.common.concoctions.ConcoctionUtils;
import com.verdantartifice.primalmagic.common.concoctions.FuseType;
import com.verdantartifice.primalmagic.common.containers.ConcocterContainer;
import com.verdantartifice.primalmagic.common.crafting.IConcoctingRecipe;
import com.verdantartifice.primalmagic.common.crafting.RecipeTypesPM;
import com.verdantartifice.primalmagic.common.sources.IManaContainer;
import com.verdantartifice.primalmagic.common.sources.Source;
import com.verdantartifice.primalmagic.common.sources.SourceList;
import com.verdantartifice.primalmagic.common.tiles.TileEntityTypesPM;
import com.verdantartifice.primalmagic.common.tiles.base.IOwnedTileEntity;
import com.verdantartifice.primalmagic.common.tiles.base.TileInventoryPM;
import com.verdantartifice.primalmagic.common.wands.IWand;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;

public class ConcocterTileEntity extends TileInventoryPM implements  MenuProvider, IOwnedTileEntity, IManaContainer {
    protected static final int MAX_INPUT_ITEMS = 9;
    protected static final int WAND_SLOT_INDEX = 9;
    protected static final int OUTPUT_SLOT_INDEX = 10;
    protected static final int[] SLOTS_FOR_UP = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8 };
    protected static final int[] SLOTS_FOR_DOWN = new int[] { 10 };
    protected static final int[] SLOTS_FOR_SIDES = new int[] { 9 };
    
    protected int cookTime;
    protected int cookTimeTotal;
    protected ManaStorage manaStorage;
    protected UUID ownerUUID;
    protected Player ownerCache;

    protected LazyOptional<IManaStorage> manaStorageOpt = LazyOptional.of(() -> this.manaStorage);
    
    // Define a container-trackable representation of this tile's relevant data
    protected final ContainerData concocterData = new ContainerData() {
        @Override
        public int get(int index) {
            switch (index) {
            case 0:
                return ConcocterTileEntity.this.cookTime;
            case 1:
                return ConcocterTileEntity.this.cookTimeTotal;
            case 2:
                return ConcocterTileEntity.this.manaStorage.getManaStored(Source.INFERNAL);
            case 3:
                return ConcocterTileEntity.this.manaStorage.getMaxManaStored(Source.INFERNAL);
            default:
                return 0;
            }
        }

        @Override
        public void set(int index, int value) {
            // Don't set mana storage values
            switch (index) {
            case 0:
                ConcocterTileEntity.this.cookTime = value;
                break;
            case 1:
                ConcocterTileEntity.this.cookTimeTotal = value;
                break;
            }
        }

        @Override
        public int getCount() {
            return 4;
        }
    };
    
    public ConcocterTileEntity(BlockPos pos, BlockState state) {
        super(TileEntityTypesPM.CONCOCTER.get(), pos, state, MAX_INPUT_ITEMS + 2);
        this.manaStorage = new ManaStorage(10000, 1000, 1000, Source.INFERNAL);
    }
    
    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        
        this.cookTime = compound.getInt("CookTime");
        this.cookTimeTotal = compound.getInt("CookTimeTotal");
        this.manaStorage.deserializeNBT(compound.getCompound("ManaStorage"));
        
        this.ownerCache = null;
        this.ownerUUID = null;
        if (compound.contains("OwnerUUID")) {
            this.ownerUUID = compound.getUUID("OwnerUUID");
        }
    }

    @Override
    public CompoundTag save(CompoundTag compound) {
        compound.putInt("CookTime", this.cookTime);
        compound.putInt("CookTimeTotal", this.cookTimeTotal);
        compound.put("ManaStorage", this.manaStorage.serializeNBT());
        if (this.ownerUUID != null) {
            compound.putUUID("OwnerUUID", this.ownerUUID);
        }
        return super.save(compound);
    }

    @Override
    public AbstractContainerMenu createMenu(int windowId, Inventory playerInv, Player player) {
        return new ConcocterContainer(windowId, playerInv, this, this.concocterData);
    }

    @Override
    public void setTileOwner(Player owner) {
        this.ownerCache = owner;
        this.ownerUUID = owner.getUUID();
    }

    @Override
    public Player getTileOwner() {
        if (this.ownerUUID != null && this.hasLevel() && this.level instanceof ServerLevel serverLevel) {
            Player livePlayer = serverLevel.getServer().getPlayerList().getPlayer(this.ownerUUID);
            if (livePlayer == null) {
                // If no matching player is found in the server list, presumably because they're offline, return the cached player object
                return this.ownerCache;
            } else {
                // Otherwise, update the cache and return the live player
                this.ownerCache = livePlayer;
                return livePlayer;
            }
        }
        return null;
    }

    @Override
    public Component getDisplayName() {
        return new TranslatableComponent(this.getBlockState().getBlock().getDescriptionId());
    }

    public static void tick(Level level, BlockPos pos, BlockState state, ConcocterTileEntity entity) {
        boolean shouldMarkDirty = false;
        
        if (!level.isClientSide) {
            // Fill up internal mana storage with that from any inserted wands
            ItemStack wandStack = entity.items.get(WAND_SLOT_INDEX);
            if (!wandStack.isEmpty() && wandStack.getItem() instanceof IWand) {
                IWand wand = (IWand)wandStack.getItem();
                int centimanaMissing = entity.manaStorage.getMaxManaStored(Source.INFERNAL) - entity.manaStorage.getManaStored(Source.INFERNAL);
                int centimanaToTransfer = Mth.clamp(centimanaMissing, 0, 100);
                if (wand.consumeMana(wandStack, null, Source.INFERNAL, centimanaToTransfer)) {
                    entity.manaStorage.receiveMana(Source.INFERNAL, centimanaToTransfer, false);
                    shouldMarkDirty = true;
                }
            }

            SimpleContainer realInv = new SimpleContainer(MAX_INPUT_ITEMS);
            SimpleContainer testInv = new SimpleContainer(MAX_INPUT_ITEMS);
            for (int index = 0; index < MAX_INPUT_ITEMS; index++) {
                ItemStack invStack = entity.items.get(index);
                realInv.setItem(index, invStack);
                // Don't consider fuse length when testing item inputs for recipe determination
                testInv.setItem(index, ConcoctionUtils.isBomb(invStack) ? ConcoctionUtils.setFuseType(invStack.copy(), FuseType.MEDIUM) : invStack);
            }
            IConcoctingRecipe recipe = level.getServer().getRecipeManager().getRecipeFor(RecipeTypesPM.CONCOCTING, testInv, level).orElse(null);
            if (entity.canConcoct(realInv, recipe)) {
                entity.cookTime++;
                if (entity.cookTime >= entity.cookTimeTotal) {
                    entity.cookTime = 0;
                    entity.cookTimeTotal = entity.getCookTimeTotal();
                    entity.doConcoction(realInv, recipe);
                    shouldMarkDirty = true;
                }
            } else {
                entity.cookTime = Mth.clamp(entity.cookTime - 2, 0, entity.cookTimeTotal);
            }
            
            level.setBlock(pos, state.setValue(ConcocterBlock.HAS_BOTTLE, entity.showBottle()), Constants.BlockFlags.BLOCK_UPDATE);
        }

        if (shouldMarkDirty) {
            entity.setChanged();
            entity.syncTile(true);
        }
    }
    
    protected boolean canConcoct(Container inputInv, @Nullable IConcoctingRecipe recipe) {
        if (!inputInv.isEmpty() && recipe != null) {
            ItemStack output = recipe.getResultItem();
            if (output.isEmpty()) {
                return false;
            } else if (this.getMana(Source.INFERNAL) < (100 * recipe.getManaCosts().getAmount(Source.INFERNAL))) {
                return false;
            } else if (!recipe.getRequiredResearch().isKnownByStrict(this.getTileOwner())) {
                return false;
            } else {
                ItemStack currentOutput = this.items.get(OUTPUT_SLOT_INDEX);
                if (currentOutput.isEmpty()) {
                    return true;
                } else if (!currentOutput.sameItem(output)) {
                    return false;
                } else if (currentOutput.getCount() + output.getCount() <= this.getMaxStackSize() && currentOutput.getCount() + output.getCount() <= currentOutput.getMaxStackSize()) {
                    return true;
                } else {
                    return currentOutput.getCount() + output.getCount() <= output.getMaxStackSize();
                }
            }
        } else {
            return false;
        }
    }
    
    protected void doConcoction(Container inputInv, @Nullable IConcoctingRecipe recipe) {
        if (recipe != null && this.canConcoct(inputInv, recipe)) {
            ItemStack recipeOutput = recipe.assemble(inputInv);
            ItemStack currentOutput = this.items.get(OUTPUT_SLOT_INDEX);
            if (currentOutput.isEmpty()) {
                this.items.set(OUTPUT_SLOT_INDEX, recipeOutput);
            } else if (recipeOutput.getItem() == currentOutput.getItem() && ItemStack.tagMatches(recipeOutput, currentOutput)) {
                currentOutput.grow(recipeOutput.getCount());
            }
            
            for (int index = 0; index < inputInv.getContainerSize(); index++) {
                ItemStack stack = inputInv.getItem(index);
                if (!stack.isEmpty()) {
                    stack.shrink(1);
                }
            }
            this.setMana(Source.INFERNAL, this.getMana(Source.INFERNAL) - (100 * recipe.getManaCosts().getAmount(Source.INFERNAL)));
        }
    }
    
    protected boolean showBottle() {
        return this.cookTime > 0 || !this.items.get(OUTPUT_SLOT_INDEX).isEmpty();
    }
    
    protected int getCookTimeTotal() {
        return 200;
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        this.manaStorageOpt.invalidate();
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (!this.remove && cap == PrimalMagicCapabilities.MANA_STORAGE) {
            return this.manaStorageOpt.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public int getMana(Source source) {
        return this.manaStorage.getManaStored(source);
    }

    @Override
    public SourceList getAllMana() {
        SourceList mana = new SourceList();
        for (Source source : Source.SORTED_SOURCES) {
            int amount = this.manaStorage.getManaStored(source);
            if (amount > 0) {
                mana.add(source, amount);
            }
        }
        return mana;
    }

    @Override
    public int getMaxMana() {
        // TODO Fix up
        return this.manaStorage.getMaxManaStored(Source.INFERNAL);
    }

    @Override
    public void setMana(Source source, int amount) {
        this.manaStorage.setMana(source, amount);
        this.setChanged();
        this.syncTile(true);
    }

    @Override
    public void setMana(SourceList mana) {
        this.manaStorage.setMana(mana);
        this.setChanged();
        this.syncTile(true);
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        ItemStack slotStack = this.items.get(index);
        super.setItem(index, stack);
        boolean flag = !stack.isEmpty() && stack.sameItem(slotStack) && ItemStack.tagMatches(stack, slotStack);
        if (index >= 0 && index < MAX_INPUT_ITEMS && !flag) {
            this.cookTimeTotal = this.getCookTimeTotal();
            this.cookTime = 0;
            this.setChanged();
        }
    }

    @Override
    public boolean canPlaceItem(int slotIndex, ItemStack stack) {
        if (slotIndex == 10) {
            return false;
        } else if (slotIndex == 9) {
            return stack.getItem() instanceof IWand;
        } else {
            return true;
        }
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        if (side == Direction.UP) {
            return SLOTS_FOR_UP;
        } else if (side == Direction.DOWN) {
            return SLOTS_FOR_DOWN;
        } else {
            return SLOTS_FOR_SIDES;
        }
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack itemStackIn, Direction direction) {
        return this.canPlaceItem(index, itemStackIn);
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        return true;
    }
}
