/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Estaticos.EnumCardinalidade;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.model.mxGraphModel;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxPoint;
import com.mxgraph.view.mxGraph;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Aluno
 */
public class Ligacao {

    private mxGraph g;
    private mxCell ligacao;
    private String id;
    private mxCell cellDestino, cellOrigem;
    private boolean total;
    private EnumCardinalidade cardinalidade;
    String caracteristicas = "edgeStyle=none;startArrow=none;endArrow=none;fontSize=20;fontStyle=" + mxConstants.FONT_BOLD;

    public Ligacao(mxGraph g, mxCell cellOrigem, mxCell cellDestino, EnumCardinalidade cardinalidade) {
        this.g = g;
        this.cellOrigem = cellOrigem;
        this.cellDestino = cellDestino;
        this.total = false;
        this.cardinalidade = cardinalidade;
        this.id = null;
    }

    public Ligacao(mxCell cell, mxGraph grafico) {
        this.g = grafico;
        this.cellOrigem = (mxCell) cell.getSource();
        this.cellDestino = (mxCell) cell.getTarget();
        this.total = cell.isTotal();
        switch (cell.getCardinalidade()) {
            case 0:
                this.cardinalidade = EnumCardinalidade.ZERO_PARA_UM;
                break;
            case 1:
                this.cardinalidade = EnumCardinalidade.UM_PARA_UM;
                break;
            case 2:
                this.cardinalidade = EnumCardinalidade.UM_PARA_MUITOS;
                break;
            case 3:
                this.cardinalidade = EnumCardinalidade.MUITOS_PARA_MUITOS;
                break;
            case 4:
                this.cardinalidade = EnumCardinalidade.NENHUM;
        }
        this.id = cell.getId();
        this.ligacao = cell;
        this.caracteristicas = cell.getStyle();
    }

    public void desenha() {
        this.g.getModel().beginUpdate();
        Object parent = this.g.getDefaultParent();
        ligacao = (mxCell) g.insertEdge(parent, id, cardinalidade.toString(), cellOrigem, cellDestino, caracteristicas);
        ligacao.setSource(cellOrigem);
        ligacao.setTarget(cellDestino);
        ligacao.setTotal(total);
        switch (cardinalidade) {
            case ZERO_PARA_UM:
                ligacao.setCardinalidade(0);
                break;
            case UM_PARA_UM:
                ligacao.setCardinalidade(1);
                break;
            case UM_PARA_MUITOS:
                ligacao.setCardinalidade(2);
                break;
            case MUITOS_PARA_MUITOS:
                ligacao.setCardinalidade(3);
                break;
            case NENHUM:
                ligacao.setCardinalidade(4);
                break;
        }
        if (ligacao.getTipo() == null || ligacao.getTipo().equals("")) {
            this.ligacao.setTipo("ligacao");
        }
        this.g.getModel().endUpdate();
    }

    public void atualiza() {
        this.g.getModel().beginUpdate();
        //this.g.getModel().setValue(ligacao, nome);
        this.g.updateCellSize(ligacao);

//        mxGeometry g = (mxGeometry) cell.getGeometry().clone();
//        mxRectangle bounds = new mxRectangle(pX, pY, largura, altura);
//        g.setWidth(cell.getGeometry().getWidth() + 10);
//        g.setHeight(bounds.getHeight());
//        cell.setGeometry(g);
        //mxConstants.FONT_UNDERLINE;
        if (isTotal()) {
            caracteristicas += ";shape=doubleConnector;simples";
            ligacao.setTotal(true);
        } else {
            caracteristicas = caracteristicas.replace(";shape=doubleConnector;simples", "");
            ligacao.setTotal(false);
        }
        if (!ligacao.getTipo().equals("ligacao")) {
            ligacao.setCardinalidade(2);
        } else {
            switch (cardinalidade) {
                case ZERO_PARA_UM:
                    ligacao.setCardinalidade(0);
                    break;
                case UM_PARA_UM:
                    ligacao.setCardinalidade(1);
                    break;
                case UM_PARA_MUITOS:
                    ligacao.setCardinalidade(2);
                    break;
                case MUITOS_PARA_MUITOS:
                    ligacao.setCardinalidade(3);
                    break;
            }
        }
        this.g.getModel().setValue(ligacao, cardinalidade.toString());
        this.g.getModel().setStyle(ligacao, caracteristicas);
        this.g.getModel().endUpdate();
    }

    public static void autoRelacionamento(Ligacao l1, Ligacao l2, mxGraph g) {
        g.getModel().beginUpdate();
        l1.getLigacao().getStyle().replace("edgeStyle=none", "edgeStyle=orthogonalEdgeStyle");
        l2.getLigacao().getStyle().replace("edgeStyle=none", "edgeStyle=orthogonalEdgeStyle");
        try {
            mxGeometry geometryOfEdge = ((mxGraphModel) (g.getModel())).getGeometry(l1.getLigacao());
            geometryOfEdge = (mxGeometry) geometryOfEdge.clone();
            List<mxPoint> ptsL1 = geometryOfEdge.getPoints();
            ptsL1 = new ArrayList<mxPoint>();
            mxCell sc1 = l1.getSource();
            mxCell tg1 = l1.getTarget();
            ptsL1.add(new mxPoint(tg1.getGeometry().getX() + (tg1.getGeometry().getWidth() / 2), sc1.getGeometry().getY() + (sc1.getGeometry().getHeight() / 2)));
            geometryOfEdge.setPoints(ptsL1);
            ((mxGraphModel) (g.getModel())).setGeometry(l1.getLigacao(), geometryOfEdge);

            mxGeometry geometryOfEdge2 = ((mxGraphModel) (g.getModel())).getGeometry(l2.getLigacao());
            geometryOfEdge2 = (mxGeometry) geometryOfEdge2.clone();
            List<mxPoint> ptsL2 = geometryOfEdge2.getPoints();
            ptsL2 = new ArrayList<mxPoint>();
            mxCell sc2 = l2.getSource();
            mxCell tg2 = l2.getTarget();
            ptsL2.add(new mxPoint(sc2.getGeometry().getX() + (sc2.getGeometry().getWidth() / 2), tg2.getGeometry().getY() + (tg2.getGeometry().getHeight() / 2)));
            geometryOfEdge2.setPoints(ptsL2);
            ((mxGraphModel) (g.getModel())).setGeometry(l2.getLigacao(), geometryOfEdge2);
        } finally {
            g.getModel().endUpdate();
        }
    }

    public mxGraph getG() {
        return g;
    }

    public void setG(mxGraph g) {
        this.g = g;
    }

    public mxCell getLigacao() {
        return ligacao;
    }

    public void setLigacao(mxCell ligacao) {
        this.ligacao = ligacao;
    }

    public mxCell getTarget() {
        return cellDestino;
    }

    public void setTarget(mxCell cellDestino) {
        this.cellDestino = cellDestino;
    }

    public mxCell getSource() {
        return cellOrigem;
    }

    public void setSource(mxCell cellOrigem) {
        this.cellOrigem = cellOrigem;
    }

    public boolean isTotal() {
        return total;
    }

    public void setTotal(boolean total) {
        this.total = total;
    }

    public EnumCardinalidade getCardinalidade() {
        return cardinalidade;
    }

    public void setCardinalidade(EnumCardinalidade cardinalidade) {
        this.cardinalidade = cardinalidade;
    }

}
