package com.verdantartifice.primalmagic.common.items.tools;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.verdantartifice.primalmagic.common.entities.projectiles.AbstractTridentEntity;
import com.google.common.collect.ImmutableMultimap.Builder;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TridentItem;
import net.minecraft.stats.Stats;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

/**
 * Base class definition of a craftable, repairable trident item made of a magical metal.
 *  
 * @author Daedalus4096
 */
public abstract class AbstractTieredTridentItem extends TridentItem {
    protected final IItemTier tier;
    protected final Multimap<Attribute, AttributeModifier> attributeModifiers;

    public AbstractTieredTridentItem(IItemTier tier, Item.Properties properties) {
        super(properties.defaultMaxDamage(tier.getMaxUses()));
        this.tier = tier;
        Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Tool modifier", 7.0D + tier.getAttackDamage(), AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(ATTACK_SPEED_MODIFIER, "Tool modifier", (double)-2.9F, AttributeModifier.Operation.ADDITION));
        this.attributeModifiers = builder.build();
    }
    
    public IItemTier getTier() {
        return this.tier;
    }

    @Override
    public int getItemEnchantability() {
        return this.tier.getEnchantability();
    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        return this.tier.getRepairMaterial().test(repair) || super.getIsRepairable(toRepair, repair);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType equipmentSlot) {
        return equipmentSlot == EquipmentSlotType.MAINHAND ? this.attributeModifiers : super.getAttributeModifiers(equipmentSlot);
    }
    
    protected abstract AbstractTridentEntity getThrownEntity(World world, LivingEntity thrower, ItemStack stack);

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft) {
        if (entityLiving instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity)entityLiving;
            int duration = this.getUseDuration(stack) - timeLeft;
            if (duration >= 10) {
                int riptide = EnchantmentHelper.getRiptideModifier(stack);
                if (riptide <= 0 || player.isWet()) {
                    if (!worldIn.isRemote) {
                        stack.damageItem(1, player, (p) -> {
                            p.sendBreakAnimation(entityLiving.getActiveHand());
                        });
                        if (riptide == 0) {
                            AbstractTridentEntity trident = this.getThrownEntity(worldIn, player, stack);
                            trident.setDirectionAndMovement(player, player.rotationPitch, player.rotationYaw, 0.0F, 2.5F + (float)riptide * 0.5F, 1.0F);
                            if (player.abilities.isCreativeMode) {
                                trident.pickupStatus = AbstractArrowEntity.PickupStatus.CREATIVE_ONLY;
                            }
                            
                            worldIn.addEntity(trident);
                            worldIn.playMovingSound((PlayerEntity)null, trident, SoundEvents.ITEM_TRIDENT_THROW, SoundCategory.PLAYERS, 1.0F, 1.0F);
                            if (!player.abilities.isCreativeMode) {
                                player.inventory.deleteStack(stack);
                            }
                        }
                    }
                    
                    player.addStat(Stats.ITEM_USED.get(this));
                    if (riptide > 0) {
                        float f7 = player.rotationYaw;
                        float f = player.rotationPitch;
                        float f1 = -MathHelper.sin(f7 * ((float)Math.PI / 180F)) * MathHelper.cos(f * ((float)Math.PI / 180F));
                        float f2 = -MathHelper.sin(f * ((float)Math.PI / 180F));
                        float f3 = MathHelper.cos(f7 * ((float)Math.PI / 180F)) * MathHelper.cos(f * ((float)Math.PI / 180F));
                        float f4 = MathHelper.sqrt(f1 * f1 + f2 * f2 + f3 * f3);
                        float f5 = 3.0F * ((1.0F + (float)riptide) / 4.0F);
                        f1 = f1 * (f5 / f4);
                        f2 = f2 * (f5 / f4);
                        f3 = f3 * (f5 / f4);
                        player.addVelocity((double)f1, (double)f2, (double)f3);
                        player.startSpinAttack(20);
                        if (player.isOnGround()) {
                            player.move(MoverType.SELF, new Vector3d(0.0D, 1.1999999D, 0.0D));
                        }
                        
                        SoundEvent sound;
                        if (riptide >= 3) {
                            sound = SoundEvents.ITEM_TRIDENT_RIPTIDE_3;
                        } else if (riptide == 2) {
                            sound = SoundEvents.ITEM_TRIDENT_RIPTIDE_2;
                        } else {
                            sound = SoundEvents.ITEM_TRIDENT_RIPTIDE_1;
                        }
                        
                        worldIn.playMovingSound((PlayerEntity)null, player, sound, SoundCategory.PLAYERS, 1.0F, 1.0F);
                    }
                }
            }
        }
    }
}
