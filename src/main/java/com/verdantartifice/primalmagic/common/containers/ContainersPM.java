package com.verdantartifice.primalmagic.common.containers;

import com.verdantartifice.primalmagic.PrimalMagic;

import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.registries.ObjectHolder;

/**
 * Object holder for mod containers.  Actual values populated by Forge post-registration.
 * 
 * @author Daedalus4096
 */
@ObjectHolder(PrimalMagic.MODID)
public class ContainersPM {
    public static final ContainerType<GrimoireContainer> GRIMOIRE = null;
    public static final ContainerType<ArcaneWorkbenchContainer> ARCANE_WORKBENCH = null;
    public static final ContainerType<WandAssemblyTableContainer> WAND_ASSEMBLY_TABLE = null;
    public static final ContainerType<AnalysisTableContainer> ANALYSIS_TABLE = null;
    public static final ContainerType<CalcinatorContainer> CALCINATOR = null;
    public static final ContainerType<WandInscriptionTableContainer> WAND_INSCRIPTION_TABLE = null;
    public static final ContainerType<SpellcraftingAltarContainer> SPELLCRAFTING_ALTAR = null;
    public static final ContainerType<WandChargerContainer> WAND_CHARGER = null;
}
