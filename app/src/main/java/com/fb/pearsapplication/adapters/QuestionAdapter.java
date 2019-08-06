package com.fb.pearsapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fb.pearsapplication.R;
import com.fb.pearsapplication.models.UserQuestion;

import java.text.DateFormat;
import java.util.List;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.ViewHolder> {

    private List<UserQuestion> UQList;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View UQView =inflater.inflate(R.layout.item_user_question, parent, false);
        ViewHolder viewHolder = new ViewHolder(UQView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserQuestion userQ = UQList.get(position);
        holder.tvDate.setText(DateFormat.getDateInstance().format(userQ.getQuestion().getTargetDate()));
        holder.tvQuestion.setText(userQ.getQuestion().getQuestion());
        holder.tvAnswer.setText(userQ.getAnswer());
    }

    @Override
    public int getItemCount() {
        return UQList.size();
    }

    public QuestionAdapter(List<UserQuestion> userQuestions) {
        UQList = userQuestions;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvDate;
        public TextView tvQuestion;
        public TextView tvAnswer;

        public ViewHolder(View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvQuestion = itemView.findViewById(R.id.tvQuestion);
            tvAnswer = itemView.findViewById(R.id.tvAnswer);
        }
    }
}
