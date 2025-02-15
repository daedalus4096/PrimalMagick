package com.verdantartifice.primalmagick.test.enchantments;

import com.google.common.collect.ImmutableMap;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.items.wands.IHasWandComponents;
import com.verdantartifice.primalmagick.common.wands.WandCap;
import com.verdantartifice.primalmagick.common.wands.WandCore;
import com.verdantartifice.primalmagick.common.wands.WandGem;
import com.verdantartifice.primalmagick.test.AbstractBaseTest;
import com.verdantartifice.primalmagick.test.TestUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.TestFunction;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

import java.util.Collection;
import java.util.Map;

public class AbstractCasterEnchantingTest extends AbstractBaseTest {
    public Collection<TestFunction> caster_enchanting_tests(String templateName) {
        Map<String, Item> testParams = ImmutableMap.<String, Item>builder()
                .put("mundane_wand", ItemsPM.MUNDANE_WAND.get())
                .put("modular_wand", ItemsPM.MODULAR_WAND.get())
                .put("modular_staff", ItemsPM.MODULAR_STAFF.get())
                .build();
        return TestUtils.createParameterizedTestFunctions("caster_enchanting_tests", templateName, testParams, (helper, item) -> {
            // Get a stream of all possible enchantments for the enchanting table
            var possibleEnchantsOpt = helper.getLevel().registryAccess().registryOrThrow(Registries.ENCHANTMENT).getTag(EnchantmentTags.IN_ENCHANTING_TABLE);
            helper.assertTrue(possibleEnchantsOpt.isPresent(), "No possible enchantments found for table");

            // Create an item stack for this test's caster item
            ItemStack casterStack = new ItemStack(item);
            if (item instanceof IHasWandComponents hasComponents) {
                hasComponents.setWandCore(casterStack, WandCore.PRIMAL);
                hasComponents.setWandCap(casterStack, WandCap.HEXIUM);
                hasComponents.setWandGem(casterStack, WandGem.WIZARD);
            }

            // Confirm that enchantments are possible for the given caster item
            var enchList = EnchantmentHelper.selectEnchantment(helper.getLevel().random, casterStack, 30, possibleEnchantsOpt.get().stream());
            helper.assertFalse(enchList.isEmpty(), "No enchantments found for caster stack");
            
            helper.succeed();
        });
    }
}
