package com.verdantartifice.primalmagick.test.attunements;

import com.mojang.logging.LogUtils;
import com.verdantartifice.primalmagick.common.attunements.AttunementAttributeModifiers;
import com.verdantartifice.primalmagick.common.attunements.AttunementManager;
import com.verdantartifice.primalmagick.common.attunements.AttunementThreshold;
import com.verdantartifice.primalmagick.common.attunements.AttunementType;
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
import com.verdantartifice.primalmagick.test.TestUtils;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.TestFunction;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

public class AbstractAttunementTest extends AbstractBaseTest {
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
}
