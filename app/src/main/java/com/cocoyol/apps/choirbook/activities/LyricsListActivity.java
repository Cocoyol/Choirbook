package com.cocoyol.apps.choirbook.activities;

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

import com.cocoyol.apps.choirbook.R;
import com.cocoyol.apps.choirbook.adapters.ElementAdapter;
import com.cocoyol.apps.choirbook.adapters.VerticalDividerItemDecoration;
import com.cocoyol.apps.choirbook.models.Index;
import com.cocoyol.apps.choirbook.models.Lyric;
import com.cocoyol.apps.choirbook.ui.MarqueeToolbar;
import com.cocoyol.apps.choirbook.utils.IndexFiles;
import com.cocoyol.apps.choirbook.utils.Permissions;
import com.cocoyol.apps.choirbook.utils.ReadWriteExternalStorage;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;

import in.myinnos.alphabetsindexfastscrollrecycler.IndexFastScrollRecyclerView;

import static com.cocoyol.apps.choirbook.Consts.APP_LYRICS_FOLDER;
import static com.cocoyol.apps.choirbook.Consts.WRITE_EXTERNAL_STORAGE_CODE;

public class LyricsListActivity extends AppCompatActivity {

    //private RecyclerView recyclerViewSongs;
    private IndexFastScrollRecyclerView indexFastScrollRecyclerView;
    private LinearLayoutManager layoutManager;
    //private ElementAdapter_x lyricsAdapter;
    private ElementAdapter lyricsAdapter;

    private Bundle indexBundle;

    private boolean externalStoragePermissionGranted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lyrics_list);

        MarqueeToolbar toolbar = findViewById(R.id.genericToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.title_activity_all_lyrics_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setupDefaultLyrics();
    }

    private void setupDefaultLyrics() {
        Permissions permissions = new Permissions(this, this);
        permissions.askForPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE_CODE);
    }

    private void generateDirectory() {
        ReadWriteExternalStorage readWriteExternalStorage = new ReadWriteExternalStorage(this);

        if (readWriteExternalStorage.isExternalStorageWritable() && readWriteExternalStorage.isExternalStorageReadable()) {
            if (!readWriteExternalStorage.isDirectory(APP_LYRICS_FOLDER)) {
                if (!readWriteExternalStorage.createDirectory(APP_LYRICS_FOLDER)) {
                    Toast.makeText(this, getString(R.string.text_files_not_be_created), Toast.LENGTH_LONG).show();
                    return;
                }
            }

            boolean newFile = false;
            Field[] fields = R.raw.class.getFields();
            if (fields.length > 0 && readWriteExternalStorage.isEmptyDirectory(APP_LYRICS_FOLDER)) {
                for (Field field : fields) {
                    String filename = field.getName().replaceAll("_", " ") + ".txt";
                    try {
                        InputStream inputStream = getResources().openRawResource(field.getInt(field));
                        byte[] inputBuffer = new byte[inputStream.available()];

                        inputStream.read(inputBuffer);
                        if(!readWriteExternalStorage.exists(APP_LYRICS_FOLDER + File.separator + filename)) {
                            readWriteExternalStorage.writeToTextFile(inputBuffer, APP_LYRICS_FOLDER + File.separator + filename);
                            newFile = true;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                if(newFile)
                    Toast.makeText(this, "Example files was successfully generated.", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, getString(R.string.text_storage_not_accessible), Toast.LENGTH_LONG).show();
        }
    }

    private void loadAndShowIndex() {
        IndexFiles indexFiles = new IndexFiles(this);
        indexFiles.initializeIndex();
        final Index index = indexFiles.getIndex();

        // - LOAD VIEW (Recycler View) -
        indexFastScrollRecyclerView = findViewById(R.id.fast_scroller_recycler);
        configureIndexFastScrollRecyclerView();
        layoutManager = new LinearLayoutManager(this);
        lyricsAdapter = new ElementAdapter(this, index.getLyrics(), index.sectionsIndex, R.layout.item_linear_layout_recycler_view_song, this, new ElementAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Lyric song, int position) {
                Intent intent = new Intent(LyricsListActivity.this, LyricActivity.class);
                intent.putExtra("name", song.getName())
                        .putExtra("fullFileName", song.getFile());
                startActivity(intent);
            }
        });

        indexFastScrollRecyclerView.setLayoutManager(layoutManager);
        indexFastScrollRecyclerView.setAdapter(lyricsAdapter);

        VerticalDividerItemDecoration verticalDividerItemDecoration = new VerticalDividerItemDecoration(
                this,
                (int) (getResources().getDimension(R.dimen.song_list_section_title_text_view_size) + getResources().getDimension(R.dimen.song_list_item_icon_width) + 4 * getResources().getDimension(R.dimen.song_list_item_icon_margin_end)),
                (int) getResources().getDimension(R.dimen.song_list_item_icon_margin_end)
        );
        indexFastScrollRecyclerView.addItemDecoration(verticalDividerItemDecoration);
    }

    private void configureIndexFastScrollRecyclerView() {
        indexFastScrollRecyclerView.setIndexBarColor(R.color.colorIcons);
        indexFastScrollRecyclerView.setIndexBarTextColor(R.color.colorSecondaryText);
        indexFastScrollRecyclerView.setIndexbarHighLateTextColor(R.color.colorPrimaryText);
        indexFastScrollRecyclerView.setIndexBarHighLateTextVisibility(true);
        indexFastScrollRecyclerView.setIndexbarMargin(0);
        indexFastScrollRecyclerView.setIndexBarCornerRadius(0);
        //indexFastScrollRecyclerView.setIndexbarWidth(-getResources().getDimension(R.dimen.song_list_index_width));
    }

    /* *****    OVERRIDES   ***** */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case WRITE_EXTERNAL_STORAGE_CODE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    generateDirectory();
                    loadAndShowIndex();
                } else {
                    Toast.makeText(this, getText(R.string.text_permission_denied), Toast.LENGTH_LONG).show();
                }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_lyrics_list, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
