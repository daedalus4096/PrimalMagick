package com.verdantartifice.primalmagick.common.items.books;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import com.verdantartifice.primalmagick.common.books.BookLanguage;
import com.verdantartifice.primalmagick.common.books.BookLanguagesPM;
import com.verdantartifice.primalmagick.common.books.CodexType;
import com.verdantartifice.primalmagick.common.books.LinguisticsManager;
import com.verdantartifice.primalmagick.common.network.PacketHandler;
import com.verdantartifice.primalmagick.common.network.packets.fx.PlayClientSoundPacket;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;
import com.verdantartifice.primalmagick.common.sounds.SoundsPM;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.StringUtil;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;

/**
 * Definition of an item that grants linguistics comprehension when used by a player.
 * 
 * @author Daedalus4096
 */
public class LinguisticsGainItem extends Item {
    public static final String TAG_BOOK_LANGUAGE_ID = "BookLanguageId";
    protected static final Map<CodexType, LinguisticsGainItem> TYPE_MAP = new HashMap<>();

    protected final int amount;

    public LinguisticsGainItem(CodexType type, Item.Properties properties) {
        super(properties);
        this.amount = type.getAmount();
        TYPE_MAP.put(type, this);
    }
    
    public static ItemStack make(CodexType type, Optional<BookLanguage> bookLangOpt) {
        ItemStack retVal = new ItemStack(TYPE_MAP.get(type));
        bookLangOpt.ifPresent(bookLang -> setBookLanguage(retVal, bookLang));
        return retVal;
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
    
    public static ItemStack setBookLanguage(ItemStack stack, BookLanguage lang) {
        stack.getOrCreateTag().putString(TAG_BOOK_LANGUAGE_ID, lang.languageId().toString());
        return stack;
    }

    @Override
    public ItemStack getDefaultInstance() {
        ItemStack retVal = super.getDefaultInstance();
        setBookLanguage(retVal, BookLanguagesPM.DEFAULT.get());
        return retVal;
    }

    @Override
    public Component getName(ItemStack pStack) {
        return Component.translatable(this.getDescriptionId(), getBookLanguage(pStack).getName());
    }

    @Override
    public void appendHoverText(ItemStack pStack, Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        BookLanguage lang = getBookLanguage(pStack);
        if (lang.isComplex()) {
            if (this.amount >= lang.complexity()) {
                pTooltipComponents.add(Component.translatable("tooltip.primalmagick.codex.full", lang.getName()).withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
            } else {
                pTooltipComponents.add(Component.translatable("tooltip.primalmagick.codex.partial", lang.getName()).withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
            }
        }
    }

    @Override
    public boolean isFoil(ItemStack pStack) {
        BookLanguage lang = getBookLanguage(pStack);
        return super.isFoil(pStack) || (lang.isComplex() && this.amount >= lang.complexity());
    }
    
    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if (!pLevel.isClientSide) {
            if (pPlayer instanceof ServerPlayer serverPlayer) {
                PacketHandler.sendToPlayer(new PlayClientSoundPacket(SoundsPM.WRITING.get(), 1.0F, 1.0F + (float)pPlayer.getRandom().nextGaussian() * 0.05F), serverPlayer);
            }
            BookLanguage lang = getBookLanguage(pPlayer.getItemInHand(pUsedHand));
            if (LinguisticsManager.getComprehension(pPlayer, lang) >= lang.complexity()) {
                pPlayer.displayClientMessage(Component.translatable("event.primalmagick.linguistics_item.fluent").withStyle(ChatFormatting.RED), true);
            } else {
                LinguisticsManager.incrementComprehension(pPlayer, lang, this.amount);
                pPlayer.displayClientMessage(Component.translatable("event.primalmagick.linguistics_item.success").withStyle(ChatFormatting.GREEN), true);
                if (!pPlayer.getAbilities().instabuild) {
                    pPlayer.getItemInHand(pUsedHand).shrink(1);
                }
            }
        }
        return super.use(pLevel, pPlayer, pUsedHand);
    }

    public static void registerCreativeTabItems(CreativeModeTab.ItemDisplayParameters params, CreativeModeTab.Output output, Supplier<? extends ItemLike> itemSupplier) {
        Item item = itemSupplier.get().asItem();
        params.holders().lookup(RegistryKeysPM.BOOK_LANGUAGES).ifPresent(registryLookup -> {
            registryLookup.filterElements(BookLanguage::isComplex).listElements().map(langRef -> {
                return setBookLanguage(new ItemStack(item), langRef.value());
            }).forEach(stack -> {
                output.accept(stack);
            });
        });
    }
}
