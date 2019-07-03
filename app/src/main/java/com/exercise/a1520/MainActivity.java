package com.exercise.a1520;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Time;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements RegisterDialog.RegisterListener {
    private LinearLayout draw;
    private TextView tv_name;
    private Button btnStart;
    private String name;
    private final int REQUEST_CODE = 8080;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        draw = findViewById(R.id.draw);
        btnStart = findViewById(R.id.btnStart);
        draw.addView(new StartIcon(this));
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

        initialDB();
    }

    public void openDialog() {
        RegisterDialog rd = new RegisterDialog();
        rd.show(getSupportFragmentManager(), "Register");
    }

    public void initialDB() {
        // Create a database if it does not exist
        try {
            SQLiteDatabase db = SQLiteDatabase.openDatabase("/data/data/com.exercise.a1520/GameDB", null, SQLiteDatabase.CREATE_IF_NECESSARY);

            db.execSQL("CREATE TABLE IF NOT EXISTS GameLog (gameDate TEXT, gameTime TEXT, opponentName TEXT, winOrLose INTEGER, PRIMARY KEY(gameDate,gameTime));");
            Cursor c = db.rawQuery("select * from GameLog ORDER BY gameDate, gameTime", null);
            c.moveToNext();
            //Toast.makeText(this, c.getString(2),Toast.LENGTH_LONG).show();
            db.close();
        } catch (SQLiteException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void getText(String name, boolean isName) {
        if(isName) {
            //setContentView(R.layout.game);
            Intent i = new Intent(MainActivity.this, GameActivity.class);
            i.putExtra("name", name);
            startActivity(i);
        }else{
            openDialog();
            Toast.makeText(getApplication(), name, Toast.LENGTH_LONG).show();
        }
    }

    class StartIcon extends View {
        private boolean paper;

        public StartIcon(Context context) {
            super(context);
        }

        @Override
        public void onDraw(Canvas canvas) {
            canvas.drawColor(Color.BLACK);
            Bitmap icon = null;
            icon = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
            canvas.drawBitmap(icon, getWidth() * 0.2f, getHeight() * 0.2f, null);
            //returns bitmap of image in any drawable folder contained in res folder
            if (paper) {
                icon = BitmapFactory.decodeResource(getResources(), R.drawable.l_rock);
            } else {
                icon = BitmapFactory.decodeResource(getResources(), R.drawable.l_paper);
            }


            //drawBitmap(Bitmap bitmap, float left, float top, Paint paint)
            //Draw the specified bitmap, with its top/left corner at (x,y),
            //using the specified paint, transformed by the current matrix.
            canvas.drawBitmap(icon, getWidth() * 0.32f, getHeight() * 0.45f, null);

        }

        public boolean onTouchEvent(MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                if (event.getX() >= getWidth() * 0.32f && event.getX() <= getWidth() * 0.65f && event.getY() >= getHeight() * 0.4f && event.getY() <= getHeight() * 0.7f)
                    paper = !paper;
            }
            invalidate();
            return true;
        }
    }
}
