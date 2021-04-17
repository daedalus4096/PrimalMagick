package com.verdantartifice.primalmagic.common.items.wands;

import java.text.DecimalFormat;
import java.util.List;

import javax.annotation.Nonnull;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.attunements.AttunementManager;
import com.verdantartifice.primalmagic.common.attunements.AttunementThreshold;
import com.verdantartifice.primalmagic.common.attunements.AttunementType;
import com.verdantartifice.primalmagic.common.crafting.IWandTransform;
import com.verdantartifice.primalmagic.common.crafting.WandTransforms;
import com.verdantartifice.primalmagic.common.effects.EffectsPM;
import com.verdantartifice.primalmagic.common.enchantments.EnchantmentsPM;
import com.verdantartifice.primalmagic.common.items.armor.IManaDiscountGear;
import com.verdantartifice.primalmagic.common.sources.Source;
import com.verdantartifice.primalmagic.common.sources.SourceList;
import com.verdantartifice.primalmagic.common.spells.SpellManager;
import com.verdantartifice.primalmagic.common.spells.SpellPackage;
import com.verdantartifice.primalmagic.common.stats.StatsManager;
import com.verdantartifice.primalmagic.common.wands.IInteractWithWand;
import com.verdantartifice.primalmagic.common.wands.IWand;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.UseAction;
import net.minecraft.nbt.IntNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Base item definition for a wand.  Wands store mana for use in crafting and, optionally, casting spells.
 * They are replenished by drawing from mana fonts or being charged in a wand charger.  The wand's mana is
 * stored internally as centimana (hundredths of mana points), though most mana manipulation methods deal
 * in "real" mana, not centimana.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractWandItem extends Item implements IWand {
    protected static final DecimalFormat MANA_FORMATTER = new DecimalFormat("#######.##");
    
    public AbstractWandItem(Properties properties) {
        super(properties);
    }

    @Override
    public int getMana(ItemStack stack, Source source) {
        if (this.getMaxMana(stack) == -1) {
            // If the given wand stack has infinite mana, return that
            return -1;
        } else {
            // Otherwise get the current centimana for that source from the stack's NBT tag
            int retVal = 0;
            if (stack != null && source != null && stack.hasTag() && stack.getTag().contains(source.getTag())) {
                retVal = stack.getTag().getInt(source.getTag());
            }
            return retVal;
        }
    }
    
    protected StringTextComponent getManaText(ItemStack stack, Source source) {
        int mana = this.getMana(stack, source);
        if (mana == -1) {
            // If the given wand stack has infinte mana, show the infinity symbol
            return new StringTextComponent(Character.toString('\u221E'));
        } else {
            // Otherwise show the current real mana for that source from the stack's NBT tag
            return new StringTextComponent(MANA_FORMATTER.format(mana / 100.0D));
        }
    }

    @Override
    public SourceList getAllMana(ItemStack stack) {
        SourceList retVal = new SourceList();
        boolean isInfinite = this.getMaxMana(stack) == -1;
        for (Source source : Source.SORTED_SOURCES) {
            if (isInfinite) {
                // If the stack has infinite mana, set that into the returned source list (not merge; it would keep the default zero)
                retVal.set(source, -1);
            } else if (stack.hasTag() && stack.getTag().contains(source.getTag())) {
                // Otherwise, merge the current centimana into the returned source list
                retVal.merge(source, stack.getTag().getInt(source.getTag()));
            } else {
                retVal.merge(source, 0);
            }
        }
        return retVal;
    }

    protected StringTextComponent getMaxManaText(ItemStack stack) {
        int mana = this.getMaxMana(stack);
        if (mana == -1) {
            // If the given wand stack has infinte mana, show the infinity symbol
            return new StringTextComponent(Character.toString('\u221E'));
        } else {
            // Otherwise show the max centimana for that source from the stack's NBT tag
            return new StringTextComponent(MANA_FORMATTER.format(mana / 100.0D));
        }
    }
    
    protected void setMana(@Nonnull ItemStack stack, @Nonnull Source source, int amount) {
        // Save the given amount of centimana for the given source into the stack's NBT tag
        stack.setTagInfo(source.getTag(), IntNBT.valueOf(amount));
    }

    @Override
    public int addRealMana(ItemStack stack, Source source, int amount) {
        // If the parameters are invalid or the given wand stack has infinite mana, do nothing
        if (stack == null || source == null || this.getMaxMana(stack) == -1) {
            return 0;
        }
        
        // Otherwise, increment and set the new real mana total for the source into the wand's data, returning
        // any leftover real mana that wouldn't fit
        int toStore = this.getMana(stack, source) + (amount * 100);
        int leftover = Math.max(toStore - this.getMaxMana(stack), 0);
        this.setMana(stack, source, Math.min(toStore, this.getMaxMana(stack)));
        return leftover;
    }

    @Override
    public boolean consumeMana(ItemStack stack, PlayerEntity player, Source source, int amount) {
        if (stack == null || source == null) {
            return false;
        }
        if (this.containsMana(stack, player, source, amount)) {
            // If the wand stack contains enough mana, process the consumption and return success
            if (this.getMaxMana(stack) != -1) {
                // Only actually consume something if the wand doesn't have infinite mana
                this.setMana(stack, source, this.getMana(stack, source) - (int)(this.getTotalCostModifier(stack, player, source) * amount));
            }
            
            if (player != null) {
                int realAmount = amount / 100;
                
                // Record the spent mana statistic change with pre-discount mana
                StatsManager.incrementValue(player, source.getManaSpentStat(), realAmount);
                
                // Update temporary attunement value
                AttunementManager.incrementAttunement(player, source, AttunementType.TEMPORARY, MathHelper.floor(Math.sqrt(realAmount)));
            }
            
            return true;
        } else {
            // Otherwise return failure
            return false;
        }
    }

    @Override
    public boolean consumeMana(ItemStack stack, PlayerEntity player, SourceList sources) {
        if (stack == null || sources == null) {
            return false;
        }
        if (this.containsRealMana(stack, player, sources)) {
            // If the wand stack contains enough mana, process the consumption and return success
            boolean isInfinite = (this.getMaxMana(stack) == -1);
            SourceList attunementDeltas = new SourceList();
            for (Source source : sources.getSources()) {
                int amount = sources.getAmount(source);
                int realAmount = amount / 100;
                if (!isInfinite) {
                    // Only actually consume something if the wand doesn't have infinite mana
                    this.setMana(stack, source, this.getMana(stack, source) - (int)(this.getTotalCostModifier(stack, player, source) * amount));
                }
                
                if (player != null) {
                    // Record the spent mana statistic change with pre-discount mana
                    StatsManager.incrementValue(player, source.getManaSpentStat(), realAmount);
                }
                
                // Compute the amount of temporary attunement to be added to the player
                attunementDeltas.add(source, MathHelper.floor(Math.sqrt(realAmount)));
            }
            if (player != null && !attunementDeltas.isEmpty()) {
                // Update attunements in a batch
                AttunementManager.incrementAttunement(player, AttunementType.TEMPORARY, attunementDeltas);
            }
            return true;
        } else {
            // Otherwise return failure
            return false;
        }
    }

    @Override
    public boolean consumeRealMana(ItemStack stack, PlayerEntity player, Source source, int amount) {
        return this.consumeMana(stack, player, source, amount * 100);
    }
    
    @Override
    public boolean consumeRealMana(ItemStack stack, PlayerEntity player, SourceList sources) {
        return this.consumeMana(stack, player, sources.copy().multiply(100));
    }
    
    @Override
    public boolean containsMana(ItemStack stack, PlayerEntity player, Source source, int amount) {
        // A wand stack with infinite mana always contains the requested amount of mana
        return this.getMaxMana(stack) == -1 || this.getMana(stack, source) >= (this.getTotalCostModifier(stack, player, source) * amount);
    }

    @Override
    public boolean containsMana(ItemStack stack, PlayerEntity player, SourceList sources) {
        for (Source source : sources.getSources()) {
            if (!this.containsMana(stack, player, source, sources.getAmount(source))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean containsRealMana(ItemStack stack, PlayerEntity player, Source source, int amount) {
        return this.containsMana(stack, player, source, amount * 100);
    }
    
    @Override
    public boolean containsRealMana(ItemStack stack, PlayerEntity player, SourceList sources) {
        return this.containsMana(stack, player, sources.copy().multiply(100));
    }
    
    @Override
    public double getTotalCostModifier(ItemStack stack, PlayerEntity player, Source source) {
        // Start with the base modifier, as determined by wand cap
        double modifier = this.getBaseCostModifier(stack);
        
        // Subtract discounts from wand enchantments
        int efficiencyLevel = EnchantmentHelper.getEnchantmentLevel(EnchantmentsPM.MANA_EFFICIENCY.get(), stack);
        if (efficiencyLevel > 0) {
            modifier -= (0.02D * efficiencyLevel);
        }
        
        if (player != null) {
            // Subtract discounts from equipped player gear
            int gearDiscount = 0;
            for (ItemStack gearStack : player.getEquipmentAndArmor()) {
                if (gearStack.getItem() instanceof IManaDiscountGear) {
                    gearDiscount += ((IManaDiscountGear)gearStack.getItem()).getManaDiscount(gearStack, player);
                }
            }
            if (gearDiscount > 0) {
                modifier -= (0.01D * gearDiscount);
            }
            
            // Subtract discounts from attuned sources
            if (AttunementManager.meetsThreshold(player, source, AttunementThreshold.MINOR)) {
                modifier -= 0.05D;
            }
            
            // Substract discounts from temporary conditions
            if (player.isPotionActive(EffectsPM.MANAFRUIT.get())) {
                // 1% at amp 0, 3% at amp 1, 5% at amp 2, etc
                modifier -= (0.01D * ((2 * player.getActivePotionEffect(EffectsPM.MANAFRUIT.get()).getAmplifier()) + 1));
            }
            
            // Add penalties from temporary conditions
            if (player.isPotionActive(EffectsPM.MANA_IMPEDANCE.get())) {
                // 5% at amp 0, 10% at amp 1, 15% at amp 2, etc
                modifier += (0.05D * (player.getActivePotionEffect(EffectsPM.MANA_IMPEDANCE.get()).getAmplifier() + 1));
            }
        }
        
        return modifier;
    }
    
    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        
        Minecraft mc = Minecraft.getInstance();
        PlayerEntity player = mc.player;
        
        if (PrimalMagic.proxy.isShiftDown()) {
            // Add detailed mana information
            for (Source source : Source.SORTED_SOURCES) {
                // Only include a mana source in the listing if it's been discovered
                if (source.isDiscovered(player)) {
                    ITextComponent nameComp = new TranslationTextComponent(source.getNameTranslationKey()).mergeStyle(source.getChatColor());
                    int modifier = (int)Math.round(100.0D * this.getTotalCostModifier(stack, player, source));
                    ITextComponent line = new TranslationTextComponent("primalmagic.source.mana_tooltip", nameComp, this.getManaText(stack, source), this.getMaxManaText(stack), modifier);
                    tooltip.add(line);
                }
            }
            
            // Add inscribed spell listing
            List<SpellPackage> spells = this.getSpells(stack);
            int activeIndex = this.getActiveSpellIndex(stack);
            tooltip.add(new TranslationTextComponent("primalmagic.spells.wand_header", this.getSpellCapacityText(stack)));
            if (spells.isEmpty()) {
                tooltip.add(new TranslationTextComponent("primalmagic.spells.none"));
            } else {
                for (int index = 0; index < spells.size(); index++) {
                    SpellPackage spell = spells.get(index);
                    StringBuilder sb = new StringBuilder("  ");
                    if (index == activeIndex) {
                        // Prefix the active spell name with an asterisk to distinguish it
                        sb.append("*");
                    }
                    sb.append(spell.getName().getString());
                    tooltip.add(new StringTextComponent(sb.toString()));
                }
            }
        } else {
            // Add mana summary
            StringBuilder sb = new StringBuilder();
            boolean first = true;
            for (Source source : Source.SORTED_SOURCES) {
                // Only include a mana source in the summary if it's been discovered
                if (source.isDiscovered(player)) {
                    if (!first) {
                        sb.append("/");
                    }
                    ITextComponent manaStr = this.getManaText(stack, source).mergeStyle(source.getChatColor());
                    sb.append(manaStr.getString());
                    first = false;
                }
            }
            tooltip.add(new StringTextComponent(sb.toString()));
            
            // Add active spell
            SpellPackage activeSpell = this.getActiveSpell(stack);
            ITextComponent activeSpellName = (activeSpell == null) ?
                    new TranslationTextComponent("tooltip.primalmagic.none") :
                    activeSpell.getName();
            tooltip.add(new TranslationTextComponent("primalmagic.spells.short_wand_header", activeSpellName));
        }
    }
    
    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }
    
    @Override
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }
    
    @Override
    public IInteractWithWand getTileInUse(ItemStack wandStack, World world) {
        // Look up the world coordinates of the wand-interactable tile entity currently in use from NBT, then query the world for it
        if (wandStack.hasTag() && wandStack.getTag().contains("UsingX") && wandStack.getTag().contains("UsingY") && wandStack.getTag().contains("UsingZ")) {
            BlockPos pos = new BlockPos(wandStack.getTag().getInt("UsingX"), wandStack.getTag().getInt("UsingY"), wandStack.getTag().getInt("UsingZ"));
            TileEntity tile = world.getTileEntity(pos);
            if (tile instanceof IInteractWithWand) {
                return (IInteractWithWand)tile;
            }
        }
        return null;
    }
    
    @Override
    public <T extends TileEntity & IInteractWithWand> void setTileInUse(ItemStack wandStack, T tile) {
        // Save the position of the wand-interactable tile entity so it can be looked up later
        wandStack.setTagInfo("UsingX", IntNBT.valueOf(tile.getPos().getX()));
        wandStack.setTagInfo("UsingY", IntNBT.valueOf(tile.getPos().getY()));
        wandStack.setTagInfo("UsingZ", IntNBT.valueOf(tile.getPos().getZ()));
    }
    
    @Override
    public void clearTileInUse(ItemStack wandStack) {
        if (wandStack.hasTag()) {
            wandStack.getTag().remove("UsingX");
            wandStack.getTag().remove("UsingY");
            wandStack.getTag().remove("UsingZ");
        }
    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);
        playerIn.setActiveHand(handIn);
        SpellPackage activeSpell = this.getActiveSpell(stack);
        if (activeSpell != null && !SpellManager.isOnCooldown(playerIn)) {
            // If the wand has an active spell and spells are off the player's cooldown, attempt to cast the spell on right-click
            SpellManager.setCooldown(playerIn, activeSpell.getCooldownTicks());
            if (worldIn.isRemote) {
                return new ActionResult<>(ActionResultType.SUCCESS, stack);
            } else if (this.consumeRealMana(stack, playerIn, activeSpell.getManaCost())) {
                // If the wand contains enough mana, consume it and cast the spell
                activeSpell.cast(worldIn, playerIn, stack);
                playerIn.swingArm(handIn);
                return new ActionResult<>(ActionResultType.SUCCESS, stack);
            } else {
                return new ActionResult<>(ActionResultType.FAIL, stack);
            }
        } else {
            return new ActionResult<>(ActionResultType.PASS, stack);
        }
    }
    
    @Override
    public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context) {
        // Only process on server side
    	World world = context.getWorld();
        if (world.isRemote) {
            return ActionResultType.PASS;
        }
        
        // Bypass wand functionality if the player is sneaking
        if (context.getPlayer().isSneaking()) {
            return ActionResultType.PASS;
        }
        
        context.getPlayer().setActiveHand(context.getHand());
        
        // If the mouseover target is a wand-sensitive block, trigger that initial interaction
        BlockState bs = context.getWorld().getBlockState(context.getPos());
        if (bs.getBlock() instanceof IInteractWithWand) {
            return ((IInteractWithWand)bs.getBlock()).onWandRightClick(context.getItem(), context.getWorld(), context.getPlayer(), context.getPos(), context.getFace());
        }
        
        // If the mouseover target is a wand-sensitive tile entity, trigger that initial interaction
        TileEntity tile = context.getWorld().getTileEntity(context.getPos());
        if (tile != null && tile instanceof IInteractWithWand) {
            return ((IInteractWithWand)tile).onWandRightClick(context.getItem(), context.getWorld(), context.getPlayer(), context.getPos(), context.getFace());
        }
        
        // Otherwise, see if the mouseover target is a valid target for wand transformation
        for (IWandTransform transform : WandTransforms.getAll()) {
            if (transform.isValid(context.getWorld(), context.getPlayer(), context.getPos())) {
                if (!context.getPlayer().canPlayerEdit(context.getPos(), context.getFace(), context.getItem())) {
                    return ActionResultType.FAIL;
                } else {
                    context.getPlayer().swingArm(context.getHand());
                    transform.execute(context.getWorld(), context.getPlayer(), context.getPos());
                    return ActionResultType.SUCCESS;
                }
            }
        }
        return ActionResultType.PASS;
    }
    
    @Override
    public void onUsingTick(ItemStack stack, LivingEntity living, int count) {
        // If the player continues to hold the interact button, continue the interaction with the last wand-sensitive block/tile interacted with
        if (living instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity)living;
            IInteractWithWand wandable = this.getTileInUse(stack, player.world);
            if (wandable != null) {
                wandable.onWandUseTick(stack, player, count);
            }
        }
    }
    
    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft) {
        // Once interaction ceases, clear the last-interacted coordinates
        super.onPlayerStoppedUsing(stack, worldIn, entityLiving, timeLeft);
        this.clearTileInUse(stack);
    }
    
    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return false;
    }
    
    @Override
    public boolean canContinueUsing(ItemStack oldStack, ItemStack newStack) {
        // Don't break wand interaction just because the stack NBT changes
        return true;
    }
}
