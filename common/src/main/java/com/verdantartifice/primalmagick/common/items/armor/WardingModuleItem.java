package com.verdantartifice.primalmagick.common.items.armor;

import com.google.common.collect.ImmutableList;
import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.common.components.DataComponentsPM;
import com.verdantartifice.primalmagick.common.items.ItemRegistration;
import com.verdantartifice.primalmagick.common.misc.DeviceTier;
import com.verdantartifice.primalmagick.common.misc.ITieredDevice;
import com.verdantartifice.primalmagick.common.tags.ItemTagsPM;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.function.Supplier;

/**
 * Item definition for a warding module, a magitech device that can be attached to hard armor
 * to provide the wearer with temporary hit points at the cost of mana.
 * 
 * @author Daedalus4096
 */
public class WardingModuleItem extends Item implements ITieredDevice {
    public static final String TAG_NAME = String.join(":", Constants.MOD_ID, "WardingModule");
    
    /**
     * The total amount of centimana that can be infused into a warded armor stack at once.
     */
    public static final int MANA_CAPACITY = 10000;
    
    /**
     * The amount of centimana that can be infused into the warded armor per tick.
     */
    public static final int CHARGE_RATE = 10000;
    
    /**
     * The amount of centimana needed to regenerate one point of ward.
     */
    public static final int REGEN_COST = 500;
    
    // FIXME Use the WARDABLE_ARMOR tag as the source of truth if/when the RegisterItemDecorationsEvent is made to fire *after* tag data loads
    @SuppressWarnings("unchecked")
    protected static final List<Supplier<? extends Item>> APPLICABLE_ITEMS = ImmutableList.<Supplier<? extends Item>>builder().add(
            ItemRegistration.PRIMALITE_HEAD, ItemRegistration.PRIMALITE_CHEST, ItemRegistration.PRIMALITE_LEGS, ItemRegistration.PRIMALITE_FEET,
            ItemRegistration.HEXIUM_HEAD, ItemRegistration.HEXIUM_CHEST, ItemRegistration.HEXIUM_LEGS, ItemRegistration.HEXIUM_FEET,
            ItemRegistration.HALLOWSTEEL_HEAD, ItemRegistration.HALLOWSTEEL_CHEST, ItemRegistration.HALLOWSTEEL_LEGS, ItemRegistration.HALLOWSTEEL_FEET).build();
    
    protected final DeviceTier tier;
    
    public WardingModuleItem(DeviceTier tier, Item.Properties properties) {
        super(properties);
        this.tier = tier;
    }
    
    @Override
    public DeviceTier getDeviceTier() {
        return this.tier;
    }
    
    public boolean hasWard() {
        return this.getWardLevel() > 0;
    }
    
    public int getWardLevel() {
        return switch (this.getDeviceTier()) {
            case ENCHANTED -> 1;
            case FORBIDDEN -> 2;
            case HEAVENLY -> 3;
            default -> 0;
        };
    }
    
    public static boolean hasWardAttached(ItemStack stack) {
        return stack.is(ItemTagsPM.WARDABLE_ARMOR) && stack.getOrDefault(DataComponentsPM.WARD_LEVEL.get(), 0) > 0;
    }
    
    public static int getAttachedWardLevel(ItemStack stack) {
        return stack.getOrDefault(DataComponentsPM.WARD_LEVEL.get(), 0);
    }
    
    public static List<Supplier<? extends Item>> getApplicableItems() {
        return APPLICABLE_ITEMS;
    }
}
