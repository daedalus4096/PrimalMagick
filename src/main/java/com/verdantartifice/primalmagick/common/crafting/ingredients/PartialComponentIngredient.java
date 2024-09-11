package com.verdantartifice.primalmagick.common.crafting.ingredients;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.ingredients.AbstractIngredient;
import net.minecraftforge.common.crafting.ingredients.IIngredientSerializer;
import net.minecraftforge.registries.ForgeRegistries;

public class PartialComponentIngredient extends AbstractIngredient {
    private final List<Item> items;
    private final DataComponentPatch components;
    
    private PartialComponentIngredient(List<Item> items, DataComponentPatch components) {
        super(items.stream().map(item -> {
            ItemStack stack = new ItemStack(item);
            stack.applyComponents(components);
            return new Ingredient.ItemValue(stack);
        }));
        
        if (items.isEmpty()) {
            throw new IllegalArgumentException("Cannot create a PartialComponentIngredient with no items");
        }
        
        this.items = Collections.unmodifiableList(items);
        this.components = components;
    }
    
    @Override
    public boolean test(ItemStack pStack) {
        // If the given stack is empty or not contained in the ingredient's item list, then fail the test
        if (pStack == null || !this.items.contains(pStack.getItem())) {
            return false;
        }
        
        // Perform a partial equality test on the given stack's component map; only consider data components
        // in this ingredient's component map
        for (var entry : this.components.entrySet()) {
            if (entry.getValue().isEmpty() && pStack.has(entry.getKey())) {
                // If this ingredient's component map requires the stack have that component undefined, but it
                // is defined, then fail the test
                return false;
            } else if (entry.getValue().filter(obj -> obj.equals(pStack.get(entry.getKey()))).isEmpty()) {
                // If this ingredient's component map requires that the stack have the component defined, but
                // it is missing or not a match, then fail the test
                return false;
            }
        }
        
        // If all relevant data components are a match, then pass the test
        return true;
    }

    @Override
    public boolean isSimple() {
        return false;
    }

    @Override
    public IIngredientSerializer<? extends Ingredient> serializer() {
        return SERIALIZER;
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public static final MapCodec<PartialComponentIngredient> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ForgeRegistries.ITEMS.getCodec().listOf().fieldOf("items").forGetter(i -> i.items),
            DataComponentPatch.CODEC.optionalFieldOf("components", DataComponentPatch.EMPTY).forGetter(i -> i.components)
        ).apply(instance, PartialComponentIngredient::new));

    public static final IIngredientSerializer<PartialComponentIngredient> SERIALIZER = new IIngredientSerializer<>() {
        @Override
        public MapCodec<? extends PartialComponentIngredient> codec() {
            return CODEC;
        }

        @Override
        public PartialComponentIngredient read(RegistryFriendlyByteBuf buffer) {
            List<Item> items = buffer.readList(b -> Item.STREAM_CODEC.decode(buffer).get());
            DataComponentPatch components = DataComponentPatch.STREAM_CODEC.decode(buffer);
            return new PartialComponentIngredient(items, components);
        }

        @SuppressWarnings("deprecation")
        @Override
        public void write(RegistryFriendlyByteBuf buffer, PartialComponentIngredient value) {
            buffer.writeCollection(value.items, (b, item) -> Item.STREAM_CODEC.encode(buffer, item.builtInRegistryHolder()));
            DataComponentPatch.STREAM_CODEC.encode(buffer, value.components);
        }
    };
    
    public static class Builder {
        private final List<Item> items = new ArrayList<>();
        private final DataComponentPatch.Builder componentBuilder = DataComponentPatch.builder();
        
        public Builder item(ItemLike itemLike) {
            this.items.add(itemLike.asItem());
            return this;
        }
        
        public Builder items(ItemLike... values) {
            for (ItemLike val : values) {
                this.items.add(val.asItem());
            }
            return this;
        }
        
        public <T> Builder data(DataComponentType<T> componentType, T componentValue) {
            this.componentBuilder.set(componentType, componentValue);
            return this;
        }
        
        public PartialComponentIngredient build() {
            if (this.items.isEmpty()) {
                throw new IllegalStateException("No items added");
            }
            
            DataComponentPatch components = this.componentBuilder.build();
            if (components.isEmpty()) {
                throw new IllegalStateException("No data components added");
            }
            
            return new PartialComponentIngredient(this.items, components);
        }
    }
}
