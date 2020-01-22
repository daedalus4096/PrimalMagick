package com.verdantartifice.primalmagic.common.spells;

/**
 * Enum identifying a type of spell component.  For more information on spell components, see their
 * respective interfaces.
 * 
 * @author Daedalus4096
 * @see {@link com.verdantartifice.primalmagic.common.spells.vehicles.ISpellVehicle}
 * @see {@link com.verdantartifice.primalmagic.common.spells.payloads.ISpellPayload}
 * @see {@link com.verdantartifice.primalmagic.common.spells.mods.ISpellMod}
 */
public enum SpellComponent {
    VEHICLE,
    PAYLOAD,
    PRIMARY_MOD,
    SECONDARY_MOD
}
