package cn.coderxiaoc.api;

import cn.coderxiaoc.dto.TokenResponse;
import cn.coderxiaoc.script.XianyuScript;
import com.alibaba.fastjson2.JSONObject;
import okhttp3.*;

import java.io.IOException;
import java.util.*;

public class XianyuApis {
    private final OkHttpClient client = new OkHttpClient();
    private final String url = "https://h5api.m.goofish.com/h5/mtop.taobao.idlemessage.pc.login.token/1.0/";

    public TokenResponse getToken(Map<String, String> cookies, String deviceId) throws IOException {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String token = cookies.get("_m_h5_tk").split("_")[0];

        // 构造 data 字符串
        String dataVal = String.format("{\"appKey\":\"444e9908a51d1cb236a27862abc769c9\",\"deviceId\":\"%s\"}", deviceId);

        // 生成签名
        String sign = XianyuScript.generateSign(timestamp, token, deviceId);

        // 构造 URL 参数
        HttpUrl httpUrl = HttpUrl.parse(url).newBuilder()
                .addQueryParameter("jsv", "2.7.2")
                .addQueryParameter("appKey", "34839810")
                .addQueryParameter("t", timestamp)
                .addQueryParameter("sign", sign)
                .addQueryParameter("v", "1.0")
                .addQueryParameter("type", "originaljson")
                .addQueryParameter("accountSite", "xianyu")
                .addQueryParameter("dataType", "json")
                .addQueryParameter("timeout", "20000")
                .addQueryParameter("api", "mtop.taobao.idlemessage.pc.login.token")
                .addQueryParameter("sessionOption", "AutoLoginOnly")
                .addQueryParameter("spm_cnt", "a21ybx.im.0.0")
                .build();

        // 构造表单 body
        RequestBody body = new FormBody.Builder()
                .add("data", dataVal)
                .build();


        // 构造请求头（逐项一模一样）
        Request.Builder requestBuilder = new Request.Builder()
                .url(httpUrl)
                .post(body)
                .header("accept", "application/json")
                .header("accept-language", "zh-CN,zh;q=0.9")
                .header("cache-control", "no-cache")
                .header("origin", "https://www.goofish.com")
                .header("pragma", "no-cache")
                .header("priority", "u=1, i")
                .header("referer", "https://www.goofish.com/")
                .header("sec-ch-ua", "\"Not(A:Brand\";v=\"99\", \"Google Chrome\";v=\"133\", \"Chromium\";v=\"133\"")
                .header("sec-ch-ua-mobile", "?0")
                .header("sec-ch-ua-platform", "\"Windows\"")
                .header("sec-fetch-dest", "empty")
                .header("sec-fetch-mode", "cors")
                .header("sec-fetch-site", "same-site")
                .header("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/133.0.0.0 Safari/537.36");

        // 构造 Cookie Header 字符串
        StringBuilder cookieBuilder = new StringBuilder();
        for (Map.Entry<String, String> entry : cookies.entrySet()) {
            cookieBuilder.append(entry.getKey()).append('=').append(entry.getValue()).append("; ");
        }
        requestBuilder.header("cookie", cookieBuilder.toString());

        Request request = requestBuilder.build();


        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected HTTP response: " + response);
            }
            System.out.println();
            return JSONObject.parseObject(response.body().string(), TokenResponse.class);
        }
    }
}
