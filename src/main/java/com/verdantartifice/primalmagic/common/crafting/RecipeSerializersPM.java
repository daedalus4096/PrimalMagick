package com.verdantartifice.primalmagic.common.crafting;

import com.verdantartifice.primalmagic.PrimalMagic;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Deferred registry for mod recipe serializers.
 * 
 * @author Daedalus4096
 */
public class RecipeSerializersPM {
    private static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, PrimalMagic.MODID);
    
    public static void init() {
        RECIPE_SERIALIZERS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
    
    public static final RegistryObject<RecipeSerializer<ShapelessArcaneRecipe>> ARCANE_CRAFTING_SHAPELESS = RECIPE_SERIALIZERS.register("arcane_crafting_shapeless", ShapelessArcaneRecipe.Serializer::new);
    public static final RegistryObject<RecipeSerializer<ShapedArcaneRecipe>> ARCANE_CRAFTING_SHAPED = RECIPE_SERIALIZERS.register("arcane_crafting_shaped", ShapedArcaneRecipe.Serializer::new);
    public static final RegistryObject<SimpleRecipeSerializer<FlyingCarpetDyeRecipe>> FLYING_CARPET_DYE = RECIPE_SERIALIZERS.register("flying_carpet_dye_special", () -> new SimpleRecipeSerializer<>(FlyingCarpetDyeRecipe::new));
    public static final RegistryObject<RecipeSerializer<RitualRecipe>> RITUAL = RECIPE_SERIALIZERS.register("ritual", RitualRecipe.Serializer::new);
    public static final RegistryObject<SimpleRecipeSerializer<WandAssemblyRecipe>> WAND_ASSEMBLY_SPECIAL = RECIPE_SERIALIZERS.register("wand_assembly_special", () -> new SimpleRecipeSerializer<>(WandAssemblyRecipe::new));
    public static final RegistryObject<SimpleRecipeSerializer<WandInscriptionRecipe>> WAND_INSCRIPTION_SPECIAL = RECIPE_SERIALIZERS.register("wand_inscription_special", () -> new SimpleRecipeSerializer<>(WandInscriptionRecipe::new));
    public static final RegistryObject<SimpleRecipeSerializer<SpellcraftingRecipe>> SPELLCRAFTING_SPECIAL = RECIPE_SERIALIZERS.register("spellcrafting_special", () -> new SimpleRecipeSerializer<>(SpellcraftingRecipe::new));
    public static final RegistryObject<RecipeSerializer<RunecarvingRecipe>> RUNECARVING = RECIPE_SERIALIZERS.register("runecarving", RunecarvingRecipe.Serializer::new);
    public static final RegistryObject<RecipeSerializer<ConcoctingRecipe>> CONCOCTING = RECIPE_SERIALIZERS.register("concocting", ConcoctingRecipe.Serializer::new);
    public static final RegistryObject<SimpleRecipeSerializer<TieredShieldDecorationRecipe>> TIERED_SHIELD_DECORATION = RECIPE_SERIALIZERS.register("tiered_shield_decoration", () -> new SimpleRecipeSerializer<>(TieredShieldDecorationRecipe::new));
    public static final RegistryObject<RecipeSerializer<DissolutionRecipe>> DISSOLUTION = RECIPE_SERIALIZERS.register("dissolution", DissolutionRecipe.Serializer::new);
}
