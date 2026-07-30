package com.verdantartifice.primalmagick.datagen.models;

import net.neoforged.neoforge.client.model.generators.template.TransformVecBuilder;
import org.joml.Vector3f;

import java.util.Optional;

public class TransformVecBuilderPMNeoforge implements ITransformVecBuilderPM {
    private Optional<Vector3f> translationOpt = Optional.empty();
    private Optional<Vector3f> leftRotOpt = Optional.empty();
    private Optional<Vector3f> rightRotOpt = Optional.empty();
    private Optional<Vector3f> scaleOpt = Optional.empty();

    @Override
    public ITransformVecBuilderPM translation(float x, float y, float z) {
        this.translationOpt = Optional.of(new Vector3f(x, y, z));
        return this;
    }

    @Override
    public ITransformVecBuilderPM leftRotation(float x, float y, float z) {
        this.leftRotOpt = Optional.of(new Vector3f(x, y, z));
        return this;
    }

    @Override
    public ITransformVecBuilderPM rightRotation(float x, float y, float z) {
        this.rightRotOpt = Optional.of(new Vector3f(x, y, z));
        return this;
    }

    @Override
    public ITransformVecBuilderPM scale(float x, float y, float z) {
        this.scaleOpt = Optional.of(new Vector3f(x, y, z));
        return this;
    }

    @Override
    public ITransformVecBuilderPM scale(float sc) {
        return this.scale(sc, sc, sc);
    }

    public void exportTo(TransformVecBuilder other) {
        this.translationOpt.ifPresent(vec -> other.translation(vec.x(), vec.y(), vec.z()));
        this.leftRotOpt.ifPresent(vec -> other.rotation(vec.x(), vec.y(), vec.z()));
        this.rightRotOpt.ifPresent(vec -> other.rightRotation(vec.x(), vec.y(), vec.z()));
        this.scaleOpt.ifPresent(vec -> other.scale(vec.x(), vec.y(), vec.z()));
    }
}
