package com.verdantartifice.primalmagick.datagen.models;

public interface ITransformVecBuilderPM {
    ITransformVecBuilderPM translation(float x, float y, float z);
    ITransformVecBuilderPM leftRotation(float x, float y, float z);
    ITransformVecBuilderPM rightRotation(float x, float y, float z);
    ITransformVecBuilderPM scale(float x, float y, float z);
    ITransformVecBuilderPM scale(float sc);
}
