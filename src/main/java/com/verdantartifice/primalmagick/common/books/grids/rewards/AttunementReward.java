package com.verdantartifice.primalmagick.common.books.grids.rewards;

import com.google.common.base.Verify;
import com.google.gson.JsonObject;
import com.verdantartifice.primalmagick.common.attunements.AttunementManager;
import com.verdantartifice.primalmagick.common.attunements.AttunementType;
import com.verdantartifice.primalmagick.common.sources.Source;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;

/**
 * Linguistics grid reward that grants permanent attunement to a source.
 * 
 * @author Daedalus4096
 */
public class AttunementReward extends AbstractReward {
    public static final String TYPE = "attunement";
    public static final IRewardSerializer<AttunementReward> SERIALIZER = new Serializer();
    
    private Source source;
    private int points;
    
    static {
        AbstractReward.register(TYPE, AttunementReward::fromNBT, SERIALIZER);
    }
    
    private AttunementReward() {}
    
    protected AttunementReward(Source source, int points) {
        Verify.verifyNotNull(source, "Invalid source for attunement reward");
        this.source = source;
        this.points = points;
    }
    
    public static AttunementReward fromNBT(CompoundTag tag) {
        AttunementReward retVal = new AttunementReward();
        retVal.deserializeNBT(tag);
        return retVal;
    }

    @Override
    public void grant(ServerPlayer player) {
        AttunementManager.incrementAttunement(player, this.source, AttunementType.PERMANENT, this.points);
    }

    @Override
    public Component getDescription() {
        Component amountText = Component.translatable("label.primalmagick.attunement_gain." + Mth.clamp(this.points, 0, 5));
        Component sourceText = this.source.getNameText();
        return Component.translatable("label.primalmagick.scribe_table.grid.reward.attunement", sourceText, amountText);
    }

    @Override
    public ResourceLocation getIconLocation() {
        return this.source.getImage();
    }

    @Override
    public String getRewardType() {
        return TYPE;
    }

    @SuppressWarnings("unchecked")
    @Override
    public IRewardSerializer<AttunementReward> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = super.serializeNBT();
        tag.putString("Source", this.source.getTag());
        tag.putInt("Points", this.points);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        super.deserializeNBT(nbt);
        this.source = Source.getSource(nbt.getString("Source"));
        Verify.verifyNotNull(this.source, "Invalid source for attunement reward");
        this.points = nbt.getInt("Points");
    }

    public static class Serializer implements IRewardSerializer<AttunementReward> {
        @Override
        public AttunementReward read(ResourceLocation templateId, JsonObject json) {
            Source source = Source.getSource(json.getAsJsonPrimitive("source").getAsString());
            int points = json.getAsJsonPrimitive("points").getAsInt();
            return new AttunementReward(source, points);
        }

        @Override
        public AttunementReward fromNetwork(FriendlyByteBuf buf) {
            return new AttunementReward(Source.getSource(buf.readUtf()), buf.readVarInt());
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, AttunementReward reward) {
            buf.writeUtf(reward.source.getTag());
            buf.writeVarInt(reward.points);
        }
    }
}
