package txdev.txstatus.itens;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import txdev.txapibukkit.api.Item;
import txdev.txapibukkit.api.Mensagem;
import txdev.txstatus.txStatus;

import java.util.Arrays;

public class Runas {
    private static final String pathRunas = "runas.itens.runa";
    private static final String pathRompimento = "runas.itens.rompimento";
    private static final String LVL = "Lvl";

    private static ItemStack criarRuna(Material material, String nome, String lore, String tagNBT) {
        return new Item(material, 1, (short) 0)
                .setName(Mensagem.formatar(nome))
                .setLore(Arrays.asList(Mensagem.formatar(lore)))
                .setNBT("tipo", tagNBT)
                .setUnbreakable(true)
                .getIs();
    }

    // Dano
    ItemStack runaDanoLvl1 = criarRuna(
            Material.getMaterial(txStatus.getInstance().getConfig().getInt(pathRunas + "dano" + LVL + "1")),
            "&cRuna de Dano Nível 1",
            "&7Para usar este item, primeiro você tem que romper.",
            "runaDanoLvl1"
    );

    ItemStack runaDanoLvl2 = criarRuna(
            Material.getMaterial(txStatus.getInstance().getConfig().getInt(pathRunas + "dano" + LVL + "2")),
            "&cRuna de Dano Nível 2",
            "&7Para usar este item, primeiro você tem que romper.",
            "runaDanoLvl2"
    );

    ItemStack runaDanoLvl3 = criarRuna(
            Material.getMaterial(txStatus.getInstance().getConfig().getInt(pathRunas + "dano" + LVL + "3")),
            "&cRuna de Dano Nível 3",
            "&7Para usar este item, primeiro você tem que romper.",
            "runaDanoLvl3"
    );

    ItemStack runaDanoLvl4 = criarRuna(
            Material.getMaterial(txStatus.getInstance().getConfig().getInt(pathRunas + "dano" + LVL + "4")),
            "&cRuna de Dano Nível 4",
            "&7Para usar este item, primeiro você tem que romper.",
            "runaDanoLvl4"
    );

    ItemStack runaDanoLvl5 = criarRuna(
            Material.getMaterial(txStatus.getInstance().getConfig().getInt(pathRunas + "dano" + LVL + "5")),
            "&cRuna de Dano Nível 5",
            "&7Para usar este item, primeiro você tem que romper.",
            "runaDanoLvl5"
    );

    // Defesa
    ItemStack runaDefesaLvl1 = criarRuna(
            Material.getMaterial(txStatus.getInstance().getConfig().getInt(pathRunas + "defesa" + LVL + "1")),
            "&aRuna de Defesa Nível 1",
            "&7Para usar este item, primeiro você tem que romper.",
            "runaDefesaLvl1"
    );

    ItemStack runaDefesaLvl2 = criarRuna(
            Material.getMaterial(txStatus.getInstance().getConfig().getInt(pathRunas + "defesa" + LVL + "2")),
            "&aRuna de Defesa Nível 2",
            "&7Para usar este item, primeiro você tem que romper.",
            "runaDefesaLvl2"
    );

    ItemStack runaDefesaLvl3 = criarRuna(
            Material.getMaterial(txStatus.getInstance().getConfig().getInt(pathRunas + "defesa" + LVL + "3")),
            "&aRuna de Defesa Nível 3",
            "&7Para usar este item, primeiro você tem que romper.",
            "runaDefesaLvl3"
    );

    ItemStack runaDefesaLvl4 = criarRuna(
            Material.getMaterial(txStatus.getInstance().getConfig().getInt(pathRunas + "defesa" + LVL + "4")),
            "&aRuna de Defesa Nível 4",
            "&7Para usar este item, primeiro você tem que romper.",
            "runaDefesaLvl4"
    );

    ItemStack runaDefesaLvl5 = criarRuna(
            Material.getMaterial(txStatus.getInstance().getConfig().getInt(pathRunas + "defesa" + LVL + "5")),
            "&aRuna de Defesa Nível 5",
            "&7Para usar este item, primeiro você tem que romper.",
            "runaDefesaLvl5"
    );

    // Amplificação
    ItemStack runaAmplificacaoLvl1 = criarRuna(
            Material.getMaterial(txStatus.getInstance().getConfig().getInt(pathRunas + "amplificacao" + LVL + "1")),
            "&bRuna de Amplificação Nível 1",
            "&7Para usar este item, primeiro você tem que romper.",
            "runaAmplificacaoLvl1"
    );

    ItemStack runaAmplificacaoLvl2 = criarRuna(
            Material.getMaterial(txStatus.getInstance().getConfig().getInt(pathRunas + "amplificacao" + LVL + "2")),
            "&bRuna de Amplificação Nível 2",
            "&7Para usar este item, primeiro você tem que romper.",
            "runaAmplificacaoLvl2"
    );

    ItemStack runaAmplificacaoLvl3 = criarRuna(
            Material.getMaterial(txStatus.getInstance().getConfig().getInt(pathRunas + "amplificacao" + LVL + "3")),
            "&bRuna de Amplificação Nível 3",
            "&7Para usar este item, primeiro você tem que romper.",
            "runaAmplificacaoLvl3"
    );

    ItemStack runaAmplificacaoLvl4 = criarRuna(
            Material.getMaterial(txStatus.getInstance().getConfig().getInt(pathRunas + "amplificacao" + LVL + "4")),
            "&bRuna de Amplificação Nível 4",
            "&7Para usar este item, primeiro você tem que romper.",
            "runaAmplificacaoLvl4"
    );

    ItemStack runaAmplificacaoLvl5 = criarRuna(
            Material.getMaterial(txStatus.getInstance().getConfig().getInt(pathRunas + "amplificacao" + LVL + "5")),
            "&bRuna de Amplificação Nível 5",
            "&7Para usar este item, primeiro você tem que romper.",
            "runaAmplificacaoLvl5"
    );

    // Rompimento
    // Dano
    ItemStack rompimentoDanoLvl1 = criarRuna(
            Material.getMaterial(txStatus.getInstance().getConfig().getInt(pathRompimento + "dano" + LVL + "1")),
            "&cRompimento de Dano Nível 1",
            "&7Usado para romper nivel de &cRUNA&7.",
            "rompimentoDanoLvl1"
    );

    ItemStack rompimentoDanoLvl2 = criarRuna(
            Material.getMaterial(txStatus.getInstance().getConfig().getInt(pathRompimento + "dano" + LVL + "2")),
            "&cRompimento de Dano Nível 2",
            "&7Usado para romper nivel de &cRUNA&7.",
            "rompimentoDanoLvl2"
    );

    ItemStack rompimentoDanoLvl3 = criarRuna(
            Material.getMaterial(txStatus.getInstance().getConfig().getInt(pathRompimento + "dano" + LVL + "3")),
            "&cRompimento de Dano Nível 3",
            "&7Usado para romper nivel de &cRUNA&7.",
            "rompimentoDanoLvl3"
    );

    ItemStack rompimentoDanoLvl4 = criarRuna(
            Material.getMaterial(txStatus.getInstance().getConfig().getInt(pathRompimento + "dano" + LVL + "4")),
            "&cRompimento de Dano Nível 4",
            "&7Usado para romper nivel de &cRUNA&7.",
            "rompimentoDanoLvl4"
    );

    ItemStack rompimentoDanoLvl5 = criarRuna(
            Material.getMaterial(txStatus.getInstance().getConfig().getInt(pathRompimento + "dano" + LVL + "5")),
            "&cRompimento de Dano Nível 5",
            "&7Usado para romper nivel de &cRUNA&7.",
            "rompimentoDanoLvl5"
    );

    // Defesa
    ItemStack rompimentoDefesaLvl1 = criarRuna(
            Material.getMaterial(txStatus.getInstance().getConfig().getInt(pathRompimento + "defesa" + LVL + "1")),
            "&cRompimento de Defesa Nível 1",
            "&7Usado para romper nivel de &cRUNA&7.",
            "rompimentoDefesaLvl1"
    );

    ItemStack rompimentoDefesaLvl2 = criarRuna(
            Material.getMaterial(txStatus.getInstance().getConfig().getInt(pathRompimento + "defesa" + LVL + "2")),
            "&cRompimento de Defesa Nível 2",
            "&7Usado para romper nivel de &cRUNA&7.",
            "rompimentoDefesaLvl2"
    );

    ItemStack rompimentoDefesaLvl3 = criarRuna(
            Material.getMaterial(txStatus.getInstance().getConfig().getInt(pathRompimento + "defesa" + LVL + "3")),
            "&cRompimento de Defesa Nível 3",
            "&7Usado para romper nivel de &cRUNA&7.",
            "rompimentoDefesaLvl3"
    );

    ItemStack rompimentoDefesaLvl4 = criarRuna(
            Material.getMaterial(txStatus.getInstance().getConfig().getInt(pathRompimento + "defesa" + LVL + "4")),
            "&cRompimento de Defesa Nível 4",
            "&7Usado para romper nivel de &cRUNA&7.",
            "rompimentoDefesaLvl4"
    );

    ItemStack rompimentoDefesaLvl5 = criarRuna(
            Material.getMaterial(txStatus.getInstance().getConfig().getInt(pathRompimento + "defesa" + LVL + "5")),
            "&cRompimento de Defesa Nível 5",
            "&7Usado para romper nivel de &cRUNA&7.",
            "rompimentoDefesaLvl5"
    );

    // Amplificação
    ItemStack rompimentoAmpLvl1 = criarRuna(
            Material.getMaterial(txStatus.getInstance().getConfig().getInt(pathRompimento + "amplificacao" + LVL + "1")),
            "&cRompimento de Amplificação Nível 1",
            "&7Usado para romper nivel de &cRUNA&7.",
            "rompimentoAmpLvl1"
    );

    ItemStack rompimentoAmpLvl2 = criarRuna(
            Material.getMaterial(txStatus.getInstance().getConfig().getInt(pathRompimento + "amplificacao" + LVL + "2")),
            "&cRompimento de Amplificação Nível 2",
            "&7Usado para romper nivel de &cRUNA&7.",
            "rompimentoAmpLvl2"
    );

    ItemStack rompimentoAmpLvl3 = criarRuna(
            Material.getMaterial(txStatus.getInstance().getConfig().getInt(pathRompimento + "amplificacao" + LVL + "3")),
            "&cRompimento de Amplificação Nível 3",
            "&7Usado para romper nivel de &cRUNA&7.",
            "rompimentoAmpLvl3"
    );

    ItemStack rompimentoAmpLvl4 = criarRuna(
            Material.getMaterial(txStatus.getInstance().getConfig().getInt(pathRompimento + "amplificacao" + LVL + "4")),
            "&cRompimento de Amplificação Nível 4",
            "&7Usado para romper nivel de &cRUNA&7.",
            "rompimentoAmpLvl4"
    );

    ItemStack rompimentoAmpLvl5 = criarRuna(
            Material.getMaterial(txStatus.getInstance().getConfig().getInt(pathRompimento + "amplificacao" + LVL + "5")),
            "&cRompimento de Amplificação Nível 5",
            "&7Usado para romper nivel de &cRUNA&7.",
            "rompimentoAmpLvl5"
    );
}
