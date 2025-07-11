package com.example.prm392_v1.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_v1.R;
import com.example.prm392_v1.data.model.Quiz;

public class QuizAdapter extends ListAdapter<Quiz, QuizAdapter.QuizViewHolder> {

    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Quiz quiz);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public QuizAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Quiz> DIFF_CALLBACK = new DiffUtil.ItemCallback<Quiz>() {
        @Override
        public boolean areItemsTheSame(@NonNull Quiz oldItem, @NonNull Quiz newItem) {
            return oldItem.quizId == newItem.quizId;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Quiz oldItem, @NonNull Quiz newItem) {
            // Compare all relevant fields including the new rating fields
            return oldItem.quizName.equals(newItem.quizName) &&
                    oldItem.description.equals(newItem.description) &&
                    oldItem.isPublic == newItem.isPublic &&
                    // Check flashcards list size for content change (or a deeper comparison if needed)
                    (oldItem.flashcards != null ? oldItem.flashcards.size() : 0) ==
                            (newItem.flashcards != null ? newItem.flashcards.size() : 0) &&
                    oldItem.averageRating == newItem.averageRating && // NEW
                    oldItem.totalRatings == newItem.totalRatings;       // NEW
        }
    };

    @NonNull
    @Override
    public QuizViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_quiz, parent, false);
        return new QuizViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull QuizViewHolder holder, int position) {
        Quiz currentQuiz = getItem(position);
        holder.bind(currentQuiz);
    }

    class QuizViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewTitle;
        private final TextView textViewDescription;
        private final TextView textViewQuestionCount; // Renamed for clarity, matches item_quiz.xml
        private final TextView textViewAverageRating; // NEW

        public QuizViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.quiz_title_text);
            textViewDescription = itemView.findViewById(R.id.quiz_description_text);
            textViewQuestionCount = itemView.findViewById(R.id.quiz_question_count_text); // NEW ID
            textViewAverageRating = itemView.findViewById(R.id.text_average_rating);     // NEW ID

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(getItem(position));
                }
            });
        }

        public void bind(Quiz quiz) {
            textViewTitle.setText(quiz.quizName);
            textViewDescription.setText(quiz.description);

            // Display flashcard count
            int flashcardCount = (quiz.flashcards != null) ? quiz.flashcards.size() : 0;
            textViewQuestionCount.setText(flashcardCount + " Flashcards");

            // NEW: Display average rating
            if (quiz.totalRatings > 0) {
                // Format to one decimal place
                textViewAverageRating.setText(String.format("Đánh giá: %.1f/5 (%d đánh giá)", quiz.averageRating, quiz.totalRatings));
            } else {
                textViewAverageRating.setText("Chưa có đánh giá");
            }
        }
    }
}