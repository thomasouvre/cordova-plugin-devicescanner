package com.inwink.devicescansdk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import device.common.DecodeResult;
import device.common.ScanConst;
import device.sdk.ScanManager;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.LOG;
// import org.apache.cordova.PermissionHelper;
import org.apache.cordova.PluginResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DeviceScanner extends CordovaPlugin {
	private static final String TAG = "DeviceScanner";

	private static ScanManager mScanner;
	private static DecodeResult mDecodeResult;
	private static CallbackContext mCallbackContext;

    public static class ScanResultReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (mScanner != null) {
				mScanner.aDecodeGetResult(mDecodeResult.recycle());
				mCallbackContext.success(mDecodeResult.toString());
				// mBarType.setText(mDecodeResult.symName);
				// mResult.setText(mDecodeResult.toString());
			}
		}
	}

    @Override
    public void initialize(final CordovaInterface cordova, CordovaWebView webView) {
        LOG.v(TAG, "StatusBar: initialization");
        super.initialize(cordova, webView);
		mScanner = new ScanManager();
		mDecodeResult = new DecodeResult();
		mScanner.aDecodeSetResultType(ScanConst.ResultType.DCD_RESULT_USERMSG);
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        LOG.v(TAG, "Executing action: " + action);

		if (action.equals("scan")) {
			mCallbackContext = callbackContext;
			return true;
		}

		return false;
    }
}