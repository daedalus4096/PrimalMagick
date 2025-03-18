package com.verdantartifice.primalmagick.common.research;

import com.google.common.base.Preconditions;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.common.capabilities.IPlayerKnowledge;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;
import com.verdantartifice.primalmagick.common.research.keys.ResearchDisciplineKey;
import com.verdantartifice.primalmagick.common.research.keys.ResearchEntryKey;
import com.verdantartifice.primalmagick.common.tags.ResearchEntryTagsPM;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import com.verdantartifice.primalmagick.common.util.StreamCodecUtils;
import com.verdantartifice.primalmagick.platform.Services;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ItemLike;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Definition of a research entry, the primary component of the research system.  A research entry
 * represents a single node in the mod research tree and a single named entry in the grimoire.  A
 * research entry is made up of one or more stages, which are progressed through in sequence using
 * the grimoire.  It may have one or more parent research entries, which are required to unlock 
 * access to the entry.  It may also have zero or more addenda to be unlocked after the entry is
 * completed.
 * 
 * @author Daedalus4096
 */
public record ResearchEntry(ResearchEntryKey key, Optional<ResearchDisciplineKey> disciplineKeyOpt, Optional<ResearchTier> tierOpt,
                            Optional<String> nameKeyOpt, Optional<IconDefinition> iconOpt, List<ResearchEntryKey> parents,  ResearchEntry.Flags flags,
                            List<ResearchDisciplineKey> finales, List<ResearchStage> stages, List<ResearchAddendum> addenda) {
    public static Codec<ResearchEntry> codec() {
        return RecordCodecBuilder.create(instance -> instance.group(
                ResearchEntryKey.CODEC.fieldOf("key").forGetter(ResearchEntry::key),
                ResearchDisciplineKey.CODEC.codec().optionalFieldOf("disciplineKey").forGetter(ResearchEntry::disciplineKeyOpt),
                ResearchTier.CODEC.optionalFieldOf("tier").forGetter(ResearchEntry::tierOpt),
                Codec.STRING.optionalFieldOf("nameKey").forGetter(ResearchEntry::nameKeyOpt),
                IconDefinition.CODEC.optionalFieldOf("icon").forGetter(ResearchEntry::iconOpt),
                ResearchEntryKey.CODEC.codec().listOf().fieldOf("parents").forGetter(ResearchEntry::parents),
                ResearchEntry.Flags.CODEC.fieldOf("flags").forGetter(ResearchEntry::flags),
                ResearchDisciplineKey.CODEC.codec().listOf().fieldOf("finales").forGetter(ResearchEntry::finales),
                ResearchStage.codec().listOf().fieldOf("stages").forGetter(ResearchEntry::stages),
                ResearchAddendum.codec().listOf().fieldOf("addenda").forGetter(ResearchEntry::addenda)
            ).apply(instance, ResearchEntry::new));
    }
    
    public static StreamCodec<RegistryFriendlyByteBuf, ResearchEntry> streamCodec() {
        return StreamCodecUtils.composite(
                ResearchEntryKey.STREAM_CODEC, ResearchEntry::key,
                ByteBufCodecs.optional(ResearchDisciplineKey.STREAM_CODEC), ResearchEntry::disciplineKeyOpt,
                ByteBufCodecs.optional(ResearchTier.STREAM_CODEC), ResearchEntry::tierOpt,
                ByteBufCodecs.optional(ByteBufCodecs.STRING_UTF8), ResearchEntry::nameKeyOpt,
                ByteBufCodecs.optional(IconDefinition.STREAM_CODEC), ResearchEntry::iconOpt,
                ResearchEntryKey.STREAM_CODEC.apply(ByteBufCodecs.list()), ResearchEntry::parents,
                ResearchEntry.Flags.STREAM_CODEC, ResearchEntry::flags,
                ResearchDisciplineKey.STREAM_CODEC.apply(ByteBufCodecs.list()), ResearchEntry::finales,
                ResearchStage.streamCodec().apply(ByteBufCodecs.list()), ResearchEntry::stages,
                ResearchAddendum.streamCodec().apply(ByteBufCodecs.list()), ResearchEntry::addenda,
                ResearchEntry::new);
    }
    
    public static Builder builder(ResourceKey<ResearchEntry> key) {
        return new Builder(key);
    }
    
    public String getBaseTranslationKey() {
        return String.join(".", "research", this.key.getRootKey().location().getNamespace(), this.key.getRootKey().location().getPath());
    }
    
    public String getNameTranslationKey() {
        return this.nameKeyOpt().orElseGet(() -> String.join(".", this.getBaseTranslationKey(), "title"));
    }
    
    public Optional<String> getHintTranslationKey() {
        if (this.flags().hasHint()) {
            return Optional.of(String.join(".", this.getBaseTranslationKey(), "hint"));
        } else {
            return Optional.empty();
        }
    }
    
    public boolean isForDiscipline(ResearchDisciplineKey discipline) {
        return this.disciplineKeyOpt.isPresent() && this.disciplineKeyOpt.get().equals(discipline);
    }
    
    /**
     * Get whether this research entry is a finale for the given discipline key.
     * 
     * @param discipline the discipline to be tested
     * @return whether this research is a finale for the given discipline key
     */
    public boolean isFinaleFor(ResearchDisciplineKey discipline) {
        return this.finales.contains(discipline);
    }
    
    /**
     * Get whether this research entry is a finale for the given discipline key.
     * 
     * @param discipline the discipline to be tested
     * @return whether this research is a finale for the given discipline key
     */
    public boolean isFinaleFor(ResourceKey<ResearchDiscipline> discipline) {
        return this.isFinaleFor(new ResearchDisciplineKey(discipline));
    }
    
    private IPlayerKnowledge getKnowledge(@Nonnull Player player) {
        return Services.CAPABILITIES.knowledge(player).orElseThrow(() -> new IllegalStateException("No knowledge provider for player"));
    }
    
    public boolean isNew(@Nonnull Player player) {
        return this.getKnowledge(player).hasResearchFlag(this.key(), IPlayerKnowledge.ResearchFlag.NEW);
    }
    
    public boolean isUpdated(@Nonnull Player player) {
        return this.getKnowledge(player).hasResearchFlag(this.key(), IPlayerKnowledge.ResearchFlag.UPDATED);
    }
    
    public boolean isComplete(@Nonnull Player player) {
        return this.getKnowledge(player).getResearchStatus(player.level().registryAccess(), this.key()) == IPlayerKnowledge.ResearchStatus.COMPLETE;
    }
    
    public boolean isInProgress(@Nonnull Player player) {
        return this.getKnowledge(player).getResearchStatus(player.level().registryAccess(), this.key()) == IPlayerKnowledge.ResearchStatus.IN_PROGRESS;
    }
    
    public boolean isAvailable(@Nonnull Player player) {
        return this.parents.isEmpty() || this.parents.stream().allMatch(key -> key.isKnownBy(player));
    }
    
    public boolean isUpcoming(@Nonnull Player player) {
        Registry<ResearchEntry> registry = player.level().registryAccess().registryOrThrow(RegistryKeysPM.RESEARCH_ENTRIES);
        return !this.parents.stream().map(k -> registry.getHolder(k.getRootKey())).anyMatch(opt -> {
            return opt.isPresent() && ((opt.get().is(ResearchEntryTagsPM.OPAQUE) && !opt.get().value().key().isKnownBy(player)) || !opt.get().value().isAvailable(player));
        });
    }

    public boolean isVisible(@Nonnull Player player) {
        return this.isAvailable(player) || this.isUpcoming(player);
    }
    
    @Nonnull
    public Set<ResourceLocation> getAllRecipeIds() {
        return Stream.concat(this.stages.stream().flatMap(stage -> stage.recipes().stream()), this.addenda.stream().flatMap(addendum -> addendum.recipes().stream())).collect(Collectors.toSet());
    }
    
    @Nonnull
    public Set<ResourceLocation> getKnownRecipeIds(Player player) {
        Set<ResourceLocation> retVal = new HashSet<>();
        if (this.stages().isEmpty()) {
            // If this research entry has no stages, then it can't have any recipes, so just abort
            return retVal;
        }
        
        IPlayerKnowledge knowledge = this.getKnowledge(player);
        RegistryAccess registryAccess = player.level().registryAccess();
        
        ResearchStage currentStage = null;
        int currentStageNum = knowledge.getResearchStage(this.key);
        if (currentStageNum >= 0) {
            currentStage = this.stages().get(Math.min(currentStageNum, this.stages().size() - 1));
        }
        boolean entryComplete = (currentStageNum >= this.stages().size());
        
        if (currentStage != null) {
            retVal.addAll(currentStage.recipes());
        }
        if (entryComplete) {
            for (ResearchAddendum addendum : this.addenda()) {
                addendum.completionRequirementOpt().ifPresent(req -> {
                    if (req.isMetBy(player)) {
                        retVal.addAll(addendum.recipes());
                    }
                });
            }
            registryAccess.registryOrThrow(RegistryKeysPM.RESEARCH_ENTRIES).forEach(searchEntry -> {
                if (!searchEntry.addenda().isEmpty() && knowledge.isResearchComplete(registryAccess, searchEntry.key())) {
                    for (ResearchAddendum addendum : searchEntry.addenda()) {
                        addendum.completionRequirementOpt().ifPresent(req -> {
                            if (req.contains(this.key) && req.isMetBy(player)) {
                                retVal.addAll(addendum.recipes());
                            }
                        });
                    }
                }
            });
        }
        
        return retVal;
    }
    
    public static record Flags(boolean hidden, boolean hasHint, boolean internal, boolean finaleExempt) {
        public static final ResearchEntry.Flags EMPTY = new Flags(false, false, false, false);
        
        public static final Codec<ResearchEntry.Flags> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.BOOL.optionalFieldOf("hidden", false).forGetter(ResearchEntry.Flags::hidden),
                Codec.BOOL.optionalFieldOf("hasHint", false).forGetter(ResearchEntry.Flags::hasHint),
                Codec.BOOL.optionalFieldOf("internal", false).forGetter(ResearchEntry.Flags::internal),
                Codec.BOOL.optionalFieldOf("finaleExempt", false).forGetter(ResearchEntry.Flags::finaleExempt)
            ).apply(instance, ResearchEntry.Flags::new));
        
        public static final StreamCodec<ByteBuf, ResearchEntry.Flags> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.BOOL,
                ResearchEntry.Flags::hidden,
                ByteBufCodecs.BOOL,
                ResearchEntry.Flags::hasHint,
                ByteBufCodecs.BOOL,
                ResearchEntry.Flags::internal,
                ByteBufCodecs.BOOL,
                ResearchEntry.Flags::finaleExempt,
                ResearchEntry.Flags::new);
        
        public static ResearchEntry.Flags.Builder builder() {
            return new ResearchEntry.Flags.Builder();
        }
        
        public static class Builder {
            protected boolean hidden = false;
            protected boolean hasHint = false;
            protected boolean internal = false;
            protected boolean finaleExempt = false;
            
            public Builder hidden() {
                this.hidden = true;
                return this;
            }
            
            public Builder hasHint() {
                this.hasHint = true;
                return this;
            }
            
            public Builder internal() {
                this.internal = true;
                return this;
            }
            
            public Builder finaleExempt() {
                this.finaleExempt = true;
                return this;
            }
            
            public ResearchEntry.Flags build() {
                return new ResearchEntry.Flags(this.hidden, this.hasHint, this.internal, this.finaleExempt);
            }
        }
    }
    
    public static class Builder {
        protected final String modId;
        protected final ResearchEntryKey key;
        protected Optional<ResearchDisciplineKey> disciplineKeyOpt = Optional.empty();
        protected Optional<ResearchTier> tierOpt = Optional.empty();
        protected Optional<String> nameKeyOpt = Optional.empty();
        protected Optional<IconDefinition> iconOpt = Optional.empty();
        protected final List<ResearchEntryKey> parents = new ArrayList<>();
        protected ResearchEntry.Flags.Builder flagsBuilder = ResearchEntry.Flags.builder();
        protected final List<ResearchDisciplineKey> finales = new ArrayList<>();
        protected final List<ResearchStage.Builder> stageBuilders = new ArrayList<>();
        protected final List<ResearchAddendum.Builder> addendumBuilders = new ArrayList<>();
        
        public Builder(String modId, ResearchEntryKey key) {
            this.modId = Preconditions.checkNotNull(modId);
            this.key = Preconditions.checkNotNull(key);
        }
        
        public Builder(String modId, ResourceKey<ResearchEntry> rawKey) {
            this(modId, new ResearchEntryKey(rawKey));
        }
        
        public Builder(ResearchEntryKey key) {
            this(Constants.MOD_ID, key);
        }
        
        public Builder(ResourceKey<ResearchEntry> rawKey) {
            this(new ResearchEntryKey(rawKey));
        }
        
        public Builder discipline(ResourceKey<ResearchDiscipline> discKey) {
            this.disciplineKeyOpt = Optional.of(new ResearchDisciplineKey(discKey));
            return this;
        }
        
        public Builder tier(ResearchTier tier) {
            this.tierOpt = Optional.ofNullable(tier);
            return this;
        }

        public Builder nameKey(String key) {
            this.nameKeyOpt = Optional.ofNullable(key);
            return this;
        }
        
        public Builder icon(ItemLike item) {
            this.iconOpt = Optional.of(IconDefinition.of(item));
            return this;
        }
        
        public Builder icon(ResourceLocation loc) {
            this.iconOpt = Optional.of(IconDefinition.of(loc));
            return this;
        }
        
        public Builder icon(String path) {
            return this.icon(ResourceUtils.loc(path));
        }
        
        public Builder parent(ResearchEntryKey key) {
            this.parents.add(key);
            return this;
        }
        
        public Builder parent(ResourceKey<ResearchEntry> rawKey) {
            return this.parent(new ResearchEntryKey(rawKey));
        }
        
        public Builder flags(ResearchEntry.Flags.Builder flagsBuilder) {
            this.flagsBuilder = flagsBuilder;
            return this;
        }
        
        public Builder finale(ResourceKey<ResearchDiscipline> discKey) {
            this.finales.add(new ResearchDisciplineKey(discKey));
            return this;
        }
        
        public ResearchStage.Builder stage() {
            ResearchStage.Builder retVal = new ResearchStage.Builder(this.modId, this, this.key, this.stageBuilders.size() + 1);
            this.stageBuilders.add(retVal);
            return retVal;
        }
        
        public ResearchAddendum.Builder addendum() {
            ResearchAddendum.Builder retVal = new ResearchAddendum.Builder(this.modId, this, this.key, this.addendumBuilders.size() + 1);
            this.addendumBuilders.add(retVal);
            return retVal;
        }
        
        private void validate() {
            if (this.modId.isBlank()) {
                throw new IllegalStateException("No mod ID specified for entry");
            } else if (!this.flagsBuilder.internal && this.disciplineKeyOpt.isEmpty()) {
                throw new IllegalStateException("No discipline specified for non-internal entry");
            } else if (!this.flagsBuilder.internal && this.stageBuilders.isEmpty()) {
                throw new IllegalStateException("Non-internal entries must have at least one stage");
            }
        }
        
        public ResearchEntry build() {
            this.validate();
            return new ResearchEntry(this.key, this.disciplineKeyOpt, this.tierOpt, this.nameKeyOpt, this.iconOpt, this.parents, this.flagsBuilder.build(), this.finales,
                    this.stageBuilders.stream().map(b -> b.build()).toList(), this.addendumBuilders.stream().map(b -> b.build()).toList());
        }
    }
}
