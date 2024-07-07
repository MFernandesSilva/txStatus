package txdev.txstatus.database;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import txdev.txstatus.txStatus;

import java.io.File;
import java.sql.*;
import java.util.UUID;

public class Database {

    private final txStatus plugin;
    private Connection conexao;

    public Database(txStatus plugin) {
        this.plugin = plugin;
    }

    public void conectar() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
            File arquivoDB = new File(plugin.getDataFolder(), "player_status.db");
            String url = "jdbc:sqlite:" + arquivoDB.getAbsolutePath();

            conexao = DriverManager.getConnection(url);
            Bukkit.getLogger().info("Conexão com o banco de dados SQLite estabelecida com sucesso!");

            criarTabela();
        } catch (ClassNotFoundException e) {
            Bukkit.getLogger().severe("Driver SQLite não encontrado: " + e.getMessage());
            throw new SQLException("Erro ao carregar o driver SQLite.", e);
        }
    }

    public void desconectar() {
        try {
            if (conexao != null && !conexao.isClosed()) {
                conexao.close();
                Bukkit.getLogger().info("Conexão com o banco de dados SQLite fechada.");
            }
        } catch (SQLException e) {
            Bukkit.getLogger().severe("Erro ao fechar a conexão com o banco de dados SQLite: " + e.getMessage());
        }
    }

    private void criarTabela() throws SQLException {
        try (Statement stmt = conexao.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS player_status (" +
                    "uuid VARCHAR(36) PRIMARY KEY," +
                    "nick VARCHAR(16) NOT NULL," +
                    "danobase DOUBLE," +
                    "defesabase DOUBLE," +
                    "ampdano DOUBLE," +
                    "ampdefesa DOUBLE," +
                    "danototal DOUBLE," +
                    "defesatotal DOUBLE" +
                    ")";
            stmt.executeUpdate(sql);
        }
    }

    public void salvarDadosJogador(PlayerData playerData) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            String sql = "INSERT OR REPLACE INTO player_status (uuid, nick, danobase, defesabase, ampdano, ampdefesa, danototal, defesatotal) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = conexao.prepareStatement(sql)) {
                pstmt.setString(1, playerData.getUuid().toString());
                pstmt.setString(2, playerData.getNick());
                pstmt.setDouble(3, playerData.getDanoBase());
                pstmt.setDouble(4, playerData.getDefesaBase());
                pstmt.setDouble(5, playerData.getAmplificacaoDano());
                pstmt.setDouble(6, playerData.getAmplificacaoDefesa());
                pstmt.setDouble(7, playerData.getDanoTotal());
                pstmt.setDouble(8, playerData.getDefesaTotal());
                pstmt.executeUpdate();
            } catch (SQLException e) {
                Bukkit.getLogger().severe("Erro ao salvar dados do jogador: " + e.getMessage());
            }
        });
    }

    public void carregarDadosJogadorAsync(Player player) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            PlayerData playerData = carregarDadosJogador(player.getUniqueId());
            if (playerData == null) {
                playerData = new PlayerData(
                        player.getUniqueId(),
                        player.getName(),
                        plugin.getConfiguracao().getDanoBasePadrao(),
                        plugin.getConfiguracao().getDefesaBasePadrao(),
                        0, 0, 0, 0
                );
                salvarDadosJogador(playerData);
            }

            final PlayerData finalPlayerData = playerData;
            Bukkit.getScheduler().runTask(plugin, () -> {
                plugin.getPlayerData().put(player.getUniqueId(), finalPlayerData);
            });
        });
    }

    public void salvarDadosJogadorAsync(PlayerData playerData) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            String sql = "INSERT OR REPLACE INTO player_status (uuid, nick, danobase, defesabase, ampdano, ampdefesa, danototal, defesatotal) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = conexao.prepareStatement(sql)) {
                pstmt.setString(1, playerData.getUuid().toString());
                pstmt.setString(2, playerData.getNick());
                pstmt.setDouble(3, playerData.getDanoBase());
                pstmt.setDouble(4, playerData.getDefesaBase());
                pstmt.setDouble(5, playerData.getAmplificacaoDano());
                pstmt.setDouble(6, playerData.getAmplificacaoDefesa());
                pstmt.setDouble(7, playerData.getDanoTotal());
                pstmt.setDouble(8, playerData.getDefesaTotal());
                pstmt.executeUpdate();
            } catch (SQLException e) {
                Bukkit.getLogger().severe("Erro ao salvar dados do jogador: " + e.getMessage());
            }
        });
    }

    public PlayerData carregarDadosJogador(UUID uuid) {
        String sql = "SELECT nick, danobase, defesabase, ampdano, ampdefesa, danototal, defesatotal FROM player_status WHERE uuid = ?";
        try (PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.setString(1, uuid.toString());
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String nick = rs.getString("nick");
                    double danoBase = rs.getDouble("danobase");
                    double defesaBase = rs.getDouble("defesabase");
                    double ampDano = rs.getDouble("ampdano");
                    double ampDefesa = rs.getDouble("ampdefesa");
                    double danoTotal = rs.getDouble("danototal");
                    double defesaTotal = rs.getDouble("defesatotal");
                    return new PlayerData(uuid, nick, danoBase, defesaBase, ampDano, ampDefesa, danoTotal, defesaTotal);
                }
            }
        } catch (SQLException e) {
            Bukkit.getLogger().severe("Erro ao carregar dados do jogador: " + e.getMessage());
        }
        return null;
    }
}
