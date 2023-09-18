package com.verdantartifice.primalmagick.common.research;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.verdantartifice.primalmagick.common.sources.SourceList;
import com.verdantartifice.primalmagick.common.util.JsonUtils;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

/**
 * Definition of a research addendum.  An addendum is an addon to a research entry that is separately
 * unlocked with additional research if the entry itself is already unlocked.  Addenda have their own
 * text and may grant new recipes and attunements to the player.
 * 
 * @author Daedalus4096
 */
public class ResearchAddendum {
    protected ResearchEntry researchEntry;
    protected String textTranslationKey;
    protected List<ResourceLocation> recipes = new ArrayList<>();
    protected List<SimpleResearchKey> siblings = new ArrayList<>();
    protected CompoundResearchKey requiredResearch;
    protected SourceList attunements = SourceList.EMPTY;

    protected ResearchAddendum(@Nonnull ResearchEntry entry, @Nonnull String textTranslationKey) {
        this.researchEntry = entry;
        this.textTranslationKey = textTranslationKey;
    }
    
    @Nullable
    public static ResearchAddendum create(@Nonnull ResearchEntry entry, @Nullable String textTranslationKey) {
        return (textTranslationKey == null) ? null : new ResearchAddendum(entry, textTranslationKey);
    }
    
    @Nonnull
    public static ResearchAddendum parse(@Nonnull ResearchEntry entry, @Nonnull JsonObject obj) throws Exception {
        // Parse a research addendum from a research definition file
        ResearchAddendum addendum = create(entry, obj.getAsJsonPrimitive("text").getAsString());
        if (addendum == null) {
            throw new JsonParseException("Illegal addendum text in research JSON");
        }
        if (obj.has("recipes")) {
            addendum.recipes = JsonUtils.toResourceLocations(obj.get("recipes").getAsJsonArray());
        }
        if (obj.has("siblings")) {
            addendum.siblings = JsonUtils.toSimpleResearchKeys(obj.get("siblings").getAsJsonArray());
        }
        if (obj.has("required_research")) {
            addendum.requiredResearch = CompoundResearchKey.parse(obj.get("required_research").getAsJsonArray());
        }
        if (obj.has("attunements")) {
            addendum.attunements = JsonUtils.toSourceList(obj.get("attunements").getAsJsonObject());
        }
        return addendum;
    }
    
    @Nonnull
    public static ResearchAddendum fromNetwork(FriendlyByteBuf buf, ResearchEntry entry) {
        ResearchAddendum addendum = create(entry, buf.readUtf());
        int recipeSize = buf.readVarInt();
        for (int index = 0; index < recipeSize; index++) {
            addendum.recipes.add(new ResourceLocation(buf.readUtf()));
        }
        int siblingSize = buf.readVarInt();
        for (int index = 0; index < siblingSize; index++) {
            addendum.siblings.add(SimpleResearchKey.parse(buf.readUtf()));
        }
        addendum.requiredResearch = CompoundResearchKey.parse(buf.readUtf());
        addendum.attunements = SourceList.fromNetwork(buf);
        return addendum;
    }
    
    public static void toNetwork(FriendlyByteBuf buf, ResearchAddendum addendum) {
        buf.writeUtf(addendum.textTranslationKey);
        buf.writeVarInt(addendum.recipes.size());
        for (ResourceLocation recipe : addendum.recipes) {
            buf.writeUtf(recipe.toString());
        }
        buf.writeVarInt(addendum.siblings.size());
        for (SimpleResearchKey key : addendum.siblings) {
            buf.writeUtf(key.toString());
        }
        buf.writeUtf(addendum.requiredResearch == null ? "" : addendum.requiredResearch.toString());
        SourceList.toNetwork(buf, addendum.attunements);
    }
    
    @Nonnull
    public ResearchEntry getResearchEntry() {
        return this.researchEntry;
    }
    
    @Nonnull
    public String getTextTranslationKey() {
        return this.textTranslationKey;
    }
    
    @Nonnull
    public List<ResourceLocation> getRecipes() {
        return Collections.unmodifiableList(this.recipes);
    }
    
    @Nonnull
    public List<SimpleResearchKey> getSiblings() {
        return Collections.unmodifiableList(this.siblings);
    }
    
    @Nullable
    public CompoundResearchKey getRequiredResearch() {
        return this.requiredResearch;
    }
    
    @Nonnull
    public SourceList getAttunements() {
        return this.attunements;
    }
}
