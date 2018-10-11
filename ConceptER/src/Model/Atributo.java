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
 * @author Aluno
 */
public class Atributo implements Serializable {

    private String nome, id;
    private int pX, pY, altura, largura;
    private mxGraph g;
    private mxCell cell; //ATRIBUTO
    private mxCell cellDono; //DONO
    private String caracteristicas;
    private ArrayList<Atributo> atributos = new ArrayList<>();
    private boolean pk = false;
    private boolean fk = false;
    private boolean unique = false;
    private boolean autoIncrement = false;
    private boolean notNull = false;
    private String padrao = null;
    private boolean composto, multivalorado, derivado;
    Object ob;
    private String dominio, complemento;

    public Atributo(String nome, int pX, int pY, int largura, int altura, mxGraph g, mxCell cellDono) {
        this.nome = nome;
        this.pX = pX;
        this.pY = pY;
        this.id = null;
        this.largura = largura;
        this.altura = altura;
        this.g = g;
        this.cell = null;
        this.cellDono = cellDono;
        this.composto = false;
        this.multivalorado = false;
        this.derivado = false;
        this.dominio = "INTEGER";
        this.complemento = "";
    }

    public Atributo(mxCell cell, mxGraph g) {
        this.nome = (String) cell.getValue();
        this.pX = (int) cell.getGeometry().getX();
        this.pY = (int) cell.getGeometry().getY();
        this.largura = (int) cell.getGeometry().getWidth();
        this.altura = (int) cell.getGeometry().getHeight();
        this.g = g;
        this.id = cell.getId();
        this.cell = cell;
        this.cellDono = (mxCell) cell.getTarget();
        this.caracteristicas = cell.getStyle();
        this.unique = cell.isUnique();
        this.derivado = cell.isDerivado();
        this.autoIncrement = cell.isAuto_increment();
        this.notNull = cell.isNotNull();
        this.pk = cell.isPk();
        this.fk = cell.isFk();
        this.padrao = cell.getDefaultee();
        this.dominio = cell.getDominio();
        this.complemento = cell.getComplemento();
        if (cell.isComposto()) {
            this.composto = false;
        }
    }

    public mxCell desenha() {
        this.g.getModel().beginUpdate();
        Object atributo = null;
        Object parent = this.g.getDefaultParent();
        if (caracteristicas == null) {
            caracteristicas = "fillColor=white;strokeColor=black;shape=ellipse";
        }
        try {
            atributo = this.g.insertVertex(parent,
                    this.id,
                    this.nome,
                    this.pX,
                    this.pY,
                    this.largura,
                    this.altura,
                    caracteristicas);
            g.insertEdge(parent, null, null, atributo, cellDono, "startArrow=none;endArrow=none;");

        } finally {
            this.cell = (mxCell) atributo;
            this.cell.setTipo("atributo");
            this.cell.setDominio(dominio);
            this.cell.setComplemento(complemento);
            this.cell.setConnectable(false);
            this.cell.setTarget(cellDono);
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
        caracteristicas = "fillColor=white;strokeColor=black";
        if (composto == false && multivalorado == false && derivado == false) {
            caracteristicas += ";shape=ellipse;";
        }
        if (composto) {
            cell.setComposto(true);
            caracteristicas += ";shape=ellipse;";
        } else {
            cell.setComposto(false);
        }
        if (multivalorado && derivado) {
            cell.setMultivalorado(true);
            cell.setDerivado(true);
            caracteristicas += ";shape=doubleEllipse;dashed=1;";
        } else {
            if (multivalorado) {
                cell.setMultivalorado(true);
                caracteristicas += ";shape=doubleEllipse;";
            } else {
                cell.setMultivalorado(false);
            }
            if (derivado) {
                cell.setDerivado(true);
                caracteristicas += ";shape=ellipse;dashed=1";
            } else {
                cell.setMultivalorado(false);
            }
        }
        if (pk == true) {
            cell.setPk(true);
            caracteristicas += ";fontStyle=4";
        } else {
            cell.setPk(false);
            caracteristicas.replace(";fontStyle=4", "");
        }
        if (notNull == true) {
            cell.setNotNull(true);
        } else {
            cell.setNotNull(false);
        }
        if (unique == true) {
            cell.setUnique(true);
        } else {
            cell.setUnique(false);
        }
        if (autoIncrement == true) {
            cell.setAuto_increment(true);
        } else {
            cell.setAuto_increment(false);
        }
        if (padrao != null) {
            cell.setDefaulte(true);
            cell.setDefaultee(padrao);
        } else {
            cell.setDefaulte(false);
            cell.setDefaultee(null);
        }
        this.cell.setDominio(dominio);
        this.cell.setComplemento(complemento);
        this.cell.setStyle(this.g.getModel().setStyle(cell, caracteristicas));
        this.g.getModel().endUpdate();
    }

    public boolean isNotNull() {
        return notNull;
    }

    public void setNotNull(boolean notNull) {
        this.notNull = notNull;
    }

    public String getPadrao() {
        return padrao;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Object getOb() {
        return ob;
    }

    public void setOb(Object ob) {
        this.ob = ob;
    }

    public void setPadrao(String padrao) {
        this.padrao = padrao;
    }

    public String getNome() {
        return nome;
    }

    public String getCaracteristicas() {
        return caracteristicas;
    }

    public void setCaracteristicas(String caracteristicas) {
        this.caracteristicas = caracteristicas;
    }

    public ArrayList<Atributo> getAtributos() {
        return atributos;
    }

    public String getDominio() {
        return dominio;
    }

    public void setDominio(String dominio) {
        this.cell.setDominio(dominio);
        this.dominio = dominio;
    }

    public void setAtributos(ArrayList<Atributo> atributos) {
        this.atributos = atributos;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public boolean isComposto() {
        return composto;
    }

    public void setComposto(boolean composto) {
        this.composto = composto;
    }

    public boolean isMultivalorado() {
        return multivalorado;
    }

    public void setMultivalorado(boolean multivalorado) {
        this.multivalorado = multivalorado;
    }

    public boolean isDerivado() {
        return derivado;
    }

    public void setDerivado(boolean derivado) {
        this.derivado = derivado;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {

        this.cell.setComplemento(complemento);
        this.complemento = complemento;
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

    public boolean isPk() {
        return pk;
    }

    public void setPk(boolean pk) {
        this.pk = pk;
    }

    public boolean isFk() {
        return fk;
    }

    public void setFk(boolean fk) {
        this.fk = fk;
    }

    public boolean isUnique() {
        return unique;
    }

    public void setUnique(boolean unique) {
        this.unique = unique;
    }

    public boolean isAutoIncrement() {
        return autoIncrement;
    }

    public void setAutoIncrement(boolean autoIncrement) {
        this.autoIncrement = autoIncrement;
    }

    public mxCell getCellDono() {
        return cellDono;
    }

    public void setCellDono(mxCell cellDono) {
        this.cellDono = cellDono;
    }

}
