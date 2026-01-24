package com.w01vs.itemmodifierplugin;

import com.hypixel.hytale.assetstore.AssetRegistry;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.component.Holder;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.asset.type.item.config.Item;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.modules.entity.damage.DamageCalculatorSystems;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.server.combat.DamageCalculator;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemModifierManager {
    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();

    public static ItemModifier getRandomModifier() {
        ItemModifier mod = AssetRegistry.getAssetStore(ItemModifier.class).getAssetMap().getAsset("mod:damage:flat_phys");
        // mod.reroll();
        return mod;
    }

    private static List<ItemModifier> getModifiers(ItemStack item) {
        ArrayList<ItemModifier> result = new ArrayList<>();
        if(item == null) return result;
        ItemModifier[] modifiers = item.getFromMetadataOrNull(new KeyedCodec<>(ItemModifier.codecKey, ItemModifier.ARRAY_CODEC));
        if(modifiers != null){
            result = new ArrayList<>(Arrays.stream(modifiers).toList());
        }
        return result;
    }

    public static ItemModifierEffect applyModifiers(
            Ref<EntityStore> attacker,
//            Ref<EntityStore> defender,
            DamageCalculator calculator,
            DamageCalculatorSystems.DamageSequence sequence,
            float currentDamage
            ) {
        Player player = attacker.getStore().getComponent(attacker, Player.getComponentType());
        assert player != null;
        ItemStack heldItem = player.getInventory().getItemInHand();
        List<ItemModifier> modifiers = getModifiers(heldItem);

        // calculate the additive/multiplicative bonuses

        return new ItemModifierEffect(2, 1);
    }

    public record ItemModifierEffect(float additive, float multiplicative) {
        public float applyTo(float baseDamage) {
            return (baseDamage + additive) * multiplicative;
        }
    }
}
