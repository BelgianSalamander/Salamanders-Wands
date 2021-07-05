package salamander.superhuman.events;

import net.minecraft.server.v1_16_R3.Item;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftItem;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import salamander.superhuman.Superhuman;

public class ClickEvent implements Listener {
    @EventHandler
    public void click(PlayerInteractEvent e){
        ItemStack item = e.getItem();
        if(item == null) return;

        ItemMeta meta = item.getItemMeta();
        if(Superhuman.wandRegistry.isItemWand(e.getItem())){
            Superhuman.wandRegistry.doAction(e.getItem(), e.getPlayer());
            e.setCancelled(true);
        }
    }
}
