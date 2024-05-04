package com.verdantartifice.primalmagick.common.research;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.base.Preconditions;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.capabilities.IPlayerKnowledge;
import com.verdantartifice.primalmagick.common.capabilities.PrimalMagickCapabilities;
import com.verdantartifice.primalmagick.common.research.keys.ResearchEntryKey;

import net.minecraft.network.FriendlyByteBuf;
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
public record ResearchEntry(ResearchEntryKey key, String disciplineKey, String nameTranslationKey, Optional<Icon> iconOpt, List<ResearchEntryKey> parents,
        boolean hidden, boolean finaleExempt, List<String> finales, List<ResearchStage> stages, List<ResearchAddendum> addenda) {
    public static final Codec<ResearchEntry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResearchEntryKey.CODEC.fieldOf("key").forGetter(ResearchEntry::key),
            Codec.STRING.fieldOf("disciplineKey").forGetter(ResearchEntry::disciplineKey),
            Codec.STRING.fieldOf("nameTranslationKey").forGetter(ResearchEntry::nameTranslationKey),
            Icon.CODEC.optionalFieldOf("icon").forGetter(ResearchEntry::iconOpt),
            ResearchEntryKey.CODEC.listOf().fieldOf("parents").forGetter(ResearchEntry::parents),
            Codec.BOOL.fieldOf("hidden").forGetter(ResearchEntry::hidden),
            Codec.BOOL.fieldOf("finaleExempt").forGetter(ResearchEntry::finaleExempt),
            Codec.STRING.listOf().fieldOf("finales").forGetter(ResearchEntry::finales),
            ResearchStage.CODEC.listOf().fieldOf("stages").forGetter(ResearchEntry::stages),
            ResearchAddendum.CODEC.listOf().fieldOf("addenda").forGetter(ResearchEntry::addenda)
        ).apply(instance, ResearchEntry::new));
    
    @Nonnull
    public static ResearchEntry fromNetwork(FriendlyByteBuf buf) {
        ResearchEntryKey key = ResearchEntryKey.fromNetwork(buf);
        String disciplineKey = buf.readUtf();
        String nameTranslationKey = buf.readUtf();
        Optional<Icon> iconOpt = buf.readOptional(Icon::fromNetwork);
        List<ResearchEntryKey> parents = buf.readList(ResearchEntryKey::fromNetwork);
        boolean hidden = buf.readBoolean();
        boolean finaleExempt = buf.readBoolean();
        List<String> finales = buf.readList(b -> b.readUtf());
        List<ResearchStage> stages = buf.readList(ResearchStage::fromNetwork);
        List<ResearchAddendum> addenda = buf.readList(ResearchAddendum::fromNetwork);
        return new ResearchEntry(key, disciplineKey, nameTranslationKey, iconOpt, parents, hidden, finaleExempt, finales, stages, addenda);
    }
    
    public static void toNetwork(FriendlyByteBuf buf, ResearchEntry entry) {
        entry.key.toNetwork(buf);
        buf.writeUtf(entry.disciplineKey);
        buf.writeUtf(entry.nameTranslationKey);
        buf.writeOptional(entry.iconOpt, Icon::toNetwork);
        buf.writeCollection(entry.parents, (b, p) -> p.toNetwork(b));
        buf.writeBoolean(entry.hidden);
        buf.writeBoolean(entry.finaleExempt);
        buf.writeCollection(entry.finales, (b, f) -> b.writeUtf(f));
        buf.writeCollection(entry.stages, ResearchStage::toNetwork);
        buf.writeCollection(entry.addenda, ResearchAddendum::toNetwork);
    }
    
    /**
     * Get whether this research entry is a finale for the given discipline key.
     * 
     * @param discipline the discipline to be tested
     * @return whether this research is a finale for the given discipline key
     */
    public boolean isFinaleFor(String discipline) {
        return this.finales.contains(discipline);
    }
    
    public boolean appendStage(@Nullable ResearchStage stage) {
        return (stage == null) ? false : this.stages.add(stage);
    }
    
    public boolean appendAddendum(@Nullable ResearchAddendum addendum) {
        return (addendum == null) ? false : this.addenda.add(addendum);
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
        return this.getKnowledge(player).getResearchStatus(this.key()) == IPlayerKnowledge.ResearchStatus.COMPLETE;
    }
    
    public boolean isInProgress(@Nonnull Player player) {
        return this.getKnowledge(player).getResearchStatus(this.key()) == IPlayerKnowledge.ResearchStatus.IN_PROGRESS;
    }
    
    public boolean isAvailable(@Nonnull Player player) {
        return this.parents.isEmpty() || this.parents.stream().allMatch(key -> key.isKnownBy(player));
    }
    
    public boolean isUpcoming(@Nonnull Player player) {
        for (ResearchEntryKey parentKey : this.parents) {
            if (ResearchManager.isOpaque(parentKey) && !parentKey.isKnownBy(player)) {
                return false;
            } else {
                ResearchEntry parent = ResearchEntries.getEntry(parentKey);
                if (parent != null && !parent.isAvailable(player)) {
                    return false;
                }
            }
        }
        return true;
    }
    
    @Nonnull
    public Set<ResourceLocation> getAllRecipeIds() {
        return Stream.concat(this.stages.stream().flatMap(stage -> stage.recipes().stream()), this.addenda.stream().flatMap(addendum -> addendum.recipes().stream())).collect(Collectors.toSet());
    }
    
    @Nonnull
    public Set<ResourceLocation> getKnownRecipeIds(Player player) {
        Set<ResourceLocation> retVal = new HashSet<>();
        IPlayerKnowledge knowledge = this.getKnowledge(player);
        
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
            for (ResearchEntry searchEntry : ResearchEntries.getAllEntries()) {
                if (!searchEntry.addenda().isEmpty() && knowledge.isResearchComplete(searchEntry.key())) {
                    for (ResearchAddendum addendum : searchEntry.addenda()) {
                        addendum.completionRequirementOpt().ifPresent(req -> {
                            if (req.contains(this.key) && req.isMetBy(player)) {
                                retVal.addAll(addendum.recipes());
                            }
                        });
                    }
                }
            }
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
}
