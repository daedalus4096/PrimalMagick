package com.verdantartifice.primalmagick.common.items.tools;

import com.verdantartifice.primalmagick.common.entities.projectiles.AbstractTridentEntity;
import com.verdantartifice.primalmagick.common.items.IHasCustomRenderer;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

/**
 * Base class definition of a craftable, repairable trident item made of a magickal metal.
 *  
 * @author Daedalus4096
 */
public abstract class AbstractTieredTridentItem extends TridentItem implements IHasCustomRenderer {
    protected final Tier tier;

    public AbstractTieredTridentItem(Tier tier, Item.Properties properties) {
        super(properties.durability(tier.getUses()).attributes(createAttributes(tier)).component(DataComponents.TOOL, createToolProperties()));
        this.tier = tier;
    }
    
    public static ItemAttributeModifiers createAttributes(Tier tier) {
        return ItemAttributeModifiers.builder()
            .add(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_ID, 7.0D + tier.getAttackDamageBonus(), AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
            .add(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_ID, -2.9D, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
            .build();
    }

    public static Tool createToolProperties() {
        return new Tool(List.of(), 1.0F, 2);
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

    protected abstract AbstractTridentEntity getThrownEntity(Level world, LivingEntity thrower, ItemStack stack);

    @Override
    public void releaseUsing(ItemStack stack, Level worldIn, LivingEntity entityLiving, int timeLeft) {
        if (entityLiving instanceof Player player) {
            int duration = this.getUseDuration(stack, entityLiving) - timeLeft;
            if (duration >= 10) {
                float riptide = EnchantmentHelper.getTridentSpinAttackStrength(stack, player);
                if (riptide <= 0F || player.isInWaterOrRain()) {
                    if (!isTooDamagedToUse(stack)) {
                        Holder<SoundEvent> soundHolder = EnchantmentHelper.pickHighestLevel(stack, EnchantmentEffectComponents.TRIDENT_SOUND).orElse(SoundEvents.TRIDENT_THROW);
                        if (!worldIn.isClientSide) {
                            stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(entityLiving.getUsedItemHand()));
                            if (riptide == 0) {
                                AbstractTridentEntity trident = this.getThrownEntity(worldIn, player, stack);
                                trident.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 2.5F + (float)riptide * 0.5F, 1.0F);
                                if (player.hasInfiniteMaterials()) {
                                    trident.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                                }
                                
                                worldIn.addFreshEntity(trident);
                                worldIn.playSound((Player)null, trident, soundHolder.value(), SoundSource.PLAYERS, 1.0F, 1.0F);
                                if (!player.hasInfiniteMaterials()) {
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
                            player.startAutoSpinAttack(20, 7.0F + this.tier.getAttackDamageBonus(), stack);
                            if (player.onGround()) {
                                player.move(MoverType.SELF, new Vec3(0.0D, 1.1999999D, 0.0D));
                            }
                            
                            worldIn.playSound((Player)null, player, soundHolder.value(), SoundSource.PLAYERS, 1.0F, 1.0F);
                        }
                    }
                }
            }
        }
    }
    
    private static boolean isTooDamagedToUse(ItemStack pStack) {
        return pStack.getDamageValue() >= pStack.getMaxDamage() - 1;
    }
}
