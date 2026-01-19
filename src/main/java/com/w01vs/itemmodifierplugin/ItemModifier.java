package com.w01vs.itemmodifierplugin;

public record ItemModifier(String statId, float value, int tier) {
    // StatId refers to things like "phys_damage", "attack_speed", etc.
}
