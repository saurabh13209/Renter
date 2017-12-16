package com.twelo.renter;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class AddRenterActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private static  Integer REQ_CAPTURE = 1;
    private Database_Helper database;
    private Integer room_num;
    private ImageView img;
    private EditText name;
    private EditText phone;
    private EditText add;
    private EditText trade;
    private EditText year;
    private Button next;
    private Button profile;
    private Bitmap photo;

    private Rent_Maker rent;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==REQ_CAPTURE && resultCode==RESULT_OK){

            Bundle extras = data.getExtras();
            photo = (Bitmap) extras.get("data");
            img.setImageBitmap(photo);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_renter);


        img = (ImageView) findViewById(R.id.enter_prf_show);
        profile = (Button)findViewById(R.id.enter_prf);
        name = (EditText)findViewById(R.id.enter_name);
        phone = (EditText)findViewById(R.id.enter_phone);
        add = (EditText)findViewById(R.id.enter_address);
        trade = (EditText)findViewById(R.id.enter_trade);
        year = (EditText)findViewById(R.id.enter_year);
        next = (Button)findViewById(R.id.enter_submit);


        Intent intent = getIntent();
        final Bundle Extras = intent.getExtras();
        if (Extras!=null){
            Integer ch = Extras.getInt("type");
            if (ch==1){
                room_num = Extras.getInt("Room Num");}
            else{
                photo = BitmapFactory.decodeByteArray(Extras.getByteArray("Image"),0,Extras.getByteArray("Image").length);
                img.setImageBitmap(photo);
                name.setText(Extras.getString("Name"));
                phone.setText(Extras.getString("phone"));
                add.setText(Extras.getString("address"));
                trade.setText(Extras.getString("Trade"));
                year.setText(Extras.getString("year"));
                room_num = Extras.getInt("Room");


            }
        }


        toolbar = (Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("For Room Number : "+room_num);

        enter_data();

        profile.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {

                if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)){
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent,REQ_CAPTURE);

                }
                else{
                    Toast.makeText(AddRenterActivity.this, "Don't Have these facility", Toast.LENGTH_SHORT).show();
                }

            }
        });

        next.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {

                if (phone.getText().toString().length()==10){

                    if (!(year.getText().toString().endsWith("year")||year.getText().toString().endsWith("years")||year.getText().toString().endsWith("yrs")||year.getText().toString().endsWith("yr"))){
                        year.setText(year.getText().toString()+" year");
                    }
                    database = new Database_Helper(getApplicationContext());

                    rent = new Rent_Maker();

                    rent.setPhoto(photo);
                    rent.setRoom_num(room_num);
                    rent.setName(name.getText().toString());
                    rent.setCont_num(phone.getText().toString());
                    rent.setAdd(add.getText().toString());
                    rent.setTrad(trade.getText().toString());
                    rent.setYear(year.getText().toString());

                    try {
                        if (Extras.getInt("type")==1){
                            save_data();
                        }else{
                            database.delete_rent(Extras.getString("Name"),room_num);
                            save_data();
                            Toast.makeText(AddRenterActivity.this, "Edit_me", Toast.LENGTH_SHORT).show();
                        }
                        
                    }catch (Exception e){
                        Toast.makeText(AddRenterActivity.this, "Sorry! Some error occurred ..\n Check your Entered data", Toast.LENGTH_LONG).show();
                    }


                }
                else{
                    Toast.makeText(AddRenterActivity.this, "Please enter correct number", Toast.LENGTH_SHORT).show();
                }
            }

            private void save_data() {

                long res = database.Add_item(rent);

                if (res == -1) {
                    if (Extras.getInt("type")==1){
                        Toast.makeText(AddRenterActivity.this, "Phone is already entered", Toast.LENGTH_SHORT).show();
                    }

                }
                else{
                    startActivity(new Intent(AddRenterActivity.this,InnerActivity.class).putExtra("Room Num",room_num));
                    finish();
                }

            }
        });

    }

    private void enter_data() {
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length()==20){
                    name.setText(s.toString().substring(0,19));
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length()==11){
                    phone.setText(s.toString().substring(0,10));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }
}
