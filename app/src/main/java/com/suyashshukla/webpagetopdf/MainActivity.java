package com.suyashshukla.webpagetopdf;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.print.PrintManager;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    WebView wb, print;
    Button btn;
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        wb = findViewById(R.id.file);
        btn = findViewById(R.id.ctpdf);
        drawerLayout = findViewById(R.id.drawer);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        wb.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView v, String url) {
                super.onPageFinished(v, url);
                print = wb;
            }
        });
        wb.loadUrl("https://www.google.com/");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (print != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        // Calling createWebPrintJob()
                        PrintTheWebPage(print);
                    }else {
                        // Showing Toast message to user
                        Toast.makeText(MainActivity.this, "Not available for device below Android LOLLIPOP", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Showing Toast message to user
                    Toast.makeText(MainActivity.this, "WebPage not fully loaded", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }
    @Override
            public boolean onOptionsItemSelected(@NonNull MenuItem Item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(Item)) {
            return true;
        }
    return super.onOptionsItemSelected(Item);
    }
    PrintJob printJob;
    boolean printbtnpressed=false;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void PrintTheWebPage(WebView print) {
        printbtnpressed=true;
        PrintManager printManager=(PrintManager)
                this.getSystemService(Context.PRINT_SERVICE);
        String jobName = getString(R.string.app_name) + "webpage" + wb.getUrl();
        PrintDocumentAdapter printAdapter = wb.createPrintDocumentAdapter(jobName);
        assert printManager != null;
        printJob = printManager.print(jobName, printAdapter,
                new PrintAttributes.Builder().build());
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (printJob != null && printbtnpressed) {
            if (printJob.isCompleted()) {

                Toast.makeText(this, "Completed", Toast.LENGTH_SHORT).show();
            } else if (printJob.isStarted()) {

                Toast.makeText(this, "process started", Toast.LENGTH_SHORT).show();

            } else if (printJob.isBlocked()) {

                Toast.makeText(this, "isBlocked", Toast.LENGTH_SHORT).show();

            } else if (printJob.isCancelled()) {

                Toast.makeText(this, "process cancelled", Toast.LENGTH_SHORT).show();

            } else if (printJob.isFailed()) {

                Toast.makeText(this, "process failed", Toast.LENGTH_SHORT).show();

            } else if (printJob.isQueued()) {

                Toast.makeText(this, "isQueued", Toast.LENGTH_SHORT).show();

            }
            // set printBtnPressed false
            printbtnpressed = false;
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (wb.canGoBack()) {
                        wb.goBack();
                    } else {
                        finish();
                    }
                    return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }
}