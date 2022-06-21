package com.verdantartifice.primalmagick.common.items.tools;

import com.verdantartifice.primalmagick.common.entities.projectiles.FishingHookEntity;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;

/**
 * Definition of a fishing rod made of a magickal metal.
 * 
 * @author Daedalus4096
 */
public class TieredFishingRodItem extends FishingRodItem {
    protected final Tier tier;
    
    public TieredFishingRodItem(Tier tier, Item.Properties properties) {
        super(properties.defaultDurability(tier.getUses() / 4));
        this.tier = tier;
    }
    
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (player.fishing != null) {
            if (!level.isClientSide) {
                int val = player.fishing.retrieve(stack);
                stack.hurtAndBreak(val, player, p -> {
                    p.broadcastBreakEvent(hand);
                });
            }
            level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.FISHING_BOBBER_RETRIEVE, SoundSource.NEUTRAL, 1.0F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
            player.gameEvent(GameEvent.ITEM_INTERACT_FINISH);
        } else {
            level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.FISHING_BOBBER_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
            if (!level.isClientSide) {
                int lure = EnchantmentHelper.getFishingSpeedBonus(stack);
                int luck = EnchantmentHelper.getFishingLuckBonus(stack);
                level.addFreshEntity(new FishingHookEntity(player, level, luck, lure));
            }
            player.awardStat(Stats.ITEM_USED.get(this));
            player.gameEvent(GameEvent.ITEM_INTERACT_START);
        }
        
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    public Tier getTier() {
        return this.tier;
    }

    @Override
    public int getEnchantmentValue() {
        return this.tier.getEnchantmentValue();
    }

    @Override
    public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
        return this.tier.getRepairIngredient().test(repair) || super.isValidRepairItem(toRepair, repair);
    }
}
