package com.verdantartifice.primalmagick.common.items.wands;

import com.verdantartifice.primalmagick.client.fx.FxDispatcher;
import com.verdantartifice.primalmagick.client.util.ClientUtils;
import com.verdantartifice.primalmagick.common.capabilities.ManaStorage;
import com.verdantartifice.primalmagick.common.components.DataComponentsPM;
import com.verdantartifice.primalmagick.common.crafting.IWandTransform;
import com.verdantartifice.primalmagick.common.crafting.WandTransforms;
import com.verdantartifice.primalmagick.common.items.IHasCustomRenderer;
import com.verdantartifice.primalmagick.common.research.ResearchEntries;
import com.verdantartifice.primalmagick.common.research.ResearchManager;
import com.verdantartifice.primalmagick.common.research.keys.ResearchEntryKey;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.sources.SourceList;
import com.verdantartifice.primalmagick.common.sources.Sources;
import com.verdantartifice.primalmagick.common.spells.SpellManager;
import com.verdantartifice.primalmagick.common.spells.SpellPackage;
import com.verdantartifice.primalmagick.common.wands.IInteractWithWand;
import com.verdantartifice.primalmagick.common.wands.IWand;
import com.verdantartifice.primalmagick.common.wands.ManaManager;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Base item definition for a wand.  Wands store mana for use in crafting and, optionally, casting spells.
 * They are replenished by drawing from mana fonts or being charged in a wand charger.  The wand's mana is
 * stored internally as centimana (hundredths of mana points).
 * 
 * @author Daedalus4096
 */
public abstract class AbstractWandItem extends Item implements IWand, IHasCustomRenderer {
    protected static final DecimalFormat MANA_FORMATTER = new DecimalFormat("#######.##");
    protected static final ResearchEntryKey WAND_TRANSFORM_HINT_KEY = new ResearchEntryKey(ResearchEntries.WAND_TRANSFORM_HINT);
    
    public AbstractWandItem(Properties properties) {
        super(properties);
    }

    @Deprecated(forRemoval = true, since = "6.0.2-beta")
    @SuppressWarnings("removal")
    public ManaStorage getManaStorage(ItemStack stack) {
        // FIXME Remove in next major revision
        // If the wand already has a mana storage capability attached, return it. Otherwise, convert the stack from the
        // old component type to the new one and then return the new one.
        if (stack.has(DataComponentsPM.CAPABILITY_MANA_STORAGE.get())) {
            return stack.get(DataComponentsPM.CAPABILITY_MANA_STORAGE.get());
        } else {
            ManaStorage retVal = ManaStorage.emptyWand(this.getMaxMana(stack, null));
            retVal.setMana(stack.getOrDefault(DataComponentsPM.STORED_CENTIMANA.get(), SourceList.EMPTY));
            stack.set(DataComponentsPM.CAPABILITY_MANA_STORAGE.get(), retVal);
            stack.remove(DataComponentsPM.STORED_CENTIMANA.get());
            return retVal;
        }
    }

    @Deprecated(forRemoval = true, since = "6.0.2-beta")
    @SuppressWarnings("removal")
    private void updateManaStorageWith(ItemStack stack, Source source, int amount) {
        // FIXME Remove in next major revision
        // If the wand already has a mana storage capability attached, update it. Otherwise, convert the stack from the
        // old component type to the new one and then update the new one.
        if (stack.has(DataComponentsPM.CAPABILITY_MANA_STORAGE.get())) {
            stack.update(DataComponentsPM.CAPABILITY_MANA_STORAGE.get(), ManaStorage.EMPTY, mana -> mana.copyWith(source, amount));
            stack.set(DataComponentsPM.LAST_UPDATED.get(), System.currentTimeMillis());   // FIXME Is there a better way of marking this stack as dirty?
        } else {
            ManaStorage newStorage = ManaStorage.emptyWand(this.getMaxMana(stack, source));
            newStorage.setMana(stack.getOrDefault(DataComponentsPM.STORED_CENTIMANA.get(), SourceList.EMPTY));
            newStorage.setMana(source, amount);
            stack.set(DataComponentsPM.CAPABILITY_MANA_STORAGE.get(), newStorage);
            stack.remove(DataComponentsPM.STORED_CENTIMANA.get());
        }
    }

    @Override
    public void setMana(@NotNull ItemStack stack, @NotNull Source source, int amount) {
        // Save the given amount of centimana for the given source into the stack's data
        this.updateManaStorageWith(stack, source, amount);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, context, tooltip, flagIn);
        
        Player player = Services.PLATFORM.isClientDist() ? ClientUtils.getCurrentPlayer() : null;
        boolean showDetails = Services.PLATFORM.isClientDist() && ClientUtils.hasShiftDown();
        if (showDetails) {
            // Add detailed mana information
            for (Source source : Sources.getAllSorted()) {
                // Only include a mana source in the listing if it's been discovered
                if (source.isDiscovered(player)) {
                    Component nameComp = source.getNameText();
                    int modifier = this.getTotalCostModifier(stack, player, source, context.registries());
                    Component line = Component.translatable("tooltip.primalmagick.source.mana", nameComp, this.getManaText(stack, source, false), this.getMaxManaText(stack, source), modifier);
                    tooltip.add(line);
                }
            }
            
            // Add inscribed spell listing
            SpellManager.appendSpellListingText(player, stack, context, tooltip);
        } else {
            // Add mana summary
            boolean first = true;
            Component summaryText = Component.literal("");
            for (Source source : Sources.getAllSorted()) {
                // Only include a mana source in the summary if it's been discovered
                if (source.isDiscovered(player)) {
                    Component manaText = this.getManaText(stack, source, true).withStyle(source.getChatColor());
                    if (first) {
                        summaryText = manaText;
                    } else {
                        summaryText = Component.translatable("tooltip.primalmagick.source.mana_summary_fragment", summaryText, manaText);
                    }
                    first = false;
                }
            }
            tooltip.add(summaryText);
            
            // Add active spell
            SpellManager.appendActiveSpellText(stack, tooltip);
            
            // Add more info tooltip
            tooltip.add(Component.translatable("tooltip.primalmagick.more_info").withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY));
        }
    }
    
    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }
    
    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 72000;
    }
    
    @Override
    public BlockPos getPositionInUse(ItemStack wandStack) {
        // Look up the world coordinates of the wand-interactable tile entity currently in use from data components
        return wandStack.get(DataComponentsPM.WAND_USE_POSITION.get());
    }
    
    @Override
    public void setPositionInUse(ItemStack wandStack, BlockPos pos) {
        // Save the position of the wand-interactable tile entity so it can be looked up later
        wandStack.set(DataComponentsPM.WAND_USE_POSITION.get(), pos.immutable());
    }
    
    @Override
    public void clearPositionInUse(ItemStack wandStack) {
        wandStack.remove(DataComponentsPM.WAND_USE_POSITION.get());
    }
    
    protected static boolean isTargetWandInteractable(Level level, Player player, HitResult hit) {
        if (hit != null && hit.getType() == HitResult.Type.BLOCK && hit instanceof BlockHitResult blockHit) {
            BlockPos pos = blockHit.getBlockPos();
            if (level.getBlockState(pos).getBlock() instanceof IInteractWithWand || level.getBlockEntity(pos) instanceof IInteractWithWand) {
                return true;
            }
            return WandTransforms.getAll().stream().anyMatch(t -> t.isValid(level, player, pos));
        } else {
            return false;
        }
    }
    
    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        ItemStack stack = playerIn.getItemInHand(handIn);
        playerIn.startUsingItem(handIn);
        SpellPackage activeSpell = SpellManager.getActiveSpell(playerIn.getMainHandItem(), playerIn.getOffhandItem());
        if (activeSpell != null && !SpellManager.isOnCooldown(playerIn)) {
            // If the wand has an active spell and spells are off the player's cooldown, attempt to cast the spell on right-click
            SpellManager.setCooldown(playerIn, activeSpell.getCooldownTicks());
            if (worldIn.isClientSide) {
                return InteractionResultHolder.success(stack);
            } else {
                HitResult hit = getPlayerPOVHitResult(worldIn, playerIn, ClipContext.Fluid.SOURCE_ONLY);
                if (isTargetWandInteractable(worldIn, playerIn, hit)) {
                    // If the current mouseover target in range has special interaction with wands, then suppress the spell cast
                    return InteractionResultHolder.pass(stack);
                } else if (ManaManager.consumeMana(playerIn, stack, activeSpell.getManaCost(), worldIn.registryAccess())) {
                    // If the wand contains enough mana, consume it and cast the spell
                    activeSpell.cast(worldIn, playerIn, stack);
                    playerIn.swing(handIn);
                    return InteractionResultHolder.success(stack);
                } else {
                    return InteractionResultHolder.fail(stack);
                }
            }
        } else {
            return InteractionResultHolder.pass(stack);
        }
    }
    
    public abstract InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context);

    @Override
    public void onUseTick(Level level, LivingEntity living, ItemStack stack, int count) {
        // If the player continues to hold the interact button, continue the interaction with the last wand-sensitive block/tile interacted with
        if (living instanceof Player player) {
            BlockPos wandPos = this.getPositionInUse(stack);
            if (wandPos != null && level.getBlockEntity(wandPos) instanceof IInteractWithWand wandable) {
                Vec3 playerPos = player.position().add(0.0D, player.getEyeHeight() / 2.0D, 0.0D);
                wandable.onWandUseTick(stack, level, player, playerPos, count);
            } else if (wandPos != null) {
                for (IWandTransform transform : WandTransforms.getAll()) {
                    if (transform.isValid(level, player, wandPos)) {
                        if (level.isClientSide) {
                            // Trigger visual effects during channel
                            FxDispatcher.INSTANCE.spellImpact(wandPos.getX() + 0.5D, wandPos.getY() + 0.5D, wandPos.getZ() + 0.5D, 2, Sources.HALLOWED.getColor());
                        }
                        if (this.getUseDuration(stack, living) - count >= WandTransforms.CHANNEL_DURATION) {
                            if (!level.isClientSide) {
                                // Only execute the transform on the server side
                                transform.execute(level, player, wandPos);
                            }
                            break;
                        }
                    }
                }
            }
        }
    }
    
    @Override
    public void releaseUsing(ItemStack stack, Level worldIn, LivingEntity entityLiving, int timeLeft) {
        super.releaseUsing(stack, worldIn, entityLiving, timeLeft);
        
        // Give a hint the first time the player aborts a wand transform early
        BlockPos wandPos = this.getPositionInUse(stack);
        if (wandPos != null && !worldIn.isClientSide && entityLiving instanceof Player player && !WAND_TRANSFORM_HINT_KEY.isKnownBy(player)) {
            for (IWandTransform transform : WandTransforms.getAll()) {
                if (transform.isValid(worldIn, player, wandPos) && this.getUseDuration(stack, entityLiving) - timeLeft < WandTransforms.CHANNEL_DURATION) {
                    ResearchManager.completeResearch(player, WAND_TRANSFORM_HINT_KEY);
                    player.sendSystemMessage(Component.translatable("event.primalmagick.wand_transform_hint").withStyle(ChatFormatting.GREEN));
                    break;
                }
            }
        }

        // Once interaction ceases, clear the last-interacted coordinates
        this.clearPositionInUse(stack);
    }
}
