package com.verdantartifice.primalmagic.common.events;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.entities.EntityTypesPM;
import com.verdantartifice.primalmagic.common.entities.companions.golems.HallowsteelGolemEntity;
import com.verdantartifice.primalmagic.common.entities.companions.golems.HexiumGolemEntity;
import com.verdantartifice.primalmagic.common.entities.companions.golems.PrimaliteGolemEntity;
import com.verdantartifice.primalmagic.common.entities.companions.pixies.BasicEarthPixieEntity;
import com.verdantartifice.primalmagic.common.entities.companions.pixies.BasicMoonPixieEntity;
import com.verdantartifice.primalmagic.common.entities.companions.pixies.BasicSeaPixieEntity;
import com.verdantartifice.primalmagic.common.entities.companions.pixies.BasicSkyPixieEntity;
import com.verdantartifice.primalmagic.common.entities.companions.pixies.BasicSunPixieEntity;
import com.verdantartifice.primalmagic.common.entities.companions.pixies.GrandEarthPixieEntity;
import com.verdantartifice.primalmagic.common.entities.companions.pixies.GrandMoonPixieEntity;
import com.verdantartifice.primalmagic.common.entities.companions.pixies.GrandSeaPixieEntity;
import com.verdantartifice.primalmagic.common.entities.companions.pixies.GrandSkyPixieEntity;
import com.verdantartifice.primalmagic.common.entities.companions.pixies.GrandSunPixieEntity;
import com.verdantartifice.primalmagic.common.entities.companions.pixies.MajesticEarthPixieEntity;
import com.verdantartifice.primalmagic.common.entities.companions.pixies.MajesticMoonPixieEntity;
import com.verdantartifice.primalmagic.common.entities.companions.pixies.MajesticSeaPixieEntity;
import com.verdantartifice.primalmagic.common.entities.companions.pixies.MajesticSkyPixieEntity;
import com.verdantartifice.primalmagic.common.entities.companions.pixies.MajesticSunPixieEntity;

import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Handlers for mod entity attribute registration events.
 * 
 * @author Daedalus4096
 */
@Mod.EventBusSubscriber(modid=PrimalMagic.MODID, bus=Mod.EventBusSubscriber.Bus.MOD)
public class AttributeEvents {
    @SubscribeEvent
    public static void onEntityAttributeCreation(EntityAttributeCreationEvent event) {
        event.put(EntityTypesPM.PRIMALITE_GOLEM.get(), PrimaliteGolemEntity.getAttributeModifiers().create());
        event.put(EntityTypesPM.HEXIUM_GOLEM.get(), HexiumGolemEntity.getAttributeModifiers().create());
        event.put(EntityTypesPM.HALLOWSTEEL_GOLEM.get(), HallowsteelGolemEntity.getAttributeModifiers().create());
        event.put(EntityTypesPM.BASIC_EARTH_PIXIE.get(), BasicEarthPixieEntity.getAttributeModifiers().create());
        event.put(EntityTypesPM.GRAND_EARTH_PIXIE.get(), GrandEarthPixieEntity.getAttributeModifiers().create());
        event.put(EntityTypesPM.MAJESTIC_EARTH_PIXIE.get(), MajesticEarthPixieEntity.getAttributeModifiers().create());
        event.put(EntityTypesPM.BASIC_SEA_PIXIE.get(), BasicSeaPixieEntity.getAttributeModifiers().create());
        event.put(EntityTypesPM.GRAND_SEA_PIXIE.get(), GrandSeaPixieEntity.getAttributeModifiers().create());
        event.put(EntityTypesPM.MAJESTIC_SEA_PIXIE.get(), MajesticSeaPixieEntity.getAttributeModifiers().create());
        event.put(EntityTypesPM.BASIC_SKY_PIXIE.get(), BasicSkyPixieEntity.getAttributeModifiers().create());
        event.put(EntityTypesPM.GRAND_SKY_PIXIE.get(), GrandSkyPixieEntity.getAttributeModifiers().create());
        event.put(EntityTypesPM.MAJESTIC_SKY_PIXIE.get(), MajesticSkyPixieEntity.getAttributeModifiers().create());
        event.put(EntityTypesPM.BASIC_SUN_PIXIE.get(), BasicSunPixieEntity.getAttributeModifiers().create());
        event.put(EntityTypesPM.GRAND_SUN_PIXIE.get(), GrandSunPixieEntity.getAttributeModifiers().create());
        event.put(EntityTypesPM.MAJESTIC_SUN_PIXIE.get(), MajesticSunPixieEntity.getAttributeModifiers().create());
        event.put(EntityTypesPM.BASIC_MOON_PIXIE.get(), BasicMoonPixieEntity.getAttributeModifiers().create());
        event.put(EntityTypesPM.GRAND_MOON_PIXIE.get(), GrandMoonPixieEntity.getAttributeModifiers().create());
        event.put(EntityTypesPM.MAJESTIC_MOON_PIXIE.get(), MajesticMoonPixieEntity.getAttributeModifiers().create());
    }
}
