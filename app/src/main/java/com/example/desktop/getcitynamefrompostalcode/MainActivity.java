package com.example.desktop.getcitynamefrompostalcode;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.google.gson.Gson;


import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.example.desktop.getcitynamefrompostalcode.IpConfig.LOG_TAG;
import static org.apache.http.protocol.HTTP.UTF_8;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    AutoCompleteTextView textViewTinh, textViewHuyen, textViewXa, textViewMaHuyen;
    TextView txtCountryCode;
    private List<String> tentinhlist;
    private List<String> matinhlist;
    private List<String> tenhuyenlist;
    private List<String> mahuyenlist;
    private List<String> tenxalist;
    private List<String> Idxalist;
    private RequestQueue requestQueue;
    ArrayAdapter<String> arrayAdapter;
    ArrayAdapter<String> arrayAdapter2;
    ArrayAdapter<String> arrayAdapter3;
    private String tu_tinh = " ";
    private String tu_huyen = " ";
    private SharedPreferences pref;
    private Gson gson = new Gson();

    public String[] ten_tinh = {"Cao Bằng", "Lạng Sơn", "Quảng Ninh", "Hải Phòng", "Thái Bình",
            "Nam Ðịnh", "Phú Thọ", "Thái Nguyên", "Yên Bái", "Tuyên Quang", "Hà Giang", "Lào Cai",
            "Lai Châu", "Sơn La", "Điện Biên", "Hòa Bình", "Hà Nội", "Hải Dương", "Ninh Bình", " Thanh Hóa",
            "Nghệ An", "Hà Tĩnh", "Đà Nẵng", "Đaklak", "Đắc Nông", "Lâm Ðồng", "Hồ Chí Minh", "Đồng Nai", "Bình Dương",
            "Long An", "Tiền Giang", "Vĩnh Long", "Cần Thơ", "Đồng Tháp", "An Giang", "Kiên Giang", "Cà mau",
            "Tây Ninh", "Bến Tre", "Bà Rịa Vũng Tàu", "Quảng Bình", "Quảng Trị", "Thừa Thiên Huế", "Quảng Ngãi",
            "Bình Định", "Phú Yên", "Khánh Hòa", "Gia Lai", "KonTum", "Sóc Trăng", "Trà Vinh", "Ninh Thuận",
            "Bình Thuận", "Vĩnh Phúc", "Hưng yên", "Hà Nam", "Quảng Nam", "Bình Phước", "Bạc Liêu",
            "Hậu Giang", "Bắc Cạn", "Bắc Giang", "Bắc Ninh"
    };

    public String[] ma_tinh = {"11", "12", "14", "16", "17", "18", "19", "20", "21", "22", "23", "24",
            "25", "26", "27", "28", "29", "34", "35", "36", "37", "38", "43", "47", "48", "49", "50", "60",
            "61", "62", "63", "64", "65", "66", "67", "68", "69", "70", "71", "72", "73", "74", "75", "76",
            "77", "78", "79", "81", "82", "83", "84", "85", "86", "88", "89", "90", "92", "93", "94", "95",
            "97", "98", "99",
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Anhxa();
        arrayAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, ten_tinh);
        textViewTinh.setThreshold(1);
        textViewTinh.setAdapter(arrayAdapter);
        textViewTinh.setDropDownHeight(300);
        textViewTinh.setOnItemClickListener(this);


        tentinhlist = Arrays.asList(ten_tinh);
        matinhlist = Arrays.asList(ma_tinh);
        tenhuyenlist = new ArrayList<>();
        mahuyenlist = new ArrayList<>();
        tenxalist = new ArrayList<>();
        Idxalist = new ArrayList<>();

    }

    private void Anhxa() {
        txtCountryCode = (TextView) findViewById(R.id.textviewcodecountry);
        textViewTinh = (AutoCompleteTextView) findViewById(R.id.autotextviewtinh);
        textViewHuyen = (AutoCompleteTextView) findViewById(R.id.autotextviewhuyen);
        textViewXa = (AutoCompleteTextView) findViewById(R.id.autotextviewxa);
        textViewMaHuyen = (AutoCompleteTextView) findViewById(R.id.autotextviewmahuyen);
    }

    @Override
    //Đây là phương thức để lấy đc mã tỉnh
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String tutinhStr = arrayAdapter.getItem(position).toString();
    int tutinhposition = tentinhlist.indexOf(tutinhStr);
        try {
        tu_tinh = matinhlist.get(tutinhposition);
    } catch (Exception e) {
        Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
        return;
    }
        Toast.makeText(getApplicationContext(), tutinhStr + " " + tu_tinh, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "Vi tri la: " + tutinhposition, Toast.LENGTH_SHORT).show();

    //biến tu_tinh là mã tỉnh ngay lúc này
        new GetHuyen().execute();


}

    // Đây là phương thức để lấy được thông tin huyện gồm mã và tên
    class GetHuyen extends AsyncTask<String, Void, JsonObjectList> implements AdapterView.OnItemClickListener {
        private Exception exception;

        @Override
        protected JsonObjectList doInBackground(String... strings) {
            try {
                List<Boolean> list = new ArrayList<Boolean>();
                HttpClient httpclient = new DefaultHttpClient();
                JSONObject data = new JSONObject();
                URL url;
                HttpURLConnection conn;
                JsonObjectList res = null;

                try {
                    String url1 = IpConfig.httpURL + "/fe/api/GetDistrict/" + tu_tinh;
                    String input = url1;//+ URLEncoder.encode(data, "utf-8");
                    Log.v(LOG_TAG, "GetDistrict=" + input);
                    HttpGet httpGet = new HttpGet(input);
                    httpGet.setHeader("content-type", "application/json;charset=UTF-8");
                    HttpResponse response = null;
                    try {
                        response = httpclient.execute(httpGet);
                    } catch (Exception e) {
                        Log.v(LOG_TAG, "Log cua Exception : " + e);
                    }

                    if (response != null) {
                        String responseStr = EntityUtils.toString(response.getEntity(), UTF_8);
                        Log.v(LOG_TAG, "responseStr tren = " + responseStr);
                        String json = "{" + "\"data" + "\":" + responseStr + "}";
                        res = gson.fromJson(json, JsonObjectList.class);
                        if (res.getData().size() > 0) {
                            for (Object it : res.getData()) {
                                String respStr = gson.toJson(it);
                                Log.v(LOG_TAG, "respStr it = " + respStr);

                                JSONObject jObject = new JSONObject(respStr.substring(1, respStr.length() - 1).replace("\\", ""));
                                final String ten_huyen = jObject.getString("ten_huyen");
                                String ma_huyen = jObject.getString("ma_huyen");
                                Log.v(LOG_TAG, "ten_huyen=" + ten_huyen + "\tma_huyen=" + ma_huyen);
                                mahuyenlist.add(ma_huyen);
                                tenhuyenlist.add(ten_huyen);
                                textViewTinh.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                    }

                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                                        tenhuyenlist.clear();
                                        mahuyenlist.clear();
                                    }

                                    @Override
                                    public void afterTextChanged(Editable s) {

                                    }
                                });
                            }
                        }
                    } else {
                        Log.v(LOG_TAG, "response is null");
                    }
                } catch (Exception e) {
                    Log.v(LOG_TAG, "Exception=" + e.toString());
                }
                return res;

            } catch (Exception e) {
                Log.v(LOG_TAG, "Log Tai Asyntask : " + e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(JsonObjectList jsonObjectList) {
            try {
                arrayAdapter2 = new ArrayAdapter<String>(getBaseContext(), R.layout.support_simple_spinner_dropdown_item, tenhuyenlist);
                textViewHuyen.setAdapter(arrayAdapter2);
                textViewHuyen.setThreshold(0);
                textViewHuyen.setOnItemClickListener(this);
            } catch (Exception e) {
                Log.i(LOG_TAG, "ERROR : ");
                e.printStackTrace();
            }
            super.onPostExecute(jsonObjectList);

        }


        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String tenhuyen = arrayAdapter2.getItem(position).toString();
            int huyenpositon = tenhuyenlist.indexOf(tenhuyen);
            try {
                tu_huyen = mahuyenlist.get(huyenpositon);
                Toast.makeText(MainActivity.this, "Ma huyen chon : " + tu_huyen, Toast.LENGTH_SHORT).show();

            }catch (Exception e)
            {
                Toast.makeText(MainActivity.this, "Loi roi: " + e, Toast.LENGTH_SHORT).show();
                return;
            }
            new GetPhuong().execute();

        }
    }




//Lấy danh sách phường từ mã của huyện/quận
    class GetPhuong extends AsyncTask<String, Void, JsonObjectList> {
        private Exception exception;

        @Override
        protected JsonObjectList doInBackground(String... strings) {
            try {
                List<Boolean> list = new ArrayList<Boolean>();

                HttpClient httpclient = new DefaultHttpClient();
                JSONObject data = new JSONObject();
                URL url;
                HttpURLConnection conn;
                JsonObjectList res = null;
                try {
                    String url1 = IpConfig.httpURL + "/fe/api/GetWard/" + tu_tinh + "/" + tu_huyen ;
                    String input = url1;//+ URLEncoder.encode(data, "utf-8");
                    Log.v(LOG_TAG, "GetDistrict=" + input);
                    HttpGet httpGet = new HttpGet(input);
                    httpGet.setHeader("content-type", "application/json;charset=UTF-8");
                    HttpResponse response = null;
                    try {
                        response = httpclient.execute(httpGet);
                    } catch (Exception e) {
                        Log.v(LOG_TAG, "Log cua Exception : " + e);
                    }

                    if (response != null) {
                        String responseStr = EntityUtils.toString(response.getEntity(), UTF_8);
                        Log.v(LOG_TAG, "responseStr tren = " + responseStr);
                        String json = "{" + "\"data" + "\":" + responseStr + "}";
                        res = gson.fromJson(json, JsonObjectList.class);
                        if (res.getData().size() > 0) {
                            for (Object it : res.getData()) {
                                String respStr = gson.toJson(it);
                                Log.v(LOG_TAG, "respStr it = " + respStr);

                                JSONObject jObject = new JSONObject(respStr.substring(1, respStr.length() - 1).replace("\\", ""));
                                final String ten_xa = jObject.getString("ten_xa");
                                String ma_xa = jObject.getString("Id");
                                Log.v(LOG_TAG, "ten_xa=" + ten_xa + "\tId=" + ma_xa);
                                tenxalist.add(ten_xa);
                                Idxalist.add(ma_xa);
                                textViewHuyen.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                    }

                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                                        tenxalist.clear();
                                        Idxalist.clear();
                                    }

                                    @Override
                                    public void afterTextChanged(Editable s) {

                                    }
                                });
                            }
                        }
                    } else {
                        Log.v(LOG_TAG, "response is null");
                    }
                } catch (Exception e) {
                    Log.v(LOG_TAG, "Exception=" + e.toString());
                }
                return res;

            } catch (Exception e) {
                Log.v(LOG_TAG, "Log Tai Asyntask : " + e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(JsonObjectList jsonObjectList) {
            try {
                arrayAdapter3 = new ArrayAdapter<String>(getBaseContext(), R.layout.support_simple_spinner_dropdown_item, tenxalist);
                textViewXa.setAdapter(arrayAdapter3);
                textViewXa.setThreshold(1);
                Log.v(LOG_TAG, "nay la ten xa: " +tenxalist.toString());
                Log.v(LOG_TAG,"nay la id xa: " + Idxalist.toString());
            } catch (Exception e) {
                Log.i(LOG_TAG, "ERROR : ");
                e.printStackTrace();
            }
            super.onPostExecute(jsonObjectList);

        }

    }
}


