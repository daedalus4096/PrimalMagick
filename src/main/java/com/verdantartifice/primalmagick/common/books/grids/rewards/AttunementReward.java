package com.verdantartifice.primalmagick.common.books.grids.rewards;

import java.util.Optional;

import com.google.common.base.Verify;
import com.google.gson.JsonObject;
import com.verdantartifice.primalmagick.common.attunements.AttunementManager;
import com.verdantartifice.primalmagick.common.attunements.AttunementType;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.sources.Sources;

import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;

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
    private Optional<Component> pointsText = Optional.empty();

    public static void init() {
        AbstractReward.register(TYPE, AttunementReward::fromNBT, SERIALIZER);
    }
    
    private AttunementReward() {}
    
    protected AttunementReward(Source source, int points) {
        Verify.verifyNotNull(source, "Invalid source for attunement reward");
        this.source = source;
        this.setPoints(points);
    }
    
    public static AttunementReward fromNBT(CompoundTag tag) {
        AttunementReward retVal = new AttunementReward();
        retVal.deserializeNBT(tag);
        return retVal;
    }

    protected void setPoints(int points) {
        this.points = points;
        this.pointsText = points > 1 ? Optional.of(Component.literal(Integer.toString(points))) : Optional.empty();
    }
    
    @Override
    public void grant(ServerPlayer player, RegistryAccess registryAccess) {
        AttunementManager.incrementAttunement(player, this.source, AttunementType.PERMANENT, this.points);
    }

    @Override
    public Component getDescription(Player player, RegistryAccess registryAccess) {
        Component amountText = Component.translatable("label.primalmagick.attunement_gain." + Mth.clamp(this.points, 0, 5));
        Component sourceText = this.source.isDiscovered(player) ? this.source.getNameText() : Component.translatable(Source.getUnknownTranslationKey());
        return Component.translatable("label.primalmagick.scribe_table.grid.reward.attunement", sourceText, amountText);
    }

    @Override
    public ResourceLocation getIconLocation(Player player) {
        return this.source.isDiscovered(player) ? this.source.getImage() : Source.getUnknownImage();
    }

    @Override
    public Optional<Component> getAmountText() {
        return this.pointsText;
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
        tag.putString("Source", this.source.getId().toString());
        tag.putInt("Points", this.points);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        super.deserializeNBT(nbt);
        this.source = Sources.get(new ResourceLocation(nbt.getString("Source")));
        Verify.verifyNotNull(this.source, "Invalid source for attunement reward");
        this.setPoints(nbt.getInt("Points"));
    }

    public static class Serializer implements IRewardSerializer<AttunementReward> {
        @Override
        public AttunementReward read(ResourceLocation templateId, JsonObject json) {
            Source source = Sources.get(new ResourceLocation(json.getAsJsonPrimitive("source").getAsString()));
            int points = json.getAsJsonPrimitive("points").getAsInt();
            return new AttunementReward(source, points);
        }

        @Override
        public AttunementReward fromNetwork(FriendlyByteBuf buf) {
            return new AttunementReward(Sources.get(buf.readResourceLocation()), buf.readVarInt());
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, AttunementReward reward) {
            buf.writeResourceLocation(reward.source.getId());
            buf.writeVarInt(reward.points);
        }
    }
}
