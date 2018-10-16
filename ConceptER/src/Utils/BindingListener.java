/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import com.mxgraph.model.mxCell;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

/**
 *
 * @author Yan e Pedro
 */
public class BindingListener implements DocumentListener {
    private mxCell model;
    private String fieldName;

    public BindingListener(mxCell model, String fieldName) {
        this.model = model;

        String firstChar = String.valueOf(fieldName.charAt(0));
        if (firstChar.equals(firstChar.toLowerCase())) {
            fieldName = firstChar.toUpperCase()
                    + fieldName.substring(1, fieldName.length());
        }

        this.fieldName = fieldName;
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        dataUpdated(e);
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        dataUpdated(e);
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        dataUpdated(e);
    }

    private void dataUpdated(DocumentEvent e) {
        try {
            String text = e.getDocument().getText(
                    e.getDocument().getStartPosition().getOffset(),
                    e.getDocument().getEndPosition().getOffset() - 1);


            Method method = model.getClass().getDeclaredMethod(
                    "set" + fieldName, String.class);
            method.invoke(model, text);

        } catch (BadLocationException e1) {
            e1.printStackTrace();
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(BindingListener.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(BindingListener.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(BindingListener.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(BindingListener.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(BindingListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
