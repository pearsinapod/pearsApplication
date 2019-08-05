package com.fb.pearsapplication.fragments;

import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.fb.pearsapplication.R;
import com.fb.pearsapplication.models.Question;
import com.fb.pearsapplication.models.UserQuestion;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class ChildQuestionFragment extends Fragment {

    public TextView tvQuestion;
    public EditText etAnswer;
    public Button btnSubmit;
    public TextView tvDate;
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
        tvDate = view.findViewById(R.id.tvDate);

        tvQuestion.setText(dailyQuestion.getQuestion());
        tvDate.setText(DateFormat.getDateInstance().format(dailyQuestion.getTargetDate()));

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserQuestion userQ = new UserQuestion();
                userQ.setUser(ParseUser.getCurrentUser());
                userQ.setAnswer(etAnswer.getText().toString());
                userQ.setQuestion(dailyQuestion);
                userQ.setDate(dailyQuestion.getTargetDate());

                userQ.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e != null) {
                            e.printStackTrace();
                        } else {
                            Log.d("XYZ", "created successfully!");
                            goToSubmittedFragment();
                        }
                    }
                });
            }
        });
    }
    private void goToSubmittedFragment() {
        FragmentManager fragmentManager = getFragmentManager();
        Fragment fragment = new ChildSubmittedFragment();
        fragmentManager.beginTransaction().replace(R.id.child_fragment_container, fragment).addToBackStack(null).commit();
    }

    public void setDailyQuestion(Question question) {
        this.dailyQuestion = question;
    }
}
