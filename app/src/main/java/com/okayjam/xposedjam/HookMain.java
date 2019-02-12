package com.okayjam.xposedjam;


import android.os.Bundle;
import android.widget.TextView;

import java.lang.reflect.Field;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class HookMain implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        if(loadPackageParam.packageName.equals("com.okayjam.xposedjam")) {
            XposedHelpers.findAndHookMethod("com.okayjam.xposedjam.MainActivity",
                    loadPackageParam.classLoader, "onCreate", Bundle.class, new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            super.beforeHookedMethod(param);
                        }

                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                           // super.afterHookedMethod(param);
                            Class c = loadPackageParam.classLoader.loadClass("com.okayjam.xposedjam.MainActivity");
                            Field field = c.getDeclaredField("tv");
                            field.setAccessible(true);
                            XposedBridge.log("Test=========================================okay");
                            TextView tv = (TextView) field.get(param.thisObject);
                            tv.setText("hook");
                        }
                    }
            );
        }
        //这里测试Hook静态变量,修改手机机型和厂商
        XposedHelpers.setStaticObjectField(android.os.Build.class, "MANUFACTURER", "Jam");//厂商
        XposedHelpers.setStaticObjectField(android.os.Build.class, "MODEL", "okayjam.com");//机型
        //XposedBridge.log(loadPackageParam.packageName);
        //XposedBridge.log("Jam========================================================++++++++++++++++=======================");

    }
}
