package com.example.onlineshop.ex_handler;

public class ExceptionMessage {
    //validations messages
    public static final String NOT_VALID_USERNAME = "Username length must be between 5 ,20 and not contains" +
            " special character";
    public static final String NOT_VALID_PASSWORD = "Password should be at least 8 character and contains at least" +
            " one capital letter and one special character ";
    public static final String NOT_VALID_FIRSTNAME = "Firstname length should be between 3,10 and not contains " +
            "number or special character";
    public static final String NOT_VALID_LASTNAME = "Lastname length should be between 3,10 and not contains " +
            " number or special character";

    public static final String NOT_VALID_NUMBER = "Number must be bigger than zero";
    public static final String NOT_VALID_PRICE = "Price must be bigger than zero";
    //other message

    public static final String USER_NOT_ENABLES = "Need To Verifying Email";
    public static final String EMPTY_CART = "Cart Cant Be Empty";
    public static final String NOT_ENOUGH_BALANCE = "Balance Is Not Enough";
    public static final String EMAIL_VERIFIED = "Email has already been verified";

    public static String notValidAmount(int amount) {
        return "Amount: " + amount + " Is Not Valid";
    }

    public static String notValidCode(String code) {
        return "Code: " + code + " Is Not Valid";
    }

    public static String userNotFound(String field, String value) {
        return "No User Found With " + field + ": " + value;
    }

    public static String productNotFound(long id) {
        return "No Product Found With Id: " + id;
    }

    public static String roleNotFound(String roleName) {
        return "No Role Found With Name: " + roleName;
    }

    public static String duplicateProduct(String name) {
        return name + "Is Already Exist In Your Cart";
    }
}
