package com.verdantartifice.primalmagick.client.renderers.itemstack;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.object.projectile.TridentModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.special.NoDataSpecialModelRenderer;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3fc;

import java.util.function.Consumer;

/**
 * Custom item stack renderer for magickal metal tridents.
 * 
 * @author Daedalus4096
 * @see com.verdantartifice.primalmagick.common.items.tools.AbstractTieredTridentItem
 */
public abstract class AbstractTieredTridentSpecialRenderer implements NoDataSpecialModelRenderer {
    protected final TridentModel model;
    
    public AbstractTieredTridentSpecialRenderer(TridentModel model) {
        this.model = model;
    }

    @Override
    public void submit(@NotNull PoseStack poseStack, @NotNull SubmitNodeCollector submitNodeCollector, int lightCoords, int overlayCoords, boolean hasFoil, int outlineColor) {
        submitNodeCollector.submitModelPart(
                this.model.root(),
                poseStack,
                this.model.renderType(this.getTextureLocation()),
                lightCoords,
                overlayCoords,
                null,
                false,
                hasFoil,
                -1,
                null,
                outlineColor
        );
    }

    @Override
    public void getExtents(@NotNull Consumer<Vector3fc> consumer) {
        PoseStack poseStack = new PoseStack();
        this.model.root().getExtentsForGui(poseStack, consumer);
    }

    public abstract Identifier getTextureLocation();
}
