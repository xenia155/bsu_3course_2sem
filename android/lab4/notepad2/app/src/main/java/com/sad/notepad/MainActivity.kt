package com.sad.notepad

import android.gesture.Gesture
import android.gesture.GestureLibraries
import android.gesture.GestureLibrary
import android.gesture.GestureOverlayView
import android.gesture.Prediction
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText

class MainActivity : AppCompatActivity(), GestureOverlayView.OnGesturePerformedListener {
    private var gLib: GestureLibrary? = null
    private var gestures: GestureOverlayView? = null
    private lateinit var etText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        etText = findViewById(R.id.etText)
        gLib = GestureLibraries.fromRawResource(this, R.raw.gesture)
        if (!gLib!!.load()) {
            finish()
        }
        gestures = findViewById(R.id.gov1)
        gestures!!.addOnGesturePerformedListener(this)
    }

    override fun onGesturePerformed(overlay: GestureOverlayView?, gesture: Gesture?) {
        val predictions: java.util.ArrayList<Prediction> = gLib!!.recognize(gesture)
        if (predictions.size > 0) {
            val prediction: Prediction = predictions[0]
            if (prediction.name.equals("delete")) {
                etText.setText(etText.text.toString().toCharArray(), 0, etText.length() - 1)
                return
            }
            if (prediction.name.equals("\\n")) {
                etText.append("\n")
                return
            }
            etText.append(prediction.name)
        }
    }
}