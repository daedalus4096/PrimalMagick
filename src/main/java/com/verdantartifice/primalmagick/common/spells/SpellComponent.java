package com.verdantartifice.primalmagick.common.spells;

/**
 * Enum identifying a type of spell component.  For more information on spell components, see their
 * respective interfaces.
 * 
 * @author Daedalus4096
 * @see {@link com.verdantartifice.primalmagick.common.spells.vehicles.ISpellVehicle}
 * @see {@link com.verdantartifice.primalmagick.common.spells.payloads.ISpellPayload}
 * @see {@link com.verdantartifice.primalmagick.common.spells.mods.ISpellMod}
 */
public enum SpellComponent {
    VEHICLE,
    PAYLOAD,
    PRIMARY_MOD,
    SECONDARY_MOD
}
