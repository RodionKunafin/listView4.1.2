package com.example.listview412;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    SimpleAdapter listContentAdapter;
    public SharedPreferences sharedPref;
    public static String NOTE_TEXT = "note_text";
    private TextView mSaveText;//новое



    public List<Map<String, String>> prepareContent() {
        final String[] strings = getString(R.string.large_text).split("\n");
        sharedPref = getSharedPreferences("MyNote",MODE_PRIVATE);//новое
        SharedPreferences.Editor myEditor = sharedPref.edit();
        String noteTxt = mSaveText.getText().toString();//новое
        myEditor.putString(NOTE_TEXT, noteTxt);//новое
        myEditor.apply();

        final SwipeRefreshLayout swipeLayout = findViewById(R.id.swipeRefresh);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                String noteTxt = sharedPref.getString(NOTE_TEXT, "");//новое
                mSaveText.setText(noteTxt);//НОВОЕ
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
        }
        return list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ListView listView = findViewById(R.id.list);
        mSaveText = findViewById(R.id.saveText);


        final List<Map<String, String>> values = prepareContent();
        String[] from = {"left", "right"};
        int[] to = {R.id.left_text, R.id.right_text};
        final BaseAdapter listContentAdapter = new SimpleAdapter(this, values, R.layout.item_simple, from, to);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                values.remove(0);
                listContentAdapter.notifyDataSetChanged();
            }
        });
        listView.setAdapter(listContentAdapter);


    }
}
