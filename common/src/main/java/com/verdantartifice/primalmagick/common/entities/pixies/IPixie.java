package com.verdantartifice.primalmagick.common.entities.pixies;

import com.verdantartifice.primalmagick.common.sources.Source;

/**
 * Base interface exposing a pixie's stature and its related attributes.
 * 
 * @author Daedalus4096
 */
public interface IPixie {
    PixieRank getPixieRank();
    Source getPixieSource();
    int getSpellPower();
}
