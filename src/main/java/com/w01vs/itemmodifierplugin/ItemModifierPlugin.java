package com.w01vs.itemmodifierplugin;

import com.hypixel.hytale.assetstore.AssetLoadResult;
import com.hypixel.hytale.assetstore.AssetRegistry;
import com.hypixel.hytale.assetstore.AssetStore;
import com.hypixel.hytale.builtin.creativehub.CreativeHubPlugin;
import com.hypixel.hytale.builtin.creativehub.config.CreativeHubEntityConfig;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.codecs.array.ArrayCodec;
import com.hypixel.hytale.common.plugin.PluginManifest;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.component.Holder;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.HytaleServer;
import com.hypixel.hytale.server.core.entity.damage.DamageDataComponent;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.event.events.player.AddPlayerToWorldEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;
import com.hypixel.hytale.server.core.inventory.Inventory;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.inventory.container.ItemContainer;
import com.hypixel.hytale.server.core.modules.entity.damage.DamageModule;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatMap;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatsModule;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.Interaction;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nonnull;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    public static ComponentType<EntityStore, ItemModifierComponent> modifierComponentType;

    protected void setup() {
        this.getCommandRegistry().registerCommand(new OpenModifierUICommand());
    }

    @Override
    protected void start() {
        this.getCodecRegistry(Interaction.CODEC).register("OpenModifierWindow", OpenModifierBenchPageInteraction.class, OpenModifierBenchPageInteraction.CODEC);
        this.getEntityStoreRegistry().registerSystem(new CustomBlockSystem());
        modifierComponentType = getEntityStoreRegistry().registerComponent(
                ItemModifierComponent.class,
                "itemmodifier:modifiers",
                ItemModifierComponent.CODEC
        );
        this.getEntityStoreRegistry().registerSystem(new DamageModifier());
        this.getAssetRegistry().register(new ModifierAssetStore());
    }
}
