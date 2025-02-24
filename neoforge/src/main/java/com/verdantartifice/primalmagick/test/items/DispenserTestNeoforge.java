package com.verdantartifice.primalmagick.test.items;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.test.TestUtilsNeoforge;
import net.minecraft.gametest.framework.GameTestGenerator;
import net.minecraft.gametest.framework.TestFunction;
import net.neoforged.neoforge.gametest.GameTestHolder;

import java.util.Collection;

@GameTestHolder(Constants.MOD_ID)
public class DispenserTestNeoforge extends AbstractDispenserTest {
    @GameTestGenerator
    public Collection<TestFunction> mana_arrows_fired_from_dispenser() {
        return super.mana_arrows_fired_from_dispenser(TestUtilsNeoforge.DEFAULT_GENERATOR_TEMPLATE);
    }
}
