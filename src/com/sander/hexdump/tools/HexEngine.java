package com.sander.hexdump.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class HexEngine {
    public interface HexCallback {
        void onResult(String hexOutput);
        void onError(String error);
    }

    public void readFile(final String filePath, final HexCallback cb) {
        new Thread(() -> {
            try {
                File file = new File(filePath);
                if (!file.exists()) { cb.onError("File not found."); return; }

                int maxBytes = 2048; // Baca 2KB pertama
                byte[] buffer = new byte[maxBytes];
                InputStream is = new FileInputStream(file);
                int bytesRead = is.read(buffer);
                is.close();

                if (bytesRead <= 0) { cb.onError("File is empty."); return; }

                StringBuilder out = new StringBuilder();
                
                // Magic Bytes Sederhana (Cek 4 byte awal)
                String magic = "";
                for(int k=0; k<Math.min(4, bytesRead); k++) magic += String.format("%02X", buffer[k]);
                out.append("> Signature: ").append(magic).append(" \n\n");

                out.append(String.format("%-8s %-48s %s\n", "OFFSET", "HEX", "ASCII"));
                out.append("-----------------------------------------------------------------\n");

                for (int i = 0; i < bytesRead; i += 16) {
                    out.append(String.format("%08X ", i)); // Offset
                    
                    StringBuilder ascii = new StringBuilder();
                    int lineLen = Math.min(16, bytesRead - i);

                    for (int j = 0; j < 16; j++) {
                        if (j < lineLen) {
                            byte b = buffer[i + j];
                            out.append(String.format("%02X ", b)); // Hex
                            ascii.append((b >= 32 && b <= 126) ? (char) b : '.'); // ASCII
                        } else {
                            out.append("   ");
                        }
                    }
                    out.append(" ").append(ascii.toString()).append("\n");
                }
                cb.onResult(out.toString());

            } catch (Exception e) { cb.onError(e.getMessage()); }
        }).start();
    }
}
