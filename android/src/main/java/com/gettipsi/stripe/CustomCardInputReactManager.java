package com.gettipsi.stripe;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Xml;
import android.widget.EditText;
import android.view.inputmethod.InputMethodManager;

import com.devmarvel.creditcardentry.library.CreditCardForm;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.UIManagerModule;
import com.facebook.react.uimanager.annotations.ReactProp;

import org.xmlpull.v1.XmlPullParser;

/**
 * Created by dmitriy on 11/15/16
 */

public class CustomCardInputReactManager extends SimpleViewManager<CreditCardFormExtension
  > {

  public static final String REACT_CLASS = "TPSCardField";
  private static final String TAG = CustomCardInputReactManager.class.getSimpleName();
  private static final String NUMBER = "number";
  private static final String EXP_MONTH = "expMonth";
  private static final String EXP_YEAR = "expYear";
  private static final String CCV = "cvc";
  private static final String ZIPCODE = "addressZip";

  private ThemedReactContext reactContext;
  private WritableMap currentParams;

  private String currentNumber;
  private int currentMonth;
  private int currentYear;
  private String currentCCV;
  private String currentZipcode;

  @Override
  public String getName() {
    return REACT_CLASS;
  }

  @Override
  protected CreditCardFormExtension createViewInstance(final ThemedReactContext reactContext) {
    XmlPullParser parser = reactContext.getResources().getXml(R.xml.stub_material);
    try {
      parser.next();
      parser.nextTag();
    } catch (Exception e) {
      e.printStackTrace();
    }

    AttributeSet attr = Xml.asAttributeSet(parser);
    final CreditCardFormExtension creditCardForm = new CreditCardFormExtension(reactContext, attr);
    setListeners(creditCardForm);
    this.reactContext = reactContext;
    creditCardForm.post(new Runnable() {
      @Override
      public void run() {
        InputMethodManager inputMethodManager = (InputMethodManager) reactContext.getSystemService(reactContext.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInputFromWindow(creditCardForm.getApplicationWindowToken(), InputMethodManager.SHOW_IMPLICIT, 0);
        creditCardForm.focusCreditCard();
      }
    });
    return creditCardForm;
  }

  @ReactProp(name = "postalCodeEntryEnabled")
  public void postalCodeEntryEnabled(CreditCardFormExtension view, boolean enabled){
    view.setIncludeZip(reactContext, enabled);
    setListeners(view);
  }

  @ReactProp(name = "enabled")
  public void setEnabled(CreditCardFormExtension view, boolean enabled) {
    view.setEnabled(enabled);
  }

  @ReactProp(name = "backgroundColor")
  public void setBackgroundColor(CreditCardFormExtension view, int color) {
    Log.d("TAG", "setBackgroundColor: "+color);
    view.setBackgroundColor(color);
  }

  @ReactProp(name = "cardNumber")
  public void setCardNumber(CreditCardFormExtension view, String cardNumber) {
    view.setCardNumber(cardNumber, true);
  }

  @ReactProp(name = "expDate")
  public void setExpDate(CreditCardFormExtension view, String expDate) {
    view.setExpDate(expDate, true);
  }

  @ReactProp(name = "securityCode")
  public void setSecurityCode(CreditCardFormExtension view, String securityCode) {
    view.setSecurityCode(securityCode, true);
  }

  @ReactProp(name = "numberPlaceholder")
  public void setCreditCardTextHint(CreditCardFormExtension view, String creditCardTextHint) {
    view.setCreditCardTextHint(creditCardTextHint);
  }

  @ReactProp(name = "expirationPlaceholder")
  public void setExpDateTextHint(CreditCardFormExtension view, String expDateTextHint) {
    view.setExpDateTextHint(expDateTextHint);
  }

  @ReactProp(name = "cvcPlaceholder")
  public void setSecurityCodeTextHint(CreditCardFormExtension view, String securityCodeTextHint) {
    view.setSecurityCodeTextHint(securityCodeTextHint);
  }

  private void setListeners(final CreditCardFormExtension view){

    final EditText ccNumberEdit = (EditText) view.findViewById(R.id.cc_card);
    final EditText ccExpEdit = (EditText) view.findViewById(R.id.cc_exp);
    final EditText ccCcvEdit = (EditText) view.findViewById(R.id.cc_ccv);
    final EditText ccZipEdit = (EditText) view.findViewById(R.id.cc_zip);

    ccNumberEdit.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
      }

      @Override
      public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        Log.d(TAG, "onTextChanged: cardNumber = "+charSequence);
        currentNumber = charSequence.toString().replaceAll(" ", "");
        postEvent(view);
      }

      @Override
      public void afterTextChanged(Editable editable) {
      }
    });

    ccExpEdit.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
      }

      @Override
      public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        Log.d(TAG, "onTextChanged: EXP_YEAR = "+charSequence);
        try {
          currentMonth = view.getCreditCard().getExpMonth();
        }catch (Exception e){
          if (charSequence.length() == 0)
            currentMonth = 0;
        }
        try {
          currentYear = view.getCreditCard().getExpYear();
        }catch (Exception e){
          currentYear = 0;
        }
        postEvent(view);
      }

      @Override
      public void afterTextChanged(Editable editable) {
      }
    });

    ccCcvEdit.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
      }

      @Override
      public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        Log.d(TAG, "onTextChanged: CCV = "+charSequence);
        currentCCV = charSequence.toString();
        postEvent(view);
      }

      @Override
      public void afterTextChanged(Editable editable) {
      }
    });

    if (ccZipEdit != null) {
      ccZipEdit.addTextChangedListener(new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
          Log.d(TAG, "onTextChanged: ZIPCODE = " + charSequence);
          currentZipcode = charSequence.toString();
          postEvent(view);
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
      });
    }

  }

  private void postEvent(CreditCardFormExtension view){
    currentParams = Arguments.createMap();
    currentParams.putString(NUMBER, currentNumber);
    currentParams.putInt(EXP_MONTH, currentMonth);
    currentParams.putInt(EXP_YEAR, currentYear);
    currentParams.putString(CCV, currentCCV);
    currentParams.putString(ZIPCODE, currentZipcode);
    boolean isValid = view.isCreditCardValid();
    reactContext.getNativeModule(UIManagerModule.class)
      .getEventDispatcher().dispatchEvent(
      new CreditCardFormOnChangeEvent(view.getId(), currentParams, isValid));
  }

  private void updateView(CreditCardFormExtension view){

  }
}
