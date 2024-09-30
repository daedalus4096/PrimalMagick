package com.verdantartifice.primalmagick.test.research;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.common.research.ResearchEntries;
import com.verdantartifice.primalmagick.common.research.ResearchManager;
import com.verdantartifice.primalmagick.test.TestUtils;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.minecraftforge.gametest.GameTestHolder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@GameTestHolder(Constants.MOD_ID + ".research")
public class ResearchTest {
    protected static final Logger LOGGER = LogManager.getLogger();
    
    @GameTest(template = TestUtils.DEFAULT_TEMPLATE)
    public static void research_grant_works(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        
        helper.assertFalse(ResearchManager.isResearchStarted(player, ResearchEntries.FIRST_STEPS), "Test research already started on new player");
        helper.assertFalse(ResearchManager.isResearchComplete(player, ResearchEntries.FIRST_STEPS), "Test research already complete on new player");
        
        helper.assertTrue(ResearchManager.progressResearch(player, ResearchEntries.FIRST_STEPS), "Failed to progress test research");
        
        helper.assertTrue(ResearchManager.isResearchStarted(player, ResearchEntries.FIRST_STEPS), "Test research not started after progressing");
        helper.assertFalse(ResearchManager.isResearchComplete(player, ResearchEntries.FIRST_STEPS), "Test research completed after only progressing");
        
        helper.assertTrue(ResearchManager.completeResearch(player, ResearchEntries.FIRST_STEPS), "Failed to complete test research");
        
        helper.assertTrue(ResearchManager.isResearchStarted(player, ResearchEntries.FIRST_STEPS), "Test research not started after completing");
        helper.assertTrue(ResearchManager.isResearchComplete(player, ResearchEntries.FIRST_STEPS), "Test research not complete after completing");
        
        helper.succeed();
    }
}