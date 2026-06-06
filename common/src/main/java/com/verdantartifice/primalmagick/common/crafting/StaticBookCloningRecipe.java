package com.verdantartifice.primalmagick.common.crafting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.items.books.StaticBookItem;
import com.verdantartifice.primalmagick.common.tags.ItemTagsPM;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.BookCloningRecipe;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.TransmuteRecipe;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

/**
 * Special recipe for cloning a static book using writable books.
 * 
 * @author Daedalus4096
 */
public class StaticBookCloningRecipe extends CustomRecipe {
    public static final MinMaxBounds.Ints ALLOWED_BOOK_GENERATION_RANGES = BookCloningRecipe.ALLOWED_BOOK_GENERATION_RANGES;
    public static final MinMaxBounds.Ints DEFAULT_BOOK_GENERATION_RANGES = BookCloningRecipe.DEFAULT_BOOK_GENERATION_RANGES;

    private static final Codec<MinMaxBounds.Ints> ALLOWED_GENERATION_CODEC = MinMaxBounds.Ints.CODEC.validate(MinMaxBounds.validateContainedInRange(ALLOWED_BOOK_GENERATION_RANGES));
    public static final MapCodec<StaticBookCloningRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Ingredient.CODEC.fieldOf("source").forGetter(o -> o.source),
            Ingredient.CODEC.fieldOf("material").forGetter(o -> o.material),
            ALLOWED_GENERATION_CODEC.optionalFieldOf("allowedGenerations", DEFAULT_BOOK_GENERATION_RANGES).forGetter(o -> o.allowedGenerations),
            ItemStackTemplate.CODEC.fieldOf("result").forGetter(o -> o.result)
        ).apply(instance, StaticBookCloningRecipe::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, StaticBookCloningRecipe> STREAM_CODEC = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC, o -> o.source,
            Ingredient.CONTENTS_STREAM_CODEC, o -> o.material,
            MinMaxBounds.Ints.STREAM_CODEC, o -> o.allowedGenerations,
            ItemStackTemplate.STREAM_CODEC, o -> o.result,
            StaticBookCloningRecipe::new);
    public static final RecipeSerializer<StaticBookCloningRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);

    public static final ResourceKey<Recipe<?>> RECIPE_KEY_COMMON = ResourceKey.create(Registries.RECIPE, ResourceUtils.loc("static_book_cloning_common"));
    public static final ResourceKey<Recipe<?>> RECIPE_KEY_UNCOMMON = ResourceKey.create(Registries.RECIPE, ResourceUtils.loc("static_book_cloning_uncommon"));
    public static final ResourceKey<Recipe<?>> RECIPE_KEY_RARE = ResourceKey.create(Registries.RECIPE, ResourceUtils.loc("static_book_cloning_rare"));
    public static final ResourceKey<Recipe<?>> RECIPE_KEY_TABLET = ResourceKey.create(Registries.RECIPE, ResourceUtils.loc("static_book_cloning_tablet"));

    private final Ingredient source;
    private final Ingredient material;
    private final MinMaxBounds.Ints allowedGenerations;
    private final ItemStackTemplate result;

    public StaticBookCloningRecipe(Ingredient source, Ingredient material, MinMaxBounds.Ints allowedGenerations, ItemStackTemplate result) {
        this.source = source;
        this.material = material;
        this.allowedGenerations = allowedGenerations;
        this.result = result;
    }

    private boolean canCraftCopy(ItemStack bookStack) {
        return StaticBookItem.getBookId(bookStack).isPresent() && this.allowedGenerations.matches(StaticBookItem.getGeneration(bookStack));
    }

    @Override
    public boolean matches(@NotNull CraftingInput pContainer, @NotNull Level pLevel) {
        if (pContainer.ingredientCount() < 2) {
            return false;
        } else {
            boolean hasSource = false;
            boolean hasMaterial = false;

            for (int index = 0; index < pContainer.size(); index++) {
                ItemStack slotStack = pContainer.getItem(index);
                if (!slotStack.isEmpty()) {
                    if (this.source.test(slotStack) && slotStack.is(ItemTagsPM.STATIC_BOOKS)) {
                        if (hasSource) {
                            return false;
                        }
                        if (!this.canCraftCopy(slotStack)) {
                            return false;
                        }
                        hasSource = true;
                    } else if (this.material.test(slotStack)) {
                        hasMaterial = true;
                    } else {
                        return false;
                    }
                }
            }

            return hasSource && hasMaterial;
        }
    }

    @Override
    @NotNull
    public ItemStack assemble(@NotNull CraftingInput pContainer) {
        int count = 0;
        ItemStack originalStack = ItemStack.EMPTY;
        
        for (int index = 0; index < pContainer.size(); index++) {
            ItemStack slotStack = pContainer.getItem(index);
            if (!slotStack.isEmpty()) {
                if (this.source.test(slotStack) && slotStack.is(ItemTagsPM.STATIC_BOOKS)) {
                    if (!originalStack.isEmpty()) {
                        return ItemStack.EMPTY;
                    }
                    originalStack = slotStack;
                } else if (this.material.test(slotStack)) {
                    count++;
                } else {
                    return ItemStack.EMPTY;
                }
            }
        }

        // FIXME Use consolidated data component for static book contents
        if (!originalStack.isEmpty() && count > 0 && this.canCraftCopy(originalStack)) {
            ItemStack copyStack = TransmuteRecipe.createWithOriginalComponents(this.result, originalStack, count - 1);
            StaticBookItem.setGeneration(copyStack, StaticBookItem.getGeneration(originalStack) + 1);
            return copyStack;
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
            } else if (inputStack.getItem() instanceof StaticBookItem) {
                retVal.set(index, inputStack.copyWithCount(1));
                break;
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
