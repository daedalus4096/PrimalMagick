package com.verdantartifice.primalmagick.common.research.keys;

import java.util.Objects;

import com.google.common.base.Preconditions;
import com.mojang.serialization.Codec;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.registries.ForgeRegistries;

public class EntityScanKey extends AbstractResearchKey {
    public static final Codec<EntityScanKey> CODEC = ResourceLocation.CODEC.fieldOf("entityType").xmap(loc -> {
        return new EntityScanKey(ForgeRegistries.ENTITY_TYPES.getValue(loc));
    }, key -> {
        return EntityType.getKey(key.entityType);
    }).codec();
    
    private static final String PREFIX = "*";
    
    protected final EntityType<?> entityType;
    
    public EntityScanKey(EntityType<?> entityType) {
        this.entityType = Preconditions.checkNotNull(entityType);
    }

    @Override
    public String toString() {
        return PREFIX + ForgeRegistries.ENTITY_TYPES.getKey(this.entityType).toString();
    }

    @Override
    protected ResearchKeyType<?> getType() {
        return ResearchKeyTypesPM.ENTITY_SCAN.get();
    }

    @Override
    public int hashCode() {
        return Objects.hash(entityType);
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
