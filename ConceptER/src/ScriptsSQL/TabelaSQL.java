/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ScriptsSQL;

import Estaticos.Valores;
import Estaticos.PalavrasEstaticas;
import java.util.ArrayList;

/**
 *
 * @author Yan e Pedro
 */
public class TabelaSQL {

    private String nome;
    ArrayList<AtributoSQL> atributos;

    public TabelaSQL(String nome) {
        this.nome = nome;
        atributos = new ArrayList<>();
    }

    public ArrayList<AtributoSQL> getAtributos() {
        return atributos;
    }

    @Override
    public String toString() {
        boolean possui = false;
        String cria = "";
        String tabela = PalavrasEstaticas.CRIATABELA + " " + nome + PalavrasEstaticas.PARETESESINICIO + PalavrasEstaticas.PULALINHA;
        for (int i = 0; i < atributos.size(); i++) {
            cria = "";
            cria += atributos.get(i).getNome() + " " + atributos.get(i).getTipo();

            if (!atributos.get(i).isNulo()) {
                cria += " " + PalavrasEstaticas.NULO;
            } else {
                cria += " " + PalavrasEstaticas.NAONULO;
            }
            if (atributos.get(i).isAutoIncremento()) {
                cria += " " + PalavrasEstaticas.AUTOINCREMENTO;
            }
            if (atributos.get(i).isUnico()) {
                cria += " " + PalavrasEstaticas.UNICO;
            }
            tabela += cria + PalavrasEstaticas.VIRGULA + PalavrasEstaticas.PULALINHA;
        }
        for (AtributoSQL atributo : atributos) {
            if (atributo.isPk()) {
                possui = true;
                break;
            }
        }
        if (possui) {
            cria = PalavrasEstaticas.CHAVEPRIMARIA + PalavrasEstaticas.PARETESESINICIO;
            for (AtributoSQL atributo : atributos) {
                if (atributo.isPk()) {
                    cria += atributo.getNome() + PalavrasEstaticas.VIRGULA + " ";
                }
            }
            tabela += cria.substring(0, cria.length() - 2) + PalavrasEstaticas.PARENTESESFINAL;
        }
        possui = false;
        cria = "";

        for (AtributoSQL atributo : atributos) {
            if (atributo.isFk()) {
                possui = true;
                break;
            }
        }

        if (possui) {

            for (AtributoSQL atributo : atributos) {
                if (atributo.isFk()) {
                    cria += PalavrasEstaticas.VIRGULA + PalavrasEstaticas.PULALINHA + PalavrasEstaticas.CHAVEESTRANGEIRA
                            + PalavrasEstaticas.PARETESESINICIO+ atributo.getNome()
                            + PalavrasEstaticas.PARENTESESFINAL + " "
                            + PalavrasEstaticas.REFENCIA + " " +atributo.getTabelaFK()
                            + " " + PalavrasEstaticas.PARETESESINICIO + atributo.getNomeFK()
                            + PalavrasEstaticas.PARENTESESFINAL;
                }
            }
            tabela += cria;
        }
        return tabela + PalavrasEstaticas.PULALINHA + PalavrasEstaticas.PARENTESESFINAL
                + PalavrasEstaticas.PONTOEVIRGULA
                + PalavrasEstaticas.PULALINHA;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

}
