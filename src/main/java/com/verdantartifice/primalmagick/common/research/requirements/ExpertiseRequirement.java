package com.verdantartifice.primalmagick.common.research.requirements;

import java.util.OptionalInt;
import java.util.stream.Stream;

import javax.annotation.Nonnull;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.research.ResearchDiscipline;
import com.verdantartifice.primalmagick.common.research.ResearchTier;
import com.verdantartifice.primalmagick.common.research.keys.ResearchDisciplineKey;
import com.verdantartifice.primalmagick.common.stats.ExpertiseManager;
import com.verdantartifice.primalmagick.common.util.CodecUtils;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;

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
    public static Codec<ExpertiseRequirement> codec() {
        return RecordCodecBuilder.create(instance -> instance.group(
                ResearchDisciplineKey.CODEC.fieldOf("discipline").forGetter(ExpertiseRequirement::getDiscipline),
                ResearchTier.CODEC.fieldOf("tier").forGetter(ExpertiseRequirement::getTier),
                CodecUtils.asOptionalInt(Codec.INT.optionalFieldOf("thresholdOverride")).forGetter(r -> r.thresholdOverrideOpt)
            ).apply(instance, ExpertiseRequirement::new));
    }
    
    protected final ResearchDisciplineKey discipline;
    protected final ResearchTier tier;
    protected final OptionalInt thresholdOverrideOpt;
    
    protected ExpertiseRequirement(ResearchDisciplineKey discipline, ResearchTier tier, OptionalInt thresholdOverrideOpt) {
        this.discipline = discipline;
        this.tier = tier;
        this.thresholdOverrideOpt = thresholdOverrideOpt;
    }
    
    public ExpertiseRequirement(ResourceKey<ResearchDiscipline> discipline, ResearchTier tier) {
        this(new ResearchDisciplineKey(discipline), tier, OptionalInt.empty());
    }
    
    public ExpertiseRequirement(ResourceKey<ResearchDiscipline> discipline, ResearchTier tier, int thresholdOverride) {
        this(new ResearchDisciplineKey(discipline), tier, OptionalInt.of(thresholdOverride));
    }
    
    public ResearchDisciplineKey getDiscipline() {
        return this.discipline;
    }
    
    public ResearchTier getTier() {
        return this.tier;
    }
    
    public int getThreshold() {
        return this.thresholdOverrideOpt.orElseGet(() -> ExpertiseManager.getThreshold(this.discipline, this.tier).orElse(0));
    }

    @Override
    public boolean isMetBy(Player player) {
        if (player == null) {
            return false;
        } else {
            return this.discipline.isKnownBy(player) && (ExpertiseManager.getValue(player, this.discipline).orElse(0) >= this.getThreshold());
        }
    }

    @Override
    public void consumeComponents(Player player) {
        // No action needed; expertise is never consumed
    }

    @Override
    public boolean forceComplete(Player player) {
        ExpertiseManager.setValueIfMax(player, this.discipline, this.getThreshold());
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

    @Nonnull
    static ExpertiseRequirement fromNetworkInner(FriendlyByteBuf buf) {
        ResearchDisciplineKey disc = ResearchDisciplineKey.fromNetwork(buf);
        ResearchTier tier = buf.readEnum(ResearchTier.class);
        OptionalInt thresholdOverrideOpt = buf.readBoolean() ? OptionalInt.of(buf.readVarInt()) : OptionalInt.empty();
        return new ExpertiseRequirement(disc, tier, thresholdOverrideOpt);
    }
    
    @Override
    protected void toNetworkInner(FriendlyByteBuf buf) {
        this.discipline.toNetwork(buf);
        buf.writeEnum(this.tier);
        if (this.thresholdOverrideOpt.isPresent()) {
            buf.writeBoolean(true);
            buf.writeVarInt(this.thresholdOverrideOpt.getAsInt());
        } else {
            buf.writeBoolean(false);
        }
    }
}
