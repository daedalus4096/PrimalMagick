package com.verdantartifice.primalmagick.common.research.keys;

import com.mojang.serialization.Codec;

public record ResearchKeyType<T extends AbstractResearchKey>(Codec<T> codec) {
}
