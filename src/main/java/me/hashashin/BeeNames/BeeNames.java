/*
BeeNames, simple bukkit plugin to auto name bees that live in a bee hive.
    Copyright (C) 2020  hashashin

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/
package me.hashashin.BeeNames;

import org.bukkit.Material;
import org.bukkit.entity.Bee;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.List;
import java.util.Random;


public class BeeNames extends JavaPlugin implements Listener  {

	private static BeeNames instance;

	public static BeeNames getInstance() {
		return instance;
	}

	@Override
	public void onEnable() {
		// Define the config.yml
		instance = this;
		final File check = new File(this.getDataFolder(), "config.yml");
		// Check for existing config.yml, if not create it
		if (!check.exists()) {
			this.saveDefaultConfig();
			this.reloadConfig();
		}
		// Register commands
		this.getCommand("bn-reload").setExecutor(new ReloadCommand());
		this.getCommand("bn-debug").setExecutor(new DebugCommand());
		// Register events
		this.getServer().getPluginManager().registerEvents(this, this);
		// Log loading
		this.getLogger().info("Loaded " + this.getDescription().getName() +
				" v" + this.getDescription().getVersion());
	}

	@Override
	public void onDisable() {
		// Log disabling
		this.getLogger().info("Disabled " + this.getDescription().getName() +
				" v" + this.getDescription().getVersion());
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onCreatureSpawn(CreatureSpawnEvent event) {
		// Check if Creature is BEE
		if(event.getEntityType() == EntityType.BEE) {
			if (this.getConfig().getBoolean("debug")) {
				this.getLogger().info(event.getSpawnReason().name());
			}
			// Check if the bee spawn reason was a beehive, then check if already has a name
			if (event.getSpawnReason().name().equals("BEEHIVE") && event.getEntity().getCustomName() != null) {
				if (this.getConfig().getBoolean("debug")) {
					this.getLogger().info("This bee already has a name: " + event.getEntity().getCustomName());
				}
				return;
			}
			// Check if event is instance of Bee
			if(event.getEntity() instanceof Bee) {
				// Check if the Bee lives in the wild
				if (((Bee) event.getEntity()).getHive() == null) {
					return;
				} else
					if (((Bee) event.getEntity()).getHive().getBlock().getType()
						== Material.BEE_NEST) {
					if (this.getConfig().getBoolean("debug")) {
						this.getLogger().info("This bee belongs to a nest.");
					}
					return;
				}
				// Load the bee names out of config.yml into a List
				List<?> names = this.getConfig().getList("names");
				// Pick a random name from that list
				String random_name = null;
				if (names != null) {
					Random rand = new Random();
					random_name = names.get(rand.nextInt(names.size())).toString();
				}

				// Set the custom name of the bee
				event.getEntity().setCustomName(random_name);
				event.getEntity().setCustomNameVisible(true);
				
				// Check if debug mode is enabled
				if (this.getConfig().getBoolean("debug")) {
					// Log the actions
					this.getLogger().info("Bee '"+ random_name +"' spawned at location: '"
							+ event.getLocation().getWorld().toString()
							+ "," + event.getLocation().getX() + ","
							+ event.getLocation().getY() + "," + event.getLocation().getZ() +"'");
				}
			}
		}
	}
}
