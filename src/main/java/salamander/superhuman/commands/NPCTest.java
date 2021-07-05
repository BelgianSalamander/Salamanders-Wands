package salamander.superhuman.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import salamander.superhuman.Superhuman;
import salamander.superhuman.VirtualPlayer;

import java.util.Random;

public class NPCTest implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage(ChatColor.RED + "Only players can execute this command!");
            return true;
        }

        Player player = (Player) sender;

        Random rand = new Random();

        for(int i = 0; i < 8; i++){
            new VirtualPlayer(player, (rand.nextDouble() * 8 + 2) * (rand.nextInt(2) * 2 - 1), 0, (rand.nextDouble() * 8 + 2) * (rand.nextInt(2) * 2 - 1));
        }

        return true;
    }
}
