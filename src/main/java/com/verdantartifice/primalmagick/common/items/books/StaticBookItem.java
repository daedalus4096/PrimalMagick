package com.verdantartifice.primalmagick.common.items.books;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mojang.datafixers.util.Either;
import com.verdantartifice.primalmagick.client.util.ClientUtils;
import com.verdantartifice.primalmagick.common.books.BookDefinition;
import com.verdantartifice.primalmagick.common.books.BookHelper;
import com.verdantartifice.primalmagick.common.books.BookLanguage;
import com.verdantartifice.primalmagick.common.books.BookLanguagesPM;
import com.verdantartifice.primalmagick.common.books.BookType;
import com.verdantartifice.primalmagick.common.books.BookView;
import com.verdantartifice.primalmagick.common.books.BooksPM;
import com.verdantartifice.primalmagick.common.books.LinguisticsManager;
import com.verdantartifice.primalmagick.common.components.DataComponentsPM;
import com.verdantartifice.primalmagick.common.network.PacketHandler;
import com.verdantartifice.primalmagick.common.network.packets.misc.OpenStaticBookScreenPacket;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
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
    
    protected static MutableComponent getStaticAttribute(ResourceKey<BookDefinition> bookId, String attrName) {
        return Component.translatable(String.join(".", "written_book", bookId.location().getNamespace(), bookId.location().getPath(), attrName));
    }
    
    protected static Component getNameFromBookId(ResourceKey<BookDefinition> bookId) {
        return getStaticAttribute(bookId, "title");
    }
    
    protected static MutableComponent getAuthorFromBookId(ResourceKey<BookDefinition> bookId) {
        return getStaticAttribute(bookId, "author");
    }
    
    public static Optional<Holder<BookDefinition>> getBookDefinition(ItemStack stack) {
        return Optional.ofNullable(stack.get(DataComponentsPM.BOOK_DEFINITION.get()));
    }
    
    public static Optional<ResourceKey<BookDefinition>> getBookId(ItemStack stack) {
        return getBookDefinition(stack).flatMap(defHolder -> defHolder.unwrapKey());
    }
    
    public static boolean hasBookDefinition(ItemStack stack) {
        return getBookDefinition(stack).isPresent();
    }
    
    public static ItemStack setBookDefinition(ItemStack stack, Holder<BookDefinition> bookDef) {
        stack.set(DataComponentsPM.BOOK_DEFINITION.get(), bookDef);
        return stack;
    }
    
    public static Optional<Holder<BookLanguage>> getBookLanguage(ItemStack stack) {
        return Optional.ofNullable(stack.get(DataComponentsPM.BOOK_LANGUAGE.get()));
    }
    
    public static Optional<ResourceKey<BookLanguage>> getBookLanguageId(ItemStack stack) {
        return getBookLanguage(stack).flatMap(langHolder -> langHolder.unwrapKey());
    }
    
    public static boolean hasBookLanguage(ItemStack stack) {
        return getBookLanguage(stack).isPresent();
    }
    
    public static ItemStack setBookLanguage(ItemStack stack, Holder<BookLanguage> lang) {
        stack.set(DataComponentsPM.BOOK_LANGUAGE.get(), lang);
        return stack;
    }

    public static boolean hasAuthor(ItemStack stack) {
        return !StringUtil.isNullOrEmpty(stack.get(DataComponentsPM.AUTHOR_OVERRIDE.get())) || !stack.has(DataComponentsPM.BOOK_DEFINITION.get());
    }
    
    public static Component getAuthor(ItemStack stack) {
        if (stack.has(DataComponentsPM.AUTHOR_OVERRIDE.get())) {
            // Use the author override if there's one set
            return Component.literal(stack.get(DataComponentsPM.AUTHOR_OVERRIDE.get()));
        } else {
            // Otherwise, fetch the author from lang data
            return getBookId(stack).map(StaticBookItem::getAuthorFromBookId).orElse(Component.translatable("tooltip.primalmagick.written_book.author.unknown"));
        }
    }
    
    public static ItemStack setAuthorOverride(ItemStack stack, String name) {
        stack.set(DataComponentsPM.AUTHOR_OVERRIDE.get(), name);
        return stack;
    }
    
    public static Optional<Integer> getTranslatedComprehension(ItemStack stack) {
        return Optional.ofNullable(stack.get(DataComponentsPM.TRANSLATED_COMPREHENSION.get()));
    }
    
    public static ItemStack setTranslatedComprehension(ItemStack stack, Optional<Integer> comprehensionOpt) {
        comprehensionOpt.ifPresentOrElse(comp -> {
            stack.set(DataComponentsPM.TRANSLATED_COMPREHENSION.get(), comp);
        }, () -> {
            stack.remove(DataComponentsPM.TRANSLATED_COMPREHENSION.get());
        });
        return stack;
    }
    
    public static int getGeneration(ItemStack stack) {
        return stack.getOrDefault(DataComponentsPM.BOOK_GENERATION.get(), 0);
    }
    
    public static ItemStack setGeneration(ItemStack stack, int newGeneration) {
        stack.set(DataComponentsPM.BOOK_GENERATION.get(), newGeneration);
        return stack;
    }
    
    public static BookView makeBookView(ItemStack pStack, HolderLookup.Provider registries) {
        Player player = FMLEnvironment.dist.isClient() ? ClientUtils.getCurrentPlayer() : null;
        Holder<BookDefinition> book = getBookDefinition(pStack).orElseGet(() -> registries.lookupOrThrow(RegistryKeysPM.BOOKS).getOrThrow(BooksPM.TEST_BOOK));
        Holder<BookLanguage> lang = getBookLanguage(pStack).orElseGet(() -> registries.lookupOrThrow(RegistryKeysPM.BOOK_LANGUAGES).getOrThrow(BookLanguagesPM.DEFAULT));
        int playerComprehension = 0;
        if (player != null) {
            Optional<Holder<BookLanguage>> langHolderOpt = getBookLanguage(pStack);
            if (langHolderOpt.isPresent()) {
                playerComprehension = LinguisticsManager.getComprehension(player, langHolderOpt.get());
            }
        }
        int comprehension = Math.max(playerComprehension, getTranslatedComprehension(pStack).orElse(0));
        return new BookView(Either.left(book), lang, comprehension);
    }

    @Override
    public Component getName(ItemStack pStack) {
        if (FMLEnvironment.dist.isClient() && getBookId(pStack).isPresent()) {
            HolderLookup.Provider registries = ClientUtils.getCurrentLevel().registryAccess();
            return BookHelper.getTitleText(makeBookView(pStack, registries));
        } else {
            return super.getName(pStack);
        }
    }
    
    @Override
    public void appendHoverText(ItemStack pStack, Item.TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pContext, pTooltipComponents, pIsAdvanced);
        if (FMLEnvironment.dist.isClient() && hasAuthor(pStack)) {
            Component authorText = BookHelper.getAuthorText(makeBookView(pStack, pContext.registries()), getAuthor(pStack));
            pTooltipComponents.add(Component.translatable("book.byAuthor", authorText).withStyle(ChatFormatting.GRAY));
        }
        getBookDefinition(pStack).ifPresent(defHolder -> {
            getBookLanguage(pStack).ifPresent(langHolder -> {
                pTooltipComponents.add(Component.translatable("tooltip.primalmagick.written_language.header", langHolder.get().getName()).withStyle(ChatFormatting.GRAY));
                pTooltipComponents.add(Component.translatable("book.generation." + getGeneration(pStack)).withStyle(ChatFormatting.GRAY));
                if (FMLEnvironment.dist.isClient() && hasBookDefinition(pStack) && hasBookLanguage(pStack) && langHolder.get().isComplex()) {
                    Player player = FMLEnvironment.dist.isClient() ? ClientUtils.getCurrentPlayer() : null;
                    Optional<Integer> translatedComprehension = getTranslatedComprehension(pStack);
                    int comprehension = Math.max(translatedComprehension.orElse(0), LinguisticsManager.getComprehension(player, langHolder));
                    double percentage = BookHelper.getBookComprehension(new BookView(Either.left(defHolder), langHolder, comprehension));
                    pTooltipComponents.add(Component.translatable("tooltip.primalmagick.written_language.comprehension", COMPREHENSION_FORMATTER.format(100 * percentage)).withStyle(ChatFormatting.GRAY));
                    if (translatedComprehension.isPresent()) {
                        if (translatedComprehension.get() >= langHolder.get().complexity()) {
                            pTooltipComponents.add(Component.translatable("tooltip.primalmagick.written_language.translated.full").withStyle(ChatFormatting.DARK_AQUA));
                        } else if (translatedComprehension.get() > 0) {
                            pTooltipComponents.add(Component.translatable("tooltip.primalmagick.written_language.translated.partial").withStyle(ChatFormatting.DARK_AQUA));
                        }
                    }
                    
                    int timesStudied = LinguisticsManager.getTimesStudied(player, defHolder, langHolder);
                    if (timesStudied > 0) {
                        pTooltipComponents.add(Component.translatable("tooltip.primalmagick.written_language.times_studied", timesStudied).withStyle(ChatFormatting.DARK_AQUA));
                    }
                }
            });
        });
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack stack = pPlayer.getItemInHand(pUsedHand);
        if (!pLevel.isClientSide && pPlayer instanceof ServerPlayer serverPlayer) {
            getBookDefinition(stack).ifPresentOrElse(bookDefHolder -> {
                getBookLanguage(stack).ifPresentOrElse(langHolder -> {
                    LinguisticsManager.markRead(pPlayer, bookDefHolder, langHolder);
                    PacketHandler.sendToPlayer(new OpenStaticBookScreenPacket(stack, this.bookType, pLevel.registryAccess()), serverPlayer);
                }, () -> {
                    LOGGER.error("No book language found when opening static book stack {}", stack.toString());
                });
            }, () -> {
                LOGGER.error("No book definition found when opening static book stack {}", stack.toString());
            });
        }
        return new InteractionResultHolder<ItemStack>(InteractionResult.SUCCESS, stack);
    }
    
    public static Builder builder(Supplier<StaticBookItem> baseBook, HolderLookup.Provider registries) {
        return new Builder(baseBook, registries);
    }
    
    public static class Builder {
        protected final HolderLookup.Provider registries;
        protected final Supplier<StaticBookItem> baseBook;
        protected Optional<ResourceKey<BookDefinition>> bookKeyOpt = Optional.empty();
        protected Optional<ResourceKey<BookLanguage>> langKeyOpt = Optional.empty();
        protected Optional<String> authorOpt = Optional.empty();
        protected Optional<Integer> generationOpt = Optional.empty();
        protected Optional<Integer> translationOpt = Optional.empty();
        
        protected Builder(Supplier<StaticBookItem> baseBook, HolderLookup.Provider registries) {
            this.baseBook = baseBook;
            this.registries = registries;
        }
        
        public Builder book(ResourceKey<BookDefinition> bookDef) {
            this.bookKeyOpt = Optional.ofNullable(bookDef);
            return this;
        }
        
        public Builder language(ResourceKey<BookLanguage> lang) {
            this.langKeyOpt = Optional.ofNullable(lang);
            return this;
        }
        
        public Builder author(String author) {
            this.authorOpt = Optional.ofNullable(author);
            return this;
        }
        
        public Builder generation(int gen) {
            this.generationOpt = Optional.of(gen);
            return this;
        }
        
        public Builder translation(int translation) {
            this.translationOpt = Optional.of(translation);
            return this;
        }
        
        public ItemStack build() {
            ItemStack retVal = new ItemStack(baseBook.get());
            this.bookKeyOpt.flatMap(bookKey -> this.registries.lookupOrThrow(RegistryKeysPM.BOOKS).get(bookKey)).ifPresent(bookDef -> {
                StaticBookItem.setBookDefinition(retVal, bookDef);
            });
            this.langKeyOpt.flatMap(langKey -> this.registries.lookupOrThrow(RegistryKeysPM.BOOK_LANGUAGES).get(langKey)).ifPresent(lang -> {
                StaticBookItem.setBookLanguage(retVal, lang);
            });
            this.authorOpt.ifPresent(author -> StaticBookItem.setAuthorOverride(retVal, author));
            this.generationOpt.ifPresent(gen -> StaticBookItem.setGeneration(retVal, gen));
            StaticBookItem.setTranslatedComprehension(retVal, this.translationOpt);
            return retVal;
        }
    }
}
