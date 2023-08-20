package com.verdantartifice.primalmagick.common.items.misc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.verdantartifice.primalmagick.common.attunements.AttunementType;
import com.verdantartifice.primalmagick.common.sources.Source;

/**
 * Definition of a specialized attunement gain item that grants a single point of permanent attunement.
 * 
 * @author Daedalus4096
 */
public class HummingArtifactItem extends AbstractAttunementGainItem {
    protected static final List<HummingArtifactItem> ARTIFACTS = new ArrayList<>();

    public HummingArtifactItem(Source source, Properties properties) {
        super(source, AttunementType.PERMANENT, 1, properties);
        ARTIFACTS.add(this);
    }

    public static Collection<HummingArtifactItem> getAllHummingArtifacts() {
        return Collections.unmodifiableCollection(ARTIFACTS);
    }
}
