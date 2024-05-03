package com.verdantartifice.primalmagick.common.research.requirements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.mojang.serialization.Codec;

import net.minecraft.world.entity.player.Player;

/**
 * Compound requirement that is met if the player has completed all of the component requirements.
 * 
 * @author Daedalus4096
 */
public class AndRequirement extends AbstractRequirement {
    public static final Codec<AndRequirement> CODEC = AbstractRequirement.CODEC.listOf().fieldOf("components").xmap(AndRequirement::new, req -> req.components).codec();
    
    protected final List<AbstractRequirement> components = new ArrayList<>();
    
    public AndRequirement(List<AbstractRequirement> components) {
        this.components.addAll(components);
    }
    
    public AndRequirement(AbstractRequirement... components) {
        this(Arrays.asList(components));
    }

    @Override
    public boolean isMetBy(Player player) {
        if (player == null) {
            return false;
        } else {
            return this.components.stream().allMatch(req -> req.isMetBy(player));
        }
    }

    @Override
    protected RequirementType<?> getType() {
        return RequirementsPM.AND.get();
    }
}
