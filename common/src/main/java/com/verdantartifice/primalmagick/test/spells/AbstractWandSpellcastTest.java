package com.verdantartifice.primalmagick.test.spells;

import com.google.common.collect.ImmutableMap;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.items.wands.AbstractWandItem;
import com.verdantartifice.primalmagick.common.items.wands.IHasWandComponents;
import com.verdantartifice.primalmagick.common.sources.Sources;
import com.verdantartifice.primalmagick.common.spells.SpellPackage;
import com.verdantartifice.primalmagick.common.spells.SpellPropertiesPM;
import com.verdantartifice.primalmagick.common.spells.payloads.SpellPayloadType;
import com.verdantartifice.primalmagick.common.spells.payloads.SpellPayloadsPM;
import com.verdantartifice.primalmagick.common.spells.vehicles.TouchSpellVehicle;
import com.verdantartifice.primalmagick.common.wands.WandCap;
import com.verdantartifice.primalmagick.common.wands.WandCore;
import com.verdantartifice.primalmagick.common.wands.WandGem;
import com.verdantartifice.primalmagick.test.AbstractBaseTest;
import com.verdantartifice.primalmagick.test.TestUtils;
import net.minecraft.gametest.framework.TestFunction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

import java.util.Collection;
import java.util.Map;

public class AbstractWandSpellcastTest extends AbstractBaseTest {
    protected ItemStack getTestWand() {
        return IHasWandComponents.setWandComponents(ItemsPM.MODULAR_WAND.get().getDefaultInstance(), WandCore.HEARTWOOD, WandCap.IRON, WandGem.APPRENTICE);
    }

    public Collection<TestFunction> damage_spells_deduct_mana_from_wand(String templateName) {
        Map<String, SpellPayloadType<?>> testParams = ImmutableMap.<String, SpellPayloadType<?>>builder()
                .put("earth_damage", SpellPayloadsPM.EARTH_DAMAGE.get())
                .put("sea_damage", SpellPayloadsPM.FROST_DAMAGE.get())
                .put("sky_damage", SpellPayloadsPM.LIGHTNING_DAMAGE.get())
                .put("sun_damage", SpellPayloadsPM.SOLAR_DAMAGE.get())
                .put("moon_damage", SpellPayloadsPM.LUNAR_DAMAGE.get())
                .put("blood_damage", SpellPayloadsPM.BLOOD_DAMAGE.get())
                .put("infernal_damage", SpellPayloadsPM.FLAME_DAMAGE.get())
                .put("void_damage", SpellPayloadsPM.VOID_DAMAGE.get())
                .put("hallowed_damage", SpellPayloadsPM.HOLY_DAMAGE.get())
                .build();
        return TestUtils.createParameterizedTestFunctions("damage_spells_deduct_mana_from_wand", templateName, testParams, (helper, payloadType) -> {
            var player = this.makeMockServerPlayer(helper, true);

            // Create a full wand for testing
            var wandStack = this.getTestWand();
            var wand = assertInstanceOf(helper, wandStack.getItem(), AbstractWandItem.class, "Wand stack is not a wand as expected");
            final int maxWandMana = wand.getMaxMana(wandStack);
            Sources.getAll().forEach(s -> {
                wand.addMana(wandStack, s, maxWandMana);
                helper.assertValueEqual(wand.getMana(wandStack, s), maxWandMana, "Starting wand mana for " + s.getId());
            });
            player.setItemInHand(InteractionHand.MAIN_HAND, wandStack);

            // Create a package for the tested spell payload type
            final int spellPower = 3;
            final int spellDuration = 3;
            var payloadInstance = payloadType.instanceSupplier().get();
            var spellPackage = SpellPackage.builder().name("Test Spell")
                    .vehicle().type(TouchSpellVehicle.INSTANCE).end()
                    .payload().type(payloadInstance).with(SpellPropertiesPM.POWER.get(), spellPower).with(SpellPropertiesPM.DURATION.get(), spellDuration).end()
                    .build();

            // Cast the spell
            helper.assertTrue(wand.addSpell(wandStack, spellPackage), "Spell was not added to the wand");
            helper.assertTrue(wand.setActiveSpellIndex(wandStack, 0), "Spell was not set to active");
            var result = wand.use(helper.getLevel(), player, InteractionHand.MAIN_HAND);
            helper.assertTrue(result.getResult().consumesAction(), "Wand use result was not a success");

            // Confirm that the correct amount of each source of mana was deducted from the wand
            var spellCost = spellPackage.getManaCost();
            Sources.getAll().forEach(s -> {
                final double costModifier = wand.getTotalCostModifier(wandStack, player, s, helper.getLevel().registryAccess());
                final int consumedRealMana = spellCost.getAmount(s);
                final int consumedCentimana = 100 * consumedRealMana;
                final int expectedCentimana = maxWandMana - (int)(consumedCentimana * costModifier);
                helper.assertValueEqual(wand.getMana(wandStack, s), expectedCentimana, "Final wand mana for " + s.getId());
            });

            helper.succeed();
        });
    }
}
