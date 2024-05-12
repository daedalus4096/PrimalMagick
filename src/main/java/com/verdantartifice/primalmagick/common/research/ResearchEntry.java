package com.verdantartifice.primalmagick.common.research;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.base.Preconditions;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.capabilities.IPlayerKnowledge;
import com.verdantartifice.primalmagick.common.capabilities.PrimalMagickCapabilities;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;
import com.verdantartifice.primalmagick.common.research.keys.ResearchDisciplineKey;
import com.verdantartifice.primalmagick.common.research.keys.ResearchEntryKey;
import com.verdantartifice.primalmagick.common.tags.ResearchEntryTagsPM;

import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;

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
public record ResearchEntry(ResearchEntryKey key, Optional<ResearchDisciplineKey> disciplineKeyOpt, String nameTranslationKey, Optional<Icon> iconOpt, List<ResearchEntryKey> parents,
        boolean hidden, boolean internal, boolean finaleExempt, List<ResearchDisciplineKey> finales, List<ResearchStage> stages, List<ResearchAddendum> addenda) {
    public static final Codec<ResearchEntry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResearchEntryKey.CODEC.fieldOf("key").forGetter(ResearchEntry::key),
            ResearchDisciplineKey.CODEC.optionalFieldOf("disciplineKey").forGetter(ResearchEntry::disciplineKeyOpt),
            Codec.STRING.fieldOf("nameTranslationKey").forGetter(ResearchEntry::nameTranslationKey),
            Icon.CODEC.optionalFieldOf("icon").forGetter(ResearchEntry::iconOpt),
            ResearchEntryKey.CODEC.listOf().fieldOf("parents").forGetter(ResearchEntry::parents),
            Codec.BOOL.optionalFieldOf("hidden", false).forGetter(ResearchEntry::hidden),
            Codec.BOOL.optionalFieldOf("internal", false).forGetter(ResearchEntry::internal),
            Codec.BOOL.optionalFieldOf("finaleExempt", false).forGetter(ResearchEntry::finaleExempt),
            ResearchDisciplineKey.CODEC.listOf().fieldOf("finales").forGetter(ResearchEntry::finales),
            ResearchStage.CODEC.listOf().fieldOf("stages").forGetter(ResearchEntry::stages),
            ResearchAddendum.CODEC.listOf().fieldOf("addenda").forGetter(ResearchEntry::addenda)
        ).apply(instance, ResearchEntry::new));
    
    @Nonnull
    public static ResearchEntry fromNetwork(FriendlyByteBuf buf) {
        ResearchEntryKey key = ResearchEntryKey.fromNetwork(buf);
        Optional<ResearchDisciplineKey> disciplineKeyOpt = buf.readOptional(ResearchDisciplineKey::fromNetwork);
        String nameTranslationKey = buf.readUtf();
        Optional<Icon> iconOpt = buf.readOptional(Icon::fromNetwork);
        List<ResearchEntryKey> parents = buf.readList(ResearchEntryKey::fromNetwork);
        boolean hidden = buf.readBoolean();
        boolean internal = buf.readBoolean();
        boolean finaleExempt = buf.readBoolean();
        List<ResearchDisciplineKey> finales = buf.readList(ResearchDisciplineKey::fromNetwork);
        List<ResearchStage> stages = buf.readList(ResearchStage::fromNetwork);
        List<ResearchAddendum> addenda = buf.readList(ResearchAddendum::fromNetwork);
        return new ResearchEntry(key, disciplineKeyOpt, nameTranslationKey, iconOpt, parents, hidden, internal, finaleExempt, finales, stages, addenda);
    }
    
    public static void toNetwork(FriendlyByteBuf buf, ResearchEntry entry) {
        entry.key.toNetwork(buf);
        buf.writeOptional(entry.disciplineKeyOpt, (b, d) -> d.toNetwork(b));
        buf.writeUtf(entry.nameTranslationKey);
        buf.writeOptional(entry.iconOpt, Icon::toNetwork);
        buf.writeCollection(entry.parents, (b, p) -> p.toNetwork(b));
        buf.writeBoolean(entry.hidden);
        buf.writeBoolean(entry.internal);
        buf.writeBoolean(entry.finaleExempt);
        buf.writeCollection(entry.finales, (b, f) -> f.toNetwork(b));
        buf.writeCollection(entry.stages, ResearchStage::toNetwork);
        buf.writeCollection(entry.addenda, ResearchAddendum::toNetwork);
    }
    
    public static Builder builder(ResourceKey<ResearchEntry> key) {
        return new Builder(key);
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
    
    @Nonnull
    protected IPlayerKnowledge getKnowledge(@Nonnull Player player) {
        return PrimalMagickCapabilities.getKnowledge(player).orElseThrow(() -> new IllegalStateException("No knowledge provider for player"));
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
            return opt.isPresent() && ((opt.get().is(ResearchEntryTagsPM.OPAQUE) && !opt.get().get().key().isKnownBy(player)) || !opt.get().get().isAvailable(player));
        });
    }
    
    @Nonnull
    public Set<ResourceLocation> getAllRecipeIds() {
        return Stream.concat(this.stages.stream().flatMap(stage -> stage.recipes().stream()), this.addenda.stream().flatMap(addendum -> addendum.recipes().stream())).collect(Collectors.toSet());
    }
    
    @Nonnull
    public Set<ResourceLocation> getKnownRecipeIds(Player player) {
        Set<ResourceLocation> retVal = new HashSet<>();
        IPlayerKnowledge knowledge = this.getKnowledge(player);
        RegistryAccess registryAccess = player.level().registryAccess();
        
        ResearchStage currentStage = null;
        int currentStageNum = knowledge.getResearchStage(key);
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
    
    public static record Icon(boolean isItem, ResourceLocation location) {
        public static final Codec<Icon> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.BOOL.fieldOf("isItem").forGetter(Icon::isItem),
                ResourceLocation.CODEC.fieldOf("location").forGetter(Icon::location)
            ).apply(instance, Icon::new));
        
        public static Icon of(ItemLike item) {
            return new Icon(true, ForgeRegistries.ITEMS.getKey(item.asItem()));
        }
        
        public static Icon of(ResourceLocation loc) {
            return new Icon(false, Preconditions.checkNotNull(loc));
        }
        
        @Nullable
        public Item asItem() {
            return this.isItem ? ForgeRegistries.ITEMS.getValue(this.location) : null;
        }
        
        @Nullable
        public static Icon fromNetwork(FriendlyByteBuf buf) {
            return new Icon(buf.readBoolean(), buf.readResourceLocation());
        }
        
        public static void toNetwork(FriendlyByteBuf buf, @Nullable ResearchEntry.Icon icon) {
            buf.writeBoolean(icon.isItem);
            buf.writeResourceLocation(icon.location);
        }
    }
    
    public static class Builder {
        protected final String modId;
        protected final ResearchEntryKey key;
        protected Optional<ResearchDisciplineKey> disciplineKeyOpt = Optional.empty();
        protected final String nameTranslationKey;
        protected Optional<Icon> iconOpt = Optional.empty();
        protected final List<ResearchEntryKey> parents = new ArrayList<>();
        protected boolean hidden = false;
        protected boolean internal = false;
        protected boolean finaleExempt = false;
        protected final List<ResearchDisciplineKey> finales = new ArrayList<>();
        protected final List<ResearchStage.Builder> stageBuilders = new ArrayList<>();
        protected final List<ResearchAddendum.Builder> addendumBuilders = new ArrayList<>();
        
        public Builder(String modId, ResearchEntryKey key) {
            this.modId = Preconditions.checkNotNull(modId);
            this.key = Preconditions.checkNotNull(key);
            this.nameTranslationKey = String.join(".", "research", modId.toLowerCase(), this.key.getRootKey().location().getPath().toLowerCase(), "title");
        }
        
        public Builder(String modId, ResourceKey<ResearchEntry> rawKey) {
            this(modId, new ResearchEntryKey(rawKey));
        }
        
        public Builder(ResearchEntryKey key) {
            this(PrimalMagick.MODID, key);
        }
        
        public Builder(ResourceKey<ResearchEntry> rawKey) {
            this(new ResearchEntryKey(rawKey));
        }
        
        public Builder discipline(ResourceKey<ResearchDiscipline> discKey) {
            this.disciplineKeyOpt = Optional.of(new ResearchDisciplineKey(discKey));
            return this;
        }
        
        public Builder icon(ItemLike item) {
            this.iconOpt = Optional.of(Icon.of(item));
            return this;
        }
        
        public Builder icon(ResourceLocation loc) {
            this.iconOpt = Optional.of(Icon.of(loc));
            return this;
        }
        
        public Builder icon(String path) {
            return this.icon(PrimalMagick.resource(path));
        }
        
        public Builder parent(ResearchEntryKey key) {
            this.parents.add(key);
            return this;
        }
        
        public Builder parent(ResourceKey<ResearchEntry> rawKey) {
            return this.parent(new ResearchEntryKey(rawKey));
        }
        
        public Builder hidden() {
            this.hidden = true;
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
        
        public Builder finale(ResourceKey<ResearchDiscipline> discKey) {
            this.finales.add(new ResearchDisciplineKey(discKey));
            return this;
        }
        
        public ResearchStage.Builder stage() {
            ResearchStage.Builder retVal = new ResearchStage.Builder(this.modId, this, this.key, this.stageBuilders.size());
            this.stageBuilders.add(retVal);
            return retVal;
        }
        
        public ResearchAddendum.Builder addendum() {
            ResearchAddendum.Builder retVal = new ResearchAddendum.Builder(this.modId, this, this.key, this.addendumBuilders.size());
            this.addendumBuilders.add(retVal);
            return retVal;
        }
        
        private void validate() {
            if (this.modId.isBlank()) {
                throw new IllegalStateException("No mod ID specified for entry");
            } else if (this.internal && this.disciplineKeyOpt.isEmpty()) {
                throw new IllegalStateException("No discipline specified for non-internal entry");
            } else if (this.internal && this.stageBuilders.isEmpty()) {
                throw new IllegalStateException("Non-internal entries must have at least one stage");
            }
        }
        
        public ResearchEntry build() {
            this.validate();
            return new ResearchEntry(this.key, this.disciplineKeyOpt, this.nameTranslationKey, this.iconOpt, this.parents, this.hidden, this.internal, this.finaleExempt, this.finales,
                    this.stageBuilders.stream().map(b -> b.build()).toList(), this.addendumBuilders.stream().map(b -> b.build()).toList());
        }
    }
}
