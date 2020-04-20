package com.example.a51external;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    public static final int REQUEST_CODE_PERMISSION_WRITE_STORAGE = 10;

    private Random random = new Random();

    private ItemsDataAdapter adapter;

    private List<Drawable> images = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        final ListView listView = findViewById(R.id.listView);

        int permissionStatus = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
            LoadTxt();  //работа с файлами
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_CODE_PERMISSION_WRITE_STORAGE);
        }

        setSupportActionBar(toolbar);

        fillImages();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generateRandomItemData();
            }
        });

        adapter = new ItemsDataAdapter(this, null);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showItemData(position);
            }
        });


        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                ItemData itemData = adapter.getItem(position);
                Toast.makeText(MainActivity.this,
                        "Title: " + itemData.getTitle() + "\n" +
                                "Subtitle: " + itemData.getSubtitle() + "\n" +
                                "Checked: " + itemData.isChecked(),
                        Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }
    public void LoadTxt() {
        if (isExternalStorageWriteble()) {
            File file = new File(getApplicationContext().getExternalFilesDir(null),"log.txt");
        }
    }

    public boolean isExternalStorageWriteble() {
        String state = Environment.getExternalStorageState();
        return Environment.DIRECTORY_PICTURES.equals(state);
    }

    public void onRequestPermissionResult (int requestCode, @NonNull String[] permission, @NonNull int[] grantResult){
        switch (requestCode) {
            case REQUEST_CODE_PERMISSION_WRITE_STORAGE:
                if (grantResult.length > 0
                        && grantResult[0] == PackageManager.PERMISSION_GRANTED) {
                    LoadTxt();  //работа с файлами
                }
        }
    }

    private void fillImages() {
        images.add(ContextCompat.getDrawable(MainActivity.this,
                android.R.drawable.ic_menu_report_image));
        images.add(ContextCompat.getDrawable(MainActivity.this,
                android.R.drawable.ic_menu_add));
        images.add(ContextCompat.getDrawable(MainActivity.this,
                android.R.drawable.ic_menu_agenda));
        images.add(ContextCompat.getDrawable(MainActivity.this,
                android.R.drawable.ic_menu_camera));
        images.add(ContextCompat.getDrawable(MainActivity.this,
                android.R.drawable.ic_menu_call));
    }

    private void generateRandomItemData() {
        adapter.addItem(new ItemData(
                images.get(random.nextInt(images.size())),
                "Hello" + adapter.getCount(),
                "It\'s me",
                random.nextBoolean()));
    }


    public void showItemData(int position) {
        ItemData itemData = adapter.getItem(position);
    }
}