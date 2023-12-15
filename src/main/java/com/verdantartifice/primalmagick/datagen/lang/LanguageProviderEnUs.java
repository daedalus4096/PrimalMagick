package com.verdantartifice.primalmagick.datagen.lang;

import java.util.List;

import com.verdantartifice.primalmagick.client.config.KeyBindings;
import com.verdantartifice.primalmagick.common.armortrim.TrimMaterialsPM;
import com.verdantartifice.primalmagick.common.armortrim.TrimPatternsPM;
import com.verdantartifice.primalmagick.common.attunements.AttunementThreshold;
import com.verdantartifice.primalmagick.common.attunements.AttunementType;
import com.verdantartifice.primalmagick.common.blocks.BlocksPM;
import com.verdantartifice.primalmagick.common.books.BookLanguagesPM;
import com.verdantartifice.primalmagick.common.books.BooksPM;
import com.verdantartifice.primalmagick.common.concoctions.ConcoctionType;
import com.verdantartifice.primalmagick.common.damagesource.DamageTypesPM;
import com.verdantartifice.primalmagick.common.effects.EffectsPM;
import com.verdantartifice.primalmagick.common.enchantments.EnchantmentsPM;
import com.verdantartifice.primalmagick.common.entities.EntityTypesPM;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.research.KnowledgeType;
import com.verdantartifice.primalmagick.common.research.ResearchDisciplines;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.spells.mods.AmplifySpellMod;
import com.verdantartifice.primalmagick.common.spells.mods.BurstSpellMod;
import com.verdantartifice.primalmagick.common.spells.mods.EmptySpellMod;
import com.verdantartifice.primalmagick.common.spells.mods.ForkSpellMod;
import com.verdantartifice.primalmagick.common.spells.mods.MineSpellMod;
import com.verdantartifice.primalmagick.common.spells.mods.QuickenSpellMod;
import com.verdantartifice.primalmagick.common.spells.payloads.BloodDamageSpellPayload;
import com.verdantartifice.primalmagick.common.spells.payloads.BreakSpellPayload;
import com.verdantartifice.primalmagick.common.spells.payloads.ConjureAnimalSpellPayload;
import com.verdantartifice.primalmagick.common.spells.payloads.ConjureLavaSpellPayload;
import com.verdantartifice.primalmagick.common.spells.payloads.ConjureLightSpellPayload;
import com.verdantartifice.primalmagick.common.spells.payloads.ConjureStoneSpellPayload;
import com.verdantartifice.primalmagick.common.spells.payloads.ConjureWaterSpellPayload;
import com.verdantartifice.primalmagick.common.spells.payloads.ConsecrateSpellPayload;
import com.verdantartifice.primalmagick.common.spells.payloads.DrainSoulSpellPayload;
import com.verdantartifice.primalmagick.common.spells.payloads.EarthDamageSpellPayload;
import com.verdantartifice.primalmagick.common.spells.payloads.EmptySpellPayload;
import com.verdantartifice.primalmagick.common.spells.payloads.FlameDamageSpellPayload;
import com.verdantartifice.primalmagick.common.spells.payloads.FlightSpellPayload;
import com.verdantartifice.primalmagick.common.spells.payloads.FrostDamageSpellPayload;
import com.verdantartifice.primalmagick.common.spells.payloads.HealingSpellPayload;
import com.verdantartifice.primalmagick.common.spells.payloads.HolyDamageSpellPayload;
import com.verdantartifice.primalmagick.common.spells.payloads.LightningDamageSpellPayload;
import com.verdantartifice.primalmagick.common.spells.payloads.LunarDamageSpellPayload;
import com.verdantartifice.primalmagick.common.spells.payloads.PolymorphSheepSpellPayload;
import com.verdantartifice.primalmagick.common.spells.payloads.PolymorphWolfSpellPayload;
import com.verdantartifice.primalmagick.common.spells.payloads.ShearSpellPayload;
import com.verdantartifice.primalmagick.common.spells.payloads.SolarDamageSpellPayload;
import com.verdantartifice.primalmagick.common.spells.payloads.TeleportSpellPayload;
import com.verdantartifice.primalmagick.common.spells.payloads.VoidDamageSpellPayload;
import com.verdantartifice.primalmagick.common.spells.vehicles.BoltSpellVehicle;
import com.verdantartifice.primalmagick.common.spells.vehicles.EmptySpellVehicle;
import com.verdantartifice.primalmagick.common.spells.vehicles.ProjectileSpellVehicle;
import com.verdantartifice.primalmagick.common.spells.vehicles.SelfSpellVehicle;
import com.verdantartifice.primalmagick.common.spells.vehicles.TouchSpellVehicle;
import com.verdantartifice.primalmagick.common.stats.StatsPM;
import com.verdantartifice.primalmagick.common.wands.WandCap;
import com.verdantartifice.primalmagick.common.wands.WandCore;
import com.verdantartifice.primalmagick.common.wands.WandGem;

import net.minecraft.data.PackOutput;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.registries.RegistryObject;

/**
 * Language data provider for US English.
 * 
 * @author Daedalus4096
 */
public class LanguageProviderEnUs extends AbstractLanguageProviderPM {
    public LanguageProviderEnUs(PackOutput output) {
        super(output, "en_us");
    }

    @Override
    protected void addLocalizations() {
        // Generate magickal source localizations; it's important to do these first so that the language provider has access to
        // the localized source names for the source-multiplied language builders used later.
        this.source(Source.EARTH).name("Earth").attunement("Minor attunement to the Earth source makes it easier to channel mana.  In practice, I'll pay 5% less Earth mana for all purposes.<BR>Lesser attunement to the Earth will grant me increased stamina, allowing me to swing swords and tools faster without tiring.<BR>Finally, greater attunement to the Earth will cause the very ground to rise beneath my feet when I walk, allowing me to step up the full height of a block without needing to jump.").build();
        this.source(Source.SEA).name("Sea").attunement("Minor attunement to the Sea source makes it easier to channel mana.  In practice, I'll pay 5% less Sea mana for all purposes.<BR>Lesser attunement to the Sea will grant me the grace of a dolphin, greatly increasing my swim speed.<BR>Finally, greater attunement to the Sea will allow me to breathe water as if it were air.").build();
        this.source(Source.SKY).name("Sky").attunement("Minor attunement to the Sky source makes it easier to channel mana.  In practice, I'll pay 5% less Sky mana for all purposes.<BR>Lesser attunement to the Sky will infuse me with speed and dexterity, increasing my movement speed and greatly reducing any fall damage I take.<BR>Finally, greater attunement to the Sky truly allows me to soar.  While it won't truly let me fly, I'll be able to jump higher, and even jump a second time while in midair.").build();
        this.source(Source.SUN).name("Sun").attunement("Minor attunement to the Sun source makes it easier to channel mana.  In practice, I'll pay 5% less Sun mana for all purposes.<BR>Lesser attunement to the Sun will allow me to draw nourishment from it like a plant, filling my belly so long as I'm outdoors during the daytime.<BR>Finally, greater attunement to the Sun grants me true radiance.  Whenever I'm in a dark area, I'll have a chance to create lasting light in my wake.  Should I not desire this to happen, I need only sneak.").build();
        this.source(Source.MOON).name("Moon").attunement("Minor attunement to the Moon source makes it easier to channel mana.  In practice, I'll pay 5% less Moon mana for all purposes.<BR>Lesser attunement to the Moon will let me defensively wrap myself in shifting illusions, giving me a chance to become invisible whenever I take damage.<BR>Finally, greater attunement to the Moon allows me to become one with the night, seeing through the darkness as if it were day.").build();
        this.source(Source.BLOOD).name("Blood").attunement("Minor attunement to the Blood source makes it easier to channel mana.  In practice, I'll pay 5% less Blood mana for all purposes.<BR>Lesser attunement to the Blood will let me exsanguinate my foes, causing them to bleed for more damage whenever I score a solid hit on one.  All except for the loathsome undead, that is, who have no blood to shed.<BR>Finally, greater attunement to the Blood will cause me to siphon my enemies' life force, granting me a chance to heal my wounds as I harm them.").build();
        this.source(Source.INFERNAL).name("Infernal").attunement("Minor attunement to the Infernal source makes it easier to channel mana.  In practice, I'll pay 5% less Infernal mana for all purposes.<BR>Lesser attunement to the Infernal will let me lash out with a Hellish Chain when I strike, dealing bonus fire damage to the next-closest victim to my original target.<BR>Finally, greater attunement to the Infernal will let me become one with the flames, and immune to their ravages.").build();
        this.source(Source.VOID).name("Void").attunement("Minor attunement to the Void source makes it easier to channel mana.  In practice, I'll pay 5% less Void mana for all purposes.<BR>Lesser attunement to the Void will infuse my flesh with nothingness, reducing all damage I take.<BR>Finally, greater attunement to the Void will channel the hunger of the eldritch through my attacks, granting them all increased potency.").build();
        this.source(Source.HALLOWED).name("Hallowed").attunement("Minor attunement to the Hallowed source makes it easier to channel mana.  In practice, I'll pay 5% less Hallowed mana for all purposes.<BR>Lesser attunement to the Hallowed will make me anathema to the undead, doubling all damage I deal to them.<BR>Finally, greater attunement to the Hallowed will empower my soul such that, when fatally wounded, I will not die but live on.  Such an effort will weaken my soul for a short time, preventing such a miracle from happening again until I recover.").build();
        this.add(Source.getUnknownTranslationKey(), "Unknown");
        
        // Generate item group (i.e. creative tab) localizations
        this.add("itemGroup.primalmagick", "Primal Magick");
        
        // Generate block localizations
        this.block(BlocksPM.MARBLE_RAW).name("Marble").build();
        this.block(BlocksPM.MARBLE_SLAB).name("Marble Slab").build();
        this.block(BlocksPM.MARBLE_STAIRS).name("Marble Stairs").build();
        this.block(BlocksPM.MARBLE_WALL).name("Marble Wall").build();
        this.block(BlocksPM.MARBLE_BRICKS).name("Marble Bricks").build();
        this.block(BlocksPM.MARBLE_BRICK_SLAB).name("Marble Brick Slab").build();
        this.block(BlocksPM.MARBLE_BRICK_STAIRS).name("Marble Brick Stairs").build();
        this.block(BlocksPM.MARBLE_BRICK_WALL).name("Marble Brick Wall").build();
        this.block(BlocksPM.MARBLE_PILLAR).name("Marble Pillar").build();
        this.block(BlocksPM.MARBLE_CHISELED).name("Chiseled Marble").build();
        this.block(BlocksPM.MARBLE_RUNED).name("Runed Marble").build();
        this.block(BlocksPM.MARBLE_TILES).name("Marble Tiles").build();
        this.block(BlocksPM.MARBLE_ENCHANTED).name("Enchanted Marble").build();
        this.block(BlocksPM.MARBLE_ENCHANTED_SLAB).name("Enchanted Marble Slab").build();
        this.block(BlocksPM.MARBLE_ENCHANTED_STAIRS).name("Enchanted Marble Stairs").build();
        this.block(BlocksPM.MARBLE_ENCHANTED_WALL).name("Enchanted Marble Wall").build();
        this.block(BlocksPM.MARBLE_ENCHANTED_BRICKS).name("Enchanted Marble Bricks").build();
        this.block(BlocksPM.MARBLE_ENCHANTED_BRICK_SLAB).name("Enchanted Marble Brick Slab").build();
        this.block(BlocksPM.MARBLE_ENCHANTED_BRICK_STAIRS).name("Enchanted Marble Brick Stairs").build();
        this.block(BlocksPM.MARBLE_ENCHANTED_BRICK_WALL).name("Enchanted Marble Brick Wall").build();
        this.block(BlocksPM.MARBLE_ENCHANTED_PILLAR).name("Enchanted Marble Pillar").build();
        this.block(BlocksPM.MARBLE_ENCHANTED_CHISELED).name("Enchanted Chiseled Marble").build();
        this.block(BlocksPM.MARBLE_ENCHANTED_RUNED).name("Enchanted Runed Marble").build();
        this.block(BlocksPM.MARBLE_SMOKED).name("Smoked Marble").build();
        this.block(BlocksPM.MARBLE_SMOKED_SLAB).name("Smoked Marble Slab").build();
        this.block(BlocksPM.MARBLE_SMOKED_STAIRS).name("Smoked Marble Stairs").build();
        this.block(BlocksPM.MARBLE_SMOKED_WALL).name("Smoked Marble Wall").build();
        this.block(BlocksPM.MARBLE_SMOKED_BRICKS).name("Smoked Marble Bricks").build();
        this.block(BlocksPM.MARBLE_SMOKED_BRICK_SLAB).name("Smoked Marble Brick Slab").build();
        this.block(BlocksPM.MARBLE_SMOKED_BRICK_STAIRS).name("Smoked Marble Brick Stairs").build();
        this.block(BlocksPM.MARBLE_SMOKED_BRICK_WALL).name("Smoked Marble Brick Wall").build();
        this.block(BlocksPM.MARBLE_SMOKED_PILLAR).name("Smoked Marble Pillar").build();
        this.block(BlocksPM.MARBLE_SMOKED_CHISELED).name("Smoked Chiseled Marble").build();
        this.block(BlocksPM.MARBLE_SMOKED_RUNED).name("Smoked Runed Marble").build();
        this.block(BlocksPM.MARBLE_HALLOWED).name("Hallowed Marble").build();
        this.block(BlocksPM.MARBLE_HALLOWED_SLAB).name("Hallowed Marble Slab").build();
        this.block(BlocksPM.MARBLE_HALLOWED_STAIRS).name("Hallowed Marble Stairs").build();
        this.block(BlocksPM.MARBLE_HALLOWED_WALL).name("Hallowed Marble Wall").build();
        this.block(BlocksPM.MARBLE_HALLOWED_BRICKS).name("Hallowed Marble Bricks").build();
        this.block(BlocksPM.MARBLE_HALLOWED_BRICK_SLAB).name("Hallowed Marble Brick Slab").build();
        this.block(BlocksPM.MARBLE_HALLOWED_BRICK_STAIRS).name("Hallowed Marble Brick Stairs").build();
        this.block(BlocksPM.MARBLE_HALLOWED_BRICK_WALL).name("Hallowed Marble Brick Wall").build();
        this.block(BlocksPM.MARBLE_HALLOWED_PILLAR).name("Hallowed Marble Pillar").build();
        this.block(BlocksPM.MARBLE_HALLOWED_CHISELED).name("Hallowed Chiseled Marble").build();
        this.block(BlocksPM.MARBLE_HALLOWED_RUNED).name("Hallowed Runed Marble").build();
        this.block(BlocksPM.SUNWOOD_LOG).name("Sunwood Log").build();
        this.block(BlocksPM.STRIPPED_SUNWOOD_LOG).name("Stripped Sunwood Log").build();
        this.block(BlocksPM.SUNWOOD_WOOD).name("Sunwood Wood").build();
        this.block(BlocksPM.STRIPPED_SUNWOOD_WOOD).name("Stripped Sunwood Wood").build();
        this.block(BlocksPM.SUNWOOD_LEAVES).name("Sunwood Leaves").build();
        this.block(BlocksPM.SUNWOOD_SAPLING).name("Sunwood Sapling").build();
        this.block(BlocksPM.SUNWOOD_PLANKS).name("Sunwood Planks").build();
        this.block(BlocksPM.SUNWOOD_SLAB).name("Sunwood Slab").build();
        this.block(BlocksPM.SUNWOOD_STAIRS).name("Sunwood Stairs").build();
        this.block(BlocksPM.SUNWOOD_PILLAR).name("Sunwood Pillar").build();
        this.block(BlocksPM.MOONWOOD_LOG).name("Moonwood Log").build();
        this.block(BlocksPM.STRIPPED_MOONWOOD_LOG).name("Stripped Moonwood Log").build();
        this.block(BlocksPM.MOONWOOD_WOOD).name("Moonwood Wood").build();
        this.block(BlocksPM.STRIPPED_MOONWOOD_WOOD).name("Stripped Moonwood Wood").build();
        this.block(BlocksPM.MOONWOOD_LEAVES).name("Moonwood Leaves").build();
        this.block(BlocksPM.MOONWOOD_SAPLING).name("Moonwood Sapling").build();
        this.block(BlocksPM.MOONWOOD_PLANKS).name("Moonwood Planks").build();
        this.block(BlocksPM.MOONWOOD_SLAB).name("Moonwood Slab").build();
        this.block(BlocksPM.MOONWOOD_STAIRS).name("Moonwood Stairs").build();
        this.block(BlocksPM.MOONWOOD_PILLAR).name("Moonwood Pillar").build();
        this.block(BlocksPM.HALLOWOOD_LOG).name("Hallowood Log").build();
        this.block(BlocksPM.STRIPPED_HALLOWOOD_LOG).name("Stripped Hallowood Log").build();
        this.block(BlocksPM.HALLOWOOD_WOOD).name("Hallowood Wood").build();
        this.block(BlocksPM.STRIPPED_HALLOWOOD_WOOD).name("Stripped Hallowood Wood").build();
        this.block(BlocksPM.HALLOWOOD_LEAVES).name("Hallowood Leaves").build();
        this.block(BlocksPM.HALLOWOOD_SAPLING).name("Hallowood Sapling").build();
        this.block(BlocksPM.HALLOWOOD_PLANKS).name("Hallowood Planks").build();
        this.block(BlocksPM.HALLOWOOD_SLAB).name("Hallowood Slab").build();
        this.block(BlocksPM.HALLOWOOD_STAIRS).name("Hallowood Stairs").build();
        this.block(BlocksPM.HALLOWOOD_PILLAR).name("Hallowood Pillar").build();
        this.block(BlocksPM.ARCANE_WORKBENCH).name("Arcane Workbench").build();
        this.block(BlocksPM.ANCIENT_FONT_EARTH).name("Ancient Earth Font").build();
        this.block(BlocksPM.ANCIENT_FONT_SEA).name("Ancient Sea Font").build();
        this.block(BlocksPM.ANCIENT_FONT_SKY).name("Ancient Sky Font").build();
        this.block(BlocksPM.ANCIENT_FONT_SUN).name("Ancient Sun Font").build();
        this.block(BlocksPM.ANCIENT_FONT_MOON).name("Ancient Moon Font").build();
        this.block(BlocksPM.ARTIFICIAL_FONT_EARTH).name("Artificial Earth Font").build();
        this.block(BlocksPM.ARTIFICIAL_FONT_SEA).name("Artificial Sea Font").build();
        this.block(BlocksPM.ARTIFICIAL_FONT_SKY).name("Artificial Sky Font").build();
        this.block(BlocksPM.ARTIFICIAL_FONT_SUN).name("Artificial Sun Font").build();
        this.block(BlocksPM.ARTIFICIAL_FONT_MOON).name("Artificial Moon Font").build();
        this.block(BlocksPM.ARTIFICIAL_FONT_BLOOD).name("Artificial Blood Font").build();
        this.block(BlocksPM.ARTIFICIAL_FONT_INFERNAL).name("Artificial Infernal Font").build();
        this.block(BlocksPM.ARTIFICIAL_FONT_VOID).name("Artificial Void Font").build();
        this.block(BlocksPM.ARTIFICIAL_FONT_HALLOWED).name("Artificial Hallowed Font").build();
        this.block(BlocksPM.FORBIDDEN_FONT_EARTH).name("Forbidden Earth Font").build();
        this.block(BlocksPM.FORBIDDEN_FONT_SEA).name("Forbidden Sea Font").build();
        this.block(BlocksPM.FORBIDDEN_FONT_SKY).name("Forbidden Sky Font").build();
        this.block(BlocksPM.FORBIDDEN_FONT_SUN).name("Forbidden Sun Font").build();
        this.block(BlocksPM.FORBIDDEN_FONT_MOON).name("Forbidden Moon Font").build();
        this.block(BlocksPM.FORBIDDEN_FONT_BLOOD).name("Forbidden Blood Font").build();
        this.block(BlocksPM.FORBIDDEN_FONT_INFERNAL).name("Forbidden Infernal Font").build();
        this.block(BlocksPM.FORBIDDEN_FONT_VOID).name("Forbidden Void Font").build();
        this.block(BlocksPM.FORBIDDEN_FONT_HALLOWED).name("Forbidden Hallowed Font").build();
        this.block(BlocksPM.HEAVENLY_FONT_EARTH).name("Heavenly Earth Font").build();
        this.block(BlocksPM.HEAVENLY_FONT_SEA).name("Heavenly Sea Font").build();
        this.block(BlocksPM.HEAVENLY_FONT_SKY).name("Heavenly Sky Font").build();
        this.block(BlocksPM.HEAVENLY_FONT_SUN).name("Heavenly Sun Font").build();
        this.block(BlocksPM.HEAVENLY_FONT_MOON).name("Heavenly Moon Font").build();
        this.block(BlocksPM.HEAVENLY_FONT_BLOOD).name("Heavenly Blood Font").build();
        this.block(BlocksPM.HEAVENLY_FONT_INFERNAL).name("Heavenly Infernal Font").build();
        this.block(BlocksPM.HEAVENLY_FONT_VOID).name("Heavenly Void Font").build();
        this.block(BlocksPM.HEAVENLY_FONT_HALLOWED).name("Heavenly Hallowed Font").build();
        this.block(BlocksPM.WAND_ASSEMBLY_TABLE).name("Wand Assembly Table").build();
        this.block(BlocksPM.WOOD_TABLE).name("Wood Table").build();
        this.block(BlocksPM.ANALYSIS_TABLE).name("Analysis Table").build();
        this.block(BlocksPM.ESSENCE_FURNACE).name("Essence Furnace").build();
        this.block(BlocksPM.CALCINATOR_BASIC).name("Basic Calcinator").build();
        this.block(BlocksPM.CALCINATOR_ENCHANTED).name("Enchanted Calcinator").build();
        this.block(BlocksPM.CALCINATOR_FORBIDDEN).name("Forbidden Calcinator").build();
        this.block(BlocksPM.CALCINATOR_HEAVENLY).name("Heavenly Calcinator").build();
        this.block(BlocksPM.WAND_INSCRIPTION_TABLE).name("Wand Inscription Table").build();
        this.block(BlocksPM.SPELLCRAFTING_ALTAR).name("Spellcrafting Altar").build();
        this.block(BlocksPM.WAND_CHARGER).name("Mana Charger").build();
        this.block(BlocksPM.CONSECRATION_FIELD).name("Consecration Field").build();
        this.block(BlocksPM.INFUSED_STONE_EARTH).name("Earth-Infused Stone").build();
        this.block(BlocksPM.INFUSED_STONE_SEA).name("Sea-Infused Stone").build();
        this.block(BlocksPM.INFUSED_STONE_SKY).name("Sky-Infused Stone").build();
        this.block(BlocksPM.INFUSED_STONE_SUN).name("Sun-Infused Stone").build();
        this.block(BlocksPM.INFUSED_STONE_MOON).name("Moon-Infused Stone").build();
        this.block(BlocksPM.GLOW_FIELD).name("Glow Field").build();
        this.block(BlocksPM.RESEARCH_TABLE).name("Research Table").build();
        this.block(BlocksPM.SALT_TRAIL).name("Salt Trail").build();
        this.block(BlocksPM.RITUAL_ALTAR).name("Ritual Altar").build();
        this.block(BlocksPM.ROCK_SALT_ORE).name("Rock Salt Ore").build();
        this.block(BlocksPM.SKYGLASS).name("Skyglass").build();
        this.block(BlocksPM.STAINED_SKYGLASS_BLACK).name("Black Stained Skyglass").build();
        this.block(BlocksPM.STAINED_SKYGLASS_BLUE).name("Blue Stained Skyglass").build();
        this.block(BlocksPM.STAINED_SKYGLASS_BROWN).name("Brown Stained Skyglass").build();
        this.block(BlocksPM.STAINED_SKYGLASS_CYAN).name("Cyan Stained Skyglass").build();
        this.block(BlocksPM.STAINED_SKYGLASS_GRAY).name("Gray Stained Skyglass").build();
        this.block(BlocksPM.STAINED_SKYGLASS_GREEN).name("Green Stained Skyglass").build();
        this.block(BlocksPM.STAINED_SKYGLASS_LIGHT_BLUE).name("Light Blue Stained Skyglass").build();
        this.block(BlocksPM.STAINED_SKYGLASS_LIGHT_GRAY).name("Light Gray Stained Skyglass").build();
        this.block(BlocksPM.STAINED_SKYGLASS_LIME).name("Lime Stained Skyglass").build();
        this.block(BlocksPM.STAINED_SKYGLASS_MAGENTA).name("Magenta Stained Skyglass").build();
        this.block(BlocksPM.STAINED_SKYGLASS_ORANGE).name("Orange Stained Skyglass").build();
        this.block(BlocksPM.STAINED_SKYGLASS_PINK).name("Pink Stained Skyglass").build();
        this.block(BlocksPM.STAINED_SKYGLASS_PURPLE).name("Purple Stained Skyglass").build();
        this.block(BlocksPM.STAINED_SKYGLASS_RED).name("Red Stained Skyglass").build();
        this.block(BlocksPM.STAINED_SKYGLASS_WHITE).name("White Stained Skyglass").build();
        this.block(BlocksPM.STAINED_SKYGLASS_YELLOW).name("Yellow Stained Skyglass").build();
        this.block(BlocksPM.SKYGLASS_PANE).name("Skyglass Pane").build();
        this.block(BlocksPM.STAINED_SKYGLASS_PANE_BLACK).name("Black Stained Skyglass Pane").build();
        this.block(BlocksPM.STAINED_SKYGLASS_PANE_BLUE).name("Blue Stained Skyglass Pane").build();
        this.block(BlocksPM.STAINED_SKYGLASS_PANE_BROWN).name("Brown Stained Skyglass Pane").build();
        this.block(BlocksPM.STAINED_SKYGLASS_PANE_CYAN).name("Cyan Stained Skyglass Pane").build();
        this.block(BlocksPM.STAINED_SKYGLASS_PANE_GRAY).name("Gray Stained Skyglass Pane").build();
        this.block(BlocksPM.STAINED_SKYGLASS_PANE_GREEN).name("Green Stained Skyglass Pane").build();
        this.block(BlocksPM.STAINED_SKYGLASS_PANE_LIGHT_BLUE).name("Light Blue Stained Skyglass Pane").build();
        this.block(BlocksPM.STAINED_SKYGLASS_PANE_LIGHT_GRAY).name("Light Gray Stained Skyglass Pane").build();
        this.block(BlocksPM.STAINED_SKYGLASS_PANE_LIME).name("Lime Stained Skyglass Pane").build();
        this.block(BlocksPM.STAINED_SKYGLASS_PANE_MAGENTA).name("Magenta Stained Skyglass Pane").build();
        this.block(BlocksPM.STAINED_SKYGLASS_PANE_ORANGE).name("Orange Stained Skyglass Pane").build();
        this.block(BlocksPM.STAINED_SKYGLASS_PANE_PINK).name("Pink Stained Skyglass Pane").build();
        this.block(BlocksPM.STAINED_SKYGLASS_PANE_PURPLE).name("Purple Stained Skyglass Pane").build();
        this.block(BlocksPM.STAINED_SKYGLASS_PANE_RED).name("Red Stained Skyglass Pane").build();
        this.block(BlocksPM.STAINED_SKYGLASS_PANE_WHITE).name("White Stained Skyglass Pane").build();
        this.block(BlocksPM.STAINED_SKYGLASS_PANE_YELLOW).name("Yellow Stained Skyglass Pane").build();
        this.block(BlocksPM.PRIMALITE_BLOCK).name("Primalite Block").build();
        this.block(BlocksPM.HEXIUM_BLOCK).name("Hexium Block").build();
        this.block(BlocksPM.HALLOWSTEEL_BLOCK).name("Hallowsteel Block").build();
        this.block(BlocksPM.QUARTZ_ORE).name("Quartz Ore").build();
        this.block(BlocksPM.SUNLAMP).name("Sunlamp").build();
        this.block(BlocksPM.SPIRIT_LANTERN).name("Spirit Lantern").build();
        this.block(BlocksPM.RITUAL_CANDLE_BLACK).name("Black Ritual Candle").build();
        this.block(BlocksPM.RITUAL_CANDLE_BLUE).name("Blue Ritual Candle").build();
        this.block(BlocksPM.RITUAL_CANDLE_BROWN).name("Brown Ritual Candle").build();
        this.block(BlocksPM.RITUAL_CANDLE_CYAN).name("Cyan Ritual Candle").build();
        this.block(BlocksPM.RITUAL_CANDLE_GRAY).name("Gray Ritual Candle").build();
        this.block(BlocksPM.RITUAL_CANDLE_GREEN).name("Green Ritual Candle").build();
        this.block(BlocksPM.RITUAL_CANDLE_LIGHT_BLUE).name("Light Blue Ritual Candle").build();
        this.block(BlocksPM.RITUAL_CANDLE_LIGHT_GRAY).name("Light Gray Ritual Candle").build();
        this.block(BlocksPM.RITUAL_CANDLE_LIME).name("Lime Ritual Candle").build();
        this.block(BlocksPM.RITUAL_CANDLE_MAGENTA).name("Magenta Ritual Candle").build();
        this.block(BlocksPM.RITUAL_CANDLE_ORANGE).name("Orange Ritual Candle").build();
        this.block(BlocksPM.RITUAL_CANDLE_PINK).name("Pink Ritual Candle").build();
        this.block(BlocksPM.RITUAL_CANDLE_PURPLE).name("Purple Ritual Candle").build();
        this.block(BlocksPM.RITUAL_CANDLE_RED).name("Red Ritual Candle").build();
        this.block(BlocksPM.RITUAL_CANDLE_WHITE).name("White Ritual Candle").build();
        this.block(BlocksPM.RITUAL_CANDLE_YELLOW).name("Yellow Ritual Candle").build();
        this.block(BlocksPM.OFFERING_PEDESTAL).name("Offering Pedestal").build();
        this.block(BlocksPM.INCENSE_BRAZIER).name("Incense Brazier").build();
        this.block(BlocksPM.RITUAL_LECTERN).name("Ritual Lectern").build();
        this.block(BlocksPM.RITUAL_BELL).name("Ritual Bell").build();
        this.block(BlocksPM.BLOODLETTER).name("Bloodletter").build();
        this.block(BlocksPM.SOUL_ANVIL).name("Soul Anvil").build();
        this.block(BlocksPM.CELESTIAL_HARP).name("Celestial Harp").build();
        this.block(BlocksPM.ENTROPY_SINK).name("Entropy Sink").build();
        this.block(BlocksPM.RUNESCRIBING_ALTAR_BASIC).name("Basic Runescribing Altar").build();
        this.block(BlocksPM.RUNESCRIBING_ALTAR_ENCHANTED).name("Enchanted Runescribing Altar").build();
        this.block(BlocksPM.RUNESCRIBING_ALTAR_FORBIDDEN).name("Forbidden Runescribing Altar").build();
        this.block(BlocksPM.RUNESCRIBING_ALTAR_HEAVENLY).name("Heavenly Runescribing Altar").build();
        this.block(BlocksPM.RUNECARVING_TABLE).name("Runecarving Table").build();
        this.block(BlocksPM.RUNIC_GRINDSTONE).name("Runic Grindstone").build();
        this.block(BlocksPM.HONEY_EXTRACTOR).name("Honey Extractor").build();
        this.block(BlocksPM.PRIMALITE_GOLEM_CONTROLLER).name("Primalite Golem Controller").build();
        this.block(BlocksPM.HEXIUM_GOLEM_CONTROLLER).name("Hexium Golem Controller").build();
        this.block(BlocksPM.HALLOWSTEEL_GOLEM_CONTROLLER).name("Hallowsteel Golem Controller").build();
        this.block(BlocksPM.SANGUINE_CRUCIBLE).name("Sanguine Crucible").build();
        this.block(BlocksPM.CONCOCTER).name("Concocter").build();
        this.block(BlocksPM.AUTO_CHARGER).name("Auto-Charger").build();
        this.block(BlocksPM.IGNYX_BLOCK).name("Block of Ignyx").build();
        this.block(BlocksPM.SALT_BLOCK).name("Block of Refined Salt").build();
        this.block(BlocksPM.ESSENCE_TRANSMUTER).name("Essence Transmuter").build();
        this.block(BlocksPM.DISSOLUTION_CHAMBER).name("Dissolution Chamber").build();
        this.block(BlocksPM.ZEPHYR_ENGINE).name("Zephyr Engine").build();
        this.block(BlocksPM.VOID_TURBINE).name("Void Turbine").build();
        this.block(BlocksPM.ESSENCE_CASK_ENCHANTED).name("Enchanted Essence Cask").build();
        this.block(BlocksPM.ESSENCE_CASK_FORBIDDEN).name("Forbidden Essence Cask").build();
        this.block(BlocksPM.ESSENCE_CASK_HEAVENLY).name("Heavenly Essence Cask").build();
        this.block(BlocksPM.WAND_GLAMOUR_TABLE).name("Wand Glamour Table").build();
        this.block(BlocksPM.TREEFOLK_SPROUT).name("Treefolk Sprout").build();
        this.block(BlocksPM.INFERNAL_FURNACE).name("Infernal Furnace").build();
        this.block(BlocksPM.MANA_NEXUS).name("Mana Nexus").build();
        this.block(BlocksPM.MANA_SINGULARITY).name("Mana Singularity").build();
        this.block(BlocksPM.MANA_SINGULARITY_CREATIVE).name("Creative Mana Singularity").build();
        this.block(BlocksPM.HYDROMELON).name("Hydromelon").build();
        this.block(BlocksPM.HYRDOMELON_STEM).name("Hydromelon Stem").build();
        this.block(BlocksPM.ATTACHED_HYDROMELON_STEM).name("Attached Hydromelon Stem").build();
        this.block(BlocksPM.SYNTHETIC_AMETHYST_CLUSTER).name("Synthetic Amethyst Cluster").build();
        this.block(BlocksPM.LARGE_SYNTHETIC_AMETHYST_BUD).name("Large Synthetic Amethyst Bud").build();
        this.block(BlocksPM.MEDIUM_SYNTHETIC_AMETHYST_BUD).name("Medium Synthetic Amethyst Bud").build();
        this.block(BlocksPM.SMALL_SYNTHETIC_AMETHYST_BUD).name("Small Synthetic Amethyst Bud").build();
        this.block(BlocksPM.DAMAGED_BUDDING_AMETHYST_BLOCK).name("Damaged Budding Amethyst Block").build();
        this.block(BlocksPM.CHIPPED_BUDDING_AMETHYST_BLOCK).name("Chipped Budding Amethyst Block").build();
        this.block(BlocksPM.FLAWED_BUDDING_AMETHYST_BLOCK).name("Flawed Budding Amethyst Block").build();
        this.block(BlocksPM.SYNTHETIC_DIAMOND_CLUSTER).name("Synthetic Diamond Cluster").build();
        this.block(BlocksPM.LARGE_SYNTHETIC_DIAMOND_BUD).name("Large Synthetic Diamond Bud").build();
        this.block(BlocksPM.MEDIUM_SYNTHETIC_DIAMOND_BUD).name("Medium Synthetic Diamond Bud").build();
        this.block(BlocksPM.SMALL_SYNTHETIC_DIAMOND_BUD).name("Small Synthetic Diamond Bud").build();
        this.block(BlocksPM.DAMAGED_BUDDING_DIAMOND_BLOCK).name("Damaged Budding Diamond Block").build();
        this.block(BlocksPM.CHIPPED_BUDDING_DIAMOND_BLOCK).name("Chipped Budding Diamond Block").build();
        this.block(BlocksPM.FLAWED_BUDDING_DIAMOND_BLOCK).name("Flawed Budding Diamond Block").build();
        this.block(BlocksPM.SYNTHETIC_EMERALD_CLUSTER).name("Synthetic Emerald Cluster").build();
        this.block(BlocksPM.LARGE_SYNTHETIC_EMERALD_BUD).name("Large Synthetic Emerald Bud").build();
        this.block(BlocksPM.MEDIUM_SYNTHETIC_EMERALD_BUD).name("Medium Synthetic Emerald Bud").build();
        this.block(BlocksPM.SMALL_SYNTHETIC_EMERALD_BUD).name("Small Synthetic Emerald Bud").build();
        this.block(BlocksPM.DAMAGED_BUDDING_EMERALD_BLOCK).name("Damaged Budding Emerald Block").build();
        this.block(BlocksPM.CHIPPED_BUDDING_EMERALD_BLOCK).name("Chipped Budding Emerald Block").build();
        this.block(BlocksPM.FLAWED_BUDDING_EMERALD_BLOCK).name("Flawed Budding Emerald Block").build();
        this.block(BlocksPM.SYNTHETIC_QUARTZ_CLUSTER).name("Synthetic Quartz Cluster").build();
        this.block(BlocksPM.LARGE_SYNTHETIC_QUARTZ_BUD).name("Large Synthetic Quartz Bud").build();
        this.block(BlocksPM.MEDIUM_SYNTHETIC_QUARTZ_BUD).name("Medium Synthetic Quartz Bud").build();
        this.block(BlocksPM.SMALL_SYNTHETIC_QUARTZ_BUD).name("Small Synthetic Quartz Bud").build();
        this.block(BlocksPM.DAMAGED_BUDDING_QUARTZ_BLOCK).name("Damaged Budding Quartz Block").build();
        this.block(BlocksPM.CHIPPED_BUDDING_QUARTZ_BLOCK).name("Chipped Budding Quartz Block").build();
        this.block(BlocksPM.FLAWED_BUDDING_QUARTZ_BLOCK).name("Flawed Budding Quartz Block").build();
        this.block(BlocksPM.ENDERWARD).name("Enderward").build();
        
        // Generate item localizations
        this.item(ItemsPM.GRIMOIRE).name("Grimoire").build();
        this.item(ItemsPM.CREATIVE_GRIMOIRE).name("Creative Grimoire").tooltip("Contains all the secrets of the arcane").build();
        this.item(ItemsPM.ARCANOMETER).name("Arcanometer").build();
        this.item(ItemsPM.MAGNIFYING_GLASS).name("Magnifying Glass").build();
        this.item(ItemsPM.ESSENCE_DUST_EARTH).name("Earth Dust").build();
        this.item(ItemsPM.ESSENCE_DUST_SEA).name("Sea Dust").build();
        this.item(ItemsPM.ESSENCE_DUST_SKY).name("Sky Dust").build();
        this.item(ItemsPM.ESSENCE_DUST_SUN).name("Sun Dust").build();
        this.item(ItemsPM.ESSENCE_DUST_MOON).name("Moon Dust").build();
        this.item(ItemsPM.ESSENCE_DUST_BLOOD).name("Blood Dust").build();
        this.item(ItemsPM.ESSENCE_DUST_INFERNAL).name("Infernal Dust").build();
        this.item(ItemsPM.ESSENCE_DUST_VOID).name("Void Dust").build();
        this.item(ItemsPM.ESSENCE_DUST_HALLOWED).name("Hallowed Dust").build();
        this.item(ItemsPM.ESSENCE_SHARD_EARTH).name("Earth Shard").build();
        this.item(ItemsPM.ESSENCE_SHARD_SEA).name("Sea Shard").build();
        this.item(ItemsPM.ESSENCE_SHARD_SKY).name("Sky Shard").build();
        this.item(ItemsPM.ESSENCE_SHARD_SUN).name("Sun Shard").build();
        this.item(ItemsPM.ESSENCE_SHARD_MOON).name("Moon Shard").build();
        this.item(ItemsPM.ESSENCE_SHARD_BLOOD).name("Blood Shard").build();
        this.item(ItemsPM.ESSENCE_SHARD_INFERNAL).name("Infernal Shard").build();
        this.item(ItemsPM.ESSENCE_SHARD_VOID).name("Void Shard").build();
        this.item(ItemsPM.ESSENCE_SHARD_HALLOWED).name("Hallowed Shard").build();
        this.item(ItemsPM.ESSENCE_CRYSTAL_EARTH).name("Earth Crystal").build();
        this.item(ItemsPM.ESSENCE_CRYSTAL_SEA).name("Sea Crystal").build();
        this.item(ItemsPM.ESSENCE_CRYSTAL_SKY).name("Sky Crystal").build();
        this.item(ItemsPM.ESSENCE_CRYSTAL_SUN).name("Sun Crystal").build();
        this.item(ItemsPM.ESSENCE_CRYSTAL_MOON).name("Moon Crystal").build();
        this.item(ItemsPM.ESSENCE_CRYSTAL_BLOOD).name("Blood Crystal").build();
        this.item(ItemsPM.ESSENCE_CRYSTAL_INFERNAL).name("Infernal Crystal").build();
        this.item(ItemsPM.ESSENCE_CRYSTAL_VOID).name("Void Crystal").build();
        this.item(ItemsPM.ESSENCE_CRYSTAL_HALLOWED).name("Hallowed Crystal").build();
        this.item(ItemsPM.ESSENCE_CLUSTER_EARTH).name("Earth Cluster").build();
        this.item(ItemsPM.ESSENCE_CLUSTER_SEA).name("Sea Cluster").build();
        this.item(ItemsPM.ESSENCE_CLUSTER_SKY).name("Sky Cluster").build();
        this.item(ItemsPM.ESSENCE_CLUSTER_SUN).name("Sun Cluster").build();
        this.item(ItemsPM.ESSENCE_CLUSTER_MOON).name("Moon Cluster").build();
        this.item(ItemsPM.ESSENCE_CLUSTER_BLOOD).name("Blood Cluster").build();
        this.item(ItemsPM.ESSENCE_CLUSTER_INFERNAL).name("Infernal Cluster").build();
        this.item(ItemsPM.ESSENCE_CLUSTER_VOID).name("Void Cluster").build();
        this.item(ItemsPM.ESSENCE_CLUSTER_HALLOWED).name("Hallowed Cluster").build();
        this.item(ItemsPM.ALCHEMICAL_WASTE).name("Alchemical Waste").build();
        this.item(ItemsPM.BLOODY_FLESH).name("Bloody Flesh").tooltip("A dark hunger comes over you", "as you look at this...").build();
        this.item(ItemsPM.HALLOWED_ORB).name("Hallowed Orb").tooltip("A sense of peace washes over", "you as you hold this").build();
        this.item(ItemsPM.HEARTWOOD).name("Heartwood").build();
        this.item(ItemsPM.ENCHANTED_INK).name("Enchanted Ink").build();
        this.item(ItemsPM.ENCHANTED_INK_AND_QUILL).name("Enchanted Ink and Quill").build();
        this.item(ItemsPM.ROCK_SALT).name("Rock Salt").build();
        this.item(ItemsPM.REFINED_SALT).name("Refined Salt").build();
        this.item(ItemsPM.SALTED_BAKED_POTATO).name("Salted Baked Potato").build();
        this.item(ItemsPM.SALTED_COOKED_BEEF).name("Salted Steak").build();
        this.item(ItemsPM.SALTED_COOKED_CHICKEN).name("Salted Cooked Chicken").build();
        this.item(ItemsPM.SALTED_COOKED_COD).name("Salted Cooked Cod").build();
        this.item(ItemsPM.SALTED_COOKED_MUTTON).name("Salted Cooked Mutton").build();
        this.item(ItemsPM.SALTED_COOKED_PORKCHOP).name("Salted Cooked Porkchop").build();
        this.item(ItemsPM.SALTED_COOKED_RABBIT).name("Salted Cooked Rabbit").build();
        this.item(ItemsPM.SALTED_COOKED_SALMON).name("Salted Cooked Salmon").build();
        this.item(ItemsPM.SALTED_BEETROOT_SOUP).name("Salted Beetroot Soup").build();
        this.item(ItemsPM.SALTED_MUSHROOM_STEW).name("Salted Mushroom Stew").build();
        this.item(ItemsPM.SALTED_RABBIT_STEW).name("Salted Rabbit Stew").build();
        this.item(ItemsPM.EARTHSHATTER_HAMMER).name("Earthshatter Hammer").build();
        this.item(ItemsPM.IRON_GRIT).name("Iron Grit").build();
        this.item(ItemsPM.GOLD_GRIT).name("Gold Grit").build();
        this.item(ItemsPM.COPPER_GRIT).name("Copper Grit").build();
        this.item(ItemsPM.PRIMALITE_INGOT).name("Primalite Ingot").build();
        this.item(ItemsPM.HEXIUM_INGOT).name("Hexium Ingot").build();
        this.item(ItemsPM.HALLOWSTEEL_INGOT).name("Hallowsteel Ingot").build();
        this.item(ItemsPM.PRIMALITE_NUGGET).name("Primalite Nugget").build();
        this.item(ItemsPM.HEXIUM_NUGGET).name("Hexium Nugget").build();
        this.item(ItemsPM.HALLOWSTEEL_NUGGET).name("Hallowsteel Nugget").build();
        this.item(ItemsPM.QUARTZ_NUGGET).name("Quartz Sliver").build();
        this.item(ItemsPM.MANA_PRISM).name("Mana Prism").build();
        this.item(ItemsPM.TALLOW).name("Alchemical Tallow").build();
        this.item(ItemsPM.BEESWAX).name("Beeswax").build();
        this.item(ItemsPM.MANA_SALTS).name("Mana Salts").build();
        this.item(ItemsPM.MANAFRUIT).name("Manafruit").build();
        this.item(ItemsPM.INCENSE_STICK).name("Incense Stick").build();
        this.item(ItemsPM.SOUL_GEM).name("Soul Gem").build();
        this.item(ItemsPM.SOUL_GEM_SLIVER).name("Soul Gem Sliver").build();
        this.item(ItemsPM.SPELLCLOTH).name("Spellcloth").build();
        this.item(ItemsPM.HEXWEAVE).name("Hexweave").build();
        this.item(ItemsPM.SAINTSWOOL).name("Saintswool").build();
        this.item(ItemsPM.RUNE_UNATTUNED).name("Unattuned Source Rune").build();
        this.item(ItemsPM.RUNE_EARTH).name("Earth Rune").tooltip("\"Teq\"").build();
        this.item(ItemsPM.RUNE_SEA).name("Sea Rune").tooltip("\"Ald\"").build();
        this.item(ItemsPM.RUNE_SKY).name("Sky Rune").tooltip("\"Der\"").build();
        this.item(ItemsPM.RUNE_SUN).name("Sun Rune").tooltip("\"Sid\"").build();
        this.item(ItemsPM.RUNE_MOON).name("Moon Rune").tooltip("\"Lun\"").build();
        this.item(ItemsPM.RUNE_BLOOD).name("Blood Rune").tooltip("\"Hem\"").build();
        this.item(ItemsPM.RUNE_INFERNAL).name("Infernal Rune").tooltip("\"Cey\"").build();
        this.item(ItemsPM.RUNE_VOID).name("Void Rune").tooltip("\"Zir\"").build();
        this.item(ItemsPM.RUNE_HALLOWED).name("Hallowed Rune").tooltip("\"Phal\"").build();
        this.item(ItemsPM.RUNE_ABSORB).name("Absorb Rune").tooltip("\"Kath\"").build();
        this.item(ItemsPM.RUNE_DISPEL).name("Dispel Rune").tooltip("\"Neth\"").build();
        this.item(ItemsPM.RUNE_PROJECT).name("Project Rune").tooltip("\"Bok\"").build();
        this.item(ItemsPM.RUNE_PROTECT).name("Protect Rune").tooltip("\"Kor\"").build();
        this.item(ItemsPM.RUNE_SUMMON).name("Summon Rune").tooltip("\"Tir\"").build();
        this.item(ItemsPM.RUNE_AREA).name("Area Rune").tooltip("\"Rem\"").build();
        this.item(ItemsPM.RUNE_CREATURE).name("Creature Rune").tooltip("\"Tak\"").build();
        this.item(ItemsPM.RUNE_ITEM).name("Item Rune").tooltip("\"Mag\"").build();
        this.item(ItemsPM.RUNE_SELF).name("Self Rune").tooltip("\"San\"").build();
        this.item(ItemsPM.RUNE_INSIGHT).name("Insight Rune").tooltip("\"Nif\"").build();
        this.item(ItemsPM.RUNE_POWER).name("Power Rune").tooltip("\"Par\"").build();
        this.item(ItemsPM.RUNE_GRACE).name("Grace Rune").tooltip("\"Fyr\"").build();
        this.item(ItemsPM.IMBUED_WOOL_HEAD).name("Apprentice's Hood").build();
        this.item(ItemsPM.IMBUED_WOOL_CHEST).name("Apprentice's Robes").build();
        this.item(ItemsPM.IMBUED_WOOL_LEGS).name("Apprentice's Leggings").build();
        this.item(ItemsPM.IMBUED_WOOL_FEET).name("Apprentice's Slippers").build();
        this.item(ItemsPM.SPELLCLOTH_HEAD).name("Adept's Hood").build();
        this.item(ItemsPM.SPELLCLOTH_CHEST).name("Adept's Robes").build();
        this.item(ItemsPM.SPELLCLOTH_LEGS).name("Adept's Leggings").build();
        this.item(ItemsPM.SPELLCLOTH_FEET).name("Adept's Slippers").build();
        this.item(ItemsPM.HEXWEAVE_HEAD).name("Wizard's Hood").build();
        this.item(ItemsPM.HEXWEAVE_CHEST).name("Wizard's Robes").build();
        this.item(ItemsPM.HEXWEAVE_LEGS).name("Wizard's Leggings").build();
        this.item(ItemsPM.HEXWEAVE_FEET).name("Wizard's Slippers").build();
        this.item(ItemsPM.SAINTSWOOL_HEAD).name("Archmage's Hood").build();
        this.item(ItemsPM.SAINTSWOOL_CHEST).name("Archmage's Robes").build();
        this.item(ItemsPM.SAINTSWOOL_LEGS).name("Archmage's Leggings").build();
        this.item(ItemsPM.SAINTSWOOL_FEET).name("Archmage's Slippers").build();
        this.item(ItemsPM.PRIMALITE_SWORD).name("Primalite Sword").build();
        this.item(ItemsPM.PRIMALITE_TRIDENT).name("Primalite Trident").build();
        this.item(ItemsPM.PRIMALITE_BOW).name("Primalite Bow").build();
        this.item(ItemsPM.PRIMALITE_SHOVEL).name("Primalite Shovel").build();
        this.item(ItemsPM.PRIMALITE_PICKAXE).name("Primalite Pickaxe").build();
        this.item(ItemsPM.PRIMALITE_AXE).name("Primalite Axe").build();
        this.item(ItemsPM.PRIMALITE_HOE).name("Primalite Hoe").build();
        this.item(ItemsPM.PRIMALITE_FISHING_ROD).name("Primalite Fishing Rod").build();
        this.item(ItemsPM.PRIMALITE_HEAD).name("Primalite Helmet").build();
        this.item(ItemsPM.PRIMALITE_CHEST).name("Primalite Chestplate").build();
        this.item(ItemsPM.PRIMALITE_LEGS).name("Primalite Greaves").build();
        this.item(ItemsPM.PRIMALITE_FEET).name("Primalite Boots").build();
        this.item(ItemsPM.PRIMALITE_SHIELD).name("Primalite Shield")
            .coloredName(DyeColor.WHITE, "White Primalite Shield")
            .coloredName(DyeColor.ORANGE, "Orange Primalite Shield")
            .coloredName(DyeColor.MAGENTA, "Magenta Primalite Shield")
            .coloredName(DyeColor.LIGHT_BLUE, "Light Blue Primalite Shield")
            .coloredName(DyeColor.YELLOW, "Yellow Primalite Shield")
            .coloredName(DyeColor.LIME, "Lime Primalite Shield")
            .coloredName(DyeColor.PINK, "Pink Primalite Shield")
            .coloredName(DyeColor.GRAY, "Gray Primalite Shield")
            .coloredName(DyeColor.LIGHT_GRAY, "Light Gray Primalite Shield")
            .coloredName(DyeColor.CYAN, "Cyan Primalite Shield")
            .coloredName(DyeColor.PURPLE, "Purple Primalite Shield")
            .coloredName(DyeColor.BLUE, "Blue Primalite Shield")
            .coloredName(DyeColor.BROWN, "Brown Primalite Shield")
            .coloredName(DyeColor.GREEN, "Green Primalite Shield")
            .coloredName(DyeColor.RED, "Red Primalite Shield")
            .coloredName(DyeColor.BLACK, "Black Primalite Shield")
            .build();
        this.item(ItemsPM.HEXIUM_SWORD).name("Hexium Sword").build();
        this.item(ItemsPM.HEXIUM_TRIDENT).name("Hexium Trident").build();
        this.item(ItemsPM.HEXIUM_BOW).name("Hexium Bow").build();
        this.item(ItemsPM.HEXIUM_SHOVEL).name("Hexium Shovel").build();
        this.item(ItemsPM.HEXIUM_PICKAXE).name("Hexium Pickaxe").build();
        this.item(ItemsPM.HEXIUM_AXE).name("Hexium Axe").build();
        this.item(ItemsPM.HEXIUM_HOE).name("Hexium Hoe").build();
        this.item(ItemsPM.HEXIUM_FISHING_ROD).name("Hexium Fishing Rod").build();
        this.item(ItemsPM.HEXIUM_HEAD).name("Hexium Helmet").build();
        this.item(ItemsPM.HEXIUM_CHEST).name("Hexium Chestplate").build();
        this.item(ItemsPM.HEXIUM_LEGS).name("Hexium Greaves").build();
        this.item(ItemsPM.HEXIUM_FEET).name("Hexium Boots").build();
        this.item(ItemsPM.HEXIUM_SHIELD).name("Hexium Shield")
            .coloredName(DyeColor.WHITE, "White Hexium Shield")
            .coloredName(DyeColor.ORANGE, "Orange Hexium Shield")
            .coloredName(DyeColor.MAGENTA, "Magenta Hexium Shield")
            .coloredName(DyeColor.LIGHT_BLUE, "Light Blue Hexium Shield")
            .coloredName(DyeColor.YELLOW, "Yellow Hexium Shield")
            .coloredName(DyeColor.LIME, "Lime Hexium Shield")
            .coloredName(DyeColor.PINK, "Pink Hexium Shield")
            .coloredName(DyeColor.GRAY, "Gray Hexium Shield")
            .coloredName(DyeColor.LIGHT_GRAY, "Light Gray Hexium Shield")
            .coloredName(DyeColor.CYAN, "Cyan Hexium Shield")
            .coloredName(DyeColor.PURPLE, "Purple Hexium Shield")
            .coloredName(DyeColor.BLUE, "Blue Hexium Shield")
            .coloredName(DyeColor.BROWN, "Brown Hexium Shield")
            .coloredName(DyeColor.GREEN, "Green Hexium Shield")
            .coloredName(DyeColor.RED, "Red Hexium Shield")
            .coloredName(DyeColor.BLACK, "Black Hexium Shield")
            .build();
        this.item(ItemsPM.HALLOWSTEEL_SWORD).name("Hallowsteel Sword").build();
        this.item(ItemsPM.HALLOWSTEEL_TRIDENT).name("Hallowsteel Trident").build();
        this.item(ItemsPM.HALLOWSTEEL_BOW).name("Hallowsteel Bow").build();
        this.item(ItemsPM.HALLOWSTEEL_SHOVEL).name("Hallowsteel Shovel").build();
        this.item(ItemsPM.HALLOWSTEEL_PICKAXE).name("Hallowsteel Pickaxe").build();
        this.item(ItemsPM.HALLOWSTEEL_AXE).name("Hallowsteel Axe").build();
        this.item(ItemsPM.HALLOWSTEEL_HOE).name("Hallowsteel Hoe").build();
        this.item(ItemsPM.HALLOWSTEEL_FISHING_ROD).name("Hallowsteel Fishing Rod").build();
        this.item(ItemsPM.HALLOWSTEEL_HEAD).name("Hallowsteel Helmet").build();
        this.item(ItemsPM.HALLOWSTEEL_CHEST).name("Hallowsteel Chestplate").build();
        this.item(ItemsPM.HALLOWSTEEL_LEGS).name("Hallowsteel Greaves").build();
        this.item(ItemsPM.HALLOWSTEEL_FEET).name("Hallowsteel Boots").build();
        this.item(ItemsPM.HALLOWSTEEL_SHIELD).name("Hallowsteel Shield")
            .coloredName(DyeColor.WHITE, "White Hallowsteel Shield")
            .coloredName(DyeColor.ORANGE, "Orange Hallowsteel Shield")
            .coloredName(DyeColor.MAGENTA, "Magenta Hallowsteel Shield")
            .coloredName(DyeColor.LIGHT_BLUE, "Light Blue Hallowsteel Shield")
            .coloredName(DyeColor.YELLOW, "Yellow Hallowsteel Shield")
            .coloredName(DyeColor.LIME, "Lime Hallowsteel Shield")
            .coloredName(DyeColor.PINK, "Pink Hallowsteel Shield")
            .coloredName(DyeColor.GRAY, "Gray Hallowsteel Shield")
            .coloredName(DyeColor.LIGHT_GRAY, "Light Gray Hallowsteel Shield")
            .coloredName(DyeColor.CYAN, "Cyan Hallowsteel Shield")
            .coloredName(DyeColor.PURPLE, "Purple Hallowsteel Shield")
            .coloredName(DyeColor.BLUE, "Blue Hallowsteel Shield")
            .coloredName(DyeColor.BROWN, "Brown Hallowsteel Shield")
            .coloredName(DyeColor.GREEN, "Green Hallowsteel Shield")
            .coloredName(DyeColor.RED, "Red Hallowsteel Shield")
            .coloredName(DyeColor.BLACK, "Black Hallowsteel Shield")
            .build();
        this.item(ItemsPM.MAGITECH_PARTS_BASIC).name("Basic Magitech Parts").build();
        this.item(ItemsPM.MAGITECH_PARTS_ENCHANTED).name("Enchanted Magitech Parts").build();
        this.item(ItemsPM.MAGITECH_PARTS_FORBIDDEN).name("Forbidden Magitech Parts").build();
        this.item(ItemsPM.MAGITECH_PARTS_HEAVENLY).name("Heavenly Magitech Parts").build();
        this.item(ItemsPM.SEASCRIBE_PEN).name("Seascribe Pen").build();
        this.item(ItemsPM.BASIC_EARTH_AMBROSIA).name("Earth Ambrosia").build();
        this.item(ItemsPM.BASIC_SEA_AMBROSIA).name("Sea Ambrosia").build();
        this.item(ItemsPM.BASIC_SKY_AMBROSIA).name("Sky Ambrosia").build();
        this.item(ItemsPM.BASIC_SUN_AMBROSIA).name("Sun Ambrosia").build();
        this.item(ItemsPM.BASIC_MOON_AMBROSIA).name("Moon Ambrosia").build();
        this.item(ItemsPM.BASIC_BLOOD_AMBROSIA).name("Blood Ambrosia").build();
        this.item(ItemsPM.BASIC_INFERNAL_AMBROSIA).name("Infernal Ambrosia").build();
        this.item(ItemsPM.BASIC_VOID_AMBROSIA).name("Void Ambrosia").build();
        this.item(ItemsPM.BASIC_HALLOWED_AMBROSIA).name("Hallowed Ambrosia").build();
        this.item(ItemsPM.GREATER_EARTH_AMBROSIA).name("Greater Earth Ambrosia").build();
        this.item(ItemsPM.GREATER_SEA_AMBROSIA).name("Greater Sea Ambrosia").build();
        this.item(ItemsPM.GREATER_SKY_AMBROSIA).name("Greater Sky Ambrosia").build();
        this.item(ItemsPM.GREATER_SUN_AMBROSIA).name("Greater Sun Ambrosia").build();
        this.item(ItemsPM.GREATER_MOON_AMBROSIA).name("Greater Moon Ambrosia").build();
        this.item(ItemsPM.GREATER_BLOOD_AMBROSIA).name("Greater Blood Ambrosia").build();
        this.item(ItemsPM.GREATER_INFERNAL_AMBROSIA).name("Greater Infernal Ambrosia").build();
        this.item(ItemsPM.GREATER_VOID_AMBROSIA).name("Greater Void Ambrosia").build();
        this.item(ItemsPM.GREATER_HALLOWED_AMBROSIA).name("Greater Hallowed Ambrosia").build();
        this.item(ItemsPM.SUPREME_EARTH_AMBROSIA).name("Supreme Earth Ambrosia").build();
        this.item(ItemsPM.SUPREME_SEA_AMBROSIA).name("Supreme Sea Ambrosia").build();
        this.item(ItemsPM.SUPREME_SKY_AMBROSIA).name("Supreme Sky Ambrosia").build();
        this.item(ItemsPM.SUPREME_SUN_AMBROSIA).name("Supreme Sun Ambrosia").build();
        this.item(ItemsPM.SUPREME_MOON_AMBROSIA).name("Supreme Moon Ambrosia").build();
        this.item(ItemsPM.SUPREME_BLOOD_AMBROSIA).name("Supreme Blood Ambrosia").build();
        this.item(ItemsPM.SUPREME_INFERNAL_AMBROSIA).name("Supreme Infernal Ambrosia").build();
        this.item(ItemsPM.SUPREME_VOID_AMBROSIA).name("Supreme Void Ambrosia").build();
        this.item(ItemsPM.SUPREME_HALLOWED_AMBROSIA).name("Supreme Hallowed Ambrosia").build();
        this.item(ItemsPM.FLYING_CARPET).name("Flying Carpet").build();
        this.item(ItemsPM.SPELL_SCROLL_BLANK).name("Blank Spell Scroll").build();
        this.item(ItemsPM.SPELL_SCROLL_FILLED).name("Scroll of %1$s").build();
        this.item(ItemsPM.MUNDANE_WAND).name("Mundane Wand").build();
        this.item(ItemsPM.MODULAR_WAND).name("%1$s %2$s %3$s Wand").build();
        this.item(ItemsPM.MODULAR_STAFF).name("%1$s %2$s %3$s Staff").build();
        this.item(ItemsPM.HEARTWOOD_WAND_CORE_ITEM).name("Heartwood Wand Core").build();
        this.item(ItemsPM.OBSIDIAN_WAND_CORE_ITEM).name("Obsidian Wand Core").build();
        this.item(ItemsPM.CORAL_WAND_CORE_ITEM).name("Coral Wand Core").build();
        this.item(ItemsPM.BAMBOO_WAND_CORE_ITEM).name("Bamboo Wand Core").build();
        this.item(ItemsPM.SUNWOOD_WAND_CORE_ITEM).name("Sunwood Wand Core").build();
        this.item(ItemsPM.MOONWOOD_WAND_CORE_ITEM).name("Moonwood Wand Core").build();
        this.item(ItemsPM.BONE_WAND_CORE_ITEM).name("Bone Wand Core").build();
        this.item(ItemsPM.BLAZE_ROD_WAND_CORE_ITEM).name("Blaze Rod Wand Core").build();
        this.item(ItemsPM.PURPUR_WAND_CORE_ITEM).name("Purpur Wand Core").build();
        this.item(ItemsPM.PRIMAL_WAND_CORE_ITEM).name("Primal Wand Core").build();
        this.item(ItemsPM.DARK_PRIMAL_WAND_CORE_ITEM).name("Dark Primal Wand Core").build();
        this.item(ItemsPM.PURE_PRIMAL_WAND_CORE_ITEM).name("Pure Primal Wand Core").build();
        this.item(ItemsPM.IRON_WAND_CAP_ITEM).name("Iron Wand Cap").build();
        this.item(ItemsPM.GOLD_WAND_CAP_ITEM).name("Gold Wand Cap").build();
        this.item(ItemsPM.PRIMALITE_WAND_CAP_ITEM).name("Primalite Wand Cap").build();
        this.item(ItemsPM.HEXIUM_WAND_CAP_ITEM).name("Hexium Wand Cap").build();
        this.item(ItemsPM.HALLOWSTEEL_WAND_CAP_ITEM).name("Hallowsteel Wand Cap").build();
        this.item(ItemsPM.APPRENTICE_WAND_GEM_ITEM).name("Apprentice's Wand Gem").build();
        this.item(ItemsPM.ADEPT_WAND_GEM_ITEM).name("Adept's Wand Gem").build();
        this.item(ItemsPM.WIZARD_WAND_GEM_ITEM).name("Wizard's Wand Gem").build();
        this.item(ItemsPM.ARCHMAGE_WAND_GEM_ITEM).name("Archmage's Wand Gem").build();
        this.item(ItemsPM.CREATIVE_WAND_GEM_ITEM).name("Creative Wand Gem").build();
        this.item(ItemsPM.HEARTWOOD_STAFF_CORE_ITEM).name("Heartwood Staff Core").build();
        this.item(ItemsPM.OBSIDIAN_STAFF_CORE_ITEM).name("Obsidian Staff Core").build();
        this.item(ItemsPM.CORAL_STAFF_CORE_ITEM).name("Coral Staff Core").build();
        this.item(ItemsPM.BAMBOO_STAFF_CORE_ITEM).name("Bamboo Staff Core").build();
        this.item(ItemsPM.SUNWOOD_STAFF_CORE_ITEM).name("Sunwood Staff Core").build();
        this.item(ItemsPM.MOONWOOD_STAFF_CORE_ITEM).name("Moonwood Staff Core").build();
        this.item(ItemsPM.BONE_STAFF_CORE_ITEM).name("Bone Staff Core").build();
        this.item(ItemsPM.BLAZE_ROD_STAFF_CORE_ITEM).name("Blaze Rod Staff Core").build();
        this.item(ItemsPM.PURPUR_STAFF_CORE_ITEM).name("Purpur Staff Core").build();
        this.item(ItemsPM.PRIMAL_STAFF_CORE_ITEM).name("Primal Staff Core").build();
        this.item(ItemsPM.DARK_PRIMAL_STAFF_CORE_ITEM).name("Dark Primal Staff Core").build();
        this.item(ItemsPM.PURE_PRIMAL_STAFF_CORE_ITEM).name("Pure Primal Staff Core").build();
        this.item(ItemsPM.TREEFOLK_SPAWN_EGG).name("Treefolk Spawn Egg").build();
        this.item(ItemsPM.PRIMALITE_GOLEM_SPAWN_EGG).name("Primalite Golem Spawn Egg").build();
        this.item(ItemsPM.HEXIUM_GOLEM_SPAWN_EGG).name("Hexium Golem Spawn Egg").build();
        this.item(ItemsPM.HALLOWSTEEL_GOLEM_SPAWN_EGG).name("Hallowsteel Golem Spawn Egg").build();
        this.item(ItemsPM.BASIC_EARTH_PIXIE).name("Earth Pixie").build();
        this.item(ItemsPM.GRAND_EARTH_PIXIE).name("Grand Earth Pixie").build();
        this.item(ItemsPM.MAJESTIC_EARTH_PIXIE).name("Majestic Earth Pixie").build();
        this.item(ItemsPM.BASIC_SEA_PIXIE).name("Sea Pixie").build();
        this.item(ItemsPM.GRAND_SEA_PIXIE).name("Grand Sea Pixie").build();
        this.item(ItemsPM.MAJESTIC_SEA_PIXIE).name("Majestic Sea Pixie").build();
        this.item(ItemsPM.BASIC_SKY_PIXIE).name("Sky Pixie").build();
        this.item(ItemsPM.GRAND_SKY_PIXIE).name("Grand Sky Pixie").build();
        this.item(ItemsPM.MAJESTIC_SKY_PIXIE).name("Majestic Sky Pixie").build();
        this.item(ItemsPM.BASIC_SUN_PIXIE).name("Sun Pixie").build();
        this.item(ItemsPM.GRAND_SUN_PIXIE).name("Grand Sun Pixie").build();
        this.item(ItemsPM.MAJESTIC_SUN_PIXIE).name("Majestic Sun Pixie").build();
        this.item(ItemsPM.BASIC_MOON_PIXIE).name("Moon Pixie").build();
        this.item(ItemsPM.GRAND_MOON_PIXIE).name("Grand Moon Pixie").build();
        this.item(ItemsPM.MAJESTIC_MOON_PIXIE).name("Majestic Moon Pixie").build();
        this.item(ItemsPM.BASIC_BLOOD_PIXIE).name("Blood Pixie").build();
        this.item(ItemsPM.GRAND_BLOOD_PIXIE).name("Grand Blood Pixie").build();
        this.item(ItemsPM.MAJESTIC_BLOOD_PIXIE).name("Majestic Blood Pixie").build();
        this.item(ItemsPM.BASIC_INFERNAL_PIXIE).name("Infernal Pixie").build();
        this.item(ItemsPM.GRAND_INFERNAL_PIXIE).name("Grand Infernal Pixie").build();
        this.item(ItemsPM.MAJESTIC_INFERNAL_PIXIE).name("Majestic Infernal Pixie").build();
        this.item(ItemsPM.BASIC_VOID_PIXIE).name("Void Pixie").build();
        this.item(ItemsPM.GRAND_VOID_PIXIE).name("Grand Void Pixie").build();
        this.item(ItemsPM.MAJESTIC_VOID_PIXIE).name("Majestic Void Pixie").build();
        this.item(ItemsPM.BASIC_HALLOWED_PIXIE).name("Hallowed Pixie").build();
        this.item(ItemsPM.GRAND_HALLOWED_PIXIE).name("Grand Hallowed Pixie").build();
        this.item(ItemsPM.MAJESTIC_HALLOWED_PIXIE).name("Majestic Hallowed Pixie").build();
        this.item(ItemsPM.DRAINED_BASIC_EARTH_PIXIE).name("Drained Earth Pixie").build();
        this.item(ItemsPM.DRAINED_GRAND_EARTH_PIXIE).name("Drained Grand Earth Pixie").build();
        this.item(ItemsPM.DRAINED_MAJESTIC_EARTH_PIXIE).name("Drained Majestic Earth Pixie").build();
        this.item(ItemsPM.DRAINED_BASIC_SEA_PIXIE).name("Drained Sea Pixie").build();
        this.item(ItemsPM.DRAINED_GRAND_SEA_PIXIE).name("Drained Grand Sea Pixie").build();
        this.item(ItemsPM.DRAINED_MAJESTIC_SEA_PIXIE).name("Drained Majestic Sea Pixie").build();
        this.item(ItemsPM.DRAINED_BASIC_SKY_PIXIE).name("Drained Sky Pixie").build();
        this.item(ItemsPM.DRAINED_GRAND_SKY_PIXIE).name("Drained Grand Sky Pixie").build();
        this.item(ItemsPM.DRAINED_MAJESTIC_SKY_PIXIE).name("Drained Majestic Sky Pixie").build();
        this.item(ItemsPM.DRAINED_BASIC_SUN_PIXIE).name("Drained Sun Pixie").build();
        this.item(ItemsPM.DRAINED_GRAND_SUN_PIXIE).name("Drained Grand Sun Pixie").build();
        this.item(ItemsPM.DRAINED_MAJESTIC_SUN_PIXIE).name("Drained Majestic Sun Pixie").build();
        this.item(ItemsPM.DRAINED_BASIC_MOON_PIXIE).name("Drained Moon Pixie").build();
        this.item(ItemsPM.DRAINED_GRAND_MOON_PIXIE).name("Drained Grand Moon Pixie").build();
        this.item(ItemsPM.DRAINED_MAJESTIC_MOON_PIXIE).name("Drained Majestic Moon Pixie").build();
        this.item(ItemsPM.DRAINED_BASIC_BLOOD_PIXIE).name("Drained Blood Pixie").build();
        this.item(ItemsPM.DRAINED_GRAND_BLOOD_PIXIE).name("Drained Grand Blood Pixie").build();
        this.item(ItemsPM.DRAINED_MAJESTIC_BLOOD_PIXIE).name("Drained Majestic Blood Pixie").build();
        this.item(ItemsPM.DRAINED_BASIC_INFERNAL_PIXIE).name("Drained Infernal Pixie").build();
        this.item(ItemsPM.DRAINED_GRAND_INFERNAL_PIXIE).name("Drained Grand Infernal Pixie").build();
        this.item(ItemsPM.DRAINED_MAJESTIC_INFERNAL_PIXIE).name("Drained Majestic Infernal Pixie").build();
        this.item(ItemsPM.DRAINED_BASIC_VOID_PIXIE).name("Drained Void Pixie").build();
        this.item(ItemsPM.DRAINED_GRAND_VOID_PIXIE).name("Drained Grand Void Pixie").build();
        this.item(ItemsPM.DRAINED_MAJESTIC_VOID_PIXIE).name("Drained Majestic Void Pixie").build();
        this.item(ItemsPM.DRAINED_BASIC_HALLOWED_PIXIE).name("Drained Hallowed Pixie").build();
        this.item(ItemsPM.DRAINED_GRAND_HALLOWED_PIXIE).name("Drained Grand Hallowed Pixie").build();
        this.item(ItemsPM.DRAINED_MAJESTIC_HALLOWED_PIXIE).name("Drained Majestic Hallowed Pixie").build();
        this.item(ItemsPM.SANGUINE_CORE_BLANK).name("Blank Sanguine Core").build();
        this.item(ItemsPM.SANGUINE_CORE_ALLAY).name("Sanguine Core: Allay").build();
        this.item(ItemsPM.SANGUINE_CORE_AXOLOTL).name("Sanguine Core: Axolotl").build();
        this.item(ItemsPM.SANGUINE_CORE_BAT).name("Sanguine Core: Bat").build();
        this.item(ItemsPM.SANGUINE_CORE_BEE).name("Sanguine Core: Bee").build();
        this.item(ItemsPM.SANGUINE_CORE_BLAZE).name("Sanguine Core: Blaze").build();
        this.item(ItemsPM.SANGUINE_CORE_CAMEL).name("Sanguine Core: Camel").build();
        this.item(ItemsPM.SANGUINE_CORE_CAT).name("Sanguine Core: Cat").build();
        this.item(ItemsPM.SANGUINE_CORE_CAVE_SPIDER).name("Sanguine Core: Cave Spider").build();
        this.item(ItemsPM.SANGUINE_CORE_CHICKEN).name("Sanguine Core: Chicken").build();
        this.item(ItemsPM.SANGUINE_CORE_COD).name("Sanguine Core: Cod").build();
        this.item(ItemsPM.SANGUINE_CORE_COW).name("Sanguine Core: Cow").build();
        this.item(ItemsPM.SANGUINE_CORE_CREEPER).name("Sanguine Core: Creeper").build();
        this.item(ItemsPM.SANGUINE_CORE_DOLPHIN).name("Sanguine Core: Dolphin").build();
        this.item(ItemsPM.SANGUINE_CORE_DONKEY).name("Sanguine Core: Donkey").build();
        this.item(ItemsPM.SANGUINE_CORE_DROWNED).name("Sanguine Core: Drowned").build();
        this.item(ItemsPM.SANGUINE_CORE_ELDER_GUARDIAN).name("Sanguine Core: Elder Guardian").build();
        this.item(ItemsPM.SANGUINE_CORE_ENDERMAN).name("Sanguine Core: Enderman").build();
        this.item(ItemsPM.SANGUINE_CORE_ENDERMITE).name("Sanguine Core: Endermite").build();
        this.item(ItemsPM.SANGUINE_CORE_EVOKER).name("Sanguine Core: Evoker").build();
        this.item(ItemsPM.SANGUINE_CORE_FOX).name("Sanguine Core: Fox").build();
        this.item(ItemsPM.SANGUINE_CORE_FROG).name("Sanguine Core: Frog").build();
        this.item(ItemsPM.SANGUINE_CORE_GHAST).name("Sanguine Core: Ghast").build();
        this.item(ItemsPM.SANGUINE_CORE_GLOW_SQUID).name("Sanguine Core: Glow Squid").build();
        this.item(ItemsPM.SANGUINE_CORE_GOAT).name("Sanguine Core: Goat").build();
        this.item(ItemsPM.SANGUINE_CORE_GUARDIAN).name("Sanguine Core: Guardian").build();
        this.item(ItemsPM.SANGUINE_CORE_HOGLIN).name("Sanguine Core: Hoglin").build();
        this.item(ItemsPM.SANGUINE_CORE_HORSE).name("Sanguine Core: Horse").build();
        this.item(ItemsPM.SANGUINE_CORE_HUSK).name("Sanguine Core: Husk").build();
        this.item(ItemsPM.SANGUINE_CORE_LLAMA).name("Sanguine Core: Llama").build();
        this.item(ItemsPM.SANGUINE_CORE_MAGMA_CUBE).name("Sanguine Core: Magma Cube").build();
        this.item(ItemsPM.SANGUINE_CORE_MOOSHROOM).name("Sanguine Core: Mooshroom").build();
        this.item(ItemsPM.SANGUINE_CORE_OCELOT).name("Sanguine Core: Ocelot").build();
        this.item(ItemsPM.SANGUINE_CORE_PANDA).name("Sanguine Core: Panda").build();
        this.item(ItemsPM.SANGUINE_CORE_PARROT).name("Sanguine Core: Parrot").build();
        this.item(ItemsPM.SANGUINE_CORE_PHANTOM).name("Sanguine Core: Phantom").build();
        this.item(ItemsPM.SANGUINE_CORE_PIG).name("Sanguine Core: Pig").build();
        this.item(ItemsPM.SANGUINE_CORE_PIGLIN).name("Sanguine Core: Piglin").build();
        this.item(ItemsPM.SANGUINE_CORE_PIGLIN_BRUTE).name("Sanguine Core: Piglin Brute").build();
        this.item(ItemsPM.SANGUINE_CORE_PILLAGER).name("Sanguine Core: Pillager").build();
        this.item(ItemsPM.SANGUINE_CORE_POLAR_BEAR).name("Sanguine Core: Polar Bear").build();
        this.item(ItemsPM.SANGUINE_CORE_PUFFERFISH).name("Sanguine Core: Pufferfish").build();
        this.item(ItemsPM.SANGUINE_CORE_RABBIT).name("Sanguine Core: Rabbit").build();
        this.item(ItemsPM.SANGUINE_CORE_RAVAGER).name("Sanguine Core: Ravager").build();
        this.item(ItemsPM.SANGUINE_CORE_SALMON).name("Sanguine Core: Salmon").build();
        this.item(ItemsPM.SANGUINE_CORE_SHEEP).name("Sanguine Core: Sheep").build();
        this.item(ItemsPM.SANGUINE_CORE_SHULKER).name("Sanguine Core: Shulker").build();
        this.item(ItemsPM.SANGUINE_CORE_SILVERFISH).name("Sanguine Core: Silverfish").build();
        this.item(ItemsPM.SANGUINE_CORE_SKELETON).name("Sanguine Core: Skeleton").build();
        this.item(ItemsPM.SANGUINE_CORE_SKELETON_HORSE).name("Sanguine Core: Skeleton Horse").build();
        this.item(ItemsPM.SANGUINE_CORE_SLIME).name("Sanguine Core: Slime").build();
        this.item(ItemsPM.SANGUINE_CORE_SNIFFER).name("Sanguine Core: Sniffer").build();
        this.item(ItemsPM.SANGUINE_CORE_SPIDER).name("Sanguine Core: Spider").build();
        this.item(ItemsPM.SANGUINE_CORE_SQUID).name("Sanguine Core: Squid").build();
        this.item(ItemsPM.SANGUINE_CORE_STRAY).name("Sanguine Core: Stray").build();
        this.item(ItemsPM.SANGUINE_CORE_STRIDER).name("Sanguine Core: Strider").build();
        this.item(ItemsPM.SANGUINE_CORE_TROPICAL_FISH).name("Sanguine Core: Tropical Fish").build();
        this.item(ItemsPM.SANGUINE_CORE_TURTLE).name("Sanguine Core: Turtle").build();
        this.item(ItemsPM.SANGUINE_CORE_VEX).name("Sanguine Core: Vex").build();
        this.item(ItemsPM.SANGUINE_CORE_VILLAGER).name("Sanguine Core: Villager").build();
        this.item(ItemsPM.SANGUINE_CORE_VINDICATOR).name("Sanguine Core: Vindicator").build();
        this.item(ItemsPM.SANGUINE_CORE_WITCH).name("Sanguine Core: Witch").build();
        this.item(ItemsPM.SANGUINE_CORE_WITHER_SKELETON).name("Sanguine Core: Wither Skeleton").build();
        this.item(ItemsPM.SANGUINE_CORE_WOLF).name("Sanguine Core: Wolf").build();
        this.item(ItemsPM.SANGUINE_CORE_ZOGLIN).name("Sanguine Core: Zoglin").build();
        this.item(ItemsPM.SANGUINE_CORE_ZOMBIE).name("Sanguine Core: Zombie").build();
        this.item(ItemsPM.SANGUINE_CORE_ZOMBIE_HORSE).name("Sanguine Core: Zombie Horse").build();
        this.item(ItemsPM.SANGUINE_CORE_ZOMBIE_VILLAGER).name("Sanguine Core: Zombie Villager").build();
        this.item(ItemsPM.SANGUINE_CORE_ZOMBIFIED_PIGLIN).name("Sanguine Core: Zombified Piglin").build();
        this.item(ItemsPM.SANGUINE_CORE_TREEFOLK).name("Sanguine Core: Treefolk").build();
        this.item(ItemsPM.SANGUINE_CORE_INNER_DEMON).name("Sanguine Core: Inner Demon").build();
        this.item(ItemsPM.SKYGLASS_FLASK).name("Skyglass Flask").build();
        this.item(ItemsPM.CONCOCTION)
            .concoctionName(ConcoctionType.WATER, Potions.EMPTY, "Uncraftable Concoction")
            .concoctionName(ConcoctionType.WATER, Potions.WATER, "Water Flask")
            .concoctionName(ConcoctionType.TINCTURE, Potions.EMPTY, "Uncraftable Tincture")
            .concoctionName(ConcoctionType.TINCTURE, Potions.NIGHT_VISION, "Tincture of Night Vision")
            .concoctionName(ConcoctionType.TINCTURE, Potions.INVISIBILITY, "Tincture of Invisibility")
            .concoctionName(ConcoctionType.TINCTURE, Potions.LEAPING, "Tincture of Leaping")
            .concoctionName(ConcoctionType.TINCTURE, Potions.FIRE_RESISTANCE, "Tincture of Fire Resistance")
            .concoctionName(ConcoctionType.TINCTURE, Potions.SWIFTNESS, "Tincture of Swiftness")
            .concoctionName(ConcoctionType.TINCTURE, Potions.SLOWNESS, "Tincture of Slowness")
            .concoctionName(ConcoctionType.TINCTURE, Potions.WATER_BREATHING, "Tincture of Water Breathing")
            .concoctionName(ConcoctionType.TINCTURE, Potions.HEALING, "Tincture of Healing")
            .concoctionName(ConcoctionType.TINCTURE, Potions.HARMING, "Tincture of Harming")
            .concoctionName(ConcoctionType.TINCTURE, Potions.POISON, "Tincture of Poison")
            .concoctionName(ConcoctionType.TINCTURE, Potions.REGENERATION, "Tincture of Regeneration")
            .concoctionName(ConcoctionType.TINCTURE, Potions.STRENGTH, "Tincture of Strength")
            .concoctionName(ConcoctionType.TINCTURE, Potions.WEAKNESS, "Tincture of Weakness")
            .concoctionName(ConcoctionType.TINCTURE, "levitation", "Tincture of Levitation")
            .concoctionName(ConcoctionType.TINCTURE, Potions.LUCK, "Tincture of Luck")
            .concoctionName(ConcoctionType.TINCTURE, Potions.TURTLE_MASTER, "Tincture of the Turtle Master")
            .concoctionName(ConcoctionType.TINCTURE, Potions.SLOW_FALLING, "Tincture of Slow Falling")
            .concoctionName(ConcoctionType.PHILTER, Potions.EMPTY, "Uncraftable Philter")
            .concoctionName(ConcoctionType.PHILTER, Potions.NIGHT_VISION, "Philter of Night Vision")
            .concoctionName(ConcoctionType.PHILTER, Potions.INVISIBILITY, "Philter of Invisibility")
            .concoctionName(ConcoctionType.PHILTER, Potions.LEAPING, "Philter of Leaping")
            .concoctionName(ConcoctionType.PHILTER, Potions.FIRE_RESISTANCE, "Philter of Fire Resistance")
            .concoctionName(ConcoctionType.PHILTER, Potions.SWIFTNESS, "Philter of Swiftness")
            .concoctionName(ConcoctionType.PHILTER, Potions.SLOWNESS, "Philter of Slowness")
            .concoctionName(ConcoctionType.PHILTER, Potions.WATER_BREATHING, "Philter of Water Breathing")
            .concoctionName(ConcoctionType.PHILTER, Potions.HEALING, "Philter of Healing")
            .concoctionName(ConcoctionType.PHILTER, Potions.HARMING, "Philter of Harming")
            .concoctionName(ConcoctionType.PHILTER, Potions.POISON, "Philter of Poison")
            .concoctionName(ConcoctionType.PHILTER, Potions.REGENERATION, "Philter of Regeneration")
            .concoctionName(ConcoctionType.PHILTER, Potions.STRENGTH, "Philter of Strength")
            .concoctionName(ConcoctionType.PHILTER, Potions.WEAKNESS, "Philter of Weakness")
            .concoctionName(ConcoctionType.PHILTER, "levitation", "Philter of Levitation")
            .concoctionName(ConcoctionType.PHILTER, Potions.LUCK, "Philter of Luck")
            .concoctionName(ConcoctionType.PHILTER, Potions.TURTLE_MASTER, "Philter of the Turtle Master")
            .concoctionName(ConcoctionType.PHILTER, Potions.SLOW_FALLING, "Philter of Slow Falling")
            .concoctionName(ConcoctionType.ELIXIR, Potions.EMPTY, "Uncraftable Elixir")
            .concoctionName(ConcoctionType.ELIXIR, Potions.NIGHT_VISION, "Elixir of Night Vision")
            .concoctionName(ConcoctionType.ELIXIR, Potions.INVISIBILITY, "Elixir of Invisibility")
            .concoctionName(ConcoctionType.ELIXIR, Potions.LEAPING, "Elixir of Leaping")
            .concoctionName(ConcoctionType.ELIXIR, Potions.FIRE_RESISTANCE, "Elixir of Fire Resistance")
            .concoctionName(ConcoctionType.ELIXIR, Potions.SWIFTNESS, "Elixir of Swiftness")
            .concoctionName(ConcoctionType.ELIXIR, Potions.SLOWNESS, "Elixir of Slowness")
            .concoctionName(ConcoctionType.ELIXIR, Potions.WATER_BREATHING, "Elixir of Water Breathing")
            .concoctionName(ConcoctionType.ELIXIR, Potions.HEALING, "Elixir of Healing")
            .concoctionName(ConcoctionType.ELIXIR, Potions.HARMING, "Elixir of Harming")
            .concoctionName(ConcoctionType.ELIXIR, Potions.POISON, "Elixir of Poison")
            .concoctionName(ConcoctionType.ELIXIR, Potions.REGENERATION, "Elixir of Regeneration")
            .concoctionName(ConcoctionType.ELIXIR, Potions.STRENGTH, "Elixir of Strength")
            .concoctionName(ConcoctionType.ELIXIR, Potions.WEAKNESS, "Elixir of Weakness")
            .concoctionName(ConcoctionType.ELIXIR, "levitation", "Elixir of Levitation")
            .concoctionName(ConcoctionType.ELIXIR, Potions.LUCK, "Elixir of Luck")
            .concoctionName(ConcoctionType.ELIXIR, Potions.TURTLE_MASTER, "Elixir of the Turtle Master")
            .concoctionName(ConcoctionType.ELIXIR, Potions.SLOW_FALLING, "Elixir of Slow Falling")
            .build();
        this.item(ItemsPM.BOMB_CASING).name("Bomb Casing").build();
        this.item(ItemsPM.ALCHEMICAL_BOMB)
            .concoctionName(ConcoctionType.BOMB, Potions.EMPTY, "Uncraftable Bomb")
            .concoctionName(ConcoctionType.BOMB, Potions.WATER, "Water Bomb")
            .concoctionName(ConcoctionType.BOMB, Potions.NIGHT_VISION, "Night Vision Bomb")
            .concoctionName(ConcoctionType.BOMB, Potions.INVISIBILITY, "Invisibility Bomb")
            .concoctionName(ConcoctionType.BOMB, Potions.LEAPING, "Leaping Bomb")
            .concoctionName(ConcoctionType.BOMB, Potions.FIRE_RESISTANCE, "Fire Resistance Bomb")
            .concoctionName(ConcoctionType.BOMB, Potions.SWIFTNESS, "Swiftness Bomb")
            .concoctionName(ConcoctionType.BOMB, Potions.SLOWNESS, "Slowness Bomb")
            .concoctionName(ConcoctionType.BOMB, Potions.WATER_BREATHING, "Water Breathing Bomb")
            .concoctionName(ConcoctionType.BOMB, Potions.HEALING, "Healing Bomb")
            .concoctionName(ConcoctionType.BOMB, Potions.HARMING, "Harming Bomb")
            .concoctionName(ConcoctionType.BOMB, Potions.POISON, "Poison Bomb")
            .concoctionName(ConcoctionType.BOMB, Potions.REGENERATION, "Regeneration Bomb")
            .concoctionName(ConcoctionType.BOMB, Potions.STRENGTH, "Strength Bomb")
            .concoctionName(ConcoctionType.BOMB, Potions.WEAKNESS, "Weakness Bomb")
            .concoctionName(ConcoctionType.BOMB, "levitation", "Levitation Bomb")
            .concoctionName(ConcoctionType.BOMB, Potions.LUCK, "Luck Bomb")
            .concoctionName(ConcoctionType.BOMB, Potions.TURTLE_MASTER, "Turtle Master Bomb")
            .concoctionName(ConcoctionType.BOMB, Potions.SLOW_FALLING, "Slow Falling Bomb")
            .build();
        this.item(ItemsPM.PRIMAL_SHOVEL).name("Shovel of the Quaking Earth").build();
        this.item(ItemsPM.PRIMAL_FISHING_ROD).name("Fishing Rod of the Bountiful Sea").build();
        this.item(ItemsPM.PRIMAL_AXE).name("Axe of the Lightning Crash").build();
        this.item(ItemsPM.PRIMAL_HOE).name("Hoe of the Nurturing Sun").build();
        this.item(ItemsPM.PRIMAL_PICKAXE).name("Pickaxe of the Crescent Moon").build();
        this.item(ItemsPM.FORBIDDEN_TRIDENT).name("Trident of Gaping Wounds").build();
        this.item(ItemsPM.FORBIDDEN_BOW).name("Bow of Screaming Souls").build();
        this.item(ItemsPM.FORBIDDEN_SWORD).name("Sword of the Hungering Void").build();
        this.item(ItemsPM.SACRED_SHIELD).name("Shield of the Sacred Oath").build();
        this.item(ItemsPM.MANA_ARROW_EARTH).name("Earth-Tinged Arrow").build();
        this.item(ItemsPM.MANA_ARROW_SEA).name("Sea-Tinged Arrow").build();
        this.item(ItemsPM.MANA_ARROW_SKY).name("Sky-Tinged Arrow").build();
        this.item(ItemsPM.MANA_ARROW_SUN).name("Sun-Tinged Arrow").build();
        this.item(ItemsPM.MANA_ARROW_MOON).name("Moon-Tinged Arrow").build();
        this.item(ItemsPM.MANA_ARROW_BLOOD).name("Blood-Tinged Arrow").build();
        this.item(ItemsPM.MANA_ARROW_INFERNAL).name("Infernal-Tinged Arrow").build();
        this.item(ItemsPM.MANA_ARROW_VOID).name("Void-Tinged Arrow").build();
        this.item(ItemsPM.MANA_ARROW_HALLOWED).name("Hallowed-Tinged Arrow").build();
        this.item(ItemsPM.DREAM_VISION_TALISMAN).name("Dream Vision Talisman").build();
        this.item(ItemsPM.OBSERVATION_NOTES).name("Field Research Report").tooltip("Use to gain an arcane observation").build();
        this.item(ItemsPM.THEORY_NOTES).name("Arcane Thesis").tooltip("Use to gain an arcane theory").build();
        this.item(ItemsPM.MYSTICAL_RELIC).name("Mystical Relic").tooltip("Use to gain an arcane theory, or keep to trade").build();
        this.item(ItemsPM.MYSTICAL_RELIC_FRAGMENT).name("Mystical Relic Fragment").build();
        this.item(ItemsPM.BLOOD_NOTES).name("Blood-Scrawled Ravings").tooltip("Dare you decipher it?").build();
        this.item(ItemsPM.IGNYX).name("Ignyx").build();
        this.item(ItemsPM.DOWSING_ROD).name("Dowsing Rod").build();
        this.item(ItemsPM.FOUR_LEAF_CLOVER).name("Four-Leaf Clover").build();
        this.item(ItemsPM.SHEEP_TOME).name("Tome of Polymorph: Sheep").build();
        this.item(ItemsPM.HUMMING_ARTIFACT_UNATTUNED).name("Humming Artifact").build();
        this.item(ItemsPM.HUMMING_ARTIFACT_EARTH).name("Earth-Attuned Artifact").tooltip("Use to gain a point of permanent Earth attunement").build();
        this.item(ItemsPM.HUMMING_ARTIFACT_SEA).name("Sea-Attuned Artifact").tooltip("Use to gain a point of permanent Sea attunement").build();
        this.item(ItemsPM.HUMMING_ARTIFACT_SKY).name("Sky-Attuned Artifact").tooltip("Use to gain a point of permanent Sky attunement").build();
        this.item(ItemsPM.HUMMING_ARTIFACT_SUN).name("Sun-Attuned Artifact").tooltip("Use to gain a point of permanent Sun attunement").build();
        this.item(ItemsPM.HUMMING_ARTIFACT_MOON).name("Moon-Attuned Artifact").tooltip("Use to gain a point of permanent Moon attunement").build();
        this.item(ItemsPM.HUMMING_ARTIFACT_BLOOD).name("Blood-Attuned Artifact").tooltip("Use to gain a point of permanent Blood attunement").build();
        this.item(ItemsPM.HUMMING_ARTIFACT_INFERNAL).name("Infernal-Attuned Artifact").tooltip("Use to gain a point of permanent Infernal attunement").build();
        this.item(ItemsPM.HUMMING_ARTIFACT_VOID).name("Void-Attuned Artifact").tooltip("Use to gain a point of permanent Void attunement").build();
        this.item(ItemsPM.HUMMING_ARTIFACT_HALLOWED).name("Hallowed-Attuned Artifact").tooltip("Use to gain a point of permanent Hallowed attunement").build();
        this.item(ItemsPM.RECALL_STONE).name("Recall Stone").build();
        this.item(ItemsPM.TREEFOLK_SEED).name("Treefolk Seed").build();
        this.item(ItemsPM.ATTUNEMENT_SHACKLES_EARTH).name("Earth Attunement Shackles").build();
        this.item(ItemsPM.ATTUNEMENT_SHACKLES_SEA).name("Sea Attunement Shackles").build();
        this.item(ItemsPM.ATTUNEMENT_SHACKLES_SKY).name("Sky Attunement Shackles").build();
        this.item(ItemsPM.ATTUNEMENT_SHACKLES_SUN).name("Sun Attunement Shackles").build();
        this.item(ItemsPM.ATTUNEMENT_SHACKLES_MOON).name("Moon Attunement Shackles").build();
        this.item(ItemsPM.ATTUNEMENT_SHACKLES_BLOOD).name("Blood Attunement Shackles").build();
        this.item(ItemsPM.ATTUNEMENT_SHACKLES_INFERNAL).name("Infernal Attunement Shackles").build();
        this.item(ItemsPM.ATTUNEMENT_SHACKLES_VOID).name("Void Attunement Shackles").build();
        this.item(ItemsPM.ATTUNEMENT_SHACKLES_HALLOWED).name("Hallowed Attunement Shackles").build();
        this.item(ItemsPM.BASIC_WARDING_MODULE).name("Warding Module").build();
        this.item(ItemsPM.GREATER_WARDING_MODULE).name("Greater Warding Module").build();
        this.item(ItemsPM.SUPREME_WARDING_MODULE).name("Supreme Warding Module").build();
        this.item(ItemsPM.STATIC_BOOK).name("Any Book").build();
        this.item(ItemsPM.STATIC_TABLET).name("Any Tablet").build();
        this.item(ItemsPM.CODEX).name("Codex: %1$s").build();
        this.item(ItemsPM.CODEX_CREATIVE).name("Creative Codex: %1$s").build();
        this.item(ItemsPM.LORE_TABLET_FRAGMENT).name("Ancient Tablet Fragment").build();
        this.item(ItemsPM.LORE_TABLET_DIRTY).name("Dirt-Caked Ancient Tablet").tooltip("Use to clean off the dirt and reveal the writing beneath").build();
        this.item(ItemsPM.HYDROMELON_SEEDS).name("Hydromelon Seeds").build();
        this.item(ItemsPM.HYDROMELON_SLICE).name("Hydromelon Slice").build();
        this.item(ItemsPM.BLOOD_ROSE).name("Blood Rose Bush").build();
        this.item(ItemsPM.EMBERFLOWER).name("Emberflower").build();
        this.item(ItemsPM.TICK_STICK).name("Tick Stick").build();
        this.item(ItemsPM.ENERGIZED_AMETHYST).name("Energized Amethyst Shard").build();
        this.item(ItemsPM.ENERGIZED_DIAMOND).name("Energized Diamond").build();
        this.item(ItemsPM.ENERGIZED_EMERALD).name("Energized Emerald").build();
        this.item(ItemsPM.ENERGIZED_QUARTZ).name("Energized Quartz").build();
        
        // Generate miscellaneous tooltip localizations
        this.tooltip("sanguine_core").sub("1").output("Durability: %1$d").end().build();
        this.tooltip("sanguine_core").sub("2").output("Souls per spawn: %1$d").end().build();
        this.tooltip("mana_discount").name("Mana discount: %1$d%%").build();
        this.tooltip("mana_discount_attuned").name("%2$s mana discount: %1$d%%").build();
        this.tooltip("glamoured").name("Glamoured").build();
        this.tooltip("warded").name("Warded").build();
        this.tooltip("runescribed").name("Runescribed").build();
        this.tooltip("rune_limit").name("Limit %1$d per runescribe").build();
        this.tooltip("none").name("None").build();
        this.tooltip("question_marks").name("???").build();
        this.tooltip("more_info").name("Hold Shift for more information").build();
        this.tooltip("analyze_button").sub("1").output("Analyze item affinities").end().build();
        this.tooltip("analyze_button").sub("2").output("Item will be destroyed").end().build();
        this.tooltip("spell_scroll").name("Right-click to cast; will consume scroll").build();
        this.tooltip("dream_vision_talisman.exp").name("Experience: %1$d / %2$d").build();
        this.tooltip("active").name("Active").build();
        this.tooltip("inactive").name("Inactive").build();
        this.tooltip("research_item").name("Use to gain arcane research").build();
        this.tooltip("research_table").sub("material").sub("consumed").output("This material will be consumed").end().build();
        this.tooltip("research_table").sub("material").sub("has_bonus").output("This material will grant bonus research progress").end().build();
        this.tooltip("research_table").sub("theory_gain_preview").sub("positive").sub("single").output("You will gain %1$s theory on success").end().build();
        this.tooltip("research_table").sub("theory_gain_preview").sub("positive").sub("multiple").output("You will gain %1$s theories on success").end().build();
        this.tooltip("attunement_shackles").sub("shackled").output("Shackled").end().build();
        this.tooltip("smithing_template").sub("runic_armor_trim").sub("additions_slot_description").output("Add source rune").end().build();
        this.tooltip("smithing_template").sub("runic_armor_trim").sub("applies_to").output("Robes").end().build();
        this.tooltip("smithing_template").sub("runic_armor_trim").sub("base_slot_description").output("Add a piece of robe armor").end().build();
        this.tooltip("smithing_template").sub("runic_armor_trim").sub("ingredients").output("Source Runes").end().build();
        this.tooltip("source").sub("mana").output("%1$s: %2$d / %3$d (%4$d%% mana cost)").end().build();
        this.tooltip("source").sub("mana_gauge").output("%1$s mana: %2$s / %3$s").end().build();
        this.tooltip("source").sub("mana_container").output("%1$s mana: %2$d").end().build();
        this.tooltip("source").sub("mana_summary_fragment").output("%1$s/%2$s").end().build();
        this.tooltip("spells").sub("wand_header").output("Inscribed Spells (Capacity %1$s):").end().build();
        this.tooltip("spells").sub("none").output("   None").end().build();
        this.tooltip("spells").sub("no_spell_selection").output("No spell").end().build();
        this.tooltip("spells").sub("name_unselected").output("   %1$s").end().build();
        this.tooltip("spells").sub("name_selected").output("  *%1$s").end().build();
        this.tooltip("spells").sub("short_wand_header").output("Active Spell: %1$s").end().build();
        this.tooltip("spells").sub("capacity").output("%1$d").end().build();
        this.tooltip("spells").sub("capacity_with_bonus").output("%1$d + 1 %2$s").end().build();
        this.tooltip("spells").sub("details").sub("vehicle").output("Type: %1$s").end().build();
        this.tooltip("spells").sub("details").sub("payload").output("Effect: %1$s").end().build();
        this.tooltip("spells").sub("details").sub("mods").sub("single").output("Mod: %1$s").end().build();
        this.tooltip("spells").sub("details").sub("mods").sub("double").output("Mods: %1$s, %2$s").end().build();
        this.tooltip("spells").sub("details").sub("cooldown").output("Cooldown: %1$s sec").end().build();
        this.tooltip("spells").sub("details").sub("mana_cost").output("Mana Cost: %1$s").end().build();
        this.tooltip("spells").sub("details").sub("mana_cost").sub("piece").output("%1$d %2$s").end().build();
        this.tooltip("affinities").sub("label").output("Affinities:").end().build();
        this.tooltip("affinities").sub("none").output("Affinities: None").end().build();
        this.tooltip("affinities").sub("unknown").output("Affinities: Unknown").end().build();
        this.tooltip("affinities").sub("calculating").output("Affinities: Calculating...").end().build();
        this.tooltip("written_book").sub("author").sub("unknown").output("Unknown").end().build();
        this.tooltip("written_language").sub("header").output("Language: %1$s").end().build();
        this.tooltip("written_language").sub("comprehension").output("Comprehension: %1$s%%").end().build();
        this.tooltip("written_language").sub("translated").sub("full").output("Fully translated").end().build();
        this.tooltip("written_language").sub("translated").sub("partial").output("Partially translated").end().build();
        this.tooltip("written_language").sub("obfuscated_word").output("Your eyes seem to slide off this word, no matter how hard you try to focus.").end().build();
        this.tooltip("codex").sub("full").output("Use to gain complete understanding of the %1$s language").end().build();
        this.tooltip("codex").sub("partial").output("Use to increase your understanding of the %1$s language").end().build();
        
        // Generate miscellaneous GUI label localizations
        this.label("crafting").sub("mana").output("%1$d %2$s mana").end().build();
        this.label("crafting").sub("mana").sub("base").output("Base cost: %1$d").end().build();
        this.label("crafting").sub("mana").sub("bonus").output("Efficiency bonus: %1$s").end().build();
        this.label("crafting").sub("mana").sub("penalty").output("Efficiency penalty: %1$s").end().build();
        this.label("crafting").sub("mana").sub("modified").output("Total %2$s mana: %1$s").end().build();
        this.label("crafting").sub("mana_cost_header").output("Mana cost:").end().build();
        this.label("crafting").sub("no_mana").output("No mana cost").end().build();
        this.label("crafting").sub("research_header").output("Required research:").end().build();
        this.label("analysis").sub("affinity").output("%1$d %2$s affinity").end().build();
        this.label("analysis").sub("no_item").output("No item analyzed").end().build();
        this.label("analysis").sub("no_affinities").output("No affinities detected").end().build();
        this.label("analysis").sub("calculating").output("Calculating affinities...").end().build();
        this.label("analysis").sub("affinity_report_header").output("%1$s affinities:").end().build();
        this.label("research_table").sub("missing_writing_supplies").output("Missing writing supplies!").end().build();
        this.label("research_table").sub("ready").output("Ready to begin!").end().build();
        this.label("research_table").sub("starting").output("Starting...").end().build();
        this.label("research_table").sub("start").output("Start Project").end().build();
        this.label("research_table").sub("completing").output("Completing...").end().build();
        this.label("research_table").sub("complete").output("Complete Project (%1$s%%)").end().build();
        this.label("research_table").sub("unlock").output("Unlocked by: %1$s").end().build();
        this.label("research_table").sub("aid_header").output("Active research aids:").end().build();
        this.label("research_table").sub("theory_gain_preview").sub("positive").output("+%1$s").end().build();
        this.label("research_table").sub("reward").output("%1$dx %2$s").end().build();
        this.label("research_table").sub("reward").sub("header").output("Other rewards:").end().build();
        this.label("essence_cask").sub("contents").output("Contents: %1$d / %2$d").end().build();
        this.label("essence_cask").sub("left_click").output("Left-click to withdraw one stack").end().build();
        this.label("essence_cask").sub("right_click").output("Right-click to withdraw one item").end().build();
        this.label("toast").sub("completed").output("Research Complete!").end().build();
        this.label("toast").sub("revealed").output("Research Revealed!").end().build();
        this.label("attunement_gain").sub("0").output("None").end().build();
        this.label("attunement_gain").sub("1").output("Trivial").end().build();
        this.label("attunement_gain").sub("2").output("Minor").end().build();
        this.label("attunement_gain").sub("3").output("Moderate").end().build();
        this.label("attunement_gain").sub("4").output("Major").end().build();
        this.label("attunement_gain").sub("5").output("Extreme").end().build();
        this.label("attunement_gain").sub("text").output("%1$s: %2$s").end().build();
        this.label("recipe_book").sub("loading").output("Loading...").end().build();
        this.label("experience").sub("points").output("Experience points").end().build();
        this.label("loot_table").sub("piglin_bartering").sub("desc").output("Random piglin goods").end().build();
        this.label("loot_table").sub("trade").sub("desc").output("Random basic village goods").end().build();
        this.label("loot_table").sub("prosperous_trade").sub("desc").output("Random prosperous village goods").end().build();
        this.label("loot_table").sub("rich_trade").sub("desc").output("Random rich village goods").end().build();

        // Generate entity type localizations
        this.entity(EntityTypesPM.TREEFOLK).name("Treefolk").build();
        this.entity(EntityTypesPM.INNER_DEMON).name("Inner Demon").build();
        this.entity(EntityTypesPM.FRIENDLY_WITCH).name("Witch").build();
        this.entity(EntityTypesPM.PRIMALITE_GOLEM).name("Primalite Golem").build();
        this.entity(EntityTypesPM.HEXIUM_GOLEM).name("Hexium Golem").build();
        this.entity(EntityTypesPM.HALLOWSTEEL_GOLEM).name("Hallowsteel Golem").build();
        this.entity(EntityTypesPM.BASIC_EARTH_PIXIE).name("Earth Pixie").build();
        this.entity(EntityTypesPM.GRAND_EARTH_PIXIE).name("Grand Earth Pixie").build();
        this.entity(EntityTypesPM.MAJESTIC_EARTH_PIXIE).name("Majestic Earth Pixie").build();
        this.entity(EntityTypesPM.BASIC_SEA_PIXIE).name("Sea Pixie").build();
        this.entity(EntityTypesPM.GRAND_SEA_PIXIE).name("Grand Sea Pixie").build();
        this.entity(EntityTypesPM.MAJESTIC_SEA_PIXIE).name("Majestic Sea Pixie").build();
        this.entity(EntityTypesPM.BASIC_SKY_PIXIE).name("Sky Pixie").build();
        this.entity(EntityTypesPM.GRAND_SKY_PIXIE).name("Grand Sky Pixie").build();
        this.entity(EntityTypesPM.MAJESTIC_SKY_PIXIE).name("Majestic Sky Pixie").build();
        this.entity(EntityTypesPM.BASIC_SUN_PIXIE).name("Sun Pixie").build();
        this.entity(EntityTypesPM.GRAND_SUN_PIXIE).name("Grand Sun Pixie").build();
        this.entity(EntityTypesPM.MAJESTIC_SUN_PIXIE).name("Majestic Sun Pixie").build();
        this.entity(EntityTypesPM.BASIC_MOON_PIXIE).name("Moon Pixie").build();
        this.entity(EntityTypesPM.GRAND_MOON_PIXIE).name("Grand Moon Pixie").build();
        this.entity(EntityTypesPM.MAJESTIC_MOON_PIXIE).name("Majestic Moon Pixie").build();
        this.entity(EntityTypesPM.BASIC_BLOOD_PIXIE).name("Blood Pixie").build();
        this.entity(EntityTypesPM.GRAND_BLOOD_PIXIE).name("Grand Blood Pixie").build();
        this.entity(EntityTypesPM.MAJESTIC_BLOOD_PIXIE).name("Majestic Blood Pixie").build();
        this.entity(EntityTypesPM.BASIC_INFERNAL_PIXIE).name("Infernal Pixie").build();
        this.entity(EntityTypesPM.GRAND_INFERNAL_PIXIE).name("Grand Infernal Pixie").build();
        this.entity(EntityTypesPM.MAJESTIC_INFERNAL_PIXIE).name("Majestic Infernal Pixie").build();
        this.entity(EntityTypesPM.BASIC_VOID_PIXIE).name("Void Pixie").build();
        this.entity(EntityTypesPM.GRAND_VOID_PIXIE).name("Grand Void Pixie").build();
        this.entity(EntityTypesPM.MAJESTIC_VOID_PIXIE).name("Majestic Void Pixie").build();
        this.entity(EntityTypesPM.BASIC_HALLOWED_PIXIE).name("Hallowed Pixie").build();
        this.entity(EntityTypesPM.GRAND_HALLOWED_PIXIE).name("Grand Hallowed Pixie").build();
        this.entity(EntityTypesPM.MAJESTIC_HALLOWED_PIXIE).name("Majestic Hallowed Pixie").build();
        this.entity(EntityTypesPM.FLYING_CARPET).name("Flying Carpet").build();
        this.entity(EntityTypesPM.SIN_CRYSTAL).name("Sin Crystal").build();
        
        // Generate status effect localizations
        this.mobEffect(EffectsPM.FLYING).name("Flying").build();
        this.mobEffect(EffectsPM.POLYMORPH).name("Polymorph").build();
        this.mobEffect(EffectsPM.BLEEDING).name("Bleeding").build();
        this.mobEffect(EffectsPM.WEAKENED_SOUL).name("Weakened Soul").build();
        this.mobEffect(EffectsPM.MANAFRUIT).name("Manafruit").build();
        this.mobEffect(EffectsPM.DRAIN_SOUL).name("Drain Soul").build();
        this.mobEffect(EffectsPM.MANA_IMPEDANCE).name("Mana Impedance").build();
        this.mobEffect(EffectsPM.ENDERLOCK).name("Enderlock").build();
        this.mobEffect(EffectsPM.SOULPIERCED).name("Soulpierced").build();
        @SuppressWarnings("removal")
        RegistryObject<MobEffect> stolenEssence = EffectsPM.STOLEN_ESSENCE;
        this.mobEffect(stolenEssence).name("Stolen Essence").build();
        
        // Generate mod enchantment localizations
        this.enchantment(EnchantmentsPM.LIFESTEAL).name("Lifesteal")
            .description("Grants a chance for the wielder to heal themselves a small amount when striking a creature.")
            .fullRuneText("The Lifesteal enchantment can be imbued through the use of Absorb, Self, and Blood runes.  It can be applied to any sword, axe, or trident.  When applied, it grants a chance for the wielder to heal themselves a small amount when striking a creature.")
            .partialRuneText("The Lifesteal enchantment can be imbued through the use of runes, though I'm still learning which ones.  It can be applied to any sword, axe, or trident.  When applied, it grants a chance for the wielder to heal themselves a small amount when striking a creature.")
            .build();
        this.enchantment(EnchantmentsPM.ENDERLOCK).name("Enderlock")
            .description("Causes creatures struck by the wielder to be unable to teleport for a short time.")
            .fullRuneText("The Enderlock enchantment can be imbued through the use of Dispel, Creature, and Void runes.  It can be applied to any sword, axe, or trident.  When applied, it causes creatures struck by the wielder to be unable to teleport for a short time.")
            .partialRuneText("The Enderlock enchantment can be imbued through the use of runes, though I'm still learning which ones.  It can be applied to any sword, axe, or trident.  When applied, it causes creatures struck by the wielder to be unable to teleport for a short time.")
            .build();
        this.enchantment(EnchantmentsPM.JUDGMENT).name("Judgment")
            .description("Increases the damage done by the wielder, with even more damage dealt to undead creatures.")
            .fullRuneText("The Judgment enchantment can be imbued through the use of Project, Item, and Hallowed runes.  It can be applied to any sword, axe, or trident.  When applied, it increases the damage done by the wielder, with even more damage dealt to undead creatures.  It is incompatible with other damage-increasing enchantments.")
            .partialRuneText("The Judgment enchantment can be imbued through the use of runes, though I'm still learning which ones.  It can be applied to any sword, axe, or trident.  When applied, it increases the damage done by the wielder, with even more damage dealt to undead creatures.  It is incompatible with other damage-increasing enchantments.")
            .build();
        this.enchantment(EnchantmentsPM.ENDERPORT).name("Enderport")
            .description("Causes the wielder to teleport to the location that any arrow they fire strikes.")
            .fullRuneText("The Enderport enchantment can be imbued through the use of Summon, Self, and Void runes.  It can be applied to any bow or crossbow.  When applied, it causes the wielder to teleport to the location that any arrow they fire strikes.")
            .partialRuneText("The Enderport enchantment can be imbued through the use of runes, though I'm still learning which ones.  It can be applied to any bow or crossbow.  When applied, it causes the wielder to teleport to the location that any arrow they fire strikes.")
            .build();
        this.enchantment(EnchantmentsPM.REGROWTH).name("Regrowth")
            .description("Causes the item to slowly repair itself while the wielder wears it or holds it in hand.")
            .fullRuneText("The Regrowth enchantment can be imbued through the use of Absorb, Item, and Hallowed runes.  It can be applied to any item that may be damaged or broken.  When applied, it causes the item to slowly repair itself while the wielder wears it or holds it in hand.")
            .partialRuneText("The Regrowth enchantment can be imbued through the use of runes, though I'm still learning which ones.  It can be applied to any item that may be damaged or broken.  When applied, it causes the item to slowly repair itself while the wielder wears it or holds it in hand.")
            .build();
        this.enchantment(EnchantmentsPM.AEGIS).name("Aegis")
            .description("Reduces all incoming damage, with greater effect for fire, explosion, projectile, or falling damage.")
            .fullRuneText("The Aegis enchantment can be imbued through the use of Protect, Self, and Hallowed runes.  It can be applied to any piece of armor.  When applied, it reduces all incoming damage, with greater effect for fire, explosion, projectile, or falling damage.  It does not, however, reduce the amount of time that the wearer is engulfed in flames, or the distance that the wearer is knocked back by explosions.  It is incompatible with other damage-reducing enchantments.")
            .partialRuneText("The Aegis enchantment can be imbued through the use of runes, though I'm still learning which ones.  It can be applied to any piece of armor.  When applied, it reduces all incoming damage, with greater effect for fire, explosion, projectile, or falling damage.  It does not, however, reduce the amount of time that the wearer is engulfed in flames, or the distance that the wearer is knocked back by explosions.  It is incompatible with other damage-reducing enchantments.")
            .build();
        this.enchantment(EnchantmentsPM.MANA_EFFICIENCY).name("Mana Efficiency")
            .description("Reduces all mana costs when crafting or casting a spell.")
            .fullRuneText("The Mana Efficiency enchantment can be imbued through the use of Dispel, Item, and Void runes.  It can be applied to any wand or staff.  When applied, it reduces all mana costs when crafting or casting a spell.")
            .partialRuneText("The Mana Efficiency enchantment can be imbued through the use of runes, though I'm still learning which ones.  It can be applied to any wand or staff.  When applied, it reduces all mana costs when crafting or casting a spell.")
            .build();
        this.enchantment(EnchantmentsPM.SPELL_POWER).name("Spell Power")
            .description("Increases the Power and Duration traits of spell payloads and mods by one per level when cast.")
            .fullRuneText("The Spell Power enchantment can be imbued through the use of Project, Item, and Void runes.  It can be applied to any wand or staff.  When applied, it increases the Power and Duration traits of spell payloads and mods by one per level when cast.")
            .partialRuneText("The Spell Power enchantment can be imbued through the use of runes, though I'm still learning which ones.  It can be applied to any wand or staff.  When applied, it increases the Power and Duration traits of spell payloads and mods by one per level when cast.")
            .build();
        this.enchantment(EnchantmentsPM.TREASURE).name("Treasure")
            .description("Increases the amount of loot dropped by creatures slain by the wielder, as well as the drops yielded by certain blocks, when using magick.")
            .fullRuneText("The Treasure enchantment can be imbued through the use of Project, Item, and Moon runes.  It can be applied to any wand or staff.  When applied, it increases the amount of loot dropped by creatures slain by the wielder, as well as the drops yielded by certain blocks, when using magick.")
            .partialRuneText("The Treasure enchantment can be imbued through the use of runes, though I'm still learning which ones.  It can be applied to any wand or staff.  When applied, it increases the amount of loot dropped by creatures slain by the wielder, as well as the drops yielded by certain blocks, when using magick.")
            .build();
        this.enchantment(EnchantmentsPM.BLUDGEONING).name("Bludgeoning")
            .description("Increases the damage done by the wielder when using the staff as a melee weapon.")
            .fullRuneText("The Bludgeoning enchantment can be imbued through the use of Project, Item, and Earth runes.  It can be applied to any staff.  When applied, it increases the damage done by the wielder when using the staff as a melee weapon.")
            .partialRuneText("The Bludgeoning enchantment can be imbued through the use of runes, though I'm still learning which ones.  It can be applied to any staff.  When applied, it increases the damage done by the wielder when using the staff as a melee weapon.")
            .build();
        this.enchantment(EnchantmentsPM.REVERBERATION).name("Reverberation")
            .description("Increases the tool's area of effect when digging.")
            .fullRuneText("The Reverberation enchantment can be imbued through the use of Project, Area, and Earth runes.  It can be applied to any axe, pickaxe, shovel, or hoe.  When applied, it increases the tool's area of effect when digging.  It is incompatible with the Disintegration enchantment.")
            .partialRuneText("The Reverberation enchantment can be imbued through the use of runes, though I'm still learning which ones.  It can be applied to any axe, pickaxe, shovel, or hoe.  When applied, it increases the tool's area of effect when digging.  It is incompatible with the Disintegration enchantment.")
            .build();
        this.enchantment(EnchantmentsPM.BOUNTY).name("Bounty")
            .description("Increases yields when fishing or harvesting crops.")
            .fullRuneText("The Bounty enchantment can be imbued through the use of Summon, Area, and Sea runes.  It can be applied to any fishing rod or hoe.  When applied, it increases yields when fishing or harvesting crops.")
            .partialRuneText("The Bounty enchantment can be imbued through the use of runes, though I'm still learning which ones.  It can be applied to any fishing rod or hoe.  When applied, it increases yields when fishing or harvesting crops.")
            .build();
        this.enchantment(EnchantmentsPM.DISINTEGRATION).name("Disintegration")
            .description("Breaks all blocks of the same type connected to the original target within a certain range.")
            .fullRuneText("The Disintegration enchantment can be imbued through the use of Project, Area, and Sky runes.  It can be applied to any axe, pickaxe, shovel, or hoe.  When applied, it causes a wave of destruction to radiate out from the broken block, breaking all blocks of the same type connected to the original.  It is incompatible with the Reverberation enchantment.")
            .partialRuneText("The Disintegration enchantment can be imbued through the use of runes, though I'm still learning which ones.  It can be applied to any axe, pickaxe, shovel, or hoe.  When applied, it causes a wave of destruction to radiate out from the broken block, breaking all blocks of the same type connected to the original.  It is incompatible with the Reverberation enchantment.")
            .build();
        this.enchantment(EnchantmentsPM.VERDANT).name("Verdant")
            .description("Stimulates rapid growth when used on a plant or fungus, similar to bone meal, at the cost of durability.")
            .fullRuneText("The Verdant enchantment can be imbued through the use of Summon, Creature, and Sun runes.  It can be applied to any hoe.  When applied, it causes the hoe to stimulate rapid growth when used on a plant or fungus, similar to bone meal, at the cost of durability.  Higher levels reduce the durability cost.")
            .partialRuneText("The Verdant enchantment can be imbued through the use of runes, though I'm still learning which ones.  It can be applied to any hoe.  When applied, it causes the hoe to stimulate rapid growth when used on a plant or fungus, similar to bone meal, at the cost of durability.  Higher levels reduce the durability cost.")
            .build();
        this.enchantment(EnchantmentsPM.LUCKY_STRIKE).name("Lucky Strike")
            .description("Yields bonus nuggets when harvesting certain valuable minerals.")
            .fullRuneText("The Lucky Strike enchantment can be imbued through the use of Summon, Item, and Moon runes.  It can be applied to any axe, pickaxe, shovel, or hoe.  When applied, it yields bonus nuggets when harvesting valuable minerals such as iron, gold, and quartz.")
            .partialRuneText("The Lucky Strike enchantment can be imbued through the use of runes, though I'm still learning which ones.  It can be applied to any axe, pickaxe, shovel, or hoe.  When applied, it yields bonus nuggets when harvesting valuable minerals such as iron, gold, and quartz.")
            .build();
        this.enchantment(EnchantmentsPM.RENDING).name("Rending")
            .description("Causes the victim to bleed for additional damage, with subsequent strikes increasing the severity of the bleed effect.")
            .fullRuneText("The Rending enchantment can be imbued through the use of Project, Creature, and Blood runes.  It can be applied to any sword, axe, or trident.  When applied, it causes the victim to bleed for additional damage, with subsequent strikes increasing the severity of the bleed effect.")
            .partialRuneText("The Rending enchantment can be imbued through the use of runes, though I'm still learning which ones.  It can be applied to any sword, axe, or trident.  When applied, it causes the victim to bleed for additional damage, with subsequent strikes increasing the severity of the bleed effect.")
            .build();
        this.enchantment(EnchantmentsPM.SOULPIERCING).name("Soulpiercing")
            .description("Causes the first strike against a target to drop soul gem slivers.")
            .fullRuneText("The Soulpiercing enchantment can be imbued through the use of Absorb, Creature, and Infernal runes.  It can be applied to any bow or crossbow.  When applied, the first strike against a target causes it to drop soul gem slivers.")
            .partialRuneText("The Soulpiercing enchantment can be imbued through the use of runes, though I'm still learning which ones.  It can be applied to any bow or crossbow.  When applied, the first strike against a target causes it to drop soul gem slivers.")
            .build();
        this.enchantment(EnchantmentsPM.ESSENCE_THIEF).name("Essence Thief")
            .description("Steals a portion of the victim's essence, causing them to drop essence dust when slain.")
            .fullRuneText("The Essence Thief enchantment can be imbued through the use of Summon, Item, and Void runes.  It can be applied to any sword, axe, trident, or wand.  When applied, it steals a portion of the victim's essence, causing them to drop essence dust when slain.")
            .partialRuneText("The Essence Thief enchantment can be imbued through the use of runes, though I'm still learning which ones.  It can be applied to any sword, axe, trident, or wand.  When applied, it steals a portion of the victim's essence, causing them to drop essence dust when slain.")
            .build();
        this.enchantment(EnchantmentsPM.BULWARK).name("Bulwark")
            .description("Causes the wielder to take less damage from all sources while blocking.")
            .fullRuneText("The Bulwark enchantment can be imbued through the use of Protect, Self, and Hallowed runes.  It can be applied to any shield.  When applied, it causes the wielder to take less damage from all sources while blocking, even sources that a shield could not normally protect against.")
            .partialRuneText("The Bulwark enchantment can be imbued through the use of runes, though I'm still learning which ones.  It can be applied to any shield.  When applied, it causes the wielder to take less damage from all sources while blocking, even sources that a shield could not normally protect against.")
            .build();
        this.enchantment(EnchantmentsPM.MAGICK_PROTECTION).name("Magick Protection")
            .description("Significantly reduces all incoming magick damage, such as from sorcerous spells.")
            .fullRuneText("The Magick Protection enchantment can be imbued through the use of Protect, Self, and Void runes.  It can be applied to any piece of armor.  When applied, it significantly reduces all incoming magick damage, such as from sorcerous spells.  It is incompatible with other Protection enchantments.")
            .partialRuneText("The Magick Protection enchantment can be imbued through the use of runes, though I'm still learning which ones.  It can be applied to any piece of armor.  When applied, it significantly reduces all incoming magick damage, such as from sorcerous spells.  It is incompatible with other Protection enchantments.")
            .build();
        this.enchantment(EnchantmentsPM.GUILLOTINE).name("Guillotine")
            .description("Grants a chance for the victim to drop its head when slain.")
            .fullRuneText("The Guillotine enchantment can be imbued through the use of Dispel, Creature, and Blood runes.  It can be applied to any sword or axe.  When applied, it increases the chance for the victim to drop its head when slain.")
            .partialRuneText("The Guillotine enchantment can be imbued through the use of runes, though I'm still learning which ones.  It can be applied to any sword or axe.  When applied, it increases the chance for the victim to drop its head when slain.")
            .build();

        // Generate vanilla enchantment localization extensions
        this.enchantment(Enchantments.ALL_DAMAGE_PROTECTION)
            .fullRuneText("The Protection enchantment can be imbued through the use of Protect, Self, and Earth runes.  It can be applied to any piece of armor.  When applied, it reduces all incoming damage.  It is incompatible with other Protection enchantments.")
            .partialRuneText("The Protection enchantment can be imbued through the use of runes, though I'm still learning which ones.  It can be applied to any piece of armor.  When applied, it reduces all incoming damage.  It is incompatible with other Protection enchantments.")
            .build();
        this.enchantment(Enchantments.FIRE_PROTECTION)
            .fullRuneText("The Fire Protection enchantment can be imbued through the use of Protect, Self, and Infernal runes.  It can be applied to any piece of armor.  When applied, it significantly reduces all incoming fire damage as well as the amount of time that the wearer is engulfed in flames.  It is incompatible with other Protection enchantments.")
            .partialRuneText("The Fire Protection enchantment can be imbued through the use of runes, though I'm still learning which ones.  It can be applied to any piece of armor.  When applied, it significantly reduces all incoming fire damage as well as the amount of time that the wearer is engulfed in flames.  It is incompatible with other Protection enchantments.")
            .build();
        this.enchantment(Enchantments.FALL_PROTECTION)
            .fullRuneText("The Feather Falling enchantment can be imbued through the use of Project, Item, and Sky runes.  It can be applied to any pair of boots.  When applied, it greatly reduces all damage sustained from falling.")
            .partialRuneText("The Feather Falling enchantment can be imbued through the use of runes, though I'm still learning which ones.  It can be applied to any pair of boots.  When applied, it greatly reduces all damage sustained from falling.")
            .build();
        this.enchantment(Enchantments.BLAST_PROTECTION)
            .fullRuneText("The Blast Protection enchantment can be imbued through the use of Protect, Self, and Moon runes.  It can be applied to any piece of armor.  When applied, it significantly reduces all incoming explosion damage as well as the distance that the wearer is knocked back by explosions.  It is incompatible with other Protection enchantments.")
            .partialRuneText("The Blast Protection enchantment can be imbued through the use of runes, though I'm still learning which ones.  It can be applied to any piece of armor.  When applied, it significantly reduces all incoming explosion damage as well as the distance that the wearer is knocked back by explosions.  It is incompatible with other Protection enchantments.")
            .build();
        this.enchantment(Enchantments.PROJECTILE_PROTECTION)
            .fullRuneText("The Projectile Protection enchantment can be imbued through the use of Protect, Self, and Sky runes.  It can be applied to any piece of armor.  When applied, it significantly reduces all incoming damage from projectiles, such as arrows.  It is incompatible with other Protection enchantments.")
            .partialRuneText("The Projectile Protection enchantment can be imbued through the use of runes, though I'm still learning which ones.  It can be applied to any piece of armor.  When applied, it significantly reduces all incoming damage from projectiles, such as arrows.  It is incompatible with other Protection enchantments.")
            .build();
        this.enchantment(Enchantments.RESPIRATION)
            .fullRuneText("The Respiration enchantment can be imbued through the use of Project, Item, and Sky runes.  It can be applied to any helmet.  When applied, it increases the amount of time the wearer can remain underwater before drowning.")
            .partialRuneText("The Respiration enchantment can be imbued through the use of runes, though I'm still learning which ones.  It can be applied to any helmet.  When applied, it increases the amount of time the wearer can remain underwater before drowning.")
            .build();
        this.enchantment(Enchantments.AQUA_AFFINITY)
            .fullRuneText("The Aqua Affinity enchantment can be imbued through the use of Project, Item, and Sea runes.  It can be applied to any helmet.  When applied, it increases the speed at which the wearer can dig while underwater.")
            .partialRuneText("The Aqua Affinity enchantment can be imbued through the use of runes, though I'm still learning which ones.  It can be applied to any helmet.  When applied, it increases the speed at which the wearer can dig while underwater.")
            .build();
        this.enchantment(Enchantments.THORNS)
            .fullRuneText("The Thorns enchantment can be imbued through the use of Project, Creature, and Blood runes.  It can be applied to any piece of armor.  When applied, it reflects a portion of incoming damage onto the attacker, at the cost of durability.")
            .partialRuneText("The Thorns enchantment can be imbued through the use of runes, though I'm still learning which ones.  It can be applied to any piece of armor.  When applied, it reflects a portion of incoming damage onto the attacker, at the cost of durability.")
            .build();
        this.enchantment(Enchantments.DEPTH_STRIDER)
            .fullRuneText("The Depth Strider enchantment can be imbued through the use of Project, Item, and Sea runes.  It can be applied to any pair of boots.  When applied, it increases the wearer's underwater movement speed.  It is incompatible with the Frost Walker enchantment.")
            .partialRuneText("The Depth Strider enchantment can be imbued through the use of runes, though I'm still learning which ones.  It can be applied to any pair of boots.  When applied, it increases the wearer's underwater movement speed.  It is incompatible with the Frost Walker enchantment.")
            .build();
        this.enchantment(Enchantments.FROST_WALKER)
            .fullRuneText("The Frost Walker enchantment can be imbued through the use of Project, Area, and Sea runes.  It can be applied to any pair of boots.  When applied, it freezes water beneath the wearer into frosted ice, allowing the wearer to walk on it.  It is incompatible with the Depth Strider enchantment.")
            .partialRuneText("The Frost Walker enchantment can be imbued through the use of runes, though I'm still learning which ones.  It can be applied to any pair of boots.  When applied, it freezes water beneath the wearer into frosted ice, allowing the wearer to walk on it.  It is incompatible with the Depth Strider enchantment.")
            .build();
        this.enchantment(Enchantments.SHARPNESS)
            .fullRuneText("The Sharpness enchantment can be imbued through the use of Project, Item, and Earth runes.  It can be applied to any sword or axe.  When applied, it increases the damage done by the wielder.  It is incompatible with other damage-increasing enchantments.")
            .partialRuneText("The Sharpness enchantment can be imbued through the use of runes, though I'm still learning which ones.  It can be applied to any sword or axe.  When applied, it increases the damage done by the wielder.  It is incompatible with other damage-increasing enchantments.")
            .build();
        this.enchantment(Enchantments.KNOCKBACK)
            .fullRuneText("The Knockback enchantment can be imbued through the use of Project, Creature, and Earth runes.  It can be applied to any sword.  When applied, it increases the distance which a creature struck by the wielder is knocked back.")
            .partialRuneText("The Knockback enchantment can be imbued through the use of runes, though I'm still learning which ones.  It can be applied to any sword.  When applied, it increases the distance which a creature struck by the wielder is knocked back.")
            .build();
        this.enchantment(Enchantments.FIRE_ASPECT)
            .fullRuneText("The Fire Aspect enchantment can be imbued through the use of Project, Item, and Infernal runes.  It can be applied to any sword.  When applied, creatures struck by the wielder will be set on fire.")
            .partialRuneText("The Fire Aspect enchantment can be imbued through the use of runes, though I'm still learning which ones.  It can be applied to any sword.  When applied, creatures struck by the wielder will be set on fire.")
            .build();
        this.enchantment(Enchantments.MOB_LOOTING)
            .fullRuneText("The Looting enchantment can be imbued through the use of Project, Item, and Moon runes.  It can be applied to any sword.  When applied, it increases the amount of loot dropped by creatures slain by the wielder.")
            .partialRuneText("The Looting enchantment can be imbued through the use of runes, though I'm still learning which ones.  It can be applied to any sword.  When applied, it increases the amount of loot dropped by creatures slain by the wielder.")
            .build();
        this.enchantment(Enchantments.SWEEPING_EDGE)
            .fullRuneText("The Sweeping Edge enchantment can be imbued through the use of Project, Area, and Sky runes.  It can be applied to any sword.  When applied, it increases the damage done by the wielder with sweep attacks.")
            .partialRuneText("The Sweeping Edge enchantment can be imbued through the use of runes, though I'm still learning which ones.  It can be applied to any sword.  When applied, it increases the damage done by the wielder with sweep attacks.")
            .build();
        this.enchantment(Enchantments.BLOCK_EFFICIENCY)
            .fullRuneText("The Efficiency enchantment can be imbued through the use of Project, Item, and Sky runes.  It can be applied to any axe, pickaxe, shears, or shovel.  When applied, it increases the speed with which the wielder may use the tool.")
            .partialRuneText("The Efficiency enchantment can be imbued through the use of runes, though I'm still learning which ones.  It can be applied to any axe, pickaxe, shears, or shovel.  When applied, it increases the speed with which the wielder may use the tool.")
            .build();
        this.enchantment(Enchantments.SILK_TOUCH)
            .fullRuneText("The Silk Touch enchantment can be imbued through the use of Project, Item, and Sea runes.  It can be applied to any axe, pickaxe, or shovel.  When applied, it causes any blocks broken by the wielder to be dropped intact and without damage.  It is incompatible with the Fortune enchantment.")
            .partialRuneText("The Silk Touch enchantment can be imbued through the use of runes, though I'm still learning which ones.  It can be applied to any axe, pickaxe, or shovel.  When applied, it causes any blocks broken by the wielder to be dropped intact and without damage.  It is incompatible with the Fortune enchantment.")
            .build();
        this.enchantment(Enchantments.UNBREAKING)
            .fullRuneText("The Unbreaking enchantment can be imbued through the use of Protect, Item, and Earth runes.  It can be applied to any item that may be damaged or broken.  When applied, it increases the durability of the item, making it harder to damage.")
            .partialRuneText("The Unbreaking enchantment can be imbued through the use of runes, though I'm still learning which ones.  It can be applied to any item that may be damaged or broken.  When applied, it increases the durability of the item, making it harder to damage.")
            .build();
        this.enchantment(Enchantments.BLOCK_FORTUNE)
            .fullRuneText("The Fortune enchantment can be imbued through the use of Project, Item, and Moon runes.  It can be applied to any axe, pickaxe, or shovel.  When applied, it increases the drops yielded by certain blocks.  It is incompatible with the Silk Touch enchantment.")
            .partialRuneText("The Fortune enchantment can be imbued through the use of runes, though I'm still learning which ones.  It can be applied to any axe, pickaxe, or shovel.  When applied, it increases the drops yielded by certain blocks.  It is incompatible with the Silk Touch enchantment.")
            .build();
        this.enchantment(Enchantments.POWER_ARROWS)
            .fullRuneText("The Power enchantment can be imbued through the use of Project, Item, and Sky runes.  It can be applied to any bow.  When applied, it increases the damage done by arrows fired by the wielder.")
            .partialRuneText("The Power enchantment can be imbued through the use of runes, though I'm still learning which ones.  It can be applied to any bow.  When applied, it increases the damage done by arrows fired by the wielder.")
            .build();
        this.enchantment(Enchantments.PUNCH_ARROWS)
            .fullRuneText("The Punch enchantment can be imbued through the use of Project, Creature, and Earth runes.  It can be applied to any bow.  When applied, it increases the distance which a creature struck by the wielder's arrows is knocked back.")
            .partialRuneText("The Punch enchantment can be imbued through the use of runes, though I'm still learning which ones.  It can be applied to any bow.  When applied, it increases the distance which a creature struck by the wielder's arrows is knocked back.")
            .build();
        this.enchantment(Enchantments.FLAMING_ARROWS)
            .fullRuneText("The Flame enchantment can be imbued through the use of Project, Item, and Infernal runes.  It can be applied to any bow.  When applied, it causes arrows fired by the wielder to set their targets on fire.")
            .partialRuneText("The Flame enchantment can be imbued through the use of runes, though I'm still learning which ones.  It can be applied to any bow.  When applied, it causes arrows fired by the wielder to set their targets on fire.")
            .build();
        this.enchantment(Enchantments.INFINITY_ARROWS)
            .fullRuneText("The Infinity enchantment can be imbued through the use of Summon, Item, and Sky runes.  It can be applied to any bow.  When applied, it prevents regular arrows from being consumed when the wielder fires.  It is incompatible with the Mending enchantment.")
            .partialRuneText("The Infinity enchantment can be imbued through the use of runes, though I'm still learning which ones.  It can be applied to any bow.  When applied, it prevents regular arrows from being consumed when the wielder fires.  It is incompatible with the Mending enchantment.")
            .build();
        this.enchantment(Enchantments.FISHING_LUCK)
            .fullRuneText("The Luck of the Sea enchantment can be imbued through the use of Project, Item, and Moon runes.  It can be applied to any fishing rod.  When applied, it increases the quality of the items that the wielder fishes up.")
            .partialRuneText("The Luck of the Sea enchantment can be imbued through the use of runes, though I'm still learning which ones.  It can be applied to any fishing rod.  When applied, it increases the quality of the items that the wielder fishes up.")
            .build();
        this.enchantment(Enchantments.FISHING_SPEED)
            .fullRuneText("The Lure enchantment can be imbued through the use of Summon, Creature, and Sea runes.  It can be applied to any fishing rod.  When applied, it decreases the time until the wielder gets a bite when fishing.")
            .partialRuneText("The Lure enchantment can be imbued through the use of runes, though I'm still learning which ones.  It can be applied to any fishing rod.  When applied, it decreases the time until the wielder gets a bite when fishing.")
            .build();
        this.enchantment(Enchantments.LOYALTY)
            .fullRuneText("The Loyalty enchantment can be imbued through the use of Summon, Item, and Sea runes.  It can be applied to any trident.  When applied, it causes the trident to return to the wielder when thrown.  It is incompatible with the Riptide enchantment.")
            .partialRuneText("The Loyalty enchantment can be imbued through the use of runes, though I'm still learning which ones.  It can be applied to any trident.  When applied, it causes the trident to return to the wielder when thrown.  It is incompatible with the Riptide enchantment.")
            .build();
        this.enchantment(Enchantments.IMPALING)
            .fullRuneText("The Impaling enchantment can be imbued through the use of Project, Item, and Sea runes.  It can be applied to any trident.  When applied, it causes the wielder to deal additional damage to oceanic creatures.")
            .partialRuneText("The Impaling enchantment can be imbued through the use of runes, though I'm still learning which ones.  It can be applied to any trident.  When applied, it causes the wielder to deal additional damage to oceanic creatures.")
            .build();
        this.enchantment(Enchantments.RIPTIDE)
            .fullRuneText("The Riptide enchantment can be imbued through the use of Summon, Self, and Sea runes.  It can be applied to any trident.  When applied, it causes the wielder to be launched along with the trident upon throwing it.  This effect only functions in water or rain.  It is incompatible with the Channeling and Loyalty enchantments.")
            .partialRuneText("The Riptide enchantment can be imbued through the use of runes, though I'm still learning which ones.  It can be applied to any trident.  When applied, it causes the wielder to be launched along with the trident upon throwing it.  This effect only functions in water or rain.  It is incompatible with the Channeling and Loyalty enchantments.")
            .build();
        this.enchantment(Enchantments.CHANNELING)
            .fullRuneText("The Channeling enchantment can be imbued through the use of Summon, Area, and Sky runes.  It can be applied to any trident.  When applied, it causes bolts of lightning to be channeled toward struck creatures.  This effect only functions during thunderstorms, when the target has a clear view of the sky.  It is incompatible with the Riptide enchantment.")
            .partialRuneText("The Channeling enchantment can be imbued through the use of runes, though I'm still learning which ones.  It can be applied to any trident.  When applied, it causes bolts of lightning to be channeled toward struck creatures.  This effect only functions during thunderstorms, when the target has a clear view of the sky.  It is incompatible with the Riptide enchantment.")
            .build();
        this.enchantment(Enchantments.MULTISHOT)
            .fullRuneText("The Multishot enchantment can be imbued through the use of Summon, Item, and Sky runes.  It can be applied to any crossbow.  When applied, it causes the wielder to fire three arrows at the cost of one, simultaneously.  It is incompatible with the Piercing enchantment.")
            .partialRuneText("The Multishot enchantment can be imbued through the use of runes, though I'm still learning which ones.  It can be applied to any crossbow.  When applied, it causes the wielder to fire three arrows at the cost of one, simultaneously.  It is incompatible with the Piercing enchantment.")
            .build();
        this.enchantment(Enchantments.QUICK_CHARGE)
            .fullRuneText("The Quick Charge enchantment can be imbued through the use of Project, Item, and Sky runes.  It can be applied to any crossbow.  When applied, it decreases the time it takes for the wielder to reload the crossbow.")
            .partialRuneText("The Quick Charge enchantment can be imbued through the use of runes, though I'm still learning which ones.  It can be applied to any crossbow.  When applied, it decreases the time it takes for the wielder to reload the crossbow.")
            .build();
        this.enchantment(Enchantments.PIERCING)
            .fullRuneText("The Piercing enchantment can be imbued through the use of Project, Item, and Earth runes.  It can be applied to any crossbow.  When applied, it causes arrows fired by the wielder to pass through multiple enemies.  It is incompatible with the Multishot enchantment.")
            .partialRuneText("The Piercing enchantment can be imbued through the use of runes, though I'm still learning which ones.  It can be applied to any crossbow.  When applied, it causes arrows fired by the wielder to pass through multiple enemies.  It is incompatible with the Multishot enchantment.")
            .build();
        this.enchantment(Enchantments.MENDING)
            .fullRuneText("The Mending enchantment can be imbued through the use of Absorb, Item, and Sun runes.  It can be applied to any item that may be damaged or broken.  When applied, it causes the item to repair itself when the wielder collects experience orbs.  It is incompatible with the Infinity enchantment.")
            .partialRuneText("The Mending enchantment can be imbued through the use of runes, though I'm still learning which ones.  It can be applied to any item that may be damaged or broken.  When applied, it causes the item to repair itself when the wielder collects experience orbs.  It is incompatible with the Infinity enchantment.")
            .build();
        this.enchantment(Enchantments.BANE_OF_ARTHROPODS)
            .fullRuneText("The Bane of Arthropods enchantment can be imbued through the use of Project, Item, and Blood runes.  It can be applied to any sword or axe.  When applied, it increases the damage done by the wielder to insect-like creatures.  It is incompatible with other damage-increasing enchantments.")
            .partialRuneText("The Bane of Arthropods enchantment can be imbued through the use of runes, though I'm still learning which ones.  It can be applied to any sword or axe.  When applied, it increases the damage done by the wielder to insect-like creatures.  It is incompatible with other damage-increasing enchantments.")
            .build();
        this.enchantment(Enchantments.SMITE)
            .fullRuneText("The Smite enchantment can be imbued through the use of Project, Creature, and Sun runes.  It can be applied to any sword or axe.  When applied, it increases the damage done by the wielder to undead creatures.  It is incompatible with other damage-increasing enchantments.")
            .partialRuneText("The Smite enchantment can be imbued through the use of runes, though I'm still learning which ones.  It can be applied to any sword or axe.  When applied, it increases the damage done by the wielder to undead creatures.  It is incompatible with other damage-increasing enchantments.")
            .build();
        this.enchantment(Enchantments.SOUL_SPEED)
            .fullRuneText("The Soul Speed enchantment can be imbued through the use of Project, Self, and Infernal runes.  It can be applied to any pair of boots.  When applied, it increases the wearer's movement speed when standing on soul sand or soul soil.")
            .partialRuneText("The Soul Speed enchantment can be imbued through the use of runes, though I'm still learning which ones.  It can be applied to any pair of boots.  When applied, it increases the wearer's movement speed when standing on soul sand or soul soil.")
            .build();
        this.enchantment(Enchantments.SWIFT_SNEAK)
            .fullRuneText("The Swift Sneak enchantment can be imbued through the use of Project, Self, and Sky runes.  It can be applied to any pair of pants.  When applied, it increases the wearer's movement speed when sneaking.")
            .partialRuneText("The Swift Sneak enchantment can be imbued through the use of runes, though I'm still learning which ones.  It can be applied to any pair of pants.  When applied, it increases the wearer's movement speed when sneaking.")
            .build();

        // Generate armor trim localizations
        this.trimMaterial("runic_trim_materials", List.of(TrimMaterialsPM.RUNE_EARTH, TrimMaterialsPM.RUNE_SEA, TrimMaterialsPM.RUNE_SKY, TrimMaterialsPM.RUNE_SUN, TrimMaterialsPM.RUNE_MOON, TrimMaterialsPM.RUNE_BLOOD, TrimMaterialsPM.RUNE_INFERNAL, TrimMaterialsPM.RUNE_VOID, TrimMaterialsPM.RUNE_HALLOWED))
            .namePattern("%s Rune Material").build();
        this.trimPattern(TrimPatternsPM.RUNIC).name("Runic Armor Trim").build();
        
        // Generate death message localizations for damage types
        this.deathMessage(DamageTypesPM.BLEEDING).name("%1$s bled to death").player("%1$s bled to death whilst fighting %2$s").build();
        this.deathMessage(DamageTypesPM.BLOOD_ROSE).name("%1$s was pricked to death").player("%1$s walked into a blood rose whilst trying to escape %2$s").build();
        this.deathMessage(DamageTypesPM.HELLISH_CHAIN).name("%1$s was killed by %2$s").item("%1$s was killed by %2$s using %3$s").build();
        this.deathMessage(DamageTypesPM.SORCERY_EARTH).name("%1$s was killed by %2$s using sorcery").item("%1$s was killed by %2$s using %3$s").build();
        this.deathMessage(DamageTypesPM.SORCERY_SEA).name("%1$s was killed by %2$s using sorcery").item("%1$s was killed by %2$s using %3$s").build();
        this.deathMessage(DamageTypesPM.SORCERY_SKY).name("%1$s was killed by %2$s using sorcery").item("%1$s was killed by %2$s using %3$s").build();
        this.deathMessage(DamageTypesPM.SORCERY_SUN).name("%1$s was killed by %2$s using sorcery").item("%1$s was killed by %2$s using %3$s").build();
        this.deathMessage(DamageTypesPM.SORCERY_MOON).name("%1$s was killed by %2$s using sorcery").item("%1$s was killed by %2$s using %3$s").build();
        this.deathMessage(DamageTypesPM.SORCERY_BLOOD).name("%1$s was killed by %2$s using sorcery").item("%1$s was killed by %2$s using %3$s").build();
        this.deathMessage(DamageTypesPM.SORCERY_INFERNAL).name("%1$s was killed by %2$s using sorcery").item("%1$s was killed by %2$s using %3$s").build();
        this.deathMessage(DamageTypesPM.SORCERY_VOID).name("%1$s was killed by %2$s using sorcery").item("%1$s was killed by %2$s using %3$s").build();
        this.deathMessage(DamageTypesPM.SORCERY_HALLOWED).name("%1$s was killed by %2$s using sorcery").item("%1$s was killed by %2$s using %3$s").build();
        
        // Generate wand component localizations
        this.wandComponent(WandCore.HEARTWOOD).name("Heartwood").build();
        this.wandComponent(WandCore.OBSIDIAN).name("Obsidian").build();
        this.wandComponent(WandCore.CORAL).name("Coral").build();
        this.wandComponent(WandCore.BAMBOO).name("Bamboo").build();
        this.wandComponent(WandCore.SUNWOOD).name("Sunwood").build();
        this.wandComponent(WandCore.MOONWOOD).name("Moonwood").build();
        this.wandComponent(WandCore.BONE).name("Bone").build();
        this.wandComponent(WandCore.BLAZE_ROD).name("Blaze Rod").build();
        this.wandComponent(WandCore.PURPUR).name("Purpur").build();
        this.wandComponent(WandCore.PRIMAL).name("Primal").build();
        this.wandComponent(WandCore.DARK_PRIMAL).name("Dark Primal").build();
        this.wandComponent(WandCore.PURE_PRIMAL).name("Pure Primal").build();
        this.add("wand_core.primalmagick.unknown", "Unknown");
        this.wandComponent(WandCap.IRON).name("Iron-Shod").build();
        this.wandComponent(WandCap.GOLD).name("Gold-Capped").build();
        this.wandComponent(WandCap.PRIMALITE).name("Primalite-Bossed").build();
        this.wandComponent(WandCap.HEXIUM).name("Hexium-Bound").build();
        this.wandComponent(WandCap.HALLOWSTEEL).name("Hallowsteel-Crowned").build();
        this.add("wand_cap.primalmagick.unknown", "Unknown");
        this.wandComponent(WandGem.APPRENTICE).name("Apprentice's").build();
        this.wandComponent(WandGem.ADEPT).name("Adept's").build();
        this.wandComponent(WandGem.WIZARD).name("Wizard's").build();
        this.wandComponent(WandGem.ARCHMAGE).name("Archmage's").build();
        this.wandComponent(WandGem.CREATIVE).name("Creative").build();
        this.add("wand_gem.primalmagick.unknown", "Unknown");
        
        // Generate research discipline localizations
        this.researchDiscipline(ResearchDisciplines.BASICS).name("Fundamentals").build();
        this.researchDiscipline(ResearchDisciplines.ALCHEMY).name("Alchemy").build();
        this.researchDiscipline(ResearchDisciplines.SORCERY).name("Sorcery").build();
        this.researchDiscipline(ResearchDisciplines.MANAWEAVING).name("Manaweaving").build();
        this.researchDiscipline(ResearchDisciplines.RUNEWORKING).name("Runeworking").build();
        this.researchDiscipline(ResearchDisciplines.RITUAL).name("Ritual Magick").build();
        this.researchDiscipline(ResearchDisciplines.MAGITECH).name("Magitech").build();
        this.researchDiscipline(ResearchDisciplines.SCANS).name("Scans").build();
        
        // Generate knowledge type localizations
        this.knowledgeType(KnowledgeType.OBSERVATION).name("Observation").build();
        this.knowledgeType(KnowledgeType.THEORY).name("Theory").build();
        
        // Generate attunement type localizations
        this.attunementType(AttunementType.PERMANENT).name("Permanent").build();
        this.attunementType(AttunementType.INDUCED).name("Induced").build();
        this.attunementType(AttunementType.TEMPORARY).name("Temporary").build();
        
        // Generate attunement threshold localizations
        this.attunementThreshold(AttunementThreshold.MINOR).name("Minor")
            .effect(Source.EARTH, "5% Earth mana discount")
            .effect(Source.SEA, "5% Sea mana discount")
            .effect(Source.SKY, "5% Sky mana discount")
            .effect(Source.SUN, "5% Sun mana discount")
            .effect(Source.MOON, "5% Moon mana discount")
            .effect(Source.BLOOD, "5% Blood mana discount")
            .effect(Source.INFERNAL, "5% Infernal mana discount")
            .effect(Source.VOID, "5% Void mana discount")
            .effect(Source.HALLOWED, "5% Hallowed mana discount")
            .build();
        this.attunementThreshold(AttunementThreshold.LESSER).name("Lesser")
            .effect(Source.EARTH, "Increased attack/mining speed")
            .effect(Source.SEA, "Greatly increased swim speed")
            .effect(Source.SKY, "Increased movement speed, greatly reduced fall damage")
            .effect(Source.SUN, "Photosynthesis")
            .effect(Source.MOON, "Chance to become invisible on taking damage")
            .effect(Source.BLOOD, "Exsanguination")
            .effect(Source.INFERNAL, "Hellish chain")
            .effect(Source.VOID, "Reduced damage taken")
            .effect(Source.HALLOWED, "Smite undead")
            .build();
        this.attunementThreshold(AttunementThreshold.GREATER).name("Greater")
            .effect(Source.EARTH, "Step height increase")
            .effect(Source.SEA, "Underwater breathing")
            .effect(Source.SKY, "Increased jump height, double jump")
            .effect(Source.SUN, "Chance to create light in dark areas")
            .effect(Source.MOON, "Night vision")
            .effect(Source.BLOOD, "Lifesteal")
            .effect(Source.INFERNAL, "Fire immunity")
            .effect(Source.VOID, "Increased damage dealt")
            .effect(Source.HALLOWED, "Prevent death when fatally wounded")
            .build();
        
        // Generate command output localizations
        this.command("research").sub("list").output("All research for %1$s: %2$s").end().build();
        this.command("research").sub("reset").output("Resetting all research for %1$s").end().build();
        this.command("research").sub("reset").sub("target").output("%1$s has reset all of your research").end().build();
        this.command("research").sub("grant").output("Granting research to %1$s: %2$s").end().build();
        this.command("research").sub("grant").sub("target").output("%1$s has granted you %2$s research and its parents").end().build();
        this.command("research").sub("grant_parents").output("Granting parents of research to %1$s: %2$s").end().build();
        this.command("research").sub("grant_parents").sub("target").output("%1$s has granted you the parents of %2$s research").end().build();
        this.command("research").sub("grant_all").output("Granting all research to %1$s").end().build();
        this.command("research").sub("grant_all").sub("target").output("%1$s has granted you all research").end().build();
        this.command("research").sub("revoke").output("Revoking research from %1$s: %2$s").end().build();
        this.command("research").sub("revoke").sub("target").output("%1$s has revoked your %2$s research and its children").end().build();
        this.command("research").sub("details").output("Research %1$s of player %2$s:", "  Status: %1$s", "  Current stage: %1$d", "  Flags: %1$s").end().build();
        this.command("research").sub("progress").sub("success").output("Research %1$s for %2$s progressed from stage %3$d to stage %4$d").end().build();
        this.command("research").sub("progress").sub("failure").output("Failed to progress research %1$s from stage %2$d").end().build();
        this.command("research").sub("progress").sub("target").output("%2$s has progressed your %1$s research from stage %3$d to stage %4$d").end().build();
        this.command("knowledge").sub("reset").output("Resetting all knowledge for %1$s").end().build();
        this.command("knowledge").sub("reset").sub("target").output("%1$s has reset all your knowledge").end().build();
        this.command("knowledge").sub("get").output("%1$s has %2$d levels of %3$s (%4$d points)").end().build();
        this.command("knowledge").sub("add").sub("success").output("Added %1$d points of %2$s knowledge to %3$s").end().build();
        this.command("knowledge").sub("add").sub("failure").output("Failed to add knowledge to %1$s").end().build();
        this.command("knowledge").sub("add").sub("target").output("%3$s has granted you %1$d points of %2$s knowledge").end().build();
        this.command("scans").sub("grant").sub("success").output("Granting scan research to %1$s: %2$s").end().build();
        this.command("scans").sub("grant").sub("failure").output("Failed to grant scan research to %1$s").end().build();
        this.command("scans").sub("grant").sub("target").output("%1$s granted you %2$s scan research").end().build();
        this.command("scans").sub("grant_all").output("Granting %1$d scan research entries to %2$s").end().build();
        this.command("scans").sub("grant_all").sub("target").output("%2$s granted you %1$d scan research entries").end().build();
        this.command("sources").sub("list").output("%1$s has unlocked sources: %2$s").end().build();
        this.command("sources").sub("unlock_all").output("Unlocked %2$d sources for %1$s").end().build();
        this.command("sources").sub("unlock_all").sub("target").output("%1$s has unlocked %2$d sources for you").end().build();
        this.command("sources").sub("unlock").sub("success").output("Unlocked source %2$s for %1$s").end().build();
        this.command("sources").sub("unlock").sub("failure").output("Failed to unlock source %2$s for %1$s").end().build();
        this.command("sources").sub("unlock").sub("target").output("%1$s has unlocked source %2$s for you").end().build();
        this.command("sources").sub("unlock").sub("already_unlocked").output("%1$s has already unlocked source %2$s").end().build();
        this.command("stats").sub("reset").output("Resetting all statistics for %1$s").end().build();
        this.command("stats").sub("reset").sub("target").output("%1$s has reset all your statistics").end().build();
        this.command("stats").sub("get").output("%1$s - %2$s: %3$s").end().build();
        this.command("stats").sub("set").output("Set %2$s statistic for %1$s to %3$s").end().build();
        this.command("stats").sub("set").sub("target").output("%1$s has set your %2$s statistic to %3$s").end().build();
        this.command("attunements").sub("reset").output("Resetting all attunements for %1$s").end().build();
        this.command("attunements").sub("reset").sub("target").output("%1$s has reset all your attunements").end().build();
        this.command("attunements").sub("get").sub("total").output("%1$s attunement of player %2$s: total %3$d").end().build();
        this.command("attunements").sub("get").sub("partial").output("  %1$s: %2$d").end().build();
        this.command("attunements").sub("set").sub("success").output("Set %2$s %3$s attunement for %1$s to %4$d").end().build();
        this.command("attunements").sub("set").sub("success").sub("capped").output("Set %2$s %3$s attunement for %1$s to %4$d (capped from %5$d)").end().build();
        this.command("attunements").sub("set").sub("target").output("%1$s has set your %2$s %3$s attunement to %4$d").end().build();
        this.command("attunements").sub("set").sub("target").sub("capped").output("%1$s has set your %2$s %3$s attunement to %4$d (capped from %5$d)").end().build();
        this.command("recipes").sub("reset").output("Resetting arcane recipe book for %1$s").end().build();
        this.command("recipes").sub("reset").sub("target").output("%1$s has reset your arcane recipe book").end().build();
        this.command("recipes").sub("list").sub("known").output("All known arcane recipes for %1$s: %2$s").end().build();
        this.command("recipes").sub("list").sub("highlight").output("All highlighted arcane recipes for %1$s: %2$s").end().build();
        this.command("recipes").sub("sync").output("Syncing arcane recipe book for %1$s").end().build();
        this.command("recipes").sub("sync").sub("target").output("%1$s has synced your arcane recipe book with your research").end().build();
        this.command("recipes").sub("recipe_not_arcane").output("Given recipe does not go in the arcane recipe book").end().build();
        this.command("recipes").sub("add").output("Added recipe to arcane recipe book of %1$s: %2$s").end().build();
        this.command("recipes").sub("add").sub("target").output("%1$s has added %2$s to your arcane recipe book").end().build();
        this.command("recipes").sub("remove").output("Removed recipe from arcane recipe book of %1$s: %2$s").end().build();
        this.command("recipes").sub("remove").sub("target").output("%1$s has removed %2$s from your arcane recipe book").end().build();
        this.command("research").sub("noexist").output("Research %1$s does not exist").end().build();
        this.command("discipline").sub("noexist").output("Discipline %1$s does not exist").end().build();
        this.command("knowledge_type").sub("noexist").output("Knowledge type does not exist").end().build();
        this.command("source").sub("noexist").output("Source %1$s does not exist").end().build();
        this.command("stats").sub("noexist").output("Statistic %1$s does not exist").end().build();
        this.command("attunement_type").sub("noexist").output("Attunement type does not exist").end().build();
        this.command("books").sub("noexist").output("Book type does not exist").end().build();
        this.command("books").sub("nolanguage").output("Book language does not exist").end().build();
        this.command("linguistics").sub("reset").output("Resetting all linguistics knowledge for %1$s").end().build();
        this.command("linguistics").sub("reset").sub("target").output("%1$s has reset all your linguistics knowledge").end().build();
        this.command("linguistics").sub("comprehension").sub("get").output("%1$s has %3$d comprehension for language %2$s").end().build();
        this.command("linguistics").sub("comprehension").sub("set").sub("success").output("Set %2$s language comprehension for %1$s to %3$d").end().build();
        this.command("linguistics").sub("comprehension").sub("set").sub("target").output("%1$s has set your %2$s language comprehension to %3$d").end().build();
        this.command("linguistics").sub("comprehension").sub("set").sub("success").sub("capped").output("Set %2$s language comprehension for %1$s to %3$d (capped from %4$d)").end().build();
        this.command("linguistics").sub("comprehension").sub("set").sub("target").sub("capped").output("%1$s has set your %2$s language comprehension to %3$d (capped from %4$d)").end().build();
        this.command("affinities").sub("explain").sub("from_recipe").output("Affinities for item %1$s are derived from recipe %2$s").end().build();
        this.command("affinities").sub("explain").sub("from_data").output("Affinities for item %1$s are explicitly set").end().build();
        this.command("affinities").sub("explain").sub("not_found").output("Affinity data not found for item %1$s").end().build();
        this.command("error").name("Error executing command").build();
        
        // Generate event output localizations
        this.event("add_addendum").name("Addendum added to %1$s").build();
        this.event("scan").sub("success").output("You have gained valuable research data").end().build();
        this.event("scan").sub("fail").output("You are unable to scan that").end().build();
        this.event("scan").sub("repeat").output("You learn nothing new from scanning that").end().build();
        this.event("scan").sub("toobig").output("There are too many items there to scan completely").end().build();
        this.event("found_shrine").name("A peculiar energy fills the air in this shrine, and you're not sure what to make of it. Perhaps some sleep will grant inspiration.").build();
        this.event("got_dream").name("You awaken with a start from a particularly vivid dream. You manage to write it down before it fades from your memory.").build();
        this.event("siphon_prompt").name("It feels like your wand is tugging you toward the strange device in the center of the shrine. What if you use the wand on it?").build();
        this.event("env_earth").name("You have visited the dark depths of the earth and feel enlightened.").build();
        this.event("env_sea").name("You have beheld the vast blue of the ocean and feel enlightened.").build();
        this.event("env_sky").name("You have traveled to the peak of the world and feel enlightened.").build();
        this.event("env_sun").name("You have felt the scorching rays of the desert sun and feel enlightened.").build();
        this.event("env_moon").name("You have seen the moonlight filtered through the forest trees and feel enlightened.").build();
        this.event("discover_source").sub("blood").output("Your blood sings with power as warmth spreads through you. You believe this may be a new primal source!").end().build();
        this.event("discover_source").sub("blood").sub("alternate").output("Your blood sings with power as you absorb the knowledge within. You believe this may be a new primal source!").end().build();
        this.event("discover_source").sub("infernal").output("The fiery aura of this place seethes with magick. You believe this may be a new primal source!").end().build();
        this.event("discover_source").sub("void").output("The emptiness of this place nonetheless teems with magick. You believe this may be a new primal source!").end().build();
        this.event("discover_source").sub("hallowed").output("This object seems to radiate a magick of harmony. You believe this may be a new primal source!").end().build();
        this.event("cycle_spell").name("Active spell changed to: %1$s").build();
        this.event("cycle_spell").sub("none").output("Active spell changed to: none").end().build();
        this.event("golem").sub("stay").output("I will stay here, Master").end().build();
        this.event("golem").sub("follow").output("I will follow you, Master").end().build();
        this.event("ambrosia").sub("success").output("You feel a tingling sensation as the magick courses through you").end().build();
        this.event("ambrosia").sub("failure").output("This item cannot help you attune further").end().build();
        this.event("ambrosia").sub("not_wizard").output("This stuff tastes funny. What is it?").end().build();
        this.event("dream_vision_talisman").sub("drained").output("You awake from a vision-filled dream and record your newfound observations.").end().build();
        this.event("dream_vision_talisman").sub("break").output("Your Dream Vision Talisman crumbles to dust as you wake.").end().build();
        this.event("dream_vision_talisman").sub("set_active").output("The talisman will now absorb experience.").end().build();
        this.event("dream_vision_talisman").sub("set_inactive").output("The talisman will no longer absorb experience.").end().build();
        this.event("wand_transform_hint").name("That seemed to do something, but apparently it wasn't enough. Try waving your wand for longer!").build();
        this.event("knowledge_item").sub("success").output("You have gained valuable arcane knowledge").end().build();
        this.event("knowledge_item").sub("failure").output("You can't make heads or tails of this").end().build();
        this.event("knowledge_item").sub("already_known").output("You already know everything this has to teach").end().build();
        this.event("dowsing_rod").sub("altar_stability").sub("very_good").output("Altar layout stability: very good").end().build();
        this.event("dowsing_rod").sub("altar_stability").sub("good").output("Altar layout stability: good").end().build();
        this.event("dowsing_rod").sub("altar_stability").sub("neutral").output("Altar layout stability: neutral").end().build();
        this.event("dowsing_rod").sub("altar_stability").sub("poor").output("Altar layout stability: poor").end().build();
        this.event("dowsing_rod").sub("altar_stability").sub("very_poor").output("Altar layout stability: very poor").end().build();
        this.event("dowsing_rod").sub("salt_connection").sub("active").output("Salt connection active!").end().build();
        this.event("dowsing_rod").sub("salt_connection").sub("inactive").output("No salt connection found").end().build();
        this.event("dowsing_rod").sub("symmetry").sub("found").output("Symmetric match found!").end().build();
        this.event("dowsing_rod").sub("symmetry").sub("not_found").output("No symmetric match found").end().build();
        this.event("dowsing_rod").sub("symmetry").sub("marking_pos").output("Marking position where symmetric match is expected").end().build();
        this.event("friendly_witch").sub("spawn").output("%1$s says, \"BAH! Fine! Whaddaya want?\"").end().build();
        this.event("research").sub("gain").output("You have learned a new arcane secret! Check your Grimoire!").end().build();
        this.event("attunement_item").sub("success").output("You feel a tingling sensation as the magick courses through you").end().build();
        this.event("attunement_item").sub("failure").output("This thing makes your fingers tingle. What is it?").end().build();
        this.event("recall_stone").sub("failure").output("No respawn location found").end().build();
        this.event("recall_stone").sub("cannot_cross_dimensions").output("The stone cannot take you across dimensional boundaries").end().build();
        this.event("analysis_table").sub("forbidden").output("This item is too precious to destroy in the Analysis Table; you must find another way to analyze it.").end().build();
        this.event("runic_grindstone").sub("hints_granted").output("You have gleaned a fragment of runic knowledge.").end().build();
        this.event("attunement").sub("threshold_gain").output("You have gained %2$s attunement to the %1$s").end().build();
        this.event("attunement").sub("threshold_loss").output("You have lost %2$s attunement to the %1$s").end().build();
        this.event("attunement").sub("suppression_gain").output("Your attunement to the %1$s has been suppressed").end().build();
        this.event("attunement").sub("suppression_loss").output("Your attunement to the %1$s has been restored").end().build();
        this.event("linguistics_item").sub("success").output("Your linguistics knowledge has improved").end().build();
        this.event("linguistics_item").sub("fluent").output("You are already fluent in this language").end().build();
        this.event("enderward").sub("block").output("An unseen force blocks your teleportation").end().build();
        
        this.tip("thanks").name("Thanks for playing Primal Magick! You rock!").build();
        this.tip("discord").name("Looking to connect with fellow Primal Magick players? Join our Discord! You can find an invite link on either CurseForge or Modrinth.").build();
        this.tip("more_tips").name("More tips will unlock as you progress through your arcane research, so keep checking back.").build();
        this.tip("earth_shrine_loc").name("Earth shrines can be found in flatlands, such as plains and savannas.").build();
        this.tip("sea_shrine_loc").name("Sea shrines can be found in wet or cold regions, such as beaches, swamps, or snowfields.").build();
        this.tip("sky_shrine_loc").name("Sky shrines can be found in high places, such as mountains or extreme hills.").build();
        this.tip("sun_shrine_loc").name("Sun shrines can be found in hot places, such as deserts or badlands.").build();
        this.tip("moon_shrine_loc").name("Moon shrines can be found aboveground in dimly lit biomes, primarily forests.").build();
        this.tip("no_blood_shrines").name("No known shrines exist for the Blood source of magick, so you'll have to acquire its mana through other means.").build();
        this.tip("no_infernal_shrines").name("No known shrines exist for the Infernal source of magick, so you'll have to acquire its mana through other means.").build();
        this.tip("no_void_shrines").name("No known shrines exist for the Void source of magick, so you'll have to acquire its mana through other means.").build();
        this.tip("no_hallowed_shrines").name("No known shrines exist for the Hallowed source of magick, so you'll have to acquire its mana through other means.").build();
        this.tip("go_explore").name("Exploring beyond the Overworld could be the key to unlocking new kinds of magick.").build();
        this.tip("new_disciplines").name("Continuing your arcane studies may cause you to unlock whole new disciplines of magick. Check back in your Fundamentals from time to time.").build();
        this.tip("salt").name("Refined salt can be used to make your food tastier and more filling. Keep an eye out for it while you're mining!").build();
        this.tip("treefolk").name("Strange treefolk have been sighted in the Overworld's forests. They're not aggressive, and will barter various plants in exchange for bone meal.").build();
        this.tip("view_affinities").name("You can view the magickal affinities of any analyzed item in your inventory with the View Affinities key. By default, this is Left Shift, but you can re-map that in Options.").build();
        this.tip("analysis").name("To learn which magickal affinities a block or item possesses, you must analyze it at an Analysis Table.").build();
        this.tip("no_affinities").name("As a practicing mage, you can tell at a glance if an item has no magickal affinity whatsoever. No need to feed it to an Analysis Table.").build();
        this.tip("research_table").name("The Research Table is a mage's bread and butter. You need to use it to develop Theories, which are essential for delving deeper into the secrets of magick.").build();
        this.tip("project_success").name("Your chance of success for a research project is listed on the Complete Project button at the Research Table. You can improve it be devoting materials to the project.").build();
        this.tip("research_boldness").name("If you don't have all the materials to get your research project success chance to 100%%, don't be afraid to try anyway! If you fail, you'll only lose a sheet of paper and some ink.").build();
        this.tip("research_aids").name("Certain blocks, when placed near your Research Table, will add special, more lucrative research projects to your available pool. Consider a Bookshelf as a good start.").build();
        this.tip("permanent_attunement").name("Completing certain research entries in your Grimoire will earn you permanent attunement to magick, granting you passive bonuses. Check the Attunements section of your Grimoire to learn more.").build();
        this.tip("temporary_attunement").name("Spending mana will earn you temporary attunement to magick, granting you passive bonuses for a short time. Check the Attunements section of your Grimoire to learn more.").build();
        this.tip("better_analysis").name("You'll need to improve your available analysis tools before you can analyze living creatures. Try studying your Magitech.").build();
        this.tip("spending_mana").name("A mage spends mana either by crafting items with it, such as at an Arcane Workbench, or by casting spells.").build();
        this.tip("sotu").name("The secrets of the universe will take a great deal of work to uncover, and are not for the faint of heart. Check the Fundamentals section of your Grimoire to learn what is required.").build();
        this.tip("mana_charger").name("The Mana Charger lets you convert magickal essence, such as dust, into mana for your wand. More potent types of essence will yield more mana.").build();
        this.tip("staves").name("Magickal staves can be inscribed with more spells than a regular wand, but are too large to fit in most crafting stations.").build();
        this.tip("robes").name("Robes aren't as protective as normal armor, but they do offer you a discount when spending mana.").build();
        this.tip("ritual_symmetry").name("Symmetry is very important when setting up your ritual layout. Try using a Dowsing Rod if you're having trouble with it.").build();
        this.tip("induced_attunement").name("Eating Ambrosia will earn you induced attunement to magick, granting you passive bonuses. But every time you gain induced attunement for one source, you lose a bit for all the others.").build();
        this.tip("ambrosia_cap").name("Ambrosia has a cap on how much induced attunement it can grant you. More powerful types of Ambrosia have higher caps.").build();
        this.tip("power_runes").name("Runescribing only produces enchantments of level one by default, but there are special runes, such as Insight Runes, that can improve that.").build();
        this.tip("rune_hints").name("Looking for hints as to valid rune combinations? Try feeding enchanted items to a Runic Grindstone!").build();
        
        // Generate written language localizations
        this.language(BookLanguagesPM.DEFAULT).name("Modern Minecraftian").build();
        this.language(BookLanguagesPM.GALACTIC).name("Standard Galactic").build();
        this.language(BookLanguagesPM.ILLAGER).name("Illager").build();
        this.language(BookLanguagesPM.EARTH).name("Ancient Terralinear").build();
        this.language(BookLanguagesPM.SEA).name("Ancient Hydroglyphic").build();
        this.language(BookLanguagesPM.SKY).name("Ancient Aeroform").build();
        this.language(BookLanguagesPM.SUN).name("Ancient Sunskrit").build();
        this.language(BookLanguagesPM.MOON).name("Ancient Lunarese").build();
        this.language(BookLanguagesPM.TRADE).name("Ancient Trade Jargon").build();
        this.language(BookLanguagesPM.FORBIDDEN).name("Ancient Cultist Cant").build();
        this.language(BookLanguagesPM.HALLOWED).name("Ancient High Speech").build();

        // Generate written book localizations
        this.book(BooksPM.TEST_BOOK).name("Test Book").author("Steve")
            .foreword("Test foreword")
            .text("Sphinx of black quartz, judge my vow! 1234567890.")
            .afterword("Test afterword")
            .build();
        this.book(BooksPM.DREAM_JOURNAL).name("Dream Journal").author("Unknown")
            .text("I dreamed of the shrine last night. The same strange energy still permeated the air, but this time I knew the word for it.\n\nMagick.\n\nAs if the word unlocked something in my mind, I knew what to do. In the dream, I dug beneath the base of the shrine and found stone laced with a curious dust. Sensing more magick within it, I took a handful of the dust and rubbed it onto an ordinary stick.\n\nSo imbued, the stick became something more. In the dream, I took it and waved it at a bookcase. The dream ended before I could see what resulted, but I feel like it would have been something wondrous.\n\nI feel like this could be a key to something amazing, if I just have the courage to take that first step.")
            .build();
        this.book(BooksPM.SOURCE_PRIMER).name("Source Primer").author("Unknown")
            .foreword("This book is printed very simply, as if intended for young children.")
            .text("The Earth abides.\nThe Sea flows.\nThe Sky drifts.\nThe Sun shines.\nThe Moon changes.\nThe Blood pumps.\nThe Infernal rages.\nThe Void hungers.\nThe Hallowed sings.")
            .build();
        
        // Generate spell vehicle localizations
        this.add("spells.primalmagick.vehicle.header", "Spell Type");
        this.spellVehicle(EmptySpellVehicle.TYPE).name("None").defaultName("").build();
        this.spellVehicle(TouchSpellVehicle.TYPE).name("Touch").defaultName("Hand").build();
        this.spellVehicle(ProjectileSpellVehicle.TYPE).name("Projectile").defaultName("Missile").build();
        this.spellVehicle(BoltSpellVehicle.TYPE).name("Bolt").defaultName("Bolt").detailTooltip("Bolt (%1$d blocks)").build();
        this.spellVehicle(SelfSpellVehicle.TYPE).name("Self").defaultName("Self").build();
        
        // Generate spell payload localizations
        this.add("spells.primalmagick.payload.header", "Spell Payload");
        this.spellPayload(EmptySpellPayload.TYPE).name("None").defaultName("").build();
        this.spellPayload(EarthDamageSpellPayload.TYPE).name("Earth Damage").defaultName("Earthen").detailTooltip("Earth Damage (%1$s damage)").build();
        this.spellPayload(FrostDamageSpellPayload.TYPE).name("Frost Damage").defaultName("Frost").detailTooltip("Frost Damage (%1$s damage, %2$s sec)").build();
        this.spellPayload(LightningDamageSpellPayload.TYPE).name("Lightning Damage").defaultName("Lightning").detailTooltip("Lightning Damage (%1$s damage)").build();
        this.spellPayload(SolarDamageSpellPayload.TYPE).name("Solar Damage").defaultName("Solar").detailTooltip("Solar Damage (%1$s damage, %2$s sec)").build();
        this.spellPayload(LunarDamageSpellPayload.TYPE).name("Lunar Damage").defaultName("Lunar").detailTooltip("Lunar Damage (%1$s damage, %2$s sec)").build();
        this.spellPayload(BloodDamageSpellPayload.TYPE).name("Blood Damage").defaultName("Blood").detailTooltip("Blood Damage (%1$s damage)").build();
        this.spellPayload(FlameDamageSpellPayload.TYPE).name("Flame Damage").defaultName("Flame").detailTooltip("Flame Damage (%1$s damage, %2$s sec)").build();
        this.spellPayload(VoidDamageSpellPayload.TYPE).name("Void Damage").defaultName("Void").detailTooltip("Void Damage (%1$s damage, %2$s sec)").build();
        this.spellPayload(HolyDamageSpellPayload.TYPE).name("Holy Damage").defaultName("Holy").detailTooltip("Holy Damage (%1$s damage)").build();
        this.spellPayload(BreakSpellPayload.TYPE).name("Break").defaultName("Shattering").build();
        this.spellPayload(ConjureStoneSpellPayload.TYPE).name("Conjure Stone").defaultName("Stone").build();
        this.spellPayload(ConjureWaterSpellPayload.TYPE).name("Conjure Water").defaultName("Water").build();
        this.spellPayload(ShearSpellPayload.TYPE).name("Shear").defaultName("Shearing").build();
        this.spellPayload(FlightSpellPayload.TYPE).name("Flight").defaultName("Flight").detailTooltip("Flight (%1$s sec)").build();
        this.spellPayload(ConjureLightSpellPayload.TYPE).name("Conjure Light").defaultName("Illuminating").build();
        this.spellPayload(HealingSpellPayload.TYPE).name("Healing").defaultName("Healing").detailTooltip("Healing (%1$s health)").build();
        this.spellPayload(PolymorphWolfSpellPayload.TYPE).name("Polymorph").defaultName("Morphing").detailTooltip("Polymorph (%1$s sec)").build();
        this.spellPayload(PolymorphSheepSpellPayload.TYPE).name("Polymorph: Sheep").defaultName("Sheeping").detailTooltip("Polymorph (%1$s sec)").build();
        this.spellPayload(ConjureAnimalSpellPayload.TYPE).name("Conjure Animal").defaultName("Beast").build();
        this.spellPayload(ConjureLavaSpellPayload.TYPE).name("Conjure Lava").defaultName("Lava").build();
        this.spellPayload(DrainSoulSpellPayload.TYPE).name("Drain Soul").defaultName("Soul-Draining").detailTooltip("Drain Soul (%1$s sec)").build();
        this.spellPayload(TeleportSpellPayload.TYPE).name("Teleport").defaultName("Teleport").build();
        this.spellPayload(ConsecrateSpellPayload.TYPE).name("Consecrate").defaultName("Consecration").build();
        
        // Generate spell mod localizations
        this.add("spells.primalmagick.primary_mod.header", "Primary Mod");
        this.add("spells.primalmagick.secondary_mod.header", "Secondary Mod");
        this.spellMod(EmptySpellMod.TYPE).name("None").defaultName("").build();
        this.spellMod(AmplifySpellMod.TYPE).name("Amplify").defaultName("Amplified").build();
        this.spellMod(BurstSpellMod.TYPE).name("Burst").defaultName("Bursting").detailTooltip("Burst (%1$d blocks, %2$d penetration)").build();
        this.spellMod(QuickenSpellMod.TYPE).name("Quicken").defaultName("Quickened").build();
        this.spellMod(MineSpellMod.TYPE).name("Mine").defaultName("Delayed").detailTooltip("Mine (%1$d minutes)").build();
        this.spellMod(ForkSpellMod.TYPE).name("Fork").defaultName("Forked").detailTooltip("Fork (%1$d copies, %2$s spread)").build();

        // Generate spell property localizations
        this.spellProperty("power").name("Power").build();
        this.spellProperty("duration").name("Duration").build();
        this.spellProperty("haste").name("Haste").build();
        this.spellProperty("radius").name("Radius").build();
        this.spellProperty("range").name("Range").build();
        this.spellProperty("forks").name("Forks").build();
        this.spellProperty("precision").name("Precision").build();
        this.spellProperty("silk_touch").name("Silk Touch").build();

        // Generate other spell localizations
        this.add("spells.primalmagick.default_name_format.mods.0", "%2$s %1$s");
        this.add("spells.primalmagick.default_name_format.mods.1", "%3$s %2$s %1$s");
        this.add("spells.primalmagick.default_name_format.mods.2", "%4$s %3$s %2$s %1$s");
        
        // Generate grimoire GUI localizations
        this.grimoire("index_header").name("Disciplines").build();
        this.grimoire("other_header").name("Other Topics").build();
        this.grimoire("stats_header").name("Statistics").build();
        this.grimoire("attunement_header").name("Attunements").build();
        this.grimoire("rune_enchantment_header").name("Rune Enchantments").build();
        this.grimoire("recipe_index_header").name("Recipes").build();
        this.grimoire("tips_header").name("Tips").build();
        this.grimoire("attunement_gain_header").name("Attunements Gained").build();
        this.grimoire("addendum_header").name("Addendum %1$d").build();
        this.grimoire("requirements_header").name("Requirements").build();
        this.grimoire("shapeless_arcane_recipe_header").name("Arcane Workbench recipe (Shapeless)").build();
        this.grimoire("shaped_arcane_recipe_header").name("Arcane Workbench recipe").build();
        this.grimoire("shapeless_recipe_header").name("Workbench recipe (Shapeless)").build();
        this.grimoire("shaped_recipe_header").name("Workbench recipe").build();
        this.grimoire("ritual_recipe_header").name("Ritual Altar recipe").build();
        this.grimoire("runecarving_recipe_header").name("Runecarving Table recipe").build();
        this.grimoire("concocting_recipe_header").name("Concocter recipe (Shapeless)").build();
        this.grimoire("smelting_recipe_header").name("Smelting recipe").build();
        this.grimoire("dissolution_recipe_header").name("Dissolution recipe").build();
        this.grimoire("must_obtain_header").name("Will be consumed:").build();
        this.grimoire("must_craft_header").name("Must craft:").build();
        this.grimoire("required_knowledge_header").name("Required knowledge:").build();
        this.grimoire("required_research_header").name("Must be discovered:").build();
        this.grimoire("ritual_offerings_header").name("Offerings:").build();
        this.grimoire("ritual_props_header").name("Props:").build();
        this.grimoire("complete_button").name("Complete").build();
        this.grimoire("completing_text").name("Completing...").build();
        this.grimoire("next_tip_button").name("Next Tip").build();
        this.grimoire("section_header").sub("new").output("New").end().build();
        this.grimoire("section_header").sub("updated").output("Updated").end().build();
        this.grimoire("section_header").sub("complete").output("Complete").end().build();
        this.grimoire("section_header").sub("in_progress").output("In Progress").end().build();
        this.grimoire("section_header").sub("available").output("Available").end().build();
        this.grimoire("section_header").sub("upcoming").output("Upcoming").end().build();
        this.grimoire("section_header").sub("unavailable").output("Unavailable").end().build();
        this.grimoire("missing_block").name("Missing block!").build();
        this.grimoire("upcoming_tooltip_header").name("Missing requirements:").build();
        this.grimoire("recipe_metadata").sub("discipline").output("Discipline:").end().build();
        this.grimoire("recipe_metadata").sub("entry").output("Research entry:").end().build();
        this.grimoire("attunement_meter").sub("tooltip").sub("header").output("%1$s attunement:").end().build();
        this.grimoire("attunement_meter").sub("tooltip").sub("permanent").output("Permanent: %1$d").end().build();
        this.grimoire("attunement_meter").sub("tooltip").sub("induced").output("Induced: %1$d").end().build();
        this.grimoire("attunement_meter").sub("tooltip").sub("temporary").output("Temporary: %1$d").end().build();
        
        // Generate JEI GUI localizations
        this.jei("ritual").sub("offerings").sub("header").output("Offerings").end().build();
        this.jei("ritual").sub("props").sub("header").output("Props").end().build();
        
        // Generate ritual feedback localizations
        this.ritual("instability").name("%1$s: %2$s").build();
        this.ritual("instability").sub("header").output("Instability").end().build();
        this.ritual("instability").sub("rating").output(0, "Trivial", "Low", "Moderate", "High", "Very High", "Extreme").end().build();
        this.ritual("prop").sub("ritual_candle").output("The ritual requires you to light a candle now!").end().build();
        this.ritual("prop").sub("incense_brazier").output("The ritual requires you to burn some incense now!").end().build();
        this.ritual("prop").sub("ritual_lectern").output("The ritual requires you to place an enchanted book now!").end().build();
        this.ritual("prop").sub("ritual_bell").output("The ritual requires you to ring a bell now!").end().build();
        this.ritual("prop").sub("bloodletter").output("The ritual requires you to shed your blood now!").end().build();
        this.ritual("prop").sub("soul_anvil").output("The ritual requires you to smash a soul gem now!").end().build();
        this.ritual("prop").sub("celestial_harp").output("The ritual requires you to play a celestial harp now!").end().build();
        this.ritual("prop").sub("entropy_sink").output("The ritual requires you to feed essence to an entropy sink now!").end().build();
        this.ritual("warning").sub("missing_offering").output("The ritual cannot find the required %1$s offering!").end().build();
        this.ritual("warning").sub("missing_offering").sub("empty").output("The ritual cannot find a required offering!").end().build();
        this.ritual("warning").sub("missing_prop").output("The ritual cannot find the required %1$s prop!").end().build();
        this.ritual("warning").sub("missing_prop").sub("empty").output("The ritual cannot find a required prop!").end().build();
        this.ritual("warning").sub("channel_interrupt").output("The channeling of the required offering %1$s has been interrupted!").end().build();
        this.ritual("warning").sub("channel_interrupt").sub("empty").output("The channeling of a required offering has been interrupted!").end().build();
        this.ritual("warning").sub("prop_interrupt").output("The activation of the required prop %1$s has been interrupted!").end().build();
        this.ritual("warning").sub("prop_interrupt").sub("empty").output("The activation of a required prop has been interrupted!").end().build();
        this.ritual("warning").sub("obstructed").output("The area above the ritual altar is obstructed").end().build();
        this.ritual("info").sub("started").output("Ritual started!").end().build();
        this.ritual("info").sub("complete").output("Ritual complete!").end().build();
        this.ritual("info").sub("canceled").output("Ritual canceled").end().build();
        
        // Generate concoction localizations
        this.concoction("doses_remaining").name("Doses: %1$d").build();
        this.concoction("charges_remaining").name("Charges: %1$d").build();
        this.concoction("fuse_length").name("Fuse: %1$s").build();
        this.concoction("fuse_set").name("Bomb fuse set to %1$s").build();
        this.concoction("fuse").sub("impact").output("Impact").end().build();
        this.concoction("fuse").sub("short").output("1s").end().build();
        this.concoction("fuse").sub("medium").output("3s").end().build();
        this.concoction("fuse").sub("long").output("5s").end().build();
        
        // Generate keybind localizations
        this.add(KeyBindings.KEY_CATEGORY, "Primal Magick");
        this.keyMapping(KeyBindings.CHANGE_SPELL_KEY, "change_spell").name("Change Active Spell").build();
        this.keyMapping(KeyBindings.CARPET_FORWARD_KEY, "carpet_forward").name("Flying Carpet Forward").build();
        this.keyMapping(KeyBindings.CARPET_BACKWARD_KEY, "carpet_backward").name("Flying Carpet Backward").build();
        this.keyMapping(KeyBindings.GRIMOIRE_NEXT_PAGE, "grimoire_next_page").name("Grimoire Next Page").build();
        this.keyMapping(KeyBindings.GRIMOIRE_PREV_PAGE, "grimoire_prev_page").name("Grimoire Previous Page").build();
        this.keyMapping(KeyBindings.GRIMOIRE_PREV_TOPIC, "grimoire_prev_topic").name("Grimoire Previous Topic").build();
        this.keyMapping(KeyBindings.VIEW_AFFINITY_KEY, "view_affinity").name("View Item Affinities").build();
        
        // Generate statistics localizations
        this.stat(StatsPM.GRIMOIRE_READ).name("Times Grimoire consulted").build();
        this.stat(StatsPM.ITEMS_ANALYZED).name("Item types analyzed").build();
        this.stat(StatsPM.ENTITIES_ANALYZED).name("Creature types analyzed").build();
        this.stat(StatsPM.MANA_SIPHONED).name("Mana siphoned from fonts").build();
        this.stat(StatsPM.OBSERVATIONS_MADE).name("Observations made").build();
        this.stat(StatsPM.THEORIES_FORMED).name("Theories formed").build();
        this.stat(StatsPM.CRAFTED_MANAWEAVING).name("Manaweaving items crafted").build();
        this.stat(StatsPM.CRAFTED_ALCHEMY).name("Alchemy items crafted").build();
        this.stat(StatsPM.CRAFTED_SORCERY).name("Sorcery items crafted").build();
        this.stat(StatsPM.CRAFTED_RUNEWORKING).name("Runeworking items crafted").build();
        this.stat(StatsPM.CRAFTED_RITUAL).name("Ritual Magick items crafted").build();
        this.stat(StatsPM.CRAFTED_MAGITECH).name("Magitech items crafted").build();
        this.stat(StatsPM.SPELLS_CAST).name("Spells cast").build();
        this.stat(StatsPM.SPELLS_CRAFTED).name("Spells crafted").build();
        this.stat(StatsPM.SPELLS_CRAFTED_MAX_COST).name("Cost of most expensive spell crafted").build();
        this.stat(StatsPM.RESEARCH_PROJECTS_COMPLETED).name("Research projects completed").build();
        this.stat(StatsPM.ITEMS_RUNESCRIBED).name("Items runescribed").build();
        this.stat(StatsPM.RITUALS_COMPLETED).name("Rituals conducted").build();
        this.stat(StatsPM.RITUAL_MISHAPS).name("Ritual mishaps endured").build();
        this.stat(StatsPM.CONCOCTIONS_USED).name("Concoctions used").build();
        this.stat(StatsPM.DISTANCE_TELEPORTED_CM).name("Distance teleported").build();
        this.stat(StatsPM.MANA_SPENT_TOTAL).name("Total mana spent").build();
        this.stat(StatsPM.MANA_SPENT_EARTH).name("Earth mana spent").build();
        this.stat(StatsPM.MANA_SPENT_SEA).name("Sea mana spent").build();
        this.stat(StatsPM.MANA_SPENT_SKY).name("Sky mana spent").build();
        this.stat(StatsPM.MANA_SPENT_SUN).name("Sun mana spent").build();
        this.stat(StatsPM.MANA_SPENT_MOON).name("Moon mana spent").build();
        this.stat(StatsPM.MANA_SPENT_BLOOD).name("Blood mana spent").build();
        this.stat(StatsPM.MANA_SPENT_INFERNAL).name("Infernal mana spent").build();
        this.stat(StatsPM.MANA_SPENT_VOID).name("Void mana spent").build();
        this.stat(StatsPM.MANA_SPENT_HALLOWED).name("Hallowed mana spent").build();
        this.stat(StatsPM.SHRINE_FOUND_EARTH).name("Earth shrines found").build();
        this.stat(StatsPM.SHRINE_FOUND_SEA).name("Sea shrines found").build();
        this.stat(StatsPM.SHRINE_FOUND_SKY).name("Sky shrines found").build();
        this.stat(StatsPM.SHRINE_FOUND_SUN).name("Sun shrines found").build();
        this.stat(StatsPM.SHRINE_FOUND_MOON).name("Moon shrines found").build();
        this.stat(StatsPM.BLOCKS_BROKEN_BAREHANDED).name("Hard blocks broken bare-handed").build();
        this.stat(StatsPM.SHEARS_USED).name("Times shears used").build();
        this.stat(StatsPM.TREANTS_NAMED).name("Treefolk driven to self-immolation").build();
        
        // Generate research project localizations
        this.researchProject("trade").name("Trade").text("Perhaps some nearby villagers have magickal knowledge to trade.  You assemble some goods they'll find valuable.").build();
        this.researchProject("mundane_tinkering").name("Mundane Tinkering").text("You spend some time working with your tools, creating paraphernalia to help with your magickal pursuits.").build();
        this.researchProject("redstone_tinkering").name("Redstone Tinkering").text("You spend some time tinkering with your redstone devices, seeing if their principles will help your magitech studies.").build();
        this.researchProject("expedition").name("Expedition").text("Who knows what arcane secrets lie hidden in the dungeons of the world?  You prepare an expedition to find some.").build();
        this.researchProject("nether_expedition").name("Nether Expedition").text("The fortresses of the Nether are surely filled with forbidden knowledge.  You prepare an expedition to find some.").build();
        this.researchProject("recuperation").name("Rest and Recuperation").text("Too much work dulls the mind.  Perhaps an idea will strike you if you take some time to stop and smell the roses.").build();
        this.researchProject("essence_analysis").name("Essence Analysis").text("Magickal essence has many amazing properties.  You study some to see how it reacts with other substances.").build();
        this.researchProject("brewing_experiments").name("Brewing Experiments").text("There are many similarities between brewing and wizardly alchemy.  Perhaps studying the former will give you insight into the latter.").build();
        this.researchProject("wand_tinkering").name("Wand Tinkering").text("You experiment with different methods and materials for constructing wands, hoping to refine your understanding of the process.").build();
        this.researchProject("spellwork").name("Spellwork").text("You devise and cast some simple spells, hoping to refine your technique.  Practice makes perfect, after all.").build();
        this.researchProject("advanced_essence_analysis").name("Adv. Essence Analysis").text("Analysis of basic magickal essence was rewarding.  It stands to reason that more advanced essence structures will teach you even more.").build();
        this.researchProject("enchanting_studies").name("Enchanting Studies").text("Enchanting and manaweaving are close siblings.  Studying their similarities and differences could be enlightening.").build();
        this.researchProject("observation_analysis").name("Observation Analysis").text("A more thorough analysis of your observations may yield surprising discoveries.").build();
        this.researchProject("hit_the_books").name("Hit the Books").text("There's bound to be something in all of this reading material that would be useful in your studies.").build();
        this.researchProject("beacon_emanations").name("Beacon Emanations").text("While they're not of the wizardly arts, Beacons are clearly more than just mundane objects.  Studying their emanations could be valuable.").build();
        this.researchProject("portal_detritus").name("Portal Detritus").text("Strange things sometimes come back through Nether portals.  You could learn much by studying them.").build();
        this.researchProject("draconic_energies").name("Draconic Energies").text("This egg seems to harmonize with ender pearls, releasing intriguing energies.  Is this really such a good idea, though?").build();
        this.researchProject("piglin_barter").name("Piglin Bartering").text("The piglins of the Nether must surely know secrets about that infernal place.  Exploit their craving for gold.").build();
        this.researchProject("apiamancy").name("Apiamancy").text("Some say you can see the future in the movement of bees.  It's probably overstated, but you might still learn something.").build();
        this.researchProject("end_expedition").name("End Expedition").text("The cities of The End reek of secrets.  You prepare an expedition to brave their dangers and bring some home.").build();
        this.researchProject("master_essence_analysis").name("Master Essence Analysis").text("Study of magickal essence has been profitable for you.  It's time to learn what the purest samples have to teach.").build();
        this.researchProject("draconic_memories").name("Draconic Memories").text("This dragon hoarded experiences, not gold.  Even in death it hungers for more.  Feed it, and see what it can teach you.").build();
        this.researchProject("raiding_the_raiders").name("Raiding the Raiders").text("The Illagers have surely plundered some valuable arcane secrets.  Show them that turnabout is fair play.").build();
        this.researchProject("runework").name("Runework").text("You practice carving some runes, an exacting process, hoping to refine your technique.  It could save you a lot of trouble down the road.").build();
        this.researchProject("advanced_runework").name("Advanced Runework").text("Practicing basic magickal runes was rewarding.  It stands to reason that more advanced runes would be even more elucidating.").build();
        this.researchProject("master_runework").name("Master Runework").text("Perfecting your technique of the mass production of runes, and the production of the most advanced runes, would be of great value.").build();
        this.researchProject("ritual_practice").name("Ritual Practice").text("Every little detail is important when performing a ritual, so getting some practice in would be a wise idea.").build();
        this.researchProject("advanced_ritual_practice").name("Adv. Ritual Practice").text("Practicing simple rituals helped hone your technique.  Now it's time to practice more advanced rituals.").build();
        this.researchProject("master_ritual_practice").name("Master Ritual Practice").text("The most powerful rituals are also the most dangerous.  Best to get some practice in before attempting one.").build();
        this.researchProject("magitech_tinkering").name("Magitech Tinkering").text("You spend some time tinkering with your magitech devices, to better understand the relationship between magick and technology.").build();
        this.researchProject("conduit_forces").name("Conduit Forces").text("A Conduit is a powerful artifact of the Sea, but is it magickal or something else?  Study one to try to find some answers.").build();
        this.researchProject("advanced_wand_tinkering").name("Adv. Wand Tinkering").text("You experiment with more exotic wand materials that you've discovered in your studies.  Knowledge will surely follow.").build();
        this.researchProject("advanced_spellwork").name("Advanced Spellwork").text("You devise and cast more complex spells, pushing the limits of your sorcerous knowledge.  That's the only way to expand them, after all.").build();
        this.researchProject("master_spellwork").name("Master Spellwork").text("You devise and cast reality-warping incantations which would have been major works in the past.  Now?  They're just practice.").build();
        this.researchProject("advanced_enchanting_studies").name("Adv. Enchanting Studies").text("The more you learn about manaweaving, the more intricate its relationship with enchanting.  Further experimentation is called for.").build();
        this.researchProject("master_enchanting_studies").name("Master Enchanting Studies").text("Testing the limits of what can be done with enchanting has taught you much about manaweaving.  Push those limits further.").build();
        this.researchProject("prosperous_trade").name("Prosperous Trade").text("Now that you've established yourself, the villagers should be willing to open more of their secrets to you.  Not for free, of course.").build();
        this.researchProject("rich_trade").name("Rich Trade").text("You and the villagers have cultivated a very profitable relationship.  Their deepest secrets should now be on the bargaining table.").build();

        // Generate research requirement localizations
        this.researchRequirement("u_unknown_rune").name("Unknown Rune").build();
        this.researchRequirement(Source.BLOOD.getDiscoverKey()).name("Discover a source of sanguine power").build();
        this.researchRequirement(Source.INFERNAL.getDiscoverKey()).name("Discover a source of hellish power").build();
        this.researchRequirement(Source.VOID.getDiscoverKey()).name("Discover a source of eldritch power").build();
        this.researchRequirement("t_discover_forbidden").name("Discover a source of forbidden magick").build();
        this.researchRequirement(Source.HALLOWED.getDiscoverKey()).name("Discover the secrets of the universe").build();
        this.researchRequirement("t_spells_cast_expert").name("Cast 10 spells").build();
        this.researchRequirement("t_spells_cast_master").name("Cast 50 spells").build();
        this.researchRequirement("t_spells_cast_supreme").name("Cast 250 spells").build();
        this.researchRequirement("t_spells_crafted_expert").name("Craft a spell").build();
        this.researchRequirement("t_spell_cost_master").name("Craft a spell with a mana cost of at least 50").build();
        this.researchRequirement("t_spell_cost_supreme").name("Craft a spell with a mana cost of at least 250").build();
        this.researchRequirement("m_found_shrine_earth").name("Locate an Earth shrine").build();
        this.researchRequirement("m_found_shrine_sea").name("Locate a Sea shrine").build();
        this.researchRequirement("m_found_shrine_sky").name("Locate a Sky shrine").build();
        this.researchRequirement("m_found_shrine_sun").name("Locate a Sun shrine").build();
        this.researchRequirement("m_found_shrine_moon").name("Locate a Moon shrine").build();
        this.researchRequirement("t_mana_spent_earth_expert").name("Spend 100 Earth mana (before modifiers)").hint("Mana is spent by crafting items that require it, such as at an Arcane Workbench, or by casting spells from a wand").build();
        this.researchRequirement("t_mana_spent_sea_expert").name("Spend 100 Sea mana (before modifiers)").hint("Mana is spent by crafting items that require it, such as at an Arcane Workbench, or by casting spells from a wand").build();
        this.researchRequirement("t_mana_spent_sky_expert").name("Spend 100 Sky mana (before modifiers)").hint("Mana is spent by crafting items that require it, such as at an Arcane Workbench, or by casting spells from a wand").build();
        this.researchRequirement("t_mana_spent_sun_expert").name("Spend 100 Sun mana (before modifiers)").hint("Mana is spent by crafting items that require it, such as at an Arcane Workbench, or by casting spells from a wand").build();
        this.researchRequirement("t_mana_spent_moon_expert").name("Spend 100 Moon mana (before modifiers)").hint("Mana is spent by crafting items that require it, such as at an Arcane Workbench, or by casting spells from a wand").build();
        this.researchRequirement("t_mana_spent_blood_expert").name("Spend 100 Blood mana (before modifiers)").hint("Mana is spent by crafting items that require it, such as at an Arcane Workbench, or by casting spells from a wand").build();
        this.researchRequirement("t_mana_spent_infernal_expert").name("Spend 100 Infernal mana (before modifiers)").hint("Mana is spent by crafting items that require it, such as at an Arcane Workbench, or by casting spells from a wand").build();
        this.researchRequirement("t_mana_spent_void_expert").name("Spend 100 Void mana (before modifiers)").hint("Mana is spent by crafting items that require it, such as at an Arcane Workbench, or by casting spells from a wand").build();
        this.researchRequirement("t_mana_spent_hallowed_expert").name("Spend 100 Hallowed mana (before modifiers)").hint("Mana is spent by crafting items that require it, such as at an Arcane Workbench, or by casting spells from a wand").build();
        this.researchRequirement("m_env_earth").name("Visit the dark depths of the earth").build();
        this.researchRequirement("m_env_sea").name("Behold the vast blue of the ocean").build();
        this.researchRequirement("m_env_sky").name("Travel to the peak of the world").build();
        this.researchRequirement("m_env_sun").name("Feel the scorching rays of the desert sun").build();
        this.researchRequirement("m_env_moon").name("Witness the moonlight filtered through the forest trees").build();
        this.researchRequirement("b_crafted_alchemy_expert").name("Craft 50 alchemy items").build();
        this.researchRequirement("b_crafted_alchemy_master").name("Craft 250 alchemy items").build();
        this.researchRequirement("b_crafted_alchemy_supreme").name("Craft 1000 alchemy items").build();
        this.researchRequirement("b_crafted_manaweaving_expert").name("Craft 10 manaweaving items").build();
        this.researchRequirement("b_crafted_manaweaving_master").name("Craft 50 manaweaving items").build();
        this.researchRequirement("b_crafted_manaweaving_supreme").name("Craft 250 manaweaving items").build();
        this.researchRequirement("b_crafted_runeworking_expert").name("Craft 10 runeworking items").build();
        this.researchRequirement("b_crafted_runeworking_master").name("Craft 50 runeworking items").build();
        this.researchRequirement("b_crafted_runeworking_supreme").name("Craft 250 runeworking items").build();
        this.researchRequirement("t_rituals_completed_expert").name("Conduct 2 rituals").build();
        this.researchRequirement("t_rituals_completed_master").name("Conduct 10 rituals").build();
        this.researchRequirement("t_rituals_completed_supreme").name("Conduct 50 rituals").build();
        this.researchRequirement("t_ritual_mishaps_basic").name("Endure a ritual mishap").build();
        this.researchRequirement("b_crafted_magitech_expert").name("Craft 5 magitech items").build();
        this.researchRequirement("b_crafted_magitech_master").name("Craft 25 magitech items").build();
        this.researchRequirement("b_crafted_magitech_supreme").name("Craft 100 magitech items").build();
        this.researchRequirement("t_items_runescribed_expert").name("Runescribe 2 items").build();
        this.researchRequirement("t_items_runescribed_master").name("Runescribe 10 items").build();
        this.researchRequirement("t_items_runescribed_supreme").name("Runescribe 50 items").build();
        this.researchRequirement("t_research_projects_completed").name("Complete 10 research projects at a research table").build();
        this.researchRequirement("t_items_analyzed").name("Analyze 25 distinct items").build();
        this.researchRequirement("b_scan_primalite").name("Analyze a magickal metal").hint("Complete the Alchemy research entry for Primalite, then scan a nugget, ingot, or block of it").build();
        this.researchRequirement("b_scan_hexium").name("Analyze a more potent magickal metal").hint("Complete the Alchemy research entry for Hexium, then scan a nugget, ingot, or block of it").build();
        this.researchRequirement("b_scan_hallowsteel").name("Analyze the most potent of magickal metals").hint("Complete the Alchemy research entry for Hallowsteel, then scan a nugget, ingot, or block of it").build();
        this.researchRequirement("t_flying_creature").name("Scan a flying creature").build();
        this.researchRequirement("t_golem").name("Scan an animated construct").build();
        this.researchRequirement("t_observations_made_basics").name("Analyze enough objects to make an Observation").build();
        this.researchRequirement("t_observations_made_expert").name("Make 25 Observations").build();
        this.researchRequirement("t_theories_formed_basics").name("Complete enough research projects to form a Theory").build();
        this.researchRequirement("m_blocks_broken_barehanded_expert").name("Break 50 blocks with your bare hands.  They must be at least as hard as wood.").build();
        this.researchRequirement("b_place_stone_expert").name("Place 100 stone or cobblestone").build();
        this.researchRequirement("m_drown_a_little").name("Test the limits of your underwater endurance").hint("Suffer at least one point of drowning damage").build();
        this.researchRequirement("b_shears_used_expert").name("Use shears to trim 50 things").build();
        this.researchRequirement("m_fly_elytra").name("Soar on an Elytra for 1km").build();
        this.researchRequirement("b_place_torch_expert").name("Place 100 torches").build();
        this.researchRequirement("m_near_death_experience").name("Have a near-death experience").hint("Drop to three hearts of life or less, then recover to full health without dying").build();
        this.researchRequirement("m_furry_friend").name("Make a furry, canine friend for life").hint("Tame a wolf").build();
        this.researchRequirement("m_breed_animal").name("Facilitate the miracle of birth").hint("Successfully breed two animals").build();
        this.researchRequirement("m_feel_the_burn").name("Feel the burn").hint("Suffer damage from contact with lava").build();
        this.researchRequirement("m_teleport_a_lot").name("Teleport a total of 100m").build();
        this.researchRequirement("b_scan_nether_star").name("Analyze an item of extra-dimensional power").hint("Scan a Nether Star").build();
        this.researchRequirement("t_mana_siphoned_basics").name("Siphon 10 mana from a mana font with your wand").build();
        this.researchRequirement("t_mana_siphoned_expert").name("Siphon 1000 mana from mana fonts with your wand").build();
        this.researchRequirement("t_mana_spent_total_basics").name("Spend 20 mana of any source (before modifiers)").build();
        this.researchRequirement("m_sotu_discover_blood").name("Discover a source of sanguine power").hint("Discover the Blood source by either eating Bloody Flesh, dropped by sentient humanoids, or reading Blood-Scrawled Ravings, dropped by magic users such as Witches").build();
        this.researchRequirement("m_sotu_discover_infernal").name("Discover a source of hellish power").hint("Discover the Infernal source by visiting the Nether").build();
        this.researchRequirement("m_sotu_discover_void").name("Discover a source of eldritch power").hint("Discover the Void source by visiting the End").build();
        this.researchRequirement("t_sotu_research_arcanometer").name("Learn to analyze items non-destructively").hint("Complete the Magitech research entry for the Arcanometer").build();
        this.researchRequirement("t_sotu_research_hexium").name("Learn to give form to the forbidden").hint("Complete the Alchemy research entry for Hexium").build();
        this.researchRequirement("t_sotu_research_power_rune").name("Learn to empower your runecrafting").hint("Complete the Runeworking research entry for Power Runes").build();
        this.researchRequirement("t_sotu_research_sanguine_crucible").name("Learn to create alchemical life").hint("Complete the Alchemy research entry for the Sanguine Crucible").build();
        this.researchRequirement("t_sotu_research_cleansing_rite").name("Learn to summon your inner demons").hint("Complete the Ritual Magick research entry for the Cleansing Rite").build();
        this.researchRequirement("b_sotu_scan_hallowed_orb").name("Study the remains of your inner demons, but gently").hint("Scan the Hallowed Orb dropped by your Inner Demon using the Arcanometer").build();

        // Generate research entry localizations
        this.researchEntry("FIRST_STEPS").name("First Steps")
            .stages()
                .add("What an incredible book!  While largely blank, it seems to contain more pages than could conceivably fit in its slim binding.  Plus, it generates its own updating index as I write in it.  This will make a perfect volume in which to record my progress in magickal studies.<BR>Note, however, that I said, 'largely blank'.  Sometimes when I flip randomly through its pages, I find one covered in strange notes and diagrams, a page that seems to disappear once I turn past it.  I can't understand most of their contents, but I can eke out a few hints of how to proceed in my studies, like a trail of breadcrumbs leading me to vast knowledge.<BR>So far, the first thing I've learned that the strange dust I found under the shrine is called Essence, and that it's a physical manifestation of magickal energy, called Mana.  The magickal arts rely heavily on Essence and Mana, so I should be prepared to seek out as much as I can.<BR>The second thing I've learned, after much frustration, is that magickal crafting requires techniques, and frequently infusions of Mana, that are beyond the capacity of a normal crafting table.  I'll need to devise a new type of workbench if I'm to progress further, but curiously the Grimoire gives no suggestion as to how I might do that.  Perhaps it's a test, of sorts.<BR>I wonder...  Waving a wand at a bookshelf was enough to get me this Grimoire.  Maybe waving it at a crafting table will get me what I need a second time?")
                .add("Wow, that actually worked.  I now have access to an Arcane Workbench sufficient for my magickal crafting needs.<BR>Now I just need some mana for my wand, but how to get it?  Maybe the shrines hold the answer.  I should visit one again with my wand in hand.<BR>Whatever I do, I should be prepared to harvest more mana than I think I'll need, as the Grimoire indicates that a mundane wand like this is an inefficient conducter of it.")
                .add("I've now got some mana and an arcane workbench with which to use it.  Now the question is what to do with them.<BR>The notes that I find in the grimoire make mention of five primal sources of magick: the Earth, the Sea, the Sky, the Sun, and the Moon.  It also hints that everything in the world, down to the smallest pebble, has an affinity to one or more of these sources.  That seems worth investigating, but I'll need better tools.<BR>It shouldn't take much to get me started.  A sturdy wooden table to work at, a magnifying glass to more closely study objects, and some paper on which to record my findings.  Yes, that'll do for now.<BR>Once I get that all set up, I can start analyzing things.  I expect that the analysis process will have to be quite thorough for me to learn anything, though.<BR>It would probably be best to hold off on analyzing anything truly valuable, as the item will undoubtedly be lost beyond repair after I'm done with it.  Still, studying a few items like this should give me the information I need to start pursuing more advanced knowledge.")
                .add("Okay, I've made some useful observations.  These will prove foundational as I continue to study the rudiments of magick.  I should make a point to analyze everything I come across.  Just one of each type, though.  I won't learn anything new from studying the same type of object twice.<BR>Usefully, I've also formed a detailed analysis of the magickal affinities of the objects I've studied.  Granted, I'm not sure what to do with this information yet, but I'm certain it will prove valuable down the road.  I can remind myself of the affinities of any type of object I've studied by hovering over the item in my inventory and pressing the View Item Affinities key, bound by default to Left Shift.")
                .end()
            .build();
        this.researchEntry("THEORYCRAFTING").name("Theorycrafting")
            .stages()
                .add("To paraphrase a wise philosopher, if I've learned anything so far, it's that I know nothing.  Rudimentary observations on the magickal world may suffice for studying the basics of magick, but they won't be enough to broach the advanced topics.  For that I'm going to need to form some Theories of my own.<BR>I should give some thought to how best to accomplish this, based on what I've learned so far.")
                .add("Okay, I think I've got this figured out.  I'm going to need to set up a dedicated space for my research work, and I'm going to need writing materials.  Lots of paper, and special enchanted ink.")
                .add("My research table is in place, so it's time to get to work.<BR>Once I place some writing materials on my research table, I'll be able to start on the first research project that comes to mind.  Projects come in many different shapes and sizes, but they all require materials for the best chance of success.  The materials needed for a project will be listed at the bottom of its card.<BR>Sometimes the material will be consumed as part of completing the project.  If this is the case, it will be marked with an exclamation point.  For those without an exclamation point, I simply need to have the material on hand; it won't be lost.<BR>If the material is a block and it's not slated to be consumed by the project, then I don't even need to have it on my person.  Simply having the block placed near the research table, within five blocks, will be good enough.<BR>The current success chance of the project is listed on its completion button.  Selecting materials to use for the project will increase my chances of success.  Should the project be a success, I'll make progress towards forming a Theory.  If it fails, I'll gain nothing.<BR>Having one or more research aids within five blocks of my research table might also be a good idea, but it's not mandatory.  Research aids will unlock special research projects that are easier to complete and make more Theory progress.  My analysis table would be a good start.  There may be other blocks that make good research aids, but I'll have to experiment to find out what they are.")
                .add("I've formed my first theory!  This will definitely prove useful later in my studies.<BR>I should expect research projects to become more difficult as I exhaust the low-hanging fruit.  They'll probably have lower starting success chances and require more research materials the more projects I complete.  This will only make bonus projects unlocked by research aids more economical, so I should definitely try to identify as many of them as I can.<BR>Remember, self: the research table only detects blocks within five blocks of itself.  Try an analysis table as a starting research aid.")
                .end()
            .build();
        this.researchEntry("ATTUNEMENTS").name("Attunements")
            .stages()
                .add("In my studies of the Grimoire, I've seen the concept of 'attunement' mentioned.  I should find out more about what this means.")
                .add("Fascinating.  From what I can tell, the study and use of magick brings a person more into alignment with that source of magick, body and mind.  This results in the development of incredible new abilities on the part of the attuned mage.<BR>Learning a new spell or recipe in my Grimoire will sometimes permanently increase my attunement to the relevant source; I'll be sure to note in the grimoire when this happens.  In addition, channeling mana, whether by crafting a recipe or casting a spell, will temporarily increase my attunement to that source.  The effect can be significant, but it will slowly decay over time.<BR>I've cast a simple spell on my Grimoire to track my current attunement levels.  Should I ever wish to check them, or to remind myself what attunement to a particular source will gain me, I need only check the Attunements section under Other Topics in the main index.")
                .end()
            .build();
        this.researchEntry("UNLOCK_MANAWEAVING").name("Introduction to Manaweaving")
            .stages()
                .add("Integrating mana into my crafting process should be the next goal in my magickal studies.  This will hopefully allow me to create better materials and tools for magickal crafting and for channeling mana.")
                .add("I believe I've unlocked a new magickal discipline, which I've taken to calling Manaweaving.  This will lead me to greater knowledge of working with mana directly.  I've created a new section for it under the main index of my Grimoire, and can find more information there.")
                .end()
            .build();
        this.researchEntry("UNLOCK_ALCHEMY").name("Introduction to Alchemy")
            .stages()
                .add("The properties and uses of magickal essence are the next mystery I should crack, I think.  I need to know more about this mysterious dust that I've been using.")
                .add("I've unlocked another new discipline of magick.  This one I'm calling Alchemy.  I believe it will help me understand how to better use magickal essence, and maybe even discover new types of essence.  I've created a new section for this discipline under the main index of my Grimoire, and can find more information there.")
                .end()
            .build();
        this.researchEntry("UNLOCK_SORCERY").name("Introduction to Sorcery")
            .stages()
                .add("Crafting is all well and good, but there must be a way to use mana to affect the world directly.  If this truly is magick, then I want to cast some spells!")
                .add("The discipline of Sorcery is now open to me.  This will allow me to use mana to change the world to my will, at least within the structure of spells that I can research.  I've created a new section for it under the main index of my Grimoire, and can find more information there.")
                .end()
            .build();
        this.researchEntry("UNLOCK_RUNEWORKING").name("Introduction to Runeworking")
            .stages()
                .add("During my alchemical studies in the Grimoire, I keep coming across strange symbols.  They don't seem to correspond to any of the formulae that I've examined, but have a power all their own.  I should find out more about them.")
                .add("I think that I'm starting to understand more about these symbols, or Runes as I've taken to calling them.  By inscribing them on items, I appear to be able to infuse those items with power, but the mechanics still elude me.  This will take more study.  I've created a new section for this discipline under the main index of my Grimoire, and can find more information there.")
                .end()
            .build();
        this.researchEntry("UNLOCK_RITUAL").name("Introduction to Ritual Magick")
            .stages()
                .add("There seem to be limits to the kind of power that can be exercised in magickal crafting or sorcery.  But I didn't start studying magick because I was interested in limits.  Perhaps there's a process I can use to surpass them.")
                .add("My efforts have unveiled a new discipline of magickal study: Ritual Magick.  By going through a series of steps, a sort of simulation of a magickal journey, and offering up various items in the process, I can manifest creations that would be impossible using traditional methods.  I've created a new section for this discipline under the main index of my Grimoire, and can find more information there.")
                .end()
            .build();
        this.researchEntry("UNLOCK_MAGITECH").name("Introduction to Magitech")
            .stages()
                .add("In the pursuit of magickal knowledge, it's easy to forget the simple power of mundane machinery.  The wonders that can be created without a drop of magick should not be discounted.  Perhaps there's a way to combine the two?")
                .add("Magick and redstone, together at last, forming the backbone of the discipline I call Magitech.  I'm truly excited to see what creations can be wrought with this knowledge.  I've created a new section for it under the main index of my Grimoire, and can find more information there.")
                .end()
            .build();
        this.researchEntry("TERRESTRIAL_MAGICK").name("Terrestrial Magick")
            .stages()
                .add("As I've mentioned before, the five sources of magick are the Earth, the Sea, the Sky, the Sun, and the Moon.  Almost everything I've found has some affinity to one or more of them, but what exactly are they, really?  What makes them magickal?<BR>This is probably more philosophy than magick, and I'm unlikely to learn any recipes or spells by meditating on them, but I have faith that it will be beneficial to me.  If nothing else, I'm sure I'll feel more in tune with these sources once I understand them better.")
                .end()
            .build();
        this.researchEntry("SOURCE_EARTH").name("Source: The Earth")
            .stages()
                .add("Through sacrifice, study, and exploration, I investigate the secrets of earth magick.")
                .add("Stone, strength, solidity: these are the foundations of the Earth.  It seeks the most direct answer between any two problems, not brooking any compromise or alternative.  One either overcomes it or yields.")
                .end()
            .build();
        this.researchEntry("SOURCE_SEA").name("Source: The Sea")
            .stages()
                .add("Through sacrifice, study, and exploration, I investigate the secrets of sea magick.")
                .add("Water, cold, cycles: these are the currents of the Sea.  It finds every pore, penetrates every crack, and will in time wash away whatever opposes it.  To quote a particularly wise doctor, water always wins.")
                .end()
            .build();
        this.researchEntry("SOURCE_SKY").name("Source: The Sky")
            .stages()
                .add("Through sacrifice, study, and exploration, I investigate the secrets of sky magick.")
                .add("Air, energy, motion: these are the heights of the Sky.  Always moving, ever shifting, there is no pinning down the wind.  Those who try find themselves facing a bolt from the blue.")
                .end()
            .build();
        this.researchEntry("SOURCE_SUN").name("Source: The Sun")
            .stages()
                .add("Through sacrifice, study, and exploration, I investigate the secrets of sun magick.")
                .add("Day, warmth, growth: it is this light that the Sun shines down upon us.  Not a burning flame, but a gentle warmth that nurtures and brings life.  That said, the Sun has no love for death and will purge its minions with blazing rays.")
                .end()
            .build();
        this.researchEntry("SOURCE_MOON").name("Source: The Moon")
            .stages()
                .add("Through sacrifice, study, and exploration, I investigate the secrets of moon magick.")
                .add("Night, change, fortune: these are the blessings the Moon brings us.  Ever changing, the Moon is not one's ally unless it wishes to be, and even then its friendship is fickle.  But it loves all that which the Sun does not, and cares for the creatures of darkness when nobody else will.")
                .end()
            .build();
        this.researchEntry("FORBIDDEN_MAGICK").name("Forbidden Magick")
            .stages()
                .add("What is this?!  Have I truly discovered a new source of magick?  Could I truly have stumbled across something so easily that the ancients in their wisdom missed?<BR>No, I don't think that's it at all.  I've searched the Grimoire for any mention of this source and found nothing.<BR>And yet...<BR>And yet I can almost see the gaps.  The places where this source would have become apparent, glossed over.  The formulae for which it would be a natural conclusion, elided.<BR>No, I think the ancients knew all about this source and chose to erase it from their knowledge.  For whatever reason, this magick was forbidden.<BR>I intend to find out why.")
                .end()
            .build();
        this.researchEntry("SOURCE_BLOOD").name("Source: The Blood")
            .stages()
                .add("Through sacrifice and study, I investigate the secrets of blood magick.")
                .add("Life, death, flesh: this is the pulse of the Blood.  Beating through all life (well, almost all life), it is part of us whether we wish it or not.  All a mage can do is decide whether to harness it, or to leave it to clot.")
                .end()
            .build();
        this.researchEntry("SOURCE_INFERNAL").name("Source: The Infernal")
            .stages()
                .add("Through sacrifice and study, I investigate the secrets of infernal magick.")
                .add("Flame, suffering, rage: these are the wages of the Infernal.  The native magick of the Nether and its damned occupants, countless souls have fallen prey to its grasp.  Will I fare any better?")
                .end()
            .build();
        this.researchEntry("SOURCE_VOID").name("Source: The Void")
            .stages()
                .add("Through sacrifice and study, I investigate the secrets of void magick.")
                .add("Emptiness, hunger, space: this is the paradox of the Void.  The native magick of the End and the places beyond, its secrets are all but unknowable by a human mind.  Do I have the mental fortitude to unlock its secrets, or will the attempt warp me beyond recognition?")
                .end()
            .build();
        this.researchEntry("HEAVENLY_MAGICK").name("Heavenly Magick")
            .stages()
                .add("At last, I think I begin to understand.<BR>The Earth... the Sea... the Sky... the Sun... the Moon... even the forbidden sources...<BR>They're all reflections.  Pale shadows cast by the pure, true light of the universe.  Echoes of the perfect harmony of its song.<BR>I feel like enlightenment is within my grasp now.  Perhaps even forgiveness for my many sins in the pursuit of magickal power.<BR>My journey nears its end.")
                .end()
            .build();
        this.researchEntry("SOURCE_HALLOWED").name("Source: The Hallowed")
            .stages()
                .add("Through sacrifice and study, I investigate the secrets of hallowed magick.")
                .add("Purity, harmony, enlightenment: this is the truth of the Hallowed.  There are no words to describe the lightness I now feel, the understanding I bear.  To you, reading this Grimoire after I'm gone, know that peace is within you, and that you are loved.")
                .end()
            .build();
        this.researchEntry("SECRETS_OF_THE_UNIVERSE").name("Secrets of the Universe?")
            .stages()
                .add("The secrets of the universe?  Seriously?  I swear, sometimes I think this Grimoire is alive, if only because it likes to taunt me so.  But no matter, if anyone can make such a lofty discovery, it is I.<BR>The only question is... where to begin?<BR>Such secrets will undoubtedly require a great deal of research across a breadth of disciplines.  The Grimoire seems to hint that I will need to pay special attention to ritual magick, alchemy, and runeworking in order to achieve my goals.  It's also strongly implied that I will need to explore worlds beyond my home in the Overworld.  Finally, I could swear I saw an oblique reference to some kind of... decontamination?  The translation was unclear, and the reference vanished almost as soon as I saw it.  Most curious.<BR>Regardless, this will be a long road to walk.  I had best be patient and discover what I can along the way.")
                .add("Ah, so these are the secrets of the universe, then.  Its underlying rhythm and melody.  I feel more in tune with the cosmos already.")
                .end()
            .build();
        this.researchEntry("COMPLETE_BASICS").name("Completion of Fundamentals")
            .stages()
                .add("I believe I've finally learned all that there is to know of the fundamentals of magick.")
                .add("My completion of this discipline has attuned me further to the ways of magick.  I should turn my eye to the other disciplines and see if there remains anything for me to glean.")
                .end()
            .build();
        this.researchEntry("THEORY_OF_EVERYTHING").name("Theory of Everything")
            .stages()
                .add("Could it be?  Have I really discovered all there is to know of magick?")
                .add("And so my journey reaches its end.  I have learned all there is to know of the ways of magick.  All that remains is to leave the universe a better place than I found it.<BR>Author's note: Congratulations, you have completed all the currently available content in Primal Magick.  Thank you so much for playing!  I truly hope you've enjoyed your magickal journey.  Stay tuned for future updates!")
                .end()
            .build();
        this.researchEntry("BASIC_ALCHEMY").name("Basic Alchemy")
            .stages()
                .add("I think I'm getting the hang of the basics of alchemy.  Combine dust with like-affinity materials for interesting results.  Be careful handling it.  *Definitely* don't eat it again, ugh.<BR>But how to make more of it?  There are only so many shrines in the world, and I can't very well dig underneath all of them.<BR>My Grimoire hints at something called an Essence Furnace, but doesn't say how to make one.  Maybe if I wave my wand at a regular furnace, like I did for the Arcane Workbench?")
                .add("Waving my wand at a furnace did the trick!<BR>This essence furnace will consume any item, but instead of a smelted result, will produce a small amount of essence dust based on the item's affinities.<BR>It's not very efficient, as it only produces a single handful of dust for each type of affinity that the item has, no matter how strong that affinity.  But it's a lot better than nothing.<BR>I should note, however, that items with particularly weak affinities, less than five points, only have a chance to generate a dust.  They might not generate anything at all.")
                .end()
            .build();
        this.researchEntry("EXPERT_ALCHEMY").name("Expert Alchemy")
            .stages()
                .add("My skill with alchemy is growing, but I need more practice with it before I can get over this plateau I'm on.  Nothing to do but keep at it.")
                .add("I now feel comfortable calling myself an expert with basic alchemical processes.  Time to get to the advanced stuff.  I have a feeling that there's more to essence than just dust, for instance...")
                .end()
            .build();
        this.researchEntry("MASTER_ALCHEMY").name("Master Alchemy")
            .stages()
                .add("My skill with alchemy is growing, but I need more practice with it before I can get over this plateau I'm on.  Nothing to do but keep at it.")
                .add("I've now mastered conventional alchemy and have started researching the forbidden topics that my Grimoire remains silent about.")
                .end()
            .build();
        this.researchEntry("SUPREME_ALCHEMY").name("Supreme Alchemy")
            .stages()
                .add("The top of the hill is in sight.  My skills with alchemy are almost without peer...  Almost.")
                .add("The most advanced topics in the discipline of alchemy are now at my fingertips.")
                .end()
            .build();
        this.researchEntry("COMPLETE_ALCHEMY").name("Completion of Alchemy")
            .stages()
                .add("I believe I've finally learned all that there is to know of alchemy.")
                .add("My completion of this discipline has attuned me further to the ways of magick.  I should turn my eye to the other disciplines and see if there remains anything for me to glean.")
                .end()
            .build();
        this.researchEntry("STONEMELDING").name("Stonemelding")
            .stages()
                .add("Stone is an excellent material to build with because it's so unyielding.  But that same inflexibility also makes it a pain to work with.  Cobblestone is frequently an acceptable substitute, but sometimes you need a single, solid block, not a mass of small boulders.<BR>I should study the mundane process of making stone from cobble to see if a more alchemical solution presents itself.")
                .add("As I hoped, alchemy was the answer here.  Sprinkling a handful of earth dust into the cracks of some blocks of cobblestone will cause the mass to become temporarily pliable, like wet clay.  I can then take the individual cobbles and mash them together.<BR>Even better, the process works with other types of particulate stone as well.  With a little bit of earth dust, I can gradually increase the particle size of the rock until it becomes fully solid.")
                .end()
            .build();
        this.researchEntry("SKYGLASS").name("Skyglass")
            .stages()
                .add("Glass is a beautiful addition to any base.  But the result of smelting sand in a furnace is often streaky and unrefined.  I find that I spend half my time trying to look between the seams in the blocks, rather than just enjoying the view.  Maybe alchemy can provide a superior product.")
                .add("Behold, skyglass.  By taking several blocks of glass and imbuing them with sky dust, I can remove the various impurities left behind by the furnace.<BR>The results are amazing.  The seams between blocks are invisible; except for the outer border you'd hardly even know it was there.  Plus, the glass is strong enough that it won't shatter when I break it.  Finally, skyglass can be stained like regular glass, or turned into thin panes.<BR>I can't wait to install my new skyglass window!")
                .end()
            .build();
        this.researchEntry("SHARD_SYNTHESIS").name("Shard Synthesis")
            .stages()
                .add("According to my observations, the magickal potential of dust is sadly low.  It works for some purposes, but more powerful magick will require more powerful essence.  I should figure out how to make it.")
                .add("My theory was correct!  Using a sliver of quartz as a base, I can imbue it with several handfuls of dust essence at once.  The resulting shard is both stable and more energetic than the sum of the dust used to create it.  I think this is going to unlock a lot of research avenues for me.")
                .end()
            .addenda()
                .add("As I suspected, the shard synthesis process also works for Blood dust.")
                .add("Yes, the shard synthesis process works for Infernal dust, too.")
                .add("I was a little worried that the alien nature of Void dust would alter its applicability to the shard synthesis process, but it worked fine.")
                .add("No surprise at this point, Hallowed shards are synthesized with the same process as the other sources.")
                .end()
            .build();
        this.researchEntry("SHARD_DESYNTHESIS").name("Shard Desynthesis")
            .stages()
                .add("If it's possible to make shards from dust, then logically it follows that I should be able to break them back down into dust.")
                .add("Well, the good news is that while I can indeed grind shards back down into dust, the bad news is that the process is lossy.  I only get back about half of the dust that I put into the shard in the first place, and the resulting quartz dust is completely unusable.<BR>Therefore, it would probably be wise to not convert all of my dust into shards in this way.  It's not a storage block, like I can make with iron or gold.  But should I miscalculate my essence needs somewhere along the way, I'm not completely out of luck.")
                .end()
            .addenda()
                .add("As I suspected, the shard desynthesis process also works for Blood shards.")
                .add("Yes, the shard desynthesis process works for Infernal shards, too.")
                .add("I was a little worried that the alien nature of Void shards would alter their applicability to the shard desynthesis process, but it worked fine.")
                .add("No surprise at this point, Hallowed shards are desynthesized with the same process as the other sources.")
                .end()
            .build();
        this.researchEntry("PRIMALITE").name("Primalite")
            .stages()
                .add("Each of the primal sources has wondrous effects on their own, but how might they work when combined?  I suspect that they would produce a highly magickal material, but I'll need to do some research to figure out how to make the process work.")
                .add("Success!  By combining an ingot of iron with a handful of dust from each terrestrial source, I've created a strong, stable metal with a lovely teal hue that positively thrums with magickal potential.  I've taken to called it Primalite.<BR>I can use this metal to make traditional arms and armor, stronger and far more durable than their iron counterparts, and much more readily accepting of enchantments.  But I suspect that much more exotic uses for this material will present themselves with further study.  So exciting!")
                .end()
            .build();
        this.researchEntry("CRYSTAL_SYNTHESIS").name("Crystal Synthesis")
            .stages()
                .add("If dust can be fused into shards, then perhaps shards can be fused into an even greater type of essence.  My work demands essence of greater purity and power!")
                .add("Yes, good, good.  By combining several shards with a full unit of quartz, I've created a form of crystalline essence.  Like shards before them, these crystals remain stable at room temperature and are more potent than the sum of their parts.  The ramifications of this discovery are most intriguing.")
                .end()
            .addenda()
                .add("As I suspected, the crystal synthesis process also works for Blood shards.")
                .add("Yes, the crystal synthesis process works for Infernal shards, too.")
                .add("I was a little worried that the alien nature of Void shards would alter their applicability to the crystal synthesis process, but it worked fine.")
                .add("No surprise at this point, Hallowed crystals are synthesized with the same process as the other sources.")
                .end()
            .build();
        this.researchEntry("CRYSTAL_DESYNTHESIS").name("Crystal Desynthesis")
            .stages()
                .add("I bet that I can break down these crystals into shards again, but I need to work out the details to keep the whole thing from shattering.")
                .add("By cracking the crystal at precise locations, I can fracture it into shard essence without ruining the whole thing.  Unfortunately, the process is still a lossy one.  I only get back half of the shards that I put into creating the crystal in the first place, and the quartz is utterly consumed.")
                .end()
            .addenda()
                .add("As I suspected, the crystal desynthesis process also works for Blood crystals.")
                .add("Yes, the crystal desynthesis process works for Infernal crystals, too.")
                .add("I was a little worried that the alien nature of Void crystals would alter their applicability to the crystal desynthesis process, but it worked fine.")
                .add("No surprise at this point, Hallowed crystals are desynthesized with the same process as the other sources.")
                .end()
            .build();
        this.researchEntry("HEXIUM").name("Hexium")
            .stages()
                .add("If I theorize correclty, I suspect that an even more potent magickal metal than Primalite can be created.  Early experiments have disastrously shown that simple iron is too weak to absorb the raw power of the forbidden sources, but what about Primalite?")
                .add("I hold in my hands a new metal that I call Hexium.  It is uncomfortably cold to the touch, except when it pulses with uncomfortable heat.  Still, it's managing to contain the power of the Blood, Infernal, and Void sources simultaneously, so that makes it all worthwhile.<BR>Like its precursor, I can use Hexium to craft traditional arms and armor, and their potential for enchantment exceeds even that of Primalite.  It almost feels like a waste to put such a powerful metal to use in such a mundane fashion, however.  Clearly this warrants further development.")
                .end()
            .build();
        this.researchEntry("CLUSTER_SYNTHESIS").name("Cluster Synthesis")
            .stages()
                .add("I can see it in my mind's eye: a singular cluster of essence, of perfect affinity with its primal source, unfolding like a lotus flower.  I know what I must do.")
                .add("The pattern holds.  Taking a perfect block of quartz and combining it with several crystals has produced the purest essence that I'm ever likely to see.  It would be a shame to use something so beautiful for mere magick.")
                .end()
            .addenda()
                .add("As I suspected, the cluster synthesis process also works for Blood crystals.")
                .add("Yes, the cluster synthesis process works for Infernal crystals, too.")
                .add("I was a little worried that the alien nature of Void crystals would alter their applicability to the cluster synthesis process, but it worked fine.")
                .add("No surprise at this point, Hallowed clusters are synthesized with the same process as the other sources.")
                .end()
            .build();
        this.researchEntry("CLUSTER_DESYNTHESIS").name("Cluster Desynthesis")
            .stages()
                .add("Branching before me<BR>A cluster of radiance<BR>Dare I reduce it?")
                .add("Like pruning a tree<BR>Desynthesizing clusters<BR>Perfection is lost")
                .end()
            .addenda()
                .add("As I suspected, the cluster desynthesis process also works for Blood clusters.")
                .add("Yes, the cluster desynthesis process works for Infernal clusters, too.")
                .add("I was a little worried that the alien nature of Void clusters would alter their applicability to the cluster desynthesis process, but it worked fine.")
                .add("No surprise at this point, Hallowed clusters are desynthesized with the same process as the other sources.")
                .end()
            .build();
        this.researchEntry("HALLOWSTEEL").name("Hallowsteel")
            .stages()
                .add("The heart of an ingot of Hexium churns with power, but also with instability, much like that of a younger mage I can think of.  Perhaps it too can be soothed with the song of the universe.")
                .add("Even magickal metals can be saved, it turns out.  This new Hallowsteel no longer pulses with heat and cold, but radiates light and gentle warmth.  It's hard, strong, light as a feather, and eminently enchantable.<BR>Putting it to use in traditional arms and armor is certainly a possibility, but I suspect it will be useful for so much more.  Always there is more to learn.")
                .end()
            .build();
        this.researchEntry("CALCINATOR_BASIC").name("Basic Calcinator")
            .stages()
                .add("The essence furnace is functional, but it produces very little essence.  And, if I'm being entirely honest, it smells pretty bad while it's working.  I think I can do better.")
                .add("Using the essence furnace as a base, I've produced a device that I call a Calcinator.  Made primarily of marble, with it's mana-absorbing properties, it can better contain the energies of the reduction process, resulting in a faster, more efficient production.<BR>Rather than a single dust per source, a calcinator will produce one dust for every five points of affinity an object has for a source.  If the item doesn't have five points of affinity, there will still be a chance of a single dust yield so long as it has at least a single point of affinity.  This will result in significantly higher yields for some items.<BR>A calcinator does, however, require some configuration when placed.  I can do this myself with no problem as part of placing the block, but it does mean that another mage's calcinator won't necessarily produce the same results that my own would.  Should the calcinator produce unusable waste, that's a sign of something that the calcinator's owner hasn't learned yet.")
                .end()
            .build();
        this.researchEntry("CALCINATOR_ENCHANTED").name("Enchanted Calcinator")
            .stages()
                .add("Now that I know essence shards exist, I wonder if it's possible to calcinate them directly, rather than having to synthesize them from dust and quartz.  This calls for further experimentation.")
                .add("Using enchanted marble to better contain the calcination reaction, as well as to allow stoking a hotter flame, I've created a calcinator capable of producing whole essence shards, and faster to boot!<BR>To yield a shard, an item must have at least twenty points of affinity for a given source.  At that threshold, one shard will be produced for every twenty affinity points.<BR>If an item does not meet that threshold, then dust will be produced as with a lower tier calcinator.  It's worth noting that an enchanted calcinator will never produce both shards and dust in a single reaction for a single source.  Should an item have thirty-nine points of affinity, I'll get a single shard; the remaining nineteen points will be wasted.<BR>Like a lower tier calcinator, this also requires some configuration.  Easy enough to do as part of placing the block, but it still means that another mage's calcinator won't necessarily produce the same results as one of my own.")
                .end()
            .build();
        this.researchEntry("CALCINATOR_FORBIDDEN").name("Forbidden Calcinator")
            .stages()
                .add("I need more crystals, and fusing them together with shards is too resource-intensive.  Perhaps I can upgrade my calcinator again...")
                .add("Excellent.  With smoked marble and so-called forbidden magick, I now have a calcinator capable of sustaining a reaction strong enough to produce essence crystals.  It works more quickly than the previous version as well.<BR>An item will yield one crystal if it has fifty points of affinity, and a second if it has a full one hundred.  If an item does not have at least fifty points of affinity for a source, then either shards or dust will be produced as appropriate, as with lesser calcinator tiers.  Again, a single source will only produce a single type of essence per reaction.<BR>Like lesser calcinators, this still requires configuration when placed.  A mage of lesser power will not know how to configure their calcinator to produce essence of the forbidden sources, so I should take care to always use my own, and to guard my secrets well.")
                .end()
            .build();
        this.researchEntry("CALCINATOR_HEAVENLY").name("Heavenly Calcinator")
            .stages()
                .add("Items of the strongest affinity have the potential to be transformed into clusters, I know it.  Can I bring about their metamorphosis?")
                .add("It is done.  With the aid of the Hallowed, I have created the perfect calcinator.  It is capable of creating full essence clusters, and doing so faster than any other version to date.<BR>To be turned into a cluster of essence, an item must have the maximum possible affinity for a source: one hundred points.  I'm unlikely to find naturally occurring materials of such strong affinity, but certain manufactured goods will have this potential.  Other items will be turned into essence as with a calcinator of a lower tier.<BR>Just like other calcinators, this one will only yield a single type of essence per source per reaction.  Not that there would be any leftover affinity points after producing a cluster, anyway.<BR>As ever, adjustments must be made upon placing the calcinator.  Another mage might not be familiar with all of the primal sources, so I should temper my expectations when using a calcinator that they have placed.")
                .end()
            .build();
        this.researchEntry("CRYOTREATMENT").name("Cryotreatment")
            .stages()
                .add("This sea dust is really cold to the touch.  I wonder if I could use it to freeze things?")
                .add("Well, will you look at that.  It worked!<BR>Sprinkling a handful of sea dust into a bucket of water instantly freezes it into pure ice.  And doing the same with a bucket of lava hardens it into obsidian!<BR>Magick is so cool!")
                .end()
            .build();
        this.researchEntry("SANGUINE_CRUCIBLE").name("Sanguine Crucible")
            .stages()
                .add("I've learned a spell to conjure random animal life, but that isn't good enough for me.  I want more control over the types of life I create.  And I need more than simple livestock!<BR>This will require a deep understanding of blood magick and alchemy, but I am confident in my abilities.")
                .add("It's alive!  It's alive!  Well, it will be shortly, thanks to my new Sanguine Crucible.<BR>The crucible is made of pure hexium, and infused with a startling amount of magick.  A lesser material would never be able to contain this kind of power.  Regardless, the crucible provides its own heat and slowly fills itself with... let's just call it fluid; it doesn't bear to give that too much thought.<BR>The crucible is capable of spawning almost any kind of life I can imagine, at a price.  Configuring the crucible so that it knows what to spawn requires a Sanguine Core; I know how to make a blank one now, but attuning one to a specific creature type will require more research.<BR>In addition, animating that spawned life requires souls in the form of soul gems.  Thankfully, I already know the spell to acquire those.<BR>Once I have an attuned Sanguine Core and the necessary amount of soul gems, I will need to use the core on the crucible to slot it in place, and then throw the gems into the bubbling fluid.  A few seconds later, I'll be face to face with a spawned creature to do with as I see fit.")
                .end()
            .build();
        this.researchEntry("SANGUINE_CORE_LAND_ANIMALS").name("Sanguine Cores: Land Animals")
            .stages()
                .add("Most of the creatures that I can summon will be those of the land, simple animals.  But simple can still be valuable.")
                .add("I now know how to attune a Sanguine Core to simple land animal life.")
                .end()
            .build();
        this.researchEntry("SANGUINE_CORE_SEA_CREATURES").name("Sanguine Cores: Sea Creatures")
            .stages()
                .add("The creatures of the sea and the cold require their own type of Sanguine Core attunement.")
                .add("I now know how to attune a Sanguine Core to sea creature life.")
                .end()
            .build();
        this.researchEntry("SANGUINE_CORE_FLYING_CREATURES").name("Sanguine Cores: Flying Creatures")
            .stages()
                .add("I may not have encountered many flying creatures, but I may still want to spawn them with my Sanguine Crucible.")
                .add("I now know how to attune a Sanguine Core to flying creature life.")
                .end()
            .build();
        this.researchEntry("SANGUINE_CORE_PLANTS").name("Sanguine Cores: Plants")
            .stages()
                .add("Some of the strangest life in this world takes the form of animated plants and fungus.  I desire to spawn them as well.")
                .add("I now know how to attune a Sanguine Core to animated plant and fungal life.")
                .end()
            .build();
        this.researchEntry("SANGUINE_CORE_UNDEAD").name("Sanguine Cores: Undead")
            .stages()
                .add("Ah, the loathsome undead.  If only my Sanguine Crucible would let me control them as well as spawn them.  Alas, I will have to settle for spawning them as hostile entities.")
                .add("I now know how to attune a Sanguine Core to un-life.")
                .end()
            .build();
        this.researchEntry("SANGUINE_CORE_SAPIENTS").name("Sanguine Cores: Sapients")
            .stages()
                .add("Now this will be a fine trick.  To spawn not a mere animal, but a thinking, self-aware person!  I must learn how to do this!")
                .add("I now know how to attune a Sanguine Core to sapient life.")
                .end()
            .build();
        this.researchEntry("SANGUINE_CORE_DEMONS").name("Sanguine Cores: Demons")
            .stages()
                .add("The Nether teems with demons.  How ironic it would be to play into the mad wizard archetype by summoning one.")
                .add("I now know how to attune a Sanguine Core to demonic life.<BR>I should take care to remember, however, that the energies of the Overworld are anathema to many of their kind.  If summoned here, they will quickly transform into undead creatures.")
                .end()
            .build();
        this.researchEntry("SANGUINE_CORE_ALIENS").name("Sanguine Cores: Aliens")
            .stages()
                .add("Even the End has life, after a fashion.  I am not mad enough to attempt to spawn a dragon, but perhaps the rest of it will make a fine capstone to my spawning arts.")
                .add("I now know how to attune a Sanguine Core to alien life.")
                .end()
            .build();
        this.researchEntry("IGNYX").name("Ignyx")
            .stages()
                .add("My machines hunger for energy, and it's all I can do to find enough coal to keep them running.  There has to be a way to extend my stockpile with magick.")
                .add("Score another one for the alchemical arts!<BR>Infernal dust is brimming with heat and energy, so I thought, why not infuse some coal with it?  Turns out, though, that just burned my eyebrows off.  The material structure of the coal wasn't strong enough to contain that much thermal energy.  However, if I also infuse the coal with earth dust at the same time, that results in a stable product!<BR>Well, stable-ish.  It'll still explode if you throw it hard against something.  Curiously, though, whacking on a block of the stuff with a pickaxe seems perfectly safe.  Weird.<BR>In any case, all of this has yielded a fuel that burns hotter and longer than regular coal.  Eight times as long, to be precise.  This will extend my fuel stores nicely, so long as it's handled with care.")
                .end()
            .build();
        this.researchEntry("SYNTHETIC_GEM_BUDS").name("Synthetic Gem Buds")
            .stages()
                .add("Amethyst is a fascinating gem.  A precious mineral that grows almost like a plant.  But the budding blocks that they grow from are so fragile, they can't be moved, which means harvesting them always requires some travel.<BR>I wonder if I can replicate this feature with magick, in a way that I can have closer to home?")
                .add("I'm going to call this effort a qualified success.<BR>By carefully introducing the growing nature of the Sun, tempered with the ordered structure of the Earth, I can \"teach\" otherwise inert amethyst blocks how to bud.  Gems at home, great!<BR>Unfortunately, the process is imperfect.  Unlike their geode-bound kin, these synthetic amethyst buds will decay over time, eventually returning to an inert state.<BR>The good news is that I can simply reintroduce essence to the gems and get them growing again.  It will simply take some maintenance once in a while.")
                .end()
            .addenda()
                .add("Fascinating!  By substituting the magick of the Hallowed for the magick of the Sun, I can infuse growth even into gems that don't ordinarily bud.")
                .end()
            .build();
        this.researchEntry("BASIC_SORCERY").name("Basic Sorcery")
            .stages()
                .add("I think I'm getting the hang of the basics of Sorcery.  It's all about visualizing the effect one wants to see, shaping mana to fit that form, and then unleashing it.  The bad news is that casting a spell through pure force of will has so far proven impossible.<BR>Fortunately, I have tools to work with.  I've learned how to freeze my visualization of a spell in my mind, and then capture that vision in written form on a scroll.  I can then take that scroll, speak a simple word of power, and unleash those energies on demand.  The scroll will be consumed, but that's a small price to pay.<BR>As for what kind of spell to cast, I decided to first go with an incantation with which to defend myself.  By channeling Earth mana, I can create a damaging burst of energy that will also propel the target away from me.  Curiously, players like myself seem very resistant to sorcerous attacks, taking only half as much damage as another creature would.<BR>So far, the range of my spells is limited to what I can touch, which isn't ideal, but will do for now.  I can also channel the energy of a spell through my own body, but obviously that's not a great idea to use with a damaging spell like this.<BR>To create a spell, I'll need to use a Spellcrafting Altar like the one I've just designed.  Placing a blank spell scroll on the altar, I'll be able to select what vehicle the spell should take (e.g. a Touch spell) and what payload it should carry (e.g. Earth damage).  I suspect I'll also be able to select ways to modify the spell once I learn more, but for now that's it.<BR>Once the characteristics of the spell are settled, I will have to take a wand, place it on the altar, and use it to spend mana to scribe the spell onto a scroll.<BR>I suspect there are a wide variety of types of spells I'll be able to cast with more experience, so I'd better get practicing!")
                .end()
            .build();
        this.researchEntry("EXPERT_SORCERY").name("Expert Sorcery")
            .stages()
                .add("My skill with sorcery is growing, but I need more practice with it before I can get over this plateau I'm on.  Nothing to do but keep at it.")
                .add("I now feel comfortable calling myself an expert at sorcerous spellcasting.  Now I can start researching more advanced topics.  For starters, I'd like to do more with sorcery than just create damaging bursts.")
                .end()
            .build();
        this.researchEntry("MASTER_SORCERY").name("Master Sorcery")
            .stages()
                .add("My skill with sorcery is growing, but I need more practice with it before I can get over this plateau I'm on.  Nothing to do but keep at it.")
                .add("I've now mastered conventional sorcery and have started researching the forbidden topics that my Grimoire remains silent about.")
                .end()
            .build();
        this.researchEntry("SUPREME_SORCERY").name("Supreme Sorcery")
            .stages()
                .add("The top of the hill is in sight.  My skills with sorcery are almost without peer...  Almost.")
                .add("The most advanced topics in the discipline of sorcery are now at my fingertips.")
                .end()
            .build();
        this.researchEntry("COMPLETE_SORCERY").name("Completion of Sorcery")
            .stages()
                .add("I believe I've finally learned all that there is to know of sorcery.")
                .add("My completion of this discipline has attuned me further to the ways of magick.  I should turn my eye to the other disciplines and see if there remains anything for me to glean.")
                .end()
            .build();
        this.researchEntry("WAND_INSCRIPTION").name("Wand Inscription")
            .stages()
                .add("Now that I know how to make better wands, I think it's time to leave scrolls behind.  I mean, they work, but it would be nice to be able to cast a spell mutliple times without having the scroll burn up.")
                .add("I've devised a technique for inscribing a written spell onto the core of a wand.  This will consume the scroll, but allow me to cast the spell as many times as I can afford the mana cost.<BR>A wand can only have so many spells inscribed onto it, determined by the wand's core.  I can change the wand's active spell list by pressing R, by default, and selecting a spell from the radial menu.<BR>Should I become dissatisfied with my wand's spells, I can clear it of all of them by placing it onto the Wand Inscription Table without a scroll.")
                .end()
            .build();
        this.researchEntry("SPELL_VEHICLE_PROJECTILE").name("Spell Type: Projectile")
            .stages()
                .add("Casting spells on things right next to you can be very risky.  I'd very much like to be able to affect objects and creatures from a distance, where it's safe.")
                .add("I can now take a spell, bind it to a ball of energy, and hurl that ball at a ranged target with relative ease.  This will make fighting enemies with spells much safer for me, if a bit more expensive mana-wise.<BR>Curiously, despite being pure energy, these spell projectiles are nonetheless affected by gravity.  I'll have to aim carefully when casting them.")
                .end()
            .build();
        this.researchEntry("SPELL_VEHICLE_BOLT").name("Spell Type: Bolt")
            .stages()
                .add("Gravity and travel time are for rubes.  A mage of my skill can surely transcend these constraints with the proper application of knowledge!")
                .add("I've learned how to refine my spells into bolts of energy.  These bolts propagate at the speed of thought and are not affected by gravity.<BR>The downside of these bolts is that their range barely more than that of a touch spell.  I can increase it to up to sixteen blocks, but that increases the cost of the spell.<BR>Tradeoffs will have to be made.")
                .end()
            .build();
        this.researchEntry("SPELL_PAYLOAD_FROST").name("Spell Payload: Frost")
            .stages()
                .add("Presumably I can use Sea mana to cast damaging spells in addition to Earth mana.  I wonder what form it will take?")
                .add("By using Sea mana in a damage spell, I end up with a burst of intense cold.  The potential damage seems to be the same as for an Earth spell, but rather than knocking the target back, a Frost spell will coat the target in rime, slowing it down.<BR>The strength of the slowing effect is proportional to the damage of the spell, and its duration can be configured separately.")
                .end()
            .build();
        this.researchEntry("SPELL_PAYLOAD_LIGHTNING").name("Spell Payload: Lightning")
            .stages()
                .add("One of the most iconic attack spells in lore is the lightning bolt, right?  Let's see if I can recreate that with Sky mana.")
                .add("It's not quite a lightning bolt yet, but Sky mana will indeed produce a powerful shock to anyone on the receiving end.<BR>Curiously, the damage of the lightning seems to scale differently at different power levels than for Earth damage.  Lower power levels will result in less damage than a comparable Earth spell, but higher power levels will hit harder.")
                .end()
            .build();
        this.researchEntry("SPELL_PAYLOAD_SOLAR").name("Spell Payload: Solar")
            .stages()
                .add("Sun mana doesn't normally lend itself to harming things, but anyone who's seen a desert knows it can be done.")
                .add("Using Sun mana in a damaging spell results in an intense burst of solar energy.  The damage is comparable to an Earth spell, but instead of knocking the target back it makes them glow for a few seconds.  In addition, the undead will be lit on fire, just as if they were exposed to daylight.<BR>The duration of the glow and burn effects can be configured separately from the spell's power.")
                .end()
            .build();
        this.researchEntry("SPELL_PAYLOAD_LUNAR").name("Spell Payload: Lunar")
            .stages()
                .add("The fickle Moon can hurt things as well.  I'm curious to see how it works.")
                .add("Apparently powerful bursts of lunar energy sap the strength from creatures.  Who knew?  A Moon damage spell will do comparable damage to an Earth spell, but will also weaken the target instead of knocking it back.<BR>The strength of the weakening effect is proportional to the damage of the spell, and its duration can be configured separately.")
                .end()
            .build();
        this.researchEntry("SPELL_PAYLOAD_BLOOD").name("Spell Payload: Blood")
            .stages()
                .add("Most living things have some kind of blood in them.  Let's see if a Blood mana based attack spell can exploit that.")
                .add("I've discovered that the right Blood spell will rend and tear the insides of any creature unfortunate enough to be hit by it.  The damage is less than that of an Earth spell, but because it harms the creature from within, it bypasses any armor that it may have.")
                .end()
            .build();
        this.researchEntry("SPELL_PAYLOAD_FLAME").name("Spell Payload: Flame")
            .stages()
                .add("I've been waiting for this: fire.  Surely that's what an Infernal damage spell will unleash upon my foes.  I can't wait to see it in action.")
                .add("Sure enough, Infernal mana will conjure roaring flames to incinerate my enemies.  The initial damage of the spell is comparable to an Earth damage spell, but rather than knocking it back, the unfortunate target is immolated for a few seconds.<BR>The duration of the lasting fire can be configured separately from the spell's power.<BR>I should be careful with this one, however; many creatures out there are immune to fire and if I try to cast this at them, I'll just get laughed at.")
                .end()
            .build();
        this.researchEntry("SPELL_PAYLOAD_VOID").name("Spell Payload: Void")
            .stages()
                .add("I'd wager good emeralds that the consumptive nature of Void mana will lend itself well to damaging spells.")
                .add("As I suspected, the Void is quite harmful to those exposed to bursts of it.  The damage of the main spell is about the same as that of an Earth spell, but the Void lingers in the form of a withering effect, causing extra damage over time to the target.<BR>The strength of this withering effect is proportional to the initial damage of the spell, and its duration can be configured separately.")
                .end()
            .build();
        this.researchEntry("SPELL_PAYLOAD_HOLY").name("Spell Payload: Holy")
            .stages()
                .add("The nature of the Hallowed is primarily to create, but death is a part of the great cycle of the universe as well.")
                .add("While it feels wrong, the Hallowed can kill as well as any of the other primal sources.  The damage of a Holy spell is comparable to that of an Earth spell, with the exception of the undead.  Being perversions of the cycle of the universe, they take double damage from Holy spells.")
                .end()
            .build();
        this.researchEntry("SPELL_PAYLOAD_BREAK").name("Spell Payload: Break")
            .stages()
                .add("Damaging creatures is useful and all, but sometimes I need to knock down a wall, and that's really tiring.  Maybe I can use Earth magick to break blocks in addition to knocking mobs around.")
                .add("Through the judicious application of Earth magick, I'm now able to erode the material structure of almost any solid block.<BR>The energy of the Break spell will gradually damage the block, just as if I were striking it with a tool.  This process is not instantaneous and harder blocks will take longer to break, but I can increase the speed of the breaking by increasing the spell's power.<BR>This spell has no effect when cast on living creatures.")
                .end()
            .build();
        this.researchEntry("SPELL_PAYLOAD_CONJURE_STONE").name("Spell Payload: Conjure Stone")
            .stages()
                .add("Stone may not be hard to come by in the Overworld, but sometimes I need some *right now* and just don't have any.  Maybe I can use Earth magick to conjure some from pure mana.")
                .add("I can now bring raw earth into being through only my will and some mana.  It's indistinguishable from the real thing, too!  Maybe I can use this to create some platforms, that'd be cool.")
                .end()
            .build();
        this.researchEntry("SPELL_PAYLOAD_CONJURE_WATER").name("Spell Payload: Conjure Water")
            .stages()
                .add("Water behaves curiously in the Overworld; with the right placement, an infinite amount of it can be formed.  Can I replicate this effect with magick?")
                .add("I've now learned how to create a source block of water from nothing but Sea mana.  By casting this spell, I can place a block of water anywhere I like, just as if I'd dumped it from a bucket.<BR>This spell is not without limits, however.  An extremely high ambient temperature, such as found in the Nether, will cause the spell to fail.")
                .end()
            .build();
        this.researchEntry("SPELL_PAYLOAD_SHEAR").name("Spell Payload: Shear")
            .stages()
                .add("Breaking things is all well and good, but sometimes I need to be a little more... precise.  A scalpel instead of a sledgehammer.  Let's see what magick can provide.")
                .add("With the careful application of cutting winds, I can now simulate the effect of shears on a target.  It won't damage creatures, but it will shear a sheep, harvest some leaves, or disarm a tripwire without damaging the rest of the environment.  Niche, but useful.")
                .end()
            .build();
        this.researchEntry("SPELL_PAYLOAD_FLIGHT").name("Spell Payload: Flight")
            .stages()
                .add("Ah, to soar through the sky like a bird.  Is there any more universal dream among humankind?  Perhaps Sky magick is the key to realizing that dream.")
                .add("It's not quite the same as a bird's flight, but magick has opened the skies to me regardless.<BR>By casting this spell, I imbue the target with a fleeting levitation effect.  A creature under this effect can activate it by trying to jump while already in the air.  They will stay at their current elevation as if stuck there by a pin!<BR>Moving while levitating so is as simple as walking.  Ascend by holding the jump button and descend by holding the sneak button.<BR>It's worth re-iterating that this effect is short-lived; even the mightiest mage cannot stay aloft in this way for so much as a minute.  And once the effect ends, gravity will reach out and grab you once more.")
                .end()
            .build();
        this.researchEntry("SPELL_PAYLOAD_CONJURE_LIGHT").name("Spell Payload: Conjure Light")
            .stages()
                .add("Darkness is dangerous in this world, so I would best be prepared to counter it.  A simple cantrip to conjure some light would be a highly utile spell.")
                .add("I've now learned how to create a field of light anywhere I wish.<BR>The light is bright, but will slowly fade over time.  Useful for exploring, but this won't replace my need for lighting fixtures at home.")
                .end()
            .build();
        this.researchEntry("SPELL_PAYLOAD_HEALING").name("Spell Payload: Healing")
            .stages()
                .add("Nurturing, growing, healing, that's what the Sun is truly for, not random blasts of damaging energy.  Let's see about making that magick a reality.")
                .add("I can now heal wounds with but a few words and gestures.  If that's not magickal, then what is?<BR>Should I attempt to heal myself beyond my maximum health capacity, then, if the overhealing is sufficient, I will be granted a temporary absorption shield.<BR>As with most healing effects, this is anathema to the undead.  They will be damaged proportionately to the spell's power when affected by it.")
                .end()
            .build();
        this.researchEntry("SPELL_PAYLOAD_POLYMORPH").name("Spell Payload: Polymorph")
            .stages()
                .add("Now this could be fun...  A little trick of the Moon to change something's shape?  It just might work!")
                .add("With the blessing of the Moon, I can now transform almost any living (or undead) entity into its favored creature: a wolf.  The transformation is not permanent, but it should give me time to beat a hasty retreat, or just have a good laugh.<BR>While it is technically possible to tame a polymorphed wolf, there isn't much point.  When the target of the spell reverts to its original form, it won't think any more highly of me than it did before being shape changed.")
                .end()
            .build();
        this.researchEntry("SPELL_PAYLOAD_POLYMORPH_SHEEP").name("Spell Payload: Polymorph - Sheep")
            .stages()
                .add("With the secrets I learned from that witch, I can now transform almost any living (or undead) creature into a sheep using Moon magick!  The transformation is not permanent, but I should have enough time to shear myself a little wool.<BR>That witch seemed nice.  I'll miss her.")
                .end()
            .build();
        this.researchEntry("SPELL_PAYLOAD_CONJURE_ANIMAL").name("Spell Payload: Conjure Animal")
            .stages()
                .add("The creation of new life, now that is power.  Let us see if I can exercise that power with the might of the Blood.")
                .add("I can now create living, breathing creatures from nothing but Blood mana and raw will.<BR>The spell is somewhat... chaotic, however.  While the creature will exist in this world for as long as it naturally lives, I have very limited control over what *type* of creature emerges from my incantations.  Air-breathing animals will be created if the target point is on the land, sea creatures if it's in the water, but that's it.<BR>Also, this spell can only create a single creature at a time; it is incompatible with the Burst spell mod and will fail if I attempt to combine the two.")
                .end()
            .build();
        this.researchEntry("SPELL_PAYLOAD_CONJURE_LAVA").name("Spell Payload: Conjure Lava")
            .stages()
                .add("If I can conjure water sources with my magick, why not lava?  Sure, it's incredibly dangerous to attempt, but surely a mage of my skill can overcome that.")
                .add("I've now learned how to create a source block of lava from nothing but Infernal mana.  Specifically, a *lot* of Infernal mana.<BR>By casting this spell, I can place a block of lava anywhere I like, just as if I'd poured it out from a bucket.  Naturally, it will flow just like lava, so I should take care where I put it.")
                .end()
            .build();
        this.researchEntry("SPELL_PAYLOAD_DRAIN_SOUL").name("Spell Payload: Drain Soul")
            .stages()
                .add("Whether you call it a soul, chi, life energy, or something else, all living (and undead) creatures have some.  And I predict that this resource will prove very useful for my continued workings.")
                .add("With some effort, I can now extract a portion of a creature's soul using this spell.<BR>First, I cast the spell at my target.  For a few seconds thereafter, it's soul will be 'pinned' to this world with magick.  If I then kill the creature, the soul energy will make itself manifest in the form of gems that I can collect.<BR>The number of gems, or slivers of gems, that I receive will be proportionate to the maximum health of the creature before it died.  Peaceful creatures will yield much fewer soul fragments than dangerous ones.")
                .end()
            .build();
        this.researchEntry("SPELL_PAYLOAD_TELEPORT").name("Spell Payload: Teleport")
            .stages()
                .add("Having seen creatures of the End fold space to step between points, I covet that ability.  Let me make it real for myself through my magick!")
                .add("Through the power of the Void, I can now teleport as the creatures of the End do.<BR>After casting this spell, I will instantaneously appear at whatever point it impacts.  And there appear to be no lasting impacts on my physiology, either!<BR>As this spell is only meant to teleport myself to a single point, I have made it incompatible with the Burst spell mod.  Attempting to combine the two will fail safely.  The alternative would be... messy.")
                .end()
            .build();
        this.researchEntry("SPELL_PAYLOAD_CONSECRATE").name("Spell Payload: Consecrate")
            .stages()
                .add("With all the dangers in the world, I wonder if the universe would allow me to create a space of safety?")
                .add("Through the Hallowed, I can now consecrate small spaces with the song of the universe.  Standing in these zones will heal my wounds and fill my belly, not to mention enhance my calm.<BR>Other creatures will find themselves unable to enter these consecrated spaces.  The exception to this is other players, whose free will allow themselves to move where they please with impunity.")
                .end()
            .build();
        this.researchEntry("SPELL_MOD_AMPLIFY").name("Spell Mod: Amplify")
            .stages()
                .add("It seems that spells have a relatively narrow range of power available to them.  Is this really the best I can do?")
                .add("So it turns out that throwing mana at the problem will let you bypass these limits, to a point.<BR>Any spell can have an Amplify mod attached to it.  Amplification will increase the Power and Duration of any other spell payloads or mods by the power of the Amplify mod itself.<BR>Amplifying a spell in this way will greatly increase its mana cost, but the effects can't be argued with.<BR>Only the Amplify mod with the greatest Power value will be considered for a single spell.")
                .end()
            .build();
        this.researchEntry("SPELL_MOD_BURST").name("Spell Mod: Burst")
            .stages()
                .add("Spells so far just seem to affect a single target at a time.  But what if I'm facing down a whole raiding party of pillagers?  What do I do then?")
                .add("Sometimes, explosions are the best answer.  And while this isn't quite that, it's the next best thing.<BR>A Burst spell will affect all targets within a certain range of the point of impact, be they blocks or creatures.  The range of this effect is controlled by the Burst mod's Radius config value, while it's Power value determines how well the burst penetrates intervening blocks.<BR>The effect is very mana-intensive, however, and will greatly increase the spell's cost.<BR>Some spell payloads are not compatible with the Burst mod and will fail to activate if the two are combined; this will be noted in the payload's Grimoire entry.<BR>Only the Burst mod with the highest Radius value will be considered for a single spell.")
                .end()
            .build();
        this.researchEntry("SPELL_MOD_QUICKEN").name("Spell Mod: Quicken")
            .stages()
                .add("It generally takes a few seconds to recover from casting a spell, be it from a scroll or a wand.  These seconds can be the difference between life or death in a duel.")
                .add("By applying the Quicken mod to a spell, I can decrease the amount of recovery time needed between spellcasts.  Every point of Haste value applied to the mod will decrease that recovery time by a quarter second, down to a minimum of a quarter second recovery time.<BR>Any spell can be affected by a Quicken mod, but it greatly increases the mana cost of the spell, so I should be careful not to run out my mana reserves too quickly.<BR>Only the Quicken mod with the highest Haste value will be considered for a single spell.")
                .end()
            .build();
        this.researchEntry("SPELL_MOD_MINE").name("Spell Mod: Mine")
            .stages()
                .add("Sometimes, rather than attack a creature directly, I would prefer to lay a trap.  Maybe that can be arranged.")
                .add("I've learned how to take the energy of a spell and delay its effect in the form of a Mine.  Rather than take effect immediately, a Mine will fade from view and wait for the first creature to approach it.  When that happens, the spell will be unleashed on that creature.<BR>These Mines are not permanent, but will last for a few minutes per point of Duration applied to the mod.  And though it should go without saying, these Mines only affect creatures; blocks will not trigger them, and so block-only spells are wasted in them.<BR>It's also worth noting that Mines have an arming time of a few seconds.  During this time, they are fully visible and will not trigger their loaded spell even if touched.<BR>Unlike most spell mods, Mine only slightly increases the cost of a spell, rather than greatly so.<BR>Only the Mine mod with the greatest Duration value will be considered for a single spell.")
                .end()
            .build();
        this.researchEntry("SPELL_MOD_FORK").name("Spell Mod: Fork")
            .stages()
                .add("On occasion, casting a single spell at a time is simply insufficient.  Can the universe be persuaded to let a humble mage do two things at once?")
                .add("The universe smiles upon me once more, and my knowledge increases.<BR>The Fork mod will indeed let me cast a spell multiple times in the same instant.  It's not perfect, though.  This warping of time and space causes the spells to bend from their intended path.<BR>I can mitigate this, to a point, by increasing the Precision value of the mod; each point will bring the forks back closer to true.  And increasing the Forks value of the mod will increase the number of times the spell is cast, at its full potency.<BR>Forking a spell in this fashion will greatly increase its mana cost.<BR>Only the Fork mod with the highest Forks value will be considered for a single spell.")
                .end()
            .build();
        this.researchEntry("BASIC_MANAWEAVING").name("Basic Manaweaving")
            .stages()
                .add("I think I'm starting to get the hang of Manaweaving.  It's just like normal crafting, but you need a wand on hand in order to channel mana into the pieces sometimes.<BR>So far I've come up with a simple design that uses quartz to separate mana into streams of individual sources!  It's small, but I'm proud of myself.")
                .end()
            .build();
        this.researchEntry("EXPERT_MANAWEAVING").name("Expert Manaweaving")
            .stages()
                .add("My skill with manaweaving is growing, but I need more practice with it before I can get over this plateau I'm on.  Nothing to do but keep at it.")
                .add("I now feel comfortable calling myself an expert at manaweaving.  Now I can start researching more advanced topics.  To start with, I've devised a recipe for Enchanted Marble.  It's both harder and more explosion-resistant than regular marble.")
                .end()
            .build();
        this.researchEntry("MASTER_MANAWEAVING").name("Master Manaweaving")
            .stages()
                .add("My skill with manaweaving is growing, but I need more practice with it before I can get over this plateau I'm on.  Nothing to do but keep at it.")
                .add("I've now mastered conventional manaweaving and have started researching the forbidden topics that my Grimoire remains silent about.")
                .end()
            .build();
        this.researchEntry("SUPREME_MANAWEAVING").name("Supreme Manaweaving")
            .stages()
                .add("The top of the hill is in sight.  My skills with manaweaving are almost without peer...  Almost.")
                .add("The most advanced topics in the discipline of manaweaving are now at my fingertips.")
                .end()
            .build();
        this.researchEntry("COMPLETE_MANAWEAVING").name("Completion of Manaweaving")
            .stages()
                .add("I believe I've finally learned all that there is to know of manaweaving.")
                .add("My completion of this discipline has attuned me further to the ways of magick.  I should turn my eye to the other disciplines and see if there remains anything for me to glean.")
                .end()
            .build();
        this.researchEntry("WAND_CHARGER").name("Mana Charger")
            .stages()
                .add("Running back and forth between shrines to charge my wand at the ancient fonts is getting really old, really fast.  There has to be a better way to do this.")
                .add("I've created a simple device that will break down magickal essence and convert it into raw mana, which I can in turn feed into my wand.<BR>I just need to place my wand inside and deposit some essence.  The essence will slowly be broken down into mana.<BR>It's not a fast process, but I think this will let me get a lot more work done.")
                .end()
            .build();
        this.researchEntry("MANA_SALTS").name("Mana Salts")
            .stages()
                .add("The Grimoire has hinted at a substance that may prove useful in my continued works.  Chemistry is fun, right?")
                .add("Take equal parts of refined salt, energetic redstone dust, and a magickal essence dust.  Mix vigorously.  Let sit at room temperature.<BR>The result appears to be called Mana Salts and it stores a great deal of energy, both physical and magickal.  I'm not sure what it's for yet, but I have faith that time will tell.")
                .end()
            .build();
        this.researchEntry("ADVANCED_WANDMAKING").name("Advanced Wandmaking")
            .stages()
                .add("I'm torn.  On one hand, my wand is a miracle like I'd never before seen.  Real magick!  On the other hand, it seems... limited.<BR>I mean, let's be honest here, it's a stick with some magick dust rubbed into its grain.  It can't hold much mana, and it only reacts with a very few things.  I can't even cast spells with it!<BR>I'm never going to become a real mage unless I get myself a better wand.")
                .add("Okay, I've thought about this a lot, and I'm certain that it's possible for me to make a better wand than what I have now.  After studying my Grimoire, I've concluded that my next wand needs to be made of multiple parts.<BR>First, it needs a body, a core.  I need something to hold on to, after all.  And that core has to be more receptive to magick than a simple stick.<BR>Second, I need a better way to channel mana into and out of the wand.  A pair of metal caps, maybe.<BR>Third, it needs a structure to actually hold all of the mana that I want to channel.  Something a little ostentatious, maybe, like a small gem.<BR>Each of those parts will require more research individually.  But in the meantime, I've put together a design for a table where I can assemble those parts once I have them.")
                .end()
            .build();
        this.researchEntry("STAVES").name("Magickal Staves")
            .stages()
                .add("What's a more iconic accessory for a mage than a magick staff?  Ooh, I can't wait to figure out how to make one.")
                .add("So it turns out that magick staves aren't that much different from magick wands, and are assembled in much the same way.  The primary difference is that it uses a staff core instead of a wand core.<BR>This has some side effects.  On the plus side, a staff can be inscribed with twice as many spells as a wand of similar make.  Also, it can be used as a basic melee weapon in a pinch.  On the down side, a staff is too large to fit in most crafting tables and devices.<BR>I've drawn up plans for staff versions of all of the wand core types I know how to make.  Details can be found in their respective Grimoire entries.")
                .end()
            .build();
        this.researchEntry("WAND_CORE_HEARTWOOD").name("Wand Core: Heartwood")
            .stages()
                .add("In studying my Grimoire, I've found mention of a material it calls Heartwood.  Apparently, it's a special type of living wood that is especially receptive to magick.<BR>I think this would make an excellent material for the core of my next wand.  I should acquire some Heartwood and study it.<BR>Unfortunately, Heartwood can only be rarely found by chopping down sunwood or moonwood trees.  And if I don't like those odds, there's only one other way to get it: killing Treefolk and ripping it from their chests.  They're not going to like that...")
                .add("Yes, Heartwood will indeed make a suitable core for a wand.  Carving and joining two pieces of it should suffice.<BR>A Heartwood wand core can hold a single inscribed spell, no more.  It has no special primal alignments that would affect the mana costs of anything.<BR>I can't say I care for working with this material.  Finding it in trees is a huge hassle, and I feel bad for those poor Treefolk.  They don't bother anyone.  I wonder if there's anything else I can make a wand core from that would work better.")
                .end()
            .addenda()
                .add("Heartwood will also make a suitable core for a staff.  As with all staves, it can be inscribed with double the normal amount of spells of its corresponding wand core.  So two.")
                .end()
            .build();
        this.researchEntry("WAND_CAP_IRON").name("Wand Cap: Iron")
            .stages()
                .add("After another round of Grimoire study, I think I'm going to start by making wand caps out of iron.  I have some concerns, but it's what I've got.")
                .add("I'll call this a partial success.<BR>The caps will fit, certainly, but they aren't as conductive as I'd hoped.  A wand capped with iron will incur a ten percent penalty on all mana costs.<BR>That may sound bad, but it's still an improvement!  By my calculations, an uncapped wand such as my current one incurs a *twenty* percent penalty.  So hey, I'll take it.<BR>Just remember, self, you'll need to make two caps, one for each end of the wand.")
                .end()
            .build();
        this.researchEntry("WAND_GEM_APPRENTICE").name("Wand Gem: Apprentice")
            .stages()
                .add("Further study suggests that to hold larger amounts of mana, I want something with a regular crystalline structure.  This has the nice side effect of getting to add a little bling to my new wand.")
                .add("A diamond will be perfect as the final component of my new wand.  Perfect, but for one small detail: diamonds are magickally inert.<BR>Thankfully, if my research is correct, that's fixable.  I just need to imbue the gem with some magickal essence to make it more receptive to mana.<BR>By my calculations, a diamond so infused will hold up to seventy-five points of mana from each source, fully triple what my current wand holds!")
                .end()
            .build();
        this.researchEntry("EARTHSHATTER_HAMMER").name("Earthshatter Hammer")
            .stages()
                .add("I go through a lot of metal ore.  Like, a LOT of it.  There has to be some way I can increase my mineral yields through magick.")
                .add("By infusing a hammer with Earth magick, I've created a tool with which to increase my mineral yields.<BR>I need only combine it with the ore or raw metal to get back purified grit, which can be smelted in a furnace for double the normal amount of metal!  The hammer even swings itself, so I don't have to worry about my arms getting tired.  Pretty cool!")
                .end()
            .build();
        this.researchEntry("SUNLAMP").name("Sunlamp")
            .stages()
                .add("Darkness is dangerous here in the Overworld, and it takes a lot of torches to keep my base safe.  Maybe Sun magick will let me be more efficient with my lighting?")
                .add("With the help of some magickal materials, I've succeeded in creating a device I call a Sunlamp.<BR>Not only does the Sunlamp shine brightly itself, but it's enchanted to spread that light.  It will periodically find dark spaces nearby and create a small glow field within them, extending its light even further.<BR>They can be attached to walls or ceilings, or simply left on the ground.  Removing a sunlamp will also remove the glow fields that it's created.")
                .end()
            .addenda()
                .add("The techniques used to create a sunlamp can also be used to spread the light of soul fire.  A Spirit Lantern not only contains the aesthetically-pleasing cool blue flame of soul fire, but its light will repel piglins.  This will be useful for securing territory in the Nether.")
                .end()
            .build();
        this.researchEntry("WAND_GEM_ADEPT").name("Wand Gem: Adept")
            .stages()
                .add("Based on my experiences with my current wand gem, I'd wager that it's possible to increase its capacity by using stronger essence.  Let's find out!")
                .add("By infusing a diamond with the magick of an essence shard, I've more than tripled the capacity of my previous works!  This new wand gem can hold 250 points of mana of each source, which should tide me over nicely for quite a while.<BR>Unfortunately, the process can't be applied to existing wand gems.  I'll need to find a fresh diamond and construct a new wand to take advantage of this discovery.")
                .end()
            .build();
        this.researchEntry("WAND_GEM_WIZARD").name("Wand Gem: Wizard")
            .stages()
                .add("I need more mana!  My current wand does not hold nearly enough for my purposes.<BR>My work must continue, and for that I'll need to research how to cram more energy into that crystalline structure.")
                .add("This will do, yes.  With stronger essence and a more refined creation process, I've created an even better wand gem.<BR>This new gem will hold 750 points of mana of each source, once again tripling the capacity of the previous version.  A fine excuse to construct a new wand.")
                .end()
            .build();
        this.researchEntry("WAND_GEM_ARCHMAGE").name("Wand Gem: Archmage")
            .stages()
                .add("The greatest efforts require the greatest of mana stores.  Do I possess the wisdom necessary to realize this humble diamond's full potential?")
                .add("The combination of a diamond with the purest of essence clusters has worked a wonder.<BR>Realized so, this new wand gem can hold a staggering 2,500 points of mana of each source, more than triple that of its previous incarnation.")
                .end()
            .build();
        this.researchEntry("WAND_CAP_GOLD").name("Wand Cap: Gold")
            .stages()
                .add("So the iron technically worked, but it has its flaws.  I wonder if gold would work better?<BR>After all, it holds more enchantments than iron when I use it in the enchanting table.")
                .add("Why didn't I think of this in the first place?  This is great.<BR>These gold caps lack the mana resistance of the iron version, so there's no penalty when channeling.  I can't wait to put them on a new core!")
                .end()
            .build();
        this.researchEntry("WAND_CAP_PRIMALITE").name("Wand Cap: Primalite")
            .stages()
                .add("That new Primalite metal I formulated is pretty nifty.  I wonder how it would work as a wand cap?")
                .add("Now this is what I'm talking about!  The magickal nature of Primalite makes it ideal for conducting mana.<BR>When used as a wand cap, not only is there no penalty when channeling mana, but it yields a five percent *bonus*.  It will now take me even less mana to accomplish the same tasks!")
                .end()
            .build();
        this.researchEntry("WAND_CAP_HEXIUM").name("Wand Cap: Hexium")
            .stages()
                .add("The raw power of Hexium would make a fitting focus for my might.  It's simply a matter of working out the details.")
                .add("Good, good...  These new wand caps are even more efficient than before.<BR>When channeling mana, I can expect to see a ten percent reduction in the mana cost of my workings.  As it should be.")
                .end()
            .build();
        this.researchEntry("WAND_CAP_HALLOWSTEEL").name("Wand Cap: Hallowsteel")
            .stages()
                .add("I've grown distrustful of the Hexium in my tools.  It's a metal of great power, but also great darkness, and I fear that taints everything it touches.<BR>Perhaps it is time to start anew.")
                .add("This was a wise decision.  Hallowsteel is perhaps the finest conducter of mana that I've ever seen.<BR>When using it as a wand cap, I will see a fifteen percent reduction in all my mana costs.  Not to mention the peace of mind that comes with wielding a tool blessed by the universe.")
                .end()
            .build();
        this.researchEntry("WAND_CORE_OBSIDIAN").name("Wand Core: Obsidian")
            .stages()
                .add("If my calculations are correct, obsidian might also make a good wand core, one with some interesting properties.  It seems worth trying out.")
                .add("Fascinating!  This wand core has a solid heft to it, and I can feel the metaphysical weight of the Earth behind it.  This alignment to the Earth comes with a few benefits, it seems.<BR>First, an obsidian wand core can be inscribed with an extra spell, so long as that spell is Earth-based.<BR>Second, it channels Earth mana more easily, resulting in a five percent mana cost reduction.<BR>Finally, and perhaps most usefully, an obsidian wand core will slowly draw small amounts of Earth magick from the environment, refilling its wand gem over time.<BR>This will be a significant upgrade for me, should I find myself using Earth magick a lot.")
                .end()
            .addenda()
                .add("Obsidian will also make a suitable core for a staff.  As with all staves, it can be inscribed with double the normal amount of spells, though the bonus Earth spell slot is not doubled.  In total, that makes two of any type of spell plus one Earth spell.")
                .end()
            .build();
        this.researchEntry("WAND_CORE_CORAL").name("Wand Core: Coral")
            .stages()
                .add("If my calculations are correct, coral might also make a good wand core, one with some interesting properties.  It seems worth trying out.")
                .add("Intriguing.  I'll definitely need to wear gloves when handling this, on account of the sharp bits, but I can feel the vastness of the Sea when I hold it.  This alignment comes with some benefits.<BR>First, a coral wand core can be inscribed with an extra spell, so long as that spell is Sea-based.<BR>Second, it channels Sea mana more easily, resulting in a five percent mana cost reduction.<BR>Finally, and perhaps most usefully, a coral wand core will slowly draw small amounts of Sea magick from the environment, refilling its wand gem over time.<BR>This will be a significant upgrade for me, should I find myself using Sea magick a lot.")
                .end()
            .addenda()
                .add("Coral will also make a suitable core for a staff.  As with all staves, it can be inscribed with double the normal amount of spells, though the bonus Sea spell slot is not doubled.  In total, that makes two of any type of spell plus one Sea spell.")
                .end()
            .build();
        this.researchEntry("WAND_CORE_BAMBOO").name("Wand Core: Bamboo")
            .stages()
                .add("If my calculations are correct, bamboo might also make a good wand core, one with some interesting properties.  It seems worth trying out.")
                .add("Hollow and light, the Sky runs through the center of this wand core, and it is so aligned.  This alignment comes with some benefits.<BR>First, a bamboo wand core can be inscribed with an extra spell, so long as that spell is Sky-based.<BR>Second, it channels Sky mana more easily, resulting in a five percent mana cost reduction.<BR>Finally, and perhaps most usefully, a bamboo wand core will slowly draw small amounts of Sky magick from the environment, refilling its wand gem over time.<BR>This will be a significant upgrade for me, should I find myself using Sky magick a lot.")
                .end()
            .addenda()
                .add("Bamboo will also make a suitable core for a staff.  As with all staves, it can be inscribed with double the normal amount of spells, though the bonus Sky spell slot is not doubled.  In total, that makes two of any type of spell plus one Sky spell.")
                .end()
            .build();
        this.researchEntry("WAND_CORE_SUNWOOD").name("Wand Core: Sunwood")
            .stages()
                .add("If my calculations are correct, sunwood might also make a good wand core, one with some interesting properties.  It seems worth trying out.")
                .add("Gently warm to the touch, this wand core casts a very faint light.  It is clearly aligned to the Sun, and that comes with some benefits.<BR>First, a sunwood wand core can be inscribed with an extra spell, so long as that spell is Sun-based.<BR>Second, it channels Sun mana more easily, resulting in a five percent mana cost reduction.<BR>Finally, and perhaps most usefully, a sunwood wand core will slowly draw small amounts of Sun magick from the environment, refilling its wand gem over time.<BR>This will be a significant upgrade for me, should I find myself using Sun magick a lot.")
                .end()
            .addenda()
                .add("Sunwood will also make a suitable core for a staff.  As with all staves, it can be inscribed with double the normal amount of spells, though the bonus Sun spell slot is not doubled.  In total, that makes two of any type of spell plus one Sun spell.")
                .end()
            .build();
        this.researchEntry("WAND_CORE_MOONWOOD").name("Wand Core: Moonwood")
            .stages()
                .add("If my calculations are correct, moonwood might also make a good wand core, one with some interesting properties.  It seems worth trying out.")
                .add("The protean nature of this moonwood wand core is slightly disturbing.  I swear I can almost feel it changing shape under my fingers.  Nonetheless, it's clearly aligned with the Moon, and that comes with some benefits.<BR>First, a moonwood wand core can be inscribed with an extra spell, so long as that spell is Moon-based.<BR>Second, it channels Moon mana more easily, resulting in a five percent mana cost reduction.<BR>Finally, and perhaps most usefully, a moonwood wand core will slowly draw small amounts of Moon magick from the environment, refilling its wand gem over time.<BR>This will be a significant upgrade for me, should I find myself using Moon magick a lot.")
                .end()
            .addenda()
                .add("Moonwood will also make a suitable core for a staff.  As with all staves, it can be inscribed with double the normal amount of spells, though the bonus Moon spell slot is not doubled.  In total, that makes two of any type of spell plus one Moon spell.")
                .end()
            .build();
        this.researchEntry("WAND_CORE_BONE").name("Wand Core: Bone")
            .stages()
                .add("The terrestrial sources of magick all have wand cores aligned to them, why not Blood?  It shouldn't be too difficult to find something which fills that niche.")
                .add("In the end, the choice of material for a Blood-aligned wand core was obvious: bone.  It's hard, straight, well-sized, and has the right mojo.<BR>Unlike lesser materials, a bone wand core can be inscribed with two spells, in addition to a bonus Blood spell.<BR>It still provides a five percent Blood mana discount on all workings, and will harvest Blood mana from the environment to refill its wand gem.<BR>All in all, the makings of a fine tool.")
                .end()
            .addenda()
                .add("Bone will also make a suitable core for a staff.  As with all staves, it can be inscribed with double the normal amount of spells, though the bonus Blood spell slot is not doubled.  In total, that makes four of any type of spell plus one Blood spell.")
                .end()
            .build();
        this.researchEntry("WAND_CORE_BLAZE_ROD").name("Wand Core: Blaze Rod")
            .stages()
                .add("The terrestrial sources of magick all have wand cores aligned to them, why not Infernal?  It shouldn't be too difficult to find something which fills that niche.")
                .add("Well, it's exotic and requires some thick gloves, but a blaze rod definitely makes for a good Infernal-aligned wand core.<BR>Unlike lesser materials, a blaze rod wand core can be inscribed with two spells, in addition to a bonus Infernal spell.<BR>It still provides a five percent Infernal mana discount on all workings, and will harvest Infernal mana from the environment to refill its wand gem.<BR>All in all, the makings of a fine tool.")
                .end()
            .addenda()
                .add("Blaze rods also make suitable cores for a staff.  As with all staves, it can be inscribed with double the normal amount of spells, though the bonus Infernal spell slot is not doubled.  In total, that makes four of any type of spell plus one Infernal spell.")
                .end()
            .build();
        this.researchEntry("WAND_CORE_PURPUR").name("Wand Core: Purpur")
            .stages()
                .add("The terrestrial sources of magick all have wand cores aligned to them, why not Void?  It shouldn't be too difficult to find something which fills that niche.")
                .add("It turned out harder than I expected to isolate a suitable material, but in the end I succeeded.  The alien purpur blocks have the right material properties and have a strong alignment to the Void.<BR>Unlike lesser materials, a purpur wand core can be inscribed with two spells, in addition to a bonus Void spell.<BR>It still provides a five percent Void mana discount on all workings, and will harvest Void mana from the environment to refill its wand gem.<BR>All in all, the makings of a fine tool.")
                .end()
            .addenda()
                .add("Purpur will also make a suitable core for a staff.  As with all staves, it can be inscribed with double the normal amount of spells, though the bonus Void spell slot is not doubled.  In total, that makes four of any type of spell plus one Void spell.")
                .end()
            .build();
        this.researchEntry("IMBUED_WOOL").name("Imbued Wool")
            .stages()
                .add("If I'm going to study magick, I feel it's important that I look the part.  That means robes.<BR>A nice silk or breathable cotton would be nice, but wool is what I've got, so wool is what I'll use.  And I should make sure to imbue them with mana to see how it affects the result.")
                .add("Well, it's scratchy and a little warm, but I think I made a good fitting of it.<BR>The addition of mana to the design was a good idea.  Not only are the robes more durable and protective this way, but they also seem to make it easier for me to channel mana while wearing them.  Each piece should give me a small discount on all my mana costs, and they're warm enough to prevent me from freezing in wintry environs.  Nice!")
                .end()
            .build();
        this.researchEntry("SPELLCLOTH").name("Spellcloth")
            .stages()
                .add("I think I've outgrown my apprentice's robes.  It's time to make something a little more functional, and a lot less scratchy.<BR>Time to see about creating a fabric that's truly magickal, rather than just imbued with a little mana.")
                .add("Oh, this will do nicely!  By starting with a wool base, embroidering it with spider-silk stitching, and infusing the result with essence, I've created a stylish and functional fabric that I call Spellcloth.  And it's silky smooth, too!<BR>The robes I make out of this cloth will be more protective and allow me to channel mana a little better, leading to deeper discounts on my mana expenditures.")
                .end()
            .build();
        this.researchEntry("HEXWEAVE").name("Hexweave")
            .stages()
                .add("I am no longer a mere adept, and an adept's garb is no longer suitable for me.  This magickal fabric I've created can be improved upon with my new power, I'm certain of it.")
                .add("Now these are robes worthy of a true wizard.<BR>The Hexweave I've created is most durable, capable of turning an angry blade.  And it conducts mana much better than Spellcloth, resulting in more efficient workings while I wear it.<BR>Plus, I do cut a striking figure in these robes, if I do say so myself.")
                .end()
            .build();
        this.researchEntry("SAINTSWOOL").name("Saintswool")
            .stages()
                .add("I can no longer bear to wear the vestments of darkness that I once garbed myself in.  They reek of hubris.<BR>Let me see if I can persuade the universe to deign to bless some humble fabric.")
                .add("I have created Saintswool, and it is beautiful.<BR>Made of purified Hexweave, and suffused with subtle spider-silk patterns, it is pleasing to the eye, protective of the body, and flowing with magick.<BR>Of course, it's a touch scratchy, but that will help to remind me that I am but a mere mortal.")
                .end()
            .build();
        this.researchEntry("ARTIFICIAL_MANA_FONTS").name("Artificial Mana Fonts")
            .stages()
                .add("The mana fonts in those ancient shrines are incredible.  If only I could move them!<BR>Maybe I can use what I've learned to replicate something like them.  Having a renewable mana source at home would be invaluable to my studies.")
                .add("I've succeeded in creating an artificial mana font!  Now I can have the luxury of a regenerating mana source in the comfort of my own home.<BR>Unfortunately, my work so far pales in comparison to that of the ancients.  My font can only hold a tenth of the mana of an ancient font at once, but at least it refills at the same speed.")
                .end()
            .addenda()
                .add("Despite never having found one in the wild, Blood mana can be collected in an artificial font as well.")
                .add("The ancients may never have dared to create an Infernal mana font, but I have!")
                .add("Even the enigmatic Void mana can have an artificial font created for it.")
                .add("It seems even the song of the universe can be captured in an artificial font.  I must care for it well.")
                .end()
            .build();
        this.researchEntry("FORBIDDEN_MANA_FONTS").name("Forbidden Mana Fonts")
            .stages()
                .add("I tire of waiting for these artificial fonts to replenish their mana supply.  Surely at this point my power rivals that of the vaunted ancients.  I can create something just as good as their works.")
                .add("This will do nicely, yes.<BR>With the careful application of my incredible power, I have created a mana font that can store just as much as that of the ancients.  And I can place them wherever I want!")
                .end()
            .addenda()
                .add("Blood mana represents the power of life and death, and now I have a regenerating source of it at my beck and call.")
                .add("I had expected an Infernal mana font to be hotter to the touch, but the stone remains quite cool.")
                .add("Pay no mind to the whispers emanating from this Void mana font.  The creatures of the beyond answer to my will, not the other way around.")
                .add("Peace and harmony, overflowing.  This Hallowed font is but a shadow of greater beauty.")
                .end()
            .build();
        this.researchEntry("HEAVENLY_MANA_FONTS").name("Heavenly Mana Fonts")
            .stages()
                .add("It is wisdom to leave the world in a better state than one found it.  Someday, one will walk in my footsteps seeking the secrets of magick.  I should leave them with better tools than were left to me.")
                .add("My latest creations will ensure that those who come after me have a plentiful supply of mana to work from.  These fonts hold ten times as much mana as the last iteration.")
                .end()
            .addenda()
                .add("The method of my discovery of Blood magick still haunts me.  I should ensure that my successors don't have to experience that by leaving them a source of Blood mana.")
                .add("I would not wish a trip to the Nether on anyone.  Perhaps it is for the best that I leave behind a source of Infernal mana.")
                .add("The way to the End is truly daunting, and its denizens most dangerous.  Those learning should not have to risk their lives.  I will leave them a source of Void mana.")
                .add("Beautiful is the song of the universe.  Its harmony should be shared with all, and share I shall with this font of Hallowed mana.")
                .end()
            .build();
        this.researchEntry("MANA_ARROWS").name("Mana-Tinged Arrows")
            .stages()
                .add("I have some mana and I have an arcane workbench.  I want to make something cool, something useful, but my knowlege is still so limited.<BR>What if I channel a bit of mana into my weapons to help me hunt?  The mana binding techniques I have are still flimsy, though, so the weaving wouldn't last long.  Very limited in how often they could be used.<BR>Hmm, what about arrows?")
                .add("Well this worked out nicely.  With a pinch of essence dust, I'm able to tinge the heads of some arrows with mana.  It's not much, but it's enough to give them a limited secondary effect when fired.<BR>Earth arrows carry the weight of stone with them, knocking the target back a little.<BR>Sea arrows hinder the target with cold, slowing them down.  They also fly true underwater.<BR>Sky arrows sail freely on the skies, unhindered by gravity so long as they're in the air.  That will make for some great sharpshooting.<BR>Sun arrows turn the target into a glowing beacon.  They also combust on contact with the undead, lighting them on fire briefly.<BR>Moon arrows sap the strength of the target, weakening them a little briefly.")
                .end()
            .addenda()
                .add("Blood arrows live up to their name, causing the target to bleed excessively for a short time, doing further damage.  Curiously, they also fly true underwater.")
                .add("Infernal arrows burst into flame as soon as they're launched, igniting anything they hit.")
                .add("Void arrows inflict a withering effect on the target briefly, doing further damage.")
                .add("Hallowed arrows simply do more damage on impact than normal arrows.  They also cause the undead to combust briefly.")
                .end()
            .build();
        this.researchEntry("ESSENCE_CASK_ENCHANTED").name("Enchanted Essence Cask")
            .stages()
                .add("With all the magical essence I'm accumulating, I foresee storage of it all becoming a problem.  I should find a solution to that.")
                .add("I've reinforced a barrel with magickal materials and enchanted it with the strongest compression enchantments I know.  This should allow me to store essence more efficiently.<BR>The resulting cask holds a little more than an ordinary double-wide chest, but only magickal essence can be placed inside.  It can hold any type of essence in varying amounts, but there's a cap on its total capacity.<BR>I should be very careful not to break one of these when it's full; just thinking about the mess it would cause makes me shudder.")
                .end()
            .build();
        this.researchEntry("ESSENCE_CASK_FORBIDDEN").name("Forbidden Essence Cask")
            .stages()
                .add("My storage system is no longer able to keep up with my demands!  I must invent a more spacious essence cask to meet my needs.")
                .add("That should do it.  By layering the compression enchantments with a little spatial warping, this new cask can hold double what the old version could.  Of course, it requires more exotic materials to craft, but that's a small price to pay.")
                .end()
            .build();
        this.researchEntry("ESSENCE_CASK_HEAVENLY").name("Heavenly Essence Cask")
            .stages()
                .add("All is as one.  This fundamental principle of unity will hopefully allow me to keep my essence stocks tidier.")
                .add("By using the purest of materials and enchantments of harmony, I have forged the ultimate essence storage device.  It will hold double again what the previous iteration was capable of.")
                .end()
            .build();
        this.researchEntry("WAND_GLAMOUR_TABLE").name("Wand Glamour Table")
            .stages()
                .add("These new advanced wands are really cool, but what if I want my wand to look different from its construction?  I bet there's a magickal way to solve that!")
                .add("With the help of some light-bending quartz and the shifting magick of the moon, I can now apply simple glamour enchantments to my wands.<BR>These glamours will let me make my wands look like they're made of whatever I want, so long as I have the components.  I just place the wand on the table, along with the new components whose appearance I want the wand to assume, and presto!  The wand components are consumed in the process, but that's just the price to pay for fashion, I guess.<BR>It's worth noting that mundane wands cannot be glamoured in this way; they're just too primitive.  All the more reason to upgrade!")
                .end()
            .build();
        this.researchEntry("ATTUNEMENT_SHACKLES").name("Attunement Shackles")
            .stages()
                .add("I've been thinking about attunement a lot lately.  The idea that practicing magick will change me is intriguing.  Being able to run faster or breathe underwater, just by being a practicing mage, sounds really cool.<BR>It's also a little scary, though.  What if I don't like the changes after all?  Will I still be me after they take hold?<BR>I should see if I can research a way to suppress these changes, just in case.")
                .add("Okay, I feel better now.  I've designed a set of metaphysical shackles that will protect me from any unwanted changes coming from magickal attunement.<BR>Using a set of attunement shackles will suppress all effects of attunement for magick of its corresponding source.  I don't have to wear them all the time (the chain is more of a metaphor than anything), but I won't be able to pick and choose the suppressed effects for that source.  It's all or nothing.<BR>The suppression will last until I use the shackles again, reversing the process.")
                .end()
            .addenda()
                .add("Attunement shackles can also be made to affect Blood magick.")
                .add("Attunement shackles can also be made to affect Infernal magick.")
                .add("Attunement shackles can also be made to affect Void magick.")
                .add("Attunement shackles can also be made to affect Hallowed magick.")
                .end()
            .build();
        this.researchEntry("BASIC_RUNEWORKING").name("Basic Runeworking")
            .stages()
                .add("I think I'm starting to get the hang of Runeworking.  Certain symbols carry inherent magickal power.  By carving them into stone, then inscribing the right combination of them onto an item, I can imbue that item with an effect much like an enchantment.<BR>While somewhat invovled, there appear to be a number of benefits to this process over using an enchanting table.<BR>First, it doesn't cost me any of my hard-won experience.  That alone makes it worth studying.<BR>Second, I have full control of the enchantment that gets applied.  No more praying for the right random result at the enchanting table.<BR>And third, I can actually apply runes to an item that I've already enchanted for further effect.<BR>This process has its drawbacks, though.  These runes are very complicated; I'm going to have to research each of them individually to make sure I get them right.  And an item can only have one set of runes applied to it at a time.  Finally, and most strangely, while a standard grindstone will remove the magick that the runes impart, it won't remove the runes themselves, so I can't apply new ones.<BR>Still, this is a discipline that seems well worth my time to pursue, if I want to give myself the arms and armor I need to survive.")
                .end()
            .build();
        this.researchEntry("EXPERT_RUNEWORKING").name("Expert Runeworking")
            .stages()
                .add("My skill with runeworking is growing, but I need more practice with it before I can get over this plateau I'm on.  Nothing to do but keep at it.")
                .add("I now feel comfortable calling myself an expert at carving and scribing runes.  Now I can start researching more advanced runes and continue my experimentation.<BR>In addition, I've devised an upgraded runescribing altar that will allow me to scribe more runes onto a single item at a time.  This means that it's now possible to have multiple rune enchantments on an item simultaneously, assuming I can find a set that overlap a little.")
                .end()
            .build();
        this.researchEntry("MASTER_RUNEWORKING").name("Master Runeworking")
            .stages()
                .add("My skill with runeworking is growing, but I need more practice with it before I can get over this plateau I'm on.  Nothing to do but keep at it.")
                .add("I've now mastered conventional runeworking and have started researching the forbidden topics that my Grimoire remains silent about.<BR>I've also once again upgraded my runescribing altar to allow for scribing more runes onto an item.  This will make it even easier to make additional rune combinations.")
                .end()
            .build();
        this.researchEntry("SUPREME_RUNEWORKING").name("Supreme Runeworking")
            .stages()
                .add("The top of the hill is in sight.  My skills with runeworking are almost without peer...  Almost.")
                .add("The most advanced topics in the discipline of runeworking are now at my fingertips.<BR>And at last, I have perfected my runescribing altar.  It will allow me to scribe the most possible runes onto an item at once, allowing for easy combinations to be made.")
                .end()
            .build();
        this.researchEntry("COMPLETE_RUNEWORKING").name("Completion of Runeworking")
            .stages()
                .add("I believe I've finally learned all that there is to know of runeworking.")
                .add("My completion of this discipline has attuned me further to the ways of magick.  I should turn my eye to the other disciplines and see if there remains anything for me to glean.")
                .end()
            .build();
        this.researchEntry("RUNE_EARTH").name("Rune: Earth")
            .stages()
                .add("Source runes are a critical component of forming rune combinations.  I should figure out how to attune a rune to the Earth.")
                .add("A little Earth dust and an unattuned rune, and presto!  One Earth rune.")
                .end()
            .build();
        this.researchEntry("RUNE_SEA").name("Rune: Sea")
            .stages()
                .add("Source runes are a critical component of forming rune combinations.  I should figure out how to attune a rune to the Sea.")
                .add("Attuning a Sea rune is a simple matter of combining an unattuned rune with some Sea dust.")
                .end()
            .build();
        this.researchEntry("RUNE_SKY").name("Rune: Sky")
            .stages()
                .add("Source runes are a critical component of forming rune combinations.  I should figure out how to attune a rune to the Sky.")
                .add("Combining an unattuned rune with some Sky dust will yield a usable Sky rune.")
                .end()
            .build();
        this.researchEntry("RUNE_SUN").name("Rune: Sun")
            .stages()
                .add("Source runes are a critical component of forming rune combinations.  I should figure out how to attune a rune to the Sun.")
                .add("Just take some Sun dust and an unattuned rune, and you've got yourself a Sun rune.")
                .end()
            .build();
        this.researchEntry("RUNE_MOON").name("Rune: Moon")
            .stages()
                .add("Source runes are a critical component of forming rune combinations.  I should figure out how to attune a rune to the Moon.")
                .add("Unattuned rune?  Check.  Moon dust?  Check.  Crafting table?  Check.  That's a Moon rune.")
                .end()
            .build();
        this.researchEntry("RUNE_PROJECT").name("Rune: Project")
            .stages()
                .add("To form a valid rune combination, I'll need access to Verb runes.  Perhaps the Grimoire can point me towards one.")
                .add("To energize or bring about.  This rune's meaning can be interpreted a number of ways, but those are the most common.  When I want to take something and cast it outward, I want a Project rune.")
                .end()
            .build();
        this.researchEntry("RUNE_PROTECT").name("Rune: Protect")
            .stages()
                .add("To form a valid rune combination, I'll need access to Verb runes.  Perhaps the Grimoire can point me towards one.")
                .add("This rune's meaning is pretty straightforward.  It guards and preserves whatever it's bound to.")
                .end()
            .build();
        this.researchEntry("RUNE_ITEM").name("Rune: Item")
            .stages()
                .add("To form a valid rune combination, I'll need access to Noun runes.  Perhaps the Grimoire can point me towards one.")
                .add("When I want the effect of the rune enchantment to affect the piece of gear it's on, that's a job for an Item rune.")
                .end()
            .build();
        this.researchEntry("RUNE_SELF").name("Rune: Self")
            .stages()
                .add("To form a valid rune combination, I'll need access to Noun runes.  Perhaps the Grimoire can point me towards one.")
                .add("To have a rune enchantment affect myself, I should use a Self rune in the combination.")
                .end()
            .build();
        this.researchEntry("RUNE_BLOOD").name("Rune: Blood")
            .stages()
                .add("The forbidden sources must have runes too, it only makes sense.  I must attune one to the Blood!")
                .add("Child's play.  Combining an unattuned rune with a handful of Blood dust attuned the rune perfectly.")
                .end()
            .build();
        this.researchEntry("RUNE_INFERNAL").name("Rune: Infernal")
            .stages()
                .add("The forbidden sources must have runes too, it only makes sense.  I must attune one to the Infernal!")
                .add("It was trivially easy to attune an Infernal rune.  I needed only introduce an unattuned rune to some Infernal dust, and the magick practically worked itself.")
                .end()
            .build();
        this.researchEntry("RUNE_VOID").name("Rune: Void")
            .stages()
                .add("The forbidden sources must have runes too, it only makes sense.  I must attune one to the Void!")
                .add("An unattuned rune will drink up Void dust as easily as any other source.")
                .end()
            .build();
        this.researchEntry("RUNE_ABSORB").name("Rune: Absorb")
            .stages()
                .add("More types of Verb runes will open up more rune combination possibilities.  I should attempt to discover some.")
                .add("This rune is sort of the opposite of the Project rune.  It pulls in and binds, rather than casting out.")
                .end()
            .build();
        this.researchEntry("RUNE_DISPEL").name("Rune: Dispel")
            .stages()
                .add("More types of Verb runes will open up more rune combination possibilities.  I should attempt to discover some.")
                .add("To destroy or unmake, that is the purview of the Dispel rune.")
                .end()
            .build();
        this.researchEntry("RUNE_SUMMON").name("Rune: Summon")
            .stages()
                .add("More types of Verb runes will open up more rune combination possibilities.  I should attempt to discover some.")
                .add("Another rune with a largely apparent meaning.  The Summon rune takes from somewhere else, and brings that something here.")
                .end()
            .build();
        this.researchEntry("RUNE_AREA").name("Rune: Area")
            .stages()
                .add("More types of Noun runes will open up more rune combination possibilities.  I should attempt to discover some.")
                .add("If I want a rune enchantment to affect more than a single target, I will probably need an Area rune.")
                .end()
            .build();
        this.researchEntry("RUNE_CREATURE").name("Rune: Creature")
            .stages()
                .add("More types of Noun runes will open up more rune combination possibilities.  I should attempt to discover some.")
                .add("Trickier than it seems, the Creature rune affects neither the items it's scribed upon nor its wielder, but another being altogether.")
                .end()
            .build();
        this.researchEntry("RUNE_HALLOWED").name("Rune: Hallowed")
            .stages()
                .add("Does the Hallowed possess a rune as well?  Can the song of the universe be captured in a glyph?")
                .add("While it's a pale shadow of the universe's blessing, combining a handful of Hallowed dust with an unattuned rune will indeed produce a Hallowed rune.")
                .end()
            .build();
        this.researchEntry("RUNE_INSIGHT").name("Rune: Insight")
            .stages()
                .add("Rune enchantments are very convenient, but they're rarely as powerful as the output of an enchanting table.  I wonder if I can change that?")
                .add("Fascinating...  A type of rune that is neither Verb nor Noun nor Source.<BR>An Insight rune can be added to any other rune combination to increase the level of the resulting enchantment.  I can only use a single Insight rune in any give runescribe, and even then some enchantments simply can't be empowered in this way.  But it's a lot better than nothing!<BR>The best part is that a single Insight rune will bolster all rune combinations I'm inscribing, rather than just one of them.")
                .end()
            .build();
        this.researchEntry("RUNE_POWER").name("Rune: Power")
            .stages()
                .add("Insight runes are useful, but I chafe at only being able to use one at a time.  I will not be limited in this way!")
                .add("Well, it's a step in the right direction.  By utilizing so-called forbidden magicks, I've been able to create a superior empowering rune, which I've aptly dubbed a Power rune.<BR>It works just like an Insight rune, in that it will improve the level of the resulting enchantment of any runescribing effort.  Unfortunately, I can only use a single Power rune in this way, but I *can* use it in combination with an Insight rune for two levels of improvement.<BR>It's not everything I wanted, but it will do for now.")
                .end()
            .build();
        this.researchEntry("RUNE_GRACE").name("Rune: Grace")
            .stages()
                .add("The song of the universe is infinite, but that has not yet manifested in my runescribing studies.  Perhaps I'm missing something.")
                .add("Ah, of course.  The solution was not to try to outsmart the universe, nor to overpower it, but simply to help it express itself in its own terms.<BR>Like an Insight or a Power rune, a Grace rune will improve the level of any enchantments that I runescribe.  Unlike those runes, I can use as many of them as I can fit on the runescribing table.<BR>This will help me create some fine tools to improve the world.")
                .end()
            .build();
        this.researchEntry("RUNIC_GRINDSTONE").name("Runic Grindstone")
            .stages()
                .add("There must be a way to remove runes from an item if I'm not satisfied with the result.  It's just so weird that I can't!")
                .add("The grindstone already does the job I want it to, almost.  It just needs a little help.<BR>By inscribing Dispel runes onto the faces of the grindstone during its construction, I can allow it to purge runes completely.  It will be as if the item had never been inscribed.<BR>But wait, there's more!  By grinding away the enchantments from items, I can gather hints as to the runes needed to replicate that enchant, if such a thing is possible and I'm familiar with the runes in question.  The hints I learn will be recorded in the Rune Enchantments section of my Grimoire.  No more blind guessing for me!")
                .end()
            .build();
        this.researchEntry("RECALL_STONE").name("Recall Stone")
            .stages()
                .add("Walking everywhere is such a drag.  Especially walking home; when you're doing that, all the fun is already behind you.  I wonder if I can use magick to speed up the process?")
                .add("With careful application of the Summon and Self runes, I can craft a stone that will whisk me back home upon use.  It's great!<BR>There are just two limitations that I've found so far.  First, the stone is one use only; it crumbles to inert dust upon use.  Second, it won't take me across dimensional boundaries.  So if I'm in the Nether and want to get home to the Overworld, I'll have to find a portal first.")
                .end()
            .build();
        this.researchEntry("RUNIC_TRIM").name("Runic Armor Trim")
            .stages()
                .add("Being able to customize my armor with trim is really cool.  It's so stylish!  But what if it was functional, too?")
                .add("I've discovered how to make my own armor trim based on the runes I've been learning about.  This runic armor trim, in addition to looking snazzy, will modify the mana discount that I get from the robes I wear.<BR>First I need to make the template, the recipe for which is included here.  Then I combine it with a piece of magickal cloth armor and a source rune at a smithing table as normal.  The modified armor will grant me double the normal mana discount for the source signified by the rune I used, at the cost of no longer providing any discount for any other sources.<BR>I should consider carefully before applying this trim, as once added it cannot be removed, only replaced with a different trim.")
                .end()
            .build();
        this.researchEntry("ENDERWARD").name("Enderward")
            .stages()
                .add("I am so tired of Endermen.  They teleport into my territory, tear up the ground, and attack me if I so much as look at them funny.  There has to be a way to keep them out.")
                .add("Finally, some peace.  I've devised a runic ward that will block the sort of teleportation that Endermen use, keeping them at bay.<BR>This Enderward will prevent anyone from teleporting into a space within sixteen blocks of where it's hung.  This includes the use of Ender Pearls, Chorus Fruit, and Endermen's natural abilities.  It won't necessarily prevent them from teleporting out, which is fine, but it will stop them from getting in.")
                .end()
            .addenda()
                .add("After some experimentation, I've determined that the Enderward does NOT block the use of Recall Stones, as it's based on different principles.  This is fine by me, as it will let me get home quickly.")
                .add("The Enderward will also block teleportation sorcery, as it uses the same principles as Ender teleportation.  Inconvenient for me, but I can live with it.")
                .end()
            .build();
        this.researchEntry("BASIC_RITUAL").name("Basic Ritual Magick")
            .stages()
                .add("I think I'm starting to get the hang of Ritual Magick.  It's like cooking a recipe.<BR>First, you assemble your ingredients.  Then you stage everything in the right place.  And then you follow each step exactly as written at the appointed time.<BR>There's just a slightly higher chance of something exploding if you do it wrong.<BR>I need several things in order to conduct a ritual.  First and most important is the central altar.  This serves as the focal point of the ritual, around which everything else is arrayed, both physically and metaphysically.<BR>Next come the offering pedestals, arrayed around the altar.  It's on these that I place the ingredients for the ritual.  Having more pedestals than I need doesn't hurt, but they might get a little in the way.<BR>Thirdly, all rituals require props of some kind, placed near the altar.  I'll come back to these in a bit.<BR>Finally, I need refined salt, and lots of it.  Every block that's part of the ritual, be it a prop or an offering pedestal, must be connected to the central altar by an unbroken line of salt.  No salt, no mystical connection, bad times.<BR>To start a ritual, I must first identify what ingredients and props it needs.  These will be listed in the Grimoire entry for each ritual.  Once identified, I array the pedestals and props around the altar, ensuring a salt connection.  Symmetry is important in this placement, and will contribute to a successful ritual.  I then place each of the ingredients on a separate pedestal and, once I'm ready to begin, wave my wand at the central altar.<BR>Once started, an orb of energy will appear above the altar.  This orb is a sort of status indicator for the ritual's stability.<BR>Right, stability.<BR>Ritual magick is dangerous, and rituals destabilize the longer they go on.  Too much instability buildup, and mishaps can happen.  That's bad, potentially disastrous.  The more red that orb over the altar is, the more unstable the ritual has become.  On the other hand, if it turns green, that means I'm doing a good job keeping the instability in check.  All rituals will destabilize over time proportionately to their instability rating, but other things can affect it as well.  The symmetry that I mentioned earlier?  Asymmetrical ritual setups will destabilize faster.  Using too much salt will also cause destabilization, as it grounds out the magick.<BR>Anyway, as the ritual progresses, the central altar will absorb the offerings I've placed on the pedestals.  At certain points, though, the ritual will pause and demand that I interact with one of the ritual's props.  This is the most dangerous time in a ritual, as instability will be mounting while no progress is being made.  Once the prop interaction is complete, the ritual will resume progress, with an added burst of stability.  Precisely how I need to interact with a prop will be detailed in its Grimoire entry.<BR>Once all of the offerings have been absorbed and all of the props have been interacted with, the ritual will end and the end result will appear on the central altar for me to claim.<BR>Should I feel at any point that a ritual is spiraling beyond my control, I can abort the ritual by waving my wand at the altar a second time.  This may have consequences, depending on the built-up instability, but that's better than letting it continue indefinitely.")
                .end()
            .build();
        this.researchEntry("EXPERT_RITUAL").name("Expert Ritual Magick")
            .stages()
                .add("My skill with ritual magick is growing, but I need more practice with it before I can get over this plateau I'm on.  Nothing to do but keep at it.")
                .add("I now feel comfortable calling myself an expert at conducting rituals.  Now I can start researching more advanced rituals and continue my studies.")
                .end()
            .build();
        this.researchEntry("MASTER_RITUAL").name("Master Ritual Magick")
            .stages()
                .add("My skill with ritual magick is growing, but I need more practice with it before I can get over this plateau I'm on.  Nothing to do but keep at it.")
                .add("I've now mastered conventional ritual magick and have started researching the forbidden topics that my Grimoire remains silent about.")
                .end()
            .build();
        this.researchEntry("SUPREME_RITUAL").name("Supreme Ritual Magick")
            .stages()
                .add("The top of the hill is in sight.  My skills with ritual magick are almost without peer...  Almost.")
                .add("The most advanced topics in the discipline of ritual magick are now at my fingertips.")
                .end()
            .build();
        this.researchEntry("COMPLETE_RITUAL").name("Completion of Ritual Magick")
            .stages()
                .add("I believe I've finally learned all that there is to know of ritual magick.")
                .add("My completion of this discipline has attuned me further to the ways of magick.  I should turn my eye to the other disciplines and see if there remains anything for me to glean.")
                .end()
            .build();
        this.researchEntry("RITUAL_CANDLES").name("Ritual Candles")
            .stages()
                .add("I'm going to need special props in order to conduct my rituals.  According to my Grimoire, ritual candles are a classic.  I should figure out what makes them special, compared to ordinary candles.")
                .add("Apparently there's a special sanctification process that goes into the preparation of ritual candles.<BR>It requires the intonation of special blessings while handling the tallow, and the inscription of mystical sigils.  No mana, though.<BR>There's nothing stopping me from using them as tasteful decoration, either, and they come in a wide variety of colors.<BR>To activate this prop during a ritual, I must light it with a flint and steel.  It's very important that the candle not already be lit when I start the ritual; the sparking of new light is symbolic.<BR>To reset the prop, I can just snuff it with my fingertips.")
                .end()
            .addenda()
                .add("It seems that ritual candles can be made from beeswax in addition to tallow.  The sanctification process is the same, so I need not alter my process, just the materials I use.")
                .end()
            .build();
        this.researchEntry("INCENSE_BRAZIER").name("Incense Brazier")
            .stages()
                .add("I'm going to need special props in order to conduct my rituals.  According to my Grimoire, incense is commonly used.")
                .add("Incense can be created from most types of flowers, simply burn it on the end of a stick.  The brazier it's burned in, however, must be specially sanctified before use.<BR>To activate this prop during a ritual, I simply take a stick of incense and add it to an unlit brazier.  It's very important that the brazier not already be lit when I start the ritual.<BR>To reset the prop, I just remove the charred incense stick with an empty hand.")
                .end()
            .build();
        this.researchEntry("MANAFRUIT").name("Manafruit")
            .stages()
                .add("My first real ritual!  I'm so excited.<BR>I'm piecing together the props and offerings I'll need from my Grimoire's hints.  I'll be ready to try it soon!")
                .add("Fruit?  Huh, okay then.<BR>It looks like it's not just any old fruit, though.  In addition to being nourishing, this Manafruit will temporarily allow me to channel mana more efficiently, resulting in a slight discount in all my mana costs.<BR>Maybe I can find some other use for it as well?")
                .end()
            .build();
        this.researchEntry("RITUAL_LECTERN").name("Ritual Lectern")
            .stages()
                .add("More advanced rituals will require more advanced props to drive them.<BR>Books are symbolic of knowledge and are important in mundane enchanting.  Perhaps they can be of use in rituals as well?")
                .add("Not just any book will do for a ritual, and I can't just be carrying it on my person.  After all, I need my hands free in order to conduct the ritual.<BR>To be useful for a ritual, the book must be enchanted first at an enchanting table.  The precise enchantment on the book doesn't matter, it just needs to be carrying the magickal energies that come with experience.<BR>As for holding the book, I'll need a specially constructed and sanctified lectern.  Moonwood should do nicely for this.<BR>To activate this prop during a ritual, I simply place the enchanted book upon it.  It's very important that the lectern be empty when I start the ritual; the surrendering of knowledge is symbolic.<BR>To reset the prop, I just retrieve the book by shift-right-clicking the lectern.")
                .end()
            .build();
        this.researchEntry("RITUAL_BELL").name("Ritual Bell")
            .stages()
                .add("More advanced rituals will require more advanced props to drive them.<BR>The pendulous motion of a bell is symbolic of the extremes of good and evil, and its sound a symbol of creative power.  Perhaps they can be of use in rituals as well?")
                .add("I can't just waltz into the nearest village, steal their bell, and expect it to work for my rituals.  To capture a pure enough tone, the bell must be constructed of a magickal metal, and must be specially sanctified during construction.<BR>To activate this prop during a ritual, I need only ring it.  My hands don't even need to be empty.<BR>Unlike most props, a ritual bell does not need to be manually reset.  However, it must have finished its first ring before it can be activated again.")
                .end()
            .build();
        this.researchEntry("BLOODLETTER").name("Bloodletter")
            .stages()
                .add("Mere physical props will not be sufficient for rituals of the caliber I now seek to perform.<BR>Blood is life, and life is magick.  It would be foolhardy not to try to incorporate blood into my rituals somehow.")
                .add("As with most props, the effective ritual shedding of blood is not so simple.  It must be shed by a blade of purest diamond and collected in a bowl of specially sanctified smoked marble.<BR>To activate this prop during a ritual, I must slice my palm open on the blade and allow the blood to be caught in the bowl.  Obviously, this will hurt and do a small amount of damage to me, but it's nothing I can't handle.<BR>To reset the prop, I must wash it clean with a bucket of water.")
                .end()
            .build();
        this.researchEntry("SOUL_ANVIL").name("Soul Anvil")
            .stages()
                .add("Mere physical props will not be sufficient for rituals of the caliber I now seek to perform.<BR>Living creatures carry a great deal of energy inside them, often colloquially refered to as a soul.  While it would be foolish to try to use my own, the sacrifice of this energy could greatly empower my rituals.<BR>But how best to release it?")
                .add("The solution here is a straightforward one: brute force.  After capturing a soul in a gem, I simply shatter the gem during the ritual at the appropriate time.<BR>But what to shatter it on?  These things are sturdy, after all.  The answer, I believe, is an anvil specially constructed of Hexium and sanctified with magickal power.<BR>To activate this prop during a ritual, I must take a soul gem in hand and shatter it upon the anvil.  It's very important that the anvil be clean when I start the ritual; it wouldn't do to mix shards of different soul gems.<BR>To reset the prop, I must wipe it clean with a piece of magickal cloth.")
                .end()
            .build();
        this.researchEntry("CELESTIAL_HARP").name("Celestial Harp")
            .stages()
                .add("I can feel the song of the universe flowing through me, and I know that it is the key to unlocking the greatest of magicks.  I must channel it during a ritual, but how?")
                .add("Mundane instruments are not up to the task of channeling the song.  I must construct a new one, made of the finest hallowed wood, and specially sanctified.<BR>To activate this prop during a ritual, I must play it with an empty hand.  The fragment of song I channel will last for several seconds, but the ritual will continue once the first notes are played.<BR>Unlike most props, a celestial harp does not need to be manually reset.  However, it must have finished playing its first song before it can be activated again.")
                .end()
            .build();
        this.researchEntry("WAND_CORE_PRIMAL").name("Wand Core: Primal")
            .stages()
                .add("The cores that I've discovered so far are potent, but there are only so many wands that a mage can carry!<BR>I wonder if I can combine their power, somehow?")
                .add("Combining the effects of these wand cores was no mean feat, but I think I've done it!  By taking each of the source-aligned wands cores and offering them up in a ritual with some mana salts, I've managed to fuse them into a single wand core with the power of all five.<BR>This core is aligned to all five terrestrial sources, meaning that it will provide the expected discount with Earth, Sea, Sky, Sun, or Moon magick, and will slowly regenerate mana of any of the five sources.  In addition, it can be inscribed with two spells, regardless of source.")
                .end()
            .addenda()
                .add("The process seems to work for creating magickal staves as well.  I simply start the ritual offering staff cores instead of wand cores.<BR>As with all staves, it can be inscribed with double the normal amount of spells, for a total of four, regardless of source.")
                .end()
            .build();
        this.researchEntry("WAND_CORE_DARK_PRIMAL").name("Wand Core: Dark Primal")
            .stages()
                .add("I am not content with having to use three separate wands to efficiently channel my so-called forbidden magick.  The process I used to create a primal wand core must be replicated for these sources!")
                .add("The ritual is dangerous, but the risk is worth the reward.<BR>By offering up a primal wand core, along with the three forbidden wand cores and more mana salts, I have created a core worthy of my power.<BR>The core remains aligned to all five terrestrial sources, but is also aligned to the three forbidden sources.  It will provide the expected mana discount and regeneration for all eight sources.  In addition, a Dark Primal wand core can be inscribed with three spells, regardless of source.")
                .end()
            .addenda()
                .add("Like expected, the ritual will also create a Dark Primal staff core, assuming that I use staff cores to start out with.<BR>As with all staves, it can be inscribed with double the normal amount of spells, for a total of six, regardless of source.")
                .end()
            .build();
        this.researchEntry("WAND_CORE_PURE_PRIMAL").name("Wand Core: Pure Primal")
            .stages()
                .add("Can the universe's love be crafted into a wand?  There's only one way to find out.")
                .add("Enlightenment made manifest, this wand shall be a tool to work wonders to share with the world.<BR>This core is aligned to all of the primal sources, including the Hallowed.  It will provide the expected discount for all magickal workings, and slowly regenerate all sources of mana.  In addition, a Pure Primal wand core can be inscribed with four spells, regardless of source.")
                .end()
            .addenda()
                .add("As with the other wand core types, a Pure Primal staff core may be created as well.<BR>As with all staves, it can be inscribed with double the normal amount of spells, for a total of eight, regardless of source.")
                .end()
            .build();
        this.researchEntry("PIXIES").name("Pixies")
            .stages()
                .add("It gets a little lonely, living the life of a mage.  Always studying, so few people to talk to who can understand my work.<BR>I wonder if there are any magickal life forms out there that would like to make friends?")
                .add("Well, it turns out that you can in fact summon friends from the aether.  Fascinating!<BR>These little folk call themselves pixies, and have apparently sworn loyalty to me?  Something about the ritual I used to summon them seems to bind them to me.  I've tried to release them from this bond, but they just swear fealty all over again!  Oh well.<BR>In any case, these little ones will follow me around and keep me company, though only three will follow me at any one time.  Should I tire of their flitting, I can just grab them with an empty hand and stick them in my pocket or something, they don't seem to mind.<BR>Their attention spans are a little short, but if I get in a fight, they're suddenly all business.  They don't like fisticuffs, due to their size, but what they will do is support me with spells!  Their magickal nature means they can call upon the sources innately instead of having to channel mana like I do.  I'm a little jealous.<BR>If they happen to be hurt too badly, they say they'll be 'drained', which I think is some kind of weakened state for them.  Feeding them some magickal essence of the right source should fix them right up!<BR>Earth pixies will attack with the magick of the land, doing damage and knocking their targets back.<BR>Sea pixies use frost magick, dealing damage and slowing their opponents to a crawl.<BR>Sky pixies use lightning.  It doesn't have any secondary effects, but I believe in them!<BR>Sun pixies don't attack at all; instead they heal me of my wounds!<BR>Finally, Moon pixies attack with lunar magick, dealing damage and weakening their targets.")
                .end()
            .addenda()
                .add("It turns out that Blood pixies exist too.  Their magick tears at the target from the inside, bypassing any armor it may be wearing.")
                .add("Infernal pixies are crazy!  The don't cast spells, no, that would be too simple.  Instead, they charge their targets like maniacs and explode.<BR>This means they don't stick around for long, but fortunately they seem to take less essence to summon than other pixies.")
                .add("As for Void pixies, talk about menacing.  They have sharp teeth and are always muttering about how hungry they are.<BR>Their magick leaves a consumptive withering effect on their targets.")
                .add("Hallowed pixies don't say much, but they're very good listeners.  They also don't like the undead much; their magick does double damage to any undead creature they hit.")
                .end()
            .build();
        this.researchEntry("GRAND_PIXIES").name("Grand Pixies")
            .stages()
                .add("The pixies that I've summoned seem to have a strict social hierarchy.  I can tell because they're always complaining about being at the bottom of it.<BR>I should see about binding their superiors to my service.  Surely their magick will be superior.")
                .add("These new pixies that I've summoned, grand pixies, seem to be knights of a sort.<BR>As defenders of whatever realm they hail from, they possess superior magick to the lesser pixies that I've been summoning until now.  They also come bearing pixie-sized armor, granting them a little more endurance than their predecessors.<BR>The types of magick that these grand pixies use has not changed, only become stronger.")
                .end()
            .addenda()
                .add("Blood pixies have knights too, and their magick is most effective at rending my foes from the inside.")
                .add("Grand Infernal pixies still explode, and they explode bigger than ever.  Note to self: don't be too close to anything they're angry at.")
                .add("This superior cadre of Void pixie can cause a zombie to wither away in mere moments.  They will make useful tools.")
                .add("The warrior-poets of their kind, grand Hallowed pixies are on a never-ending crusade against the undead.")
                .end()
            .build();
        this.researchEntry("MAJESTIC_PIXIES").name("Majestic Pixies")
            .stages()
                .add("At times, I can't help but overhear my grand pixie cohorts talk about their lieges.  Their real lieges, that is, not me.<BR>Their kind is so fascinating.  I would very much like to meet pixie royalty.")
                .add("They answered my call!  These kings and queens of pixie-dom are truly majestic in their visage, and their power is unrivaled among their kind.<BR>Surprisingly, they have agreed to work with me as well.  Not for a bond of fealty, but as a companionship of equals.  Apparently their subjects have spoken of me, and the rulers were impressed by what they heard.<BR>These majestic pixies wield their innate power with the skill of magi; their spells are even more potent than that of their knights.  I expect I will not be disappointed with their prowess in combat.")
                .end()
            .addenda()
                .add("The rulers of the Blood pixies can practically turn a zombie inside-out with a thought.  Disturbing, but undeniably effective.")
                .add("So this is where all those Infernal pixies learned it from.  Majestic Infernal pixies pack the biggest boom of them all.  One wonders why royalty would be so eager to explode, but apparently it's considered a sign of strength where they come from.")
                .add("Majestic Void pixies strike with all the consumptive wrath of the Wither itself.")
                .add("These paragons of the Hallowed speak rarely, but what words they do share are filled with wisdom.  That and curses for the undead.")
                .end()
            .build();
        this.researchEntry("AMBROSIA").name("Ambrosia")
            .stages()
                .add("So far, I've learned that there are two types of magickal attunement: permanent and temporary.  Permanent attunement comes from learning new secrets, and temporary comes from channeling mana.<BR>Both of these have drawbacks, however.  Secrets take a long time to learn for permanent attunement, and there are only so many of them out there.  And temporary attunement is just that: temporary.  I must continue channeling mana of that source, or that attunement will fade.<BR>I wonder if there's another method with which I can induce an attunement to a source?")
                .add("So this is why the Grimoire was hinting at me about Manafruit.  It's the key to my attunement!<BR>By performing the right ritual, I can transform Manafruit into a substance I've come to call Ambrosia.  Eating ambrosia will induce a small amount of attunement within me to the source with which it's aligned.<BR>There are two limits to ambrosia that I've been able to determine.  First, I can only induce a small amount of attunement using it; beyond that it just won't work.  And second, attuning myself to one source will de-attune me to all of the others to a lesser degree.<BR>Still, this may be the secret to unlocking greater levels of primal attunement for myself!")
                .end()
            .addenda()
                .add("Blood ambrosia exists, and it tastes unnervingly salty.")
                .add("Infernal ambrosia has a fiery aftertaste.")
                .add("Void ambrosia... it feels like it's eating me.  I don't want to think about it.")
                .add("The taste of Hallowed ambrosia reminds me of my very favorite things.")
                .end()
            .build();
        this.researchEntry("GREATER_AMBROSIA").name("Greater Ambrosia")
            .stages()
                .add("There must be more to ambrosia than this.  I covet the power that comes with greater attunement, and I will have it!")
                .add("I was right, of course.  By adjusting the ritual with more powerful offerings, I can create a greater version of ambrosia.<BR>This greater ambrosia will still induce attunement at the same slow rate as before, but I will be able to keep using it for significantly longer before it stops having effect.")
                .end()
            .addenda()
                .add("Blood ambrosia exists, and it tastes unnervingly salty.")
                .add("Infernal ambrosia has a fiery aftertaste.")
                .add("Void ambrosia... it feels like it's eating me.  I don't want to think about it.")
                .add("The taste of Hallowed ambrosia reminds me of my very favorite things.")
                .end()
            .build();
        this.researchEntry("SUPREME_AMBROSIA").name("Supreme Ambrosia")
            .stages()
                .add("I have learned some of the secrets of the universe, but can I attune myself to it more deeply?")
                .add("The song of the universe blesses me once more.  It has revealed how I may create a supreme ambrosia with my ritual magick.<BR>Supreme ambrosia will allow me to induce the maximum possible attunement for a source, though it won't do it any faster.")
                .end()
            .addenda()
                .add("Blood ambrosia exists, and it tastes unnervingly salty.")
                .add("Infernal ambrosia has a fiery aftertaste.")
                .add("Void ambrosia... it feels like it's eating me.  I don't want to think about it.")
                .add("The taste of Hallowed ambrosia reminds me of my very favorite things.")
                .end()
            .build();
        this.researchEntry("FLYING_CARPET").name("Flying Carpet")
            .stages()
                .add("As a mage of great power, I deserve to ride in style.  No mere horse for me.  Let's make that happen.")
                .add("The skies are mine!  I have animated a mere swatch of carpet into a fantastic flying vehicle, and I didn't even need to use forbidden magick to do it.<BR>The Flying Carpet has a small semblance of animal intelligence and will respond to my commands when I sit on it.  I need only direct it forward, as I would an animal or minecart, and it will fly in the direction I'm looking, left, right, up or down.  I can direct it backwards as well, but that is much slower.  Once I'm done flying, I can pick the carpet back up by using it while sneaking.<BR>And let's not forget about style!  While it certainly looks lovely in white, I can also change the carpet to one of a wide variety of colors by combining it with a colored dye.  Should I wish to undo the coloring, I can wash it off in a cauldron.")
                .end()
            .build();
        this.researchEntry("CLEANSING_RITE").name("Cleansing Rite")
            .stages()
                .add("I've committed many sins in my pursuit of magick.  While I have gained great power, those sins have begun to weigh upon me.  But what to do about it?")
                .add("I magicked my way into this problem, and I will magick my way back out.<BR>I've devised a ritual that will collect and congeal the negative energy of my sins, allowing me to manifest them in physical form.  This alone will not free me, however, as they will remain metaphysically bound to me unless I face and overcome them.<BR>But thanks to magick, that may be possible without undergoing years of therapy.  If I bind this negative energy to a Sanguine Core, I can use a crucible to spawn a manifestation of those sins and defeat it in combat!  Unorthodox, but it just might work.<BR>Unless it kills me, of course.  I should be very clear on that: I don't know what exactly will come out of that crucible, but it will be very powerful and very, very angry.  I had best be prepared for a difficult fight.")
                .end()
            .build();
        this.researchEntry("PRIMAL_SHOVEL").name("Primal Shovel")
            .stages()
                .add("I wonder if I can improve my tools by ritually aligning them with a primal source.  The Earth seems to make the most sense for a shovel, so let's try that.")
                .add("Ritually aligning a primalite shovel to the Earth has yielded a digging tool par excellence.<BR>When used to dig soil, sand, snow, or the like, it will excavate a full 5x5 area worth of blocks, oriented along the face that I dug.  If I wish to suppress this property, I just have to dig while sneaking.<BR>The shovel doesn't seem to want to take on any other enchantments, though.  Maybe I'll find a way around that in the future.")
                .end()
            .addenda()
                .add("I've learned how to recreate this shovel's special properties using a rune enchantment.  By combining Project, Area, and Earth runes, I can apply this enchantment to any digging tool to increase its area of effect.")
                .end()
            .build();
        this.researchEntry("PRIMAL_FISHING_ROD").name("Primal Fishing Rod")
            .stages()
                .add("I wonder if I can improve my tools by ritually aligning them with a primal source.  A fishing rod is the obvious match to the Sea, so I'm going to try that.")
                .add("Ritually aligning a primalite fishing rod to the Sea has created a bringer of abundance.<BR>When used to fish up items from the water, I stand a good chance to receive more items per catch.  Curiously, it also seems to yield more items when harvesting crops.<BR>The fishing rod doesn't seem to want to take on any other enchantments, though.  Maybe I'll find a way around that in the future.")
                .end()
            .addenda()
                .add("I've learned how to recreate this fishing rod's special properties using a rune enchantment.  By combining Summon, Area, and Sea runes, I can apply this enchantment to any fishing rod or hoe to increase yields.")
                .end()
            .build();
        this.researchEntry("PRIMAL_AXE").name("Primal Axe")
            .stages()
                .add("I wonder if I can improve my tools by ritually aligning them with a primal source.  There doesn't seem to be a good fit for the Sky, though...  Then again, lightning strikes trees all the time, right?  Maybe an axe will work.")
                .add("Ritually aligning a primalite axe to the Sky has resulted in a very destructive tool.<BR>When used to break a block, the power of the axe will arc through every adjacent block of the same type, harvesting those blocks as well.  Then it will arc through the blocks adjacent to those, and so on, breaking every like block in that cluster.  Thankfully, it seems to have an upper limit of how much it will break, otherwise that could get awkward.<BR>If I wish to suppress this property, I just have to chop while sneaking.<BR>The axe doesn't seem to want to take on any other enchantments, though.  Maybe I'll find a way around that in the future.")
                .end()
            .addenda()
                .add("I've learned how to recreate this axe's special properties using a rune enchantment.  By combining Project, Area, and Sky runes, I can apply this enchantment to any digging tool to allow it to harvest whole trees or mineral veins.  Or, well, clusters of anything else.<BR>I should be careful where I point this thing...")
                .end()
            .build();
        this.researchEntry("PRIMAL_HOE").name("Primal Hoe")
            .stages()
                .add("I wonder if I can improve my tools by ritually aligning them with a primal source.  A hoe is used to create farmland for plants, and they love the Sun.  Perhaps that will work.")
                .add("Ritually aligning a primalite hoe to the Sun has resulted in an excellent farming implement.<BR>When used on any plant matter, it will act like bone meal, fertilizing it and encouraging rapid growth.  Doing so damages the hoe, but it's still a marvelous ability.  If I wish to suppress this property, I just have to use the hoe while sneaking.<BR>The hoe doesn't seem to want to take on any other enchantments, though.  Maybe I'll find a way around that in the future.")
                .end()
            .addenda()
                .add("I've learned how to recreate this hoe's special properties using a rune enchantment.  By combining Summon, Creature, and Sun runes, I can apply this enchantment to any hoe to allow it to speed plant growth.  Higher levels of the enchant will result in less damage to the hoe when I use it that way.")
                .end()
            .build();
        this.researchEntry("PRIMAL_PICKAXE").name("Primal Pickaxe")
            .stages()
                .add("I wonder if I can improve my tools by ritually aligning them with a primal source.  The head of a pickaxe looks kind of like a crescent moon...  Maybe that's enough to establish a sympathetic link.")
                .add("Ritually aligning a primalite pickaxe to the Moon has created a very efficient mining tool.<BR>When used to dig up certain valuable minerals, the magick of the pickaxe has a very good chance of yielding bonus nuggets of that same type of ore.<BR>The pickaxe doesn't seem to want to take on any other enchantments, though.  Maybe I'll find a way around that in the future.")
                .end()
            .addenda()
                .add("I've learned how to recreate this pickaxe's special properties using a rune enchantment.  By combining Summon, Item, and Moon runes, I can apply this enchantment to any digging tool to find bonus mineral nuggets.")
                .end()
            .build();
        this.researchEntry("FORBIDDEN_TRIDENT").name("Forbidden Trident")
            .stages()
                .add("I believe that I can increase the might of my weapons by ritually aligning them with a primal source.  Virtually any weapon could conceivably by aligned with the Blood, but I think I'm going to go with a trident.  Blood flows like water, after all.")
                .add("Ritually aligning a hexium trident to the Blood has yielded an exceedingly deadly polearm.<BR>Creatures struck with it will be unable to stop bleeding for a few seconds, and the intensity of the blood loss will increase with subsequent strikes.<BR>The trident doesn't seem to want to take on any other enchantments, though.  Maybe I'll find a way around that in the future.")
                .end()
            .addenda()
                .add("I've learned how to recreate this trident's special properties using a rune enchantment.  By combining the Project, Creature, and Blood runes, I can apply this enchantment to any trident, sword, or axe to let it rend and tear at my foes' flesh.")
                .end()
            .build();
        this.researchEntry("FORBIDDEN_BOW").name("Forbidden Bow")
            .stages()
                .add("I believe that I can increase the might of my weapons by ritually aligning them with a primal source.  For the Infernal, I choose to align a bow.  The power of damned souls will propel my arrows.")
                .add("Ritually aligning a hexium bow to the Infernal has resulted in a most peculiar weapon.<BR>Creatures struck by its arrows will be pierced both body and soul, and the resulting soul fragments will drop as soul gem slivers.  Only the first ranged strike on a creature will result in a soulpierce, and some powerful creatures are immune to the effect.<BR>The bow doesn't seem to want to take on any other enchantments, though.  Maybe I'll find a way around that in the future.")
                .end()
            .addenda()
                .add("I've learned how to recreate this bow's special properties using a rune enchantment.  By combining the Absorb, Creature, and Infernal runes, I can apply this enchantment to any bow or crossbow to allow it to wound my targets in their very souls.")
                .end()
            .build();
        this.researchEntry("FORBIDDEN_SWORD").name("Forbidden Sword")
            .stages()
                .add("I believe that I can increase the might of my weapons by ritually aligning them with a primal source.  The blade cuts through air, leaving only emptiness in its wake, making it a perfect vessel for the Void.")
                .add("Ritually aligning a hexium sword to the Void has resulted in a very hungry blade.<BR>Creatures killed by it will have a portion of their essence consumed, dropping as dust for me to collect.  Some powerful creatures are immune to the effect.<BR>The sword doesn't seem to want to take on any other enchantments, though.  Maybe I'll find a way around that in the future.")
                .end()
            .addenda()
                .add("I've learned how to recreate this sword's special properties using a rune enchantment.  By combining the Summon, Item, and Void runes, I can apply this enchantment to any trident, sword, or axe to allow it to consume my foes' very essence.")
                .end()
            .build();
        this.researchEntry("SACRED_SHIELD").name("Sacred Shield")
            .stages()
                .add("I should be able to empower my tools by ritually aligning them with a primal source.  The Hallowed is a source of purity and protection, making a shield the natural choice for alignment.")
                .add("Ritually aligning a hallowsteel shield to the Hallowed has created a superlative defensive implement.<BR>Normal shields can only block certain types of incoming attacks, like swords or arrows.  This shield, however, will reduce the damage I take from *all* sources for as long as I'm blocking with it, even things that a shield couldn't normally save me from.<BR>This protection isn't quite instant, ramping up over time as I block, but it's pretty fast.  It fades as soon as I drop my guard, though.<BR>The shield doesn't seem to want to take on any other enchantments, though.  Maybe I'll find a way around that in the future.")
                .end()
            .addenda()
                .add("I've learned how to recreate this shield's special properties using a rune enchantment.  By combining the Protect, Self, and Hallowed runes, I can apply this enchantment to any shield to increase its defensive potential.")
                .end()
            .build();
        this.researchEntry("DREAM_VISION_TALISMAN").name("Dream Vision Talisman")
            .stages()
                .add("I find myself concerned about the number of Observations I'll be able to make.  It's getting harder and harder to find materials that I haven't analyzed already, and my theorycrafting work requires a steady stream of them.<BR>If only there were a way to harness the breadth of life experiences I've gained.  That might be a solution.")
                .add("This should do nicely.  I've designed a talisman that will allow me to convert my experience into concrete Observations.<BR>As long as the talisman is on my person, it will absorb any experience orbs that I pick up, drawing their knowledge into itself rather than my experience pool.  Once it's full, the next time I sleep, the talisman will grant me a useful vision in my dreams, granting me a new Observation.  Doing so damages the talisman, however, and will eventually cause it to break.<BR>Should I decide that I don't want my talisman to absorb experience for a while, I can deactivate it by using it from my hand.  I can reactivate it by using it again.")
                .end()
            .build();
        this.researchEntry("DOWSING_ROD").name("Dowsing Rod")
            .stages()
                .add("Rituals are complicated, and laying out my altar space correctly seems very important to success, according to my Grimoire.<BR>Doing this by eye and the seat of my pants strikes me as a path to disaster.  I need a tool that will give me feedback on the quality of my altar layout before I attempt a ritual.")
                .add("Well, it's simple, but I think it will do the job.<BR>Waving this dowsing rod at a ritual altar will give me a rough idea of how stable my current ritual layout is.  In addition, waving it at a prop or an offering pedestal will give me feedback on its placement.<BR>I mustn't forget, however, that a complex ritual can quickly overwhelm even a very good layout if I'm not quick and efficient in stepping through the process.")
                .end()
            .build();
        this.researchEntry("HYDROMELON").name("Hydromelon")
            .stages()
                .add("I've been thinking a lot lately about Sunwood and Moonwood trees, and I'd bet good emeralds that they're not natural.  They're just too... wondrous.  My guess is that the ancients created them through magick.<BR>I'd like to try doing something similar with my work, but what?  I find that I've had trouble finding good sources of Sea essence that I can harvest in a renewable fashion, so maybe that's a good start.")
                .add("That wasn't as hard as I expected!  I've managed to take the essence of a melon and infuse it with the nature of the Sea.<BR>These Hydromelons can now be grown in my farm, or any farm really.  The slices make for a very refreshing snack.<BR>I just need to be careful when harvesting these.  The rind is about the only thing holding it together; if I were to strip that off with an axe, the whole thing would disintegrate into a mess of water.")
                .end()
            .build();
        this.researchEntry("BLOOD_ROSE").name("Blood Rose")
            .stages()
                .add("The Hydromelon was a great success, so I'd like to keep at it.  This time perhaps I can infuse some plant life with Blood magick.  You don't often see plants with Blood affinity, so this should be a fascinating exercise.")
                .add("Excellent!  Maybe a little too excellent...<BR>Infusing a rose bush with Blood essence has created a very hungry plant.  Its thorns are wickedly sharp and if I didn't know better I'd say they reach out to snare passerby.  Perhaps I can make use of this as some very gothic base defense.")
                .end()
            .build();
        this.researchEntry("EMBERFLOWER").name("Emberflower")
            .stages()
                .add("The Hydromelon was a great success, so I'd like to keep at it.  This time perhaps I can infuse some plant life with Infernal magick.  The Nether is home to a surprising array of plant life, but I'd like something with a little more touch of home in it.")
                .add("Very good.  Infusing a sunflower with Infernal essence has created a very hot plant.  Its flower burns and smokes, giving off modest light, and can be ground down into valuable blaze powder.  It can even survive when planted in soul sand or soul soil, though I question what absorbing nutrients from such material would do to the plant.")
                .end()
            .build();
        this.researchEntry("BASIC_MAGITECH").name("Basic Magitech")
            .stages()
                .add("I think I'm starting to get the hang of Magitech.  The devices are just like normal machines, except they run on mana instead of redstone.<BR>I've drawn up schematics for set of simple, replaceable parts that I can use when designing enchanted machinery.  No doubt more advanced devices will need something more intricate, but it's a start!")
                .end()
            .build();
        this.researchEntry("EXPERT_MAGITECH").name("Expert Magitech")
            .stages()
                .add("My skill with magitech is growing, but I need more practice with it before I can get over this plateau I'm on.  Nothing to do but keep at it.")
                .add("I now feel comfortable calling myself an expert at designing magitech devices.  Now I can start researching more advanced machines and continue my studies.")
                .end()
            .build();
        this.researchEntry("MASTER_MAGITECH").name("Master Magitech")
            .stages()
                .add("My skill with magitech is growing, but I need more practice with it before I can get over this plateau I'm on.  Nothing to do but keep at it.")
                .add("I've now mastered conventional magitech and have started researching the forbidden topics that my Grimoire remains silent about.")
                .end()
            .build();
        this.researchEntry("SUPREME_MAGITECH").name("Supreme Magitech")
            .stages()
                .add("The top of the hill is in sight.  My skills with magitech are almost without peer...  Almost.")
                .add("The most advanced topics in the discipline of magitech are now at my fingertips.")
                .end()
            .build();
        this.researchEntry("COMPLETE_MAGITECH").name("Completion of Magitech")
            .stages()
                .add("I believe I've finally learned all that there is to know of magitech.")
                .add("My completion of this discipline has attuned me further to the ways of magick.  I should turn my eye to the other disciplines and see if there remains anything for me to glean.")
                .end()
            .build();
        this.researchEntry("HONEY_EXTRACTOR").name("Honey Extractor")
            .stages()
                .add("Honey is delicious and sweet, and I wish there was a way to get it more efficiently from hives.  There's more of it in honeycombs than I can get from a bottle, but it's trapped inside the wax.  Can magick help?")
                .add("I've devised a simple device that should help me extract the honey my sweet tooth craves.  It spins the honeycombs very fast, causing the cells of the comb to rupture and spill their honey.  While such a device would conceivably be possible without magick, I found that powering it with Sky mana made the design much simpler.<BR>To begin, I'll need a honeycomb and an empty glass bottle.  The extractor will also need to be powered with a small amount of Sky mana, which can be done by placing a wand in the designated slot.<BR>The machine will then process the honeycomb over several seconds, resulting in a bottle of honey and a clean clump of beeswax that I may be able to repurpose.")
                .end()
            .build();
        this.researchEntry("SEASCRIBE_PEN").name("Seascribe Pen")
            .stages()
                .add("Working at my research table requires a lot of writing, and it feels like my quill is always running out of ink.  I spend more time dipping than I do writing!  Maybe there's a better way.")
                .add("Oh, now this is elegant.  I've designed a magitech pen which will let me work at my research table much better.<BR>The pen is loaded with the same enchanted ink that I use already, but the tiny magitech device embedded within causes it to slowly pull Sea mana from the environment.  It's not much, but it's more than enough to replenish my ink supply.<BR>The result is a pen that never runs dry, no matter how much I write.  And it's very pleasing to hold, to boot.  Functional and stylish both!")
                .end()
            .build();
        this.researchEntry("ARCANOMETER").name("Arcanometer")
            .stages()
                .add("Analyzing items is tedious, exacting work, and it's a destructive process.  I'm always hesitant to analyze anything valuable because I don't want to lose it.<BR>Is there a way I can overcome these limitations?")
                .add("I'm feeling very proud of myself right now.  Behold, the Arcanometer!<BR>Rather than spending an afternoon hunched over an analysis table, I can simply take the Arcanometer, point it at a block, and trigger it.  Presto, one detailed scan result.<BR>I can even use it to scan creatures now!  For obvious reasons, they'd never let me take them apart under a magnifying glass, but now I don't have to.  I should just make sure not to let them eat me while I'm scanning them.")
                .end()
            .build();
        this.researchEntry("PRIMALITE_GOLEM").name("Primalite Golem")
            .stages()
                .add("Golems are fascinating creatures.  They're animated, but clearly not alive.  Magickal, but they don't seem to use mana.<BR>Is this a result that I can replicate with magitech?")
                .add("I've succeeded in creating my own version of an iron golem out of Primalite.  The resulting entity is tougher, faster, and smarter than a conventional iron golem.<BR>To create the golem, I must take a golem controller, place it atop a T-shape of Primalite blocks, and then activate the controller with a wand.  The resulting golem will recognize me as its master, following me around and defending me from attack.  If I want it to stop following me, for whatever reason, I can command it to stay by right-clicking on it.<BR>Should my golem become severely damaged, I can repair it by using an ingot of Primalite on it.<BR>The magick that binds the golem to me is tenuous, and I can only have a single golem servant at a time.  Should I attempt to create a second one, the first will be destroyed.")
                .end()
            .build();
        this.researchEntry("HEXIUM_GOLEM").name("Hexium Golem")
            .stages()
                .add("My research into Primalite golems was a success, but I suspect that I can improve upon it by using Hexium as a base material.  The programming will need to updated, of course, but that should be no issue for a mage of my skill.")
                .add("Brilliant!  My new Hexium golem servant is stronger and tougher than its predecessor, and immune to the ravages of flame to boot.<BR>It is created in a similar method as Primalite golems, only using an updated golem controller and a base of Hexium blocks.  Also similar to other metallic golems, I can repair this one using an ingot of Hexium.<BR>I can still only have a single active golem servant at a time, though.  No matter, the previous model is obsolete and can be disposed of.")
                .end()
            .build();
        this.researchEntry("HALLOWSTEEL_GOLEM").name("Hallowsteel Golem")
            .stages()
                .add("Enlightenment may be a shield of a sort, but it won't stop me from being eaten by a hungry zombie.  That said, I can see the hatred in the eyes of my Hexium golem, and it saddens me.<BR>Let me share the song of the universe with my creation.")
                .add("I've devised perhaps my greatest magitech creation.  Created from the purest Hallowsteel blocks and the most intricate controller I've ever designed, this golem will guard me from the forces of darkness.<BR>It is stronger and tougher than its predecessor, untouchable by flame, and immune to the consumptive effects of the wither.  Should it be necessary, I can repair it with an ingot of Hallowsteel.<BR>One golem remains all that can follow me, but with this by my side, one should be all I need.")
                .end()
            .build();
        this.researchEntry("CONCOCTING_TINCTURES").name("Concocting: Tinctures")
            .stages()
                .add("Potions are great, but bulky.  I have to down an entire bottle to get any effect out of them, and then fumble in my backpack to replace it.<BR>Can I use magick to improve them?")
                .add("It turns out that I can, in fact, improve my potions using the magickal arts.<BR>By combining the usual brewing ingredients with a handful of the appropriate dust essence, I can concentrate the effects of the potion into a more dense concoction.  These tinctures hold three doses of potion per flask, compared to the usual one dose per bottle.<BR>The concocter device can only whip up a single tincture at a time, and it needs to be powered with Infernal mana to generate the necessary heat, but that's fine.  I'm just glad to have my bag space back!")
                .end()
            .addenda()
                .add("Some tincture recipes require the use of Blood essence.")
                .add("Some tincture recipes require the use of Infernal essence.")
                .end()
            .build();
        this.researchEntry("CONCOCTING_PHILTERS").name("Concocting: Philters")
            .stages()
                .add("Tinctures are the work of an amateur.  A mage of my prodigious skill can do better.")
                .add("And better I have done.<BR>By substituting shard essence for the dust used in a tincture, I can create even more potent concoctions.  These philters carry six doses of potion per flask.<BR>Philter recipes will use triple the amount of Infernal mana in the concocter, but that's a small price to pay for such potency.")
                .end()
            .addenda()
                .add("Some philter recipes require the use of Blood essence.")
                .add("Some philter recipes require the use of Infernal essence.")
                .end()
            .build();
        this.researchEntry("CONCOCTING_ELIXIRS").name("Concocting: Elixirs")
            .stages()
                .add("Potency is an illusion.  Each drop of potion carries within it the entire memory of what it must do, it need only be reminded how.")
                .add("Apparently what those drops of potion need to jog their memory is more essence.  A crystal, to be precise.<BR>The resulting elixirs contain nine full doses of potion per flask.  I must be careful not to accidentally overindulge with such tiny sips needed.<BR>The mana necessary to prepare these elixirs has again tripled, but that is alright; mana is not some treasure to be hoarded, after all.")
                .end()
            .addenda()
                .add("Some elixir recipes require the use of Blood essence.")
                .add("Some elixir recipes require the use of Infernal essence.")
                .end()
            .build();
        this.researchEntry("CONCOCTING_BOMBS").name("Concocting: Bombs")
            .stages()
                .add("Some potions just aren't meant to be drunk, at least by oneself.  Or perhaps meant to be shared?<BR>Either way, skyglass flasks don't shatter like the regular glass bottles in splash potions do.  And even if they did, who wants to waste all those doses of potion at once?<BR>I must devise a new method of distribution.")
                .add("Bombs!  Bombs are great!<BR>By creating a set of special bomb casings, I can distribute individual doses of potion to anyone at range.  A single set of casings will produce six bombs.<BR>Alchemical bombs don't just explode on impact, necessarily.  I mean, they will if you score a direct hit on a creature, but if you miss, they'll bounce.  Bombs are on a timed fuse and will detonate when that time runs out.  I can change the length of the fuse by using it while sneaking.")
                .end()
            .addenda()
                .add("Some bomb recipes requires the use of Blood essence.")
                .add("Some bomb recipes requires the use of Infernal essence.")
                .add("Some bomb recipes requires the use of Void essence.")
                .end()
            .build();
        this.researchEntry("ENTROPY_SINK").name("Entropy Sink")
            .stages()
                .add("Ritual magick is dangerous stuff, I've learned.  Even the simplest ritual can destabilize quickly.  Perhaps I can design a device to make them safer?")
                .add("I've designed a machine that will capture stray magickal energies in the environment and convert them to harmless light.<BR>The machine functions like a normal ritual prop, but one that can be used with any ritual.  To activate it, I must use a unit of magickal essence on the device when the ritual calls for it; if I do it before then, the machine will have no effect.  Once activated, it will generate a burst of stability and allow the ritual to progress, with more potent essence yielding greater stability gains.<BR>Unlike most props, the entropy sink does not have to be manually reset.  It does, however, require a minute to cool back down after use and cannot be used in a ritual again until then.")
                .end()
            .build();
        this.researchEntry("AUTO_CHARGER").name("Auto-Charger")
            .stages()
                .add("As my wands can hold more mana, keeping them charged has become a challenge.  Even with mana fonts at home, it can take minutes of channeling to get one to full.<BR>I have an idea for something to ease this process...")
                .add("My newest device is similar to a Mana Charger, but rather than consuming essence it will automatically siphon from nearby fonts.<BR>Only one wand can be held in the charger at a time, and it only has a range of five blocks, but it can pull from multiple fonts at once.<BR>Best of all, I can just drop the wand in and walk away to do other things while it works.  I think this is going to save me a lot of time.")
                .end()
            .build();
        this.researchEntry("ESSENCE_TRANSMUTER").name("Essence Transmuter")
            .stages()
                .add("Managing my essence stocks is a tricky feat.  Earth essence appears to be in almost everything, while some other sources are much harder to come by.<BR>I wonder if there's anything I can do to help balance this out.")
                .add("I've designed a new magitech device that I call an Essence Transmuter.  It utilizes the protean magick of the Moon to convert one source of essence into another source.<BR>The process is a very lossy one, though.  Currently it takes eight units of the same type of essence to produce one unit of another type of essence.  In addition, the source of essence that I get back is random, beyond my control.<BR>Like some other devices, the Essence Transmuter requires a certain amount of configuration.  It's not difficult, and I can do it automatically as part of placing the block, but it does mean that another mage's transmuter won't be set up precisely like my own.  Results could be unpredictable.")
                .end()
            .build();
        this.researchEntry("DISSOLUTION_CHAMBER").name("Dissolution Chamber")
            .stages()
                .add("Even using an Earthshatter Hammer, my works present an untenable demand upon my mineral stocks.  There must be a way to stretch my stores even further.<BR>I have more important things to do than dig in the dirt like some peasant!")
                .add("Brilliant!  With the careful application of Earth mana, I can now precisely dissolve select types of mineral.  The end result of this process is a larger amount of pure metals to be smelted into ingots.<BR>I can also utilize this process to break down stone into ever-finer particles with minimal loss of mass.  Very useful for when I need to create large quantities of sand for glazing.  There may even be other things that I can dissolve this way.")
                .end()
            .build();
        this.researchEntry("ZEPHYR_ENGINE").name("Zephyr Engine")
            .stages()
                .add("Larger machines are often focused on moving things around, but pistons are so short-ranged and water canals are messy.  Perhaps I can design a machine to generate a strong wind to accomplish the same thing, but better?")
                .add("Action at a distance doesn't have to be magickal, though the magick does make it a lot cooler.  This new Zephyr Engine will push any items or creatures in front of it away.<BR>The engine's output scales with the strength of the redstone signal it receives as input.  Stronger signals will result in more powerful wind that reaches farther away and pushes things faster.")
                .end()
            .build();
        this.researchEntry("VOID_TURBINE").name("Void Turbine")
            .stages()
                .add("Sometimes I don't want to push things away, but rather to pull them in.  Whether into collection mechanisms or diabolical traps, I want none to escape my grasp.")
                .add("Augmented with the power of the void, this wind current generator suits my needs.  This new Void Turbine will pull any items or creatures in front of it towards itself.<BR>The turbine's suction scales with the strength of the redstone signal it receives as input.  Stronger signals will result in more powerful wind that reaches farther away and pulls things faster.")
                .end()
            .build();
        this.researchEntry("INFERNAL_FURNACE").name("Infernal Furnace")
            .stages()
                .add("These confounded furnaces are agonizing!  My hands are black from always having to shovel in endless amounts of coal to fuel them.  Surely with the power at my disposal I can do better.")
                .add("I've devised an improved model of furnace that smelts materials purely using heat generated by converting Infernal mana into raw flames.  It's much more efficient than a standard furnace, using only a fraction of a point of mana per item, and I don't have to shovel coal anymore!<BR>Still, I was hoping for something that could also process materials more quickly.  Five seconds to smelt an ingot of iron isn't that bad, I suppose, but I was really hoping for better.  Perhaps another discovery will help me improve this device further.")
                .end()
            .addenda()
                .add("Ignyx, of course!  This infernally infused fuel will be perfect for supercharging my Infernal Furnace.<BR>If I supplement the Infernal mana injected into the furnace with a unit of Ignyx, the smelting process will go several times faster than normal.  Though I suppose that means I'm back to shoveling coal.  Alas.  At least it's optional now.")
                .end()
            .build();
        this.researchEntry("MANA_NEXUS").name("Mana Nexus")
            .stages()
                .add("I grow tired of carting around wands like batteries.  There must be a better way to store mana for long periods of time for quick charging.")
                .add("With careful study of the flows of mana and practice in its applications, I have created the Mana Nexus.<BR>Like the Auto-Charger before it, the Mana Nexus will siphon mana directly from any nearby mana fonts.  This improved model, however, will keep that mana in internal storage indefinitely, rather than requiring immediate application to a wand.<BR>This allow me to keep greater reserves of power on hand.  In addition, the Mana Nexus will also accept mana from any essence or spare wands I have lying around.")
                .end()
            .build();
        this.researchEntry("MANA_SINGULARITY").name("Mana Singularity")
            .stages()
                .add("The Mana Nexus is an admirable effort, but I believe I can do better.")
                .add("The universe sings its blessings once more.  With sanctified parts and a core infused with more potent essence, this new Mana Singularity can store more mana than ever.")
                .end()
            .build();
        this.researchEntry("WARDING_MODULE").name("Warding Module")
            .stages()
                .add("Primalite armor is pretty potent, but nobody likes getting hit in the face, even while armored.  I wonder if there's something I can do to protect myself further?")
                .add("Nice!  A little runescribing here, a little magitech there, and I've got a device which will generate a magickal barrier around me!<BR>This Warding Module can be attached to any piece of magickal metal armor by combining them at a crafting table.  While attached, it will protect me by granting extra temporary health.<BR>This protection isn't free, though.  The ward requires Earth mana to generate, and I can't just siphon it from my wand directly.  I'll need to infuse the armor with mana using a Mana Charger or similar device befor the ward will activate.")
                .end()
            .build();
        this.researchEntry("GREATER_WARDING_MODULE").name("Greater Warding Module")
            .stages()
                .add("As the threats facing my person grow in power, so must my protection.  A more potent Warding Module is required.")
                .add("This will do for now.  By using more powerful runes and more potent magickal metal parts, I've designed a new warding module that's half again as powerful as the base model.<BR>It still requires Earth mana to function and must be attached to magickal metal armor, but that's a small price to pay for life and limb.")
                .end()
            .build();
        this.researchEntry("SUPREME_WARDING_MODULE").name("Supreme Warding Module")
            .stages()
                .add("Life is a gift that must not be squandered.  It must be cherished and protected.  With magickal barriers.")
                .add("Further upgrades have yielded a new warding module design fully twice as effective as the base model.  My confidence in my safety is higher than ever.")
                .end()
            .build();
        this.researchEntry("RAW_MARBLE").name("Marble")
            .stages()
                .add("Marble is a lovely variety of stone, but that's not what makes it so special.  It synergizes with magick, reflecting, absorbing, or redirecting it at the will of the mage.<BR>I can see why the ancients used marble to build their shrines.  It will be very important in many of my workings.")
                .end()
            .build();
        this.researchEntry("HALLOWED_ORB").name("Hallowed Orb")
            .stages()
                .add("Now just what was such a powerful creature of darkness doing with this?  The orb hums with a soothing power of a sort I've never felt before.  And I do mean hum: I can feel, rather than hear, music in my heart when I hold it.<BR>I want to see this power grow, see what it will become when mature.  Perhaps if I infuse its energy into a tree sapling...")
                .end()
            .build();
        this.researchEntry("HALLOWOOD_TREES").name("Hallowood Trees")
            .stages()
                .add("Positively wondrous!  To think that such beauty could be the end product of the cleansing rite I've undertaken.<BR>These trees resonate with Hallowed energy, seeming to draw life from the very creative forces of the universe, rather than mere sunlight.  Perhaps I could learn more by studying their essence.<BR>Yes, that feels right.  Nurturing life to gain knowledge.  Perhaps this is a lesson in humility that I sorely need.")
                .end()
            .build();
        this.researchEntry("SUNWOOD_TREES").name("Sunwood Trees")
            .stages()
                .add("These magickal trees wax and wane over time.  During the day they're fully present, but at night they fade out to near invisibility.<BR>They seem strongly aligned with the Sun, and could prove a useful source of essence of that type.<BR>Alternately, I could make some tasteful decoration with it.  The wood loses its phasing properties with too much processing, though.")
                .end()
            .build();
        this.researchEntry("MOONWOOD_TREES").name("Moonwood Trees")
            .stages()
                .add("These magickal trees wax and wane over time.  During the night they're fully present, but in they daytime they fade out to near invisibility.<BR>They seem strongly aligned with the Moon, and could prove a useful source of essence of that type.<BR>Alternately, I could make some tasteful decoration with it.  The wood loses its phasing properties with too much processing, though.")
                .end()
            .build();
        this.researchEntry("ROCK_SALT").name("Rock Salt")
            .stages()
                .add("Ah, salt.  This delicious mineral can be found all over the place.<BR>Rock salt needs to be refined in a furnace before it's of much use, though.  The Grimoire seems to hint that it has interesting interactions with magick, but doesn't go into detail.<BR>In time, perhaps I will learn more.  Until then, I can use it to season my food to make it tastier and more filling!")
                .end()
            .build();
        this.researchEntry("ALCHEMICAL_WASTE").name("Alchemical Waste")
            .stages()
                .add("Oh, gross.  What is this garbage?  It's not essence, that's for sure.<BR>I thought I had my equipment properly configured to process every type of magickal essence there was.  Perhaps this waste is an indicator that I haven't learned as much as I think.<BR>I should keep an eye out for objects bearing unknown magickal signatures.  Save them for later, maybe.  This gunk, on the other hand, I can definitely throw out.")
                .end()
            .build();
        this.researchEntry("QUARTZ").name("Quartz")
            .stages()
                .add("Curious...<BR>I've heard tell of this mineral quartz before, but I was always under the impression that it wasn't found in the Overworld.  Guess I was wrong.<BR>Notes in the Grimoire have taught me techniques to carve up a single piece of quartz into multiple slivers.  This should allow me to use it sparingly as needed.")
                .end()
            .build();
        this.researchEntry("INNER_DEMON").name("Inner Demon")
            .stages()
                .add("Wow...  I'm not sure whether it was brave or foolish of me to scan that thing instead of fighting or running, but I got a clear read, at least.  As I suspected, this creature is an amalgam of my darkest impulses, twisted and empowered by forbidden magick.<BR>There doesn't seem to be a safe way to fight this thing.  Its fists are infused with the Infernal, meaning that it will set me aflame with every strike.  But if I'm out of range of that, it will instead lash out with Blood magick, ignoring my armor and tearing me apart from the inside.<BR>Worst of all, perhaps, is its mastery of Void magick.  Every so often, it will use that to summon a Sin Crystal which will steadily heal it until destroyed.  But the crystals project a damaging magickal aura which will hurt me if I get too close to them.  I'll have to deal with those quickly and carefully.")
                .end()
            .build();
        this.researchEntry("BOOKSHELF").name("Bookshelf")
            .stages()
                .add("If there's one thing a mage can never have enough of, it's books.  That and places to store them.<BR>Many of the books I come across will contain reference material valuable in my work.  A bookshelf would make a valuable research aid for my research table.")
                .end()
            .build();
        this.researchEntry("BEEHIVE").name("Beehive")
            .stages()
                .add("Look at those little bugs go!  Their movements are hypnotic, and I can't help but feel like there may be a greater meaning in their seeming chaos.<BR>Maybe a beehive would make a good research aid for my research table?")
                .end()
            .build();
        this.researchEntry("BEACON").name("Beacon")
            .stages()
                .add("It may not be tied to any of the primal sources, but this thing is definitely magickal.  I could write a book on the study of its emanations and their effects.<BR>This would definitely make a valuable research aid for my research table.")
                .end()
            .build();
        this.researchEntry("DRAGON_EGG").name("Dragon Egg")
            .stages()
                .add("This egg is more than just a trophy.  If my analysis is correct, it's a powerful magickal artifact in its own right.<BR>I wonder what I could learn if I used it as a research aid for my research table?")
                .end()
            .build();
        this.researchEntry("DRAGON_HEAD").name("Dragon Head")
            .stages()
                .add("A strange vitality courses through this dragon head.  While definitely dead, it still seems to hunger for... something.<BR>Perhaps I can utilize this deathless hunger as a research aid somehow?")
                .end()
            .build();
        this.researchEntry("MYSTICAL_RELIC").name("Mystical Relics")
            .stages()
                .add("It appears the ancients left more than just shrines behind.  They also left knowledge in the form of inscribed stone tablets.<BR>Of course, finding an intact one seems impossible.  I'll have to assemble these relics from fragments that I recover in the world.<BR>Studying a completed relic will reveal to me a new theory!  Or, if I need the emeralds, a Librarian villager might be interested in buying them from me.")
                .end()
            .build();
        this.researchEntry("HUMMING_ARTIFACT").name("Humming Artifact")
            .stages()
                .add("What a curious artifact.  Its magick makes my fingers tingle, but it feels somehow... incomplete.  Perhaps if I were to combine it with some magickal essence?")
                .end()
            .addenda()
                .add("The artifact also responds to Blood essence.")
                .add("The artifact also responds to Infernal essence.")
                .add("The artifact also responds to Void essence.")
                .add("The artifact also responds to Hallowed essence.")
                .end()
            .build();
        this.researchEntry("TREEFOLK").name("Treefolk")
            .stages()
                .add("What fascinating creatures!  These treefolk appear to be ambulatory plant life.  Not only that, but they seem intelligent!<BR>For the most part, these creatures seem peaceful, or at least content to live and let live.  They will defend themselves if attacked, but will otherwise go about their business and leave me to mine.<BR>Treefolk appear to have a special love of bone meal, which I suppose shouldn't be too surprising given its nutritive properties for plants.  They seem willing to barter various plants they've collected in exchange for the material.  I can either hand the bone meal to them, or just drop it on the ground nearby.  I wonder what they do with the stuff, though; they don't have visible mouths to eat it.  Just one of those mysteries, I guess.<BR>Perhaps most exciting, however, is that treefolk appear to be innately magickal.  They appear to be able to speed the growth of most plant life with a touch!  If only I could convince them to follow me home...  They'd do wonders for my farm.")
                .end()
            .build();
    }
}
