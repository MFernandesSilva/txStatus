package txdev.txstatus.runas;

import txdev.txstatus.config.Config;
import txdev.txstatus.txStatus;

public class Runa {
    private final TipoRuna tipo;
    private int nivel;
    private int subnivel;
    private double valorAtributo;

    public Runa(TipoRuna tipo, int nivel, int subnivel){
        this.tipo = tipo;
        this.nivel = nivel;
        this.subnivel = subnivel;
        this.valorAtributo = this.valorAtributo = calcularValorAtributo(txStatus.getInstance().getConfiguracao());
    }

    // getters

    public TipoRuna getTipo() {
        return tipo;
    }

    public int getNivel() {
        return nivel;
    }

    public int getSubnivel() {
        return subnivel;
    }

    public double getValorAtributo() {
        return valorAtributo;
    }

    // setters
    public void setNivel(int nivel) {
        this.nivel = nivel;
        this.valorAtributo = calcularValorAtributo(txStatus.getInstance().getConfiguracao());
    }

    public void setSubnivel(int subnivel) {
        this.subnivel = subnivel;
        this.valorAtributo = calcularValorAtributo(txStatus.getInstance().getConfiguracao());
    }

    private double calcularValorAtributo(Config config) {
        String path = "runas." + tipo.toString().toLowerCase() + ".lvl" + nivel + ".sublvl" + subnivel;
        return config.config.getDouble(path, 0.0);
    }
}
