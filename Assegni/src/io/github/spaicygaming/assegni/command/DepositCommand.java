package io.github.spaicygaming.assegni.command;

import io.github.spaicygaming.assegni.NotesPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Matthew on 14/01/2015.
 */
public class DepositCommand implements CommandExecutor {

    /*
     * The plugin instance
     */
    private NotesPlugin plugin;

    /**
     * Creates the "/deposit" command handler
     */
    public DepositCommand(NotesPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can deposit bank notes");
        } else if (!sender.hasPermission("banknotes.deposit")) {
            sender.sendMessage(plugin.getMessage("messages.insufficient-permissions"));
        } else {
            Player player = (Player) sender;
            ItemStack item = player.getItemInHand();

            if (item != null && plugin.isBanknote(item)) {
                double amount = plugin.getBanknoteAmount(item);

                if (Double.compare(amount, 0) > 0) {
                    // Double check the response
                    plugin.getEconomy().depositPlayer(player, amount);
                    player.sendMessage(plugin.getMessage("messages.note-redeemed")
                            .replace("[money]", plugin.formatDouble(amount)));
                } else {
                    player.sendMessage(plugin.getMessage("messages.invalid-note"));
                }

                // Remove the slip
                if (item.getAmount() <= 1) {
                    player.getInventory().removeItem(item);
                } else {
                    item.setAmount(item.getAmount() - 1);
                }
            } else {
                player.sendMessage(plugin.getMessage("messages.nothing-in-hand"));
            }
        }
        return true;
    }
}
