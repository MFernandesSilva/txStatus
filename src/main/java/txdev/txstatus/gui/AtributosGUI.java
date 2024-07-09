package txdev.txstatus.gui;

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
import txdev.txstatus.runas.Runa;
import txdev.txstatus.runas.TipoRuna;
import txdev.txstatus.txStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AtributosGUI implements Listener {

    private final txStatus plugin;

    public AtributosGUI(txStatus plugin) {
        this.plugin = plugin;
    }

    public static void abrirGUI(Player player, txStatus plugin) {
        PlayerData playerData = plugin.getPlayerData().get(player.getUniqueId());
        if (playerData == null) {
            player.sendMessage(Mensagem.formatar(plugin.getConfiguracao().getPrefix() + "&cErro ao carregar seus atributos."));
            return;
        }

        Inventory gui = Inventario.criarInventario(54, Mensagem.formatar("             &c&lATRIBUTOS"));

        // Itens da GUI
        ItemStack espada = new Item(Material.getMaterial(plugin.getConfig().getInt("id_gui.dano")), 1, (short) 0)
                .setName(Mensagem.formatar("&cDANO"))
                .setLore(Arrays.asList(
                        Mensagem.formatar("&7Dano Base: &c" + playerData.getDanoBase()),
                        Mensagem.formatar("&7Amplificação de Dano: &c" + playerData.getAmplificacaoDano() + "%"),
                        Mensagem.formatar("&7Dano Total: &c" + playerData.getDanoTotal())
                ))
                .setUnbreakable(true)
                .getIs();

        ItemStack peitoral = new Item(Material.getMaterial(plugin.getConfig().getInt("id_gui.defesa")), 1, (short) 0)
                .setName(Mensagem.formatar("&aDEFESA"))
                .setLore(Arrays.asList(
                        Mensagem.formatar("&7Defesa Base: &a" + playerData.getDefesaBase()),
                        Mensagem.formatar("&7Amplificação de Defesa: &a" + playerData.getAmplificacaoDefesa() + "%"),
                        Mensagem.formatar("&7Defesa Total: &a" + playerData.getDefesaTotal())
                ))
                .setUnbreakable(true)
                .getIs();

        ItemStack video = new Item(Material.STAINED_GLASS_PANE, 1, (short) 14)
                .setName(" ")
                .setLore(Arrays.asList(""))
                .getIs();

        ItemStack itemRunas = criarItemRunas(playerData, plugin);

        for (int slot : new int[]{0, 1, 7, 8, 9, 17, 36, 44, 45, 46, 52, 53}) {
            Inventario.adicionarItem(gui, video, slot);
        }
        Inventario.adicionarItem(gui, espada, 38);
        Inventario.adicionarItem(gui, peitoral, 42);
        Inventario.adicionarItem(gui, itemRunas, 40);

        player.openInventory(gui);
    }

    private static ItemStack criarItemRunas(PlayerData playerData, txStatus plugin) {
        List<String> lore = new ArrayList<>();
        for (TipoRuna tipoRuna : TipoRuna.values()) {
            Runa runa = playerData.getRunas().get(tipoRuna);
            if (runa.getNivel() > 0) { // Exibir apenas se a runa tiver nível maior que 0
                lore.add(Mensagem.formatar("&7" + tipoRuna + ": &f" + runa.getNivel() + "-" + runa.getSubnivel()));
                lore.add(Mensagem.formatar("&7+" + runa.getValorAtributo() + (tipoRuna == TipoRuna.AMPLIFICACAO ? "%" : ""))); // Exibir o valor com % para amplificação
                lore.add(""); // Linha em branco para separar
            }
        }
        Material material = Material.ENCHANTED_BOOK; // Exemplo de material para o item de runas
        return new Item(material, 1, (short) 0)
                .setName(Mensagem.formatar("&5Runas"))
                .setLore(lore)
                .setUnbreakable(true)
                .getIs();
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory().getTitle().equals(Mensagem.formatar("             &c&lATRIBUTOS"))) {
            event.setCancelled(true);
        }
    }
}
