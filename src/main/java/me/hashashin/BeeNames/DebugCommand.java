package me.hashashin.BeeNames;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class DebugCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        BeeNames bn = BeeNames.getInstance();
        if (bn.getConfig().getBoolean("debug")) {
            bn.getConfig().set("debug", false);
            bn.getLogger().info("Debug log disabled.");
        }
        else {
            bn.getConfig().set("debug", true);
            bn.getLogger().info("Debug log enabled.");
        }
        bn.saveConfig();
        return true;
    }
}
