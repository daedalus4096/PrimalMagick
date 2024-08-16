package com.verdantartifice.primalmagick.test.ftux;

import java.util.Collection;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.ImmutableMap;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.blocks.BlocksPM;
import com.verdantartifice.primalmagick.common.books.BookLanguagesPM;
import com.verdantartifice.primalmagick.common.books.BooksPM;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.items.books.StaticBookItem;
import com.verdantartifice.primalmagick.common.research.ResearchEntries;
import com.verdantartifice.primalmagick.common.research.ResearchManager;
import com.verdantartifice.primalmagick.common.tags.ItemTagsPM;
import com.verdantartifice.primalmagick.common.util.InventoryUtils;
import com.verdantartifice.primalmagick.test.TestUtils;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestGenerator;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.TestFunction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.gametest.GameTestHolder;

@GameTestHolder(PrimalMagick.MODID)
public class FtuxTest {
    protected static final Logger LOGGER = LogManager.getLogger();
    
    @GameTestGenerator
    public static Collection<TestFunction> fontDiscoveryTests() {
        Map<String, Block> testParams = ImmutableMap.<String, Block>builder()
                .put("earth", BlocksPM.ANCIENT_FONT_EARTH.get())
                .put("sea", BlocksPM.ANCIENT_FONT_SEA.get())
                .put("sky", BlocksPM.ANCIENT_FONT_SKY.get())
                .put("sun", BlocksPM.ANCIENT_FONT_SUN.get())
                .put("moon", BlocksPM.ANCIENT_FONT_MOON.get())
                .build();
        return TestUtils.createParameterizedTestFunctions("fontDiscoveryTests", testParams, (helper, block) -> {
            // Create a player in the level and confirm that they start out not having found a shrine
            @SuppressWarnings("removal")
            ServerPlayer player = helper.makeMockServerPlayerInLevel();
            player.setPos(Vec3.atBottomCenterOf(helper.absolutePos(BlockPos.ZERO)));
            helper.assertFalse(ResearchManager.isResearchComplete(player, ResearchEntries.FOUND_SHRINE), "Found Shrine research already present on new player");

            // Place an ancient font near the player
            BlockPos fontPos = new BlockPos(1, 1, 1);
            helper.setBlock(fontPos, block);
            helper.assertBlockState(fontPos, state -> {
                return state.is(block);
            }, () -> "Test font not found!");
            
            // Succeed once the player has been marked as having found a shrine
            helper.succeedWhen(() -> {
                helper.assertTrue(ResearchManager.isResearchComplete(player, ResearchEntries.FOUND_SHRINE), "Found Shrine research not complete");
                helper.getLevel().getServer().getPlayerList().remove(player);
            });
        });
    }
    
    @GameTest(template = "primalmagick:test/floor5x5x5", timeoutTicks = 150)
    public static void sleepingAfterShrineGrantsDream(GameTestHelper helper) {
        // Create a player who's found a shrine and is primed for the dream, but hasn't had it yet
        @SuppressWarnings("removal")
        ServerPlayer player = helper.makeMockServerPlayerInLevel();
        BlockPos playerPos = new BlockPos(0, 2, 0);
        player.setPos(Vec3.atBottomCenterOf(helper.absolutePos(playerPos)));
        helper.assertTrue(ResearchManager.completeResearch(player, ResearchEntries.FOUND_SHRINE), "Failed to grant prerequisite research");
        helper.assertFalse(ResearchManager.isResearchComplete(player, ResearchEntries.GOT_DREAM), "Created player already marked as having had the dream");
        helper.assertTrue(player.getInventory().isEmpty(), "Fresh player inventory is not empty");
        
        // Place a bed and sleep in it
        BlockPos bedPos = new BlockPos(2, 2, 2);
        TestUtils.placeBed(helper, bedPos);
        helper.setNight();
        player.startSleepInBed(helper.absolutePos(bedPos));
        player.stopSleeping();
        
        // Succeed once the player has been marked as having had the dream and received the dream journal
        helper.succeedWhen(() -> {
            helper.assertTrue(ResearchManager.isResearchComplete(player, ResearchEntries.GOT_DREAM), "Got Dream research not complete");
            NonNullList<ItemStack> stacks = InventoryUtils.find(player, ItemTagsPM.STATIC_BOOKS);
            helper.assertTrue(stacks.size() == 1, "No potential Dream Journals found");
            helper.assertTrue(stacks.stream().anyMatch(stack -> {
                return StaticBookItem.getBookId(stack).filter(id -> BooksPM.DREAM_JOURNAL.equals(id)).isPresent() &&
                        StaticBookItem.getBookLanguageId(stack).filter(id -> BookLanguagesPM.DEFAULT.equals(id)).isPresent() &&
                        StaticBookItem.getAuthor(stack).equals(player.getName());
            }), "Dream Journal components are not a match to expected");
            helper.getLevel().getServer().getPlayerList().remove(player);
        });
    }
}
