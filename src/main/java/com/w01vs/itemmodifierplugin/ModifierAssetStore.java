package com.w01vs.itemmodifierplugin;

import com.hypixel.hytale.assetstore.AssetStore;
import com.hypixel.hytale.assetstore.AssetUpdateQuery;
import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.event.IEventBus;
import com.hypixel.hytale.server.core.asset.HytaleAssetStore;

import javax.annotation.Nonnull;
import java.nio.file.Path;
import java.util.Map;
import java.util.Set;

public class ModifierAssetStore extends HytaleAssetStore<String, ItemModifier, ModifierAssetMap> {

    public ModifierAssetStore() {
        super(HytaleAssetStore.builder(ItemModifier.class, new ModifierAssetMap())
                        .setPath("Modifiers")
                        .setCodec(ItemModifier.ASSET_CODEC)
                        .setExtension(".json")
                // If you have a packet generator, add it here to sync with players
                // .setPacketGenerator(new ModifierPacketGenerator())
        );
    }


}
