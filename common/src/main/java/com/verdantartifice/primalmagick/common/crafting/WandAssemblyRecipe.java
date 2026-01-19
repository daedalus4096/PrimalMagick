package com.verdantartifice.primalmagick.common.crafting;

import com.verdantartifice.primalmagick.common.capabilities.ManaStorage;
import com.verdantartifice.primalmagick.common.components.DataComponentsPM;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.items.wands.IHasWandComponents;
import com.verdantartifice.primalmagick.common.items.wands.StaffCoreItem;
import com.verdantartifice.primalmagick.common.items.wands.WandCapItem;
import com.verdantartifice.primalmagick.common.items.wands.WandCoreItem;
import com.verdantartifice.primalmagick.common.items.wands.WandGemItem;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import com.verdantartifice.primalmagick.common.wands.WandGem;
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
 * Special definition for a wand assembly recipe.
 * 
 * @author Daedalus4096
 */
public class WandAssemblyRecipe extends CustomRecipe {
    public static final ResourceKey<Recipe<?>> RECIPE_KEY = ResourceKey.create(Registries.RECIPE, ResourceUtils.loc("wand_assembly"));

    public WandAssemblyRecipe(CraftingBookCategory category) {
        super(category);
    }

    private static ItemStack getItem(@NotNull CraftingInput inv, int index) {
        return (index >= 0 && index < inv.size()) ? inv.getItem(index) : ItemStack.EMPTY;
    }

    @Override
    public boolean matches(@NotNull CraftingInput inv, @NotNull Level worldIn) {
        if (inv.ingredientCount() != 4) {
            return false;
        }

        ItemStack coreStack = getItem(inv, 0);
        ItemStack gemStack = getItem(inv, 1);
        ItemStack capStack1 = getItem(inv, 2);
        ItemStack capStack2 = getItem(inv, 3);
        
        // Make sure the crafting inventory has a core, a gem, and two identical caps
        return !coreStack.isEmpty() && ((coreStack.getItem() instanceof WandCoreItem) || (coreStack.getItem() instanceof StaffCoreItem)) &&
               !gemStack.isEmpty() && (gemStack.getItem() instanceof WandGemItem) &&
               !capStack1.isEmpty() && (capStack1.getItem() instanceof WandCapItem) &&
               !capStack2.isEmpty() && ItemStack.isSameItem(capStack1, capStack2);
    }

    @Override
    @NotNull
    public ItemStack assemble(@NotNull CraftingInput inv, @NotNull HolderLookup.Provider registries) {
        ItemStack coreStack = getItem(inv, 0);
        ItemStack gemStack = getItem(inv, 1);
        ItemStack capStack = getItem(inv, 2);
        
        ItemStack outputStack = (coreStack.getItem() instanceof StaffCoreItem) ? 
                new ItemStack(ItemsPM.MODULAR_STAFF.get()) :
                new ItemStack(ItemsPM.MODULAR_WAND.get());
        IHasWandComponents outputItem = (IHasWandComponents)outputStack.getItem();
        
        // Set the components of the modular wand/staff
        if (coreStack.getItem() instanceof StaffCoreItem) {
            outputItem.setWandCore(outputStack, ((StaffCoreItem)coreStack.getItem()).getWandCore());
        } else {
            outputItem.setWandCore(outputStack, ((WandCoreItem)coreStack.getItem()).getWandCore());
        }
        outputItem.setWandCap(outputStack, ((WandCapItem)capStack.getItem()).getWandCap());
        WandGem gem = ((WandGemItem)gemStack.getItem()).getWandGem();
        outputItem.setWandGem(outputStack, gem);
        outputStack.set(DataComponentsPM.CAPABILITY_MANA_STORAGE.get(), ManaStorage.emptyWand(gem.getCapacity()));

        return outputStack;
    }

    @Override
    @NotNull
    public RecipeSerializer<? extends CustomRecipe> getSerializer() {
        return RecipeSerializersPM.WAND_ASSEMBLY_SPECIAL.get();
    }
}
