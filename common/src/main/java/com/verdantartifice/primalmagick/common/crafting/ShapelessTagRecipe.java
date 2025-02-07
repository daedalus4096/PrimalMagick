package com.verdantartifice.primalmagick.common.crafting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.util.StreamCodecUtils;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

import java.util.function.Predicate;

/**
 * Definition for a shapeless arcane tag recipe.  Like a normal shapeless arcane recipe, except that
 * it outputs a tag rather than a specific item stack.
 * 
 * @author Daedalus4096
 */
public class ShapelessTagRecipe extends AbstractTagCraftingRecipe<CraftingInput> implements IShapelessRecipePM<CraftingInput>, CraftingRecipe {
    protected final CraftingBookCategory category;
    protected final NonNullList<Ingredient> recipeItems;
    protected final boolean isSimple;
    
    public ShapelessTagRecipe(String group, CraftingBookCategory category, TagKey<Item> outputTag, int outputAmount, NonNullList<Ingredient> items) {
        super(group, outputTag, outputAmount);
        this.category = category;
        this.recipeItems = items;
        this.isSimple = items.stream().allMatch(Services.INGREDIENTS::isSimple);
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return this.recipeItems;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializersPM.CRAFTING_SHAPELESS_TAG.get();
    }

    @Override
    public CraftingBookCategory category() {
        return this.category;
    }
    
    @Override
    public boolean isSimple() {
        return this.isSimple;
    }

    public static class Serializer implements RecipeSerializer<ShapelessTagRecipe> {
        protected static final MapCodec<ShapelessTagRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> {
            return instance.group(
                    Codec.STRING.optionalFieldOf("group", "").forGetter(sar -> sar.group),
                    CraftingBookCategory.CODEC.fieldOf("category").orElse(CraftingBookCategory.MISC).forGetter(sar -> sar.category),
                    TagKey.codec(Registries.ITEM).fieldOf("outputTag").forGetter(sar -> sar.outputTag),
                    Codec.INT.fieldOf("outputAmount").forGetter(sar -> sar.outputAmount),
                    Ingredient.CODEC_NONEMPTY.listOf().fieldOf("ingredients").flatXmap(ingredients -> {
                        Ingredient[] ingArray = ingredients.stream().filter(Predicate.not(Ingredient::isEmpty)).toArray(Ingredient[]::new);
                        if (ingArray.length == 0) {
                            return DataResult.error(() -> "No ingredients for shapeless arcane recipe");
                        } else if (ingArray.length > ShapedArcaneRecipe.MAX_WIDTH * ShapedArcaneRecipe.MAX_HEIGHT) {
                            return DataResult.error(() -> "Too many ingredients for shapeless arcane recipe");
                        } else {
                            return DataResult.success(NonNullList.of(Ingredient.EMPTY, ingArray));
                        }
                    }, DataResult::success).forGetter(sar -> sar.recipeItems)
                ).apply(instance, ShapelessTagRecipe::new);
        });
        
        @Override
        public MapCodec<ShapelessTagRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, ShapelessTagRecipe> streamCodec() {
            return StreamCodec.of(ShapelessTagRecipe.Serializer::toNetwork, ShapelessTagRecipe.Serializer::fromNetwork);
        }

        private static ShapelessTagRecipe fromNetwork(RegistryFriendlyByteBuf pBuffer) {
            String group = pBuffer.readUtf();
            CraftingBookCategory category = pBuffer.readEnum(CraftingBookCategory.class);
            
            int count = pBuffer.readVarInt();
            NonNullList<Ingredient> ingredients = NonNullList.withSize(count, Ingredient.EMPTY);
            ingredients.replaceAll(ing -> Ingredient.CONTENTS_STREAM_CODEC.decode(pBuffer));
            
            TagKey<Item> resultTag = StreamCodecUtils.tagKey(Registries.ITEM).decode(pBuffer);
            int resultAmount = pBuffer.readVarInt();
            
            return new ShapelessTagRecipe(group, category, resultTag, resultAmount, ingredients);
        }

        private static void toNetwork(RegistryFriendlyByteBuf pBuffer, ShapelessTagRecipe pRecipe) {
            pBuffer.writeUtf(pRecipe.group);
            pBuffer.writeEnum(pRecipe.category);
            pBuffer.writeVarInt(pRecipe.recipeItems.size());
            for (Ingredient ingredient : pRecipe.recipeItems) {
                Ingredient.CONTENTS_STREAM_CODEC.encode(pBuffer, ingredient);
            }
            StreamCodecUtils.tagKey(Registries.ITEM).encode(pBuffer, pRecipe.outputTag);
            pBuffer.writeVarInt(pRecipe.outputAmount);
        }
    }
}
