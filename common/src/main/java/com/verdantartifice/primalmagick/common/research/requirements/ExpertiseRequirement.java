package com.verdantartifice.primalmagick.common.research.requirements;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.research.ResearchDiscipline;
import com.verdantartifice.primalmagick.common.research.ResearchTier;
import com.verdantartifice.primalmagick.common.research.keys.ResearchDisciplineKey;
import com.verdantartifice.primalmagick.common.stats.ExpertiseManager;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * Requirement that the player has accumulated at least the required amount of expertise in the
 * given discipline.  Similar to a {@link StatRequirement}, except that the threshold is
 * calculated dynamically instead of being statically defined at creation.  For most disciplines,
 * the threshold is calculated based on the granted expertise of that discipline's associated
 * recipes, with a goal that increasing a tier requires roughly one craft of each discipline
 * recipe.  Sorcery, as it has few to no associated recipes, is instead based on the mana cost
 * of spells cast; because this cannot be calculated ahead of time, sorcery requirements must
 * have a threshold override specified.
 * 
 * @author Daedalus4096
 */
public class ExpertiseRequirement extends AbstractRequirement<ExpertiseRequirement> {
    public static MapCodec<ExpertiseRequirement> codec() {
        return RecordCodecBuilder.mapCodec(instance -> instance.group(
                ResearchDisciplineKey.CODEC.fieldOf("discipline").forGetter(ExpertiseRequirement::getDiscipline),
                ResearchTier.CODEC.fieldOf("tier").forGetter(ExpertiseRequirement::getTier),
                Codec.INT.optionalFieldOf("thresholdOverride").forGetter(r -> r.thresholdOverrideOpt)
            ).apply(instance, ExpertiseRequirement::new));
    }
    
    public static StreamCodec<ByteBuf, ExpertiseRequirement> streamCodec() {
        return StreamCodec.composite(
                ResearchDisciplineKey.STREAM_CODEC,
                ExpertiseRequirement::getDiscipline,
                ResearchTier.STREAM_CODEC,
                ExpertiseRequirement::getTier,
                ByteBufCodecs.optional(ByteBufCodecs.VAR_INT),
                req -> req.thresholdOverrideOpt,
                ExpertiseRequirement::new);
    }
    
    protected final ResearchDisciplineKey discipline;
    protected final ResearchTier tier;
    protected final Optional<Integer> thresholdOverrideOpt;
    protected Optional<Integer> thresholdCache = Optional.empty();
    
    protected ExpertiseRequirement(ResearchDisciplineKey discipline, ResearchTier tier, Optional<Integer> thresholdOverrideOpt) {
        this.discipline = discipline;
        this.tier = tier;
        this.thresholdOverrideOpt = thresholdOverrideOpt;
    }
    
    public ExpertiseRequirement(ResourceKey<ResearchDiscipline> discipline, ResearchTier tier) {
        this(new ResearchDisciplineKey(discipline), tier, Optional.empty());
    }
    
    public ExpertiseRequirement(ResourceKey<ResearchDiscipline> discipline, ResearchTier tier, int thresholdOverride) {
        this(new ResearchDisciplineKey(discipline), tier, Optional.of(thresholdOverride));
    }
    
    public ResearchDisciplineKey getDiscipline() {
        return this.discipline;
    }
    
    public ResearchTier getTier() {
        return this.tier;
    }
    
    public int getThreshold(Level level) {
        return this.thresholdOverrideOpt.orElseGet(() -> this.getThresholdInner(level));
    }
    
    protected int getThresholdInner(Level level) {
        if (this.thresholdCache.isEmpty()) {
            this.thresholdCache = Optional.of(ExpertiseManager.getThreshold(level, this.discipline, this.tier).orElse(0));
        }
        return this.thresholdCache.get();
    }

    @Override
    public boolean isMetBy(Player player) {
        if (player == null) {
            return false;
        } else {
            return this.discipline.isKnownBy(player) && (ExpertiseManager.getValue(player, this.discipline).orElse(0) >= this.getThreshold(player.level()));
        }
    }

    @Override
    public void consumeComponents(Player player) {
        // No action needed; expertise is never consumed
    }

    @Override
    public boolean forceComplete(Player player) {
        ExpertiseManager.setValueIfMax(player, this.discipline, this.getThreshold(player.level()));
        return true;
    }

    @Override
    public RequirementCategory getCategory() {
        return RequirementCategory.STAT;
    }

    @Override
    public Stream<AbstractRequirement<?>> streamByCategory(RequirementCategory category) {
        return category == this.getCategory() ? Stream.of(this) : Stream.empty();
    }

    @Override
    protected RequirementType<ExpertiseRequirement> getType() {
        return RequirementsPM.EXPERTISE.get();
    }
}
