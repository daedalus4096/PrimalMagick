package com.verdantartifice.primalmagick.common.research.topics;

import javax.annotation.Nullable;

import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;
import com.verdantartifice.primalmagick.common.research.keys.ResearchDisciplineKey;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

/**
 * Research topic that points to a mod research discipline in the Grimoire.
 * 
 * @author Daedalus4096
 */
public class DisciplineResearchTopic extends AbstractResearchTopic {
    public DisciplineResearchTopic(ResearchDisciplineKey disciplineKey, int page) {
        super(AbstractResearchTopic.Type.RESEARCH_DISCIPLINE, disciplineKey.getRootKey().location().toString(), page);
    }
    
    @Nullable
    public ResearchDisciplineKey getData() {
        return new ResearchDisciplineKey(ResourceKey.create(RegistryKeysPM.RESEARCH_DISCIPLINES, new ResourceLocation(this.data)));
    }
}
