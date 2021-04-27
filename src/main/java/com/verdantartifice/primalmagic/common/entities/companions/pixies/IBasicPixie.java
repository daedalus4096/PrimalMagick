package com.verdantartifice.primalmagic.common.entities.companions.pixies;

/**
 * Interface identifying a basic pixie, of the weakest stature.
 * 
 * @author Daedalus4096
 */
public interface IBasicPixie extends IPixie {
    public default int getSpellPower() {
        return 1;
    }
}
