package com.verdantartifice.primalmagick.client.events;

import com.verdantartifice.primalmagick.client.renderers.entity.BasicPixieRenderer;
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
import com.verdantartifice.primalmagick.client.renderers.tile.model.ManaCubeModel;
import com.verdantartifice.primalmagick.client.renderers.tile.model.ManaRelayFrameModel;
import com.verdantartifice.primalmagick.client.renderers.tile.model.SpellcraftingAltarRingModel;
import com.verdantartifice.primalmagick.common.entities.EntityTypesPM;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**
 * Respond to client-only entity renderer registration events.
 * 
 * @author Daedalus4096
 */
public class EntityRendererEvents {
    public static void onRegisterEntityRenderers(EntityRendererRegistrar entityRenderers) {
        // Register renderers for each entity type
        entityRenderers.register(EntityTypesPM.SPELL_PROJECTILE.get(), SpellProjectileRenderer::new);
        entityRenderers.register(EntityTypesPM.SPELL_MINE.get(), SpellMineRenderer::new);
        entityRenderers.register(EntityTypesPM.SIN_CRASH.get(), SinCrashRenderer::new);
        entityRenderers.register(EntityTypesPM.SIN_CRYSTAL.get(), SinCrystalRenderer::new);
        entityRenderers.register(EntityTypesPM.APPLE.get(), ThrownItemRenderer::new);
        entityRenderers.register(EntityTypesPM.IGNYX.get(), ThrownItemRenderer::new);
        entityRenderers.register(EntityTypesPM.ALCHEMICAL_BOMB.get(), ThrownItemRenderer::new);
        entityRenderers.register(EntityTypesPM.MANA_ARROW.get(), ManaArrowRenderer::new);
        entityRenderers.register(EntityTypesPM.PRIMALITE_TRIDENT.get(), PrimaliteTridentRenderer::new);
        entityRenderers.register(EntityTypesPM.HEXIUM_TRIDENT.get(), HexiumTridentRenderer::new);
        entityRenderers.register(EntityTypesPM.HALLOWSTEEL_TRIDENT.get(), HallowsteelTridentRenderer::new);
        entityRenderers.register(EntityTypesPM.FORBIDDEN_TRIDENT.get(), ForbiddenTridentRenderer::new);
        entityRenderers.register(EntityTypesPM.FLYING_CARPET.get(), FlyingCarpetRenderer::new);
        entityRenderers.register(EntityTypesPM.TREEFOLK.get(), TreefolkRenderer::new);
        entityRenderers.register(EntityTypesPM.INNER_DEMON.get(), InnerDemonRenderer::new);
        entityRenderers.register(EntityTypesPM.FRIENDLY_WITCH.get(), FriendlyWitchRenderer::new);
        entityRenderers.register(EntityTypesPM.PRIMALITE_GOLEM.get(), PrimaliteGolemRenderer::new);
        entityRenderers.register(EntityTypesPM.HEXIUM_GOLEM.get(), HexiumGolemRenderer::new);
        entityRenderers.register(EntityTypesPM.HALLOWSTEEL_GOLEM.get(), HallowsteelGolemRenderer::new);
        entityRenderers.register(EntityTypesPM.BASIC_EARTH_PIXIE.get(), BasicPixieRenderer::new);
        entityRenderers.register(EntityTypesPM.GRAND_EARTH_PIXIE.get(), GrandPixieRenderer::new);
        entityRenderers.register(EntityTypesPM.MAJESTIC_EARTH_PIXIE.get(), MajesticPixieRenderer::new);
        entityRenderers.register(EntityTypesPM.BASIC_SEA_PIXIE.get(), BasicPixieRenderer::new);
        entityRenderers.register(EntityTypesPM.GRAND_SEA_PIXIE.get(), GrandPixieRenderer::new);
        entityRenderers.register(EntityTypesPM.MAJESTIC_SEA_PIXIE.get(), MajesticPixieRenderer::new);
        entityRenderers.register(EntityTypesPM.BASIC_SKY_PIXIE.get(), BasicPixieRenderer::new);
        entityRenderers.register(EntityTypesPM.GRAND_SKY_PIXIE.get(), GrandPixieRenderer::new);
        entityRenderers.register(EntityTypesPM.MAJESTIC_SKY_PIXIE.get(), MajesticPixieRenderer::new);
        entityRenderers.register(EntityTypesPM.BASIC_SUN_PIXIE.get(), BasicPixieRenderer::new);
        entityRenderers.register(EntityTypesPM.GRAND_SUN_PIXIE.get(), GrandPixieRenderer::new);
        entityRenderers.register(EntityTypesPM.MAJESTIC_SUN_PIXIE.get(), MajesticPixieRenderer::new);
        entityRenderers.register(EntityTypesPM.BASIC_MOON_PIXIE.get(), BasicPixieRenderer::new);
        entityRenderers.register(EntityTypesPM.GRAND_MOON_PIXIE.get(), GrandPixieRenderer::new);
        entityRenderers.register(EntityTypesPM.MAJESTIC_MOON_PIXIE.get(), MajesticPixieRenderer::new);
        entityRenderers.register(EntityTypesPM.BASIC_BLOOD_PIXIE.get(), BasicPixieRenderer::new);
        entityRenderers.register(EntityTypesPM.GRAND_BLOOD_PIXIE.get(), GrandPixieRenderer::new);
        entityRenderers.register(EntityTypesPM.MAJESTIC_BLOOD_PIXIE.get(), MajesticPixieRenderer::new);
        entityRenderers.register(EntityTypesPM.BASIC_INFERNAL_PIXIE.get(), BasicPixieRenderer::new);
        entityRenderers.register(EntityTypesPM.GRAND_INFERNAL_PIXIE.get(), GrandPixieRenderer::new);
        entityRenderers.register(EntityTypesPM.MAJESTIC_INFERNAL_PIXIE.get(), MajesticPixieRenderer::new);
        entityRenderers.register(EntityTypesPM.BASIC_VOID_PIXIE.get(), BasicPixieRenderer::new);
        entityRenderers.register(EntityTypesPM.GRAND_VOID_PIXIE.get(), GrandPixieRenderer::new);
        entityRenderers.register(EntityTypesPM.MAJESTIC_VOID_PIXIE.get(), MajesticPixieRenderer::new);
        entityRenderers.register(EntityTypesPM.BASIC_HALLOWED_PIXIE.get(), BasicPixieRenderer::new);
        entityRenderers.register(EntityTypesPM.GRAND_HALLOWED_PIXIE.get(), GrandPixieRenderer::new);
        entityRenderers.register(EntityTypesPM.MAJESTIC_HALLOWED_PIXIE.get(), MajesticPixieRenderer::new);
    }

    public interface EntityRendererRegistrar {
        <T extends Entity> void register(EntityType<? extends T> entityType, EntityRendererProvider<T> entityRendererProvider);
    }

    public static void onRegisterLayerDefinitions(BiConsumer<ModelLayerLocation, Supplier<LayerDefinition>> consumer) {
        // Register layer definitions for models
        consumer.accept(ModelLayersPM.FLYING_CARPET, FlyingCarpetModel::createBodyLayer);
        consumer.accept(ModelLayersPM.PIXIE_BASIC, () -> PixieModel.createBodyLayer(false));
        consumer.accept(ModelLayersPM.PIXIE_ROYAL, () -> PixieModel.createBodyLayer(true));
        consumer.accept(ModelLayersPM.SPELL_MINE, SpellMineModel::createBodyLayer);
        consumer.accept(ModelLayersPM.SPELL_PROJECTILE, SpellProjectileModel::createBodyLayer);
        consumer.accept(ModelLayersPM.TREEFOLK, () -> LayerDefinition.create(HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F), 64, 64));
        consumer.accept(ModelLayersPM.SPELLCRAFTING_ALTAR_RING, SpellcraftingAltarRingModel::createBodyLayer);
        consumer.accept(ModelLayersPM.MANA_CUBE, ManaCubeModel::createBodyLayer);
        consumer.accept(ModelLayersPM.MANA_RELAY_FRAME, ManaRelayFrameModel::createBodyLayer);
    }
}
