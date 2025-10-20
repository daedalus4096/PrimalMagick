package com.verdantartifice.primalmagick.test.ftux;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.test.TestUtilsForge;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestGenerator;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.TestFunction;
import net.minecraftforge.gametest.GameTestHolder;

import java.util.Collection;

@GameTestHolder(Constants.MOD_ID + ".forge.ftux")
public class FtuxTestForge extends AbstractFtuxTest {
    @GameTest(template = TestUtilsForge.DEFAULT_TEMPLATE)
    @Override
    public void transform_abort_gives_hint(GameTestHelper helper) {
        super.transform_abort_gives_hint(helper);
    }

    @GameTest(template = TestUtilsForge.DEFAULT_TEMPLATE)
    @Override
    public void transform_without_dream_does_nothing(GameTestHelper helper) {
        super.transform_without_dream_does_nothing(helper);
    }

    @GameTest(template = TestUtilsForge.DEFAULT_TEMPLATE)
    @Override
    public void transform_grimoire(GameTestHelper helper) {
        super.transform_grimoire(helper);
    }
}
