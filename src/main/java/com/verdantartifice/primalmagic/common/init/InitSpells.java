package com.verdantartifice.primalmagic.common.init;

import com.verdantartifice.primalmagic.common.spells.SpellManager;
import com.verdantartifice.primalmagic.common.spells.packages.SelfSpellPackage;
import com.verdantartifice.primalmagic.common.spells.packages.TouchSpellPackage;
import com.verdantartifice.primalmagic.common.spells.payloads.EarthDamageSpellPayload;
import com.verdantartifice.primalmagic.common.spells.payloads.FrostDamageSpellPayload;

public class InitSpells {
    public static void initSpells() {
        registerSpellPackageTypes();
        registerSpellPayloadTypes();
    }

    private static void registerSpellPayloadTypes() {
        SpellManager.registerPackageType(TouchSpellPackage.TYPE);
        SpellManager.registerPackageType(SelfSpellPackage.TYPE);
    }

    private static void registerSpellPackageTypes() {
        SpellManager.registerPayloadType(EarthDamageSpellPayload.TYPE);
        SpellManager.registerPayloadType(FrostDamageSpellPayload.TYPE);
    }
}
