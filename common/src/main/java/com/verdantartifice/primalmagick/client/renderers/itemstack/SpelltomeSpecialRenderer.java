package com.verdantartifice.primalmagick.client.renderers.itemstack;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.items.tools.SpelltomeItem;
import com.verdantartifice.primalmagick.common.misc.DeviceTier;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.object.book.BookModel;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3fc;

import java.util.function.Consumer;

public class SpelltomeSpecialRenderer implements SpecialModelRenderer<DeviceTier> {
    protected static final SpriteId TEXTURE_APPRENTICE = Sheets.BLOCK_ENTITIES_MAPPER.apply(ResourceUtils.loc("spelltome/apprentice"));
    protected static final SpriteId TEXTURE_ADEPT = Sheets.BLOCK_ENTITIES_MAPPER.apply(ResourceUtils.loc("spelltome/adept"));
    protected static final SpriteId TEXTURE_WIZARD = Sheets.BLOCK_ENTITIES_MAPPER.apply(ResourceUtils.loc("spelltome/wizard"));
    protected static final SpriteId TEXTURE_ARCHMAGE = Sheets.BLOCK_ENTITIES_MAPPER.apply(ResourceUtils.loc("spelltome/archmage"));

    private final SpriteGetter sprites;
    private final BookModel model;
    private final BookModel.State state;

    public SpelltomeSpecialRenderer(SpriteGetter sprites, BookModel model, BookModel.State state) {
        this.sprites = sprites;
        this.model = model;
        this.state = state;
    }

    private static SpriteId getTextureLocation(@Nullable DeviceTier tier) {
        return tier == null ? TEXTURE_APPRENTICE : switch (tier) {
            case BASIC -> TEXTURE_APPRENTICE;
            case ENCHANTED -> TEXTURE_ADEPT;
            case FORBIDDEN -> TEXTURE_WIZARD;
            case HEAVENLY, CREATIVE -> TEXTURE_ARCHMAGE;
        };
    }

    @Override
    public void submit(
            @Nullable DeviceTier tier,
            @NotNull PoseStack poseStack,
            @NotNull SubmitNodeCollector submitNodeCollector,
            int lightCoords,
            int overlayCoords,
            boolean hasFoil,
            int outlineColor) {
        submitNodeCollector.submitModel(
                this.model, this.state, poseStack, lightCoords, overlayCoords, -1, getTextureLocation(tier), this.sprites, outlineColor, null
        );
    }

    @Override
    public void getExtents(@NotNull Consumer<Vector3fc> consumer) {
        PoseStack poseStack = new PoseStack();
        this.model.setupAnim(this.state);
        this.model.root().getExtentsForGui(poseStack, consumer);
    }

    @Override
    public @Nullable DeviceTier extractArgument(@NotNull ItemStack itemStack) {
        return itemStack.getItem() instanceof SpelltomeItem tomeItem ? tomeItem.getDeviceTier() : null;
    }

    public record Unbaked(float openAngle, float page1, float page2) implements SpecialModelRenderer.Unbaked<DeviceTier> {
        public static final MapCodec<SpelltomeSpecialRenderer.Unbaked> MAP_CODEC = RecordCodecBuilder.mapCodec(
                i -> i.group(
                                Codec.FLOAT.fieldOf("open_angle").forGetter(SpelltomeSpecialRenderer.Unbaked::openAngle),
                                Codec.FLOAT.fieldOf("page1").forGetter(SpelltomeSpecialRenderer.Unbaked::page1),
                                Codec.FLOAT.fieldOf("page2").forGetter(SpelltomeSpecialRenderer.Unbaked::page2)
                        )
                        .apply(i, SpelltomeSpecialRenderer.Unbaked::new)
        );

        @Override
        public SpelltomeSpecialRenderer bake(@NotNull BakingContext context) {
            return new SpelltomeSpecialRenderer(
                    context.sprites(),
                    new BookModel(context.entityModelSet().bakeLayer(ModelLayers.BOOK)),
                    new BookModel.State(this.openAngle * (float) (Math.PI / 180.0), this.page1, this.page2)
            );
        }

        @Override
        @NotNull
        public MapCodec<SpelltomeSpecialRenderer.Unbaked> type() {
            return MAP_CODEC;
        }
    }
}
