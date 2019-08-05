package com.fb.pearsapplication.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.fb.pearsapplication.R;
import com.fb.pearsapplication.models.Question;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class ChildQuestionFragment extends Fragment {

    public TextView tvQuestion;
    public EditText etAnswer;
    public Button btnSubmit;
    Question dailyQuestion;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_child_question, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvQuestion = view.findViewById(R.id.tvQuestion);
        etAnswer = view.findViewById(R.id.etAnswer);
        btnSubmit = view.findViewById(R.id.btnSubmit);
    }

    public void setDailyQuestion(Question question) {
        this.dailyQuestion = question;
    }
}
