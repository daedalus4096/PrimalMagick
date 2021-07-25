package com.verdantartifice.primalmagic.common.items.misc;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import com.google.common.collect.Iterables;
import com.verdantartifice.primalmagic.common.entities.companions.AbstractCompanionEntity;
import com.verdantartifice.primalmagic.common.entities.companions.CompanionManager;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.stats.Stats;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.BaseSpawner;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Constants;

/**
 * Spawn egg that is defined with a lazily-specified entity type.  Necessary for mod use because items
 * are registered before entity types.
 * 
 * @author Daedalus4096
 * @see {@link net.minecraft.item.SpawnEggItem}
 */
public class LazySpawnEggItem extends Item {
    protected static final Set<LazySpawnEggItem> EGGS = new HashSet<>();
    
    protected final int primaryColor;
    protected final int secondaryColor;
    protected final Supplier<EntityType<?>> typeSupplier;

    public LazySpawnEggItem(Supplier<EntityType<?>> typeSupplier, int primaryColor, int secondaryColor, Item.Properties properties) {
        super(properties);
        this.primaryColor = primaryColor;
        this.secondaryColor = secondaryColor;
        this.typeSupplier = typeSupplier;
        EGGS.add(this);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level world = context.getLevel();
        if (!(world instanceof ServerLevel)) {
            return InteractionResult.SUCCESS;
        } else {
            ItemStack itemStack = context.getItemInHand();
            BlockPos blockPos = context.getClickedPos();
            Direction direction = context.getClickedFace();
            BlockState blockState = world.getBlockState(blockPos);
            if (blockState.is(Blocks.SPAWNER)) {
                BlockEntity tile = world.getBlockEntity(blockPos);
                if (tile instanceof SpawnerBlockEntity) {
                    BaseSpawner abstractspawner = ((SpawnerBlockEntity)tile).getSpawner();
                    EntityType<?> entitytype1 = this.getType(itemStack.getTag());
                    abstractspawner.setEntityId(entitytype1);
                    tile.setChanged();
                    world.sendBlockUpdated(blockPos, blockState, blockState, Constants.BlockFlags.DEFAULT);
                    itemStack.shrink(1);
                    return InteractionResult.CONSUME;
                }
            }
            
            BlockPos spawnPos = blockState.getCollisionShape(world, blockPos).isEmpty() ? blockPos : blockPos.relative(direction);
            EntityType<?> entitytype = this.getType(itemStack.getTag());
            Entity entity = entitytype.spawn((ServerLevel)world, itemStack, context.getPlayer(), spawnPos, MobSpawnType.SPAWN_EGG, true, !Objects.equals(blockPos, spawnPos) && direction == Direction.UP);
            if (entity != null) {
                itemStack.shrink(1);
            }
            if (entity instanceof AbstractCompanionEntity) {
                CompanionManager.addCompanion(context.getPlayer(), (AbstractCompanionEntity)entity);
            }
            
            return InteractionResult.CONSUME;
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        ItemStack itemstack = playerIn.getItemInHand(handIn);
        HitResult raytraceresult = getPlayerPOVHitResult(worldIn, playerIn, ClipContext.Fluid.SOURCE_ONLY);
        if (raytraceresult.getType() != HitResult.Type.BLOCK) {
            return InteractionResultHolder.pass(itemstack);
        } else if (!(worldIn instanceof ServerLevel)) {
            return InteractionResultHolder.success(itemstack);
        } else {
            BlockHitResult blockraytraceresult = (BlockHitResult)raytraceresult;
            BlockPos blockpos = blockraytraceresult.getBlockPos();
            if (!(worldIn.getBlockState(blockpos).getBlock() instanceof LiquidBlock)) {
                return InteractionResultHolder.pass(itemstack);
            } else if (worldIn.mayInteract(playerIn, blockpos) && playerIn.mayUseItemAt(blockpos, blockraytraceresult.getDirection(), itemstack)) {
                EntityType<?> entitytype = this.getType(itemstack.getTag());
                if (entitytype.spawn((ServerLevel)worldIn, itemstack, playerIn, blockpos, MobSpawnType.SPAWN_EGG, false, false) == null) {
                    return InteractionResultHolder.pass(itemstack);
                } else {
                    if (!playerIn.abilities.instabuild) {
                        itemstack.shrink(1);
                    }
                    playerIn.awardStat(Stats.ITEM_USED.get(this));
                    return InteractionResultHolder.consume(itemstack);
                }
            } else {
                return InteractionResultHolder.fail(itemstack);
            }
        }
    }
    
    public EntityType<?> getType(@Nullable CompoundTag nbt) {
        if (nbt != null && nbt.contains("EntityTag", Constants.NBT.TAG_COMPOUND)) {
            CompoundTag tag = nbt.getCompound("EntityTag");
            if (tag.contains("id", Constants.NBT.TAG_STRING)) {
                return EntityType.byString(tag.getString("id")).orElseGet(this.typeSupplier);
            }
        }
        return this.typeSupplier.get();
    }
    
    public static Iterable<LazySpawnEggItem> getEggs() {
        return Iterables.unmodifiableIterable(EGGS);
    }
    
    @OnlyIn(Dist.CLIENT)
    public int getColor(int tintIndex) {
        return tintIndex == 0 ? this.primaryColor : this.secondaryColor;
    }
}
