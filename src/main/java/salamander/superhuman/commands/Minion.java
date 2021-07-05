package salamander.superhuman.commands;

import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import salamander.superhuman.Superhuman;
import salamander.superhuman.entities.BlazeMinion;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import salamander.superhuman.wands.Wand;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Minion implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage(ChatColor.RED + "Only players can run this command!");
            return true;
        }

        Player player = (Player) sender;

        return true;
    }
}
