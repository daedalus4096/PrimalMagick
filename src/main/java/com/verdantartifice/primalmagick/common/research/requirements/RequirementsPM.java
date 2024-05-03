package com.verdantartifice.primalmagick.common.research.requirements;

import java.util.function.Supplier;

import com.mojang.serialization.Codec;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;

import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;

public class RequirementsPM {
    private static final DeferredRegister<RequirementType<?>> DEFERRED_TYPES = DeferredRegister.create(RegistryKeysPM.REQUIREMENT_TYPES, PrimalMagick.MODID);
    public static final Supplier<IForgeRegistry<RequirementType<?>>> TYPES = DEFERRED_TYPES.makeRegistry(RegistryBuilder::new);
    
    public static final RegistryObject<RequirementType<ResearchRequirement>> RESEARCH = register("research", ResearchRequirement.CODEC);
    
    protected static <T extends AbstractRequirement> RegistryObject<RequirementType<T>> register(String id, Codec<T> codec) {
        return DEFERRED_TYPES.register(id, () -> new RequirementType<T>(codec));
    }
}
