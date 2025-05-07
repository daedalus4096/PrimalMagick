package com.verdantartifice.primalmagick.common.items.misc;

import com.verdantartifice.primalmagick.common.entities.pixies.PixieRank;
import com.verdantartifice.primalmagick.common.sources.Source;

/**
 * Interface denoting an item that spawns a pixie when used.
 *
 * @author Daedalus4096
 */
public interface IPixieItem {
    PixieRank getPixieRank();
    Source getPixieSource();
}
