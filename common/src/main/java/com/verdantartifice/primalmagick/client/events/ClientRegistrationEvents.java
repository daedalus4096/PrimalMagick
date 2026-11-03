package com.verdantartifice.primalmagick.client.events;

import com.mojang.serialization.MapCodec;
import com.verdantartifice.primalmagick.client.books.LexiconLoader;
import com.verdantartifice.primalmagick.client.books.StyleGuideLoader;
import com.verdantartifice.primalmagick.client.fx.particles.NoteEmitterParticle;
import com.verdantartifice.primalmagick.client.fx.particles.ParticleTypesPM;
import com.verdantartifice.primalmagick.client.fx.particles.PotionExplosionParticle;
import com.verdantartifice.primalmagick.client.fx.particles.SpellBoltParticle;
import com.verdantartifice.primalmagick.client.fx.particles.SpellBoltParticleGroup;
import com.verdantartifice.primalmagick.client.renderers.itemstack.ArcanometerSpecialRenderer;
import com.verdantartifice.primalmagick.client.renderers.itemstack.ForbiddenTridentSpecialRenderer;
import com.verdantartifice.primalmagick.client.renderers.itemstack.HallowsteelShieldSpecialRenderer;
import com.verdantartifice.primalmagick.client.renderers.itemstack.HallowsteelTridentSpecialRenderer;
import com.verdantartifice.primalmagick.client.renderers.itemstack.HexiumShieldSpecialRenderer;
import com.verdantartifice.primalmagick.client.renderers.itemstack.HexiumTridentSpecialRenderer;
import com.verdantartifice.primalmagick.client.renderers.itemstack.ManaFontSpecialRenderer;
import com.verdantartifice.primalmagick.client.renderers.itemstack.ManaInjectorSpecialRenderer;
import com.verdantartifice.primalmagick.client.renderers.itemstack.ManaOrbSpecialRenderer;
import com.verdantartifice.primalmagick.client.renderers.itemstack.ManaRelaySpecialRenderer;
import com.verdantartifice.primalmagick.client.renderers.itemstack.ModularWandSpecialRenderer;
import com.verdantartifice.primalmagick.client.renderers.itemstack.MundaneWandSpecialRenderer;
import com.verdantartifice.primalmagick.client.renderers.itemstack.PixieHouseSpecialRenderer;
import com.verdantartifice.primalmagick.client.renderers.itemstack.PrimaliteShieldSpecialRenderer;
import com.verdantartifice.primalmagick.client.renderers.itemstack.PrimaliteTridentSpecialRenderer;
import com.verdantartifice.primalmagick.client.renderers.itemstack.ScanStateItemProperty;
import com.verdantartifice.primalmagick.client.renderers.itemstack.SpellcraftingAltarSpecialRenderer;
import com.verdantartifice.primalmagick.client.renderers.itemstack.SpelltomeSpecialRenderer;
import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperty;
import com.verdantartifice.primalmagick.client.tooltips.ClientAffinityTooltipComponent;
import com.verdantartifice.primalmagick.common.affinities.AffinityTooltipComponent;
import com.verdantartifice.primalmagick.common.crafting.recipe_book.RecipeBookCategoriesPM;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleGroup;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
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
    
    public static void onClientReloadListenerRegister(BiConsumer<Identifier, PreparableReloadListener> reloadListenerConsumer) {
        reloadListenerConsumer.accept(ResourceUtils.loc("lexicon"), LexiconLoader.getOrCreateInstance());
        reloadListenerConsumer.accept(ResourceUtils.loc("styleguide"), StyleGuideLoader.getOrCreateInstance());
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
        consumer.accept(ResourceUtils.loc("mana_relay"), ManaRelaySpecialRenderer.Unbaked.MAP_CODEC);
        consumer.accept(ResourceUtils.loc("mana_font"), ManaFontSpecialRenderer.Unbaked.MAP_CODEC);
        consumer.accept(ResourceUtils.loc("mana_orb"), ManaOrbSpecialRenderer.Unbaked.MAP_CODEC);
        consumer.accept(ResourceUtils.loc("spellcrafting_altar"), SpellcraftingAltarSpecialRenderer.Unbaked.MAP_CODEC);
        consumer.accept(ResourceUtils.loc("mundane_wand"), MundaneWandSpecialRenderer.Unbaked.MAP_CODEC);
        consumer.accept(ResourceUtils.loc("modular_wand"), ModularWandSpecialRenderer.Unbaked.MAP_CODEC);
        consumer.accept(ResourceUtils.loc("arcanometer"), ArcanometerSpecialRenderer.Unbaked.MAP_CODEC);
    }

    public static void onRegisterRangeSelectItemModelProperties(BiConsumer<Identifier, MapCodec<? extends RangeSelectItemModelProperty>> consumer) {
        consumer.accept(ScanStateItemProperty.ID, ScanStateItemProperty.MAP_CODEC);
    }
}
