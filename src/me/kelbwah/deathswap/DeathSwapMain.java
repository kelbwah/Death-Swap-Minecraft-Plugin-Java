package me.kelbwah.deathswap;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import net.md_5.bungee.api.ChatColor;

public class DeathSwapMain extends JavaPlugin{
	
	public static ArrayList<Player> players = new ArrayList<Player>();
	
	@Override
	public void onEnable()
	{
		Bukkit.getServer().getPluginManager().registerEvents(this, this);
	}
	
	public void onDisable() {}
	
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if(label.equalsIgnoreCase("player1"))
		{
			if(sender instanceof Player)
			{
				Player p1 = (Player) sender;
				players.add(0, p1);
				if(p1.hasPermission("player1.use"))
				{
					p1.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "You are now player one!");
					return true;
				}
				p1.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "You do not have permission to use this");
				return true;
			}
		}
		else if(label.equalsIgnoreCase("player2"))
		{
			if(sender instanceof Player)
			{
				Player p2 = (Player) sender;
				players.add(p2);
				if(p2.hasPermission("player2.use"))
				{
					p2.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "You are now player two!");
					return true;
				}
				p2.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "You do not have permission to use this");
				return true;
			}
			
		}
		else if(label.equalsIgnoreCase("swapstart"))
		{
			Plugin plugin = this;
			Player theSender = (Player) sender;
			if(theSender.hasPermission("swapstart.use"))
			{
				for(Player player : players)
				{
					player.sendMessage("The game has begun. There will be 5 minutes between each swap.");
				}
				
				BukkitScheduler scheduler = getServer().getScheduler();
				scheduler.scheduleSyncRepeatingTask(this, new Runnable() {
					@Override
					public void run() {
						
						Bukkit.getScheduler().runTaskTimer(plugin, new Runnable()
						{
							int time = 10;
							//Put @Override just in case
							public void run()
							{
								if(time >= 0)
								{
									for(Player p : Bukkit.getOnlinePlayers())
									{					
										if(time > 0)
										{
											p.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + this.time + "...");
										}
									}
									this.time--;
								}
								if(this.time < 0)
								{
									if(this.time == -300)
									{
										time = 10;
										return;
									}
								}							
								if(this.time == 0)
								{
									for(Player p : Bukkit.getOnlinePlayers())
									{
										p.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "Swap!");
									}
									Location player1Loc = players.get(0).getLocation();
									Location player2Loc = players.get(1).getLocation();
									
									players.get(0).teleport(player2Loc);
									players.get(1).teleport(player1Loc);
									return;
								}
							}
							
						}, 0L, 20L);
					}
				}, 0L, 6000L);
				
				return true;
				
			}
			theSender.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "You do not have permission to use this");
			return true;
			
		}
		return true;
		
	}
	
}
