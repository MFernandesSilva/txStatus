package txdev.txstatus.database;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import txdev.txstatus.runas.Runa;
import txdev.txstatus.runas.TipoRuna;
import txdev.txstatus.txStatus;

import java.io.File;
import java.sql.*;
import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;

public class Database {

    private static final String CRIAR_TABELA_SQL = "CREATE TABLE IF NOT EXISTS player_status (" +
            "uuid VARCHAR(36) PRIMARY KEY," +
            "nick VARCHAR(16) NOT NULL," +
            "danobase DOUBLE," +
            "defesabase DOUBLE," +
            "ampdano DOUBLE," +
            "ampdefesa DOUBLE," +
            "danototal DOUBLE," +
            "defesatotal DOUBLE," +
            "runa_dano_nivel INTEGER DEFAULT 0," +
            "runa_dano_subnivel INTEGER DEFAULT 0," +
            "runa_defesa_nivel INTEGER DEFAULT 0," +
            "runa_defesa_subnivel INTEGER DEFAULT 0," +
            "runa_amplificacao_nivel INTEGER DEFAULT 0," +
            "runa_amplificacao_subnivel INTEGER DEFAULT 0" +
            ")";

    private static final String SALVAR_DADOS_SQL = "INSERT OR REPLACE INTO player_status (uuid, nick, danobase, defesabase, ampdano, ampdefesa, danototal, defesatotal, runa_dano_nivel, runa_dano_subnivel, runa_defesa_nivel, runa_defesa_subnivel, runa_amplificacao_nivel, runa_amplificacao_subnivel) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String CARREGAR_DADOS_SQL = "SELECT nick, danobase, defesabase, ampdano, ampdefesa, danototal, defesatotal, runa_dano_nivel, runa_dano_subnivel, runa_defesa_nivel, runa_defesa_subnivel, runa_amplificacao_nivel, runa_amplificacao_subnivel FROM player_status WHERE uuid = ?";

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
            stmt.executeUpdate(CRIAR_TABELA_SQL);
        }
    }

    public void salvarDadosJogador(PlayerData playerData) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try (PreparedStatement pstmt = conexao.prepareStatement(SALVAR_DADOS_SQL)) {
                pstmt.setString(1, playerData.getUuid().toString());
                pstmt.setString(2, playerData.getNick());
                pstmt.setDouble(3, playerData.getDanoBase());
                pstmt.setDouble(4, playerData.getDefesaBase());
                pstmt.setDouble(5, playerData.getAmplificacaoDano());
                pstmt.setDouble(6, playerData.getAmplificacaoDefesa());
                pstmt.setDouble(7, playerData.getDanoTotal());
                pstmt.setDouble(8, playerData.getDefesaTotal());

                int index = 9;
                for (Runa runa : playerData.getRunas().values()) {
                    pstmt.setInt(index++, runa.getNivel());
                    pstmt.setInt(index++, runa.getSubnivel());
                }

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
                Map<TipoRuna, Runa> runas = new EnumMap<>(TipoRuna.class);
                for (TipoRuna tipoRuna : TipoRuna.values()) {
                    runas.put(tipoRuna, new Runa(tipoRuna, 0, 0));
                }

                playerData = new PlayerData(
                        player.getUniqueId(),
                        player.getName(),
                        plugin.getConfiguracao().getDanoBasePadrao(),
                        plugin.getConfiguracao().getDefesaBasePadrao(),
                        0, 0, 0, 0,
                        runas
                );
                salvarDadosJogador(playerData);
            }

            Map<TipoRuna, Runa> runas = new EnumMap<>(TipoRuna.class);
            try (PreparedStatement pstmt = conexao.prepareStatement(CARREGAR_DADOS_SQL)) {
                pstmt.setString(1, player.getUniqueId().toString());
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        for (TipoRuna tipo : TipoRuna.values()) {
                            int nivel = rs.getInt("runa_" + tipo.toString().toLowerCase() + "_nivel");
                            int subnivel = rs.getInt("runa_" + tipo.toString().toLowerCase() + "_subnivel");
                            runas.put(tipo, new Runa(tipo, nivel, subnivel));
                        }
                    }
                }
            } catch (SQLException e) {
                Bukkit.getLogger().severe("Erro ao carregar runas do jogador: " + e.getMessage());
            }

            for (TipoRuna tipo : TipoRuna.values()) {
                runas.put(tipo, runas.getOrDefault(tipo, new Runa(tipo, 0, 0)));
            }
            playerData.setRunas(runas);

            final PlayerData finalPlayerData = playerData;
            Bukkit.getScheduler().runTask(plugin, () -> {
                plugin.getPlayerData().put(player.getUniqueId(), finalPlayerData);
            });
        });
    }


    private PlayerData carregarDadosJogador(UUID uuid) {
        try (PreparedStatement pstmt = conexao.prepareStatement(CARREGAR_DADOS_SQL)) {
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

                    Map<TipoRuna, Runa> runas = new EnumMap<>(TipoRuna.class);
                    for (TipoRuna tipo : TipoRuna.values()) {
                        int nivel = rs.getInt("runa_" + tipo.toString().toLowerCase() + "_nivel");
                        int subnivel = rs.getInt("runa_" + tipo.toString().toLowerCase() + "_subnivel");
                        runas.put(tipo, new Runa(tipo, nivel, subnivel));
                    }

                    return new PlayerData(uuid, nick, danoBase, defesaBase, ampDano, ampDefesa, danoTotal, defesaTotal, runas);
                }
            }
        } catch (SQLException e) {
            Bukkit.getLogger().severe("Erro ao carregar dados do jogador: " + e.getMessage());
        }
        return null;
    }

    public void salvarDadosJogadorAsync(PlayerData playerData) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> salvarDadosJogador(playerData));
    }
}
