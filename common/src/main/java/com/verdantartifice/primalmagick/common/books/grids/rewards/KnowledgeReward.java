package com.verdantartifice.primalmagick.common.books.grids.rewards;

import com.google.common.base.Preconditions;
import com.google.common.base.Verify;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.research.KnowledgeType;
import com.verdantartifice.primalmagick.common.research.ResearchManager;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * Linguistics grid reward that grants research knowledge (i.e. theories or observations).
 * 
 * @author Daedalus4096
 */
public class KnowledgeReward extends AbstractReward<KnowledgeReward> {
    public static final MapCodec<KnowledgeReward> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            KnowledgeType.CODEC.fieldOf("knowledgeType").forGetter(r -> r.knowledgeType),
            Codec.INT.fieldOf("levels").forGetter(r -> r.levels)
        ).apply(instance, KnowledgeReward::new));
    public static final StreamCodec<ByteBuf, KnowledgeReward> STREAM_CODEC = StreamCodec.composite(
            KnowledgeType.STREAM_CODEC, r -> r.knowledgeType,
            ByteBufCodecs.VAR_INT, r -> r.levels,
            KnowledgeReward::new);

    private KnowledgeType knowledgeType;
    private int levels;
    private Optional<Component> levelsText;
    
    public KnowledgeReward(@Nonnull KnowledgeType type, int levels) {
        Verify.verifyNotNull(type, "Invalid knowledge type for knowledge reward");
        this.knowledgeType = type;
        this.setLevels(levels);
    }
    
    @Override
    protected GridRewardType<KnowledgeReward> getType() {
        return GridRewardTypesPM.KNOWLEDGE.get();
    }

    protected void setLevels(int levels) {
        this.levels = levels;
        this.levelsText = levels > 1 ? Optional.of(Component.literal(Integer.toString(levels))) : Optional.empty();
    }
    
    @Override
    public void grant(ServerPlayer player, RegistryAccess registryAccess) {
        ResearchManager.addKnowledge(player, this.knowledgeType, this.levels * this.knowledgeType.getProgression());
    }

    @Override
    public Component getDescription(Player player, RegistryAccess registryAccess) {
        Component amountText = Component.literal(Integer.toString(this.levels));
        Component typeText = Component.translatable(this.knowledgeType.getNameTranslationKey());
        return Component.translatable("label.primalmagick.scribe_table.grid.reward.knowledge", typeText, amountText);
    }

    @Override
    public ResourceLocation getIconLocation(Player player) {
        return this.knowledgeType.getIconLocation();
    }

    @Override
    public Optional<Component> getAmountText() {
        return this.levelsText;
    }
    
    public static class Builder {
        protected final KnowledgeType type;
        protected int levels = 0;
        
        protected Builder(KnowledgeType type) {
            this.type = Preconditions.checkNotNull(type);
        }
        
        public static Builder reward(KnowledgeType type) {
            return new Builder(type);
        }
        
        public Builder levels(int levels) {
            this.levels = levels;
            return this;
        }
        
        private void validate() {
            if (this.levels < 0) {
                throw new IllegalStateException("Levels value must be non-negative");
            }
        }
        
        public KnowledgeReward build() {
            this.validate();
            return new KnowledgeReward(this.type, this.levels);
        }
    }
}
