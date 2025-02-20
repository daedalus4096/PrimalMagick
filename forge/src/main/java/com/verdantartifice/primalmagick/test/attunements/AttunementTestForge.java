package com.verdantartifice.primalmagick.test.attunements;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.test.TestUtilsForge;
import net.minecraft.gametest.framework.GameTestGenerator;
import net.minecraft.gametest.framework.TestFunction;
import net.minecraftforge.gametest.GameTestHolder;

import java.util.Collection;

@GameTestHolder(Constants.MOD_ID + ".forge.attunements")
public class AttunementTestForge extends AbstractAttunementTest {
    @GameTestGenerator
    public Collection<TestFunction> minor_attunement_gives_mana_discount() {
        return super.minor_attunement_gives_mana_discount(TestUtilsForge.DEFAULT_TEMPLATE);
    }
}
