package com.verdantartifice.primalmagic.common.entities.projectiles;

import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.level.Level;
import net.minecraftforge.fmllegacy.network.NetworkHooks;

/**
 * Definition of a fishing hook entity that works with any fishing rod derived from FishingRodItem,
 * instead of just Items.FISHING_ROD.
 * 
 * @author Daedalus4096
 */
public class FishingHookEntity extends FishingHook {
    public FishingHookEntity(EntityType<? extends FishingHookEntity> type, Level level) {
        super(type, level);
    }
    
    public FishingHookEntity(Player player, Level level, int luck, int lureSpeed) {
        super(player, level, luck, lureSpeed);
    }

    @Override
    public boolean shouldStopFishing(Player player) {
        boolean inMainHand = player.getMainHandItem().getItem() instanceof FishingRodItem;
        boolean inOffHand = player.getOffhandItem().getItem() instanceof FishingRodItem;
        if (!player.isRemoved() && player.isAlive() && (inMainHand || inOffHand) && this.distanceToSqr(player) <= 1024.0D) {
            return false;
        } else {
            this.discard();
            return true;
        }
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
