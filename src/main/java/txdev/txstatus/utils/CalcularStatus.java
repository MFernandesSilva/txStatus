package txdev.txstatus.utils;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import txdev.txstatus.database.PlayerData;
import txdev.txstatus.txStatus;

public class CalcularStatus {

    // Calcula o dano final que o atacante causará na vítima


    // Calcula e atualiza os atributos totais do jogador
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
