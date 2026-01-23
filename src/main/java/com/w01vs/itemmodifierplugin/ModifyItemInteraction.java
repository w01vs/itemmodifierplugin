package com.w01vs.itemmodifierplugin;

import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.codecs.array.ArrayCodec;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.math.util.MathUtil;
import com.hypixel.hytale.protocol.packets.interface_.Page;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.entity.entities.player.pages.PageManager;
import com.hypixel.hytale.server.core.entity.entities.player.pages.choices.ChoiceInteraction;
import com.hypixel.hytale.server.core.inventory.ItemContext;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.inventory.container.ItemContainer;
import com.hypixel.hytale.server.core.inventory.container.SimpleItemContainer;
import com.hypixel.hytale.server.core.inventory.transaction.ItemStackSlotTransaction;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.bson.BsonDocument;


import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ModifyItemInteraction extends ChoiceInteraction {
    protected final ItemContext itemContext;
    protected final double repairPenalty;

    public ModifyItemInteraction(ItemContext itemContext, double repairPenalty) {
        this.itemContext = itemContext;
        this.repairPenalty = repairPenalty;
    }

    @Override
    public void run(@Nonnull Store<EntityStore> store, @Nonnull Ref<EntityStore> ref, @Nonnull PlayerRef playerRef) {
        Player playerComponent = store.getComponent(ref, Player.getComponentType());
        PageManager pageManager = playerComponent.getPageManager();
        ItemStack itemStack = this.itemContext.getItemStack();
        double itemStackDurability = itemStack.getDurability();
        double itemStackMaxDurability = itemStack.getMaxDurability();
        double ratioAmountRepaired = 1.0 - itemStackDurability / itemStackMaxDurability;
        double newMaxDurability = MathUtil.floor(itemStackMaxDurability - itemStack.getItem().getMaxDurability() * (this.repairPenalty * ratioAmountRepaired));
        if (itemStackDurability >= newMaxDurability) {
            playerRef.sendMessage(Message.translation("server.general.repair.penaltyTooBig").color("#ff5555"));
            pageManager.setPage(ref, store, Page.None);
        } else {
            if (newMaxDurability <= 10.0) {
                newMaxDurability = 10.0;
                playerRef.sendMessage(Message.translation("server.general.repair.tooLowDurability").color("#ff5555"));
            }
        }
        ItemModifier[] newMetadata;
        ItemModifier newModifier;
        ItemModifier[] metadata = itemStack.getFromMetadataOrNull(new KeyedCodec<>(ItemModifier.codecKey, new ArrayCodec<ItemModifier>( ItemModifier.CODEC, ItemModifier[]::new)));
        if(metadata != null) {
            ArrayList<ItemModifier> itemModifiers = new ArrayList<>(Arrays.stream(metadata).toList());
            // call function to get a new modifier and append it
            newModifier = new ItemModifier();
            itemModifiers.add(newModifier);

            newMetadata = itemModifiers.toArray(new ItemModifier[0]);
        }
        else {
            newMetadata = new ItemModifier[1];
            newMetadata[0] = new ItemModifier("damage:flat_phys", 2);
        }

        ItemStack newItemStack = itemStack
                .withMetadata(new KeyedCodec<>(ItemModifier.codecKey, new ArrayCodec<ItemModifier>( ItemModifier.CODEC, ItemModifier[]::new)), newMetadata);
        ItemStackSlotTransaction replaceTransaction = this.itemContext
                .getContainer()
                .replaceItemStackInSlot(this.itemContext.getSlot(), itemStack, newItemStack);
        if (!replaceTransaction.succeeded()) {
            playerRef.sendMessage(Message.raw("Failed to modify item").color("#eb4034"));
            pageManager.setPage(ref, store, Page.None);
        } else {
            Message newItemStackMessage = Message.translation(newItemStack.getItem().getTranslationKey());
            playerRef.sendMessage(Message.raw("Succesfully added"));
            pageManager.setPage(ref, store, Page.None);
        }
    }

}