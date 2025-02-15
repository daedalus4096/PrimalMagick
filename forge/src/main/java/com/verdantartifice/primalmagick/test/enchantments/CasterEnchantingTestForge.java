package com.verdantartifice.primalmagick.test.enchantments;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.test.TestUtilsForge;
import net.minecraft.gametest.framework.GameTestGenerator;
import net.minecraft.gametest.framework.TestFunction;
import net.minecraftforge.gametest.GameTestHolder;

import java.util.Collection;

@GameTestHolder(Constants.MOD_ID + ".forge.caster_enchanting")
public class CasterEnchantingTestForge extends AbstractCasterEnchantingTest {
    @GameTestGenerator
    public Collection<TestFunction> caster_enchanting_tests() {
        return super.caster_enchanting_tests(TestUtilsForge.DEFAULT_TEMPLATE);
    }
}
