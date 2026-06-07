package com.example.test.enums;


import lombok.Getter;

@Getter
public enum RefundStatus {
    PENDING("pending"),
    FAILED("failed"),
    SUCCEEDED("succeeded"),
    CANCELLED("cancelled"),
    REQUIRES_ACTION("requires_action"),

    UNKNOWN("unknown");

    private final String stripeValue;
    RefundStatus(String stripeValue) {
        this.stripeValue = stripeValue;
    }

    public String stripeValue() {
        return stripeValue;
    }

    public static  RefundStatus fromStripe(String status){
        if(status == null){
            return RefundStatus.UNKNOWN;
        }

        for(RefundStatus refundStatus : RefundStatus.values()){
            if(refundStatus.stripeValue.equalsIgnoreCase(status)){
                return refundStatus;
            }
        }
        return RefundStatus.UNKNOWN;
    }

    public String toStripe(){
        return this.stripeValue;
    }

}
