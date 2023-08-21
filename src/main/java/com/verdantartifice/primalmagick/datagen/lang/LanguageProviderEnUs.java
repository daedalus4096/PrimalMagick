package com.verdantartifice.primalmagick.datagen.lang;

import com.verdantartifice.primalmagick.common.attunements.AttunementThreshold;
import com.verdantartifice.primalmagick.common.attunements.AttunementType;
import com.verdantartifice.primalmagick.common.blocks.BlocksPM;
import com.verdantartifice.primalmagick.common.enchantments.EnchantmentsPM;
import com.verdantartifice.primalmagick.common.entities.EntityTypesPM;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.research.KnowledgeType;
import com.verdantartifice.primalmagick.common.research.ResearchDisciplines;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.spells.mods.EmptySpellMod;
import com.verdantartifice.primalmagick.common.spells.payloads.EarthDamageSpellPayload;
import com.verdantartifice.primalmagick.common.spells.vehicles.TouchSpellVehicle;
import com.verdantartifice.primalmagick.common.wands.WandCap;
import com.verdantartifice.primalmagick.common.wands.WandCore;
import com.verdantartifice.primalmagick.common.wands.WandGem;

import net.minecraft.data.PackOutput;

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
        // Generate magickal source localizations
        this.source(Source.EARTH).name("Earth")
            .attunement("Minor attunement to the Earth source makes it easier to channel mana.  In practice, I'll pay 5% less Earth mana for all purposes.<BR>Lesser attunement to the Earth will grant me increased stamina, allowing me to swing swords and tools faster without tiring.<BR>Finally, greater attunement to the Earth will cause the very ground to rise beneath my feet when I walk, allowing me to step up the full height of a block without needing to jump.")
            .build();
        this.source(Source.SEA).name("Sea").build();
        this.source(Source.SKY).name("Sky").build();
        this.source(Source.SUN).name("Sun").build();
        this.source(Source.MOON).name("Moon").build();
        this.source(Source.BLOOD).name("Blood").build();
        this.source(Source.INFERNAL).name("Infernal").build();
        this.source(Source.VOID).name("Void").build();
        this.source(Source.HALLOWED).name("Hallowed").build();
        this.add(Source.getUnknownTranslationKey(), "Unknown");
        
        // Generate block localizations
        this.block(BlocksPM.MARBLE_RAW).name("Marble").build();
        
        // Generate item localizations
        this.item(ItemsPM.HALLOWED_ORB).name("Hallowed Orb").tooltip("A sense of peace washes over", "you as you hold this").build();
        this.item(ItemsPM.RUNE_EARTH).name("Earth Rune").tooltip("\"Teq\"").build();
        
        // Generate entity type localizations
        this.entity(EntityTypesPM.TREEFOLK).name("Treefolk").build();
        
        // Generate enchantment localizations
        this.enchantment(EnchantmentsPM.LIFESTEAL).name("Lifesteal")
            .description("Grants a chance for the wielder to heal themselves a small amount when striking a creature.")
            .fullRuneText("The Lifesteal enchantment can be imbued through the use of Absorb, Self, and Blood runes.  It can be applied to any sword, axe, or trident.  When applied, it grants a chance for the wielder to heal themselves a small amount when striking a creature.")
            .partialRuneText("The Lifesteal enchantment can be imbued through the use of runes, though I'm still learning which ones.  It can be applied to any sword, axe, or trident.  When applied, it grants a chance for the wielder to heal themselves a small amount when striking a creature.")
            .build();
        
        // Generate wand component localizations
        this.wandComponent(WandCore.HEARTWOOD).name("Heartwood").build();
        this.wandComponent(WandCap.IRON).name("Iron-Shod").build();
        this.wandComponent(WandGem.APPRENTICE).name("Apprentice's").build();
        
        // Generate research discipline localizations
        this.researchDiscipline(ResearchDisciplines.BASICS).name("Fundamentals").build();
        
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
            .build();
        this.attunementThreshold(AttunementThreshold.LESSER).name("Lesser")
            .effect(Source.EARTH, "Increased attack/mining speed")
            .build();
        this.attunementThreshold(AttunementThreshold.GREATER).name("Greater")
            .effect(Source.EARTH, "Step height increase")
            .build();
        
        // Generate command output localizations
        this.command("research").sub("grant").output("Granting research to %1$s: %2$s").end().build();
        this.command("research").sub("grant").sub("target").output("%1$s has granted you %2$s research and its parents").end().build();
        this.command("research").sub("details").output("Research %1$s of player %2$s:", "  Status: %1$s", "  Current stage: %1$d", "  Flags: %1$s").end().build();
        this.command("error").name("Error executing command").build();
        
        // Generate event output localizations
        this.event("add_addendum").name("Addendum added to %1$s").build();
        this.event("scan").sub("success").output("You have gained valuable research data").end().build();
        this.event("scan").sub("fail").output("You are unable to scan that").end().build();
        this.event("scan").sub("repeat").output("You learn nothing new from scanning that").end().build();
        this.event("scan").sub("toobig").output("There are too many items there to scan completely").end().build();
        
        // Generate written book localizations
        this.book("dream_journal").name("Dream Journal")
            .text("\"I dreamed of the shrine last night. The same strange energy still permeated the air, but this time I knew the word for it.\n\nMagick.\n\nAs if the word unlocked something in my mind, I knew what to do. In the dream, I dug beneath the base of\"")
            .text("\"the shrine and found stone laced with a curious dust. Sensing more magick within it, I took a handful of the dust and rubbed it onto an ordinary stick.\n\nSo imbued, the stick became something more. In the dream, I took it and waved it at a bookcase.\"")
            .text("\"The dream ended before I could see what resulted, but I feel like it would have been something wondrous.\n\nI feel like this could be a key to something amazing, if I just have the courage to take that first step.\"")
            .build();
        
        // Generate spell vehicle localizations
        this.add("spells.primalmagick.vehicle.header", "Spell Type");
        this.spellVehicle(TouchSpellVehicle.TYPE).name("Touch").defaultName("Hand").build();
        
        // Generate spell payload localizations
        this.add("spells.primalmagick.payload.header", "Spell Payload");
        this.spellPayload(EarthDamageSpellPayload.TYPE).name("Earth Damage").defaultName("Earthen").detailTooltip("Earth Damage (%1$s damage)").build();
        
        // Generate spell mod localizations
        this.add("spells.primalmagick.primary_mod.header", "Primary Mod");
        this.add("spells.primalmagick.secondary_mod.header", "Secondary Mod");
        this.spellMod(EmptySpellMod.TYPE).name("None").defaultName("").build();
        
        // Generate research entry localizations
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
    }
}
