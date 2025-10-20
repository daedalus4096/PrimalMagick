package com.verdantartifice.primalmagick.test.ftux;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.test.TestUtilsNeoforge;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestGenerator;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.TestFunction;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

import java.util.Collection;

@GameTestHolder(Constants.MOD_ID)
public class FtuxTestNeoforge extends AbstractFtuxTest {
    @PrefixGameTestTemplate(false)
    @GameTest(template = TestUtilsNeoforge.DEFAULT_TEMPLATE)
    @Override
    public void transform_abort_gives_hint(GameTestHelper helper) {
        super.transform_abort_gives_hint(helper);
    }

    @PrefixGameTestTemplate(false)
    @GameTest(template = TestUtilsNeoforge.DEFAULT_TEMPLATE)
    @Override
    public void transform_without_dream_does_nothing(GameTestHelper helper) {
        super.transform_without_dream_does_nothing(helper);
    }

    @PrefixGameTestTemplate(false)
    @GameTest(template = TestUtilsNeoforge.DEFAULT_TEMPLATE)
    @Override
    public void transform_grimoire(GameTestHelper helper) {
        super.transform_grimoire(helper);
    }
}
