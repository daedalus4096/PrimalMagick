package com.verdantartifice.primalmagick.client.renderers.itemstack;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.verdantartifice.primalmagick.client.renderers.tile.ManaFontTER;
import com.verdantartifice.primalmagick.common.blocks.mana.AbstractManaFontBlock;
import com.verdantartifice.primalmagick.common.misc.DeviceTier;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import com.verdantartifice.primalmagick.platform.Services;
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

import java.awt.Color;

/**
 * Custom item stack renderer for ancient mana fonts.
 * 
 * @author Daedalus4096
 * @see com.verdantartifice.primalmagick.common.blocks.mana.AbstractManaFontBlock
 */
public class ManaFontISTER extends BlockEntityWithoutLevelRenderer {
    private static final ModelResourceLocation MRL_BASIC = Services.MODEL_RESOURCE_LOCATIONS.create(ResourceUtils.loc("ancient_font_earth"), "");
    private static final ModelResourceLocation MRL_ENCHANTED = Services.MODEL_RESOURCE_LOCATIONS.create(ResourceUtils.loc("artificial_font_earth"), "");
    private static final ModelResourceLocation MRL_FORBIDDEN = Services.MODEL_RESOURCE_LOCATIONS.create(ResourceUtils.loc("forbidden_font_earth"), "");
    private static final ModelResourceLocation MRL_HEAVENLY = Services.MODEL_RESOURCE_LOCATIONS.create(ResourceUtils.loc("heavenly_font_earth"), "");
    
    public ManaFontISTER() {
        super(Minecraft.getInstance() == null ? null : Minecraft.getInstance().getBlockEntityRenderDispatcher(), 
                Minecraft.getInstance() == null ? null : Minecraft.getInstance().getEntityModels());
    }
    
    protected void addVertex(VertexConsumer renderer, PoseStack stack, float x, float y, float z, float r, float g, float b, float u, float v) {
        renderer.addVertex(stack.last().pose(), x, y, z)
                .setColor(r, g, b, 1.0F)
                .setUv(u, v)
                .setUv2(240, 240)
                .setNormal(1, 0, 0);
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
