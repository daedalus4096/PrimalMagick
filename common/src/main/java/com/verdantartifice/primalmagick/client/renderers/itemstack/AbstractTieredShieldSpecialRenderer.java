package com.verdantartifice.primalmagick.client.renderers.itemstack;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.object.equipment.ShieldModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BannerRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.Unit;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BannerPatternLayers;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3fc;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * Custom item stack renderer for magickal metal shields.
 * 
 * @author Daedalus4096
 * @see com.verdantartifice.primalmagick.common.items.tools.AbstractTieredShieldItem
 */
public abstract class AbstractTieredShieldSpecialRenderer implements SpecialModelRenderer<DataComponentMap> {
    private final SpriteGetter sprites;
    private final ShieldModel model;
    
    public AbstractTieredShieldSpecialRenderer(SpriteGetter sprites, ShieldModel model) {
        this.sprites = sprites;
        this.model = model;
    }

    @Override
    public @Nullable DataComponentMap extractArgument(ItemStack itemStack) {
        return itemStack.immutableComponents();
    }

    @NotNull
    protected abstract SpriteId getSpriteId(boolean hasPatterns);

    @Override
    public void submit(
            @Nullable DataComponentMap components,
            @NotNull PoseStack poseStack,
            @NotNull SubmitNodeCollector submitNodeCollector,
            int lightCoords,
            int overlayCoords,
            boolean hasFoil,
            int outlineColor) {
        BannerPatternLayers patterns = components != null
                ? components.getOrDefault(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY)
                : BannerPatternLayers.EMPTY;
        DyeColor baseColor = components != null ? components.get(DataComponents.BASE_COLOR) : null;
        boolean hasPatterns = !patterns.layers().isEmpty() || baseColor != null;
        SpriteId baseSpriteId = this.getSpriteId(hasPatterns);
        submitNodeCollector.submitModel(this.model, Unit.INSTANCE, poseStack, lightCoords, overlayCoords, -1, baseSpriteId, this.sprites, outlineColor, null);
        if (hasPatterns) {
            BannerRenderer.submitPatterns(
                    this.sprites,
                    poseStack,
                    submitNodeCollector,
                    lightCoords,
                    overlayCoords,
                    this.model,
                    Unit.INSTANCE,
                    false,
                    Objects.requireNonNullElse(baseColor, DyeColor.WHITE),
                    patterns,
                    null
            );
        }

        if (hasFoil) {
            submitNodeCollector.submitModel(
                    this.model, Unit.INSTANCE, poseStack, RenderTypes.entityGlint(), lightCoords, overlayCoords, -1, this.sprites.get(baseSpriteId), 0, null
            );
        }
    }

    @Override
    public void getExtents(@NotNull Consumer<Vector3fc> consumer) {
        PoseStack poseStack = new PoseStack();
        this.model.root().getExtentsForGui(poseStack, consumer);
    }
}
