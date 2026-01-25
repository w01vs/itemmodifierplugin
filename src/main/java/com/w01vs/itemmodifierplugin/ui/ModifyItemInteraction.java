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

        pageManager.openCustomPage(ref, store, new ModifierPageFull(playerRef, itemContext.getItemStack()));

//        ItemModifier[] newMetadata;
//        ItemModifier newModifier;
//        ItemModifier[] metadata = itemStack.getFromMetadataOrNull(new KeyedCodec<>(ItemModifier.codecKey, new ArrayCodec<ItemModifier>( ItemModifier.CODEC, ItemModifier[]::new)));
//        if(metadata != null) {
//            ArrayList<ItemModifier> itemModifiers = new ArrayList<>(Arrays.stream(metadata).toList());
//            newModifier = ItemModifierManager.getRandomModifier();
//            itemModifiers.add(newModifier);
//
//            newMetadata = itemModifiers.toArray(new ItemModifier[0]);
//        }
//        else {
//            newMetadata = new ItemModifier[1];
//            newMetadata[0] = newModifier = ItemModifierManager.getRandomModifier();
//        }
//
//        ItemStack newItemStack = itemStack
//                .withMetadata(new KeyedCodec<>(ItemModifier.codecKey, new ArrayCodec<ItemModifier>( ItemModifier.CODEC, ItemModifier[]::new)), newMetadata);
//        ItemStackSlotTransaction replaceTransaction = this.itemContext
//                .getContainer()
//                .replaceItemStackInSlot(this.itemContext.getSlot(), itemStack, newItemStack);
//        if (!replaceTransaction.succeeded()) {
//            playerRef.sendMessage(Message.raw("Failed to modify item").color("#eb4034"));
//            pageManager.setPage(ref, store, Page.None);
//        } else {
//            Message newItemStackMessage = Message.translation(newItemStack.getItem().getTranslationKey());
//            playerRef.sendMessage(Message.raw("Succesfully added modifier"));
//            pageManager.setPage(ref, store, Page.None);
//        }
    }

}