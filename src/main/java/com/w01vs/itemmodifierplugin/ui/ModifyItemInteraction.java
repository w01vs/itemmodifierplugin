package com.w01vs.itemmodifierplugin.ui;

import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.codecs.array.ArrayCodec;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.protocol.packets.interface_.Page;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.entity.entities.player.pages.PageManager;
import com.hypixel.hytale.server.core.entity.entities.player.pages.choices.ChoiceInteraction;
import com.hypixel.hytale.server.core.inventory.ItemContext;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.inventory.transaction.ItemStackSlotTransaction;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.w01vs.itemmodifierplugin.asset.ItemModifier;
import com.w01vs.itemmodifierplugin.asset.ItemModifierManager;


import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;

public class ModifyItemInteraction extends ChoiceInteraction {
    protected final ItemContext itemContext;
    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();

    public ModifyItemInteraction(ItemContext itemContext) {
        this.itemContext = itemContext;
    }

    @Override
    public void run(@Nonnull Store<EntityStore> store, @Nonnull Ref<EntityStore> ref, @Nonnull PlayerRef playerRef) {
        Player playerComponent = store.getComponent(ref, Player.getComponentType());
        PageManager pageManager = playerComponent.getPageManager();
        ItemStack itemStack = this.itemContext.getItemStack();

        pageManager.openCustomPage(ref, store, new ModifierPageFull(playerRef, itemContext));
    }

}