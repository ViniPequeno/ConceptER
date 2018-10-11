/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import com.mxgraph.model.mxCell;
import java.util.ArrayList;

/**
 *
 * @author Yan e Pedro
 */
public class Historico implements Cloneable {

    public ArrayList<mxCell> atual;

    public Historico(ArrayList<mxCell> atual) {
        this.atual = atual;
    }

    public ArrayList<mxCell> getNewCells() {
        ArrayList<mxCell> newCells = new ArrayList<>(atual);
        for (mxCell c : atual) {
            System.out.println(c);
        }
        return newCells;
    }
}
