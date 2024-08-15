package com.verdantartifice.primalmagick.test.ftux;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.blocks.BlocksPM;
import com.verdantartifice.primalmagick.common.research.ResearchEntries;
import com.verdantartifice.primalmagick.common.research.ResearchManager;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.gametest.GameTestHolder;

@GameTestHolder(PrimalMagick.MODID)
public class FtuxTest {
    protected static final Logger LOGGER = LogManager.getLogger();
    
    @GameTest(template = "primalmagick:test/empty3x3x3")
    public static void ancientFontPrimesDream(GameTestHelper helper) {
        @SuppressWarnings("removal")
        Player player = helper.makeMockServerPlayerInLevel();
        player.setPos(Vec3.atBottomCenterOf(helper.absolutePos(BlockPos.ZERO)));
        helper.assertFalse(ResearchManager.isResearchComplete(player, ResearchEntries.FOUND_SHRINE), "Found Shrine research already present on new player");

        BlockPos fontPos = new BlockPos(1, 0, 1);
        helper.setBlock(fontPos, BlocksPM.ANCIENT_FONT_EARTH.get());
        helper.assertBlockState(fontPos, state -> {
            return state.is(BlocksPM.ANCIENT_FONT_EARTH.get());
        }, () -> "Test font not found!");
        
        helper.succeedWhen(() -> {
            helper.assertTrue(ResearchManager.isResearchComplete(player, ResearchEntries.FOUND_SHRINE), "Found Shrine research not complete");
        });
    }
}
