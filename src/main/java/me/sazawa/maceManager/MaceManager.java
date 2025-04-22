package me.sazawa.maceManager;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Collections;
import java.util.List;

public final class MaceManager extends JavaPlugin implements Listener, CommandExecutor {

    FileConfiguration config = this.getConfig();

    @Override
    public void onLoad() {
        saveDefaultConfig();
    }

    @Override
    public void onEnable() {

        if (config.getBoolean("canDropMace")) {
            this.getLogger().info("yes i can drop mace");
        } else {
            this.getLogger().info("no i can't drop mace");
        }
        this.getServer().getPluginManager().registerEvents(this, this);
        this.getCommand("createMace").setExecutor(this);
    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin Disabled!");
    }

    @EventHandler
    public void onAttack(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player attacker) {
            if (attacker.getInventory().getItemInMainHand().getType() == Material.MACE) {
                if (config.getBoolean("canUseMace")) {
                    return;
                }
                if (attacker.hasPermission("maceManager.bypass")) {
                    return;
                }

                if (attacker.getInventory().getItemInMainHand().getItemMeta().getLore() == null) {
                    Material newItem = Material.valueOf(config.getString("replace-with"));
                    if (newItem == null) {
                        newItem = Material.ENCHANTED_GOLDEN_APPLE;
                    }
                    attacker.getInventory().setItemInMainHand(ItemStack.of( newItem, 1));
                    attacker.sendMessage("[MaceManager] " + "You cannot use this mace! Only event crafted mace can be used. Your mace was replaced with a " + newItem.toString() + " instead.");
                }

            }
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getView().getPlayer().hasPermission("maceManager.bypass")) {
            return;
        }
        if (config.getBoolean("canStoreMace")) {
            return;
        }

        Inventory top = event.getView().getTopInventory();
        Inventory bottom = event.getView().getBottomInventory();

        if (top.getType() == InventoryType.BARREL ||
                top.getType() == InventoryType.BEACON ||
                top.getType() == InventoryType.BLAST_FURNACE ||
                top.getType() == InventoryType.BREWING ||
                top.getType() == InventoryType.CARTOGRAPHY ||
                top.getType() == InventoryType.CHEST ||
                top.getType() == InventoryType.CHISELED_BOOKSHELF ||
                top.getType() == InventoryType.COMPOSTER ||
                top.getType() == InventoryType.CRAFTER ||
                top.getType() == InventoryType.DECORATED_POT ||
                top.getType() == InventoryType.DISPENSER ||
                top.getType() == InventoryType.DROPPER ||
                top.getType() == InventoryType.ENDER_CHEST ||
                top.getType() == InventoryType.FURNACE ||
                top.getType() == InventoryType.GRINDSTONE ||
                top.getType() == InventoryType.HOPPER ||
                top.getType() == InventoryType.JUKEBOX ||
                top.getType() == InventoryType.LECTERN ||
                top.getType() == InventoryType.LOOM ||
                top.getType() == InventoryType.MERCHANT ||
                top.getType() == InventoryType.SHULKER_BOX ||
                top.getType() == InventoryType.SMITHING ||
                top.getType() == InventoryType.SMOKER ||
                top.getType() == InventoryType.STONECUTTER ||
                top.getType() == InventoryType.WORKBENCH) {

            if (bottom.getType() == InventoryType.PLAYER) {
                if (event.getCurrentItem().getType() == Material.MACE || event.getCursor().getType() == Material.MACE) {
                    event.setCancelled(true);
                }

                if (event.getClick().isKeyboardClick()) {
                    event.setCancelled(true);
                }
            }

        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        if (event.getPlayer().hasPermission("maceManager.bypass")) {
            return;
        }
        if (config.getBoolean("canDropMace")) {
            return;
        }
        if (event.getItemDrop().getItemStack().getType() == Material.MACE) {
            event.setCancelled(true);
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if (commandSender instanceof Player sender) {
            ItemStack newMace = new ItemStack(Material.MACE);
            newMace.setAmount(1);
            if (strings.length == 0) {
                newMace.setLore(Collections.singletonList("This is an event won mace."));
            } else {
                StringBuilder lore = new StringBuilder();
                for (String string : strings) {
                    lore.append(string).append(" ");
                }
                newMace.setLore(List.of(lore.toString()));
            }
            sender.getInventory().setItemInMainHand(newMace);
        }
        return true;
    }
}
