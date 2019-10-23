package com.verdantartifice.primalmagic.common.items.misc;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.client.renderers.itemstack.ArcanometerTEISR;
import com.verdantartifice.primalmagic.common.sources.AffinityManager;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
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
                    ItemStack mouseOverStack = getMouseOverItemStack();
                    if (mouseOverStack.isEmpty() || AffinityManager.isScanned(mouseOverStack, (PlayerEntity)entity)) {
                        this.decrementScanState();
                    } else {
                        this.incrementScanState();
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
    
    @OnlyIn(Dist.CLIENT)
    public static ItemStack getMouseOverItemStack() {
        ItemStack stack = ItemStack.EMPTY;
        Minecraft mc = Minecraft.getInstance();
        Entity entity = mc.getRenderViewEntity();
        double reachDistance = mc.playerController.extendedReach() ? 6.0D : (double)mc.playerController.getBlockReachDistance();
        Vec3d eyePos = entity.getEyePosition(1.0F);
        double sqReachDistance = (mc.objectMouseOver != null) ? mc.objectMouseOver.getHitVec().squareDistanceTo(eyePos) : (reachDistance * reachDistance);
        Vec3d lookVector = entity.getLook(1.0F);
        Vec3d reachPos = eyePos.add(lookVector.scale(reachDistance));
        AxisAlignedBB aabb = entity.getBoundingBox().expand(lookVector.scale(reachDistance)).grow(1.0D, 1.0D, 1.0D);
        EntityRayTraceResult entityResult = ProjectileHelper.func_221273_a(entity, eyePos, reachPos, aabb, (testEntity) -> {
            return !testEntity.isSpectator();
        }, sqReachDistance);
        if (entityResult != null) {
            // TODO Support scanning non-item entities
            if (entityResult.getEntity() instanceof ItemEntity) {
                stack = ((ItemEntity)entityResult.getEntity()).getItem();
            }
        } else if (mc.objectMouseOver != null && mc.objectMouseOver.getType() == RayTraceResult.Type.BLOCK) {
            BlockRayTraceResult blockResult = (BlockRayTraceResult)mc.objectMouseOver;
            stack = new ItemStack(mc.world.getBlockState(blockResult.getPos()).getBlock());
        }
        return stack;
    }
}
