package com.verdantartifice.primalmagic.common.items.food;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Iterables;
import com.verdantartifice.primalmagic.common.attunements.AttunementManager;
import com.verdantartifice.primalmagic.common.attunements.AttunementType;
import com.verdantartifice.primalmagic.common.research.ResearchManager;
import com.verdantartifice.primalmagic.common.research.SimpleResearchKey;
import com.verdantartifice.primalmagic.common.sources.Source;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
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
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
        if (!worldIn.isRemote && (entityLiving instanceof PlayerEntity)) {
            PlayerEntity player = (PlayerEntity)entityLiving;

            // Only modify attunements if the player has started mod progression
            if (ResearchManager.isResearchComplete(player, SimpleResearchKey.parse("FIRST_STEPS"))) {
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
                    player.sendStatusMessage(new TranslationTextComponent("event.primalmagic.ambrosia.success").mergeStyle(TextFormatting.GREEN), true);
                } else {
                    player.sendStatusMessage(new TranslationTextComponent("event.primalmagic.ambrosia.failure").mergeStyle(TextFormatting.RED), true);
                }
            } else {
                player.sendStatusMessage(new TranslationTextComponent("event.primalmagic.ambrosia.not_wizard").mergeStyle(TextFormatting.RED), true);
            }
        }
        return super.onItemUseFinish(stack, worldIn, entityLiving);
    }
    
    @OnlyIn(Dist.CLIENT)
    public int getColor(int tintIndex) {
        return tintIndex == 0 ? 0xFFFFFF : this.source.getColor();
    }
    
    public static Iterable<AmbrosiaItem> getAmbrosias() {
        return Iterables.unmodifiableIterable(AMBROSIAS);
    }
}
