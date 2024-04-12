package com.verdantartifice.primalmagick.common.items.books;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.function.Supplier;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.verdantartifice.primalmagick.client.util.ClientUtils;
import com.verdantartifice.primalmagick.common.books.BookDefinition;
import com.verdantartifice.primalmagick.common.books.BookHelper;
import com.verdantartifice.primalmagick.common.books.BookLanguage;
import com.verdantartifice.primalmagick.common.books.BookLanguagesPM;
import com.verdantartifice.primalmagick.common.books.BookType;
import com.verdantartifice.primalmagick.common.books.BookView;
import com.verdantartifice.primalmagick.common.books.BooksPM;
import com.verdantartifice.primalmagick.common.books.LinguisticsManager;
import com.verdantartifice.primalmagick.common.network.PacketHandler;
import com.verdantartifice.primalmagick.common.network.packets.misc.OpenStaticBookScreenPacket;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
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
import net.minecraftforge.fml.loading.FMLEnvironment;

/**
 * Item definition for a readable book populated by static, localized text rather than carrying
 * it all around in NBT for every copy.
 * 
 * @author Daedalus4096
 */
public class StaticBookItem extends Item {
    protected static final Logger LOGGER = LogManager.getLogger();
    
    public static final String TAG_BOOK_ID = "BookId";
    public static final String TAG_BOOK_LANGUAGE_ID = "BookLanguageId";
    public static final String TAG_AUTHOR_OVERRIDE = "AuthorOverride";
    public static final String TAG_COMPREHENSION = "Comprehension";
    public static final String TAG_GENERATION = "Generation";
    public static final int MAX_GENERATION = 2;
    
    protected static final DecimalFormat COMPREHENSION_FORMATTER = new DecimalFormat("###.#");
    protected static final Map<BookType, StaticBookItem> TYPE_MAP = new HashMap<>();
    
    protected final BookType bookType;

    public StaticBookItem(BookType type, Item.Properties properties) {
        super(properties);
        this.bookType = type;
        TYPE_MAP.put(type, this);
    }
    
    public static ItemStack make(Supplier<StaticBookItem> itemSupplier, Optional<ResourceKey<BookDefinition>> bookDefKeyOpt, Optional<ResourceKey<BookLanguage>> bookLangKeyOpt) {
        return make(itemSupplier, bookDefKeyOpt, bookLangKeyOpt, Optional.empty(), OptionalInt.empty(), OptionalInt.empty());
    }
    
    public static ItemStack make(Supplier<StaticBookItem> itemSupplier, Optional<ResourceKey<BookDefinition>> bookDefKeyOpt, Optional<ResourceKey<BookLanguage>> bookLangKeyOpt, Optional<String> authorOpt, 
            OptionalInt generationOpt, OptionalInt translationOpt) {
        ItemStack retVal = new ItemStack(itemSupplier.get());
        bookDefKeyOpt.ifPresent(bookDefKey -> setBookDefinition(retVal, bookDefKey));
        bookLangKeyOpt.ifPresent(bookLangKey -> setBookLanguage(retVal, bookLangKey));
        authorOpt.ifPresent(authorOverride -> setAuthorOverride(retVal, authorOverride));
        generationOpt.ifPresent(generation -> setGeneration(retVal, generation));
        setTranslatedComprehension(retVal, translationOpt);
        return retVal;
    }
    
    protected static MutableComponent getStaticAttribute(ResourceKey<BookDefinition> bookId, String attrName) {
        return Component.translatable(String.join(".", "written_book", bookId.location().getNamespace(), bookId.location().getPath(), attrName));
    }
    
    public static Optional<ResourceKey<BookDefinition>> getBookId(ItemStack stack) {
        CompoundTag rootTag = stack.getTag();
        if (rootTag != null) {
            String str = rootTag.getString(TAG_BOOK_ID);
            if (!StringUtil.isNullOrEmpty(str)) {
                ResourceLocation loc = ResourceLocation.tryParse(str);
                return loc == null ? Optional.empty() : Optional.ofNullable(ResourceKey.create(RegistryKeysPM.BOOKS, loc));
            }
        }
        return Optional.empty();
    }
    
    public static boolean hasBookDefinition(ItemStack stack) {
        return getBookId(stack).isPresent();
    }
    
    public static Optional<Holder.Reference<BookDefinition>> getBookDefinition(ItemStack stack, RegistryAccess registryAccess) {
        return getBookId(stack).flatMap(k -> registryAccess.registryOrThrow(RegistryKeysPM.BOOKS).getHolder(k));
    }
    
    public static ItemStack setBookDefinition(ItemStack stack, ResourceKey<BookDefinition> defKey) {
        stack.getOrCreateTag().putString(TAG_BOOK_ID, defKey.location().toString());
        return stack;
    }
    
    public static Optional<ResourceKey<BookLanguage>> getBookLanguageId(ItemStack stack) {
        CompoundTag rootTag = stack.getTag();
        if (rootTag != null) {
            String str = rootTag.getString(TAG_BOOK_LANGUAGE_ID);
            if (!StringUtil.isNullOrEmpty(str)) {
                ResourceLocation loc = ResourceLocation.tryParse(str);
                return loc == null ? Optional.empty() : Optional.ofNullable(ResourceKey.create(RegistryKeysPM.BOOK_LANGUAGES, loc));
            }
        }
        return Optional.empty();
    }
    
    public static boolean hasBookLanguage(ItemStack stack) {
        return getBookLanguageId(stack).isPresent();
    }
    
    public static Optional<Holder.Reference<BookLanguage>> getBookLanguage(ItemStack stack, RegistryAccess registryAccess) {
        return getBookLanguageId(stack).flatMap(k -> registryAccess.registryOrThrow(RegistryKeysPM.BOOK_LANGUAGES).getHolder(k));
    }
    
    public static ItemStack setBookLanguage(ItemStack stack, ResourceKey<BookLanguage> lang) {
        stack.getOrCreateTag().putString(TAG_BOOK_LANGUAGE_ID, lang.location().toString());
        return stack;
    }

    protected static Component getNameFromBookId(ResourceKey<BookDefinition> bookId) {
        return getStaticAttribute(bookId, "title");
    }
    
    public static boolean hasAuthor(ItemStack stack) {
        return stack.hasTag() && (!StringUtil.isNullOrEmpty(stack.getTag().getString(TAG_AUTHOR_OVERRIDE)) || !StringUtil.isNullOrEmpty(stack.getTag().getString(TAG_BOOK_ID)));
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
        return getBookId(stack).map(StaticBookItem::getAuthorFromBookId).orElse(Component.translatable("tooltip.primalmagick.written_book.author.unknown"));
    }
    
    protected static MutableComponent getAuthorFromBookId(ResourceKey<BookDefinition> bookId) {
        return getStaticAttribute(bookId, "author");
    }
    
    public static ItemStack setAuthorOverride(ItemStack stack, String name) {
        stack.getOrCreateTag().putString(TAG_AUTHOR_OVERRIDE, name);
        return stack;
    }
    
    public static OptionalInt getTranslatedComprehension(ItemStack stack) {
        CompoundTag rootTag = stack.getTag();
        return (rootTag == null) ? OptionalInt.empty() : OptionalInt.of(rootTag.getInt(TAG_COMPREHENSION));
    }
    
    public static ItemStack setTranslatedComprehension(ItemStack stack, OptionalInt comprehensionOpt) {
        comprehensionOpt.ifPresentOrElse(comprehension -> stack.getOrCreateTag().putInt(TAG_COMPREHENSION, comprehension), () -> stack.getOrCreateTag().remove(TAG_COMPREHENSION));
        return stack;
    }
    
    public static int getGeneration(ItemStack stack) {
        CompoundTag rootTag = stack.getTag();
        return rootTag == null ? 0 : rootTag.getInt(TAG_GENERATION);
    }
    
    public static ItemStack setGeneration(ItemStack stack, int newGeneration) {
        stack.getOrCreateTag().putInt(TAG_GENERATION, newGeneration);
        return stack;
    }
    
    private static BookView makeBookView(ItemStack pStack) {
        Player player = FMLEnvironment.dist.isClient() ? ClientUtils.getCurrentPlayer() : null;
        ResourceKey<BookDefinition> bookKey = getBookId(pStack).orElse(BooksPM.TEST_BOOK);
        ResourceKey<BookLanguage> langKey = getBookLanguageId(pStack).orElse(BookLanguagesPM.DEFAULT);
        int playerComprehension = 0;
        if (player != null) {
            Optional<Holder.Reference<BookLanguage>> langHolderOpt = getBookLanguage(pStack, player.level().registryAccess());
            if (langHolderOpt.isPresent()) {
                playerComprehension = LinguisticsManager.getComprehension(player, langHolderOpt.get());
            }
        }
        int comprehension = Math.max(playerComprehension, getTranslatedComprehension(pStack).orElse(0));
        return new BookView(bookKey, langKey, comprehension);
    }

    @Override
    public Component getName(ItemStack pStack) {
        if (FMLEnvironment.dist.isClient() && getBookId(pStack).isPresent()) {
            return BookHelper.getTitleText(makeBookView(pStack), ClientUtils.getCurrentLevel().registryAccess());
        } else {
            return super.getName(pStack);
        }
    }
    
    @Override
    public void appendHoverText(ItemStack pStack, Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        if (pLevel.isClientSide && hasAuthor(pStack)) {
            Component authorText = BookHelper.getAuthorText(makeBookView(pStack), getAuthor(pStack), pLevel.registryAccess());
            pTooltipComponents.add(Component.translatable("book.byAuthor", authorText).withStyle(ChatFormatting.GRAY));
        }
        getBookLanguage(pStack, pLevel.registryAccess()).ifPresent(langHolder -> {
            pTooltipComponents.add(Component.translatable("tooltip.primalmagick.written_language.header", langHolder.get().getName()).withStyle(ChatFormatting.GRAY));
            pTooltipComponents.add(Component.translatable("book.generation." + getGeneration(pStack)).withStyle(ChatFormatting.GRAY));
            if (pLevel.isClientSide && hasBookDefinition(pStack) && hasBookLanguage(pStack) && langHolder.get().isComplex()) {
                Player player = FMLEnvironment.dist.isClient() ? ClientUtils.getCurrentPlayer() : null;
                Optional<Holder.Reference<BookDefinition>> defHolderOpt = getBookDefinition(pStack, pLevel.registryAccess());
                OptionalInt translatedComprehension = getTranslatedComprehension(pStack);
                int comprehension = Math.max(translatedComprehension.orElse(0), LinguisticsManager.getComprehension(player, langHolder));
                double percentage = BookHelper.getBookComprehension(new BookView(getBookId(pStack).orElseThrow(), langHolder.key(), comprehension), pLevel.registryAccess());
                pTooltipComponents.add(Component.translatable("tooltip.primalmagick.written_language.comprehension", COMPREHENSION_FORMATTER.format(100 * percentage)).withStyle(ChatFormatting.GRAY));
                if (translatedComprehension.isPresent()) {
                    if (translatedComprehension.getAsInt() >= langHolder.get().complexity()) {
                        pTooltipComponents.add(Component.translatable("tooltip.primalmagick.written_language.translated.full").withStyle(ChatFormatting.DARK_AQUA));
                    } else if (translatedComprehension.getAsInt() > 0) {
                        pTooltipComponents.add(Component.translatable("tooltip.primalmagick.written_language.translated.partial").withStyle(ChatFormatting.DARK_AQUA));
                    }
                }
                
                int timesStudied = defHolderOpt.isPresent() ? LinguisticsManager.getTimesStudied(player, defHolderOpt.get(), langHolder) : 0;
                if (timesStudied > 0) {
                    pTooltipComponents.add(Component.translatable("tooltip.primalmagick.written_language.times_studied", timesStudied).withStyle(ChatFormatting.DARK_AQUA));
                }
            }
        });
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack stack = pPlayer.getItemInHand(pUsedHand);
        if (!pLevel.isClientSide && pPlayer instanceof ServerPlayer serverPlayer) {
            getBookDefinition(stack, pLevel.registryAccess()).ifPresentOrElse(bookDefHolder -> {
                getBookLanguage(stack, pLevel.registryAccess()).ifPresentOrElse(langHolder -> {
                    LinguisticsManager.markRead(pPlayer, bookDefHolder, langHolder);
                    PacketHandler.sendToPlayer(new OpenStaticBookScreenPacket(stack, this.bookType), serverPlayer);
                }, () -> {
                    LOGGER.error("No book language found when opening static book stack {}", stack.toString());
                });
            }, () -> {
                LOGGER.error("No book definition found when opening static book stack {}", stack.toString());
            });
        }
        return new InteractionResultHolder<ItemStack>(InteractionResult.SUCCESS, stack);
    }
}
