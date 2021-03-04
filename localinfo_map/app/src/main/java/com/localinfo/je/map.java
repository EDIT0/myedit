package com.localinfo.je;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pedro.library.AutoPermissions;
import com.pedro.library.AutoPermissionsListener;

import net.daum.mf.map.api.MapCircle;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.INPUT_METHOD_SERVICE;


public class map extends Fragment implements AutoPermissionsListener, MapView.MapViewEventListener, MapView.POIItemEventListener, View.OnClickListener {
    private final String TAG = "map";

    ViewGroup viewGroup;

    MapView mapView;
    ViewGroup mapViewContainer;
    MapPOIItem marker, mylocation, search_marker;
    MapCircle circle;

    Retrofit retrofit;
    Retrofit_API retrofit_api;
    Call<Keyword_Result> call;
    Call<address_itmes> call3;

    EditText search;
    Button search_button, setmylocation, find_button;

    static String rest_api_key = "KakaoAK 27dbdb4272b77bac9c3023c97264fc9d";
    static String base_url = "https://dapi.kakao.com/";
    static double current_location_lat;
    static double current_location_long;
    static String region_type_depth_dong = "";
    static String region_type_depth_gu = "";
    static String region_type_depth_si = "";
    List<Keyword_Items> items_list = new ArrayList<>();
    static int limit = 0;
    static boolean running = true;

    private FloatingActionButton fab_main, fab_cvs, fab_hospital, fab_pill, fab_library, fab_laundry, fab_park, fab_film
            , fab_cafe, fab_parking, fab_gas_station, fab_bank, fab_bigmart;
    private Animation fab_open, fab_close;
    private boolean isFabOpen = false;

    String current_info_name = "";
    double latitude;
    double longitude;
    LocationManager locationManager;
    LocationListener locationListener;

    InputMethodManager keypad;

    static boolean initiation = false;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.i(TAG,"onAttach()");
        if(initiation == false){
            initiation = true;

            try {
                getMyLocation();
            }catch (Exception e){
                Request_Permission();
            }
        }

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG,"onCreate()");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(TAG,"onActivityCreated()");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG,"onStart()");

        mapView.removeCircle(circle);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG,"onResume()");

        mapView.addCircle(circle);

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG,"onPause()");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG,"onStop()");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i(TAG,"onDestroyView()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG,"onDestroy()");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i(TAG,"onDetach()");
        items_list.clear();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG,"onCreateView()");
        viewGroup = (ViewGroup) inflater.inflate(R.layout.activity_map, container, false);



        mapView = new MapView(getActivity());
        marker = new MapPOIItem();
        mylocation = new MapPOIItem();
        search_marker = new MapPOIItem();

        mapView.setZoomLevel(3,true);
        mapView.setMapRotationAngle(0,true);

        mapViewContainer = viewGroup.findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);



        circle = new MapCircle(
                MapPoint.mapPointWithGeoCoord(current_location_lat, current_location_long), // center
                Range_change.range, // radius
                Color.argb(100, 34, 50, 78), // strokeColor
                Color.argb(100, 237, 238, 239) // fillColor
        );
        circle.setTag(1234);


        mapView.setMapViewEventListener(this);
        mapView.setPOIItemEventListener(this);

        search = viewGroup.findViewById(R.id.search);
        search_button = viewGroup.findViewById(R.id.search_button);
        setmylocation = viewGroup.findViewById(R.id.setmylocation);
        keypad = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
        find_button = (Button) viewGroup.findViewById(R.id.find_button);


        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    request_retrofit_get_Search_LocationName(search.getText().toString(),"Search_input");
                    mapView.removeAllPOIItems();
                    mapView.removeAllCircles();
                    search.setText("");
                    try {
                        keypad.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }catch (Exception e){

                    }
                    return true;
                }

                return false;
            }
        });

        find_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mapView.removeAllPOIItems();

                limit = 0;
                running = true;

                items_list.clear();

                for(int i=0;i<3;i++) {
                    if(running == false){
                        return;
                    }
                    else {
                        if(current_info_name.equals("")){
                            Toast.makeText(getActivity(),"검색할 카테고리를 지정해주세요.",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            request_retrofit_get_Search_Location_Directly(current_location_lat, current_location_long, i, current_info_name);
                        }
                    }
                }
            }
        });

        setmylocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    getMyLocation();
                }catch (Exception e){
                    AutoPermissions.Companion.loadAllPermissions(getActivity(), 1);
                }
            }
        });

        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                request_retrofit_get_Search_LocationName(search.getText().toString(),"Search_input");
                mapView.removeAllPOIItems();
                mapView.removeAllCircles();
                search.setText("");
                try {
                    keypad.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }catch (Exception e){

                }

            }
        });


        fab_open = AnimationUtils.loadAnimation(getActivity(), R.anim.floating_open);
        fab_close = AnimationUtils.loadAnimation(getActivity(), R.anim.floating_close);

        fab_main = (FloatingActionButton) viewGroup.findViewById(R.id.fab_main);
        fab_cvs = (FloatingActionButton) viewGroup.findViewById(R.id.fab_cvs);
        fab_hospital = (FloatingActionButton) viewGroup.findViewById(R.id.fab_hospital);
        fab_pill = (FloatingActionButton) viewGroup.findViewById(R.id.fab_pill);
        fab_library = (FloatingActionButton) viewGroup.findViewById(R.id.fab_library);
        fab_laundry = (FloatingActionButton) viewGroup.findViewById(R.id.fab_laundry);
        fab_park = (FloatingActionButton) viewGroup.findViewById(R.id.fab_park);
        fab_film = (FloatingActionButton) viewGroup.findViewById(R.id.fab_film);
        fab_cafe = (FloatingActionButton) viewGroup.findViewById(R.id.fab_cafe);
        fab_parking = (FloatingActionButton) viewGroup.findViewById(R.id.fab_parking);
        fab_gas_station = (FloatingActionButton) viewGroup.findViewById(R.id.fab_gas_station);
        fab_bank = (FloatingActionButton) viewGroup.findViewById(R.id.fab_bank);
        fab_bigmart = (FloatingActionButton) viewGroup.findViewById(R.id.fab_bigmart);

        fab_main.setOnClickListener(this);
        fab_cvs.setOnClickListener(this);
        fab_hospital.setOnClickListener(this);
        fab_pill.setOnClickListener(this);
        fab_library.setOnClickListener(this);
        fab_laundry.setOnClickListener(this);
        fab_park.setOnClickListener(this);
        fab_film.setOnClickListener(this);
        fab_cafe.setOnClickListener(this);
        fab_parking.setOnClickListener(this);
        fab_gas_station.setOnClickListener(this);
        fab_bank.setOnClickListener(this);
        fab_bigmart.setOnClickListener(this);



        return viewGroup;
    }

    //===========================내 위치 구하기===================================

    void getMyLocation(){

        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);

        boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                Log.i(TAG, "onLocationChanged()");
                double lat = location.getLatitude();
                double lng = location.getLongitude();

                latitude = lat;
                longitude = lng;

                Log.i(TAG, "" + latitude + " / " + longitude);

                mapView.removeAllPOIItems();
                mapView.removeAllCircles();
                MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(latitude, longitude);
                mapView.setMapCenterPoint(mapPoint, true);
    /*mylocation.setItemName("현위치");
    mylocation.setTag(99);
    mylocation.setMapPoint(mapPoint);
    mylocation.setMarkerType(MapPOIItem.MarkerType.BluePin);
    mapView.addPOIItem(mylocation);*/

                locationManager.removeUpdates(locationListener);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
                Log.i(TAG, "onStatusChanged()");
            }

            public void onProviderEnabled(String provider) {
                Log.i(TAG, "onProviderEnabled()");
            }

            public void onProviderDisabled(String provider) {
                Log.i(TAG, "onProviderDisabled()");
            }
        };

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, locationListener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, locationListener);

    }

    //============================showMarker=========================================

    void showMarker(){
        Log.i(TAG,""+items_list.size());

        MapPoint mapPoint1;
        for(int i=0;i<items_list.size();i++){
            mapPoint1 = MapPoint.mapPointWithGeoCoord(Double.parseDouble(items_list.get(i).getY()), Double.parseDouble(items_list.get(i).getX()));

            marker = new MapPOIItem();
            marker.setItemName(items_list.get(i).getPlace_name());
            marker.setTag(i);
            marker.setMapPoint(mapPoint1);

            marker.setMarkerType(MapPOIItem.MarkerType.CustomImage);

            if(current_info_name.equals("편의점")) {
                marker.setCustomImageResourceId(R.drawable.marker_cvs);
            }
            else if(current_info_name.equals("병원")){
                marker.setCustomImageResourceId(R.drawable.marker_hospital);
            }
            else if(current_info_name.equals("약국")){
                marker.setCustomImageResourceId(R.drawable.marker_pill);
            }
            else if(current_info_name.equals("도서관")){
                marker.setCustomImageResourceId(R.drawable.marker_library);
            }
            else if(current_info_name.equals("세탁소")){
                marker.setCustomImageResourceId(R.drawable.marker_laundry);
            }
            else if(current_info_name.equals("공원")){
                marker.setCustomImageResourceId(R.drawable.marker_park);
            }
            else if(current_info_name.equals("영화관")){
                marker.setCustomImageResourceId(R.drawable.marker_film);
            }
            else if(current_info_name.equals("카페")){
                marker.setCustomImageResourceId(R.drawable.marker_cafe);
            }
            else if(current_info_name.equals("주차장")){
                marker.setCustomImageResourceId(R.drawable.marker_parking);
            }
            else if(current_info_name.equals("주유소")){
                marker.setCustomImageResourceId(R.drawable.marker_gas_station);
            }
            else if(current_info_name.equals("은행")){
                marker.setCustomImageResourceId(R.drawable.marker_bank);
            }
            else if(current_info_name.equals("대형마트")){
                marker.setCustomImageResourceId(R.drawable.marker_bigmart);
            }
            marker.setCustomImageAutoscale(false);

            mapView.addPOIItem(marker);
        }

    }

    //========================MapView.MapViewEventListener=============================

    @Override
    public void onMapViewInitialized(MapView mapView) {
        Log.i(TAG,"onMapViewInitialized()");
    }

    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {
        Log.i(TAG,"onMapViewCenterPointMoved()");
    }

    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int i) {
        Log.i(TAG,"onMapViewZoomLevelChanged()");
    }

    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {
        Log.i(TAG,"onMapViewSingleTapped()");
    }

    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {
        Log.i(TAG,"onMapViewDoubleTapped()");
    }

    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {
        Log.i(TAG,"onMapViewLongPressed()");
    }

    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {
        Log.i(TAG,"onMapViewDragStarted()");
        //mapView.removeAllPOIItems();
    }

    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {
        Log.i(TAG,"onMapViewDragEnded()");
    }

    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {
        Log.i(TAG,"onMapViewMoveFinished()");
        locationManager.removeUpdates(locationListener);

        mapView.removeCircle(circle);
        mapView.removePOIItem(search_marker);

        Log.i(TAG,""+mapPoint.getMapPointGeoCoord().latitude + " / " + mapPoint.getMapPointGeoCoord().longitude);

        current_location_lat = mapPoint.getMapPointGeoCoord().latitude;
        current_location_long = mapPoint.getMapPointGeoCoord().longitude;


        //search_marker.setItemName(temp.getAddress_name());
        search_marker.setTag(99);
        search_marker.setItemName("중심");
        search_marker.setMapPoint(mapPoint);
        search_marker.setMarkerType(MapPOIItem.MarkerType.CustomImage);
        search_marker.setCustomImageResourceId(R.drawable.center_point);
        search_marker.setCustomImageAutoscale(false);
        search_marker.setCustomImageAnchor(0.5f, 1.0f);
        mapView.addPOIItem(search_marker);

        circle = new MapCircle(
                MapPoint.mapPointWithGeoCoord(current_location_lat, current_location_long), // center
                Range_change.range, // radius
                Color.argb(100, 34, 50, 78), // strokeColor
                Color.argb(100, 237, 238, 239) // fillColor
        );
        circle.setTag(1234);
        mapView.addCircle(circle);

    }

    //========================MapView.POIItemEventListener=================================

    @Override
    public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {
        Log.i(TAG,"onPOIItemSelected()");
        //Toast.makeText(getActivity(),"터치1",Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {
        Log.i(TAG,"onCalloutBalloonOfPOIItemTouched_1()");
        //Toast.makeText(getActivity(),"터치2",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {
        Log.i(TAG,"onCalloutBalloonOfPOIItemTouched_2()");
        //Toast.makeText(getActivity(),"터치3",Toast.LENGTH_SHORT).show();

        try {
            Intent intent = new Intent(getActivity(), Marker_detail.class);

            intent.putExtra("getPlace_name", items_list.get(mapPOIItem.getTag()).getPlace_name());
            intent.putExtra("getAddress_name", items_list.get(mapPOIItem.getTag()).getAddress_name());
            intent.putExtra("getRoad_address_name", items_list.get(mapPOIItem.getTag()).getRoad_address_name());
            intent.putExtra("getCategory_name", items_list.get(mapPOIItem.getTag()).getCategory_name());
            intent.putExtra("getDistance", items_list.get(mapPOIItem.getTag()).getDistance());
            intent.putExtra("getPlace_url", items_list.get(mapPOIItem.getTag()).getPlace_url());
            intent.putExtra("getPhone", items_list.get(mapPOIItem.getTag()).getPhone());

            Log.i(TAG, items_list.get(mapPOIItem.getTag()).getDistance() + " / " + items_list.get(mapPOIItem.getTag()).getPhone());

            startActivityForResult(intent,0);
        }catch (Exception e){

        }


    }



    @Override
    public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {
        Log.i(TAG,"onDraggablePOIItemMoved()");
        //Toast.makeText(getActivity(),"터치4",Toast.LENGTH_SHORT).show();
    }


    //================================통신===========================================

    public void request_retrofit_get_Search_LocationName(final String address, String call_from){
        retrofit = new Retrofit.Builder()
                .baseUrl(base_url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofit_api = retrofit.create(Retrofit_API.class);

        if(call_from.equals("Search_input")){
            call = retrofit_api.get_Search_LocationName(rest_api_key, address, 1);
            call.enqueue(new Callback<Keyword_Result>() {
                @Override
                public void onResponse(Call<Keyword_Result> call, Response<Keyword_Result> response) {
                    if (response.isSuccessful()) {
                        Keyword_Result result = response.body();
                        List<Keyword_Items> keyword_items = result.getDocuments();
                        try {
                            Keyword_Items temp = keyword_items.get(0);

                            MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(Double.parseDouble(temp.getY()), Double.parseDouble(temp.getX()));
                            mapView.setMapCenterPoint(mapPoint, true);

                            MapPOIItem search_marker = new MapPOIItem();
                            search_marker.setItemName(temp.getPlace_name());
                            search_marker.setTag(99);
                            search_marker.setMapPoint(mapPoint);
                            search_marker.setMarkerType(MapPOIItem.MarkerType.CustomImage);
                            search_marker.setCustomImageResourceId(R.drawable.center_point);
                            search_marker.setCustomImageAutoscale(false);
                            mapView.addPOIItem(search_marker);
                        }
                        catch (Exception e){
                            call3 = retrofit_api.get_Search_LocationName_address(rest_api_key, address, 1,"similar");
                            call3.enqueue(new Callback<address_itmes>() {
                                @Override
                                public void onResponse(Call<address_itmes> call, Response<address_itmes> response) {
                                    if (response.isSuccessful()) {
                                        address_itmes result = response.body();
                                        List<address_itmes.Document> address_doc = result.getDocuments();
                                        try {
                                            address_itmes.Address temp = address_doc.get(0).getAddress();

                                            MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(Double.parseDouble(temp.getY()), Double.parseDouble(temp.getX()));
                                            mapView.setMapCenterPoint(mapPoint, true);

                                            MapPOIItem search_marker = new MapPOIItem();
                                            search_marker.setItemName(temp.getAddress_name());
                                            search_marker.setTag(99);
                                            search_marker.setMapPoint(mapPoint);
                                            search_marker.setMarkerType(MapPOIItem.MarkerType.CustomImage);
                                            search_marker.setCustomImageResourceId(R.drawable.center_point);
                                            search_marker.setCustomImageAutoscale(false);
                                            search_marker.setCustomImageAnchor(0.5f, 2.0f);
                                            mapView.addPOIItem(search_marker);
                                        }
                                        catch (Exception e){
                                            Toast.makeText(getContext(),"검색어를 확인해주세요.",Toast.LENGTH_SHORT).show();
                                        }


                                    } else {
                                        Log.i(TAG, "Retrofit Error - request_retrofit_get_Search_LocationName_address");
                                    }
                                }

                                @Override
                                public void onFailure(Call<address_itmes> call, Throwable t) {
                                    Log.i(TAG, "onFailure() - request_retrofit_get_Search_LocationName_address");
                                }
                            });
                        }


                    } else {
                        Log.i(TAG, "Retrofit Error - request_retrofit_get_Search_LocationName");
                    }
                }

                @Override
                public void onFailure(Call<Keyword_Result> call, Throwable t) {
                    Log.i(TAG, "onFailure() - request_retrofit_get_Search_LocationName");
                }
            });
        }
    }







    public void request_retrofit_get_Search_Location_Directly(double x, double y,int i, String info_name){
        retrofit = new Retrofit.Builder()
                .baseUrl(base_url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofit_api = retrofit.create(Retrofit_API.class);

        if(info_name.equals("편의점")) {

            call = retrofit_api.get_Search_Location_Directly_category(rest_api_key, "CS2", "편의점", String.valueOf(y), String.valueOf(x), 15, Range_change.range, i + 1, "distance");
            call.enqueue(new Callback<Keyword_Result>() {
                @Override
                public void onResponse(Call<Keyword_Result> call, Response<Keyword_Result> response) {
                    if (response.isSuccessful()) {
                        Keyword_Result result = response.body();

                        List<Keyword_Items> keyword_items = result.getDocuments();

                        //Log.i(TAG, "편의점 토탈카운트: " + result.getMeta().getTotal_count());
                        //Log.i(TAG, "키워드 아이템 사이즈: " + keyword_items.size());

                        if (result.getMeta().getIs_end() == true && limit == 1) {
                            running = false;
                            showMarker();
                        } else {
                            for (int i = 0; i < keyword_items.size(); i++) {
                                Keyword_Items temp = keyword_items.get(i);
                                items_list.add(temp);

                            }
                            if (result.getMeta().getIs_end() == true) {
                                limit = 1;
                            }
                            showMarker();

                        }

                        //Log.i(TAG, "아이템 사이즈: " + items_list.size());

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

        else if(info_name.equals("병원")){

            call = retrofit_api.get_Search_Location_Directly_category(rest_api_key, "HP8","병원", String.valueOf(y), String.valueOf(x), 15, Range_change.range, i + 1, "distance");
            call.enqueue(new Callback<Keyword_Result>() {
                @Override
                public void onResponse(Call<Keyword_Result> call, Response<Keyword_Result> response) {
                    if (response.isSuccessful()) {
                        Keyword_Result result = response.body();

                        List<Keyword_Items> keyword_items = result.getDocuments();

                        //Log.i(TAG, "토탈카운트: " + result.getMeta().getTotal_count());
                        //Log.i(TAG, "키워드 아이템 사이즈: " + keyword_items.size());

                        if (result.getMeta().getIs_end() == true && limit == 1) {
                            running = false;
                            showMarker();
                        } else {
                            for (int i = 0; i < keyword_items.size(); i++) {
                                Keyword_Items temp = keyword_items.get(i);
                                items_list.add(temp);

                            }
                            if (result.getMeta().getIs_end() == true) {
                                limit = 1;
                            }
                            showMarker();

                        }

                        //Log.i(TAG, "아이템 사이즈: " + items_list.size());

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

        else if(info_name.equals("약국")){

            call = retrofit_api.get_Search_Location_Directly_category(rest_api_key, "PM9","약국", String.valueOf(y), String.valueOf(x), 15, Range_change.range, i + 1, "distance");
            call.enqueue(new Callback<Keyword_Result>() {
                @Override
                public void onResponse(Call<Keyword_Result> call, Response<Keyword_Result> response) {
                    if (response.isSuccessful()) {
                        Keyword_Result result = response.body();

                        List<Keyword_Items> keyword_items = result.getDocuments();

                        //Log.i(TAG, "토탈카운트: " + result.getMeta().getTotal_count());
                        //Log.i(TAG, "키워드 아이템 사이즈: " + keyword_items.size());

                        if (result.getMeta().getIs_end() == true && limit == 1) {
                            running = false;
                            showMarker();
                        } else {
                            for (int i = 0; i < keyword_items.size(); i++) {
                                Keyword_Items temp = keyword_items.get(i);
                                items_list.add(temp);

                            }
                            if (result.getMeta().getIs_end() == true) {
                                limit = 1;
                            }
                            showMarker();

                        }

                        //Log.i(TAG, "아이템 사이즈: " + items_list.size());

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

        else if(info_name.equals("도서관")){

            call = retrofit_api.get_Search_Location_Directly(rest_api_key, "도서관", String.valueOf(y), String.valueOf(x), 15, Range_change.range, i + 1, "distance");
            call.enqueue(new Callback<Keyword_Result>() {
                @Override
                public void onResponse(Call<Keyword_Result> call, Response<Keyword_Result> response) {
                    if (response.isSuccessful()) {
                        Keyword_Result result = response.body();

                        List<Keyword_Items> keyword_items = result.getDocuments();

                        //Log.i(TAG, "토탈카운트: " + result.getMeta().getTotal_count());
                        //Log.i(TAG, "키워드 아이템 사이즈: " + keyword_items.size());

                        if (result.getMeta().getIs_end() == true && limit == 1) {
                            running = false;
                            showMarker();
                        } else {
                            for (int i = 0; i < keyword_items.size(); i++) {
                                //Log.i(TAG, result.getDocuments().get(i).getPlace_name());

                                /*Log.i(TAG,"페이지 여부: " + result.getMeta().getIs_end() + " / " + result.getMeta().getTotal_count());*/

                                Keyword_Items temp = keyword_items.get(i);
                                if(!(temp.getPlace_name().contains("도서관점") || temp.getPlace_name().contains("ATM") ||
                                        temp.getPlace_name().contains("민원") || temp.getPlace_name().contains("창구") ||
                                        temp.getPlace_name().contains("365") || temp.getPlace_name().contains("구내") ||
                                        temp.getPlace_name().contains("매점") || temp.getPlace_name().contains("식당"))) {
                                    items_list.add(temp);
                                }

                            }
                            if (result.getMeta().getIs_end() == true) {
                                limit = 1;
                            }
                            showMarker();

                        }

                        //Log.i(TAG, "아이템 사이즈: " + items_list.size());

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

        else if(info_name.equals("세탁소")){

            call = retrofit_api.get_Search_Location_Directly(rest_api_key, "세탁소", String.valueOf(y), String.valueOf(x), 15, Range_change.range, i + 1, "distance");
            call.enqueue(new Callback<Keyword_Result>() {
                @Override
                public void onResponse(Call<Keyword_Result> call, Response<Keyword_Result> response) {
                    if (response.isSuccessful()) {
                        Keyword_Result result = response.body();

                        List<Keyword_Items> keyword_items = result.getDocuments();

                        //Log.i(TAG, "토탈카운트: " + result.getMeta().getTotal_count());
                        //Log.i(TAG, "키워드 아이템 사이즈: " + keyword_items.size());

                        if (result.getMeta().getIs_end() == true && limit == 1) {
                            running = false;
                            showMarker();
                        } else {
                            for (int i = 0; i < keyword_items.size(); i++) {
                                Keyword_Items temp = keyword_items.get(i);
                                if(!(temp.getPlace_name().contains("필터추가하고싶을때사용"))) {
                                    items_list.add(temp);
                                }

                            }
                            if (result.getMeta().getIs_end() == true) {
                                limit = 1;
                            }
                            showMarker();

                        }

                        //Log.i(TAG, "아이템 사이즈: " + items_list.size());

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

        else if(info_name.equals("공원")){

            call = retrofit_api.get_Search_Location_Directly(rest_api_key, "공원", String.valueOf(y), String.valueOf(x), 15, Range_change.range, i + 1, "distance");
            call.enqueue(new Callback<Keyword_Result>() {
                @Override
                public void onResponse(Call<Keyword_Result> call, Response<Keyword_Result> response) {
                    if (response.isSuccessful()) {
                        Keyword_Result result = response.body();

                        List<Keyword_Items> keyword_items = result.getDocuments();

                        //Log.i(TAG, "토탈카운트: " + result.getMeta().getTotal_count());
                        //Log.i(TAG, "키워드 아이템 사이즈: " + keyword_items.size());

                        if (result.getMeta().getIs_end() == true && limit == 1) {
                            running = false;
                            showMarker();
                        } else {
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
                                    items_list.add(temp);
                                }

                            }
                            if (result.getMeta().getIs_end() == true) {
                                limit = 1;
                            }
                            showMarker();

                        }

                        //Log.i(TAG, "아이템 사이즈: " + items_list.size());

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

        else if(info_name.equals("영화관")){

            call = retrofit_api.get_Search_Location_Directly(rest_api_key, "영화관", String.valueOf(y), String.valueOf(x), 15, Range_change.range, i + 1, "distance");
            call.enqueue(new Callback<Keyword_Result>() {
                @Override
                public void onResponse(Call<Keyword_Result> call, Response<Keyword_Result> response) {
                    if (response.isSuccessful()) {
                        Keyword_Result result = response.body();

                        List<Keyword_Items> keyword_items = result.getDocuments();

                        //Log.i(TAG, "토탈카운트: " + result.getMeta().getTotal_count());
                        //Log.i(TAG, "키워드 아이템 사이즈: " + keyword_items.size());

                        if (result.getMeta().getIs_end() == true && limit == 1) {
                            running = false;
                            showMarker();
                        } else {
                            for (int i = 0; i < keyword_items.size(); i++) {
                                Keyword_Items temp = keyword_items.get(i);
                                if(!(temp.getPlace_name().contains("화장실"))) {
                                    items_list.add(temp);
                                }


                            }
                            if (result.getMeta().getIs_end() == true) {
                                limit = 1;
                            }
                            showMarker();
                        }

                        //Log.i(TAG, "아이템 사이즈: " + items_list.size());

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

        else if(info_name.equals("카페")){

            call = retrofit_api.get_Search_Location_Directly_category(rest_api_key, "CE7","카페", String.valueOf(y), String.valueOf(x), 15, Range_change.range, i + 1, "distance");
            call.enqueue(new Callback<Keyword_Result>() {
                @Override
                public void onResponse(Call<Keyword_Result> call, Response<Keyword_Result> response) {
                    if (response.isSuccessful()) {
                        Keyword_Result result = response.body();

                        List<Keyword_Items> keyword_items = result.getDocuments();

                        //Log.i(TAG, "토탈카운트: " + result.getMeta().getTotal_count());
                        //Log.i(TAG, "키워드 아이템 사이즈: " + keyword_items.size());

                        if (result.getMeta().getIs_end() == true && limit == 1) {
                            running = false;
                            showMarker();
                        } else {
                            for (int i = 0; i < keyword_items.size(); i++) {
                                Keyword_Items temp = keyword_items.get(i);
                                items_list.add(temp);
                            }
                            if (result.getMeta().getIs_end() == true) {
                                limit = 1;
                            }
                            showMarker();

                        }

                        //Log.i(TAG, "아이템 사이즈: " + items_list.size());

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

        else if(info_name.equals("주차장")){

            call = retrofit_api.get_Search_Location_Directly_category(rest_api_key, "PK6","주차장", String.valueOf(y), String.valueOf(x), 15, Range_change.range, i + 1, "distance");
            call.enqueue(new Callback<Keyword_Result>() {
                @Override
                public void onResponse(Call<Keyword_Result> call, Response<Keyword_Result> response) {
                    //통신 성공 시
                    if (response.isSuccessful()) {
                        Keyword_Result result = response.body();

                        List<Keyword_Items> keyword_items = result.getDocuments();

                        //Log.i(TAG, "토탈카운트: " + result.getMeta().getTotal_count());
                        //Log.i(TAG, "키워드 아이템 사이즈: " + keyword_items.size());

                        if (result.getMeta().getIs_end() == true && limit == 1) {
                            running = false;
                            showMarker();
                        } else {
                            for (int i = 0; i < keyword_items.size(); i++) {
                                Keyword_Items temp = keyword_items.get(i);
                                items_list.add(temp);

                            }
                            if (result.getMeta().getIs_end() == true) {
                                limit = 1;
                            }
                            showMarker();

                        }

                        //Log.i(TAG, "아이템 사이즈: " + items_list.size());

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

        else if(info_name.equals("주유소")){

            call = retrofit_api.get_Search_Location_Directly_category(rest_api_key, "OL7","주유소", String.valueOf(y), String.valueOf(x), 15, Range_change.range, i + 1, "distance");
            call.enqueue(new Callback<Keyword_Result>() {
                @Override
                public void onResponse(Call<Keyword_Result> call, Response<Keyword_Result> response) {
                    if (response.isSuccessful()) {
                        Keyword_Result result = response.body();

                        List<Keyword_Items> keyword_items = result.getDocuments();

                        //Log.i(TAG, "토탈카운트: " + result.getMeta().getTotal_count());
                        //Log.i(TAG, "키워드 아이템 사이즈: " + keyword_items.size());

                        if (result.getMeta().getIs_end() == true && limit == 1) {
                            running = false;
                            showMarker();
                        } else {
                            for (int i = 0; i < keyword_items.size(); i++) {
                                Keyword_Items temp = keyword_items.get(i);
                                items_list.add(temp);

                            }
                            if (result.getMeta().getIs_end() == true) {
                                limit = 1;
                            }
                            showMarker();

                        }

                        //Log.i(TAG, "아이템 사이즈: " + items_list.size());

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

        else if(info_name.equals("은행")){

            call = retrofit_api.get_Search_Location_Directly_category(rest_api_key, "BK9","은행", String.valueOf(y), String.valueOf(x), 15, Range_change.range, i + 1, "distance");
            call.enqueue(new Callback<Keyword_Result>() {
                @Override
                public void onResponse(Call<Keyword_Result> call, Response<Keyword_Result> response) {
                    if (response.isSuccessful()) {
                        Keyword_Result result = response.body();

                        List<Keyword_Items> keyword_items = result.getDocuments();

                        //Log.i(TAG, "토탈카운트: " + result.getMeta().getTotal_count());
                        //Log.i(TAG, "키워드 아이템 사이즈: " + keyword_items.size());

                        if (result.getMeta().getIs_end() == true && limit == 1) {
                            running = false;
                            showMarker();
                        } else {
                            for (int i = 0; i < keyword_items.size(); i++) {
                                Keyword_Items temp = keyword_items.get(i);
                                items_list.add(temp);

                            }
                            if (result.getMeta().getIs_end() == true) {
                                limit = 1;
                            }
                            showMarker();

                        }

                        //Log.i(TAG, "아이템 사이즈: " + items_list.size());

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

        else if(info_name.equals("대형마트")){

            call = retrofit_api.get_Search_Location_Directly_category(rest_api_key, "MT1","대형마트", String.valueOf(y), String.valueOf(x), 15, Range_change.range, i + 1, "distance");
            call.enqueue(new Callback<Keyword_Result>() {
                @Override
                public void onResponse(Call<Keyword_Result> call, Response<Keyword_Result> response) {
                    if (response.isSuccessful()) {
                        Keyword_Result result = response.body();

                        List<Keyword_Items> keyword_items = result.getDocuments();

                        //Log.i(TAG, "토탈카운트: " + result.getMeta().getTotal_count());
                        //Log.i(TAG, "키워드 아이템 사이즈: " + keyword_items.size());

                        if (result.getMeta().getIs_end() == true && limit == 1) {
                            running = false;
                            showMarker();
                        } else {
                            for (int i = 0; i < keyword_items.size(); i++) {
                                Keyword_Items temp = keyword_items.get(i);
                                items_list.add(temp);
                            }
                            if (result.getMeta().getIs_end() == true) {
                                limit = 1;
                            }
                            showMarker();
                        }

                        //Log.i(TAG, "아이템 사이즈: " + items_list.size());

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


    //================================권한 코드=======================================

    void Request_Permission(){
        AutoPermissions.Companion.loadAllPermissions(getActivity(), 1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        AutoPermissions.Companion.parsePermissions(getActivity(), requestCode, permissions, this);


    }
    @Override
    public void onDenied(int i, String[] strings) {
        Log.i(TAG,"onDenied()");
        Toast.makeText(getActivity(),"위치 권한을 허용해주세요.\n오직 사용자 위치 정보만 이용합니다.",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGranted(int i, String[] strings) {
        Log.i(TAG,"onGranted()");
    }

    //====================================클릭 시=========================================

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.fab_main:
                toggleFab("");
                break;
            case R.id.fab_cvs:
                toggleFab("편의점");
                break;
            case R.id.fab_hospital:
                toggleFab("병원");
                break;
            case R.id.fab_pill:
                toggleFab("약국");
                break;
            case R.id.fab_library:
                toggleFab("도서관");
                break;
            case R.id.fab_laundry:
                toggleFab("세탁소");
                break;
            case R.id.fab_park:
                toggleFab("공원");
                break;
            case R.id.fab_film:
                toggleFab("영화관");
                break;
            case R.id.fab_cafe:
                toggleFab("카페");
                break;
            case R.id.fab_parking:
                toggleFab("주차장");
                break;
            case R.id.fab_gas_station:
                toggleFab("주유소");
                break;
            case R.id.fab_bank:
                toggleFab("은행");
                break;
            case R.id.fab_bigmart:
                toggleFab("대형마트");
                break;
        }

    }

    private void toggleFab(String info_name) {
        if (isFabOpen) {
            fab_main.setImageResource(R.drawable.open);

            fab_cvs.startAnimation(fab_close);
            fab_hospital.startAnimation(fab_close);
            fab_pill.startAnimation(fab_close);
            fab_library.startAnimation(fab_close);
            fab_laundry.startAnimation(fab_close);
            fab_park.startAnimation(fab_close);
            fab_film.startAnimation(fab_close);
            fab_cafe.startAnimation(fab_close);
            fab_parking.startAnimation(fab_close);
            fab_gas_station.startAnimation(fab_close);
            fab_bank.startAnimation(fab_close);
            fab_bigmart.startAnimation(fab_close);

            fab_cvs.setClickable(false);
            fab_hospital.setClickable(false);
            fab_pill.setClickable(false);
            fab_library.setClickable(false);
            fab_laundry.setClickable(false);
            fab_park.setClickable(false);
            fab_film.setClickable(false);
            fab_cafe.setClickable(false);
            fab_parking.setClickable(false);
            fab_gas_station.setClickable(false);
            fab_bank.setClickable(false);
            fab_bigmart.setClickable(false);

            isFabOpen = false;

            limit = 0;
            running = true;

            items_list.clear();

            mapView.removeAllPOIItems();

            current_info_name = info_name;

            for (int i = 0; i < 3; i++) {
                if (running == false) {
                    return;
                } else {
                    request_retrofit_get_Search_Location_Directly(current_location_lat, current_location_long, i, info_name);
                }
            }
        } else {
            fab_main.setImageResource(R.drawable.close);

            fab_cvs.startAnimation(fab_open);
            fab_hospital.startAnimation(fab_open);
            fab_pill.startAnimation(fab_open);
            fab_library.startAnimation(fab_open);
            fab_laundry.startAnimation(fab_open);
            fab_park.startAnimation(fab_open);
            fab_film.startAnimation(fab_open);
            fab_cafe.startAnimation(fab_open);
            fab_parking.startAnimation(fab_open);
            fab_gas_station.startAnimation(fab_open);
            fab_bank.startAnimation(fab_open);
            fab_bigmart.startAnimation(fab_open);

            fab_cvs.setClickable(true);
            fab_hospital.setClickable(true);
            fab_pill.setClickable(true);
            fab_library.setClickable(true);
            fab_laundry.setClickable(true);
            fab_park.setClickable(true);
            fab_film.setClickable(true);
            fab_cafe.setClickable(true);
            fab_parking.setClickable(true);
            fab_gas_station.setClickable(true);
            fab_bank.setClickable(true);
            fab_bigmart.setClickable(true);

            isFabOpen = true;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            mapView.removeCircle(circle);

        }
    }

}