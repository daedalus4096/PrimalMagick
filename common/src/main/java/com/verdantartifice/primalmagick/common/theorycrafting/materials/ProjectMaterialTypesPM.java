package com.verdantartifice.primalmagick.common.theorycrafting.materials;

import com.mojang.serialization.MapCodec;
import com.verdantartifice.primalmagick.common.registries.IRegistryItem;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

import java.util.function.Supplier;

public class ProjectMaterialTypesPM {
    public static final IRegistryItem<ProjectMaterialType<?>, ProjectMaterialType<ExperienceProjectMaterial>> EXPERIENCE = register("experience", ExperienceProjectMaterial::codec, ExperienceProjectMaterial::streamCodec);
    public static final IRegistryItem<ProjectMaterialType<?>, ProjectMaterialType<ItemProjectMaterial>> ITEM = register("item", ItemProjectMaterial::codec, ItemProjectMaterial::streamCodec);
    public static final IRegistryItem<ProjectMaterialType<?>, ProjectMaterialType<ItemTagProjectMaterial>> ITEM_TAG = register("item_tag", ItemTagProjectMaterial::codec, ItemTagProjectMaterial::streamCodec);
    public static final IRegistryItem<ProjectMaterialType<?>, ProjectMaterialType<ObservationProjectMaterial>> OBSERVATION = register("observation", ObservationProjectMaterial::codec, ObservationProjectMaterial::streamCodec);
    
    protected static <T extends AbstractProjectMaterial<T>> IRegistryItem<ProjectMaterialType<?>, ProjectMaterialType<T>> register(String id, Supplier<MapCodec<T>> codecSupplier, Supplier<StreamCodec<? super RegistryFriendlyByteBuf, T>> streamCodecSupplier) {
        return Services.PROJECT_MATERIAL_TYPES.register(id, () -> new ProjectMaterialType<T>(ResourceUtils.loc(id), codecSupplier, streamCodecSupplier));
    }
}
