package com.dsgly.bixin.net;

import android.text.TextUtils;
import android.util.Log;

import com.dsgly.bixin.net.requestParam.BaseParam;
import com.dsgly.bixin.utils.UCUtils;

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
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

/**
 * Created by bjdengxuan1 on 2017/6/19.
 */

public class RequestUtils {

    private static OkHttpClient httpClient;
    private static final MediaType MEDIA_OBJECT_STREAM = MediaType.parse("application/octet-stream");
    public static void initOkhttp(){
        if(httpClient == null){
            httpClient = new OkHttpClient();
            /*httpClient = new OkHttpClient.Builder()
                    .connectTimeout(6, TimeUnit.SECONDS)
                    .readTimeout(2, TimeUnit.SECONDS)
                    .writeTimeout(6, TimeUnit.SECONDS)
                    .build();*/
        }
    }

    public static void requestUrl(String url,NetworkParam networkParam){
        FormBody.Builder bodyBuilder = new FormBody.Builder();
        Request.Builder builder = new Request.Builder().url(url).post(bodyBuilder.build());
        Request request = builder.build();
        httpClient.newCall(request).enqueue(networkParam.callback);
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

    public static void startGetRequestExt(NetworkParam networkParam,String oneParameter){
        if(networkParam == null || networkParam.key == null){
            throw new IllegalArgumentException("request url must not be empty");
        }
        String requestUrl = networkParam.key.getHostPath()+networkParam.key.getApi();
        if(requestUrl.endsWith("/")){
            requestUrl = requestUrl + oneParameter;
        }else {
            requestUrl = requestUrl + "/" + oneParameter;
        }
        if(!TextUtils.isEmpty(requestUrl)) {
            if(networkParam.block){
                networkParam.handler.sendEmptyMessage(NetworkParam.NET_SHOW_PROGRESS);
            }
            startRequest(requestUrl,null,networkParam.callback,networkParam.headers);
        }
    }

    public static void startPostRequestExt(NetworkParam networkParam,String oneParameter){
        if(networkParam == null || networkParam.key == null){
            throw new IllegalArgumentException("request url must not be empty");
        }
        String requestUrl = networkParam.key.getHostPath()+networkParam.key.getApi();
        if(requestUrl.endsWith("/")){
            requestUrl = requestUrl + oneParameter;
        }else {
            requestUrl = requestUrl + "/" + oneParameter;
        }
        if(!TextUtils.isEmpty(requestUrl)) {
            if(networkParam.block){
                networkParam.handler.sendEmptyMessage(NetworkParam.NET_SHOW_PROGRESS);
            }
            startRequest(requestUrl,new FormBody.Builder().build(),networkParam.callback,networkParam.headers);
        }
    }

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

    public static void sendMoment(String url,String cntent,String videoPath,String coverPath,Callback callback){
        initOkhttp();
    }

    public static void uploadFile(String url, String fileName,String pic, Callback callback) {
        initOkhttp();

        //判断文件类型
        MediaType MEDIA_TYPE1 = MediaType.parse(judgeType(pic));
        MediaType MEDIA_TYPE2 = MediaType.parse(judgeType(fileName));

        MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        File file1 = new File(fileName);
        File file2 = new File(pic);

        /*requestBody.addFormDataPart("mp4", file1.getName(), createProgressRequestBody(MEDIA_OBJECT_STREAM, file1));
        requestBody.addFormDataPart("png", file1.getName(), createProgressRequestBody(MEDIA_OBJECT_STREAM, file2));*/
        if(file1 != null){
            // MediaType.parse() 里面是上传的文件类型。
            RequestBody body = RequestBody.create(MEDIA_TYPE2, file1);
            String filename = file1.getName();
            // 参数分别为， 请求key ，文件名称 ， RequestBody
            requestBody.addFormDataPart("sss.mp4", file1.getName(), body);
        }
        if(file2 != null){
            // MediaType.parse() 里面是上传的文件类型。
            RequestBody body = RequestBody.create(MEDIA_TYPE1, file2);
            String filename = file2.getName();
            // 参数分别为， 请求key ，文件名称 ， RequestBody
            requestBody.addFormDataPart("ttt.png", file2.getName(), body);
        }
        requestBody.addFormDataPart("meId", UCUtils.meId);
        requestBody.addFormDataPart("content","dengxuan1 test");
        Request request = new Request.Builder().url(url).post(requestBody.build()).build();
        httpClient.newBuilder().writeTimeout(50, TimeUnit.SECONDS).build().newCall(request).enqueue(callback);
        //httpClient.newBuilder().readTimeout(5000, TimeUnit.MILLISECONDS).build().newCall(request).enqueue(callback);
        // readTimeout("请求超时时间" , 时间单位);
        //创建文件参数
        /*MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", "ss.jpg",
                        RequestBody.create(MEDIA_TYPE1, new File(pic)))
                .addFormDataPart("file", "xx.mp4",
                        RequestBody.create(MEDIA_TYPE2, new File(fileName)))
                .addFormDataPart("meId", UCUtils.meId)
                .addFormDataPart("content","sssss");
        //发出请求参数
        Request request = new Request.Builder()
                .url(url)
                .post(builder.build())
                .build();
        httpClient.newCall(request).enqueue(callback);*/
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

    public static void uploadUserAvatar(String hostPath,String filePath,Callback callback){
        initOkhttp();
        //判断文件类型
        MediaType MEDIA_TYPE1 = MediaType.parse(judgeType(filePath));
        MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        File file1 = new File(filePath);
        if(file1.exists()){
            // MediaType.parse() 里面是上传的文件类型。
            RequestBody body = RequestBody.create(MEDIA_TYPE1, file1);
            String filename = file1.getName();
            // 参数分别为， 请求key ，文件名称 ， RequestBody
            requestBody.addFormDataPart("avatar", filename, body);
            requestBody.addFormDataPart("meId", UCUtils.meId);
            Request request = new Request.Builder().url(hostPath).post(requestBody.build()).build();
            httpClient.newBuilder().writeTimeout(50, TimeUnit.SECONDS).build().newCall(request).enqueue(callback);
        }
    }
}
