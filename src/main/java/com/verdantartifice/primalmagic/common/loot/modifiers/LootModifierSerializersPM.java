package com.verdantartifice.primalmagic.common.loot.modifiers;

import com.verdantartifice.primalmagic.PrimalMagic;

import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Deferred registry for the mod's global loot modifier serializers.
 * 
 * @author Daedalus4096
 */
public class LootModifierSerializersPM {
    private static final DeferredRegister<GlobalLootModifierSerializer<?>> SERIALIZERS = DeferredRegister.create(ForgeRegistries.LOOT_MODIFIER_SERIALIZERS, PrimalMagic.MODID);
    
    public static void init() {
        SERIALIZERS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
    
    public static final RegistryObject<GlobalLootModifierSerializer<BloodyFleshModifier>> BLOODY_FLESH = SERIALIZERS.register("bloody_flesh", BloodyFleshModifier.Serializer::new);
}
