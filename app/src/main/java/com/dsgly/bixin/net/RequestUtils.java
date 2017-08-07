package com.dsgly.bixin.net;

import android.text.TextUtils;
import android.util.Log;

import com.dsgly.bixin.net.requestParam.BaseParam;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by bjdengxuan1 on 2017/6/19.
 */

public class RequestUtils {

    private static OkHttpClient httpClient;

    public static void initOkhttp(){
        if(httpClient == null){
            httpClient = new OkHttpClient();
        }
    }

    public static void startGetRequest(NetworkParam networkParam){
        if(networkParam == null || networkParam.key == null){
            throw new IllegalArgumentException("request url must not be empty");
        }
        String getParams="";
        if(networkParam.param != null) {
            getParams = assembleGetParam(networkParam.param);
        }
        String requestUrl = networkParam.key.getHostPath()+networkParam.key.getApi()+getParams;
        if(!TextUtils.isEmpty(requestUrl)) {
            if(networkParam.block){
                networkParam.handler.sendEmptyMessage(NetworkParam.NET_SHOW_PROGRESS);
            }
            startRequest(requestUrl,null,networkParam.callback,networkParam.headers);
        }
    }

    /*public static void startRequest(String url, RequestBody body, Callback callback){
        *//*initOkhttp();
        Request request;
        if(body != null){
            request = new Request.Builder().url(url).post(body).addHeader("token-param",createRandom(false,16)).build();
            Log.i("network",url+body.toString());
        }else {
            request = new Request.Builder().url(url).build();
            Log.i("network",url);
        }
        httpClient.newCall(request).enqueue(callback);*//*
        startRequest(url,body,callback,null);
    }*/

    public static void startRequest(String url, RequestBody body, Callback callback,Map<String,String> headers){
        initOkhttp();
        Request request;
        Request.Builder builder;
        if(body != null){
            builder = new Request.Builder().url(url).post(body);
            Log.i("network",url+body.toString());
        }else {
            builder = new Request.Builder().url(url);
            Log.i("network",url);
        }
        if(headers != null){
            Set<String> keyset = headers.keySet();
            for(String key : keyset){
                builder.addHeader(key,headers.get(key));
            }
        }
        request = builder.build();
        httpClient.newCall(request).enqueue(callback);
    }

    public static void startRequest(NetworkParam networkParam){
        initOkhttp();
        if(networkParam == null || networkParam.key == null){
            throw new IllegalArgumentException("request url must not be empty");
        }
        Map<String,String> params = getRequestBodyFromBaseParam(networkParam.param);
        FormBody.Builder builder = new FormBody.Builder();
        for (String key : params.keySet()) {
            builder.add(key, params.get(key));
        }
        String requestUrl = networkParam.key.getHostPath()+networkParam.key.getApi();
        if(!TextUtils.isEmpty(requestUrl)) {
            if(networkParam.block){
                networkParam.handler.sendEmptyMessage(NetworkParam.NET_SHOW_PROGRESS);
            }
            startRequest(requestUrl, builder.build(), networkParam.callback,networkParam.headers);
        }
    }

    public static void uploadFile(String url, String fileName, Callback callback) {
        initOkhttp();
        //判断文件类型
        MediaType MEDIA_TYPE = MediaType.parse(judgeType(fileName));
        //创建文件参数
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(MEDIA_TYPE.type(), fileName,
                        RequestBody.create(MEDIA_TYPE, new File(fileName)));
        //发出请求参数
        Request request = new Request.Builder()
                .url(url)
                .post(builder.build())
                .build();
        httpClient.newCall(request).enqueue(callback);
    }

    public static void downLoadFile(String url, final String fileDir, final String fileName,final NetProgress progressListener){
        initOkhttp();
        Request request = new Request.Builder()
                .url(url)
                .build();
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                if(response == null || response.body() == null){
                    return;
                }
                try {
                    is = response.body().byteStream();
                    File file = new File(fileDir, fileName);
                    fos = new FileOutputStream(file);
                    long totalSize = response.body().contentLength();
                    long sum = 0;
                    while ((len = is.read(buf)) != -1) {
                        sum += len;
                        int progress = (int) (sum * 1.0f/totalSize * 100);
                        if(progressListener != null){
                            progressListener.onProgress(progress);
                        }
                        fos.write(buf, 0, len);
                    }
                    fos.flush();
                    if(progressListener != null){
                        progressListener.onComplete();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (is != null) is.close();
                    if (fos != null) fos.close();
                }
            }
        });
    }

    private static String judgeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }


    private static Map<String,String> getRequestBodyFromBaseParam(BaseParam param){
        Map<String,String> map = new HashMap<>();
        if(param != null){
            Class<?> clz = param.getClass();
            for(Field f:clz.getDeclaredFields()){
                f.setAccessible(true);
                String name = f.getName();
                if(name.startsWith("$")||"serialVersionUID".equals(name)){
                    continue;
                }
                try {
                    if(f.get(param)!=null) {
                        map.put(name, f.get(param).toString());
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return map;
    }

    private static String assembleGetParam(BaseParam param){
        StringBuilder builder = new StringBuilder("?");
        if(param != null){
            Class<?> clz = param.getClass();
            for(Field f:clz.getDeclaredFields()){
                f.setAccessible(true);
                String name = f.getName();
                if(name.startsWith("$")||"serialVersionUID".equals(name)){
                    continue;
                }
                try {
                    if(f.get(param)!=null) {
                        builder.append(name).append("=").append(f.get(param).toString()).append("&");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if(builder.toString().length()>0){
            return builder.toString().substring(0,builder.toString().length()-1);
        }
        return builder.toString();
    }
}
