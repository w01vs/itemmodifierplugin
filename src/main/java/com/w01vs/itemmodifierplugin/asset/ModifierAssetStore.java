package com.w01vs.itemmodifierplugin.asset;

import com.hypixel.hytale.server.core.asset.HytaleAssetStore;

public class ModifierAssetStore extends HytaleAssetStore<String, ItemModifierTemplate, ModifierAssetMap> {

    public ModifierAssetStore() {
        super(HytaleAssetStore.builder(ItemModifierTemplate.class, new ModifierAssetMap())
                        .setPath("Modifiers")
                        .setCodec(ItemModifierTemplate.ASSET_CODEC)
                        .setExtension(".json")
        );
    }


}
