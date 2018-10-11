/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.view.mxGraph;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Aluno
 */
public class Entidade implements Serializable {

    private String nome, id;
    private int pX, pY, altura, largura;
    private mxGraph g;
    private mxCell cell;
    private ArrayList<Atributo> atributos = new ArrayList<>();
    private String caracteristicas = "";
    private boolean fraca;
    
    public static final int LARGURA_INICIAL = 100;
    public static final int ALTURA_INICIAL = 50;
    
    
    public Entidade(String nome, int pX, int pY, int largura, int altura, mxGraph g) {
        this.nome = nome;
        this.fraca = false;
        this.pX = pX;
        this.pY = pY;
        this.largura = largura;
        this.altura = altura;
        this.g = g;
        id = null;
        cell = null;
        g.setCellsEditable(false);
    }

    public Entidade(mxCell cell, mxGraph g) {
        this.nome = (String) cell.getValue();
        this.fraca = cell.isFraca();
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

    public String getCaracteristicas() {
        return caracteristicas;
    }

    public mxCell desenha() {
        this.g.getModel().beginUpdate();
        Object entidade = null;
        Object parent = this.g.getDefaultParent();
        if (caracteristicas.equals("")) {
            caracteristicas = "fillColor=white;strokeColor=black;whiteSpace=wrap;shape=rectangle;";
        }
        try {
            entidade = this.g.insertVertex(parent,
                    this.id,
                    this.nome,
                    this.pX,
                    this.pY,
                    this.largura,
                    this.altura,
                    caracteristicas);
        } finally {
            this.cell = (mxCell) entidade;
            this.cell.setTipo("entidade");
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
        //g.setWidth(cell.getGeometry().getWidth() + 10);
        g.setWidth(largura);
        g.setHeight(bounds.getHeight());
        cell.setGeometry(g);
        if (fraca) {
            cell.setFraca(true);
            caracteristicas = "fillColor=white;strokeColor=black;whiteSpace=wrap;shape=doubleRectangle";
        } else {
            cell.setFraca(false);
            caracteristicas = "fillColor=white;strokeColor=black;whiteSpace=wrap;shape=rectangle";
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

    public void setCaracteristicas(String caracteristicas) {
        this.caracteristicas = caracteristicas;
    }

    public boolean isFraca() {
        return fraca;
    }

    public void setFraca(boolean fraca) {
        this.fraca = fraca;
    }

    public int getpX() {
        return pX;
    }

    public void setpX(int pX) {
        this.pX = pX;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
