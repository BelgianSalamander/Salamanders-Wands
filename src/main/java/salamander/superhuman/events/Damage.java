package salamander.superhuman.events;

import net.minecraft.server.v1_16_R3.DamageSource;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.persistence.PersistentDataType;
import salamander.superhuman.Superhuman;

public class Damage implements Listener {
    @EventHandler
    public void onDamage(EntityDamageEvent e){
        if(!e.getEntity().getPersistentDataContainer().has(Superhuman.superTag, PersistentDataType.BYTE)){
            return;
        }
        EntityDamageEvent.DamageCause cause = e.getCause();
        if(cause.equals(DamageSource.HOT_FLOOR) ||
                cause.equals(DamageSource.BURN) ||
                cause.equals(DamageSource.LAVA) ||
                cause.equals(DamageSource.FIRE)
        ){
            e.setCancelled(true);
        }
    }
}
