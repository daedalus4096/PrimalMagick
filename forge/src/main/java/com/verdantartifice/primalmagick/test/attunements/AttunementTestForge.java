package com.verdantartifice.primalmagick.test.attunements;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.test.TestUtilsForge;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestGenerator;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.TestFunction;
import net.minecraftforge.gametest.GameTestHolder;

import java.util.Collection;

@GameTestHolder(Constants.MOD_ID + ".forge.attunements")
public class AttunementTestForge extends AbstractAttunementTest {
    @GameTestGenerator
    public Collection<TestFunction> minor_attunement_gives_mana_discount() {
        return super.minor_attunement_gives_mana_discount(TestUtilsForge.DEFAULT_TEMPLATE);
    }

    @GameTest(template = TestUtilsForge.DEFAULT_TEMPLATE)
    @Override
    public void lesser_earth_attunement_gives_haste_modifier(GameTestHelper helper) {
        super.lesser_earth_attunement_gives_haste_modifier(helper);
    }

    @GameTest(template = TestUtilsForge.DEFAULT_TEMPLATE)
    @Override
    public void greater_earth_attunement_gives_step_height_modifier(GameTestHelper helper) {
        super.greater_earth_attunement_gives_step_height_modifier(helper);
    }

    @GameTest(template = TestUtilsForge.DEFAULT_TEMPLATE)
    @Override
    public void lesser_sea_attunement_gives_increased_swim_speed(GameTestHelper helper) {
        super.lesser_sea_attunement_gives_increased_swim_speed(helper);
    }

    @GameTest(template = TestUtilsForge.DEFAULT_TEMPLATE)
    @Override
    public void greater_sea_attunement_gives_water_breathing(GameTestHelper helper) {
        super.greater_sea_attunement_gives_water_breathing(helper);
    }
}
