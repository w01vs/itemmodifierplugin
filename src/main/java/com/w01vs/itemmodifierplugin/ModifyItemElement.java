package com.w01vs.itemmodifierplugin;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.player.pages.choices.ChoiceElement;
import com.hypixel.hytale.server.core.entity.entities.player.pages.choices.ChoiceInteraction;
import com.hypixel.hytale.server.core.entity.entities.player.pages.itemrepair.RepairItemInteraction;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.ui.builder.UIEventBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import javax.annotation.Nonnull;

public class ModifyItemElement extends ChoiceElement {
    protected ItemStack itemStack;

    public ModifyItemElement(ItemStack itemStack, ModifyItemInteraction interaction) {
        this.itemStack = itemStack;
        this.interactions = new ChoiceInteraction[]{interaction};
    }

    @Override
    public void addButton(@Nonnull UICommandBuilder commandBuilder, UIEventBuilder eventBuilder, String selector, PlayerRef playerRef) {
        int durabilityPercentage = (int)Math.round(this.itemStack.getDurability() / this.itemStack.getMaxDurability() * 100.0);
        commandBuilder.append("#ElementList", "Pages/ItemRepairElement.ui");
        commandBuilder.set(selector + " #Icon.ItemId", this.itemStack.getItemId());
        commandBuilder.set(selector + " #Name.TextSpans", Message.translation(this.itemStack.getItem().getTranslationKey()));
        commandBuilder.set(selector + " #Durability.Text", durabilityPercentage + "%");
    }
}
