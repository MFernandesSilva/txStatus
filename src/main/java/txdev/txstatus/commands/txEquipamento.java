package txdev.txstatus.commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import txdev.txapibukkit.api.Item;
import txdev.txapibukkit.api.Mensagem;
import txdev.txstatus.txStatus;

import java.util.Arrays;

public class txEquipamento implements CommandExecutor {

    private final txStatus plugin;

    public txEquipamento(txStatus plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("txstatus.admin")) {
            sender.sendMessage(Mensagem.formatar(plugin.getConfiguracao().getPrefix() + plugin.getMensagensPadrao().np()));
            return true;
        }

        if (args.length != 6) {
            sender.sendMessage(Mensagem.formatar(plugin.getConfiguracao().getPrefix() + "&cUso: /txequipamento <espada/capacete/peitoral/calca/botas> <nome> <danobase> <ampdano> <defesabase> <ampdefesa>"));
            return true;
        }

        String tipoEquipamento = args[0].toLowerCase();
        String nome = args[1];
        double danoBase, ampDano, defesaBase, ampDefesa;

        try {
            danoBase = Double.parseDouble(args[2]);
            ampDano = Double.parseDouble(args[3]);
            defesaBase = Double.parseDouble(args[4]);
            ampDefesa = Double.parseDouble(args[5]);
        } catch (NumberFormatException e) {
            sender.sendMessage(Mensagem.formatar(plugin.getConfiguracao().getPrefix() + "&cOs valores de atributos devem ser números."));
            return true;
        }

        Material material;
        switch (tipoEquipamento) {
            case "espada":
                material = Material.DIAMOND_SWORD;
                break;
            case "capacete":
                material = Material.DIAMOND_HELMET;
                break;
            case "peitoral":
                material = Material.DIAMOND_CHESTPLATE;
                break;
            case "calca":
                material = Material.DIAMOND_LEGGINGS;
                break;
            case "botas":
                material = Material.DIAMOND_BOOTS;
                break;
            default:
                sender.sendMessage(Mensagem.formatar(plugin.getConfiguracao().getPrefix() + "&cTipo de equipamento inválido."));
                return true;
        }

        ItemStack equipamento = new Item(material, 1, (short) 0)
                .setName(ChatColor.translateAlternateColorCodes('&', nome))
                .setUnbreakable(true)
                .setNBT("danoBase", danoBase)
                .setNBT("ampDano", ampDano)
                .setNBT("defesaBase", defesaBase)
                .setNBT("ampDefesa", ampDefesa)
                .getIs();

        if (sender instanceof Player) {
            Player player = (Player) sender;
            player.getInventory().addItem(equipamento);
            player.sendMessage(Mensagem.formatar(plugin.getConfiguracao().getPrefix() + "&aEquipamento criado com sucesso!"));
        } else {
            sender.sendMessage(Mensagem.formatar("&cEste comando só pode ser executado por jogadores."));
        }

        return true;
    }
}
