package com.verdantartifice.primalmagick.test.items;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.test.TestUtilsForge;
import net.minecraft.gametest.framework.GameTestGenerator;
import net.minecraft.gametest.framework.TestFunction;
import net.minecraftforge.gametest.GameTestHolder;

import java.util.Collection;

@GameTestHolder(Constants.MOD_ID + ".forge.dispenser")
public class DispenserTestForge extends AbstractDispenserTest {
    @GameTestGenerator
    public Collection<TestFunction> mana_arrows_fired_from_dispenser() {
        return super.mana_arrows_fired_from_dispenser(TestUtilsForge.DEFAULT_TEMPLATE);
    }
}
