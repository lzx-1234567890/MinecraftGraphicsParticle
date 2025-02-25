package com.lzx.minecraftparticle.ui.draw.param.widgets;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import androidx.fragment.app.FragmentManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.lzx.minecraftparticle.R;
import com.lzx.minecraftparticle.ui.draw.param.DrawFragment;

public class ParticleCategoryParamView extends ParamView{
    private TextInputLayout textInputLayout;
    private TextInputEditText inputEditText;
    private MaterialButton selectButton;
    
    private DrawFragment drawFragment;
    
    private String resetInput = "";
    
    public ParticleCategoryParamView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //加载视图
        LayoutInflater.from(context).inflate(R.layout.param_path, this, true);
        //获取属性
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ParticleCategoryParamView);
        String hint = a.getString(R.styleable.ParticleCategoryParamView_pcpvHint);
        String input = a.getString(R.styleable.ParticleCategoryParamView_pcpvInput);
        a.recycle();
        //设置视图
        textInputLayout = (TextInputLayout)findViewById(R.id.textInputLayout);
        textInputLayout.setHint(hint);
        inputEditText = (TextInputEditText)findViewById(R.id.inputEditText);
        inputEditText.setText(input);
        selectButton = (MaterialButton)findViewById(R.id.selectButton);
        selectButton.setOnClickListener(v -> {
            FragmentManager fm = drawFragment.getActivity().getSupportFragmentManager();
            new ParticleCategoryDialogFragment(this).show(fm, "选择粒子种类");
        });
    }
    
    //输入的get,set
    public String getInput(){
        return inputEditText.getText().toString();
    }
    
    public void setInput(String input){
        inputEditText.setText(input);
    }
    
    //设置重置输入
    public void setResetInput(String resetInput) {
        this.resetInput = resetInput;
    }
    
    //重置输入
    public void resetInput(){
        inputEditText.setText(resetInput);
    }
    
    //设置错误输入
    public void setError(String text) {
        textInputLayout.setErrorEnabled(true);
        textInputLayout.setError(text);
    }
    
    //取消错误输入
    public void clearError() {
        textInputLayout.setErrorEnabled(false);
    }
    
    //设置DrawFragment
    public void setDrawFragment(DrawFragment drawFragment) {
        this.drawFragment = drawFragment;
    }
    
    //搜索到
    public void setSearch(String search) {
        setInput(search);
    }
}
