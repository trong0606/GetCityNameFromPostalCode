package com.example.desktop.getcitynamefrompostalcode;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;


import com.android.volley.toolbox.HttpResponse;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.net.InetAddress;
import java.net.UnknownHostException;

//Class dùng để cấu hình Ip
public class IpConfig {
    public static final String LOG_TAG = "IpConfig.java";
    public static final int MODE_PRIVATE = 0;
    public static final String domainserver = "10.10.5.244";
    public static String httpURL = "http://192.168.100.120:7777";
    public static final String ipdefault = "112.109.89.79";
    public static final String ipserver = "10.10.5.244";
    public static String password = "123456";
    public static final String portdefault = "9998";
    public static final String portserver = "7777";
    public static final String url = "http://10.10.100.15:8080/alfresco/cmisatom";
    public static final String username = "admin";
//    private ConnectionDetector cd;
    private Context context;
    private Dialog dialog;
    private SharedPreferences.Editor editor;
    private SharedPreferences pref;


    public IpConfig(Context context) {
        this.context = context;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean isPortOpen(java.lang.String r9, int r10, int r11) {
        throw new UnsupportedOperationException("Method not decompiled: com.example.desktop.getcitynamefrompostalcode.IpConfig.isPortOpen(java.lang.String, int, int):boolean");
    }
    public boolean checkconnectserver() {
        this.pref = this.context.getSharedPreferences("MyPref", 0);
        this.editor = this.pref.edit();
        this.editor.putString("ipPort", "7777");
        this.editor.putString("ipUrl", "10.10.5.244");
        this.editor.putString("ipDomain", "10.10.5.244");
        this.editor.commit();
        Boolean flagCheckconnect = Boolean.valueOf(false);
        String ipUrl = this.pref.getString("ipUrl", "");
        String ipPort = this.pref.getString("ipPort", "");
        String ipDomain = this.pref.getString("ipDomain", "");
        if (ipUrl.equals("")) {
            ipUrl = "10.10.5.244";
        }
        if (ipPort.equals("")) {
            ipPort = "7777";
        }
        if (ipDomain.equals("")) {
            ipDomain = "10.10.5.244";
        }
        Log.v(LOG_TAG, "ipUrl=" + ipUrl + "\t" + "ipPort=" + ipPort);
//        this.cd = new ConnectionDetector(this.context);
        Boolean isInternetPresent = Boolean.valueOf(false);
//        isInternetPresent = Boolean.valueOf(this.cd.isConnectingToInternet());
        Log.v(LOG_TAG, "isInternetPresent=" + isInternetPresent);
        if (isInternetPresent.booleanValue()) {
            InetAddress address = null;
            try {
                address = InetAddress.getByName(ipDomain);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
            boolean connectserver = false;
            if (address != null) {
                Log.v(LOG_TAG, "domain = " + ipDomain);
                try {
                    connectserver = isPortOpen(address.getHostAddress().toString(), Integer.parseInt(ipPort), 5000);
                } catch (Exception e2) {
//                    Toast.makeText(this.context, this.context.getResources().getString(C0220R.string.serverdisconnect), 2).show();
                }
                this.editor.putString("ipUrl", address.getHostAddress().toString().trim());
                Log.v(LOG_TAG, "domain Ip:" + address.getHostAddress());
            } else {
                connectserver = false;
            }
            this.editor.commit();
            Log.v(LOG_TAG, "connect server  :" + connectserver);
            if (connectserver) {
                Log.v(LOG_TAG, "urlCheck pref:" + address.getHostAddress().toString().trim() + ":" + ipPort);
                flagCheckconnect = Boolean.valueOf(connectServer(address.getHostAddress().toString().trim(), ipPort));
            } else {
                Log.v(LOG_TAG, "Can not Post to server");
//                Toast.makeText(this.context, this.context.getResources().getString(C0220R.string.serverdisconnect), 0).show();
            }
        }
        return flagCheckconnect.booleanValue();
    }

    public boolean connectServer(String urlCheck, String urlCheckPort) {
        Boolean checkconnect = Boolean.valueOf(false);
        HttpClient httpclient = new DefaultHttpClient();
        JSONObject dato = new JSONObject();
        try {
            Log.v(LOG_TAG, "connect gateway:http://" + urlCheck + ":" + urlCheckPort);
            HttpGet httpGet = new HttpGet("http://" + urlCheck + ":" + urlCheckPort + "/fe/api/connectgateway");
            httpGet.setHeader("content-type", "application/json;charset=UTF-8");
            org.apache.http.HttpResponse response = httpclient.execute(httpGet);
            if (response != null) {
                String status = new JSONObject(EntityUtils.toString(response.getEntity(), "UTF-8")).getString("status");
                if (status.equals("true")) {
                    checkconnect = Boolean.valueOf(true);
                } else {
//                    Toast.makeText(this.context, this.context.getResources().getString(C0220R.string.serverdisconnect), 0).show();
                }
                Log.v(LOG_TAG, "status:" + status);
            }
        } catch (Exception e) {
            Log.v(LOG_TAG, "Can not Post to server");
//            Toast.makeText(this.context, this.context.getResources().getString(C0220R.string.serverdisconnect), 0).show();
        }
        return checkconnect.booleanValue();
    }

    public void showAlertDialog(Context context, String title, String message, Boolean status) {
        this.dialog = new Dialog(context);
        this.dialog.requestWindowFeature(1);
//        this.dialog.setContentView(C0220R.layout.dialoglayoutnointernet);
        this.dialog.show();
    }
}

