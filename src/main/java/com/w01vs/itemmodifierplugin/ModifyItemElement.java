package com.w01vs.itemmodifierplugin;

import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.codecs.array.ArrayCodec;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.player.pages.choices.ChoiceElement;
import com.hypixel.hytale.server.core.entity.entities.player.pages.choices.ChoiceInteraction;
import com.hypixel.hytale.server.core.entity.entities.player.pages.itemrepair.RepairItemInteraction;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.ui.builder.UIEventBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;

public class ModifyItemElement extends ChoiceElement {
    protected ItemStack itemStack;

    public ModifyItemElement(ItemStack itemStack, ModifyItemInteraction interaction) {
        this.itemStack = itemStack;
        this.interactions = new ChoiceInteraction[]{interaction};
    }

    @Override
    public void addButton(@Nonnull UICommandBuilder commandBuilder, UIEventBuilder eventBuilder, String selector, PlayerRef playerRef) {
        int durabilityPercentage = (int)Math.round(this.itemStack.getDurability() / this.itemStack.getMaxDurability() * 100.0);
        commandBuilder.append("#ElementList", "ModifyItemElement.ui");
        commandBuilder.set(selector + " #Icon.ItemId", this.itemStack.getItemId());
        commandBuilder.set(selector + " #Name.TextSpans", Message.translation(this.itemStack.getItem().getTranslationKey()));
        ItemModifier[] modifiers = this.itemStack.getFromMetadataOrNull(new KeyedCodec<>(ItemModifier.codecKey, new ArrayCodec<ItemModifier>( ItemModifier.CODEC, ItemModifier[]::new)));
        int size = 0;
        if(modifiers != null) {
            ArrayList<ItemModifier> modifiersList = new ArrayList<>(Arrays.stream(modifiers).toList());
            size = modifiersList.size();
        }
        commandBuilder.set(selector + " #Durability.Text", size + " modifiers");
    }
}
