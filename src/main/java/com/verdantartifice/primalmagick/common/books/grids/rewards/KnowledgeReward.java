package com.verdantartifice.primalmagick.common.books.grids.rewards;

import javax.annotation.Nonnull;

import com.google.common.base.Verify;
import com.google.gson.JsonObject;
import com.verdantartifice.primalmagick.common.research.KnowledgeType;
import com.verdantartifice.primalmagick.common.research.ResearchManager;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

/**
 * Linguistics grid reward that grants research knowledge (i.e. theories or observations).
 * 
 * @author Daedalus4096
 */
public class KnowledgeReward extends AbstractReward {
    public static final String TYPE = "knowledge";
    public static final IRewardSerializer<KnowledgeReward> SERIALIZER = new Serializer();

    private KnowledgeType knowledgeType;
    private int points;
    
    protected KnowledgeReward(@Nonnull KnowledgeType type, int points) {
        Verify.verifyNotNull(type, "Invalid knowledge type for knowledge reward");
        this.knowledgeType = type;
        this.points = points;
    }
    
    @Override
    public void grant(ServerPlayer player) {
        ResearchManager.addKnowledge(player, this.knowledgeType, this.points);
    }

    @Override
    public Component getDescription() {
        Component amountText = Component.literal(Integer.toString(this.points));
        Component typeText = Component.translatable(this.knowledgeType.getNameTranslationKey());
        return Component.translatable("label.primalmagick.scribe_table.grid.reward.knowledge", typeText, amountText);
    }

    @Override
    public ResourceLocation getIconLocation() {
        return this.knowledgeType.getIconLocation();
    }

    @Override
    public String getRewardType() {
        return TYPE;
    }

    @SuppressWarnings("unchecked")
    @Override
    public IRewardSerializer<KnowledgeReward> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = super.serializeNBT();
        tag.putString("Type", this.knowledgeType.getSerializedName());
        tag.putInt("Points", this.points);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        super.deserializeNBT(nbt);
        this.knowledgeType = KnowledgeType.fromName(nbt.getString("Type"));
        Verify.verifyNotNull(this.knowledgeType, "Invalid knowledge type for knowledge reward");
        this.points = nbt.getInt("Points");
    }

    public static class Serializer implements IRewardSerializer<KnowledgeReward> {
        @Override
        public KnowledgeReward read(ResourceLocation templateId, JsonObject json) {
            KnowledgeType type = KnowledgeType.fromName(json.getAsJsonPrimitive("type").getAsString());
            int points = json.getAsJsonPrimitive("points").getAsInt();
            return new KnowledgeReward(type, points);
        }

        @Override
        public KnowledgeReward fromNetwork(FriendlyByteBuf buf) {
            return new KnowledgeReward(KnowledgeType.fromName(buf.readUtf()), buf.readVarInt());
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, KnowledgeReward reward) {
            buf.writeUtf(reward.knowledgeType.getSerializedName());
            buf.writeVarInt(reward.points);
        }
    }
}
