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

            if (damager instanceof LivingEntity && victim instanceof LivingEntity) {
                double danoBase = event.getDamage();
                double danoFinal = danoBase;

                if (damager instanceof Player) {
                    Player playerDamager = (Player) damager;
                    PlayerData damagerData = plugin.getPlayerData().get(playerDamager.getUniqueId());
                    if (damagerData != null) {
                        danoFinal = damagerData.getDanoTotal();
                    }
                }

                if (victim instanceof Player) {
                    PlayerData victimData = plugin.getPlayerData().get(victim.getUniqueId());
                    if (victimData != null) {
                        danoFinal -= victimData.getDefesaTotal();
                    }
                }

                danoFinal = Math.max(0, danoFinal);

                event.setDamage(danoFinal);
            }
        } catch (Exception e) {
            Bukkit.getLogger().severe("Erro ao calcular o dano: " + e.getMessage());
        }
    }
}
