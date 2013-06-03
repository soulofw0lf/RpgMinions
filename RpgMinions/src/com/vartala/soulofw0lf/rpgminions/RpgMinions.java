package com.vartala.soulofw0lf.rpgminions;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import de.kumpelblase2.remoteentities.EntityManager;
import de.kumpelblase2.remoteentities.RemoteEntities;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.RemoteEntityType;
import de.kumpelblase2.remoteentities.api.features.RemoteTamingFeature;
import de.kumpelblase2.remoteentities.api.features.TamingFeature;
import de.kumpelblase2.remoteentities.api.thinking.DamageBehavior;
import de.kumpelblase2.remoteentities.api.thinking.InteractBehavior;
import de.kumpelblase2.remoteentities.api.thinking.goals.DesireFollowTamer;
import de.kumpelblase2.remoteentities.api.thinking.goals.DesireLookAtNearest;
import de.kumpelblase2.remoteentities.persistence.serializers.YMLSerializer;

public class RpgMinions extends JavaPlugin implements Listener{
	private EntityManager npcManager;
	public void onEnable()
	{
		Bukkit.getPluginManager().registerEvents(this, this);
		this.npcManager = RemoteEntities.createManager(this);
		Double X = 2.3417384896988276;
		Double Y = 27.0;
		Double Z = -90.16451800337704;
	    String world = "spawn";
	    World thisworld = Bukkit.getWorld(world);
	    Location newloc = new Location(thisworld, X, Y, Z);
		RemoteEntity entity = this.npcManager.createNamedEntity(RemoteEntityType.Human, newloc,"Messenger", false);
		entity.setStationary(true);
		entity.setPushable(false);
		entity.setStationary(true);
		entity.getMind().addMovementDesire(new DesireLookAtNearest(entity, Player.class, 8F), 10);
		entity.getMind().addBehaviour(new InteractBehavior(entity)
		{
			@Override
			public void onInteract(Player inPlayer)
			{
				inPlayer.sendMessage("Hey guys, I just wanted you to know I'm doing some major programming so right now the server isn't very playable!");
				inPlayer.sendMessage("Sorry for the inconvenience but it's going to be extremely worth it!!");
			}
		});entity.getMind().addBehaviour(new DamageBehavior(entity) {
			@Override
			public void onDamage(EntityDamageEvent event) {
				event.setCancelled(true);
			}
		});
		entity.getBukkitEntity().getEquipment().setBoots(new ItemStack(309, 1));
		entity.getBukkitEntity().getEquipment().setLeggings(new ItemStack(308, 1));
		entity.getBukkitEntity().getEquipment().setChestplate(new ItemStack(307, 1));
		entity.getBukkitEntity().getEquipment().setHelmet(new ItemStack(306, 1));
		entity.getBukkitEntity().getEquipment().setItemInHand(new ItemStack(267, 1));
		
		this.npcManager.setEntitySerializer(new YMLSerializer(this));
		this.npcManager.saveEntities();
	}
	public void onchunkload(ChunkLoadEvent event){
		this.npcManager.loadEntities();
	}
	@Override
	public void onDisable(){
	}
	@EventHandler
	public void onJoin(PlayerJoinEvent inEvent) throws Exception
	{
		Player p = inEvent.getPlayer();
		final Player player = p;
		if (!(p.getLocation().getWorld().getName().equalsIgnoreCase("Spawn"))){
			return;
		}
		RemoteEntity entity = this.npcManager.createEntity(RemoteEntityType.Villager, inEvent.getPlayer().getLocation(), false);
		Villager z = (Villager)entity.getBukkitEntity();
        z.setBaby();
		TamingFeature feature = new RemoteTamingFeature(entity);
		feature.tame(inEvent.getPlayer());
		  
          entity.setName(p.getName() + "'s Guide");
		entity.getFeatures().addFeature(feature);
		entity.setPushable(false);
		entity.setSpeed(2);
		entity.getMind().addMovementDesire(new DesireFollowTamer(entity, 5, 15), entity.getMind().getHighestMovementPriority() + 1);
		entity.getMind().addBehaviour(new InteractBehavior(entity)
		{
			@Override
			public void onInteract(Player inPlayer)
			{
				if (!(inPlayer.getName().equalsIgnoreCase(player.getName()))){
					return;
				}
				inPlayer.sendMessage("Please pay attention to the tutorial master! Or you can use the magical command §4tutskip");
				inPlayer.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 50, 1), true);
			}
		});entity.getMind().addBehaviour(new DamageBehavior(entity) {
			@Override
			public void onDamage(EntityDamageEvent event) {
				event.setCancelled(true);
			}
		});
		this.npcManager.loadEntities();
	}
}

