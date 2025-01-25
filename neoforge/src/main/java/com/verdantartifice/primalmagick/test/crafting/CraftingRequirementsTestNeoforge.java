package com.verdantartifice.primalmagick.test.crafting;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.test.TestUtilsNeoforge;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

@GameTestHolder(Constants.MOD_ID)
public class CraftingRequirementsTestNeoforge extends AbstractCraftingRequirementsTest{
    @PrefixGameTestTemplate(false)
    @GameTest(template = TestUtilsNeoforge.DEFAULT_TEMPLATE)
    @Override
    public void arcane_recipe(GameTestHelper helper) {
        super.arcane_recipe(helper);
    }

    @PrefixGameTestTemplate(false)
    @GameTest(template = TestUtilsNeoforge.DEFAULT_TEMPLATE)
    @Override
    public void ritual_recipe(GameTestHelper helper) {
        super.ritual_recipe(helper);
    }
}
