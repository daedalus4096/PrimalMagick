package com.verdantartifice.primalmagick.common.theorycrafting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.theorycrafting.materials.AbstractProjectMaterial;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.Objects;

/**
 * An instance of a material in an initialized theorycrafting project.  Tracks whether the material
 * has been selected for use.
 * 
 * @author Daedalus4096
 */
public class MaterialInstance {
    public static Codec<MaterialInstance> codec() {
        return RecordCodecBuilder.create(instance -> instance.group(
                AbstractProjectMaterial.dispatchCodec().fieldOf("materialDefinition").forGetter(MaterialInstance::getMaterialDefinition),
                Codec.BOOL.fieldOf("selected").forGetter(MaterialInstance::isSelected)
            ).apply(instance, MaterialInstance::new));
    }
    
    public static StreamCodec<RegistryFriendlyByteBuf, MaterialInstance> streamCodec() {
        return StreamCodec.composite(
                AbstractProjectMaterial.dispatchStreamCodec(),
                MaterialInstance::getMaterialDefinition,
                ByteBufCodecs.BOOL,
                MaterialInstance::isSelected,
                MaterialInstance::new);
    }
    
    protected final AbstractProjectMaterial<?> materialDefinition;
    protected boolean selected = false;
    
    public MaterialInstance(AbstractProjectMaterial<?> materialDefinition) {
        this(materialDefinition, false);
    }
    
    protected MaterialInstance(AbstractProjectMaterial<?> materialDefinition, boolean selected) {
        this.materialDefinition = materialDefinition;
        this.selected = selected;
    }
    
    public AbstractProjectMaterial<?> getMaterialDefinition() {
        return this.materialDefinition;
    }
    
    public boolean isSelected() {
        return this.selected;
    }
    
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof MaterialInstance that)) return false;
        return selected == that.selected && Objects.equals(materialDefinition, that.materialDefinition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(materialDefinition, selected);
    }
}
