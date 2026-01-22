package com.w01vs.itemmodifierplugin;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.math.util.MathUtil;
import com.hypixel.hytale.protocol.SoundCategory;
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
import com.hypixel.hytale.server.core.universe.world.SoundUtil;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.bson.BsonString;

import javax.annotation.Nonnull;

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
        ItemStack newItemStack = itemStack.withRestoredDurability(newMaxDurability).withMetadata("description", new BsonString("This is my modified weapon"));
        ItemStackSlotTransaction replaceTransaction = this.itemContext
                .getContainer()
                .replaceItemStackInSlot(this.itemContext.getSlot(), itemStack, newItemStack);
        if (!replaceTransaction.succeeded()) {
            playerRef.sendMessage(Message.raw("Failed to repair item").color("#eb4034"));
            pageManager.setPage(ref, store, Page.None);
        } else {
            Message newItemStackMessage = Message.translation(newItemStack.getItem().getTranslationKey());
            playerRef.sendMessage(Message.translation("server.general.repair.successful").param("itemName", newItemStackMessage));
            pageManager.setPage(ref, store, Page.None);
        }
    }

}