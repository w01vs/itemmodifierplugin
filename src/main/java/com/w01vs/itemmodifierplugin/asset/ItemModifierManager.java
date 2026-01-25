package com.w01vs.itemmodifierplugin.asset;

import com.hypixel.hytale.assetstore.AssetRegistry;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.modules.entity.damage.DamageCalculatorSystems;
import com.hypixel.hytale.server.core.modules.entity.damage.DamageCause;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.server.combat.DamageCalculator;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ItemModifierManager {
    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();

    public static ItemModifier getRandomModifier() {
        ItemModifierTemplate mod = AssetRegistry.getAssetStore(ItemModifierTemplate.class).getAssetMap().getAsset("flat_phys");
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
            float currentDamage,
            float initialDamage,
            int damageCauseIndex
            ) {
        Player player = attacker.getStore().getComponent(attacker, Player.getComponentType());
        assert player != null;
        ItemStack heldItem = player.getInventory().getItemInHand();
        List<ItemModifier> modifiers = getModifiers(heldItem);

        float flat = 0;
        float multi = 1;
        // calculate the additive/multiplicative bonuses
        for(ItemModifier mod : modifiers) {
            Map<String, ValueProvider> multiMap = mod.getMultiplicative();
            String damageType = DamageCause.getAssetMap().getAsset(damageCauseIndex).getId();
            multi += multiMap.getOrDefault(damageType, new ValueProvider()).get();

            Map<String, ValueProvider> addMap = mod.getAdditive();
            flat += addMap.getOrDefault(damageType, new ValueProvider()).get() * (currentDamage / initialDamage);
        }

        return new ItemModifierEffect(flat, multi);
    }

    public record ItemModifierEffect(float additive, float multiplicative) {
        public float applyTo(float baseDamage) {
            return (baseDamage + additive) * multiplicative;
        }
    }
}
