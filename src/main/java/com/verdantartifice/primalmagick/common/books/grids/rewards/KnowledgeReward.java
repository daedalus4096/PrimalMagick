package com.verdantartifice.primalmagick.common.books.grids.rewards;

import java.util.Optional;

import javax.annotation.Nonnull;

import com.google.common.base.Verify;
import com.google.gson.JsonObject;
import com.verdantartifice.primalmagick.common.research.KnowledgeType;
import com.verdantartifice.primalmagick.common.research.ResearchManager;

import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

/**
 * Linguistics grid reward that grants research knowledge (i.e. theories or observations).
 * 
 * @author Daedalus4096
 */
public class KnowledgeReward extends AbstractReward {
    public static final String TYPE = "knowledge";
    public static final IRewardSerializer<KnowledgeReward> SERIALIZER = new Serializer();

    private KnowledgeType knowledgeType;
    private int levels;
    private Optional<Component> levelsText;
    
    public static void init() {
        AbstractReward.register(TYPE, KnowledgeReward::fromNBT, SERIALIZER);
    }
    
    private KnowledgeReward() {}
    
    protected KnowledgeReward(@Nonnull KnowledgeType type, int levels) {
        Verify.verifyNotNull(type, "Invalid knowledge type for knowledge reward");
        this.knowledgeType = type;
        this.setLevels(levels);
    }
    
    public static KnowledgeReward fromNBT(CompoundTag tag) {
        KnowledgeReward retVal = new KnowledgeReward();
        retVal.deserializeNBT(tag);
        return retVal;
    }
    
    protected void setLevels(int levels) {
        this.levels = levels;
        this.levelsText = levels > 1 ? Optional.of(Component.literal(Integer.toString(levels))) : Optional.empty();
    }
    
    @Override
    public void grant(ServerPlayer player, RegistryAccess registryAccess) {
        ResearchManager.addKnowledge(player, this.knowledgeType, this.levels * this.knowledgeType.getProgression());
    }

    @Override
    public Component getDescription(Player player) {
        Component amountText = Component.literal(Integer.toString(this.levels));
        Component typeText = Component.translatable(this.knowledgeType.getNameTranslationKey());
        return Component.translatable("label.primalmagick.scribe_table.grid.reward.knowledge", typeText, amountText);
    }

    @Override
    public ResourceLocation getIconLocation(Player player) {
        return this.knowledgeType.getIconLocation();
    }

    @Override
    public Optional<Component> getAmountText() {
        return this.levelsText;
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
        tag.putString("KnowledgeType", this.knowledgeType.getSerializedName());
        tag.putInt("Levels", this.levels);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        super.deserializeNBT(nbt);
        this.knowledgeType = KnowledgeType.fromName(nbt.getString("KnowledgeType"));
        Verify.verifyNotNull(this.knowledgeType, "Invalid knowledge type for knowledge reward");
        this.setLevels(nbt.getInt("Levels"));
    }

    public static class Serializer implements IRewardSerializer<KnowledgeReward> {
        @Override
        public KnowledgeReward read(ResourceLocation templateId, JsonObject json) {
            KnowledgeType type = KnowledgeType.fromName(json.getAsJsonPrimitive("knowledge_type").getAsString());
            int levels = json.getAsJsonPrimitive("levels").getAsInt();
            return new KnowledgeReward(type, levels);
        }

        @Override
        public KnowledgeReward fromNetwork(FriendlyByteBuf buf) {
            return new KnowledgeReward(KnowledgeType.fromName(buf.readUtf()), buf.readVarInt());
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, KnowledgeReward reward) {
            buf.writeUtf(reward.knowledgeType.getSerializedName());
            buf.writeVarInt(reward.levels);
        }
    }
}
