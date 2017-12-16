package com.twelo.renter;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ShowActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private byte[] img_byte;

    private String name_main;
    private Integer room_main;

    private ImageView profile_img;
    private TextView name;
    private TextView phone;
    private TextView address;
    private TextView trade;
    private TextView year;
    private TextView room;

    private Database_Helper database_helper;

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ShowActivity.this, InnerActivity.class).putExtra("Room Num", room_main));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);


        Intent intent = getIntent();

        Bundle bundle = intent.getExtras();

        profile_img = (ImageView) findViewById(R.id.profile_image);
        name = (TextView) findViewById(R.id.show_name);
        phone = (TextView) findViewById(R.id.show_phone);
        address = (TextView) findViewById(R.id.show_address);
        trade = (TextView) findViewById(R.id.show_trade);
        year = (TextView) findViewById(R.id.show_year);
        room = (TextView) findViewById(R.id.show_room_num);

        if (bundle != null) {
            name_main = bundle.getString("Name");
            room_main = bundle.getInt("Room Num");

            img_byte = bundle.getByteArray("Image");
            profile_img.setImageBitmap(BitmapFactory.decodeByteArray(img_byte, 0, img_byte.length));
        }


        database_helper = new Database_Helper(getApplicationContext());


        set_data();
        ImageButton call = (ImageButton) findViewById(R.id.show_call);
        call.setOnClickListener(new ImageButton.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_CALL);

                intent.setData(Uri.parse("tel:" + phone.getText().toString()));
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    Intent intent2 = new Intent(Intent.ACTION_DIAL);
                    intent2.setData(Uri.parse("tel:" + phone.getText().toString()));
                    startActivity(intent2);
                    return;
                }
                startActivity(intent);

                Toast.makeText(ShowActivity.this, "Calling", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void set_data() {

        Cursor cur = database_helper.get_Table(name_main, room_main);
        while (cur.moveToNext()) {
            name.setText(cur.getString(2));
            phone.setText(cur.getString(3));
            address.setText(cur.getString(4));
            trade.setText(cur.getString(5));
            year.setText(cur.getString(6));
            room.setText("Room Num " + cur.getString(1));


        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.show_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.edit_rent) {
            Intent intent = new Intent(ShowActivity.this, AddRenterActivity.class);
            intent.putExtra("type", 2);

            intent.putExtra("Image", img_byte);
            intent.putExtra("Name", name_main);
            intent.putExtra("phone", phone.getText().toString());
            intent.putExtra("address", address.getText().toString());
            intent.putExtra("Trade", trade.getText().toString());
            intent.putExtra("year", year.getText().toString());
            intent.putExtra("Room", room_main);

            startActivity(intent);
            finish();

        }

        if (id == R.id.delete_rent) {

            final AlertDialog.Builder builder = new AlertDialog.Builder(ShowActivity.this);
            View view = getLayoutInflater().inflate(R.layout.delete_sure, null);
            builder.setView(view);

            final AlertDialog dialog = builder.create();
            dialog.show();

            Button yes = (Button) view.findViewById(R.id.del_yes);
            Button no = (Button) view.findViewById(R.id.del_no);

            yes.setOnClickListener(new Button.OnClickListener() {

                @Override
                public void onClick(View v) {
                    database_helper.delete_rent(name_main, room_main);
                    startActivity(new Intent(ShowActivity.this, InnerActivity.class).putExtra("Room Num", room_main));
                    finish();
                }
            });

            no.setOnClickListener(new Button.OnClickListener() {

                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }

        if (id == R.id.call_rent) {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + phone.getText().toString()));

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                Intent intent2 = new Intent(Intent.ACTION_DIAL);
                intent2.setData(Uri.parse("tel:" + phone.getText().toString()));
                startActivity(intent2);

            } else {
                startActivity(intent);
            }


            Toast.makeText(this, "Calling", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}
