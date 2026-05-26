package com.verdantartifice.primalmagick.common.crafting;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

/**
 * Special definition for a spellcrafting recipe.
 * 
 * @author Daedalus4096
 */
public class SpellcraftingRecipe extends CustomRecipe {
    public static final MapCodec<SpellcraftingRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Ingredient.CODEC.fieldOf("ingredient").forGetter(o -> o.ingredient),
            ItemStackTemplate.CODEC.fieldOf("result").forGetter(o -> o.result)
    ).apply(instance, SpellcraftingRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, SpellcraftingRecipe> STREAM_CODEC = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC, o -> o.ingredient,
            ItemStackTemplate.STREAM_CODEC, o -> o.result,
            SpellcraftingRecipe::new);

    public static final RecipeSerializer<SpellcraftingRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);
    public static final ResourceKey<Recipe<?>> RECIPE_KEY = ResourceKey.create(Registries.RECIPE, ResourceUtils.loc("spellcrafting"));
    
    private final Ingredient ingredient;
    private final ItemStackTemplate result;
    
    public SpellcraftingRecipe(Ingredient ingredient, ItemStackTemplate result) {
        this.ingredient = ingredient;
        this.result = result;
    }

    @Override
    public boolean matches(@NotNull CraftingInput inv, @NotNull Level worldIn) {
        return this.ingredient.test(inv.getItem(0));
    }

    @Override
    @NotNull
    public ItemStack assemble(@NotNull CraftingInput inv) {
        return this.result.create();
    }

    @Override
    @NotNull
    public RecipeSerializer<? extends CustomRecipe> getSerializer() {
        return SERIALIZER;
    }
}
