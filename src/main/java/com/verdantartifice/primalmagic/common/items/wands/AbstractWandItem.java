package com.verdantartifice.primalmagic.common.items.wands;

import java.util.List;

import javax.annotation.Nonnull;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.crafting.IWandTransform;
import com.verdantartifice.primalmagic.common.crafting.WandTransforms;
import com.verdantartifice.primalmagic.common.sources.Source;
import com.verdantartifice.primalmagic.common.sources.SourceList;
import com.verdantartifice.primalmagic.common.wands.IInteractWithWand;
import com.verdantartifice.primalmagic.common.wands.IWand;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.UseAction;
import net.minecraft.nbt.IntNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public abstract class AbstractWandItem extends Item implements IWand {
    public AbstractWandItem(Properties properties) {
        super(properties);
    }

    @Override
    public int getMana(ItemStack stack, Source source) {
        int retVal = 0;
        if (stack != null && source != null && stack.hasTag() && stack.getTag().contains(source.getTag())) {
            retVal = stack.getTag().getInt(source.getTag());
        }
        return retVal;
    }

    @Override
    public SourceList getAllMana(ItemStack stack) {
        SourceList retVal = new SourceList();
        for (Source source : Source.SORTED_SOURCES) {
            if (stack.hasTag() && stack.getTag().contains(source.getTag())) {
                retVal.merge(source, stack.getTag().getInt(source.getTag()));
            } else {
                retVal.merge(source, 0);
            }
        }
        return retVal;
    }

    public abstract int getMaxMana(ItemStack stack);
    
    protected void setMana(@Nonnull ItemStack stack, @Nonnull Source source, int amount) {
        stack.setTagInfo(source.getTag(), new IntNBT(amount));
    }

    @Override
    public int addMana(ItemStack stack, Source source, int amount) {
        if (stack == null || source == null) {
            return 0;
        }
        int toStore = this.getMana(stack, source) + amount;
        int leftover = Math.max(toStore - this.getMaxMana(stack), 0);
        this.setMana(stack, source, Math.min(toStore, this.getMaxMana(stack)));
        return leftover;
    }

    @Override
    public boolean consumeMana(ItemStack stack, PlayerEntity player, Source source, int amount) {
        if (stack == null || player == null || source == null) {
            return false;
        }
        if (this.containsMana(stack, player, source, amount)) {
            this.setMana(stack, source, this.getMana(stack, source) - amount);
            return true;
        } else {
            return false;
        }
    }
    
    @Override
    public boolean consumeMana(ItemStack stack, PlayerEntity player, SourceList sources) {
        if (stack == null || player == null || sources == null) {
            return false;
        }
        if (this.containsMana(stack, player, sources)) {
            for (Source source : sources.getSources()) {
                this.setMana(stack, source, this.getMana(stack, source) - sources.getAmount(source));
            }
            return true;
        } else {
            return false;
        }
    }
    
    @Override
    public boolean containsMana(ItemStack stack, PlayerEntity player, Source source, int amount) {
        return this.getMana(stack, source) >= amount;
    }
    
    @Override
    public boolean containsMana(ItemStack stack, PlayerEntity player, SourceList sources) {
        for (Source source : sources.getSources()) {
            if (!this.containsMana(stack, player, source, sources.getAmount(source))) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        PlayerEntity player = Minecraft.getInstance().player;
        if (PrimalMagic.proxy.isShiftDown()) {
            for (Source source : Source.SORTED_SOURCES) {
                if (source.isDiscovered(player)) {
                    ITextComponent nameComp = new TranslationTextComponent(source.getNameTranslationKey()).applyTextStyle(source.getChatColor());
                    ITextComponent line = new TranslationTextComponent("primalmagic.source.mana_tooltip", nameComp.getFormattedText(), this.getMana(stack, source), this.getMaxMana(stack));
                    tooltip.add(line);
                }
            }
        } else {
            StringBuilder sb = new StringBuilder();
            boolean first = true;
            for (Source source : Source.SORTED_SOURCES) {
                if (source.isDiscovered(player)) {
                    if (!first) {
                        sb.append("/");
                    }
                    int mana = this.getMana(stack, source);
                    ITextComponent manaStr = new StringTextComponent(Integer.toString(mana)).applyTextStyle(source.getChatColor());
                    sb.append(manaStr.getFormattedText());
                    first = false;
                }
            }
            tooltip.add(new StringTextComponent(sb.toString()));
        }
    }
    
    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }
    
    @Override
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }
    
    @Override
    public IInteractWithWand getTileInUse(ItemStack wandStack, World world) {
        if (wandStack.hasTag() && wandStack.getTag().contains("UsingX") && wandStack.getTag().contains("UsingY") && wandStack.getTag().contains("UsingZ")) {
            BlockPos pos = new BlockPos(wandStack.getTag().getInt("UsingX"), wandStack.getTag().getInt("UsingY"), wandStack.getTag().getInt("UsingZ"));
            TileEntity tile = world.getTileEntity(pos);
            if (tile instanceof IInteractWithWand) {
                return (IInteractWithWand)tile;
            }
        }
        return null;
    }
    
    @Override
    public <T extends TileEntity & IInteractWithWand> void setTileInUse(ItemStack wandStack, T tile) {
        wandStack.setTagInfo("UsingX", new IntNBT(tile.getPos().getX()));
        wandStack.setTagInfo("UsingY", new IntNBT(tile.getPos().getY()));
        wandStack.setTagInfo("UsingZ", new IntNBT(tile.getPos().getZ()));
    }
    
    @Override
    public void clearTileInUse(ItemStack wandStack) {
        if (wandStack.hasTag()) {
            wandStack.getTag().remove("UsingX");
            wandStack.getTag().remove("UsingY");
            wandStack.getTag().remove("UsingZ");
        }
    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);
        playerIn.setActiveHand(handIn);
        return new ActionResult<>(ActionResultType.SUCCESS, stack);
    }
    
    @Override
    public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context) {
        if (context.getPlayer().isSneaking()) {
            return ActionResultType.PASS;
        }
        BlockState bs = context.getWorld().getBlockState(context.getPos());
        if (bs.getBlock() instanceof IInteractWithWand) {
            return ((IInteractWithWand)bs.getBlock()).onWandRightClick(context.getItem(), context.getWorld(), context.getPlayer(), context.getPos(), context.getFace());
        }
        TileEntity tile = context.getWorld().getTileEntity(context.getPos());
        if (tile != null && tile instanceof IInteractWithWand) {
            return ((IInteractWithWand)tile).onWandRightClick(context.getItem(), context.getWorld(), context.getPlayer(), context.getPos(), context.getFace());
        }
        for (IWandTransform transform : WandTransforms.getAll()) {
            if (transform.isValid(context.getWorld(), context.getPlayer(), context.getPos())) {
                if (!context.getPlayer().canPlayerEdit(context.getPos(), context.getFace(), context.getItem())) {
                    return ActionResultType.FAIL;
                } else {
                    context.getPlayer().swingArm(context.getHand());
                    transform.execute(context.getWorld(), context.getPlayer(), context.getPos());
                    return ActionResultType.SUCCESS;
                }
            }
        }
        return ActionResultType.PASS;
    }
    
    @Override
    public void onUsingTick(ItemStack stack, LivingEntity living, int count) {
        if (living instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity)living;
            IInteractWithWand wandable = this.getTileInUse(stack, player.world);
            if (wandable != null) {
                wandable.onWandUseTick(stack, player, count);
            }
        }
    }
    
    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft) {
        super.onPlayerStoppedUsing(stack, worldIn, entityLiving, timeLeft);
        this.clearTileInUse(stack);
    }
    
    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return false;
    }
    
    @Override
    public boolean canContinueUsing(ItemStack oldStack, ItemStack newStack) {
        return true;
    }
}
