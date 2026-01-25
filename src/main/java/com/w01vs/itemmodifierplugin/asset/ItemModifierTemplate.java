package com.w01vs.itemmodifierplugin.asset;

import com.hypixel.hytale.assetstore.JsonAsset;
import com.hypixel.hytale.assetstore.codec.AssetBuilderCodec;
import com.hypixel.hytale.assetstore.codec.AssetCodec;
import com.hypixel.hytale.assetstore.map.JsonAssetWithMap;
import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.codecs.array.ArrayCodec;
import com.hypixel.hytale.codec.codecs.map.MapCodec;
import com.hypixel.hytale.common.util.MapUtil;
import com.hypixel.hytale.logger.HytaleLogger;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

import javax.annotation.Nonnull;
import java.util.Map;


public class ItemModifierTemplate implements JsonAssetWithMap<String, ModifierAssetMap>, JsonAsset<String> {
    private static final AssetBuilderCodec.Builder<String, ItemModifierTemplate> CODEC_BUILDER = AssetBuilderCodec.builder(
                    ItemModifierTemplate.class,
                    ItemModifierTemplate::new,
                    Codec.STRING,
                    (mod, id) -> mod.id = id,
                    mod -> mod.id,
                    (mod, data) -> {}, // Data field if applicable
                    mod -> null
            )
            // 2. Additive Map with Inheritance
            .<Map<String, ValueProvider>>appendInherited(
                    new KeyedCodec<>("Additive", new MapCodec<>(ValueProvider.CODEC, Object2ObjectOpenHashMap::new)),
                    (mod, v) -> mod.additive = v,
                    mod -> mod.additive,
                    (mod, parent) -> mod.additive = MapUtil.combineUnmodifiable(mod.additive, parent.additive)
            )
            .documentation("Additive stat changes (e.g., +5 Physical Damage). Supports inheritance from parent modifiers.")
            .add()

            // 3. Multiplicative Map with Inheritance
            .<Map<String, ValueProvider>>appendInherited(
                    new KeyedCodec<>("Multiplicative", new MapCodec<>(ValueProvider.CODEC, Object2ObjectOpenHashMap::new)),
                    (mod, v) -> mod.multiplicative = v,
                    mod -> mod.multiplicative,
                    (mod, parent) -> mod.multiplicative = MapUtil.combineUnmodifiable(mod.multiplicative, parent.multiplicative)
            )
            .documentation("Multiplicative stat changes (e.g., x1.2 Attack Speed).")
            .add();

    public static final AssetCodec<String, ItemModifierTemplate> ASSET_CODEC = CODEC_BUILDER.build();

    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();
    private String id;
    private Map<String, ValueProvider> additive = new Object2ObjectOpenHashMap<>();
    private Map<String, ValueProvider> multiplicative = new Object2ObjectOpenHashMap<>();

    public Map<String, ValueProvider> getAdditive() {
        Map<String, ValueProvider> additiveCopy = new Object2ObjectOpenHashMap<>(additive.size());
        for (Map.Entry<String, ValueProvider> entry : additive.entrySet()) {
            additiveCopy.put(entry.getKey(), new ValueProvider(entry.getValue()));
        }
        return additiveCopy;
    }

    public  Map<String, ValueProvider> getMultiplicative() {
        Map<String, ValueProvider> multiplicativeCopy = new Object2ObjectOpenHashMap<>(multiplicative.size());
        for (Map.Entry<String, ValueProvider> entry : multiplicative.entrySet()) {
            multiplicativeCopy.put(entry.getKey(), new ValueProvider(entry.getValue()));
        }
        return multiplicativeCopy;
    }


    public ItemModifierTemplate() {
        id = "unknown";
    }

    public ItemModifierTemplate(@Nonnull String id, @Nonnull String damageType, @Nonnull ModifierType modType, @Nonnull Float value) {
        this.id = id;
        switch(modType) {
            case ModifierType.ADDITIVE -> additive.put(damageType, new ValueProvider(value));
            case ModifierType.MULTIPLICATIVE -> multiplicative.put(damageType, new ValueProvider(value));
        }
    }

    public ItemModifierTemplate(@Nonnull String id, @Nonnull String damageType, @Nonnull ModifierType modType, @Nonnull Float min, @Nonnull Float max) {
        this.id = id;
        switch(modType) {
            case ModifierType.ADDITIVE -> additive.put(damageType, new ValueProvider(min, max));
            case ModifierType.MULTIPLICATIVE -> multiplicative.put(damageType, new ValueProvider(min, max));
        }
    }

    public String getId() { return id; }

    protected ItemModifierTemplate(ItemModifierTemplate other) {
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

    public ItemModifierTemplate clone() {
        return new ItemModifierTemplate(this);
    }

    public enum ModifierType {
        MULTIPLICATIVE,
        ADDITIVE
    }
}
