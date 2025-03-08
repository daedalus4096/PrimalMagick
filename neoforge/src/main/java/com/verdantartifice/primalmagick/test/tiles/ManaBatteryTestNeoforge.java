package com.verdantartifice.primalmagick.test.tiles;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.test.TestUtilsNeoforge;
import net.minecraft.gametest.framework.GameTestGenerator;
import net.minecraft.gametest.framework.TestFunction;
import net.neoforged.neoforge.gametest.GameTestHolder;

import java.util.Collection;

@GameTestHolder(Constants.MOD_ID)
public class ManaBatteryTestNeoforge extends AbstractManaBatteryTest {
    @GameTestGenerator
    public Collection<TestFunction> mana_battery_can_have_its_menu_opened() {
        return super.mana_battery_can_have_its_menu_opened(TestUtilsNeoforge.DEFAULT_GENERATOR_TEMPLATE);
    }

    @GameTestGenerator
    public Collection<TestFunction> mana_battery_output_allows_chargeable_items() {
        return super.mana_battery_output_allows_chargeable_items(TestUtilsNeoforge.DEFAULT_GENERATOR_TEMPLATE);
    }

    @GameTestGenerator
    public Collection<TestFunction> mana_battery_output_does_not_allow_unchargeable_items() {
        return super.mana_battery_output_does_not_allow_unchargeable_items(TestUtilsNeoforge.DEFAULT_GENERATOR_TEMPLATE);
    }

    @GameTestGenerator
    public Collection<TestFunction> mana_battery_input_allows_essence() {
        return super.mana_battery_input_allows_essence(TestUtilsNeoforge.DEFAULT_GENERATOR_TEMPLATE);
    }

    @GameTestGenerator
    public Collection<TestFunction> mana_battery_input_allows_wands() {
        return super.mana_battery_input_allows_wands(TestUtilsNeoforge.DEFAULT_GENERATOR_TEMPLATE);
    }
}
