package txdev.txstatus.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import txdev.txstatus.database.PlayerData;
import txdev.txstatus.txStatus;

public class Damage implements Listener {

    private final txStatus plugin;

    public Damage(txStatus plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        try {
            Entity damager = event.getDamager();
            Entity victim = event.getEntity();

            // Verifica se o dano é causado por uma entidade viva (jogador ou mob)
            if (damager instanceof LivingEntity && victim instanceof LivingEntity) {
                double danoBase = event.getDamage();
                double danoFinal = danoBase;

                // Se o atacante for um jogador, calcula o dano com base em seus atributos
                if (damager instanceof Player) {
                    Player playerDamager = (Player) damager;
                    PlayerData damagerData = plugin.getPlayerData().get(playerDamager.getUniqueId());
                    if (damagerData != null) {
                        danoFinal = damagerData.getDanoTotal(); // Usa o dano total do jogador
                    }
                }

                // Se a vítima for um jogador, aplica a defesa
                if (victim instanceof Player) {
                    PlayerData victimData = plugin.getPlayerData().get(victim.getUniqueId());
                    if (victimData != null) {
                        danoFinal -= victimData.getDefesaTotal();
                    }
                }

                danoFinal = Math.max(0, danoFinal); // Garante que o dano não seja negativo

                // Log de depuração (opcional)
                Bukkit.getLogger().info("Dano base: " + danoBase);
                Bukkit.getLogger().info("Dano final: " + danoFinal);

                event.setDamage(danoFinal);
            }
        } catch (Exception e) {
            Bukkit.getLogger().severe("Erro ao calcular o dano: " + e.getMessage());
        }
    }
}
