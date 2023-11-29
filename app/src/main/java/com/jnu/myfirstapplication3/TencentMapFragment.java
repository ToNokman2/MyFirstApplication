package com.jnu.myfirstapplication3;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.tencent.tencentmap.mapsdk.maps.CameraUpdateFactory;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;
import com.tencent.tencentmap.mapsdk.maps.model.Marker;
import com.tencent.tencentmap.mapsdk.maps.model.MarkerOptions;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TencentMapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TencentMapFragment extends Fragment {
    private com.tencent.tencentmap.mapsdk.maps.MapView mapView = null;

    public TencentMapFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment BaiduMapFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TencentMapFragment newInstance(String param1, String param2) {
        TencentMapFragment fragment = new TencentMapFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_tencent_map, container, false);
        mapView = rootView.findViewById(R.id.mapView);

        TencentMap TCMap = mapView.getMap();
//
        // 添加图标型Marker
        LatLng position1 = new LatLng(22.252731, 113.535649);//JNU坐标
        TCMap.moveCamera(CameraUpdateFactory.newLatLng(position1));

        // 创建一个Marker对象
        MarkerOptions markerOptions = new MarkerOptions(position1);
        //点击Marker是否弹信息框
        markerOptions.infoWindowEnable(true);//默认为true
        //信息框编辑
        markerOptions.title("暨南大学(珠海校区)")
                .snippet("坐标:(22.252702,113.53562)");
        // 添加标记到地图上
        Marker marker = TCMap.addMarker(markerOptions);
        //显示信息窗口
        marker.showInfoWindow();
        //设置Marker点击事件
        TCMap.setOnInfoWindowClickListener(new TencentMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Log.i("TAG","InfoWindow被点击时回调函数");
            }
            @Override
            public void onInfoWindowClickLocation(int width, int height, int x, int y) {
                Log.i("TAG","当InfoWindow点击时，点击的回调");
            }
        });
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }
    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }
    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
}