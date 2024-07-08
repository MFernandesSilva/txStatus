package txdev.txstatus.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import txdev.txapibukkit.api.Mensagem;
import txdev.txapidbc.NBT;
import txdev.txstatus.database.PlayerData;
import txdev.txstatus.txStatus;

public class Damage implements Listener {

    private final txStatus plugin;

    public Damage(txStatus plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        try {
            Entity damager = event.getDamager();
            Entity victim = event.getEntity();
            double danoFinal = event.getDamage(); // Dano base inicial
            boolean usarSistemaPadrao = false; // Flag para usar o sistema de dano padrão

            if (damager instanceof Player) {
                Player playerDamager = (Player) damager;
                PlayerData damagerData = plugin.getPlayerData().get(playerDamager.getUniqueId());

                if (damagerData != null) {
                    if (victim instanceof Player) {
                        // Atacante e vítima são players
                        PlayerData victimData = plugin.getPlayerData().get(victim.getUniqueId());

                        if (victimData != null) {
                            danoFinal = damagerData.getDanoTotal() - victimData.getDefesaTotal();
                            int novaVida = (int) Math.max(0, NBT.getHealth((Player)victim) - danoFinal);
                            NBT.setHealth((Player) victim, novaVida);

                            // Debug (remover em produção)
                            ((Player) victim).sendMessage(Mensagem.formatar("&7Sua vida foi ajustada para: " + novaVida));
                        }
                    } else {
                        // Atacante é player, vítima é entidade/NPC
                        danoFinal = damagerData.getDanoTotal();
                        usarSistemaPadrao = true; // Usar sistema de dano padrão
                    }
                }
            } else if (victim instanceof Player) {
                // Atacante é entidade/NPC, vítima é player
                PlayerData victimData = plugin.getPlayerData().get(victim.getUniqueId());

                if (victimData != null) {
                    danoFinal = event.getDamage() - victimData.getDefesaTotal(); // Dano base - defesa do player
                    int novaVida = (int) Math.max(0, NBT.getHealth((Player)victim) - danoFinal);
                    NBT.setHealth((Player) victim, novaVida);

                    // Debug (remover em produção)
                    ((Player) victim).sendMessage(Mensagem.formatar("&7Sua vida foi ajustada para: " + novaVida));
                }
            }

            // Aplicar dano (ou cancelar o evento se for usar o sistema NBT)
            if (usarSistemaPadrao) {
                event.setDamage(danoFinal);
            } else {
                event.setCancelled(true);
            }
        } catch (Exception e) {
            Bukkit.getLogger().severe("Erro ao calcular o dano: " + e.getMessage());
        }
    }
}
