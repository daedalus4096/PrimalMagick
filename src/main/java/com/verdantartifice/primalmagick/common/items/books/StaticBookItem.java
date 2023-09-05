package com.verdantartifice.primalmagick.common.items.books;

import java.util.List;
import java.util.Optional;

import com.verdantartifice.primalmagick.common.books.BookDefinition;
import com.verdantartifice.primalmagick.common.books.BooksPM;
import com.verdantartifice.primalmagick.common.network.PacketHandler;
import com.verdantartifice.primalmagick.common.network.packets.misc.OpenStaticBookScreenPacket;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.StringUtil;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

/**
 * Item definition for a readable book populated by static, localized text rather than carrying
 * it all around in NBT for every copy.
 * 
 * @author Daedalus4096
 */
public class StaticBookItem extends Item {
    public static final String TAG_BOOK_ID = "BookId";
    public static final String TAG_AUTHOR_OVERRIDE = "AuthorOverride";
    public static final String TAG_GENERATION = "Generation";
    public static final int MAX_GENERATION = 2;
    
    public StaticBookItem(Item.Properties properties) {
        super(properties);
    }
    
    protected static MutableComponent getStaticAttribute(ResourceLocation bookId, String attrName) {
        return Component.translatable(String.join(".", "written_book", bookId.getNamespace(), bookId.getPath(), attrName));
    }
    
    public static Optional<ResourceLocation> getBookId(ItemStack stack) {
        CompoundTag rootTag = stack.getTag();
        if (rootTag != null) {
            String str = rootTag.getString(TAG_BOOK_ID);
            if (!StringUtil.isNullOrEmpty(str)) {
                return Optional.ofNullable(ResourceLocation.tryParse(str));
            }
        }
        return Optional.empty();
    }
    
    public static Optional<BookDefinition> getBookDefinition(ItemStack stack) {
        return getBookId(stack).map(BooksPM.BOOKS.get()::getValue);
    }
    
    public static void setBookId(ItemStack stack, ResourceLocation bookId) {
        stack.getOrCreateTag().putString(TAG_BOOK_ID, bookId.toString());
    }

    @Override
    public Component getName(ItemStack pStack) {
        return getBookId(pStack).map(StaticBookItem::getNameFromBookId).orElse(super.getName(pStack));
    }
    
    protected static Component getNameFromBookId(ResourceLocation bookId) {
        return getStaticAttribute(bookId, "title");
    }
    
    public static Component getAuthor(ItemStack stack) {
        // Use the author override if there's one set
        CompoundTag rootTag = stack.getTag();
        if (rootTag != null) {
            String authorOverride = rootTag.getString(TAG_AUTHOR_OVERRIDE);
            if (!StringUtil.isNullOrEmpty(authorOverride)) {
                return Component.literal(authorOverride);
            }
        }
        
        // Otherwise, fetch the author from lang data
        return getBookId(stack).map(StaticBookItem::getAuthorFromBookId).orElse(Component.translatable("tooltip.written_book.author.unknown"));
    }
    
    protected static MutableComponent getAuthorFromBookId(ResourceLocation bookId) {
        return getStaticAttribute(bookId, "author");
    }
    
    public static void setAuthorOverride(ItemStack stack, String name) {
        stack.getOrCreateTag().putString(TAG_AUTHOR_OVERRIDE, name);
    }
    
    public static int getGeneration(ItemStack stack) {
        return stack.getTag().getInt(TAG_GENERATION);
    }
    
    public static void setGeneration(ItemStack stack, int newGeneration) {
        stack.getOrCreateTag().putInt(TAG_GENERATION, newGeneration);
    }

    @Override
    public void appendHoverText(ItemStack pStack, Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(Component.translatable("book.byAuthor", getAuthor(pStack)).withStyle(ChatFormatting.GRAY));
        pTooltipComponents.add(Component.translatable("book.generation." + getGeneration(pStack)).withStyle(ChatFormatting.GRAY));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack stack = pPlayer.getItemInHand(pUsedHand);
        if (!pLevel.isClientSide && pPlayer instanceof ServerPlayer serverPlayer) {
            getBookDefinition(stack).ifPresent(bookDef -> PacketHandler.sendToPlayer(new OpenStaticBookScreenPacket(bookDef), serverPlayer));
        }
        return new InteractionResultHolder<ItemStack>(InteractionResult.SUCCESS, stack);
    }
}
