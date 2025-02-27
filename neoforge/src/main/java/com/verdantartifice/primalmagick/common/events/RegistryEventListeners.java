package com.verdantartifice.primalmagick.common.events;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.platform.registries.GridRewardTypeRegistryServiceNeoforge;
import com.verdantartifice.primalmagick.platform.registries.ProjectMaterialTypeRegistryServiceNeoforge;
import com.verdantartifice.primalmagick.platform.registries.RequirementTypeRegistryServiceNeoforge;
import com.verdantartifice.primalmagick.platform.registries.ResearchKeyTypeRegistryServiceNeoforge;
import com.verdantartifice.primalmagick.platform.registries.ResearchTopicTypeRegistryServiceNeoforge;
import com.verdantartifice.primalmagick.platform.registries.RewardTypeRegistryServiceNeoforge;
import com.verdantartifice.primalmagick.platform.registries.RitualStepTypeRegistryServiceNeoforge;
import com.verdantartifice.primalmagick.platform.registries.SpellModTypeRegistryServiceNeoforge;
import com.verdantartifice.primalmagick.platform.registries.SpellPayloadTypeRegistryServiceNeoforge;
import com.verdantartifice.primalmagick.platform.registries.SpellPropertyRegistryServiceNeoforge;
import com.verdantartifice.primalmagick.platform.registries.SpellVehicleTypeRegistryServiceNeoforge;
import com.verdantartifice.primalmagick.platform.registries.WeightFunctionTypeRegistryServiceNeoforge;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;

/**
 * Neoforge listeners for mod registry-related events.
 *
 * @author Daedalus4096
 */
@EventBusSubscriber(modid = Constants.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class RegistryEventListeners {
    @SubscribeEvent
    public static void onNewRegistry(NewRegistryEvent event) {
        event.register(ResearchKeyTypeRegistryServiceNeoforge.TYPES);   // TODO Is there a better way to do this than direct static service invocation?
        event.register(RequirementTypeRegistryServiceNeoforge.TYPES);
        event.register(ProjectMaterialTypeRegistryServiceNeoforge.TYPES);
        event.register(RewardTypeRegistryServiceNeoforge.TYPES);
        event.register(WeightFunctionTypeRegistryServiceNeoforge.TYPES);
        event.register(SpellPropertyRegistryServiceNeoforge.PROPERTIES);
        event.register(SpellModTypeRegistryServiceNeoforge.TYPES);
        event.register(SpellVehicleTypeRegistryServiceNeoforge.TYPES);
        event.register(SpellPayloadTypeRegistryServiceNeoforge.TYPES);
        event.register(GridRewardTypeRegistryServiceNeoforge.TYPES);
        event.register(ResearchTopicTypeRegistryServiceNeoforge.TYPES);
        event.register(RitualStepTypeRegistryServiceNeoforge.TYPES);
    }

    @SubscribeEvent
    public static void onNewDatapackRegistry(DataPackRegistryEvent.NewRegistry event) {
        RegistryEvents.onNewDatapackRegistry(event::dataPackRegistry);
    }
}
