package com.verdantartifice.primalmagick.mixin;

import com.verdantartifice.primalmagick.common.enchantments.EnchantmentsPM;
import com.verdantartifice.primalmagick.common.tags.DamageTypeTagsPM;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.EnchantedCountIncreaseFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

/**
 * Mixin to allow the Treasure enchantment to function as if it were Looting when calculating loot resulting from
 * sorcery damage done with a wand or staff, or from a melee attack with a staff.
 *
 * @author Daedalus4096
 */
@Mixin(EnchantedCountIncreaseFunction.class)
public abstract class EnchantedCountIncreaseFunctionMixin extends LootItemConditionalFunction {
    @Shadow
    @Final
    private Holder<Enchantment> enchantment;

    @Shadow
    @Final
    private NumberProvider value;

    @Shadow
    @Final
    private int limit;

    @Shadow
    private boolean hasLimit() {
        return false;
    }

    protected EnchantedCountIncreaseFunctionMixin(List<LootItemCondition> pPredicates) {
        super(pPredicates);
    }

    @Inject(method = "run", at = @At("HEAD"), cancellable = true)
    protected void onRun(ItemStack pStack, LootContext pContext, CallbackInfoReturnable<ItemStack> cir) {
        DamageSource damageSource = pContext.getParamOrNull(LootContextParams.DAMAGE_SOURCE);
        if (this.enchantment.is(Enchantments.LOOTING) &&
                damageSource != null &&
                (damageSource.is(DamageTypeTagsPM.IS_SORCERY) || damageSource.is(DamageTypeTags.IS_PLAYER_ATTACK)) &&
                pContext.getParamOrNull(LootContextParams.ATTACKING_ENTITY) instanceof LivingEntity livingEntity) {
            // Use the better of Treasure or Looting when doing wand or staff damage
            Holder<Enchantment> treasureHolder = pContext.getResolver().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(EnchantmentsPM.TREASURE);
            int enchantmentLevel = Math.max(EnchantmentHelper.getEnchantmentLevel(this.enchantment, livingEntity),
                    EnchantmentHelper.getEnchantmentLevel(treasureHolder, livingEntity));
            if (enchantmentLevel > 0) {
                float f = (float)enchantmentLevel * this.value.getFloat(pContext);
                pStack.grow(Math.round(f));
                if (this.hasLimit()) {
                    pStack.limitSize(this.limit);
                }
            }
            cir.setReturnValue(pStack);
        }
    }
}
