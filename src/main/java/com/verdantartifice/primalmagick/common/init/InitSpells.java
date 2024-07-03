package com.verdantartifice.primalmagick.common.init;

import com.verdantartifice.primalmagick.common.spells.SpellManager;
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

/**
 * Point of registration for mod spell vehicles, spell payloads, and spell mods.  Registration order
 * determines the order in which they appear in the spellcrafting GUI.
 * 
 * @author Daedalus4096
 * @deprecated
 */
@Deprecated
public class InitSpells {
    public static void initSpells() {
        registerSpellVehicleTypes();
        registerSpellPayloadTypes();
        registerSpellModTypes();
    }

    private static void registerSpellVehicleTypes() {
        SpellManager.registerVehicleType(EmptySpellVehicle.TYPE, EmptySpellVehicle::new, EmptySpellVehicle::getRequirement);
        SpellManager.registerVehicleType(TouchSpellVehicle.TYPE, TouchSpellVehicle::new, TouchSpellVehicle::getRequirement);
        SpellManager.registerVehicleType(ProjectileSpellVehicle.TYPE, ProjectileSpellVehicle::new, ProjectileSpellVehicle::getRequirement);
        SpellManager.registerVehicleType(BoltSpellVehicle.TYPE, BoltSpellVehicle::new, BoltSpellVehicle::getRequirement);
        SpellManager.registerVehicleType(SelfSpellVehicle.TYPE, SelfSpellVehicle::new, SelfSpellVehicle::getRequirement);
    }

    private static void registerSpellPayloadTypes() {
        SpellManager.registerPayloadType(EmptySpellPayload.TYPE, EmptySpellPayload::new, EmptySpellPayload::getRequirement);
        SpellManager.registerPayloadType(EarthDamageSpellPayload.TYPE, EarthDamageSpellPayload::new, EarthDamageSpellPayload::getRequirement);
        SpellManager.registerPayloadType(FrostDamageSpellPayload.TYPE, FrostDamageSpellPayload::new, FrostDamageSpellPayload::getRequirement);
        SpellManager.registerPayloadType(LightningDamageSpellPayload.TYPE, LightningDamageSpellPayload::new, LightningDamageSpellPayload::getRequirement);
        SpellManager.registerPayloadType(SolarDamageSpellPayload.TYPE, SolarDamageSpellPayload::new, SolarDamageSpellPayload::getRequirement);
        SpellManager.registerPayloadType(LunarDamageSpellPayload.TYPE, LunarDamageSpellPayload::new, LunarDamageSpellPayload::getRequirement);
        SpellManager.registerPayloadType(BloodDamageSpellPayload.TYPE, BloodDamageSpellPayload::new, BloodDamageSpellPayload::getRequirement);
        SpellManager.registerPayloadType(FlameDamageSpellPayload.TYPE, FlameDamageSpellPayload::new, FlameDamageSpellPayload::getRequirement);
        SpellManager.registerPayloadType(VoidDamageSpellPayload.TYPE, VoidDamageSpellPayload::new, VoidDamageSpellPayload::getRequirement);
        SpellManager.registerPayloadType(HolyDamageSpellPayload.TYPE, HolyDamageSpellPayload::new, HolyDamageSpellPayload::getRequirement);
        SpellManager.registerPayloadType(BreakSpellPayload.TYPE, BreakSpellPayload::new, BreakSpellPayload::getRequirement);
        SpellManager.registerPayloadType(ConjureStoneSpellPayload.TYPE, ConjureStoneSpellPayload::new, ConjureStoneSpellPayload::getRequirement);
        SpellManager.registerPayloadType(ConjureWaterSpellPayload.TYPE, ConjureWaterSpellPayload::new, ConjureWaterSpellPayload::getRequirement);
        SpellManager.registerPayloadType(ShearSpellPayload.TYPE, ShearSpellPayload::new, ShearSpellPayload::getRequirement);
        SpellManager.registerPayloadType(FlightSpellPayload.TYPE, FlightSpellPayload::new, FlightSpellPayload::getRequirement);
        SpellManager.registerPayloadType(ConjureLightSpellPayload.TYPE, ConjureLightSpellPayload::new, ConjureLightSpellPayload::getRequirement);
        SpellManager.registerPayloadType(HealingSpellPayload.TYPE, HealingSpellPayload::new, HealingSpellPayload::getRequirement);
        SpellManager.registerPayloadType(PolymorphWolfSpellPayload.TYPE, PolymorphWolfSpellPayload::new, PolymorphWolfSpellPayload::getRequirement);
        SpellManager.registerPayloadType(PolymorphSheepSpellPayload.TYPE, PolymorphSheepSpellPayload::new, PolymorphSheepSpellPayload::getRequirement);
        SpellManager.registerPayloadType(ConjureAnimalSpellPayload.TYPE, ConjureAnimalSpellPayload::new, ConjureAnimalSpellPayload::getRequirement);
        SpellManager.registerPayloadType(ConjureLavaSpellPayload.TYPE, ConjureLavaSpellPayload::new, ConjureLavaSpellPayload::getRequirement);
        SpellManager.registerPayloadType(DrainSoulSpellPayload.TYPE, DrainSoulSpellPayload::new, DrainSoulSpellPayload::getRequirement);
        SpellManager.registerPayloadType(TeleportSpellPayload.TYPE, TeleportSpellPayload::new, TeleportSpellPayload::getRequirement);
        SpellManager.registerPayloadType(ConsecrateSpellPayload.TYPE, ConsecrateSpellPayload::new, ConsecrateSpellPayload::getRequirement);
    }

    private static void registerSpellModTypes() {
        SpellManager.registerModType(EmptySpellMod.TYPE, EmptySpellMod::new, EmptySpellMod::getRequirement);
        SpellManager.registerModType(AmplifySpellMod.TYPE, AmplifySpellMod::new, AmplifySpellMod::getRequirement);
        SpellManager.registerModType(BurstSpellMod.TYPE, BurstSpellMod::new, BurstSpellMod::getRequirement);
        SpellManager.registerModType(QuickenSpellMod.TYPE, QuickenSpellMod::new, QuickenSpellMod::getRequirement);
        SpellManager.registerModType(MineSpellMod.TYPE, MineSpellMod::new, MineSpellMod::getRequirement);
        SpellManager.registerModType(ForkSpellMod.TYPE, ForkSpellMod::new, ForkSpellMod::getRequirement);
    }
}
