package io.cbitler.wbfillpause

import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.plugin.java.JavaPlugin

class WBFillPause : JavaPlugin() {
    var running = false;
    override fun onEnable() {
        // Put the fill in a known state
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "wb fill cancel");
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "wb world fill 500");
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "wb fill confirm");
        if (!Bukkit.getServer().onlinePlayers.isEmpty()) {
            println("Fill started, but paused due to players online");
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "wb fill pause");
            running = false;
        } else {
            println("No players online, fill continuing")
            running = true;
        }
        Bukkit.getPluginManager().registerEvents(object : Listener {
            /**
             * When a player joins, if the fill is going, stop it
             */
            @EventHandler
            fun handleJoin(e: PlayerJoinEvent) {
                if (running) {
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "wb fill pause");
                    Bukkit.getLogger().info("Stopped wb fill")
                    running = false;
                }
            }

            /**
             * When a player leaves, check if the server is empty
             * and the fill is not running
             */
            @EventHandler
            fun handleLeave(e: PlayerQuitEvent) {
                if (Bukkit.getServer().onlinePlayers.size-1 == 0 && !running) {
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "wb fill pause");
                    Bukkit.getLogger().info("Started wb fill")
                    running = true;
                }
            }
        }, this);
    }
}