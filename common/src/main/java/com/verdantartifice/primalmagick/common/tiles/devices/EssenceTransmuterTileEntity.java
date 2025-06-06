package com.verdantartifice.primalmagick.common.tiles.devices;

import com.verdantartifice.primalmagick.common.capabilities.IItemHandlerPM;
import com.verdantartifice.primalmagick.common.capabilities.IManaStorage;
import com.verdantartifice.primalmagick.common.capabilities.ITileResearchCache;
import com.verdantartifice.primalmagick.common.capabilities.ManaStorage;
import com.verdantartifice.primalmagick.common.capabilities.TileResearchCache;
import com.verdantartifice.primalmagick.common.components.DataComponentsPM;
import com.verdantartifice.primalmagick.common.items.essence.EssenceItem;
import com.verdantartifice.primalmagick.common.items.essence.EssenceType;
import com.verdantartifice.primalmagick.common.menus.EssenceTransmuterMenu;
import com.verdantartifice.primalmagick.common.research.keys.AbstractResearchKey;
import com.verdantartifice.primalmagick.common.tiles.base.IManaContainingBlockEntity;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.sources.SourceList;
import com.verdantartifice.primalmagick.common.sources.Sources;
import com.verdantartifice.primalmagick.common.tags.ItemTagsPM;
import com.verdantartifice.primalmagick.common.tiles.BlockEntityTypesPM;
import com.verdantartifice.primalmagick.common.tiles.base.AbstractTileSidedInventoryPM;
import com.verdantartifice.primalmagick.common.tiles.base.IOwnedTileEntity;
import com.verdantartifice.primalmagick.common.util.ItemUtils;
import com.verdantartifice.primalmagick.common.util.WeightedRandomBag;
import com.verdantartifice.primalmagick.common.wands.IWand;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponentMap.Builder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Definition of an essence transmuter tile entity.  Performs the processing for the corresponding block.
 * 
 * @author Daedalus4096
 * @see com.verdantartifice.primalmagick.common.blocks.devices.EssenceTransmuterBlock
 */
public abstract class EssenceTransmuterTileEntity extends AbstractTileSidedInventoryPM implements MenuProvider, IManaContainingBlockEntity, IOwnedTileEntity {
    private static final Logger LOGGER = LogManager.getLogger();

    public static final int INPUT_INV_INDEX = 0;
    public static final int OUTPUT_INV_INDEX = 1;
    public static final int WAND_INV_INDEX = 2;
    protected static final int ESSENCE_PER_TRANSMUTE = 8;
    protected static final int OUTPUT_CAPACITY = 9;
    
    protected int processTime;
    protected int processTimeTotal;
    protected UUID ownerUUID;
    protected ManaStorage manaStorage;
    protected ITileResearchCache researchCache;
    protected Source nextOutputSource;
    
    protected Set<AbstractResearchKey<?>> relevantResearch = Collections.emptySet();
    protected final Predicate<AbstractResearchKey<?>> relevantFilter = k -> this.getRelevantResearch().contains(k);
    
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
                return EssenceTransmuterTileEntity.this.manaStorage.getManaStored(Sources.MOON);
            case 3:
                return EssenceTransmuterTileEntity.this.manaStorage.getMaxManaStored(Sources.MOON);
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
        super(BlockEntityTypesPM.ESSENCE_TRANSMUTER.get(), pos, state);
        this.manaStorage = new ManaStorage(2000, 200, 200, Sources.MOON);
        this.researchCache = new TileResearchCache();
    }

    public ITileResearchCache getUncachedTileResearchCache() {
        return this.researchCache;
    }

    public IManaStorage<?> getUncachedManaStorage() {
        return this.manaStorage;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void loadAdditional(CompoundTag compound, HolderLookup.Provider registries) {
        super.loadAdditional(compound, registries);
        this.processTime = compound.getInt("ProcessTime");
        this.processTimeTotal = compound.getInt("ProcessTimeTotal");
        ManaStorage.CODEC.parse(registries.createSerializationContext(NbtOps.INSTANCE), compound.get("ManaStorage")).resultOrPartial(msg -> {
            LOGGER.error("Failed to decode mana storage: {}", msg);
        }).ifPresent(mana -> mana.copyManaInto(this.manaStorage));
        this.researchCache.deserializeNBT(registries, compound.getCompound("ResearchCache"));
        this.nextOutputSource = compound.contains("NextSource", Tag.TAG_STRING) ? Sources.get(ResourceLocation.parse(compound.getString("NextSource"))) : null;
        
        this.ownerUUID = null;
        if (compound.contains("OwnerUUID")) {
            String ownerUUIDStr = compound.getString("OwnerUUID");
            if (!ownerUUIDStr.isEmpty()) {
                this.ownerUUID = UUID.fromString(ownerUUIDStr);
            }
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void saveAdditional(CompoundTag compound, HolderLookup.Provider registries) {
        super.saveAdditional(compound, registries);
        compound.putInt("ProcessTime", this.processTime);
        compound.putInt("ProcessTimeTotal", this.processTimeTotal);
        ManaStorage.CODEC.encodeStart(registries.createSerializationContext(NbtOps.INSTANCE), this.manaStorage).resultOrPartial(msg -> {
            LOGGER.error("Failed to encode mana storage: {}", msg);
        }).ifPresent(encoded -> compound.put("ManaStorage", encoded));
        compound.put("ResearchCache", this.researchCache.serializeNBT(registries));
        if (this.nextOutputSource != null) {
            compound.putString("NextSource", this.nextOutputSource.getId().toString());
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
        return 200;
    }

    public static void tick(Level level, BlockPos pos, BlockState state, EssenceTransmuterTileEntity entity) {
        boolean shouldMarkDirty = false;

        if (!level.isClientSide) {
            // Fill up internal mana storage with that from any inserted wands
            ItemStack wandStack = entity.getItem(WAND_INV_INDEX, 0);
            if (!wandStack.isEmpty() && wandStack.getItem() instanceof IWand wand) {
                int centimanaMissing = entity.manaStorage.getMaxManaStored(Sources.MOON) - entity.manaStorage.getManaStored(Sources.MOON);
                int centimanaToTransfer = Mth.clamp(centimanaMissing, 0, 100);
                if (wand.consumeMana(wandStack, null, Sources.MOON, centimanaToTransfer, level.registryAccess())) {
                    entity.manaStorage.receiveMana(Sources.MOON, centimanaToTransfer, false);
                    shouldMarkDirty = true;
                }
            }
            
            // Process ingredients
            ItemStack essenceStack = entity.getItem(INPUT_INV_INDEX, 0);
            if (!essenceStack.isEmpty() && entity.manaStorage.getManaStored(Sources.MOON) >= entity.getManaCost()) {
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
            Sources.stream().filter(s -> !s.equals(inputSource) && this.isSourceKnown(s)).forEach(s -> bag.add(s, 1.0D));
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
            this.manaStorage.extractMana(Sources.MOON, this.getManaCost(), false);
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
        if (invIndex == INPUT_INV_INDEX && (stack.isEmpty() || !ItemStack.isSameItemSameComponents(stack, slotStack))) {
            this.processTimeTotal = this.getProcessTimeTotal();
            this.processTime = 0;
            this.nextOutputSource = null;
            this.setChanged();
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
        return this.manaStorage.getMaxManaStored(Sources.MOON);
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
        this.ownerUUID = owner == null ? null : owner.getUUID();
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
        if (source == null || source.getDiscoverKey().isEmpty()) {
            return true;
        } else {
            Player owner = this.getTileOwner();
            if (owner != null) {
                // Check the live research list if possible
                return source.isDiscovered(owner);
            } else {
                // Check the research cache if the owner is unavailable
                return this.researchCache.isResearchComplete(source.getDiscoverKey().get());
            }
        }
    }
    
    protected Set<AbstractResearchKey<?>> getRelevantResearch() {
        return this.relevantResearch;
    }
    
    protected static Set<AbstractResearchKey<?>> assembleRelevantResearch() {
        return Sources.streamSorted().map(Source::getDiscoverKey).filter(Optional::isPresent).map(Optional::get).collect(Collectors.toUnmodifiableSet());
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
    public Optional<Integer> getInventoryIndexForFace(@NotNull Direction face) {
        return switch (face) {
            case DOWN -> Optional.of(OUTPUT_INV_INDEX);
            default -> Optional.of(INPUT_INV_INDEX);
        };
    }

    @Override
    protected NonNullList<IItemHandlerPM> createItemHandlers() {
        NonNullList<IItemHandlerPM> retVal = NonNullList.withSize(this.getInventoryCount(), Services.ITEM_HANDLERS.create(this));
        
        // Create input handler
        retVal.set(INPUT_INV_INDEX, Services.ITEM_HANDLERS.builder(this.inventories.get(INPUT_INV_INDEX), this)
                .itemValidFunction((slot, stack) -> stack.is(ItemTagsPM.ESSENCES))
                .build());
        
        // Create fuel handler
        retVal.set(WAND_INV_INDEX, Services.ITEM_HANDLERS.builder(this.inventories.get(WAND_INV_INDEX), this)
                .itemValidFunction((slot, stack) -> stack.getItem() instanceof IWand)
                .build());

        // Create output handler
        retVal.set(OUTPUT_INV_INDEX, Services.ITEM_HANDLERS.builder(this.inventories.get(OUTPUT_INV_INDEX), this)
                .itemValidFunction((slot, stack) -> false)
                .build());
        
        return retVal;
    }

    @Override
    protected void applyImplicitComponents(DataComponentInput pComponentInput) {
        super.applyImplicitComponents(pComponentInput);
        pComponentInput.getOrDefault(DataComponentsPM.CAPABILITY_MANA_STORAGE.get(), ManaStorage.EMPTY).copyManaInto(this.manaStorage);
    }

    @Override
    protected void collectImplicitComponents(Builder pComponents) {
        super.collectImplicitComponents(pComponents);
        pComponents.set(DataComponentsPM.CAPABILITY_MANA_STORAGE.get(), this.manaStorage);
    }

    @Override
    public void removeComponentsFromTag(CompoundTag pTag) {
        pTag.remove("ManaStorage");
    }
}
