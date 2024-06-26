package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.Random;

public class GameActivity extends AppCompatActivity {

    SharedPreferences pref;
    int bestResult;

    TextView tvInfo;
    TextView recordInfo;
    EditText etInput;
    Button bControl;
    Button bPlayMore;

    int need_num;
    boolean game_over;
    int tries_amount;

    Dialog dial;

    Animation animAlpha;
    Animation animScale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_activity);

        tvInfo = findViewById(R.id.try_to_guess);
        recordInfo = findViewById(R.id.score);
        etInput = findViewById(R.id.editTextNumber);
        bControl = findViewById(R.id.button1);
        bPlayMore = findViewById(R.id.button2);

        pref = getSharedPreferences("user_details", MODE_PRIVATE);
        int record_num = pref.getInt("record_num", 4);
        bestResult = record_num;

        tvInfo.setText(getResources().getString(R.string.try_to_guess));
        recordInfo.setText(("Best res: " + record_num + " tries"));
        bControl.setText(getResources().getString(R.string.input_value));

        Random rand = new Random();
        need_num = rand.nextInt(200) + 1;
        game_over = false;
        tries_amount = 0;
        tries_amount = pref.getInt("tries_amount", 0);
        recordInfo.setText("Best res: " + record_num + " tries");
        dial = new Dialog(GameActivity.this);

        animAlpha = AnimationUtils.loadAnimation(this, R.anim.flicker);
        animScale = AnimationUtils.loadAnimation(this, R.anim.increase);

        Animation a = AnimationUtils.loadAnimation(this, R.anim.increase);
        a.reset();
        tvInfo.clearAnimation();
        tvInfo.startAnimation(a);
    }

    public void onClick(View v) {
        tries_amount++;
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("tries_amount", tries_amount);
        editor.apply();
        String try_str = "Try №" + tries_amount + ": ";

        if (etInput.getText().toString().equals("")) {
            tvInfo.setText((try_str + getResources().getString(R.string.error_empty_str)));

            dial.setTitle("Incorrect input");
            dial.setContentView(R.layout.dialog);
            TextView text = dial.findViewById(R.id.DialogView);
            text.setText((try_str + getResources().getString(R.string.error_empty_str)));
            dial.show();

            return;
        }

        int guess_num = Integer.parseInt(etInput.getText().toString());

        if (guess_num > 200) {
            tvInfo.setText((try_str + getResources().getString(R.string.error_too_big)));

            dial.setTitle("Incorrect input");
            dial.setContentView(R.layout.dialog);
            TextView text = dial.findViewById(R.id.DialogView);
            text.setText((try_str + getResources().getString(R.string.error_too_big)));
            dial.show();

            etInput.setText("");
            return;
        }

        if (guess_num < 1) {
            tvInfo.setText((try_str + getResources().getString(R.string.error_too_small)));

            dial.setTitle("Incorrect input");
            dial.setContentView(R.layout.dialog);
            TextView text = dial.findViewById(R.id.DialogView);
            text.setText((try_str + getResources().getString(R.string.error_too_small)));
            dial.show();

            etInput.setText("");
            return;
        }

        if (guess_num < need_num) {
            tvInfo.setText((try_str + getResources().getString(R.string.behind)));
            etInput.setText("");
        } else if (guess_num > need_num) {
            tvInfo.setText((try_str + getResources().getString(R.string.ahead)));
            etInput.setText("");
        } else {
            tvInfo.setText((try_str + getResources().getString(R.string.hit)));

            dial.setTitle("You won!");
            dial.setContentView(R.layout.dialog);
            TextView text = dial.findViewById(R.id.DialogView);
            text.setText((getResources().getString(R.string.game_over) + "\nYour res " + tries_amount + " попыток."));
            dial.show();

            Animation a = AnimationUtils.loadAnimation(this, R.anim.increase);
            a.reset();
            bPlayMore.clearAnimation();
            bPlayMore.startAnimation(a);

            game_over = true;

            if (tries_amount < bestResult || bestResult == 0) {
                bestResult = tries_amount;
                SharedPreferences.Editor bestResultEditor = pref.edit();
                bestResultEditor.putInt("record_num", bestResult);
                bestResultEditor.apply();
            }
        }

        Animation a = AnimationUtils.loadAnimation(this, R.anim.flicker);
        a.reset();
        tvInfo.clearAnimation();
        tvInfo.startAnimation(a);
    }

    public void playMore(View v) {
        tvInfo.setText(getResources().getString(R.string.try_to_guess));
        bControl.setText(getResources().getString(R.string.input_value));
        etInput.setText("");
        recordInfo.setText(("Best res: " + bestResult + " tries"));

        Random rand = new Random();
        need_num = rand.nextInt(200) + 1;
        game_over = false;
        tries_amount = 0;
        SharedPreferences.Editor editor = pref.edit();
        editor.remove("tries_amount");
        editor.apply();
        bPlayMore.clearAnimation();

        Animation a = AnimationUtils.loadAnimation(this, R.anim.increase);
        a.reset();
        tvInfo.clearAnimation();
        tvInfo.startAnimation(a);
    }
}