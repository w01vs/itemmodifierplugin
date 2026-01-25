package com.w01vs.itemmodifierplugin.asset;

import com.hypixel.hytale.assetstore.JsonAsset;
import com.hypixel.hytale.assetstore.codec.AssetBuilderCodec;
import com.hypixel.hytale.assetstore.codec.AssetCodec;
import com.hypixel.hytale.assetstore.map.JsonAssetWithMap;
import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.codecs.array.ArrayCodec;
import com.hypixel.hytale.codec.codecs.map.MapCodec;
import com.hypixel.hytale.common.util.MapUtil;
import com.hypixel.hytale.logger.HytaleLogger;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

import javax.annotation.Nonnull;
import java.util.Map;


public class ItemModifier {
    public static final BuilderCodec<ItemModifier> CODEC = BuilderCodec.builder(ItemModifier.class, ItemModifier::new)
            .append(new KeyedCodec<>("Id", BuilderCodec.STRING), (mod, v) -> mod.id = v, mod -> mod.id)
            .add()
            .<Map<String, ValueProvider>>append(
                    new KeyedCodec<>("Additive", new MapCodec<>(ValueProvider.CODEC, Object2ObjectOpenHashMap::new)),
                    (mod, v) -> mod.additive = v,
                    mod -> mod.additive
            )
            .add()
            .<Map<String, ValueProvider>>append(
                    new KeyedCodec<>("Multiplicative", new MapCodec<>(ValueProvider.CODEC, Object2ObjectOpenHashMap::new)),
                    (mod, v) -> mod.multiplicative = v,
                    mod -> mod.multiplicative
            )
            .add()
            .build();

    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();
    private String id;
    private Map<String, ValueProvider> additive = new Object2ObjectOpenHashMap<>();
    private Map<String, ValueProvider> multiplicative = new Object2ObjectOpenHashMap<>();
    public static final ArrayCodec<ItemModifier> ARRAY_CODEC = new ArrayCodec<ItemModifier>(ItemModifier.CODEC, ItemModifier[]::new);

    public static final String codecKey = "ItemModifiers";

    public ItemModifier() {
        id = "unknown";
    }

    public ItemModifier(@Nonnull String id, @Nonnull String damageType, @Nonnull ModifierType modType, @Nonnull Float value) {
        this.id = id;
        switch(modType) {
            case ModifierType.ADDITIVE -> additive.put(damageType, new ValueProvider(value));
            case ModifierType.MULTIPLICATIVE -> multiplicative.put(damageType, new ValueProvider(value));
        }
    }

    public ItemModifier(@Nonnull String id, @Nonnull String damageType, @Nonnull ModifierType modType, @Nonnull Float min, @Nonnull Float max) {
        this.id = id;
        switch(modType) {
            case ModifierType.ADDITIVE -> additive.put(damageType, new ValueProvider(min, max));
            case ModifierType.MULTIPLICATIVE -> multiplicative.put(damageType, new ValueProvider(min, max));
        }
    }

    public String getId() { return id; }

    public Map<String, ValueProvider> getAdditive() { return additive; }

    public  Map<String, ValueProvider> getMultiplicative() { return multiplicative; }

    protected ItemModifier(ItemModifier other) {
        this.id = other.id;
        this.additive = new Object2ObjectOpenHashMap<>(other.additive.size());

        for (Map.Entry<String, ValueProvider> entry : other.additive.entrySet()) {
            this.additive.put(entry.getKey(), new ValueProvider(entry.getValue()));
        }

        this.multiplicative = new Object2ObjectOpenHashMap<>(other.multiplicative.size());
        for (Map.Entry<String, ValueProvider> entry : other.multiplicative.entrySet()) {
            this.multiplicative.put(entry.getKey(), new ValueProvider(entry.getValue()));
        }
    }

    public ItemModifier(ItemModifierTemplate template) {
        this.id = template.getId();
        this.additive = template.getAdditiveRoll();
        this.multiplicative = template.getMultiplicativeRoll();
    }

    public ItemModifier clone() {
        return new ItemModifier(this);
    }

    public enum ModifierType {
        MULTIPLICATIVE,
        ADDITIVE
    }
}
