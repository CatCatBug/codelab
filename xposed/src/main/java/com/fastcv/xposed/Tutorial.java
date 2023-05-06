package com.fastcv.xposed;

import android.util.Log;

import java.util.Map;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class Tutorial implements IXposedHookLoadPackage {

    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        if (loadPackageParam.packageName.equals("com.umeox.myqibla")) {
            XposedBridge.log("找到目标app" + loadPackageParam.packageName);
            Log.d("xcl_debug", "找到目标app");

            hook_method("android.webkit.WebView", loadPackageParam.classLoader, "loadUrl", String.class, new XC_MethodHook() { // from class: com.cqttech.myapplication.HookTest.1
                protected void beforeHookedMethod(MethodHookParam methodHookParam) throws Throwable {
                    Log.d("xcl_debug", "抓取 WebView: loadUrl --- ");
                    super.beforeHookedMethod(methodHookParam);
                }
            });

            hook_method("com.thingclips.smart.jsbridge.dsbridge.DWebView", loadPackageParam.classLoader, "loadUrl", String.class, new XC_MethodHook() { // from class: com.cqttech.myapplication.HookTest.1
                protected void beforeHookedMethod(MethodHookParam methodHookParam) throws Throwable {
                    Log.d("xcl_debug", "抓取 DWebView: loadUrl --- ");
                    super.beforeHookedMethod(methodHookParam);
                }
            });

            hook_method("com.thingclips.smart.jsbridge.dsbridge.DWebView", loadPackageParam.classLoader, "loadUrl", String.class, Map.class, new XC_MethodHook() { // from class: com.cqttech.myapplication.HookTest.1
                protected void beforeHookedMethod(MethodHookParam methodHookParam) throws Throwable {
                    Log.d("xcl_debug", "抓取 DWebView: loadUrl2 --- ");
                    Log.d("xcl_debug", "methodHookParam[1] --- " + methodHookParam.args[0]);
                    XposedBridge.log("请求地址：" + methodHookParam.args[0]);
                    Map<String,String> p2 = (Map<String,String>)methodHookParam.args[1];
                    for (String s : p2.keySet()) {
                        XposedBridge.log("请求头 --->  key: " + s + "  value : " + p2.get(s));
                        Log.d("xcl_debug", "methodHookParam[2] --- key: " + s + "  value : " + p2.get(s));
                    }
                    super.beforeHookedMethod(methodHookParam);
                }
            });

            hook_method("android.webkit.WebView", loadPackageParam.classLoader, "loadData", String.class, String.class, String.class, new XC_MethodHook() { // from class: com.cqttech.myapplication.HookTest.1
                protected void beforeHookedMethod(MethodHookParam methodHookParam) throws Throwable {
                    Log.d("xcl_debug", "WebView beforeHookedMethod: loadData --- ");
                    super.beforeHookedMethod(methodHookParam);
                }
            });

            hook_method("android.webkit.WebView", loadPackageParam.classLoader, "loadDataWithBaseURL", String.class, String.class, String.class, String.class, String.class, new XC_MethodHook() { // from class: com.cqttech.myapplication.HookTest.1
                protected void beforeHookedMethod(MethodHookParam methodHookParam) throws Throwable {
                    Log.d("xcl_debug", "WebView beforeHookedMethod: loadDataWithBaseURL --- ");
                    super.beforeHookedMethod(methodHookParam);
                }
            });

            hook_method("com.thingclips.smart.hometab.activity.FamilyHomeActivity", loadPackageParam.classLoader, "onResume", new XC_MethodHook() { // from class: com.cqttech.myapplication.HookTest.1
                protected void beforeHookedMethod(MethodHookParam methodHookParam) throws Throwable {
                    Log.d("xcl_debug", "目标界面已启动");
                    super.beforeHookedMethod(methodHookParam);
                }
            });


//            hook_method("android.app.Activity", loadPackageParam.classLoader, "onResume", new XC_MethodHook() { // from class: com.cqttech.myapplication.HookTest.2
//                protected void beforeHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) throws Throwable {
//                    Log.d("xcl_debug", "beforeHookedMethod: onResume --- " + methodHookParam.thisObject);
//                    super.beforeHookedMethod(methodHookParam);
//                }
//            });

//            Class<?> cls = Object.class;
//            Method[] methods = XposedHelpers.findClass("com.tencent.open.agent.QuickLoginAuthorityActivity$1", loadPackageParam.classLoader).getMethods();
//            int length = methods.length;
//            int i = 0;
//            while (true) {
//                if (i >= length) {
//                    break;
//                }
//                Method method = methods[i];
//                if (method.getName().equals("onGetTicketNoPasswd")) {
//                    Log.d("xcl_debug", "handleLoadPackage: find Method onGetTicketNoPasswd");
//                    cls = method.getParameterTypes()[1];
//                    break;
//                }
//                i++;
//            }
//            Log.d("xcl_debug", "handleLoadPackage: arg1 " + cls.getName());
//            hook_method("com.tencent.open.agent.QuickLoginAuthorityActivity$1", loadPackageParam.classLoader, "onGetTicketNoPasswd", String.class, cls, Integer.TYPE, Bundle.class, new XC_MethodHook() { // from class: com.cqttech.myapplication.HookTest.8
//                protected void beforeHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) throws Throwable {
//                    Log.d("xcl_debug", "QuickLoginAuthorityActivity$1: onGetTicketNoPasswd --- hook arg-0  = " + methodHookParam.args[0] + "  arg-1 = " + Arrays.toString((byte[]) methodHookParam.args[1]) + "    arg-2 = " + methodHookParam.args[2] + "   arg-3 = " + methodHookParam.args[3]);
//                    Object obj = ((Bundle) methodHookParam.args[3]).get("st_temp");
//                    StringBuilder sb = new StringBuilder();
//                    sb.append("onGetTicketNoPasswd: st_temp_obj = ");
//                    sb.append(obj);
//                    Log.d("xcl_debug", sb.toString());
//                    if (obj != null) {
//                        Log.d("xcl_debug", "onGetTicketNoPasswd: st_temp = " + obj.getClass().getPackage().getName());
//                    }
//                    super.beforeHookedMethod(methodHookParam);
//                }
//            });
//            hook_method("android.content.Intent", loadPackageParam.classLoader, "setData", Uri.class, new XC_MethodHook() { // from class: com.cqttech.myapplication.HookTest.9
//                protected void afterHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) throws Throwable {
//                    Log.d("xcl_debug", "afterHookedMethod: " + Uri.decode(((Uri) methodHookParam.args[0]).toString()));
//                    super.afterHookedMethod(methodHookParam);
//                }
//            });
        }
    }

    private void hook_method(String str, ClassLoader classLoader, String str2, Object... objArr) {
        try {
            XposedHelpers.findAndHookMethod(str, classLoader, str2, objArr);
        } catch (Exception e) {
            XposedBridge.log(e);
        }
    }
}
