package txdev.txstatus;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import txdev.txapibukkit.api.DefaultMessages;
import txdev.txapibukkit.api.Mensagem;
import txdev.txstatus.commands.*;
import txdev.txstatus.config.Config;
import txdev.txstatus.database.Database;
import txdev.txstatus.database.PlayerData;
import txdev.txstatus.events.*;
import txdev.txstatus.gui.AtributosGUI;
import txdev.txstatus.utils.CalcularStatus;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class txStatus extends JavaPlugin {

    private Map<UUID, PlayerData> playerData = new HashMap<>();

    private static txStatus instance;
    private final Config config;
    private final Database database;

    public txStatus() {
        this.config = new Config(this);
        this.database = new Database(this);
    }

    @Override
    public void onEnable() {
        instance = this;

        config.carregarConfiguracoes();

        try {
            database.conectar();
        } catch (SQLException e) {
            Bukkit.getLogger().severe("Erro ao conectar ao banco de dados: " + e.getMessage());
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        registerCommands();
        registerEvents();

        Bukkit.getConsoleSender().sendMessage(Mensagem.formatar("&e------ txStatus ------"));
        Bukkit.getConsoleSender().sendMessage(Mensagem.formatar("&eHabilitado com sucesso!"));
        Bukkit.getConsoleSender().sendMessage(Mensagem.formatar("&e------ txStatus ------"));

        Bukkit.getScheduler().runTaskTimer(this, this::atualizarAtributos, 20L, 20L);
    }

    @Override
    public void onDisable() {
        database.desconectar();

        Bukkit.getConsoleSender().sendMessage(Mensagem.formatar("&e------ txStatus ------"));
        Bukkit.getConsoleSender().sendMessage(Mensagem.formatar("&eDesabilitado com sucesso!"));
        Bukkit.getConsoleSender().sendMessage(Mensagem.formatar("&e------ txStatus ------"));
    }

    private void registerCommands(){
        getCommand("atributos").setExecutor(new Atributos(this));
        getCommand("txatributos").setExecutor(new txAtributos(this));
        getCommand("veratributos").setExecutor(new VerAtributos(this));
        getCommand("txequipamento").setExecutor(new txEquipamento(this));
        getCommand("txrunas").setExecutor(new txRunas(this));
        getCommand("txrunasupgrade").setExecutor(new txRunasUpgrade(this));
        getCommand("txrunasromper").setExecutor(new txRunasRomper(this));
    }

    private void registerEvents(){
        Bukkit.getPluginManager().registerEvents(new Damage(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerJoin(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerQuit(this), this);
        Bukkit.getPluginManager().registerEvents(new AtributosGUI(this), this);
        Bukkit.getPluginManager().registerEvents(new ItemHeld(this), this);
    }

    public static txStatus getInstance() {return instance;}

    public Config getConfiguracao() {return config;}

    public Database db() {return database;}

    public Map<UUID, PlayerData> getPlayerData() {return playerData;}

    public DefaultMessages getMensagensPadrao() {return new DefaultMessages();}

    private void atualizarAtributos() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerData playerData = getPlayerData().get(player.getUniqueId());
            if (playerData != null) {
                CalcularStatus.calcularAtributos(playerData);
            }
        }
    }

}
