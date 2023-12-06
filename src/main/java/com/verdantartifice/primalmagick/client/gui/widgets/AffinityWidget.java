package com.verdantartifice.primalmagick.client.gui.widgets;

import java.util.Collections;
import java.util.List;

import com.verdantartifice.primalmagick.common.sources.Source;

import net.minecraft.network.chat.Component;

/**
 * Display widget for showing how much of a given source is in an item's affinities.
 * 
 * @author Daedalus4096
 */
public class AffinityWidget extends AbstractSourceWidget {
    public AffinityWidget(Source source, int amount, int xIn, int yIn) {
        super(source, amount, xIn, yIn);
    }

    protected List<Component> getTooltipLines() {
        Component labelText = Component.translatable("label.primalmagick.analysis.affinity", this.amount, this.getSourceText());
        return Collections.singletonList(labelText);
    }
}
