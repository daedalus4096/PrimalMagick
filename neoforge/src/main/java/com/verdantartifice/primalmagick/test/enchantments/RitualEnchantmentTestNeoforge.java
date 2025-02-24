package com.verdantartifice.primalmagick.test.enchantments;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.test.TestUtilsNeoforge;
import net.minecraft.gametest.framework.GameTestGenerator;
import net.minecraft.gametest.framework.TestFunction;
import net.neoforged.neoforge.gametest.GameTestHolder;

import java.util.Collection;

@GameTestHolder(Constants.MOD_ID)
public class RitualEnchantmentTestNeoforge extends AbstractRitualEnchantmentTest {
    @GameTestGenerator
    public Collection<TestFunction> essence_thief() {
        return super.essence_thief(TestUtilsNeoforge.DEFAULT_GENERATOR_TEMPLATE);
    }
}
