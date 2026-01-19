package com.w01vs.itemmodifierplugin;

import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ItemModifierComponent implements Component<EntityStore> {
    private final List<ItemModifier> modifiers = new ArrayList<>();
    private static final int MAX_MODIFIERS = 3;
    private String selectedOption = "none";

    public static final BuilderCodec<ItemModifierComponent> CODEC = BuilderCodec
            .builder(ItemModifierComponent.class, ItemModifierComponent::new)
            .append(
                    new KeyedCodec<>("SelectedOption", BuilderCodec.STRING),
                    (comp, val) -> comp.selectedOption = val,
                    comp -> comp.selectedOption
            )
            .add()
            .build();

    public void addModifier(ItemModifier mod) {
        if (modifiers.size() < MAX_MODIFIERS) {
            modifiers.add(mod);
        }
    }

    public List<ItemModifier> getModifiers() {
        return Collections.unmodifiableList(modifiers);
    }

    @Override
    public @Nullable Component<EntityStore> clone() {
        return null;
    }

    // You'll need to implement serialization methods here later
    // so the stats stay on the item after a restart.
}
