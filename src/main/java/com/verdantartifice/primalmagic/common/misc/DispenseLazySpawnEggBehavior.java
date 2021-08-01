package com.verdantartifice.primalmagic.common.misc;

import com.verdantartifice.primalmagic.common.items.misc.LazySpawnEggItem;

import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.DispenserBlock;

/**
 * Dispenser behavior for lazy spawn egg items.
 * 
 * @author Daedalus4096
 */
public class DispenseLazySpawnEggBehavior extends DefaultDispenseItemBehavior {
    @Override
    protected ItemStack execute(BlockSource source, ItemStack stack) {
        if (stack.getItem() instanceof LazySpawnEggItem) {
            Direction direction = source.getBlockState().getValue(DispenserBlock.FACING);
            EntityType<?> entityType = ((LazySpawnEggItem)stack.getItem()).getType(stack.getTag());
            entityType.spawn(source.getLevel(), stack, null, source.getPos().relative(direction), MobSpawnType.DISPENSER, direction != Direction.UP, false);
            stack.shrink(1);
            return stack;
        } else {
            return stack;
        }
    }
}
