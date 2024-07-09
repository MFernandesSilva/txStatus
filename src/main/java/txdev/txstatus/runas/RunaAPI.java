package txdev.txstatus.runas;

import org.bukkit.entity.Player;
import txdev.txstatus.database.PlayerData;
import txdev.txstatus.txStatus;

import java.util.Map;

public class RunaAPI {

    public static void adicionarRuna(Player player, TipoRuna tipoRuna, int nivel, int subnivel) {
        PlayerData playerData = txStatus.getInstance().getPlayerData().get(player.getUniqueId());
        if (playerData != null) {
            Runa novaRuna = new Runa(tipoRuna, nivel, subnivel);
            playerData.adicionarRuna(novaRuna);
        }
    }

    public static void removerRuna(Player player, TipoRuna tipoRuna) {
        PlayerData playerData = txStatus.getInstance().getPlayerData().get(player.getUniqueId());
        if (playerData != null) {
            playerData.getRunas().remove(tipoRuna);
        }
    }

    public static void atualizarRuna(Player player, TipoRuna tipoRuna, int novoNivel, int novoSubnivel) {
        PlayerData playerData = txStatus.getInstance().getPlayerData().get(player.getUniqueId());
        if (playerData != null) {
            Runa runa = playerData.getRunas().get(tipoRuna);
            if (runa != null) {
                runa.setNivel(novoNivel);
                runa.setSubnivel(novoSubnivel);
            }
        }
    }

    public static boolean podeEvoluirRuna(Player player, TipoRuna tipoRuna) {
        PlayerData playerData = txStatus.getInstance().getPlayerData().get(player.getUniqueId());
        if (playerData != null) {
            Runa runa = playerData.getRunas().get(tipoRuna);
            if (runa != null) {
                int nivelMaximo = getNivelMaximo(runa.getNivel());
                int subnivelMaximo = getSubnivelMaximo(runa.getNivel());
                return runa.getNivel() < 5 && runa.getSubnivel() == subnivelMaximo;
            }
        }
        return false;
    }

    // Método para determinar o nível máximo da runa com base no nível atual (corrigido para Java 8)
    public static int getNivelMaximo(int nivelAtual) {
        switch (nivelAtual) {
            case 0:
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 3;
            case 4:
                return 4;
            default:
                return 5;
        }
    }

    // Método para determinar o subnível máximo da runa com base no nível atual (corrigido para Java 8)
    public static int getSubnivelMaximo(int nivelAtual) {
        switch (nivelAtual) {
            case 0:
                return 0;
            case 1:
                return 20;
            case 2:
                return 15;
            case 3:
                return 10;
            case 4:
                return 5;
            default:
                return 3;
        }
    }
}
