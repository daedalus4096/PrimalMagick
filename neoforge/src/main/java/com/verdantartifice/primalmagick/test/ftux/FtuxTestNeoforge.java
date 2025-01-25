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
    @GameTestGenerator
    public Collection<TestFunction> font_discovery_tests() {
        return super.font_discovery_tests(String.join(":", Constants.MOD_ID, TestUtilsNeoforge.DEFAULT_TEMPLATE));
    }

    @PrefixGameTestTemplate(false)
    @GameTest(template = "test/floor5x5x5", timeoutTicks = 150)
    @Override
    public void sleeping_after_shrine_grants_dream(GameTestHelper helper) {
        super.sleeping_after_shrine_grants_dream(helper);
    }

    @GameTestGenerator
    public Collection<TestFunction> mundane_wand_crafting_tests() {
        return super.mundane_wand_crafting_tests(String.join(":", Constants.MOD_ID, TestUtilsNeoforge.DEFAULT_TEMPLATE));
    }

    @PrefixGameTestTemplate(false)
    @GameTest(template = TestUtilsNeoforge.DEFAULT_TEMPLATE)
    @Override
    public void transform_abort_gives_hint(GameTestHelper helper) {
        super.transform_abort_gives_hint(helper);
    }

    @PrefixGameTestTemplate(false)
    @GameTest(template = TestUtilsNeoforge.DEFAULT_TEMPLATE)
    @Override
    public void transform_grimoire(GameTestHelper helper) {
        super.transform_grimoire(helper);
    }
}
