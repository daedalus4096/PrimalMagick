package com.verdantartifice.primalmagick.test.tiles;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.test.TestUtilsNeoforge;
import net.minecraft.gametest.framework.GameTestGenerator;
import net.minecraft.gametest.framework.TestFunction;
import net.neoforged.neoforge.gametest.GameTestHolder;

import java.util.Collection;

@GameTestHolder(Constants.MOD_ID)
public class WandChargerTestNeoforge extends AbstractWandChargerTest {
    @GameTestGenerator
    public Collection<TestFunction> wand_charger_output_allows_chargeable_items() {
        return super.wand_charger_output_allows_chargeable_items(TestUtilsNeoforge.DEFAULT_GENERATOR_TEMPLATE);
    }

    @GameTestGenerator
    public Collection<TestFunction> wand_charger_output_does_not_allow_unchargeable_items() {
        return super.wand_charger_output_does_not_allow_unchargeable_items(TestUtilsNeoforge.DEFAULT_GENERATOR_TEMPLATE);
    }

    @GameTestGenerator
    public Collection<TestFunction> wand_charger_input_allows_essence() {
        return super.wand_charger_input_allows_essence(TestUtilsNeoforge.DEFAULT_GENERATOR_TEMPLATE);
    }

    @GameTestGenerator
    public Collection<TestFunction> wand_charger_input_does_not_allow_non_essence() {
        return super.wand_charger_input_does_not_allow_non_essence(TestUtilsNeoforge.DEFAULT_GENERATOR_TEMPLATE);
    }

    @GameTestGenerator
    public Collection<TestFunction> wand_charger_can_charge_with_right_items() {
        return super.wand_charger_can_charge_with_right_items(TestUtilsNeoforge.DEFAULT_GENERATOR_TEMPLATE);
    }

    @GameTestGenerator
    public Collection<TestFunction> wand_charger_do_charge_with_right_items() {
        return super.wand_charger_do_charge_with_right_items(TestUtilsNeoforge.DEFAULT_GENERATOR_TEMPLATE);
    }
}
