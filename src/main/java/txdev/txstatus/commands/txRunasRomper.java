package txdev.txstatus.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import txdev.txapibukkit.api.Mensagem;
import txdev.txstatus.database.PlayerData;
import txdev.txstatus.runas.Runa;
import txdev.txstatus.runas.RunaAPI;
import txdev.txstatus.runas.TipoRuna;
import txdev.txstatus.txStatus;

public class txRunasRomper implements CommandExecutor {

    private final txStatus plugin;
    private static final String PREFIX = txStatus.getInstance().getConfiguracao().getPrefix();

    public txRunasRomper(txStatus plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("txstatus.admin")) {
            sender.sendMessage(Mensagem.formatar(plugin.getConfiguracao().getPrefix() + plugin.getMensagensPadrao().np()));
            return true;
        }

        if (args.length != 2) {
            sender.sendMessage(Mensagem.formatar(plugin.getConfiguracao().getPrefix() + "&cUso: /txrunasromper <tipo> <jogador>"));
            return true;
        }

        TipoRuna tipoRuna;
        try {
            tipoRuna = TipoRuna.valueOf(args[0].toUpperCase());
        } catch (IllegalArgumentException e) {
            sender.sendMessage(Mensagem.formatar(plugin.getConfiguracao().getPrefix() + "&cTipo de runa inválido."));
            return true;
        }

        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            sender.sendMessage(Mensagem.formatar(plugin.getConfiguracao().getPrefix() + "&cJogador não encontrado."));
            return true;
        }

        PlayerData playerData = plugin.getPlayerData().get(target.getUniqueId());
        if (playerData == null) {
            sender.sendMessage(Mensagem.formatar(plugin.getConfiguracao().getPrefix() + "&cJogador não encontrado ou sem atributos."));
            return true;
        }

        Runa runa = playerData.getRunas().get(tipoRuna);
        if (runa == null) {
            sender.sendMessage(Mensagem.formatar(plugin.getConfiguracao().getPrefix() + "&cJogador não possui este tipo de runa."));
            return true;
        }

        if (!RunaAPI.podeEvoluirRuna(target, tipoRuna)) {
            sender.sendMessage(Mensagem.formatar(plugin.getConfiguracao().getPrefix() + "&cA runa não pode ser rompida ainda."));
            return true;
        }

        int novoNivel = runa.getNivel() + 1;
        runa.setNivel(novoNivel);
        runa.setSubnivel(1); // Resetar o subnível para 1 no novo nível

        plugin.db().salvarDadosJogadorAsync(playerData);
        sender.sendMessage(Mensagem.formatar(plugin.getConfiguracao().getPrefix() + "&aRuna rompida com sucesso para &e" + target.getName() + "&a."));
        target.sendMessage(Mensagem.formatar(plugin.getConfiguracao().getPrefix() + "&aSua runa de &e" + tipoRuna + " &afoi rompida para o nível " + novoNivel + "!"));

        return true;
    }
}
