package com.w01vs.itemmodifierplugin;

import com.hypixel.hytale.assetstore.AssetMap;
import com.hypixel.hytale.assetstore.AssetRegistry;
import com.hypixel.hytale.assetstore.codec.AssetCodec;
import com.hypixel.hytale.codec.ExtraInfo;
import com.hypixel.hytale.logger.HytaleLogger;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class ModifierAssetMap extends AssetMap<String, ItemModifier> {
    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();
    private final Map<String, ItemModifier> modifiers = new Object2ObjectOpenHashMap<>();
    private final Map<String, Path> assetPaths = new Object2ObjectOpenHashMap<>();
    private final Map<String, String> assetPacks = new Object2ObjectOpenHashMap<>();
    private final Map<Integer, Set<String>> tagToKeys = new Int2ObjectOpenHashMap<>();


    @Override
    protected void putAll(@Nonnull String packName, AssetCodec<String, ItemModifier> codec,
                          Map<String, ItemModifier> newAssets, Map<String, Path> paths,
                          Map<String, Set<String>> tagsByAsset) {

        this.modifiers.putAll(newAssets);
        this.assetPaths.putAll(paths);

        modifiers.forEach((id, _) -> {
            LOGGER.atWarning().log(id);
        });

        tagsByAsset.forEach((assetId, tags) -> {
            for (String tag : tags) {
                int index = AssetRegistry.getOrCreateTagIndex(tag);
                this.tagToKeys.computeIfAbsent(index, k -> new HashSet<>()).add(assetId);
            }
        });
    }

    @Override
    public @Nullable ItemModifier getAsset(String id) {
        return this.modifiers.get(id);
    }

    @Override
    public int getAssetCount() {
        return this.modifiers.size();
    }

    @Override
    public Map<String, ItemModifier> getAssetMap() {
        return this.modifiers;
    }

    @Override
    protected void clear() {
        this.modifiers.clear();
    }

    @Override
    public @Nullable Path getPath(String id) {
        return this.assetPaths.get(id);
    }

    @Override
    public @Nullable String getAssetPack(String id) {
        return this.assetPacks.getOrDefault(id, "");
    }

    @Override
    public Set<String> getKeysForPack(@Nonnull String packName) {
        return this.assetPacks.entrySet().stream()
                .filter(e -> e.getValue().equals(packName))
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
    }

    @Override
    public @Nullable ItemModifier getAsset(@Nonnull String packKey, String key) {
        ItemModifier asset = this.modifiers.get(key);
        return (asset != null && packKey.equals(this.assetPacks.get(key))) ? asset : null;
    }

    @Override
    public Set<String> getKeys(Path path) {
        return this.assetPaths.entrySet().stream()
                .filter(entry -> entry.getValue().equals(path))
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<String> getChildren(String parentId) {
        return this.modifiers.keySet().stream()
                .filter(key -> key.startsWith(parentId + ":"))
                .collect(Collectors.toSet());
    }

    @Override
    public Map<String, Path> getPathMap(@Nonnull String packName) {
        Map<String, Path> packPaths = new Object2ObjectOpenHashMap<>();
        for (String key : getKeysForPack(packName)) {
            packPaths.put(key, this.assetPaths.get(key));
        }
        return packPaths;
    }

    @Override
    public Set<String> getKeysForTag(int tagIndex) {
        return this.tagToKeys.getOrDefault(tagIndex, Collections.emptySet());
    }

    @Override
    public IntSet getTagIndexes() {
        return new IntOpenHashSet(this.tagToKeys.keySet());
    }

    @Override
    public int getTagCount() {
        return this.tagToKeys.size();
    }

    @Override
    protected Set<String> remove(Set<String> keys) {
        for (String key : keys) {
            this.modifiers.remove(key);
            this.assetPaths.remove(key);
            this.assetPacks.remove(key);
            // Clean key out of all tag sets
            this.tagToKeys.values().forEach(set -> set.remove(key));
        }
        return keys;
    }

    @Override
    protected Set<String> remove(@Nonnull String packName, Set<String> keys, List<Map.Entry<String, Object>> metadata) {
        // This is a more targeted removal often used during partial reloads
        return this.remove(keys);
    }
}
