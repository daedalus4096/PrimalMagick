package com.verdantartifice.primalmagick.common.tiles.devices;

import com.verdantartifice.primalmagick.common.capabilities.IItemHandlerPM;
import com.verdantartifice.primalmagick.common.capabilities.IManaStorage;
import com.verdantartifice.primalmagick.common.capabilities.ManaStorage;
import com.verdantartifice.primalmagick.common.components.DataComponentsPM;
import com.verdantartifice.primalmagick.common.menus.InfernalFurnaceMenu;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.sources.SourceList;
import com.verdantartifice.primalmagick.common.sources.Sources;
import com.verdantartifice.primalmagick.common.tags.ItemTagsPM;
import com.verdantartifice.primalmagick.common.tiles.BlockEntityTypesPM;
import com.verdantartifice.primalmagick.common.tiles.base.AbstractTileSidedInventoryPM;
import com.verdantartifice.primalmagick.common.tiles.base.IManaContainingBlockEntity;
import com.verdantartifice.primalmagick.common.wands.IWand;
import com.verdantartifice.primalmagick.platform.Services;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap.Builder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.RecipeCraftingHolder;
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.FuelValues;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Definition of an infernal furnace tile entity.  Performs the smelting for the corresponding block.
 * 
 * @see com.verdantartifice.primalmagick.common.blocks.devices.InfernalFurnaceBlock
 * @author Daedalus4096
 */
public abstract class InfernalFurnaceTileEntity extends AbstractTileSidedInventoryPM implements MenuProvider, IManaContainingBlockEntity, RecipeCraftingHolder, StackedContentsCompatible {
    protected static final int SUPERCHARGE_MULTIPLIER = 5;
    protected static final int MANA_PER_HALF_SECOND = 100;
    protected static final int DEFAULT_COOK_TIME = 100;
    protected static final int LIT_GRACE_TICKS_MAX = 5;
    public static final int OUTPUT_INV_INDEX = 0;
    public static final int INPUT_INV_INDEX = 1;
    public static final int WAND_INV_INDEX = 2;
    public static final int FUEL_INV_INDEX = 3;
    
    protected int superchargeTime;
    protected int superchargeTimeTotal;
    protected int processTime;
    protected int processTimeTotal;
    protected int litGraceTicks;
    protected ManaStorage manaStorage;

    private final Object2IntOpenHashMap<ResourceKey<Recipe<?>>> recipesUsed = new Object2IntOpenHashMap<>();

    // Define a container-trackable representation of this tile's relevant data
    protected final ContainerData furnaceData = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> InfernalFurnaceTileEntity.this.processTime;
                case 1 -> InfernalFurnaceTileEntity.this.processTimeTotal;
                case 2 -> InfernalFurnaceTileEntity.this.manaStorage.getManaStored(Sources.INFERNAL);
                case 3 -> InfernalFurnaceTileEntity.this.manaStorage.getMaxManaStored(Sources.INFERNAL);
                case 4 -> InfernalFurnaceTileEntity.this.superchargeTime;
                case 5 -> InfernalFurnaceTileEntity.this.superchargeTimeTotal;
                default -> 0;
            };
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
        super(BlockEntityTypesPM.INFERNAL_FURNACE.get(), pos, state);
        this.manaStorage = new ManaStorage(10000, 1000, 1000, Sources.INFERNAL);
    }

    public IManaStorage<?> getUncachedManaStorage() {
        return this.manaStorage;
    }

    @Override
    protected void loadAdditional(@NotNull ValueInput input) {
        super.loadAdditional(input);
        this.processTime = input.getIntOr("ProcessTime", 0);
        this.processTimeTotal = input.getIntOr("ProcessTimeTotal", 0);
        this.superchargeTime = input.getIntOr("SuperchargeTime", 0);
        this.superchargeTimeTotal = input.getIntOr("SuperchargeTimeTotal", 0);
        input.read("ManaStorage", ManaStorage.CODEC).ifPresent(s -> s.copyManaInto(this.manaStorage));

        this.recipesUsed.clear();
        input.childrenListOrEmpty("RecipesUsed").stream().forEach(child -> {
            Optional<ResourceKey<Recipe<?>>> recipeOpt = child.read("Recipe", ResourceKey.codec(Registries.RECIPE));
            Optional<Integer> countOpt = child.getInt("Count");
            if (recipeOpt.isPresent() && countOpt.isPresent()) {
                this.recipesUsed.put(recipeOpt.get(), countOpt.get().intValue());
            }
        });
    }

    @Override
    protected void saveAdditional(@NotNull ValueOutput output) {
        super.saveAdditional(output);
        output.putInt("ProcessTime", this.processTime);
        output.putInt("ProcessTimeTotal", this.processTimeTotal);
        output.putInt("SuperchargeTime", this.superchargeTime);
        output.putInt("SuperchargeTimeTotal", this.superchargeTimeTotal);
        output.store("ManaStorage", ManaStorage.CODEC, this.manaStorage);

        ValueOutput.ValueOutputList childList = output.childrenList("RecipesUsed");
        this.recipesUsed.forEach((key, value) -> {
            ValueOutput child = childList.addChild();
            child.store("Recipe", ResourceKey.codec(Registries.RECIPE), key);
            child.putInt("Count", value);
        });
    }

    @Override
    public AbstractContainerMenu createMenu(int pContainerId, @NotNull Inventory pPlayerInventory, @NotNull Player pPlayer) {
        return new InfernalFurnaceMenu(pContainerId, pPlayerInventory, this.getBlockPos(), this, this.furnaceData);
    }

    @Override
    @NotNull
    public Component getDisplayName() {
        return Component.translatable(this.getBlockState().getBlock().getDescriptionId());
    }
    
    private boolean isLit() {
        return this.processTime > 0 || this.litGraceTicks > 0;
    }
    
    private boolean isCharged(ServerLevel serverLevel) {
        int current = this.getMana(Sources.INFERNAL);
        return current > 0 && current >= getManaNeeded(serverLevel, this);
    }
    
    private boolean isSupercharged() {
        return this.superchargeTime > 0;
    }
    
    private static Optional<RecipeHolder<SmeltingRecipe>> getActiveRecipe(ServerLevel level, InfernalFurnaceTileEntity entity) {
        return level.recipeAccess().getRecipeFor(RecipeType.SMELTING, new SingleRecipeInput(entity.getItem(INPUT_INV_INDEX, 0)), level);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, InfernalFurnaceTileEntity entity) {
        if (!(level instanceof ServerLevel serverLevel)) {
            return;
        }
        
        boolean shouldMarkDirty = false;
        boolean startedLit = entity.isLit();
        
        entity.litGraceTicks = Mth.clamp(entity.litGraceTicks - 1, 0, LIT_GRACE_TICKS_MAX);
        
        if (!serverLevel.isClientSide()) {
            // Fill up internal mana storage with that from any inserted wands
            ItemStack wandStack = entity.getItem(WAND_INV_INDEX, 0);
            if (!wandStack.isEmpty() && wandStack.getItem() instanceof IWand wand) {
                int centimanaMissing = entity.manaStorage.getMaxManaStored(Sources.INFERNAL) - entity.manaStorage.getManaStored(Sources.INFERNAL);
                int centimanaToTransfer = Mth.clamp(centimanaMissing, 0, 100);
                if (wand.consumeMana(wandStack, null, Sources.INFERNAL, centimanaToTransfer, serverLevel.registryAccess())) {
                    entity.manaStorage.receiveMana(Sources.INFERNAL, centimanaToTransfer, false);
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
        if (entity.isCharged(serverLevel) && inputPopulated) {
            RecipeHolder<?> recipeHolder = getActiveRecipe(serverLevel, entity).orElse(null);
            int furnaceMaxStackSize = entity.itemHandlers.get(FUEL_INV_INDEX).getSlotLimit(0);
            
            // Handle supercharge burn
            if (!entity.isSupercharged() && fuelPopulated && canBurn(serverLevel.registryAccess(), recipeHolder, entity, furnaceMaxStackSize)) {
                entity.superchargeTimeTotal = entity.getSuperchargeDuration(fuelStack, serverLevel.fuelValues());
                entity.superchargeTime = entity.superchargeTimeTotal;
                if (entity.isSupercharged()) {
                    shouldMarkDirty = true;
                    ItemStack remainderStack = fuelStack.getItem().getCraftingRemainder();
                    if (!remainderStack.isEmpty()) {
                        entity.setItem(FUEL_INV_INDEX, 0, remainderStack);
                    } else {
                        fuelStack.shrink(1);
                        if (fuelStack.isEmpty()) {
                            entity.setItem(FUEL_INV_INDEX, 0, ItemStack.EMPTY);
                        }
                    }
                }
            }
            
            // Process the item being smelted
            if (entity.isCharged(serverLevel) && canBurn(serverLevel.registryAccess(), recipeHolder, entity, furnaceMaxStackSize)) {
                entity.processTime += (entity.isSupercharged() ? SUPERCHARGE_MULTIPLIER : 1);
                if (entity.processTime >= entity.processTimeTotal) {
                    entity.processTime = 0;
                    entity.processTimeTotal = getTotalCookTime(serverLevel, entity, DEFAULT_COOK_TIME);
                    entity.litGraceTicks = LIT_GRACE_TICKS_MAX;
                    if (burn(serverLevel.registryAccess(), recipeHolder, entity, furnaceMaxStackSize)) {
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
        
        // Update the block's LIT block state property if needed
        if (startedLit != entity.isLit()) {
            shouldMarkDirty = true;
            state = state.setValue(BlockStateProperties.LIT, entity.isLit());
            serverLevel.setBlock(pos, state, Block.UPDATE_ALL);
        }
        
        // Notify the world a block change has occurred if needed
        if (shouldMarkDirty) {
            setChanged(serverLevel, pos, state);
        }
   }
    
    private static boolean canBurn(RegistryAccess registryAccess, @Nullable RecipeHolder<?> recipeHolder, InfernalFurnaceTileEntity entity, int maxFurnaceStackSize) {
        if (!entity.getItem(INPUT_INV_INDEX, 0).isEmpty() && recipeHolder != null) {
            @SuppressWarnings("unchecked")
            ItemStack recipeOutput = ((Recipe<SingleRecipeInput>)recipeHolder.value()).assemble(new SingleRecipeInput(entity.getItem(INPUT_INV_INDEX, 0)));
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
        if (entity.getLevel() instanceof ServerLevel serverLevel && recipeHolder != null && canBurn(registryAccess, recipeHolder, entity, maxFurnaceStackSize)) {
            ItemStack inputStack = entity.getItem(INPUT_INV_INDEX, 0);
            @SuppressWarnings("unchecked")
            ItemStack recipeOutput = ((Recipe<SingleRecipeInput>)recipeHolder.value()).assemble(new SingleRecipeInput(entity.getItem(INPUT_INV_INDEX, 0)));
            ItemStack existingOutput = entity.getItem(OUTPUT_INV_INDEX, 0);
            if (existingOutput.isEmpty()) {
                entity.setItem(OUTPUT_INV_INDEX, 0, recipeOutput.copy());
            } else if (ItemStack.isSameItem(recipeOutput, existingOutput)) {
                existingOutput.grow(recipeOutput.getCount());
            }
            
            if (entity.manaStorage.canExtract(Sources.INFERNAL)) {
                entity.manaStorage.extractMana(Sources.INFERNAL, getManaNeeded(serverLevel, entity), false);
            }
            inputStack.shrink(1);
            return true;
        } else {
            return false;
        }
    }
    
    protected int getSuperchargeDuration(ItemStack pFuel, FuelValues fuelValues) {
        if (pFuel.isEmpty()) {
            return 0;
        } else {
            // Shorten based on speed boost
            return Services.EVENTS.getBurnTime(pFuel, RecipeType.SMELTING, fuelValues) / SUPERCHARGE_MULTIPLIER;
        }
    }

    protected static int getTotalCookTime(ServerLevel pLevel, InfernalFurnaceTileEntity pBlockEntity, int defaultTime) {
        // Infernal furnaces take half as long to smelt items as normal
        return getActiveRecipe(pLevel, pBlockEntity).map(RecipeHolder::value).map(AbstractCookingRecipe::cookingTime).map(t -> t / 2).orElse(defaultTime);
    }
    
    private static int getManaNeeded(ServerLevel pLevel, InfernalFurnaceTileEntity pBlockEntity) {
        // Return centimana per ten ticks of cooking time of the current recipe, or zero if no recipe is active
        return pBlockEntity.getItem(INPUT_INV_INDEX, 0).isEmpty() ? 0 : (getTotalCookTime(pLevel, pBlockEntity, 0) / 10) * MANA_PER_HALF_SECOND;
    }
    
    @Override
    public void fillStackedContents(@NotNull StackedItemContents pHelper) {
        for (int invIndex = 0; invIndex < this.getInventoryCount(); invIndex++) {
            for (int slotIndex = 0; slotIndex < this.getInventorySize(invIndex); slotIndex++) {
                pHelper.accountStack(this.getItem(invIndex, slotIndex));
            }
        }
    }

    @Override
    public int getMana(@NotNull Source source) {
        return this.manaStorage.getManaStored(source);
    }

    @Override
    @NotNull
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
    public void setMana(@NotNull Source source, int amount) {
        this.manaStorage.setMana(source, amount);
        this.setChanged();
        this.syncTile(true);
    }

    @Override
    public void setMana(@NotNull SourceList mana) {
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
        boolean flag = !stack.isEmpty() && ItemStack.isSameItemSameComponents(this.getItem(invIndex, slotIndex), stack);
        super.setItem(invIndex, slotIndex, stack);
        if (invIndex == INPUT_INV_INDEX && !flag && this.level instanceof ServerLevel serverLevel) {
            this.processTimeTotal = getTotalCookTime(serverLevel, this, DEFAULT_COOK_TIME);
            this.processTime = 0;
            this.setChanged();
        }
    }

    @Override
    public void awardUsedRecipes(@NotNull Player player, @NotNull List<ItemStack> stacks) {
        // Skip the default implementation and do nothing
    }
    
    public void awardUsedRecipesAndPopExperience(ServerPlayer pPlayer) {
        List<RecipeHolder<?>> recipes = this.getRecipesToAwardAndPopExperience(pPlayer.level(), pPlayer.position());
        pPlayer.awardRecipes(recipes);
        recipes.stream().filter(Predicate.not(Objects::isNull)).forEach(r -> pPlayer.triggerRecipeCrafted(r, this.inventories.get(INPUT_INV_INDEX)));
        this.recipesUsed.clear();
    }
    
    public List<RecipeHolder<?>> getRecipesToAwardAndPopExperience(ServerLevel pLevel, Vec3 pPopVec) {
        List<RecipeHolder<?>> retVal = new ArrayList<>();
        for (Object2IntMap.Entry<ResourceKey<Recipe<?>>> entry : this.recipesUsed.object2IntEntrySet()) {
            pLevel.recipeAccess().byKey(entry.getKey()).ifPresent(recipeHolder -> {
                retVal.add(recipeHolder);
                createExperience(pLevel, pPopVec, entry.getIntValue(), ((AbstractCookingRecipe)recipeHolder.value()).experience());
            });
        }
        return retVal;
    }
    
    private static void createExperience(ServerLevel pLevel, Vec3 pPopVec, int pRecipeIndex, float pExperience) {
        int i = Mth.floor((float)pRecipeIndex * pExperience);
        float f = Mth.frac((float)pRecipeIndex * pExperience);
        if (f != 0.0F && pLevel.getRandom().nextDouble() < (double)f) {
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
            case WAND_INV_INDEX, OUTPUT_INV_INDEX, INPUT_INV_INDEX, FUEL_INV_INDEX -> 1;
            default -> 0;
        };
    }

    @Override
    public Optional<Integer> getInventoryIndexForFace(@NotNull Direction face) {
        return switch (face) {
            case DOWN -> Optional.of(OUTPUT_INV_INDEX);
            case UP -> Optional.of(INPUT_INV_INDEX);
            default -> Optional.of(FUEL_INV_INDEX);
        };
    }

    @Override
    protected NonNullList<IItemHandlerPM> createItemHandlers() {
        NonNullList<IItemHandlerPM> retVal = NonNullList.withSize(this.getInventoryCount(), Services.ITEM_HANDLERS.create(this));
        
        // Create input handler
        retVal.set(INPUT_INV_INDEX, Services.ITEM_HANDLERS.create(this.inventories.get(INPUT_INV_INDEX), this));

        // Create fuel handler
        retVal.set(FUEL_INV_INDEX, Services.ITEM_HANDLERS.builder(this.inventories.get(FUEL_INV_INDEX), this)
                .itemValidFunction((slot, stack) -> stack.is(ItemTagsPM.INFERNAL_SUPERCHARGE_FUEL))
                .build());
        
        // Create wand handler
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
    protected void applyImplicitComponents(@NotNull DataComponentGetter pComponentInput) {
        super.applyImplicitComponents(pComponentInput);
        pComponentInput.getOrDefault(DataComponentsPM.CAPABILITY_MANA_STORAGE.get(), ManaStorage.EMPTY).copyManaInto(this.manaStorage);
    }

    @Override
    protected void collectImplicitComponents(@NotNull Builder pComponents) {
        super.collectImplicitComponents(pComponents);
        pComponents.set(DataComponentsPM.CAPABILITY_MANA_STORAGE.get(), this.manaStorage);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void removeComponentsFromTag(ValueOutput output) {
        output.discard("ManaStorage");
    }
}
