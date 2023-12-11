package com.verdantartifice.primalmagick.common.theorycrafting.rewards;

import com.google.gson.JsonObject;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

/**
 * Theorycrafting reward that grants experience points.
 * 
 * @author Daedalus4096
 */
public class ExperienceReward extends AbstractReward {
    public static final String TYPE = "experience";
    public static final IRewardSerializer<ExperienceReward> SERIALIZER = new Serializer();
    
    private int points;
    
    public ExperienceReward() {
        this.points = 0;
    }
    
    protected ExperienceReward(int points) {
        this.points = points;
    }

    @Override
    public void grant(ServerPlayer player) {
        player.giveExperiencePoints(this.points);
    }

    @Override
    public Component getDescription() {
        Component label = Component.translatable("label.primalmagick.experience.points");
        return Component.translatable("label.primalmagick.research_table.reward", this.points, label);
    }

    @Override
    public String getRewardType() {
        return TYPE;
    }

    @SuppressWarnings("unchecked")
    @Override
    public IRewardSerializer<ExperienceReward> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = super.serializeNBT();
        tag.putInt("Points", this.points);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        super.deserializeNBT(nbt);
        this.points = nbt.getInt("Points");
    }

    public static class Serializer implements IRewardSerializer<ExperienceReward> {
        @Override
        public ExperienceReward read(ResourceLocation templateId, JsonObject json) {
            int points = json.getAsJsonPrimitive("points").getAsInt();
            return new ExperienceReward(points);
        }

        @Override
        public ExperienceReward fromNetwork(FriendlyByteBuf buf) {
            return new ExperienceReward(buf.readVarInt());
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, ExperienceReward reward) {
            buf.writeVarInt(reward.points);
        }
    }
}
