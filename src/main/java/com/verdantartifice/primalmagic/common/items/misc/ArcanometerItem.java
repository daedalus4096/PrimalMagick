package com.verdantartifice.primalmagic.common.items.misc;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.client.renderers.itemstack.ArcanometerTEISR;
import com.verdantartifice.primalmagic.common.sources.AffinityManager;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ArcanometerItem extends Item {
    public ArcanometerItem() {
        super(new Item.Properties().group(PrimalMagic.ITEM_GROUP).maxStackSize(1).rarity(Rarity.UNCOMMON).setTEISR(() -> ArcanometerTEISR::new));
        this.setRegistryName(PrimalMagic.MODID, "arcanometer");
        this.addPropertyOverride(new ResourceLocation(PrimalMagic.MODID, "scan_state"), new IItemPropertyGetter() {
            @OnlyIn(Dist.CLIENT)
            protected float scanState = 0;

            @OnlyIn(Dist.CLIENT)
            @Override
            public float call(ItemStack stack, World world, LivingEntity entity) {
                if (entity == null || !(entity instanceof PlayerEntity)) {
                    return 0.0F;
                } else {
                    if (world == null) {
                        world = entity.world;
                    }
                    PlayerEntity player = (PlayerEntity)entity;
                    RayTraceResult result = rayTrace(world, player, RayTraceContext.FluidMode.ANY);
                    if (result.getType() == RayTraceResult.Type.MISS) {
                        this.decrementScanState();
                    } else if (result.getType() == RayTraceResult.Type.ENTITY) {
                        EntityRayTraceResult entityResult = (EntityRayTraceResult)result;
                        if (entityResult.getEntity() instanceof ItemEntity) {
                            ItemStack entityStack = ((ItemEntity)entityResult.getEntity()).getItem();
                            if (AffinityManager.isScanned(entityStack, player)) {
                                this.decrementScanState();
                            } else {
                                this.incrementScanState();
                            }
                        } else {
                            // TODO Support scanning non-item entities
                            this.decrementScanState();
                        }
                    } else {
                        BlockRayTraceResult blockResult = (BlockRayTraceResult)result;
                        ItemStack blockStack = new ItemStack(world.getBlockState(blockResult.getPos()).getBlock(), 1);
                        if (AffinityManager.isScanned(blockStack, player)) {
                            this.decrementScanState();
                        } else {
                            this.incrementScanState();
                        }
                    }
                    return scanState;
                }
            }
            
            @OnlyIn(Dist.CLIENT)
            protected void incrementScanState() {
                this.scanState = Math.min(4.0F, this.scanState + 0.25F);
            }
            
            @OnlyIn(Dist.CLIENT)
            protected void decrementScanState() {
                this.scanState = Math.max(0.0F, this.scanState - 0.25F);
            }
        });
    }
}
