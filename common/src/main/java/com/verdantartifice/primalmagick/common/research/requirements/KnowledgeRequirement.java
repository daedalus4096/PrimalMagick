package com.verdantartifice.primalmagick.common.research.requirements;

import com.google.common.base.Preconditions;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.research.KnowledgeType;
import com.verdantartifice.primalmagick.common.research.ResearchManager;
import com.verdantartifice.primalmagick.platform.Services;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.player.Player;
import org.apache.commons.lang3.mutable.MutableBoolean;

import java.util.stream.Stream;

/**
 * Requirement that the player has accumulated a given amount of the given knowledge type.
 * 
 * @author Daedalus4096
 */
public class KnowledgeRequirement extends AbstractRequirement<KnowledgeRequirement> {
    public static final MapCodec<KnowledgeRequirement> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            KnowledgeType.CODEC.fieldOf("knowledgeType").forGetter(req -> req.knowledgeType),
            ExtraCodecs.POSITIVE_INT.fieldOf("amount").forGetter(req -> req.amount)
        ).apply(instance, KnowledgeRequirement::new));
    
    public static final StreamCodec<ByteBuf, KnowledgeRequirement> STREAM_CODEC = StreamCodec.composite(
            KnowledgeType.STREAM_CODEC,
            KnowledgeRequirement::getKnowledgeType,
            ByteBufCodecs.VAR_INT,
            KnowledgeRequirement::getAmount,
            KnowledgeRequirement::new);
    
    protected final KnowledgeType knowledgeType;
    protected final int amount;
    
    public KnowledgeRequirement(KnowledgeType knowledgeType, int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Knowledge amount must be greater than zero");
        }
        this.knowledgeType = Preconditions.checkNotNull(knowledgeType);
        this.amount = amount;
    }
    
    public KnowledgeType getKnowledgeType() {
        return this.knowledgeType;
    }
    
    public int getAmount() {
        return this.amount;
    }

    @Override
    public boolean isMetBy(Player player) {
        if (player == null) {
            return false;
        } else {
            MutableBoolean retVal = new MutableBoolean(false);
            Services.CAPABILITIES.knowledge(player).ifPresent(knowledge -> {
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
    public boolean forceComplete(Player player) {
        // Do nothing; materials are not consumed when force completing a knowledge requirement
        return true;
    }

    @Override
    public RequirementCategory getCategory() {
        return RequirementCategory.KNOWLEDGE;
    }

    @Override
    public Stream<AbstractRequirement<?>> streamByCategory(RequirementCategory category) {
        return category == this.getCategory() ? Stream.of(this) : Stream.empty();
    }

    @Override
    protected RequirementType<KnowledgeRequirement> getType() {
        return RequirementsPM.KNOWLEDGE.get();
    }
}
