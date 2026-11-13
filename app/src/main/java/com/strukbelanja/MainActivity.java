package com.strukbelanja;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import java.text.NumberFormat;
import java.util.Locale;

public class MainActivity extends Activity {
    private final String[] namaBarang = {"Buku", "Pulpen", "Penghapus", "Penggaris", "Pensil"};
    private final int[] hargaBarang = {5000, 3000, 2000, 4000, 2500};
    private final Keranjang keranjang = new Keranjang();
    private final double PPN = 0.11;
    private TextView hasil;
    private Spinner pilihan;
    private EditText inputJumlah;
    private final NumberFormat rupiah = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pilihan = findViewById(R.id.pilihanBarang);
        inputJumlah = findViewById(R.id.inputJumlah);
        hasil = findViewById(R.id.hasilStruk);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, namaBarang);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pilihan.setAdapter(adapter);

        findViewById(R.id.tambahButton).setOnClickListener(v -> tambahKeKeranjang());
        findViewById(R.id.hitungButton).setOnClickListener(v -> tampilkanStruk());
        findViewById(R.id.resetButton).setOnClickListener(v -> {
            keranjang.kosongkan();
            inputJumlah.setText("");
            hasil.setText("Keranjang dikosongkan.");
        });
    }

    private void tambahKeKeranjang() {
        String teksJumlah = inputJumlah.getText().toString().trim();
        if (teksJumlah.isEmpty()) {
            inputJumlah.setError("Masukkan jumlah barang");
            return;
        }
        int jumlah;
        try { jumlah = Integer.parseInt(teksJumlah); }
        catch (NumberFormatException e) { inputJumlah.setError("Jumlah harus berupa angka"); return; }
        if (jumlah <= 0) { inputJumlah.setError("Jumlah harus lebih dari 0"); return; }

        int posisi = pilihan.getSelectedItemPosition();
        keranjang.tambahBarang(new Barang(namaBarang[posisi], hargaBarang[posisi], jumlah));
        Toast.makeText(this, namaBarang[posisi] + " ditambahkan ke keranjang", Toast.LENGTH_SHORT).show();
        inputJumlah.setText("");
    }

    private void tampilkanStruk() {
        if (keranjang.getDaftarBarang().isEmpty()) {
            hasil.setText("Keranjang masih kosong. Tambahkan barang terlebih dahulu.");
            return;
        }
        StringBuilder struk = new StringBuilder("===== STRUK BELANJA =====\n");
        for (Barang barang : keranjang.getDaftarBarang()) {
            struk.append(barang.getNama()).append(" x ").append(barang.getJumlah())
                 .append(" = ").append(rupiah.format(barang.hitungSubtotal())).append("\n");
        }
        int subtotal = keranjang.hitungSubtotal();
        int pajak = (int) Math.round(subtotal * PPN);
        int total = subtotal + pajak;
        struk.append("-------------------------\nSubtotal: ").append(rupiah.format(subtotal))
             .append("\nPPN 11%: ").append(rupiah.format(pajak))
             .append("\nTOTAL: ").append(rupiah.format(total));
        hasil.setText(struk.toString());
    }
}
