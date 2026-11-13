# Struk Belanja Android

Project Android native menggunakan Java dan XML.

## Isi aplikasi
- Menu tetap: Buku, Pulpen, Penghapus, Penggaris, Pensil.
- Pengguna dapat menambahkan barang dan jumlah berkali-kali.
- Menghitung subtotal, PPN 11%, dan total.
- Tombol untuk menampilkan struk dan mengosongkan keranjang.

## Build APK melalui GitHub Actions
1. Unggah seluruh isi folder ini ke repository GitHub.
2. Buka tab Actions.
3. Pilih workflow Build APK dan jalankan, atau push ke branch main.
4. Setelah selesai, buka hasil run dan unduh artifact `StrukBelanja-debug`.
5. Ekstrak artifact untuk mendapatkan `app-debug.apk`.

Catatan: project ini belum diuji dengan build lokal. Jika workflow gagal, kirimkan log error agar dapat diperbaiki.
