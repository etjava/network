package com.etjava.test;

import java.nio.charset.Charset;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

public class Test {

	static int socketTimeout = 60000;// 请求超时时间
    static int connectTimeout = 60000;// 传输超时时间
    
    /**
     * 使用SOAP1.1发送消息
     *
     * @param postUrl 请求地址
     * @param soapXml 请求xml
     * @param soapAction 需要请求的接口
     * @return
     */
    public static String doPostSoap(String postUrl, String soapXml,String soapAction) {
        String retStr = "";
        // 创建HttpClientBuilder
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        // HttpClient
        CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
        HttpPost httpPost = new HttpPost(postUrl);
        //  设置请求和传输超时时间
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(socketTimeout)
                .setConnectTimeout(connectTimeout).build();
        httpPost.setConfig(requestConfig);
        try {
            httpPost.setHeader("Content-Type", "text/xml;charset=UTF-8");
            httpPost.setHeader("SOAPAction", soapAction);
            StringEntity data = new StringEntity(soapXml,
                    Charset.forName("UTF-8"));
            httpPost.setEntity(data);
            CloseableHttpResponse response = closeableHttpClient
                    .execute(httpPost);
            HttpEntity httpEntity = response.getEntity();
            if (httpEntity != null) {
                // 打印响应内容
                retStr = EntityUtils.toString(httpEntity, "UTF-8");
            }
            // 释放资源
            closeableHttpClient.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retStr;
    }

    public static void main(String[] args) {
        String weatherSoapXml = " <soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:web=\"http://WebXml.com.cn/\">"+
        	   " <soapenv:Header/>"+
        	   " <soapenv:Body>"+
        	     " <web:getSupportCity>"+
        	         "<web:byProvinceName>广东</web:byProvinceName>"+
        	      "</web:getSupportCity>"+
        	   "</soapenv:Body>"+
        	"</soapenv:Envelope>";
        String postUrl = "http://www.webxml.com.cn/WebServices/WeatherWebService.asmx";
        //采用SOAP1.1调用服务端，这种方式能调用服务端为soap1.1和soap1.2的服务
        String result = doPostSoap(postUrl, weatherSoapXml, "http://WebXml.com.cn/getSupportCity");
        System.out.println(result);
    }
}
