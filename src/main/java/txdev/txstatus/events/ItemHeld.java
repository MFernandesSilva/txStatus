package txdev.txstatus.events;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;

import txdev.txapibukkit.api.Mensagem;
import txdev.txapibukkit.api.NBT;
import txdev.txstatus.database.PlayerData;
import txdev.txstatus.txStatus;
import txdev.txstatus.utils.CalcularStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ItemHeld implements Listener {

    private final txStatus plugin;
    private final Map<UUID, PlayerData> originalAttributes = new HashMap<>();

    public ItemHeld(txStatus plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onItemHeld(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();

        ItemStack newItem = player.getInventory().getItem(event.getNewSlot());
        ItemStack previousItem = player.getInventory().getItem(event.getPreviousSlot());

        PlayerData playerData = plugin.getPlayerData().get(playerUUID);
        if (playerData == null) return;


        if (newItem != null && newItem.getType() == Material.DIAMOND_SWORD && isEquipamentoPersonalizado(newItem)) {
            handleEquipmentChange(player, playerUUID, playerData, newItem, previousItem);
        } else if (previousItem != null && previousItem.getType() == Material.DIAMOND_SWORD && isEquipamentoPersonalizado(previousItem)) {
            if (originalAttributes.containsKey(playerUUID)) {
                restaurarAtributosOriginais(playerUUID, playerData);
            }
        }
    }

    private boolean isEquipamentoPersonalizado(ItemStack item) {
        return NBT.hasNBTKey(item, "danoBase") && NBT.hasNBTKey(item, "ampDano") &&
                NBT.hasNBTKey(item, "defesaBase") && NBT.hasNBTKey(item, "ampDefesa");
    }

    private void aplicarAtributosItem(PlayerData playerData, double danoBaseItem, double ampDanoItem, double defesaBaseItem, double ampDefesaItem) {
        playerData.setDanoBase(playerData.getDanoBase() + danoBaseItem);
        playerData.setAmplificacaoDano(playerData.getAmplificacaoDano() + ampDanoItem);
        playerData.setDefesaBase(playerData.getDefesaBase() + defesaBaseItem);
        playerData.setAmplificacaoDefesa(playerData.getAmplificacaoDefesa() + ampDefesaItem);
    }

    private void restaurarAtributosOriginais(UUID playerUUID, PlayerData playerData) {
        PlayerData originalData = originalAttributes.remove(playerUUID);
        playerData.setDanoBase(originalData.getDanoBase());
        playerData.setAmplificacaoDano(originalData.getAmplificacaoDano());
        playerData.setDefesaBase(originalData.getDefesaBase());
        playerData.setAmplificacaoDefesa(originalData.getAmplificacaoDefesa());
        CalcularStatus.calcularAtributos(playerData);
    }

    private void handleEquipmentChange(Player player, UUID playerUUID, PlayerData playerData, ItemStack newItem, ItemStack previousItem){
        try {
            double danoBaseItem = NBT.getNBT(newItem, "danoBase", Double.class);
            double ampDanoItem = NBT.getNBT(newItem, "ampDano", Double.class);
            double defesaBaseItem = NBT.getNBT(newItem, "defesaBase", Double.class);
            double ampDefesaItem = NBT.getNBT(newItem, "ampDefesa", Double.class);


            if (!originalAttributes.containsKey(playerUUID)) {
                originalAttributes.put(playerUUID, playerData.clone());
            } else {
                PlayerData originalData = originalAttributes.get(playerUUID);
                playerData.setDanoBase(originalData.getDanoBase());
                playerData.setAmplificacaoDano(originalData.getAmplificacaoDano());
                playerData.setDefesaBase(originalData.getDefesaBase());
                playerData.setAmplificacaoDefesa(originalData.getAmplificacaoDefesa());
            }

            aplicarAtributosItem(playerData, danoBaseItem, ampDanoItem, defesaBaseItem, ampDefesaItem);
        } catch (NumberFormatException e) {
            player.sendMessage(Mensagem.formatar(plugin.getConfiguracao().getPrefix() + "&cEste item não é um equipamento válido."));
        }
    }
}
