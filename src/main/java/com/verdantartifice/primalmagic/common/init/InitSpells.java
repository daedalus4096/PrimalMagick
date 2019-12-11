package com.verdantartifice.primalmagic.common.init;

import com.verdantartifice.primalmagic.common.spells.SpellManager;
import com.verdantartifice.primalmagic.common.spells.mods.EmptySpellMod;
import com.verdantartifice.primalmagic.common.spells.packages.ProjectileSpellPackage;
import com.verdantartifice.primalmagic.common.spells.packages.SelfSpellPackage;
import com.verdantartifice.primalmagic.common.spells.packages.TouchSpellPackage;
import com.verdantartifice.primalmagic.common.spells.payloads.EarthDamageSpellPayload;
import com.verdantartifice.primalmagic.common.spells.payloads.FrostDamageSpellPayload;

public class InitSpells {
    public static void initSpells() {
        registerSpellPackageTypes();
        registerSpellPayloadTypes();
        registerSpellModTypes();
    }

    private static void registerSpellPayloadTypes() {
        SpellManager.registerPackageType(TouchSpellPackage.TYPE);
        SpellManager.registerPackageType(ProjectileSpellPackage.TYPE);
        SpellManager.registerPackageType(SelfSpellPackage.TYPE);
    }

    private static void registerSpellPackageTypes() {
        SpellManager.registerPayloadType(EarthDamageSpellPayload.TYPE);
        SpellManager.registerPayloadType(FrostDamageSpellPayload.TYPE);
    }

    private static void registerSpellModTypes() {
        SpellManager.registerModType(EmptySpellMod.TYPE);
    }
}
