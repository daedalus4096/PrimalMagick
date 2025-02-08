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
    @GameTestGenerator
    public Collection<TestFunction> font_discovery_tests() {
        return super.font_discovery_tests(TestUtilsForge.DEFAULT_TEMPLATE);
    }

    @GameTestGenerator
    public Collection<TestFunction> mundane_wand_crafting_tests() {
        return super.mundane_wand_crafting_tests(TestUtilsForge.DEFAULT_TEMPLATE);
    }

    @GameTest(template = "primalmagick:test/floor5x5x5", timeoutTicks = 150)
    @Override
    public void sleeping_after_shrine_grants_dream(GameTestHelper helper) {
        super.sleeping_after_shrine_grants_dream(helper);
    }

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
