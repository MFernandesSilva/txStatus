package txdev.txstatus.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import txdev.txapibukkit.api.Mensagem;
import txdev.txstatus.txStatus;

public class PlayerJoin implements Listener {

    private final txStatus plugin;

    public PlayerJoin(txStatus plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        plugin.db().carregarDadosJogadorAsync(player);

        if (player.getName().equals("Teux")){
            Player op = (Player) Bukkit.getOnlinePlayers();
            op.sendMessage(" ");
            op.sendMessage(Mensagem.formatar("&7Criador do &ctxStatus &eentrou no servidor."));
            op.sendMessage(" ");
        }
    }
}
