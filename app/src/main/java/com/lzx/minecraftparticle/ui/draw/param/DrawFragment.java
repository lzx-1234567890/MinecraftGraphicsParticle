package com.lzx.minecraftparticle.ui.draw.param;
import android.net.Uri;
import android.view.View;
import androidx.fragment.app.Fragment;
import com.lzx.minecraftparticle.logic.Repository;
import com.lzx.minecraftparticle.logic.model.Save;

public abstract class DrawFragment extends Fragment{
    protected Save save;
    protected int mode; //0为绘制 1为修改
    
    public void setSave(Save save) {this.save = save;};
    public Save getSave() {return this.save;};
    
    public void save(String name, Uri imageUri, Integer[] size) {
        save.setName(name);
        save.setImageUri(imageUri.toString());
        Repository.getInstance().save(save, size);
    }
    
    public void modify() {
        Repository.getInstance().modifySave(save);
    }
    
    public void reset() {};
    public boolean updateParam(boolean output) {return true;};
}
