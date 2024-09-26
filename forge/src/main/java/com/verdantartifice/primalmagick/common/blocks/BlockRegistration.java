package com.verdantartifice.primalmagick.common.blocks;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.blocks.base.SaplingBlockPM;
import com.verdantartifice.primalmagick.common.blocks.crafting.ArcaneWorkbenchBlock;
import com.verdantartifice.primalmagick.common.blocks.crafting.CalcinatorBlock;
import com.verdantartifice.primalmagick.common.blocks.crafting.ConcocterBlock;
import com.verdantartifice.primalmagick.common.blocks.crafting.EssenceFurnaceBlock;
import com.verdantartifice.primalmagick.common.blocks.crafting.RunecarvingTableBlock;
import com.verdantartifice.primalmagick.common.blocks.crafting.RunescribingAltarBlock;
import com.verdantartifice.primalmagick.common.blocks.crafting.RunicGrindstoneBlock;
import com.verdantartifice.primalmagick.common.blocks.crafting.SpellcraftingAltarBlock;
import com.verdantartifice.primalmagick.common.blocks.crafting.WandAssemblyTableBlock;
import com.verdantartifice.primalmagick.common.blocks.crafting.WandGlamourTableBlock;
import com.verdantartifice.primalmagick.common.blocks.crafting.WandInscriptionTableBlock;
import com.verdantartifice.primalmagick.common.blocks.crops.HydromelonBlock;
import com.verdantartifice.primalmagick.common.blocks.devices.AnalysisTableBlock;
import com.verdantartifice.primalmagick.common.blocks.devices.DissolutionChamberBlock;
import com.verdantartifice.primalmagick.common.blocks.devices.EssenceCaskBlock;
import com.verdantartifice.primalmagick.common.blocks.devices.EssenceTransmuterBlock;
import com.verdantartifice.primalmagick.common.blocks.devices.HoneyExtractorBlock;
import com.verdantartifice.primalmagick.common.blocks.devices.InfernalFurnaceBlock;
import com.verdantartifice.primalmagick.common.blocks.devices.ResearchTableBlock;
import com.verdantartifice.primalmagick.common.blocks.devices.SanguineCrucibleBlock;
import com.verdantartifice.primalmagick.common.blocks.devices.ScribeTableBlock;
import com.verdantartifice.primalmagick.common.blocks.devices.SunlampBlock;
import com.verdantartifice.primalmagick.common.blocks.devices.VoidTurbineBlock;
import com.verdantartifice.primalmagick.common.blocks.devices.ZephyrEngineBlock;
import com.verdantartifice.primalmagick.common.blocks.flowers.BloodRoseBlock;
import com.verdantartifice.primalmagick.common.blocks.flowers.EmberflowerBlock;
import com.verdantartifice.primalmagick.common.blocks.golems.HallowsteelGolemControllerBlock;
import com.verdantartifice.primalmagick.common.blocks.golems.HexiumGolemControllerBlock;
import com.verdantartifice.primalmagick.common.blocks.golems.PrimaliteGolemControllerBlock;
import com.verdantartifice.primalmagick.common.blocks.mana.AncientManaFontBlock;
import com.verdantartifice.primalmagick.common.blocks.mana.ArtificialManaFontBlock;
import com.verdantartifice.primalmagick.common.blocks.mana.AutoChargerBlock;
import com.verdantartifice.primalmagick.common.blocks.mana.ManaBatteryBlock;
import com.verdantartifice.primalmagick.common.blocks.mana.WandChargerBlock;
import com.verdantartifice.primalmagick.common.blocks.minerals.BuddingGemClusterBlock;
import com.verdantartifice.primalmagick.common.blocks.minerals.BuddingGemSourceBlock;
import com.verdantartifice.primalmagick.common.blocks.minerals.GemBudType;
import com.verdantartifice.primalmagick.common.blocks.misc.CarvedBookshelfBlock;
import com.verdantartifice.primalmagick.common.blocks.misc.ConsecrationFieldBlock;
import com.verdantartifice.primalmagick.common.blocks.misc.EnderwardBlock;
import com.verdantartifice.primalmagick.common.blocks.misc.GlowFieldBlock;
import com.verdantartifice.primalmagick.common.blocks.misc.PillarBlock;
import com.verdantartifice.primalmagick.common.blocks.misc.SkyglassBlock;
import com.verdantartifice.primalmagick.common.blocks.misc.SkyglassPaneBlock;
import com.verdantartifice.primalmagick.common.blocks.misc.StainedSkyglassBlock;
import com.verdantartifice.primalmagick.common.blocks.misc.StainedSkyglassPaneBlock;
import com.verdantartifice.primalmagick.common.blocks.misc.WoodTableBlock;
import com.verdantartifice.primalmagick.common.blocks.rituals.BloodletterBlock;
import com.verdantartifice.primalmagick.common.blocks.rituals.CelestialHarpBlock;
import com.verdantartifice.primalmagick.common.blocks.rituals.EntropySinkBlock;
import com.verdantartifice.primalmagick.common.blocks.rituals.IncenseBrazierBlock;
import com.verdantartifice.primalmagick.common.blocks.rituals.OfferingPedestalBlock;
import com.verdantartifice.primalmagick.common.blocks.rituals.RitualAltarBlock;
import com.verdantartifice.primalmagick.common.blocks.rituals.RitualBellBlock;
import com.verdantartifice.primalmagick.common.blocks.rituals.RitualCandleBlock;
import com.verdantartifice.primalmagick.common.blocks.rituals.RitualLecternBlock;
import com.verdantartifice.primalmagick.common.blocks.rituals.SaltTrailBlock;
import com.verdantartifice.primalmagick.common.blocks.rituals.SoulAnvilBlock;
import com.verdantartifice.primalmagick.common.blocks.trees.MoonwoodLeavesBlock;
import com.verdantartifice.primalmagick.common.blocks.trees.MoonwoodLogBlock;
import com.verdantartifice.primalmagick.common.blocks.trees.MoonwoodPillarBlock;
import com.verdantartifice.primalmagick.common.blocks.trees.MoonwoodPlanksBlock;
import com.verdantartifice.primalmagick.common.blocks.trees.MoonwoodSlabBlock;
import com.verdantartifice.primalmagick.common.blocks.trees.MoonwoodStairsBlock;
import com.verdantartifice.primalmagick.common.blocks.trees.StrippableLogBlock;
import com.verdantartifice.primalmagick.common.blocks.trees.SunwoodLeavesBlock;
import com.verdantartifice.primalmagick.common.blocks.trees.SunwoodLogBlock;
import com.verdantartifice.primalmagick.common.blocks.trees.SunwoodPillarBlock;
import com.verdantartifice.primalmagick.common.blocks.trees.SunwoodPlanksBlock;
import com.verdantartifice.primalmagick.common.blocks.trees.SunwoodSlabBlock;
import com.verdantartifice.primalmagick.common.blocks.trees.SunwoodStairsBlock;
import com.verdantartifice.primalmagick.common.blocks.trees.TreeGrowersPM;
import com.verdantartifice.primalmagick.common.blocks.trees.TreefolkSproutBlock;
import com.verdantartifice.primalmagick.common.items.ItemReferencesPM;
import com.verdantartifice.primalmagick.common.misc.DeviceTier;
import com.verdantartifice.primalmagick.common.sources.Sources;
import com.verdantartifice.primalmagick.common.tags.BlockTagsPM;
import net.minecraft.core.BlockPos;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.AttachedStemBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.StemBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Optional;

/**
 * Deferred registry for mod blocks.
 * 
 * @author Daedalus4096
 */
public class BlockRegistration {
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Constants.MOD_ID);

    public static DeferredRegister<Block> getDeferredRegister() {
        return BLOCKS;
    }
    
    public static void init() {
        BLOCKS.register(PrimalMagick.getModLoadingContext().getModEventBus());
    }
}
