package salamander.superhuman.wands;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import salamander.superhuman.Superhuman;

import java.util.HashMap;
import java.util.Map;

public class WandRegistry {
    Map<String, Wand> wands;
    static NamespacedKey key = new NamespacedKey(Superhuman.getPlugin(), "wand");

    public WandRegistry(){
        wands = new HashMap<>();
    }

    public void register(Wand wand){
        wands.put(wand.getIdentifier(), wand);
    }

    public boolean isItemWand(ItemStack item){
        if(item == null) return false;
        if(item.getItemMeta() == null) return false;
        return wands.containsKey(item.getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.STRING));
    }

    public Wand getWand(String name){
        return wands.get(name);
    }

    public void doAction(ItemStack item, Player player){
        if(item == null) return;
        if(item.getItemMeta() == null) return;
        wands.get(item.getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.STRING)).onClick(player);
    }
}
