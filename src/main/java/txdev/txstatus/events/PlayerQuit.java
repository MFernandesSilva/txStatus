package txdev.txstatus.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import txdev.txstatus.database.PlayerData;
import txdev.txstatus.txStatus;

public class PlayerQuit implements Listener {

    private final txStatus plugin;

    public PlayerQuit(txStatus plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        PlayerData playerData = plugin.getPlayerData().get(event.getPlayer().getUniqueId());
        if (playerData != null) {
            plugin.db().salvarDadosJogadorAsync(playerData);
            plugin.getPlayerData().remove(event.getPlayer().getUniqueId());
        }
    }
}
