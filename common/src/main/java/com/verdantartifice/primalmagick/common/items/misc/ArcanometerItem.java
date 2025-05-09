package com.verdantartifice.primalmagick.common.items.misc;

import com.verdantartifice.primalmagick.client.renderers.itemstack.ArcanometerISTER;
import com.verdantartifice.primalmagick.common.items.IHasCustomRenderer;
import com.verdantartifice.primalmagick.common.network.PacketHandler;
import com.verdantartifice.primalmagick.common.network.packets.misc.ScanEntityPacket;
import com.verdantartifice.primalmagick.common.network.packets.misc.ScanItemPacket;
import com.verdantartifice.primalmagick.common.network.packets.misc.ScanPositionPacket;
import com.verdantartifice.primalmagick.common.research.ResearchManager;
import com.verdantartifice.primalmagick.common.sounds.SoundsPM;
import com.verdantartifice.primalmagick.common.util.EntityUtils;
import com.verdantartifice.primalmagick.common.util.RayTraceUtils;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

import javax.annotation.Nullable;
import java.util.function.Supplier;

/**
 * Item definition for an arcanometer.  An arcanometer is a PKE meter-like device that scans the 
 * primal affinities of blocks and items.  It is intended to be an upgrade to the analysis table.
 *  
 * @author Daedalus4096
 */
public abstract class ArcanometerItem extends Item implements IHasCustomRenderer {
    public static final ResourceLocation SCAN_STATE_PROPERTY = ResourceUtils.loc("scan_state");

    private BlockEntityWithoutLevelRenderer customRenderer;

    public ArcanometerItem() {
        super(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON));
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
            HitResult result = RayTraceUtils.getMouseOver(worldIn, playerIn);
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
                    PacketHandler.sendToServer(new ScanPositionPacket(pos, playerIn.getItemInHand(handIn)));
                }
            }
        }
        return super.use(worldIn, playerIn, handIn);
    }

    @Override
    public Supplier<BlockEntityWithoutLevelRenderer> getCustomRendererSupplier() {
        if (this.customRenderer == null) {
            this.customRenderer = this.getCustomRendererSupplierUncached().get();
        }
        return () -> this.customRenderer;
    }

    @Override
    public Supplier<BlockEntityWithoutLevelRenderer> getCustomRendererSupplierUncached() {
        return ArcanometerISTER::new;
    }
}
