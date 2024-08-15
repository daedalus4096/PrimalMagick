package com.verdantartifice.primalmagick.test.ftux;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.ImmutableMap;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.blocks.BlocksPM;
import com.verdantartifice.primalmagick.common.research.ResearchEntries;
import com.verdantartifice.primalmagick.common.research.ResearchManager;
import com.verdantartifice.primalmagick.test.TestOptions;
import com.verdantartifice.primalmagick.test.TestUtils;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestGenerator;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.TestFunction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
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
    
    @GameTestGenerator
    public static Collection<TestFunction> fontDiscoveryTests() {
        List<TestFunction> retVal = new ArrayList<>();
        Map<String, Block> testParams = ImmutableMap.<String, Block>builder()
                .put("earth", BlocksPM.ANCIENT_FONT_EARTH.get())
                .put("sea", BlocksPM.ANCIENT_FONT_SEA.get())
                .put("sky", BlocksPM.ANCIENT_FONT_SKY.get())
                .put("sun", BlocksPM.ANCIENT_FONT_SUN.get())
                .put("moon", BlocksPM.ANCIENT_FONT_MOON.get())
                .build();
        testParams.forEach((name, block) -> {
            retVal.add(TestUtils.createNestedTestFunction(name, TestOptions.builder().template("primalmagick:test/empty3x3x3").build(), helper -> {
                @SuppressWarnings("removal")
                Player player = helper.makeMockServerPlayerInLevel();
                player.setPos(Vec3.atBottomCenterOf(helper.absolutePos(BlockPos.ZERO)));
                helper.assertFalse(ResearchManager.isResearchComplete(player, ResearchEntries.FOUND_SHRINE), "Found Shrine research already present on new player");

                BlockPos fontPos = new BlockPos(1, 0, 1);
                helper.setBlock(fontPos, block);
                helper.assertBlockState(fontPos, state -> {
                    return state.is(block);
                }, () -> "Test font not found!");
                
                helper.succeedWhen(() -> {
                    helper.assertTrue(ResearchManager.isResearchComplete(player, ResearchEntries.FOUND_SHRINE), "Found Shrine research not complete");
                });
            }));
        });
        LOGGER.debug("Generated {} font discovery tests", retVal.size());
        return retVal;
    }
}
