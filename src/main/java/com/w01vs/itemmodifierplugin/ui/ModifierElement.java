package com.w01vs.itemmodifierplugin.ui;

import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.codecs.array.ArrayCodec;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.player.pages.choices.ChoiceElement;
import com.hypixel.hytale.server.core.entity.entities.player.pages.choices.ChoiceInteraction;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.ui.builder.UIEventBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.w01vs.itemmodifierplugin.asset.ItemModifier;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;

public class ModifierElement extends ChoiceElement {
    protected ItemModifier modifier;

    public ModifierElement(ItemModifier modifier) {
        this.modifier = modifier;
        this.interactions = new ChoiceInteraction[]{};
    }

    @Override
    public void addButton(@Nonnull UICommandBuilder commandBuilder, UIEventBuilder eventBuilder, String selector, PlayerRef playerRef) {
        commandBuilder.append("#ElementList", "ModifierElement.ui");
//        commandBuilder.set(selector + " #Icon.ItemId", this.itemStack.getItemId());

        commandBuilder.set(selector + " #Name.TextSpans", Message.raw(modifier.getId()));
//        commandBuilder.set(selector + " #Durability.Text", size + " modifiers");
    }
}
