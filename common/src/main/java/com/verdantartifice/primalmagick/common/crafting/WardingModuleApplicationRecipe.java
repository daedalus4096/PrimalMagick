package com.verdantartifice.primalmagick.common.crafting;

import com.mojang.serialization.MapCodec;
import com.verdantartifice.primalmagick.common.items.armor.WardingModuleItem;
import com.verdantartifice.primalmagick.common.tags.ItemTagsPM;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

/**
 * Special recipe for applying a warding module to a piece of armor.
 * 
 * @author Daedalus4096
 */
public class WardingModuleApplicationRecipe extends CustomRecipe {
    public static final WardingModuleApplicationRecipe INSTANCE = new WardingModuleApplicationRecipe();
    public static final MapCodec<WardingModuleApplicationRecipe> MAP_CODEC = MapCodec.unit(INSTANCE);
    public static final StreamCodec<RegistryFriendlyByteBuf, WardingModuleApplicationRecipe> STREAM_CODEC = StreamCodec.unit(INSTANCE);
    public static final RecipeSerializer<WardingModuleApplicationRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);
    public static final ResourceKey<Recipe<?>> RECIPE_KEY = ResourceKey.create(Registries.RECIPE, ResourceUtils.loc("warding_module_application"));

    @Override
    public boolean matches(@NotNull CraftingInput pContainer, @NotNull Level pLevel) {
        if (pContainer.ingredientCount() != 2) {
            return false;
        }

        ItemStack moduleStack = ItemStack.EMPTY;
        ItemStack armorStack = ItemStack.EMPTY;
        
        for (int index = 0; index < pContainer.size(); index++) {
            ItemStack stack = pContainer.getItem(index);
            if (!stack.isEmpty()) {
                if (stack.getItem() instanceof WardingModuleItem) {
                    if (!moduleStack.isEmpty()) {
                        return false;
                    }
                    moduleStack = stack;
                } else if (stack.is(ItemTagsPM.WARDABLE_ARMOR)) {
                    if (!armorStack.isEmpty()) {
                        return false;
                    }
                    armorStack = stack;
                } else {
                    return false;
                }
            }
        }
        
        return !moduleStack.isEmpty() && !armorStack.isEmpty();
    }

    @Override
    @NotNull
    public ItemStack assemble(@NotNull CraftingInput pContainer) {
        ItemStack moduleStack = ItemStack.EMPTY;
        ItemStack armorStack = ItemStack.EMPTY;
        
        for (int index = 0; index < pContainer.size(); index++) {
            ItemStack stack = pContainer.getItem(index);
            if (!stack.isEmpty()) {
                if (stack.getItem() instanceof WardingModuleItem) {
                    moduleStack = stack;
                } else if (stack.is(ItemTagsPM.WARDABLE_ARMOR)) {
                    armorStack = stack.copy();
                }
            }
        }
        
        if (!armorStack.isEmpty() && moduleStack.getItem() instanceof WardingModuleItem module) {
            return module.applyWard(armorStack);
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    @NotNull
    public RecipeSerializer<? extends CustomRecipe> getSerializer() {
        return SERIALIZER;
    }
}
