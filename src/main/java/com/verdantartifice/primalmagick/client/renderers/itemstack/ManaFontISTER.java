package com.verdantartifice.primalmagick.client.renderers.itemstack;

import java.awt.Color;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.client.renderers.tile.ManaFontTER;
import com.verdantartifice.primalmagick.common.blocks.mana.AbstractManaFontBlock;
import com.verdantartifice.primalmagick.common.misc.DeviceTier;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

/**
 * Custom item stack renderer for ancient mana fonts.
 * 
 * @author Daedalus4096
 * @see {@link com.verdantartifice.primalmagick.common.blocks.mana.AbstractManaFontBlock}
 */
public class ManaFontISTER extends BlockEntityWithoutLevelRenderer {
    private static final ModelResourceLocation MRL_BASIC = new ModelResourceLocation(PrimalMagick.resource("ancient_font_earth"), "");
    private static final ModelResourceLocation MRL_ENCHANTED = new ModelResourceLocation(PrimalMagick.resource("artificial_font_earth"), "");
    private static final ModelResourceLocation MRL_FORBIDDEN = new ModelResourceLocation(PrimalMagick.resource("forbidden_font_earth"), "");
    private static final ModelResourceLocation MRL_HEAVENLY = new ModelResourceLocation(PrimalMagick.resource("heavenly_font_earth"), "");
    
    public ManaFontISTER() {
        super(Minecraft.getInstance() == null ? null : Minecraft.getInstance().getBlockEntityRenderDispatcher(), 
                Minecraft.getInstance() == null ? null : Minecraft.getInstance().getEntityModels());
    }
    
    protected void addVertex(VertexConsumer renderer, PoseStack stack, float x, float y, float z, float r, float g, float b, float u, float v) {
        renderer.vertex(stack.last().pose(), x, y, z)
                .color(r, g, b, 1.0F)
                .uv(u, v)
                .uv2(240, 240)
                .normal(1, 0, 0)
                .endVertex();
    }
    
    protected ModelResourceLocation getModelResourceLocation(DeviceTier tier) {
        return switch (tier) {
            case BASIC -> MRL_BASIC;
            case ENCHANTED -> MRL_ENCHANTED;
            case FORBIDDEN -> MRL_FORBIDDEN;
            case HEAVENLY -> MRL_HEAVENLY;
            default -> MRL_BASIC;
        };
    }
    
    @Override
    public void renderByItem(ItemStack itemStack, ItemDisplayContext transformType, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        Item item = itemStack.getItem();
        if (item instanceof BlockItem && ((BlockItem)item).getBlock() instanceof AbstractManaFontBlock) {
            AbstractManaFontBlock block = (AbstractManaFontBlock)((BlockItem)item).getBlock();
            Minecraft mc = Minecraft.getInstance();
            ItemRenderer itemRenderer = mc.getItemRenderer();
            
            Color sourceColor = new Color(block.getSource().getColor());
            float r = sourceColor.getRed() / 255.0F;
            float g = sourceColor.getGreen() / 255.0F;
            float b = sourceColor.getBlue() / 255.0F;
            float ds = 0.1875F;

            @SuppressWarnings("deprecation")
            TextureAtlasSprite sprite = mc.getModelManager().getAtlas(TextureAtlas.LOCATION_BLOCKS).getSprite(ManaFontTER.TEXTURE);
            VertexConsumer builder = buffer.getBuffer(RenderType.solid());
            
            // Draw the font base
            BakedModel model = mc.getModelManager().getModel(this.getModelResourceLocation(block.getDeviceTier()));
            itemRenderer.renderModelLists(model, itemStack, combinedLight, combinedOverlay, matrixStack, builder);
            
            // Draw the font core
            matrixStack.pushPose();
            matrixStack.translate(0.5D, 0.5D, 0.5D);
            matrixStack.mulPose(Axis.ZP.rotationDegrees(45.0F)); // Tilt the core onto its diagonal
            matrixStack.mulPose(Axis.XP.rotationDegrees(45.0F)); // Tilt the core onto its diagonal
            
            // Draw the south face of the core
            this.addVertex(builder, matrixStack, -ds, ds, ds, r, g, b, sprite.getU0(), sprite.getV1());
            this.addVertex(builder, matrixStack, -ds, -ds, ds, r, g, b, sprite.getU0(), sprite.getV0());
            this.addVertex(builder, matrixStack, ds, -ds, ds, r, g, b, sprite.getU1(), sprite.getV0());
            this.addVertex(builder, matrixStack, ds, ds, ds, r, g, b, sprite.getU1(), sprite.getV1());
            
            // Draw the north face of the core
            this.addVertex(builder, matrixStack, -ds, ds, -ds, r, g, b, sprite.getU0(), sprite.getV1());
            this.addVertex(builder, matrixStack, ds, ds, -ds, r, g, b, sprite.getU1(), sprite.getV1());
            this.addVertex(builder, matrixStack, ds, -ds, -ds, r, g, b, sprite.getU1(), sprite.getV0());
            this.addVertex(builder, matrixStack, -ds, -ds, -ds, r, g, b, sprite.getU0(), sprite.getV0());
            
            // Draw the east face of the core
            this.addVertex(builder, matrixStack, ds, ds, -ds, r, g, b, sprite.getU0(), sprite.getV1());
            this.addVertex(builder, matrixStack, ds, ds, ds, r, g, b, sprite.getU1(), sprite.getV1());
            this.addVertex(builder, matrixStack, ds, -ds, ds, r, g, b, sprite.getU1(), sprite.getV0());
            this.addVertex(builder, matrixStack, ds, -ds, -ds, r, g, b, sprite.getU0(), sprite.getV0());
            
            // Draw the west face of the core
            this.addVertex(builder, matrixStack, -ds, -ds, ds, r, g, b, sprite.getU1(), sprite.getV0());
            this.addVertex(builder, matrixStack, -ds, ds, ds, r, g, b, sprite.getU1(), sprite.getV1());
            this.addVertex(builder, matrixStack, -ds, ds, -ds, r, g, b, sprite.getU0(), sprite.getV1());
            this.addVertex(builder, matrixStack, -ds, -ds, -ds, r, g, b, sprite.getU0(), sprite.getV0());
            
            // Draw the top face of the core
            this.addVertex(builder, matrixStack, ds, ds, -ds, r, g, b, sprite.getU1(), sprite.getV0());
            this.addVertex(builder, matrixStack, -ds, ds, -ds, r, g, b, sprite.getU0(), sprite.getV0());
            this.addVertex(builder, matrixStack, -ds, ds, ds, r, g, b, sprite.getU0(), sprite.getV1());
            this.addVertex(builder, matrixStack, ds, ds, ds, r, g, b, sprite.getU1(), sprite.getV1());
            
            // Draw the bottom face of the core
            this.addVertex(builder, matrixStack, ds, -ds, -ds, r, g, b, sprite.getU1(), sprite.getV0());
            this.addVertex(builder, matrixStack, ds, -ds, ds, r, g, b, sprite.getU1(), sprite.getV1());
            this.addVertex(builder, matrixStack, -ds, -ds, ds, r, g, b, sprite.getU0(), sprite.getV1());
            this.addVertex(builder, matrixStack, -ds, -ds, -ds, r, g, b, sprite.getU0(), sprite.getV0());
            
            matrixStack.popPose();
        }
    }
}
