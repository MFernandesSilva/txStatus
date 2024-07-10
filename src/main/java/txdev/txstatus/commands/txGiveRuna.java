package txdev.txstatus.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import txdev.txapibukkit.api.Mensagem;
import txdev.txstatus.itens.Runas;
import txdev.txstatus.runas.TipoRuna;
import txdev.txstatus.txStatus;

public class txGiveRuna implements CommandExecutor {

    private final txStatus plugin;

    public txGiveRuna(txStatus plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("txstatus.admin")) {
            sender.sendMessage(Mensagem.formatar(plugin.getConfiguracao().getPrefix() + plugin.getMensagensPadrao().np()));
            return true;
        }

        if (args.length != 2) {
            sender.sendMessage(Mensagem.formatar(plugin.getConfiguracao().getPrefix() + "&cUso: /txgiverunas <tipo> <nivel>"));
            return true;
        }

        TipoRuna tipoRuna;
        try {
            tipoRuna = TipoRuna.valueOf(args[0].toUpperCase());
        } catch (IllegalArgumentException e) {
            sender.sendMessage(Mensagem.formatar(plugin.getConfiguracao().getPrefix() + "&cTipo de runa inválido."));
            return true;
        }

        int nivel;
        try {
            nivel = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            sender.sendMessage(Mensagem.formatar(plugin.getConfiguracao().getPrefix() + "&cNível deve ser um número."));
            return true;
        }

        if (nivel < 1 || nivel > 5) {
            sender.sendMessage(Mensagem.formatar(plugin.getConfiguracao().getPrefix() + "&cNível inválido (1-5)."));
            return true;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage(Mensagem.formatar(plugin.getMensagensPadrao().cc()));
            return true;
        }

        Player player = (Player) sender;
        ItemStack runa = obterRunaPorTipoENivel(tipoRuna, nivel);

        if (runa != null) {
            player.getInventory().addItem(runa);
            player.sendMessage(Mensagem.formatar(plugin.getConfiguracao().getPrefix() + "&aVocê recebeu uma runa de " + tipoRuna + " nível " + nivel + "."));
        } else {
            player.sendMessage(Mensagem.formatar(plugin.getConfiguracao().getPrefix() + "&cErro ao criar a runa."));
        }

        return true;
    }

    private ItemStack obterRunaPorTipoENivel(TipoRuna tipoRuna, int nivel) {
        Runas runas = new Runas(); // Crie uma instância da classe Runas
        switch (tipoRuna) {
            case DANO:
                return runas.runasDano[nivel - 1];
            case DEFESA:
                return runas.runasDefesa[nivel - 1];
            case AMPLIFICACAO:
                return runas.runasAmplificacao[nivel - 1];
            default:
                return null;
        }
    }
}
