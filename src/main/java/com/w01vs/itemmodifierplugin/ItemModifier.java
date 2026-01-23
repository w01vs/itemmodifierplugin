package com.w01vs.itemmodifierplugin;

import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.codecs.array.ArrayCodec;
import com.hypixel.hytale.component.Holder;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import java.util.ArrayList;

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

    private String id = "unknown:modifier";
    private int minValue = 0;
    private int maxValue = 0;
    // if hasRange = false -> minValue is used.
    private boolean hasRange = false;

    public ItemModifier() {}

    public ItemModifier(String id, int value) {
        this.id = id;
        minValue = value;
        hasRange = false;
    }

    public ItemModifier(String id, int minValue, int maxValue) {
        this.id = id;
        this.minValue = minValue;
        this.maxValue = maxValue;
        hasRange = true;
    }

    public String getId() {
        return id;
    }

    public int getMinValue() {
        return minValue;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public boolean getHasRange() {
        return hasRange;
    }
}
