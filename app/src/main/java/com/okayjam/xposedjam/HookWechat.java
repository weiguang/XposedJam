package com.okayjam.xposedjam;


import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.Map;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static android.text.TextUtils.isEmpty;
import static de.robv.android.xposed.XposedBridge.log;
import static de.robv.android.xposed.XposedHelpers.callMethod;
import static de.robv.android.xposed.XposedHelpers.callStaticMethod;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import static de.robv.android.xposed.XposedHelpers.findClass;

public class HookWechat implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        XposedBridge.log(" ============ lpparam:"+ lpparam.appInfo+ lpparam.packageName);
        if(lpparam.packageName.equals("com.okayjam.xposedjam")) {
            XposedBridge.log("xposedjam ============ lpparam:"+ lpparam.appInfo+ lpparam.packageName);
        }
        if(lpparam.packageName.equals("com.tencent.mm")) {
            XposedBridge.log("jam we== lpparam:"+ lpparam.appInfo+ lpparam.packageName);
            findAndHookMethod("com.tencent.wcdb.database.SQLiteDatabase", lpparam.classLoader, "insert", String.class, String.class, ContentValues.class, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    ContentValues contentValues = (ContentValues) param.args[2];
                    String tableName = (String) param.args[0];
                    XposedBridge.log("------------------------insert start---------------------" + "\n\n");
                    XposedBridge.log("param args1:" + (String)param.args[0]);
                    XposedBridge.log("param args1:" + (String)param.args[1]);
                    log("param args3 contentValues:");
                    for (Map.Entry<String, Object> item : contentValues.valueSet())
                    {
                        if (item.getValue() != null) {
                            XposedBridge.log(item.getKey() + "---------" + item.getValue().toString());
                        } else {
                            XposedBridge.log(item.getKey() + "---------" + "null");
                        }
                    }
                    XposedBridge.log("------------------------insert over---------------------" + "\n\n");

                    if (TextUtils.isEmpty(tableName) || !tableName.equals("message")) {
                        return;
                    }
                    Integer type = contentValues.getAsInteger("type");
                    XposedBridge.log("type:"+ type);
                }
            });
        }
        //这里测试Hook静态变量,修改手机机型和厂商
        XposedHelpers.setStaticObjectField(android.os.Build.class, "MANUFACTURER", "Jam");//厂商
        XposedHelpers.setStaticObjectField(android.os.Build.class, "MODEL", "okayjam.com");//机型
        //XposedBridge.log(loadPackageParam.packageName);
        //XposedBridge.log("Jam========================================================++++++++++++++++=======================");

    }
}
