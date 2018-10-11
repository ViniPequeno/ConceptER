/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Estaticos.EnumCardinalidade;
import com.mxgraph.model.mxCell;
import com.mxgraph.view.mxGraph;
import java.util.ArrayList;

/**
 *
 * @author Yan e Pedro
 */
public class Heranca {

    private mxCell entidadeMae;
    private ArrayList<Entidade> entidadesFilhas;
    private String tipo,id,caracteristicas;
    private mxGraph g;
    private mxCell cellTipo;
    private int pX, pY, largura,altura;
    private boolean total;
    private Ligacao cellTotal;

    public Heranca(mxCell entidadeMae, String tipo, mxGraph g) {
        this.entidadeMae = entidadeMae;
        this.entidadesFilhas = new ArrayList<>();
        this.tipo = tipo;
        this.g = g;
        this.total = false;
        id = null;
    }

    public Heranca(mxCell cellTipo, mxGraph g) {
        this.tipo = (String) cellTipo.getValue();
        this.pX = (int) cellTipo.getGeometry().getX();
        this.pY = (int) cellTipo.getGeometry().getY();
        this.largura = (int) cellTipo.getGeometry().getWidth();
        this.altura = (int) cellTipo.getGeometry().getHeight();
        this.entidadesFilhas = new ArrayList<>();
        this.g = g;
        this.id = cellTipo.getId();
        this.g.setCellsEditable(false);
        this.caracteristicas = cellTipo.getStyle();
        this.cellTipo = cellTipo;
        this.cellTipo.setTipo("heranca"+this.cellTipo.getValue());
        this.total = cellTipo.isTotal();
    }

    public void desenha(ArrayList<Entidade> entidades, ArrayList<Ligacao> ligacoes) {
        this.g.getModel().beginUpdate();
        Object parent = this.g.getDefaultParent();
        int posx = (int) entidadeMae.getGeometry().getX();
        int posy = (int) entidadeMae.getGeometry().getY();
        entidadeMae.setTipo("entidadeMae");
        try {
            this.cellTipo = (mxCell) this.g.insertVertex(parent, null, this.tipo, posx + 40, posy + 100, 20, 20, "resizable=0;editable=0;shape=ellipse;");
            Entidade entidade = new Entidade("Entidade", posx - 125, posy + 170, 100, 50, g);
            entidade.desenha();
            entidadesFilhas.add(entidade);
            entidade.getCell().setTipo("entidadeFilha");
            entidades.add(entidade);
            mxCell cell = (mxCell)this.g.insertEdge(parent, null, "U", this.cellTipo, this.entidadesFilhas.get(0).getCell(), "edgeStyle=none;startArrow=none;endArrow=none;strokeColor=red;");
            Ligacao l2 = new Ligacao(cell, g);
            l2.setSource(cellTipo);
            l2.setTarget(entidadeMae);
            l2.setCardinalidade(EnumCardinalidade.NENHUM);
            l2.getLigacao().setTotal(false);
            l2.getLigacao().setTipo("ligacaoEntidadeFilha");
            ligacoes.add(l2);
            cellTipo.insertEdge(cell,true);
            cellTotal = new Ligacao((mxCell) this.g.insertEdge(parent, null, "", this.cellTipo, entidadeMae, "edgeStyle=none;startArrow=none;endArrow=none;strokeColor=red;"), this.g);
            cellTotal.setSource(cellTipo);
            cellTotal.setTarget(entidadeMae);
            cellTotal.setCardinalidade(EnumCardinalidade.NENHUM);
            cellTotal.getLigacao().setTotal(false);
            cellTotal.getLigacao().setTipo("ligacaoEntidadeMae");
            ligacoes.add(cellTotal);
        } finally {
            if (tipo.equals("d")) {
                this.cellTipo.setTipo("herancad");
            }else{
                this.cellTipo.setTipo("herancao");
            }
            this.cellTipo.setConnectable(false);
            this.g.getModel().endUpdate();
        }
    }
    
    public mxCell desenha(){
        this.g.getModel().beginUpdate();
        Object parent = this.g.getDefaultParent();
        if (caracteristicas.equals("")) {
            caracteristicas = "resizable=0;editable=0;shape=ellipse;";
        }
        try {
            cellTipo = (mxCell) this.g.insertVertex(parent,
                    this.id,
                    this.tipo,
                    this.pX,
                    this.pY,
                    this.largura,
                    this.altura,
                    caracteristicas);
        } finally {
            if (tipo.equals("d")) {
                this.cellTipo.setTipo("herancad");
            }else{
                this.cellTipo.setTipo("herancao");
            }
            this.cellTipo.setConnectable(false);
            this.g.getModel().endUpdate();
        }
        return this.cellTipo;
    }

    public void addEntidade(ArrayList<Entidade> entidades, ArrayList<Ligacao> ligacoes) {
        this.g.getModel().beginUpdate();
        Object parent = this.g.getDefaultParent();

        int posx = (int) entidadeMae.getGeometry().getX() + (entidadesFilhas.size() * 120);
        int posy = (int) entidadeMae.getGeometry().getY();

        try {
            Entidade entidade = new Entidade("Entidade", posx - 125, posy + 170, 100, 50, g);
            entidade.desenha();
            entidade.getCell().setTipo("entidadeFilha");
            entidadesFilhas.add(entidade);
            entidades.add(entidade);
            Ligacao l = new Ligacao((mxCell) this.g.insertEdge(parent, null, "U", this.cellTipo, entidade.getCell(), "edgeStyle=none;startArrow=none;endArrow=none;strokeColor=red;"), this.g);
            l.setSource(cellTipo);
            l.setTarget(entidadeMae);
            l.setCardinalidade(EnumCardinalidade.NENHUM);
            l.getLigacao().setTotal(false);
            l.getLigacao().setTipo("ligacaoEntidadeFilha");
            ligacoes.add(l);
        } finally {
            this.g.getModel().endUpdate();
        }
    }

    public mxCell getEntidadeMae() {
        return entidadeMae;
    }

    public void setEntidadeMae(mxCell entidadeMae) {
        this.entidadeMae = entidadeMae;
    }

    public ArrayList<Entidade> getEntidadesFilhas() {
        return entidadesFilhas;
    }

    public void setEntidadesFilhas(ArrayList<Entidade> entidadesFilhas) {
        this.entidadesFilhas = entidadesFilhas;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Entidade getEntidadesFilhasEm(int index) {
        return entidadesFilhas.get(index);
    }

    public mxCell getCellTipo() {
        return cellTipo;
    }

    public mxGraph getG() {
        return g;
    }

    public void setG(mxGraph g) {
        this.g = g;
    }  

    public boolean isTotal() {
        return total;
    }

    public void setTotal(boolean total) {
        this.total = total;
    }

    public Ligacao getCellTotal() {
        return cellTotal;
    }

    public void setCellTotal(Ligacao cellTotal) {
        this.cellTotal = cellTotal;
    }
    
}
