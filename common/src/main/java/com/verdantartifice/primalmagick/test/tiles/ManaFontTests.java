package com.verdantartifice.primalmagick.test.tiles;

import com.verdantartifice.primalmagick.common.blocks.BlocksPM;
import com.verdantartifice.primalmagick.common.blocks.mana.AbstractManaFontBlock;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.sources.Sources;
import com.verdantartifice.primalmagick.common.tiles.mana.AbstractManaFontTileEntity;
import com.verdantartifice.primalmagick.common.wands.IWand;
import com.verdantartifice.primalmagick.test.AbstractBaseTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

public class ManaFontTests extends AbstractBaseTest {
    private static ItemStack getWandTestStack() {
        // TODO Add support for other test items, like modular wands, modular staves, and warded armor
        return ItemsPM.MUNDANE_WAND.get().getDefaultInstance();
    }

    private static AbstractManaFontBlock getFontTestBlock() {
        // TODO Add support for other fonts
        return BlocksPM.ANCIENT_FONT_EARTH.get();
    }

    public static void mana_font_siphoned_by_wand(GameTestHelper helper) {
        var wandStack = getWandTestStack();
        var block = getFontTestBlock();

        // Create a test player in the level and put a wand in their hand
        var player = makeMockServerPlayer(helper, true);
        var wand = assertInstanceOf(helper, wandStack.getItem(), IWand.class, "Wand stack not a wand as expected");
        player.setItemInHand(InteractionHand.MAIN_HAND, wandStack);

        // Place the font block in the world and fill it
        var fontPos = BlockPos.ZERO;
        helper.setBlock(fontPos, block);
        helper.assertBlockState(fontPos, state -> state.is(block), state -> Component.literal("Font not placed correctly"));
        var fontTile = helper.getBlockEntity(fontPos, AbstractManaFontTileEntity.class);
        fontTile.setMana(fontTile.getManaCapacity());

        // Confirm the initial wand and font state
        Sources.getAll().forEach(s -> assertValueEqual(helper, wand.getMana(wandStack, s), 0, "Initial wand mana for " + s.getId()));
        assertValueEqual(helper, fontTile.getMana(), fontTile.getManaCapacity(), "Initial font mana");

        // Siphon a bit of mana from the font
        fontTile.doSiphon(wandStack, helper.getLevel(), player, player.getEyePosition());

        // Confirm that the correct amount of mana was siphoned from the font to the wand
        var expectedSiphon = wand.getSiphonAmount(wandStack);
        Sources.getAll().forEach(s -> {
            var expectedMana = s.equals(block.getSource()) ? expectedSiphon : 0;
            assertValueEqual(helper, wand.getMana(wandStack, s), expectedMana, "Final wand mana for " + s.getId());
        });
        var fontCentimanaCapacity = fontTile.getManaCapacity();
        var actualCentimana = fontTile.getMana();
        assertValueEqual(helper, actualCentimana, fontCentimanaCapacity - expectedSiphon, "Final font mana");

        helper.succeed();
    }

    public static void mana_font_recharges(GameTestHelper helper) {
        var block = getFontTestBlock();

        // Place the font block in the world and ensure it's empty
        var fontPos = BlockPos.ZERO;
        helper.setBlock(fontPos, block);
        helper.assertBlockState(fontPos, state -> state.is(block), state -> Component.literal("Font not placed correctly"));
        var fontTile = helper.getBlockEntity(fontPos, AbstractManaFontTileEntity.class);
        fontTile.setMana(0);
        assertValueEqual(helper, fontTile.getMana(), 0, "Initial font mana");

        // Trigger a recharge tick for the font
        fontTile.doRecharge();

        // Confirm that the font recharged one tick's worth of mana
        assertValueEqual(helper, fontTile.getMana(), fontTile.getManaRechargedPerTick(), "Final font mana");
    }
}
