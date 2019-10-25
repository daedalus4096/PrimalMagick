package com.verdantartifice.primalmagic.client.util;

import javax.annotation.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RayTraceUtils {
    @Nullable
    public static RayTraceResult getMouseOver() {
        Minecraft mc = Minecraft.getInstance();
        Entity viewEntity = mc.getRenderViewEntity();
        double reachDistance = mc.playerController.extendedReach() ? 6.0D : (double)mc.playerController.getBlockReachDistance();
        Vec3d eyePos = viewEntity.getEyePosition(1.0F);
        double sqReachDistance = (mc.objectMouseOver != null) ? mc.objectMouseOver.getHitVec().squareDistanceTo(eyePos) : (reachDistance * reachDistance);
        Vec3d lookVector = viewEntity.getLook(1.0F);
        Vec3d reachPos = eyePos.add(lookVector.scale(reachDistance));
        AxisAlignedBB aabb = viewEntity.getBoundingBox().expand(lookVector.scale(reachDistance)).grow(1.0D, 1.0D, 1.0D);
        EntityRayTraceResult entityResult = ProjectileHelper.func_221269_a(mc.world, viewEntity, eyePos, reachPos, aabb, (testEntity) -> {
            return !testEntity.isSpectator();
        }, sqReachDistance);
        if (entityResult != null) {
            return entityResult;
        } else {
            return mc.objectMouseOver;
        }
    }
}
