package com.strukbelanja;

public class Barang {
    private final String nama;
    private final int harga;
    private final int jumlah;

    public Barang(String nama, int harga, int jumlah) {
        this.nama = nama;
        this.harga = harga;
        this.jumlah = jumlah;
    }
    public String getNama() { return nama; }
    public int getHarga() { return harga; }
    public int getJumlah() { return jumlah; }
    public int hitungSubtotal() { return harga * jumlah; }
}
