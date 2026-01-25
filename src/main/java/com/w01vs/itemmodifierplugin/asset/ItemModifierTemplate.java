package com.w01vs.itemmodifierplugin.asset;

import com.hypixel.hytale.assetstore.JsonAsset;
import com.hypixel.hytale.assetstore.codec.AssetBuilderCodec;
import com.hypixel.hytale.assetstore.codec.AssetCodec;
import com.hypixel.hytale.assetstore.map.JsonAssetWithMap;
import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.codecs.map.MapCodec;
import com.hypixel.hytale.common.util.MapUtil;
import com.hypixel.hytale.logger.HytaleLogger;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

import java.nio.file.WatchEvent;
import java.util.Map;


public class ItemModifierTemplate implements JsonAssetWithMap<String, ModifierAssetMap>, JsonAsset<String> {
    private static final AssetBuilderCodec.Builder<String, ItemModifierTemplate> CODEC_BUILDER = AssetBuilderCodec.builder(
                    ItemModifierTemplate.class,
                    ItemModifierTemplate::new,
                    Codec.STRING,
                    (mod, id) -> mod.id = id,
                    mod -> mod.id,
                    (mod, data) -> {},
                    mod -> null
            )
            .<Map<String, ModifierRangeGroup>>appendInherited(
                    new KeyedCodec<>("Additive", new MapCodec<>(ModifierRangeGroup.CODEC, Object2ObjectOpenHashMap::new)),
                    (mod, v) -> mod.additive = v,
                    mod -> mod.additive,
                    (mod, parent) -> mod.additive = MapUtil.combineUnmodifiable(mod.additive, parent.additive)
            )
            .add()
            .<Map<String, ModifierRange>>appendInherited(
                    new KeyedCodec<>("Multiplicative", new MapCodec<>(ModifierRange.CODEC, Object2ObjectOpenHashMap::new)),
                    (mod, v) -> mod.multiplicative = v,
                    mod -> mod.multiplicative,
                    (mod, parent) -> mod.multiplicative = MapUtil.combineUnmodifiable(mod.multiplicative, parent.multiplicative)
            )
            .add();

    public static final AssetCodec<String, ItemModifierTemplate> ASSET_CODEC = CODEC_BUILDER.build();

    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();
    private String id;
    private Map<String, ModifierRangeGroup> additive = new Object2ObjectOpenHashMap<>();
    private Map<String, ModifierRange> multiplicative = new Object2ObjectOpenHashMap<>();

    public ItemModifier getItemModifier() {
        return new ItemModifier(this);
    }


    public Map<String, ValueProvider> getAdditiveRoll() {
        Map<String, ValueProvider> rolls = new Object2ObjectOpenHashMap<>();
        for (Map.Entry<String, ModifierRangeGroup> entry : additive.entrySet()) {
            ModifierRangeGroup group = entry.getValue();
            float rolledLow = group.low.roll();
            float rolledHigh = group.high.roll();
            rolls.put(entry.getKey(), new ValueProvider(rolledLow, rolledHigh));
        }
        return rolls;
    }

    public Map<String, ValueProvider> getMultiplicativeRoll() {
        Map<String, ValueProvider> rolls = new Object2ObjectOpenHashMap<>();
        for (Map.Entry<String, ModifierRange> entry : multiplicative.entrySet()) {
            float rolledValue = entry.getValue().roll();
            rolls.put(entry.getKey(), new ValueProvider(rolledValue));
        }
        return rolls;
    }
    public ItemModifierTemplate() {
        id = "unknown";
    }

    public String getId() { return id; }

    public static class ModifierRangeGroup {
        public ModifierRange low;
        public ModifierRange high;
        public static final BuilderCodec<ModifierRangeGroup> CODEC = BuilderCodec.builder(ModifierRangeGroup.class, ModifierRangeGroup::new)
                .append(new KeyedCodec<>("Low", ModifierRange.CODEC), (g, v) -> g.low = v, g -> g.low)
                .add()
                .append(new KeyedCodec<>("High", ModifierRange.CODEC), (g, v) -> g.high = v, g -> g.high)
                .add()
                .build();
    }

    public static class ModifierRange {
        public float min;
        public float max;
        public static final BuilderCodec<ModifierRange> CODEC = BuilderCodec.builder(ModifierRange.class, ModifierRange::new)
                .append(new KeyedCodec<>("Min", Codec.FLOAT), (r, v) -> r.min = v, r -> r.min)
                .add()
                .append(new KeyedCodec<>("Max", Codec.FLOAT), (r, v) -> r.max = v, r -> r.max)
                .add()
                .build();

        public float roll() {
            if (min >= max) return min;
            return min + (float) (Math.random() * (max - min));
        }
    }
}
