package com.verdantartifice.primalmagick.common.crafting;

import com.verdantartifice.primalmagick.common.items.essence.EssenceItem;
import com.verdantartifice.primalmagick.common.items.tools.ManaOrbItem;
import com.verdantartifice.primalmagick.common.tags.ItemTagsPM;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

/**
 * Special recipe to attune a mana orb to a source and allow it to store mana.
 *
 * @see com.verdantartifice.primalmagick.common.items.tools.ManaOrbItem
 * @author Daedalus4096
 */
public class AttuneManaOrbRecipe extends CustomRecipe {
    public static final ResourceKey<Recipe<?>> RECIPE_KEY = ResourceKey.create(Registries.RECIPE, ResourceUtils.loc("attune_mana_orb"));

    public AttuneManaOrbRecipe(CraftingBookCategory pCategory) {
        super(pCategory);
    }

    @Override
    public boolean matches(@NotNull CraftingInput craftingInput, @NotNull Level level) {
        ItemStack orbStack = ItemStack.EMPTY;
        ItemStack dustStack = ItemStack.EMPTY;

        if (craftingInput.ingredientCount() != 2) {
            return false;
        }
        for (int index = 0; index < craftingInput.size(); index++) {
            ItemStack slotStack = craftingInput.getItem(index);
            if (!slotStack.isEmpty()) {
                if (slotStack.getItem() instanceof ManaOrbItem) {
                    if (!orbStack.isEmpty()) {
                        return false;
                    }
                    orbStack = slotStack;
                } else if (slotStack.is(ItemTagsPM.ESSENCES_DUSTS)) {
                    if (!dustStack.isEmpty()) {
                        return false;
                    }
                    dustStack = slotStack;
                } else {
                    return false;
                }
            }
        }

        // Dust is not required, but an orb is
        return !orbStack.isEmpty();
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull CraftingInput craftingInput, @NotNull HolderLookup.Provider provider) {
        ItemStack orbStack = ItemStack.EMPTY;
        ItemStack dustStack = ItemStack.EMPTY;

        for (int index = 0; index < craftingInput.size(); index++) {
            ItemStack slotStack = craftingInput.getItem(index);
            if (!slotStack.isEmpty()) {
                if (slotStack.getItem() instanceof ManaOrbItem) {
                    if (!orbStack.isEmpty()) {
                        return ItemStack.EMPTY;
                    }
                    orbStack = slotStack;
                } else if (slotStack.is(ItemTagsPM.ESSENCES_DUSTS)) {
                    if (!dustStack.isEmpty()) {
                        return ItemStack.EMPTY;
                    }
                    dustStack = slotStack;
                } else {
                    return ItemStack.EMPTY;
                }
            }
        }

        return !orbStack.isEmpty() ? ManaOrbItem.attuneStorage(orbStack, dustStack.getItem() instanceof EssenceItem essence ? essence.getSource() : null) : ItemStack.EMPTY;
    }

    @Override
    public @NotNull RecipeSerializer<? extends CustomRecipe> getSerializer() {
        return RecipeSerializersPM.ATTUNE_MANA_ORB.get();
    }
}
