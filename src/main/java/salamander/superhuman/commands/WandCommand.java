package salamander.superhuman.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import salamander.superhuman.Superhuman;

public class WandCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player)){
            return true;
        }

        ((Player) sender).getInventory().setItemInMainHand(Superhuman.wandRegistry.getWand(args[0]).getItem());
        
        return true;
    }
}
