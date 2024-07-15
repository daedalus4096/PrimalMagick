package com.verdantartifice.primalmagick.common.books.grids;

import javax.annotation.Nonnull;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.books.grids.rewards.AbstractReward;
import com.verdantartifice.primalmagick.common.books.grids.rewards.EmptyReward;
import com.verdantartifice.primalmagick.common.books.grids.rewards.IReward;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

/**
 * Class encapsulating a data-defined node definition for a linguistics grid.  These definitions
 * determine the contents of each node of a specific linguistics comprehension grid.
 * 
 * @author Daedalus4096
 */
public class GridNodeDefinition {
    public static Codec<GridNodeDefinition> codec() {
        return RecordCodecBuilder.create(instance -> instance.group(
                Codec.INT.fieldOf("vocabularyCost").forGetter(d -> d.vocabularyCost),
                AbstractReward.dispatchCodec().fieldOf("reward").forGetter(d -> d.reward)
            ).apply(instance, GridNodeDefinition::new));
    }

    public static StreamCodec<RegistryFriendlyByteBuf, GridNodeDefinition> streamCodec() {
        return StreamCodec.composite(
                ByteBufCodecs.VAR_INT, d -> d.vocabularyCost,
                AbstractReward.dispatchStreamCodec(), d -> d.reward,
                GridNodeDefinition::new);
    }
    
    protected int vocabularyCost;
    protected AbstractReward<?> reward;
    
    protected GridNodeDefinition(int vocabularyCost, AbstractReward<?> reward) {
        this.vocabularyCost = vocabularyCost;
        this.reward = reward;
    }
    
    public int getVocabularyCost() {
        return this.vocabularyCost;
    }
    
    @Nonnull
    public IReward getReward() {
        return this.reward == null ? EmptyReward.INSTANCE : this.reward;
    }
}
