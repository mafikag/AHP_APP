/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package menuutama;

/**
 *
 * @author ENMA
 */
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;


public class MatriksAhp {
    private boolean sedangUpdate = false;

    public void addReciprocalListener(JTextField source, JTextField target) {
        source.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { sync(source, target); }
            public void removeUpdate(DocumentEvent e) { sync(source, target); }
            public void changedUpdate(DocumentEvent e) { sync(source, target); }
        });
    }

    private void sync(JTextField source, JTextField target) {
        if (sedangUpdate) return;

        try {
            sedangUpdate = true;
            String isi = source.getText().trim();
            if (!isi.isEmpty()) {
                double nilai = Double.parseDouble(isi);
                if (nilai != 0) {
                    double hasil = 1.0 / nilai;
                    target.setText(String.format("%.2f", hasil));
                } else {
                    target.setText("");
                }
            } else {
                target.setText("");
            }
        } catch (NumberFormatException e) {
            target.setText("");
        } finally {
            sedangUpdate = false;
        }
    }

    // Optional: untuk menyetel nilai diagonal = 1
    public void setDiagonalToOne(JTextField[][] fields) {
        for (int i = 0; i < fields.length; i++) {
            fields[i][i].setText("1");
            fields[i][i].setEditable(false);
        }
    }

    // Optional: menginisialisasi listener otomatis untuk seluruh simetri
    public void autoBindMatrix(JTextField[][] fields) {
        int size = fields.length;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i < j) {
                    addReciprocalListener(fields[i][j], fields[j][i]);
                    addReciprocalListener(fields[j][i], fields[i][j]);
                }
            }
        }
    }
}
