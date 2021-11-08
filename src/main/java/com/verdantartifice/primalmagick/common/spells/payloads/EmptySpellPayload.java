package com.verdantartifice.primalmagick.common.spells.payloads;

import com.verdantartifice.primalmagick.common.research.CompoundResearchKey;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.spells.SpellPackage;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

/**
 * Definition of an empty spell payload.  This payload has no effect and is not valid in spells.  Its 
 * only purpose is to provide a selection entry in the spellcrafting altar GUI for when the player has
 * not selected a payload for the spell.
 * 
 * @author Daedalus4096
 */
public class EmptySpellPayload extends AbstractSpellPayload {
    public static final String TYPE = "none";

    @Override
    public void execute(HitResult target, Vec3 burstPoint, SpellPackage spell, Level world, LivingEntity caster, ItemStack spellSource, Entity projectileEntity) {
        // Do nothing
    }

    @Override
    public boolean isActive() {
        return false;
    }
    
    @Override
    public Source getSource() {
        return Source.EARTH;
    }

    @Override
    public int getBaseManaCost() {
        return 0;
    }

    @Override
    public void playSounds(Level world, BlockPos origin) {
        // Do nothing
    }

    @Override
    protected String getPayloadType() {
        return TYPE;
    }
    
    public static CompoundResearchKey getResearch() {
        return null;
    }
}
