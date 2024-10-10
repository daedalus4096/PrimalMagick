package com.verdantartifice.primalmagick.platform.registries;

import com.verdantartifice.primalmagick.common.menus.MenuTypeRegistration;
import com.verdantartifice.primalmagick.common.menus.base.IMenuFactory;
import com.verdantartifice.primalmagick.common.registries.IRegistryItem;
import com.verdantartifice.primalmagick.common.registries.RegistryItemForge;
import com.verdantartifice.primalmagick.platform.services.IMenuTypeService;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;

import java.util.function.Supplier;

/**
 * Forge implementation of the menu type registry service.
 *
 * @author Daedalus4096
 */
public class MenuTypeServiceForge extends AbstractBuiltInRegistryServiceForge<MenuType<?>> implements IMenuTypeService {
    @Override
    protected Supplier<DeferredRegister<MenuType<?>>> getDeferredRegisterSupplier() {
        return MenuTypeRegistration::getDeferredRegister;
    }

    @Override
    protected Registry<MenuType<?>> getRegistry() {
        return BuiltInRegistries.MENU;
    }

    @Override
    public <T extends AbstractContainerMenu> IRegistryItem<MenuType<?>, MenuType<T>> register(String name, IMenuFactory<T> menuFactory) {
        return new RegistryItemForge<>(this.getDeferredRegisterSupplier().get().register(name, () -> IForgeMenuType.create(menuFactory::create)));
    }
}
