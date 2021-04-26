package com.verdantartifice.primalmagic.common.items.misc;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import com.google.common.collect.Iterables;
import com.verdantartifice.primalmagic.common.entities.companions.AbstractCompanionEntity;
import com.verdantartifice.primalmagic.common.entities.companions.CompanionManager;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.MobSpawnerTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.spawner.AbstractSpawner;
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
    public ActionResultType onItemUse(ItemUseContext context) {
        World world = context.getWorld();
        if (!(world instanceof ServerWorld)) {
            return ActionResultType.SUCCESS;
        } else {
            ItemStack itemStack = context.getItem();
            BlockPos blockPos = context.getPos();
            Direction direction = context.getFace();
            BlockState blockState = world.getBlockState(blockPos);
            if (blockState.matchesBlock(Blocks.SPAWNER)) {
                TileEntity tile = world.getTileEntity(blockPos);
                if (tile instanceof MobSpawnerTileEntity) {
                    AbstractSpawner abstractspawner = ((MobSpawnerTileEntity)tile).getSpawnerBaseLogic();
                    EntityType<?> entitytype1 = this.getType(itemStack.getTag());
                    abstractspawner.setEntityType(entitytype1);
                    tile.markDirty();
                    world.notifyBlockUpdate(blockPos, blockState, blockState, Constants.BlockFlags.DEFAULT);
                    itemStack.shrink(1);
                    return ActionResultType.CONSUME;
                }
            }
            
            BlockPos spawnPos = blockState.getCollisionShapeUncached(world, blockPos).isEmpty() ? blockPos : blockPos.offset(direction);
            EntityType<?> entitytype = this.getType(itemStack.getTag());
            Entity entity = entitytype.spawn((ServerWorld)world, itemStack, context.getPlayer(), spawnPos, SpawnReason.SPAWN_EGG, true, !Objects.equals(blockPos, spawnPos) && direction == Direction.UP);
            if (entity != null) {
                itemStack.shrink(1);
            }
            if (entity instanceof AbstractCompanionEntity) {
                CompanionManager.addCompanion(context.getPlayer(), (AbstractCompanionEntity)entity);
            }
            
            return ActionResultType.CONSUME;
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        RayTraceResult raytraceresult = rayTrace(worldIn, playerIn, RayTraceContext.FluidMode.SOURCE_ONLY);
        if (raytraceresult.getType() != RayTraceResult.Type.BLOCK) {
            return ActionResult.resultPass(itemstack);
        } else if (!(worldIn instanceof ServerWorld)) {
            return ActionResult.resultSuccess(itemstack);
        } else {
            BlockRayTraceResult blockraytraceresult = (BlockRayTraceResult)raytraceresult;
            BlockPos blockpos = blockraytraceresult.getPos();
            if (!(worldIn.getBlockState(blockpos).getBlock() instanceof FlowingFluidBlock)) {
                return ActionResult.resultPass(itemstack);
            } else if (worldIn.isBlockModifiable(playerIn, blockpos) && playerIn.canPlayerEdit(blockpos, blockraytraceresult.getFace(), itemstack)) {
                EntityType<?> entitytype = this.getType(itemstack.getTag());
                if (entitytype.spawn((ServerWorld)worldIn, itemstack, playerIn, blockpos, SpawnReason.SPAWN_EGG, false, false) == null) {
                    return ActionResult.resultPass(itemstack);
                } else {
                    if (!playerIn.abilities.isCreativeMode) {
                        itemstack.shrink(1);
                    }
                    playerIn.addStat(Stats.ITEM_USED.get(this));
                    return ActionResult.resultConsume(itemstack);
                }
            } else {
                return ActionResult.resultFail(itemstack);
            }
        }
    }
    
    public EntityType<?> getType(@Nullable CompoundNBT nbt) {
        if (nbt != null && nbt.contains("EntityTag", Constants.NBT.TAG_COMPOUND)) {
            CompoundNBT tag = nbt.getCompound("EntityTag");
            if (tag.contains("id", Constants.NBT.TAG_STRING)) {
                return EntityType.byKey(tag.getString("id")).orElseGet(this.typeSupplier);
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
