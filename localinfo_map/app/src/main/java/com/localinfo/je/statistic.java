package com.localinfo.je;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.localinfo.je.map.base_url;
import static com.localinfo.je.map.current_location_lat;
import static com.localinfo.je.map.current_location_long;
import static com.localinfo.je.map.region_type_depth_dong;
import static com.localinfo.je.map.region_type_depth_gu;
import static com.localinfo.je.map.region_type_depth_si;
import static com.localinfo.je.map.rest_api_key;

public class statistic extends Fragment {
    final String TAG = "statistic";

    ViewGroup viewGroup;

    Retrofit retrofit;
    Retrofit_API retrofit_api;
    Call<Keyword_Result> call;
    Call<statistic_items> call2;
    String madeurl = "http://edit0.dothome.co.kr/";

    int library_limit = 0;
    int laundry_limit = 0;
    int park_limit = 0;
    int film_limit = 0;
    boolean library_running = true;
    boolean laundry_running = true;
    boolean park_running = true;
    boolean film_running = true;

    int cvs_count, hospital_count, pill_count, library_count, laundry_count, park_count, film_count, cafe_count, parking_count, gas_station_count, bank_count, bigmart_count;

    double household_1p_count, household_2p_count, household_3p_count, household_np_count;
    double housing_house_count, housing_apt_count, housing_rowhouse_count, housing_multihouse_count, housing_nonhouse_count;
    double age_0_9_count, age_10_19_count, age_20_29_count, age_30_39_count, age_40_49_count, age_50_59_count, age_60_69_count, age_70_up_count;

    ArrayList<Integer> jsonList = new ArrayList<>(); //시설
    ArrayList<String> labelList = new ArrayList<>(); //시설
    ArrayList<Double> jsonList_age = new ArrayList<>(); //연령
    ArrayList<String> labelList_age = new ArrayList<>(); //연령
    BarChart barChart, barChart_age;
    PieChart household, housing;

    ProgressDialog progressDialog;

    Handler handler = new Handler();

    TextView environ, crime, location, traffic, cctv;
    LinearLayout analysis_layout;

    @Override
    public void onDetach() {
        super.onDetach();
        /*Log.i(TAG,"cvs_count:" + cvs_count + "\nhospital_count:" +hospital_count +"\npill_count:" +pill_count + "\nlibrary_count:" + library_count +
                "\nlaundry_count:" + laundry_count + "\npark_count:" + park_count + "\nfilm_count:" + film_count + "\ncafe_count:" + cafe_count +
                "\nparking_count:" + parking_count + "\ngas_station_count:" + gas_station_count + "\nbank_count:" + bank_count + "\nbigmart_count:" + bigmart_count);*/
    }

    @Override
    public void onPause() {
        super.onPause();

        library_limit = 0;
        laundry_limit = 0;
        park_limit = 0;
        film_limit = 0;
        library_running = true;
        laundry_running = true;
        park_running = true;
        film_running = true;

        jsonList.clear();
        labelList.clear();
        jsonList_age.clear();
        labelList_age.clear();

        housing_house_count = 0;
        housing_apt_count = 0;
        housing_rowhouse_count = 0;
        housing_multihouse_count = 0;
        housing_nonhouse_count = 0;

        household_1p_count = 0;
        household_2p_count = 0;
        household_3p_count = 0;
        household_np_count = 0;

        age_0_9_count = 0;
        age_10_19_count = 0;
        age_20_29_count = 0;
        age_30_39_count = 0;
        age_40_49_count = 0;
        age_50_59_count = 0;
        age_60_69_count = 0;
        age_70_up_count = 0;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.i(TAG,"onAttach()");

        Log.i(TAG,"통계시작");

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("현재 위치 데이터 분석중 ...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        request_retrofit_get_Search_Location_Directly_no_filter(current_location_lat, current_location_long, 1,""); //필터없는거 개수

        for(int i=0;i<3;i++) { //필터있는거 개수
            if(library_running == false && laundry_running == false && park_running == false && film_running == false){
                continue;
            }
            else {
                request_retrofit_get_Search_Location_Directly(current_location_lat,current_location_long, i, "");
            }
        }

        request_retrofit_Search_address(); //분석데이터 요청
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewGroup = (ViewGroup) inflater.inflate(R.layout.activity_statistic, container, false);

        analysis_layout = (LinearLayout) viewGroup.findViewById(R.id.analysis_layout);
        environ = (TextView) viewGroup.findViewById(R.id.environ);
        crime = (TextView) viewGroup.findViewById(R.id.crime);
        location = (TextView) viewGroup.findViewById(R.id.location);
        traffic = (TextView) viewGroup.findViewById(R.id.traffic);
        cctv = (TextView) viewGroup.findViewById(R.id.cctv);

        barChart = (BarChart)viewGroup.findViewById(R.id.chart);
        household = (PieChart) viewGroup.findViewById(R.id.household);
        housing = (PieChart) viewGroup.findViewById(R.id.housing);
        barChart_age = (BarChart) viewGroup.findViewById(R.id.chart_age);


        location.setText(" " + region_type_depth_si +" " + region_type_depth_gu +" " + region_type_depth_dong);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                init_bar();
                BarChartGraph(labelList, jsonList); //주변 건물 수 그래프 호출
                PieChartGraph_household(); //세대 수 그래프 호출
                PieChartGraph_housing(); //주택 수 그래프 호출
                init_bar_age();
                BarChartGraph_age(labelList_age,jsonList_age);

                library_count=0;
                laundry_count=0;
                park_count=0;
                film_count=0;

            }
        }).start();

        return viewGroup;
    }

    //==================================시설 수(막대그래프)==============================

    public void init_bar(){

        jsonList.add(cvs_count);
        jsonList.add(hospital_count);
        jsonList.add(pill_count);
        jsonList.add(library_count);
        jsonList.add(laundry_count);
        jsonList.add(park_count);
        jsonList.add(film_count);
        jsonList.add(cafe_count);
        jsonList.add(parking_count);
        jsonList.add(gas_station_count);
        jsonList.add(bank_count);
        jsonList.add(bigmart_count);

        labelList.add("1");
        labelList.add("2");
        labelList.add("3");
        labelList.add("4");
        labelList.add("5");
        labelList.add("6");
        labelList.add("7");
        labelList.add("8");
        labelList.add("9");
        labelList.add("10");
        labelList.add("11");
        labelList.add("12");

    }

    private void BarChartGraph(ArrayList<String> labelList, ArrayList<Integer> valList) {
        // BarChart 메소드
        ArrayList<BarEntry> entries = new ArrayList<>();
        for(int i=0; i < valList.size();i++){
            //Log.i(TAG,"valList 사이즈: "+valList.size());
            entries.add(new BarEntry((Integer) valList.get(i), i));
        }

        BarDataSet depenses = new BarDataSet (entries, "수량"); // 변수로 받아서 넣어줘도 됨
        depenses.setAxisDependency(YAxis.AxisDependency.RIGHT);
        barChart.setDescription(" ");

        ArrayList<String> labels = new ArrayList<String>();
        for(int i=0; i < labelList.size(); i++){
            //Log.i(TAG,"labelList 사이즈: "+labelList.size());
            labels.add((String) labelList.get(i));
        }

        final BarData data = new BarData(labels,depenses); // 라이브러리 v3.x 사용하면 에러 발생함
        //depenses.setColors(ColorTemplate.LIBERTY_COLORS);
        depenses.setColor(Color.rgb(10,135,186));

        handler.post(new Runnable() {
            @Override
            public void run() {
                barChart.setData(data);
                barChart.animateXY(3000,3000);
                barChart.invalidate();
                //progressDialog.dismiss();
            }
        });

    }

    //===================================세대 수(원형 그래프)============================================

    private void PieChartGraph_household() {

        ArrayList NoOfEmp_household;
        ArrayList kind_household;

        double t_1 = (double)Math.round(household_1p_count*10000)/100.0;
        double t_2 = (double)Math.round(household_2p_count*10000)/100.0;
        double t_3 = (double)Math.round(household_3p_count*10000)/100.0;
        double t_n = (double)Math.round(household_np_count*10000)/100.0;

        NoOfEmp_household = new ArrayList();

        NoOfEmp_household.add(new Entry((float) t_1, 0));
        NoOfEmp_household.add(new Entry((float) t_2, 1));
        NoOfEmp_household.add(new Entry((float) t_3, 2));
        NoOfEmp_household.add(new Entry((float) t_n, 3));



        kind_household = new ArrayList();

        kind_household.add("1인");
        kind_household.add("2인");
        kind_household.add("3인");
        kind_household.add("4인 ↑");

        PieDataSet dataSet = new PieDataSet(NoOfEmp_household, "세대 수");
        final PieData data = new PieData(kind_household, dataSet); // MPAndroidChart v3.X 오류 발생
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        handler.post(new Runnable() {
            @Override
            public void run() {
                household.setData(data);
                household.animateXY(3000,3000);
                household.invalidate();
                progressDialog.dismiss();
            }
        });


    }

    //==============================주택 종류 (원형그래프)===========================

    private void PieChartGraph_housing() {

        ArrayList NoOfEmp_housing;
        ArrayList kind_housing;

        double t_1 = (double)Math.round(housing_house_count*10000)/100.0;
        double t_2 = (double)Math.round(housing_apt_count*10000)/100.0;
        double t_3 = (double)Math.round(housing_rowhouse_count*10000)/100.0;
        double t_4 = (double)Math.round(housing_multihouse_count*10000)/100.0;
        double t_5 = (double)Math.round(housing_nonhouse_count*10000)/100.0;

        NoOfEmp_housing = new ArrayList();

        NoOfEmp_housing.add(new Entry((float) t_1, 0));
        NoOfEmp_housing.add(new Entry((float) t_2, 1));
        NoOfEmp_housing.add(new Entry((float) t_3, 2));
        NoOfEmp_housing.add(new Entry((float) t_4, 3));
        NoOfEmp_housing.add(new Entry((float) t_5, 4));


        kind_housing = new ArrayList();

        kind_housing.add("주택");
        kind_housing.add("아파트");
        kind_housing.add("연립");
        kind_housing.add("다세대");
        kind_housing.add("비거주용");

        PieDataSet dataSet = new PieDataSet(NoOfEmp_housing, "주택 수");
        final PieData data = new PieData(kind_housing, dataSet); // MPAndroidChart v3.X 오류 발생
        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);

        handler.post(new Runnable() {
            @Override
            public void run() {
                housing.setData(data);
                housing.animateXY(3000,3000);
                housing.invalidate();
                //progressDialog.dismiss();
            }
        });
    }


    //===================================연령별 인구 그래프(막대그래프)=======================

    public void init_bar_age(){

        double t_1 = (double)Math.round(age_0_9_count*10000)/100.0;
        double t_2 = (double)Math.round(age_10_19_count*10000)/100.0;
        double t_3 = (double)Math.round(age_20_29_count*10000)/100.0;
        double t_4 = (double)Math.round(age_30_39_count*10000)/100.0;
        double t_5 = (double)Math.round(age_40_49_count*10000)/100.0;
        double t_6 = (double)Math.round(age_50_59_count*10000)/100.0;
        double t_7 = (double)Math.round(age_60_69_count*10000)/100.0;
        double t_8 = (double)Math.round(age_70_up_count*10000)/100.0;


        jsonList_age.add(t_1);
        jsonList_age.add(t_2);
        jsonList_age.add(t_3);
        jsonList_age.add(t_4);
        jsonList_age.add(t_5);
        jsonList_age.add(t_6);
        jsonList_age.add(t_7);
        jsonList_age.add(t_8);


        labelList_age.add("10세 ↓");
        labelList_age.add("10대");
        labelList_age.add("20대");
        labelList_age.add("30대");
        labelList_age.add("40대");
        labelList_age.add("50대");
        labelList_age.add("60대");
        labelList_age.add("70세 ↑");

    }

    private void BarChartGraph_age(ArrayList<String> labelList, ArrayList<Double> valList) {
        // BarChart 메소드
        ArrayList<BarEntry> entries = new ArrayList<>();
        for(int i=0; i < valList.size();i++){
            //Log.i(TAG,"valList 사이즈: "+valList.size());
            entries.add(new BarEntry(Float.parseFloat(String.valueOf(valList.get(i))), i));
        }

        BarDataSet depenses = new BarDataSet (entries, "연령별 비율"); // 변수로 받아서 넣어줘도 됨
        depenses.setAxisDependency(YAxis.AxisDependency.RIGHT);
        barChart_age.setDescription(" ");

        ArrayList<String> labels = new ArrayList<String>();
        for(int i=0; i < labelList.size(); i++){
            //Log.i(TAG,"labelList 사이즈: "+labelList.size());
            labels.add((String) labelList.get(i));
        }

        final BarData data = new BarData(labels,depenses); // 라이브러리 v3.x 사용하면 에러 발생함
        //depenses.setColors(ColorTemplate.LIBERTY_COLORS);
        depenses.setColor(Color.rgb(11,76,188));

        handler.post(new Runnable() {
            @Override
            public void run() {
                barChart_age.setData(data);
                barChart_age.animateXY(3000,3000);
                barChart_age.invalidate();
                //progressDialog.dismiss();
            }
        });

        valList.clear();
        labelList.clear();

    }


    //=================================통신 서울시 주소 얻기(동)===================================

    public void request_retrofit_Search_address() {
        retrofit = new Retrofit.Builder()
                .baseUrl(madeurl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofit_api = retrofit.create(Retrofit_API.class);

        if(region_type_depth_si.equals("서울특별시")) {

            call2 = retrofit_api.get_statistic_list();
            call2.enqueue(new Callback<statistic_items>() {
                @Override
                public void onResponse(Call<statistic_items> call2, Response<statistic_items> response) {
                    if (response.isSuccessful()) {
                        statistic_items result = response.body();

                        List<statistic_items.Datum> analysis_data = result.getData();

                        for (int i = 0; i < analysis_data.size(); i++) {
                            if (region_type_depth_gu.equals(analysis_data.get(i).getGu())) {
                                if (region_type_depth_dong.equals(analysis_data.get(i).getDong())) {
                                    Log.i(TAG, "로컬 정보: " + analysis_data.get(i).get1_person_hh());

                                    try {
                                        //생활환경 만족도
                                        environ.setText("주거환경: " + analysis_data.get(i).getEnviron_house() + "점\n" +
                                                "경제환경: " + analysis_data.get(i).getEnviron_econ() + "점\n" +
                                                "사회환경: " + analysis_data.get(i).getEnviron_social() + "점\n" +
                                                "교육환경: " + analysis_data.get(i).getEnviron_educ() + "점");

                                        //범죄율
                                        double crime_temp_si = analysis_data.get(i).getCrime_per_capita_si() * 1000;
                                        double crime_temp_gu = analysis_data.get(i).getCrime_per_capita_gu() * 1000;
                                        double crime_si = (double) Math.round(crime_temp_si * 100) / 100.0;
                                        double crime_gu = (double) Math.round(crime_temp_gu * 100) / 100.0;
                                        crime.setText("시 범죄율: " + crime_si + "회\n" +
                                                "구 범죄율: " + crime_gu + "회");

                                        //교통안전지수
                                        double traffic_temp_si = analysis_data.get(i).getTraffic_safety_si();
                                        double traffic_temp_gu = analysis_data.get(i).getTraffic_safety_gu();
                                        double traffic_si = (double) Math.round(traffic_temp_si * 100) / 100.0;
                                        double traffic_gu = (double) Math.round(traffic_temp_gu * 100) / 100.0;
                                        traffic.setText("시 교통안전지수: " + traffic_si + "점\n" +
                                                "구 교통안전지수: " + traffic_gu + "점");

                                        //CCTV 수
                                        double cctv_si = (double) Math.round(analysis_data.get(i).getCctv_per_sqkm_si_mean() * 100) / 100.0;
                                        double cctv_gu = (double) Math.round(analysis_data.get(i).getCctv_per_sqkm_gu() * 100) / 100.0;
                                        if (cctv_gu > 124.17) {
                                            cctv.setText("시 CCTV 평균 설치 수량: " + cctv_si + "대\n" +
                                                    "구 CCTV 설치 수량: " + cctv_gu + "대 (많은 편)");
                                        } else if (cctv_gu < 79.25) {
                                            cctv.setText("시 CCTV 평균 설치 수량: " + cctv_si + "대\n" +
                                                    "구 CCTV 설치 수량: " + cctv_gu + "대 (적은 편)");
                                        } else {
                                            cctv.setText("시 CCTV 평균 설치 수량: " + cctv_si + "대\n" +
                                                    "구 CCTV 설치 수량: " + cctv_gu + "대 (보통)");
                                        }

                                        //세대 수
                                        household_1p_count = analysis_data.get(i).get1_person_hh();
                                        household_2p_count = analysis_data.get(i).get2_person_hh();
                                        household_3p_count = analysis_data.get(i).get3_person_hh();
                                        household_np_count = analysis_data.get(i).getN_person_hh();

                                        //연령별 수
                                        age_0_9_count = analysis_data.get(i).get0_9();
                                        age_10_19_count = analysis_data.get(i).get10_19();
                                        age_20_29_count = analysis_data.get(i).get20_29();
                                        age_30_39_count = analysis_data.get(i).get30_39();
                                        age_40_49_count = analysis_data.get(i).get40_49();
                                        age_50_59_count = analysis_data.get(i).get50_59();
                                        age_60_69_count = analysis_data.get(i).get60_69();
                                        age_70_up_count = analysis_data.get(i).get70_();

                                        //주택 수
                                        housing_house_count = analysis_data.get(i).getHouse();
                                        housing_apt_count = analysis_data.get(i).getApt();
                                        housing_rowhouse_count = analysis_data.get(i).getRow_house();
                                        housing_multihouse_count = analysis_data.get(i).getMulti_house();
                                        housing_nonhouse_count = analysis_data.get(i).getNon_house();

                                    }catch (Exception e){
                                        Toast.makeText(getActivity(),"일부 데이터가 존재하지 않습니다.",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }

                    } else {
                        Log.i(TAG, "Retrofit Error - request_retrofit_Search_address()");
                    }
                }

                @Override
                public void onFailure(Call<statistic_items> call2, Throwable t) {
                    Log.i(TAG, "onFailure() - request_retrofit_Search_address()");
                }
            });
        }
        else{
            Toast.makeText(getActivity(),"현재 서울특별시를 제외한 다른 지역의 분석 서비스는 준비 중 입니다.", Toast.LENGTH_LONG).show();
        }
    }


    //=================================통신 필터 필요없는 곳===================================

    public void request_retrofit_get_Search_Location_Directly_no_filter(double x, double y,int i, String info_name) {
        retrofit = new Retrofit.Builder()
                .baseUrl(base_url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofit_api = retrofit.create(Retrofit_API.class);


        call = retrofit_api.get_Search_Location_Directly_category(rest_api_key, "CS2", "편의점", String.valueOf(y), String.valueOf(x), 15, Range_change.range, i, "distance");
        call.enqueue(new Callback<Keyword_Result>() {
            @Override
            public void onResponse(Call<Keyword_Result> call, Response<Keyword_Result> response) {
                if (response.isSuccessful()) {
                    Keyword_Result result = response.body();

                    List<Keyword_Items> keyword_items = result.getDocuments();

                    //Log.i(TAG, "토탈카운트: " + result.getMeta().getTotal_count());

                    cvs_count = result.getMeta().getTotal_count();


                } else {
                    Log.i(TAG, "Retrofit Error - request_retrofit_get_Search_Location_Directly_no_filter");
                }
            }

            @Override
            public void onFailure(Call<Keyword_Result> call, Throwable t) {
                Log.i(TAG, "onFailure() - request_retrofit_get_Search_Location_Directly_no_filter");
            }
        });


        call = retrofit_api.get_Search_Location_Directly_category(rest_api_key, "HP8", "병원", String.valueOf(y), String.valueOf(x), 15, Range_change.range, i, "distance");
        call.enqueue(new Callback<Keyword_Result>() {
            @Override
            public void onResponse(Call<Keyword_Result> call, Response<Keyword_Result> response) {
                if (response.isSuccessful()) {
                    Keyword_Result result = response.body();

                    List<Keyword_Items> keyword_items = result.getDocuments();

                    //Log.i(TAG, "토탈카운트: " + result.getMeta().getTotal_count());

                    hospital_count = result.getMeta().getTotal_count();


                } else {
                    Log.i(TAG, "Retrofit Error - request_retrofit_get_Search_Location_Directly_no_filter");
                }
            }

            @Override
            public void onFailure(Call<Keyword_Result> call, Throwable t) {
                Log.i(TAG, "onFailure() - request_retrofit_get_Search_Location_Directly_no_filter");
            }
        });


        call = retrofit_api.get_Search_Location_Directly_category(rest_api_key, "PM9", "약국", String.valueOf(y), String.valueOf(x), 15, Range_change.range, i, "distance");
        call.enqueue(new Callback<Keyword_Result>() {
            @Override
            public void onResponse(Call<Keyword_Result> call, Response<Keyword_Result> response) {
                if (response.isSuccessful()) {
                    Keyword_Result result = response.body();

                    List<Keyword_Items> keyword_items = result.getDocuments();

                    //Log.i(TAG, "토탈카운트: " + result.getMeta().getTotal_count());

                    pill_count = result.getMeta().getTotal_count();


                } else {
                    Log.i(TAG, "Retrofit Error - request_retrofit_get_Search_Location_Directly_no_filter");
                }
            }

            @Override
            public void onFailure(Call<Keyword_Result> call, Throwable t) {
                Log.i(TAG, "onFailure() - request_retrofit_get_Search_Location_Directly_no_filter");
            }
        });

        call = retrofit_api.get_Search_Location_Directly_category(rest_api_key, "CE7","카페", String.valueOf(y), String.valueOf(x), 15, Range_change.range, i, "distance");
        call.enqueue(new Callback<Keyword_Result>() {
            @Override
            public void onResponse(Call<Keyword_Result> call, Response<Keyword_Result> response) {
                if (response.isSuccessful()) {
                    Keyword_Result result = response.body();

                    List<Keyword_Items> keyword_items = result.getDocuments();

                    //Log.i(TAG, "토탈카운트: " + result.getMeta().getTotal_count());

                    cafe_count = result.getMeta().getTotal_count();


                } else {
                    Log.i(TAG, "Retrofit Error - request_retrofit_get_Search_Location_Directly_no_filter");
                }
            }

            @Override
            public void onFailure(Call<Keyword_Result> call, Throwable t) {
                Log.i(TAG, "onFailure() - request_retrofit_get_Search_Location_Directly_no_filter");
            }
        });





        call = retrofit_api.get_Search_Location_Directly_category(rest_api_key, "PK6","주차장", String.valueOf(y), String.valueOf(x), 15, Range_change.range, i, "distance");
        call.enqueue(new Callback<Keyword_Result>() {
            @Override
            public void onResponse(Call<Keyword_Result> call, Response<Keyword_Result> response) {
                if (response.isSuccessful()) {
                    Keyword_Result result = response.body();

                    List<Keyword_Items> keyword_items = result.getDocuments();

                    //Log.i(TAG, "토탈카운트: " + result.getMeta().getTotal_count());

                    parking_count = result.getMeta().getTotal_count();

                } else {
                    Log.i(TAG, "Retrofit Error - request_retrofit_get_Search_Location_Directly_no_filter");
                }
            }

            @Override
            public void onFailure(Call<Keyword_Result> call, Throwable t) {
                Log.i(TAG, "onFailure() - request_retrofit_get_Search_Location_Directly_no_filter");
            }
        });





        call = retrofit_api.get_Search_Location_Directly_category(rest_api_key, "OL7","주유소", String.valueOf(y), String.valueOf(x), 15, Range_change.range, i, "distance");
        call.enqueue(new Callback<Keyword_Result>() {
            @Override
            public void onResponse(Call<Keyword_Result> call, Response<Keyword_Result> response) {
                if (response.isSuccessful()) {
                    Keyword_Result result = response.body();

                    List<Keyword_Items> keyword_items = result.getDocuments();

                    //Log.i(TAG, "토탈카운트: " + result.getMeta().getTotal_count());

                    gas_station_count = result.getMeta().getTotal_count();


                } else {
                    Log.i(TAG, "Retrofit Error - request_retrofit_get_Search_Location_Directly_no_filter");
                }
            }

            @Override
            public void onFailure(Call<Keyword_Result> call, Throwable t) {
                Log.i(TAG, "onFailure() - request_retrofit_get_Search_Location_Directly_no_filter");
            }
        });





        call = retrofit_api.get_Search_Location_Directly_category(rest_api_key, "BK9","은행", String.valueOf(y), String.valueOf(x), 15, Range_change.range, i, "distance");
        call.enqueue(new Callback<Keyword_Result>() {
            @Override
            public void onResponse(Call<Keyword_Result> call, Response<Keyword_Result> response) {
                if (response.isSuccessful()) {
                    Keyword_Result result = response.body();

                    List<Keyword_Items> keyword_items = result.getDocuments();

                    //Log.i(TAG, "토탈카운트: " + result.getMeta().getTotal_count());

                    bank_count = result.getMeta().getTotal_count();


                } else {
                    Log.i(TAG, "Retrofit Error - request_retrofit_get_Search_Location_Directly_no_filter");
                }
            }

            @Override
            public void onFailure(Call<Keyword_Result> call, Throwable t) {
                Log.i(TAG, "onFailure() - request_retrofit_get_Search_Location_Directly_no_filter");
            }
        });





        call = retrofit_api.get_Search_Location_Directly_category(rest_api_key, "MT1","대형마트", String.valueOf(y), String.valueOf(x), 15, Range_change.range, i, "distance");
        call.enqueue(new Callback<Keyword_Result>() {
            @Override
            public void onResponse(Call<Keyword_Result> call, Response<Keyword_Result> response) {
                if (response.isSuccessful()) {
                    Keyword_Result result = response.body();

                    List<Keyword_Items> keyword_items = result.getDocuments();

                    //Log.i(TAG, "토탈카운트: " + result.getMeta().getTotal_count());

                    bigmart_count = result.getMeta().getTotal_count();


                } else {
                    Log.i(TAG, "Retrofit Error - request_retrofit_get_Search_Location_Directly_no_filter");
                }
            }

            @Override
            public void onFailure(Call<Keyword_Result> call, Throwable t) {
                Log.i(TAG, "onFailure() - request_retrofit_get_Search_Location_Directly_no_filter");
            }
        });
    }






    //=================================통신 필터 필요함===================================

    public void request_retrofit_get_Search_Location_Directly(double x, double y,int i, String info_name){
        retrofit = new Retrofit.Builder()
                .baseUrl(base_url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofit_api = retrofit.create(Retrofit_API.class);

        call = retrofit_api.get_Search_Location_Directly(rest_api_key, "도서관", String.valueOf(y), String.valueOf(x), 15, Range_change.range, i + 1, "distance");
        call.enqueue(new Callback<Keyword_Result>() {
            @Override
            public void onResponse(Call<Keyword_Result> call, Response<Keyword_Result> response) {
                if (response.isSuccessful()) {
                    Keyword_Result result = response.body();

                    List<Keyword_Items> keyword_items = result.getDocuments();

                    //Log.i(TAG, "토탈카운트: " + result.getMeta().getTotal_count());

                    if (result.getMeta().getIs_end() == true && library_limit == 1) {
                        library_running = false;
                    }
                    else {
                        for (int i = 0; i < keyword_items.size(); i++) {

                            Keyword_Items temp = keyword_items.get(i);
                            if(!(temp.getPlace_name().contains("도서관점") || temp.getPlace_name().contains("ATM") ||
                                    temp.getPlace_name().contains("민원") || temp.getPlace_name().contains("창구") ||
                                    temp.getPlace_name().contains("365") || temp.getPlace_name().contains("구내") ||
                                    temp.getPlace_name().contains("매점"))) {
                                library_count++;
                                //Log.i(TAG,"도서관 카운트 추가하였습니다: " + library_count + "개");
                            }

                        }
                        if (result.getMeta().getIs_end() == true) {
                            library_limit = 1;
                        }


                    }


                } else {
                    Log.i(TAG, "Retrofit Error - request_retrofit_get_Search_Location_Directly");
                }
            }

            @Override
            public void onFailure(Call<Keyword_Result> call, Throwable t) {
                Log.i(TAG, "onFailure() - request_retrofit_get_Search_Location_Directly");
            }
        });





        call = retrofit_api.get_Search_Location_Directly(rest_api_key, "세탁소", String.valueOf(y), String.valueOf(x), 15, Range_change.range, i + 1, "distance");
        call.enqueue(new Callback<Keyword_Result>() {
            @Override
            public void onResponse(Call<Keyword_Result> call, Response<Keyword_Result> response) {
                if (response.isSuccessful()) {
                    Keyword_Result result = response.body();

                    List<Keyword_Items> keyword_items = result.getDocuments();

                    //Log.i(TAG, "토탈카운트: " + result.getMeta().getTotal_count());

                    if (result.getMeta().getIs_end() == true && laundry_limit == 1) {
                        laundry_running = false;
                    }
                    else {
                        for (int i = 0; i < keyword_items.size(); i++) {

                            Keyword_Items temp = keyword_items.get(i);
                            if(!(temp.getPlace_name().contains("필터추가하고싶을때사용"))) {
                                laundry_count++;
                            }

                        }
                        if (result.getMeta().getIs_end() == true) {
                            laundry_limit = 1;
                        }
                    }


                } else {
                    Log.i(TAG, "Retrofit Error - request_retrofit_get_Search_Location_Directly");
                }
            }

            @Override
            public void onFailure(Call<Keyword_Result> call, Throwable t) {
                Log.i(TAG, "onFailure() - request_retrofit_get_Search_Location_Directly");
            }
        });





        call = retrofit_api.get_Search_Location_Directly(rest_api_key, "공원", String.valueOf(y), String.valueOf(x), 15, Range_change.range, i + 1, "distance");
        call.enqueue(new Callback<Keyword_Result>() {
            @Override
            public void onResponse(Call<Keyword_Result> call, Response<Keyword_Result> response) {
                //통신 성공 시
                if (response.isSuccessful()) {
                    Keyword_Result result = response.body();

                    List<Keyword_Items> keyword_items = result.getDocuments();

                    //Log.i(TAG, "토탈카운트: " + result.getMeta().getTotal_count());

                    if (result.getMeta().getIs_end() == true && park_limit == 1) {
                        park_running = false;
                    }
                    else {
                        for (int i = 0; i < keyword_items.size(); i++) {

                            Keyword_Items temp = keyword_items.get(i);
                            if(!(temp.getPlace_name().contains("쏘카존") || temp.getPlace_name().contains("지진") ||
                                    temp.getPlace_name().contains("화장실") || temp.getPlace_name().contains("수학") ||
                                    temp.getPlace_name().contains("영어") || temp.getPlace_name().contains("과학") ||
                                    temp.getPlace_name().contains("국어") || temp.getPlace_name().contains("학원") ||
                                    temp.getPlace_name().contains("주차장") || temp.getPlace_name().contains("공원점") ||
                                    temp.getPlace_name().contains("주점") || temp.getPlace_name().contains("게이트") ||
                                    temp.getPlace_name().contains("출입구") || temp.getPlace_name().contains("주택") ||
                                    temp.getPlace_name().contains("관리") || temp.getPlace_name().contains("포장") ||
                                    temp.getPlace_name().contains("교차로") || temp.getPlace_name().contains("공원역") ||
                                    temp.getPlace_name().contains("음수대") || temp.getPlace_name().contains("그린존") ||
                                    temp.getPlace_name().contains("앞역") || temp.getPlace_name().contains("뒤역점") ||
                                    temp.getPlace_name().contains("호선") || temp.getPlace_name().contains("ATM") ||
                                    temp.getPlace_name().contains("빌라") || temp.getPlace_name().contains("슈퍼") ||
                                    temp.getPlace_name().contains("마트"))) {
                                park_count++;
                            }
                        }
                        if (result.getMeta().getIs_end() == true) {
                            park_limit = 1;
                        }
                    }

                } else {
                    Log.i(TAG, "Retrofit Error - request_retrofit_get_Search_Location_Directly");
                }
            }

            @Override
            public void onFailure(Call<Keyword_Result> call, Throwable t) {
                Log.i(TAG, "onFailure() - request_retrofit_get_Search_Location_Directly");
            }
        });





        call = retrofit_api.get_Search_Location_Directly(rest_api_key, "영화관", String.valueOf(y), String.valueOf(x), 15, Range_change.range, i + 1, "distance");
        call.enqueue(new Callback<Keyword_Result>() {
            @Override
            public void onResponse(Call<Keyword_Result> call, Response<Keyword_Result> response) {
                if (response.isSuccessful()) {
                    Keyword_Result result = response.body();

                    List<Keyword_Items> keyword_items = result.getDocuments();

                    //Log.i(TAG, "토탈카운트: " + result.getMeta().getTotal_count());

                    if (result.getMeta().getIs_end() == true && film_limit == 1) {
                        film_running = false;
                    } else {
                        for (int i = 0; i < keyword_items.size(); i++) {
                            Keyword_Items temp = keyword_items.get(i);
                            if(!(temp.getPlace_name().contains("화장실"))) {
                                film_count++;
                            }

                        }
                        if (result.getMeta().getIs_end() == true) {
                            film_limit = 1;
                        }
                    }

                } else {
                    Log.i(TAG, "Retrofit Error - request_retrofit_get_Search_Location_Directly");
                }
            }

            @Override
            public void onFailure(Call<Keyword_Result> call, Throwable t) {
                Log.i(TAG, "onFailure() - request_retrofit_get_Search_Location_Directly");
            }
        });

    }
}