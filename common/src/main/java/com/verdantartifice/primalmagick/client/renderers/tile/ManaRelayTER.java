package com.verdantartifice.primalmagick.client.renderers.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.verdantartifice.primalmagick.client.renderers.models.ModelLayersPM;
import com.verdantartifice.primalmagick.client.renderers.tile.model.ManaCubeModel;
import com.verdantartifice.primalmagick.common.tiles.mana.ManaRelayTileEntity;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.block.state.BlockState;

public class ManaRelayTER implements BlockEntityRenderer<ManaRelayTileEntity> {
    public static final ResourceLocation CORE_TEXTURE = ResourceUtils.loc("entity/mana_cube");
    public static final Material CORE_MATERIAL = new Material(InventoryMenu.BLOCK_ATLAS, CORE_TEXTURE);

    protected final ManaCubeModel manaCubeModel;

    public ManaRelayTER(BlockEntityRendererProvider.Context context) {
        this.manaCubeModel = new ManaCubeModel(context.bakeLayer(ModelLayersPM.MANA_CUBE));
    }

    @Override
    public void render(ManaRelayTileEntity manaRelayTileEntity, float v, PoseStack poseStack, MultiBufferSource multiBufferSource, int combinedLight, int combinedOverlay) {
        Minecraft mc = Minecraft.getInstance();
        BlockState state = manaRelayTileEntity.getBlockState();
        long time = manaRelayTileEntity.getLevel().getLevelData().getGameTime();

        // TODO Render the relay's frame

        // TODO Render the relay's core
        final float scale = 0.375F;
        poseStack.pushPose();
        poseStack.scale(scale, scale, scale);
        VertexConsumer ringBuilder = CORE_MATERIAL.buffer(multiBufferSource, RenderType::entitySolid);
        this.manaCubeModel.renderToBuffer(poseStack, ringBuilder, combinedLight, combinedOverlay, -1);  // TODO Cycle through colors
        poseStack.popPose();
    }
}
