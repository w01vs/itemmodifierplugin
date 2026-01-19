package com.w01vs.itemmodifierplugin;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hypixel.hytale.builtin.crafting.CraftingPlugin;
import com.hypixel.hytale.math.util.ChunkUtil;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.protocol.packets.window.WindowType;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.BlockType;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.bench.CraftingBench;
import com.hypixel.hytale.server.core.entity.entities.player.windows.BlockWindow;
import com.hypixel.hytale.server.core.entity.entities.player.windows.Window;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.chunk.WorldChunk;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class ModifierWindow extends BlockWindow {
    private final JsonObject windowData = new JsonObject();

    public ModifierWindow(@NotNull WindowType windowType, Vector3i pos, World world) {
        BlockType blockType = world.getBlockType(pos.x, pos.y, pos.z);
        WorldChunk chunk = world.getChunk(ChunkUtil.indexChunkFromBlock(pos.x, pos.z));
        super(windowType, pos.x, pos.y, pos.z, chunk.getRotationIndex(pos.x, pos.y, pos.z), blockType);
        JsonArray categories = new JsonArray();
        JsonObject category = new JsonObject();
        categories.add(category);
        category.addProperty("id", "Weapons");
        category.addProperty("name", "Weapons");
        category.addProperty("icon", "Icons/ItemsGenerated/Example_Block.png");
        JsonArray recipesArray = new JsonArray();
        category.add("craftableRecipes", recipesArray);

        JsonArray itemCategories = new JsonArray();


        JsonObject itemCategory = new JsonObject();
        itemCategory.addProperty("id", "Sword");
        itemCategory.addProperty("icon", "Icons/ItemsGenerated/Example_Block.png");
        itemCategory.addProperty("diagram", "");
        itemCategory.addProperty("slots", 1);
        itemCategory.addProperty("specialSlot", false);
        itemCategories.add(itemCategory);

        category.add("itemCategories", itemCategories);

        this.windowData.add("categories", categories);
    }

    @Override
    public @NotNull JsonObject getData() {
        return windowData;
    }

    @Override
    protected boolean onOpen0() {
            return true;
    }

    @Override
    protected void onClose0() {

    }
}
