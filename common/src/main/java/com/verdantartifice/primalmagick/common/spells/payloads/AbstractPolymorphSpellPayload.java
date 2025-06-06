package com.verdantartifice.primalmagick.common.spells.payloads;

import com.verdantartifice.primalmagick.common.misc.EntitySwapper;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.sources.Sources;
import com.verdantartifice.primalmagick.common.spells.SpellManager;
import com.verdantartifice.primalmagick.common.spells.SpellPackage;
import com.verdantartifice.primalmagick.common.spells.SpellPropertiesPM;
import com.verdantartifice.primalmagick.common.spells.SpellProperty;
import com.verdantartifice.primalmagick.common.spells.SpellPropertyConfiguration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

/**
 * Definition of a polymorph spell.  Temporarily replaces the target living, non-player, non-boss
 * entity with another entity.  The length of the replacement scales with the payload's duration
 * property.  NBT data of the original entity is preserved for the swap back.  Has no effect on
 * blocks.
 * 
 * @author Daedalus4096
 * @see com.verdantartifice.primalmagick.common.misc.EntitySwapper
 */
public abstract class AbstractPolymorphSpellPayload<T extends AbstractPolymorphSpellPayload<T>> extends AbstractSpellPayload<T> {
    private static final Supplier<List<SpellProperty>> PROPERTIES = () -> Arrays.asList(SpellPropertiesPM.NON_ZERO_DURATION.get());

    @Override
    protected List<SpellProperty> getPropertiesInner() {
        return PROPERTIES.get();
    }

    protected abstract EntityType<?> getNewEntityType();
    
    @Override
    public void execute(HitResult target, Vec3 burstPoint, SpellPackage spell, Level world, LivingEntity caster, ItemStack spellSource, Entity projectileEntity) {
        if (target != null && target.getType() == HitResult.Type.ENTITY) {
            EntityHitResult entityTarget = (EntityHitResult)target;
            if (SpellManager.canPolymorph(entityTarget.getEntity().getType())) {
                // Create and enqueue an entity swapper for the target entity
                CompoundTag originalData = entityTarget.getEntity().saveWithoutId(new CompoundTag());
                int ticks = 20 * this.getDurationSeconds(spell, spellSource, caster, world.registryAccess());
                EntitySwapper.enqueue(entityTarget.getEntity(), new EntitySwapper(this.getNewEntityType(), originalData, Optional.of(ticks), 0));
            }
        }
    }
    
    @Override
    public Source getSource() {
        return Sources.MOON;
    }

    @Override
    public int getBaseManaCost(SpellPropertyConfiguration properties) {
        return 5 * properties.get(SpellPropertiesPM.NON_ZERO_DURATION.get());
    }
    
    protected abstract SoundEvent getCastSoundEvent();

    @Override
    public void playSounds(Level world, BlockPos origin) {
        world.playSound(null, origin, this.getCastSoundEvent(), SoundSource.PLAYERS, 1.0F, 1.0F + (float)(world.random.nextGaussian() * 0.05D));
    }

    protected int getDurationSeconds(SpellPackage spell, ItemStack spellSource, LivingEntity caster, HolderLookup.Provider registries) {
        return 6 * this.getModdedPropertyValue(SpellPropertiesPM.NON_ZERO_DURATION.get(), spell, spellSource, caster, registries);
    }

    @Override
    public Component getDetailTooltip(SpellPackage spell, ItemStack spellSource, LivingEntity caster, HolderLookup.Provider registries) {
        return Component.translatable("spells.primalmagick.payload." + this.getPayloadType() + ".detail_tooltip", DECIMAL_FORMATTER.format(this.getDurationSeconds(spell, spellSource, caster, registries)));
    }
}
