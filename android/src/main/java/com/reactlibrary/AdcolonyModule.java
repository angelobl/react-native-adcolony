package com.adcolony;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;

import com.adcolony.sdk.AdColony;
import com.adcolony.sdk.AdColonyAdOptions;
import com.adcolony.sdk.AdColonyAppOptions;
import com.adcolony.sdk.AdColonyInterstitial;
import com.adcolony.sdk.AdColonyInterstitialListener;
import com.adcolony.sdk.AdColonyReward;
import com.adcolony.sdk.AdColonyRewardListener;
import com.adcolony.sdk.AdColonyUserMetadata;
import com.adcolony.sdk.AdColonyZone;

import android.os.Handler;
import android.os.Looper;
import android.telecom.Call;
import android.util.Log;

import java.util.HashMap;

public class AdcolonyModule extends ReactContextBaseJavaModule {

    private final ReactApplicationContext reactContext;
    private static final String TAG = "RNAdcolony";

    private AdColonyInterstitial ad;
    private AdColonyInterstitialListener listener;

    public AdcolonyModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    private void onConfigure(final String appId,final String zoneId,final String userId) {
      String consent = "1";
      AdColonyAppOptions appOptions = new AdColonyAppOptions().setUserID(userId).setKeepScreenOn(true).setGDPRConsentString(consent).setGDPRRequired(true); 
      //.setKeepScreenOn(true);
      AdColony.configure(getCurrentActivity(),appOptions, appId, zoneId);

      listener = new AdColonyInterstitialListener() {
        @Override
        public void onRequestFilled(AdColonyInterstitial add) {
            // Ad passed back in request filled callback, ad can now be shown
            ad = add;

        }
        
        

      };
      //AdColony.setRewardListener(listener);
      //AdColony.requestInterstitial(zoneId, listener);
    }



    
      @ReactMethod
      public void loadAds(final String zoneId) {
        AdColony.requestInterstitial(zoneId, listener);
      }
    
      @ReactMethod
      public void showAdReward() {
        
        //AdColonyAppOptions appOptions = AdColony.getAppOptions().setGDPRConsentString(consent).setGDPRRequired(true); 
        //AdColony.setAppOptions(appOptions);
        ad.show();
      }
    
      @ReactMethod
      public void configure(final String appId,final String zoneId,final String userId) {
        onConfigure(appId,zoneId,userId);
      }

    @Override
    public String getName() {
        return "Adcolony";
    }

    @ReactMethod
    public void sampleMethod(String stringArgument, int numberArgument, Callback callback) {
        
        callback.invoke("Received numberArgument: " + numberArgument + " stringArgument: " + stringArgument);
    }
}
