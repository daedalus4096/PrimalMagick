package com.verdantartifice.primalmagick.common.events;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.common.books.grids.rewards.GridRewardTypeRegistration;
import com.verdantartifice.primalmagick.common.research.topics.ResearchTopicTypeRegistration;
import com.verdantartifice.primalmagick.common.rituals.steps.RitualStepTypeRegistration;
import com.verdantartifice.primalmagick.common.spells.SpellPropertyRegistration;
import com.verdantartifice.primalmagick.common.spells.mods.SpellModTypeRegistration;
import com.verdantartifice.primalmagick.common.spells.payloads.SpellPayloadTypeRegistration;
import com.verdantartifice.primalmagick.common.spells.vehicles.SpellVehicleTypeRegistration;
import com.verdantartifice.primalmagick.common.theorycrafting.weights.WeightFunctionTypeRegistration;
import com.verdantartifice.primalmagick.platform.registries.ProjectMaterialTypeRegistryServiceNeoforge;
import com.verdantartifice.primalmagick.platform.registries.RequirementTypeRegistryServiceNeoforge;
import com.verdantartifice.primalmagick.platform.registries.ResearchKeyTypeRegistryServiceNeoforge;
import com.verdantartifice.primalmagick.platform.registries.RewardTypeRegistryServiceNeoforge;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;

/**
 * Neoforge listeners for mod registry-related events.
 *
 * @author Daedalus4096
 */
@EventBusSubscriber(modid = Constants.MOD_ID)
public class RegistryEventListeners {
    @SubscribeEvent
    public static void onNewRegistry(NewRegistryEvent event) {
        event.register(ResearchKeyTypeRegistryServiceNeoforge.TYPES);   // TODO Is there a better way to do this than direct static service invocation?
        event.register(RequirementTypeRegistryServiceNeoforge.TYPES);
        event.register(ProjectMaterialTypeRegistryServiceNeoforge.TYPES);
        event.register(RewardTypeRegistryServiceNeoforge.TYPES);
        event.register(WeightFunctionTypeRegistration.TYPES);
        event.register(SpellPropertyRegistration.PROPERTIES);
        event.register(SpellModTypeRegistration.TYPES);
        event.register(SpellVehicleTypeRegistration.TYPES);
        event.register(SpellPayloadTypeRegistration.TYPES);
        event.register(GridRewardTypeRegistration.TYPES);
        event.register(ResearchTopicTypeRegistration.TYPES);
        event.register(RitualStepTypeRegistration.TYPES);
    }

    @SubscribeEvent
    public static void onNewDatapackRegistry(DataPackRegistryEvent.NewRegistry event) {
        RegistryEvents.onNewDatapackRegistry(event::dataPackRegistry);
    }
}
