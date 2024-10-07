package com.verdantartifice.primalmagick.common.crafting;

import com.verdantartifice.primalmagick.common.registries.IRegistryItem;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;

import java.util.function.Supplier;

/**
 * Deferred registry for mod recipe serializers.
 * 
 * @author Daedalus4096
 */
public class RecipeSerializersPM {
    public static final IRegistryItem<RecipeSerializer<?>, RecipeSerializer<ShapelessArcaneRecipe>> ARCANE_CRAFTING_SHAPELESS = register("arcane_crafting_shapeless", ShapelessArcaneRecipe.Serializer::new);
    public static final IRegistryItem<RecipeSerializer<?>, RecipeSerializer<ShapedArcaneRecipe>> ARCANE_CRAFTING_SHAPED = register("arcane_crafting_shaped", ShapedArcaneRecipe.Serializer::new);
    public static final IRegistryItem<RecipeSerializer<?>, SimpleCraftingRecipeSerializer<FlyingCarpetDyeRecipe>> FLYING_CARPET_DYE = register("flying_carpet_dye_special", () -> new SimpleCraftingRecipeSerializer<>(FlyingCarpetDyeRecipe::new));
    public static final IRegistryItem<RecipeSerializer<?>, RecipeSerializer<RitualRecipe>> RITUAL = register("ritual", RitualRecipe.Serializer::new);
    public static final IRegistryItem<RecipeSerializer<?>, SimpleCraftingRecipeSerializer<WandAssemblyRecipe>> WAND_ASSEMBLY_SPECIAL = register("wand_assembly_special", () -> new SimpleCraftingRecipeSerializer<>(WandAssemblyRecipe::new));
    public static final IRegistryItem<RecipeSerializer<?>, SimpleCraftingRecipeSerializer<WandInscriptionRecipe>> WAND_INSCRIPTION_SPECIAL = register("wand_inscription_special", () -> new SimpleCraftingRecipeSerializer<>(WandInscriptionRecipe::new));
    public static final IRegistryItem<RecipeSerializer<?>, SimpleCraftingRecipeSerializer<SpellcraftingRecipe>> SPELLCRAFTING_SPECIAL = register("spellcrafting_special", () -> new SimpleCraftingRecipeSerializer<>(SpellcraftingRecipe::new));
    public static final IRegistryItem<RecipeSerializer<?>, RecipeSerializer<RunecarvingRecipe>> RUNECARVING = register("runecarving", RunecarvingRecipe.Serializer::new);
    public static final IRegistryItem<RecipeSerializer<?>, RecipeSerializer<ConcoctingRecipe>> CONCOCTING = register("concocting", ConcoctingRecipe.Serializer::new);
    public static final IRegistryItem<RecipeSerializer<?>, SimpleCraftingRecipeSerializer<TieredShieldDecorationRecipe>> TIERED_SHIELD_DECORATION = register("tiered_shield_decoration", () -> new SimpleCraftingRecipeSerializer<>(TieredShieldDecorationRecipe::new));
    public static final IRegistryItem<RecipeSerializer<?>, RecipeSerializer<DissolutionRecipe>> DISSOLUTION = register("dissolution", DissolutionRecipe.Serializer::new);
    public static final IRegistryItem<RecipeSerializer<?>, SimpleCraftingRecipeSerializer<WandGlamourRecipe>> WAND_GLAMOUR_SPECIAL = register("wand_glamour_special", () -> new SimpleCraftingRecipeSerializer<>(WandGlamourRecipe::new));
    public static final IRegistryItem<RecipeSerializer<?>, SimpleCraftingRecipeSerializer<WardingModuleApplicationRecipe>> WARDING_MODULE_APPLICATION = register("warding_module_application", () -> new SimpleCraftingRecipeSerializer<>(WardingModuleApplicationRecipe::new));
    public static final IRegistryItem<RecipeSerializer<?>, SimpleCraftingRecipeSerializer<StaticBookCloningRecipe>> STATIC_BOOK_CLONING = register("static_book_cloning", () -> new SimpleCraftingRecipeSerializer<>(StaticBookCloningRecipe::new));
    public static final IRegistryItem<RecipeSerializer<?>, SimpleCraftingRecipeSerializer<WritableBookCraftingRecipe>> WRITABLE_BOOK_CRAFTING = register("writable_book_crafting", () -> new SimpleCraftingRecipeSerializer<>(WritableBookCraftingRecipe::new));
    public static final IRegistryItem<RecipeSerializer<?>, RecipeSerializer<ShapelessTagRecipe>> CRAFTING_SHAPELESS_TAG = register("crafting_shapeless_tag", ShapelessTagRecipe.Serializer::new);
    public static final IRegistryItem<RecipeSerializer<?>, RecipeSerializer<ShapelessArcaneTagRecipe>> ARCANE_CRAFTING_SHAPELESS_TAG = register("arcane_crafting_shapeless_tag", ShapelessArcaneTagRecipe.Serializer::new);
    public static final IRegistryItem<RecipeSerializer<?>, RecipeSerializer<DissolutionTagRecipe>> DISSOLUTION_TAG = register("dissolution_tag", DissolutionTagRecipe.Serializer::new);

    private static <T extends Recipe<?>, S extends RecipeSerializer<T>> IRegistryItem<RecipeSerializer<?>, S> register(String name, Supplier<S> supplier) {
        return Services.RECIPE_SERIALIZERS.register(name, supplier);
    }
}
