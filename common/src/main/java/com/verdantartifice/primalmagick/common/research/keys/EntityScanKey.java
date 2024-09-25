package com.verdantartifice.primalmagick.common.research.keys;

import java.util.Objects;

import com.google.common.base.Preconditions;
import com.mojang.serialization.MapCodec;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.research.IconDefinition;
import com.verdantartifice.primalmagick.common.research.requirements.RequirementCategory;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.registries.ForgeRegistries;

public class EntityScanKey extends AbstractResearchKey<EntityScanKey> {
    public static final MapCodec<EntityScanKey> CODEC = ResourceLocation.CODEC.fieldOf("entityType").xmap(loc -> {
        return new EntityScanKey(ForgeRegistries.ENTITY_TYPES.getValue(loc));
    }, key -> {
        return EntityType.getKey(key.entityType);
    });
    public static final StreamCodec<ByteBuf, EntityScanKey> STREAM_CODEC = ResourceLocation.STREAM_CODEC.map(loc -> {
        return new EntityScanKey(ForgeRegistries.ENTITY_TYPES.getValue(loc));
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
        return PREFIX + ForgeRegistries.ENTITY_TYPES.getKey(this.entityType).toString();
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
