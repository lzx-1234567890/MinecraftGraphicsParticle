package com.lzx.minecraftparticle.ui.draw.graphics;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import com.google.android.material.imageview.ShapeableImageView;
import com.lzx.minecraftparticle.R;
import java.util.ArrayList;
import java.util.List;

public class GraphicsParticleAdapter extends ArrayAdapter{
    private Context context;
    private ArrayList<Bitmap> bitmaps;
    private int layoutResourceId;

    public GraphicsParticleAdapter(Context context, int layoutResourceId, ArrayList<Bitmap> bitmaps) {
        super(context, layoutResourceId, bitmaps);
        this.context = context;
        this.bitmaps = bitmaps;
        this.layoutResourceId = layoutResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(layoutResourceId, parent, false);
        }
        
        ShapeableImageView particleImageView = convertView.findViewById(R.id.particleImageView);
        particleImageView.setImageBitmap(bitmaps.get(position));
        
        return convertView;
    }
}
