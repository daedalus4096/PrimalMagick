package com.verdantartifice.primalmagick.common.crafting;

import com.verdantartifice.primalmagick.common.registries.IRegistryItem;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;

import java.util.function.Supplier;

/**
 * Deferred registry for mod recipe serializers.
 * 
 * @author Daedalus4096
 */
public class RecipeSerializersPM {
    public static void init() {
        // Pass the service initialization through this class so it gets class loaded and fields registered
        Services.RECIPE_SERIALIZERS_REGISTRY.init();
    }

    public static final IRegistryItem<RecipeSerializer<?>, RecipeSerializer<ShapedArcaneRecipe>> SHAPED_ARCANE = register("shaped_arcane", () -> ShapedArcaneRecipe.SERIALIZER);
    public static final IRegistryItem<RecipeSerializer<?>, RecipeSerializer<ShapelessArcaneRecipe>> SHAPELESS_ARCANE = register("shapeless_arcane", () -> ShapelessArcaneRecipe.SERIALIZER);
    public static final IRegistryItem<RecipeSerializer<?>, RecipeSerializer<ShapelessArcaneTagRecipe>> SHAPELESS_ARCANE_TAG = register("shapeless_arcane_tag", () -> ShapelessArcaneTagRecipe.SERIALIZER);
    public static final IRegistryItem<RecipeSerializer<?>, RecipeSerializer<ShapelessTagRecipe>> SHAPELESS_TAG = register("shapeless_tag", () -> ShapelessTagRecipe.SERIALIZER);
    public static final IRegistryItem<RecipeSerializer<?>, RecipeSerializer<ConcoctingRecipe>> CONCOCTING = register("concocting", () -> ConcoctingRecipe.SERIALIZER);
    public static final IRegistryItem<RecipeSerializer<?>, RecipeSerializer<DissolutionRecipe>> DISSOLUTION = register("dissolution", () -> DissolutionRecipe.SERIALIZER);
    public static final IRegistryItem<RecipeSerializer<?>, RecipeSerializer<DissolutionTagRecipe>> DISSOLUTION_TAG = register("dissolution_tag", () -> DissolutionTagRecipe.SERIALIZER);
    public static final IRegistryItem<RecipeSerializer<?>, RecipeSerializer<RunecarvingRecipe>> RUNECARVING = register("runecarving", () -> RunecarvingRecipe.SERIALIZER);
    public static final IRegistryItem<RecipeSerializer<?>, RecipeSerializer<RitualRecipe>> RITUAL = register("ritual", () -> RitualRecipe.SERIALIZER);

    public static final IRegistryItem<RecipeSerializer<?>, RecipeSerializer<FlyingCarpetDyeRecipe>> FLYING_CARPET_DYE = register("flying_carpet_dye_special", () -> FlyingCarpetDyeRecipe.SERIALIZER);
    public static final IRegistryItem<RecipeSerializer<?>, RecipeSerializer<WandAssemblyRecipe>> WAND_ASSEMBLY_SPECIAL = register("wand_assembly_special", () -> WandAssemblyRecipe.SERIALIZER);
    public static final IRegistryItem<RecipeSerializer<?>, RecipeSerializer<WandInscriptionRecipe>> WAND_INSCRIPTION_SPECIAL = register("wand_inscription_special", () -> WandInscriptionRecipe.SERIALIZER);
    public static final IRegistryItem<RecipeSerializer<?>, RecipeSerializer<SpellcraftingRecipe>> SPELLCRAFTING_SPECIAL = register("spellcrafting_special", () -> SpellcraftingRecipe.SERIALIZER);
    public static final IRegistryItem<RecipeSerializer<?>, RecipeSerializer<WandGlamourRecipe>> WAND_GLAMOUR_SPECIAL = register("wand_glamour_special", () -> WandGlamourRecipe.SERIALIZER);
    public static final IRegistryItem<RecipeSerializer<?>, RecipeSerializer<WardingModuleApplicationRecipe>> WARDING_MODULE_APPLICATION = register("warding_module_application", () -> WardingModuleApplicationRecipe.SERIALIZER);
    public static final IRegistryItem<RecipeSerializer<?>, RecipeSerializer<StaticBookCloningRecipe>> STATIC_BOOK_CLONING = register("static_book_cloning", () -> StaticBookCloningRecipe.SERIALIZER);
    public static final IRegistryItem<RecipeSerializer<?>, RecipeSerializer<WritableBookCraftingRecipe>> WRITABLE_BOOK_CRAFTING = register("writable_book_crafting", () -> WritableBookCraftingRecipe.SERIALIZER);
    public static final IRegistryItem<RecipeSerializer<?>, RecipeSerializer<AttuneManaOrbRecipe>> ATTUNE_MANA_ORB = register("attune_mana_orb_special", () -> AttuneManaOrbRecipe.SERIALIZER);

    private static <T extends Recipe<?>, S extends RecipeSerializer<T>> IRegistryItem<RecipeSerializer<?>, S> register(String name, Supplier<S> supplier) {
        return Services.RECIPE_SERIALIZERS_REGISTRY.register(name, supplier);
    }
}
