package com.lzx.minecraftparticle.ui.view;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
import com.lzx.minecraftparticle.R;


public class ParticleColumnView extends LinearLayout{
    public ParticleColumnView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //获取属性
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ParticleColumnView);
        String type = a.getString(R.styleable.ParticleColumnView_pcvType);
        String description = a.getString(R.styleable.ParticleColumnView_pcvDescription);
        int img = a.getResourceId(R.styleable.ParticleColumnView_pcvImg, R.drawable.logo);
        a.recycle();
        
        //视图初始化
        LayoutInflater.from(context).inflate(R.layout.particle_column, this, true);
        ShapeableImageView mainImageView = findViewById(R.id.mainImageView);
        MaterialTextView typeTextView = findViewById(R.id.typeTextView);
        MaterialTextView descriptionTextView = findViewById(R.id.descriptionTextView);
        mainImageView.setImageResource(img);
        typeTextView.setText(type);
        descriptionTextView.setText(description);
    }
    
//    //设置recyclerview
//    public void setRecyclerView(FragmentActivity fa, Save.Type[] type) {
//        RecyclerView recyclerView = findViewById(R.id.recyclerView);
//        recyclerView.setNestedScrollingEnabled(false);
//        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
//        recyclerView.setAdapter(new ParticleAdapter(fa, type));
//    }
}

