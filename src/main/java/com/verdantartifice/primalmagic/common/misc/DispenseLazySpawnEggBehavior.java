package com.verdantartifice.primalmagic.common.misc;

import com.verdantartifice.primalmagic.common.items.misc.LazySpawnEggItem;

import net.minecraft.block.DispenserBlock;
import net.minecraft.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;

/**
 * Dispenser behavior for lazy spawn egg items.
 * 
 * @author Daedalus4096
 */
public class DispenseLazySpawnEggBehavior extends DefaultDispenseItemBehavior {
    @Override
    protected ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
        if (stack.getItem() instanceof LazySpawnEggItem) {
            Direction direction = source.getBlockState().get(DispenserBlock.FACING);
            EntityType<?> entityType = ((LazySpawnEggItem)stack.getItem()).getType(stack.getTag());
            entityType.spawn(source.getWorld(), stack, null, source.getBlockPos().offset(direction), SpawnReason.DISPENSER, direction != Direction.UP, false);
            stack.shrink(1);
            return stack;
        } else {
            return stack;
        }
    }
}
