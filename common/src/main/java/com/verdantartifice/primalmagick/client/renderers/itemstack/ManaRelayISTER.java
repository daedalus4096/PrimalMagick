package com.verdantartifice.primalmagick.client.renderers.itemstack;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.verdantartifice.primalmagick.client.renderers.models.ModelLayersPM;
import com.verdantartifice.primalmagick.client.renderers.tile.model.ManaCubeModel;
import com.verdantartifice.primalmagick.common.blocks.mana.ManaRelayBlock;
import com.verdantartifice.primalmagick.common.sources.Sources;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class ManaRelayISTER extends BlockEntityWithoutLevelRenderer {
    private static final ResourceLocation CORE_TEXTURE = ResourceUtils.loc("entity/mana_cube");
    private static final Material CORE_MATERIAL = new Material(InventoryMenu.BLOCK_ATLAS, CORE_TEXTURE);

    protected ManaCubeModel model;

    public ManaRelayISTER() {
        super(Minecraft.getInstance() == null ? null : Minecraft.getInstance().getBlockEntityRenderDispatcher(),
                Minecraft.getInstance() == null ? null : Minecraft.getInstance().getEntityModels());
    }

    @Override
    public void onResourceManagerReload(ResourceManager pResourceManager) {
        this.model = new ManaCubeModel(Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayersPM.MANA_CUBE));
    }

    @Override
    public void renderByItem(ItemStack pStack, ItemDisplayContext pDisplayContext, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {
        if (pStack.getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof ManaRelayBlock relayBlock) {
            Minecraft mc = Minecraft.getInstance();
            ItemRenderer itemRenderer = mc.getItemRenderer();
            VertexConsumer builder = pBuffer.getBuffer(RenderType.solid());

            // TODO Draw the relay frame

            // TODO Draw the relay core
            final float scale = 0.375F;
            pPoseStack.pushPose();
            pPoseStack.translate(0.5D, 0D, 0.5D);
            pPoseStack.scale(scale, scale, scale);
            VertexConsumer ringBuilder = CORE_MATERIAL.buffer(pBuffer, RenderType::entitySolid);
            this.model.renderToBuffer(pPoseStack, ringBuilder, pPackedLight, pPackedOverlay, -1);  // TODO Cycle through colors
            pPoseStack.popPose();
        }
    }
}
