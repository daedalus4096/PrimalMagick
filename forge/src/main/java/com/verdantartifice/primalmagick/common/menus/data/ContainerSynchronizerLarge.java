package com.verdantartifice.primalmagick.common.menus.data;

import com.verdantartifice.primalmagick.common.network.PacketHandler;
import com.verdantartifice.primalmagick.common.network.packets.data.ContainerSetVarintDataPacket;

import net.minecraft.core.NonNullList;
import net.minecraft.network.protocol.game.ClientboundContainerSetContentPacket;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerSynchronizer;
import net.minecraft.world.item.ItemStack;

/**
 * Implementation of the Minecraft {@link ContainerSynchronizer} interface that syncs data slots
 * as varints rather than shorts to allow for larger data values.
 * 
 * @author Daedalus4096
 */
public class ContainerSynchronizerLarge implements ContainerSynchronizer {
    private final ServerPlayer player;
    
    public ContainerSynchronizerLarge(ServerPlayer player) {
        this.player = player;
    }

    @Override
    public void sendInitialData(AbstractContainerMenu pContainer, NonNullList<ItemStack> pItems, ItemStack pCarriedItem, int[] pInitialData) {
        this.player.connection.send(new ClientboundContainerSetContentPacket(pContainer.containerId, pContainer.incrementStateId(), pItems, pCarriedItem));
        for (int index = 0; index < pInitialData.length; index++) {
            this.broadcastDataValue(pContainer, index, pInitialData[index]);
        }
    }

    @Override
    public void sendSlotChange(AbstractContainerMenu pContainer, int pSlot, ItemStack pItemStack) {
        this.player.connection.send(new ClientboundContainerSetSlotPacket(pContainer.containerId, pContainer.incrementStateId(), pSlot, pItemStack));
    }

    @Override
    public void sendCarriedChange(AbstractContainerMenu pContainerMenu, ItemStack pStack) {
        this.player.connection.send(new ClientboundContainerSetSlotPacket(-1, pContainerMenu.incrementStateId(), -1, pStack));
    }

    @Override
    public void sendDataChange(AbstractContainerMenu pContainer, int pId, int pValue) {
        this.broadcastDataValue(pContainer, pId, pValue);
    }
    
    protected void broadcastDataValue(AbstractContainerMenu pContainer, int pId, int pValue) {
        // Send custom packet
        PacketHandler.sendToPlayer(new ContainerSetVarintDataPacket(pContainer.containerId, pId, pValue), this.player);
    }
}
