package com.verdantartifice.primalmagick.test.spells;

import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.items.wands.AbstractWandItem;
import com.verdantartifice.primalmagick.common.items.wands.IHasWandComponents;
import com.verdantartifice.primalmagick.common.research.ResearchDisciplines;
import com.verdantartifice.primalmagick.common.sources.Sources;
import com.verdantartifice.primalmagick.common.spells.SpellPackage;
import com.verdantartifice.primalmagick.common.spells.SpellPropertiesPM;
import com.verdantartifice.primalmagick.common.spells.payloads.SpellPayloadType;
import com.verdantartifice.primalmagick.common.spells.vehicles.TouchSpellVehicle;
import com.verdantartifice.primalmagick.common.stats.ExpertiseManager;
import com.verdantartifice.primalmagick.common.wands.WandCap;
import com.verdantartifice.primalmagick.common.wands.WandCore;
import com.verdantartifice.primalmagick.common.wands.WandGem;
import com.verdantartifice.primalmagick.test.AbstractBaseTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

public class WandSpellcastTests extends AbstractBaseTest {
    protected static ItemStack getTestWand() {
        return IHasWandComponents.setWandComponents(ItemsPM.MODULAR_WAND.get().getDefaultInstance(), WandCore.HEARTWOOD, WandCap.IRON, WandGem.APPRENTICE);
    }

    public static void damage_spells_deduct_mana_from_wand_and_award_expertise(GameTestHelper helper, SpellPayloadType<?> payloadType) {
        var player = makeMockServerPlayer(helper, true);
        var expOpt = ExpertiseManager.getValue(player, ResearchDisciplines.SORCERY);
        assertTrue(helper, expOpt.isPresent(), "Sorcery expertise not found");
        assertValueEqual(helper, expOpt.get(), 0, "Starting sorcery expertise");

        // Create a full wand for testing
        var wandStack = getTestWand();
        var wand = assertInstanceOf(helper, wandStack.getItem(), AbstractWandItem.class, "Wand stack is not a wand as expected");
        Sources.getAll().forEach(s -> {
            final int maxWandMana = wand.getMaxMana(wandStack, s);
            wand.addMana(wandStack, s, maxWandMana);
            assertValueEqual(helper, wand.getMana(wandStack, s), maxWandMana, "Starting wand mana for " + s.getId());
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
        assertTrue(helper, wand.addSpell(wandStack, spellPackage), "Spell was not added to the wand");
        assertTrue(helper, wand.setActiveSpellIndex(wandStack, 0), "Spell was not set to active");
        var result = wand.use(helper.getLevel(), player, InteractionHand.MAIN_HAND);
        assertTrue(helper, result.consumesAction(), "Wand use result was not a success");

        // Confirm that the correct amount of each source of mana was deducted from the wand
        var spellCost = spellPackage.getManaCost();
        Sources.getAll().forEach(s -> {
            final int maxWandMana = wand.getMaxMana(wandStack, s);
            final int consumedCentimana = spellCost.getAmount(s);
            final int finalCost = wand.getModifiedCost(wandStack, player, s, consumedCentimana, helper.getLevel().registryAccess());
            final int expectedCentimana = maxWandMana - finalCost;
            assertValueEqual(helper, wand.getMana(wandStack, s), expectedCentimana, "Final wand mana for " + s.getId());
        });

        // Confirm that the correct amount of sorcery expertise was awarded
        var expectedExpertise = spellCost.getManaSize() / 100;
        expOpt = ExpertiseManager.getValue(player, ResearchDisciplines.SORCERY);
        assertTrue(helper, expOpt.isPresent(), "Final sorcery expertise not found");
        assertValueEqual(helper, expOpt.get(), expectedExpertise, "Final sorcery expertise");

        helper.succeed();
    }
}
