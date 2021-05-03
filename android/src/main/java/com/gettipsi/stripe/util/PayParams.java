package com.gettipsi.stripe.util;

/**
 * Created by ngoriachev on 13/03/2018.
 */

public abstract class PayParams {

  public static final String CURRENCY_CODE = "currencyCode";
  public static final String BILLING_ADDRESS_REQUIRED = "billing_address_required";
  public static final String SHIPPING_ADDRESS_REQUIRED = "shipping_address_required";
  public static final String PHONE_NUMBER_REQUIRED = "phone_number_required";
  public static final String EMAIL_REQUIRED = "email_required";
  public static final String TOTAL_PRICE = "amount";
  public static final String UNIT_PRICE = "unit_price";
  public static final String LINE_ITEMS = "items";
  public static final String QUANTITY = "quantity";
  public static final String DESCRIPTION = "label";
  public static final String ANDROID_PAY_USE_PAYMENT_INTENT = "usePaymentIntent";
  public static final String COUNTRY_CODE = "countryCode";


}
