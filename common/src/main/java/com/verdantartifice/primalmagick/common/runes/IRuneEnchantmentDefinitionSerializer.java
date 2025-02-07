package com.verdantartifice.primalmagick.common.runes;

import net.minecraft.network.FriendlyByteBuf;

/**
 * Primary interface for the serializer of a data-defined rune enchantment definition.
 * 
 * @author Daedalus4096
 */
public interface IRuneEnchantmentDefinitionSerializer {
    /**
     * Read a rune enchantment definition from the network
     */
    RuneEnchantmentDefinition fromNetwork(FriendlyByteBuf buf);

    /**
     * Write a rune enchantment definition to the network
     */
    void toNetwork(FriendlyByteBuf buf, RuneEnchantmentDefinition data);
}
