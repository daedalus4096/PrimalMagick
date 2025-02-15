package com.verdantartifice.primalmagick.test.enchantments;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.test.TestUtilsNeoforge;
import net.minecraft.gametest.framework.GameTestGenerator;
import net.minecraft.gametest.framework.TestFunction;
import net.neoforged.neoforge.gametest.GameTestHolder;

import java.util.Collection;

@GameTestHolder(Constants.MOD_ID)
public class CasterEnchantingTestNeoforge extends AbstractCasterEnchantingTest {
    @GameTestGenerator
    public Collection<TestFunction> caster_enchanting_tests() {
        return super.caster_enchanting_tests(String.join(":", Constants.MOD_ID, TestUtilsNeoforge.DEFAULT_TEMPLATE));
    }
}
