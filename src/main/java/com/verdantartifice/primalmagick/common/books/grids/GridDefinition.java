package com.verdantartifice.primalmagick.common.books.grids;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import org.joml.Vector2i;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.verdantartifice.primalmagick.common.books.BookLanguage;
import com.verdantartifice.primalmagick.common.books.BookLanguagesPM;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * Class encapsulating a data-defined definition for a linguistics grid.  These definitions determine
 * the layout and contents of the linguistics comprehension grids for each ancient language.
 * 
 * @author Daedalus4096
 */
public class GridDefinition implements INBTSerializable<CompoundTag> {
    public static final IGridDefinitionSerializer SERIALIZER = new Serializer();
    
    protected ResourceLocation key;
    protected BookLanguage language;
    protected Vector2i startPos;
    protected final Map<Vector2i, GridNodeDefinition> nodes = new HashMap<>();
    
    private GridDefinition() {}
    
    protected GridDefinition(ResourceLocation key, BookLanguage language, Vector2i startPos, Map<Vector2i, GridNodeDefinition> nodes) {
        this.key = key;
        this.language = language;
        this.startPos = startPos;
        this.nodes.putAll(nodes);
    }
    
    @Nullable
    public static GridDefinition fromNBT(CompoundTag tag) {
        if (tag == null) {
            return null;
        } else {
            GridDefinition retVal = new GridDefinition();
            retVal.deserializeNBT(tag);
            return retVal;
        }
    }
    
    public ResourceLocation getKey() {
        return this.key;
    }
    
    public BookLanguage getLanguage() {
        return this.language;
    }
    
    public Vector2i getStartPos() {
        return this.startPos;
    }
    
    public Map<Vector2i, GridNodeDefinition> getNodes() {
        return Collections.unmodifiableMap(this.nodes);
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag retVal = new CompoundTag();
        retVal.putString("Key", this.key.toString());
        retVal.putString("Language", this.language.languageId().toString());
        retVal.putInt("StartX", this.startPos.x());
        retVal.putInt("StartY", this.startPos.y());
        
        ListTag nodeListTag = new ListTag();
        for (Map.Entry<Vector2i, GridNodeDefinition> entry : this.nodes.entrySet()) {
            CompoundTag nodeTag = new CompoundTag();
            nodeTag.putInt("PosX", entry.getKey().x());
            nodeTag.putInt("PosY", entry.getKey().y());
            nodeTag.put("NodeDef", entry.getValue().serializeNBT());
            nodeListTag.add(nodeTag);
        }
        retVal.put("Nodes", nodeListTag);
        
        return retVal;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.key = new ResourceLocation(nbt.getString("Key"));
        this.language = BookLanguagesPM.LANGUAGES.get().getValue(new ResourceLocation(nbt.getString("Language")));
        this.startPos = new Vector2i(nbt.getInt("StartX"), nbt.getInt("StartY"));
        
        this.nodes.clear();
        ListTag nodeListTag = nbt.getList("Nodes", Tag.TAG_COMPOUND);
        for (int index = 0; index < nodeListTag.size(); index++) {
            CompoundTag nodeTag = nodeListTag.getCompound(index);
            this.nodes.put(new Vector2i(nodeTag.getInt("PosX"), nodeTag.getInt("PosY")), GridNodeDefinition.fromNBT(nodeTag.getCompound("NodeDef")));
        }
    }
    
    public static class Serializer implements IGridDefinitionSerializer {
        @Override
        public GridDefinition read(ResourceLocation gridId, JsonObject json) {
            ResourceLocation key = new ResourceLocation(json.getAsJsonPrimitive("key").getAsString());
            BookLanguage language = BookLanguagesPM.LANGUAGES.get().getValue(new ResourceLocation(json.getAsJsonPrimitive("language").getAsString()));
            Vector2i startPos = new Vector2i(json.getAsJsonPrimitive("startX").getAsInt(), json.getAsJsonPrimitive("startY").getAsInt());
            
            Map<Vector2i, GridNodeDefinition> nodes = new HashMap<>();
            JsonArray nodesJson = json.getAsJsonArray("nodes");
            for (JsonElement nodeElement : nodesJson) {
                try {
                    JsonObject nodeObj = nodeElement.getAsJsonObject();
                    Vector2i nodePos = new Vector2i(nodeObj.getAsJsonPrimitive("posX").getAsInt(), nodeObj.getAsJsonPrimitive("posY").getAsInt());
                    GridNodeDefinition nodeDef = GridNodeDefinition.SERIALIZER.read(gridId, nodeObj.getAsJsonObject("nodeDef"));
                    nodes.put(nodePos, nodeDef);
                } catch (Exception e) {
                    throw new JsonSyntaxException("Invalid node definition in grid definition JSON for " + gridId.toString(), e);
                }
            }
            
            return new GridDefinition(key, language, startPos, nodes);
        }

        @Override
        public GridDefinition fromNetwork(FriendlyByteBuf buf) {
            ResourceLocation key = buf.readResourceLocation();
            BookLanguage language = BookLanguagesPM.LANGUAGES.get().getValue(buf.readResourceLocation());
            Vector2i startPos = new Vector2i(buf.readVarInt(), buf.readVarInt());
            
            Map<Vector2i, GridNodeDefinition> nodes = new HashMap<>();
            int nodeCount = buf.readVarInt();
            for (int index = 0; index < nodeCount; index++) {
                nodes.put(new Vector2i(buf.readVarInt(), buf.readVarInt()), GridNodeDefinition.SERIALIZER.fromNetwork(buf));
            }
            
            return new GridDefinition(key, language, startPos, nodes);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, GridDefinition gridDef) {
            buf.writeResourceLocation(gridDef.key);
            buf.writeResourceLocation(gridDef.language.languageId());
            buf.writeVarInt(gridDef.startPos.x());
            buf.writeVarInt(gridDef.startPos.y());
            buf.writeVarInt(gridDef.nodes.size());
            for (Map.Entry<Vector2i, GridNodeDefinition> entry : gridDef.nodes.entrySet()) {
                buf.writeVarInt(entry.getKey().x());
                buf.writeVarInt(entry.getKey().y());
                GridNodeDefinition.SERIALIZER.toNetwork(buf, entry.getValue());
            }
        }
    }
}
