package com.adcolony;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.Arguments;

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

    private void sendEvent(ReactApplicationContext context, String eventName, final WritableMap params) {
      context.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(eventName, params);
  }

    private void onConfigure(final String appId,final String zoneId,final String userId) {
      final WritableMap params1 = Arguments.createMap();
        //final WritableMap params2 = Arguments.createMap();
        //final WritableMap params3 = Arguments.createMap();
      String consent = "1";
      AdColonyAppOptions appOptions = new AdColonyAppOptions().setUserID(userId).setKeepScreenOn(true).setGDPRConsentString(consent).setGDPRRequired(true); 
      //.setKeepScreenOn(true);
      AdColony.configure(getCurrentActivity(),appOptions, appId, zoneId);

      listener = new AdColonyInterstitialListener() {
        @Override
        public void onRequestFilled(AdColonyInterstitial add) {
            // Ad passed back in request filled callback, ad can now be shown
            params1.putBoolean("adRequestFilled", true);
            sendEvent(reactContext, "Event", params1);
            ad = add;

        }
        /*
        @Override
        public void onRequestNotFilled(AdColonyZone zone) {
            // Ad request was not filled
            params2.putBoolean("adRequestNotFilled", true);
                    sendEvent(reactContext, "Event", params2);
        }

        @Override
        public void onOpened(AdColonyInterstitial ad) {
            // Ad opened, reset UI to reflect state change
            showButton.setEnabled(false);
            progress.setVisibility(View.VISIBLE);
            Log.d(TAG, "onOpened");
        }

        @Override
        public void onExpiring(AdColonyInterstitial ad) {
            // Request a new ad if ad is expiring
            params3.putBoolean("adExpired", true);
                    sendEvent(reactContext, "Event", params3);
        }
    */    

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
