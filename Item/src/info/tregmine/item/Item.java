package info.tregmine.item;


import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import info.tregmine.Tregmine; 
import info.tregmine.api.TregminePlayer;


public class Item extends JavaPlugin {
	public final Logger log = Logger.getLogger("Minecraft");
	public Tregmine tregmine = null;


	@Override
	public void onEnable() {
		Plugin test = this.getServer().getPluginManager().getPlugin("Tregmine");

		if(this.tregmine == null) {
			if(test != null) {
				this.tregmine = ((Tregmine)test);
			} else {
				log.info(this.getDescription().getName() + " " + this.getDescription().getVersion() + " - could not find Tregmine");
				this.getServer().getPluginManager().disablePlugin(this);
			}
		}

	}

	@Override
	public void onDisable() {
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		String commandName = command.getName().toLowerCase();
		Player player = null;
		if(!(sender instanceof Player)) {
			return false;
		} else {
			player = (Player) sender;
		}
		info.tregmine.api.TregminePlayer tregminePlayer = this.tregmine.tregminePlayer.get(player.getName());

		if(commandName.equals("item") && (tregminePlayer.isAdmin() || tregminePlayer.getMetaBoolean("builder"))) {
			int matID, amount, data;

			try {
				matID = Integer.parseInt(args[0]);
			} catch (Exception  e) {
				try {
					matID = Material.getMaterial(args[0].toUpperCase()).getId();
				} catch (NullPointerException ne) {
					matID = 0;
				}
			}

			try {
				amount = Integer.parseInt(args[1]);
			} catch (Exception  e) {
				amount = 1;
			}

			try {
				data = Integer.parseInt(args[2]);
			} catch (Exception  e) {
				data = 0;
			}

			if (matID > 0) {		
				ItemStack item = new ItemStack(matID, amount, (byte) data);
				PlayerInventory inv = player.getInventory();
				if (item.getType() == Material.MONSTER_EGG) {
					player.kickPlayer("Don't do that again ASSHOLE!");
					return true;
				}
				
				ItemMeta meta = item.getItemMeta();
				//meta.setDisplayName(ChatColor.GREEN + Material.getMaterial(matID).toString() + " spawned by " + player.getName());
				List<String> lore = new ArrayList<String>();
				lore.add(ChatColor.RED + "SPAWNED");
				TregminePlayer p = this.tregmine.getPlayer(player);
				lore.add(ChatColor.WHITE + "by: " + p.getChatChannel() );
				lore.add(ChatColor.WHITE + "Value: " + ChatColor.MAGIC + "0" + ChatColor.RESET + ChatColor.WHITE + " Treg" );
				meta.setLore(lore);
				
				item.setItemMeta(meta);
				
				inv.addItem(item);
				player.sendMessage("You received " + amount + " of " + ChatColor.DARK_AQUA + Material.getMaterial(matID).toString().toLowerCase() + ".");
				this.log.info(player.getName() +" SPAWNED " + amount + ":" + Material.getMaterial(matID).toString());
			} else {
				player.sendMessage(ChatColor.DARK_AQUA + "/item <id|name> <amount> <data>.");
			}
			return true;
		}

		if(commandName.equals("einitem") && (tregminePlayer.isAdmin() || tregminePlayer.getMetaBoolean("builder"))) {
			int matID, amount, data;

			try {
				matID = Integer.parseInt(args[0]);
			} catch (Exception  e) {
				try {
					matID = Material.getMaterial(args[0].toUpperCase()).getId();
				} catch (NullPointerException ne) {
					matID = 0;
				}
			}

			try {
				amount = Integer.parseInt(args[1]);
			} catch (Exception  e) {
				amount = 1;
			}

			try {
				data = Integer.parseInt(args[2]);
			} catch (Exception  e) {
				data = 0;
			}

			if (matID > 0) {		
				ItemStack item = new ItemStack(matID, amount, (byte) data);
				PlayerInventory inv = player.getInventory();
				if (item.getType() == Material.MONSTER_EGG) {
					player.kickPlayer("Don't do that again ASSHOLE!");
					return true;
				}
				inv.addItem(item);				
				player.sendMessage("You received " + amount + " of " + ChatColor.DARK_AQUA + Material.getMaterial(matID).toString().toLowerCase() + ".");
				this.log.info(player.getName() +" SPAWNED " + amount + ":" + Material.getMaterial(matID).toString());
			} else {
				player.sendMessage(ChatColor.DARK_AQUA + "/item <id|name> <amount> <data>.");
			}
			return true;
		}
		
		
		if(commandName.equals("give") && tregminePlayer.isAdmin()) {
			int matID, amount, data;
				
			Player to = getServer().matchPlayer(args[0]).get(0);
			
			if (to == null) {
				return false;
			}
			
			
			try {
				matID = Integer.parseInt(args[1]);
			} catch (Exception  e) {
				try {
					matID = Material.getMaterial(args[1].toUpperCase()).getId();
				} catch (NullPointerException ne) {
					matID = 0;
				}
			}

			try {
				amount = Integer.parseInt(args[2]);
			} catch (Exception  e) {
				amount = 1;
			}

			try {
				data = Integer.parseInt(args[3]);
			} catch (Exception  e) {
				data = 0;
			}

			if (matID > 0) {		
				ItemStack item = new ItemStack(matID, amount, (byte) data);
				PlayerInventory inv = to.getInventory();
				if (item.getType() == Material.MONSTER_EGG) {
					player.kickPlayer("Don't do that again ASSHOLE!");
					return true;
				}
				inv.addItem(item);
				player.sendMessage("You gave " + amount + " of " + ChatColor.DARK_AQUA + Material.getMaterial(matID).toString().toLowerCase() + " to " + to.getName() + ".");
				this.log.info(player.getName() +" SPAWNED " + amount + ":" + Material.getMaterial(matID).toString() + "=>" + to.getName());
				to.sendMessage(ChatColor.YELLOW + "You were gifted by the gods. Look in your inventory!");
			} else {
				player.sendMessage(ChatColor.DARK_AQUA + "/give <player> <id|name> <amount> <data>");
			}
			return true;
		}

		return false;
	}
	
	@Override
	public void onLoad() {
	}

}
