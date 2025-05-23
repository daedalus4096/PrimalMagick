package com.verdantartifice.primalmagick.client.renderers.itemstack;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
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
            DeviceTier tier = manaOrbItem.getDeviceTier();
            Model coreModel = this.getCoreModel(tier);

            pPoseStack.pushPose();
            pPoseStack.scale(1.0F, -1.0F, -1.0F);
            VertexConsumer vertexConsumer = ItemRenderer.getFoilBufferDirect(pBuffer, coreModel.renderType(getCoreTexture(tier)), false, pStack.hasFoil());
            coreModel.renderToBuffer(pPoseStack, vertexConsumer, pPackedLight, pPackedOverlay, -1);
            pPoseStack.popPose();
        }
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
