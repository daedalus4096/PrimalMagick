package com.verdantartifice.primalmagick.common.crafting;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.items.essence.EssenceItem;
import com.verdantartifice.primalmagick.common.items.tools.ManaOrbItem;
import com.verdantartifice.primalmagick.common.tags.ItemTagsPM;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.TransmuteRecipe;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

/**
 * Special recipe to attune a mana orb to a source and allow it to store mana.
 *
 * @see com.verdantartifice.primalmagick.common.items.tools.ManaOrbItem
 * @author Daedalus4096
 */
public class AttuneManaOrbRecipe extends CustomRecipe {
    public static final MapCodec<AttuneManaOrbRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Ingredient.CODEC.fieldOf("orb").forGetter(o -> o.orb),
            Ingredient.CODEC.fieldOf("dust").forGetter(o -> o.dust),
            ItemStackTemplate.CODEC.fieldOf("result").forGetter(o -> o.result)
        ).apply(instance, AttuneManaOrbRecipe::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, AttuneManaOrbRecipe> STREAM_CODEC = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC, o -> o.orb,
            Ingredient.CONTENTS_STREAM_CODEC, o -> o.dust,
            ItemStackTemplate.STREAM_CODEC, o -> o.result,
            AttuneManaOrbRecipe::new);
    public static final RecipeSerializer<AttuneManaOrbRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);

    public static final ResourceKey<Recipe<?>> RECIPE_KEY_APPRENTICE = ResourceKey.create(Registries.RECIPE, ResourceUtils.loc("attune_mana_orb_apprentice"));
    public static final ResourceKey<Recipe<?>> RECIPE_KEY_ADEPT = ResourceKey.create(Registries.RECIPE, ResourceUtils.loc("attune_mana_orb_adept"));
    public static final ResourceKey<Recipe<?>> RECIPE_KEY_WIZARD = ResourceKey.create(Registries.RECIPE, ResourceUtils.loc("attune_mana_orb_wizard"));
    public static final ResourceKey<Recipe<?>> RECIPE_KEY_ARCHMAGE = ResourceKey.create(Registries.RECIPE, ResourceUtils.loc("attune_mana_orb_archmage"));

    private final Ingredient orb;
    private final Ingredient dust;
    private final ItemStackTemplate result;

    public AttuneManaOrbRecipe(Ingredient orb, Ingredient dust, ItemStackTemplate result) {
        this.orb = orb;
        this.dust = dust;
        this.result = result;
    }

    @Override
    public boolean matches(@NotNull CraftingInput craftingInput, @NotNull Level level) {
        boolean hasOrb = false;
        boolean hasDust = false;

        if (craftingInput.ingredientCount() != 2) {
            return false;
        }
        for (int index = 0; index < craftingInput.size(); index++) {
            ItemStack slotStack = craftingInput.getItem(index);
            if (!slotStack.isEmpty()) {
                if (this.orb.test(slotStack) && slotStack.getItem() instanceof ManaOrbItem) {
                    if (hasOrb) {
                        return false;
                    }
                    hasOrb = true;
                } else if (this.dust.test(slotStack)) {
                    if (hasDust) {
                        return false;
                    }
                    hasDust = true;
                } else {
                    return false;
                }
            }
        }

        // Dust is not required, but an orb is
        return hasOrb;
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull CraftingInput craftingInput) {
        ItemStack orbStack = ItemStack.EMPTY;
        ItemStack dustStack = ItemStack.EMPTY;

        for (int index = 0; index < craftingInput.size(); index++) {
            ItemStack slotStack = craftingInput.getItem(index);
            if (!slotStack.isEmpty()) {
                if (this.orb.test(slotStack) && slotStack.getItem() instanceof ManaOrbItem) {
                    if (!orbStack.isEmpty()) {
                        return ItemStack.EMPTY;
                    }
                    orbStack = slotStack;
                } else if (this.dust.test(slotStack)) {
                    if (!dustStack.isEmpty()) {
                        return ItemStack.EMPTY;
                    }
                    dustStack = slotStack;
                } else {
                    return ItemStack.EMPTY;
                }
            }
        }

        if (!orbStack.isEmpty()) {
            ItemStack result = TransmuteRecipe.createWithOriginalComponents(this.result, orbStack);
            return ManaOrbItem.attuneStorage(result, dustStack.getItem() instanceof EssenceItem essence ? essence.getSource() : null);
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public @NotNull RecipeSerializer<? extends CustomRecipe> getSerializer() {
        return SERIALIZER;
    }
}
