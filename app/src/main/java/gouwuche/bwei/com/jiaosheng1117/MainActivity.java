package gouwuche.bwei.com.jiaosheng1117;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.youth.banner.Banner;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private Banner banner;
    private TextView tvOP,tvCP,tvContent;
    private Button btnBuy;
    private String path = "https://www.zhaoapi.cn/product/getProductDetail?pid=10";
    private Button gou;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gou = (Button)findViewById(R.id.gou);
        banner = (Banner) findViewById(R.id.banner);
        tvContent = (TextView) findViewById(R.id.tvContent);
        tvOP = (TextView) findViewById(R.id.tvOP);
        tvCP = (TextView) findViewById(R.id.tvCP);
        initOkHttp();


    }


    private void initOkHttp() {

        OkHttpClient client = new OkHttpClient();


        Request request = new Request.Builder()
                .url(path)
                .build();

        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        String string = null;
                        try {
                            string = response.body().string();

                            Gson gson = new Gson();

                            Bean bean = gson.fromJson(string, Bean.class);


                            String images = bean.getData().getImages();
                            final String[] split = images.split("\\|");

                            ArrayList<String> strings = new ArrayList<>();

                            for (String e : split) {
                                strings.add(e);
                            }
                            System.out.println("图片" + strings.get(0));

                            //是否自动轮播
                            banner.isAutoPlay(true);
                            //图片两秒切换
                            banner.setDelayTime(2000);
                            //图片集合
                            banner.setImages(strings);
                            //banner样式
                            banner.setBannerStyle(Banner.ACCESSIBILITY_LIVE_REGION_POLITE);


                            tvContent.setText(bean.getData().getTitle());
                            tvOP.setText(bean.getData().getPrice() + "");
                            tvOP.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线
                            tvCP.setText(bean.getData().getBargainPrice() + "");

                            gou.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    OkHttpClient client1 = new OkHttpClient();


                                    final Request request1 = new Request.Builder()
                                            .url("https://www.zhaoapi.cn/product/addCart?uid=100&pid=10")
                                            .build();

                                    Call call1 = client1.newCall(request1);

                                    call1.enqueue(new Callback() {
                                        @Override
                                        public void onFailure(Call call, IOException e) {

                                        }

                                        @Override
                                        public void onResponse(Call call, final Response response) throws IOException {


                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    String string1 = null;
                                                    try {
                                                        string1 = response.body().string();
                                                        Gson gson1 = new Gson();

                                                        BuBean buyBean = gson1.fromJson(string1, BuBean.class);

                                                        if (buyBean.getCode().equals("0")) {
                                                            Toast.makeText(MainActivity.this, "已加入购物车", Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            Toast.makeText(MainActivity.this, "失败", Toast.LENGTH_SHORT).show();
                                                        }

                                                    } catch (IOException e) {
                                                        e.printStackTrace();
                                                    }

                                                }
                                            });


                                        }
                                    });


                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                });


            }
        });
    }

    }
