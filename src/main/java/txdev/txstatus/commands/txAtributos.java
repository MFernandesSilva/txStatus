package txdev.txstatus.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import txdev.txapibukkit.api.Mensagem;
import txdev.txstatus.database.PlayerData;
import txdev.txstatus.txStatus;
import txdev.txstatus.utils.CalcularStatus;

public class txAtributos implements CommandExecutor {

    private final txStatus plugin;
    private static final String PREFIX = txStatus.getInstance().getConfiguracao().getPrefix();
    private static final String MSG_ATRIBUTO_INVALIDO = " &cAtributo inválido.";
    private static final String MSG_QUANTIDADE_INVALIDA = " &cA quantidade deve ser um número.";
    private static final String MSG_JOGADOR_NAO_ENCONTRADO = " &cJogador não encontrado.";
    private static final String MSG_JOGADOR_SEM_ATRIBUTOS = " &cJogador não encontrado ou sem atributos.";

    public txAtributos(txStatus plugin) {this.plugin = plugin;}

    @Override
    public boolean onCommand(CommandSender s, Command c, String label, String[] args) {
        if (!s.hasPermission("txstatus.admin")) {
            s.sendMessage(Mensagem.formatar(plugin.getConfiguracao().getPrefix() + plugin.getMensagensPadrao().np()));
            return true;
        }

        if (args.length < 4) {
            s.sendMessage(Mensagem.formatar(PREFIX + plugin.getConfiguracao().getMensagemUsoTxAtributos()));
            return true;
        }

        String operacao = args[0].toLowerCase();
        String atributo = args[1].toLowerCase();
        double quantidade;

        try {
            quantidade = Double.parseDouble(args[2]);
        } catch (NumberFormatException e) {
            s.sendMessage(Mensagem.formatar(PREFIX + MSG_QUANTIDADE_INVALIDA));
            return true;
        }

        Player target = Bukkit.getPlayer(args[3]);
        if (target == null) {
            s.sendMessage(Mensagem.formatar(PREFIX + MSG_JOGADOR_NAO_ENCONTRADO));
            return true;
        }

        PlayerData playerData = plugin.getPlayerData().get(target.getUniqueId());
        if (playerData == null) {
            s.sendMessage(Mensagem.formatar(PREFIX + MSG_JOGADOR_SEM_ATRIBUTOS));
            return true;
        }

        switch (operacao) {
            case "add":
                adicionarAtributo(s, playerData, atributo, quantidade);
                break;
            case "remove":
                removerAtributo(s, playerData, atributo, quantidade);
                break;
            case "set":
                definirAtributo(s, playerData, atributo, quantidade);
                break;
            default:
                s.sendMessage(Mensagem.formatar(PREFIX + plugin.getConfiguracao().getMensagemUsoTxAtributos()));
                return true;
        }

        plugin.db().salvarDadosJogadorAsync(playerData);
        s.sendMessage(Mensagem.formatar(PREFIX + plugin.getConfiguracao().getMensagemAtributosAlterados().replace("%jogador%", target.getName())));
        target.sendMessage(Mensagem.formatar(PREFIX + plugin.getConfiguracao().getMensagemSeusAtributosAlterados().replace("%staff%", s.getName())));

        return true;
    }

    private void adicionarAtributo(CommandSender s, PlayerData playerData, String atributo, double quantidade) {
        switch (atributo) {
            case "danobase":
                playerData.setDanoBase(playerData.getDanoBase() + quantidade);
                break;
            case "defesabase":
                playerData.setDefesaBase(playerData.getDefesaBase() + quantidade);
                break;
            case "ampdano":
                playerData.setAmplificacaoDano(playerData.getAmplificacaoDano() + quantidade);
                break;
            case "ampdefesa":
                playerData.setAmplificacaoDefesa(playerData.getAmplificacaoDefesa() + quantidade);
                break;
            default:
                s.sendMessage(Mensagem.formatar(PREFIX + MSG_ATRIBUTO_INVALIDO));
        }
        CalcularStatus.calcularAtributos(playerData);
    }

    private void removerAtributo(CommandSender s, PlayerData playerData, String atributo, double quantidade) {
        switch (atributo) {
            case "danobase":
                playerData.setDanoBase(Math.max(0, playerData.getDanoBase() - quantidade));
                break;
            case "defesabase":
                playerData.setDefesaBase(Math.max(0, playerData.getDefesaBase() - quantidade));
                break;
            case "ampdano":
                playerData.setAmplificacaoDano(Math.max(0, playerData.getAmplificacaoDano() - quantidade));
                break;
            case "ampdefesa":
                playerData.setAmplificacaoDefesa(Math.max(0, playerData.getAmplificacaoDefesa() - quantidade));
                break;
            default:
                s.sendMessage(Mensagem.formatar(PREFIX + MSG_ATRIBUTO_INVALIDO));
        }
        CalcularStatus.calcularAtributos(playerData);
    }

    private void definirAtributo(CommandSender s, PlayerData playerData, String atributo, double quantidade) {
        switch (atributo) {
            case "danobase":
                playerData.setDanoBase(quantidade);
                break;
            case "defesabase":
                playerData.setDefesaBase(quantidade);
                break;
            case "ampdano":
                playerData.setAmplificacaoDano(quantidade);
                break;
            case "ampdefesa":
                playerData.setAmplificacaoDefesa(quantidade);
                break;
            default:
                s.sendMessage(Mensagem.formatar(PREFIX + MSG_ATRIBUTO_INVALIDO));
        }
        CalcularStatus.calcularAtributos(playerData);
    }
}
