package salamander.superhuman.entities;

import net.minecraft.server.v1_16_R3.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;

public class BlazeMinion extends EntityBlaze {
    EntityPlayer owner;
    private int timeOfLife;

    public BlazeMinion(Location location, Player owner) {
        super(EntityTypes.BLAZE, ((CraftWorld) location.getWorld()).getHandle());
        this.setPosition(location.getX(), location.getY(), location.getZ());
        this.expToDrop = 1;
        this.owner = ((CraftPlayer) owner).getHandle();
        timeOfLife = 0;
    }

    @Override
    protected void initPathfinder(){
        this.targetSelector.a(4, createShootFireballGoal(this));
        this.goalSelector.a(5, new PathfinderGoalMoveTowardsRestriction(this, 1.0));
        this.goalSelector.a(7, new PathfinderGoalRandomStrollLand(this, 1.0, 0.0f));
        this.goalSelector.a(8, new PathfinderGoalLookAtPlayer(this, EntityPlayer.class, 8.0f));
        this.goalSelector.a(8, new PathfinderGoalRandomLookaround(this));
        //this.targetSelector.a(1, new RevengeGoal(this, new Class[0]).setGroupRevenge(new Class[0]));
        this.targetSelector.a(1, new PathfinderGoalNearestAttackableTarget<EntityPlayer>(this, EntityPlayer.class, 10, true, false, entity -> !this.owner.equals(entity)));
    }

    @Override
    public void mobTick(){
        if(timeOfLife > 600 && timeOfLife % 10 == 0){
            this.damageEntity(DamageSource.MAGIC, (timeOfLife - 500) / 100);
        }
        timeOfLife++;
        super.mobTick();
    }

    private static PathfinderGoal createShootFireballGoal(EntityBlaze entity){
        try {
            Class<?> clazz = Class.forName("net.minecraft.server.v1_16_R3.EntityBlaze$PathfinderGoalBlazeFireball");
            Constructor<?> ctr = clazz.getConstructor(EntityBlaze.class);
            ctr.setAccessible(true);
            return (PathfinderGoal) ctr.newInstance(entity);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
