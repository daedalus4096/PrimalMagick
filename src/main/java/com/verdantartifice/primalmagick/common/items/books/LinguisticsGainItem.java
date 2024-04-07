package com.verdantartifice.primalmagick.common.items.books;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import com.verdantartifice.primalmagick.client.util.ClientUtils;
import com.verdantartifice.primalmagick.common.books.BookLanguage;
import com.verdantartifice.primalmagick.common.books.BookLanguagesPM;
import com.verdantartifice.primalmagick.common.books.CodexType;
import com.verdantartifice.primalmagick.common.books.LinguisticsManager;
import com.verdantartifice.primalmagick.common.network.PacketHandler;
import com.verdantartifice.primalmagick.common.network.packets.fx.PlayClientSoundPacket;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;
import com.verdantartifice.primalmagick.common.sounds.SoundsPM;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
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
import net.minecraftforge.fml.loading.FMLEnvironment;

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
    
    public static ItemStack make(CodexType type, ResourceKey<BookLanguage> bookLang) {
        ItemStack retVal = new ItemStack(TYPE_MAP.get(type));
        setBookLanguage(retVal, bookLang);
        return retVal;
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
    
    public static Optional<Holder.Reference<BookLanguage>> getBookLanguage(ItemStack stack, RegistryAccess registryAccess) {
        return getBookLanguageId(stack).flatMap(k -> registryAccess.registryOrThrow(RegistryKeysPM.BOOK_LANGUAGES).getHolder(k));
    }
    
    public static ItemStack setBookLanguage(ItemStack stack, ResourceKey<BookLanguage> lang) {
        stack.getOrCreateTag().putString(TAG_BOOK_LANGUAGE_ID, lang.location().toString());
        return stack;
    }

    @Override
    public ItemStack getDefaultInstance() {
        ItemStack retVal = super.getDefaultInstance();
        setBookLanguage(retVal, BookLanguagesPM.DEFAULT);
        return retVal;
    }

    @Override
    public Component getName(ItemStack pStack) {
        Component nameComponent = CommonComponents.EMPTY;
        Level level = FMLEnvironment.dist.isClient() ? ClientUtils.getCurrentLevel() : null;
        if (level != null) {
            Optional<Holder.Reference<BookLanguage>> langHolderOpt = getBookLanguage(pStack, level.registryAccess());
            if (langHolderOpt.isPresent()) {
                nameComponent = langHolderOpt.get().get().getName();
            }
        }
        return Component.translatable(this.getDescriptionId(), nameComponent);
    }

    @Override
    public void appendHoverText(ItemStack pStack, Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        Level level = FMLEnvironment.dist.isClient() ? ClientUtils.getCurrentLevel() : null;
        if (level != null) {
            getBookLanguage(pStack, level.registryAccess()).ifPresent(lang -> {
                if (lang.get().isComplex()) {
                    if (this.amount >= lang.get().complexity()) {
                        pTooltipComponents.add(Component.translatable("tooltip.primalmagick.codex.full", lang.get().getName()).withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
                    } else {
                        pTooltipComponents.add(Component.translatable("tooltip.primalmagick.codex.partial", lang.get().getName()).withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
                    }
                }
            });
        }
    }

    @Override
    public boolean isFoil(ItemStack pStack) {
        Level level = FMLEnvironment.dist.isClient() ? ClientUtils.getCurrentLevel() : null;
        if (level != null) {
            Optional<Holder.Reference<BookLanguage>> lang = getBookLanguage(pStack, level.registryAccess());
            return super.isFoil(pStack) || (lang.get().get().isComplex() && this.amount >= lang.get().get().complexity());
        }
        return super.isFoil(pStack);
    }
    
    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if (!pLevel.isClientSide) {
            if (pPlayer instanceof ServerPlayer serverPlayer) {
                PacketHandler.sendToPlayer(new PlayClientSoundPacket(SoundsPM.WRITING.get(), 1.0F, 1.0F + (float)pPlayer.getRandom().nextGaussian() * 0.05F), serverPlayer);
            }
            getBookLanguage(pPlayer.getItemInHand(pUsedHand), pLevel.registryAccess()).ifPresent(lang -> {
                if (LinguisticsManager.getComprehension(pPlayer, lang) >= lang.get().complexity()) {
                    pPlayer.displayClientMessage(Component.translatable("event.primalmagick.linguistics_item.fluent").withStyle(ChatFormatting.RED), true);
                } else {
                    LinguisticsManager.incrementComprehension(pPlayer, lang, this.amount);
                    pPlayer.displayClientMessage(Component.translatable("event.primalmagick.linguistics_item.success").withStyle(ChatFormatting.GREEN), true);
                    if (!pPlayer.getAbilities().instabuild) {
                        pPlayer.getItemInHand(pUsedHand).shrink(1);
                    }
                }
            });
        }
        return super.use(pLevel, pPlayer, pUsedHand);
    }

    public static void registerCreativeTabItems(CreativeModeTab.ItemDisplayParameters params, CreativeModeTab.Output output, Supplier<? extends ItemLike> itemSupplier) {
        Item item = itemSupplier.get().asItem();
        params.holders().lookup(RegistryKeysPM.BOOK_LANGUAGES).ifPresent(registryLookup -> {
            registryLookup.filterElements(BookLanguage::isComplex).listElements().map(langRef -> {
                return setBookLanguage(new ItemStack(item), langRef.key() );
            }).forEach(stack -> {
                output.accept(stack);
            });
        });
    }
}
