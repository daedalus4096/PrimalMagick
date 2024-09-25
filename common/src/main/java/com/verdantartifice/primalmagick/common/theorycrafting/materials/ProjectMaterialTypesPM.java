package com.verdantartifice.primalmagick.common.theorycrafting.materials;

import java.util.function.Supplier;

import com.mojang.serialization.MapCodec;
import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;

import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;

public class ProjectMaterialTypesPM {
    private static final DeferredRegister<ProjectMaterialType<?>> DEFERRED_TYPES = DeferredRegister.create(RegistryKeysPM.PROJECT_MATERIAL_TYPES, Constants.MOD_ID);
    public static final Supplier<IForgeRegistry<ProjectMaterialType<?>>> TYPES = DEFERRED_TYPES.makeRegistry(RegistryBuilder::new);
    
    public static void init() {
        DEFERRED_TYPES.register(PrimalMagick.getModLoadingContext().getModEventBus());
    }
    
    public static final RegistryObject<ProjectMaterialType<ExperienceProjectMaterial>> EXPERIENCE = register("experience", ExperienceProjectMaterial::codec, ExperienceProjectMaterial::streamCodec);
    public static final RegistryObject<ProjectMaterialType<ItemProjectMaterial>> ITEM = register("item", ItemProjectMaterial::codec, ItemProjectMaterial::streamCodec);
    public static final RegistryObject<ProjectMaterialType<ItemTagProjectMaterial>> ITEM_TAG = register("item_tag", ItemTagProjectMaterial::codec, ItemTagProjectMaterial::streamCodec);
    public static final RegistryObject<ProjectMaterialType<ObservationProjectMaterial>> OBSERVATION = register("observation", ObservationProjectMaterial::codec, ObservationProjectMaterial::streamCodec);
    
    protected static <T extends AbstractProjectMaterial<T>> RegistryObject<ProjectMaterialType<T>> register(String id, Supplier<MapCodec<T>> codecSupplier, Supplier<StreamCodec<? super RegistryFriendlyByteBuf, T>> streamCodecSupplier) {
        return DEFERRED_TYPES.register(id, () -> new ProjectMaterialType<T>(ResourceUtils.loc(id), codecSupplier, streamCodecSupplier));
    }
}
