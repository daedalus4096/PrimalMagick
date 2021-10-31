package com.verdantartifice.primalmagic.common.init;

import com.verdantartifice.primalmagic.common.spells.SpellManager;
import com.verdantartifice.primalmagic.common.spells.mods.AmplifySpellMod;
import com.verdantartifice.primalmagic.common.spells.mods.BurstSpellMod;
import com.verdantartifice.primalmagic.common.spells.mods.EmptySpellMod;
import com.verdantartifice.primalmagic.common.spells.mods.ForkSpellMod;
import com.verdantartifice.primalmagic.common.spells.mods.MineSpellMod;
import com.verdantartifice.primalmagic.common.spells.mods.QuickenSpellMod;
import com.verdantartifice.primalmagic.common.spells.payloads.BloodDamageSpellPayload;
import com.verdantartifice.primalmagic.common.spells.payloads.BreakSpellPayload;
import com.verdantartifice.primalmagic.common.spells.payloads.ConjureAnimalSpellPayload;
import com.verdantartifice.primalmagic.common.spells.payloads.ConjureLavaSpellPayload;
import com.verdantartifice.primalmagic.common.spells.payloads.ConjureLightSpellPayload;
import com.verdantartifice.primalmagic.common.spells.payloads.ConjureStoneSpellPayload;
import com.verdantartifice.primalmagic.common.spells.payloads.ConjureWaterSpellPayload;
import com.verdantartifice.primalmagic.common.spells.payloads.ConsecrateSpellPayload;
import com.verdantartifice.primalmagic.common.spells.payloads.DrainSoulSpellPayload;
import com.verdantartifice.primalmagic.common.spells.payloads.EarthDamageSpellPayload;
import com.verdantartifice.primalmagic.common.spells.payloads.EmptySpellPayload;
import com.verdantartifice.primalmagic.common.spells.payloads.FlameDamageSpellPayload;
import com.verdantartifice.primalmagic.common.spells.payloads.FlightSpellPayload;
import com.verdantartifice.primalmagic.common.spells.payloads.FrostDamageSpellPayload;
import com.verdantartifice.primalmagic.common.spells.payloads.HealingSpellPayload;
import com.verdantartifice.primalmagic.common.spells.payloads.HolyDamageSpellPayload;
import com.verdantartifice.primalmagic.common.spells.payloads.LightningDamageSpellPayload;
import com.verdantartifice.primalmagic.common.spells.payloads.LunarDamageSpellPayload;
import com.verdantartifice.primalmagic.common.spells.payloads.PolymorphSpellPayload;
import com.verdantartifice.primalmagic.common.spells.payloads.SolarDamageSpellPayload;
import com.verdantartifice.primalmagic.common.spells.payloads.TeleportSpellPayload;
import com.verdantartifice.primalmagic.common.spells.payloads.VoidDamageSpellPayload;
import com.verdantartifice.primalmagic.common.spells.vehicles.BoltSpellVehicle;
import com.verdantartifice.primalmagic.common.spells.vehicles.EmptySpellVehicle;
import com.verdantartifice.primalmagic.common.spells.vehicles.ProjectileSpellVehicle;
import com.verdantartifice.primalmagic.common.spells.vehicles.SelfSpellVehicle;
import com.verdantartifice.primalmagic.common.spells.vehicles.TouchSpellVehicle;

/**
 * Point of registration for mod spell vehicles, spell payloads, and spell mods.  Registration order
 * determines the order in which they appear in the spellcrafting GUI.
 * 
 * @author Daedalus4096
 */
public class InitSpells {
    public static void initSpells() {
        registerSpellVehicleTypes();
        registerSpellPayloadTypes();
        registerSpellModTypes();
    }

    private static void registerSpellVehicleTypes() {
        SpellManager.registerVehicleType(EmptySpellVehicle.TYPE, EmptySpellVehicle::new, EmptySpellVehicle::getResearch);
        SpellManager.registerVehicleType(TouchSpellVehicle.TYPE, TouchSpellVehicle::new, TouchSpellVehicle::getResearch);
        SpellManager.registerVehicleType(ProjectileSpellVehicle.TYPE, ProjectileSpellVehicle::new, ProjectileSpellVehicle::getResearch);
        SpellManager.registerVehicleType(BoltSpellVehicle.TYPE, BoltSpellVehicle::new, BoltSpellVehicle::getResearch);
        SpellManager.registerVehicleType(SelfSpellVehicle.TYPE, SelfSpellVehicle::new, SelfSpellVehicle::getResearch);
    }

    private static void registerSpellPayloadTypes() {
        SpellManager.registerPayloadType(EmptySpellPayload.TYPE, EmptySpellPayload::new, EmptySpellPayload::getResearch);
        SpellManager.registerPayloadType(EarthDamageSpellPayload.TYPE, EarthDamageSpellPayload::new, EarthDamageSpellPayload::getResearch);
        SpellManager.registerPayloadType(FrostDamageSpellPayload.TYPE, FrostDamageSpellPayload::new, FrostDamageSpellPayload::getResearch);
        SpellManager.registerPayloadType(LightningDamageSpellPayload.TYPE, LightningDamageSpellPayload::new, LightningDamageSpellPayload::getResearch);
        SpellManager.registerPayloadType(SolarDamageSpellPayload.TYPE, SolarDamageSpellPayload::new, SolarDamageSpellPayload::getResearch);
        SpellManager.registerPayloadType(LunarDamageSpellPayload.TYPE, LunarDamageSpellPayload::new, LunarDamageSpellPayload::getResearch);
        SpellManager.registerPayloadType(BloodDamageSpellPayload.TYPE, BloodDamageSpellPayload::new, BloodDamageSpellPayload::getResearch);
        SpellManager.registerPayloadType(FlameDamageSpellPayload.TYPE, FlameDamageSpellPayload::new, FlameDamageSpellPayload::getResearch);
        SpellManager.registerPayloadType(VoidDamageSpellPayload.TYPE, VoidDamageSpellPayload::new, VoidDamageSpellPayload::getResearch);
        SpellManager.registerPayloadType(HolyDamageSpellPayload.TYPE, HolyDamageSpellPayload::new, HolyDamageSpellPayload::getResearch);
        SpellManager.registerPayloadType(BreakSpellPayload.TYPE, BreakSpellPayload::new, BreakSpellPayload::getResearch);
        SpellManager.registerPayloadType(ConjureStoneSpellPayload.TYPE, ConjureStoneSpellPayload::new, ConjureStoneSpellPayload::getResearch);
        SpellManager.registerPayloadType(ConjureWaterSpellPayload.TYPE, ConjureWaterSpellPayload::new, ConjureWaterSpellPayload::getResearch);
        SpellManager.registerPayloadType(FlightSpellPayload.TYPE, FlightSpellPayload::new, FlightSpellPayload::getResearch);
        SpellManager.registerPayloadType(ConjureLightSpellPayload.TYPE, ConjureLightSpellPayload::new, ConjureLightSpellPayload::getResearch);
        SpellManager.registerPayloadType(HealingSpellPayload.TYPE, HealingSpellPayload::new, HealingSpellPayload::getResearch);
        SpellManager.registerPayloadType(PolymorphSpellPayload.TYPE, PolymorphSpellPayload::new, PolymorphSpellPayload::getResearch);
        SpellManager.registerPayloadType(ConjureAnimalSpellPayload.TYPE, ConjureAnimalSpellPayload::new, ConjureAnimalSpellPayload::getResearch);
        SpellManager.registerPayloadType(ConjureLavaSpellPayload.TYPE, ConjureLavaSpellPayload::new, ConjureLavaSpellPayload::getResearch);
        SpellManager.registerPayloadType(DrainSoulSpellPayload.TYPE, DrainSoulSpellPayload::new, DrainSoulSpellPayload::getResearch);
        SpellManager.registerPayloadType(TeleportSpellPayload.TYPE, TeleportSpellPayload::new, TeleportSpellPayload::getResearch);
        SpellManager.registerPayloadType(ConsecrateSpellPayload.TYPE, ConsecrateSpellPayload::new, ConsecrateSpellPayload::getResearch);
    }

    private static void registerSpellModTypes() {
        SpellManager.registerModType(EmptySpellMod.TYPE, EmptySpellMod::new, EmptySpellMod::getResearch);
        SpellManager.registerModType(AmplifySpellMod.TYPE, AmplifySpellMod::new, AmplifySpellMod::getResearch);
        SpellManager.registerModType(BurstSpellMod.TYPE, BurstSpellMod::new, BurstSpellMod::getResearch);
        SpellManager.registerModType(QuickenSpellMod.TYPE, QuickenSpellMod::new, QuickenSpellMod::getResearch);
        SpellManager.registerModType(MineSpellMod.TYPE, MineSpellMod::new, MineSpellMod::getResearch);
        SpellManager.registerModType(ForkSpellMod.TYPE, ForkSpellMod::new, ForkSpellMod::getResearch);
    }
}
