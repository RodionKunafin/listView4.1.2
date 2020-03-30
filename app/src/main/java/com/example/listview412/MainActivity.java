package com.example.listview412;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    SimpleAdapter listContentAdapter;
    public SharedPreferences SharedPref;
    public static String NOTE_TEXT = "note_text";



    public List<Map<String, String>> prepareContent() {
        final String[] strings = getString(R.string.large_text).split("\n");
        SharedPref = getPreferences(MODE_PRIVATE);
        final SharedPreferences.Editor myEditor = SharedPref.edit();
        myEditor.putString(NOTE_TEXT, String.valueOf(strings));
        myEditor.apply();


        final SwipeRefreshLayout swipeLayout = findViewById(R.id.swipeRefresh);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                listContentAdapter= (SimpleAdapter) prepareContent();
                listContentAdapter.notifyDataSetChanged();
                swipeLayout.setRefreshing(false);
            }
        });



        List<Map<String, String>> list = new ArrayList<>();
        for (String string : strings) {
            Map<String, String> firstMap = new HashMap<>();
            firstMap.put("left", String.valueOf(string.length()));
            firstMap.put("right", string);
            list.add(firstMap);

           /* Map<String, String> secondMap = new HashMap<>();
            secondMap.put("left", "Товарищ Абаддон");
            secondMap.put("right", "Абаддо́н Разоритель — Воитель Хаоса, командующий Чёрного Легиона, " +
                    "неформальный лидер, объединяющий всех космодесантников Хаоса и последователей " +
                    "Губительных Сил против Империума Человечества, в целостности свержение Императора" +
                    " Человечества и власти Терры, и по обстоятельству, месть за павшего примарха " +
                    "Хоруса Луперкаля. Организует Чёрные крестовые походы, последний из которых —" +
                    " 13-й. Ключевой целью постепенных и продвигающихся атак является захват Терры.");
            list.add(secondMap);*/
        }
        return list;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView listView = findViewById(R.id.listView);


        final List<Map<String, String>> values = prepareContent();
        String[] from = {"left", "right"};
        int[] to = {R.id.left_text, R.id.right_text};

        final BaseAdapter listContentAdapter = new SimpleAdapter(this, values, R.layout.item_simple, from, to);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.clear(view);//в этом месте не работают методы удаления строки из List,вот такая запись: values.remove(values) компилятор  на нее  не ругается но не работает
                listContentAdapter.notifyDataSetChanged();
            }

        });
        listView.setAdapter(listContentAdapter);


    }
}
