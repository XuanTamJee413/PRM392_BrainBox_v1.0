package com.example.prm392_v1.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.prm392_v1.R;
import com.example.prm392_v1.data.model.Flashcard;
import java.util.List;

public class AddedFlashcardAdapter extends RecyclerView.Adapter<AddedFlashcardAdapter.ViewHolder> {

    private List<Flashcard> flashcardList;

    public AddedFlashcardAdapter(List<Flashcard> flashcardList) {
        this.flashcardList = flashcardList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_added_flashcard, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(flashcardList.get(position), position + 1);
    }

    @Override
    public int getItemCount() {
        return flashcardList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textCardNumber;
        TextView textCardQuestion;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textCardNumber = itemView.findViewById(R.id.text_card_number);
            textCardQuestion = itemView.findViewById(R.id.text_card_question);
        }

        void bind(Flashcard card, int number) {
            textCardNumber.setText(String.format("%d.", number));
            textCardQuestion.setText(card.question);
        }
    }
}