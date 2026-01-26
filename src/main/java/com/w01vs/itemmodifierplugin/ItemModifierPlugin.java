package com.w01vs.itemmodifierplugin;

import com.hypixel.hytale.assetstore.AssetRegistry;
import com.hypixel.hytale.assetstore.AssetStore;
import com.hypixel.hytale.common.plugin.PluginManifest;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.asset.type.item.config.Item;
import com.hypixel.hytale.server.core.entity.damage.DamageDataComponent;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.modules.entity.damage.DamageModule;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatMap;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatsModule;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.Interaction;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.w01vs.itemmodifierplugin.asset.ModifierAssetStore;
import com.w01vs.itemmodifierplugin.command.OpenModifierUICommand;
import com.w01vs.itemmodifierplugin.system.CustomBlockSystem;
import com.w01vs.itemmodifierplugin.system.DamageModifier;
import com.w01vs.itemmodifierplugin.ui.OpenModifierBenchPageInteraction;

public class ItemModifierPlugin extends JavaPlugin {
    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();



    public static final PluginManifest MANIFEST = PluginManifest.corePlugin(ItemModifierPlugin.class)
            .depends(DamageModule.class) // Crucial for DamageDataComponent
            .depends(EntityStatsModule.class) // Crucial for EntityStatMap
            .depends(DamageDataComponent.class)
            .depends(EntityStatMap.class)
            .build();

    public ItemModifierPlugin(JavaPluginInit init) {
        super(init);
        LOGGER.atInfo().log("Hello from %s version %s", this.getName(), this.getManifest().getVersion().toString());
    }

    @Override
    protected void start() {
        this.getCommandRegistry().registerCommand(new OpenModifierUICommand());
        this.getCodecRegistry(Interaction.CODEC).register("OpenModifierWindow", OpenModifierBenchPageInteraction.class, OpenModifierBenchPageInteraction.CODEC);
        this.getEntityStoreRegistry().registerSystem(new CustomBlockSystem());
        this.getEntityStoreRegistry().registerSystem(new DamageModifier());
        this.getAssetRegistry().register(new ModifierAssetStore());

        // do in loadedassetsevent
//        var reg = AssetRegistry.getAssetStore(Item.class).getAssetMap();
//        reg.getAssetMap().forEach((id, item) -> {
//            item.getArmor().getDamageClassEnhancement().clear();
//            item.getArmor().getDamageResistanceValues().clear();
//        });
    }
}
