package com.verdantartifice.primalmagick.common.research.keys;

import com.google.common.base.Preconditions;
import com.mojang.serialization.MapCodec;
import com.verdantartifice.primalmagick.common.research.IconDefinition;
import com.verdantartifice.primalmagick.common.research.requirements.RequirementCategory;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import com.verdantartifice.primalmagick.platform.Services;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;

import java.util.Objects;

public class EntityScanKey extends AbstractResearchKey<EntityScanKey> {
    public static final MapCodec<EntityScanKey> CODEC = ResourceLocation.CODEC.fieldOf("entityType").xmap(loc -> {
        return new EntityScanKey(Services.ENTITY_TYPES_REGISTRY.get(loc));
    }, key -> {
        return EntityType.getKey(key.entityType);
    });
    public static final StreamCodec<ByteBuf, EntityScanKey> STREAM_CODEC = ResourceLocation.STREAM_CODEC.map(loc -> {
        return new EntityScanKey(Services.ENTITY_TYPES_REGISTRY.get(loc));
    }, key -> {
        return EntityType.getKey(key.entityType);
    });
    
    private static final String PREFIX = "*";
    private static final ResourceLocation ICON_MAP = ResourceUtils.loc("textures/research/research_map.png");

    protected final EntityType<?> entityType;
    
    public EntityScanKey(EntityType<?> entityType) {
        this.entityType = Preconditions.checkNotNull(entityType);
    }

    @Override
    public String toString() {
        return PREFIX + Services.ENTITY_TYPES_REGISTRY.getKey(this.entityType).toString();
    }

    @Override
    public RequirementCategory getRequirementCategory() {
        return RequirementCategory.RESEARCH;
    }

    @Override
    protected ResearchKeyType<EntityScanKey> getType() {
        return ResearchKeyTypesPM.ENTITY_SCAN.get();
    }

    @Override
    public IconDefinition getIcon(RegistryAccess registryAccess) {
        return IconDefinition.of(ICON_MAP);
    }

    @Override
    public int hashCode() {
        return Objects.hash(EntityType.getKey(this.entityType));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        EntityScanKey other = (EntityScanKey) obj;
        return EntityType.getKey(other.entityType).equals(EntityType.getKey(this.entityType));
    }
}
