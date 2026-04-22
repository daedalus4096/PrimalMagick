package com.verdantartifice.primalmagick.common.capabilities;

import com.verdantartifice.primalmagick.common.research.keys.AbstractResearchKey;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

/**
 * Default implementation of the tile research cache capability.
 * 
 * @author Daedalus4096
 */
public class TileResearchCache implements ITileResearchCache {
    private final Set<AbstractResearchKey<?>> research = ConcurrentHashMap.newKeySet();

    @Override
    public void serialize(ValueOutput output) {
        ValueOutput.TypedOutputList<AbstractResearchKey<?>> childList = output.list("Research", AbstractResearchKey.dispatchCodec());
        this.research.forEach(childList::add);
    }

    @Override
    public void deserialize(ValueInput input) {
        this.clear();
        input.listOrEmpty("Research", AbstractResearchKey.dispatchCodec()).stream().forEach(this.research::add);
    }

    @Override
    public void clear() {
        this.research.clear();
    }

    @Override
    public boolean isResearchComplete(AbstractResearchKey<?> key) {
        if (key == null) {
            return false;
        } else {
            return this.research.contains(key);
        }
    }

    @Override
    public boolean isResearchComplete(List<AbstractResearchKey<?>> keys) {
        return keys.stream().allMatch(this::isResearchComplete);
    }
    
    @Override
    public void update(Player player, Predicate<AbstractResearchKey<?>> researchFilter) {
        Services.CAPABILITIES.knowledge(player).ifPresent(knowledge -> {
            this.clear();
            RegistryAccess registryAccess = player.level().registryAccess();
            for (AbstractResearchKey<?> key : knowledge.getResearchSet()) {
                if (knowledge.isResearchComplete(registryAccess, key) && (researchFilter == null || researchFilter.test(key))) {
                    this.research.add(key);
                }
            }
        });
    }
}
