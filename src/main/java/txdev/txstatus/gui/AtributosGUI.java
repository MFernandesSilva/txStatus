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

    // Constantes para slots e mensagens
    private static final int SLOT_ESPADA = 38;
    private static final int SLOT_PEITORAL = 42;
    private static final int SLOT_RUNAS = 40;
    private static final String TITULO_GUI = "             &c&lATRIBUTOS";
    private static final String MSG_ERRO_ATRIBUTOS = "&cErro ao carregar seus atributos.";

    private final txStatus plugin;

    public AtributosGUI(txStatus plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin); // Registrar o listener da GUI
    }

    public static void abrirGUI(Player player, txStatus plugin) {
        PlayerData playerData = plugin.getPlayerData().get(player.getUniqueId());
        if (playerData == null) {
            player.sendMessage(Mensagem.formatar(plugin.getConfiguracao().getPrefix() + MSG_ERRO_ATRIBUTOS));
            return;
        }

        Inventory gui = Inventario.criarInventario(54, Mensagem.formatar(TITULO_GUI));

        // Itens da GUI
        ItemStack espada = criarItemAtributo(playerData, Material.getMaterial(plugin.getConfig().getInt("id_gui.dano")), "&cDANO", "Dano Base", "Amplificação de Dano", TipoRuna.DANO);
        ItemStack peitoral = criarItemAtributo(playerData, Material.getMaterial(plugin.getConfig().getInt("id_gui.defesa")), "&aDEFESA", "Defesa Base", "Amplificação de Defesa", TipoRuna.DEFESA);
        ItemStack itemRunas = criarItemRunas(playerData, plugin);

        // Vidro para preencher os espaços vazios
        ItemStack vidro = new Item(Material.STAINED_GLASS_PANE, 1, (short) 15) // Vidro preto (cor 15)
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

    // Método auxiliar para criar itens de atributo (espada e peitoral)
    private static ItemStack criarItemAtributo(PlayerData playerData, Material material, String nome, String nomeAtributoBase, String nomeAmplificacao, TipoRuna tipoRuna) {
        // Calcula os valores totais do atributo com base nas runas
        double valorBaseTotal = playerData.getDanoBase(); // Valor base do atributo
        double valorAmplificacaoTotal = playerData.getAmplificacaoDano(); // Valor de amplificação do atributo

        // Define a cor da lore de acordo com o tipo de runa
        String corLore = "&c"; // Vermelho para dano
        if (tipoRuna == TipoRuna.DEFESA) {
            corLore = "&a"; // Verde para defesa
            valorBaseTotal = playerData.getDefesaBase();
            valorAmplificacaoTotal = playerData.getAmplificacaoDefesa();
        }

        valorBaseTotal += playerData.getRunas().get(tipoRuna).getValorAtributo();
        valorAmplificacaoTotal += playerData.getRunas().get(TipoRuna.AMPLIFICACAO).getValorAtributo();

        double valorTotal = valorBaseTotal + (valorBaseTotal * valorAmplificacaoTotal / 100); // Calcula o valor total

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

    // Método para criar o item de runas
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
        Material material = Material.getMaterial(plugin.getConfig().getInt("id_gui.runa")); // Exemplo de material para o item de runas
        return new Item(material, 1, (short) 0)
                .setName(Mensagem.formatar("&5Runas"))
                .setLore(lore)
                .setUnbreakable(true)
                .getIs();
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory().getTitle().equals(Mensagem.formatar(TITULO_GUI))) {
            event.setCancelled(true); // Impede que o jogador pegue os itens
        }
    }
}
