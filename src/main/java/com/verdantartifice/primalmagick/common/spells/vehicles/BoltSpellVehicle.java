package com.verdantartifice.primalmagick.common.spells.vehicles;

import java.util.Arrays;
import java.util.List;

import com.mojang.serialization.MapCodec;
import com.verdantartifice.primalmagick.common.network.PacketHandler;
import com.verdantartifice.primalmagick.common.network.packets.fx.SpellBoltPacket;
import com.verdantartifice.primalmagick.common.research.ResearchEntries;
import com.verdantartifice.primalmagick.common.research.keys.ResearchEntryKey;
import com.verdantartifice.primalmagick.common.research.requirements.AbstractRequirement;
import com.verdantartifice.primalmagick.common.research.requirements.ResearchRequirement;
import com.verdantartifice.primalmagick.common.spells.SpellPackage;
import com.verdantartifice.primalmagick.common.spells.SpellPropertiesPM;
import com.verdantartifice.primalmagick.common.spells.SpellProperty;
import com.verdantartifice.primalmagick.common.spells.SpellPropertyConfiguration;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

/**
 * Definition of a bolt spell vehicle.  Bolts are mid-range, instant spell vehicles that are not
 * affected by gravity.  Essentially, they're a longer-range touch vehicle with a particle effect.
 * 
 * @author Daedalus4096
 */
public class BoltSpellVehicle extends AbstractRaycastSpellVehicle<BoltSpellVehicle> {
    public static final BoltSpellVehicle INSTANCE = new BoltSpellVehicle();
    
    public static final MapCodec<BoltSpellVehicle> CODEC = MapCodec.unit(BoltSpellVehicle.INSTANCE);
    public static final StreamCodec<ByteBuf, BoltSpellVehicle> STREAM_CODEC = StreamCodec.unit(BoltSpellVehicle.INSTANCE);
    
    public static final String TYPE = "bolt";
    protected static final AbstractRequirement<?> REQUIREMENT = new ResearchRequirement(new ResearchEntryKey(ResearchEntries.SPELL_VEHICLE_BOLT));
    protected static final List<SpellProperty> PROPERTIES = Arrays.asList(SpellPropertiesPM.RANGE.get());

    public static AbstractRequirement<?> getRequirement() {
        return REQUIREMENT;
    }

    @Override
    public SpellVehicleType<BoltSpellVehicle> getType() {
        return SpellVehiclesPM.BOLT.get();
    }

    @Override
    protected List<SpellProperty> getPropertiesInner() {
        return PROPERTIES;
    }

    @Override
    protected double getReachDistance(LivingEntity caster, SpellPackage spell, ItemStack spellSource) {
        return (double)this.getRangeBlocks(spell, spellSource);
    }

    @Override
    protected String getVehicleType() {
        return TYPE;
    }
    
    @Override
    public int getBaseManaCostModifier(SpellPropertyConfiguration properties) {
        return properties.get(SpellPropertiesPM.RANGE.get());
    }
    
    @Override
    protected void drawFx(Level world, SpellPackage spell, Vec3 source, Vec3 target) {
        if (spell.getPayload() != null) {
            // Show a bolt particle effect to every player in range
            PacketHandler.sendToAllAround(
                    new SpellBoltPacket(source, target, spell.getPayload().getComponent().getSource().getColor()), 
                    world.dimension(), 
                    BlockPos.containing(source), 
                    64.0D);
        }
    }
    
    protected int getRangeBlocks(SpellPackage spell, ItemStack spellSource) {
        return 6 + (2 * spell.getVehicle().getPropertyValue(SpellPropertiesPM.RANGE.get()));
    }

    @Override
    public Component getDetailTooltip(SpellPackage spell, ItemStack spellSource) {
        return Component.translatable("spells.primalmagick.vehicle." + this.getVehicleType() + ".detail_tooltip", this.getRangeBlocks(spell, spellSource));
    }

    @Override
    public boolean isIndirect() {
        return true;
    }
}
