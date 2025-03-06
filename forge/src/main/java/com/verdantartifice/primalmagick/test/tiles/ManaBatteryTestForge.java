package com.verdantartifice.primalmagick.test.tiles;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.test.TestUtilsForge;
import net.minecraft.gametest.framework.GameTestGenerator;
import net.minecraft.gametest.framework.TestFunction;
import net.minecraftforge.gametest.GameTestHolder;

import java.util.Collection;

@GameTestHolder(Constants.MOD_ID + ".forge.mana_battery")
public class ManaBatteryTestForge extends AbstractManaBatteryTest {
    @GameTestGenerator
    public Collection<TestFunction> mana_battery_output_allows_chargeable_items() {
        return super.mana_battery_output_allows_chargeable_items(TestUtilsForge.DEFAULT_TEMPLATE);
    }

    @GameTestGenerator
    public Collection<TestFunction> mana_battery_output_does_not_allow_unchargeable_items() {
        return super.mana_battery_output_does_not_allow_unchargeable_items(TestUtilsForge.DEFAULT_TEMPLATE);
    }
}
