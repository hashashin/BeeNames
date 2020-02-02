package me.hashashin.BeeNames;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReloadCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {
        BeeNames bn = BeeNames.getInstance();
        bn.reloadConfig();
        bn.getLogger().info("Config reloaded " + bn.getConfig().getStringList("names").size()
                + " names in the list. "+ bn.getDescription().getName() +
                " v" + bn.getDescription().getVersion());
        return true;
    }
}
