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
import java.text.DecimalFormat;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;


public class hitungMatriksKriteria {
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
    
    
    //untuk mengsum baris
    public void hitungJumlahBaris(JTextField[][] fields, JTextField[] sumFields) {
        DecimalFormat df = new DecimalFormat("#.###");
        for (int i = 0; i < 5; i++) {
            double jumlah = 0.0;
            for (int j = 0; j < 5; j++) {
                try {
                    double nilai = Double.parseDouble(fields[i][j].getText());
                    jumlah += nilai;
                } catch (NumberFormatException e) {
                // Jika field kosong atau tidak valid, anggap 0
                }
            }
            sumFields[i].setText(df.format(jumlah));
        }
    }
    
    //hitung normalisasi matriks
    public void hitungNormalisasiDanPrioritas(JTextField[][] fields, JTextField[][] normalfields, JTextField[] sumfields, JTextField[] normalSumFields, JTextField[] sumColsfields, JTextField[] prioritasfields ) {
    double[][] matriksPerbandingan = new double[5][5];
    double[] jumlahBaris = new double[5]; // Bukan jumlah kolom
    DecimalFormat df = new DecimalFormat("#.###");

    // Ambil nilai dari matriks perbandingan dan hitung jumlah baris
    for (int i = 0; i < 5; i++) {
        for (int j = 0; j < 5; j++) {
            try {
                matriksPerbandingan[i][j] = Double.parseDouble(fields[i][j].getText());
            } catch (NumberFormatException e) {
                matriksPerbandingan[i][j] = 0.0;
            }
            jumlahBaris[i] += matriksPerbandingan[i][j];
        }
    }

    // Tampilkan jumlah baris ke JTextField (opsional, jika ingin)
    for (int i = 0; i < 5; i++) {
        sumfields[i].setText(df.format(jumlahBaris[i]));
    }

    // Hitung matriks normalisasi dan prioritas berdasarkan pembagian dengan jumlah baris
    for (int i = 0; i < 5; i++) {
        double jumlahNormalisasi = 0.0;
        for (int j = 0; j < 5; j++) {
            double nilaiNormalisasi = (jumlahBaris[i] != 0) ? matriksPerbandingan[i][j] / jumlahBaris[i] : 0.0;
            normalfields[i][j].setText(df.format(nilaiNormalisasi));
            jumlahNormalisasi += nilaiNormalisasi;
        }

        normalSumFields[i].setText(df.format(jumlahNormalisasi)); // Jumlah baris normalisasi
    }
    // Hitung jumlah kolom normalisasi dan tampilkan
    
    for (int j = 0; j < 5; j++) {
        double jumlahKolom = 0.0;
        for (int i = 0; i < 5; i++) {
            try {
                double nilai = Double.parseDouble(normalfields[i][j].getText());
                jumlahKolom += nilai;
            } catch (NumberFormatException e) {
            // Jika error parsing, lewati saja
            }
        }
        sumColsfields[j].setText(df.format(jumlahKolom));
        double prioritas = jumlahKolom / 5.0; // Rata-rata
        prioritasfields[j].setText(df.format(prioritas));
    }
}
}
