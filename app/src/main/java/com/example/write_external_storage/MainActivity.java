package com.example.write_external_storage;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private final Executor executor = Executors.newSingleThreadExecutor();

    private TextView messageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        messageView = findViewById(R.id.message);
        Button button = findViewById(R.id.button);
        button.setOnClickListener(v -> doProgress());
    }

    private void doProgress() {
        executor.execute(() -> {
            clearMessage();

            addMessage("Creating temp file on external storage");
            File file = null;
            try {
                File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
                dir.mkdirs();
                file = File.createTempFile("test-2", ".txt", dir);
                addMessage("Success");
            } catch (Throwable e) {
                addMessage(Log.getStackTraceString(e));
            }
            if (file == null) {
                return;
            }
//            Uri uri = null;
//            try {
//                Uri dir = MediaStore.Files.getContentUri(null);
//                uri = Uri.withAppendedPath(dir, "test.txt");
//                addMessage("Success");
//            } catch (Throwable e) {
//                addMessage(Log.getStackTraceString(e));
//            }
            addMessage("Writing something to created file");
            try (Writer writer = new OutputStreamWriter(new FileOutputStream(file))) {
                writer.write("This is some content");
                addMessage("Success");
            } catch (Throwable e) {
                addMessage(Log.getStackTraceString(e));
            }
        });
    }

    private void clearMessage() {
        runOnUiThread(() -> {
            messageView.setText("");
        });
    }

    private void addMessage(String text) {
        runOnUiThread(() -> {
            StringBuilder builder = new StringBuilder();
            if (messageView.getText() != null) {
                builder.append(messageView.getText());
                builder.append("\n");
            }
            builder.append(text);
            builder.append("\n---");
            messageView.setText(builder);
        });
    }
}