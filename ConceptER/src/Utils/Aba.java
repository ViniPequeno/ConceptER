/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import Model.Atributo;
import Model.Entidade;
import Model.Relacionamento;
import Model.Agregacao;
import Model.Heranca;
import Model.Ligacao;
import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxPoint;
import com.mxgraph.view.mxGraph;
import java.util.ArrayList;

/**
 *
 * @author user
 */
public class Aba extends mxGraphComponent {

    private ArrayList<Entidade> entidades;
    private ArrayList<Relacionamento> relacionamentos;
    private ArrayList<Atributo> atributos;
    private ArrayList<Ligacao> ligacoes;
    private ArrayList<Agregacao> agregacoes;
    private ArrayList<Heranca> herancas;

    //Posição em relações ao número de alterações do abas.get(tabSelecionada).getHistorico()órico
    private int posicaoHistorico;
    //Armazena alterações do usuário para DESFAZER/REFAZER
    private ArrayList<Historico> historico;
    //Armazena todos os objetos da tela
    private ArrayList<mxCell> arrayCell;

    public Aba(mxGraph graph) {
        super(graph);
        entidades = new ArrayList<>();
        relacionamentos = new ArrayList<>();
        atributos = new ArrayList<>();
        ligacoes = new ArrayList<>();
        historico = new ArrayList<>();
        arrayCell = new ArrayList<>();
        agregacoes = new ArrayList<>();
        herancas = new ArrayList<>();
        posicaoHistorico = 0;

        this.setAutoExtend(true);
        this.setAutoscrolls(true);
        this.setAutoScroll(true);
        this.setCenterZoom(true);
        this.setCenterPage(true);
        this.setEnterStopsCellEditing(true);
        this.setOpaque(true);
        this.setToolTips(true);
        this.setConnectable(true);
        this.setGridVisible(true);        //Grade visível
        this.setGridStyle(3);             //Estilo da grade

        this.historico.add(new Historico((ArrayList<mxCell>) this.arrayCell.clone()));

        this.graph.setCellsDisconnectable(false);     //Nao Permite que as setas sejam desconectadas
        this.graph.setEdgeLabelsMovable(false);       //Nao permite que a descricao da seta seja movida
        this.graph.setAllowNegativeCoordinates(false);
        this.graph.setResetEdgesOnMove(true);
        this.graph.setAutoSizeCells(true);
        this.graph.getSelectionModel().setSingleSelection(true);
        this.graph.addListener(mxEvent.CELLS_MOVED, (sender, evt) -> {
            for (Relacionamento r : relacionamentos) {
                if (r.isAutoRelacionamento()) {
                    Ligacao.autoRelacionamento(r.getLigacoes().get(0), r.getLigacoes().get(1), this.graph);
                    for (Ligacao l : r.getLigacoes()) {
                        for (mxPoint p : l.getLigacao().getGeometry().getPoints()) {
                        }
                    }
                }
            }
        });
    }

    public ArrayList<Agregacao> getAgregacoes() {
        return agregacoes;
    }

    public void setAgregacoes(ArrayList<Agregacao> agregacoes) {
        this.agregacoes = agregacoes;
    }

    public ArrayList<Entidade> getEntidades() {
        return entidades;
    }

    public void setEntidades(ArrayList<Entidade> entidades) {
        this.entidades = entidades;
    }

    public ArrayList<Relacionamento> getRelacionamentos() {
        return relacionamentos;
    }

    public void setRelacionamentos(ArrayList<Relacionamento> relacionamentos) {
        this.relacionamentos = relacionamentos;
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

    public ArrayList<Heranca> getHerancas() {
        return herancas;
    }

    public void setHerancas(ArrayList<Heranca> herancas) {
        this.herancas = herancas;
    }

    public ArrayList<Historico> getHistorico() {
        return historico;
    }

    public void setHist(ArrayList<Historico> hist) {
        this.historico = hist;
    }

    public ArrayList<mxCell> getArrayCell() {
        return arrayCell;
    }

    public void setArrayCell(ArrayList<mxCell> arrayCell) {
        this.arrayCell = arrayCell;
    }

    public int getPosicaoHistorico() {
        return posicaoHistorico;
    }

    public void setPosicaoHistorico(int posicaoHistorico) {
        this.posicaoHistorico = posicaoHistorico;
    }

}
