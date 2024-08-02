package com.verdantartifice.primalmagick.datagen.linguistics;

import javax.annotation.Nonnull;

import org.joml.Vector2i;

import com.google.gson.JsonObject;
import com.verdantartifice.primalmagick.common.books.grids.GridDefinition;

public class GridNodeDefinitionBuilder {
    protected final int xPos;
    protected final int yPos;
    protected int cost = 1;
    protected IFinishedGridNodeReward reward;
    
    protected GridNodeDefinitionBuilder(int x, int y) {
        this.xPos = x;
        this.yPos = y;
    }
    
    public static GridNodeDefinitionBuilder node(int x, int y) {
        return new GridNodeDefinitionBuilder(x, y);
    }
    
    public GridNodeDefinitionBuilder cost(int cost) {
        this.cost = cost;
        return this;
    }
    
    public GridNodeDefinitionBuilder reward(@Nonnull IFinishedGridNodeReward reward) {
        this.reward = reward;
        return this;
    }
    
    private void validate() {
        if (this.xPos < GridDefinition.MIN_POS || this.xPos > GridDefinition.MAX_POS) {
            throw new IllegalStateException("Out of bounds node position X-coordinate; must be between " + GridDefinition.MIN_POS + " and " + GridDefinition.MAX_POS);
        }
        if (this.yPos < GridDefinition.MIN_POS || this.yPos > GridDefinition.MAX_POS) {
            throw new IllegalStateException("Out of bounds node position Y-coordinate; must be between " + GridDefinition.MIN_POS + " and " + GridDefinition.MAX_POS);
        }
        if (this.cost < 0) {
            throw new IllegalStateException("Invalid cost value for linguistics grid node");
        }
        if (this.reward == null) {
            throw new IllegalStateException("No reward for linguistics grid node");
        }
    }
    
    public IFinishedGridNode build() {
        this.validate();
        return new Result(this.xPos, this.yPos, this.cost, this.reward);
    }
    
    public static class Result implements IFinishedGridNode {
        protected final int xPos;
        protected final int yPos;
        protected final int cost;
        protected final IFinishedGridNodeReward reward;

        public Result(int x, int y, int cost, @Nonnull IFinishedGridNodeReward reward) {
            this.xPos = x;
            this.yPos = y;
            this.cost = cost;
            this.reward = reward;
        }

        @Override
        public Vector2i getPosition() {
            return new Vector2i(this.xPos, this.yPos);
        }

        @Override
        public IFinishedGridNodeReward getReward() {
            return this.reward;
        }

        @Override
        public void serialize(JsonObject json) {
            json.addProperty("x", this.xPos);
            json.addProperty("y", this.yPos);
            json.addProperty("cost", this.cost);
            json.add("reward", this.reward.serialize());
        }
    }
}
