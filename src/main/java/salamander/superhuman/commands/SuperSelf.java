package salamander.superhuman.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import salamander.superhuman.Superhuman;

public class SuperSelf implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage(ChatColor.RED + "Only players can run this command!");
            return true;
        }

        ((Player) sender).getPersistentDataContainer().set(Superhuman.superTag, PersistentDataType.BYTE, (byte) 1);

        return true;
    }
}
