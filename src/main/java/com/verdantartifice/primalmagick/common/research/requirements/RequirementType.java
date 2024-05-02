package com.verdantartifice.primalmagick.common.research.requirements;

import com.mojang.serialization.Codec;

public record RequirementType<T extends AbstractRequirement>(Codec<T> codec) {
}
