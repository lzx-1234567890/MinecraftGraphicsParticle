package com.lzx.minecraftparticle.ui.draw.param.widgets;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.lzx.minecraftparticle.R;

public class NormalParamView extends ParamView{
    private TextInputLayout textInputLayout;
    private TextInputEditText inputEditText;
    
    private String resetInput = "";
    
    public NormalParamView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //加载视图
        LayoutInflater.from(context).inflate(R.layout.param_normal, this, true);
        //获取属性
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.NormalParamView);
        String hint = a.getString(R.styleable.NormalParamView_npvHint);
        //String input = a.getString(R.styleable.NormalParamView_npvInput);
        int inputType = a.getInt(R.styleable.NormalParamView_npvInputType, 1);
        a.recycle();
        //设置视图
        textInputLayout = (TextInputLayout)findViewById(R.id.textInputLayout);
        textInputLayout.setHint(hint);
        inputEditText = (TextInputEditText)findViewById(R.id.inputEditText);
        //inputEditText.setText(input);
        inputEditText.setInputType(0);
        if((inputType & 1) != 0) {
            inputEditText.setInputType(inputEditText.getInputType() | InputType.TYPE_CLASS_TEXT);
        }
        if((inputType & 2) != 0) {
            inputEditText.setInputType(inputEditText.getInputType() | InputType.TYPE_CLASS_NUMBER);
        }
        if((inputType & 4) != 0){
            inputEditText.setInputType(inputEditText.getInputType() | InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        }
        if((inputType & 8) != 0){
            inputEditText.setInputType(inputEditText.getInputType() | InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);
        }
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
}
