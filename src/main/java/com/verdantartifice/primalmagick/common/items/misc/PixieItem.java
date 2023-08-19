package com.verdantartifice.primalmagick.common.items.misc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

import com.verdantartifice.primalmagick.common.entities.companions.CompanionManager;
import com.verdantartifice.primalmagick.common.entities.companions.pixies.AbstractPixieEntity;
import com.verdantartifice.primalmagick.common.sources.Source;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.common.ForgeSpawnEggItem;

/**
 * Definition of a spawn item for a pixie companion.
 * 
 * @author Daedalus4096
 */
public class PixieItem extends ForgeSpawnEggItem {
    protected static final List<PixieItem> PIXIES = new ArrayList<>();
    
    public PixieItem(Supplier<EntityType<? extends AbstractPixieEntity>> typeSupplier, Source source, Item.Properties properties) {
        super(typeSupplier, 0xFFFFFF, source.getColor(), properties);
        PIXIES.add(this);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        if (level instanceof ServerLevel serverLevel) {
            ItemStack stack = context.getItemInHand();
            BlockPos pos = context.getClickedPos();
            Direction dir = context.getClickedFace();
            BlockState state = level.getBlockState(pos);
            Player player = context.getPlayer();
            EntityType<?> entityType = this.getType(stack.getTag());
            
            BlockPos spawnPos = state.getCollisionShape(level, pos).isEmpty() ? pos : pos.relative(dir);
            Entity spawned = entityType.spawn(serverLevel, stack, player, spawnPos, MobSpawnType.SPAWN_EGG, true, !Objects.equals(pos, spawnPos) && dir == Direction.UP);
            if (spawned != null) {
                stack.shrink(1);
                level.gameEvent(player, GameEvent.ENTITY_PLACE, pos);
                
                // This block is why we're re-implementing this method, so that we can link the newly created pixie entity with its owner
                if (spawned instanceof AbstractPixieEntity pixie) {
                    CompanionManager.addCompanion(player, pixie);
                }
            }
            
            return InteractionResult.CONSUME;
        } else {
            return InteractionResult.SUCCESS;
        }
    }
    
    public static Collection<PixieItem> getAllPixies() {
        return Collections.unmodifiableCollection(PIXIES);
    }
}
