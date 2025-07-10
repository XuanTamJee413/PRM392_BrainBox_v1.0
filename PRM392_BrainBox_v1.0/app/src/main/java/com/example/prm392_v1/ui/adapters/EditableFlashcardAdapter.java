package com.example.prm392_v1.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.prm392_v1.R;
import com.example.prm392_v1.data.model.Flashcard;
import java.util.List;
import android.widget.Button;

public class EditableFlashcardAdapter extends RecyclerView.Adapter<EditableFlashcardAdapter.ViewHolder> {

    private List<Flashcard> flashcardList;
    private OnDeleteClickListener listener;

    public interface OnDeleteClickListener {
        void onDeleteClick(Flashcard flashcard, int position);
    }

    public EditableFlashcardAdapter(List<Flashcard> flashcardList, OnDeleteClickListener listener) {
        this.flashcardList = flashcardList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_editable_flashcard, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(flashcardList.get(position));
    }

    @Override
    public int getItemCount() {
        return flashcardList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textQuestion;
        Button buttonDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textQuestion = itemView.findViewById(R.id.text_flashcard_question);
            buttonDelete = itemView.findViewById(R.id.button_delete_flashcard);

            buttonDelete.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (listener != null && pos != RecyclerView.NO_POSITION) {
                    listener.onDeleteClick(flashcardList.get(pos), pos);
                }
            });
        }

        void bind(Flashcard card) {
            textQuestion.setText(card.question);
        }
    }
}