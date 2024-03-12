package com.verdantartifice.primalmagick.common.books.grids.rewards;

import com.google.common.base.Verify;
import com.google.gson.JsonObject;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.books.BookLanguage;
import com.verdantartifice.primalmagick.common.books.BookLanguagesPM;
import com.verdantartifice.primalmagick.common.books.LinguisticsManager;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;

/**
 * Linguistics grid reward that grants comprehension of a language.
 * 
 * @author Daedalus4096
 */
public class ComprehensionReward extends AbstractReward {
    public static final String TYPE = "comprehension";
    public static final IRewardSerializer<ComprehensionReward> SERIALIZER = new Serializer();
    private static final ResourceLocation ICON_LOC = PrimalMagick.resource("textures/gui/sprites/scribe_table/gain_comprehension.png");
    
    private BookLanguage language;
    private int points;
    
    static {
        AbstractReward.register(TYPE, ComprehensionReward::fromNBT, SERIALIZER);
    }
    
    private ComprehensionReward() {}
    
    protected ComprehensionReward(BookLanguage language, int points) {
        Verify.verifyNotNull(language, "Invalid language for comprehension reward");
        this.language = language;
        this.points = points;
    }
    
    public static ComprehensionReward fromNBT(CompoundTag tag) {
        ComprehensionReward retVal = new ComprehensionReward();
        retVal.deserializeNBT(tag);
        return retVal;
    }
    
    @Override
    public void grant(ServerPlayer player) {
        LinguisticsManager.incrementComprehension(player, this.language, this.points);
    }

    @Override
    public Component getDescription() {
        Component amountText = Component.translatable("label.primalmagick.comprehension_gain." + Mth.clamp(this.points, 0, 5));
        Component langText = this.language.getName();
        return Component.translatable("label.primalmagick.scribe_table.grid.reward.comprehension", langText, amountText);
    }

    @Override
    public ResourceLocation getIconLocation() {
        return ICON_LOC;
    }

    @Override
    public String getRewardType() {
        return TYPE;
    }

    @SuppressWarnings("unchecked")
    @Override
    public IRewardSerializer<ComprehensionReward> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = super.serializeNBT();
        tag.putString("Language", this.language.languageId().toString());
        tag.putInt("Points", this.points);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        super.deserializeNBT(nbt);
        this.language = BookLanguagesPM.LANGUAGES.get().getValue(new ResourceLocation(nbt.getString("Language")));
        Verify.verifyNotNull(this.language, "Invalid language for comprehension reward");
        this.points = nbt.getInt("Points");
    }

    public static class Serializer implements IRewardSerializer<ComprehensionReward> {
        @Override
        public ComprehensionReward read(ResourceLocation templateId, JsonObject json) {
            BookLanguage language = BookLanguagesPM.LANGUAGES.get().getValue(new ResourceLocation(json.getAsJsonPrimitive("language").getAsString()));
            int points = json.getAsJsonPrimitive("points").getAsInt();
            return new ComprehensionReward(language, points);
        }

        @Override
        public ComprehensionReward fromNetwork(FriendlyByteBuf buf) {
            return new ComprehensionReward(BookLanguagesPM.LANGUAGES.get().getValue(buf.readResourceLocation()), buf.readVarInt());
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, ComprehensionReward reward) {
            buf.writeResourceLocation(reward.language.languageId());
            buf.writeVarInt(reward.points);
        }
    }
}
