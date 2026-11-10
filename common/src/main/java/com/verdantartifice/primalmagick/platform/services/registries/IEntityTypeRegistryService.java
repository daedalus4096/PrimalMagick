package com.verdantartifice.primalmagick.platform.services.registries;

import com.verdantartifice.primalmagick.common.registries.IRegistryItem;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

import java.util.function.UnaryOperator;

public interface IEntityTypeRegistryService extends IRegistryService<EntityType<?>> {
    <E extends Entity> IRegistryItem<EntityType<?>, EntityType<E>> registerEntityType(String name, EntityType.EntityFactory<E> factory, MobCategory category, UnaryOperator<EntityType.Builder<E>> builder);
}
