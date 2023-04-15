package com.atrust;

import android.os.AsyncTask;

import com.atrust.exception.VpnInitException;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.sangfor.sdk.SFUemSDK;
import com.sangfor.sdk.base.SFAuthResultListener;
import com.sangfor.sdk.base.SFAuthType;
import com.sangfor.sdk.base.SFBaseMessage;
import com.sangfor.sdk.base.SFConstants;
import com.sangfor.sdk.base.SFRegetSmsListener;
import com.sangfor.sdk.base.SFSDKFlags;
import com.sangfor.sdk.base.SFSDKMode;
import com.sangfor.sdk.base.SFSmsMessage;
import com.sangfor.sdk.utils.SFLogN;

import java.util.HashMap;
import java.util.Map;

public class AtrustVpnModule extends ReactContextBaseJavaModule implements LifecycleEventListener, SFAuthResultListener {

  private static Promise PROMISE;
  private SFAuthType mNextAuthType;
  private final String TAG = "RNSangforAtrustVpn";

  public AtrustVpnModule(ReactApplicationContext reactContext) {
    super(reactContext);
    reactContext.addLifecycleEventListener(this);
  }

  @ReactMethod
  public void login(final String url, final String username, final String password, final Promise promise) {
    PROMISE = promise;
    SFUemSDK.getInstance().setAuthResultListener(this);
    AsyncTask task = new AsyncTask<Object, Object, Boolean>() {

      @Override
      protected Boolean doInBackground(Object... params) {
        return true;
      }

      @Override
      protected void onPostExecute(Boolean result) {

        try {
          initSDK();
          SFUemSDK.getInstance().startPasswordAuth(url, username, password);
        } catch (Exception e) {
          PROMISE.reject("授权异常", e.getMessage());
        }
      }
    };
    task.execute();
  }

  @ReactMethod
  public void SecondLogin(final String code, final Promise promise) {
    PROMISE = promise;
    SFUemSDK.getInstance().setAuthResultListener(this);
    AsyncTask task = new AsyncTask<Object, Object, Boolean>() {

      @Override
      protected Boolean doInBackground(Object... params) {
        return true;
      }

      @Override
      protected void onPostExecute(Boolean result) {

        try {
          Map<String, String> authParams = new HashMap<>();

          authParams.put(SFConstants.AUTH_KEY_SMS, code);

          SFUemSDK.getInstance().doSecondaryAuth(mNextAuthType, authParams);
        } catch (Exception e) {
          PROMISE.reject("SecondLogin", e.getMessage());
        }
      }
    };
    task.execute();
  }

  @ReactMethod
  public void regetSmsCode(final Promise promise) {
    PROMISE = promise;
    SFUemSDK.getInstance().setAuthResultListener(this);
    AsyncTask task = new AsyncTask<Object, Object, Boolean>() {

      @Override
      protected Boolean doInBackground(Object... params) {
        return true;
      }

      @Override
      protected void onPostExecute(Boolean result) {

        try {
          SFUemSDK.getInstance().getSFAuth().regetSmsCode(new SFRegetSmsListener() {
            @Override
            public void onRegetSmsCode(boolean success, SFSmsMessage message) {
              WritableMap map = Arguments.createMap();
              if (success) {
                map.putString("result", "success");
              } else {
                map.putString("result", message.toString());
              }
              promise.resolve(map);
            }
          });
        } catch (Exception e) {
          PROMISE.reject("regetSmsCode", e.getMessage());
        }
      }
    };
    task.execute();
  }

  @ReactMethod
  public void logout(final Promise promise) {
    if (promise != null) {
      PROMISE = promise;
    }
    AsyncTask task = new AsyncTask<Object, Object, Boolean>() {

      @Override
      protected Boolean doInBackground(Object... params) {
        return true;
      }

      @Override
      protected void onPostExecute(Boolean result) {
        SFUemSDK.getInstance().logout();
      }
    };
    task.execute();
  }

  @ReactMethod
  public void getVpnStatus(final Promise promise) {
    if (promise != null) {
      PROMISE = promise;
    }
    AsyncTask task = new AsyncTask<Object, Object, Boolean>() {

      @Override
      protected Boolean doInBackground(Object... params) {
        return true;
      }

      @Override
      protected void onPostExecute(Boolean result) {
        String status = SFUemSDK.getInstance().getAuthStatus().toString();
        promise.resolve(status);
      }
    };
    task.execute();
  }

  @Override
  public String getName() {
    return TAG;
  }

  private void resolve(String message) {
    WritableMap map = Arguments.createMap();
    map.putString("result", message);
    if (PROMISE != null) {
      PROMISE.resolve(map);
      PROMISE = null;
    }
  }

  private void reject(long errorCode, String message) {
    if (PROMISE != null) {
      PROMISE.reject(String.valueOf(errorCode), message);
      PROMISE = null;
    }
  }

  private void initSDK() throws VpnInitException {
    SFSDKMode sdkMode = SFSDKMode.MODE_VPN;
    try {
      int sdkFlags = SFSDKFlags.FLAGS_HOST_APPLICATION;      //表明是单应用或者是主应用
      sdkFlags |= SFSDKFlags.FLAGS_VPN_MODE_TCP;              //表明使用VPN功能中的TCP模式
      SFUemSDK.getInstance().initSDK(getReactApplicationContext(), sdkMode, sdkFlags, null);//初始化SDK
    } catch (Exception e) {
      throw new VpnInitException("0", "初始化SDK失败");
    }
  }

  @Override
  public void onAuthProgress(SFAuthType nextAuthType, SFBaseMessage message) {
    SFLogN.info(TAG, "need next auth, authType: " + nextAuthType.name());

    mNextAuthType = nextAuthType;
    if (nextAuthType == SFAuthType.AUTH_TYPE_SMS) {
      this.resolve(mNextAuthType.toString());
      // 处理短信主认证的一些逻辑，如显示验证码输入框
    } else {
      /**
       * 服务端配置了首次登陆强制修改密码，或者其他非短信验证码二次认证类型时，认证时也会回调此方法,
       * 此时如果不打算适配此类型二次认证，建议给用户提示，让管理员调整配置
       */
      this.resolve("暂不支持此种认证类型");
    }

  }

  @Override
  public void onAuthSuccess(final SFBaseMessage message) {
    this.resolve(message.mErrStr);
  }

  @Override
  public void onAuthFailed(final SFBaseMessage message) {
    this.reject(message.mErrCode, message.mErrStr);
  }


  @Override
  public void onHostResume() {
  }

  @Override
  public void onHostPause() {
  }

  @Override
  public void onHostDestroy() {
    this.logout(null);
  }

}
