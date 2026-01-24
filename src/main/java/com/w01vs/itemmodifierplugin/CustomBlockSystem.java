package com.w01vs.itemmodifierplugin;

import com.hypixel.hytale.component.*;
import com.hypixel.hytale.component.dependency.Dependency;
import com.hypixel.hytale.component.dependency.Order;
import com.hypixel.hytale.component.dependency.SystemDependency;
import com.hypixel.hytale.component.dependency.SystemGroupDependency;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.entity.damage.DamageDataComponent;
import com.hypixel.hytale.server.core.modules.entity.damage.Damage;
import com.hypixel.hytale.server.core.modules.entity.damage.DamageEventSystem;
import com.hypixel.hytale.server.core.modules.entity.damage.DamageModule;
import com.hypixel.hytale.server.core.modules.entity.damage.DamageSystems;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatMap;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatsSystems;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.Set;

public class CustomBlockSystem extends DamageEventSystem implements EntityStatsSystems.StatModifyingSystem {
    private final Set<Dependency<EntityStore>> dependencies = Set.of(
            new SystemGroupDependency<>(Order.AFTER, DamageModule.get().getGatherDamageGroup()),
            new SystemDependency<EntityStore, DamageSystems.DamageStamina>(Order.BEFORE, DamageSystems.DamageStamina.class)
            );

    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();
    @Nullable
    @Override
    public SystemGroup<EntityStore> getGroup() {
        // Join the same group as the target system
        return DamageModule.get().getInspectDamageGroup();
    }

    final static Query<EntityStore> QUERY = Query.and(DamageDataComponent.getComponentType(), EntityStatMap.getComponentType(), PlayerRef.getComponentType());

    @Override
    public void handle(int index, @NotNull ArchetypeChunk<EntityStore> archetypeChunk, @NotNull Store<EntityStore> store, @NotNull CommandBuffer<EntityStore> commandBuffer, @NotNull Damage damage) {
        damage.putMetaObject(Damage.STAMINA_DRAIN_MULTIPLIER, Float.valueOf(0.0f));
        LOGGER.atInfo().log("My custom block system handled an event");
    }



    @Nonnull
    @Override
    public Query<EntityStore> getQuery() {
        return QUERY;
    }
}

