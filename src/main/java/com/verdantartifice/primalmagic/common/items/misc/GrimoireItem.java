package com.verdantartifice.primalmagic.common.items.misc;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.containers.GrimoireContainer;
import com.verdantartifice.primalmagic.common.items.base.ItemPM;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class GrimoireItem extends ItemPM implements INamedContainerProvider {
    public GrimoireItem() {
        super("grimoire", new Item.Properties().group(PrimalMagic.ITEM_GROUP).maxStackSize(1).rarity(Rarity.UNCOMMON));
    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        if (!worldIn.isRemote && playerIn instanceof ServerPlayerEntity) {
            NetworkHooks.openGui((ServerPlayerEntity)playerIn, this);
        }
        return new ActionResult<ItemStack>(ActionResultType.SUCCESS, playerIn.getHeldItem(handIn));
    }

    @Override
    public Container createMenu(int windowId, PlayerInventory inv, PlayerEntity player) {
        return new GrimoireContainer(windowId);
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent(this.getTranslationKey());
    }
}
