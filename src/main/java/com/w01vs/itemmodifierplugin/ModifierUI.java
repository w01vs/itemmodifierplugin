package com.w01vs.itemmodifierplugin;

import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.protocol.packets.interface_.CustomPageLifetime;
import com.hypixel.hytale.protocol.packets.interface_.CustomUIEventBindingType;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.player.pages.InteractiveCustomUIPage;
import com.hypixel.hytale.server.core.ui.builder.EventData;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.ui.builder.UIEventBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public class ModifierUI extends InteractiveCustomUIPage<ModifierUI.ModifierUIData> {
    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();

    public ModifierUI(@NotNull PlayerRef playerRef, @NotNull CustomPageLifetime lifetime) {
        super(playerRef, lifetime, ModifierUIData.CODEC);
    }

    @Override
    public void build(@Nonnull Ref<EntityStore> ref, @Nonnull UICommandBuilder uiCommandBuilder, @Nonnull UIEventBuilder uiEventBuilder, @Nonnull Store<EntityStore> store) {
        ModifierUIStateComponent component = store.ensureAndGetComponent(ref, ModifierUIStateComponent.getComponentType());
        uiCommandBuilder.append("ModifierWorkbench.ui");
        uiCommandBuilder.set("#MyLabel.TextSpans", Message.raw("This is my message"));
        uiCommandBuilder.set("#MyInput.Value", component.persistentInput);

        uiEventBuilder.addEventBinding(CustomUIEventBindingType.ValueChanged, "#MyInput", EventData.of("@MyInput", "#MyInput.Value"), false);
    }

    @Override
    public void handleDataEvent(@Nonnull Ref<EntityStore> ref, @Nonnull Store<EntityStore> store, @Nonnull ModifierUIData data) {
        super.handleDataEvent(ref, store, data);

        ModifierUIStateComponent component = store.ensureAndGetComponent(ref, ModifierUIStateComponent.getComponentType());

        component.persistentInput = data.value;

        LOGGER.atInfo()
                .log("This event has been set to: " + data.value);

        sendUpdate();
    }

    public static class ModifierUIData {
        public static final BuilderCodec<ModifierUIData> CODEC = BuilderCodec.<ModifierUIData>builder(ModifierUIData.class, ModifierUIData::new)
                .append(new KeyedCodec<>("@MyInput", BuilderCodec.STRING), (data, value) -> data.value = value, data -> data.value)
                .add()
                .build();

        private String value;
    }


}
