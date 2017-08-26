package com.dsgly.bixin.wigets;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v7.app.AppCompatDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.dsgly.bixin.R;
import com.dsgly.bixin.utils.DisplayUtil;


/**
 * 聊天输入框
 * Created by madong on 2017/6/30.
 */

public class EditTextDialog extends AppCompatDialog implements KeyboardWatcher.OnKeyboardToggleListener {

    private KeyboardWatcher keyboardWatcher;

    private OnEditTextListener mOnEditTextListener;
    private TextView replyTv;
    private EditText editText;
    private TextView sendView;

    public EditTextDialog(@NonNull Context context) {
        this(context, R.style.Edit);
    }

    public EditTextDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        // We hide the title bar for any style configuration. Otherwise, there will be a gap
        // above the bottom sheet when it is expanded.
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        init();
    }

    public EditTextDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        init();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        window.setBackgroundDrawableResource(android.R.color.transparent);

        keyboardWatcher = new KeyboardWatcher(this);
        keyboardWatcher.setListener(this);
    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.luobo_layout_live_edittext, null);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DisplayUtil.hideKeyboard(v);
            }
        });

        replyTv = (TextView) view.findViewById(R.id.tv_edit_reply);
        editText = (EditText) view.findViewById(R.id.et_chat);
        sendView = (TextView) view.findViewById(R.id.luobo_live_edit_send);
        sendView.setEnabled(false);
        sendView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnEditTextListener != null) {
                    if (mOnEditTextListener.onSend(editText.getText())) {
                        editText.setText(null);
                        DisplayUtil.hideKeyboard(view);
                    }
                }
            }
        });
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)
                        || (actionId == EditorInfo.IME_ACTION_SEND)) {
                    if (event == null || event.getAction() == KeyEvent.ACTION_DOWN) {
                        if (mOnEditTextListener != null) {
                            if (mOnEditTextListener.onSend(editText.getText())) {
                                editText.setText(null);
                                DisplayUtil.hideKeyboard(v);
                            }
                        }
                    }
                    return true;
                }
                return false;
            }
        });
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable!=null && !TextUtils.isEmpty(editable.toString())) {
                    sendView.setEnabled(true);
                }else {
                    sendView.setEnabled(false);
                }
            }
        });

        setContentView(view);
    }

    public void setOnEditTextListener(OnEditTextListener onEditTextListener) {
        this.mOnEditTextListener = onEditTextListener;
    }

    public void release() {
        if (keyboardWatcher != null) {
            keyboardWatcher.destroy();
        }
        dismiss();
    }

    @Override
    public void onKeyboardShown(int keyboardSize) {
    }

    @Override
    public void onKeyboardClosed() {
        hide(); // 如调用dismiss则keyboardWatcher失效
        if (mOnEditTextListener != null && editText != null) {
            mOnEditTextListener.onTextNotify(editText.getText());
        }
    }

    public interface OnEditTextListener {

        boolean onSend(CharSequence message);

        void onTextNotify(CharSequence charSequence);
    }
}
