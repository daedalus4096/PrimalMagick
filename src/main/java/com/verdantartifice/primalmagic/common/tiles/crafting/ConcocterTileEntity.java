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

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;

public class ConcocterTileEntity extends TileInventoryPM implements ITickableTileEntity, INamedContainerProvider, IOwnedTileEntity, IManaContainer {
    protected static final int MAX_INPUT_ITEMS = 9;
    protected static final int WAND_SLOT_INDEX = 9;
    protected static final int OUTPUT_SLOT_INDEX = 10;
    
    protected int cookTime;
    protected int cookTimeTotal;
    protected ManaStorage manaStorage;
    protected PlayerEntity owner;
    protected UUID ownerUUID;

    protected LazyOptional<IManaStorage> manaStorageOpt = LazyOptional.of(() -> this.manaStorage);
    
    // Define a container-trackable representation of this tile's relevant data
    protected final IIntArray concocterData = new IIntArray() {
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
        public int size() {
            return 4;
        }
    };
    
    public ConcocterTileEntity() {
        super(TileEntityTypesPM.CONCOCTER.get(), MAX_INPUT_ITEMS + 2);
        this.manaStorage = new ManaStorage(10000, 1000, 1000, Source.INFERNAL);
    }
    
    @Override
    public void read(BlockState state, CompoundNBT compound) {
        super.read(state, compound);
        
        this.cookTime = compound.getInt("CookTime");
        this.cookTimeTotal = compound.getInt("CookTimeTotal");
        this.manaStorage.deserializeNBT(compound.getCompound("ManaStorage"));
        
        this.owner = null;
        this.ownerUUID = null;
        if (compound.contains("OwnerUUID")) {
            this.ownerUUID = compound.getUniqueId("OwnerUUID");
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.putInt("CookTime", this.cookTime);
        compound.putInt("CookTimeTotal", this.cookTimeTotal);
        compound.put("ManaStorage", this.manaStorage.serializeNBT());
        if (this.ownerUUID != null) {
            compound.putUniqueId("OwnerUUID", this.ownerUUID);
        }
        return super.write(compound);
    }

    @Override
    public Container createMenu(int windowId, PlayerInventory playerInv, PlayerEntity player) {
        return new ConcocterContainer(windowId, playerInv, this, this.concocterData);
    }

    @Override
    public void setTileOwner(PlayerEntity owner) {
        this.owner = owner;
        this.ownerUUID = owner.getUniqueID();
    }

    @Override
    public PlayerEntity getTileOwner() {
        if (this.owner == null && this.ownerUUID != null && this.hasWorld() && this.world instanceof ServerWorld) {
            // If the owner cache is empty, find the entity matching the owner's unique ID
            ServerPlayerEntity player = ((ServerWorld)this.world).getServer().getPlayerList().getPlayerByUUID(this.ownerUUID);
            if (player != null) {
                this.owner = player;
            } else {
                this.ownerUUID = null;
            }
        }
        return this.owner;
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent(this.getBlockState().getBlock().getTranslationKey());
    }

    @Override
    public void tick() {
        boolean shouldMarkDirty = false;
        
        if (!this.world.isRemote) {
            // Fill up internal mana storage with that from any inserted wands
            ItemStack wandStack = this.items.get(WAND_SLOT_INDEX);
            if (!wandStack.isEmpty() && wandStack.getItem() instanceof IWand) {
                IWand wand = (IWand)wandStack.getItem();
                int centimanaMissing = this.manaStorage.getMaxManaStored(Source.INFERNAL) - this.manaStorage.getManaStored(Source.INFERNAL);
                int centimanaToTransfer = MathHelper.clamp(centimanaMissing, 0, 100);
                if (wand.consumeMana(wandStack, null, Source.INFERNAL, centimanaToTransfer)) {
                    this.manaStorage.receiveMana(Source.INFERNAL, centimanaToTransfer, false);
                    shouldMarkDirty = true;
                }
            }

            Inventory realInv = new Inventory(MAX_INPUT_ITEMS);
            Inventory testInv = new Inventory(MAX_INPUT_ITEMS);
            for (int index = 0; index < MAX_INPUT_ITEMS; index++) {
                ItemStack invStack = this.items.get(index);
                realInv.setInventorySlotContents(index, invStack);
                // Don't consider fuse length when testing item inputs for recipe determination
                testInv.setInventorySlotContents(index, ConcoctionUtils.isBomb(invStack) ? ConcoctionUtils.setFuseType(invStack.copy(), FuseType.MEDIUM) : invStack);
            }
            IConcoctingRecipe recipe = this.world.getServer().getRecipeManager().getRecipe(RecipeTypesPM.CONCOCTING, testInv, this.world).orElse(null);
            if (this.canConcoct(realInv, recipe)) {
                this.cookTime++;
                if (this.cookTime >= this.cookTimeTotal) {
                    this.cookTime = 0;
                    this.cookTimeTotal = this.getCookTimeTotal();
                    this.doConcoction(realInv, recipe);
                    shouldMarkDirty = true;
                }
            } else {
                this.cookTime = MathHelper.clamp(this.cookTime - 2, 0, this.cookTimeTotal);
            }
            
            this.world.setBlockState(this.getPos(), this.world.getBlockState(this.getPos()).with(ConcocterBlock.HAS_BOTTLE, this.showBottle()), Constants.BlockFlags.BLOCK_UPDATE);
        }

        if (shouldMarkDirty) {
            this.markDirty();
            this.syncTile(true);
        }
    }
    
    protected boolean canConcoct(IInventory inputInv, @Nullable IConcoctingRecipe recipe) {
        if (!inputInv.isEmpty() && recipe != null) {
            ItemStack output = recipe.getRecipeOutput();
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
                } else if (!currentOutput.isItemEqual(output)) {
                    return false;
                } else if (currentOutput.getCount() + output.getCount() <= this.getInventoryStackLimit() && currentOutput.getCount() + output.getCount() <= currentOutput.getMaxStackSize()) {
                    return true;
                } else {
                    return currentOutput.getCount() + output.getCount() <= output.getMaxStackSize();
                }
            }
        } else {
            return false;
        }
    }
    
    protected void doConcoction(IInventory inputInv, @Nullable IConcoctingRecipe recipe) {
        if (recipe != null && this.canConcoct(inputInv, recipe)) {
            ItemStack recipeOutput = recipe.getCraftingResult(inputInv);
            ItemStack currentOutput = this.items.get(OUTPUT_SLOT_INDEX);
            if (currentOutput.isEmpty()) {
                this.items.set(OUTPUT_SLOT_INDEX, recipeOutput);
            } else if (recipeOutput.getItem() == currentOutput.getItem() && ItemStack.areItemStackTagsEqual(recipeOutput, currentOutput)) {
                currentOutput.grow(recipeOutput.getCount());
            }
            
            for (int index = 0; index < inputInv.getSizeInventory(); index++) {
                ItemStack stack = inputInv.getStackInSlot(index);
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
    protected void invalidateCaps() {
        super.invalidateCaps();
        this.manaStorageOpt.invalidate();
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (!this.removed && cap == PrimalMagicCapabilities.MANA_STORAGE) {
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
        this.markDirty();
        this.syncTile(true);
    }

    @Override
    public void setMana(SourceList mana) {
        this.manaStorage.setMana(mana);
        this.markDirty();
        this.syncTile(true);
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        ItemStack slotStack = this.items.get(index);
        super.setInventorySlotContents(index, stack);
        boolean flag = !stack.isEmpty() && stack.isItemEqual(slotStack) && ItemStack.areItemStackTagsEqual(stack, slotStack);
        if (index >= 0 && index < MAX_INPUT_ITEMS && !flag) {
            this.cookTimeTotal = this.getCookTimeTotal();
            this.cookTime = 0;
            this.markDirty();
        }
    }
}
