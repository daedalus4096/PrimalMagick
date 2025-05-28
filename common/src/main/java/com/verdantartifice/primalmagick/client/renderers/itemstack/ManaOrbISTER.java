package com.verdantartifice.primalmagick.client.renderers.itemstack;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.verdantartifice.primalmagick.client.renderers.itemstack.model.ManaOrbAdeptModel;
import com.verdantartifice.primalmagick.client.renderers.itemstack.model.ManaOrbApprenticeModel;
import com.verdantartifice.primalmagick.client.renderers.itemstack.model.ManaOrbArchmageModel;
import com.verdantartifice.primalmagick.client.renderers.itemstack.model.ManaOrbNuggetModel;
import com.verdantartifice.primalmagick.client.renderers.itemstack.model.ManaOrbWizardModel;
import com.verdantartifice.primalmagick.client.renderers.models.ModelLayersPM;
import com.verdantartifice.primalmagick.common.items.tools.ManaOrbItem;
import com.verdantartifice.primalmagick.common.misc.DeviceTier;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class ManaOrbISTER extends BlockEntityWithoutLevelRenderer {
    protected static final ResourceLocation TEXTURE_APPRENTICE = ResourceUtils.loc("textures/entity/mana_orb/apprentice.png");
    protected static final ResourceLocation TEXTURE_ADEPT = ResourceUtils.loc("textures/entity/mana_orb/adept.png");
    protected static final ResourceLocation TEXTURE_WIZARD = ResourceUtils.loc("textures/entity/mana_orb/wizard.png");
    protected static final ResourceLocation TEXTURE_ARCHMAGE = ResourceUtils.loc("textures/entity/mana_orb/archmage.png");
    protected static final ResourceLocation TEXTURE_NUGGET = ResourceUtils.loc("textures/entity/mana_orb/nugget.png");

    protected static final int BOB_CYCLE_TIME_TICKS = 200;

    protected Model apprenticeCoreModel;
    protected Model adeptCoreModel;
    protected Model wizardCoreModel;
    protected Model archmageCoreModel;
    protected Model nuggetModel;

    public ManaOrbISTER() {
        super(Minecraft.getInstance() == null ? null : Minecraft.getInstance().getBlockEntityRenderDispatcher(),
                Minecraft.getInstance() == null ? null : Minecraft.getInstance().getEntityModels());
    }

    @Override
    public void onResourceManagerReload(ResourceManager pResourceManager) {
        this.apprenticeCoreModel = new ManaOrbApprenticeModel(Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayersPM.MANA_ORB_APPRENTICE));
        this.adeptCoreModel = new ManaOrbAdeptModel(Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayersPM.MANA_ORB_ADEPT));
        this.wizardCoreModel = new ManaOrbWizardModel(Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayersPM.MANA_ORB_WIZARD));
        this.archmageCoreModel = new ManaOrbArchmageModel(Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayersPM.MANA_ORB_ARCHMAGE));
        this.nuggetModel = new ManaOrbNuggetModel(Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayersPM.MANA_ORB_NUGGET));
    }

    @Override
    public void renderByItem(ItemStack pStack, ItemDisplayContext pDisplayContext, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {
        if (pStack.getItem() instanceof ManaOrbItem manaOrbItem) {
            Minecraft mc = Minecraft.getInstance();
            long time = mc.level.getGameTime();
            boolean animateNuggets = shouldAnimateNuggets(pDisplayContext);
            double partialTime = time + (double)mc.getTimer().getGameTimeDeltaPartialTick(false);
            double bobDelta = 0.0625D * Math.sin(partialTime * (2D * Math.PI / (double)BOB_CYCLE_TIME_TICKS));
            int rot = 2 * (int)(time % 360);

            DeviceTier tier = manaOrbItem.getDeviceTier();
            Model coreModel = this.getCoreModel(tier);

            pPoseStack.pushPose();
            if (pDisplayContext.firstPerson()) {
                pPoseStack.translate(0D, bobDelta, 0D);
            }
            pPoseStack.scale(1.0F, -1.0F, -1.0F);

            pPoseStack.pushPose();
            VertexConsumer coreVertexConsumer = ItemRenderer.getFoilBufferDirect(pBuffer, coreModel.renderType(getCoreTexture(tier)), false, pStack.hasFoil());
            coreModel.renderToBuffer(pPoseStack, coreVertexConsumer, pPackedLight, pPackedOverlay, -1);
            pPoseStack.popPose();

            pPoseStack.pushPose();
            VertexConsumer nuggetVertexConsumer = ItemRenderer.getFoilBufferDirect(pBuffer, this.nuggetModel.renderType(TEXTURE_NUGGET), false, pStack.hasFoil());
            for (int nuggetIndex = 0; nuggetIndex < 4; nuggetIndex++) {
                pPoseStack.pushPose();
                pPoseStack.mulPose(Axis.YP.rotationDegrees(45 + (90 * nuggetIndex)));
                if (animateNuggets) {
                    pPoseStack.mulPose(Axis.YP.rotationDegrees(rot));
                }
                pPoseStack.translate(0.25D, -0.0625D, 0D);
                if (animateNuggets) {
                    pPoseStack.translate(bobDelta, 0D, 0D);
                }
                this.nuggetModel.renderToBuffer(pPoseStack, nuggetVertexConsumer, pPackedLight, pPackedOverlay, -1);
                pPoseStack.popPose();
            }
            pPoseStack.popPose();

            pPoseStack.popPose();
        }
    }

    private static boolean shouldAnimateNuggets(ItemDisplayContext context) {
        return context == ItemDisplayContext.FIRST_PERSON_RIGHT_HAND ||
                context == ItemDisplayContext.FIRST_PERSON_LEFT_HAND ||
                context == ItemDisplayContext.THIRD_PERSON_RIGHT_HAND ||
                context == ItemDisplayContext.THIRD_PERSON_LEFT_HAND;
    }

    private Model getCoreModel(DeviceTier tier) {
        return switch (tier) {
            case BASIC -> this.apprenticeCoreModel;
            case ENCHANTED -> this.adeptCoreModel;
            case FORBIDDEN -> this.wizardCoreModel;
            case HEAVENLY, CREATIVE -> this.archmageCoreModel;
        };
    }

    private static ResourceLocation getCoreTexture(DeviceTier tier) {
        return switch (tier) {
            case BASIC -> TEXTURE_APPRENTICE;
            case ENCHANTED -> TEXTURE_ADEPT;
            case FORBIDDEN -> TEXTURE_WIZARD;
            case HEAVENLY, CREATIVE -> TEXTURE_ARCHMAGE;
        };
    }
}
