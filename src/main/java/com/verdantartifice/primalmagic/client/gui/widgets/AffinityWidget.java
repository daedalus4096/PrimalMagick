package com.verdantartifice.primalmagic.client.gui.widgets;

import com.verdantartifice.primalmagic.common.sources.Source;

/**
 * Display widget for showing how much of a given source is in an item's affinities.
 * 
 * @author Daedalus4096
 */
public class AffinityWidget extends AbstractSourceWidget {
    public AffinityWidget(Source source, int amount, int xIn, int yIn) {
        super(source, amount, xIn, yIn);
    }

    @Override
    protected String getTooltipTranslationKey() {
        return "primalmagic.analysis.affinity_tooltip";
    }
}
