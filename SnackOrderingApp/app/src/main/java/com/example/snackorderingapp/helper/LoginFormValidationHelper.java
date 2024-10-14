package com.example.snackorderingapp.helper;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginFormValidationHelper implements TextWatcher {

    private TextInputEditText phoneTextField, passwordTextField;
    private TextInputLayout phoneFieldLayout, passwordFieldLayout;
    private Button loginBtn;
    private String phoneField, passwordField;

    public LoginFormValidationHelper(TextInputEditText phoneTextField,
                                     TextInputEditText passwordTextField,
                                     TextInputLayout phoneFieldLayout,
                                     TextInputLayout passwordFieldLayout,
                                     Button loginBtn) {
        this.phoneTextField = phoneTextField;
        this.passwordTextField = passwordTextField;
        this.phoneFieldLayout = phoneFieldLayout;
        this.passwordFieldLayout = passwordFieldLayout;
        this.loginBtn = loginBtn;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {


    }

    @Override
    public void afterTextChanged(Editable s) {
        phoneField = phoneTextField.getText().toString().trim();
        passwordField = passwordTextField.getText().toString().trim();
        phoneFieldLayout.setError(null);
        passwordFieldLayout.setError(null);

        if(phoneField.isEmpty() || phoneField.isBlank() ){
            phoneFieldLayout.setError("Số điện thoại không được để trống");
        }

        if(passwordField.isEmpty() || passwordField.isBlank()){
            passwordFieldLayout.setError("Mật khẩu không được để trống");
        }

        // RE-ENABLE BUTTON IF ALL FIELDS ARE FILLED:
        loginBtn.setEnabled(!phoneField.isBlank() && !passwordField.isBlank());
    }

    public String getFormattedPhoneNumber() {
        if (phoneField.startsWith("0") && phoneField.length() == 10) {
            return "+84" + phoneField.substring(1);
        }
        return phoneField;
    }
}