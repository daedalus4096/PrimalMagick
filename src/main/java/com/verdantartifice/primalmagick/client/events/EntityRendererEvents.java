package com.verdantartifice.primalmagick.client.events;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.client.renderers.entity.BasicPixieRenderer;
import com.verdantartifice.primalmagick.client.renderers.entity.FishingHookRenderer;
import com.verdantartifice.primalmagick.client.renderers.entity.FlyingCarpetRenderer;
import com.verdantartifice.primalmagick.client.renderers.entity.ForbiddenTridentRenderer;
import com.verdantartifice.primalmagick.client.renderers.entity.FriendlyWitchRenderer;
import com.verdantartifice.primalmagick.client.renderers.entity.GrandPixieRenderer;
import com.verdantartifice.primalmagick.client.renderers.entity.HallowsteelGolemRenderer;
import com.verdantartifice.primalmagick.client.renderers.entity.HallowsteelTridentRenderer;
import com.verdantartifice.primalmagick.client.renderers.entity.HexiumGolemRenderer;
import com.verdantartifice.primalmagick.client.renderers.entity.HexiumTridentRenderer;
import com.verdantartifice.primalmagick.client.renderers.entity.InnerDemonRenderer;
import com.verdantartifice.primalmagick.client.renderers.entity.MajesticPixieRenderer;
import com.verdantartifice.primalmagick.client.renderers.entity.ManaArrowRenderer;
import com.verdantartifice.primalmagick.client.renderers.entity.PrimaliteGolemRenderer;
import com.verdantartifice.primalmagick.client.renderers.entity.PrimaliteTridentRenderer;
import com.verdantartifice.primalmagick.client.renderers.entity.SinCrashRenderer;
import com.verdantartifice.primalmagick.client.renderers.entity.SinCrystalRenderer;
import com.verdantartifice.primalmagick.client.renderers.entity.SpellMineRenderer;
import com.verdantartifice.primalmagick.client.renderers.entity.SpellProjectileRenderer;
import com.verdantartifice.primalmagick.client.renderers.entity.TreefolkRenderer;
import com.verdantartifice.primalmagick.client.renderers.entity.model.FlyingCarpetModel;
import com.verdantartifice.primalmagick.client.renderers.entity.model.PixieModel;
import com.verdantartifice.primalmagick.client.renderers.entity.model.SpellMineModel;
import com.verdantartifice.primalmagick.client.renderers.entity.model.SpellProjectileModel;
import com.verdantartifice.primalmagick.client.renderers.models.ModelLayersPM;
import com.verdantartifice.primalmagick.client.renderers.tile.model.SpellcraftingAltarRingModel;
import com.verdantartifice.primalmagick.common.entities.EntityTypesPM;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Respond to client-only entity renderer registration events.
 * 
 * @author Daedalus4096
 */
@Mod.EventBusSubscriber(modid=PrimalMagick.MODID, value=Dist.CLIENT, bus=Mod.EventBusSubscriber.Bus.MOD)
public class EntityRendererEvents {
    @SubscribeEvent
    public static void onRegisterEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        // Register renderers for each entity type
        event.registerEntityRenderer(EntityTypesPM.SPELL_PROJECTILE.get(), SpellProjectileRenderer::new);
        event.registerEntityRenderer(EntityTypesPM.SPELL_MINE.get(), SpellMineRenderer::new);
        event.registerEntityRenderer(EntityTypesPM.SIN_CRASH.get(), SinCrashRenderer::new);
        event.registerEntityRenderer(EntityTypesPM.SIN_CRYSTAL.get(), SinCrystalRenderer::new);
        event.registerEntityRenderer(EntityTypesPM.APPLE.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(EntityTypesPM.IGNYX.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(EntityTypesPM.ALCHEMICAL_BOMB.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(EntityTypesPM.MANA_ARROW.get(), ManaArrowRenderer::new);
        event.registerEntityRenderer(EntityTypesPM.FISHING_HOOK.get(), FishingHookRenderer::new);
        event.registerEntityRenderer(EntityTypesPM.PRIMALITE_TRIDENT.get(), PrimaliteTridentRenderer::new);
        event.registerEntityRenderer(EntityTypesPM.HEXIUM_TRIDENT.get(), HexiumTridentRenderer::new);
        event.registerEntityRenderer(EntityTypesPM.HALLOWSTEEL_TRIDENT.get(), HallowsteelTridentRenderer::new);
        event.registerEntityRenderer(EntityTypesPM.FORBIDDEN_TRIDENT.get(), ForbiddenTridentRenderer::new);
        event.registerEntityRenderer(EntityTypesPM.FLYING_CARPET.get(), FlyingCarpetRenderer::new);
        event.registerEntityRenderer(EntityTypesPM.TREEFOLK.get(), TreefolkRenderer::new);
        event.registerEntityRenderer(EntityTypesPM.INNER_DEMON.get(), InnerDemonRenderer::new);
        event.registerEntityRenderer(EntityTypesPM.FRIENDLY_WITCH.get(), FriendlyWitchRenderer::new);
        event.registerEntityRenderer(EntityTypesPM.PRIMALITE_GOLEM.get(), PrimaliteGolemRenderer::new);
        event.registerEntityRenderer(EntityTypesPM.HEXIUM_GOLEM.get(), HexiumGolemRenderer::new);
        event.registerEntityRenderer(EntityTypesPM.HALLOWSTEEL_GOLEM.get(), HallowsteelGolemRenderer::new);
        event.registerEntityRenderer(EntityTypesPM.BASIC_EARTH_PIXIE.get(), BasicPixieRenderer::new);
        event.registerEntityRenderer(EntityTypesPM.GRAND_EARTH_PIXIE.get(), GrandPixieRenderer::new);
        event.registerEntityRenderer(EntityTypesPM.MAJESTIC_EARTH_PIXIE.get(), MajesticPixieRenderer::new);
        event.registerEntityRenderer(EntityTypesPM.BASIC_SEA_PIXIE.get(), BasicPixieRenderer::new);
        event.registerEntityRenderer(EntityTypesPM.GRAND_SEA_PIXIE.get(), GrandPixieRenderer::new);
        event.registerEntityRenderer(EntityTypesPM.MAJESTIC_SEA_PIXIE.get(), MajesticPixieRenderer::new);
        event.registerEntityRenderer(EntityTypesPM.BASIC_SKY_PIXIE.get(), BasicPixieRenderer::new);
        event.registerEntityRenderer(EntityTypesPM.GRAND_SKY_PIXIE.get(), GrandPixieRenderer::new);
        event.registerEntityRenderer(EntityTypesPM.MAJESTIC_SKY_PIXIE.get(), MajesticPixieRenderer::new);
        event.registerEntityRenderer(EntityTypesPM.BASIC_SUN_PIXIE.get(), BasicPixieRenderer::new);
        event.registerEntityRenderer(EntityTypesPM.GRAND_SUN_PIXIE.get(), GrandPixieRenderer::new);
        event.registerEntityRenderer(EntityTypesPM.MAJESTIC_SUN_PIXIE.get(), MajesticPixieRenderer::new);
        event.registerEntityRenderer(EntityTypesPM.BASIC_MOON_PIXIE.get(), BasicPixieRenderer::new);
        event.registerEntityRenderer(EntityTypesPM.GRAND_MOON_PIXIE.get(), GrandPixieRenderer::new);
        event.registerEntityRenderer(EntityTypesPM.MAJESTIC_MOON_PIXIE.get(), MajesticPixieRenderer::new);
        event.registerEntityRenderer(EntityTypesPM.BASIC_BLOOD_PIXIE.get(), BasicPixieRenderer::new);
        event.registerEntityRenderer(EntityTypesPM.GRAND_BLOOD_PIXIE.get(), GrandPixieRenderer::new);
        event.registerEntityRenderer(EntityTypesPM.MAJESTIC_BLOOD_PIXIE.get(), MajesticPixieRenderer::new);
        event.registerEntityRenderer(EntityTypesPM.BASIC_INFERNAL_PIXIE.get(), BasicPixieRenderer::new);
        event.registerEntityRenderer(EntityTypesPM.GRAND_INFERNAL_PIXIE.get(), GrandPixieRenderer::new);
        event.registerEntityRenderer(EntityTypesPM.MAJESTIC_INFERNAL_PIXIE.get(), MajesticPixieRenderer::new);
        event.registerEntityRenderer(EntityTypesPM.BASIC_VOID_PIXIE.get(), BasicPixieRenderer::new);
        event.registerEntityRenderer(EntityTypesPM.GRAND_VOID_PIXIE.get(), GrandPixieRenderer::new);
        event.registerEntityRenderer(EntityTypesPM.MAJESTIC_VOID_PIXIE.get(), MajesticPixieRenderer::new);
        event.registerEntityRenderer(EntityTypesPM.BASIC_HALLOWED_PIXIE.get(), BasicPixieRenderer::new);
        event.registerEntityRenderer(EntityTypesPM.GRAND_HALLOWED_PIXIE.get(), GrandPixieRenderer::new);
        event.registerEntityRenderer(EntityTypesPM.MAJESTIC_HALLOWED_PIXIE.get(), MajesticPixieRenderer::new);
    }
    
    @SubscribeEvent
    public static void onRegisterLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        // Register layer definitions for models
        event.registerLayerDefinition(ModelLayersPM.FLYING_CARPET, FlyingCarpetModel::createBodyLayer);
        event.registerLayerDefinition(ModelLayersPM.PIXIE_BASIC, () -> PixieModel.createBodyLayer(false));
        event.registerLayerDefinition(ModelLayersPM.PIXIE_ROYAL, () -> PixieModel.createBodyLayer(true));
        event.registerLayerDefinition(ModelLayersPM.SPELL_MINE, SpellMineModel::createBodyLayer);
        event.registerLayerDefinition(ModelLayersPM.SPELL_PROJECTILE, SpellProjectileModel::createBodyLayer);
        event.registerLayerDefinition(ModelLayersPM.TREEFOLK, () -> LayerDefinition.create(HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F), 64, 64));
        event.registerLayerDefinition(ModelLayersPM.SPELLCRAFTING_ALTAR_RING, SpellcraftingAltarRingModel::createBodyLayer);
    }
}
