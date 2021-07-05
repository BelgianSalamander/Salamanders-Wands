package salamander.superhuman.commands;

import org.bukkit.ChatColor;
import org.bukkit.Particle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

public class Invisible implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage(ChatColor.RED + "Only players can run this command!");
        }

        Player player = (Player) sender;

        for(int i = 0; i < 20; i++) {
            player.getWorld().spawnParticle(Particle.LAVA, player.getLocation().getX(), player.getLocation().getY() + ((double)i) / 10.0f, player.getLocation().getZ(), 5);
        }

        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 600, 3, true, false));

        return true;
    }
}
