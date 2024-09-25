package com.verdantartifice.primalmagick.client.gui.widgets.grimoire;

import com.verdantartifice.primalmagick.common.research.requirements.AbstractRequirement;
import com.verdantartifice.primalmagick.common.research.requirements.ExpertiseRequirement;
import com.verdantartifice.primalmagick.common.research.requirements.IVanillaStatRequirement;
import com.verdantartifice.primalmagick.common.research.requirements.ItemStackRequirement;
import com.verdantartifice.primalmagick.common.research.requirements.ItemTagRequirement;
import com.verdantartifice.primalmagick.common.research.requirements.KnowledgeRequirement;
import com.verdantartifice.primalmagick.common.research.requirements.ResearchRequirement;
import com.verdantartifice.primalmagick.common.research.requirements.StatRequirement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;

public class RequirementWidgetFactory {
    public static AbstractWidget fromRequirement(AbstractRequirement<?> requirement, int x, int y, boolean isComplete) {
        Minecraft mc = Minecraft.getInstance();
        if (requirement instanceof ItemStackRequirement req) {
            return new ItemStackWidget(req.getStack(), x, y, isComplete);
        } else if (requirement instanceof ItemTagRequirement req) {
            return new ItemTagWidget(req.getTag(), req.getAmount(), x, y, isComplete);
        } else if (requirement instanceof KnowledgeRequirement req) {
            return new KnowledgeWidget(req.getKnowledgeType(), req.getAmount(), x, y, isComplete);
        } else if (requirement instanceof ResearchRequirement req) {
            return new ResearchWidget(req.getRootKey(), x, y, isComplete);
        } else if (requirement instanceof ExpertiseRequirement req) {
            return new ExpertiseProgressWidget(req.getDiscipline(), req.getTier(), req.getThreshold(mc.level), x, y, isComplete);
        } else if (requirement instanceof StatRequirement req) {
            return new StatProgressWidget(req.getStat(), req.getRequiredValue(), x, y, isComplete);
        } else if (requirement instanceof IVanillaStatRequirement req) {
            return new VanillaStatProgressWidget(req, x, y, isComplete);
        } else {
            return null;
        }
    }
}
