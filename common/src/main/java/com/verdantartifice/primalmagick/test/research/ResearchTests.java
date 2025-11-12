package com.verdantartifice.primalmagick.test.research;

import com.verdantartifice.primalmagick.common.research.ResearchEntries;
import com.verdantartifice.primalmagick.common.research.ResearchManager;
import com.verdantartifice.primalmagick.test.AbstractBaseTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;

public class ResearchTests extends AbstractBaseTest {
    public static void research_grant_works(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        
        assertFalse(helper, ResearchManager.isResearchStarted(player, ResearchEntries.FIRST_STEPS), "Test research already started on new player");
        assertFalse(helper, ResearchManager.isResearchComplete(player, ResearchEntries.FIRST_STEPS), "Test research already complete on new player");
        
        assertTrue(helper, ResearchManager.progressResearch(player, ResearchEntries.FIRST_STEPS), "Failed to progress test research");
        
        assertTrue(helper, ResearchManager.isResearchStarted(player, ResearchEntries.FIRST_STEPS), "Test research not started after progressing");
        assertFalse(helper, ResearchManager.isResearchComplete(player, ResearchEntries.FIRST_STEPS), "Test research completed after only progressing");
        
        assertTrue(helper, ResearchManager.completeResearch(player, ResearchEntries.FIRST_STEPS), "Failed to complete test research");
        
        assertTrue(helper, ResearchManager.isResearchStarted(player, ResearchEntries.FIRST_STEPS), "Test research not started after completing");
        assertTrue(helper, ResearchManager.isResearchComplete(player, ResearchEntries.FIRST_STEPS), "Test research not complete after completing");
        
        helper.succeed();
    }
}
