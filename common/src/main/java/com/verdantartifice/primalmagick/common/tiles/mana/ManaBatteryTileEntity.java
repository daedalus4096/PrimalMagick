package com.verdantartifice.primalmagick.common.tiles.mana;

import com.verdantartifice.primalmagick.common.blocks.mana.AbstractManaFontBlock;
import com.verdantartifice.primalmagick.common.blocks.mana.ManaBatteryBlock;
import com.verdantartifice.primalmagick.common.capabilities.IItemHandlerPM;
import com.verdantartifice.primalmagick.common.capabilities.IManaStorage;
import com.verdantartifice.primalmagick.common.capabilities.ManaStorage;
import com.verdantartifice.primalmagick.common.components.DataComponentsPM;
import com.verdantartifice.primalmagick.common.items.essence.EssenceItem;
import com.verdantartifice.primalmagick.common.menus.ManaBatteryMenu;
import com.verdantartifice.primalmagick.common.sources.IManaContainer;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.sources.SourceList;
import com.verdantartifice.primalmagick.common.sources.Sources;
import com.verdantartifice.primalmagick.common.tiles.BlockEntityTypesPM;
import com.verdantartifice.primalmagick.common.tiles.base.AbstractTileSidedInventoryPM;
import com.verdantartifice.primalmagick.common.wands.IWand;
import com.verdantartifice.primalmagick.common.wands.WandCap;
import com.verdantartifice.primalmagick.common.wands.WandGem;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponentMap.Builder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.VisibleForTesting;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Definition of a mana battery tile entity.  Holds the charge for the corresponding block.
 * 
 * @see com.verdantartifice.primalmagick.common.blocks.mana.ManaBatteryBlock
 * @author Daedalus4096
 */
public abstract class ManaBatteryTileEntity extends AbstractTileSidedInventoryPM implements MenuProvider, IManaContainer {
    private static final Logger LOGGER = LogManager.getLogger();

    protected static final int FONT_RANGE = 5;
    protected static final int INPUT_INV_INDEX = 0;
    protected static final int CHARGE_INV_INDEX = 1;
    
    protected int chargeTime;
    protected int chargeTimeTotal;
    protected int fontSiphonTime;
    protected ManaStorage manaStorage;

    protected final Set<BlockPos> fontLocations = new HashSet<>();

    // Define a container-trackable representation of this tile's relevant data
    protected final ContainerData chargerData = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> ManaBatteryTileEntity.this.chargeTime;
                case 1 -> ManaBatteryTileEntity.this.chargeTimeTotal;
                case 2 -> ManaBatteryTileEntity.this.manaStorage.getManaStored(Sources.EARTH);
                case 3 -> ManaBatteryTileEntity.this.manaStorage.getMaxManaStored(Sources.EARTH);
                case 4 -> ManaBatteryTileEntity.this.manaStorage.getManaStored(Sources.SEA);
                case 5 -> ManaBatteryTileEntity.this.manaStorage.getMaxManaStored(Sources.SEA);
                case 6 -> ManaBatteryTileEntity.this.manaStorage.getManaStored(Sources.SKY);
                case 7 -> ManaBatteryTileEntity.this.manaStorage.getMaxManaStored(Sources.SKY);
                case 8 -> ManaBatteryTileEntity.this.manaStorage.getManaStored(Sources.SUN);
                case 9 -> ManaBatteryTileEntity.this.manaStorage.getMaxManaStored(Sources.SUN);
                case 10 -> ManaBatteryTileEntity.this.manaStorage.getManaStored(Sources.MOON);
                case 11 -> ManaBatteryTileEntity.this.manaStorage.getMaxManaStored(Sources.MOON);
                case 12 -> ManaBatteryTileEntity.this.manaStorage.getManaStored(Sources.BLOOD);
                case 13 -> ManaBatteryTileEntity.this.manaStorage.getMaxManaStored(Sources.BLOOD);
                case 14 -> ManaBatteryTileEntity.this.manaStorage.getManaStored(Sources.INFERNAL);
                case 15 -> ManaBatteryTileEntity.this.manaStorage.getMaxManaStored(Sources.INFERNAL);
                case 16 -> ManaBatteryTileEntity.this.manaStorage.getManaStored(Sources.VOID);
                case 17 -> ManaBatteryTileEntity.this.manaStorage.getMaxManaStored(Sources.VOID);
                case 18 -> ManaBatteryTileEntity.this.manaStorage.getManaStored(Sources.HALLOWED);
                case 19 -> ManaBatteryTileEntity.this.manaStorage.getMaxManaStored(Sources.HALLOWED);
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            // Don't set mana storage values
            switch (index) {
            case 0:
                ManaBatteryTileEntity.this.chargeTime = value;
                break;
            case 1:
                ManaBatteryTileEntity.this.chargeTimeTotal = value;
                break;
            }
        }

        @Override
        public int getCount() {
            return 20;
        }
    };
    
    public ManaBatteryTileEntity(BlockPos pos, BlockState state) {
        super(BlockEntityTypesPM.MANA_BATTERY.get(), pos, state);
        this.manaStorage = new ManaStorage(this.getBatteryCapacity(), this.getBatteryTransferCap(), Sources.getAllSorted().toArray(new Source[0]));
    }

    public IManaStorage<?> getUncachedManaStorage() {
        return this.manaStorage;
    }

    protected int getBatteryCapacity() {
        // Return the capacity of the battery in centimana
        if (this.getBlockState().getBlock() instanceof ManaBatteryBlock batteryBlock) {
            return switch (batteryBlock.getDeviceTier()) {
                case FORBIDDEN -> 4 * WandGem.WIZARD.getCapacity();
                case HEAVENLY -> 4 * WandGem.ARCHMAGE.getCapacity();
                case CREATIVE -> -1;
                default -> 0;
            };
        } else {
            return 0;
        }
    }

    @VisibleForTesting
    public int getBatteryTransferCap() {
        // Return the max amount of centimana that can be transfered by the battery per tick
        if (this.getBlockState().getBlock() instanceof ManaBatteryBlock batteryBlock) {
            return switch (batteryBlock.getDeviceTier()) {
                case FORBIDDEN -> WandCap.HEXIUM.getSiphonAmount();
                case HEAVENLY, CREATIVE -> WandCap.HALLOWSTEEL.getSiphonAmount();
                default -> 0;
            };
        } else {
            return 0;
        }
    }
    
    public static void tick(Level level, BlockPos pos, BlockState state, ManaBatteryTileEntity entity) {
        boolean shouldMarkDirty = false;
        
        if (!level.isClientSide) {
            ItemStack inputStack = entity.getItem(INPUT_INV_INDEX, 0);
            ItemStack chargeStack = entity.getItem(CHARGE_INV_INDEX, 0);
            
            // Scan surroundings for mana fonts once a second
            if (entity.fontSiphonTime % 20 == 0) {
                entity.scanSurroundings();
            }

            // Siphon from nearby fonts
            Vec3 chargerCenter = Vec3.atCenterOf(pos);
            for (BlockPos fontPos : entity.fontLocations) {
                if (entity.fontSiphonTime % 5 == 0 && 
                        level.getBlockEntity(fontPos) instanceof AbstractManaFontTileEntity fontEntity && 
                        level.getBlockState(fontPos).getBlock() instanceof AbstractManaFontBlock) {
                    fontEntity.doSiphon(entity.manaStorage, level, null, chargerCenter, entity.getBatteryTransferCap());
                }
            }
            entity.fontSiphonTime++;

            if (!inputStack.isEmpty()) {
                // Break down input if it's essence
                if (entity.canBreakDownEssence(inputStack)) {
                    entity.chargeTime++;
                    if (entity.chargeTime >= entity.chargeTimeTotal) {
                        entity.chargeTime = 0;
                        entity.chargeTimeTotal = entity.getChargeTimeTotal();
                        entity.breakDownEssence(inputStack);
                        shouldMarkDirty = true;
                    }
                } else {
                    entity.chargeTime = 0;
                }
                
                // Siphon from input if it's a wand
                for (Source source : Sources.getAllSorted()) {
                    if (entity.canSiphonWand(inputStack, source) && entity.doWandSiphon(inputStack, source)) {
                        shouldMarkDirty = true;
                    }
                }
            }
            
            if (!chargeStack.isEmpty()) {
                // Output mana to wand
                for (Source source : Sources.getAllSorted()) {
                    if (entity.canOutputToWand(chargeStack, source)) {
                        entity.doOutput(chargeStack, source);
                        shouldMarkDirty = true;
                    }
                }
            }
        }
        
        if (shouldMarkDirty) {
            entity.setChanged();
            entity.syncTile(true);
        }
    }
    
    protected int getChargeTimeTotal() {
        return 100;
    }
    
    protected boolean canBreakDownEssence(ItemStack inputStack) {
        if (!inputStack.isEmpty() && inputStack.getItem() instanceof EssenceItem essenceItem) {
            return this.manaStorage.getManaStored(essenceItem.getSource()) < this.manaStorage.getMaxManaStored(essenceItem.getSource());
        } else {
            return false;
        }
    }
    
    protected void breakDownEssence(ItemStack inputStack) {
        if (this.canBreakDownEssence(inputStack) && inputStack.getItem() instanceof EssenceItem essenceItem) {
            this.manaStorage.setMana(essenceItem.getSource(), this.manaStorage.getManaStored(essenceItem.getSource()) + essenceItem.getEssenceType().getManaEquivalent());
            inputStack.shrink(1);
        }
    }
    
    protected boolean canSiphonWand(ItemStack inputStack, Source source) {
        return !inputStack.isEmpty() && 
                inputStack.getItem() instanceof IWand wand &&
                wand.containsManaRaw(inputStack, source, 1) &&
                this.manaStorage.getManaStored(source) < this.manaStorage.getMaxManaStored(source);
    }
    
    protected boolean doWandSiphon(ItemStack inputStack, Source source) {
        if (this.canSiphonWand(inputStack, source) && inputStack.getItem() instanceof IWand wand) {
            int maxTransferRate = Math.min(this.getBatteryTransferCap(), wand.getSiphonAmount(inputStack));
            int centimanaMissingInBattery = this.manaStorage.getMaxManaStored(source) - this.manaStorage.getManaStored(source);
            int centimanaPresentInWand = wand.getMana(inputStack, source);
            if (centimanaPresentInWand == -1) {
                centimanaPresentInWand = centimanaMissingInBattery;
            }
            int centimanaToTransfer = Mth.clamp(Math.min(centimanaPresentInWand, centimanaMissingInBattery), 0, maxTransferRate);
            if (wand.removeManaRaw(inputStack, source, centimanaToTransfer)) {
                this.manaStorage.receiveMana(source, centimanaToTransfer, false);
                return true;
            }
        }
        return false;
    }
    
    @SuppressWarnings("deprecation")
    protected void scanSurroundings() {
        BlockPos pos = this.getBlockPos();
        if (Services.LEVEL.isAreaLoaded(this.level, pos, FONT_RANGE)) {
            this.fontLocations.clear();
            Iterable<BlockPos> positions = BlockPos.betweenClosed(pos.offset(-FONT_RANGE, -FONT_RANGE, -FONT_RANGE), pos.offset(FONT_RANGE, FONT_RANGE, FONT_RANGE));
            for (BlockPos searchPos : positions) {
                if (this.level.getBlockState(searchPos).getBlock() instanceof AbstractManaFontBlock) {
                    this.fontLocations.add(searchPos.immutable());
                }
            }
        }
    }
    
    protected boolean canOutputToWand(ItemStack outputStack, Source source) {
        if (!outputStack.isEmpty() && (this.manaStorage.getMaxManaStored(source) == -1 || this.manaStorage.getManaStored(source) > 0)) {
            return outputStack.has(DataComponentsPM.CAPABILITY_MANA_STORAGE.get());
        }
        return false;
    }
    
    protected void doOutput(ItemStack outputStack, Source source) {
        if (this.canOutputToWand(outputStack, source)) {
            if (outputStack.has(DataComponentsPM.CAPABILITY_MANA_STORAGE.get())) {
                outputStack.update(DataComponentsPM.CAPABILITY_MANA_STORAGE.get(), ManaStorage.EMPTY, stackManaStorage -> {
                    int centimanaToTransfer = Math.min(this.getBatteryTransferCap(), this.manaStorage.getManaStored(source));
                    int transferedCentimana = stackManaStorage.receiveMana(source, centimanaToTransfer, false);
                    this.manaStorage.extractMana(source, transferedCentimana, false);
                    return stackManaStorage;
                });
                outputStack.set(DataComponentsPM.LAST_UPDATED.get(), System.currentTimeMillis());   // FIXME Is there a better way of marking this stack as dirty?
            }
        }
    }
    
    @Override
    public void loadAdditional(CompoundTag compound, HolderLookup.Provider registries) {
        super.loadAdditional(compound, registries);
        this.chargeTime = compound.getInt("ChargeTime");
        this.chargeTimeTotal = compound.getInt("ChargeTimeTotal");
        this.fontSiphonTime = compound.getInt("FontSiphonTime");
        ManaStorage.CODEC.parse(registries.createSerializationContext(NbtOps.INSTANCE), compound.get("ManaStorage")).resultOrPartial(msg -> {
            LOGGER.error("Failed to decode mana storage: {}", msg);
        }).ifPresent(mana -> mana.copyManaInto(this.manaStorage));
    }
    
    @Override
    protected void saveAdditional(CompoundTag compound, HolderLookup.Provider registries) {
        super.saveAdditional(compound, registries);
        compound.putInt("ChargeTime", this.chargeTime);
        compound.putInt("ChargeTimeTotal", this.chargeTimeTotal);
        compound.putInt("FontSiphonTime", this.fontSiphonTime);
        ManaStorage.CODEC.encodeStart(registries.createSerializationContext(NbtOps.INSTANCE), this.manaStorage).resultOrPartial(msg -> {
            LOGGER.error("Failed to encode mana storage: {}", msg);
        }).ifPresent(encoded -> compound.put("ManaStorage", encoded));
    }

    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new ManaBatteryMenu(pContainerId, pPlayerInventory, this.getBlockPos(), this, this.chargerData);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable(this.getBlockState().getBlock().getDescriptionId());
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
        return this.manaStorage.getMaxManaStored(Sources.EARTH);
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
            this.chargeTimeTotal = this.getChargeTimeTotal();
            this.chargeTime = 0;
            this.setChanged();
        }
    }

    @Override
    protected int getInventoryCount() {
        return 2;
    }

    @Override
    protected int getInventorySize(int inventoryIndex) {
        return switch (inventoryIndex) {
            case INPUT_INV_INDEX, CHARGE_INV_INDEX -> 1;
            default -> 0;
        };
    }

    @Override
    public Optional<Integer> getInventoryIndexForFace(@NotNull Direction face) {
        return switch (face) {
            case UP -> Optional.of(INPUT_INV_INDEX);
            case DOWN -> Optional.empty();
            default -> Optional.of(CHARGE_INV_INDEX);
        };
    }

    @Override
    protected NonNullList<IItemHandlerPM> createHandlers() {
        NonNullList<IItemHandlerPM> retVal = NonNullList.withSize(this.getInventoryCount(), Services.ITEM_HANDLERS.create(this));
        
        // Create input handler
        retVal.set(INPUT_INV_INDEX, Services.ITEM_HANDLERS.builder(this.inventories.get(INPUT_INV_INDEX), this)
                .itemValidFunction((slot, stack) -> (stack.getItem() instanceof IWand) || (stack.getItem() instanceof EssenceItem))
                .build());
        
        // Create charge handler
        retVal.set(CHARGE_INV_INDEX, Services.ITEM_HANDLERS.builder(this.inventories.get(CHARGE_INV_INDEX), this)
                .itemValidFunction((slot, stack) -> stack.has(DataComponentsPM.CAPABILITY_MANA_STORAGE.get()))
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
