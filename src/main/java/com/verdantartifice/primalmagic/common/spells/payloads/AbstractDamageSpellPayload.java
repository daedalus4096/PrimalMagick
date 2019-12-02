package com.verdantartifice.primalmagic.common.spells.payloads;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.sources.SourceList;
import com.verdantartifice.primalmagic.common.spells.ISpellPackage;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public abstract class AbstractDamageSpellPayload extends AbstractSpellPayload {
    protected int power;
    
    public AbstractDamageSpellPayload() {}
    
    public AbstractDamageSpellPayload(int power) {
        this.power = power;
    }
    
    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = super.serializeNBT();
        nbt.putInt("Power", this.power);
        return nbt;
    }
    
    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        super.deserializeNBT(nbt);
        this.power = nbt.getInt("Power");
    }

    @Override
    public void execute(RayTraceResult target, ISpellPackage spell, World world, PlayerEntity caster) {
        if (target == null || target.getType() == RayTraceResult.Type.MISS) {
            PrimalMagic.LOGGER.info("No target found for {} damage spell {}", this.getSource().getTag(), spell.getName());
        } else if (target.getType() == RayTraceResult.Type.ENTITY) {
            PrimalMagic.LOGGER.info("Casting {} damage spell {} on entity {}", this.getSource().getTag(), spell.getName(), ((EntityRayTraceResult)target).getEntity().getName().getFormattedText());
        } else if (target.getType() == RayTraceResult.Type.BLOCK) {
            BlockRayTraceResult blockResult = (BlockRayTraceResult)target;
            BlockState state = world.getBlockState(blockResult.getPos());
            PrimalMagic.LOGGER.info("Casting {} damage spell {} on block {} at {}", this.getSource().getTag(), spell.getName(), state.getBlock().getRegistryName().toString(), blockResult.getPos().toString());
        }
    }

    @Override
    public SourceList getManaCost() {
        return new SourceList().add(this.getSource(), this.power);
    }
}
