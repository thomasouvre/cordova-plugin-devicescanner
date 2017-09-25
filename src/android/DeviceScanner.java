package com.inwink.devicescansdk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import device.common.DecodeStateCallback;
import device.common.DecodeResult;
import device.common.ScanConst;
import device.sdk.ScanManager;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.LOG;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DeviceScanner extends CordovaPlugin {
	private static final String TAG = "DeviceScanner";

	private static boolean disabled;
	private static ScanManager mScanner;
	private static DecodeResult mDecodeResult;
	private static CallbackContext mCallbackContext;

	public static final int CODE_SCANNER_NOT_FOUND = 1;

    public static class ScanResultReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (mScanner != null) {
				mScanner.aDecodeGetResult(mDecodeResult.recycle());
				if (mCallbackContext != null) {
					PluginResult result = new PluginResult(PluginResult.Status.OK, mDecodeResult.toString());
					result.setKeepCallback(true);
					mCallbackContext.sendPluginResult(result);
				}
			}
		}
	}

    @Override
    public void initialize(final CordovaInterface cordova, CordovaWebView webView) {
        LOG.v(TAG, "DeviceScanner: initialization");
        super.initialize(cordova, webView);
		mScanner = new ScanManager();
		mDecodeResult = new DecodeResult();
		try {
			mScanner.aDecodeSetResultType(ScanConst.ResultType.DCD_RESULT_USERMSG);
			disabled = false;
		} catch (Exception e) {
			LOG.v(TAG, "DeviceScanner: initialization failed");
			disabled = true;
			return;
		}
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        LOG.v(TAG, "Executing action: " + action);

		boolean ok = !disabled && mScanner != null;
		if (!ok) {
			JSONObject r = new JSONObject();
			r.put("message", "scanner not found");
			r.put("code", CODE_SCANNER_NOT_FOUND);
			callbackContext.error(r);
		}

		boolean methodFound = false;

		if (action.equals("scan")) {
			methodFound = true;
			if (ok) scan(args, callbackContext);
		}

		if (action.equals("clear")) {
			methodFound = true;
			if (ok) clear(args, callbackContext);
		}

		if (action.equals("getInfos")) {
			methodFound = true;
			if (ok) getInfos(args, callbackContext);
		}

		if (action.equals("setInfos")) {
			methodFound = true;
			if (ok) setInfos(args, callbackContext);
		}

		return methodFound;
    }

    public void scan(JSONArray args, CallbackContext callbackContext) throws JSONException {
		mCallbackContext = callbackContext;
	}

	public void clear(JSONArray args, CallbackContext callbackContext) throws JSONException {
		mCallbackContext = null;
	}

	public void getInfos(JSONArray args, CallbackContext callbackContext) throws JSONException {
		JSONObject r = new JSONObject();

		boolean isEnabled = mScanner.aDecodeGetDecodeEnable() == 1;
		int triggerMode = mScanner.aDecodeGetTriggerMode();
		//boolean isTriggerModeAuto = mScanner.aDecodeGetTriggerMode() == ScanConst.TriggerMode.DCD_TRIGGER_MODE_AUTO;
		//boolean isTriggerModeContinuous = mScanner.aDecodeGetTriggerMode() == ScanConst.TriggerMode.DCD_TRIGGER_MODE_CONTINUOUS;
		//boolean isTriggerModeOneShot = mScanner.aDecodeGetTriggerMode() == ScanConst.TriggerMode.DCD_TRIGGER_MODE_ONESHOT;
		boolean isBeepEnable = mScanner.aDecodeGetBeepEnable() == 1;

		r.put("enabled", isEnabled);
		r.put("triggerMode", triggerMode);
		r.put("beep", isBeepEnable);
		callbackContext.success(r);
	}

	public void setInfos(JSONArray args, CallbackContext callbackContext) throws JSONException {
		JSONObject r = new JSONObject();

		JSONObject data = args.getJSONObject(0);
		if (data.has("enabled")) {
			boolean isEnabled = data.optBoolean("enabled", true);
			mScanner.aDecodeSetDecodeEnable(isEnabled ? 1 : 0);
		}
		if (data.has("triggerMode")) {
			int triggerMode = data.optInt("triggerMode", ScanConst.TriggerMode.DCD_TRIGGER_MODE_ONESHOT);
			mScanner.aDecodeSetTriggerMode(triggerMode);
		}
		if (data.has("beep")) {
			boolean isBeepEnabled = data.optBoolean("beep", true);
			mScanner.aDecodeSetBeepEnable(isBeepEnabled ? 1 : 0);
		}
		getInfos(args, callbackContext);
	}
}