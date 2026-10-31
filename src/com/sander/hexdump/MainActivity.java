package com.sander.hexdump;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Button;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.InputType;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.RelativeSizeSpan;
import android.content.ClipboardManager;
import android.content.ClipData;
import android.content.Context;
import android.widget.Toast;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class MainActivity extends Activity {
    
    TextView tvTerminal;
    ScrollView terminalScroll;
    EditText inputTerminal;
    Handler handler = new Handler(Looper.getMainLooper());

    // PALET WARNA
    int colorOffset = Color.parseColor("#ADD8E6"); // Biru Muda
    int colorHex = Color.parseColor("#FFFFFF");    // Putih
    int colorAsciiPr = Color.parseColor("#FFFFFF");// Putih
    int colorAsciiNp = Color.parseColor("#808080");// Abu-abu
    int colorRed = Color.parseColor("#FF0000");
    int colorCyan = Color.parseColor("#00FFFF");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        LinearLayout mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.setBackgroundColor(Color.BLACK);
        
        // --- TERMINAL ---
        tvTerminal = new TextView(this);
        tvTerminal.setTypeface(Typeface.create(Typeface.MONOSPACE, Typeface.BOLD)); 
        tvTerminal.setTextColor(colorHex); 
        tvTerminal.setTextSize(10); 
        tvTerminal.setPadding(10, 10, 10, 10);
        tvTerminal.setTextIsSelectable(true); 
        
        terminalScroll = new ScrollView(this);
        terminalScroll.addView(tvTerminal);
        
        // --- TOMBOL COPY ---
        Button btnCopy = new Button(this);
        btnCopy.setText("COPY ALL LOG");
        btnCopy.setTextSize(8);
        btnCopy.setTypeface(Typeface.MONOSPACE);
        btnCopy.setBackgroundColor(Color.parseColor("#222222"));
        btnCopy.setTextColor(Color.WHITE);
        btnCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("HexDump", tvTerminal.getText().toString());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(MainActivity.this, "Log Copied!", Toast.LENGTH_SHORT).show();
            }
        });

        // --- INPUT COMMAND ---
        inputTerminal = new EditText(this);
        inputTerminal.setHint("$ command...");
        inputTerminal.setTypeface(Typeface.MONOSPACE);
        inputTerminal.setTextColor(colorCyan);
        inputTerminal.setHintTextColor(Color.GRAY);
        inputTerminal.setBackgroundColor(Color.parseColor("#111111"));
        inputTerminal.setTextSize(11); 
        inputTerminal.setInputType(InputType.TYPE_CLASS_TEXT);
        
        inputTerminal.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, android.view.KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    String cmd = inputTerminal.getText().toString().trim();
                    if (!cmd.isEmpty()) { executeCommand(cmd); inputTerminal.setText(""); }
                    return true;
                }
                return false;
            }
        });

        mainLayout.addView(terminalScroll, new LinearLayout.LayoutParams(-1, 0, 1.0f));
        mainLayout.addView(btnCopy, new LinearLayout.LayoutParams(-1, -2));
        mainLayout.addView(inputTerminal, new LinearLayout.LayoutParams(-1, -2));
        setContentView(mainLayout);
        
        // --- HEADER RAPI (ANTI WRAP) ---
        appendLog("=========================================", colorCyan);
        appendLog("      SANDERHEXDUMP v1.0 (STABLE)          ", colorCyan);
        appendLog("   Native Hex Editor & Reverse Tool      ", colorCyan);
        appendLog("=========================================\n", colorCyan);
        
        appendLog("Type 'help' for commands.\n", colorHex);
    }

    private void appendLog(final String text, final int color) {
        handler.post(new Runnable() {
            @Override public void run() {
                SpannableStringBuilder sb = new SpannableStringBuilder(text);
                sb.setSpan(new ForegroundColorSpan(color), 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                tvTerminal.append(sb);
                tvTerminal.append("\n");
                terminalScroll.post(new Runnable() { @Override public void run() { terminalScroll.fullScroll(View.FOCUS_DOWN); } });
            }
        });
    }

    private void appendHexLog(final SpannableStringBuilder sb) {
        handler.post(new Runnable() {
            @Override public void run() {
                tvTerminal.append(sb);
                tvTerminal.append("\n");
                terminalScroll.post(new Runnable() { @Override public void run() { terminalScroll.fullScroll(View.FOCUS_DOWN); } });
            }
        });
    }

    private void executeCommand(String cmdLine) {
        String[] parts = cmdLine.split("\\s+");
        String cmd = parts[0].toLowerCase();
        
        // PROMPT GEDE & BOLD
        SpannableStringBuilder cmdSb = new SpannableStringBuilder();
        String prompt = "root@sanderhexdump:~# ";
        cmdSb.append(prompt);
        cmdSb.setSpan(new ForegroundColorSpan(colorCyan), 0, prompt.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        cmdSb.setSpan(new RelativeSizeSpan(1.2f), 0, prompt.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        
        int startInput = cmdSb.length();
        cmdSb.append(cmdLine);
        cmdSb.setSpan(new ForegroundColorSpan(Color.WHITE), startInput, cmdSb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        cmdSb.setSpan(new StyleSpan(Typeface.BOLD), startInput, cmdSb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        cmdSb.setSpan(new RelativeSizeSpan(1.1f), startInput, cmdSb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        appendHexLog(cmdSb);

        if (cmd.equals("help")) {
            appendLog("- clear\n- hex <path_file>", colorHex);
        } else if (cmd.equals("clear")) {
            tvTerminal.setText("root@sanderhexdump:~# \n");
        } else if (cmd.equals("hex") && parts.length > 1) {
            readHexFile(parts[1]);
        } else {
            appendLog("sh: " + cmd + ": command not found", colorRed);
        }
    }

    private void readHexFile(final String filePath) {
        new Thread(new Runnable() {
            @Override public void run() {
                try {
                    File file = new File(filePath);
                    if (!file.exists()) { appendLog("[!] File not found.", colorRed); return; }

                    byte[] buffer = new byte[1024];
                    InputStream is = new FileInputStream(file);
                    int bytesRead = is.read(buffer);
                    is.close();

                    if (bytesRead <= 0) { appendLog("[!] File is empty.", colorRed); return; }

                    SpannableStringBuilder sb = new SpannableStringBuilder();
                    
                    // HEADER
                    String header = "OFFSET    HEX                 | ASCII\n";
                    sb.append(header);
                    sb.setSpan(new ForegroundColorSpan(colorAsciiNp), 0, header.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    
                    // GARIS PEMISAH
                    String line = "------------------------------|----------------\n";
                    sb.append(line);
                    sb.setSpan(new ForegroundColorSpan(Color.parseColor("#333333")), header.length(), header.length() + line.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                    for (int i = 0; i < bytesRead; i += 12) {
                        int startOffset = sb.length();
                        
                        // 1. OFFSET (Biru)
                        String offsetStr = String.format("%06X   ", i);
                        sb.append(offsetStr);
                        sb.setSpan(new ForegroundColorSpan(colorOffset), startOffset, startOffset + offsetStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        
                        // 2. HEX (Putih)
                        StringBuilder ascii = new StringBuilder();
                        int lineLen = Math.min(12, bytesRead - i);
                        int hexStart = sb.length();

                        for (int j = 0; j < 12; j++) {
                            if (j < lineLen) {
                                byte b = buffer[i + j];
                                sb.append(String.format("%02X ", b)); 
                                ascii.append((b >= 32 && b <= 126) ? (char) b : '.'); 
                            } else {
                                sb.append("   "); 
                            }
                        }
                        sb.setSpan(new ForegroundColorSpan(colorHex), hexStart, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                        // 3. PIPA PEMISAH
                        sb.append("| ");
                        
                        // 4. ASCII (Putih/Abu)
                        for (int k = 0; k < ascii.length(); k++) {
                            char c = ascii.charAt(k);
                            int cStart = sb.length();
                            sb.append(c);
                            if (c == '.') {
                                sb.setSpan(new ForegroundColorSpan(colorAsciiNp), cStart, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            } else {
                                sb.setSpan(new ForegroundColorSpan(colorAsciiPr), cStart, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            }
                        }
                        
                        sb.append("\n\n"); // Jeda baris
                    }
                    
                    appendHexLog(sb);

                } catch (Exception e) { 
                    final String err = e.getMessage();
                    handler.post(new Runnable() {
                        @Override public void run() { appendLog("[!] Error: " + err, colorRed); }
                    });
                }
            }
        }).start();
    }
}
