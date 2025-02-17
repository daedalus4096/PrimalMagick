package com.verdantartifice.primalmagick.common.items.misc;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class EarthshatterHammerItemNeoforge extends EarthshatterHammerItem{
    public EarthshatterHammerItemNeoforge() {
        // In Neoforge, the hammer can be marked as irreparable without use of a mixin
        super(new Item.Properties().durability(255).rarity(Rarity.UNCOMMON).setNoRepair());
    }
}
