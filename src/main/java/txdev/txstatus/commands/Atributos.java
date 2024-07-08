package txdev.txstatus.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import txdev.txapibukkit.api.Mensagem;
import txdev.txstatus.gui.AtributosGUI;
import txdev.txstatus.txStatus;

public class Atributos implements CommandExecutor {

    private final txStatus plugin;

    public Atributos(txStatus plugin) {this.plugin = plugin;}

    @Override
    public boolean onCommand(CommandSender s, Command c, String label, String[] args) {
        if (!(s instanceof Player)) {
            s.sendMessage(Mensagem.formatar(plugin.getMensagensPadrao().cc()));
            return true;
        }

        Player p = (Player) s;

        if (!plugin.getPlayerData().containsKey(p.getUniqueId())) {
            p.sendMessage(Mensagem.formatar(plugin.getConfiguracao().getPrefix() + " &7Relogue para carregar seus dados."));
            return true;
        }

        AtributosGUI.abrirGUI(p, plugin);
        return true;
    }
}
