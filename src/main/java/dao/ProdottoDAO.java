package dao;

import model.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.*;

import util.SqlIdentifierValidator;

public class ProdottoDAO {
    
	//Variabile private Tabella Attiva
	private String tabellaAttiva;

    //Metodi Gestione Prodotto
    public boolean inserisciProdotto(Prodotto p) {
        if (tabellaAttiva == null) return false;
        try {
            Connection conn = DBConnection.getConnection();
            String sql = "INSERT INTO " + SqlIdentifierValidator.quote(tabellaAttiva) +
                         " (nome, categoria, taglia, tipologia, colori, quantita, prezzoAcquisto, prezzoVendita) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, p.getNome());
            ps.setString(2, p.getCategoria().name());
            ps.setString(3, p.getTaglia() != null ? p.getTaglia().name() : null);
            ps.setString(4, p.getTipologia() != null ? p.getTipologia().name() : null);
            ps.setString(5, String.join(",", p.getColori().stream().map(Enum::name).toList()));
            ps.setInt(6, p.getQuantita());
            ps.setDouble(7, p.getPrezzoAcquisto());
            ps.setDouble(8, p.getPrezzoVendita());
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Prodotto fromResultSet(ResultSet rs) throws SQLException {
        Prodotto p = new Prodotto();
        p.setId(rs.getInt("id"));
        p.setNome(rs.getString("nome"));
        p.setCategoria(Categoria.valueOf(rs.getString("categoria")));
        String tagliaStr = rs.getString("taglia");
        p.setTaglia(tagliaStr != null ? Taglia.valueOf(tagliaStr) : null);
        String tipoStr = rs.getString("tipologia");
        p.setTipologia(tipoStr != null ? Tipologia.valueOf(tipoStr) : null);
        String coloriStr = rs.getString("colori");
        Set<Colore> colori = new HashSet<>();
        if (coloriStr != null && !coloriStr.isEmpty()) {
            for (String c : coloriStr.split(",")) {
                colori.add(Colore.valueOf(c));
            }
        }
        p.setColori(colori);
        p.setQuantita(rs.getInt("quantita"));
        p.setPrezzoAcquisto(rs.getDouble("prezzoAcquisto"));
        p.setPrezzoVendita(rs.getDouble("prezzoVendita"));
        return p;
    }

    public boolean aggiornaProdotto(Prodotto p) {
        if (tabellaAttiva == null) return false;
        String query = "UPDATE " + SqlIdentifierValidator.quote(tabellaAttiva)
            + " SET nome = ?, categoria = ?, taglia = ?, tipologia = ?, colori = ?, quantita = ?, prezzoAcquisto = ?, prezzoVendita = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, p.getNome());
            stmt.setString(2, p.getCategoria().name());
            stmt.setString(3, p.getTaglia() != null ? p.getTaglia().name() : null);
            stmt.setString(4, p.getTipologia() != null ? p.getTipologia().name() : null);
            stmt.setString(5, String.join(",", p.getColori().stream().map(Enum::name).toList()));
            stmt.setInt(6, p.getQuantita());
            stmt.setDouble(7, p.getPrezzoAcquisto());
            stmt.setDouble(8, p.getPrezzoVendita());
            stmt.setInt(9, p.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean eliminaProdotto(int id) {
        if (tabellaAttiva == null) return false;
        String query = "DELETE FROM " + SqlIdentifierValidator.quote(tabellaAttiva)
            + " WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    //Metodi Import/Export CSV
    public boolean importaDaCSV(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String riga;
            while ((riga = reader.readLine()) != null) {
                String[] campi = riga.split(";");
                if (campi.length < 8) continue;

                Set<Colore> colori = new HashSet<>();
                if (!campi[4].isEmpty()) {
                    for (String col : campi[4].split(",")) {
                        colori.add(Colore.valueOf(col));
                    }
                }

                Prodotto p = new Prodotto(0, campi[0],
                        Categoria.valueOf(campi[1]),
                        campi[2].isEmpty() ? null : Taglia.valueOf(campi[2]),
                        campi[3].isEmpty() ? null : Tipologia.valueOf(campi[3]),
                        colori,
                        Integer.parseInt(campi[5]),
                        Double.parseDouble(campi[6]),
                        Double.parseDouble(campi[7])
                );
                inserisciProdotto(p);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean esportaCSV(File file) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            List<Prodotto> lista = getListaProdotti();
            for (Prodotto p : lista) {
                writer.write(String.join(";",
                    p.getNome(),
                    p.getCategoria().name(),
                    p.getTaglia() != null ? p.getTaglia().name() : "",
                    p.getTipologia() != null ? p.getTipologia().name() : "",
                    String.join(",", p.getColori().stream().map(Enum::name).toList()),
                    String.valueOf(p.getQuantita()),
                    String.valueOf(p.getPrezzoAcquisto()),
                    String.valueOf(p.getPrezzoVendita())
                ));
                writer.newLine();
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    //Getters Prodotti
    public List<Prodotto> getListaProdotti() {
        List<Prodotto> lista = new ArrayList<>();
        if (tabellaAttiva == null) return lista;
        try {
            Connection conn = DBConnection.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(
                "SELECT * FROM " + SqlIdentifierValidator.quote(tabellaAttiva)
            );
            while (rs.next()) {
                Prodotto p = fromResultSet(rs);
                lista.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }
    
    public Prodotto getProdottoById(int id) {
        if (tabellaAttiva == null) return null;
        String query = "SELECT * FROM " + SqlIdentifierValidator.quote(tabellaAttiva)
            + " WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return fromResultSet(rs); 
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    //Getter e Setter Tabella Attiva
    public String getTabellaAttiva() {
        return tabellaAttiva; }
    public void setTabellaAttiva(String tabella) {
        if (tabella != null) {
            SqlIdentifierValidator.requireValid(tabella);
        }
        this.tabellaAttiva = tabella;
    }

}