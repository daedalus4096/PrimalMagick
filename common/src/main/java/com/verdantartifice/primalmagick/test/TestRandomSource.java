package com.verdantartifice.primalmagick.test;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.PositionalRandomFactory;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class TestRandomSource implements RandomSource {
    private final Optional<Boolean> nextBoolean;
    private final Optional<Double> nextDouble;
    private final Optional<Float> nextFloat;
    private final Optional<Double> nextGaussian;
    private final Optional<Integer> nextInt;
    private final Optional<Long> nextLong;

    private TestRandomSource(Optional<Boolean> nextBoolean, Optional<Double> nextDouble, Optional<Float> nextFloat, Optional<Double> nextGaussian, Optional<Integer> nextInt, Optional<Long> nextLong) {
        this.nextBoolean = nextBoolean;
        this.nextDouble = nextDouble;
        this.nextFloat = nextFloat;
        this.nextGaussian = nextGaussian;
        this.nextInt = nextInt;
        this.nextLong = nextLong;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public @NotNull RandomSource fork() {
        return new TestRandomSource(this.nextBoolean, this.nextDouble, this.nextFloat, this.nextGaussian, this.nextInt, this.nextLong);
    }

    @Override
    public @NotNull PositionalRandomFactory forkPositional() {
        throw new NotImplementedException();
    }

    @Override
    public void setSeed(long l) {
        // Do nothing
    }

    @Override
    public int nextInt() {
        return this.nextInt.orElseThrow(IllegalStateException::new);
    }

    @Override
    public int nextInt(int i) {
        return this.nextInt.orElseThrow(IllegalStateException::new);
    }

    @Override
    public long nextLong() {
        return this.nextLong.orElseThrow(IllegalStateException::new);
    }

    @Override
    public boolean nextBoolean() {
        return this.nextBoolean.orElseThrow(IllegalStateException::new);
    }

    @Override
    public float nextFloat() {
        return this.nextFloat.orElseThrow(IllegalStateException::new);
    }

    @Override
    public double nextDouble() {
        return this.nextDouble.orElseThrow(IllegalStateException::new);
    }

    @Override
    public double nextGaussian() {
        return this.nextGaussian.orElseThrow(IllegalStateException::new);
    }

    public static class Builder {
        private Optional<Boolean> nextBoolean = Optional.empty();
        private Optional<Double> nextDouble = Optional.empty();
        private Optional<Float> nextFloat = Optional.empty();
        private Optional<Double> nextGaussian = Optional.empty();
        private Optional<Integer> nextInt = Optional.empty();
        private Optional<Long> nextLong = Optional.empty();

        public Builder setBoolean(boolean b) {
            this.nextBoolean = Optional.of(b);
            return this;
        }

        public Builder setDouble(double d) {
            this.nextDouble = Optional.of(d);
            return this;
        }

        public Builder setFloat(float f) {
            this.nextFloat = Optional.of(f);
            return this;
        }

        public Builder setGaussian(double d) {
            this.nextGaussian = Optional.of(d);
            return this;
        }

        public Builder setInt(int i) {
            this.nextInt = Optional.of(i);
            return this;
        }

        public Builder setLong(long l) {
            this.nextLong = Optional.of(l);
            return this;
        }

        public TestRandomSource build() {
            return new TestRandomSource(this.nextBoolean, this.nextDouble, this.nextFloat, this.nextGaussian, this.nextInt, this.nextLong);
        }
    }
}
