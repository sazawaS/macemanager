package me.sazawa.maceManager;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
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

    @Override
    public void onEnable() {
        getLogger().info("Hello Plugin Started!");
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
                if (attacker.hasPermission("maceManager.bypass")) {
                    this.getLogger().info("Player " + attacker.getName() + " used MACE and he is allowed to.");
                } else
                {
                    if (attacker.getInventory().getItemInMainHand().getItemMeta().getLore() == null) {
                        this.getLogger().info("Player " + attacker.getName() + " used MACE.");
                        attacker.getInventory().setItemInMainHand(ItemStack.of(Material.ENCHANTED_GOLDEN_APPLE, 1));
                        attacker.sendMessage("[MaceManager] " + "You cannot use this mace! Only event crafted mace can be used. Your mace was replaced with an egap instead.");
                    }
                }

            }
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if(event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) {
            return;
        }
        Inventory top = event.getView().getTopInventory();
        Inventory bottom = event.getView().getBottomInventory();
        if ((top.getType() == InventoryType.CHEST || top.getType() == InventoryType.ENDER_CHEST || top.getType() == InventoryType.HOPPER || top.getType() == InventoryType.FURNACE || top.getType() == InventoryType.BARREL || top.getType() == InventoryType.BLAST_FURNACE) && bottom.getType() == InventoryType.PLAYER){
            if (event.getCurrentItem() != null) {
                if (event.getCurrentItem().getType() == Material.MACE) {
                    if (event.getView().getPlayer().hasPermission("maceManager.bypass")) {
                        return;
                    }
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        if (event.getItemDrop().getItemStack().getType() == Material.MACE) {
            if (event.getPlayer().hasPermission("maceManager.bypass")) {
                return;
            }
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
                String lore = "";
                for (String string : strings) {
                    lore = lore + string + " ";
                }
                newMace.setLore(List.of(lore));
            }
            sender.getInventory().setItemInMainHand(newMace);
        }
        return true;
    }
}
