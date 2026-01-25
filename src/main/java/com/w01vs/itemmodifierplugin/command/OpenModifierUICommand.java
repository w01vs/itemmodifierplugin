package com.w01vs.itemmodifierplugin.command;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.w01vs.itemmodifierplugin.ui.ModifierPage;

import javax.annotation.Nonnull;

public class OpenModifierUICommand extends AbstractPlayerCommand {
    private String pageInput;

    public OpenModifierUICommand() {
        super("modifiers", "opens modifier UI");
    }

    public void setPageInput(@Nonnull String string) {
        pageInput = string;
    }

    public String getPageInput() {
        return pageInput;
    }


    @Override
    protected void execute(@Nonnull CommandContext ctx,
                           @Nonnull Store<EntityStore> store,
                           @Nonnull Ref<EntityStore> ref,
                           @Nonnull PlayerRef playerRef,
                           @Nonnull World world) {
        Player player = store.getComponent(ref, Player.getComponentType());
        player.getPageManager().openCustomPage(
                ref,
                store,
                new ModifierPage(playerRef, player.getInventory().getCombinedArmorHotbarUtilityStorage()));
//        CombinedItemContainer targetInventory = player.getInventory().getCombinedHotbarFirst();
//        ItemContainer targetItemContainer = targetInventory;
//        player.getPageManager().setPageWithWindows(ref, store, Page.Bench, true, new ContainerWindow(targetItemContainer));
        ctx.sendMessage(Message.raw("Opening Modifier UI..."));
    }
}


