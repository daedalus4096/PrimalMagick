package com.verdantartifice.primalmagic.common.enchantments;

import com.verdantartifice.primalmagic.PrimalMagic;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class EnchantmentsPM {
    private static final DeferredRegister<Enchantment> ENCHANTMENTS = new DeferredRegister<>(ForgeRegistries.ENCHANTMENTS, PrimalMagic.MODID);
    
    public static void init() {
        ENCHANTMENTS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
    
    public static final RegistryObject<Enchantment> LIFESTEAL = ENCHANTMENTS.register("lifesteal", () -> new LifestealEnchantment(Enchantment.Rarity.RARE, EquipmentSlotType.MAINHAND));
    public static final RegistryObject<Enchantment> ENDERLOCK = ENCHANTMENTS.register("enderlock", () -> new EnderlockEnchantment(Enchantment.Rarity.RARE, EquipmentSlotType.MAINHAND));
    public static final RegistryObject<Enchantment> JUDGMENT = ENCHANTMENTS.register("judgment", () -> new JudgmentEnchantment(Enchantment.Rarity.VERY_RARE, EquipmentSlotType.MAINHAND));
    public static final RegistryObject<Enchantment> ENDERPORT = ENCHANTMENTS.register("enderport", () -> new EnderportEnchantment(Enchantment.Rarity.RARE, EquipmentSlotType.MAINHAND));
    public static final RegistryObject<Enchantment> REGROWTH = ENCHANTMENTS.register("regrowth", () -> new RegrowthEnchantment(Enchantment.Rarity.VERY_RARE, EquipmentSlotType.values()));
}
