package com.verdantartifice.primalmagick.common.items.misc;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import com.verdantartifice.primalmagick.client.util.ClientUtils;
import com.verdantartifice.primalmagick.common.attunements.AttunementManager;
import com.verdantartifice.primalmagick.common.sources.Source;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;

/**
 * Item definition for attunement shackles.  An item that, when carried in a player's inventory, will
 * suppress the effects of any lesser or greater attunements they may have achieved.  Minor attunements
 * still function.
 * 
 * @author Daedalus4096
 */
public class AttunementShacklesItem extends Item {
    protected static final Map<Source, AttunementShacklesItem> SHACKLES = new HashMap<>();
    
    protected final Source source;
    
    public AttunementShacklesItem(Source source, Item.Properties properties) {
        super(properties);
        this.source = source;
        SHACKLES.put(source, this);
    }
    
    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if (!pLevel.isClientSide) {
            AttunementManager.setSuppressed(pPlayer, this.source, !AttunementManager.isSuppressed(pPlayer, this.source));
        }
        return super.use(pLevel, pPlayer, pUsedHand);
    }

    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, level, tooltip, flagIn);
        
        Player player = (FMLEnvironment.dist == Dist.CLIENT) ? ClientUtils.getCurrentPlayer() : null;
        if (AttunementManager.isSuppressed(player, this.source)) {
            tooltip.add(Component.translatable("tooltip.primalmagick.active").withStyle(ChatFormatting.GREEN));
        } else {
            tooltip.add(Component.translatable("tooltip.primalmagick.inactive").withStyle(ChatFormatting.RED));
        }
    }

    public int getColor(int tintIndex) {
        return tintIndex == 0 ? 0xFFFFFF : this.source.getColor();
    }
    
    @Nullable
    public static AttunementShacklesItem getShackles(Source source) {
        return SHACKLES.getOrDefault(source, null);
    }
    
    public static Collection<AttunementShacklesItem> getAllShackles() {
        return Collections.unmodifiableCollection(SHACKLES.values());
    }
}
