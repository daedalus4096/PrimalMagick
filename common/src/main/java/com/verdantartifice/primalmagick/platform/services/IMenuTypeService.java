package com.verdantartifice.primalmagick.platform.services;

import com.verdantartifice.primalmagick.common.menus.base.IMenuFactory;
import com.verdantartifice.primalmagick.common.registries.IRegistryItem;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

public interface IMenuTypeService extends IRegistryService<MenuType<?>> {
    <T extends AbstractContainerMenu> IRegistryItem<MenuType<?>, MenuType<T>> register(String name, IMenuFactory<T> menuFactory);
}
