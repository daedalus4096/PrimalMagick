package com.verdantartifice.primalmagick.datagen.linguistics;

import java.util.function.Consumer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.joml.Vector2i;

import com.google.gson.JsonObject;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.books.BookLanguage;
import com.verdantartifice.primalmagick.common.books.grids.GridDefinition;

import net.minecraft.resources.ResourceLocation;

public class GridDefinitionBuilder {
    protected final ResourceLocation key;
    protected ResourceLocation bookLanguage;
    protected Vector2i startPos;
    // TODO List of finished node definitions
    
    protected GridDefinitionBuilder(@Nonnull ResourceLocation key) {
        this.key = key;
    }

    public static GridDefinitionBuilder grid(@Nonnull ResourceLocation key) {
        return new GridDefinitionBuilder(key);
    }
    
    public static GridDefinitionBuilder grid(@Nonnull String keyNamespace, @Nonnull String keyPath) {
        return grid(new ResourceLocation(keyNamespace, keyPath));
    }
    
    public static GridDefinitionBuilder grid(@Nonnull String keyPath) {
        return grid(PrimalMagick.resource(keyPath));
    }
    
    public GridDefinitionBuilder language(@Nullable ResourceLocation lang) {
        this.bookLanguage = lang;
        return this;
    }
    
    public GridDefinitionBuilder language(@Nullable BookLanguage lang) {
        if (lang != null) {
            this.bookLanguage = lang.languageId();
        }
        return this;
    }
    
    public GridDefinitionBuilder startPos(Vector2i pos) {
        this.startPos = pos;
        return this;
    }
    
    public GridDefinitionBuilder startPos(int x, int y) {
        return this.startPos(new Vector2i(x, y));
    }
    
    private void validate(ResourceLocation id) {
        if (this.key == null) {
            throw new IllegalStateException("No key for linguistics grid " + id.toString());
        }
        if (this.bookLanguage == null) {
            throw new IllegalStateException("No language for linguistics grid " + id.toString());
        }
        if (this.startPos == null) {
            throw new IllegalStateException("No start position for linguistics grid " + id.toString());
        }
        if (this.startPos.x() < GridDefinition.MIN_POS || this.startPos.x() > GridDefinition.MAX_POS) {
            throw new IllegalStateException("Out of bounds start position X-coordinate for linguistics grid " + id.toString() + "; must be between " + GridDefinition.MIN_POS + " and " + GridDefinition.MAX_POS);
        }
        if (this.startPos.y() < GridDefinition.MIN_POS || this.startPos.y() > GridDefinition.MAX_POS) {
            throw new IllegalStateException("Out of bounds start position Y-coordinate for linguistics grid " + id.toString() + "; must be between " + GridDefinition.MIN_POS + " and " + GridDefinition.MAX_POS);
        }
        // TODO Validate nodes
    }
    
    public void build(Consumer<IFinishedGrid> consumer) {
        this.build(consumer, this.key);
    }
    
    public void build(Consumer<IFinishedGrid> consumer, String name) {
        this.build(consumer, new ResourceLocation(name));
    }
    
    public void build(Consumer<IFinishedGrid> consumer, ResourceLocation id) {
        this.validate(id);
        // TODO Construct finished grid
    }
    
    public static class Result implements IFinishedGrid {
        @Override
        public ResourceLocation getId() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void serialize(JsonObject json) {
            // TODO Auto-generated method stub
            
        }
    }
}
