package txdev.txstatus.utils;

import txdev.txstatus.database.PlayerData;
import txdev.txstatus.runas.Runa;

public class CalcularStatus {
    public static void calcularAtributos(PlayerData playerData) {
        if (playerData == null) {
            throw new IllegalArgumentException("Dados do jogador não podem ser nulos.");
        }

        double danoBase = playerData.getDanoBase();
        double ampDano = playerData.getAmplificacaoDano();
        double defesaBase = playerData.getDefesaBase();
        double ampDefesa = playerData.getAmplificacaoDefesa();

        for (Runa runa : playerData.getRunas().values()) {
            switch (runa.getTipo()) {
                case DANO:
                    danoBase += runa.getValorAtributo();
                    break;
                case DEFESA:
                    defesaBase += runa.getValorAtributo();
                    break;
                case AMPLIFICACAO:
                    ampDano += runa.getValorAtributo();
                    ampDefesa += runa.getValorAtributo();
                    break;
            }
        }

        double danoTotal = danoBase + (danoBase * (ampDano / 100));
        double defesaTotal = defesaBase + (defesaBase * (ampDefesa / 100));

        playerData.setDanoTotal(danoTotal);
        playerData.setDefesaTotal(defesaTotal);
    }
}
