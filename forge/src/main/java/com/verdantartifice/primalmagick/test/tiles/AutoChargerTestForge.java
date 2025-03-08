package com.verdantartifice.primalmagick.test.tiles;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.test.TestUtilsForge;
import net.minecraft.gametest.framework.GameTestGenerator;
import net.minecraft.gametest.framework.TestFunction;
import net.minecraftforge.gametest.GameTestHolder;

import java.util.Collection;

@GameTestHolder(Constants.MOD_ID + ".forge.auto_charger")
public class AutoChargerTestForge extends AbstractAutoChargerTest {
    @GameTestGenerator
    public Collection<TestFunction> auto_charger_output_allows_chargeable_items() {
        return super.auto_charger_output_allows_chargeable_items(TestUtilsForge.DEFAULT_TEMPLATE);
    }

    @GameTestGenerator
    public Collection<TestFunction> auto_charger_output_does_not_allow_unchargeable_items() {
        return super.auto_charger_output_does_not_allow_unchargeable_items(TestUtilsForge.DEFAULT_TEMPLATE);
    }

    @GameTestGenerator
    public Collection<TestFunction> auto_charger_siphons_into_chargeable_items() {
        return super.auto_charger_siphons_into_chargeable_items(TestUtilsForge.DEFAULT_TEMPLATE);
    }
}
