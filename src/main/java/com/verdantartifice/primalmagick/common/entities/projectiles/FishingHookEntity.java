package com.verdantartifice.primalmagick.common.entities.projectiles;

import com.verdantartifice.primalmagick.common.entities.EntityTypesPM;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.level.Level;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.network.packets.SpawnEntity;

/**
 * Definition of a fishing hook entity that works with any fishing rod derived from FishingRodItem,
 * instead of just Items.FISHING_ROD.
 * 
 * @author Daedalus4096
 */
public class FishingHookEntity extends FishingHook implements IEntityAdditionalSpawnData {
    public FishingHookEntity(SpawnEntity spawnPacket, Level level) {
        super(level.getPlayerByUUID(spawnPacket.getAdditionalData().readUUID()), level, 0, 0);
    }
    
    public FishingHookEntity(Player player, Level level, int luck, int lureSpeed) {
        super(player, level, luck, lureSpeed);
    }

    @Override
    public EntityType<?> getType() {
        return EntityTypesPM.FISHING_HOOK.get();
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
    public void writeSpawnData(FriendlyByteBuf buffer) {
        Player player = this.getPlayerOwner();
        if (player != null) {
            buffer.writeUUID(player.getUUID());
        }
    }

    @Override
    public void readSpawnData(FriendlyByteBuf additionalData) {
        // Do nothing
    }
}
