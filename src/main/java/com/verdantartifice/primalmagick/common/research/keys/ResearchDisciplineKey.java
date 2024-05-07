package com.verdantartifice.primalmagick.common.research.keys;

import javax.annotation.Nonnull;

import com.mojang.serialization.Codec;
import com.verdantartifice.primalmagick.common.research.requirements.RequirementCategory;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;

public class ResearchDisciplineKey extends AbstractResearchKey<ResearchDisciplineKey> {
    public static final Codec<ResearchDisciplineKey> CODEC = Codec.STRING.fieldOf("rootKey").xmap(ResearchDisciplineKey::new, key -> key.rootKey).codec();
    
    protected final String rootKey;
    
    public ResearchDisciplineKey(String rootKey) {
        this.rootKey = rootKey;
    }
    
    public String getRootKey() {
        return this.rootKey;
    }

    @Override
    public String toString() {
        return this.rootKey;
    }

    @Override
    public RequirementCategory getRequirementCategory() {
        return RequirementCategory.RESEARCH;
    }

    @Override
    protected ResearchKeyType<ResearchDisciplineKey> getType() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isKnownBy(Player player) {
        // TODO Auto-generated method stub
        return super.isKnownBy(player);
    }
    
    @Nonnull
    public static ResearchDisciplineKey fromNetwork(FriendlyByteBuf buf) {
        return new ResearchDisciplineKey(buf.readUtf());
    }

    @Override
    protected void toNetworkInner(FriendlyByteBuf buf) {
        buf.writeUtf(this.rootKey);
    }

}
