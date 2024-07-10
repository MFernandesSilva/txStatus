package txdev.txstatus.events;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import txdev.txapibukkit.api.Mensagem;
import txdev.txapibukkit.api.NBT;
import txdev.txstatus.database.PlayerData;
import txdev.txstatus.runas.Runa;
import txdev.txstatus.runas.RunaAPI;
import txdev.txstatus.runas.TipoRuna;
import txdev.txstatus.txStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UseRunas implements Listener {
    private final txStatus plugin;

    private static final String PREFIX = txStatus.getInstance().getConfiguracao().getPrefix();

    private final Map<UUID, Long> cooldowns = new HashMap<>();

    public UseRunas(txStatus plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onRunaUse(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        ItemStack itemUsado = player.getItemInHand();

        if (itemUsado != null && itemUsado.getType() != Material.AIR &&
                (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)) {

            String tipoRuna = NBT.getNBT(itemUsado, "tipo", String.class);

            if (tipoRuna != null) {
                if (tipoRuna.startsWith("runa")) {
                    usarRuna(player, itemUsado, tipoRuna);
                } else if (tipoRuna.startsWith("rompimento")) {
                    romperRuna(player, itemUsado, tipoRuna);
                }
            }
        }
    }

    // Método para usar uma runa
    private void usarRuna(Player player, ItemStack itemUsado, String tipoRuna) {
        String[] partes = tipoRuna.split("Lvl");
        TipoRuna tipo = TipoRuna.valueOf(partes[0].replace("runa", "").toUpperCase());
        int nivel = Integer.parseInt(partes[1]);

        if (temRompimento(player, tipo, nivel)) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "txrunasupgrade " + tipo + " " + player.getName());
            itemUsado.setAmount(itemUsado.getAmount() - 1); // Remove o item da runa
        } else {
            player.sendMessage(Mensagem.formatar(PREFIX + "&cVocê precisa do rompimento para usar essa runa."));
        }
    }

    // Método para romper uma runa
    private void romperRuna(Player player, ItemStack itemUsado, String tipoRuna) {
        String[] partes = tipoRuna.split("Lvl");
        TipoRuna tipo = TipoRuna.valueOf(partes[0].replace("rompimento", "").toUpperCase());
        int nivel = Integer.parseInt(partes[1]);

        if (podeRomperRuna(player, tipo, nivel)) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "txrunasromper " + tipo + " " + player.getName());
            itemUsado.setAmount(itemUsado.getAmount() - 1); // Remove o item de rompimento
        } else {
            player.sendMessage(Mensagem.formatar(PREFIX + "&cVocê não pode romper essa runa ainda."));
        }
    }

    // Método para verificar se o jogador tem o rompimento necessário
    private boolean temRompimento(Player player, TipoRuna tipo, int nivel) {
        for (ItemStack item : player.getInventory()) {
            if (item != null && item.getType() != Material.AIR) {
                String tipoRuna = NBT.getNBT(item, "tipo", String.class);
                if (tipoRuna != null && tipoRuna.equals("rompimento" + tipo + "Lvl" + nivel)) {
                    return true; // Jogador tem o rompimento
                }
            }
        }
        return false; // Jogador não tem o rompimento
    }

    // Método para verificar se o jogador pode romper a runa
    private boolean podeRomperRuna(Player player, TipoRuna tipo, int nivel) {
        PlayerData playerData = plugin.getPlayerData().get(player.getUniqueId());
        if (playerData == null) return false;

        Runa runa = playerData.getRunas().get(tipo);
        if (runa == null) return false;

        return runa.getNivel() == nivel - 1 && runa.getSubnivel() == RunaAPI.getSubnivelMaximo(runa.getNivel());
    }
}
