package com.w01vs.itemmodifierplugin;

import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.SystemGroup;
import com.hypixel.hytale.component.dependency.Dependency;
import com.hypixel.hytale.component.dependency.Order;
import com.hypixel.hytale.component.dependency.SystemGroupDependency;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.math.random.RandomExtra;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.modules.entity.damage.Damage;
import com.hypixel.hytale.server.core.modules.entity.damage.DamageEventSystem;
import com.hypixel.hytale.server.core.modules.entity.damage.DamageModule;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.Set;

public class DamageModifier extends DamageEventSystem {
    private static final Query<EntityStore> QUERY = Query.and(Player.getComponentType(), ItemModifierPlugin.modifierComponentType);
    @Nonnull
    private static final Set<Dependency<EntityStore>> DEPENDENCIES = Set.of(
            new SystemGroupDependency<>(Order.AFTER, DamageModule.get().getGatherDamageGroup())
    );

    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();

    @Nonnull
    @Override
    public Set<Dependency<EntityStore>> getDependencies() {
        return DEPENDENCIES;
    }

    @Nullable
    @Override
    public SystemGroup<EntityStore> getGroup() {
        // Join the same group as the target system
        return DamageModule.get().getGatherDamageGroup();
    }

    @Override
    public void handle(
            int index,
            @NotNull ArchetypeChunk<EntityStore> archetypeChunk,
            @NotNull Store<EntityStore> store,
            @NotNull CommandBuffer<EntityStore> commandBuffer,
            @NotNull Damage damage
    ) {
        ItemModifierComponent itemModifierComponent = archetypeChunk.getComponent(index, ItemModifierPlugin.modifierComponentType);


        assert itemModifierComponent != null;

        Optional<ItemModifier> modOption = itemModifierComponent.getModifiers().stream().filter(m -> m.getId().equals("mod:damage:flat_phys")).findFirst();

        if(modOption.isPresent()) {
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
    }

    @Override
    public @Nullable Query<EntityStore> getQuery() {
        return QUERY;
    }
}
