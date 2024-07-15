package com.verdantartifice.primalmagick.common.books.grids;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.joml.Vector2i;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.books.BookLanguage;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;
import com.verdantartifice.primalmagick.common.util.CodecUtils;
import com.verdantartifice.primalmagick.common.util.StreamCodecUtils;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;

/**
 * Class encapsulating a data-defined definition for a linguistics grid.  These definitions determine
 * the layout and contents of the linguistics comprehension grids for each ancient language.  Node
 * coordinates increase right and down, with (0,0) being in the top-left corner to match screen coords.
 * 
 * @author Daedalus4096
 */
public class GridDefinition {
    public static final int MIN_POS = 0;
    public static final int MAX_POS = 7;
    
    public static Codec<GridDefinition> codec() {
        return RecordCodecBuilder.create(instance -> instance.group(
                ResourceLocation.CODEC.fieldOf("key").forGetter(GridDefinition::getKey),
                ResourceKey.codec(RegistryKeysPM.BOOK_LANGUAGES).fieldOf("language").forGetter(GridDefinition::getLanguage),
                CodecUtils.VECTOR2I.fieldOf("startPos").forGetter(GridDefinition::getStartPos),
                ExtraCodecs.strictUnboundedMap(CodecUtils.VECTOR2I, GridNodeDefinition.codec()).fieldOf("nodes").forGetter(GridDefinition::getNodes)
            ).apply(instance, GridDefinition::new));
    }
    
    public static StreamCodec<RegistryFriendlyByteBuf, GridDefinition> streamCodec() {
        return StreamCodec.composite(
                ResourceLocation.STREAM_CODEC, GridDefinition::getKey,
                ResourceKey.streamCodec(RegistryKeysPM.BOOK_LANGUAGES), GridDefinition::getLanguage,
                StreamCodecUtils.VECTOR2I, GridDefinition::getStartPos,
                ByteBufCodecs.map(HashMap::new, StreamCodecUtils.VECTOR2I, GridNodeDefinition.streamCodec()), GridDefinition::getNodes,
                GridDefinition::new);
    }
    
    protected ResourceLocation key;
    protected ResourceKey<BookLanguage> language;
    protected Vector2i startPos;
    protected final Map<Vector2i, GridNodeDefinition> nodes = new HashMap<>();
    
    protected GridDefinition(ResourceLocation key, ResourceKey<BookLanguage> language, Vector2i startPos, Map<Vector2i, GridNodeDefinition> nodes) {
        this.key = key;
        this.language = language;
        this.startPos = startPos;
        this.nodes.putAll(nodes);
    }
    
    public ResourceLocation getKey() {
        return this.key;
    }
    
    public ResourceKey<BookLanguage> getLanguage() {
        return this.language;
    }
    
    public Vector2i getStartPos() {
        return this.startPos;
    }
    
    public Map<Vector2i, GridNodeDefinition> getNodes() {
        return Collections.unmodifiableMap(this.nodes);
    }
    
    public boolean isValidPos(int x, int y) {
        return this.isValidPos(new Vector2i(x, y));
    }
    
    public boolean isValidPos(Vector2i pos) {
        return this.nodes.containsKey(pos);
    }
    
    public Optional<GridNodeDefinition> getNode(int x, int y) {
        return this.getNode(new Vector2i(x, y));
    }
    
    public Optional<GridNodeDefinition> getNode(Vector2i pos) {
        return this.isValidPos(pos) ? Optional.ofNullable(this.nodes.get(pos)) : Optional.empty();
    }
}
