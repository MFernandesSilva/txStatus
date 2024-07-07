package txdev.txstatus.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import txdev.txapibukkit.api.Mensagem;
import txdev.txstatus.database.PlayerData;
import txdev.txstatus.txStatus;

public class VerAtributos implements CommandExecutor {

    private final txStatus plugin;

    public VerAtributos(txStatus plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("txstatus.admin")) {
            sender.sendMessage(Mensagem.formatar(plugin.getConfiguracao().getPrefix() + plugin.getMensagensPadrao().np()));
            return true;
        }

        if (args.length != 1) {
            sender.sendMessage(Mensagem.formatar(plugin.getConfiguracao().getPrefix() + "&cUso: /veratributos <jogador>"));
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(Mensagem.formatar(plugin.getConfiguracao().getPrefix() + "&cJogador não encontrado."));
            return true;
        }

        PlayerData playerData = plugin.getPlayerData().get(target.getUniqueId());
        if (playerData == null) {
            sender.sendMessage(Mensagem.formatar(plugin.getConfiguracao().getPrefix() + plugin.getConfiguracao().getMensagemErroVerAtributos()));
            return true;
        }

        String mensagem = plugin.getConfiguracao().getMensagemVerAtributos()
                .replace("%jogador%", target.getName())
                .replace("%danobase%", String.format("%.2f", playerData.getDanoBase()))
                .replace("%ampdano%", String.format("%.2f", playerData.getAmplificacaoDano()))
                .replace("%danototal%", String.format("%.2f", playerData.getDanoTotal()))
                .replace("%defesabase%", String.format("%.2f", playerData.getDefesaBase()))
                .replace("%ampdefesa%", String.format("%.2f", playerData.getAmplificacaoDefesa()))
                .replace("%defesatotal%", String.format("%.2f", playerData.getDefesaTotal()));

        sender.sendMessage(Mensagem.formatar(plugin.getConfiguracao().getPrefix() + mensagem));

        return true;
    }
}
