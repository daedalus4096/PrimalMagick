package com.verdantartifice.primalmagick.test.spells;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.test.TestUtilsForge;
import net.minecraft.gametest.framework.GameTestGenerator;
import net.minecraft.gametest.framework.TestFunction;
import net.minecraftforge.gametest.GameTestHolder;

import java.util.Collection;

@GameTestHolder(Constants.MOD_ID + ".forge.wand_spellcast")
public class WandSpellcastTestForge extends AbstractWandSpellcastTest {
    @GameTestGenerator
    public Collection<TestFunction> damage_spells_deduct_mana_from_wand_and_award_expertise() {
        return super.damage_spells_deduct_mana_from_wand_and_award_expertise(TestUtilsForge.DEFAULT_TEMPLATE);
    }
}
