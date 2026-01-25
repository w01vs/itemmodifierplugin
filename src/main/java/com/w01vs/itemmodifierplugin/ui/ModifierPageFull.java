package com.w01vs.itemmodifierplugin.ui;

import com.hypixel.hytale.codec.KeyedCodec;
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
import com.w01vs.itemmodifierplugin.asset.ItemModifier;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import javax.annotation.Nonnull;

public class ModifierPageFull extends ChoiceBasePage {
    public ModifierPageFull(@Nonnull PlayerRef playerRef, @Nonnull ItemStack itemStack) {
        super(playerRef, getItemElements(itemStack), "ModifierPage.ui");
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
    protected static ChoiceElement[] getItemElements(@Nonnull ItemStack itemStack) {
        List<ChoiceElement> elements = new ObjectArrayList<>();
        ItemModifier[] modifiers = itemStack.getFromMetadataOrNull(new KeyedCodec<>(ItemModifier.codecKey, ItemModifier.ARRAY_CODEC));
        if(modifiers != null) {
            for(ItemModifier mod : modifiers) {
               elements.add(new ModifierElement(mod));
            }
        }
        return elements.toArray(ChoiceElement[]::new);
    }
}

