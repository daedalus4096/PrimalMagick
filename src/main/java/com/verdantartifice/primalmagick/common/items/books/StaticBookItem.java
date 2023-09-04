package com.verdantartifice.primalmagick.common.items.books;

import java.util.List;
import java.util.Optional;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringUtil;
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
    
    public StaticBookItem(Item.Properties properties) {
        super(properties);
    }
    
    public static int getPageCount(ItemStack stack) {
        // TODO Retrieve NBT cached page count if present, otherwise process text and return calculated page count
        return 0;
    }
    
    protected static MutableComponent getStaticAttribute(ResourceLocation bookId, String attrName) {
        return Component.translatable(String.join(".", "written_book", bookId.getNamespace(), bookId.getPath(), attrName));
    }
    
    protected Optional<ResourceLocation> getBookId(ItemStack stack) {
        CompoundTag rootTag = stack.getTag();
        if (rootTag != null) {
            String str = rootTag.getString(TAG_BOOK_ID);
            if (!StringUtil.isNullOrEmpty(str)) {
                return Optional.ofNullable(ResourceLocation.tryParse(str));
            }
        }
        return Optional.empty();
    }
    
    public static void setBookId(ItemStack stack, ResourceLocation bookId) {
        stack.getOrCreateTag().putString(TAG_BOOK_ID, bookId.toString());
    }

    @Override
    public Component getName(ItemStack pStack) {
        return this.getBookId(pStack).map(StaticBookItem::getNameFromBookId).orElse(super.getName(pStack));
    }
    
    protected static Component getNameFromBookId(ResourceLocation bookId) {
        return getStaticAttribute(bookId, "title");
    }
    
    public Component getAuthor(ItemStack stack) {
        // Use the author override if there's one set
        CompoundTag rootTag = stack.getTag();
        if (rootTag != null) {
            String authorOverride = rootTag.getString(TAG_AUTHOR_OVERRIDE);
            if (!StringUtil.isNullOrEmpty(authorOverride)) {
                return Component.literal(authorOverride);
            }
        }
        
        // Otherwise, fetch the author from lang data
        return this.getBookId(stack).map(StaticBookItem::getAuthorFromBookId).orElse(Component.translatable("tooltip.written_book.author.unknown"));
    }
    
    protected static MutableComponent getAuthorFromBookId(ResourceLocation bookId) {
        return getStaticAttribute(bookId, "author");
    }
    
    public static void setAuthorOverride(ItemStack stack, String name) {
        stack.getOrCreateTag().putString(TAG_AUTHOR_OVERRIDE, name);
    }

    @Override
    public void appendHoverText(ItemStack pStack, Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(Component.translatable("book.byAuthor", this.getAuthor(pStack)).withStyle(ChatFormatting.GRAY));
    }
}
