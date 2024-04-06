package com.verdantartifice.primalmagick.common.books.grids.rewards;

import java.util.Optional;

import com.google.common.base.Verify;
import com.google.gson.JsonObject;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.books.BookLanguage;
import com.verdantartifice.primalmagick.common.books.BookLanguagesPM;
import com.verdantartifice.primalmagick.common.books.LinguisticsManager;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;

import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;

/**
 * Linguistics grid reward that grants comprehension of a language.
 * 
 * @author Daedalus4096
 */
public class ComprehensionReward extends AbstractReward {
    public static final String TYPE = "comprehension";
    public static final IRewardSerializer<ComprehensionReward> SERIALIZER = new Serializer();
    private static final ResourceLocation ICON_LOC = PrimalMagick.resource("textures/gui/sprites/scribe_table/gain_comprehension.png");
    
    private ResourceKey<BookLanguage> language;
    private int points;
    private Optional<Component> pointsText = Optional.empty();
    
    public static void init() {
        AbstractReward.register(TYPE, ComprehensionReward::fromNBT, SERIALIZER);
    }
    
    private ComprehensionReward() {}
    
    protected ComprehensionReward(ResourceKey<BookLanguage> language, int points) {
        Verify.verifyNotNull(language, "Invalid language for comprehension reward");
        this.language = language;
        this.setPoints(points);
    }
    
    public static ComprehensionReward fromNBT(CompoundTag tag) {
        ComprehensionReward retVal = new ComprehensionReward();
        retVal.deserializeNBT(tag);
        return retVal;
    }
    
    protected void setPoints(int points) {
        this.points = points;
        this.pointsText = Optional.of(Component.literal(Integer.toString(this.points)));
    }
    
    @Override
    public void grant(ServerPlayer player, RegistryAccess registryAccess) {
        registryAccess.registryOrThrow(RegistryKeysPM.BOOK_LANGUAGES).getHolder(this.language).ifPresent(langHolder -> {
            LinguisticsManager.incrementComprehension(player, langHolder, this.points);
        });
    }

    @Override
    public Component getDescription(Player player) {
        Component amountText = Component.translatable("label.primalmagick.comprehension_gain." + Mth.clamp(this.points, 0, 5));
        Component langText = this.language.getName();
        return Component.translatable("label.primalmagick.scribe_table.grid.reward.comprehension", langText, amountText);
    }

    @Override
    public ResourceLocation getIconLocation(Player player) {
        return ICON_LOC;
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
    public IRewardSerializer<ComprehensionReward> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = super.serializeNBT();
        tag.putString("Language", this.language.location().toString());
        tag.putInt("Points", this.points);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        super.deserializeNBT(nbt);
        ResourceLocation langLoc = new ResourceLocation(nbt.getString("Language"));
        Verify.verifyNotNull(langLoc, "Invalid language for comprehension reward");
        this.language = ResourceKey.create(RegistryKeysPM.BOOK_LANGUAGES, langLoc);
        this.setPoints(nbt.getInt("Points"));
    }

    public static class Serializer implements IRewardSerializer<ComprehensionReward> {
        @Override
        public ComprehensionReward read(ResourceLocation templateId, JsonObject json) {
            ResourceKey<BookLanguage> language = ResourceKey.create(RegistryKeysPM.BOOK_LANGUAGES, new ResourceLocation(json.getAsJsonPrimitive("language").getAsString()));
            int points = json.getAsJsonPrimitive("points").getAsInt();
            return new ComprehensionReward(language, points);
        }

        @Override
        public ComprehensionReward fromNetwork(FriendlyByteBuf buf) {
            return new ComprehensionReward(buf.readResourceKey(RegistryKeysPM.BOOK_LANGUAGES), buf.readVarInt());
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, ComprehensionReward reward) {
            buf.writeResourceKey(reward.language);
            buf.writeVarInt(reward.points);
        }
    }
}
