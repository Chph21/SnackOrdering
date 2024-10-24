package com.example.snackorderingapp.helper;

public class ApiLinksHelper {
    // MAIN BASE API URI:
    private static final String BASE_URL = "http://10.0.2.2:8080/";


    public static String autenticateUri(){
        return BASE_URL + "account/login";
    }

    public static String registerUri(){
        return BASE_URL + "account/register";
    }

    public static String refreshTokenUri(){
        return BASE_URL + "account/refresh-token";
    }

    public static String updateOrderUri() {
        return BASE_URL + "order";
    }


    public static String getUserInfoUri(String phone){
        return BASE_URL + "account/phone/" + phone;
    }
    public static String generateOtpUri(){
        return BASE_URL + "account/generate-sms-otp";
    }
    public static String verifyOtpUri(){
        return BASE_URL + "account/verify-sms-otp";
    }
    public static String getOrdersUri() {
        return BASE_URL + "order";
    }

    public static String getUserByIdUri(Integer userId) {
        return BASE_URL + "account/" + userId;
    }
    public static String getUsersUri() {
        return BASE_URL + "account";
    }
    public static String updateUserUri() {
        return BASE_URL + "account";
    }
}
// END OF API LINKS HELPER CLASS.