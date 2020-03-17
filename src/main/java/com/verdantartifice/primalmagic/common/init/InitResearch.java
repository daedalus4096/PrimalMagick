package com.verdantartifice.primalmagic.common.init;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.items.ItemsPM;
import com.verdantartifice.primalmagic.common.research.CompoundResearchKey;
import com.verdantartifice.primalmagic.common.research.ResearchDisciplines;
import com.verdantartifice.primalmagic.common.research.ResearchManager;
import com.verdantartifice.primalmagic.common.research.ScanResearchTrigger;
import com.verdantartifice.primalmagic.common.research.ScanSourceUnlockTrigger;
import com.verdantartifice.primalmagic.common.research.SimpleResearchKey;
import com.verdantartifice.primalmagic.common.sources.Source;
import com.verdantartifice.primalmagic.common.stats.StatsPM;
import com.verdantartifice.primalmagic.common.theorycrafting.TheorycraftManager;
import com.verdantartifice.primalmagic.common.theorycrafting.projects.AdvancedEssenceAnalysisProject;
import com.verdantartifice.primalmagic.common.theorycrafting.projects.BeaconEmanationsProject;
import com.verdantartifice.primalmagic.common.theorycrafting.projects.BrewingExperimentsProject;
import com.verdantartifice.primalmagic.common.theorycrafting.projects.DraconicEnergiesProject;
import com.verdantartifice.primalmagic.common.theorycrafting.projects.EnchantingStudiesProject;
import com.verdantartifice.primalmagic.common.theorycrafting.projects.EssenceAnalysisProject;
import com.verdantartifice.primalmagic.common.theorycrafting.projects.ExpeditionProject;
import com.verdantartifice.primalmagic.common.theorycrafting.projects.HitTheBooksProject;
import com.verdantartifice.primalmagic.common.theorycrafting.projects.MundaneTinkeringProject;
import com.verdantartifice.primalmagic.common.theorycrafting.projects.NetherExpeditionProject;
import com.verdantartifice.primalmagic.common.theorycrafting.projects.ObservationAnalysisProject;
import com.verdantartifice.primalmagic.common.theorycrafting.projects.PortalDetritusProject;
import com.verdantartifice.primalmagic.common.theorycrafting.projects.RecuperationProject;
import com.verdantartifice.primalmagic.common.theorycrafting.projects.RedstoneTinkeringProject;
import com.verdantartifice.primalmagic.common.theorycrafting.projects.SpellworkProject;
import com.verdantartifice.primalmagic.common.theorycrafting.projects.TradeProject;
import com.verdantartifice.primalmagic.common.theorycrafting.projects.WandTinkeringProject;

import net.minecraft.util.ResourceLocation;

/**
 * Point of registration for mod research data.
 * 
 * @author Daedalus4096
 */
public class InitResearch {
    public static void initResearch() {
        initDisciplines();
        initResearchFiles();
        ResearchManager.parseAllResearch();
        initScanResearch();
        initResearchProjects();
    }
    
    private static void initDisciplines() {
        ResearchDisciplines.registerDiscipline("BASICS", null, new ResourceLocation(PrimalMagic.MODID, "textures/item/grimoire.png"), null);
        ResearchDisciplines.registerDiscipline("ALCHEMY", CompoundResearchKey.from(SimpleResearchKey.parse("UNLOCK_ALCHEMY")), new ResourceLocation(PrimalMagic.MODID, "textures/research/discipline_alchemy.png"), StatsPM.CRAFTED_ALCHEMY);
        ResearchDisciplines.registerDiscipline("SORCERY", CompoundResearchKey.from(SimpleResearchKey.parse("UNLOCK_SORCERY")), new ResourceLocation(PrimalMagic.MODID, "textures/research/discipline_sorcery.png"), StatsPM.CRAFTED_SORCERY);
        ResearchDisciplines.registerDiscipline("MANAWEAVING", CompoundResearchKey.from(SimpleResearchKey.parse("UNLOCK_MANAWEAVING")), new ResourceLocation(PrimalMagic.MODID, "textures/research/discipline_manaweaving.png"), StatsPM.CRAFTED_MANAWEAVING);
        ResearchDisciplines.registerDiscipline("RUNEWORKING", CompoundResearchKey.from(SimpleResearchKey.parse("UNLOCK_RUNEWORKING")), new ResourceLocation(PrimalMagic.MODID, "textures/research/discipline_runeworking.png"), StatsPM.CRAFTED_RUNEWORKING);
        ResearchDisciplines.registerDiscipline("RITUAL", CompoundResearchKey.from(SimpleResearchKey.parse("UNLOCK_RITUAL")), new ResourceLocation(PrimalMagic.MODID, "textures/research/discipline_ritual.png"), StatsPM.CRAFTED_RITUAL);
        ResearchDisciplines.registerDiscipline("MAGITECH", CompoundResearchKey.from(SimpleResearchKey.parse("UNLOCK_MAGITECH")), new ResourceLocation(PrimalMagic.MODID, "textures/research/discipline_magitech.png"), StatsPM.CRAFTED_MAGITECH);
        ResearchDisciplines.registerDiscipline("SCANS", CompoundResearchKey.from(SimpleResearchKey.parse("UNLOCK_SCANS")), new ResourceLocation(PrimalMagic.MODID, "textures/item/magnifying_glass.png"), null);
    }
    
    private static void initResearchFiles() {
        ResearchDisciplines.registerResearchLocation(new ResourceLocation(PrimalMagic.MODID, "research/basics"));
        ResearchDisciplines.registerResearchLocation(new ResourceLocation(PrimalMagic.MODID, "research/alchemy"));
        ResearchDisciplines.registerResearchLocation(new ResourceLocation(PrimalMagic.MODID, "research/sorcery"));
        ResearchDisciplines.registerResearchLocation(new ResourceLocation(PrimalMagic.MODID, "research/manaweaving"));
        ResearchDisciplines.registerResearchLocation(new ResourceLocation(PrimalMagic.MODID, "research/runeworking"));
        ResearchDisciplines.registerResearchLocation(new ResourceLocation(PrimalMagic.MODID, "research/ritual"));
        ResearchDisciplines.registerResearchLocation(new ResourceLocation(PrimalMagic.MODID, "research/magitech"));
        ResearchDisciplines.registerResearchLocation(new ResourceLocation(PrimalMagic.MODID, "research/scans"));
    }
    
    private static void initScanResearch() {
        ResearchManager.registerScanTrigger(new ScanSourceUnlockTrigger(ItemsPM.HALLOWED_ORB.get(), Source.HALLOWED));
        ResearchManager.registerScanTrigger(new ScanResearchTrigger(ItemsPM.MARBLE_RAW.get(), SimpleResearchKey.parse("RAW_MARBLE")));
    }
    
    private static void initResearchProjects() {
        TheorycraftManager.registerProjectType(TradeProject.TYPE, TradeProject::new);
        TheorycraftManager.registerProjectType(MundaneTinkeringProject.TYPE, MundaneTinkeringProject::new);
        TheorycraftManager.registerProjectType(RedstoneTinkeringProject.TYPE, RedstoneTinkeringProject::new);
        TheorycraftManager.registerProjectType(ExpeditionProject.TYPE, ExpeditionProject::new);
        TheorycraftManager.registerProjectType(NetherExpeditionProject.TYPE, NetherExpeditionProject::new);
        TheorycraftManager.registerProjectType(RecuperationProject.TYPE, RecuperationProject::new);
        TheorycraftManager.registerProjectType(EssenceAnalysisProject.TYPE, EssenceAnalysisProject::new);
        TheorycraftManager.registerProjectType(BrewingExperimentsProject.TYPE, BrewingExperimentsProject::new);
        TheorycraftManager.registerProjectType(WandTinkeringProject.TYPE, WandTinkeringProject::new);
        TheorycraftManager.registerProjectType(SpellworkProject.TYPE, SpellworkProject::new);
        TheorycraftManager.registerProjectType(AdvancedEssenceAnalysisProject.TYPE, AdvancedEssenceAnalysisProject::new);
        TheorycraftManager.registerProjectType(EnchantingStudiesProject.TYPE, EnchantingStudiesProject::new);
        TheorycraftManager.registerProjectType(ObservationAnalysisProject.TYPE, ObservationAnalysisProject::new);
        TheorycraftManager.registerProjectType(HitTheBooksProject.TYPE, HitTheBooksProject::new);
        TheorycraftManager.registerProjectType(BeaconEmanationsProject.TYPE, BeaconEmanationsProject::new);
        TheorycraftManager.registerProjectType(PortalDetritusProject.TYPE, PortalDetritusProject::new);
        TheorycraftManager.registerProjectType(DraconicEnergiesProject.TYPE, DraconicEnergiesProject::new);
    }
}
