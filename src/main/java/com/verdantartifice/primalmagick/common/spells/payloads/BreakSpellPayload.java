package com.verdantartifice.primalmagick.common.spells.payloads;

import java.util.Map;

import com.verdantartifice.primalmagick.common.enchantments.EnchantmentsPM;
import com.verdantartifice.primalmagick.common.misc.BlockBreaker;
import com.verdantartifice.primalmagick.common.research.CompoundResearchKey;
import com.verdantartifice.primalmagick.common.research.ResearchNames;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.spells.SpellPackage;
import com.verdantartifice.primalmagick.common.spells.SpellProperty;

import net.minecraft.core.BlockPos;
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
public class BreakSpellPayload extends AbstractSpellPayload {
    public static final String TYPE = "break";
    protected static final CompoundResearchKey RESEARCH = ResearchNames.SPELL_PAYLOAD_BREAK.get().compoundKey();

    public BreakSpellPayload() {
        super();
    }
    
    public BreakSpellPayload(int power) {
        super();
        this.getProperty("power").setValue(power);
    }
    
    public static CompoundResearchKey getResearch() {
        return RESEARCH;
    }
    
    @Override
    protected Map<String, SpellProperty> initProperties() {
        Map<String, SpellProperty> propMap = super.initProperties();
        propMap.put("power", new SpellProperty("power", "spells.primalmagick.property.power", 1, 5));
        propMap.put("silk_touch", new SpellProperty("silk_touch", "spells.primalmagick.property.silk_touch", 0, 1));
        return propMap;
    }
    
    @Override
    public void execute(HitResult target, Vec3 burstPoint, SpellPackage spell, Level world, LivingEntity caster, ItemStack spellSource, Entity projectileEntity) {
        if (target != null && target.getType() == HitResult.Type.BLOCK && caster instanceof Player) {
            // Create and enqueue a block breaker for the target block
            BlockHitResult blockTarget = (BlockHitResult)target;
            BlockPos pos = blockTarget.getBlockPos();
            BlockState state = world.getBlockState(pos);
            float durability = (float)Math.sqrt(100.0F * state.getDestroySpeed(world, pos));
            boolean silk = (this.getPropertyValue("silk_touch") == 1);
            int treasure = spellSource.getEnchantmentLevel(EnchantmentsPM.TREASURE.get());
            BlockBreaker breaker = new BlockBreaker.Builder().power(this.getModdedPropertyValue("power", spell, spellSource)).target(pos, state).durability(durability).player((Player)caster).tool(spellSource).silkTouch(silk).fortune(treasure).alwaysDrop().build();
            BlockBreaker.schedule(world, 1, breaker);
        }
    }

    @Override
    public Source getSource() {
        return Source.EARTH;
    }

    @Override
    public int getBaseManaCost() {
        return this.getPropertyValue("power") + (5 * this.getPropertyValue("silk_touch"));
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
