package salamander.superhuman;

import com.mojang.authlib.GameProfile;
import com.mojang.datafixers.util.Pair;
import net.minecraft.server.v1_16_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftItem;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.List;

public class VirtualPlayer extends EntityPlayer{
    private Player realPlayer;
    private double offsetX, offsetZ, offsetY;
    private List<Pair<EnumItemSlot, ItemStack>> prevMaterialList = new LinkedList<>();
    private int timeSinceSpawn = 0;

    public VirtualPlayer(Player player, double offsetX, double offsetY, double offsetZ){
        super(((CraftServer) Bukkit.getServer()).getServer(), ((CraftWorld) player.getWorld()).getHandle(), new GameProfile(player.getUniqueId(), player.getName()), new PlayerInteractManager(((CraftWorld) player.getWorld()).getHandle()));
        MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
        WorldServer world = ((CraftWorld) player.getWorld()).getHandle();

        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetZ = offsetZ;

        Location loc = player.getLocation();
        this.setLocation(loc.getX() + offsetX, loc.getY() + offsetY, loc.getZ() + offsetZ, loc.getPitch(), loc.getYaw());

        realPlayer = player;

        for(Player all : Bukkit.getOnlinePlayers()){
            PlayerConnection connection = ((CraftPlayer) all).getHandle().playerConnection;
            connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, this));
            connection.sendPacket(new PacketPlayOutNamedEntitySpawn(this));
            connection.sendPacket(new PacketPlayOutEntityHeadRotation(this, (byte)(this.yaw * 256 / 360)));
            connection.sendPacket(new PacketPlayOutEntity.PacketPlayOutEntityLook(this.getId(), (byte)(this.yaw * 256 / 360), (byte)(this.pitch * 256 / 360), true));
        }

        Superhuman.virtualPlayers.add(this);
    }

    public void copyPlayer(){
        Location loc = realPlayer.getLocation();
        double prevX = this.locX();
        double prevY = this.locY();
        double prevZ = this.locZ();
        this.setLocation(loc.getX() + offsetX, loc.getY() + offsetY, loc.getZ() + offsetZ, loc.getPitch(), loc.getYaw());
        PacketPlayOutEntity.PacketPlayOutRelEntityMoveLook movePacket = new PacketPlayOutEntity.PacketPlayOutRelEntityMoveLook(this.getId(), (short)( (this.locX() - prevX) * 4096),
                (short)((this.locY() - prevY) * 4096),
                (short)((this.locZ() - prevZ) * 4096),
                (byte)(loc.getYaw() * 256 / 360),
                (byte)(loc.getPitch() * 256 / 360),
                false);
        PacketPlayOutEntityHeadRotation rotationPacket = new PacketPlayOutEntityHeadRotation(this, (byte)(loc.getYaw() * 256 / 360));

        List<Pair<EnumItemSlot, ItemStack>> material = new LinkedList<>();
        material.add(new Pair<>(EnumItemSlot.MAINHAND, CraftItemStack.asNMSCopy(realPlayer.getInventory().getItemInMainHand())));
        material.add(new Pair<>(EnumItemSlot.OFFHAND, CraftItemStack.asNMSCopy(realPlayer.getInventory().getItemInOffHand())));
        material.add(new Pair<>(EnumItemSlot.HEAD, CraftItemStack.asNMSCopy(realPlayer.getInventory().getHelmet())));
        material.add(new Pair<>(EnumItemSlot.CHEST, CraftItemStack.asNMSCopy(realPlayer.getInventory().getChestplate())));
        material.add(new Pair<>(EnumItemSlot.LEGS, CraftItemStack.asNMSCopy(realPlayer.getInventory().getLeggings())));
        material.add(new Pair<>(EnumItemSlot.FEET, CraftItemStack.asNMSCopy(realPlayer.getInventory().getBoots())));

        PacketPlayOutEntityEquipment equipmentPacket = new PacketPlayOutEntityEquipment(this.getId(), material);

        boolean sendEquipmentPacket = false;
        if(prevMaterialList.size() > 0) {
            for (int i = 0; i < 6; i++) {
                if (!material.get(i).getSecond().getName().equals(prevMaterialList.get(i).getSecond().getName())){
                    sendEquipmentPacket = true;
                }
            }
        }else{
            sendEquipmentPacket = true;
        }

        for(Player all : Bukkit.getOnlinePlayers()){
            PlayerConnection connection = ((CraftPlayer) all).getHandle().playerConnection;
            connection.sendPacket(rotationPacket);
            connection.sendPacket(movePacket);
            if(sendEquipmentPacket) {
                connection.sendPacket(equipmentPacket);
                System.out.println("Sent equipment packet!");
            }
        }

        if(sendEquipmentPacket){
            prevMaterialList = material;
        }
    }

    public boolean doVirtualPlayerTick(){
        this.copyPlayer();
        timeSinceSpawn++;
        if(timeSinceSpawn > 400){
            this.removeSelf();
            return true;
        }
        return false;
    }

    public void removeSelf(){
        PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(this.getId());
        for(Player player : Bukkit.getOnlinePlayers()){
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
        }

        Superhuman.virtualPlayers.remove(this);
    }

    @Override
    public void collide(Entity entity){}
}
