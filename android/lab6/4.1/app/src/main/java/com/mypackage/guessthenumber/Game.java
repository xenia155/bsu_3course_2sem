package com.mypackage.guessthenumber;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;

public class Game extends AppCompatActivity {

    RandNum random_number;
    TextView tvInfo;
    EditText etInput;
    Button bControl;
    ImageButton bRestart;
    Context context;
    ProgressBar progressBar;
    int counter;
    ImageView img;
    Animation animationAlphaUp;
    Animation animationMoving;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        context = Game.this;
        progressBar = (ProgressBar) findViewById(R.id.progressBar2);
        tvInfo = (TextView) findViewById(R.id.textView);
        etInput = (EditText) findViewById(R.id.editTextNumberSigned);
        bControl = (Button) findViewById(R.id.button);
        bRestart = (ImageButton) findViewById(R.id.button2);
        img = (ImageView) findViewById(R.id.imageView);
        bControl.setBackgroundColor(getResources().getColor(R.color.red));
        bRestart.setVisibility(View.INVISIBLE);
        bRestart.setEnabled(false);
        random_number = new RandNum(1, 200);
        this.counter = 0;
//        animationAlphaUp = AnimationUtils.loadAnimation(this, R.anim.alpha_restart);
//        animationMoving = AnimationUtils.loadAnimation(this, R.anim.transform_animation);
    }

    public void onClickRestart(View v) {
        random_number.generateRandNum();
        this.counter = 0;
        this.progressBar.setProgress(this.counter);
        tvInfo.setText(getResources().getString(R.string.try_to_guess));
        bRestart.clearAnimation();
        img.clearAnimation();
        bRestart.setVisibility(View.INVISIBLE);
        bRestart.setEnabled(false);
    }


    public void lose() {
        counter += 1;
        progressBar.setProgress(this.counter);
        if (counter == this.progressBar.getMax()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(R.string.lose);
            builder.setMessage(R.string.tooooMuch);
            AlertDialog alert = builder.create();
            alert.show();
            bRestart.setVisibility(View.VISIBLE);
            bRestart.clearAnimation();
            bRestart.startAnimation(animationAlphaUp);
            bRestart.setEnabled(true);
            saveAttemptsCount();
        }
    }

    public void onClick(View v) {
        try {
            int guess = Integer.parseInt(etInput.getText().toString());

            if (guess > random_number.getTop() || guess < random_number.getBottom())
                throw new NumberFormatException("Out of boundary");
            if (guess < random_number.getNumber()) {
                lose();
                tvInfo.setText(getResources().getString(R.string.behind));
            } else if (guess > random_number.getNumber()) {
                lose();
                tvInfo.setText(getResources().getString(R.string.ahead));
            } else {
                this.counter += 1;
                progressBar.setProgress(this.counter);
                saveBest_result(this.counter);
                tvInfo.setText(getResources().getString(R.string.hit));

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(R.string.win);
                builder.setMessage(R.string.congrats);
                AlertDialog alert = builder.create();
                bRestart.setVisibility(View.VISIBLE);
                bRestart.setEnabled(true);
                bRestart.clearAnimation();
                bRestart.startAnimation(animationAlphaUp);
                img.startAnimation(animationMoving);
                alert.show();
                saveAttemptsCount();
            }


        } catch (NumberFormatException ex) {
            if (ex.getMessage() == "Out of boundary") {
                tvInfo.setText(getResources().getString(R.string.invalid_range));
            } else
                tvInfo.setText(getResources().getString(R.string.error));
        }
    }

    public void saveBest_result(int counter){
        SharedPreferences sharedPreferences = context.getSharedPreferences("AppDetails", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        int prev_result = sharedPreferences.getInt("BestResult", -1);
        if (counter < prev_result || prev_result == -1)
        {
            editor.putInt("BestResult", counter);
            editor.commit();
        }

    }

    public void saveAttemptsCount(){
        SharedPreferences sharedPreferences = context.getSharedPreferences("AppDetails", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        int count = sharedPreferences.getInt("AttemptsCount", 0);
        editor.putInt("AttemptsCount", count + 1);
        editor.commit();
    }

    public void getInfo(View v){
        SharedPreferences sharedPreferences = context.getSharedPreferences("AppDetails", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String uuid = sharedPreferences.getString("UUID", null);
        int attempts = sharedPreferences.getInt("AttemptsCount", 0);
        int best_result = sharedPreferences.getInt("BestResult", -1);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.info);
        StringBuilder str = new StringBuilder();
        if (uuid != null)
            str.append("UUID: " + uuid + "\n");
        str.append("Total attempts: " + attempts + "\n");
        if (best_result != -1)
            str.append("Best Result: " + best_result);
        else
            str.append("Best Result: no any result saved" );
        builder.setMessage(str.toString());
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void logOut(View v){
        SharedPreferences sharedPreferences = context.getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Email", null);
        editor.putString("Password", null);
        editor.commit();
        Intent intent;
        intent = new Intent(Game.this,LoginActivity.class);
        startActivity(intent);
    }
}