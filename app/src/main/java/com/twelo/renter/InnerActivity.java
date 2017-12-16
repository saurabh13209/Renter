package com.twelo.renter;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class InnerActivity extends AppCompatActivity {

    private ListView inner_list;
    private Toolbar toolbar;
    private Database_Helper database_helper;
    private Integer num;

    private ArrayList<String> Name_lst = new ArrayList<>();
    private ArrayList<String> Trade_lst = new ArrayList<>();
    private ArrayList<String> year_lst = new ArrayList<>();
    private ArrayList<byte[]> img_lst = new ArrayList<>();

    Cursor cursor;


    @Override
    public void onBackPressed() {
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inner);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);


        final Intent intent = getIntent();

        Bundle Extras = intent.getExtras();

        if (Extras != null) {
            num = Extras.getInt("Room Num");
        }

        database_helper = new Database_Helper(getApplicationContext());
        cursor = database_helper.get_inner_list_item(num);
        
        while (cursor.moveToNext()){
            img_lst.add(cursor.getBlob(0));
            Name_lst.add(cursor.getString(1));
            Trade_lst.add(cursor.getString(2));
            year_lst.add(cursor.getString(3));

        }

        getSupportActionBar().setTitle("Room Number : "+num);
        inner_list = (ListView) findViewById(R.id.inner_list);
        Addapter addapter = new Addapter();

        inner_list.setAdapter(addapter);

        inner_list.setOnItemClickListener(new ListView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView text = (TextView)view.findViewById(R.id.inn_name);

                if (text.getText().toString().toLowerCase().equals("name")){
                    Intent intent1 = new Intent(InnerActivity.this,AddRenterActivity.class);
                    intent1.putExtra("type",1);
                    intent1.putExtra("Room Num",num);
                    startActivity(intent1);
                }
                else{
                    Intent intent1 = new Intent(InnerActivity.this,ShowActivity.class);
                    intent1.putExtra("Name",Name_lst.get(position));
                    intent1.putExtra("Room Num",num);
                    intent1.putExtra("Image",img_lst.get(position));
                    startActivity(intent1);
                }
            }
        });


    }

    public class Addapter extends BaseAdapter {

        @Override
        public int getCount() {
            if (num>5){
                return 2;
            }
            else{
                return 4;
            }
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
            convertView = getLayoutInflater().inflate(R.layout.inner_list_eachrow, null);
            TextView name=  (TextView)convertView.findViewById(R.id.inn_name);
            TextView trade = (TextView)convertView.findViewById(R.id.inn_trade);
            TextView yrs = (TextView)convertView.findViewById(R.id.inn_yrs);

            ImageView img = (ImageView)convertView.findViewById(R.id.inn_img);

            try{

                Bitmap main_img = BitmapFactory.decodeByteArray(img_lst.get(position), 0, img_lst.get(position).length);
                img.setImageBitmap(main_img);


                name.setText(Name_lst.get(position).toString());
                trade.setText(Trade_lst.get(position).toString());
                yrs.setText(year_lst.get(position).toString());

            }catch (Exception e){

            }

            return convertView;
        }
    }
}
