package com.verdantartifice.primalmagick.test.crafting;

import com.verdantartifice.primalmagick.common.blocks.BlocksPM;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.menus.ArcaneWorkbenchMenu;
import com.verdantartifice.primalmagick.common.research.ResearchDisciplines;
import com.verdantartifice.primalmagick.common.research.ResearchEntries;
import com.verdantartifice.primalmagick.common.research.ResearchManager;
import com.verdantartifice.primalmagick.common.sources.Sources;
import com.verdantartifice.primalmagick.common.stats.ExpertiseManager;
import com.verdantartifice.primalmagick.common.wands.IWand;
import com.verdantartifice.primalmagick.test.AbstractBaseTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

public class AbstractArcaneWorkbenchTest extends AbstractBaseTest {
    public void arcane_workbench_craft_works(GameTestHelper helper) {
        // Create a test player with the research needed for mana salts
        var player = this.makeMockServerPlayer(helper);
        ResearchManager.forceGrantWithAllParents(player, ResearchEntries.MANA_SALTS);
        helper.assertTrue(ExpertiseManager.getValue(player, ResearchDisciplines.MANAWEAVING).orElse(-1) == 0, "Expected starting expertise is not zero for test player");

        // Place an arcane workbench
        BlockPos tablePos = new BlockPos(1, 1, 1);
        helper.setBlock(tablePos, BlocksPM.ARCANE_WORKBENCH.get());
        helper.assertBlockPresent(BlocksPM.ARCANE_WORKBENCH.get(), tablePos);

        // Open the workbench menu
        var menuProvider = new MenuProvider() {
            @Override
            public @NotNull AbstractContainerMenu createMenu(int windowId, Inventory inv, Player player) {
                return new ArcaneWorkbenchMenu(windowId, inv, ContainerLevelAccess.create(helper.getLevel(), helper.absolutePos(tablePos)));
            }

            @Override
            public @NotNull Component getDisplayName() {
                return Component.literal("Arcane Workbench");
            }
        };
        player.openMenu(menuProvider);
        var menu = assertInstanceOf(helper, player.containerMenu, ArcaneWorkbenchMenu.class, "Menu not of expected type");

        // Populate the arcane workbench with materials
        menu.getSlots().get(1).safeInsert(new ItemStack(Items.REDSTONE));
        menu.getSlots().get(2).safeInsert(new ItemStack(ItemsPM.REFINED_SALT.get()));
        menu.getSlots().get(3).safeInsert(new ItemStack(ItemsPM.ESSENCE_DUST_EARTH.get()));

        // Populate the arcane workbench with a full wand for mana
        ItemStack wandStack = ItemsPM.MUNDANE_WAND.get().getDefaultInstance();
        IWand wand = assertInstanceOf(helper, wandStack.getItem(), IWand.class, "Wand not of expected type");
        var maxCentimana = wand.getMaxMana(wandStack);
        var maxRealMana = maxCentimana / 100;
        Sources.getAll().forEach(s -> wand.addRealMana(wandStack, s, maxRealMana));
        menu.getSlots().get(10).safeInsert(wandStack);

        // Take the result that should be there and confirm it's the right type of item
        var output = menu.quickMoveStack(player, 0);
        helper.assertTrue(output.is(ItemsPM.MANA_SALTS.get()), "Output item not of expected type");

        // Confirm that crafting materials were consumed
        helper.assertFalse(menu.getSlots().get(1).hasItem(), "Redstone material stack not empty");
        helper.assertFalse(menu.getSlots().get(2).hasItem(), "Salt material stack not empty");
        helper.assertFalse(menu.getSlots().get(3).hasItem(), "Essence material stack not empty");

        // Confirm that mana was deducted from the wand correctly
        // FIXME Don't use hard-coded mana values for expectations
        helper.assertValueEqual(wand.getMana(wandStack, Sources.EARTH), 1900, "Wand remaining earth mana");
        helper.assertValueEqual(wand.getMana(wandStack, Sources.SEA), 1900, "Wand remaining sea mana");
        helper.assertValueEqual(wand.getMana(wandStack, Sources.SKY), 1900, "Wand remaining sky mana");
        helper.assertValueEqual(wand.getMana(wandStack, Sources.SUN), 1900, "Wand remaining sun mana");
        helper.assertValueEqual(wand.getMana(wandStack, Sources.MOON), 1900, "Wand remaining moon mana");
        helper.assertValueEqual(wand.getMana(wandStack, Sources.BLOOD), 2500, "Wand remaining blood mana");
        helper.assertValueEqual(wand.getMana(wandStack, Sources.INFERNAL), 2500, "Wand remaining infernal mana");
        helper.assertValueEqual(wand.getMana(wandStack, Sources.VOID), 2500, "Wand remaining void mana");
        helper.assertValueEqual(wand.getMana(wandStack, Sources.HALLOWED), 2500, "Wand remaining hallowed mana");

        // Confirm that expertise was granted to the player
        helper.assertTrue(ExpertiseManager.getValue(player, ResearchDisciplines.MANAWEAVING).orElse(-1) == 5, "Final expertise is not as expected for test player");

        helper.succeed();
    }
}
