package com.verdantartifice.primalmagick.common.menus.base;

import com.verdantartifice.primalmagick.platform.services.registries.IMenuTypeService;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import org.jetbrains.annotations.NotNull;

/**
 * Intermediate interface which can be passed to {@link IMenuTypeService}
 * to create a {@link MenuType} in either Forge or Neoforge.
 *
 * @param <T> the type of menu to be created by this factory
 */
public interface IMenuFactory<T extends AbstractContainerMenu> extends MenuType.MenuSupplier<T> {
    T create(int id, Inventory inventory, FriendlyByteBuf buffer);

    @Override
    default @NotNull T create(int id, @NotNull Inventory inventory) {
        return create(id, inventory, null);
    }
}
