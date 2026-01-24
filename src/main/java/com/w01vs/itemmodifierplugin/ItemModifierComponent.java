package com.w01vs.itemmodifierplugin;

import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ItemModifierComponent implements Component<EntityStore> {
    private List<ItemModifier> modifiers = new ArrayList<>();
    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();
    private static final int MAX_MODIFIERS = 3;

    public static final BuilderCodec<ItemModifierComponent> CODEC = BuilderCodec
            .builder(ItemModifierComponent.class, ItemModifierComponent::new)
            .build();

    public boolean addModifier(ItemModifier mod) {
        boolean alreadyPresent = modifiers.stream().anyMatch(m -> m.getId().equals(mod.getId()));
        if (modifiers.size() < MAX_MODIFIERS && !alreadyPresent) {
            modifiers.add(mod);
            return true;
        }
        LOGGER.atInfo().log("Failed to add: " + mod.getId() + " because: alreadyPresent:" + alreadyPresent + " or size: " + (modifiers.size() > MAX_MODIFIERS));
        return false;
    }

    public boolean removeModifier(String id) {
        for(ItemModifier mod : modifiers) {
            if(mod.getId().equals(id)) {
                modifiers.remove(mod);
                return true;
            }
        }
        return false;
    }

    public List<ItemModifier> getModifiers() {
        return Collections.unmodifiableList(modifiers);
    }

    public ItemModifierComponent() {}

    protected ItemModifierComponent(ItemModifierComponent other) {
        this.modifiers = new ArrayList<ItemModifier>(other.modifiers);
    }

    @Override
    public @Nullable Component<EntityStore> clone() {
        ItemModifierComponent other = new ItemModifierComponent();
        for(ItemModifier mod : modifiers) {
            other.addModifier(mod.clone());
        }
        return other;
    }
}
