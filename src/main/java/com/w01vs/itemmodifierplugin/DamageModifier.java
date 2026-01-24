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
import com.hypixel.hytale.server.core.inventory.Inventory;
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
import java.util.ArrayList;
import java.util.Arrays;
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
        if(source instanceof Damage.EntitySource entitySource) {
            Ref<EntityStore> attackerRef = entitySource.getRef();

            Player attackerPlayer = commandBuffer.getComponent(attackerRef, Player.getComponentType());
            if(attackerPlayer != null) {
                DamageCalculatorSystems.DamageSequence seq = damage.getMetaObject(DamageCalculatorSystems.DAMAGE_SEQUENCE);
                DamageCalculator calc = seq.getDamageCalculator();
                // damage calc and sequence hold the big boi data; might not need it since ratio's will fix most stuff
                ItemModifierManager.ItemModifierEffect effect = ItemModifierManager.applyModifiers(attackerRef, calc, seq, damage.getAmount(), damage.getInitialAmount(), damage.getDamageCauseIndex());
                damage.setAmount(effect.applyTo(damage.getAmount()));
            }
        }


    }

    @Override
    public @Nullable Query<EntityStore> getQuery() {
        return QUERY;
    }
}
