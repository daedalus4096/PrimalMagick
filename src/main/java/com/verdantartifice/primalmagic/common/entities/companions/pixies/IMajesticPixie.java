package com.verdantartifice.primalmagic.common.entities.companions.pixies;

/**
 * Interface identifying a majestic pixie, of the highest stature.
 * 
 * @author Daedalus4096
 */
public interface IMajesticPixie extends IPixie {
    public default int getSpellPower() {
        return 5;
    }
}
