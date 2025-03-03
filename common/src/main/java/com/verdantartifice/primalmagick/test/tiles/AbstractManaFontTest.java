package com.verdantartifice.primalmagick.test.tiles;

import com.google.common.collect.ImmutableMap;
import com.verdantartifice.primalmagick.common.blocks.mana.AbstractManaFontBlock;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.items.wands.IHasWandComponents;
import com.verdantartifice.primalmagick.common.sources.Sources;
import com.verdantartifice.primalmagick.common.tiles.mana.AbstractManaFontTileEntity;
import com.verdantartifice.primalmagick.common.wands.IWand;
import com.verdantartifice.primalmagick.common.wands.WandCap;
import com.verdantartifice.primalmagick.common.wands.WandCore;
import com.verdantartifice.primalmagick.common.wands.WandGem;
import com.verdantartifice.primalmagick.platform.Services;
import com.verdantartifice.primalmagick.test.AbstractBaseTest;
import com.verdantartifice.primalmagick.test.TestUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.TestFunction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

public class AbstractManaFontTest extends AbstractBaseTest {
    public Collection<TestFunction> font_siphoned_by_wand(String templateName) {
        Map<String, Item> testParams1 = ImmutableMap.<String, Item>builder()
                .put("mundane_wand", ItemsPM.MUNDANE_WAND.get())
                .put("modular_wand", ItemsPM.MODULAR_WAND.get())
                .put("modular_staff", ItemsPM.MODULAR_STAFF.get())
                .build();
        Map<String, AbstractManaFontBlock> testParams2 = AbstractManaFontBlock.getAll().stream().collect(Collectors.toMap(b -> Services.BLOCKS_REGISTRY.getKey(b).getPath(), b -> b));
        return TestUtils.createDualParameterizedTestFunctions("font_drained_by_wand", templateName, testParams1, testParams2, (helper, wandItem, block) -> {
            // Create a test player in the level and put a wand in their hand
            var player = this.makeMockServerPlayer(helper, true);
            var wandStack = IHasWandComponents.setWandComponents(wandItem.getDefaultInstance(), WandCore.HEARTWOOD, WandCap.IRON, WandGem.APPRENTICE);
            var wand = assertInstanceOf(helper, wandStack.getItem(), IWand.class, "Wand stack not a wand as expected");
            player.setItemInHand(InteractionHand.MAIN_HAND, wandStack);

            // Place the font block in the world and fill it
            var fontPos = BlockPos.ZERO;
            helper.setBlock(fontPos, block);
            helper.assertBlockState(fontPos, state -> state.is(block), () -> "Font not placed correctly");
            var fontTile = helper.<AbstractManaFontTileEntity>getBlockEntity(fontPos);
            fontTile.setMana(fontTile.getManaCapacity());

            // Confirm the initial wand and font state
            Sources.getAll().forEach(s -> helper.assertValueEqual(wand.getMana(wandStack, s), 0, "Initial wand mana for " + s.getId()));
            helper.assertValueEqual(fontTile.getMana(), fontTile.getManaCapacity(), "Initial font mana");

            // Siphon a bit of mana from the font
            fontTile.doSiphon(wandStack, helper.getLevel(), player, player.getEyePosition());

            // Confirm that the correct amount of mana was siphoned from the font to the wand
            var expectedSiphon = wand.getSiphonAmount(wandStack);
            Sources.getAll().forEach(s -> {
                var expectedMana = s.equals(block.getSource()) ? expectedSiphon : 0;
                helper.assertValueEqual(wand.getMana(wandStack, s), expectedMana, "Final wand mana for " + s.getId());
            });
            var fontRealManaCapacity = fontTile.getManaCapacity();
            var fontCentimanaCapacity = 100 * fontRealManaCapacity;
            var actualRealMana = fontTile.getMana();
            var actualCentimana = 100 * actualRealMana;
            helper.assertValueEqual(actualCentimana, fontCentimanaCapacity - expectedSiphon, "Final font mana");

            helper.succeed();
        });
    }
}
