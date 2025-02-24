package com.verdantartifice.primalmagick.test.items;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.test.TestUtilsForge;
import net.minecraft.gametest.framework.GameTestGenerator;
import net.minecraft.gametest.framework.TestFunction;
import net.minecraftforge.gametest.GameTestHolder;

import java.util.Collection;

@GameTestHolder(Constants.MOD_ID + ".forge.beeswax")
public class BeeswaxTestForge extends AbstractBeeswaxTest {
    @GameTestGenerator
    public Collection<TestFunction> apply_beeswax_directly() {
        return super.apply_beeswax_directly(TestUtilsForge.DEFAULT_TEMPLATE);
    }

    @GameTestGenerator
    public Collection<TestFunction> apply_beeswax_via_crafting() {
        return super.apply_beeswax_via_crafting(TestUtilsForge.DEFAULT_TEMPLATE);
    }
}
