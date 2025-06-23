package com.verdantartifice.primalmagick.common.theorycrafting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;
import com.verdantartifice.primalmagick.common.research.KnowledgeType;
import com.verdantartifice.primalmagick.common.rewards.AbstractReward;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import org.apache.commons.lang3.mutable.MutableDouble;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Class representing an initialized theorycrafting project, created from a data-defined template.
 * Theorycrafting projects grant the player progress toward gaining a theory by spending materials,
 * such as items or observations.
 * 
 * @author Daedalus4096
 */
public record Project(ResourceKey<ProjectTemplate> templateKey, List<MaterialInstance> activeMaterials, List<AbstractReward<?>> otherRewards, double baseSuccessChance, double baseRewardMultiplier,
        Optional<ResourceLocation> aidBlock) {
    public static Codec<Project> codec() {
        return RecordCodecBuilder.create(instance -> instance.group(
                ResourceKey.codec(RegistryKeysPM.PROJECT_TEMPLATES).fieldOf("templateKey").forGetter(Project::templateKey),
                MaterialInstance.codec().listOf().fieldOf("activeMaterials").forGetter(Project::activeMaterials),
                AbstractReward.dispatchCodec().listOf().fieldOf("otherRewards").forGetter(Project::otherRewards),
                Codec.DOUBLE.fieldOf("baseSuccessChance").forGetter(Project::baseSuccessChance),
                Codec.DOUBLE.fieldOf("baseRewardMultiplier").forGetter(Project::baseRewardMultiplier),
                ResourceLocation.CODEC.optionalFieldOf("aidBlock").forGetter(Project::aidBlock)
            ).apply(instance, Project::new));
    }
    
    public static StreamCodec<RegistryFriendlyByteBuf, Project> streamCodec() {
        return StreamCodec.composite(
                ResourceKey.streamCodec(RegistryKeysPM.PROJECT_TEMPLATES),
                Project::templateKey,
                MaterialInstance.streamCodec().apply(ByteBufCodecs.list()),
                Project::activeMaterials,
                AbstractReward.dispatchStreamCodec().apply(ByteBufCodecs.list()),
                Project::otherRewards,
                ByteBufCodecs.DOUBLE,
                Project::baseSuccessChance,
                ByteBufCodecs.DOUBLE,
                Project::baseRewardMultiplier,
                ByteBufCodecs.optional(ResourceLocation.STREAM_CODEC),
                Project::aidBlock,
                Project::new);
    }
    
    @Nonnull
    public String getNameTranslationKey() {
        return String.join(".", "research_project", this.templateKey.location().getNamespace(), this.templateKey.location().getPath(), "name");
    }
    
    @Nonnull
    public String getTextTranslationKey() {
        return String.join(".", "research_project", this.templateKey.location().getNamespace(), this.templateKey.location().getPath(), "text");
    }

    protected double getSuccessChancePerMaterial() {
        // A full complement of materials should always be enough to get the player to 100% success chance
        int materialCount = this.activeMaterials.size();
        if (materialCount <= 0) {
            return 0.0D;
        } else {
            return (1.0D - this.baseSuccessChance) / materialCount;
        }
    }
    
    public double getSuccessChance() {
        MutableDouble chance = new MutableDouble(this.baseSuccessChance);
        final double per = this.getSuccessChancePerMaterial();
        this.activeMaterials().stream().filter(m -> m.isSelected()).forEach($ -> chance.add(per));
        return Mth.clamp(chance.doubleValue(), 0.0D, 1.0D);
    }
    
    public boolean isSatisfied(Player player, Set<Block> surroundings) {
        // Determine satisfaction from selected materials
        return !this.activeMaterials.stream().anyMatch(m -> m.isSelected() && !m.getMaterialDefinition().isSatisfied(player, surroundings));
    }
    
    public boolean consumeSelectedMaterials(Player player) {
        return this.activeMaterials.stream().filter(m -> m.isSelected()).allMatch(m -> m.getMaterialDefinition().consume(player));
    }
    
    public int getTheoryPointReward() {
        int value = (int)(KnowledgeType.THEORY.getProgression() * (this.baseRewardMultiplier + this.activeMaterials().stream().filter(m -> m.isSelected()).mapToDouble(m -> m.getMaterialDefinition().getBonusReward()).sum()));
        return switch (Services.CONFIG.theorycraftSpeed()) {
            case SLOW -> value / 2;
            case FAST -> value * 2;
            default -> value;
        };
    }
    
    @Nullable
    public Optional<Block> getAidBlock() {
        return this.aidBlock.map(Services.BLOCKS_REGISTRY::get);
    }
}
