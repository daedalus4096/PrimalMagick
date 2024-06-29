package com.verdantartifice.primalmagick.common.util;

import java.util.function.Function;

import com.mojang.datafixers.util.Function7;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.Registry;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

public class StreamCodecUtils {
    public static <B, C, T1, T2, T3, T4, T5, T6, T7> StreamCodec<B, C> composite(
            final StreamCodec<? super B, T1> pCodec1,
            final Function<C, T1> pGetter1,
            final StreamCodec<? super B, T2> pCodec2,
            final Function<C, T2> pGetter2,
            final StreamCodec<? super B, T3> pCodec3,
            final Function<C, T3> pGetter3,
            final StreamCodec<? super B, T4> pCodec4,
            final Function<C, T4> pGetter4,
            final StreamCodec<? super B, T5> pCodec5,
            final Function<C, T5> pGetter5,
            final StreamCodec<? super B, T6> pCodec6,
            final Function<C, T6> pGetter6,
            final StreamCodec<? super B, T7> pCodec7,
            final Function<C, T7> pGetter7,
            final Function7<T1, T2, T3, T4, T5, T6, T7, C> pFactory
        ) {
            return new StreamCodec<B, C>() {
                @Override
                public C decode(B buf) {
                    T1 t1 = pCodec1.decode(buf);
                    T2 t2 = pCodec2.decode(buf);
                    T3 t3 = pCodec3.decode(buf);
                    T4 t4 = pCodec4.decode(buf);
                    T5 t5 = pCodec5.decode(buf);
                    T6 t6 = pCodec6.decode(buf);
                    T7 t7 = pCodec7.decode(buf);
                    return pFactory.apply(t1, t2, t3, t4, t5, t6, t7);
                }

                @Override
                public void encode(B buf, C value) {
                    pCodec1.encode(buf, pGetter1.apply(value));
                    pCodec2.encode(buf, pGetter2.apply(value));
                    pCodec3.encode(buf, pGetter3.apply(value));
                    pCodec4.encode(buf, pGetter4.apply(value));
                    pCodec5.encode(buf, pGetter5.apply(value));
                    pCodec6.encode(buf, pGetter6.apply(value));
                    pCodec7.encode(buf, pGetter7.apply(value));
                }
            };
        }

    public static <T> StreamCodec<ByteBuf, TagKey<T>> tagKey(ResourceKey<? extends Registry<T>> pRegistry) {
        return ResourceLocation.STREAM_CODEC.map(loc -> TagKey.create(pRegistry, loc), TagKey::location);
    }
}
