package com.verdantartifice.primalmagick.test.crafting;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.common.blocks.BlocksPM;
import com.verdantartifice.primalmagick.common.items.ItemRegistration;
import com.verdantartifice.primalmagick.common.menus.RunecarvingTableMenu;
import com.verdantartifice.primalmagick.common.research.ResearchDisciplines;
import com.verdantartifice.primalmagick.common.research.ResearchEntries;
import com.verdantartifice.primalmagick.common.research.ResearchManager;
import com.verdantartifice.primalmagick.common.stats.ExpertiseManager;
import com.verdantartifice.primalmagick.common.tiles.crafting.RunecarvingTableTileEntity;
import com.verdantartifice.primalmagick.test.AbstractBaseTest;
import com.verdantartifice.primalmagick.test.TestUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.gametest.GameTestHolder;

@GameTestHolder(Constants.MOD_ID + ".runecarving")
public class RunecarvingTest extends AbstractBaseTest {
    @GameTest(template = TestUtils.DEFAULT_TEMPLATE)
    public static void craft_works(GameTestHelper helper) {
        // Create a test player with the research needed for basic runecarving
        var player = helper.makeMockServerPlayer();
        ResearchManager.forceGrantWithAllParents(player, ResearchEntries.BASIC_RUNEWORKING);
        helper.assertTrue(ExpertiseManager.getValue(player, ResearchDisciplines.RUNEWORKING).orElse(-1) == 0, "Expected starting expertise is not zero for test player");
        
        // Place a runecarving table
        BlockPos tablePos = new BlockPos(1, 1, 1);
        helper.setBlock(tablePos, BlocksPM.RUNECARVING_TABLE.get());
        helper.assertBlockPresent(BlocksPM.RUNECARVING_TABLE.get(), tablePos);
        
        // Populate the runecarving table with materials
        var baseTile = helper.getBlockEntity(tablePos);
        var tile = assertInstanceOf(helper, baseTile, RunecarvingTableTileEntity.class, "Block entity not of expected type");
        tile.setItem(0, 0, new ItemStack(Items.STONE_SLAB));
        helper.assertTrue(tile.getItem(0, 0).is(Items.STONE_SLAB), "Stone slab material not properly set");
        tile.setItem(0, 1, new ItemStack(Items.LAPIS_LAZULI));
        helper.assertTrue(tile.getItem(0, 1).is(Items.LAPIS_LAZULI), "Lapis lazuli material not properly set");
        
        // Open the block entity menu and select the first (and only) recipe
        player.openMenu(tile, tablePos);
        var menu = assertInstanceOf(helper, player.containerMenu, RunecarvingTableMenu.class, "Menu not of expected type");
        helper.assertTrue(menu.getRecipeListSize() == 1, "Recipe list not as expected in runecarving menu");
        helper.assertTrue(menu.clickMenuButton(player, 0), "Recipe selection failed");
        
        // Take the result that should be there and confirm it's the right type of rune
        var output = menu.quickMoveStack(player, 2);
        helper.assertTrue(output.is(ItemRegistration.RUNE_UNATTUNED.get()), "Output item not of expected type");
        
        // Confirm that crafting materials were consumed
        helper.assertTrue(tile.getItem(0, 0).isEmpty(), "Stone slab material stack not empty");
        helper.assertTrue(tile.getItem(0, 1).isEmpty(), "Lapis lazuli material stack not empty");
        
        // Confirm that expertise was granted to the player
        helper.assertTrue(ExpertiseManager.getValue(player, ResearchDisciplines.RUNEWORKING).orElse(-1) == 5, "Final expertise is not as expected for test player");
        
        helper.succeed();
    }
}
