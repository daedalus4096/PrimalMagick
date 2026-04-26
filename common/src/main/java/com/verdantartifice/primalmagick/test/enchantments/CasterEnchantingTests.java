package com.verdantartifice.primalmagick.test.enchantments;

import com.verdantartifice.primalmagick.common.items.wands.IHasWandComponents;
import com.verdantartifice.primalmagick.common.wands.WandCap;
import com.verdantartifice.primalmagick.common.wands.WandCore;
import com.verdantartifice.primalmagick.common.wands.WandGem;
import com.verdantartifice.primalmagick.test.AbstractBaseTest;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

import java.util.stream.StreamSupport;

public class CasterEnchantingTests extends AbstractBaseTest {
    public static void caster_can_be_enchanted(GameTestHelper helper, Item item) {
        // Get a stream of all possible enchantments for the enchanting table
        var possibleEnchants = helper.getLevel().registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getTagOrEmpty(EnchantmentTags.IN_ENCHANTING_TABLE);
        var possibleEnchantsStream = StreamSupport.stream(possibleEnchants.spliterator(), false);

        // Create an item stack for this test's caster item
        ItemStack casterStack = new ItemStack(item);
        if (item instanceof IHasWandComponents hasComponents) {
            hasComponents.setWandCore(casterStack, WandCore.PRIMAL);
            hasComponents.setWandCap(casterStack, WandCap.HEXIUM);
            hasComponents.setWandGem(casterStack, WandGem.WIZARD);
        }

        // Confirm that enchantments are possible for the given caster item
        var enchList = EnchantmentHelper.selectEnchantment(helper.getLevel().getRandom(), casterStack, 30, possibleEnchantsStream);
        assertFalse(helper, enchList.isEmpty(), "No enchantments found for caster stack");

        helper.succeed();
    }
}
