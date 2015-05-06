package com.example.user.ohmygod.netservice;

public class RequestedUrl {

    private static final String ROOT = "http://1.ipbmbcon2015.sinaapp.com/";

    public static String logInUrl() {
        // TODO:
        return ROOT;
    }

    public static String getVcodeUrl_reg() {
        return ROOT + "register/sendVerifyCode.php";
    }

    public static String verifyMailUrl_reg() {
        return ROOT + "register/verifyMail.php";
    }

    public static String setPwUrl_reg() {
        return ROOT + "register/setPassword.php";
    }

    public static String verifyMailUrl_findPw() {
        return ROOT + "forget/verifyMail.php";
    }

    public static String getVcodeUrl_findPw() {
        return ROOT + "forget/sendVerifyCode.php";
    }

    public static String setPwUrl_findPw() {
        return ROOT + "forget/setPassword.php";
    }
}
