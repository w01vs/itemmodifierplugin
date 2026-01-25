package com.w01vs.itemmodifierplugin.ui;

import com.hypixel.hytale.builtin.adventure.memories.window.MemoriesWindow;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.protocol.InteractionType;
import com.hypixel.hytale.protocol.packets.interface_.Page;
import com.hypixel.hytale.server.core.entity.InteractionContext;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.modules.interaction.interaction.CooldownHandler;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.client.SimpleBlockInteraction;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

public class OpenModifierBenchPageInteraction extends SimpleBlockInteraction {
    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();
    public static final BuilderCodec<OpenModifierBenchPageInteraction> CODEC = BuilderCodec.builder(
            OpenModifierBenchPageInteraction.class, OpenModifierBenchPageInteraction::new, SimpleBlockInteraction.CODEC)
            .build();

    public OpenModifierBenchPageInteraction(@Nonnull String id) {
        super(id);
    }

    protected OpenModifierBenchPageInteraction() {

    }

    @Override
    protected void interactWithBlock(@NotNull World world,
                                     @NotNull CommandBuffer<EntityStore> commandBuffer,
                                     @NotNull InteractionType interactionType,
                                     @NotNull InteractionContext context,
                                     @Nullable ItemStack itemInHand,
                                     @NotNull Vector3i targetBlock,
                                     @NotNull CooldownHandler cooldownHandler) {
        Ref<EntityStore> ref = context.getEntity();
        Store<EntityStore> store = ref.getStore();
        Player playerComponent = commandBuffer.getComponent(ref, Player.getComponentType());
        PlayerRef playerRef = store.getComponent(ref, PlayerRef.getComponentType());
        if (playerComponent != null && playerRef != null) {

            MemoriesWindow window = new MemoriesWindow();
            playerComponent.getPageManager().setPageWithWindows(ref, store, Page.Bench, true, window);
            LOGGER.atInfo().log("Opening a UI window");
        }
    }

    @Override
    protected void simulateInteractWithBlock(
            @Nonnull InteractionType type, @Nonnull InteractionContext context, @javax.annotation.Nullable ItemStack itemInHand, @Nonnull World world, @Nonnull Vector3i targetBlock
    ) {
    }
}
