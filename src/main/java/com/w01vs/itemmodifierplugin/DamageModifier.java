package com.w01vs.itemmodifierplugin;

import com.hypixel.hytale.builtin.portals.systems.CloseWorldWhenBreakingDeviceSystems;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.codecs.array.ArrayCodec;
import com.hypixel.hytale.component.*;
import com.hypixel.hytale.component.dependency.Dependency;
import com.hypixel.hytale.component.dependency.Order;
import com.hypixel.hytale.component.dependency.SystemGroupDependency;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.math.random.RandomExtra;
import com.hypixel.hytale.server.core.entity.LivingEntity;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.meta.MetaKey;
import com.hypixel.hytale.server.core.modules.entity.AllLegacyLivingEntityTypesQuery;
import com.hypixel.hytale.server.core.modules.entity.damage.Damage;
import com.hypixel.hytale.server.core.modules.entity.damage.DamageCalculatorSystems;
import com.hypixel.hytale.server.core.modules.entity.damage.DamageEventSystem;
import com.hypixel.hytale.server.core.modules.entity.damage.DamageModule;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatMap;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.server.combat.DamageCalculator;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.Set;

public class DamageModifier extends DamageEventSystem {
    private static final Query<EntityStore> QUERY = EntityStatMap.getComponentType();
    @Nonnull
    private static final Set<Dependency<EntityStore>> DEPENDENCIES = Set.of(
            new SystemGroupDependency<>(Order.BEFORE, DamageModule.get().getInspectDamageGroup()),
            new SystemGroupDependency<>(Order.AFTER, DamageModule.get().getFilterDamageGroup())
    );

    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();

    @Nonnull
    @Override
    public Set<Dependency<EntityStore>> getDependencies() {
        return DEPENDENCIES;
    }

    @Override
    public void handle(
            int index,
            @NotNull ArchetypeChunk<EntityStore> archetypeChunk,
            @NotNull Store<EntityStore> store,
            @NotNull CommandBuffer<EntityStore> commandBuffer,
            @NotNull Damage damage
    ) {
        Damage.Source source = damage.getSource();
        int c = commandBuffer.getStore().getEntityCountFor(ItemModifierPlugin.modifierComponentType);
        LOGGER.atWarning().log(c + " entities in store with a modifier component");
        if(source instanceof Damage.EntitySource entitySource) {
            Ref<EntityStore> attackerRef = entitySource.getRef();

            Player attackerPlayer = commandBuffer.getComponent(attackerRef, Player.getComponentType());

            ItemStack heldItem = attackerPlayer.getInventory().getItemInHand();

            // get metadata from the modifiers and apply
            int typeIndex = damage.getDamageCauseIndex();
            DamageCalculatorSystems.DamageSequence seq = damage.getMetaObject(DamageCalculatorSystems.DAMAGE_SEQUENCE);
            DamageCalculator calc = seq.getDamageCalculator();
            // damage calc and sequence hold the big boi data; might not need it since ratio's will fix most stuff
            // e.g. amount / initialamount scaling applied to any bonuses i do



            ItemModifierComponent itemModifierComponent =  commandBuffer.getComponent(attackerRef, ItemModifierPlugin.modifierComponentType);
            if (itemModifierComponent != null) {
                Optional < ItemModifier > modOption = itemModifierComponent.getModifiers().stream().filter(m -> m.getId().equals("mod:damage:flat_phys")).findFirst();

                if (modOption.isPresent()) {
                    ItemModifier mod = modOption.get();
                    float startDamage = damage.getInitialAmount();
                    LOGGER.atInfo().log("Initial damage of the hit was: " + startDamage);
                    float currentDamage = damage.getAmount();
                    LOGGER.atInfo().log("Damage with systems effect was: " + currentDamage);
                    float addedDamage = mod.getMinValue();
                    LOGGER.atInfo().log("Damage with systems effect was: " + addedDamage);
                    float newDamage = currentDamage + addedDamage;
                    LOGGER.atInfo().log("Damage with systems effect was: " + newDamage);
                    damage.setAmount(newDamage);
                }
            } else {
                LOGGER.atWarning().log("No modifier component found");
            }
        }


    }

    @Override
    public @Nullable Query<EntityStore> getQuery() {
        return QUERY;
    }
}
