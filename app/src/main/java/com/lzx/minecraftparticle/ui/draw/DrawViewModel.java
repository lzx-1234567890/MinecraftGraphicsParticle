package com.lzx.minecraftparticle.ui.draw;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.lzx.minecraftparticle.logic.model.Save;
import com.lzx.minecraftparticle.ui.draw.graphics.GraphicsFragment;
import com.lzx.minecraftparticle.ui.draw.param.DrawFragment;

public class DrawViewModel extends ViewModel{
    private MutableLiveData<Save> save = new MutableLiveData<>();
    private MutableLiveData<DrawFragment> drawFragment = new MutableLiveData<>();
    private MutableLiveData<GraphicsFragment> graphicsFragment = new MutableLiveData<>();
    
    public MutableLiveData<Save> getSave() {return save;}
    public MutableLiveData<DrawFragment> getDrawFragment() {return drawFragment;}
    public MutableLiveData<GraphicsFragment> getGraphicsFragment() {return graphicsFragment;}
    
    public void setSave(Save save) {this.save.setValue(save);}
    public void setDrawFragment(DrawFragment df) {this.drawFragment.setValue(df);}
    public void setGraphicsFragment(GraphicsFragment gf) {this.graphicsFragment.setValue(gf);}
}
