package com.story.egypt;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;

public class GameLoopActivity extends AppCompatActivity {

    private static final String SAVED_MONEY = "money";
    private static final String SAVED_LEVEL = "level";
    private static final String SAVED_KOEF = "koef";

    @BindView(R.id.i1)
    ImageView i1;
    @BindView(R.id.i2)
    ImageView i2;
    @BindView(R.id.i3)
    ImageView i3;
    @BindView(R.id.i4)
    ImageView i4;
    @BindView(R.id.i5)
    ImageView i5;
    @BindView(R.id.i6)
    ImageView i6;
    @BindView(R.id.i7)
    ImageView i7;
    @BindView(R.id.i8)
    ImageView i8;
    @BindView(R.id.i9)
    ImageView i9;
    @BindView(R.id.i10)
    ImageView i10;
    @BindView(R.id.i11)
    ImageView i11;
    @BindView(R.id.i12)
    ImageView i12;
    @BindView(R.id.i13)
    ImageView i13;
    @BindView(R.id.i14)
    ImageView i14;
    @BindView(R.id.i15)
    ImageView i15;

    @BindView(R.id.btn_spin)
    ImageButton btn_spin;
    @BindView(R.id.btn_coin_value_minus)
    ImageButton btn_coin_value_minus;
    @BindView(R.id.btn_coin_value_plus)
    ImageButton btn_coin_value_plus;
    @BindView(R.id.btn_level_minus)
    ImageButton btn_level_minus;
    @BindView(R.id.btn_level_plus)
    ImageButton btn_level_plus;
    @BindView(R.id.btn_back)
    ImageButton btn_back;

    @BindView(R.id.coins_text)
    TextView coins_text;
    @BindView(R.id.coin_value_text)
    TextView coin_value_text;
    @BindView(R.id.level_text)
    TextView level_text;
    @BindView(R.id.spin_text)
    TextView spin_text;

    @BindDrawable(R.drawable.i1)
    Drawable img1;
    @BindDrawable(R.drawable.i2)
    Drawable img2;
    @BindDrawable(R.drawable.i3)
    Drawable img3;
    @BindDrawable(R.drawable.i4)
    Drawable img4;
    @BindDrawable(R.drawable.i5)
    Drawable img5;

    SharedPreferences sp;

    private boolean running, rotate = false;
    private int x, balance, spin_money = 100, level, miliseconds, rotation, a = 0;
    private float koef = 0.05f;

    private int imageIndex1,
            imageIndex2,
            imageIndex3,
            imageIndex4,
            imageIndex5,
            imageIndex6,
            imageIndex7,
            imageIndex8,
            imageIndex9,
            imageIndex10,
            imageIndex11,
            imageIndex12,
            imageIndex13,
            imageIndex14,
            imageIndex15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_loop);
        ButterKnife.bind(this);
        load();
        updateKoefAndLevel();

        btn_spin.setOnClickListener(v -> {
            if(!running && balance >= spin_money*koef*level) {
                if(balance < 100){
                    balance+=1000;
                }
                i1.setRotation(0);i2.setRotation(0);i3.setRotation(0);i4.setRotation(0);i5.setRotation(0);i6.setRotation(0);i7.setRotation(0);i8.setRotation(0);i9.setRotation(0);i10.setRotation(0);i11.setRotation(0);i12.setRotation(0);i13.setRotation(0);i14.setRotation(0);i15.setRotation(0);
                a = 0;
                balance-=spin_money*koef*level;
                updateKoefAndLevel();
                running = true;
            }
        });
        btn_coin_value_minus.setOnClickListener(v -> {
            if(koef >= 1) koef/=5;
            updateKoefAndLevel();
        });
        btn_coin_value_plus.setOnClickListener(v -> {
            if(koef < 2) koef*=5;
            updateKoefAndLevel();
        });
        btn_level_minus.setOnClickListener(v -> {
            if(level > 1) level--;
            updateKoefAndLevel();
        });
        btn_level_plus.setOnClickListener(v -> {
            if(level < 10) level++;
            updateKoefAndLevel();
        });
        btn_back.setOnClickListener(v -> {
            if(!running){
                createDialogWindow();
            }
        });

        gameUpdater();
    }

    private void spinSlot(){
        if(miliseconds < 50) {
            imageIndex1 = generateImage();
            setImage(i1, imageIndex1);
            imageIndex2 = generateImage();
            setImage(i2, imageIndex2);
            imageIndex3 = generateImage();
            setImage(i3, imageIndex3);
        }
        if(miliseconds < 60) {
            imageIndex4 = generateImage();
            setImage(i4, imageIndex4);
            imageIndex5 = generateImage();
            setImage(i5, imageIndex5);
            imageIndex6 = generateImage();
            setImage(i6, imageIndex6);
        }
        if(miliseconds < 70) {
            imageIndex7 = generateImage();
            setImage(i7, imageIndex7);
            imageIndex8 = generateImage();
            setImage(i8, imageIndex8);
            imageIndex9 = generateImage();
            setImage(i9, imageIndex9);
        }
        if(miliseconds < 80) {
            imageIndex10 = generateImage();
            setImage(i10, imageIndex10);
            imageIndex11 = generateImage();
            setImage(i11, imageIndex11);
            imageIndex12 = generateImage();
            setImage(i12, imageIndex12);
        }
        if(miliseconds < 90) {
            imageIndex13 = generateImage();
            setImage(i13, imageIndex13);
            imageIndex14 = generateImage();
            setImage(i14, imageIndex14);
            imageIndex15 = generateImage();
            setImage(i15, imageIndex15);
        }
        if(miliseconds > 90){
            stopAndCheck();
            updateKoefAndLevel();
            save();
        }
    }

    private int generateImage(){
        Random random = new Random();
        return 1 + Math.abs(random.nextInt()%5);
    }

    private void setImage(ImageView view, int index){
        if(index == 1){
            view.setImageDrawable(img1);
        }
        if(index == 2){
            view.setImageDrawable(img2);
        }
        if(index == 3){
            view.setImageDrawable(img3);
        }
        if(index == 4){
            view.setImageDrawable(img4);
        }
        if(index == 5){
            view.setImageDrawable(img5);
        }
    }

    private void stopAndCheck(){
        running = false;
        miliseconds = 0;
        rotation = 0;
        rotate = true;
        rotateImages();
    }

    private void rotateImages(){
        if(rotate){
             if(imageIndex2 == imageIndex5 && imageIndex5 == imageIndex8 && imageIndex8 == imageIndex11 && imageIndex11 == imageIndex14){
                i2.setRotation(rotation);
                i5.setRotation(rotation);
                i8.setRotation(rotation);
                i11.setRotation(rotation);
                i14.setRotation(rotation);
                x = 8;
                if(a == 0){
                    balance += spin_money * koef * level * x;
                    updateKoefAndLevel();
                }
                a = 1;
            }
            else if(imageIndex2 == imageIndex5 && imageIndex5 == imageIndex8 && imageIndex8 == imageIndex11 && imageIndex11 == imageIndex13){
                i2.setRotation(rotation);
                i5.setRotation(rotation);
                i8.setRotation(rotation);
                i11.setRotation(rotation);
                i13.setRotation(rotation);
                x = 8;
                if(a == 0){
                    balance += spin_money * koef * level * x;
                    updateKoefAndLevel();
                }
                a = 1;
            }
            else if(imageIndex2 == imageIndex5 && imageIndex5 == imageIndex8 && imageIndex8 == imageIndex11 && imageIndex11 == imageIndex15){
                i2.setRotation(rotation);
                i5.setRotation(rotation);
                i8.setRotation(rotation);
                i11.setRotation(rotation);
                i15.setRotation(rotation);
                x = 8;
                if(a == 0){
                    balance += spin_money * koef * level * x;
                    updateKoefAndLevel();
                }
                a = 1;
            }
            else if(imageIndex2 == imageIndex5 && imageIndex5 == imageIndex7 && imageIndex7 == imageIndex11 && imageIndex11 == imageIndex14){
                i2.setRotation(rotation);
                i5.setRotation(rotation);
                i7.setRotation(rotation);
                i11.setRotation(rotation);
                i14.setRotation(rotation);
                x = 8;
                if(a == 0){
                    balance += spin_money * koef * level * x;
                    updateKoefAndLevel();
                }
                a = 1;
            }
            else if(imageIndex2 == imageIndex5 && imageIndex5 == imageIndex9 && imageIndex9 == imageIndex11 && imageIndex11 == imageIndex14){
                i2.setRotation(rotation);
                i5.setRotation(rotation);
                i9.setRotation(rotation);
                i11.setRotation(rotation);
                i14.setRotation(rotation);
                x = 8;
                if(a == 0){
                    balance += spin_money * koef * level * x;
                    updateKoefAndLevel();
                }
                a = 1;
            }
            else if(imageIndex2 == imageIndex4 && imageIndex4 == imageIndex7 && imageIndex7 == imageIndex10 && imageIndex10 == imageIndex14){
                i2.setRotation(rotation);
                i4.setRotation(rotation);
                i7.setRotation(rotation);
                i10.setRotation(rotation);
                i14.setRotation(rotation);
                x = 8;
                if(a == 0){
                    balance += spin_money * koef * level * x;
                    updateKoefAndLevel();
                }
                a = 1;
            }
            else if(imageIndex2 == imageIndex6 && imageIndex6 == imageIndex9 && imageIndex9 == imageIndex12 && imageIndex12 == imageIndex14){
                i2.setRotation(rotation);
                i6.setRotation(rotation);
                i9.setRotation(rotation);
                i12.setRotation(rotation);
                i14.setRotation(rotation);
                x = 8;
                if(a == 0){
                    balance += spin_money * koef * level * x;
                    updateKoefAndLevel();
                }
                a = 1;
            }
            else if(imageIndex2 == imageIndex4 && imageIndex4 == imageIndex8 && imageIndex8 == imageIndex11 && imageIndex11 == imageIndex14){
                i2.setRotation(rotation);
                i4.setRotation(rotation);
                i8.setRotation(rotation);
                i11.setRotation(rotation);
                i14.setRotation(rotation);
                x = 8;
                if(a == 0){
                    balance += spin_money * koef * level * x;
                    updateKoefAndLevel();
                }
                a = 1;
            }
            else if(imageIndex2 == imageIndex6 && imageIndex6 == imageIndex8 && imageIndex8 == imageIndex11 && imageIndex11 == imageIndex14) {
                 i2.setRotation(rotation);
                 i6.setRotation(rotation);
                 i8.setRotation(rotation);
                 i11.setRotation(rotation);
                 i14.setRotation(rotation);
                 x = 8;
                 if (a == 0) {
                     balance += spin_money * koef * level * x;
                     updateKoefAndLevel();
                 }
                 a = 1;
             }
             else if(imageIndex3 == imageIndex5 && imageIndex5 == imageIndex8 && imageIndex8 == imageIndex11 && imageIndex11 == imageIndex14) {
                 i3.setRotation(rotation);
                 i5.setRotation(rotation);
                 i8.setRotation(rotation);
                 i11.setRotation(rotation);
                 i14.setRotation(rotation);
                 x = 8;
                 if (a == 0) {
                     balance += spin_money * koef * level * x;
                     updateKoefAndLevel();
                 }
                 a = 1;
             }
             else if(imageIndex1 == imageIndex5 && imageIndex5 == imageIndex8 && imageIndex8 == imageIndex11 && imageIndex11 == imageIndex14) {
                 i1.setRotation(rotation);
                 i5.setRotation(rotation);
                 i8.setRotation(rotation);
                 i11.setRotation(rotation);
                 i14.setRotation(rotation);
                 x = 8;
                 if (a == 0) {
                     balance += spin_money * koef * level * x;
                     updateKoefAndLevel();
                 }
                 a = 1;
             }
             else if(imageIndex2 == imageIndex5 && imageIndex5 == imageIndex8 && imageIndex8 == imageIndex12 && imageIndex12 == imageIndex15){
                 i2.setRotation(rotation);
                 i5.setRotation(rotation);
                 i8.setRotation(rotation);
                 i12.setRotation(rotation);
                 i15.setRotation(rotation);
                 x = 8;
                 if(a == 0){
                     balance += spin_money * koef * level * x;
                     updateKoefAndLevel();
                 }
                 a = 1;
             }
             else if(imageIndex3 == imageIndex5 && imageIndex5 == imageIndex8 && imageIndex8 == imageIndex11 && imageIndex11 == imageIndex13){
                 i3.setRotation(rotation);
                 i5.setRotation(rotation);
                 i8.setRotation(rotation);
                 i11.setRotation(rotation);
                 i13.setRotation(rotation);
                 x = 8;
                 if(a == 0){
                     balance += spin_money * koef * level * x;
                     updateKoefAndLevel();
                 }
                 a = 1;
             }
             else if(imageIndex3 == imageIndex5 && imageIndex5 == imageIndex8 && imageIndex8 == imageIndex10 && imageIndex10 == imageIndex13){
                 i3.setRotation(rotation);
                 i5.setRotation(rotation);
                 i8.setRotation(rotation);
                 i10.setRotation(rotation);
                 i13.setRotation(rotation);
                 x = 8;
                 if(a == 0){
                     balance += spin_money * koef * level * x;
                     updateKoefAndLevel();
                 }
                 a = 1;
             }
             else if(imageIndex3 == imageIndex5 && imageIndex5 == imageIndex8 && imageIndex8 == imageIndex12 && imageIndex12 == imageIndex15){
                 i3.setRotation(rotation);
                 i5.setRotation(rotation);
                 i8.setRotation(rotation);
                 i12.setRotation(rotation);
                 i15.setRotation(rotation);
                 x = 8;
                 if(a == 0){
                     balance += spin_money * koef * level * x;
                     updateKoefAndLevel();
                 }
                 a = 1;
             }
             else if(imageIndex1 == imageIndex5 && imageIndex5 == imageIndex8 && imageIndex8 == imageIndex11 && imageIndex11 == imageIndex13){
                 i1.setRotation(rotation);
                 i5.setRotation(rotation);
                 i8.setRotation(rotation);
                 i11.setRotation(rotation);
                 i13.setRotation(rotation);
                 x = 8;
                 if(a == 0){
                     balance += spin_money * koef * level * x;
                     updateKoefAndLevel();
                 }
                 a = 1;
             }
             else if(imageIndex1 == imageIndex5 && imageIndex5 == imageIndex8 && imageIndex8 == imageIndex12 && imageIndex12 == imageIndex15){
                 i1.setRotation(rotation);
                 i5.setRotation(rotation);
                 i8.setRotation(rotation);
                 i12.setRotation(rotation);
                 i15.setRotation(rotation);
                 x = 8;
                 if(a == 0){
                     balance += spin_money * koef * level * x;
                     updateKoefAndLevel();
                 }
                 a = 1;
             }
            else if(imageIndex2 == imageIndex5 && imageIndex5 == imageIndex8 && imageIndex8 == imageIndex11){
                i2.setRotation(rotation);
                i5.setRotation(rotation);
                i8.setRotation(rotation);
                i11.setRotation(rotation);
                x = 4;
                if(a == 0){
                    balance += spin_money * koef * level * x;
                    updateKoefAndLevel();
                }
                a = 1;
            }
            else if(imageIndex2 == imageIndex5 && imageIndex5 == imageIndex8 && imageIndex8 == imageIndex10){
                i2.setRotation(rotation);
                i5.setRotation(rotation);
                i8.setRotation(rotation);
                i10.setRotation(rotation);
                x = 4;
                if(a == 0){
                    balance += spin_money * koef * level * x;
                    updateKoefAndLevel();
                }
                a = 1;
            }
            else if(imageIndex2 == imageIndex5 && imageIndex5 == imageIndex8 && imageIndex8 == imageIndex12){
                i2.setRotation(rotation);
                i5.setRotation(rotation);
                i8.setRotation(rotation);
                i12.setRotation(rotation);
                x = 4;
                if(a == 0){
                    balance += spin_money * koef * level * x;
                    updateKoefAndLevel();
                }
                a = 1;
            }
            else if(imageIndex2 == imageIndex5 && imageIndex5 == imageIndex7 && imageIndex7 == imageIndex10){
                i2.setRotation(rotation);
                i5.setRotation(rotation);
                i7.setRotation(rotation);
                i10.setRotation(rotation);
                x = 4;
                if(a == 0){
                    balance += spin_money * koef * level * x;
                    updateKoefAndLevel();
                }
                a = 1;
            }
            else if(imageIndex2 == imageIndex5 && imageIndex5 == imageIndex9 && imageIndex9 == imageIndex12){
                i2.setRotation(rotation);
                i5.setRotation(rotation);
                i9.setRotation(rotation);
                i12.setRotation(rotation);
                x = 4;
                if(a == 0){
                    balance += spin_money * koef * level * x;
                    updateKoefAndLevel();
                }
                a = 1;
            }
            else if(imageIndex2 == imageIndex5 && imageIndex5 == imageIndex7 && imageIndex7 == imageIndex11){
                i2.setRotation(rotation);
                i5.setRotation(rotation);
                i7.setRotation(rotation);
                i11.setRotation(rotation);
                x = 4;
                if(a == 0){
                    balance += spin_money * koef * level * x;
                    updateKoefAndLevel();
                }
                a = 1;
            }
            else if(imageIndex2 == imageIndex5 && imageIndex5 == imageIndex9 && imageIndex9 == imageIndex11){
                i2.setRotation(rotation);
                i5.setRotation(rotation);
                i9.setRotation(rotation);
                i11.setRotation(rotation);
                x = 4;
                if(a == 0){
                    balance += spin_money * koef * level * x;
                    updateKoefAndLevel();
                }
                a = 1;
            }
            else if(imageIndex2 == imageIndex6 && imageIndex6 == imageIndex9 && imageIndex9 == imageIndex11){
                i2.setRotation(rotation);
                i6.setRotation(rotation);
                i9.setRotation(rotation);
                i11.setRotation(rotation);
                x = 4;
                if(a == 0){
                    balance += spin_money * koef * level * x;
                    updateKoefAndLevel();
                }
                a = 1;
            }
            else if(imageIndex2 == imageIndex4 && imageIndex4 == imageIndex7 && imageIndex7 == imageIndex11){
                i2.setRotation(rotation);
                i4.setRotation(rotation);
                i7.setRotation(rotation);
                i11.setRotation(rotation);
                x = 4;
                if(a == 0){
                    balance += spin_money * koef * level * x;
                    updateKoefAndLevel();
                }
                a = 1;
            }
            else if(imageIndex2 == imageIndex5 && imageIndex5 == imageIndex8){
                i2.setRotation(rotation);
                i5.setRotation(rotation);
                i8.setRotation(rotation);
                x = 2;
                if(a == 0){
                    balance += spin_money * koef * level * x;
                    updateKoefAndLevel();
                }
                a = 1;
            }
            else if(imageIndex5 == imageIndex8 && imageIndex8 == imageIndex11){
                i5.setRotation(rotation);
                i8.setRotation(rotation);
                i11.setRotation(rotation);
                x = 2;
                if(a == 0){
                    balance += spin_money * koef * level * x;
                    updateKoefAndLevel();
                }
                a = 1;
            }
            else if(imageIndex2 == imageIndex4 && imageIndex4 == imageIndex7){
                i2.setRotation(rotation);
                i4.setRotation(rotation);
                i7.setRotation(rotation);
                x = 2;
                if(a == 0){
                    balance += spin_money * koef * level * x;
                    updateKoefAndLevel();
                }
                a = 1;
            }
            else if(imageIndex2 == imageIndex6 && imageIndex6 == imageIndex9){
                i2.setRotation(rotation);
                i6.setRotation(rotation);
                i9.setRotation(rotation);
                x = 2;
                if(a == 0){
                    balance += spin_money * koef * level * x;
                    updateKoefAndLevel();
                }
                a = 1;
            }
            else if(imageIndex2 == imageIndex5 && imageIndex5 == imageIndex7){
                i2.setRotation(rotation);
                i5.setRotation(rotation);
                i7.setRotation(rotation);
                x = 2;
                if(a == 0){
                    balance += spin_money * koef * level * x;
                    updateKoefAndLevel();
                }
                a = 1;
            }
            else if(imageIndex2 == imageIndex5 && imageIndex5 == imageIndex9){
                i2.setRotation(rotation);
                i5.setRotation(rotation);
                i9.setRotation(rotation);
                x = 2;
                if(a == 0){
                    balance += spin_money * koef * level * x;
                    updateKoefAndLevel();
                }
                a = 1;
            } 
            else if(imageIndex2 == imageIndex8 && imageIndex8 == imageIndex14){
                 i2.setRotation(rotation);
                 i8.setRotation(rotation);
                 i14.setRotation(rotation);
                 x = 2;
                 if(a == 0){
                     balance += spin_money * koef * level * x;
                     updateKoefAndLevel();
                 }
                 a = 1;
             }
             else if(imageIndex8 == imageIndex11 && imageIndex11 == imageIndex14){
                 i8.setRotation(rotation);
                 i11.setRotation(rotation);
                 i14.setRotation(rotation);
                 x = 2;
                 if(a == 0){
                     balance += spin_money * koef * level * x;
                     updateKoefAndLevel();
                 }
                 a = 1;
             }
            else {
                x = 0;
            }
        }


    }

    private void gameUpdater(){
        Handler h = new Handler();
        h.post(new Runnable() {
            @Override
            public void run() {
                if(running){
                    miliseconds++;
                    spinSlot();
                }
                if(rotate && !running){
                    rotation+=5;
                    if(rotation == 360){
                        rotation = 0;
                    }
                    rotateImages();
                }
                h.postDelayed(this, 10);
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void updateKoefAndLevel(){
        coin_value_text.setText(Float.toString(koef));
        coins_text.setText("Coins: " + (balance));
        level_text.setText("Level: " + (level));
        spin_text.setText("Spin " + (spin_money*koef*level));
    }

    private void save(){
        sp = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(SAVED_MONEY, balance);
        editor.putInt(SAVED_LEVEL, level);
        editor.putFloat(SAVED_KOEF, koef);
        editor.apply();
    }
    private void load(){
        sp = getPreferences(MODE_PRIVATE);
        koef = sp.getFloat(SAVED_KOEF, 0.2f);
        level = sp.getInt(SAVED_LEVEL, 1);
        balance = sp.getInt(SAVED_MONEY, 10000);
    }

    public void createDialogWindow(){
        new AlertDialog.Builder(this)
                .setTitle("Are you sure?")
                .setMessage("Do you realy want exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, (arg0, arg1) -> GameLoopActivity.super.onBackPressed()).create().show();
    }

    @Override
    public void onBackPressed() {
        save();
        createDialogWindow();
    }
}