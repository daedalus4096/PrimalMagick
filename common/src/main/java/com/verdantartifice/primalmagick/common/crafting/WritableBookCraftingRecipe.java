package com.verdantartifice.primalmagick.common.crafting;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.theorycrafting.IWritingImplement;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

/**
 * Special recipe for creating a writable book using a blank book and a mod writing implement.
 * 
 * @author Daedalus4096
 */
public class WritableBookCraftingRecipe extends CustomRecipe {
    public static final MapCodec<WritableBookCraftingRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Ingredient.CODEC.fieldOf("book").forGetter(o -> o.book),
            Ingredient.CODEC.fieldOf("pen").forGetter(o -> o.pen),
            ItemStackTemplate.CODEC.fieldOf("result").forGetter(o -> o.result)
        ).apply(instance, WritableBookCraftingRecipe::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, WritableBookCraftingRecipe> STREAM_CODEC = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC, o -> o.book,
            Ingredient.CONTENTS_STREAM_CODEC, o -> o.pen,
            ItemStackTemplate.STREAM_CODEC, o -> o.result,
            WritableBookCraftingRecipe::new);
    public static final RecipeSerializer<WritableBookCraftingRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);

    public static final ResourceKey<Recipe<?>> RECIPE_KEY = ResourceKey.create(Registries.RECIPE, ResourceUtils.loc("writable_book_crafting"));

    private final Ingredient book;
    private final Ingredient pen;
    private final ItemStackTemplate result;

    public WritableBookCraftingRecipe(Ingredient book, Ingredient pen, ItemStackTemplate result) {
        this.book = book;
        this.pen = pen;
        this.result = result;
    }

    @Override
    public boolean matches(@NotNull CraftingInput pContainer, @NotNull Level pLevel) {
        if (pContainer.ingredientCount() != 2) {
            return false;
        }

        boolean hasPen = false;
        boolean hasBook = false;

        for (int index = 0; index < pContainer.size(); index++) {
            ItemStack stack = pContainer.getItem(index);
            if (!stack.isEmpty()) {
                if (this.pen.test(stack) && stack.getItem() instanceof IWritingImplement) {
                    if (hasPen) {
                        return false;
                    }
                    hasPen = true;
                } else if (this.book.test(stack)) {
                    if (hasBook) {
                        return false;
                    }
                    hasBook = true;
                } else {
                    return false;
                }
            }
        }
        
        return hasPen && hasBook;
    }

    @Override
    @NotNull
    public ItemStack assemble(@NotNull CraftingInput pContainer) {
        boolean hasPen = false;
        boolean hasBook = false;
        
        for (int index = 0; index < pContainer.size(); index++) {
            ItemStack stack = pContainer.getItem(index);
            if (!stack.isEmpty()) {
                if (this.pen.test(stack) && stack.getItem() instanceof IWritingImplement) {
                    hasPen = true;
                } else if (this.book.test(stack)) {
                    hasBook = true;
                }
            }
        }
        
        if (hasPen && hasBook) {
            return this.result.create();
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    @NotNull
    public NonNullList<ItemStack> getRemainingItems(@NotNull CraftingInput pContainer) {
        NonNullList<ItemStack> retVal = NonNullList.withSize(pContainer.size(), ItemStack.EMPTY);
        
        for (int index = 0; index < retVal.size(); index++) {
            ItemStack inputStack = pContainer.getItem(index);
            ItemStackTemplate remainderStack = inputStack.getItem().getCraftingRemainder();
            if (remainderStack != null) {
                retVal.set(index, remainderStack.create());
            } else if (inputStack.getItem() instanceof IWritingImplement oldPen) {
                ItemStack leftoverStack = inputStack.copyWithCount(1);
                if (oldPen.isDamagedOnUse()) {
                    int newDamage = leftoverStack.getDamageValue() + 1;
                    if (newDamage >= leftoverStack.getMaxDamage()) {
                        leftoverStack = ItemStack.EMPTY;
                    } else {
                        leftoverStack.setDamageValue(newDamage);
                    }
                }
                retVal.set(index, leftoverStack);
            }
        }
        
        return retVal;
    }

    @Override
    @NotNull
    public RecipeSerializer<? extends CustomRecipe> getSerializer() {
        return SERIALIZER;
    }
}
