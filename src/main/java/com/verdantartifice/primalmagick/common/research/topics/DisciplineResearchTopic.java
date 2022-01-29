package com.verdantartifice.primalmagick.common.research.topics;

import javax.annotation.Nullable;

import com.verdantartifice.primalmagick.common.research.ResearchDiscipline;
import com.verdantartifice.primalmagick.common.research.ResearchDisciplines;

/**
 * Research topic that points to a mod research discipline in the Grimoire.
 * 
 * @author Daedalus4096
 */
public class DisciplineResearchTopic extends AbstractResearchTopic {
    public DisciplineResearchTopic(ResearchDiscipline discipline, int page) {
        super(AbstractResearchTopic.Type.RESEARCH_DISCIPLINE, discipline.getKey(), page);
    }
    
    @Nullable
    public ResearchDiscipline getData() {
        return ResearchDisciplines.getDiscipline(this.data);
    }
}
