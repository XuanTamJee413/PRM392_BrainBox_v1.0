package com.example.prm392_v1.ui.adapters;

import android.util.Log;
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

    private static final String TAG = "QuizAdapter";
    private OnItemClickListener listener;
    private OnDownloadClickListener downloadClickListener;

    public interface OnItemClickListener {
        void onItemClick(Quiz quiz);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnDownloadClickListener {
        void onDownloadClick(Quiz quiz);
    }

    public void setOnDownloadClickListener(OnDownloadClickListener listener) {
        this.downloadClickListener = listener;
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
            return oldItem.quizName.equals(newItem.quizName) &&
                    oldItem.description.equals(newItem.description) &&
                    oldItem.isPublic == newItem.isPublic &&
                    (oldItem.flashcards != null ? oldItem.flashcards.size() : 0) ==
                            (newItem.flashcards != null ? newItem.flashcards.size() : 0) &&
                    oldItem.averageRating == newItem.averageRating &&
                    oldItem.totalRatings == newItem.totalRatings;
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
        private final TextView textViewQuestionCount;
        private final TextView textViewAverageRating;
        private final View btnDownload;

        public QuizViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.quiz_title_text);
            textViewDescription = itemView.findViewById(R.id.quiz_description_text);
            textViewQuestionCount = itemView.findViewById(R.id.quiz_question_count_text);
            textViewAverageRating = itemView.findViewById(R.id.text_average_rating);
            btnDownload = itemView.findViewById(R.id.download_button);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(getItem(position));
                }
            });
            btnDownload.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (downloadClickListener != null && position != RecyclerView.NO_POSITION) {
                    downloadClickListener.onDownloadClick(getItem(position));
                }
            });
        }

        public void bind(Quiz quiz) {
            textViewTitle.setText(quiz.quizName);
            textViewDescription.setText(quiz.description);

            int flashcardCount = (quiz.flashcards != null) ? quiz.flashcards.size() : 0;
            textViewQuestionCount.setText(flashcardCount + " Flashcards");

            if (quiz.totalRatings > 0) {
                textViewAverageRating.setText(String.format("Đánh giá: %.1f/5 (%d đánh giá)", quiz.averageRating, quiz.totalRatings));
            } else {
                textViewAverageRating.setText("Chưa có đánh giá");
            }
            Log.d(TAG, "Binding quizId: " + quiz.quizId + ", averageRating: " + quiz.averageRating + ", totalRatings: " + quiz.totalRatings);
        }
    }
}