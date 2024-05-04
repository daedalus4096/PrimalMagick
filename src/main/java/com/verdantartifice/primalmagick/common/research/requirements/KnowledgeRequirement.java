package com.verdantartifice.primalmagick.common.research.requirements;

import org.apache.commons.lang3.mutable.MutableBoolean;

import com.google.common.base.Preconditions;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.capabilities.PrimalMagickCapabilities;
import com.verdantartifice.primalmagick.common.research.KnowledgeType;
import com.verdantartifice.primalmagick.common.research.ResearchManager;

import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.player.Player;

/**
 * Requirement that the player has accumulated a given amount of the given knowledge type.
 * 
 * @author Daedalus4096
 */
public class KnowledgeRequirement extends AbstractRequirement {
    public static final Codec<KnowledgeRequirement> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            KnowledgeType.CODEC.fieldOf("knowledgeType").forGetter(req -> req.knowledgeType),
            ExtraCodecs.POSITIVE_INT.fieldOf("amount").forGetter(req -> req.amount)
        ).apply(instance, KnowledgeRequirement::new));
    
    protected final KnowledgeType knowledgeType;
    protected final int amount;
    
    public KnowledgeRequirement(KnowledgeType knowledgeType, int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Knowledge amount must be greater than zero");
        }
        this.knowledgeType = Preconditions.checkNotNull(knowledgeType);
        this.amount = amount;
    }

    @Override
    public boolean isMetBy(Player player) {
        if (player == null) {
            return false;
        } else {
            MutableBoolean retVal = new MutableBoolean(false);
            PrimalMagickCapabilities.getKnowledge(player).ifPresent(knowledge -> {
                if (knowledge.getKnowledge(this.knowledgeType) >= this.amount) {
                    retVal.setTrue();
                }
            });
            return retVal.booleanValue();
        }
    }

    @Override
    public void consumeComponents(Player player) {
        if (player != null && this.isMetBy(player)) {
            ResearchManager.addKnowledge(player, this.knowledgeType, -(this.amount * this.knowledgeType.getProgression()));
        }
    }

    @Override
    protected RequirementType<?> getType() {
        return RequirementsPM.KNOWLEDGE.get();
    }
}
