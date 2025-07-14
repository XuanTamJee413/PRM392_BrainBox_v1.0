package com.example.prm392_v1.ui.main;

import android.content.Context;
import android.graphics.Color;
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

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder> { // Renamed to TestAdapter if you prefer

    private final List<Flashcard> flashcardList;
    private final int[] userAnswers;
    private final Context context;
    private boolean showFeedback = false;

    public QuestionAdapter(Context context, List<Flashcard> flashcardList) {
        this.context = context;
        this.flashcardList = flashcardList;
        this.userAnswers = new int[flashcardList.size()]; // Initialize with 0s
    }

    // Call this method when the user submits the quiz
    public void setShowFeedback(boolean showFeedback) {
        this.showFeedback = showFeedback;
        notifyDataSetChanged(); // Refresh the RecyclerView to show feedback
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

        holder.textQuestionNumber.setText("Câu " + (position + 1) + ":");
        holder.textQuestion.setText(flashcard.question);

        holder.radioOptionA.setText("A. " + flashcard.option1);
        holder.radioOptionB.setText("B. " + flashcard.option2);
        holder.radioOptionC.setText("C. " + flashcard.option3);
        holder.radioOptionD.setText("D. " + flashcard.option4);

        // Clear previous selection to prevent incorrect states due to recycling views
        holder.radioGroupOptions.clearCheck();

        // Set user's previous answer if available
        if (userAnswers[position] != 0) {
            switch (userAnswers[position]) {
                case 1: holder.radioOptionA.setChecked(true); break;
                case 2: holder.radioOptionB.setChecked(true); break;
                case 3: holder.radioOptionC.setChecked(true); break;
                case 4: holder.radioOptionD.setChecked(true); break;
            }
        }

        // Listener for RadioGroup to record user's answer
        holder.radioGroupOptions.setOnCheckedChangeListener(null); // Clear listener to prevent infinite loop
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
            userAnswers[position] = selectedAnswer; // Store the selected answer
        });

        // Show feedback after submission
        if (showFeedback) {
            holder.textFeedback.setVisibility(View.VISIBLE);
            int correctAnswer = flashcard.answer;
            int userAnswer = userAnswers[position];

            // Reset all radio button colors to default text color first
            holder.radioOptionA.setTextColor(ContextCompat.getColor(context, android.R.color.tab_indicator_text));
            holder.radioOptionB.setTextColor(ContextCompat.getColor(context, android.R.color.tab_indicator_text));
            holder.radioOptionC.setTextColor(ContextCompat.getColor(context, android.R.color.tab_indicator_text));
            holder.radioOptionD.setTextColor(ContextCompat.getColor(context, android.R.color.tab_indicator_text));


            if (userAnswer == correctAnswer) {
                holder.textFeedback.setText("Đúng!");
                holder.textFeedback.setTextColor(ContextCompat.getColor(context, android.R.color.holo_green_dark));
                // Highlight correct option in green
                switch (correctAnswer) {
                    case 1: holder.radioOptionA.setTextColor(ContextCompat.getColor(context, android.R.color.holo_green_dark)); break;
                    case 2: holder.radioOptionB.setTextColor(ContextCompat.getColor(context, android.R.color.holo_green_dark)); break;
                    case 3: holder.radioOptionC.setTextColor(ContextCompat.getColor(context, android.R.color.holo_green_dark)); break;
                    case 4: holder.radioOptionD.setTextColor(ContextCompat.getColor(context, android.R.color.holo_green_dark)); break;
                }
            } else {
                String correctOptionText = getOptionText(flashcard, correctAnswer);
                holder.textFeedback.setText("Sai! Đáp án đúng: " + correctOptionText);
                holder.textFeedback.setTextColor(ContextCompat.getColor(context, android.R.color.holo_red_dark));

                // Highlight user's incorrect answer in red
                switch (userAnswer) {
                    case 1: holder.radioOptionA.setTextColor(ContextCompat.getColor(context, android.R.color.holo_red_dark)); break;
                    case 2: holder.radioOptionB.setTextColor(ContextCompat.getColor(context, android.R.color.holo_red_dark)); break;
                    case 3: holder.radioOptionC.setTextColor(ContextCompat.getColor(context, android.R.color.holo_red_dark)); break;
                    case 4: holder.radioOptionD.setTextColor(ContextCompat.getColor(context, android.R.color.holo_red_dark)); break;
                }
                // Highlight correct answer in green
                switch (correctAnswer) {
                    case 1: holder.radioOptionA.setTextColor(ContextCompat.getColor(context, android.R.color.holo_green_dark)); break;
                    case 2: holder.radioOptionB.setTextColor(ContextCompat.getColor(context, android.R.color.holo_green_dark)); break;
                    case 3: holder.radioOptionC.setTextColor(ContextCompat.getColor(context, android.R.color.holo_green_dark)); break;
                    case 4: holder.radioOptionD.setTextColor(ContextCompat.getColor(context, android.R.color.holo_green_dark)); break;
                }
            }
            // Disable radio group after submission
            for (int i = 0; i < holder.radioGroupOptions.getChildCount(); i++) {
                holder.radioGroupOptions.getChildAt(i).setEnabled(false);
            }
        } else {
            holder.textFeedback.setVisibility(View.GONE);
            // Re-enable radio group
            for (int i = 0; i < holder.radioGroupOptions.getChildCount(); i++) {
                holder.radioGroupOptions.getChildAt(i).setEnabled(true);
            }
            // Reset all radio button colors to default text color
            holder.radioOptionA.setTextColor(ContextCompat.getColor(context, android.R.color.tab_indicator_text));
            holder.radioOptionB.setTextColor(ContextCompat.getColor(context, android.R.color.tab_indicator_text));
            holder.radioOptionC.setTextColor(ContextCompat.getColor(context, android.R.color.tab_indicator_text));
            holder.radioOptionD.setTextColor(ContextCompat.getColor(context, android.R.color.tab_indicator_text));
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