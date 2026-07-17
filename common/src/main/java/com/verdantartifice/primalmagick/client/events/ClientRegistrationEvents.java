package com.verdantartifice.primalmagick.client.events;

import com.mojang.serialization.MapCodec;
import com.verdantartifice.primalmagick.client.books.LexiconLoader;
import com.verdantartifice.primalmagick.client.books.StyleGuideLoader;
import com.verdantartifice.primalmagick.client.fx.particles.NoteEmitterParticle;
import com.verdantartifice.primalmagick.client.fx.particles.ParticleTypesPM;
import com.verdantartifice.primalmagick.client.fx.particles.PotionExplosionParticle;
import com.verdantartifice.primalmagick.client.fx.particles.SpellBoltParticle;
import com.verdantartifice.primalmagick.client.fx.particles.SpellBoltParticleGroup;
import com.verdantartifice.primalmagick.client.renderers.itemstack.ForbiddenTridentSpecialRenderer;
import com.verdantartifice.primalmagick.client.renderers.itemstack.HallowsteelShieldSpecialRenderer;
import com.verdantartifice.primalmagick.client.renderers.itemstack.HallowsteelTridentSpecialRenderer;
import com.verdantartifice.primalmagick.client.renderers.itemstack.HexiumShieldSpecialRenderer;
import com.verdantartifice.primalmagick.client.renderers.itemstack.HexiumTridentSpecialRenderer;
import com.verdantartifice.primalmagick.client.renderers.itemstack.ManaInjectorSpecialRenderer;
import com.verdantartifice.primalmagick.client.renderers.itemstack.PixieHouseSpecialRenderer;
import com.verdantartifice.primalmagick.client.renderers.itemstack.PrimaliteShieldSpecialRenderer;
import com.verdantartifice.primalmagick.client.renderers.itemstack.PrimaliteTridentSpecialRenderer;
import com.verdantartifice.primalmagick.client.renderers.itemstack.SpelltomeSpecialRenderer;
import com.verdantartifice.primalmagick.client.tooltips.ClientAffinityTooltipComponent;
import com.verdantartifice.primalmagick.common.affinities.AffinityTooltipComponent;
import com.verdantartifice.primalmagick.common.crafting.recipe_book.RecipeBookCategoriesPM;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import com.verdantartifice.primalmagick.common.wands.WandCap;
import com.verdantartifice.primalmagick.common.wands.WandCore;
import com.verdantartifice.primalmagick.common.wands.WandGem;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleGroup;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.crafting.ExtendedRecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeBookCategories;
import net.minecraft.world.item.crafting.RecipeBookCategory;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Respond to client-only Forge registration events.
 * 
 * @author Daedalus4096
 */
public class ClientRegistrationEvents {
    public static void onRegisterParticleProviders(SpecialParticleProviderRegistrar special) {
        // FIXME Sprite set particles are registered in platform-specific code due to access transformer weirdness
        special.register(ParticleTypesPM.POTION_EXPLOSION.get(), new PotionExplosionParticle.Provider());
        special.register(ParticleTypesPM.NOTE_EMITTER.get(), new NoteEmitterParticle.Provider());
    }

    public interface SpecialParticleProviderRegistrar {
        <T extends ParticleOptions> void register(ParticleType<T> type, ParticleProvider<T> provider);
    }

    public static void onRegisterParticleGroups(BiConsumer<ParticleRenderType, Function<ParticleEngine, ParticleGroup<?>>> groupConsumer) {
        groupConsumer.accept(SpellBoltParticle.RENDER_TYPE, SpellBoltParticleGroup::new);
    }

    public static void onRegisterRecipeBookSearchCategories(BiConsumer<ExtendedRecipeBookCategory, List<RecipeBookCategory>> consumer) {
        consumer.accept(RecipeBookCategoriesPM.SEARCH_ARCANE_CRAFTING.get(), List.of(
                RecipeBookCategoriesPM.CRAFTING_ARCANE.get(),
                RecipeBookCategories.CRAFTING_EQUIPMENT,
                RecipeBookCategories.CRAFTING_BUILDING_BLOCKS,
                RecipeBookCategories.CRAFTING_MISC,
                RecipeBookCategories.CRAFTING_REDSTONE
        ));
        consumer.accept(RecipeBookCategoriesPM.SEARCH_CONCOCTER.get(), List.of(
                RecipeBookCategoriesPM.CONCOCTER_DRINKABLE.get(),
                RecipeBookCategoriesPM.CONCOCTER_BOMB.get()
        ));
        consumer.accept(RecipeBookCategoriesPM.SEARCH_DISSOLUTION.get(), List.of(
                RecipeBookCategoriesPM.DISSOLUTION_ORES.get(),
                RecipeBookCategoriesPM.DISSOLUTION_MISC.get()
        ));
    }
    
    /**
     * Register special model resource locations that must be loaded even if not tied to a block state.
     */
    public static void onModelRegister(Consumer<ModelResourceLocation> modelConsumer) {
        modelConsumer.accept(Services.MODEL_RESOURCE_LOCATIONS.createStandalone(ResourceUtils.loc("mundane_wand_core")));
        for (WandCore core : WandCore.getAllWandCores()) {
            modelConsumer.accept(Services.MODEL_RESOURCE_LOCATIONS.createStandalone(core.getWandModelResourceLocationNamespace()));
            modelConsumer.accept(Services.MODEL_RESOURCE_LOCATIONS.createStandalone(core.getStaffModelResourceLocationNamespace()));
        }
        for (WandCap cap : WandCap.getAllWandCaps()) {
            modelConsumer.accept(Services.MODEL_RESOURCE_LOCATIONS.createStandalone(cap.getWandModelResourceLocationNamespace()));
            modelConsumer.accept(Services.MODEL_RESOURCE_LOCATIONS.createStandalone(cap.getStaffModelResourceLocationNamespace()));
        }
        for (WandGem gem : WandGem.getAllWandGems()) {
            modelConsumer.accept(Services.MODEL_RESOURCE_LOCATIONS.createStandalone(gem.getModelResourceLocationNamespace()));
        }
        for (int index = 0; index <= 4; index++) {
            modelConsumer.accept(Services.MODEL_RESOURCE_LOCATIONS.createStandalone(ResourceUtils.loc("arcanometer_" + index)));
        }
    }
    
    public static void onClientReloadListenerRegister(Consumer<PreparableReloadListener> reloadListenerConsumer) {
        reloadListenerConsumer.accept(ItemsPM.MANA_ORB_APPRENTICE.get().getCustomRendererSupplier().get());
        reloadListenerConsumer.accept(ItemsPM.MANA_ORB_ADEPT.get().getCustomRendererSupplier().get());
        reloadListenerConsumer.accept(ItemsPM.MANA_ORB_WIZARD.get().getCustomRendererSupplier().get());
        reloadListenerConsumer.accept(ItemsPM.MANA_ORB_ARCHMAGE.get().getCustomRendererSupplier().get());
        reloadListenerConsumer.accept(ItemsPM.SPELLCRAFTING_ALTAR.get().getCustomRendererSupplier().get());
        reloadListenerConsumer.accept(ItemsPM.MANA_RELAY_BASIC.get().getCustomRendererSupplier().get());
        reloadListenerConsumer.accept(ItemsPM.MANA_RELAY_ENCHANTED.get().getCustomRendererSupplier().get());
        reloadListenerConsumer.accept(ItemsPM.MANA_RELAY_FORBIDDEN.get().getCustomRendererSupplier().get());
        reloadListenerConsumer.accept(ItemsPM.MANA_RELAY_HEAVENLY.get().getCustomRendererSupplier().get());
        reloadListenerConsumer.accept(LexiconLoader.getOrCreateInstance());
        reloadListenerConsumer.accept(StyleGuideLoader.getOrCreateInstance());
    }
    
    public static void onRegisterClientTooltipComponentFactories(TooltipComponentRegistrar clientTooltipComponentRegistrar) {
        clientTooltipComponentRegistrar.register(AffinityTooltipComponent.class, ClientAffinityTooltipComponent::new);
    }

    public interface TooltipComponentRegistrar {
        <T extends TooltipComponent> void register(Class<T> type, Function<? super T, ? extends ClientTooltipComponent> factory);
    }

    public static void onRegisterSpecialModelRenderer(BiConsumer<Identifier, MapCodec<? extends SpecialModelRenderer.Unbaked<?>>> consumer) {
        consumer.accept(ResourceUtils.loc("primalite_shield"), PrimaliteShieldSpecialRenderer.Unbaked.MAP_CODEC);
        consumer.accept(ResourceUtils.loc("hexium_shield"), HexiumShieldSpecialRenderer.Unbaked.MAP_CODEC);
        consumer.accept(ResourceUtils.loc("hallowsteel_shield"), HallowsteelShieldSpecialRenderer.Unbaked.MAP_CODEC);
        consumer.accept(ResourceUtils.loc("primalite_trident"), PrimaliteTridentSpecialRenderer.Unbaked.MAP_CODEC);
        consumer.accept(ResourceUtils.loc("hexium_trident"), HexiumTridentSpecialRenderer.Unbaked.MAP_CODEC);
        consumer.accept(ResourceUtils.loc("hallowsteel_trident"), HallowsteelTridentSpecialRenderer.Unbaked.MAP_CODEC);
        consumer.accept(ResourceUtils.loc("forbidden_trident"), ForbiddenTridentSpecialRenderer.Unbaked.MAP_CODEC);
        consumer.accept(ResourceUtils.loc("spelltome"), SpelltomeSpecialRenderer.Unbaked.MAP_CODEC);
        consumer.accept(ResourceUtils.loc("pixie_house"), PixieHouseSpecialRenderer.Unbaked.MAP_CODEC);
        consumer.accept(ResourceUtils.loc("mana_injector"), ManaInjectorSpecialRenderer.Unbaked.MAP_CODEC);
    }
}
