package txdev.txstatus.config;

import org.bukkit.configuration.file.FileConfiguration;
import txdev.txstatus.txStatus;

public class Config {

    private final txStatus plugin;
    private FileConfiguration config;

    public Config(txStatus plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
        carregarConfiguracoes();
    }

    public void carregarConfiguracoes() {
        plugin.saveDefaultConfig();
        config = plugin.getConfig();
    }

    public String getPrefix() {return config.getString("prefix", "&7[&ctxStatus&7] ");}

    public double getDanoBasePadrao() {return config.getDouble("atributos.dano_base_padrao", 1.0);}

    public double getDefesaBasePadrao() {return config.getDouble("atributos.defesa_base_padrao", 0.0);}

    public String getMensagemAtributos() {return config.getString("mensagens.atributos", " &aSeus atributos foram atualizados!");}

    public String getMensagemErroAtributos() {return config.getString("mensagens.erro_atributos", " &cErro ao atualizar seus atributos.");}

    public String getMensagemVerAtributos() {
        return config.getString("mensagens.ver_atributos",
                "&aAtributos de &e%jogador%&a:\n" +
                        "&7Dano Base: &c%danobase%\n" +
                        "&7Amplificação de Dano: &c%ampdano%&\n" +
                        "&7Dano Total: &c%danototal%\n" +
                        "&7Defesa Base: &a%defesabase%\n" +
                        "&7Amplificação de Defesa: &a%ampdefesa%&\n" +
                        "&7Defesa Total: &a%defesatotal%");
    }

    public String getMensagemErroVerAtributos() {
        return config.getString("mensagens.erro_ver_atributos", " &cJogador não encontrado ou sem atributos.");
    }

    public String getMensagemAtributosAlterados() {
        return config.getString("mensagens.atributos_alterados", " &7Você alterou os atributos de &e%jogador%&a.");
    }

    public String getMensagemSeusAtributosAlterados() {
        return config.getString("mensagens.seus_atributos_alterados", " &7Seus atributos foram alterados por &e%staff%&a.");
    }

    public String getMensagemErroAtributosAlterados() {
        return config.getString("mensagens.erro_atributos_alterados", " &cErro ao alterar os atributos do jogador.");
    }

    public String getMensagemUsoTxAtributos() {
        return config.getString("mensagens.uso_txatributos", " &cUso: /txatributos <add/remove/set> <atributo> <quantidade> <jogador>");
    }
}
