package com.verdantartifice.primalmagick.common.items.tools;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableMultimap.Builder;
import com.google.common.collect.Multimap;
import com.verdantartifice.primalmagick.common.entities.projectiles.AbstractTridentEntity;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

/**
 * Base class definition of a craftable, repairable trident item made of a magickal metal.
 *  
 * @author Daedalus4096
 */
public abstract class AbstractTieredTridentItem extends TridentItem {
    protected final Tier tier;
    protected final Multimap<Attribute, AttributeModifier> attributeModifiers;

    public AbstractTieredTridentItem(Tier tier, Item.Properties properties) {
        super(properties.defaultDurability(tier.getUses()));
        this.tier = tier;
        Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", 7.0D + tier.getAttackDamageBonus(), AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier", (double)-2.9F, AttributeModifier.Operation.ADDITION));
        this.attributeModifiers = builder.build();
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

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot equipmentSlot) {
        return equipmentSlot == EquipmentSlot.MAINHAND ? this.attributeModifiers : super.getDefaultAttributeModifiers(equipmentSlot);
    }
    
    protected abstract AbstractTridentEntity getThrownEntity(Level world, LivingEntity thrower, ItemStack stack);

    @Override
    public void releaseUsing(ItemStack stack, Level worldIn, LivingEntity entityLiving, int timeLeft) {
        if (entityLiving instanceof Player) {
            Player player = (Player)entityLiving;
            int duration = this.getUseDuration(stack) - timeLeft;
            if (duration >= 10) {
                int riptide = EnchantmentHelper.getRiptide(stack);
                if (riptide <= 0 || player.isInWaterOrRain()) {
                    if (!worldIn.isClientSide) {
                        stack.hurtAndBreak(1, player, (p) -> {
                            p.broadcastBreakEvent(entityLiving.getUsedItemHand());
                        });
                        if (riptide == 0) {
                            AbstractTridentEntity trident = this.getThrownEntity(worldIn, player, stack);
                            trident.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 2.5F + (float)riptide * 0.5F, 1.0F);
                            if (player.getAbilities().instabuild) {
                                trident.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                            }
                            
                            worldIn.addFreshEntity(trident);
                            worldIn.playSound((Player)null, trident, SoundEvents.TRIDENT_THROW, SoundSource.PLAYERS, 1.0F, 1.0F);
                            if (!player.getAbilities().instabuild) {
                                player.getInventory().removeItem(stack);
                            }
                        }
                    }
                    
                    player.awardStat(Stats.ITEM_USED.get(this));
                    if (riptide > 0) {
                        float f7 = player.getYRot();
                        float f = player.getXRot();
                        float f1 = -Mth.sin(f7 * ((float)Math.PI / 180F)) * Mth.cos(f * ((float)Math.PI / 180F));
                        float f2 = -Mth.sin(f * ((float)Math.PI / 180F));
                        float f3 = Mth.cos(f7 * ((float)Math.PI / 180F)) * Mth.cos(f * ((float)Math.PI / 180F));
                        float f4 = Mth.sqrt(f1 * f1 + f2 * f2 + f3 * f3);
                        float f5 = 3.0F * ((1.0F + (float)riptide) / 4.0F);
                        f1 = f1 * (f5 / f4);
                        f2 = f2 * (f5 / f4);
                        f3 = f3 * (f5 / f4);
                        player.push((double)f1, (double)f2, (double)f3);
                        player.startAutoSpinAttack(20);
                        if (player.onGround()) {
                            player.move(MoverType.SELF, new Vec3(0.0D, 1.1999999D, 0.0D));
                        }
                        
                        SoundEvent sound;
                        if (riptide >= 3) {
                            sound = SoundEvents.TRIDENT_RIPTIDE_3;
                        } else if (riptide == 2) {
                            sound = SoundEvents.TRIDENT_RIPTIDE_2;
                        } else {
                            sound = SoundEvents.TRIDENT_RIPTIDE_1;
                        }
                        
                        worldIn.playSound((Player)null, player, sound, SoundSource.PLAYERS, 1.0F, 1.0F);
                    }
                }
            }
        }
    }
}
