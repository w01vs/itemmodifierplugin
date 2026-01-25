package com.w01vs.itemmodifierplugin.ui;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.entity.entities.player.pages.choices.ChoiceBasePage;
import com.hypixel.hytale.server.core.entity.entities.player.pages.choices.ChoiceElement;
import com.hypixel.hytale.server.core.inventory.ItemContext;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.inventory.container.ItemContainer;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.ui.builder.UIEventBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import javax.annotation.Nonnull;

public class ModifierPage extends ChoiceBasePage {
    public ModifierPage(@Nonnull PlayerRef playerRef, @Nonnull ItemContainer itemContainer) {
        super(playerRef, getItemElements(itemContainer), "ModifyItemPage.ui");
    }

    @Override
    public void build(
            @Nonnull Ref<EntityStore> ref, @Nonnull UICommandBuilder commandBuilder, @Nonnull UIEventBuilder eventBuilder, @Nonnull Store<EntityStore> store
    ) {
        if (this.getElements().length > 0) {
            super.build(ref, commandBuilder, eventBuilder, store);
        } else {
            commandBuilder.append(this.getPageLayout());
            commandBuilder.clear("#ElementList");
            commandBuilder.appendInline("#ElementList", "Label { Text: %No items to modify; Style: (Alignment: Center); }");
        }
    }

    @Nonnull
    protected static ChoiceElement[] getItemElements(@Nonnull ItemContainer itemContainer) {
        List<ChoiceElement> elements = new ObjectArrayList<>();

        for (short slot = 0; slot < itemContainer.getCapacity(); slot++) {
            ItemStack itemStack = itemContainer.getItemStack(slot);
            if (!ItemStack.isEmpty(itemStack) && !itemStack.isUnbreakable() && itemStack.getMaxDurability() > 0) {
                ItemContext itemContext = new ItemContext(itemContainer, slot, itemStack);
                elements.add(new ModifyItemElement(itemStack, new ModifyItemInteraction(itemContext)));
            }
        }

        return elements.toArray(ChoiceElement[]::new);
    }
}

