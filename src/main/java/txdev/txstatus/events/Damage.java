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
    public void onDamage(EntityDamageByEntityEvent e){
        try {
            Entity attacker = e.getDamager();
            Entity victim = e.getEntity();
            double danoFinal = e.getDamage();
            boolean usarEvento = false;

            if (attacker instanceof Player){
                Player playerAttacker = (Player) attacker;
                PlayerData attackerData = plugin.getPlayerData().get(playerAttacker.getUniqueId());

                if (attackerData != null){
                    if (victim instanceof Player){
                        Player playerVictim = (Player) victim;
                        PlayerData victimData = plugin.getPlayerData().get(playerVictim.getUniqueId());

                        if (victimData != null){
                            danoFinal = attackerData.getDanoTotal();

                            if (danoFinal > 0){
                                danoFinal -= victimData.getDefesaTotal();;
                            }

                            if (victimData.getDefesaTotal() >= attackerData.getDanoTotal()){
                                danoFinal = 0;
                            }

                            double maxVida = NBT.getMaxHealth(playerVictim);
                            int novaVida = (int) Math.min(maxVida, Math.max(0, NBT.getHealth(playerVictim) - danoFinal));

                            NBT.setHealth(playerVictim, novaVida);

                            playerVictim.sendMessage(Mensagem.formatar("&7Sua vida foi ajustada para: " + novaVida));

                            if (novaVida == 0) {
                                playerVictim.sendMessage("&7Você foi derrotado!");
                                playerVictim.isDead();
                            }
                        }
                    } else {
                        danoFinal = attackerData.getDanoTotal();
                        usarEvento = true;
                    }
                }
            } else if (victim instanceof Player) {
                Player playerVictim = (Player) victim;
                PlayerData victimData = plugin.getPlayerData().get(playerVictim.getUniqueId());

                if (victimData != null){
                    danoFinal = e.getDamage();

                    if (danoFinal > 0){
                        danoFinal -= victimData.getDefesaTotal();
                    }

                    if (victimData.getDefesaTotal() >= e.getDamage()){
                        danoFinal = 0;
                    }

                    double maxVida = NBT.getMaxHealth(playerVictim);
                    int novaVida = (int) Math.min(maxVida, Math.max(0, NBT.getHealth(playerVictim) - danoFinal));

                    NBT.setHealth(playerVictim, novaVida);

                    playerVictim.sendMessage(Mensagem.formatar("&7Sua vida foi ajustada para: " + novaVida));

                    if (novaVida == 0) {
                        playerVictim.sendMessage("&7Você foi derrotado!");
                        playerVictim.isDead();
                    }
                }
            }

            if (usarEvento){
                e.setDamage(danoFinal);
            } else {
                e.setCancelled(true);
            }
        } catch (Exception exception){
            Bukkit.getLogger().severe("Erro ao calcular o dano: " + exception.getMessage());
        }
    }
}
