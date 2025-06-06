package com.verdantartifice.primalmagick.test.tiles;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.test.TestUtilsNeoforge;
import net.minecraft.gametest.framework.GameTestGenerator;
import net.minecraft.gametest.framework.TestFunction;
import net.neoforged.neoforge.gametest.GameTestHolder;

import java.util.Collection;

@GameTestHolder(Constants.MOD_ID)
public class AutoChargerTestNeoforge extends AbstractAutoChargerTest {
    @GameTestGenerator
    public Collection<TestFunction> auto_charger_output_allows_chargeable_items() {
        return super.auto_charger_output_allows_chargeable_items(TestUtilsNeoforge.DEFAULT_GENERATOR_TEMPLATE);
    }

    @GameTestGenerator
    public Collection<TestFunction> auto_charger_output_does_not_allow_unchargeable_items() {
        return super.auto_charger_output_does_not_allow_unchargeable_items(TestUtilsNeoforge.DEFAULT_GENERATOR_TEMPLATE);
    }

    @GameTestGenerator
    public Collection<TestFunction> auto_charger_can_have_chargeable_items_inserted() {
        return super.auto_charger_can_have_chargeable_items_inserted(TestUtilsNeoforge.DEFAULT_GENERATOR_TEMPLATE);
    }

    @GameTestGenerator
    public Collection<TestFunction> auto_charger_cannot_have_unchargeable_items_inserted() {
        return super.auto_charger_cannot_have_unchargeable_items_inserted(TestUtilsNeoforge.DEFAULT_GENERATOR_TEMPLATE);
    }

    @GameTestGenerator
    public Collection<TestFunction> auto_charger_can_have_chargeable_items_removed() {
        return super.auto_charger_can_have_chargeable_items_removed(TestUtilsNeoforge.DEFAULT_GENERATOR_TEMPLATE);
    }

    @GameTestGenerator
    public Collection<TestFunction> auto_charger_siphons_into_chargeable_items() {
        return super.auto_charger_siphons_into_chargeable_items(String.join(":", Constants.MOD_ID, "test/floor5x5x5"));
    }
}
