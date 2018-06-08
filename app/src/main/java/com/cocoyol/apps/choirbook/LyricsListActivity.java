package com.cocoyol.apps.choirbook;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.widget.Toast;

import com.cocoyol.apps.choirbook.adapters.ElementAdapter;
import com.cocoyol.apps.choirbook.adapters.VerticalDividerItemDecoration;
import com.cocoyol.apps.choirbook.models.Index;
import com.cocoyol.apps.choirbook.models.Lyric;
import com.cocoyol.apps.choirbook.ui.MarqueeToolbar;
import com.cocoyol.apps.choirbook.utils.IndexFiles;
import com.cocoyol.apps.choirbook.utils.Permissions;
import com.cocoyol.apps.choirbook.utils.ReadWriteExternalStorage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Field;

import static com.cocoyol.apps.choirbook.Consts.APP_LYRICS_FOLDER;
import static com.cocoyol.apps.choirbook.Consts.WRITE_EXTERNAL_STORAGE_CODE;

public class LyricsListActivity extends AppCompatActivity {

    private RecyclerView recyclerViewSongs;
    private LinearLayoutManager layoutManager;
    private ElementAdapter lyricsAdapter;

    private boolean externalStoragePermissionGranted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lyrics_list);

        MarqueeToolbar toolbar = findViewById(R.id.genericToolbar);
        setSupportActionBar(toolbar);

        // - INICIALIZACIÓN -
        // Creación de ejemplos (temporal)
        cpExamples();

        // Cargar valores en el índice
        IndexFiles indexFiles = new IndexFiles(this);
        final Index index = indexFiles.getIndex();

        // - CARGAR VISTA (Recycler View) -
        recyclerViewSongs = findViewById(R.id.recyclerViewSongs);
        layoutManager = new LinearLayoutManager(this);
        lyricsAdapter = new ElementAdapter(index.getLyrics(), R.layout.item_linear_layout_recycler_view_song, this, new ElementAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Lyric song, int position) {
                //Toast.makeText(MainActivity.this, "Elemento clicado: " + position + " - " + song.getName(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LyricsListActivity.this, LyricActivity.class);
                intent.putExtra("name", song.getName())
                        .putExtra("fullFileName", song.getFile());
                startActivity(intent);
            }
        });

        recyclerViewSongs.setLayoutManager(layoutManager);
        recyclerViewSongs.setAdapter(lyricsAdapter);

        VerticalDividerItemDecoration verticalDividerItemDecoration = new VerticalDividerItemDecoration(this, (int) getResources().getDimension(R.dimen.song_list_item_icon_width), (int) getResources().getDimension(R.dimen.song_list_item_icon_margin_end));
        recyclerViewSongs.addItemDecoration(verticalDividerItemDecoration);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_lyrics_list, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void cpExamples() {

        File dir = new File(getFilesDir() + File.separator + "songs");
        if(!dir.isDirectory()) {
            try {
                dir.mkdirs();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (dir.list().length < 1) {

            Field[] fields = R.raw.class.getFields();
            if (fields.length > 0) {
                for (Field field : fields) {

                    try {
                        InputStream inputStream = getResources().openRawResource(field.getInt(field));
                        byte[] inputBuffer = new byte[inputStream.available()];

                        inputStream.read(inputBuffer);

                        File target = new File(getFilesDir() + File.separator + "songs" + File.separator + field.getName().replaceAll("_", " ") + ".txt");

                        FileOutputStream outStream = new FileOutputStream(target);
                        outStream.write(inputBuffer);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                Toast.makeText(this, "Archivos de ejemplo generados.", Toast.LENGTH_SHORT).show();
            }
        }

        ReadWriteExternalStorage readWriteExternalStorage = new ReadWriteExternalStorage(this);
        Permissions permissions = new Permissions(this, this);
        permissions.askForPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE_CODE);
    }

    private void generateDirectory() {
        ReadWriteExternalStorage readWriteExternalStorage = new ReadWriteExternalStorage(this);

        if (!readWriteExternalStorage.isExternalStorageWritable() || !readWriteExternalStorage.isExternalStorageReadable()) {
            Toast.makeText(this, getString(R.string.text_storage_not_accessible), Toast.LENGTH_LONG).show();
        } else if (!readWriteExternalStorage.isDirectory(APP_LYRICS_FOLDER)) {
            if (!readWriteExternalStorage.createDirectory(APP_LYRICS_FOLDER)) {
                Toast.makeText(this, getString(R.string.text_files_not_be_created), Toast.LENGTH_LONG).show();
            }
        } else {
            boolean newFile = false;
            Field[] fields = R.raw.class.getFields();
            if (fields.length > 0 && readWriteExternalStorage.isEmptyDirectory(APP_LYRICS_FOLDER)) {
                for (Field field : fields) {
                    String filename = field.getName().replaceAll("_", " ") + ".txt";
                    try {
                        InputStream inputStream = getResources().openRawResource(field.getInt(field));
                        byte[] inputBuffer = new byte[inputStream.available()];

                        inputStream.read(inputBuffer);
                        if(!readWriteExternalStorage.exists(APP_LYRICS_FOLDER + APP_LYRICS_FOLDER + File.separator + filename)) {
                            readWriteExternalStorage.writeToTextFile(inputBuffer, APP_LYRICS_FOLDER + File.separator + filename);
                            newFile = true;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                if(newFile)
                    Toast.makeText(this, "Example files was generated.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case WRITE_EXTERNAL_STORAGE_CODE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Toast.makeText(this, "HECHO!!!!!", Toast.LENGTH_SHORT).show();
                    generateDirectory();
                } else {
                    Toast.makeText(this, getText(R.string.text_permission_denied), Toast.LENGTH_LONG).show();
                }
        }
    }
}
