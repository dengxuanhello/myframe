package com.dsgly.bixin.biz.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.XmlResourceParser;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
/**
 * 用于scheme跳转
 * Created by bjdengxuan1 on 2017/6/23.
 */
public abstract class SchemeDispatcher
{
    public static final String RESULT_BROADCAST_MESSAGE_ACTION = "_RESULT_BROADCAST_MESSAGE_ACTION_";
    private static String homeScheme = null;
    private static final String HTTP_SCHEME_HEAD = "http://scrimmage.xxx.com/bs?url=";

    public static String getHomeScheme(Context ctx){
        if (TextUtils.isEmpty(homeScheme)) {
            homeScheme = getMetaData(ctx.getApplicationContext(), "MAIN_SCHEME");
        }
        return homeScheme;
    }

    public static String getMetaData(Context app, String name){
        Bundle bundle = app.getApplicationInfo().metaData;
        if ((bundle != null) && (!bundle.isEmpty())) {
            return bundle.getString(name);
        }
        XmlResourceParser parser = null;
        AssetManager assmgr = null;
        try
        {
            assmgr = (AssetManager)AssetManager.class.newInstance();
            Method m = AssetManager.class.getDeclaredMethod("addAssetPath", new Class[] { String.class });
            m.setAccessible(true);

            int cookie = ((Integer)m.invoke(assmgr, new Object[] { app.getApplicationInfo().sourceDir })).intValue();
            if (cookie != 0)
            {
                String ANDROID_RESOURCES = "http://schemas.android.com/apk/res/android";
                parser = assmgr.openXmlResourceParser(cookie, "AndroidManifest.xml");
                boolean findAppMetadata = false;
                int event = parser.getEventType();
                while (event != 1)
                {
                    switch (event)
                    {
                        case 2:
                            String nodeName = parser.getName();
                            String metadataName;
                            if ("meta-data".equals(nodeName))
                            {
                                findAppMetadata = true;
                                metadataName = parser.getAttributeValue(ANDROID_RESOURCES, "name");
                                if (metadataName.equals(name)) {
                                    return parser.getAttributeValue(ANDROID_RESOURCES, "value");
                                }
                            }
                            else if (findAppMetadata)
                            {
                                return null;
                            }
                            break;
                    }
                    event = parser.next();
                }
            }
        }
        catch (Throwable e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (parser != null) {
                parser.close();
            }
            if (assmgr != null) {
                assmgr.close();
            }
        }
        return null;
    }

    public static void sendSchemeAndClearStack(Fragment context, String url)
    {
        sendSchemeAndClearStack(context, url, getHomeScheme(context.getActivity()));
    }

    public static void sendSchemeAndClearStack(Context context, String url)
    {
        sendSchemeAndClearStack(context, url, getHomeScheme(context));
    }

    public static void sendSchemeAndClearStack(Fragment context, String url, Bundle bundle)
    {
        sendSchemeAndClearStack(context, getHomeScheme(context.getActivity()), url, bundle);
    }

    public static void sendSchemeAndClearStack(Context context, String url, Bundle bundle)
    {
        sendSchemeAndClearStack(context, getHomeScheme(context), url, bundle);
    }

    public static void sendSchemeAndClearStack(Fragment context, String launcherScheme, String url)
    {
        sendSchemeAndClearStack(context, launcherScheme, url, null);
    }

    public static void sendSchemeAndClearStack(Context context, String launcherScheme, String url)
    {
        sendSchemeAndClearStack(context, launcherScheme, url, null);
    }

    public static void sendSchemeAndClearStack(Fragment context, String launcherScheme, String url, Bundle bundle)
    {
        if (url.toLowerCase().startsWith(HTTP_SCHEME_HEAD))
        {
            url = url.substring(HTTP_SCHEME_HEAD.length());
            try
            {
                url = URLDecoder.decode(url, "UTF-8");
            }
            catch (UnsupportedEncodingException e) {}
        }
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(launcherScheme));

        intent.addFlags(67108864);
        context.startActivity(intent);

        intent = new Intent("android.intent.action.VIEW", Uri.parse(url));
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        context.startActivity(intent);
    }

    public static void sendSchemeAndClearStack(Context context, String launcherScheme, String url, Bundle bundle)
    {
        if (url.toLowerCase().startsWith(HTTP_SCHEME_HEAD))
        {
            url = url.substring(HTTP_SCHEME_HEAD.length());
            try
            {
                url = URLDecoder.decode(url, "UTF-8");
            }
            catch (UnsupportedEncodingException e) {}
        }
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(launcherScheme));

        intent.addFlags(67108864);
        context.startActivity(intent);

        intent = new Intent("android.intent.action.VIEW", Uri.parse(url));
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        context.startActivity(intent);
    }

    public static void sendSchemeForResultAndClearStack(Fragment activity, String url, int requestCode)
    {
        sendSchemeForResultAndClearStack(activity, getHomeScheme(activity.getActivity()), url, requestCode);
    }

    public static void sendSchemeForResultAndClearStack(Activity activity, String url, int requestCode)
    {
        sendSchemeForResultAndClearStack(activity, getHomeScheme(activity), url, requestCode);
    }

    public static void sendSchemeForResultAndClearStack(Fragment activity, String launcherScheme, String url, int requestCode)
    {
        sendSchemeForResultAndClearStack(activity, launcherScheme, url, requestCode, null);
    }

    public static void sendSchemeForResultAndClearStack(Activity activity, String launcherScheme, String url, int requestCode)
    {
        sendSchemeForResultAndClearStack(activity, launcherScheme, url, requestCode, null);
    }

    public static void sendSchemeForResultAndClearStack(Fragment activity, String launcherScheme, String url, int requestCode, Bundle bundle)
    {
        if (url.toLowerCase().startsWith(HTTP_SCHEME_HEAD))
        {
            url = url.substring(HTTP_SCHEME_HEAD.length());
            try
            {
                url = URLDecoder.decode(url, "UTF-8");
            }
            catch (UnsupportedEncodingException e) {}
        }
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(launcherScheme));

        intent.addFlags(67108864);
        activity.startActivity(intent);

        intent = new Intent("android.intent.action.VIEW", Uri.parse(url));
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        activity.startActivityForResult(intent, requestCode);
    }

    public static void sendSchemeForResultAndClearStack(Activity context, String launcherScheme, String url, int requestCode, Bundle bundle)
    {
        if (url.toLowerCase().startsWith(HTTP_SCHEME_HEAD))
        {
            url = url.substring(HTTP_SCHEME_HEAD.length());
            try
            {
                url = URLDecoder.decode(url, "UTF-8");
            }
            catch (UnsupportedEncodingException e) {}
        }
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(launcherScheme));

        intent.addFlags(67108864);
        context.startActivity(intent);

        intent = new Intent("android.intent.action.VIEW", Uri.parse(url));
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        context.startActivityForResult(intent, requestCode);
    }

    public static void sendScheme(Fragment context, String url)
    {
        sendScheme(context, url, null, false, 0);
    }

    public static void sendScheme(Context context, String url)
    {
        sendScheme(context, url, null, false, 0);
    }

    public static void sendScheme(Fragment context, String url, Bundle bundle)
    {
        sendScheme(context, url, bundle, false, 0);
    }

    public static void sendScheme(Context context, String url, Bundle bundle)
    {
        sendScheme(context, url, bundle, false, 0);
    }

    public static void sendScheme(Fragment context, String url, boolean clearTop)
    {
        sendScheme(context, url, null, clearTop, 0);
    }

    public static void sendScheme(Context context, String url, boolean clearTop)
    {
        sendScheme(context, url, null, clearTop, 0);
    }

    public static void sendScheme(Fragment context, String url, int flag)
    {
        sendScheme(context, url, null, false, flag);
    }

    public static void sendScheme(Context context, String url, int flag)
    {
        sendScheme(context, url, null, false, flag);
    }

    public static void sendScheme(Fragment context, String url, Bundle bundle, boolean clearTop, int flag)
    {
        if (url.toLowerCase().startsWith(HTTP_SCHEME_HEAD))
        {
            url = url.substring(HTTP_SCHEME_HEAD.length());
            try
            {
                url = URLDecoder.decode(url, "UTF-8");
            }
            catch (UnsupportedEncodingException e) {}
        }
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(url));
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        if (clearTop) {
            intent.addFlags(67108864);
        }
        if (flag != 0) {
            intent.setFlags(flag);
        }
        context.startActivity(intent);
    }

    public static void sendScheme(Context context, String url, Bundle bundle, boolean clearTop, int flag)
    {
        if (url.toLowerCase().startsWith(HTTP_SCHEME_HEAD))
        {
            url = url.substring(HTTP_SCHEME_HEAD.length());
            try
            {
                url = URLDecoder.decode(url, "UTF-8");
            }
            catch (UnsupportedEncodingException e) {}
        }
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(url));
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        if (clearTop) {
            intent.addFlags(67108864);
        }
        if (flag != 0) {
            intent.setFlags(flag);
        }
        context.startActivity(intent);
    }

    public static void sendSchemeForResult(Fragment activity, String url, int requestCode)
    {
        sendSchemeForResult(activity, url, requestCode, null);
    }

    public static void sendSchemeForResult(Activity activity, String url, int requestCode)
    {
        sendSchemeForResult(activity, url, requestCode, null);
    }

    public static void sendSchemeForResult(Fragment activity, String url, int requestCode, int flag)
    {
        sendSchemeForResult(activity, url, requestCode, null, flag);
    }

    public static void sendSchemeForResult(Activity activity, String url, int requestCode, int flag)
    {
        sendSchemeForResult(activity, url, requestCode, null, flag);
    }

    public static void sendSchemeForResult(Fragment activity, String url, int requestCode, Bundle bundle)
    {
        sendSchemeForResult(activity, url, requestCode, bundle, 0);
    }

    public static void sendSchemeForResult(Activity activity, String url, int requestCode, Bundle bundle)
    {
        sendSchemeForResult(activity, url, requestCode, bundle, 0);
    }

    public static void sendSchemeForResult(Fragment activity, String url, int requestCode, Bundle bundle, int flag)
    {
        if (url.toLowerCase().startsWith(HTTP_SCHEME_HEAD))
        {
            url = url.substring(HTTP_SCHEME_HEAD.length());
            try
            {
                url = URLDecoder.decode(url, "UTF-8");
            }
            catch (UnsupportedEncodingException e) {}
        }
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(url));
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        if (flag != 0) {
            intent.setFlags(flag);
        }
        activity.startActivityForResult(intent, requestCode);
    }

    public static void sendSchemeForResult(Activity activity, String url, int requestCode, Bundle bundle, int flag)
    {
        if (url.toLowerCase().startsWith(HTTP_SCHEME_HEAD))
        {
            url = url.substring(HTTP_SCHEME_HEAD.length());
            try
            {
                url = URLDecoder.decode(url, "UTF-8");
            }
            catch (UnsupportedEncodingException e) {}
        }
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(url));
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        if (flag != 0) {
            intent.setFlags(flag);
        }
        activity.startActivityForResult(intent, requestCode);
    }
}
