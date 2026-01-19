package com.w01vs.itemmodifierplugin;

import com.hypixel.hytale.common.plugin.PluginManifest;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.entity.damage.DamageDataComponent;
import com.hypixel.hytale.server.core.modules.entity.damage.DamageModule;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatMap;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatsModule;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.Interaction;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

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

    private ComponentType<EntityStore, ItemModifierComponent> modifierComponentType;

    protected void setup() {
        this.getCommandRegistry().registerCommand(new ExampleCommand(this.getName(), this.getManifest().getVersion().toString()));
        this.getCommandRegistry().registerCommand(new OpenModifierUICommand());
        modifierComponentType = getEntityStoreRegistry().registerComponent(
                ItemModifierComponent.class,
                "itemmodifier:modifiers",
                ItemModifierComponent.CODEC
        );

        modifierUIStateComponent = getEntityStoreRegistry().registerComponent(
                ModifierUIStateComponent.class,
                "itemmodifiers:modifiersuistate",
                ModifierUIStateComponent.CODEC
        );
    }

    public static ComponentType<EntityStore, ModifierUIStateComponent> modifierUIStateComponent;

    @Override
    protected void start() {
        this.getCodecRegistry(Interaction.CODEC).register("OpenModifierWindow", OpenModifierBenchPageInteraction.class, OpenModifierBenchPageInteraction.CODEC);
        this.getEntityStoreRegistry().registerSystem(new CustomBlockSystem());
    }
}
