package com.verdantartifice.primalmagick.test.spells;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.test.TestUtilsNeoforge;
import net.minecraft.gametest.framework.GameTestGenerator;
import net.minecraft.gametest.framework.TestFunction;
import net.neoforged.neoforge.gametest.GameTestHolder;

import java.util.Collection;

@GameTestHolder(Constants.MOD_ID)
public class WandSpellcastTestNeoforge extends AbstractWandSpellcastTest {
    @GameTestGenerator
    public Collection<TestFunction> damage_spells_deduct_mana_from_wand() {
        return super.damage_spells_deduct_mana_from_wand(TestUtilsNeoforge.DEFAULT_GENERATOR_TEMPLATE);
    }
}
