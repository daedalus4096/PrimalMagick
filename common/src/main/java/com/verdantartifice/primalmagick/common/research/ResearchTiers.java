package com.verdantartifice.primalmagick.common.research;

import com.verdantartifice.primalmagick.common.misc.IconDefinition;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;

import java.util.Optional;

public class ResearchTiers {
    public static final ResearchTier BASIC = new ResearchTier(0, ResourceUtils.loc("basic"), 1, 4,Optional.empty());
    public static final ResearchTier EXPERT = new ResearchTier(1, ResourceUtils.loc("expert"), 5, 20, Optional.of(IconDefinition.of(ResourceUtils.loc("textures/research/expertise_expert.png"))));
    public static final ResearchTier MASTER = new ResearchTier(2, ResourceUtils.loc("master"), 25, 100, Optional.of(IconDefinition.of(ResourceUtils.loc("textures/research/expertise_master.png"))));
    public static final ResearchTier SUPREME = new ResearchTier(3, ResourceUtils.loc("supreme"), 125, 500, Optional.of(IconDefinition.of(ResourceUtils.loc("textures/research/expertise_supreme.png"))));
}
