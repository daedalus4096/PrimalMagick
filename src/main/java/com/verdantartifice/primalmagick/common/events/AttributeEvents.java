package com.verdantartifice.primalmagick.common.events;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.entities.EntityTypesPM;
import com.verdantartifice.primalmagick.common.entities.companions.golems.HallowsteelGolemEntity;
import com.verdantartifice.primalmagick.common.entities.companions.golems.HexiumGolemEntity;
import com.verdantartifice.primalmagick.common.entities.companions.golems.PrimaliteGolemEntity;
import com.verdantartifice.primalmagick.common.entities.companions.pixies.IBasicPixie;
import com.verdantartifice.primalmagick.common.entities.companions.pixies.IGrandPixie;
import com.verdantartifice.primalmagick.common.entities.companions.pixies.IMajesticPixie;
import com.verdantartifice.primalmagick.common.entities.misc.FriendlyWitchEntity;
import com.verdantartifice.primalmagick.common.entities.misc.InnerDemonEntity;
import com.verdantartifice.primalmagick.common.entities.treefolk.TreefolkEntity;

import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Handlers for mod entity attribute registration events.
 * 
 * @author Daedalus4096
 */
@Mod.EventBusSubscriber(modid=PrimalMagick.MODID, bus=Mod.EventBusSubscriber.Bus.MOD)
public class AttributeEvents {
    @SubscribeEvent
    public static void onEntityAttributeCreation(EntityAttributeCreationEvent event) {
        event.put(EntityTypesPM.TREEFOLK.get(), TreefolkEntity.getAttributeModifiers().build());
        event.put(EntityTypesPM.INNER_DEMON.get(), InnerDemonEntity.getAttributeModifiers().build());
        event.put(EntityTypesPM.FRIENDLY_WITCH.get(), FriendlyWitchEntity.getAttributeModifiers().build());
        event.put(EntityTypesPM.PRIMALITE_GOLEM.get(), PrimaliteGolemEntity.getAttributeModifiers().build());
        event.put(EntityTypesPM.HEXIUM_GOLEM.get(), HexiumGolemEntity.getAttributeModifiers().build());
        event.put(EntityTypesPM.HALLOWSTEEL_GOLEM.get(), HallowsteelGolemEntity.getAttributeModifiers().build());
        event.put(EntityTypesPM.BASIC_EARTH_PIXIE.get(), IBasicPixie.getAttributeModifiers().build());
        event.put(EntityTypesPM.GRAND_EARTH_PIXIE.get(), IGrandPixie.getAttributeModifiers().build());
        event.put(EntityTypesPM.MAJESTIC_EARTH_PIXIE.get(), IMajesticPixie.getAttributeModifiers().build());
        event.put(EntityTypesPM.BASIC_SEA_PIXIE.get(), IBasicPixie.getAttributeModifiers().build());
        event.put(EntityTypesPM.GRAND_SEA_PIXIE.get(), IGrandPixie.getAttributeModifiers().build());
        event.put(EntityTypesPM.MAJESTIC_SEA_PIXIE.get(), IMajesticPixie.getAttributeModifiers().build());
        event.put(EntityTypesPM.BASIC_SKY_PIXIE.get(), IBasicPixie.getAttributeModifiers().build());
        event.put(EntityTypesPM.GRAND_SKY_PIXIE.get(), IGrandPixie.getAttributeModifiers().build());
        event.put(EntityTypesPM.MAJESTIC_SKY_PIXIE.get(), IMajesticPixie.getAttributeModifiers().build());
        event.put(EntityTypesPM.BASIC_SUN_PIXIE.get(), IBasicPixie.getAttributeModifiers().build());
        event.put(EntityTypesPM.GRAND_SUN_PIXIE.get(), IGrandPixie.getAttributeModifiers().build());
        event.put(EntityTypesPM.MAJESTIC_SUN_PIXIE.get(), IMajesticPixie.getAttributeModifiers().build());
        event.put(EntityTypesPM.BASIC_MOON_PIXIE.get(), IBasicPixie.getAttributeModifiers().build());
        event.put(EntityTypesPM.GRAND_MOON_PIXIE.get(), IGrandPixie.getAttributeModifiers().build());
        event.put(EntityTypesPM.MAJESTIC_MOON_PIXIE.get(), IMajesticPixie.getAttributeModifiers().build());
        event.put(EntityTypesPM.BASIC_BLOOD_PIXIE.get(), IBasicPixie.getAttributeModifiers().build());
        event.put(EntityTypesPM.GRAND_BLOOD_PIXIE.get(), IGrandPixie.getAttributeModifiers().build());
        event.put(EntityTypesPM.MAJESTIC_BLOOD_PIXIE.get(), IMajesticPixie.getAttributeModifiers().build());
        event.put(EntityTypesPM.BASIC_INFERNAL_PIXIE.get(), IBasicPixie.getAttributeModifiers().build());
        event.put(EntityTypesPM.GRAND_INFERNAL_PIXIE.get(), IGrandPixie.getAttributeModifiers().build());
        event.put(EntityTypesPM.MAJESTIC_INFERNAL_PIXIE.get(), IMajesticPixie.getAttributeModifiers().build());
        event.put(EntityTypesPM.BASIC_VOID_PIXIE.get(), IBasicPixie.getAttributeModifiers().build());
        event.put(EntityTypesPM.GRAND_VOID_PIXIE.get(), IGrandPixie.getAttributeModifiers().build());
        event.put(EntityTypesPM.MAJESTIC_VOID_PIXIE.get(), IMajesticPixie.getAttributeModifiers().build());
        event.put(EntityTypesPM.BASIC_HALLOWED_PIXIE.get(), IBasicPixie.getAttributeModifiers().build());
        event.put(EntityTypesPM.GRAND_HALLOWED_PIXIE.get(), IGrandPixie.getAttributeModifiers().build());
        event.put(EntityTypesPM.MAJESTIC_HALLOWED_PIXIE.get(), IMajesticPixie.getAttributeModifiers().build());
    }
}
