package com.verdantartifice.primalmagic.common.affinities;

import javax.annotation.Nonnull;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.verdantartifice.primalmagic.common.sources.SourceList;
import com.verdantartifice.primalmagic.common.util.JsonUtils;

import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.ResourceLocation;

/**
 * Data-defined affinity entry for a block.
 * 
 * @author Daedalus4096
 */
public class BlockAffinityEntry extends AffinityEntry {
    protected ResourceLocation baseEntry;
    protected SourceList setValues;
    protected SourceList addValues;
    protected SourceList removeValues;
    
    protected BlockAffinityEntry(@Nonnull ResourceLocation id) {
        super(id);
    }

    @Nonnull
    public static BlockAffinityEntry parse(@Nonnull JsonObject obj) throws Exception {
        String id = obj.getAsJsonPrimitive("id").getAsString();
        if (id == null) {
            throw new JsonParseException("Illegal entry ID in affinity JSON");
        }

        BlockAffinityEntry entry = new BlockAffinityEntry(new ResourceLocation(id));
        if (obj.has("set") && obj.has("base")) {
            throw new JsonParseException("Affinity entry may not have both set and base attributes");
        } else if (obj.has("set")) {
            entry.setValues = JsonUtils.toSourceList(obj.get("set").getAsJsonObject());
        } else if (obj.has("base")) {
            entry.baseEntry = new ResourceLocation(obj.getAsJsonPrimitive("base").getAsString());
            if (obj.has("add")) {
                entry.addValues = JsonUtils.toSourceList(obj.get("add").getAsJsonObject());
            }
            if (obj.has("remove")) {
                entry.removeValues = JsonUtils.toSourceList(obj.get("remove").getAsJsonObject());
            }
        } else {
            throw new JsonParseException("Affinity entry must have either set or base attributes");
        }
        
        return entry;
    }

    @Override
    protected SourceList calculateTotal(@Nonnull RecipeManager recipeManager) {
        if (this.setValues != null) {
            return this.setValues;
        } else if (this.baseEntry != null) {

        }
        // TODO Auto-generated method stub
        return null;
    }
}
