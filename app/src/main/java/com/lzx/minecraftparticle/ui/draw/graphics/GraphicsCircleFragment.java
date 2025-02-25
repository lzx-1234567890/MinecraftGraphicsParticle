package com.lzx.minecraftparticle.ui.draw.graphics;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.lzx.minecraftparticle.databinding.FragmentGraphicsBinding;
import com.lzx.minecraftparticle.logic.model.Save;
import com.lzx.minecraftparticle.logic.model.Saves.SaveCircle;
import com.lzx.minecraftparticle.logic.model.Vector;
import com.lzx.minecraftparticle.logic.utils.DrawUtil;
import java.util.ArrayList;

public class GraphicsCircleFragment extends GraphicsFragment{
    @Override
    public void createGraphics(Save save) {
        renderer.setRendering(false);
        ArrayList<Vector> points = DrawUtil.getCirclePoints((SaveCircle)save);
        renderer.setPoints(points);
        renderer.updateGraphicsVertices();
        renderer.updateGraphicsTextureCoords();
        renderer.updateGraphicsIndices();
        renderer.setRendering(true);
    }
}
