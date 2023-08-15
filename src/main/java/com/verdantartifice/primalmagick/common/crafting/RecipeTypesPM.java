package com.verdantartifice.primalmagick.common.crafting;

import com.verdantartifice.primalmagick.PrimalMagick;

import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * Holder for mod custom recipe types.
 * 
 * @author Daedalus4096
 */
public class RecipeTypesPM {
    private static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, PrimalMagick.MODID);
    
    public static void init() {
        RECIPE_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
    
    public static final RegistryObject<RecipeType<IArcaneRecipe>> ARCANE_CRAFTING = RECIPE_TYPES.register("arcane_crafting", () -> RecipeType.simple(PrimalMagick.resource("arcane_crafting")));
    public static final RegistryObject<RecipeType<IRitualRecipe>> RITUAL = RECIPE_TYPES.register("ritual", () -> RecipeType.simple(PrimalMagick.resource("ritual")));
    public static final RegistryObject<RecipeType<IRunecarvingRecipe>> RUNECARVING = RECIPE_TYPES.register("runecarving", () -> RecipeType.simple(PrimalMagick.resource("runecarving")));
    public static final RegistryObject<RecipeType<IConcoctingRecipe>> CONCOCTING = RECIPE_TYPES.register("concocting", () -> RecipeType.simple(PrimalMagick.resource("concocting")));
    public static final RegistryObject<RecipeType<IDissolutionRecipe>> DISSOLUTION = RECIPE_TYPES.register("dissolution", () -> RecipeType.simple(PrimalMagick.resource("dissolution")));
}
