package com.lzx.minecraftparticle.ui.draw.param.widgets;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.lzx.minecraftparticle.R;

public class CoordinateParamView extends ParamView{
    private TextInputLayout textInputLayout_x;
    private TextInputLayout textInputLayout_y;
    private TextInputLayout textInputLayout_z;
    private TextInputEditText inputEditText_x;
    private TextInputEditText inputEditText_y;
    private TextInputEditText inputEditText_z;
    
    private String resetInput_x = "";
    private String resetInput_y = "";
    private String resetInput_z = "";
    
    private final String[] errorTexts = {"X坐标输入错误", "Y坐标输入错误", "Z坐标输入错误"};
    
    public CoordinateParamView(Context context, AttributeSet attrs){
        super(context, attrs);
        //加载视图
        LayoutInflater.from(context).inflate(R.layout.param_coordinate, this, true);
        //获取属性
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CoordinateParamView);
        String hint_x = a.getString(R.styleable.CoordinateParamView_cpvHint_x);
        String hint_y = a.getString(R.styleable.CoordinateParamView_cpvHint_y);
        String hint_z = a.getString(R.styleable.CoordinateParamView_cpvHint_z);
        String input_x = a.getString(R.styleable.CoordinateParamView_cpvInput_x);
        String input_y = a.getString(R.styleable.CoordinateParamView_cpvInput_y);
        String input_z = a.getString(R.styleable.CoordinateParamView_cpvInput_z);
        a.recycle();
        // 设置视图
        textInputLayout_x = (TextInputLayout)findViewById(R.id.textInputLayoutX);
        textInputLayout_x.setHint(hint_x);
        textInputLayout_y = (TextInputLayout)findViewById(R.id.textInputLayoutY);
        textInputLayout_y.setHint(hint_y);
        textInputLayout_z = (TextInputLayout)findViewById(R.id.textInputLayoutZ);
        textInputLayout_z.setHint(hint_z);
        inputEditText_x = (TextInputEditText) findViewById(R.id.inputEditTextX);
        inputEditText_x.setText(input_x);
        inputEditText_y = (TextInputEditText) findViewById(R.id.inputEditTextY);
        inputEditText_y.setText(input_y);
        inputEditText_z = (TextInputEditText) findViewById(R.id.inputEditTextZ);
        inputEditText_z.setText(input_z);
        
    }
    
    //获取EditText
    private TextInputEditText getEditText(int index) {
        switch(index) {
            case 0:
                return inputEditText_x;
            case 1:
                return inputEditText_y;
            case 2:
                return inputEditText_z;
            default:
                return inputEditText_x;
        }
    }
    
    //获取TextInputLayout
    private TextInputLayout getTextInputLayout(int index) {
        switch(index) {
            case 0:
                return textInputLayout_x;
            case 1:
                return textInputLayout_y;
            case 2:
                return textInputLayout_z;
            default:
                return textInputLayout_x;
        }
    }
    
    //获取输入
    public String[] getInput(){
        return new String[]{inputEditText_x.getText().toString(), inputEditText_y.getText().toString(), inputEditText_z.getText().toString()};
    }
    
    public String getInput(int index) {
        return getEditText(index).getText().toString();
    }
    
    //设置输入
    public void setInput(String[] inputs){
        inputEditText_x.setText(inputs[0]);
        inputEditText_y.setText(inputs[1]);
        inputEditText_z.setText(inputs[2]);
    }
    
    public void setInput(int index, String input) {
        getEditText(index).setText(input);
    }
    
    //设置重置输入
    public void setResetInput(String[] resetInputs) {
        resetInput_x = resetInputs[0];
        resetInput_y = resetInputs[1];
        resetInput_z = resetInputs[2];
    }

    //重置输入
    public void resetInput(){
        inputEditText_x.setText(resetInput_x);
        inputEditText_y.setText(resetInput_y);
        inputEditText_z.setText(resetInput_z);
    }
    
    //设置错误输入
    public void setError(int index) {
        getTextInputLayout(index).setErrorEnabled(true);
        getTextInputLayout(index).setError(errorTexts[index]);
    }
    
    //取消错误输入
    public void clearError(int index) {
        getTextInputLayout(index).setErrorEnabled(false);
    }
    
    //设置文本监听
    public void setTextWather(TextWatcher[] tws) {
        inputEditText_x.addTextChangedListener(tws[0]);
        inputEditText_y.addTextChangedListener(tws[1]);
        inputEditText_z.addTextChangedListener(tws[2]);
    }
    
    public void setTextWather(int index, TextWatcher tw) {
        getEditText(index).addTextChangedListener(tw);
    }
    
}
