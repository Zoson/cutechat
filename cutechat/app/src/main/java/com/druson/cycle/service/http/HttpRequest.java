package com.druson.cycle.service.http;

/**
 * Created by zoson on 3/15/15.
 */

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class HttpRequest {
    public static final String TAG = HttpRequest.class.getSimpleName();
    public static final int GET = 0x1000;
    public static final int POST = 0x1001;
    public static final int UP_FILE = 0x1002;
    public static final int DOWN_FILE = 0x1003;
    public static final int DOWN_IMG = 0x1004;

    public static final int SUCC = 0x1;
    public static final int FAIL = 0x2;

    public static Response sendGet(Request request){
        BufferedReader in = null;
        String url = getUrl(request.url,request.api,request.getStringParams());
        Log.i(TAG, "sendGet start " + url);
        StringBuilder data = new StringBuilder();
        Response response = new Response();
        try {
            URL realUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection)realUrl.openConnection();
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("Connection", "Close");
            connection.setRequestProperty("contentType", "utf-8");
            connection.connect();
            Map<String,List<String>> map = connection.getHeaderFields();
            System.out.println("GET " + map);
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = null;
            while ((line = in.readLine())!=null){
                data.append(line);
            }
            response.data_string = data.toString();
            response.state = SUCC;
            in.close();
            Log.i(TAG, "sendGet OK " + url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            response.state = FAIL;
            Log.i(TAG, "URL error may be the Request is not correct");
        }catch (IOException e){
            e.printStackTrace();
            Log.i(TAG, "connection stream error");
            response.state = FAIL;
        }finally {
            try {
                if (in!=null)in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return response;
    }

    public static Response sendPost(Request request){
        Log.i(TAG, "sendPost start " + request.url + "/" + request.api+" params "+request.getStringParams());
        PrintWriter out = null;
        BufferedReader in = null;
        StringBuilder data = new StringBuilder();
        Response response = new Response();
        try {
            URL realUrl = new URL(getUrl(request.url,request.api,""));
            HttpURLConnection connection = (HttpURLConnection)realUrl.openConnection();
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            out = new PrintWriter(connection.getOutputStream());
            out.print(request.getStringParams());
            out.flush();
            Log.i(TAG, "conn=" + connection.getHeaderFields());
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = null;
            while ((line=in.readLine())!=null){
                data.append(line);
            }
            Log.i(TAG, " SendPost " + data.toString());
            System.out.println(TAG + " SendPost " + data.toString());
            response.data_string = data.toString();
            response.state = SUCC;
            in.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            response.state = FAIL;
        } catch (IOException e){
            e.printStackTrace();
            response.state = FAIL;
        }finally {
            try {
                if (in!=null)in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (out!=null)out.close();
        }
        return response;
    }

    protected static String getUrl(String ip,String api,String param){
        StringBuilder url = new StringBuilder();
        if(api.equals("")){
            System.out.println(ip);
            return ip;
        }else {
            if (param.equals("")) {
                url.append(ip).append("/").append(api).append("/");
            } else {
                url.append(ip).append("/").append(api).append("/?").append(param);
            }
            return url.toString();
        }
    }

    public static Response getFile(String urlString) {
        Response response = new Response();
        BufferedInputStream is;
        String url = urlString;
        try {
            System.out.println("getPhoto:" + urlString);
            URL imgUrl = new URL(url);
            // 使用HttpURLConnection打开连接
            HttpURLConnection connection = (HttpURLConnection) imgUrl
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            // 将得到的数据转化成InputStream
            is = new BufferedInputStream(connection.getInputStream());
            // 将InputStream转换成Bitmap
            byte[] bytes = new byte[1024];
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int count = 0;
            while ((count=is.read(bytes))!=-1){
                out.write(bytes,0,count);
            }
            response.data_bytes = out.toByteArray();
            out.close();
//            BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inSampleSize = 2;
//            Bitmap bitmap = BitmapFactory.decodeStream(is, null, options);
            response.state = SUCC;
            is.close();
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            System.out.println("[getNetWorkBitmap->]MalformedURLException");
            response.state = FAIL;
            response.data_string = "网络错误";
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("[getNetWorkBitmap->]IOException");
            response.state = FAIL;
            response.data_string = "网络错误";
            e.printStackTrace();
        }
        return response;
    }

    public static Response sendFile(Request request){
        Log.i(TAG, "sendFile " + request.url + "/" + request.api);
        Response response = new Response();
        StringBuilder data = new StringBuilder();
        BufferedReader in ;
        try {
            URL realUrl = new URL(getUrl(request.url,request.api,""));
            HttpURLConnection connection = (HttpURLConnection)realUrl.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Charset","UTF-8");
            //设置边界
            String BOUNDARY = "----------" + System.currentTimeMillis();
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
            //请求正文信息
            OutputStream out = new DataOutputStream(connection.getOutputStream());
            // 第一部分：
            Map<String,String> params = request.params;
            for (String key:params.keySet()){
                StringBuilder sb_param = new StringBuilder();
                sb_param.append("--").append(BOUNDARY).append("\r\n");
                sb_param.append("Content-Disposition: form-data; name=\""+key+"\"\r\n\r\n");
                sb_param.append(params.get(key));
                out.write(sb_param.toString().getBytes("utf-8"));
            }
            //写入传输文件
            Map<String,String> fileparams = request.fileparams;
            for (String key:fileparams.keySet()){
                File file = new File(fileparams.get(key));
                if (!(file.exists()&&file.isFile())){
                    continue;
                }
                StringBuilder sb_file = new StringBuilder();
                sb_file.append("\r\n");
                sb_file.append("--"); // ////////必须多两道线
                sb_file.append(BOUNDARY);
                sb_file.append("\r\n");
                sb_file.append("Content-Disposition: form-data;name=\""+key+"\";filename=\""
                        + file.getName() + "\"\r\n");
                sb_file.append("Content-Type:application/octet-stream\r\n\r\n");
                byte[] head = sb_file.toString().getBytes("utf-8");
                out.write(head);
                // 文件正文部分
                DataInputStream filein = new DataInputStream(new FileInputStream(file));
                int bytes = 0;
                byte[] bufferOut = new byte[1024];
                while ((bytes = filein.read(bufferOut)) != -1) {
                    out.write(bufferOut, 0, bytes);
                }
                filein.close();
                byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");// 定义最后数据分隔线
                out.write(foot);
            }
            out.flush();
            out.close();

            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = null;
            while ((line=in.readLine())!=null){
                data.append(line);
            }
            response.data_string = data.toString();
            in.close();
            response.state = SUCC;
            System.out.println("sendFile "+data.toString());
            Log.i(TAG, "sendFile " + request.url + "/" + request.api);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            response.state = FAIL;
        } catch (IOException e){
            e.printStackTrace();
            response.state = FAIL;
        }
        return response;
    }
}
