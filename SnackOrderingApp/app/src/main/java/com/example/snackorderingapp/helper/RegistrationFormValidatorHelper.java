package com.example.snackorderingapp.helper;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class RegistrationFormValidatorHelper implements TextWatcher {

    private TextInputEditText txtPhone, txtPassword, txtConfirmPassword;
    private TextInputLayout txtPhoneLayout, txtPasswordLayout, txtConfirmPasswordLayout;
    private Button registerBtn;
    private String phoneField, passwordField, confirmPasswordField;

    public RegistrationFormValidatorHelper(TextInputEditText txtPhone,
                                           TextInputEditText txtPassword,
                                           TextInputEditText txtConfirmPassword,
                                           TextInputLayout txtPhoneLayout,
                                           TextInputLayout txtPasswordLayout,
                                           TextInputLayout txtConfirmPasswordLayout,
                                           Button registerBtn) {
        this.txtPhone = txtPhone;
        this.txtPassword = txtPassword;
        this.txtConfirmPassword = txtConfirmPassword;
        this.txtPhoneLayout = txtPhoneLayout;
        this.txtPasswordLayout = txtPasswordLayout;
        this.txtConfirmPasswordLayout = txtConfirmPasswordLayout;
        this.registerBtn = registerBtn;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // Not used, but required to implement TextWatcher
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // Clear errors when text changes
        txtPhoneLayout.setError(null);
        txtPasswordLayout.setError(null);
        txtConfirmPasswordLayout.setError(null);
    }

    @Override
    public void afterTextChanged(Editable s) {
        phoneField = txtPhone.getText().toString().trim();
        passwordField = txtPassword.getText().toString().trim();
        confirmPasswordField = txtConfirmPassword.getText().toString().trim();

        validatePhone();
        validatePassword();
        validateConfirmPassword();

        // Enable register button if all fields are valid
        registerBtn.setEnabled(!phoneField.isEmpty() && !passwordField.isEmpty()
                && !confirmPasswordField.isEmpty() && passwordField.equals(confirmPasswordField));
    }

    private void validatePhone() {
        if (phoneField.isEmpty()) {
            txtPhoneLayout.setError("Số điện thoại không được để trống");
        }
    }

    private void validatePassword() {
        if (passwordField.isEmpty()) {
            txtPasswordLayout.setError("Mật khẩu không được để trống");
        } else if (passwordField.length() < 8) {
            txtPasswordLayout.setError("Mật khẩu phải có ít nhất 8 ký tự");
        }
    }

    private void validateConfirmPassword() {
        if (confirmPasswordField.isEmpty()) {
            txtConfirmPasswordLayout.setError("Xác nhận mật khẩu không được để trống");
        } else if (!confirmPasswordField.equals(passwordField)) {
            txtConfirmPasswordLayout.setError("Mật khẩu không khớp");
        }
    }

    public String getFormattedPhoneNumber() {
        if (phoneField.startsWith("0") && phoneField.length() == 10) {
            return "+84" + phoneField.substring(1);
        }
        return phoneField;
    }
}