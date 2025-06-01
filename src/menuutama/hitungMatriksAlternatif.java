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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import koneksi.Koneksi;


public class hitungMatriksAlternatif {
    private boolean sedangUpdate = false;
    private Connection conn = new Koneksi().connect();

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
        for (int i = 0; i < 3; i++) {
            double jumlah = 0.0;
            for (int j = 0; j < 3; j++) {
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
    double[][] matriksPerbandingan = new double[3][3];
    double[] jumlahBaris = new double[3]; // Bukan jumlah kolom
    DecimalFormat df = new DecimalFormat("#.###");

    // Ambil nilai dari matriks perbandingan dan hitung jumlah baris
    for (int i = 0; i < 3; i++) {
        for (int j = 0; j < 3; j++) {
            try {
                matriksPerbandingan[i][j] = Double.parseDouble(fields[i][j].getText());
            } catch (NumberFormatException e) {
                matriksPerbandingan[i][j] = 0.0;
            }
            jumlahBaris[i] += matriksPerbandingan[i][j];
        }
    }

    // Tampilkan jumlah baris ke JTextField (opsional, jika ingin)
    for (int i = 0; i < 3; i++) {
        sumfields[i].setText(df.format(jumlahBaris[i]));
    }

    // Hitung matriks normalisasi dan prioritas berdasarkan pembagian dengan jumlah baris
    for (int i = 0; i < 3; i++) {
        double jumlahNormalisasi = 0.0;
        for (int j = 0; j < 3; j++) {
            double nilaiNormalisasi = (jumlahBaris[i] != 0) ? matriksPerbandingan[i][j] / jumlahBaris[i] : 0.0;
            normalfields[i][j].setText(df.format(nilaiNormalisasi));
            jumlahNormalisasi += nilaiNormalisasi;
        }

        normalSumFields[i].setText(df.format(jumlahNormalisasi)); // Jumlah baris normalisasi
    }
    
    // Hitung jumlah kolom normalisasi dan tampilkan
    for (int j = 0; j < 3; j++) {
        double jumlahKolom = 0.0;
        for (int i = 0; i < 3; i++) {
            try {
                double nilai = Double.parseDouble(normalfields[i][j].getText());
                jumlahKolom += nilai;
            } catch (NumberFormatException e) {
            // Jika error parsing, lewati saja
            }
        }
        sumColsfields[j].setText(df.format(jumlahKolom));
        double prioritas = jumlahKolom / 3.0; // Rata-rata
        prioritasfields[j].setText(df.format(prioritas));
    }
}
    // Hitung Nilai Prioritas Akhir
    public void hitungDanSimpanPrioritasAkhir() {
    try {
        int jumlahKriteria = 5;

        // 1. Ambil nilai prioritas tiap kriteria
        double[] bobotKriteria = new double[jumlahKriteria];
        String sqlKriteria = "SELECT nilai_prioritas FROM kriteria ORDER BY kd_kriteria ASC";
        try (PreparedStatement ps = conn.prepareStatement(sqlKriteria);
             ResultSet rs = ps.executeQuery()) {
            int i = 0;
            while (rs.next()) {
                bobotKriteria[i++] = rs.getDouble("nilai_prioritas");
            }
        }

        // 2. Ambil semua alternatif dari tabel alternatif
        List<Integer> idAlternatifList = new ArrayList<>();
        List<String> namaAlternatifList = new ArrayList<>();

        String sqlAlternatif = "SELECT id_layanan, nama_layanan FROM alternatif ORDER BY id_layanan ASC";
        try (PreparedStatement ps = conn.prepareStatement(sqlAlternatif);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                idAlternatifList.add(Integer.parseInt(rs.getString("id_layanan")));
                namaAlternatifList.add(rs.getString("nama_layanan"));
            }
        }

        int jumlahAlternatif = idAlternatifList.size();
        int jmlhKriteria = bobotKriteria.length;

        // 3. Ambil nilai prioalternatif dan simpan ke array 2 dimensi
        double[][] nilaiAlternatif = new double[jumlahAlternatif][jmlhKriteria];
        String sqlNilai = "SELECT nilai FROM prioalternatif ORDER BY id ASC"; // Perbaikan di sini

        try (PreparedStatement ps = conn.prepareStatement(sqlNilai);
            ResultSet rs = ps.executeQuery()) {
            int index = 0;
            while (rs.next()) {
                if (index >= jumlahAlternatif * jmlhKriteria) {
                    System.err.println("Data lebih banyak dari ukuran array. Periksa jumlahAlternatif dan jmlhKriteria!");
                    break;
                }

            int kriIndex = index / jumlahAlternatif;
            int altIndex = index % jumlahAlternatif;
            nilaiAlternatif[altIndex][kriIndex] = rs.getDouble("nilai");
            index++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

//System.out.println(Arrays.toString(bobotKriteria));
//for (int i = 0; i < nilaiAlternatif.length; i++) {
//    System.out.println(Arrays.toString(nilaiAlternatif[i]));
//}


        // 4. Hitung hasil penilaian AHP
        double[] hasilPenilaian = new double[jumlahAlternatif];
        for (int i = 0; i < jumlahAlternatif; i++) {
        double total = 0;
        for (int j = 0; j < jumlahKriteria; j++) {
            total += nilaiAlternatif[i][j] * bobotKriteria[j];
        }
        hasilPenilaian[i] = total;
        }

        // 4. Kosongkan dulu tabel seleksi
        conn.prepareStatement("DELETE FROM seleksi").executeUpdate();

        // 5. Simpan hasil penilaian ke tabel seleksi
        String sqlInsert = "INSERT INTO seleksi (id_layanan, nama_layanan, hasil_penilaian) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sqlInsert)) {
            for (int i = 0; i < jumlahAlternatif; i++) {
                ps.setInt(1, idAlternatifList.get(i));
                ps.setString(2, namaAlternatifList.get(i));
                ps.setDouble(3, hasilPenilaian[i]);
                ps.addBatch();
            }
            ps.executeBatch();
        }

        System.out.println("Prioritas akhir berhasil dihitung dan disimpan ke tabel seleksi.");
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Terjadi kesalahan: " + e.getMessage());
    }

    }
}
