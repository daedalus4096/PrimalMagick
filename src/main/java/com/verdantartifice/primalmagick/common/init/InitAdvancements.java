package com.verdantartifice.primalmagick.common.init;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.advancements.critereon.CriteriaTriggersPM;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.CriterionTrigger;

/**
 * Point of registration for advancement related data.
 * 
 * @author Daedalus4096
 */
public class InitAdvancements {
    public static void initCriteria() {
        registerCriterion("research_completed", CriteriaTriggersPM.RESEARCH_COMPLETED);
        registerCriterion("stat_value", CriteriaTriggersPM.STAT_VALUE);
        registerCriterion("linguistics_comprehension", CriteriaTriggersPM.LINGUISTICS_COMPREHENSION);
        registerCriterion("runescribing", CriteriaTriggersPM.RUNESCRIBING);
        registerCriterion("recall_stone", CriteriaTriggersPM.RECALL_STONE);
        registerCriterion("entity_hurt_player_ext", CriteriaTriggersPM.ENTITY_HURT_PLAYER_EXT);
    }
    
    private static void registerCriterion(String name, CriterionTrigger<?> trigger) {
        CriteriaTriggers.register(String.join(":", PrimalMagick.MODID, name), trigger);
    }
}
