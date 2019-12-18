package com.verdantartifice.primalmagic.common.init;

import com.verdantartifice.primalmagic.common.spells.SpellManager;
import com.verdantartifice.primalmagic.common.spells.mods.EmptySpellMod;
import com.verdantartifice.primalmagic.common.spells.payloads.EarthDamageSpellPayload;
import com.verdantartifice.primalmagic.common.spells.payloads.EmptySpellPayload;
import com.verdantartifice.primalmagic.common.spells.payloads.FrostDamageSpellPayload;
import com.verdantartifice.primalmagic.common.spells.payloads.LightningDamageSpellPayload;
import com.verdantartifice.primalmagic.common.spells.payloads.SolarDamageSpellPayload;
import com.verdantartifice.primalmagic.common.spells.vehicles.EmptySpellVehicle;
import com.verdantartifice.primalmagic.common.spells.vehicles.ProjectileSpellVehicle;
import com.verdantartifice.primalmagic.common.spells.vehicles.SelfSpellVehicle;
import com.verdantartifice.primalmagic.common.spells.vehicles.TouchSpellVehicle;

public class InitSpells {
    public static void initSpells() {
        registerSpellPackageTypes();
        registerSpellPayloadTypes();
        registerSpellModTypes();
    }

    private static void registerSpellPayloadTypes() {
        SpellManager.registerVehicleType(EmptySpellVehicle.TYPE, EmptySpellVehicle::new, EmptySpellVehicle::getResearch);
        SpellManager.registerVehicleType(TouchSpellVehicle.TYPE, TouchSpellVehicle::new, TouchSpellVehicle::getResearch);
        SpellManager.registerVehicleType(ProjectileSpellVehicle.TYPE, ProjectileSpellVehicle::new, ProjectileSpellVehicle::getResearch);
        SpellManager.registerVehicleType(SelfSpellVehicle.TYPE, SelfSpellVehicle::new, SelfSpellVehicle::getResearch);
    }

    private static void registerSpellPackageTypes() {
        SpellManager.registerPayloadType(EmptySpellPayload.TYPE, EmptySpellPayload::new, EmptySpellPayload::getResearch);
        SpellManager.registerPayloadType(EarthDamageSpellPayload.TYPE, EarthDamageSpellPayload::new, EarthDamageSpellPayload::getResearch);
        SpellManager.registerPayloadType(FrostDamageSpellPayload.TYPE, FrostDamageSpellPayload::new, FrostDamageSpellPayload::getResearch);
        SpellManager.registerPayloadType(LightningDamageSpellPayload.TYPE, LightningDamageSpellPayload::new, LightningDamageSpellPayload::getResearch);
        SpellManager.registerPayloadType(SolarDamageSpellPayload.TYPE, SolarDamageSpellPayload::new, SolarDamageSpellPayload::getResearch);
    }

    private static void registerSpellModTypes() {
        SpellManager.registerModType(EmptySpellMod.TYPE, EmptySpellMod::new, EmptySpellMod::getResearch);
    }
}
