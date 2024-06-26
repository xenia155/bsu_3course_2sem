package com.overwees.multithreadexample2;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;


public class ProgressFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_progress, container, false);

        ProgressBar indicatorBar = (ProgressBar) view.findViewById(R.id.indicator);
        TextView statusView = (TextView) view.findViewById(R.id.statusView);
        Button btnFetch = (Button)view.findViewById(R.id.progressBtn);

        MyViewModel model = new ViewModelProvider(requireActivity()).get(MyViewModel.class);

        model.getValue().observe(getViewLifecycleOwner(), value -> {
            indicatorBar.setProgress(value);
            statusView.setText("Статус: " + value);
        });
        btnFetch.setOnClickListener(v -> model.execute());
        return view;
    }
}