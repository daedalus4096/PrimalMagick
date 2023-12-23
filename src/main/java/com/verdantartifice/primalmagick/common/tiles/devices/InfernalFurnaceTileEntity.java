package com.verdantartifice.primalmagick.common.tiles.devices;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import com.verdantartifice.primalmagick.common.capabilities.IManaStorage;
import com.verdantartifice.primalmagick.common.capabilities.ItemStackHandlerPM;
import com.verdantartifice.primalmagick.common.capabilities.ManaStorage;
import com.verdantartifice.primalmagick.common.capabilities.PrimalMagickCapabilities;
import com.verdantartifice.primalmagick.common.menus.InfernalFurnaceMenu;
import com.verdantartifice.primalmagick.common.sources.IManaContainer;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.sources.SourceList;
import com.verdantartifice.primalmagick.common.tags.ItemTagsPM;
import com.verdantartifice.primalmagick.common.tiles.TileEntityTypesPM;
import com.verdantartifice.primalmagick.common.tiles.base.AbstractTileSidedInventoryPM;
import com.verdantartifice.primalmagick.common.wands.IWand;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.RecipeCraftingHolder;
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;

/**
 * Definition of an infernal furnace tile entity.  Performs the smelting for the corresponding block.
 * 
 * @see {@link com.verdantartifice.primalmagick.common.blocks.devices.InfernalFurnaceBlock}
 * @author Daedalus4096
 */
public class InfernalFurnaceTileEntity extends AbstractTileSidedInventoryPM implements MenuProvider, IManaContainer, RecipeCraftingHolder, StackedContentsCompatible {
    protected static final int SUPERCHARGE_MULTIPLIER = 5;
    protected static final int MANA_PER_HALF_SECOND = 1;
    protected static final int DEFAULT_COOK_TIME = 100;
    protected static final int LIT_GRACE_TICKS_MAX = 5;
    protected static final int OUTPUT_INV_INDEX = 0;
    protected static final int INPUT_INV_INDEX = 1;
    protected static final int FUEL_INV_INDEX = 2;
    
    protected int superchargeTime;
    protected int superchargeTimeTotal;
    protected int processTime;
    protected int processTimeTotal;
    protected int litGraceTicks;
    protected ManaStorage manaStorage;
    protected LazyOptional<IManaStorage> manaStorageOpt = LazyOptional.of(() -> this.manaStorage);

    private final Object2IntOpenHashMap<ResourceLocation> recipesUsed = new Object2IntOpenHashMap<>();

    // Define a container-trackable representation of this tile's relevant data
    protected final ContainerData furnaceData = new ContainerData() {
        @Override
        public int get(int index) {
            switch (index) {
            case 0:
                return InfernalFurnaceTileEntity.this.processTime;
            case 1:
                return InfernalFurnaceTileEntity.this.processTimeTotal;
            case 2:
                return InfernalFurnaceTileEntity.this.manaStorage.getManaStored(Source.INFERNAL);
            case 3:
                return InfernalFurnaceTileEntity.this.manaStorage.getMaxManaStored(Source.INFERNAL);
            case 4:
                return InfernalFurnaceTileEntity.this.superchargeTime;
            case 5:
                return InfernalFurnaceTileEntity.this.superchargeTimeTotal;
            default:
                return 0;
            }
        }

        @Override
        public void set(int index, int value) {
            // Don't set mana storage values
            switch (index) {
            case 0:
                InfernalFurnaceTileEntity.this.processTime = value;
                break;
            case 1:
                InfernalFurnaceTileEntity.this.processTimeTotal = value;
                break;
            case 4:
                InfernalFurnaceTileEntity.this.superchargeTime = value;
                break;
            case 5:
                InfernalFurnaceTileEntity.this.superchargeTimeTotal = value;
                break;
            }
        }

        @Override
        public int getCount() {
            return 6;
        }
    };
    
    public InfernalFurnaceTileEntity(BlockPos pos, BlockState state) {
        super(TileEntityTypesPM.INFERNAL_FURNACE.get(), pos, state);
        this.manaStorage = new ManaStorage(10000, 100, 100, Source.INFERNAL);
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        this.processTime = compound.getInt("ProcessTime");
        this.processTimeTotal = compound.getInt("ProcessTimeTotal");
        this.superchargeTime = compound.getInt("SuperchargeTime");
        this.superchargeTimeTotal = compound.getInt("SuperchargeTimeTotal");
        this.manaStorage.deserializeNBT(compound.getCompound("ManaStorage"));
        
        CompoundTag recipesUsedTag = compound.getCompound("RecipesUsed");
        for (String key : recipesUsedTag.getAllKeys()) {
            this.recipesUsed.put(new ResourceLocation(key), recipesUsedTag.getInt(key));
        }
    }

    @Override
    protected void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        compound.putInt("ProcessTime", this.processTime);
        compound.putInt("ProcessTimeTotal", this.processTimeTotal);
        compound.putInt("SuperchargeTime", this.superchargeTime);
        compound.putInt("SuperchargeTimeTotal", this.superchargeTimeTotal);
        compound.put("ManaStorage", this.manaStorage.serializeNBT());
        
        CompoundTag recipesUsedTag = new CompoundTag();
        this.recipesUsed.forEach((key, value) -> {
            recipesUsedTag.putInt(key.toString(), value);
        });
        compound.put("RecipesUsed", recipesUsedTag);
    }

    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new InfernalFurnaceMenu(pContainerId, pPlayerInventory, this.getBlockPos(), this, this.furnaceData);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable(this.getBlockState().getBlock().getDescriptionId());
    }
    
    private boolean isLit() {
        return this.processTime > 0 || this.litGraceTicks > 0;
    }
    
    private boolean isCharged() {
        int current = this.getMana(Source.INFERNAL);
        return current > 0 && current >= getManaNeeded(this.getLevel(), this);
    }
    
    private boolean isSupercharged() {
        return this.superchargeTime > 0;
    }
    
    private static Optional<RecipeHolder<SmeltingRecipe>> getActiveRecipe(Level level, InfernalFurnaceTileEntity entity) {
        SimpleContainer testInv = new SimpleContainer(entity.getItem(INPUT_INV_INDEX, 0));
        return level.getRecipeManager().getRecipeFor(RecipeType.SMELTING, testInv, level);
    }
    
    @Override
    public void onLoad() {
        super.onLoad();
        this.processTimeTotal = getTotalCookTime(this.level, this, DEFAULT_COOK_TIME);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, InfernalFurnaceTileEntity entity) {
        boolean shouldMarkDirty = false;
        boolean startedLit = entity.isLit();
        
        entity.litGraceTicks = Mth.clamp(entity.litGraceTicks - 1, 0, LIT_GRACE_TICKS_MAX);
        
        if (!level.isClientSide) {
            // Fill up internal mana storage with that from any inserted wands
            ItemStack wandStack = entity.getItem(FUEL_INV_INDEX, 1);
            if (!wandStack.isEmpty() && wandStack.getItem() instanceof IWand wand) {
                int centimanaMissing = entity.manaStorage.getMaxManaStored(Source.INFERNAL) - entity.manaStorage.getManaStored(Source.INFERNAL);
                int centimanaToTransfer = Mth.clamp(centimanaMissing, 0, 100);
                if (wand.consumeMana(wandStack, null, Source.INFERNAL, centimanaToTransfer)) {
                    entity.manaStorage.receiveMana(Source.INFERNAL, centimanaToTransfer, false);
                    shouldMarkDirty = true;
                }
            }
        }
         
        if (entity.isSupercharged()) {
            --entity.superchargeTime;
        }
        
        ItemStack fuelStack = entity.getItem(FUEL_INV_INDEX, 0);
        boolean inputPopulated = !entity.getItem(INPUT_INV_INDEX, 0).isEmpty();
        boolean fuelPopulated = !fuelStack.isEmpty();
        if (entity.isCharged() && inputPopulated) {
            RecipeHolder<?> recipeHolder = getActiveRecipe(level, entity).orElse(null);
            int furnaceMaxStackSize = entity.itemHandlers.get(INPUT_INV_INDEX).getSlotLimit(0);
            
            // Handle supercharge burn
            if (!entity.isSupercharged() && fuelPopulated && canBurn(level.registryAccess(), recipeHolder, entity, furnaceMaxStackSize)) {
                entity.superchargeTimeTotal = entity.getSuperchargeDuration(fuelStack);
                entity.superchargeTime = entity.superchargeTimeTotal;
                if (entity.isSupercharged()) {
                    shouldMarkDirty = true;
                    if (fuelStack.hasCraftingRemainingItem()) {
                        entity.setItem(FUEL_INV_INDEX, 0, fuelStack.getCraftingRemainingItem());
                    } else {
                        fuelStack.shrink(1);
                        if (fuelStack.isEmpty()) {
                            entity.setItem(FUEL_INV_INDEX, 0, ItemStack.EMPTY);
                        }
                    }
                }
            }
            
            // Process the item being smelted
            if (entity.isCharged() && canBurn(level.registryAccess(), recipeHolder, entity, furnaceMaxStackSize)) {
                entity.processTime += (entity.isSupercharged() ? SUPERCHARGE_MULTIPLIER : 1);
                if (entity.processTime >= entity.processTimeTotal) {
                    entity.processTime = 0;
                    entity.processTimeTotal = getTotalCookTime(level, entity, DEFAULT_COOK_TIME);
                    entity.litGraceTicks = LIT_GRACE_TICKS_MAX;
                    if (burn(level.registryAccess(), recipeHolder, entity, furnaceMaxStackSize)) {
                        entity.setRecipeUsed(recipeHolder);
                    }
                    shouldMarkDirty = true;
                }
            } else {
                entity.processTime = 0;
            }
        } else if (entity.processTime > 0) {
            // Decay progress if not enough mana is available
            entity.processTime = Mth.clamp(entity.processTime - 2, 0, entity.processTimeTotal);
        }
        
        // Update the block's LIT blockstate property if needed
        if (startedLit != entity.isLit()) {
            shouldMarkDirty = true;
            state = state.setValue(BlockStateProperties.LIT, entity.isLit());
            level.setBlock(pos, state, Block.UPDATE_ALL);
        }
        
        // Notify the world a block change has occurred if needed
        if (shouldMarkDirty) {
            setChanged(level, pos, state);
        }
   }
    
    private static boolean canBurn(RegistryAccess registryAccess, @Nullable RecipeHolder<?> recipeHolder, InfernalFurnaceTileEntity entity, int maxFurnaceStackSize) {
        if (!entity.getItem(INPUT_INV_INDEX, 0).isEmpty() && recipeHolder != null) {
            @SuppressWarnings("unchecked")
            ItemStack recipeOutput = ((Recipe<Container>)recipeHolder.value()).assemble(new RecipeWrapper(entity.itemHandlers.get(INPUT_INV_INDEX)), registryAccess);
            if (recipeOutput.isEmpty()) {
                return false;
            } else {
                ItemStack existingOutput = entity.getItem(OUTPUT_INV_INDEX, 0);
                if (existingOutput.isEmpty()) {
                    return true;
                } else if (!ItemStack.isSameItem(recipeOutput, existingOutput)) {
                    return false;
                } else if (existingOutput.getCount() + recipeOutput.getCount() <= maxFurnaceStackSize && existingOutput.getCount() + recipeOutput.getCount() <= existingOutput.getMaxStackSize()) {
                    return true;
                } else {
                    return existingOutput.getCount() + recipeOutput.getCount() <= recipeOutput.getMaxStackSize();
                }
            }
        } else {
            return false;
        }
    }
    
    private static boolean burn(RegistryAccess registryAccess, @Nullable RecipeHolder<?> recipeHolder, InfernalFurnaceTileEntity entity, int maxFurnaceStackSize) {
        if (recipeHolder != null && canBurn(registryAccess, recipeHolder, entity, maxFurnaceStackSize)) {
            ItemStack inputStack = entity.getItem(INPUT_INV_INDEX, 0);
            @SuppressWarnings("unchecked")
            ItemStack recipeOutput = ((Recipe<Container>)recipeHolder.value()).assemble(new RecipeWrapper(entity.itemHandlers.get(INPUT_INV_INDEX)), registryAccess);
            ItemStack existingOutput = entity.getItem(OUTPUT_INV_INDEX, 0);
            if (existingOutput.isEmpty()) {
                entity.setItem(OUTPUT_INV_INDEX, 0, recipeOutput.copy());
            } else if (ItemStack.isSameItem(recipeOutput, existingOutput)) {
                existingOutput.grow(recipeOutput.getCount());
            }
            
            if (entity.manaStorage.canExtract(Source.INFERNAL)) {
                entity.manaStorage.extractMana(Source.INFERNAL, getManaNeeded(entity.getLevel(), entity), false);
            }
            inputStack.shrink(1);
            return true;
        } else {
            return false;
        }
    }
    
    protected int getSuperchargeDuration(ItemStack pFuel) {
        if (pFuel.isEmpty()) {
            return 0;
        } else {
            // Shorten based on speed boost
            return ForgeHooks.getBurnTime(pFuel, RecipeType.SMELTING) / SUPERCHARGE_MULTIPLIER;
        }
    }

    private static int getTotalCookTime(Level pLevel, InfernalFurnaceTileEntity pBlockEntity, int defaultTime) {
        // Infernal furnaces take half as long to smelt items as normal
        return getActiveRecipe(pLevel, pBlockEntity).map(RecipeHolder::value).map(AbstractCookingRecipe::getCookingTime).map(t -> t / 2).orElse(defaultTime);
    }
    
    private static int getManaNeeded(Level pLevel, InfernalFurnaceTileEntity pBlockEntity) {
        // Return centimana per ten ticks of cooking time of the current recipe, or zero if no recipe is active
        return pBlockEntity.getItem(INPUT_INV_INDEX, 0).isEmpty() ? 0 : (getTotalCookTime(pLevel, pBlockEntity, 0) / 10) * MANA_PER_HALF_SECOND;
    }
    
    public static boolean isSuperchargeFuel(ItemStack pStack) {
        return pStack.is(ItemTagsPM.INFERNAL_SUPERCHARGE_FUEL);
    }

    @Override
    public void fillStackedContents(StackedContents pHelper) {
        for (int invIndex = 0; invIndex < this.getInventoryCount(); invIndex++) {
            for (int slotIndex = 0; slotIndex < this.getInventorySize(invIndex); slotIndex++) {
                pHelper.accountStack(this.getItem(invIndex, slotIndex));
            }
        }
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (!this.remove && cap == PrimalMagickCapabilities.MANA_STORAGE) {
            return this.manaStorageOpt.cast();
        } else {
            return super.getCapability(cap, side);
        }
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        this.manaStorageOpt.invalidate();
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
    public void setRecipeUsed(RecipeHolder<?> pRecipeHolder) {
        if (pRecipeHolder != null) {
            this.recipesUsed.addTo(pRecipeHolder.id(), 1);
        }
    }

    @Override
    public RecipeHolder<?> getRecipeUsed() {
        return null;
    }

    @Override
    public void setItem(int invIndex, int slotIndex, ItemStack stack) {
        boolean flag = !stack.isEmpty() && ItemStack.isSameItemSameTags(this.getItem(invIndex, slotIndex), stack);
        super.setItem(invIndex, slotIndex, stack);
        if (invIndex == INPUT_INV_INDEX && !flag && this.hasLevel()) {
            this.processTimeTotal = getTotalCookTime(this.level, this, DEFAULT_COOK_TIME);
            this.processTime = 0;
            this.setChanged();
        }
    }

    @Override
    public void awardUsedRecipes(Player player, List<ItemStack> stacks) {
        // Skip the default implementation and do nothing
    }
    
    public void awardUsedRecipesAndPopExperience(ServerPlayer pPlayer) {
        List<RecipeHolder<?>> recipes = this.getRecipesToAwardAndPopExperience(pPlayer.serverLevel(), pPlayer.position());
        pPlayer.awardRecipes(recipes);
        recipes.stream().filter(Predicate.not(Objects::isNull)).forEach(r -> pPlayer.triggerRecipeCrafted(r, this.inventories.get(INPUT_INV_INDEX)));
        this.recipesUsed.clear();
    }
    
    public List<RecipeHolder<?>> getRecipesToAwardAndPopExperience(ServerLevel pLevel, Vec3 pPopVec) {
        List<RecipeHolder<?>> retVal = new ArrayList<>();
        for (Object2IntMap.Entry<ResourceLocation> entry : this.recipesUsed.object2IntEntrySet()) {
            pLevel.getRecipeManager().byKey(entry.getKey()).ifPresent(recipeHolder -> {
                retVal.add(recipeHolder);
                createExperience(pLevel, pPopVec, entry.getIntValue(), ((AbstractCookingRecipe)recipeHolder.value()).getExperience());
            });
        }
        return retVal;
    }
    
    private static void createExperience(ServerLevel pLevel, Vec3 pPopVec, int pRecipeIndex, float pExperience) {
        int i = Mth.floor((float)pRecipeIndex * pExperience);
        float f = Mth.frac((float)pRecipeIndex * pExperience);
        if (f != 0.0F && Math.random() < (double)f) {
            i++;
        }
        ExperienceOrb.award(pLevel, pPopVec, i);
    }

    @Override
    protected int getInventoryCount() {
        return 3;
    }

    @Override
    protected int getInventorySize(int inventoryIndex) {
        return switch (inventoryIndex) {
            case INPUT_INV_INDEX, OUTPUT_INV_INDEX -> 1;
            case FUEL_INV_INDEX -> 2;
            default -> 0;
        };
    }

    @Override
    protected OptionalInt getInventoryIndexForFace(Direction face) {
        return switch (face) {
            case UP -> OptionalInt.of(INPUT_INV_INDEX);
            case DOWN -> OptionalInt.of(OUTPUT_INV_INDEX);
            default -> OptionalInt.of(FUEL_INV_INDEX);
        };
    }

    @Override
    protected NonNullList<ItemStackHandler> createHandlers() {
        NonNullList<ItemStackHandler> retVal = NonNullList.withSize(this.getInventoryCount(), new ItemStackHandlerPM(this));
        
        // Create input handler
        retVal.set(INPUT_INV_INDEX, new ItemStackHandlerPM(this.inventories.get(INPUT_INV_INDEX), this));
        
        // Create fuel handler
        retVal.set(FUEL_INV_INDEX, new ItemStackHandlerPM(this.inventories.get(FUEL_INV_INDEX), this) {
            @Override
            public boolean isItemValid(int slot, ItemStack stack) {
                if (slot == 0) {
                    return stack.is(ItemTagsPM.INFERNAL_SUPERCHARGE_FUEL);
                } else if (slot == 1) {
                    return stack.getItem() instanceof IWand;
                } else {
                    return false;
                }
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
        // Slot 0 was the output item stack
        this.setItem(OUTPUT_INV_INDEX, 0, legacyItems.get(0));

        // Slot 1 was the input item stack
        this.setItem(INPUT_INV_INDEX, 0, legacyItems.get(1));

        // Slot 2 was the supercharge fuel item stack
        this.setItem(FUEL_INV_INDEX, 0, legacyItems.get(2));

        // Slot 3 was the wand item stack
        this.setItem(FUEL_INV_INDEX, 1, legacyItems.get(3));
    }
}
