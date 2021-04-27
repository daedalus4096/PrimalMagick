package com.verdantartifice.primalmagic.common.entities.companions.pixies;

/**
 * Interface identifying a grand pixie, of middling stature.
 * 
 * @author Daedalus4096
 */
public interface IGrandPixie extends IPixie {
    public default int getSpellPower() {
        return 3;
    }
}
