package com.w01vs.itemmodifierplugin.ui;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.codecs.array.ArrayCodec;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.protocol.packets.interface_.CustomPageLifetime;
import com.hypixel.hytale.protocol.packets.interface_.CustomUIEventBindingType;
import com.hypixel.hytale.protocol.packets.interface_.Page;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.entity.entities.player.pages.InteractiveCustomUIPage;
import com.hypixel.hytale.server.core.entity.entities.player.pages.PageManager;
import com.hypixel.hytale.server.core.entity.entities.player.pages.choices.ChoiceBasePage;
import com.hypixel.hytale.server.core.entity.entities.player.pages.choices.ChoiceElement;
import com.hypixel.hytale.server.core.entity.entities.player.pages.choices.ChoiceInteraction;
import com.hypixel.hytale.server.core.inventory.ItemContext;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.inventory.transaction.ItemStackSlotTransaction;
import com.hypixel.hytale.server.core.ui.builder.EventData;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.ui.builder.UIEventBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.w01vs.itemmodifierplugin.asset.ItemModifier;
import com.w01vs.itemmodifierplugin.asset.ItemModifierManager;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Nonnull;

public class ModifierPageFull extends InteractiveCustomUIPage<ModifierPageFull.ChoicePageEventData> {
    private final ChoiceElement[] elements;
    private final String pageLayout;
    private final ItemContext itemContext;
    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();

    public ModifierPageFull(@Nonnull PlayerRef playerRef, @Nonnull ItemContext itemContext) {
        super(playerRef, CustomPageLifetime.CanDismiss, ModifierPageFull.ChoicePageEventData.CODEC);
        elements = getItemElements(itemContext.getItemStack());
        this.itemContext = itemContext;
        pageLayout = "ModifierPage.ui";
    }

    @Override
    public void build(
            @Nonnull Ref<EntityStore> ref, @Nonnull UICommandBuilder commandBuilder, @Nonnull UIEventBuilder eventBuilder, @Nonnull Store<EntityStore> store
    ) {
        if (elements.length > 0) {
            commandBuilder.append(pageLayout);
            commandBuilder.clear("#ElementList");
                for (int i = 0; i < elements.length; i++) {
                    String selector = "#ElementList[" + i + "]";
                    ChoiceElement element = elements[i];
                    element.addButton(commandBuilder, eventBuilder, selector, this.playerRef);
                    eventBuilder.addEventBinding(CustomUIEventBindingType.Activating, selector, EventData.of("Index", Integer.toString(i)), false);
                }
        } else {
            commandBuilder.append(pageLayout);
            commandBuilder.clear("#ElementList");
            commandBuilder.appendInline("#ElementList", "Label { Text: \"No items to modify\"; Style: (Alignment: Center); }");
        }

        eventBuilder.addEventBinding(
                CustomUIEventBindingType.Activating,
                "#AddModifierButton",
                EventData.of("Index", "-1").append("NewModifier", "true"),
                false
        );
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

    public void handleDataEvent(@Nonnull Ref<EntityStore> ref, @Nonnull Store<EntityStore> store, @Nonnull ModifierPageFull.ChoicePageEventData data) {
        if(data.getIndex() == -1 && data.isNewModifier()) {
        Player playerComponent = store.getComponent(ref, Player.getComponentType());
        PageManager pageManager = playerComponent.getPageManager();
        ItemModifier[] newMetadata;
        ItemModifier newModifier;
        ItemStack itemStack = itemContext.getItemStack();
        ItemModifier[] metadata = itemStack.getFromMetadataOrNull(new KeyedCodec<>(ItemModifier.codecKey, ItemModifier.ARRAY_CODEC));
        if(metadata != null) {
            ArrayList<ItemModifier> itemModifiers = new ArrayList<>(Arrays.stream(metadata).toList());
            newModifier = ItemModifierManager.getRandomModifier();
            itemModifiers.add(newModifier);

            newMetadata = itemModifiers.toArray(new ItemModifier[0]);
        }
        else {
            newMetadata = new ItemModifier[1];
            newMetadata[0] = newModifier = ItemModifierManager.getRandomModifier();
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
            playerRef.sendMessage(Message.raw("Succesfully added modifier"));
            pageManager.setPage(ref, store, Page.None);
        }
            return;
        }
        ChoiceElement element = this.elements[data.getIndex()];
        if (element.canFulfillRequirements(store, ref, this.playerRef)) {
            ChoiceInteraction[] interactions = element.getInteractions();

            for (ChoiceInteraction interaction : interactions) {
                interaction.run(store, ref, this.playerRef);
            }
        }
    }

    public static class ChoicePageEventData {
        static final String ELEMENT_INDEX = "Index";
        public static final BuilderCodec<ModifierPageFull.ChoicePageEventData> CODEC = BuilderCodec.builder(
                        ModifierPageFull.ChoicePageEventData.class, ModifierPageFull.ChoicePageEventData::new
                )
                .append(new KeyedCodec<>("Index", Codec.STRING), (choicePageEventData, s) -> {
                    choicePageEventData.indexStr = s;
                    choicePageEventData.index = Integer.parseInt(s);
                }, choicePageEventData -> choicePageEventData.indexStr)
                .add()
                .append(new KeyedCodec<>("NewModifier", Codec.STRING), (d, v) -> d.newModifier = Boolean.parseBoolean(v), d -> String.valueOf(d.newModifier))                .add()
                .build();
        private String indexStr;
        private int index;
        private boolean newModifier;

        public int getIndex() {
            return this.index;
        }

        public boolean isNewModifier() {
            return newModifier;
        }
    }
}

