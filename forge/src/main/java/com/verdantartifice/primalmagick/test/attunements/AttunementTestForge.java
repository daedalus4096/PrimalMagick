package com.verdantartifice.primalmagick.test.attunements;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.test.TestUtilsForge;
import net.minecraft.gametest.framework.BeforeBatch;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestGenerator;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.TestFunction;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.gametest.GameTestHolder;

import java.util.Collection;

@GameTestHolder(Constants.MOD_ID + ".forge.attunements")
public class AttunementTestForge extends AbstractAttunementTest {
    @GameTest(template = TestUtilsForge.DEFAULT_TEMPLATE)
    @Override
    public void lesser_blood_attunement_inflicts_bleeding(GameTestHelper helper) {
        super.lesser_blood_attunement_inflicts_bleeding(helper);
    }

    @GameTest(template = TestUtilsForge.DEFAULT_TEMPLATE)
    @Override
    public void greater_blood_attunement_grants_chance_at_self_healing(GameTestHelper helper) {
        super.greater_blood_attunement_grants_chance_at_self_healing(helper);
    }

    @GameTestGenerator
    public Collection<TestFunction> greater_infernal_attunement_prevents_fire_damage() {
        return super.greater_infernal_attunement_prevents_fire_damage(TestUtilsForge.DEFAULT_TEMPLATE);
    }

    @GameTest(template = TestUtilsForge.DEFAULT_TEMPLATE)
    @Override
    public void lesser_void_attunement_reduces_damage_taken(GameTestHelper helper) {
        super.lesser_void_attunement_reduces_damage_taken(helper);
    }

    @GameTest(template = TestUtilsForge.DEFAULT_TEMPLATE)
    @Override
    public void greater_void_attunement_increases_damage_dealt(GameTestHelper helper) {
        super.greater_void_attunement_increases_damage_dealt(helper);
    }

    @GameTest(template = TestUtilsForge.DEFAULT_TEMPLATE)
    @Override
    public void lesser_hallowed_attunement_doubles_damage_dealt_to_undead(GameTestHelper helper) {
        super.lesser_hallowed_attunement_doubles_damage_dealt_to_undead(helper);
    }

    @GameTest(template = TestUtilsForge.DEFAULT_TEMPLATE)
    @Override
    public void greater_hallowed_attunement_prevents_death(GameTestHelper helper) {
        super.greater_hallowed_attunement_prevents_death(helper);
    }
}
