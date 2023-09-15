package com.verdantartifice.primalmagick.common.items.books;

import java.util.List;
import java.util.function.Supplier;

import com.verdantartifice.primalmagick.common.books.BookLanguagesPM;
import com.verdantartifice.primalmagick.common.books.BooksPM;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

/**
 * Item which is transformed into a randomized instance of a static book item upon use.  Exists so
 * that NBT attachement doesn't have to happen as part of a crafting recipe.
 * 
 * @author Daedalus4096
 */
public class StaticBookGeneratorItem extends Item {
    protected final Supplier<? extends StaticBookItem> itemSupplier;
    
    public StaticBookGeneratorItem(Supplier<? extends StaticBookItem> itemSupplier, Item.Properties properties) {
        super(properties);
        this.itemSupplier = itemSupplier;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if (!pLevel.isClientSide && pPlayer instanceof ServerPlayer serverPlayer) {
            ItemStack bookStack = new ItemStack(this.itemSupplier.get());
            
            // TODO Choose a random book and random language from a random culture
            StaticBookItem.setBookDefinition(bookStack, BooksPM.DREAM_JOURNAL.get());
            StaticBookItem.setBookLanguage(bookStack, BookLanguagesPM.ILLAGER.get());
            
            serverPlayer.level().playSound((Player)null, serverPlayer.getX(), serverPlayer.getY(), serverPlayer.getZ(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.2F, ((serverPlayer.getRandom().nextFloat() - serverPlayer.getRandom().nextFloat()) * 0.7F + 1.0F) * 2.0F);
            serverPlayer.getItemInHand(pUsedHand).shrink(1);
            if (!serverPlayer.getInventory().add(bookStack)) {
                serverPlayer.drop(bookStack, false);
            }
        }
        return super.use(pLevel, pPlayer, pUsedHand);
    }

    @Override
    public void appendHoverText(ItemStack pStack, Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(Component.translatable(this.getDescriptionId() + ".tooltip").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
    }
}
