package com.sad.guess_number;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.text.Editable;
import android.view.View;
import android.widget.*;
import android.os.Bundle;
import android.app.AlertDialog;

import java.util.ArrayList;

public class MainActivity extends Activity implements GestureOverlayView.OnGesturePerformedListener {
    int count_tries = 0;
    TextView tvInfo;
    EditText etInput;
    Button bControl;
    int guess;
    boolean gameFinished;
    private ProgressBar progressBar;
    GestureLibrary gLib;
    GestureOverlayView gestures;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvInfo = findViewById(R.id.textView1);
        etInput = findViewById(R.id.editText1);
        bControl = findViewById(R.id.button1);
        guess = (int) (Math.random() * 100);
        gameFinished = false;
        count_tries = 0;
        progressBar = findViewById(R.id.game_progress);
        progressBar.setMax(10);
        gLib = GestureLibraries.fromRawResource(this, R.raw.gestures);
        if (!gLib.load()) {
            finish();
        }

        gestures = findViewById(R.id.gestureOverlay);
        gestures.addOnGesturePerformedListener(this);
    }

    public void onClick(View v) {

        handle_button();
    }

    void handle_button() {
        if (!gameFinished) {
            int inp;
            try {
                inp = Integer.parseInt(etInput.getText().toString());
            } catch (Exception e) {
                tvInfo.setText(getResources().getString(R.string.empty_entry));
                return;
            }
            if (inp > 100) {
                tvInfo.setText(getResources().getString(R.string.error));
                return;
            }
            if (inp > guess)
                tvInfo.setText(getResources().getString(R.string.ahead));
            if (inp < guess)
                tvInfo.setText(getResources().getString(R.string.behind));
            count_tries++;
            updateProgress();
            if (count_tries > 10) {
                showAlert();
            }
            if (inp == guess) {
                gameFinished = true;
                showAlert();
            }
        }
        etInput.setText("");
    }

    private void updateProgress() {
        progressBar.setProgress(count_tries);
    }

    private void showAlert() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        if (gameFinished) {
            alertDialogBuilder.setTitle(getText(R.string.congratulations));
            alertDialogBuilder.setMessage(String.format(getResources().getString(R.string.hit), count_tries));
        } else {
            alertDialogBuilder.setTitle(getText(R.string.regrets));
            alertDialogBuilder.setMessage(R.string.regret_message);
        }
        alertDialogBuilder.setPositiveButton(getText(R.string.exit), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        alertDialogBuilder.setNegativeButton(getText(R.string.play_more), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                guess = (int) (Math.random() * 100);
                bControl.setText(getResources().getString(R.string.input_value));
                tvInfo.setText(getResources().getString(R.string.try_to_guess));
                gameFinished = false;
                count_tries = 0;
                updateProgress();
            }
        });
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.create().show();
    }

    public int getGuessNumber() {
        return guess;
    }



    @Override
    public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
        ArrayList<Prediction> predictions = gLib.recognize(gesture);
        if (predictions.size() > 0) {
            Prediction prediction = predictions.get(0);
            if (prediction.score > 1.0) {
                if (prediction.name.equals("one"))
                    etInput.setText(etInput.getText() + "1");
                if (prediction.name.equals("two"))
                    etInput.setText(etInput.getText() + "2");
                if (prediction.name.equals("three"))
                    etInput.setText(etInput.getText() + "3");
                if (prediction.name.equals("four"))
                    etInput.setText(etInput.getText() + "4");
                if (prediction.name.equals("five"))
                    etInput.setText(etInput.getText() + "5");
                if (prediction.name.equals("six"))
                    etInput.setText(etInput.getText() + "6");
                if (prediction.name.equals("seven"))
                    etInput.setText(etInput.getText() + "7");
                if (prediction.name.equals("eight"))
                    etInput.setText(etInput.getText() + "8");
                if (prediction.name.equals("nine"))
                    etInput.setText(etInput.getText() + "9");
                if (prediction.name.equals("zero"))
                    etInput.setText(etInput.getText() + "0");
                if (prediction.name.equals("stop"))
                    handle_button();
                if (prediction.name.equals("erase")) {
                    Editable editable = etInput.getText();
                    int length = editable.length();

                    if (length > 0) {
                        editable.delete(length - 1, length);
                    }

                }

            }
        }
    }
}