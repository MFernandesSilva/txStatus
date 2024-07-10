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
import txdev.txstatus.runas.Runa;
import txdev.txstatus.runas.TipoRuna;
import txdev.txstatus.txStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AtributosGUI implements Listener {

    private static final int SLOT_ESPADA = 38;
    private static final int SLOT_PEITORAL = 42;
    private static final int SLOT_RUNAS = 40;
    private static final String TITULO_GUI = "             &c&lATRIBUTOS";
    private static final String MSG_ERRO_ATRIBUTOS = "&cErro ao carregar seus atributos.";

    private final txStatus plugin;

    public AtributosGUI(txStatus plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public static void abrirGUI(Player player, txStatus plugin) {
        PlayerData playerData = plugin.getPlayerData().get(player.getUniqueId());
        if (playerData == null) {
            player.sendMessage(Mensagem.formatar(plugin.getConfiguracao().getPrefix() + MSG_ERRO_ATRIBUTOS));
            return;
        }

        Inventory gui = Inventario.criarInventario(54, Mensagem.formatar(TITULO_GUI));

        ItemStack espada = criarItemAtributo(playerData, Material.getMaterial(plugin.getConfig().getInt("id_gui.dano")), "&cDANO", "Dano Base", "Amplificação de Dano", TipoRuna.DANO);
        ItemStack peitoral = criarItemAtributo(playerData, Material.getMaterial(plugin.getConfig().getInt("id_gui.defesa")), "&aDEFESA", "Defesa Base", "Amplificação de Defesa", TipoRuna.DEFESA);
        ItemStack itemRunas = criarItemRunas(playerData, plugin);

        ItemStack vidro = new Item(Material.STAINED_GLASS_PANE, 1, (short) 15)
                .setName(" ")
                .setLore(Arrays.asList(""))
                .getIs();

        for (int slot : new int[]{0, 1, 7, 8, 9, 17, 36, 44, 45, 46, 52, 53}) {
            Inventario.adicionarItem(gui, vidro, slot);
        }

        Inventario.adicionarItem(gui, espada, SLOT_ESPADA);
        Inventario.adicionarItem(gui, peitoral, SLOT_PEITORAL);
        Inventario.adicionarItem(gui, itemRunas, SLOT_RUNAS);

        player.openInventory(gui);
    }

    private static ItemStack criarItemAtributo(PlayerData playerData, Material material, String nome, String nomeAtributoBase, String nomeAmplificacao, TipoRuna tipoRuna) {
        double valorBaseTotal = playerData.getDanoBase();
        double valorAmplificacaoTotal = playerData.getAmplificacaoDano();

        String corLore = "&c";
        if (tipoRuna == TipoRuna.DEFESA) {
            corLore = "&a";
            valorBaseTotal = playerData.getDefesaBase();
            valorAmplificacaoTotal = playerData.getAmplificacaoDefesa();
        }

        valorBaseTotal += playerData.getRunas().get(tipoRuna).getValorAtributo();
        valorAmplificacaoTotal += playerData.getRunas().get(TipoRuna.AMPLIFICACAO).getValorAtributo();

        double valorTotal = valorBaseTotal + (valorBaseTotal * valorAmplificacaoTotal / 100);

        return new Item(material, 1, (short) 0)
                .setName(Mensagem.formatar(nome))
                .setLore(Arrays.asList(
                        Mensagem.formatar("&7" + nomeAtributoBase + ": " + corLore + valorBaseTotal),
                        Mensagem.formatar("&7" + nomeAmplificacao + ": " + corLore + valorAmplificacaoTotal + "%"),
                        Mensagem.formatar("&7Total: " + corLore + valorTotal)
                ))
                .setUnbreakable(true)
                .getIs();
    }

    private static ItemStack criarItemRunas(PlayerData playerData, txStatus plugin) {
        List<String> lore = new ArrayList<>();
        for (TipoRuna tipoRuna : TipoRuna.values()) {
            Runa runa = playerData.getRunas().get(tipoRuna);
            if (runa.getNivel() > 0) {
                lore.add(Mensagem.formatar("&7" + tipoRuna + ": &f" + runa.getNivel() + "-" + runa.getSubnivel()));
                lore.add(Mensagem.formatar("&7+" + runa.getValorAtributo() + (tipoRuna == TipoRuna.AMPLIFICACAO ? "%" : "")));
                lore.add("");
            }
        }
        Material material = Material.getMaterial(plugin.getConfig().getInt("id_gui.runa"));
        return new Item(material, 1, (short) 0)
                .setName(Mensagem.formatar("&5Runas"))
                .setLore(lore)
                .setUnbreakable(true)
                .getIs();
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory().getTitle().equals(Mensagem.formatar(TITULO_GUI))) {
            event.setCancelled(true);
        }
    }
}
