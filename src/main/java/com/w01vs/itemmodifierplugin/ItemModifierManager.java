package com.w01vs.itemmodifierplugin;

import com.hypixel.hytale.component.Holder;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import java.util.ArrayList;

public class ItemModifierManager {
    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();
    public static void updateModifiers(Holder<EntityStore> holder, ArrayList<ItemModifier> modifiers) {
        String log = "";
        for(ItemModifier mod : modifiers) {
            log += "* " + mod.getId() + " *";
        }
        LOGGER.atInfo().log("Updated modifiers: " + log);
    }

    public static void updateModifiers(Ref<EntityStore> ref, Store<EntityStore> store, ArrayList<ItemModifier> modifiers) {
        String log = "";
        for(ItemModifier mod : modifiers) {
            log += "* " + mod.getId() + " *";
        }
        LOGGER.atInfo().log("Updated modifiers: " + log);
    }
}
