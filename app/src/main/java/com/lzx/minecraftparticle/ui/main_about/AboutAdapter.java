package com.lzx.minecraftparticle.ui.main_about;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
import com.lzx.minecraftparticle.R;
import com.lzx.minecraftparticle.UpdateContentDialogFragment;
import com.lzx.minecraftparticle.ui.tutorial.TutorialActivity;


public class AboutAdapter extends RecyclerView.Adapter<AboutAdapter.ViewHolder>{
    FragmentActivity fa;
    
    String[] items;
    Integer[] imgs;
    
    public AboutAdapter(FragmentActivity fa, String[] items, Integer[] imgs) {
        this.fa = fa;
        this.items = items;
        this.imgs = imgs;
    }
    
    //ViewHolder内部类
    class ViewHolder extends RecyclerView.ViewHolder{
        MaterialCardView cardView ;
        MaterialTextView aboutTextView;
        ShapeableImageView iconImageView;
        
        public ViewHolder(@NonNull View view) {
            super(view);
            this.cardView = view.findViewById(R.id.cardView);
            this.aboutTextView = view.findViewById(R.id.aboutTextView);
            this.iconImageView = view.findViewById(R.id.iconImageView);
        }
        
    }
    
    //载入视图
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.about_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }
    
    //设置控件
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.cardView.setOnClickListener(v -> {
            click(position);
        });
        holder.aboutTextView.setText(items[position]);
        holder.iconImageView.setImageResource(imgs[position]);
    }
    
    //数据数量
    @Override
    public int getItemCount() {
        return items.length;
    }
    
    public void click(int id) {
        switch(id) {
            case 0:
                Intent intent = new Intent(fa, TutorialActivity.class);
                fa.startActivity(intent);
                break;
            case 1:
                intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("mqq://card/show_pslcard?src_type=internal&source=sharecard&version=1&uin=1820455493"));
                try {
                    fa.startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(fa, "请先安装QQ应用", Toast.LENGTH_SHORT).show();
                }
                break;
            case 2:
                new UpdateContentDialogFragment().show(fa.getSupportFragmentManager(), "更新内容");
                break;
            case 3:
                Toast.makeText(fa, "目前已经是最新版本", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
