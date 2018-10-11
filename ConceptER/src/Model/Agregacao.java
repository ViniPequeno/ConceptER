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
import java.util.ArrayList;

/**
 *
 * @author Yan e Pedro
 */
public class Agregacao {

    private String nome, id;
    private int pX, pY, altura, largura;
    private mxGraph g;
    private mxCell cell;
    private String caracteristicas = "";
    private ArrayList<Atributo> atributos;
    private ArrayList<Ligacao> ligacoes;

    public Agregacao(String nome, int pX, int pY, int largura, int altura, mxGraph g) {
        this.nome = nome;
        this.pX = pX;
        this.pY = pY;
        this.largura = largura;
        this.altura = altura;
        this.g = g;
        this.id = null;
        this.cell = null;
        atributos = new ArrayList<>();
        ligacoes = new ArrayList<>();
        g.setCellsEditable(false);
    }

    public Agregacao(mxCell cell, mxGraph g) {
        this.nome = (String) cell.getValue();
        this.pX = (int) cell.getGeometry().getX();
        this.pY = (int) cell.getGeometry().getY();
        this.largura = (int) cell.getGeometry().getWidth();
        this.altura = (int) cell.getGeometry().getHeight();
        this.g = g;
        this.id = cell.getId();
        g.setCellsEditable(false);
        this.caracteristicas = cell.getStyle();
        this.cell = cell;
    }

    public mxCell desenha() {
        this.g.getModel().beginUpdate();
        Object agregacao = null;
        Object parent = this.g.getDefaultParent();
        if (caracteristicas.equals("")) {
            caracteristicas = "fillColor=white;strokeColor=black;whiteSpace=wrap;shape=aggregation";
        }
        try {
            agregacao = this.g.insertVertex(parent,
                    this.id,
                    this.nome,
                    this.pX,
                    this.pY,
                    this.largura,
                    this.altura,
                    caracteristicas);
        } finally {
            this.cell = (mxCell) agregacao;
            this.cell.setTipo("agregacao");
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
        g.setWidth(largura);
        g.setHeight(bounds.getHeight());
        cell.setGeometry(g);
        this.g.getModel().setStyle(cell, caracteristicas);
        this.g.getModel().endUpdate();
    }

    public String getNome() {
        return nome;
    }

    public int getpX() {
        return pX;
    }

    public int getpY() {
        return pY;
    }

    public int getAltura() {
        return altura;
    }

    public int getLargura() {
        return largura;
    }

    public mxCell getCell() {
        return cell;
    }

    public String getCaracteristicas() {
        return caracteristicas;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setpX(int pX) {
        this.pX = pX;
    }

    public void setpY(int pY) {
        this.pY = pY;
    }

    public void setAltura(int altura) {
        this.altura = altura;
    }

    public void setLargura(int largura) {
        this.largura = largura;
    }

    public void setCell(mxCell cell) {
        this.cell = cell;
    }

    public void setCaracteristicas(String caracteristicas) {
        this.caracteristicas = caracteristicas;
    }
    
    public ArrayList<Atributo> getAtributos() {
        return atributos;
    }

    public void setAtributos(ArrayList<Atributo> atributos) {
        this.atributos = atributos;
    }
    
    public ArrayList<Ligacao> getLigacoes() {
        return ligacoes;
    }

    public void setLigacoes(ArrayList<Ligacao> ligacoes) {
        this.ligacoes = ligacoes;
    }
}
