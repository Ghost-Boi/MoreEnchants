package ghostboi.moreenchants;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;


public final class MoreEnchants extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        // Plugin startup logic
        CustomEnchants.register();

        this.getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public  boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        if(label.equalsIgnoreCase("telepathy")){
            if(!(sender instanceof Player))
                return true;
            Player player = (Player) sender;
            ItemStack item = new ItemStack(Material.DIAMOND_PICKAXE);
            item.addUnsafeEnchantment(CustomEnchants.TELEPATHY, 1);

            ItemMeta meta = item.getItemMeta();
            List<String> lore = new ArrayList<String>();
            lore.add(ChatColor.GRAY + "Telepathy");
            if(meta.hasLore())
                for(String l : meta.getLore())
                    lore.add(l);
            meta.setLore(lore);
            item.setItemMeta(meta);

            player.getInventory().addItem(item);
            return true;
        }else if(label.equalsIgnoreCase("autosmelt")) {
            if (!(sender instanceof Player))
                return true;
            Player player = (Player) sender;
            ItemStack item = new ItemStack(Material.DIAMOND_PICKAXE);
            item.addUnsafeEnchantment(CustomEnchants.AUTOSMELT, 1);

            ItemMeta meta = item.getItemMeta();
            List<String> lore = new ArrayList<String>();
            lore.add(ChatColor.GRAY + "Autosmelt");
            if (meta.hasLore())
                for (String l : meta.getLore())
                    lore.add(l);
            meta.setLore(lore);
            item.setItemMeta(meta);

            player.getInventory().addItem(item);
            return true;
        }
        return true;
    }

    @EventHandler()
    public void onBlockBreak(BlockBreakEvent event){
        if(event.getPlayer().getInventory().getItemInMainHand() == null)
            return;
        if(!event.getPlayer().getInventory().getItemInMainHand().hasItemMeta())
            return;

        // TELEPATHY ENCHANT
        if(event.getPlayer().getInventory().getItemInMainHand().getItemMeta().hasEnchant(CustomEnchants.TELEPATHY)) {
            if (event.getPlayer().getGameMode() == GameMode.CREATIVE || event.getPlayer().getGameMode() == GameMode.SPECTATOR)
                return;
            if(event.getBlock().getState() instanceof Container)
                return;

            event.setDropItems(false);
            Player player = event.getPlayer();
            Block block = event.getBlock();

            Collection<ItemStack> drops = block.getDrops(player.getInventory().getItemInMainHand());
            if(drops.isEmpty())
                return;
            if (event.getPlayer().getInventory().firstEmpty() == -1){
                block.getWorld().dropItemNaturally(block.getLocation(), drops.iterator().next());
            }else{
                player.getInventory().addItem(drops.iterator().next());
            }
        }

        //AUTOSMELT ENCHANT
        if(event.getPlayer().getInventory().getItemInMainHand().getItemMeta().hasEnchant(CustomEnchants.AUTOSMELT)) {
            if (event.getPlayer().getGameMode() == GameMode.CREATIVE || event.getPlayer().getGameMode() == GameMode.SPECTATOR)
                return;
            event.setDropItems(false);
            Block block = event.getBlock();
            Player player = event.getPlayer();
            ItemStack drops = null;

            if(block.getType() == Material.IRON_ORE){
                drops = new ItemStack(Material.IRON_INGOT);
                block.getWorld().dropItemNaturally(block.getLocation(), drops);
                block.getWorld().spawnParticle(Particle.FLAME, block.getLocation(), 2);
                return;
            }else if(block.getType() == Material.GOLD_ORE){
                drops = new ItemStack(Material.GOLD_INGOT);
                block.getWorld().dropItemNaturally(block.getLocation(), drops);
                block.getWorld().spawnParticle(Particle.FLAME, block.getLocation(), 2);
                return;
            }else if(block.getType() == Material.ANCIENT_DEBRIS){
                drops = new ItemStack(Material.NETHERITE_SCRAP);
                block.getWorld().dropItemNaturally(block.getLocation(), drops);
                block.getWorld().spawnParticle(Particle.FLAME, block.getLocation(), 2);
                return;
            }
            Collection<ItemStack> _drops = block.getDrops(player.getInventory().getItemInMainHand());
            if(_drops.isEmpty())
                return;
            block.getWorld().dropItemNaturally(block.getLocation(), _drops.iterator().next());
        }
    }
}
