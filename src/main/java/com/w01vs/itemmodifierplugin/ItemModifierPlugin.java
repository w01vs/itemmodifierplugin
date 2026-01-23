package com.w01vs.itemmodifierplugin;

import com.hypixel.hytale.builtin.creativehub.CreativeHubPlugin;
import com.hypixel.hytale.builtin.creativehub.config.CreativeHubEntityConfig;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.codecs.array.ArrayCodec;
import com.hypixel.hytale.common.plugin.PluginManifest;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.component.Holder;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.entity.damage.DamageDataComponent;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.event.events.player.AddPlayerToWorldEvent;
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
import java.util.ArrayList;
import java.util.Arrays;

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
        this.getCommandRegistry().registerCommand(new OpenModifierUICommand());
        modifierComponentType = getEntityStoreRegistry().registerComponent(
                ItemModifierComponent.class,
                "itemmodifier:modifiers",
                ItemModifierComponent.CODEC
        );


    }

    @Override
    protected void start() {
        this.getCodecRegistry(Interaction.CODEC).register("OpenModifierWindow", OpenModifierBenchPageInteraction.class, OpenModifierBenchPageInteraction.CODEC);
        this.getEntityStoreRegistry().registerSystem(new CustomBlockSystem());
        this.getEventRegistry().registerGlobal(AddPlayerToWorldEvent.class, ItemModifierPlugin::onPlayerAddToWorld);
    }


    private static void onPlayerAddToWorld(@Nonnull AddPlayerToWorldEvent event) {
        Holder<EntityStore> holder = event.getHolder();
        Player player = holder.getComponent(Player.getComponentType());
        if(player != null) {
            ItemContainer hotbar = player.getInventory().getHotbar();
            ItemStack hand = player.getInventory().getItemInHand();
            hotbar.registerChangeEvent( hotbarEvent -> {
                ItemStack eventHand = player.getInventory().getItemInHand();
                if(eventHand != null) {
                    ItemModifier[] modifiers = eventHand.getFromMetadataOrNull(new KeyedCodec<>(ItemModifier.codecKey, new ArrayCodec<ItemModifier>(ItemModifier.CODEC, ItemModifier[]::new)));
                    if(modifiers != null) {
                        ItemModifierManager.updateModifiers(holder, new ArrayList<ItemModifier>(Arrays.stream(modifiers).toList()));
                    }
                }
            });
        }
    }
}
