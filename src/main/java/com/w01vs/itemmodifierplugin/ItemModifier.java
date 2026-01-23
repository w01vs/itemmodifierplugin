package com.w01vs.itemmodifierplugin;

import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;


public class ItemModifier {
    public static final BuilderCodec<ItemModifier> CODEC = BuilderCodec.builder(ItemModifier.class, ItemModifier::new)
            .append(new KeyedCodec<>("Id", BuilderCodec.STRING), (d, v) -> d.id = v, d -> d.id)
            .add()
            .append(new KeyedCodec<>("MinValue", BuilderCodec.INTEGER), (d, v) -> d.minValue = v, d -> d.minValue)
            .add()
            .append(new KeyedCodec<>("MaxValue", BuilderCodec.INTEGER), (d, v) -> d.maxValue = v, d -> d.maxValue)
            .add()
            .append(new KeyedCodec<>("HasRange", BuilderCodec.BOOLEAN), (d, v) -> d.hasRange = v, d -> d.hasRange)
            .add()
            .build();

    public static final String codecKey = "ItemModifiers";

    private String id;
    private int minValue;
    private int maxValue;
    // if hasRange = false -> minValue is used.
    private boolean hasRange;

    public ItemModifier() {
        id = "unknown";
        minValue = 0;
        maxValue = 0;
        hasRange = false;
    }

    public ItemModifier(String id, int value) {
        this.id = id;
        minValue = value;
        maxValue = 0;
        hasRange = false;
    }

    public ItemModifier(String id, int minValue, int maxValue) {
        this.id = id;
        this.minValue = minValue;
        this.maxValue = maxValue;
        hasRange = true;
    }

    public String getId() { return id; }

    public int getMinValue() { return minValue; }

    public int getMaxValue() { return maxValue; }

    public boolean getHasRange() { return hasRange; }

    protected ItemModifier(ItemModifier other) {
        this.id = other.id;
        this.minValue = other.minValue;
        this.maxValue = other.maxValue;
        this.hasRange = other.hasRange;
    }

    public ItemModifier clone() {
        return new ItemModifier(this);
    }
}
