package com.verdantartifice.primalmagick.common.tiles.crafting;

import java.util.Collections;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Nullable;

import com.verdantartifice.primalmagick.common.blocks.crafting.ConcocterBlock;
import com.verdantartifice.primalmagick.common.capabilities.IManaStorage;
import com.verdantartifice.primalmagick.common.capabilities.ITileResearchCache;
import com.verdantartifice.primalmagick.common.capabilities.ItemStackHandlerPM;
import com.verdantartifice.primalmagick.common.capabilities.ManaStorage;
import com.verdantartifice.primalmagick.common.capabilities.PrimalMagickCapabilities;
import com.verdantartifice.primalmagick.common.capabilities.TileResearchCache;
import com.verdantartifice.primalmagick.common.concoctions.ConcoctionUtils;
import com.verdantartifice.primalmagick.common.concoctions.FuseType;
import com.verdantartifice.primalmagick.common.crafting.IConcoctingRecipe;
import com.verdantartifice.primalmagick.common.crafting.RecipeTypesPM;
import com.verdantartifice.primalmagick.common.menus.ConcocterMenu;
import com.verdantartifice.primalmagick.common.research.keys.AbstractResearchKey;
import com.verdantartifice.primalmagick.common.research.requirements.AbstractRequirement;
import com.verdantartifice.primalmagick.common.sources.IManaContainer;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.sources.SourceList;
import com.verdantartifice.primalmagick.common.sources.Sources;
import com.verdantartifice.primalmagick.common.tiles.TileEntityTypesPM;
import com.verdantartifice.primalmagick.common.tiles.base.AbstractTileSidedInventoryPM;
import com.verdantartifice.primalmagick.common.tiles.base.IOwnedTileEntity;
import com.verdantartifice.primalmagick.common.wands.IWand;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;

public class ConcocterTileEntity extends AbstractTileSidedInventoryPM implements  MenuProvider, IOwnedTileEntity, IManaContainer, StackedContentsCompatible {
    protected static final int INPUT_INV_INDEX = 0;
    protected static final int WAND_INV_INDEX = 1;
    protected static final int OUTPUT_INV_INDEX = 2;
    protected static final int MAX_INPUT_ITEMS = 9;
    
    protected int cookTime;
    protected int cookTimeTotal;
    protected ManaStorage manaStorage;
    protected ITileResearchCache researchCache;
    protected UUID ownerUUID;

    protected LazyOptional<IManaStorage> manaStorageOpt = LazyOptional.of(() -> this.manaStorage);
    protected LazyOptional<ITileResearchCache> researchCacheOpt = LazyOptional.of(() -> this.researchCache);
    
    protected Set<AbstractResearchKey<?>> relevantResearch = Collections.emptySet();
    protected final Predicate<AbstractResearchKey<?>> relevantFilter = k -> this.getRelevantResearch().contains(k);
    
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
                return ConcocterTileEntity.this.manaStorage.getManaStored(Sources.INFERNAL);
            case 3:
                return ConcocterTileEntity.this.manaStorage.getMaxManaStored(Sources.INFERNAL);
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
        super(TileEntityTypesPM.CONCOCTER.get(), pos, state);
        this.manaStorage = new ManaStorage(10000, 1000, 1000, Sources.INFERNAL);
        this.researchCache = new TileResearchCache();
    }
    
    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        
        this.cookTime = compound.getInt("CookTime");
        this.cookTimeTotal = compound.getInt("CookTimeTotal");
        this.manaStorage.deserializeNBT(compound.getCompound("ManaStorage"));
        this.researchCache.deserializeNBT(compound.getCompound("ResearchCache"));
        
        this.ownerUUID = null;
        if (compound.contains("OwnerUUID")) {
            this.ownerUUID = compound.getUUID("OwnerUUID");
        }
    }

    @Override
    protected void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        compound.putInt("CookTime", this.cookTime);
        compound.putInt("CookTimeTotal", this.cookTimeTotal);
        compound.put("ManaStorage", this.manaStorage.serializeNBT());
        compound.put("ResearchCache", this.researchCache.serializeNBT());
        if (this.ownerUUID != null) {
            compound.putUUID("OwnerUUID", this.ownerUUID);
        }
    }

    @Override
    public AbstractContainerMenu createMenu(int windowId, Inventory playerInv, Player player) {
        return new ConcocterMenu(windowId, playerInv, this.getBlockPos(), this, this.concocterData);
    }

    @Override
    public void setTileOwner(Player owner) {
        // Set the owner and update the local research cache with their relevant research
        this.ownerUUID = owner.getUUID();
        this.researchCache.update(owner, this.relevantFilter);
    }

    @Override
    public Player getTileOwner() {
        if (this.ownerUUID != null && this.hasLevel() && this.level instanceof ServerLevel serverLevel) {
            Player livePlayer = serverLevel.getServer().getPlayerList().getPlayer(this.ownerUUID);
            if (livePlayer != null && livePlayer.tickCount % 20 == 0) {
                // Update research cache with current player research
                this.researchCache.update(livePlayer, this.relevantFilter);
            }
            return livePlayer;
        }
        return null;
    }

    protected boolean isResearchKnown(Optional<AbstractRequirement<?>> requirementOpt) {
        if (requirementOpt.isEmpty()) {
            return true;
        } else {
            Player owner = this.getTileOwner();
            if (owner != null) {
                // Check the live data if possible
                return requirementOpt.get().isMetBy(owner);
            } else {
                // Check the research cache if the owner is unavailable
                return this.researchCache.isResearchComplete(requirementOpt.get().streamKeys().toList());
            }
        }
    }
    
    protected Set<AbstractResearchKey<?>> getRelevantResearch() {
        return this.relevantResearch;
    }
    
    protected static Set<AbstractResearchKey<?>> assembleRelevantResearch(RecipeManager recipeManager) {
        // Get a set of all the research keys used in any concocting recipe
        return recipeManager.getAllRecipesFor(RecipeTypesPM.CONCOCTING.get()).stream().flatMap(holder -> {
            return holder.value().getRequirement().map(req -> {
                return req.streamKeys();
            }).orElse(Stream.empty());
        }).distinct().collect(Collectors.toUnmodifiableSet());
    }
    
    @Override
    public Component getDisplayName() {
        return Component.translatable(this.getBlockState().getBlock().getDescriptionId());
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (!this.level.isClientSide) {
            this.relevantResearch = assembleRelevantResearch(this.level.getRecipeManager());
        }
        this.cookTimeTotal = this.getCookTimeTotal();
    }

    public static void tick(Level level, BlockPos pos, BlockState state, ConcocterTileEntity entity) {
        boolean shouldMarkDirty = false;
        
        if (!level.isClientSide) {
            // Fill up internal mana storage with that from any inserted wands
            ItemStack wandStack = entity.getItem(WAND_INV_INDEX, 0);
            if (!wandStack.isEmpty() && wandStack.getItem() instanceof IWand) {
                IWand wand = (IWand)wandStack.getItem();
                int centimanaMissing = entity.manaStorage.getMaxManaStored(Sources.INFERNAL) - entity.manaStorage.getManaStored(Sources.INFERNAL);
                int centimanaToTransfer = Mth.clamp(centimanaMissing, 0, 100);
                if (wand.consumeMana(wandStack, null, Sources.INFERNAL, centimanaToTransfer, level.registryAccess())) {
                    entity.manaStorage.receiveMana(Sources.INFERNAL, centimanaToTransfer, false);
                    shouldMarkDirty = true;
                }
            }

            SimpleContainer realInv = new SimpleContainer(MAX_INPUT_ITEMS);
            SimpleContainer testInv = new SimpleContainer(MAX_INPUT_ITEMS);
            for (int index = 0; index < MAX_INPUT_ITEMS; index++) {
                ItemStack invStack = entity.getItem(INPUT_INV_INDEX, index);
                realInv.setItem(index, invStack);
                // Don't consider fuse length when testing item inputs for recipe determination
                testInv.setItem(index, ConcoctionUtils.isBomb(invStack) ? ConcoctionUtils.setFuseType(invStack.copy(), FuseType.MEDIUM) : invStack);
            }
            IConcoctingRecipe recipe = level.getServer().getRecipeManager().getRecipeFor(RecipeTypesPM.CONCOCTING.get(), testInv, level).map(RecipeHolder::value).orElse(null);
            if (entity.canConcoct(realInv, level.registryAccess(), recipe)) {
                entity.cookTime++;
                if (entity.cookTime >= entity.cookTimeTotal) {
                    entity.cookTime = 0;
                    entity.cookTimeTotal = entity.getCookTimeTotal();
                    entity.doConcoction(realInv, level.registryAccess(), recipe);
                    shouldMarkDirty = true;
                }
            } else {
                entity.cookTime = Mth.clamp(entity.cookTime - 2, 0, entity.cookTimeTotal);
            }
            
            level.setBlock(pos, state.setValue(ConcocterBlock.HAS_BOTTLE, entity.showBottle()), Block.UPDATE_CLIENTS);
        }

        if (shouldMarkDirty) {
            entity.setChanged();
            entity.syncTile(true);
        }
    }
    
    protected boolean canConcoct(Container inputInv, RegistryAccess registryAccess, @Nullable IConcoctingRecipe recipe) {
        if (!inputInv.isEmpty() && recipe != null) {
            ItemStack output = recipe.getResultItem(registryAccess);
            if (output.isEmpty()) {
                return false;
            } else if (this.getMana(Sources.INFERNAL) < (100 * recipe.getManaCosts().getAmount(Sources.INFERNAL))) {
                return false;
            } else if (!this.isResearchKnown(recipe.getRequirement())) {
                return false;
            } else {
                ItemStack currentOutput = this.getItem(OUTPUT_INV_INDEX, 0);
                if (currentOutput.isEmpty()) {
                    return true;
                } else if (!ItemStack.isSameItem(currentOutput, output)) {
                    return false;
                } else if (currentOutput.getCount() + output.getCount() <= this.itemHandlers.get(OUTPUT_INV_INDEX).getSlotLimit(0) && 
                        currentOutput.getCount() + output.getCount() <= currentOutput.getMaxStackSize()) {
                    return true;
                } else {
                    return currentOutput.getCount() + output.getCount() <= output.getMaxStackSize();
                }
            }
        } else {
            return false;
        }
    }
    
    protected void doConcoction(Container inputInv, RegistryAccess registryAccess, @Nullable IConcoctingRecipe recipe) {
        if (recipe != null && this.canConcoct(inputInv, registryAccess, recipe)) {
            ItemStack recipeOutput = recipe.assemble(inputInv, registryAccess);
            ItemStack currentOutput = this.getItem(OUTPUT_INV_INDEX, 0);
            if (currentOutput.isEmpty()) {
                this.setItem(OUTPUT_INV_INDEX, 0, recipeOutput);
            } else if (ItemStack.isSameItemSameComponents(recipeOutput, currentOutput)) {
                currentOutput.grow(recipeOutput.getCount());
            }
            
            for (int index = 0; index < inputInv.getContainerSize(); index++) {
                ItemStack stack = inputInv.getItem(index);
                if (!stack.isEmpty()) {
                    stack.shrink(1);
                }
            }
            this.setMana(Sources.INFERNAL, this.getMana(Sources.INFERNAL) - (100 * recipe.getManaCosts().getAmount(Sources.INFERNAL)));
        }
    }
    
    protected boolean showBottle() {
        return this.cookTime > 0 || !this.getItem(OUTPUT_INV_INDEX, 0).isEmpty();
    }
    
    protected int getCookTimeTotal() {
        return 100;
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        this.manaStorageOpt.invalidate();
        this.researchCacheOpt.invalidate();
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (!this.remove && cap == PrimalMagickCapabilities.MANA_STORAGE) {
            return this.manaStorageOpt.cast();
        } else if (!this.remove && cap == PrimalMagickCapabilities.RESEARCH_CACHE) {
            return this.researchCacheOpt.cast();
        } else {
            return super.getCapability(cap, side);
        }
    }

    @Override
    public int getMana(Source source) {
        return this.manaStorage.getManaStored(source);
    }

    @Override
    public SourceList getAllMana() {
        SourceList.Builder mana = SourceList.builder();
        for (Source source : Sources.getAllSorted()) {
            int amount = this.manaStorage.getManaStored(source);
            if (amount > 0) {
                mana.with(source, amount);
            }
        }
        return mana.build();
    }

    @Override
    public int getMaxMana() {
        // TODO Fix up
        return this.manaStorage.getMaxManaStored(Sources.INFERNAL);
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
    public void setItem(int invIndex, int slotIndex, ItemStack stack) {
        ItemStack slotStack = this.getItem(invIndex, slotIndex);
        super.setItem(invIndex, slotIndex, stack);
        boolean flag = !stack.isEmpty() && ItemStack.isSameItemSameComponents(stack, slotStack);
        if (invIndex == INPUT_INV_INDEX && !flag) {
            this.cookTimeTotal = this.getCookTimeTotal();
            this.cookTime = 0;
            this.setChanged();
        }
    }

    @Override
    public void fillStackedContents(StackedContents stackedContents) {
        for (int invIndex = 0; invIndex < this.getInventoryCount(); invIndex++) {
            for (int slotIndex = 0; slotIndex < this.getInventorySize(invIndex); slotIndex++) {
                stackedContents.accountStack(this.getItem(invIndex, slotIndex));
            }
        }
    }

    @Override
    protected int getInventoryCount() {
        return 3;
    }

    @Override
    protected int getInventorySize(int inventoryIndex) {
        return switch (inventoryIndex) {
            case INPUT_INV_INDEX -> MAX_INPUT_ITEMS;
            case WAND_INV_INDEX -> 1;
            case OUTPUT_INV_INDEX -> 1;
            default -> 0;
        };
    }

    @Override
    protected OptionalInt getInventoryIndexForFace(Direction face) {
        return switch (face) {
            case UP -> OptionalInt.of(INPUT_INV_INDEX);
            case DOWN -> OptionalInt.of(OUTPUT_INV_INDEX);
            default -> OptionalInt.of(WAND_INV_INDEX);
        };
    }

    @Override
    protected NonNullList<ItemStackHandler> createHandlers() {
        NonNullList<ItemStackHandler> retVal = NonNullList.withSize(this.getInventoryCount(), new ItemStackHandlerPM(this));
        
        // Create input handler
        retVal.set(INPUT_INV_INDEX, new ItemStackHandlerPM(this.inventories.get(INPUT_INV_INDEX), this));
        
        // Create fuel handler
        retVal.set(WAND_INV_INDEX, new ItemStackHandlerPM(this.inventories.get(WAND_INV_INDEX), this) {
            @Override
            public boolean isItemValid(int slot, ItemStack stack) {
                return stack.getItem() instanceof IWand;
            }
        });

        // Create output handler
        retVal.set(OUTPUT_INV_INDEX, new ItemStackHandlerPM(this.inventories.get(OUTPUT_INV_INDEX), this) {
            @Override
            public boolean isItemValid(int slot, ItemStack stack) {
                return false;
            }
        });
        
        return retVal;
    }

    @Override
    protected void loadLegacyItems(NonNullList<ItemStack> legacyItems) {
        // Slots 0-8 were the input item stacks
        for (int inputIndex = 0; inputIndex < MAX_INPUT_ITEMS; inputIndex++) {
            this.setItem(INPUT_INV_INDEX, inputIndex, legacyItems.get(inputIndex));
        }
        
        // Slot 9 was the wand item stack
        this.setItem(WAND_INV_INDEX, 0, legacyItems.get(MAX_INPUT_ITEMS));
        
        // Slot 10 was the output item stack
        this.setItem(OUTPUT_INV_INDEX, 0, legacyItems.get(MAX_INPUT_ITEMS + 1));
    }
}
