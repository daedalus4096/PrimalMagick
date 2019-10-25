package com.verdantartifice.primalmagic.common.items.misc;

import javax.annotation.Nonnull;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.client.renderers.itemstack.ArcanometerTEISR;
import com.verdantartifice.primalmagic.common.network.PacketHandler;
import com.verdantartifice.primalmagic.common.network.packets.misc.ScanPacket;
import com.verdantartifice.primalmagic.common.sounds.SoundsPM;
import com.verdantartifice.primalmagic.common.sources.AffinityManager;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.Rarity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
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
    @Nonnull
    public static ItemStack getMouseOverItemStack() {
        ItemStack stack = ItemStack.EMPTY;
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
            Entity entity = entityResult.getEntity();
            if (entity instanceof ItemEntity) {
                stack = ((ItemEntity)entity).getItem();
            } else if (entity instanceof BoatEntity) {
                stack = new ItemStack(((BoatEntity)entity).getItemBoat());
            } else if (entity.getType().equals(EntityType.ITEM_FRAME)) {
                stack = new ItemStack(Items.ITEM_FRAME);
            } else if (entity.getType().equals(EntityType.ARMOR_STAND)) {
                stack = new ItemStack(Items.ARMOR_STAND);
            } else if (entity.getType().equals(EntityType.MINECART)) {
                stack = new ItemStack(Items.MINECART);
            } else if (entity.getType().equals(EntityType.CHEST_MINECART)) {
                stack = new ItemStack(Items.CHEST_MINECART);
            } else if (entity.getType().equals(EntityType.FURNACE_MINECART)) {
                stack = new ItemStack(Items.FURNACE_MINECART);
            } else if (entity.getType().equals(EntityType.HOPPER_MINECART)) {
                stack = new ItemStack(Items.HOPPER_MINECART);
            } else if (entity.getType().equals(EntityType.TNT_MINECART)) {
                stack = new ItemStack(Items.TNT_MINECART);
            } else if (entity.getType().equals(EntityType.COMMAND_BLOCK_MINECART)) {
                stack = new ItemStack(Items.COMMAND_BLOCK_MINECART);
            } else if (entity.getType().equals(EntityType.END_CRYSTAL)) {
                stack = new ItemStack(Items.END_CRYSTAL);
            } else if (entity.getType().equals(EntityType.PAINTING)) {
                stack = new ItemStack(Items.PAINTING);
            }
        } else if (mc.objectMouseOver != null && mc.objectMouseOver.getType() == RayTraceResult.Type.BLOCK) {
            BlockRayTraceResult blockResult = (BlockRayTraceResult)mc.objectMouseOver;
            stack = new ItemStack(mc.world.getBlockState(blockResult.getPos()).getBlock());
        }
        return stack;
    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        if (worldIn.isRemote) {
            ItemStack targetStack = getMouseOverItemStack();
            if (!targetStack.isEmpty()) {
                worldIn.playSound(playerIn, playerIn.posX, playerIn.posY, playerIn.posZ, SoundsPM.SCAN, SoundCategory.MASTER, 1.0F, 1.0F);
                if (AffinityManager.isScanned(targetStack, playerIn)) {
                    playerIn.sendStatusMessage(new TranslationTextComponent("event.primalmagic.scan.repeat").applyTextStyle(TextFormatting.RED), true);
                } else {
                    PacketHandler.sendToServer(new ScanPacket(targetStack));
                }
            }
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }
}
