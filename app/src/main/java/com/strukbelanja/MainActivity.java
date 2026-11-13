
package com.strukbelanja;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.*;

import java.text.NumberFormat;
import java.util.Locale;

public class MainActivity extends Activity {

    private final String[] namaBarang = {
            "Buku", "Pulpen", "Penghapus", "Penggaris", "Pensil"
    };

    private final int[] hargaBarang = {
            5000, 3000, 2000, 4000, 2500
    };

    private final Keranjang keranjang = new Keranjang();
    private final double PPN = 0.11;

    private TextView hasil;
    private TextView textPPN;
    private TextView textTotal;

    private Spinner pilihan;
    private EditText inputJumlah;
    private TableLayout tableKeranjang;

    private int posisiDipilih = 0;

    private final NumberFormat rupiah =
            NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // MENGHUBUNGKAN KOMPONEN XML
        pilihan = findViewById(R.id.pilihanBarang);
        inputJumlah = findViewById(R.id.inputJumlah);

        hasil = findViewById(R.id.hasilStruk);
        textPPN = findViewById(R.id.textPPN);
        textTotal = findViewById(R.id.textTotal);

        tableKeranjang = findViewById(R.id.tableKeranjang);

        // ADAPTER SPINNER LAMA
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                namaBarang
        );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        pilihan.setAdapter(adapter);

        // KARTU PILIHAN BARANG
        findViewById(R.id.bukuButton).setOnClickListener(v ->
                pilihBarang(0));

        findViewById(R.id.pulpenButton).setOnClickListener(v ->
                pilihBarang(1));

        findViewById(R.id.penghapusButton).setOnClickListener(v ->
                pilihBarang(2));

        findViewById(R.id.penggarisButton).setOnClickListener(v ->
                pilihBarang(3));

        findViewById(R.id.pensilButton).setOnClickListener(v ->
                pilihBarang(4));

        // TOMBOL TAMBAH KERANJANG
        findViewById(R.id.tambahButton).setOnClickListener(v ->
                tambahKeKeranjang());

        // TOMBOL CETAK STRUK
        findViewById(R.id.hitungButton).setOnClickListener(v ->
                tampilkanStruk());

        // TOMBOL HAPUS BARANG TERAKHIR
        findViewById(R.id.hapusButton).setOnClickListener(v ->
                hapusBarang());

        // TOMBOL RESET
        findViewById(R.id.resetButton).setOnClickListener(v ->
                resetKeranjang());

        perbaruiTampilan();
    }

    // MEMILIH BARANG
    private void pilihBarang(int posisi) {

        posisiDipilih = posisi;

        pilihan.setSelection(posisi);

        Toast.makeText(
                this,
                namaBarang[posisi] + " dipilih",
                Toast.LENGTH_SHORT
        ).show();
    }

    // MENAMBAHKAN BARANG KE KERANJANG
    private void tambahKeKeranjang() {

        String teksJumlah = inputJumlah.getText().toString().trim();

        if (teksJumlah.isEmpty()) {
            inputJumlah.setError("Masukkan jumlah barang");
            return;
        }

        int jumlah;

        try {
            jumlah = Integer.parseInt(teksJumlah);
        } catch (NumberFormatException e) {
            inputJumlah.setError("Jumlah harus berupa angka");
            return;
        }

        if (jumlah <= 0) {
            inputJumlah.setError("Jumlah harus lebih dari 0");
            return;
        }

        Barang barangBaru = new Barang(
                namaBarang[posisiDipilih],
                hargaBarang[posisiDipilih],
                jumlah
        );

        keranjang.tambahBarang(barangBaru);

        Toast.makeText(
                this,
                namaBarang[posisiDipilih] + " berhasil ditambahkan!",
                Toast.LENGTH_SHORT
        ).show();

        inputJumlah.setText("");

        perbaruiTampilan();
    }

    // MEMPERBARUI TABEL DAN TOTAL PEMBAYARAN
    private void perbaruiTampilan() {

        // HAPUS BARIS TABEL LAMA, HEADER TETAP ADA
        if (tableKeranjang.getChildCount() > 1) {
            tableKeranjang.removeViews(
                    1,
                    tableKeranjang.getChildCount() - 1
            );
        }

        int subtotal = 0;

        // MENAMPILKAN ISI KERANJANG
        for (Barang barang : keranjang.getDaftarBarang()) {

            int harga = barang.getHarga();
            int jumlah = barang.getJumlah();
            int totalBarang = barang.hitungSubtotal();

            subtotal += totalBarang;

            TableRow row = new TableRow(this);

            row.addView(buatKolom(barang.getNama()));
            row.addView(buatKolom(rupiah.format(harga)));
            row.addView(buatKolom(String.valueOf(jumlah)));
            row.addView(buatKolom(rupiah.format(totalBarang)));

            tableKeranjang.addView(row);
        }

        // MENGHITUNG PPN
        int pajak = (int) Math.round(subtotal * PPN);

        // TOTAL PEMBAYARAN
        int total = subtotal + pajak;

        hasil.setText(rupiah.format(subtotal));
        textPPN.setText(rupiah.format(pajak));
        textTotal.setText(rupiah.format(total));
    }

    // MEMBUAT KOLOM TABEL
    private TextView buatKolom(String teks) {

        TextView kolom = new TextView(this);

        kolom.setText(teks);
        kolom.setTextSize(10);
        kolom.setTextColor(0xFF35483B);
        kolom.setGravity(Gravity.CENTER_VERTICAL);
        kolom.setPadding(5, 12, 5, 12);
        kolom.setBackgroundColor(0xFFFFFFFF);

        return kolom;
    }

    // MENGHAPUS BARANG TERAKHIR
    private void hapusBarang() {

        if (keranjang.getDaftarBarang().isEmpty()) {

            Toast.makeText(
                    this,
                    "Keranjang masih kosong",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        int posisiTerakhir =
                keranjang.getDaftarBarang().size() - 1;

        keranjang.getDaftarBarang().remove(posisiTerakhir);

        perbaruiTampilan();

        Toast.makeText(
                this,
                "Barang terakhir berhasil dihapus",
                Toast.LENGTH_SHORT
        ).show();
    }

    // RESET SELURUH KERANJANG
    private void resetKeranjang() {

        keranjang.kosongkan();

        inputJumlah.setText("");

        posisiDipilih = 0;
        pilihan.setSelection(0);

        perbaruiTampilan();

        Toast.makeText(
                this,
                "Keranjang berhasil dikosongkan",
                Toast.LENGTH_SHORT
        ).show();
    }

    // MENAMPILKAN STRUK BELANJA
    private void tampilkanStruk() {

        if (keranjang.getDaftarBarang().isEmpty()) {

            Toast.makeText(
                    this,
                    "Keranjang masih kosong!",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        StringBuilder struk = new StringBuilder();

        struk.append("       TOKO ALAT TULIS\n");
        struk.append("============================\n");

        for (Barang barang : keranjang.getDaftarBarang()) {

            struk.append(barang.getNama())
                    .append("\n");

            struk.append(barang.getJumlah())
                    .append(" x ")
                    .append(rupiah.format(barang.getHarga()))
                    .append(" = ")
                    .append(rupiah.format(barang.hitungSubtotal()))
                    .append("\n\n");
        }

        int subtotal = keranjang.hitungSubtotal();

        int pajak = (int) Math.round(subtotal * PPN);

        int total = subtotal + pajak;

        struk.append("============================\n");

        struk.append("Total Belanja : ")
                .append(rupiah.format(subtotal))
                .append("\n");

        struk.append("PPN 11%       : ")
                .append(rupiah.format(pajak))
                .append("\n");

        struk.append("============================\n");

        struk.append("TOTAL BAYAR   : ")
                .append(rupiah.format(total))
                .append("\n");

        struk.append("============================\n");

        struk.append("     TERIMA KASIH!");

        // MENAMPILKAN STRUK
        new AlertDialog.Builder(this)
                .setTitle("Struk Belanja")
                .setMessage(struk.toString())
                .setPositiveButton("Tutup", null)
                .show();
    }
}
