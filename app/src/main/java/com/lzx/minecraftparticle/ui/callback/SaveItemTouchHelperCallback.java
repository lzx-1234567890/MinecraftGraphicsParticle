package com.lzx.minecraftparticle.ui.callback;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.lzx.minecraftparticle.ui.adapter.SaveAdapter;


public class SaveItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private final SaveAdapter adapter;
    
    private final int[] position = new int[2];

    public SaveItemTouchHelperCallback(SaveAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return false; // 启用长按拖动
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if(actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            viewHolder.itemView.setAlpha(0.4f);
            position[0] = viewHolder.getAdapterPosition();
        }
        super.onSelectedChanged(viewHolder, actionState);
    }

    @Override
    public void clearView(@NonNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        viewHolder.itemView.setAlpha(1f);
        position[1] = viewHolder.getAdapterPosition();
        adapter.sortItem(position[0], position[1]);
        position[0] = 0;
        position[1] = 0;
        super.clearView(recyclerView, viewHolder);
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT; // 允许上下拖动
        //int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END; // 允许左右滑动
        int swipeFlags = 0;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, RecyclerView.ViewHolder source, RecyclerView.ViewHolder target) {
        adapter.moveItem(source.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
        // 滑动删除的逻辑
        adapter.removeItem(viewHolder.getAdapterPosition());
    }

}