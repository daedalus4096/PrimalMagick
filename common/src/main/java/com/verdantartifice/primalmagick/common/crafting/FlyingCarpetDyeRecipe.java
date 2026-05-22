package com.verdantartifice.primalmagick.common.crafting;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.items.entities.FlyingCarpetItem;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.TransmuteRecipe;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

/**
 * Special definition for a recipe to dye a flying carpet.
 * 
 * @author Daedalus4096
 */
public class FlyingCarpetDyeRecipe extends CustomRecipe {
    public static final MapCodec<FlyingCarpetDyeRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Ingredient.CODEC.fieldOf("carpet").forGetter(o -> o.carpet),
            Ingredient.CODEC.fieldOf("dye").forGetter(o -> o.dye),
            ItemStackTemplate.CODEC.fieldOf("result").forGetter(o -> o.result)
        ).apply(instance, FlyingCarpetDyeRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, FlyingCarpetDyeRecipe> STREAM_CODEC = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC, o -> o.carpet,
            Ingredient.CONTENTS_STREAM_CODEC, o -> o.dye,
            ItemStackTemplate.STREAM_CODEC, o -> o.result,
            FlyingCarpetDyeRecipe::new);

    public static final RecipeSerializer<FlyingCarpetDyeRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);
    public static final ResourceKey<Recipe<?>> RECIPE_KEY = ResourceKey.create(Registries.RECIPE, ResourceUtils.loc("flying_carpet_dye"));

    private final Ingredient carpet;
    private final Ingredient dye;
    private final ItemStackTemplate result;

    public FlyingCarpetDyeRecipe(Ingredient carpet, Ingredient dye, ItemStackTemplate result) {
        this.carpet = carpet;
        this.dye = dye;
        this.result = result;
    }

    @Override
    public boolean matches(@NotNull CraftingInput inv, @NotNull Level worldIn) {
        if (inv.ingredientCount() != 2) {
            return false;
        } else {
            boolean hasCarpet = false;
            boolean hasDye = false;

            for (int index = 0; index < inv.size(); index++) {
                ItemStack slotStack = inv.getItem(index);
                if (!slotStack.isEmpty()) {
                    if (this.carpet.test(slotStack) && slotStack.getItem() instanceof FlyingCarpetItem) {
                        if (hasCarpet) {
                            return false;
                        } else {
                            hasCarpet = true;
                        }
                    } else {
                        if (!this.dye.test(slotStack) || hasDye || !slotStack.has(DataComponents.DYE)) {
                            return false;
                        } else {
                            hasDye = true;
                        }
                    }
                }
            }

            return hasCarpet && hasDye;
        }
    }

    @Override
    @NotNull
    public ItemStack assemble(@NotNull CraftingInput inv) {
        DyeColor targetColor = DyeColor.WHITE;
        ItemStack target = ItemStack.EMPTY;

        for (int index = 0; index < inv.size(); index++) {
            ItemStack slotStack = inv.getItem(index);
            if (!slotStack.isEmpty()) {
                if (this.carpet.test(slotStack) && slotStack.getItem() instanceof FlyingCarpetItem) {
                    target = slotStack;
                } else if (this.dye.test(slotStack) && slotStack.has(DataComponents.DYE)) {
                    targetColor = slotStack.getOrDefault(DataComponents.DYE, DyeColor.WHITE);
                }
            }
        }

        ItemStack result = TransmuteRecipe.createWithOriginalComponents(this.result, target);
        result.set(DataComponents.DYED_COLOR, new DyedItemColor(targetColor.getFireworkColor()));
        return result;
    }

    @Override
    @NotNull
    public RecipeSerializer<? extends CustomRecipe> getSerializer() {
        return SERIALIZER;
    }
}
