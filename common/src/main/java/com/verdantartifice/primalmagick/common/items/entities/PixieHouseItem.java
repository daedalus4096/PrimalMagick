package com.verdantartifice.primalmagick.common.items.entities;

import com.verdantartifice.primalmagick.common.entities.EntityTypesPM;
import com.verdantartifice.primalmagick.common.entities.misc.PixieHouseEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.function.Consumer;

public class PixieHouseItem extends Item {
    public PixieHouseItem(Item.Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        Direction direction = pContext.getClickedFace();
        if (direction == Direction.DOWN) {
            return InteractionResult.FAIL;
        } else {
            Level level = pContext.getLevel();
            BlockPlaceContext blockPlaceContext = new BlockPlaceContext(pContext);
            BlockPos blockPos = blockPlaceContext.getClickedPos();
            ItemStack itemStack = pContext.getItemInHand();
            Vec3 vec3 = Vec3.atBottomCenterOf(blockPos);
            AABB aabb = EntityTypesPM.PIXIE_HOUSE.get().getDimensions().makeBoundingBox(vec3.x(), vec3.y(), vec3.z());
            if (level.noCollision(null, aabb) && level.getEntities(null, aabb).isEmpty()) {
                if (level instanceof ServerLevel serverLevel) {
                    Consumer<PixieHouseEntity> consumer = EntityType.createDefaultStackConfig(serverLevel, itemStack, pContext.getPlayer());
                    PixieHouseEntity pixieHouse = EntityTypesPM.PIXIE_HOUSE.get().create(serverLevel, consumer, blockPos, MobSpawnType.SPAWN_EGG, true, true);
                    if (pixieHouse == null) {
                        return InteractionResult.FAIL;
                    }

                    float facing = (float) Mth.floor((Mth.wrapDegrees(pContext.getRotation() - 180.0F) + 45.0F) / 90.0F) * 90.0F;
                    pixieHouse.moveTo(pixieHouse.getX(), pixieHouse.getY(), pixieHouse.getZ(), facing, 0.0F);
                    serverLevel.addFreshEntityWithPassengers(pixieHouse);
                    level.playSound(null, pixieHouse.getX(), pixieHouse.getY(), pixieHouse.getZ(), SoundEvents.ARMOR_STAND_PLACE, SoundSource.BLOCKS, 0.75F, 0.8F);
                    pixieHouse.gameEvent(GameEvent.ENTITY_PLACE, pContext.getPlayer());
                }

                itemStack.shrink(1);
                return InteractionResult.sidedSuccess(level.isClientSide);
            } else {
                return InteractionResult.FAIL;
            }
        }
    }
}
