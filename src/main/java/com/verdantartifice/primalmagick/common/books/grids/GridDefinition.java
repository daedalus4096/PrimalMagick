package com.verdantartifice.primalmagick.common.books.grids;

import java.util.HashMap;
import java.util.Map;

import org.joml.Vector2i;

import com.google.gson.JsonObject;
import com.verdantartifice.primalmagick.common.books.BookLanguage;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

/**
 * Class encapsulating a data-defined definition for a linguistics grid.  These definitions determine
 * the layout and contents of the linguistics comprehension grids for each ancient language.
 * 
 * @author Daedalus4096
 */
public class GridDefinition {
    protected ResourceLocation key;
    protected BookLanguage language;
    protected Vector2i startPos;
    protected final Map<Vector2i, GridNodeDefinition> nodes = new HashMap<>();
    
    public static class Serializer implements IGridDefinitionSerializer {
        @Override
        public GridDefinition read(ResourceLocation templateId, JsonObject json) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public GridDefinition fromNetwork(FriendlyByteBuf buf) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, GridDefinition template) {
            // TODO Auto-generated method stub
            
        }
    }
}
