/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.view.mxGraph;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Yan e Pedro
 */
public class Relacionamento implements Serializable {

    private String nome, id, nomeSQL;
    private boolean identificacao;
    private int pX, pY, altura, largura;
    private mxGraph g;
    private mxCell cell;
    private ArrayList<Atributo> atributos;
    private ArrayList<Ligacao> ligacoes;
    String caracteristicas = "";
    private boolean autoRelacionamento;

    public Relacionamento(String nome, int pX, int pY, int largura, int altura, mxGraph g, boolean autoRelacionamento) {
        this.nome = nome;
        this.pX = pX;
        this.nomeSQL=nome;
        this.pY = pY;
        this.altura = altura;
        this.largura = largura;
        this.g = g;
        this.id = null;
        this.identificacao = false;
        this.autoRelacionamento = autoRelacionamento;
        atributos = new ArrayList<>();
        ligacoes = new ArrayList<>();
    }

    public Relacionamento(mxCell cell, mxGraph g) {
        this.nome = (String) cell.getValue();
        this.nomeSQL = (String) cell.getNomeSQL();
        this.pX = (int) cell.getGeometry().getX();
        this.pY = (int) cell.getGeometry().getY();
        this.altura = (int) cell.getGeometry().getHeight();
        this.largura = (int) cell.getGeometry().getWidth();
        this.g = g;
        atributos = new ArrayList<>();
        ligacoes = new ArrayList<>();
        this.cell = cell;
        this.id = cell.getId();
        this.caracteristicas = cell.getStyle();
        this.identificacao = cell.isIdentificacao();
        this.autoRelacionamento = cell.isAutoRelacionamento();
    }

    public mxCell desenha() {
        
        this.g.getModel().beginUpdate();
        Object relacionamento = null;
        Object parent = this.g.getDefaultParent();
        caracteristicas = "fillColor=white;strokeColor=black;whiteSpace=wrap;shape=rhombus";

        try {
            relacionamento = this.g.insertVertex(parent,
                    this.id,
                    this.nome,
                    this.pX,
                    this.pY,
                    this.largura,
                    this.altura,
                    caracteristicas);
        } finally {
            this.cell = (mxCell) relacionamento;
            this.cell.setNomeSQL(nomeSQL);
            this.cell.setTipo("relacionamento");
            this.cell.setConnectable(false);
            this.g.getModel().endUpdate();
        }
        return this.cell;
    }

    public void atualiza() {
        this.g.getModel().beginUpdate();
        this.g.getModel().setValue(cell, nome);
        this.g.updateCellSize(cell);
        mxGeometry g = (mxGeometry) cell.getGeometry().clone();
        mxRectangle bounds = new mxRectangle(pX, pY, largura, altura);
        g.setWidth(cell.getGeometry().getWidth() + 10);
        g.setHeight(bounds.getHeight());
        cell.setGeometry(g);
        if (!identificacao) {
            caracteristicas = "fillColor=white;strokeColor=black;whiteSpace=wrap;shape=rhombus";
        } else {
            caracteristicas = "fillColor=white;strokeColor=black;whiteSpace=wrap;shape=doubleRhombus;";
        }
        this.g.getModel().setStyle(cell, caracteristicas);
        this.g.getModel().endUpdate();
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public boolean isIdentificacao() {
        return identificacao;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setIdentificacao(boolean identificacao) {
        this.identificacao = identificacao;
    }

    public ArrayList<Ligacao> getLigacoes() {
        return ligacoes;
    }

    public void setLigacoes(ArrayList<Ligacao> ligacoes) {
        this.ligacoes = ligacoes;
    }

    public String getNomeSQL() {
        return nomeSQL;
    }

    public void setNomeSQL(String nomeSQL) {
        this.nomeSQL = nomeSQL;
    }

    
    public boolean isAutoRelacionamento() {
        return autoRelacionamento;
    }

    public void setAutoRelacionamento(boolean autoRelacionamento) {
        this.autoRelacionamento = autoRelacionamento;
    }
    

    public String getCaracteristicas() {
        return caracteristicas;
    }

    public void setCaracteristicas(String caracteristicas) {
        this.caracteristicas = caracteristicas;
    }

    public int getpX() {
        return pX;
    }

    public void setpX(int pX) {
        this.pX = pX;
    }

    public int getpY() {
        return pY;
    }

    public void setpY(int pY) {
        this.pY = pY;
    }

    public int getAltura() {
        return altura;
    }

    public void setAltura(int altura) {
        this.altura = altura;
    }

    public int getLargura() {
        return largura;
    }

    public void setLargura(int largura) {
        this.largura = largura;
    }

    public mxGraph getG() {
        return g;
    }

    public void setG(mxGraph g) {
        this.g = g;
    }

    public mxCell getCell() {
        return cell;
    }

    public void setCell(mxCell cell) {
        this.cell = cell;
    }

    public ArrayList<Atributo> getAtributos() {
        return atributos;
    }

    public void setAtributos(ArrayList<Atributo> atributos) {
        this.atributos = atributos;
    }
    
}
