package com.example.prm392_v1.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.example.prm392_v1.R;
import com.example.prm392_v1.data.model.Quiz;

public class ManageQuizAdapter extends ListAdapter<Quiz, ManageQuizAdapter.QuizViewHolder> {

    private OnButtonClickListener listener;

    public interface OnButtonClickListener {
        void onEditClick(Quiz quiz);
        void onDeleteClick(Quiz quiz);
    }

    public void setOnButtonClickListener(OnButtonClickListener listener) {
        this.listener = listener;
    }

    public ManageQuizAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Quiz> DIFF_CALLBACK = new DiffUtil.ItemCallback<Quiz>() {
        @Override
        public boolean areItemsTheSame(@NonNull Quiz oldItem, @NonNull Quiz newItem) {
            return oldItem.quizId == newItem.quizId;
        }
        @Override
        public boolean areContentsTheSame(@NonNull Quiz oldItem, @NonNull Quiz newItem) {
            return oldItem.quizName.equals(newItem.quizName);
        }
    };

    @NonNull
    @Override
    public QuizViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_manage_quiz, parent, false);
        return new QuizViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull QuizViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    class QuizViewHolder extends RecyclerView.ViewHolder {
        TextView textName, textDescription;
        Button buttonEdit, buttonDelete;

        public QuizViewHolder(@NonNull View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.text_quiz_name);
            textDescription = itemView.findViewById(R.id.text_quiz_description);
            buttonEdit = itemView.findViewById(R.id.button_edit);
            buttonDelete = itemView.findViewById(R.id.button_delete);

            buttonEdit.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (listener != null && pos != RecyclerView.NO_POSITION) {
                    listener.onEditClick(getItem(pos));
                }
            });

            buttonDelete.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (listener != null && pos != RecyclerView.NO_POSITION) {
                    listener.onDeleteClick(getItem(pos));
                }
            });
        }

        void bind(Quiz quiz) {
            textName.setText(quiz.quizName);
            textDescription.setText(quiz.description);
        }
    }
}