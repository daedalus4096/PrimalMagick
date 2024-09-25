package com.verdantartifice.primalmagick.common.crafting;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.PrimalMagick;

import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * Holder for mod custom recipe types.
 * 
 * @author Daedalus4096
 */
public class RecipeTypesPM {
    private static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, Constants.MOD_ID);
    
    public static void init() {
        RECIPE_TYPES.register(PrimalMagick.getModLoadingContext().getModEventBus());
    }
    
    public static final RegistryObject<RecipeType<IArcaneRecipe>> ARCANE_CRAFTING = RECIPE_TYPES.register("arcane_crafting", () -> RecipeType.simple(ResourceUtils.loc("arcane_crafting")));
    public static final RegistryObject<RecipeType<IRitualRecipe>> RITUAL = RECIPE_TYPES.register("ritual", () -> RecipeType.simple(ResourceUtils.loc("ritual")));
    public static final RegistryObject<RecipeType<IRunecarvingRecipe>> RUNECARVING = RECIPE_TYPES.register("runecarving", () -> RecipeType.simple(ResourceUtils.loc("runecarving")));
    public static final RegistryObject<RecipeType<IConcoctingRecipe>> CONCOCTING = RECIPE_TYPES.register("concocting", () -> RecipeType.simple(ResourceUtils.loc("concocting")));
    public static final RegistryObject<RecipeType<IDissolutionRecipe>> DISSOLUTION = RECIPE_TYPES.register("dissolution", () -> RecipeType.simple(ResourceUtils.loc("dissolution")));
}
