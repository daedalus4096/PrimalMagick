package com.verdantartifice.primalmagick.common.spells.payloads;

import java.util.Arrays;
import java.util.List;

import com.mojang.serialization.MapCodec;
import com.verdantartifice.primalmagick.common.enchantments.EnchantmentHelperPM;
import com.verdantartifice.primalmagick.common.enchantments.EnchantmentsPM;
import com.verdantartifice.primalmagick.common.misc.BlockBreaker;
import com.verdantartifice.primalmagick.common.research.ResearchEntries;
import com.verdantartifice.primalmagick.common.research.keys.ResearchEntryKey;
import com.verdantartifice.primalmagick.common.research.requirements.AbstractRequirement;
import com.verdantartifice.primalmagick.common.research.requirements.ResearchRequirement;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.sources.Sources;
import com.verdantartifice.primalmagick.common.spells.SpellPackage;
import com.verdantartifice.primalmagick.common.spells.SpellPropertiesPM;
import com.verdantartifice.primalmagick.common.spells.SpellProperty;
import com.verdantartifice.primalmagick.common.spells.SpellPropertyConfiguration;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

/**
 * Definition for a block breaking spell.  Breaks the target block over time without further interaction
 * from the player, with the time required increasing with the block's hardness.  Has no effect on
 * entities.  Has no effect when cast by non-players.
 * 
 * @author Daedalus4096
 * @see {@link com.verdantartifice.primalmagick.common.misc.BlockBreaker}
 */
public class BreakSpellPayload extends AbstractSpellPayload<BreakSpellPayload> {
    public static final BreakSpellPayload INSTANCE = new BreakSpellPayload();
    
    public static final MapCodec<BreakSpellPayload> CODEC = MapCodec.unit(BreakSpellPayload.INSTANCE);
    public static final StreamCodec<ByteBuf, BreakSpellPayload> STREAM_CODEC = StreamCodec.unit(BreakSpellPayload.INSTANCE);
    
    public static final String TYPE = "break";
    protected static final AbstractRequirement<?> REQUIREMENT = new ResearchRequirement(new ResearchEntryKey(ResearchEntries.SPELL_PAYLOAD_BREAK));
    protected static final List<SpellProperty> PROPERTIES = Arrays.asList(SpellPropertiesPM.POWER.get(), SpellPropertiesPM.SILK_TOUCH.get());

    public static AbstractRequirement<?> getRequirement() {
        return REQUIREMENT;
    }
    
    public static BreakSpellPayload getInstance() {
        return INSTANCE;
    }
    
    @Override
    public SpellPayloadType<BreakSpellPayload> getType() {
        return SpellPayloadsPM.BREAK.get();
    }

    @Override
    protected List<SpellProperty> getPropertiesInner() {
        return PROPERTIES;
    }

    @Override
    public void execute(HitResult target, Vec3 burstPoint, SpellPackage spell, Level world, LivingEntity caster, ItemStack spellSource, Entity projectileEntity) {
        if (target != null && target.getType() == HitResult.Type.BLOCK && caster instanceof Player player) {
            // Create and enqueue a block breaker for the target block
            BlockHitResult blockTarget = (BlockHitResult)target;
            BlockPos pos = blockTarget.getBlockPos();
            BlockState state = world.getBlockState(pos);
            float durability = (float)Math.sqrt(100.0F * state.getDestroySpeed(world, pos));
            boolean silk = (spell.payload().getPropertyValue(SpellPropertiesPM.SILK_TOUCH.get()) == 1);
            int treasure = EnchantmentHelperPM.getEnchantmentLevel(spellSource, EnchantmentsPM.TREASURE, world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT));
            BlockBreaker breaker = new BlockBreaker.Builder().power(this.getModdedPropertyValue(SpellPropertiesPM.POWER.get(), spell, spellSource, world.registryAccess()))
                    .target(pos, state).durability(durability).player(player).tool(spellSource).silkTouch(silk).fortune(treasure).alwaysDrop().build();
            BlockBreaker.schedule(world, 1, breaker);
        }
    }

    @Override
    public Source getSource() {
        return Sources.EARTH;
    }

    @Override
    public int getBaseManaCost(SpellPropertyConfiguration properties) {
        return properties.get(SpellPropertiesPM.POWER.get()) + (5 * properties.get(SpellPropertiesPM.SILK_TOUCH.get()));
    }

    @Override
    public void playSounds(Level world, BlockPos origin) {
        world.playSound(null, origin, SoundEvents.UI_STONECUTTER_TAKE_RESULT, SoundSource.PLAYERS, 1.0F, 1.0F + (float)(world.random.nextGaussian() * 0.05D));
    }

    @Override
    protected String getPayloadType() {
        return TYPE;
    }
}
