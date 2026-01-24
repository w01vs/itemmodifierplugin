package com.w01vs.itemmodifierplugin;

import com.hypixel.hytale.component.Holder;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.asset.type.item.config.Item;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import java.util.ArrayList;

public class ItemModifierManager {
    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();
    public static void updateModifiers(Holder<EntityStore> holder, ArrayList<ItemModifier> modifiers) {
        String log = "";
        ItemModifierComponent component = holder.ensureAndGetComponent(ItemModifierPlugin.modifierComponentType);
        for(ItemModifier mod : modifiers) {
            boolean added = component.addModifier(mod);
            if(added)
                log += "* " + mod.getId() + " *";
        }
        holder.replaceComponent(ItemModifierPlugin.modifierComponentType, component);
        LOGGER.atInfo().log("Updated modifiers: " + log);
    }

    public static void updateModifiers(Ref<EntityStore> ref, Store<EntityStore> store, ArrayList<ItemModifier> modifiers) {
        String log = "";
        ItemModifierComponent component = store.ensureAndGetComponent(ref, ItemModifierPlugin.modifierComponentType);
        for(ItemModifier mod : modifiers) {
            boolean added = component.addModifier(mod);
            if(added)
                log += "* " + mod.getId() + " *";
        }
        store.replaceComponent(ref, ItemModifierPlugin.modifierComponentType, component);
        LOGGER.atInfo().log("Updated modifiers: " + log);
    }
}
