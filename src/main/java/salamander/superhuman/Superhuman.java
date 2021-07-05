package salamander.superhuman;

import org.bukkit.*;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.plugin.Plugin;
import salamander.superhuman.commands.*;
import salamander.superhuman.entities.BlazeMinion;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import salamander.superhuman.events.ClickEvent;
import salamander.superhuman.events.Damage;
import salamander.superhuman.events.PlayerJoin;
import salamander.superhuman.wands.Wand;
import salamander.superhuman.wands.WandRegistry;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public final class Superhuman extends JavaPlugin {
    static public NamespacedKey superTag;
    static public List<VirtualPlayer> virtualPlayers;
    static private Plugin plugin;
    static public WandRegistry wandRegistry;

    public static Plugin getPlugin() {
        return plugin;
    }

    @Override
    public void onEnable() {
        plugin = this;
        wandRegistry = new WandRegistry();
        registerWands();

        superTag = new NamespacedKey(this, "super");
        virtualPlayers = new LinkedList<>();

        getServer().getPluginCommand("minion").setExecutor(new Minion());
        getServer().getPluginCommand("superself").setExecutor(new SuperSelf());
        getServer().getPluginCommand("invis").setExecutor(new Invisible());
        getServer().getPluginCommand("npc").setExecutor(new NPCTest());
        getServer().getPluginCommand("wand").setExecutor(new WandCommand());

        getServer().getPluginManager().registerEvents(new Damage(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoin(), this);
        getServer().getPluginManager().registerEvents(new ClickEvent(), this);

        Bukkit.getScheduler().runTaskTimer(this, () -> {
            for(Player player : getServer().getOnlinePlayers()){
                if(player.getPersistentDataContainer().has(superTag, PersistentDataType.BYTE) && player.isSneaking()){
                    for(Entity entity : player.getNearbyEntities(2, 2, 2)){
                        if((entity instanceof LivingEntity)) {
                            if (!(entity instanceof BlazeMinion)) {
                                if (!entity.getPersistentDataContainer().has(superTag, PersistentDataType.BYTE)) {
                                    if(!((LivingEntity) entity).hasPotionEffect(PotionEffectType.WITHER)) {
                                        ((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 40, 0));
                                    }
                                }
                            }
                        }
                    }
                }
            }
            List<VirtualPlayer> toRemove = new LinkedList<>();
            for(int i = virtualPlayers.size() - 1; i >= 0; i--){
                VirtualPlayer npc = virtualPlayers.get(i);
                if(npc.doVirtualPlayerTick()){
                    toRemove.add(npc);
                }
            }

            for(VirtualPlayer i : toRemove){
                virtualPlayers.remove(i);
            }
        }, 1, 1);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void registerWands(){
        wandRegistry.register(new Wand(Material.BLAZE_ROD, 30000, ChatColor.YELLOW + "Minion Wand", (Player player) -> {
            Location loc = player.getLocation();
            System.out.println("Minion wand used!");

            int locX = loc.getBlockX();
            int locY = loc.getBlockY();
            int locZ = loc.getBlockZ();

            List<Location> validSpawningSpots = new LinkedList<>();

            for(int x = -2; x < 3; x++){
                for(int y = -1; y < 3; y++){
                    for(int z  = -2; z < 3; z++){
                        if(player.getWorld().getBlockAt(locX + x, locY + y, locZ + z).isPassable() && player.getWorld().getBlockAt(locX + x, locY + y + 1, locZ + z).isPassable()){
                            validSpawningSpots.add(new Location(player.getWorld() ,locX + x + 0.5 , locY + y, locZ + z + 0.5));
                        }
                    }
                }
            }

            if(validSpawningSpots.size() == 0){
                System.out.println("No valid spawns!");
                return;
            }

            Random rand = new Random();

            for(int i = rand.nextInt(3) + 3; i >= 0; i--){
                int index = rand.nextInt(validSpawningSpots.size());
                Location spawningLocation = validSpawningSpots.get(index);
                BlazeMinion minion = new BlazeMinion(spawningLocation, player);
                ((CraftWorld) player.getWorld()).getHandle().addEntity(minion);
                if(validSpawningSpots.size() > i){
                    validSpawningSpots.remove(index);
                }
            }

        }, "minionCooldown", "MinionWand"));

        wandRegistry.register(new Wand(Material.SOUL_TORCH, 30000, ChatColor.DARK_PURPLE + "Illusion Wand", (Player player) -> {
            Random rand = new Random();

            for(int i = 0; i < 8; i++){
                new VirtualPlayer(player, (rand.nextDouble() * 8 + 2) * (rand.nextInt(2) * 2 - 1), 0, (rand.nextDouble() * 8 + 2) * (rand.nextInt(2) * 2 - 1));
            }
        }, "illusionCooldown", "IllusionWand"));

        wandRegistry.register(new Wand(Material.GHAST_TEAR, 60000, ChatColor.WHITE + "Invisibility Wand", (Player player) -> {
            for(int i = 0; i < 20; i++) {
                player.getWorld().spawnParticle(Particle.LAVA, player.getLocation().getX(), player.getLocation().getY() + ((double)i) / 10.0f, player.getLocation().getZ(), 5);
            }

            player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 600, 3, true, false));
        }, "invisibilityCooldown", "InvisibilityWand"));
    }

}
