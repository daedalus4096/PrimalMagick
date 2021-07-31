package com.verdantartifice.primalmagic.common.items.food;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Iterables;
import com.verdantartifice.primalmagic.common.attunements.AttunementManager;
import com.verdantartifice.primalmagic.common.attunements.AttunementType;
import com.verdantartifice.primalmagic.common.research.ResearchManager;
import com.verdantartifice.primalmagic.common.research.SimpleResearchKey;
import com.verdantartifice.primalmagic.common.sources.Source;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Item definition for ambrosia.  Magical food that induces attunement to a primal source in the eater, to a limit.
 * 
 * @author Daedalus4096
 */
public class AmbrosiaItem extends Item {
    public static final List<AmbrosiaItem> AMBROSIAS = new ArrayList<>();
    protected static final int BONUS = 2;
    protected static final int PENALTY = -1;
    
    protected final Source source;
    protected final int limit;
    
    public AmbrosiaItem(Source source, int limit, Item.Properties properties) {
        super(properties);
        this.source = source;
        this.limit = limit;
        AMBROSIAS.add(this);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level worldIn, LivingEntity entityLiving) {
        if (!worldIn.isClientSide && (entityLiving instanceof Player)) {
            Player player = (Player)entityLiving;

            // Only modify attunements if the player has started mod progression
            if (ResearchManager.isResearchComplete(player, SimpleResearchKey.FIRST_STEPS)) {
                int limit = Math.min(this.limit, AttunementType.INDUCED.getMaximum());
                int current = AttunementManager.getAttunement(player, this.source, AttunementType.INDUCED);
                int toIncrement = Math.min(BONUS, limit - current);
                if (toIncrement > 0) {
                    for (Source source : Source.SORTED_SOURCES) {
                        if (source == this.source) {
                            AttunementManager.incrementAttunement(player, source, AttunementType.INDUCED, toIncrement);
                        } else {
                            AttunementManager.incrementAttunement(player, source, AttunementType.INDUCED, PENALTY);
                        }
                    }
                    player.displayClientMessage(new TranslatableComponent("event.primalmagic.ambrosia.success").withStyle(ChatFormatting.GREEN), true);
                } else {
                    player.displayClientMessage(new TranslatableComponent("event.primalmagic.ambrosia.failure").withStyle(ChatFormatting.RED), true);
                }
            } else {
                player.displayClientMessage(new TranslatableComponent("event.primalmagic.ambrosia.not_wizard").withStyle(ChatFormatting.RED), true);
            }
        }
        return super.finishUsingItem(stack, worldIn, entityLiving);
    }
    
    @OnlyIn(Dist.CLIENT)
    public int getColor(int tintIndex) {
        return tintIndex == 0 ? 0xFFFFFF : this.source.getColor();
    }
    
    public static Iterable<AmbrosiaItem> getAmbrosias() {
        return Iterables.unmodifiableIterable(AMBROSIAS);
    }
}
