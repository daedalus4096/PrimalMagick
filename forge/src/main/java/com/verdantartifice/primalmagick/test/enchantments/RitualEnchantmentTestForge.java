package com.verdantartifice.primalmagick.test.enchantments;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.test.TestUtilsForge;
import net.minecraft.gametest.framework.GameTestGenerator;
import net.minecraft.gametest.framework.TestFunction;
import net.minecraftforge.gametest.GameTestHolder;

import java.util.Collection;

@GameTestHolder(Constants.MOD_ID + ".forge.ritual_enchants")
public class RitualEnchantmentTestForge extends AbstractRitualEnchantmentTest {
    @GameTestGenerator
    public Collection<TestFunction> essence_thief() {
        return super.essence_thief(TestUtilsForge.DEFAULT_TEMPLATE);
    }
}
