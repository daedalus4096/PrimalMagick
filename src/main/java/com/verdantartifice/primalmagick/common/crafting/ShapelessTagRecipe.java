package com.verdantartifice.primalmagick.common.crafting;

import java.util.function.Predicate;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

/**
 * Definition for a shapeless arcane tag recipe.  Like a normal shapeless arcane recipe, except that
 * it outputs a tag rather than a specific item stack.
 * 
 * @author Daedalus4096
 * @see {@link net.minecraft.item.crafting.ShapelessRecipe}
 */
public class ShapelessTagRecipe extends AbstractTagCraftingRecipe<CraftingInput> implements IShapelessRecipePM<CraftingInput>, CraftingRecipe {
    protected final CraftingBookCategory category;
    protected final NonNullList<Ingredient> recipeItems;
    protected final boolean isSimple;
    
    public ShapelessTagRecipe(String group, CraftingBookCategory category, TagKey<Item> outputTag, int outputAmount, NonNullList<Ingredient> items) {
        super(group, outputTag, outputAmount);
        this.category = category;
        this.recipeItems = items;
        this.isSimple = items.stream().allMatch(Ingredient::isSimple);
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
        protected static final Codec<ShapelessTagRecipe> CODEC = RecordCodecBuilder.create(instance -> {
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
        public Codec<ShapelessTagRecipe> codec() {
            return CODEC;
        }

        @Override
        public ShapelessTagRecipe fromNetwork(FriendlyByteBuf pBuffer) {
            String group = pBuffer.readUtf(32767);
            CraftingBookCategory category = pBuffer.readEnum(CraftingBookCategory.class);
            
            int count = pBuffer.readVarInt();
            NonNullList<Ingredient> ingredients = NonNullList.withSize(count, Ingredient.EMPTY);
            for (int index = 0; index < ingredients.size(); index++) {
                ingredients.set(index, Ingredient.fromNetwork(pBuffer));
            }
            
            TagKey<Item> resultTag = TagKey.create(Registries.ITEM, pBuffer.readResourceLocation());
            int resultAmount = pBuffer.readVarInt();
            
            return new ShapelessTagRecipe(group, category, resultTag, resultAmount, ingredients);
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, ShapelessTagRecipe pRecipe) {
            pBuffer.writeUtf(pRecipe.group);
            pBuffer.writeEnum(pRecipe.category);
            pBuffer.writeVarInt(pRecipe.recipeItems.size());
            for (Ingredient ingredient : pRecipe.recipeItems) {
                ingredient.toNetwork(pBuffer);
            }
            pBuffer.writeResourceLocation(pRecipe.outputTag.location());
            pBuffer.writeVarInt(pRecipe.outputAmount);
        }
    }
}
