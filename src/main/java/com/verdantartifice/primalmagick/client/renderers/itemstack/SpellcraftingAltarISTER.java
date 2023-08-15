package com.verdantartifice.primalmagick.client.renderers.itemstack;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.client.renderers.models.ModelLayersPM;
import com.verdantartifice.primalmagick.client.renderers.tile.model.SpellcraftingAltarRingModel;
import com.verdantartifice.primalmagick.common.blocks.BlocksPM;
import com.verdantartifice.primalmagick.common.blocks.crafting.SpellcraftingAltarBlock;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

/**
 * Custom item stack renderer for spellcrafting altars.
 * 
 * @author Daedalus4096
 */
public class SpellcraftingAltarISTER extends BlockEntityWithoutLevelRenderer {
    private static final ModelResourceLocation MRL = BlockModelShaper.stateToModelLocation(BlocksPM.SPELLCRAFTING_ALTAR.get().defaultBlockState());
    private static final ResourceLocation TEXTURE = PrimalMagick.resource("entity/spellcrafting_altar/spellcrafting_altar_ring");
    private static final Material RING_MATERIAL = new Material(InventoryMenu.BLOCK_ATLAS, TEXTURE);
    
    protected SpellcraftingAltarRingModel model;
    
    public SpellcraftingAltarISTER() {
        super(Minecraft.getInstance() == null ? null : Minecraft.getInstance().getBlockEntityRenderDispatcher(), 
                Minecraft.getInstance() == null ? null : Minecraft.getInstance().getEntityModels());
    }

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {
        this.model = new SpellcraftingAltarRingModel(Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayersPM.SPELLCRAFTING_ALTAR_RING));
    }

    @Override
    public void renderByItem(ItemStack itemStack, ItemDisplayContext transformType, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        Item item = itemStack.getItem();
        if (item instanceof BlockItem blockItem && blockItem.getBlock() instanceof SpellcraftingAltarBlock) {
            Minecraft mc = Minecraft.getInstance();
            ItemRenderer itemRenderer = mc.getItemRenderer();
            VertexConsumer builder = buffer.getBuffer(RenderType.solid());
            
            // Draw the altar base
            BakedModel model = mc.getModelManager().getModel(MRL);
            itemRenderer.renderModelLists(model, itemStack, combinedLight, combinedOverlay, matrixStack, builder);
            
            // Draw the altar ring
            matrixStack.pushPose();
            matrixStack.translate(0.5D, 0D, 0.5D);
            matrixStack.translate(0D, 2.4D, 0D);    // Model position correction
            matrixStack.mulPose(Axis.YP.rotationDegrees(90F));  // Model rotation correction
            matrixStack.scale(1.0F, -1.0F, -1.0F);
            VertexConsumer ringBuilder = RING_MATERIAL.buffer(buffer, RenderType::entitySolid);
            this.model.renderToBuffer(matrixStack, ringBuilder, combinedLight, combinedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
            matrixStack.popPose();
        }
    }
}
