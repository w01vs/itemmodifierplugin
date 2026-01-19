package com.w01vs.itemmodifierplugin;

import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

public class ModifierUIStateComponent implements Component<EntityStore> {
    public String persistentInput = "";
    public static final BuilderCodec<ModifierUIStateComponent> CODEC = BuilderCodec.builder(ModifierUIStateComponent.class, ModifierUIStateComponent::new)
            .append(new KeyedCodec<>("PeristentInput", BuilderCodec.STRING), (data, value) -> data.persistentInput = value, data -> data.persistentInput)
            .add()
            .build();
    public ModifierUIStateComponent() {}

    public ModifierUIStateComponent(@Nonnull ModifierUIStateComponent other) {
        this.persistentInput = other.persistentInput;
    }

    public static ComponentType<EntityStore, ModifierUIStateComponent> getComponentType() {
        return ItemModifierPlugin.modifierUIStateComponent;
    }

    @Override
    @Nonnull
    public Component<EntityStore> clone() {
        return new ModifierUIStateComponent(this);
    }
}
