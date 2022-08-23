package com.example.onlineshop.ex_handler;

public class ExceptionMessage {
    public static final String USER_NOT_ENABLES = "Need To Verifying Email";
    public static final String EMPTY_CART = "Cart Cant Be Empty";
    public static final String NOT_ENOUGH_BALANCE = "Balance Is Not Enough";

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

    public static String duplicateProduct(String name){
        return name+ "Is Already Exist In Your Cart";
    }
}
