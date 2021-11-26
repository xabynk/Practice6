package ru.mirea.yudin.notebook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private EditText filepath;
    private EditText et;
    private String fileName;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preferences = getPreferences(MODE_PRIVATE);
        et = findViewById(R.id.editText);
        filepath = findViewById(R.id.editTextFile);
        if (!preferences.getString("PATH", "none").equals("none")) {
            fileName = preferences.getString("PATH", "none");
            filepath.setText(fileName);
            new Thread(new Runnable() {
                public void run() {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    et.post(new Runnable() {
                        public void run() {
                            et.setText(getTextFromFile());
                        }
                    });
                }
            }).start();
        }
    }

    public String getTextFromFile() {
        FileInputStream fin = null;
        try {
            fin = openFileInput(fileName);
            byte[] bytes = new byte[fin.available()];
            fin.read(bytes);
            String text = new String(bytes);
            Log.d(LOG_TAG, text);
            return text;
        } catch (
                IOException ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            try {
                if (fin != null)
                    fin.close();
            } catch (IOException ex) {
                Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        return null;
    }

    public void onSave(View v) {
        SharedPreferences.Editor editor = preferences.edit();
        fileName = filepath.getText().toString();
        editor.putString("PATH", fileName);
        editor.apply();
        FileOutputStream outputStream;

        try {
            outputStream = openFileOutput(fileName, Context.MODE_PRIVATE);
            outputStream.write(et.getText().toString().getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Toast.makeText(this, "Notebook saved", Toast.LENGTH_SHORT).show();
    }
}