package com.lzx.minecraftparticle.ui.adapter;
import android.annotation.SuppressLint;
import android.net.Uri;;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
import com.lzx.minecraftparticle.MinecraftParticleApplication;
import com.lzx.minecraftparticle.R;
import com.lzx.minecraftparticle.logic.Repository;
import com.lzx.minecraftparticle.logic.model.Save;


public class SaveAdapter extends RecyclerView.Adapter<SaveAdapter.ViewHolder>{
    private ItemTouchHelper helper;
    private OnSaveAdapterRequestListener listener;

    public SaveAdapter(OnSaveAdapterRequestListener listener) {
        this.listener = listener;
    }

    public interface OnSaveAdapterRequestListener {
        int getSaveNum();
        void onRemoveSave();
        void onSelected(ViewHolder holder);
    }

    
    //ViewHolder内部类
    public static class ViewHolder extends RecyclerView.ViewHolder{
        MaterialCardView cardView;
        ShapeableImageView mainImageView, sortImageView;
        MaterialTextView nameTextView, informationTextView;
        
        Save save;
        
        public ViewHolder(@NonNull View view) {
            super(view);
            this.cardView = view.findViewById(R.id.cardView);
            this.mainImageView = view.findViewById(R.id.mainImageView);
            this.sortImageView = view.findViewById(R.id.sortImageView);
            this.nameTextView = view.findViewById(R.id.nameTextView);
            this.informationTextView = view.findViewById(R.id.informationTextView);
        }
        
        public void setSave(Save save) {
            this.save = save;
        }
        public Save getSave() {return this.save;}
    }
    
    //载入视图
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.save_item, parent, false);
        return new ViewHolder(v);
    }
    
    //设置控件
    @SuppressLint({"DefaultLocale", "ClickableViewAccessibility"})
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //获取存档

        //设置holder的属性
        holder.save = Repository.getInstance().getSave(position);
        
        //设置视图
        holder.mainImageView.setImageURI(Uri.parse(holder.save.getImageUri()));
        holder.nameTextView.setText(holder.save.getName());
        holder.informationTextView.setText(String.format("%s:%d\n%s:%s\n%s:%s\n%s:%s", MinecraftParticleApplication.context.getString(R.string.save_information_basic_id), holder.save.getId(), MinecraftParticleApplication.context.getString(R.string.save_information_basic_type), holder.save.getType().getName(), MinecraftParticleApplication.context.getString(R.string.save_information_basic_createDate), holder.save.getCreateDate(), MinecraftParticleApplication.context.getString(R.string.save_information_basic_modifyDate), holder.save.getModifyDate()));
        
        //设置监听
        holder.cardView.setOnClickListener(v -> {
            listener.onSelected(holder);
            //new SaveItemDialogFragment(this, holder.save).show(listener.getFragmentActivity().getSupportFragmentManager(), "保存项目");
        });
        
        if(holder.sortImageView.getTag() == null || !(Boolean)(holder.sortImageView.getTag())) {
            holder.sortImageView.setOnTouchListener((v, event) -> {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    holder.sortImageView.setTag(true);
                    helper.startDrag(holder);
                    return true;
                }
                return false;
            });
        }
    }
    
    //数据数量
    @Override
    public int getItemCount() {
        return listener.getSaveNum();
    }
    
    public void setItemTouchHelper(ItemTouchHelper helper) {
        this.helper = helper;
    }

    public void moveItem(int from, int to) {
        if(from == to) {return;}
        notifyItemMoved(from, to);
    }
    
    public void sortItem(int from, int to) {
        if(from == to) {return;}
        
        //Save的移动
        Repository.getInstance().sortSave(from, to);

        //holders排序变化
//        ViewHolder fromHolder = holders.get(from);
//        holders.remove(from);
//        holders.add(to, fromHolder);
//        
//        //holder属性设置
//        if(from < to) {
//            for(int i = from;i <= to;i++) {
//                ViewHolder holder = holders.get(i);
//                holder.setSave(Repository.getInstance().getSave(i));
//                holder.setPos(i);
//                holder.cardView.setOnClickListener(v -> {
//                    new SaveItemDialogFragment(this, holder.save).show(fa.getSupportFragmentManager(), "保存项目");
//                });
//                holder.informationTextView.setText(String.format("ID:%d\n类型:%s\n创建日期:%s\n修改日期:%s", holder.save.getId(), holder.save.getType().getName(), holder.save.getCreateDate(), holder.save.getModifyDate()));
//            }
//        }else if(from > to) {
//            for(int i = to;i <= from;i++) {
//                ViewHolder holder = holders.get(i);
//                holder.setSave(Repository.getInstance().getSave(i));
//                holder.setPos(i);
//                holder.cardView.setOnClickListener(v -> {
//                    new SaveItemDialogFragment(this, holder.save).show(fa.getSupportFragmentManager(), "保存项目");
//                });
//                holder.informationTextView.setText(String.format("ID:%d\n类型:%s\n创建日期:%s\n修改日期:%s", holder.save.getId(), holder.save.getType().getName(), holder.save.getCreateDate(), holder.save.getModifyDate()));
//            }
//        }
        if(from < to){
            notifyItemRangeChanged(from, to);
        }else if(from > to) {
            notifyItemRangeChanged(to, from);
        }
        
        if(from == 0) {
            notifyItemChanged(to);
        }else if(to == 0) {
            notifyItemChanged(from);
        }
    }
    
    public void removeItem(int position) {
        //Save的移除
        Repository.getInstance().removeSave(position);
        
        //holder属性设置
        //Toast.makeText(fa, holders.size() + "", Toast.LENGTH_SHORT).show();
        //holders.remove(holders.get(position));
        //Toast.makeText(fa,  fa.getSharedPreferences("Save", Context.MODE_PRIVATE).getInt("saveNum", -1) + "", Toast.LENGTH_SHORT).show();
//        for(int i = position;i < holders.size();i++) {
//            ViewHolder holder = holders.get(i);
//            holder.setSave(Repository.getInstance().getSave(i));
//            holder.setPos(i);
//            holder.cardView.setOnClickListener(v -> {
//                new SaveItemDialogFragment(this, holder.save).show(fa.getSupportFragmentManager(), "保存项目");
//            });
//            holder.informationTextView.setText(String.format("ID:%d\n类型:%s\n创建日期:%s\n修改日期:%s", holder.save.getId(), holder.save.getType().getName(), holder.save.getCreateDate(), holder.save.getModifyDate()));
//        }
        
        //项目通知
        listener.onRemoveSave();
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, listener.getSaveNum());

    }
    
}
