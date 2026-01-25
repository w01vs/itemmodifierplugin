package com.w01vs.itemmodifierplugin.ui;


import com.hypixel.hytale.component.ComponentAccessor;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.server.core.entity.InteractionContext;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.entity.entities.player.pages.CustomUIPage;
import com.hypixel.hytale.server.core.inventory.ItemContext;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.server.OpenCustomUIInteraction;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import javax.annotation.Nonnull;

public class ModifierPageSupplier implements OpenCustomUIInteraction.CustomPageSupplier {

    @Override
    public CustomUIPage tryCreate(
            @Nonnull Ref<EntityStore> ref,
            @Nonnull ComponentAccessor<EntityStore> componentAccessor,
            @Nonnull PlayerRef playerRef,
            @Nonnull InteractionContext context
    ) {
        Player playerComponent = componentAccessor.getComponent(ref, Player.getComponentType());

        assert playerComponent != null;

        ItemContext itemContext = context.createHeldItemContext();
        return itemContext == null
                ? null
                : new ModifierPage(playerRef, playerComponent.getInventory().getCombinedArmorHotbarUtilityStorage());
    }
}
