package txdev.txstatus.utils;

import txdev.txstatus.database.PlayerData;

public class CalcularStatus {
    public static void calcularAtributos(PlayerData playerData) {
        if (playerData == null) {
            throw new IllegalArgumentException("Dados do jogador não podem ser nulos.");
        }

        double danoBase = playerData.getDanoBase();
        double ampDano = playerData.getAmplificacaoDano();
        double defesaBase = playerData.getDefesaBase();
        double ampDefesa = playerData.getAmplificacaoDefesa();

        double danoTotal = danoBase + (danoBase * (ampDano / 100));
        double defesaTotal = defesaBase + (defesaBase * (ampDefesa / 100));

        playerData.setDanoTotal(danoTotal);
        playerData.setDefesaTotal(defesaTotal);
    }
}
