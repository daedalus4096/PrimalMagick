package com.verdantartifice.primalmagic.common.items.misc;

import javax.annotation.Nullable;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.client.renderers.itemstack.ArcanometerISTER;
import com.verdantartifice.primalmagic.common.network.PacketHandler;
import com.verdantartifice.primalmagic.common.network.packets.misc.ScanEntityPacket;
import com.verdantartifice.primalmagic.common.network.packets.misc.ScanItemPacket;
import com.verdantartifice.primalmagic.common.network.packets.misc.ScanPositionPacket;
import com.verdantartifice.primalmagic.common.research.ResearchManager;
import com.verdantartifice.primalmagic.common.sounds.SoundsPM;
import com.verdantartifice.primalmagic.common.util.EntityUtils;
import com.verdantartifice.primalmagic.common.util.RayTraceUtils;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Item definition for an arcanometer.  An arcanometer is a PKE meter-like device that scans the 
 * primal affinities of blocks and items.  It is intended to be an upgrade to the analysis table.
 *  
 * @author Daedalus4096
 */
public class ArcanometerItem extends Item {
    public static final ResourceLocation SCAN_STATE_PROPERTY = new ResourceLocation(PrimalMagic.MODID, "scan_state");

    public ArcanometerItem() {
        super(new Item.Properties().tab(PrimalMagic.ITEM_GROUP).stacksTo(1).rarity(Rarity.UNCOMMON).setISTER(() -> ArcanometerISTER::new));
    }
    
    public static ItemPropertyFunction getScanStateProperty() {
    	return new ItemPropertyFunction() {
            @OnlyIn(Dist.CLIENT)
            protected float scanState = 0;

            @OnlyIn(Dist.CLIENT)
            @Override
            public float call(ItemStack stack, ClientLevel world, LivingEntity entity) {
                if (entity == null || !(entity instanceof Player)) {
                    return 0.0F;
                } else {
                    // If the currently moused-over block/item has not yet been scanned, raise the antennae
                    if (isMouseOverScannable(RayTraceUtils.getMouseOver(), world, (Player)entity)) {
                        this.incrementScanState();
                    } else {
                        this.decrementScanState();
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
        };
    }
    
    public static boolean isMouseOverScannable(@Nullable HitResult result, @Nullable Level world, @Nullable Player player) {
        if (result == null || world == null) {
            return false;
        } else if (result.getType() == HitResult.Type.ENTITY) {
            // If the current mouseover is an entity, try to get its corresponding item and scan that if it has one, otherwise scan the entity itself
            Entity entity = ((EntityHitResult)result).getEntity();
            ItemStack stack = EntityUtils.getEntityItemStack(entity);
            if (!stack.isEmpty()) {
                return !ResearchManager.isScanned(stack, player);
            } else {
                return !ResearchManager.isScanned(entity.getType(), player);
            }
        } else if (result.getType() == HitResult.Type.BLOCK) {
            // If the current mouseover is a block, try to get its corresponding block item and scan that
            BlockPos pos = ((BlockHitResult)result).getBlockPos();
            ItemStack stack = new ItemStack(world.getBlockState(pos).getBlock());
            return !stack.isEmpty() && !ResearchManager.isScanned(stack, player);
        } else {
            return false;
        }
    }
    
    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        if (worldIn.isClientSide) {
            HitResult result = RayTraceUtils.getMouseOver();
            if (result != null && result.getType() != HitResult.Type.MISS) {
                // If something is being moused over, play the sound effect for the player and send a scan packet to the server
                worldIn.playSound(playerIn, playerIn.blockPosition(), SoundsPM.SCAN.get(), SoundSource.MASTER, 1.0F, 1.0F);
                if (result.getType() == HitResult.Type.ENTITY) {
                    Entity entity = ((EntityHitResult)result).getEntity();
                    ItemStack entityStack = EntityUtils.getEntityItemStack(entity);
                    if (!entityStack.isEmpty()) {
                        PacketHandler.sendToServer(new ScanItemPacket(entityStack));
                    } else {
                        PacketHandler.sendToServer(new ScanEntityPacket(entity.getType()));
                    }
                } else if (result.getType() == HitResult.Type.BLOCK) {
                    BlockPos pos = ((BlockHitResult)result).getBlockPos();
                    PacketHandler.sendToServer(new ScanPositionPacket(pos));
                }
            }
        }
        return super.use(worldIn, playerIn, handIn);
    }
}
