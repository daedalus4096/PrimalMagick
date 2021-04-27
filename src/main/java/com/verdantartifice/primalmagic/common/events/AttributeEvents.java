package com.verdantartifice.primalmagic.common.events;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.entities.EntityTypesPM;
import com.verdantartifice.primalmagic.common.entities.companions.golems.HallowsteelGolemEntity;
import com.verdantartifice.primalmagic.common.entities.companions.golems.HexiumGolemEntity;
import com.verdantartifice.primalmagic.common.entities.companions.golems.PrimaliteGolemEntity;
import com.verdantartifice.primalmagic.common.entities.companions.pixies.BasicEarthPixieEntity;
import com.verdantartifice.primalmagic.common.entities.companions.pixies.GrandEarthPixieEntity;
import com.verdantartifice.primalmagic.common.entities.companions.pixies.MajesticEarthPixieEntity;

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
    }
}
