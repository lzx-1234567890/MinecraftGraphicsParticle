package com.lzx.minecraftparticle.ui.draw.param.widgets;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;
import com.lzx.minecraftparticle.R;
import com.lzx.minecraftparticle.logic.model.ParticleCategory;
import java.util.ArrayList;

public class ParticleCategorySuggestionsAdapter extends RecyclerView.Adapter<ParticleCategorySuggestionsAdapter.ViewHolder> implements Filterable{
    Filter filter;
    ParticleCategoryDialogFragment dialog;
    ArrayList<ParticleCategory> particleCategories;
    ArrayList<ParticleCategory> filteredParticleCategories = new ArrayList<ParticleCategory>();
    
    
    ParticleCategorySuggestionsAdapter(ParticleCategoryDialogFragment dialog, ArrayList<ParticleCategory> particleCategories) {
        this.dialog = dialog;
        this.particleCategories = particleCategories;
        this.filteredParticleCategories.addAll(particleCategories);
    }
    
    //ViewHolder内部类
    public class ViewHolder extends RecyclerView.ViewHolder{
        MaterialCardView cardView;
        MaterialTextView idTextView, nameTextView, indexTextView;
        
        public ViewHolder(@NonNull View view) {
            super(view);
            cardView = (MaterialCardView)view.findViewById(R.id.cardView);
            idTextView = (MaterialTextView)view.findViewById(R.id.idTextView);
            nameTextView = (MaterialTextView)view.findViewById(R.id.nameTextView);
            indexTextView = (MaterialTextView)view.findViewById(R.id.indexTextView);
        }
    }
    
    //载入视图
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.param_pc_suggestions_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }
    
    //设置控件
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.cardView.setOnClickListener(v -> {
            dialog.onClickSearchSuggestion(filteredParticleCategories.get(position).getId(), filteredParticleCategories.get(position).getName());
        });
        holder.idTextView.setText(filteredParticleCategories.get(position).getId());
        holder.nameTextView.setText(filteredParticleCategories.get(position).getName());
        holder.indexTextView.setText(Integer.toString(position + 1));
    }
    
    //数据数量
    @Override
    public int getItemCount() {
        return filteredParticleCategories.size();
    }
    
    //获取Filter
    @Override
    public Filter getFilter() {
        if(filter == null) {
            filter = new DataFilter();
        }
        return filter;
    }

    private class DataFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if(constraint == null || constraint.length() == 0) {
                results.values = particleCategories;
                results.count = particleCategories.size();
            }else {
                ArrayList<ParticleCategory> filteredParticleCategories = new ArrayList<>();
                for(ParticleCategory item : particleCategories) {
                    if(item.getId().contains(constraint) || item.getName().contains(constraint)) {
                        filteredParticleCategories.add(item);
                    }
                }
                results.values = filteredParticleCategories;
                results.count = filteredParticleCategories.size();
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredParticleCategories = (ArrayList<ParticleCategory>)results.values;
            notifyDataSetChanged();
        }
    }
}
