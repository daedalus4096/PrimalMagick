package com.verdantartifice.primalmagick.common.tiles.devices;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.OptionalInt;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.verdantartifice.primalmagick.common.capabilities.IManaStorage;
import com.verdantartifice.primalmagick.common.capabilities.ITileResearchCache;
import com.verdantartifice.primalmagick.common.capabilities.ItemStackHandlerPM;
import com.verdantartifice.primalmagick.common.capabilities.ManaStorage;
import com.verdantartifice.primalmagick.common.capabilities.PrimalMagickCapabilities;
import com.verdantartifice.primalmagick.common.capabilities.TileResearchCache;
import com.verdantartifice.primalmagick.common.items.essence.EssenceItem;
import com.verdantartifice.primalmagick.common.items.essence.EssenceType;
import com.verdantartifice.primalmagick.common.menus.EssenceTransmuterMenu;
import com.verdantartifice.primalmagick.common.research.SimpleResearchKey;
import com.verdantartifice.primalmagick.common.sources.IManaContainer;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.sources.SourceList;
import com.verdantartifice.primalmagick.common.tags.ItemTagsPM;
import com.verdantartifice.primalmagick.common.tiles.TileEntityTypesPM;
import com.verdantartifice.primalmagick.common.tiles.base.AbstractTileSidedInventoryPM;
import com.verdantartifice.primalmagick.common.tiles.base.IOwnedTileEntity;
import com.verdantartifice.primalmagick.common.util.ItemUtils;
import com.verdantartifice.primalmagick.common.util.WeightedRandomBag;
import com.verdantartifice.primalmagick.common.wands.IWand;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;

/**
 * Definition of an essence transmuter tile entity.  Performs the processing for the corresponding block.
 * 
 * @author Daedalus4096
 * @see {@link com.verdantartifice.primalmagick.common.blocks.devices.EssenceTransmuterBlock}
 */
public class EssenceTransmuterTileEntity extends AbstractTileSidedInventoryPM implements MenuProvider, IManaContainer, IOwnedTileEntity {
    protected static final int INPUT_INV_INDEX = 0;
    protected static final int OUTPUT_INV_INDEX = 1;
    protected static final int WAND_INV_INDEX = 2;
    protected static final int ESSENCE_PER_TRANSMUTE = 8;
    protected static final int OUTPUT_CAPACITY = 9;
    
    protected int processTime;
    protected int processTimeTotal;
    protected UUID ownerUUID;
    protected ManaStorage manaStorage;
    protected ITileResearchCache researchCache;
    protected Source nextOutputSource;
    
    protected LazyOptional<IManaStorage> manaStorageOpt = LazyOptional.of(() -> this.manaStorage);
    protected LazyOptional<ITileResearchCache> researchCacheOpt = LazyOptional.of(() -> this.researchCache);
    
    protected Set<SimpleResearchKey> relevantResearch = Collections.emptySet();
    protected final Predicate<SimpleResearchKey> relevantFilter = k -> this.getRelevantResearch().contains(k);
    
    // Define a container-trackable representation of this tile's relevant data
    protected final ContainerData transmuterData = new ContainerData() {
        @Override
        public int get(int index) {
            switch (index) {
            case 0:
                return EssenceTransmuterTileEntity.this.processTime;
            case 1:
                return EssenceTransmuterTileEntity.this.processTimeTotal;
            case 2:
                return EssenceTransmuterTileEntity.this.manaStorage.getManaStored(Source.MOON);
            case 3:
                return EssenceTransmuterTileEntity.this.manaStorage.getMaxManaStored(Source.MOON);
            default:
                return 0;
            }
        }

        @Override
        public void set(int index, int value) {
            // Don't set mana storage values
            switch (index) {
            case 0:
                EssenceTransmuterTileEntity.this.processTime = value;
                break;
            case 1:
                EssenceTransmuterTileEntity.this.processTimeTotal = value;
                break;
            }
        }

        @Override
        public int getCount() {
            return 4;
        }
    };
    
    public EssenceTransmuterTileEntity(BlockPos pos, BlockState state) {
        super(TileEntityTypesPM.ESSENCE_TRANSMUTER.get(), pos, state);
        this.manaStorage = new ManaStorage(10000, 100, 100, Source.MOON);
        this.researchCache = new TileResearchCache();
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        this.processTime = compound.getInt("ProcessTime");
        this.processTimeTotal = compound.getInt("ProcessTimeTotal");
        this.manaStorage.deserializeNBT(compound.getCompound("ManaStorage"));
        this.researchCache.deserializeNBT(compound.getCompound("ResearchCache"));
        this.nextOutputSource = compound.contains("NextSource", Tag.TAG_STRING) ? Source.getSource(compound.getString("NextSource")) : null;
        
        this.ownerUUID = null;
        if (compound.contains("OwnerUUID")) {
            String ownerUUIDStr = compound.getString("OwnerUUID");
            if (!ownerUUIDStr.isEmpty()) {
                this.ownerUUID = UUID.fromString(ownerUUIDStr);
            }
        }
    }

    @Override
    protected void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        compound.putInt("ProcessTime", this.processTime);
        compound.putInt("ProcessTimeTotal", this.processTimeTotal);
        compound.put("ManaStorage", this.manaStorage.serializeNBT());
        compound.put("ResearchCache", this.researchCache.serializeNBT());
        if (this.nextOutputSource != null) {
            compound.putString("NextSource", this.nextOutputSource.getTag());
        }
        if (this.ownerUUID != null) {
            compound.putString("OwnerUUID", this.ownerUUID.toString());
        }
    }

    @Override
    public AbstractContainerMenu createMenu(int windowId, Inventory playerInv, Player player) {
        return new EssenceTransmuterMenu(windowId, playerInv, this.getBlockPos(), this, this.transmuterData);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable(this.getBlockState().getBlock().getDescriptionId());
    }
    
    protected int getProcessTimeTotal() {
        return 100;
    }
    
    protected int getManaCost() {
        return 10;
    }
    
    @Override
    public void onLoad() {
        super.onLoad();
        if (!this.level.isClientSide) {
            this.relevantResearch = assembleRelevantResearch();
        }
        this.processTimeTotal = this.getProcessTimeTotal();
    }

    public static void tick(Level level, BlockPos pos, BlockState state, EssenceTransmuterTileEntity entity) {
        boolean shouldMarkDirty = false;

        if (!level.isClientSide) {
            // Fill up internal mana storage with that from any inserted wands
            ItemStack wandStack = entity.getItem(WAND_INV_INDEX, 0);
            if (!wandStack.isEmpty() && wandStack.getItem() instanceof IWand wand) {
                int centimanaMissing = entity.manaStorage.getMaxManaStored(Source.MOON) - entity.manaStorage.getManaStored(Source.MOON);
                int centimanaToTransfer = Mth.clamp(centimanaMissing, 0, 100);
                if (wand.consumeMana(wandStack, null, Source.MOON, centimanaToTransfer)) {
                    entity.manaStorage.receiveMana(Source.MOON, centimanaToTransfer, false);
                    shouldMarkDirty = true;
                }
            }
            
            // Process ingredients
            ItemStack essenceStack = entity.getItem(INPUT_INV_INDEX, 0);
            if (!essenceStack.isEmpty() && entity.manaStorage.getManaStored(Source.MOON) >= entity.getManaCost()) {
                // If transmutable input is in place, process it
                if (entity.canTransmute(essenceStack)) {
                    entity.processTime++;
                    if (entity.processTime == entity.processTimeTotal) {
                        entity.processTime = 0;
                        entity.processTimeTotal = entity.getProcessTimeTotal();
                        entity.doTransmute(essenceStack);
                        entity.nextOutputSource = null;
                        shouldMarkDirty = true;
                    }
                } else {
                    entity.processTime = 0;
                }
            } else if (entity.processTime > 0) {
                // Decay any transmute progress
                entity.processTime = Mth.clamp(entity.processTime - 2, 0, entity.processTimeTotal);
            }
        }
        if (shouldMarkDirty) {
            entity.setChanged();
            entity.syncTile(true);
        }
    }
    
    @Nonnull
    protected Source getNextSource(Source inputSource, RandomSource rng) {
        if (this.nextOutputSource == null || this.nextOutputSource.equals(inputSource)) {
            // Generate a new random, known source different from the input
            WeightedRandomBag<Source> bag = new WeightedRandomBag<>();
            for (Source source : Source.SOURCES.values()) {
                if (!source.equals(inputSource) && this.isSourceKnown(source)) {
                    bag.add(source, 1.0D);
                }
            }
            this.nextOutputSource = bag.getRandom(rng);
        }
        return this.nextOutputSource;
    }

    protected boolean canTransmute(ItemStack inputStack) {
        List<ItemStack> newOutputs = this.getNewOutputs(inputStack);
        return newOutputs != null && newOutputs.size() <= OUTPUT_CAPACITY;
    }

    protected void doTransmute(ItemStack inputStack) {
        List<ItemStack> newOutputs = this.getNewOutputs(inputStack);
        if (newOutputs != null) {
            // Merge the items already in the output inventory with the new output items from the transmutation
            for (int index = 0; index < Math.min(newOutputs.size(), OUTPUT_CAPACITY); index++) {
                ItemStack out = newOutputs.get(index);
                this.setItem(OUTPUT_INV_INDEX, index, (out == null ? ItemStack.EMPTY : out));
            }
            
            // Deduct the inputs
            inputStack.shrink(ESSENCE_PER_TRANSMUTE);
            this.manaStorage.extractMana(Source.MOON, this.getManaCost(), false);
        }
    }
    
    @Nullable
    protected List<ItemStack> getNewOutputs(ItemStack inputStack) {
        if (inputStack != null && !inputStack.isEmpty() && inputStack.getCount() >= ESSENCE_PER_TRANSMUTE && inputStack.getItem() instanceof EssenceItem essence) {
            EssenceType inputType = essence.getEssenceType();
            Source inputSource = essence.getSource();
            Source outputSource = this.getNextSource(inputSource, this.level.random);
            ItemStack outputItem = EssenceItem.getEssence(inputType, outputSource, 1);
            List<ItemStack> currentOutputs = this.inventories.get(OUTPUT_INV_INDEX);
            List<ItemStack> mergedOutputs = ItemUtils.mergeItemStackIntoList(currentOutputs, outputItem);
            return mergedOutputs;
        } else {
            return null;
        }
    }

    @Override
    public void setItem(int invIndex, int slotIndex, ItemStack stack) {
        ItemStack slotStack = this.getItem(invIndex, slotIndex);
        super.setItem(invIndex, slotIndex, stack);
        if (invIndex == INPUT_INV_INDEX && (stack.isEmpty() || !ItemStack.isSameItemSameTags(stack, slotStack))) {
            this.processTimeTotal = this.getProcessTimeTotal();
            this.processTime = 0;
            this.nextOutputSource = null;
            this.setChanged();
        }
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
    public void invalidateCaps() {
        super.invalidateCaps();
        this.manaStorageOpt.invalidate();
        this.researchCacheOpt.invalidate();
    }

    @Override
    public int getMana(Source source) {
        return this.manaStorage.getManaStored(source);
    }

    @Override
    public SourceList getAllMana() {
        SourceList.Builder mana = SourceList.builder();
        for (Source source : Source.SORTED_SOURCES) {
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
        return this.manaStorage.getMaxManaStored(Source.MOON);
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
    public void setTileOwner(Player owner) {
        this.ownerUUID = owner.getUUID();
        this.updateResearchCache(owner);
    }

    @Override
    public Player getTileOwner() {
        if (this.ownerUUID != null && this.hasLevel() && this.level instanceof ServerLevel serverLevel) {
            Player livePlayer = serverLevel.getServer().getPlayerList().getPlayer(this.ownerUUID);
            if (livePlayer != null && livePlayer.tickCount % 20 == 0) {
                // Update research cache with current player research
                this.updateResearchCache(livePlayer);
            }
            return livePlayer;
        }
        return null;
    }
    
    protected void updateResearchCache(Player player) {
        this.researchCache.update(player, this.relevantFilter);
        this.nextOutputSource = null;
    }
    
    protected boolean isSourceKnown(@Nullable Source source) {
        if (source == null || source.getDiscoverKey() == null) {
            return true;
        } else {
            Player owner = this.getTileOwner();
            if (owner != null) {
                // Check the live research list if possible
                return source.isDiscovered(owner);
            } else {
                // Check the research cache if the owner is unavailable
                return this.researchCache.isResearchComplete(source.getDiscoverKey());
            }
        }
    }
    
    protected Set<SimpleResearchKey> getRelevantResearch() {
        return this.relevantResearch;
    }
    
    protected static Set<SimpleResearchKey> assembleRelevantResearch() {
        return Source.SORTED_SOURCES.stream().map(s -> s.getDiscoverKey()).filter(Objects::nonNull).collect(Collectors.toUnmodifiableSet());
    }

    @Override
    protected int getInventoryCount() {
        return 3;
    }

    @Override
    protected int getInventorySize(int inventoryIndex) {
        return switch (inventoryIndex) {
            case INPUT_INV_INDEX, WAND_INV_INDEX -> 1;
            case OUTPUT_INV_INDEX -> OUTPUT_CAPACITY;
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
        retVal.set(INPUT_INV_INDEX, new ItemStackHandlerPM(this.inventories.get(INPUT_INV_INDEX), this) {
            @Override
            public boolean isItemValid(int slot, ItemStack stack) {
                return stack.is(ItemTagsPM.ESSENCES);
            }
        });
        
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
        // Slot 0 was the input item stack
        this.setItem(INPUT_INV_INDEX, 0, legacyItems.get(0));
        
        // Slots 1-9 were the output item stacks
        for (int inputIndex = 0; inputIndex < OUTPUT_CAPACITY; inputIndex++) {
            this.setItem(OUTPUT_INV_INDEX, inputIndex, legacyItems.get(inputIndex + 1));
        }
        
        // Slot 10 was the wand item stack
        this.setItem(WAND_INV_INDEX, 0, legacyItems.get(OUTPUT_CAPACITY + 1));
    }
}
