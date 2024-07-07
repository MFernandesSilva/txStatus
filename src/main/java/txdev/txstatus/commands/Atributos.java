package txdev.txstatus.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import txdev.txapibukkit.api.DefaultMessages;
import txdev.txapibukkit.api.Mensagem;
import txdev.txstatus.gui.AtributosGUI;
import txdev.txstatus.txStatus;

public class Atributos implements CommandExecutor {

    private final txStatus plugin;

    public Atributos(txStatus plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Mensagem.formatar(plugin.getMensagensPadrao().cc()));
            return true;
        }

        Player player = (Player) sender;

        // Verificar se o jogador tem os dados carregados
        if (!plugin.getPlayerData().containsKey(player.getUniqueId())) {
            player.sendMessage(Mensagem.formatar(plugin.getConfiguracao().getPrefix() + " &7Seus dados ainda estão sendo carregados. Aguarde um momento."));
            return true;
        }

        AtributosGUI.abrirGUI(player, plugin);
        return true;
    }
}
