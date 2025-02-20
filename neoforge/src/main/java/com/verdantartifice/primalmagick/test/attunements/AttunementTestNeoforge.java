package com.verdantartifice.primalmagick.test.attunements;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.test.TestUtilsNeoforge;
import net.minecraft.gametest.framework.GameTestGenerator;
import net.minecraft.gametest.framework.TestFunction;
import net.neoforged.neoforge.gametest.GameTestHolder;

import java.util.Collection;

@GameTestHolder(Constants.MOD_ID)
public class AttunementTestNeoforge extends AbstractAttunementTest {
    @GameTestGenerator
    public Collection<TestFunction> minor_attunement_gives_mana_discount() {
        return super.minor_attunement_gives_mana_discount(TestUtilsNeoforge.DEFAULT_GENERATOR_TEMPLATE);
    }
}
