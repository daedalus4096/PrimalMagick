package com.verdantartifice.primalmagick.common.books.grids;

import com.google.gson.JsonObject;
import com.verdantartifice.primalmagick.common.books.grids.rewards.IReward;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

/**
 * Class encapsulating a data-defined node definition for a linguistics grid.  These definitions
 * determine the contents of each node of a specific linguistics comprehension grid.
 * 
 * @author Daedalus4096
 */
public class GridNodeDefinition {
    protected int vocabularyCost;
    protected IReward reward;
    
    public static class Serializer implements IGridNodeDefinitionSerializer {
        @Override
        public GridNodeDefinition read(ResourceLocation templateId, JsonObject json) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public GridNodeDefinition fromNetwork(FriendlyByteBuf buf) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, GridNodeDefinition template) {
            // TODO Auto-generated method stub
            
        }
    }
}
