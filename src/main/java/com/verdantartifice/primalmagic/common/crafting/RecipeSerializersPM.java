package com.verdantartifice.primalmagic.common.crafting;

import com.verdantartifice.primalmagic.PrimalMagic;

import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Deferred registry for mod recipe serializers.
 * 
 * @author Daedalus4096
 */
public class RecipeSerializersPM {
    private static final DeferredRegister<IRecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, PrimalMagic.MODID);
    
    public static void init() {
        RECIPE_SERIALIZERS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
    
    public static final RegistryObject<IRecipeSerializer<ShapelessArcaneRecipe>> ARCANE_CRAFTING_SHAPELESS = RECIPE_SERIALIZERS.register("arcane_crafting_shapeless", ShapelessArcaneRecipe.Serializer::new);
    public static final RegistryObject<IRecipeSerializer<ShapedArcaneRecipe>> ARCANE_CRAFTING_SHAPED = RECIPE_SERIALIZERS.register("arcane_crafting_shaped", ShapedArcaneRecipe.Serializer::new);
    public static final RegistryObject<SpecialRecipeSerializer<FlyingCarpetDyeRecipe>> FLYING_CARPET_DYE = RECIPE_SERIALIZERS.register("flying_carpet_dye_special", () -> new SpecialRecipeSerializer<>(FlyingCarpetDyeRecipe::new));
    public static final RegistryObject<IRecipeSerializer<RitualRecipe>> RITUAL = RECIPE_SERIALIZERS.register("ritual", RitualRecipe.Serializer::new);
    public static final RegistryObject<SpecialRecipeSerializer<WandAssemblyRecipe>> WAND_ASSEMBLY_SPECIAL = RECIPE_SERIALIZERS.register("wand_assembly_special", () -> new SpecialRecipeSerializer<>(WandAssemblyRecipe::new));
    public static final RegistryObject<SpecialRecipeSerializer<WandInscriptionRecipe>> WAND_INSCRIPTION_SPECIAL = RECIPE_SERIALIZERS.register("wand_inscription_special", () -> new SpecialRecipeSerializer<>(WandInscriptionRecipe::new));
    public static final RegistryObject<SpecialRecipeSerializer<SpellcraftingRecipe>> SPELLCRAFTING_SPECIAL = RECIPE_SERIALIZERS.register("spellcrafting_special", () -> new SpecialRecipeSerializer<>(SpellcraftingRecipe::new));
    public static final RegistryObject<IRecipeSerializer<RunecarvingRecipe>> RUNECARVING = RECIPE_SERIALIZERS.register("runecarving", RunecarvingRecipe.Serializer::new);
}
