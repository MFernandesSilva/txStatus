package txdev.txstatus.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import txdev.txapibukkit.api.Mensagem;
import txdev.txstatus.database.PlayerData;
import txdev.txstatus.runas.RunaAPI;
import txdev.txstatus.runas.TipoRuna;
import txdev.txstatus.txStatus;

public class txRunas implements CommandExecutor {

    private final txStatus plugin;

    public txRunas(txStatus plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("txstatus.admin")) {
            sender.sendMessage(Mensagem.formatar(plugin.getConfiguracao().getPrefix() + plugin.getMensagensPadrao().np()));
            return true;
        }

        if (args.length != 4) {
            sender.sendMessage(Mensagem.formatar(plugin.getConfiguracao().getPrefix() + "&cUso: /txrunas <tipo> <nivel> <subnivel> <jogador>"));
            return true;
        }

        TipoRuna tipoRuna;
        try {
            tipoRuna = TipoRuna.valueOf(args[0].toUpperCase());
        } catch (IllegalArgumentException e) {
            sender.sendMessage(Mensagem.formatar(plugin.getConfiguracao().getPrefix() + "&cTipo de runa inválido."));
            return true;
        }

        int nivel, subnivel;
        try {
            nivel = Integer.parseInt(args[1]);
            subnivel = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            sender.sendMessage(Mensagem.formatar(plugin.getConfiguracao().getPrefix() + "&cNível e subnível devem ser números."));
            return true;
        }

        if (nivel < 1 || nivel > 5 || subnivel < 1 || subnivel > RunaAPI.getSubnivelMaximo(nivel)) {
            sender.sendMessage(Mensagem.formatar(plugin.getConfiguracao().getPrefix() + "&cNível ou subnível inválido para esse tipo de runa."));
            return true;
        }

        Player target = Bukkit.getPlayer(args[3]);
        if (target == null) {
            sender.sendMessage(Mensagem.formatar(plugin.getConfiguracao().getPrefix() + "&cJogador não encontrado."));
            return true;
        }

        PlayerData playerData = plugin.getPlayerData().get(target.getUniqueId());
        if (playerData == null) {
            sender.sendMessage(Mensagem.formatar(plugin.getConfiguracao().getPrefix() + "&cJogador não encontrado ou sem atributos."));
            return true;
        }

        RunaAPI.adicionarRuna(target, tipoRuna, nivel, subnivel); // Adiciona ou atualiza a runa
        plugin.db().salvarDadosJogadorAsync(playerData);
        sender.sendMessage(Mensagem.formatar(plugin.getConfiguracao().getPrefix() + "&aRuna definida com sucesso para &e" + target.getName() + "&a."));

        return true;
    }
}
