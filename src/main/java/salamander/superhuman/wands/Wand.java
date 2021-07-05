package salamander.superhuman.wands;

import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.ChatMessageType;
import net.minecraft.server.v1_16_R3.IMaterial;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import salamander.superhuman.Superhuman;

import java.awt.*;
import java.util.function.Function;

public class Wand {
    private ItemStack item;
    private long cooldown;
    private WandAction action;
    private NamespacedKey cooldownKey;
    private String identifier;
    public Wand(Material material, long cooldown, String displayName, WandAction action, String cooldownName, String identifier){
        item = new ItemStack(material, 1);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.text(displayName));

        meta.getPersistentDataContainer().set(WandRegistry.key, PersistentDataType.STRING, identifier);

        item.setItemMeta(meta);

        this.identifier = identifier;

        this.cooldown = cooldown;
        this.action = action;
        this.cooldownKey = new NamespacedKey(Superhuman.getPlugin(), cooldownName);
    }

    public void onClick(Player player){
        Long lastCooldown = player.getPersistentDataContainer().get(cooldownKey, PersistentDataType.LONG);

        if(lastCooldown == null){
            player.getPersistentDataContainer().set(cooldownKey, PersistentDataType.LONG, System.currentTimeMillis());
            //System.out.println("Player doesn't have cooldown!");
            action.method(player);
        }else if(System.currentTimeMillis() -  lastCooldown > cooldown){
            player.getPersistentDataContainer().set(cooldownKey, PersistentDataType.LONG, System.currentTimeMillis());
            //System.out.println("Current Time: " + System.currentTimeMillis() + " Last Used: " + lastCooldown + " Cooldown: " + cooldown);
            action.method(player);
        }else{
            player.sendMessage(ChatColor.RED + "That wand is on cooldown!");
        }
    }

    public ItemStack getItem(){
        return item;
    }

    public String getIdentifier(){
        return identifier;
    }
}
