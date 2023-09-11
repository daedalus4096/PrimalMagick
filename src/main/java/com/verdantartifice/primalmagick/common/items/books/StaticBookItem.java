package com.verdantartifice.primalmagick.common.items.books;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;

import com.verdantartifice.primalmagick.client.books.BookHelper;
import com.verdantartifice.primalmagick.client.books.BookView;
import com.verdantartifice.primalmagick.client.util.ClientUtils;
import com.verdantartifice.primalmagick.common.books.BookDefinition;
import com.verdantartifice.primalmagick.common.books.BookLanguage;
import com.verdantartifice.primalmagick.common.books.BookLanguagesPM;
import com.verdantartifice.primalmagick.common.books.BooksPM;
import com.verdantartifice.primalmagick.common.capabilities.PrimalMagickCapabilities;
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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;

/**
 * Item definition for a readable book populated by static, localized text rather than carrying
 * it all around in NBT for every copy.
 * 
 * @author Daedalus4096
 */
public class StaticBookItem extends Item {
    public static final String TAG_BOOK_ID = "BookId";
    public static final String TAG_BOOK_LANGUAGE_ID = "BookLanguageId";
    public static final String TAG_AUTHOR_OVERRIDE = "AuthorOverride";
    public static final String TAG_GENERATION = "Generation";
    public static final int MAX_GENERATION = 2;
    
    protected static final DecimalFormat COMPREHENSION_FORMATTER = new DecimalFormat("###.#");

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
    
    public static BookDefinition getBookDefinition(ItemStack stack) {
        return getBookId(stack).map(BooksPM.BOOKS.get()::getValue).orElse(BooksPM.TEST_BOOK.get());
    }
    
    public static void setBookDefinition(ItemStack stack, BookDefinition def) {
        stack.getOrCreateTag().putString(TAG_BOOK_ID, def.bookId().toString());
    }
    
    protected static Optional<ResourceLocation> getBookLanguageId(ItemStack stack) {
        CompoundTag rootTag = stack.getTag();
        if (rootTag != null) {
            String str = rootTag.getString(TAG_BOOK_LANGUAGE_ID);
            if (!StringUtil.isNullOrEmpty(str)) {
                return Optional.ofNullable(ResourceLocation.tryParse(str));
            }
        }
        return Optional.empty();
    }
    
    public static BookLanguage getBookLanguage(ItemStack stack) {
        return getBookLanguageId(stack).map(BookLanguagesPM.LANGUAGES.get()::getValue).orElse(BookLanguagesPM.DEFAULT.get());
    }
    
    public static void setBookLanguage(ItemStack stack, BookLanguage lang) {
        stack.getOrCreateTag().putString(TAG_BOOK_LANGUAGE_ID, lang.languageId().toString());
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
        BookLanguage lang = getBookLanguage(pStack);
        pTooltipComponents.add(Component.translatable("book.byAuthor", getAuthor(pStack)).withStyle(ChatFormatting.GRAY));
        pTooltipComponents.add(Component.translatable("tooltip.primalmagick.written_language.header", getBookLanguage(pStack).getName()).withStyle(ChatFormatting.GRAY));
        pTooltipComponents.add(Component.translatable("book.generation." + getGeneration(pStack)).withStyle(ChatFormatting.GRAY));
        if (lang.isComplex()) {
            Player player = (FMLEnvironment.dist == Dist.CLIENT) ? ClientUtils.getCurrentPlayer() : null;
            BookDefinition def = getBookDefinition(pStack);
            int comprehension = PrimalMagickCapabilities.getLinguistics(player).<Integer>map(linguistics -> linguistics.getComprehension(lang.languageId())).orElse(0);
            double percentage = BookHelper.getBookComprehension(new BookView(BooksPM.BOOKS.get().getResourceKey(def).orElseThrow(), lang.languageId(), comprehension));
            pTooltipComponents.add(Component.translatable("tooltip.primalmagick.written_language.comprehension", COMPREHENSION_FORMATTER.format(100 * percentage)).withStyle(ChatFormatting.GRAY));
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack stack = pPlayer.getItemInHand(pUsedHand);
        if (!pLevel.isClientSide && pPlayer instanceof ServerPlayer serverPlayer) {
            PacketHandler.sendToPlayer(new OpenStaticBookScreenPacket(stack), serverPlayer);
        }
        return new InteractionResultHolder<ItemStack>(InteractionResult.SUCCESS, stack);
    }
}
