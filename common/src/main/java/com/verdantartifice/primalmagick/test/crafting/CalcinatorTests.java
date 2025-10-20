package com.verdantartifice.primalmagick.test.crafting;

import com.verdantartifice.primalmagick.common.affinities.AffinityManager;
import com.verdantartifice.primalmagick.common.blocks.BlocksPM;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.research.ResearchEntries;
import com.verdantartifice.primalmagick.common.research.ResearchManager;
import com.verdantartifice.primalmagick.common.sources.SourceList;
import com.verdantartifice.primalmagick.common.tiles.crafting.AbstractCalcinatorTileEntity;
import com.verdantartifice.primalmagick.test.AbstractBaseTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.concurrent.CompletableFuture;

public class CalcinatorTests extends AbstractBaseTest {
    public static void calcinator_works(GameTestHelper helper, boolean playerPresent) {
        // Create a test player with the research needed for basic alchemy
        var player = makeMockServerPlayer(helper, playerPresent);
        ResearchManager.forceGrantWithAllParents(player, ResearchEntries.BASIC_ALCHEMY);

        // Pre-cache the affinities for cobblestone so that they're ready for the calcinator later
        AffinityManager.getInstance().setCachedItemResult(new ItemStack(Items.COBBLESTONE), CompletableFuture.completedFuture(SourceList.builder().withEarth(5).build()));

        // Place a basic calcinator and set the player to be its owner
        BlockPos calcinatorPos = new BlockPos(1, 1, 1);
        helper.setBlock(calcinatorPos, BlocksPM.CALCINATOR_BASIC.get());
        helper.assertBlockPresent(BlocksPM.CALCINATOR_BASIC.get(), calcinatorPos);
        var calcinator = helper.getBlockEntity(calcinatorPos, AbstractCalcinatorTileEntity.class);
        calcinator.setTileOwner(player);

        // Add a cobblestone to the calcinator's input slot and force immediate calcination
        calcinator.setItem(0, 0, new ItemStack(Items.COBBLESTONE));
        assertTrue(helper, calcinator.getItem(0, 0).is(Items.COBBLESTONE), "Input cobblestone not set correctly");
        calcinator.setItem(1, 0, new ItemStack(Items.CHARCOAL));
        assertTrue(helper, calcinator.getItem(1, 0).is(Items.CHARCOAL), "Input fuel not set correctly");
        for (int index = 0; index < 9; index++) {
            assertTrue(helper, calcinator.getItem(2, index).isEmpty(), "Output slot " + index + " is not empty before calcination");
        }
        calcinator.doCalcination();

        // Confirm that the input materials were consumed and that the appropriate essence has been generated in the output
        assertTrue(helper, calcinator.getItem(0, 0).isEmpty(), "Input item stack not empty after calcination");
        var outputStack = calcinator.getItem(2, 0);
        assertTrue(helper, outputStack.is(ItemsPM.ESSENCE_DUST_EARTH.get()), "Output is not Earth Dust as expected");
        assertTrue(helper, outputStack.getCount() == 1, "Output stack count is not one as expected");

        helper.succeed();
    }
}
