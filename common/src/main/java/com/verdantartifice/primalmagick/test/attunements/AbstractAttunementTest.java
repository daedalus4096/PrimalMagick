package com.verdantartifice.primalmagick.test.attunements;

import com.google.common.collect.ImmutableMap;
import com.mojang.logging.LogUtils;
import com.verdantartifice.primalmagick.common.attunements.AttunementAttributeModifiers;
import com.verdantartifice.primalmagick.common.attunements.AttunementManager;
import com.verdantartifice.primalmagick.common.attunements.AttunementThreshold;
import com.verdantartifice.primalmagick.common.attunements.AttunementType;
import com.verdantartifice.primalmagick.common.blocks.BlocksPM;
import com.verdantartifice.primalmagick.common.capabilities.IPlayerCooldowns;
import com.verdantartifice.primalmagick.common.damagesource.DamageSourcesPM;
import com.verdantartifice.primalmagick.common.effects.EffectsPM;
import com.verdantartifice.primalmagick.common.events.CombatEvents;
import com.verdantartifice.primalmagick.common.events.PlayerEvents;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.items.wands.ModularWandItem;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.sources.Sources;
import com.verdantartifice.primalmagick.common.wands.WandCap;
import com.verdantartifice.primalmagick.common.wands.WandCore;
import com.verdantartifice.primalmagick.common.wands.WandGem;
import com.verdantartifice.primalmagick.platform.Services;
import com.verdantartifice.primalmagick.test.AbstractBaseTest;
import com.verdantartifice.primalmagick.test.TestRandomSource;
import com.verdantartifice.primalmagick.test.TestUtils;
import net.minecraft.core.RegistryAccess;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.TestFunction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.LightLayer;
import org.apache.commons.lang3.mutable.MutableFloat;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class AbstractAttunementTest extends AbstractBaseTest {
    public void beforeDayBatch(ServerLevel level) {
        level.setDayTime(6000);
        level.tick(() -> true);
    }

    public void beforeNightBatch(ServerLevel level) {
        level.setDayTime(18000);
        level.tick(() -> true);
    }

    public Collection<TestFunction> minor_attunement_gives_mana_discount(String templateName) {
        Map<String, Source> testParams = Sources.streamSorted().collect(Collectors.toMap(source -> source.getId().getPath(), source -> source));
        return TestUtils.createParameterizedTestFunctions("minor_attunement_gives_mana_discount", templateName, testParams, (helper, source) -> {
            // Create a test player
            var player = this.makeMockServerPlayer(helper);

            // Create a wand with no expected mana discounts or penalties
            ItemStack wandStack = new ItemStack(ItemsPM.MODULAR_WAND.get());
            var wandItem = assertInstanceOf(helper, wandStack.getItem(), ModularWandItem.class, "Wand is not of the expected type");
            wandItem.setWandCore(wandStack, WandCore.HEARTWOOD);
            wandItem.setWandCap(wandStack, WandCap.GOLD);
            wandItem.setWandGem(wandStack, WandGem.ADEPT);

            // Confirm that all sources have neither a discount nor penalty before attunement grant
            Sources.streamSorted().forEach(s -> {
                double actual = wandItem.getTotalCostModifier(wandStack, player, s, helper.getLevel().registryAccess());
                helper.assertTrue(actual == 1D, "Base wand cost modifier is not as expected for source " + s.getId());
            });

            // Grant the test player minor attunement in the source being tested
            AttunementManager.setAttunement(player, source, AttunementType.PERMANENT, AttunementThreshold.MINOR.getValue());

            // Confirm that the tested source has a 5% discount while all others are unmodified
            Sources.streamSorted().forEach(s -> {
                double expected = s.equals(source) ? 0.95D : 1D;
                double actual = wandItem.getTotalCostModifier(wandStack, player, s, helper.getLevel().registryAccess());
                helper.assertTrue(actual == expected, "Final wand cost modifier is not as expected for source " + s.getId() + ": " + actual);
            });

            helper.succeed();
        });
    }

    public void lesser_earth_attunement_gives_haste_modifier(GameTestHelper helper) {
        // Create a test player
        var player = this.makeMockServerPlayer(helper);

        // Confirm that the player doesn't have the relevant attribute modifier to start
        helper.assertFalse(player.getAttributes().hasModifier(Attributes.ATTACK_SPEED, AttunementAttributeModifiers.EARTH_LESSER_ID), "Player has unexpected attribute modifier");

        // Grant the test player lesser attunement to the Earth
        AttunementManager.setAttunement(player, Sources.EARTH, AttunementType.PERMANENT, AttunementThreshold.LESSER.getValue());

        // Confirm that the player has the relevant attribute modifier with attunement
        helper.assertTrue(player.getAttributes().hasModifier(Attributes.ATTACK_SPEED, AttunementAttributeModifiers.EARTH_LESSER_ID), "Player does not have expected attribute modifier");

        helper.succeed();
    }

    public void greater_earth_attunement_gives_step_height_modifier(GameTestHelper helper) {
        // Create a test player
        var player = this.makeMockServerPlayer(helper);

        // Confirm that the player doesn't have the relevant attribute modifier to start
        helper.assertFalse(player.getAttributes().hasModifier(Attributes.STEP_HEIGHT, AttunementAttributeModifiers.EARTH_GREATER_ID), "Player has unexpected attribute modifier");

        // Grant the test player greater attunement to the Earth and force processing of attunement buffs
        AttunementManager.setAttunement(player, Sources.EARTH, AttunementType.PERMANENT, AttunementThreshold.GREATER.getValue());
        PlayerEvents.applyAttunementBuffs(player);

        // Confirm that the player has the relevant attribute modifier with attunement
        helper.assertTrue(player.getAttributes().hasModifier(Attributes.STEP_HEIGHT, AttunementAttributeModifiers.EARTH_GREATER_ID), "Player does not have expected attribute modifier");

        helper.succeed();
    }

    public void lesser_sea_attunement_gives_increased_swim_speed(GameTestHelper helper) {
        // Create a test player
        var player = this.makeMockServerPlayer(helper);

        // Confirm that the player doesn't have the relevant attribute modifier to start
        helper.assertFalse(player.getAttributes().hasModifier(Services.ATTRIBUTES.swimSpeed(), AttunementAttributeModifiers.SEA_LESSER_ID), "Player has unexpected attribute modifier");

        // Grant the test player lesser attunement to the Sea
        AttunementManager.setAttunement(player, Sources.SEA, AttunementType.PERMANENT, AttunementThreshold.LESSER.getValue());

        // Confirm that the player has the relevant attribute modifier with attunement
        helper.assertTrue(player.getAttributes().hasModifier(Services.ATTRIBUTES.swimSpeed(), AttunementAttributeModifiers.SEA_LESSER_ID), "Player does not have expected attribute modifier");

        helper.succeed();
    }

    public void greater_sea_attunement_gives_water_breathing(GameTestHelper helper) {
        // Create a test player
        var player = this.makeMockServerPlayer(helper);

        // Confirm that the player has the relevant effect with attunement
        helper.assertFalse(player.hasEffect(MobEffects.WATER_BREATHING), "Player has unexpected effect");

        // Grant the test player greater attunement to the Sea and force processing of attunement buffs
        AttunementManager.setAttunement(player, Sources.SEA, AttunementType.PERMANENT, AttunementThreshold.GREATER.getValue());
        PlayerEvents.applyAttunementBuffs(player);

        // Confirm that the player has the relevant effect with attunement
        helper.assertTrue(player.hasEffect(MobEffects.WATER_BREATHING), "Player does not have expected effect");

        helper.succeed();
    }

    public void lesser_sky_attunement_gives_movement_speed_modifier(GameTestHelper helper) {
        // Create a test player
        var player = this.makeMockServerPlayer(helper);

        // Confirm that the player doesn't have the relevant attribute modifier to start
        helper.assertFalse(player.getAttributes().hasModifier(Attributes.MOVEMENT_SPEED, AttunementAttributeModifiers.SKY_LESSER_ID), "Player has unexpected attribute modifier");

        // Grant the test player lesser attunement to the Sky
        AttunementManager.setAttunement(player, Sources.SKY, AttunementType.PERMANENT, AttunementThreshold.LESSER.getValue());

        // Confirm that the player has the relevant attribute modifier with attunement
        helper.assertTrue(player.getAttributes().hasModifier(Attributes.MOVEMENT_SPEED, AttunementAttributeModifiers.SKY_LESSER_ID), "Player does not have expected attribute modifier");

        helper.succeed();
    }

    public void lesser_sky_attunement_reduces_fall_damage_taken(GameTestHelper helper) {
        final float expectedDamage = 8F;

        // Create a test player
        var player = helper.makeMockPlayer(GameType.SURVIVAL);

        // Confirm that the player takes normal falling damage to start
        player.hurt(player.damageSources().fall(), expectedDamage);
        var actualDamage = player.getMaxHealth() - player.getHealth();
        helper.assertTrue(actualDamage == expectedDamage, "Player did not take expected damage without attunement: " + actualDamage);

        // Reset the player's health and grant them lesser attunement to the Sky
        player.setHealth(player.getMaxHealth());
        AttunementManager.setAttunement(player, Sources.SKY, AttunementType.PERMANENT, AttunementThreshold.LESSER.getValue());

        // Confirm that the player takes reduced falling damage with attunement
        player.hurt(player.damageSources().fall(), expectedDamage);
        actualDamage = player.getMaxHealth() - player.getHealth();
        helper.assertTrue(actualDamage < expectedDamage, "Player did not take reduced damage with attunement: " + actualDamage);

        helper.succeed();
    }

    public void greater_sky_attunement_increases_jump_strength(GameTestHelper helper) {
        // Create a test player
        var player1 = this.makeMockServerPlayer(helper);
        var expectedJumpStrength = player1.getAttributeValue(Attributes.JUMP_STRENGTH);

        // Have the player jump and measure its unmodified jump strength
        player1.jumpFromGround();
        helper.assertTrue(player1.getDeltaMovement().y() == expectedJumpStrength, "Player did not have expected starting jump strength");

        // Discard that player and create another one
        player1.discard();
        var player2 = this.makeMockServerPlayer(helper);

        // Grant the new player greater attunement to the Sky
        AttunementManager.setAttunement(player2, Sources.SKY, AttunementType.PERMANENT, AttunementThreshold.GREATER.getValue());

        // Have the new player jump and confirm that their jump strength is greater
        player2.jumpFromGround();
        helper.assertTrue(player2.getDeltaMovement().y() > expectedJumpStrength, "Player did not have boosted jump strength as expected");

        helper.succeed();
    }

    // TODO Add a double jump test for greater sky attunement

    public void lesser_sun_attunement_regenerates_food_during_day(GameTestHelper helper) {
        final int startFood = 6;
        final int endFood = startFood + 1;

        // Create a test player
        var player = this.makeMockServerPlayer(helper);

        // Set starting test conditions
        helper.setDayTime(0);
        helper.getLevel().tick(() -> true);
        player.getFoodData().setFoodLevel(startFood);
        helper.assertTrue(player.getFoodData().getFoodLevel() == startFood, "Player does not have expected food without attunement");

        // Grant the player lesser attunement to the Sun and tick photosynthesis
        AttunementManager.setAttunement(player, Sources.SUN, AttunementType.PERMANENT, AttunementThreshold.LESSER.getValue());
        PlayerEvents.handlePhotosynthesis(player);

        // Confirm that their food level has regenerated
        var actualFood = player.getFoodData().getFoodLevel();
        helper.assertTrue(actualFood == endFood, "Player does not have expected food with attunement: " + actualFood);

        helper.succeed();
    }

    public void lesser_sun_attunement_does_not_regenerate_food_during_night(GameTestHelper helper) {
        final int startFood = 6;

        // Create a test player
        var player = this.makeMockServerPlayer(helper);

        // Set starting test conditions
        helper.setNight();
        helper.getLevel().tick(() -> true);
        player.getFoodData().setFoodLevel(startFood);
        helper.assertTrue(player.getFoodData().getFoodLevel() == startFood, "Player does not have expected food without attunement");

        // Grant the player lesser attunement to the Sun and tick photosynthesis
        AttunementManager.setAttunement(player, Sources.SUN, AttunementType.PERMANENT, AttunementThreshold.LESSER.getValue());
        PlayerEvents.handlePhotosynthesis(player);

        // Confirm that their food level has regenerated
        helper.assertTrue(player.getFoodData().getFoodLevel() == startFood, "Player does not have expected food with attunement");

        helper.succeed();
    }

    // FIXME Highly intermittent; world time doesn't seem consistent with explicitly set values
    public void greater_sun_attunement_does_not_drop_glow_fields_during_day(GameTestHelper helper) {
        // Create a test player
        var player = this.makeMockServerPlayer(helper, true);
        var playerPos = player.blockPosition();

        // Create a random source that will always trigger a light drop
        var rng = TestRandomSource.builder().setDouble(0D).build();

        // Confirm that there's no glow field present to start
        LogUtils.getLogger().warn("Block light level before attunement during day: {}", helper.getLevel().getBrightness(LightLayer.BLOCK, playerPos));
        helper.assertFalse(helper.getLevel().getBlockState(playerPos).is(BlocksPM.GLOW_FIELD.get()), "Glow field present when it shouldn't be before attunement");

        // Grant the player greater attunement to the Sun and trigger light drop
        AttunementManager.setAttunement(player, Sources.SUN, AttunementType.PERMANENT, AttunementThreshold.GREATER.getValue());
        LogUtils.getLogger().warn("Block light level after attunement during day: {}", helper.getLevel().getBrightness(LightLayer.BLOCK, playerPos));
        PlayerEvents.handleLightDrop(player, rng);

        // Confirm that the glow field is still not present
        LogUtils.getLogger().warn("Block light level after light drop during day: {}", helper.getLevel().getBrightness(LightLayer.BLOCK, playerPos));
        helper.assertFalse(helper.getLevel().getBlockState(playerPos).is(BlocksPM.GLOW_FIELD.get()), "Glow field present when it shouldn't be after attunement");

        helper.succeed();
    }

    // FIXME Highly intermittent; world time doesn't seem consistent with explicitly set values
    public void greater_sun_attunement_drops_glow_fields_during_night(GameTestHelper helper) {
        // Create a test player
        var player = this.makeMockServerPlayer(helper, true);
        var playerPos = player.blockPosition();

        // Create a random source that will always trigger a light drop
        var rng = TestRandomSource.builder().setDouble(0D).build();

        // Confirm that there's no glow field present to start
        LogUtils.getLogger().warn("Block light level before attunement during night: {}", helper.getLevel().getBrightness(LightLayer.BLOCK, playerPos));
        helper.assertFalse(helper.getLevel().getBlockState(playerPos).is(BlocksPM.GLOW_FIELD.get()), "Glow field present when it shouldn't be before attunement");

        // Grant the player greater attunement to the Sun and trigger light drop
        AttunementManager.setAttunement(player, Sources.SUN, AttunementType.PERMANENT, AttunementThreshold.GREATER.getValue());
        LogUtils.getLogger().warn("Block light level after attunement during night: {}", helper.getLevel().getBrightness(LightLayer.BLOCK, playerPos));
        PlayerEvents.handleLightDrop(player, rng);

        // Confirm that there's still no glow field (because it's day time)
        LogUtils.getLogger().warn("Block light level after light drop during night: {}", helper.getLevel().getBrightness(LightLayer.BLOCK, playerPos));
        helper.assertTrue(helper.getLevel().getBlockState(playerPos).is(BlocksPM.GLOW_FIELD.get()), "Glow field missing after attunement");

        helper.succeed();
    }

    public void lesser_moon_attunement_grants_invisibility_chance_on_hurt(GameTestHelper helper) {
        // Create a test player
        var player = this.makeMockServerPlayer(helper);

        // Create a random source that will always trigger
        var rng = TestRandomSource.builder().setDouble(0D).setGaussian(1D).build();

        // Confirm that the player is not granted invisibility without attunement
        CombatEvents.grantInvisibilityOnHurt(player, helper.getLevel(), rng);
        helper.assertFalse(player.hasEffect(MobEffects.INVISIBILITY), "Player has invisibility when they shouldn't");

        // Grant the player lesser attunement to the Moon
        AttunementManager.setAttunement(player, Sources.MOON, AttunementType.PERMANENT, AttunementThreshold.LESSER.getValue());

        // Confirm that the player is granted invisibility with attunement
        CombatEvents.grantInvisibilityOnHurt(player, helper.getLevel(), rng);
        helper.assertTrue(player.hasEffect(MobEffects.INVISIBILITY), "Player does not have invisibility with attunement");

        helper.succeed();
    }

    public void greater_moon_attunement_grants_night_vision(GameTestHelper helper) {
        // Create a test player
        var player = this.makeMockServerPlayer(helper);

        // Confirm that the player has the relevant effect with attunement
        helper.assertFalse(player.hasEffect(MobEffects.NIGHT_VISION), "Player has unexpected effect");

        // Grant the test player greater attunement to the Moon and force processing of attunement buffs
        AttunementManager.setAttunement(player, Sources.MOON, AttunementType.PERMANENT, AttunementThreshold.GREATER.getValue());
        PlayerEvents.applyAttunementBuffs(player);

        // Confirm that the player has the relevant effect with attunement
        helper.assertTrue(player.hasEffect(MobEffects.NIGHT_VISION), "Player does not have expected effect");

        helper.succeed();
    }

    public void lesser_blood_attunement_inflicts_bleeding(GameTestHelper helper) {
        // Create a test player
        var player = this.makeMockServerPlayer(helper, true);

        // Create a test target
        var target = helper.spawnWithNoFreeWill(EntityType.COW, player.position());

        // Confirm that bleeding is not inflicted upon the target without player attunement
        MutableFloat damage1 = new MutableFloat(2F);
        CombatEvents.onEntityHurt(target, player.damageSources().playerAttack(player), damage1::getValue, damage1::setValue);
        helper.assertFalse(target.hasEffect(Objects.requireNonNull(EffectsPM.BLEEDING.getHolder())), "Target has effect before attunement");

        // Grant the test player lesser attunement to the Blood
        AttunementManager.setAttunement(player, Sources.BLOOD, AttunementType.PERMANENT, AttunementThreshold.LESSER.getValue());

        // Confirm that bleeding is inflicted upon the target with player attunement
        MutableFloat damage2 = new MutableFloat(2F);
        CombatEvents.onEntityHurt(target, player.damageSources().playerAttack(player), damage2::getValue, damage2::setValue);
        helper.assertTrue(target.hasEffect(Objects.requireNonNull(EffectsPM.BLEEDING.getHolder())), "Target does not have effect after attunement");

        helper.succeed();
    }

    public void greater_blood_attunement_grants_chance_at_self_healing(GameTestHelper helper) {
        final float startHealth = 5F;

        // Create a test player and start them damaged
        var player = this.makeMockServerPlayer(helper);
        player.setHealth(startHealth);

        // Create a test target
        var target = helper.spawnWithNoFreeWill(EntityType.COW, player.position());

        // Confirm that the player is not healed without attunement
        MutableFloat damage1 = new MutableFloat(12F);   // Do enough damage to force the RNG
        CombatEvents.onEntityHurt(target, player.damageSources().playerAttack(player), damage1::getValue, damage1::setValue);
        helper.assertTrue(player.getHealth() == startHealth, "Player does not have expected health before attunement");

        // Grant the test player greater attunement to the Blood
        AttunementManager.setAttunement(player, Sources.BLOOD, AttunementType.PERMANENT, AttunementThreshold.GREATER.getValue());

        // Confirm that the player is healed by one point after striking with attunement
        MutableFloat damage2 = new MutableFloat(12F);   // Do enough damage to force the RNG
        CombatEvents.onEntityHurt(target, player.damageSources().playerAttack(player), damage2::getValue, damage2::setValue);
        final float expected = startHealth + 1;
        final float actual = player.getHealth();
        helper.assertTrue(actual == expected, "Player does not have expected health after attunement: " + actual);

        helper.succeed();
    }

    // FIXME Intermittent; secondary target sometimes just not taking damage from the chain
    public void lesser_infernal_attunement_fires_hellish_chain_on_attack(GameTestHelper helper) {
        final float damage = 4F;

        // Create a test player
        var player = this.makeMockServerPlayer(helper, true);

        // Create a pair of test targets
        var target1 = helper.spawnWithNoFreeWill(EntityType.COW, player.blockPosition().north());
        var target2 = helper.spawnWithNoFreeWill(EntityType.COW, player.blockPosition().west());

        // Confirm that the secondary target is not harmed when striking the primary target without attunement
        CombatEvents.onAttack(target1, player.damageSources().playerAttack(player), damage);
        helper.assertTrue(target2.getHealth() == target2.getMaxHealth(), "Secondary target hurt before attunement");

        // Grant the test player lesser attunement to the Infernal
        AttunementManager.setAttunement(player, Sources.INFERNAL, AttunementType.PERMANENT, AttunementThreshold.LESSER.getValue());

        // Confirm that the secondary target is harmed for half the damage to the primary target with attunement
        CombatEvents.onAttack(target1, player.damageSources().playerAttack(player), damage);
        final float expected = target2.getMaxHealth() - (0.5F * damage);
        final float actual = target2.getHealth();
        helper.assertTrue(expected == actual, "Secondary target not at expected health after attunement: " + actual);

        helper.succeed();
    }

    public Collection<TestFunction> greater_infernal_attunement_prevents_fire_damage(String templateName) {
        Map<String, Function<RegistryAccess, DamageSource>> testParams = ImmutableMap.<String, Function<RegistryAccess, DamageSource>>builder()
                .put("inFire", registryAccess -> new DamageSources(registryAccess).inFire())
                .put("onFire", registryAccess -> new DamageSources(registryAccess).onFire())
                .put("lava", registryAccess -> new DamageSources(registryAccess).lava())
                .put("hotFloor", registryAccess -> new DamageSources(registryAccess).hotFloor())
                .put("infernalSorcery", registryAccess -> DamageSourcesPM.sorcery(registryAccess, Sources.INFERNAL, null))
                .build();
        return TestUtils.createParameterizedTestFunctions("greater_infernal_attunement_prevents_fire_damage", templateName, testParams, (helper, func) -> {
            // Create a test player
            var player = this.makeMockServerPlayer(helper);

            // Initialize the test damage source
            var damageSource = func.apply(helper.getLevel().registryAccess());

            // Confirm that the attack event is not supposed to be cancelled without attunement
            helper.assertFalse(CombatEvents.onAttack(player, damageSource, 5F), "Damage being cancelled before attunement");

            // Grant the test player greater attunement to the Infernal
            AttunementManager.setAttunement(player, Sources.INFERNAL, AttunementType.PERMANENT, AttunementThreshold.GREATER.getValue());

            // Confirm that the attack event is supposed to be cancelled with attunement
            helper.assertTrue(CombatEvents.onAttack(player, damageSource, 5F), "Damage not being cancelled after attunement");

            helper.succeed();
        });
    }

    public void lesser_void_attunement_reduces_damage_taken(GameTestHelper helper) {
        final float startingDamage = 5F;

        // Create a test player
        var player = this.makeMockServerPlayer(helper);

        // Create a test mob to simulate attacking
        var attacker = helper.spawnWithNoFreeWill(EntityType.WOLF, player.position());

        // Confirm that the player takes normal damage without attunement
        MutableFloat damage1 = new MutableFloat(startingDamage);
        CombatEvents.onEntityHurt(player, player.damageSources().mobAttack(attacker), damage1::getValue, damage1::setValue);
        helper.assertTrue(damage1.getValue() == startingDamage, "Damage modified without attunement");

        // Grant the test player lesser attunement to the Void
        AttunementManager.setAttunement(player, Sources.VOID, AttunementType.PERMANENT, AttunementThreshold.LESSER.getValue());

        // Confirm that the player takes reduced damage with attunement
        MutableFloat damage2 = new MutableFloat(startingDamage);
        CombatEvents.onEntityHurt(player, player.damageSources().mobAttack(attacker), damage2::getValue, damage2::setValue);
        final float actual = damage2.getValue();
        final float expected = 0.9F * startingDamage;
        helper.assertTrue(expected == actual, "Damage not modified as expected after attunement");

        helper.succeed();
    }

    public void greater_void_attunement_increases_damage_dealt(GameTestHelper helper) {
        final float startingDamage = 5F;

        // Create a test player
        var player = this.makeMockServerPlayer(helper);

        // Create a test target
        var target = helper.spawnWithNoFreeWill(EntityType.COW, player.position());

        // Confirm that the target takes normal damage without attunement
        MutableFloat damage1 = new MutableFloat(startingDamage);
        CombatEvents.onEntityHurt(target, player.damageSources().playerAttack(player), damage1::getValue, damage1::setValue);
        helper.assertTrue(damage1.getValue() == startingDamage, "Damage modified without attunement");

        // Grant the test player greater attunement to the Void
        AttunementManager.setAttunement(player, Sources.VOID, AttunementType.PERMANENT, AttunementThreshold.GREATER.getValue());

        // Confirm that the target takes increased damage with attunement
        MutableFloat damage2 = new MutableFloat(startingDamage);
        CombatEvents.onEntityHurt(target, player.damageSources().playerAttack(player), damage2::getValue, damage2::setValue);
        final float actual = damage2.getValue();
        final float expected = 1.25F * startingDamage;
        helper.assertTrue(expected == actual, "Damage not modified as expected after attunement");

        helper.succeed();
    }

    public void lesser_hallowed_attunement_doubles_damage_dealt_to_undead(GameTestHelper helper) {
        final float startingDamage = 5F;

        // Create a test player
        var player = this.makeMockServerPlayer(helper);

        // Create a test undead target
        var target = helper.spawnWithNoFreeWill(EntityType.ZOMBIE, player.position());

        // Confirm that the target takes normal damage without attunement
        MutableFloat damage1 = new MutableFloat(startingDamage);
        CombatEvents.onEntityHurt(target, player.damageSources().playerAttack(player), damage1::getValue, damage1::setValue);
        helper.assertTrue(damage1.getValue() == startingDamage, "Damage modified without attunement");

        // Grant the player lesser attunement to the Hallowed
        AttunementManager.setAttunement(player, Sources.HALLOWED, AttunementType.PERMANENT, AttunementThreshold.LESSER.getValue());

        // Confirm that the target takes increased damage with attunement
        MutableFloat damage2 = new MutableFloat(startingDamage);
        CombatEvents.onEntityHurt(target, player.damageSources().playerAttack(player), damage2::getValue, damage2::setValue);
        final float actual = damage2.getValue();
        final float expected = 2F * startingDamage;
        helper.assertTrue(expected == actual, "Damage not modified as expected after attunement");

        helper.succeed();
    }

    public void greater_hallowed_attunement_prevents_death(GameTestHelper helper) {
        // Create a test player
        var player = this.makeMockServerPlayer(helper);

        // Confirm that the target is not saved without attunement
        helper.assertFalse(CombatEvents.onDeath(player), "Death cancelled before attunement");
        helper.assertTrue(player.getActiveEffects().isEmpty(), "Player has unexpected effects before attunement");
        helper.assertTrue(Services.CAPABILITIES.cooldowns(player).map(c -> c.getRemainingCooldown(IPlayerCooldowns.CooldownType.DEATH_SAVE)).orElse(0L) == 0L,
                "Player incurred a death save cooldown before attunement");

        // Grant the player greater attunement to the Hallowed
        AttunementManager.setAttunement(player, Sources.HALLOWED, AttunementType.PERMANENT, AttunementThreshold.GREATER.getValue());

        // Confirm that the target is saved with attunement
        helper.assertTrue(CombatEvents.onDeath(player), "Death not cancelled after attunement");
        helper.assertTrue(player.hasEffect(MobEffects.REGENERATION), "Player missing regeneration effect after attunement");
        helper.assertTrue(player.hasEffect(MobEffects.ABSORPTION), "Player missing absorption effect after attunement");
        helper.assertTrue(player.hasEffect(Objects.requireNonNull(EffectsPM.WEAKENED_SOUL.getHolder())), "Player missing weakened soul effect after attunement");
        helper.assertTrue(Services.CAPABILITIES.cooldowns(player).map(c -> c.getRemainingCooldown(IPlayerCooldowns.CooldownType.DEATH_SAVE)).orElse(0L) > 0L,
                "Player missing a death save cooldown after attunement");

        helper.succeed();
    }
}
