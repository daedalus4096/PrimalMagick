package com.verdantartifice.primalmagick.common.affinities;

import com.verdantartifice.primalmagick.common.sources.SourceList;
import net.minecraft.world.inventory.tooltip.TooltipComponent;

/**
 * Tooltip component data for an item's affinities.
 * 
 * @author Daedalus4096
 */
public class AffinityTooltipComponent implements TooltipComponent {
    protected final SourceList affinities;
    
    public AffinityTooltipComponent(SourceList affinities) {
        this.affinities = affinities.copy();
    }
    
    public SourceList getAffinities() {
        return this.affinities;
    }
}
