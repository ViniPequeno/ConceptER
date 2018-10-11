/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ScriptsSQL;

import Estaticos.PalavrasEstaticas;
import Telas.PanelPrincipal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import Estaticos.SGBDs;

/**
 *
 * @author User
 */
public class Conexao {

    private String url;
    private boolean ERRO = false;
    private String sql;
    private String sgbd;
    private String server;
    private String usuario;
    private String senha;
    private String banco;
    private String porta;
    private Connection conexao;
    private Statement statement;
    private PanelPrincipal pp;

    public Conexao(String server, String banco, String usuario, String senha,
            String porta, boolean criarBanco, int posicao, PanelPrincipal pp) {
        this.pp = pp;
        this.banco = banco;
        this.senha = senha;
        this.usuario = usuario;
        this.sgbd = SGBDs.getSGBDs().get(posicao);
        this.server = server;
        this.porta = porta;
        if (criarBanco) {
            this.url = "jdbc:" + this.sgbd + "://" + this.server + ":" + this.porta;
            criarBanco();
        }
        this.url = "jdbc:" + this.sgbd + "://" + this.server + ":" + this.porta + "/" + this.banco;
        criarTabelas();
    }

    private void criarBanco() {
        try {
            this.conexao = DriverManager.getConnection(url, usuario, senha);
            this.statement = this.conexao.createStatement();
            this.statement.execute(PalavrasEstaticas.CRIABANCO + " " + banco);
            this.statement.close();
            this.conexao.close();
        } catch (SQLException e) {
            ERRO = true;
            JOptionPane.showMessageDialog(null, "ERRO " + e.getMessage());
        }
    }

    private void criarTabelas() {
        try {
            Scripts scripts = new Scripts(pp);
            ArrayList<TabelaSQL> alt = scripts.gerar();
            boolean possui = false;
            boolean erro = false;
            for (TabelaSQL alt2 : alt) {
                for (AtributoSQL a : alt2.getAtributos()) {
                    if (a.isPk()) {
                        possui = true;
                    }
                }
                if (possui == false) {
                    JOptionPane.showMessageDialog(null,
                            "A tabela " + alt2.getNome() + " não possui chave primária");
                    erro = true;
                    break;
                }
            }
            if (erro==false) {
                this.conexao = DriverManager.getConnection(url, usuario, senha);
                this.statement = this.conexao.createStatement();
                boolean tem = false;
                for (TabelaSQL alt2 : alt) {
                    for (AtributoSQL asql : alt2.getAtributos()) {
                        if (asql.isFk()) {
                            tem = true;
                        }
                    }
                    if (tem == false) {
                        this.statement.executeUpdate(alt2.toString());
                    } else {
                        tem = false;
                    }
                }
                tem = false;
                for (TabelaSQL alt2 : alt) {
                    tem = false;
                    for (AtributoSQL asql : alt2.getAtributos()) {
                        if (asql.isFk()) {
                            tem = true;
                        }
                    }
                    if (tem == true) {
                        this.statement.executeUpdate(alt2.toString());
                    }
                }
                this.statement.close();
                this.conexao.close();
            }
        } catch (SQLException e) {
            ERRO = true;
            JOptionPane.showMessageDialog(null, "ERRO " + e.getMessage());
        }

    }

    public boolean isERRO() {
        return ERRO;
    }

    public void setERRO(boolean ERRO) {
        this.ERRO = ERRO;
    }

}
