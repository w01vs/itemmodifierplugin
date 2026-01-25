package com.w01vs.itemmodifierplugin.asset;

import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.math.random.RandomExtra;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Supplier;

public class ValueProvider extends ItemModifierTemplate.ModifierRangeGroup implements Supplier<Float> {
    // Initialize as null to represent "not present"
    @Nullable private Float value = null;
    @Nullable private Float min = null;
    @Nullable private Float max = null;

    public static final BuilderCodec<ValueProvider> CODEC = BuilderCodec.builder(ValueProvider.class, ValueProvider::new)
            .append(new KeyedCodec<>("Value", BuilderCodec.FLOAT), (p, v) -> p.value = v, p -> p.value)
            .add()
            .append(new KeyedCodec<>("Min", BuilderCodec.FLOAT), (p, v) -> p.min = v, p -> p.min)
            .add()
            .append(new KeyedCodec<>("Max", BuilderCodec.FLOAT), (p, v) -> p.max = v, p -> p.max)
            .add()
            .build();

    public ValueProvider() {}

    public ValueProvider(@Nonnull Float value) {
        this.value = value;
    }

    public ValueProvider(@Nonnull Float min, @Nonnull Float max) {
        this.min = min;
        this.max = max;
    }

    public Float get() {
        if (min != null && max != null) {
            return min + (float)(RandomExtra.randomRange(min, max));
        }
        return value != null ? value : 0.0f;
    }

    protected ValueProvider(ValueProvider other) {
        this.value = other.value;
        this.min = other.min;
        this.max = other.max;
    }

    public ValueProvider clone() {
        return new ValueProvider(this);
    }
}
