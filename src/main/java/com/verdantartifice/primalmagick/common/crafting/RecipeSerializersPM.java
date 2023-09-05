package com.verdantartifice.primalmagick.common.crafting;

import com.verdantartifice.primalmagick.PrimalMagick;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * Deferred registry for mod recipe serializers.
 * 
 * @author Daedalus4096
 */
public class RecipeSerializersPM {
    private static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, PrimalMagick.MODID);
    
    public static void init() {
        RECIPE_SERIALIZERS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
    
    public static final RegistryObject<RecipeSerializer<ShapelessArcaneRecipe>> ARCANE_CRAFTING_SHAPELESS = RECIPE_SERIALIZERS.register("arcane_crafting_shapeless", ShapelessArcaneRecipe.Serializer::new);
    public static final RegistryObject<RecipeSerializer<ShapedArcaneRecipe>> ARCANE_CRAFTING_SHAPED = RECIPE_SERIALIZERS.register("arcane_crafting_shaped", ShapedArcaneRecipe.Serializer::new);
    public static final RegistryObject<SimpleCraftingRecipeSerializer<FlyingCarpetDyeRecipe>> FLYING_CARPET_DYE = RECIPE_SERIALIZERS.register("flying_carpet_dye_special", () -> new SimpleCraftingRecipeSerializer<>(FlyingCarpetDyeRecipe::new));
    public static final RegistryObject<RecipeSerializer<RitualRecipe>> RITUAL = RECIPE_SERIALIZERS.register("ritual", RitualRecipe.Serializer::new);
    public static final RegistryObject<SimpleCraftingRecipeSerializer<WandAssemblyRecipe>> WAND_ASSEMBLY_SPECIAL = RECIPE_SERIALIZERS.register("wand_assembly_special", () -> new SimpleCraftingRecipeSerializer<>(WandAssemblyRecipe::new));
    public static final RegistryObject<SimpleCraftingRecipeSerializer<WandInscriptionRecipe>> WAND_INSCRIPTION_SPECIAL = RECIPE_SERIALIZERS.register("wand_inscription_special", () -> new SimpleCraftingRecipeSerializer<>(WandInscriptionRecipe::new));
    public static final RegistryObject<SimpleCraftingRecipeSerializer<SpellcraftingRecipe>> SPELLCRAFTING_SPECIAL = RECIPE_SERIALIZERS.register("spellcrafting_special", () -> new SimpleCraftingRecipeSerializer<>(SpellcraftingRecipe::new));
    public static final RegistryObject<RecipeSerializer<RunecarvingRecipe>> RUNECARVING = RECIPE_SERIALIZERS.register("runecarving", RunecarvingRecipe.Serializer::new);
    public static final RegistryObject<RecipeSerializer<ConcoctingRecipe>> CONCOCTING = RECIPE_SERIALIZERS.register("concocting", ConcoctingRecipe.Serializer::new);
    public static final RegistryObject<SimpleCraftingRecipeSerializer<TieredShieldDecorationRecipe>> TIERED_SHIELD_DECORATION = RECIPE_SERIALIZERS.register("tiered_shield_decoration", () -> new SimpleCraftingRecipeSerializer<>(TieredShieldDecorationRecipe::new));
    public static final RegistryObject<RecipeSerializer<DissolutionRecipe>> DISSOLUTION = RECIPE_SERIALIZERS.register("dissolution", DissolutionRecipe.Serializer::new);
    public static final RegistryObject<SimpleCraftingRecipeSerializer<WandGlamourRecipe>> WAND_GLAMOUR_SPECIAL = RECIPE_SERIALIZERS.register("wand_glamour_special", () -> new SimpleCraftingRecipeSerializer<>(WandGlamourRecipe::new));
    public static final RegistryObject<SimpleCraftingRecipeSerializer<WardingModuleApplicationRecipe>> WARDING_MODULE_APPLICATION = RECIPE_SERIALIZERS.register("warding_module_application", () -> new SimpleCraftingRecipeSerializer<>(WardingModuleApplicationRecipe::new));
    public static final RegistryObject<SimpleCraftingRecipeSerializer<StaticBookCloningRecipe>> STATIC_BOOK_CLONING = RECIPE_SERIALIZERS.register("static_book_cloning", () -> new SimpleCraftingRecipeSerializer<>(StaticBookCloningRecipe::new));
}
