package com.verdantartifice.primalmagick.test.tiles;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.test.TestUtilsForge;
import net.minecraft.gametest.framework.GameTestGenerator;
import net.minecraft.gametest.framework.TestFunction;
import net.minecraftforge.gametest.GameTestHolder;

import java.util.Collection;

@GameTestHolder(Constants.MOD_ID + ".forge.wand_charger")
public class WandChargerTestForge extends AbstractWandChargerTest {
    @GameTestGenerator
    public Collection<TestFunction> wand_charger_output_allows_chargeable_items() {
        return super.wand_charger_output_allows_chargeable_items(TestUtilsForge.DEFAULT_TEMPLATE);
    }

    @GameTestGenerator
    public Collection<TestFunction> wand_charger_output_does_not_allow_unchargeable_items() {
        return super.wand_charger_output_does_not_allow_unchargeable_items(TestUtilsForge.DEFAULT_TEMPLATE);
    }

    @GameTestGenerator
    public Collection<TestFunction> wand_charger_input_allows_essence() {
        return super.wand_charger_input_allows_essence(TestUtilsForge.DEFAULT_TEMPLATE);
    }

    @GameTestGenerator
    public Collection<TestFunction> wand_charger_input_does_not_allow_non_essence() {
        return super.wand_charger_input_does_not_allow_non_essence(TestUtilsForge.DEFAULT_TEMPLATE);
    }

    @GameTestGenerator
    public Collection<TestFunction> wand_charger_can_charge_with_right_items() {
        return super.wand_charger_can_charge_with_right_items(TestUtilsForge.DEFAULT_TEMPLATE);
    }

    @GameTestGenerator
    public Collection<TestFunction> wand_charger_do_charge_with_right_items() {
        return super.wand_charger_do_charge_with_right_items(TestUtilsForge.DEFAULT_TEMPLATE);
    }
}
