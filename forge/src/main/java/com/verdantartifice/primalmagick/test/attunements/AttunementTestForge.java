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
    @BeforeBatch(batch = "attunementDayTests")
    @Override
    public void beforeDayBatch(ServerLevel level) {
        super.beforeDayBatch(level);
    }

    @BeforeBatch(batch = "attunementNightTests")
    @Override
    public void beforeNightBatch(ServerLevel level) {
        super.beforeNightBatch(level);
    }

    @GameTestGenerator
    public Collection<TestFunction> minor_attunement_gives_mana_discount() {
        return super.minor_attunement_gives_mana_discount(TestUtilsForge.DEFAULT_TEMPLATE);
    }

    @GameTest(template = TestUtilsForge.DEFAULT_TEMPLATE)
    @Override
    public void lesser_earth_attunement_gives_haste_modifier(GameTestHelper helper) {
        super.lesser_earth_attunement_gives_haste_modifier(helper);
    }

    @GameTest(template = TestUtilsForge.DEFAULT_TEMPLATE)
    @Override
    public void greater_earth_attunement_gives_step_height_modifier(GameTestHelper helper) {
        super.greater_earth_attunement_gives_step_height_modifier(helper);
    }

    @GameTest(template = TestUtilsForge.DEFAULT_TEMPLATE)
    @Override
    public void lesser_sea_attunement_gives_increased_swim_speed(GameTestHelper helper) {
        super.lesser_sea_attunement_gives_increased_swim_speed(helper);
    }

    @GameTest(template = TestUtilsForge.DEFAULT_TEMPLATE)
    @Override
    public void greater_sea_attunement_gives_water_breathing(GameTestHelper helper) {
        super.greater_sea_attunement_gives_water_breathing(helper);
    }

    @GameTest(template = TestUtilsForge.DEFAULT_TEMPLATE)
    @Override
    public void lesser_sky_attunement_gives_movement_speed_modifier(GameTestHelper helper) {
        super.lesser_sky_attunement_gives_movement_speed_modifier(helper);
    }

    @GameTest(template = TestUtilsForge.DEFAULT_TEMPLATE)
    @Override
    public void lesser_sky_attunement_reduces_fall_damage_taken(GameTestHelper helper) {
        super.lesser_sky_attunement_reduces_fall_damage_taken(helper);
    }

    @GameTest(template = TestUtilsForge.DEFAULT_TEMPLATE)
    @Override
    public void greater_sky_attunement_increases_jump_strength(GameTestHelper helper) {
        super.greater_sky_attunement_increases_jump_strength(helper);
    }

    @GameTest(template = TestUtilsForge.DEFAULT_TEMPLATE, batch = "attunementDayTests")
    @Override
    public void lesser_sun_attunement_regenerates_food_during_day(GameTestHelper helper) {
        super.lesser_sun_attunement_regenerates_food_during_day(helper);
    }

    @GameTest(template = TestUtilsForge.DEFAULT_TEMPLATE, batch = "attunementNightTests")
    @Override
    public void lesser_sun_attunement_does_not_regenerate_food_during_night(GameTestHelper helper) {
        super.lesser_sun_attunement_does_not_regenerate_food_during_night(helper);
    }

    @GameTest(template = TestUtilsForge.DEFAULT_TEMPLATE)
    @Override
    public void lesser_moon_attunement_grants_invisibility_chance_on_hurt(GameTestHelper helper) {
        super.lesser_moon_attunement_grants_invisibility_chance_on_hurt(helper);
    }

    @GameTest(template = TestUtilsForge.DEFAULT_TEMPLATE)
    @Override
    public void greater_moon_attunement_grants_night_vision(GameTestHelper helper) {
        super.greater_moon_attunement_grants_night_vision(helper);
    }

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

    @GameTest(template = TestUtilsForge.DEFAULT_TEMPLATE)
    @Override
    public void lesser_infernal_attunement_fires_hellish_chain_on_attack(GameTestHelper helper) {
        super.lesser_infernal_attunement_fires_hellish_chain_on_attack(helper);
    }

    @GameTestGenerator
    public Collection<TestFunction> greater_infernal_attunement_prevents_fire_damage() {
        return super.greater_infernal_attunement_prevents_fire_damage(TestUtilsForge.DEFAULT_TEMPLATE);
    }
}
