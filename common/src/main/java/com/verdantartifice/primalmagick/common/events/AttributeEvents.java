package com.verdantartifice.primalmagick.common.events;

import com.verdantartifice.primalmagick.common.entities.EntityTypesPM;
import com.verdantartifice.primalmagick.common.entities.golems.HallowsteelGolemEntity;
import com.verdantartifice.primalmagick.common.entities.golems.HexiumGolemEntity;
import com.verdantartifice.primalmagick.common.entities.golems.PrimaliteGolemEntity;
import com.verdantartifice.primalmagick.common.entities.pixies.IBasicPixie;
import com.verdantartifice.primalmagick.common.entities.pixies.IGrandPixie;
import com.verdantartifice.primalmagick.common.entities.pixies.IMajesticPixie;
import com.verdantartifice.primalmagick.common.entities.misc.FriendlyWitchEntity;
import com.verdantartifice.primalmagick.common.entities.misc.InnerDemonEntity;
import com.verdantartifice.primalmagick.common.entities.misc.PixieHouseEntity;
import com.verdantartifice.primalmagick.common.entities.treefolk.TreefolkEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;

import java.util.function.BiConsumer;

/**
 * Common event handler logic for mod entity attribute registration events.
 * 
 * @author Daedalus4096
 */
public class AttributeEvents {
    public static void onEntityAttributeCreation(BiConsumer<EntityType<? extends LivingEntity>, AttributeSupplier> consumer) {
        consumer.accept(EntityTypesPM.TREEFOLK.get(), TreefolkEntity.getAttributeModifiers().build());
        consumer.accept(EntityTypesPM.INNER_DEMON.get(), InnerDemonEntity.getAttributeModifiers().build());
        consumer.accept(EntityTypesPM.FRIENDLY_WITCH.get(), FriendlyWitchEntity.getAttributeModifiers().build());
        consumer.accept(EntityTypesPM.PRIMALITE_GOLEM.get(), PrimaliteGolemEntity.getAttributeModifiers().build());
        consumer.accept(EntityTypesPM.HEXIUM_GOLEM.get(), HexiumGolemEntity.getAttributeModifiers().build());
        consumer.accept(EntityTypesPM.HALLOWSTEEL_GOLEM.get(), HallowsteelGolemEntity.getAttributeModifiers().build());
        consumer.accept(EntityTypesPM.BASIC_EARTH_PIXIE.get(), IBasicPixie.getAttributeModifiers().build());
        consumer.accept(EntityTypesPM.GRAND_EARTH_PIXIE.get(), IGrandPixie.getAttributeModifiers().build());
        consumer.accept(EntityTypesPM.MAJESTIC_EARTH_PIXIE.get(), IMajesticPixie.getAttributeModifiers().build());
        consumer.accept(EntityTypesPM.BASIC_SEA_PIXIE.get(), IBasicPixie.getAttributeModifiers().build());
        consumer.accept(EntityTypesPM.GRAND_SEA_PIXIE.get(), IGrandPixie.getAttributeModifiers().build());
        consumer.accept(EntityTypesPM.MAJESTIC_SEA_PIXIE.get(), IMajesticPixie.getAttributeModifiers().build());
        consumer.accept(EntityTypesPM.BASIC_SKY_PIXIE.get(), IBasicPixie.getAttributeModifiers().build());
        consumer.accept(EntityTypesPM.GRAND_SKY_PIXIE.get(), IGrandPixie.getAttributeModifiers().build());
        consumer.accept(EntityTypesPM.MAJESTIC_SKY_PIXIE.get(), IMajesticPixie.getAttributeModifiers().build());
        consumer.accept(EntityTypesPM.BASIC_SUN_PIXIE.get(), IBasicPixie.getAttributeModifiers().build());
        consumer.accept(EntityTypesPM.GRAND_SUN_PIXIE.get(), IGrandPixie.getAttributeModifiers().build());
        consumer.accept(EntityTypesPM.MAJESTIC_SUN_PIXIE.get(), IMajesticPixie.getAttributeModifiers().build());
        consumer.accept(EntityTypesPM.BASIC_MOON_PIXIE.get(), IBasicPixie.getAttributeModifiers().build());
        consumer.accept(EntityTypesPM.GRAND_MOON_PIXIE.get(), IGrandPixie.getAttributeModifiers().build());
        consumer.accept(EntityTypesPM.MAJESTIC_MOON_PIXIE.get(), IMajesticPixie.getAttributeModifiers().build());
        consumer.accept(EntityTypesPM.BASIC_BLOOD_PIXIE.get(), IBasicPixie.getAttributeModifiers().build());
        consumer.accept(EntityTypesPM.GRAND_BLOOD_PIXIE.get(), IGrandPixie.getAttributeModifiers().build());
        consumer.accept(EntityTypesPM.MAJESTIC_BLOOD_PIXIE.get(), IMajesticPixie.getAttributeModifiers().build());
        consumer.accept(EntityTypesPM.BASIC_INFERNAL_PIXIE.get(), IBasicPixie.getAttributeModifiers().build());
        consumer.accept(EntityTypesPM.GRAND_INFERNAL_PIXIE.get(), IGrandPixie.getAttributeModifiers().build());
        consumer.accept(EntityTypesPM.MAJESTIC_INFERNAL_PIXIE.get(), IMajesticPixie.getAttributeModifiers().build());
        consumer.accept(EntityTypesPM.BASIC_VOID_PIXIE.get(), IBasicPixie.getAttributeModifiers().build());
        consumer.accept(EntityTypesPM.GRAND_VOID_PIXIE.get(), IGrandPixie.getAttributeModifiers().build());
        consumer.accept(EntityTypesPM.MAJESTIC_VOID_PIXIE.get(), IMajesticPixie.getAttributeModifiers().build());
        consumer.accept(EntityTypesPM.BASIC_HALLOWED_PIXIE.get(), IBasicPixie.getAttributeModifiers().build());
        consumer.accept(EntityTypesPM.GRAND_HALLOWED_PIXIE.get(), IGrandPixie.getAttributeModifiers().build());
        consumer.accept(EntityTypesPM.MAJESTIC_HALLOWED_PIXIE.get(), IMajesticPixie.getAttributeModifiers().build());
        consumer.accept(EntityTypesPM.PIXIE_HOUSE.get(), PixieHouseEntity.getAttributeModifiers().build());
        consumer.accept(EntityTypesPM.BASIC_GUARDIAN_PIXIE.get(), IBasicPixie.getAttributeModifiers().build());
        consumer.accept(EntityTypesPM.GRAND_GUARDIAN_PIXIE.get(), IGrandPixie.getAttributeModifiers().build());
        consumer.accept(EntityTypesPM.MAJESTIC_GUARDIAN_PIXIE.get(), IMajesticPixie.getAttributeModifiers().build());
    }
}
