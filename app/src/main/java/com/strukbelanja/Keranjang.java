package com.strukbelanja;

import java.util.ArrayList;
import java.util.List;

public class Keranjang {
    private final List<Barang> daftarBarang = new ArrayList<>();
    public void tambahBarang(Barang barang) { daftarBarang.add(barang); }
    public List<Barang> getDaftarBarang() { return daftarBarang; }
    public int hitungSubtotal() {
        int total = 0;
        for (Barang barang : daftarBarang) total += barang.hitungSubtotal();
        return total;
    }
    public void kosongkan() { daftarBarang.clear(); }
}
