package txdev.txstatus.database;

import java.util.UUID;

public class PlayerData {

    private final UUID uuid;
    private String nick;
    private double danoBase;
    private double defesaBase;
    private double amplificacaoDano;
    private double amplificacaoDefesa;
    private double danoTotal;
    private double defesaTotal;

    // Construtor
    public PlayerData(UUID uuid, String nick, double danoBase, double defesaBase, double amplificacaoDano,
                      double amplificacaoDefesa, double danoTotal, double defesaTotal) {
        this.uuid = uuid;
        this.nick = nick;
        this.danoBase = danoBase;
        this.defesaBase = defesaBase;
        this.amplificacaoDano = amplificacaoDano;
        this.amplificacaoDefesa = amplificacaoDefesa;
        this.danoTotal = danoTotal;
        this.defesaTotal = defesaTotal;
    }

    // Getters
    public UUID getUuid() {
        return uuid;
    }

    public String getNick() {
        return nick;
    }

    public double getDanoBase() {
        return danoBase;
    }

    public double getDefesaBase() {
        return defesaBase;
    }

    public double getAmplificacaoDano() {
        return amplificacaoDano;
    }

    public double getAmplificacaoDefesa() {
        return amplificacaoDefesa;
    }

    public double getDanoTotal() {
        return danoTotal;
    }

    public double getDefesaTotal() {
        return defesaTotal;
    }

    // Setters
    public void setNick(String nick) {
        this.nick = nick;
    }

    public void setDanoBase(double danoBase) {
        this.danoBase = danoBase;
        atualizarDanoTotal();
    }

    public void setDefesaBase(double defesaBase) {
        this.defesaBase = defesaBase;
        atualizarDefesaTotal();
    }

    public void setAmplificacaoDano(double amplificacaoDano) {
        this.amplificacaoDano = amplificacaoDano;
        atualizarDanoTotal();
    }

    public void setAmplificacaoDefesa(double amplificacaoDefesa) {
        this.amplificacaoDefesa = amplificacaoDefesa;
        atualizarDefesaTotal();
    }

    public void setDanoTotal(double danoTotal) {
        this.danoTotal = danoTotal;
    }

    public void setDefesaTotal(double defesaTotal) {
        this.defesaTotal = defesaTotal;
    }

    // Métodos privados para atualizar os totais
    private void atualizarDanoTotal() {
        this.danoTotal = this.danoBase + (this.danoBase * (this.amplificacaoDano / 100));
    }

    private void atualizarDefesaTotal() {
        this.defesaTotal = this.defesaBase + (this.defesaBase * (this.amplificacaoDefesa / 100));
    }
}
