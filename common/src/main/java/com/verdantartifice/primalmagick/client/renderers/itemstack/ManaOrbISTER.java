package com.verdantartifice.primalmagick.client.renderers.itemstack;

import com.mojang.blaze3d.vertex.PoseStack;
import com.verdantartifice.primalmagick.client.renderers.itemstack.model.ManaOrbAdeptModel;
import com.verdantartifice.primalmagick.client.renderers.itemstack.model.ManaOrbApprenticeModel;
import com.verdantartifice.primalmagick.client.renderers.itemstack.model.ManaOrbArchmageModel;
import com.verdantartifice.primalmagick.client.renderers.itemstack.model.ManaOrbNuggetModel;
import com.verdantartifice.primalmagick.client.renderers.itemstack.model.ManaOrbWizardModel;
import com.verdantartifice.primalmagick.client.renderers.models.ModelLayersPM;
import com.verdantartifice.primalmagick.common.misc.DeviceTier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class ManaOrbISTER extends BlockEntityWithoutLevelRenderer {
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
        // TODO Stub
    }

    private Model getCoreModel(DeviceTier tier) {
        return switch (tier) {
            case BASIC -> this.apprenticeCoreModel;
            case ENCHANTED -> this.adeptCoreModel;
            case FORBIDDEN -> this.wizardCoreModel;
            case HEAVENLY, CREATIVE -> this.archmageCoreModel;
        };
    }
}
