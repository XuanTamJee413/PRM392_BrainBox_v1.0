package com.example.prm392_v1.ui.adapters;

import android.content.Context;
import android.graphics.Color; // This import is not used, can be removed.
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_v1.R;
import com.example.prm392_v1.data.model.Flashcard;

import java.util.List;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder> {

    private final List<Flashcard> flashcardList;
    private final int[] userAnswers;
    private final Context context;
    private boolean showFeedback = false;

    public QuestionAdapter(Context context, List<Flashcard> flashcardList) {
        this.context = context;
        this.flashcardList = flashcardList;
        this.userAnswers = new int[flashcardList.size()];
    }

    public void setShowFeedback(boolean showFeedback) {
        this.showFeedback = showFeedback;
        notifyDataSetChanged();
    }

    public int[] getUserAnswers() {
        return userAnswers;
    }

    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_question, parent, false);
        return new QuestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionViewHolder holder, int position) {
        Flashcard flashcard = flashcardList.get(position);

        // Set question text
        holder.textQuestionNumber.setText("Câu " + (position + 1) + ":");
        holder.textQuestion.setText(flashcard.question);

        holder.radioOptionA.setText("A. " + flashcard.option1);
        holder.radioOptionB.setText("B. " + flashcard.option2);
        holder.radioOptionC.setText("C. " + flashcard.option3);
        holder.radioOptionD.setText("D. " + flashcard.option4);

        holder.radioGroupOptions.setOnCheckedChangeListener(null);
        holder.radioGroupOptions.clearCheck();

        holder.radioOptionA.setTextColor(ContextCompat.getColor(context, R.color.text_color_default));
        holder.radioOptionB.setTextColor(ContextCompat.getColor(context, R.color.text_color_default));
        holder.radioOptionC.setTextColor(ContextCompat.getColor(context, R.color.text_color_default));
        holder.radioOptionD.setTextColor(ContextCompat.getColor(context, R.color.text_color_default));

        holder.radioOptionA.setEnabled(true);
        holder.radioOptionB.setEnabled(true);
        holder.radioOptionC.setEnabled(true);
        holder.radioOptionD.setEnabled(true);
        holder.radioGroupOptions.setEnabled(true);

        if (userAnswers[position] != 0 && !showFeedback) {
            switch (userAnswers[position]) {
                case 1: holder.radioOptionA.setChecked(true); break;
                case 2: holder.radioOptionB.setChecked(true); break;
                case 3: holder.radioOptionC.setChecked(true); break;
                case 4: holder.radioOptionD.setChecked(true); break;
            }
        }

        if (!showFeedback) {
            holder.radioGroupOptions.setOnCheckedChangeListener((group, checkedId) -> {
                int selectedAnswer = 0;
                if (checkedId == R.id.radio_option_a) {
                    selectedAnswer = 1;
                } else if (checkedId == R.id.radio_option_b) {
                    selectedAnswer = 2;
                } else if (checkedId == R.id.radio_option_c) {
                    selectedAnswer = 3;
                } else if (checkedId == R.id.radio_option_d) {
                    selectedAnswer = 4;
                }
                userAnswers[position] = selectedAnswer;
                Log.d("QuestionAdapter", "Question " + (position + 1) + " selected answer: " + selectedAnswer);
            });
        }

        if (showFeedback) {
            holder.textFeedback.setVisibility(View.VISIBLE);
            int correctAnswer = flashcard.answer;
            int userAnswer = userAnswers[position];

            Log.d("QuestionAdapter", "Question " + (position + 1) + ": User answer = " + userAnswer + ", Correct answer = " + correctAnswer);

            holder.radioGroupOptions.setEnabled(false);
            for (int i = 0; i < holder.radioGroupOptions.getChildCount(); i++) {
                holder.radioGroupOptions.getChildAt(i).setEnabled(false);
            }

            if (userAnswer == 0) {
                holder.textFeedback.setText("Chưa trả lời! Đáp án đúng: " + getOptionText(flashcard, correctAnswer));
                holder.textFeedback.setTextColor(ContextCompat.getColor(context, android.R.color.holo_red_dark));
                highlightCorrectAnswer(holder, correctAnswer, android.R.color.holo_green_dark);
            } else if (userAnswer == correctAnswer) {
                holder.textFeedback.setText("Đúng!");
                holder.textFeedback.setTextColor(ContextCompat.getColor(context, android.R.color.holo_green_dark));
                highlightCorrectAnswer(holder, userAnswer, android.R.color.holo_green_dark);
            } else {
                String correctOptionText = getOptionText(flashcard, correctAnswer);
                holder.textFeedback.setText("Sai! Đáp án đúng: " + correctOptionText);
                holder.textFeedback.setTextColor(ContextCompat.getColor(context, android.R.color.holo_red_dark));

                highlightCorrectAnswer(holder, correctAnswer, android.R.color.holo_green_dark);
                highlightIncorrectAnswer(holder, userAnswer, android.R.color.holo_red_dark);
            }
        } else {
            holder.textFeedback.setVisibility(View.GONE);
            holder.textFeedback.setTextColor(ContextCompat.getColor(context, android.R.color.black)); // Or your default text color
        }
    }

    @Override
    public int getItemCount() {
        return flashcardList.size();
    }

    private String getOptionText(Flashcard card, int answer) {
        switch (answer) {
            case 1: return card.option1;
            case 2: return card.option2;
            case 3: return card.option3;
            case 4: return card.option4;
            default: return "N/A";
        }
    }

    private void highlightCorrectAnswer(QuestionViewHolder holder, int correctAnswer, int colorResId) {
        switch (correctAnswer) {
            case 1: holder.radioOptionA.setTextColor(ContextCompat.getColor(context, colorResId)); break;
            case 2: holder.radioOptionB.setTextColor(ContextCompat.getColor(context, colorResId)); break;
            case 3: holder.radioOptionC.setTextColor(ContextCompat.getColor(context, colorResId)); break;
            case 4: holder.radioOptionD.setTextColor(ContextCompat.getColor(context, colorResId)); break;
        }
    }

    private void highlightIncorrectAnswer(QuestionViewHolder holder, int userAnswer, int colorResId) {
        switch (userAnswer) {
            case 1: holder.radioOptionA.setTextColor(ContextCompat.getColor(context, colorResId)); break;
            case 2: holder.radioOptionB.setTextColor(ContextCompat.getColor(context, colorResId)); break;
            case 3: holder.radioOptionC.setTextColor(ContextCompat.getColor(context, colorResId)); break;
            case 4: holder.radioOptionD.setTextColor(ContextCompat.getColor(context, colorResId)); break;
        }
    }

    public static class QuestionViewHolder extends RecyclerView.ViewHolder {
        TextView textQuestionNumber, textQuestion, textFeedback;
        RadioGroup radioGroupOptions;
        RadioButton radioOptionA, radioOptionB, radioOptionC, radioOptionD;

        public QuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            textQuestionNumber = itemView.findViewById(R.id.text_item_question_number);
            textQuestion = itemView.findViewById(R.id.text_item_question);
            textFeedback = itemView.findViewById(R.id.text_item_feedback);
            radioGroupOptions = itemView.findViewById(R.id.radio_group_options);
            radioOptionA = itemView.findViewById(R.id.radio_option_a);
            radioOptionB = itemView.findViewById(R.id.radio_option_b);
            radioOptionC = itemView.findViewById(R.id.radio_option_c);
            radioOptionD = itemView.findViewById(R.id.radio_option_d);
        }
    }
}