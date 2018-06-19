package com.cocoyol.apps.choirbook;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.SeekBar;
import android.widget.Toast;

import com.cocoyol.apps.choirbook.ui.MarqueeToolbar;
import com.cocoyol.apps.choirbook.utils.Helpers;
import com.cocoyol.apps.choirbook.utils.ReadWriteExternalStorage;

public class LyricActivity extends AppCompatActivity {

    private MarqueeToolbar toolbar;
    private WebView webViewLyric;
    private WebSettings webSettings;

    private Bundle lyricBundle;

    private String lyric;
    private String htmlLyric;

    private int theme;
    private int defaultFontSizeIdx;
    public boolean invertColor;
    public boolean invertedColor;

    @SuppressLint("StringFormatInvalid")
    private String readFile(String path) {
        String s = "";
        if(path != null && !path.isEmpty()){
            ReadWriteExternalStorage readWriteExternalStorage = new ReadWriteExternalStorage(this);
            if(readWriteExternalStorage.isExternalStorageAccessible()) {
                if(readWriteExternalStorage.exists(path)) {
                    String tmp = readWriteExternalStorage.readFromTextFile(path);
                    if (tmp != null) s = tmp.replace("\u0000", "");
                } else {
                    Toast.makeText(this, String.format(getString(R.string.text_file_does_not_exist), path), Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, getString(R.string.text_storage_not_accessible), Toast.LENGTH_SHORT).show();
            }
        }
        return s;
    }

    private void normalizeFileContent(String path) {
        lyric = readFile(path);
        if(!lyric.isEmpty()) {
            lyric = Helpers.normalizeText(lyric);
            htmlLyric = Html.toHtml(new SpannableString(lyric));
        }
    }

    public void restoreInstanceStateVariables(Bundle savedInstanceState) {
        invertColor = false;
        invertedColor = false;
        defaultFontSizeIdx = 0;     // (0) or saved in Shared Preferences
        if(savedInstanceState != null) {
            invertColor = savedInstanceState.containsKey("invertColor") && savedInstanceState.getBoolean("invertColor");
            invertedColor = savedInstanceState.containsKey("invertedColor") && savedInstanceState.getBoolean("invertedColor");
            defaultFontSizeIdx = savedInstanceState.containsKey("fontSizeIdx") ? savedInstanceState.getInt("fontSizeIdx") : defaultFontSizeIdx;
        }
    }

    private void toggleActivityTheme() {
        if(invertColor && !invertedColor) {
            theme = R.style.AppThemeDark;
            invertedColor = true;
        } else {
            theme = R.style.AppTheme;
            invertedColor = false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Restore instances when change Theme
        restoreInstanceStateVariables(savedInstanceState);

        // Set themes
        toggleActivityTheme();
        setTheme(theme);
        setContentView(R.layout.activity_lyric);

        // Setup Toolbar
        toolbar = findViewById(R.id.genericToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Setup WebView
        webViewLyric = findViewById(R.id.webViewLyric);
        webViewLyric.setBackgroundColor(Color.TRANSPARENT);
        webViewLyric.setWebChromeClient(new WebChromeClient());
        webSettings = webViewLyric.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDefaultFontSize(selectDefaultFontSize(defaultFontSizeIdx));

        // Show Activity content
        lyricBundle = getIntent().getExtras();
        if(lyricBundle != null) {
            String fullFileName = lyricBundle.getString("fullFileName");
            getSupportActionBar().setTitle(lyricBundle.getString("name"));

            normalizeFileContent(fullFileName);
            displayFileContent(savedInstanceState);
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        webViewLyric.saveState(outState);

        outState.putInt("fontSizeIdx", defaultFontSizeIdx);
        outState.putBoolean("invertColor", invertColor);
        outState.putBoolean("invertedColor", invertedColor);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }

    SearchView searchView;
    String textQuery = "";
    Button navNextFindButton;
    Button navPrevFindButton;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_lyric, menu);

        searchView = (SearchView) menu.findItem(R.id.itemSearch).getActionView();
        searchView.setIconifiedByDefault(true);
        searchView.setMaxWidth(Integer.MAX_VALUE);

        generateFindNavigationButtons();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                textQuery = newText;
                if(!textQuery.isEmpty()) {
                    webViewLyric.findAllAsync(textQuery);
                    toggleNavButtonsColor(true);
                } else {
                    webViewLyric.clearMatches();
                    toggleNavButtonsColor(false);
                }
                return false;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                webViewLyric.clearMatches();
                toggleNavButtonsColor(false);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void generateFindNavigationButtons() {
        Drawable drawable;

        navNextFindButton = new Button(this);
        drawable = getResources().getDrawable(R.drawable.ic_arrow_down);
        navNextFindButton.setBackground(drawable);
        navNextFindButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!textQuery.isEmpty()) {
                    webViewLyric.findNext(true);
                }
            }
        });
        navNextFindButton.setVisibility(View.INVISIBLE);

        navPrevFindButton = new Button(this);
        drawable = getResources().getDrawable(R.drawable.ic_arrow_up);
        navPrevFindButton.setBackground(drawable);
        navPrevFindButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!textQuery.isEmpty()) {
                    webViewLyric.findNext(false);
                }
            }
        });
        navPrevFindButton.setVisibility(View.INVISIBLE);

        int id = searchView.getContext().getResources().getIdentifier("android:id/search_plate", null, null);
        LinearLayout linearLayout = searchView.findViewById(id);
        linearLayout.setGravity(Gravity.BOTTOM);
        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(toolbar.getHeight() * 2 / 3, toolbar.getHeight() * 2 / 3);
        linearLayout.addView(navPrevFindButton, 1, buttonParams);
        linearLayout.addView(navNextFindButton, 1, buttonParams);

    }

    private void toggleNavButtonsColor(Boolean b) {
        if(b) {
            navPrevFindButton.setVisibility(View.VISIBLE);
            navNextFindButton.setVisibility(View.VISIBLE);
        } else {
            navPrevFindButton.setVisibility(View.INVISIBLE);
            navNextFindButton.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itemFontSize:
                showFontSizeDialog();
                return true;
            case R.id.itemInvertColor:

                invertColor = true;
                //fontSizeIdx = defaultFontSizeIdx;
                recreate();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showFontSizeDialog() {
        AlertDialog.Builder fontSizeDialogBuilder = new AlertDialog.Builder(this);
        View fontSizeDialogPanel = LayoutInflater.from(this).inflate(R.layout.text_size_panel, null);
        fontSizeDialogBuilder.setTitle(getResources().getString(R.string.text_adjust_text_size));
        fontSizeDialogBuilder.setView(fontSizeDialogPanel);

        final SeekBar seekBarTextSize = fontSizeDialogPanel.findViewById(R.id.seekBarTextSize);
        seekBarTextSize.setProgress(defaultFontSizeIdx);

        seekBarTextSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                webSettings.setDefaultFontSize(selectDefaultFontSize(progress));
                defaultFontSizeIdx = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        fontSizeDialogBuilder.setPositiveButton(getResources().getString(R.string.text_finish), null);
        fontSizeDialogBuilder.create().show();
    }

    private int selectDefaultFontSize(int idx){
        int sz;
        switch (idx) {
            case 0:
                sz = (int) Helpers.getDpFromPx(getResources().getDimension(R.dimen.lyric_text_size_small), getBaseContext());
                break;
            case 1:
                sz = (int) Helpers.getDpFromPx(getResources().getDimension(R.dimen.lyric_text_size_standard), getBaseContext());
                break;
            case 2:
                sz = (int) Helpers.getDpFromPx(getResources().getDimension(R.dimen.lyric_text_size_large), getBaseContext());
                break;
            default:
                sz = (int) Helpers.getDpFromPx(getResources().getDimension(R.dimen.lyric_text_size_small), getBaseContext());
                break;
        }
        return sz;
    }

    private void displayFileContent(Bundle savedInstanceState) {
        if(savedInstanceState != null)
            webViewLyric.restoreState(savedInstanceState);
        else
            webViewLyric.loadData(htmlLyric, "text/html; charset=utf-8", "UTF-8");

        if(invertedColor) {
            webViewLyric.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    view.evaluateJavascript("var css = 'html {-webkit-filter: invert(100%);' , head = document.getElementsByTagName('head')[0], style = document.createElement('style'); " +
                            "if (!window.counter) { window.counter = 1; } else { window.counter ++; if (window.counter % 2 == 0) { var css = 'html {-webkit-filter: invert(0%); }'; } }; " +
                            "style.type = 'text/css'; if (style.styleSheet){ style.styleSheet.cssText = css; } else { style.appendChild(document.createTextNode(css)); } head.appendChild(style);", null);
                }
            });
        }

    }

    // ** INMERSIVE MODE **
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    private void showSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

}
