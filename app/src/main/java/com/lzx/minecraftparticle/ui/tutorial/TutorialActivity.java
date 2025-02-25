package com.lzx.minecraftparticle.ui.tutorial;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.appbar.MaterialToolbar;
import com.lzx.minecraftparticle.databinding.ActivityTutorialBinding;
import com.pdfview.PDFView;


public class TutorialActivity extends AppCompatActivity{
    private ActivityTutorialBinding binding;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTutorialBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        //toolbar
        MaterialToolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        
//        PDFView pdfView = binding.pdfView;
//        pdfView.fromAsset("153.pdf")
//                .pages(0, 1, 2)
//                .defaultPage(1)
//                .enableSwipe(true)
//                .swipeHorizontal(true)
//                .enableDoubletap(true)
//                .load();
        
        //PDF
        PDFView pdfView = binding.pdfView;
        pdfView.fromAsset("软件教程.pdf").show();
        Toast.makeText(this, "请等待PDF文件的加载", Toast.LENGTH_SHORT).show();
        
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId(); 
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return false;
    }
}


