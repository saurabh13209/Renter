package com.twelo.renter;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private GridView gridView;
    private Database_Helper database_helper;
    private Cursor cursor;
    private String[] result_str = new String[9];



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        database_helper = new Database_Helper(getApplicationContext());

        cursor = database_helper.total_num_each_room();

        int i=0;
        while (cursor.moveToNext()){
            if (cursor.getInt(0)>6){
                result_str[cursor.getInt(0)-1] = "Filled "+cursor.getInt(1)+"/2";
            }
            else{
                result_str[cursor.getInt(0)-1] = "Filled "+cursor.getInt(1)+"/4";
            }


            i=i+1;
        }


        gridView = (GridView)findViewById(R.id.main_list);
        CustomAdapter customAdapter = new CustomAdapter();
        gridView.setAdapter(customAdapter);

        gridView.setOnItemClickListener(new GridView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this,InnerActivity.class);
                intent.putExtra("Room Num",position+1);
                startActivity(intent);
            }
        });
    }

    public class CustomAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return 9;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            convertView = getLayoutInflater().inflate(R.layout.main_list_eachrow,null);


            TextView num  = (TextView)convertView.findViewById(R.id.room_num);
            TextView where = (TextView)convertView.findViewById(R.id.where);
            TextView res = (TextView) convertView.findViewById(R.id.result);

            if (position>5){
                num.setText(String.valueOf(position-5));
                where.setText("Home");
                convertView.setBackgroundColor(getResources().getColor(R.color.home_clr));
            }
            else{
                num.setText(String.valueOf(position+1));
            }

            if (result_str[position]!=null){
                res.setText(result_str[position]);
            }



            return convertView;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id==R.id.About){
            startActivity(new Intent(MainActivity.this,AboutActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}
