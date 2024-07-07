package txdev.txstatus.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import txdev.txapibukkit.api.Item;
import txdev.txapibukkit.api.Inventario;
import txdev.txapibukkit.api.Mensagem;
import txdev.txstatus.database.PlayerData;
import txdev.txstatus.txStatus;

import java.util.Arrays;

public class AtributosGUI implements Listener {

    private final txStatus plugin;

    public AtributosGUI(txStatus plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin); // Registrar o listener da GUI
    }

    public static void abrirGUI(Player player, txStatus plugin) {
        PlayerData playerData = plugin.getPlayerData().get(player.getUniqueId());
        if (playerData == null) {
            player.sendMessage(Mensagem.formatar(plugin.getConfiguracao().getPrefix() + "&cErro ao carregar seus atributos."));
            return;
        }

        Inventory gui = Inventario.criarInventario(9, Mensagem.formatar("&cATRIBUTOS"));

        // Itens da GUI
        ItemStack espada = new Item(Material.DIAMOND_SWORD, 1, (short) 0)
                .setName(Mensagem.formatar("&cDANO"))
                .setLore(Arrays.asList(
                        Mensagem.formatar("&7Dano Base: &c" + playerData.getDanoBase()),
                        Mensagem.formatar("&7Amplificação de Dano: &c" + playerData.getAmplificacaoDano() + "%"),
                        Mensagem.formatar("&7Dano Total: &c" + playerData.getDanoTotal())
                ))
                .setUnbreakable(true)
                .getIs();

        ItemStack peitoral = new Item(Material.DIAMOND_CHESTPLATE, 1, (short) 0)
                .setName(Mensagem.formatar("&aDEFESA"))
                .setLore(Arrays.asList(
                        Mensagem.formatar("&7Defesa Base: &a" + playerData.getDefesaBase()),
                        Mensagem.formatar("&7Amplificação de Defesa: &a" + playerData.getAmplificacaoDefesa() + "%"),
                        Mensagem.formatar("&7Defesa Total: &a" + playerData.getDefesaTotal())
                ))
                .setUnbreakable(true)
                .getIs();

        // Adicionar os itens à GUI
        Inventario.adicionarItem(gui, espada, 3); // Slot 3 (centralizado)
        Inventario.adicionarItem(gui, peitoral, 5); // Slot 5 (centralizado)

        player.openInventory(gui);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory().getTitle().equals(Mensagem.formatar("&cATRIBUTOS"))) {
            event.setCancelled(true); // Impede que o jogador pegue os itens
        }
    }
}
