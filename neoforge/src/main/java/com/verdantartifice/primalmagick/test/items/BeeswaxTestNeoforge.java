package com.verdantartifice.primalmagick.test.items;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.test.TestUtilsNeoforge;
import net.minecraft.gametest.framework.GameTestGenerator;
import net.minecraft.gametest.framework.TestFunction;
import net.neoforged.neoforge.gametest.GameTestHolder;

import java.util.Collection;

@GameTestHolder(Constants.MOD_ID)
public class BeeswaxTestNeoforge extends AbstractBeeswaxTest {
    @GameTestGenerator
    public Collection<TestFunction> apply_beeswax_directly() {
        return super.apply_beeswax_directly(TestUtilsNeoforge.DEFAULT_GENERATOR_TEMPLATE);
    }

    @GameTestGenerator
    public Collection<TestFunction> apply_beeswax_via_crafting() {
        return super.apply_beeswax_via_crafting(TestUtilsNeoforge.DEFAULT_GENERATOR_TEMPLATE);
    }
}
