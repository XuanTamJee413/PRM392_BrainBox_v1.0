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
import com.example.prm392_v1.data.model.Flashcard;
public class FlashcardListAdapter extends ListAdapter<Flashcard, FlashcardListAdapter.TermViewHolder> {

    public FlashcardListAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Flashcard> DIFF_CALLBACK = new DiffUtil.ItemCallback<Flashcard>() {
        @Override
        public boolean areItemsTheSame(@NonNull Flashcard oldItem, @NonNull Flashcard newItem) {
            return oldItem.cardId == newItem.cardId;
        }
        @Override
        public boolean areContentsTheSame(@NonNull Flashcard oldItem, @NonNull Flashcard newItem) {
            return oldItem.question.equals(newItem.question);
        }
    };

    @NonNull
    @Override
    public TermViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_flashcard_term, parent, false);
        return new TermViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TermViewHolder holder, int position) {
        Flashcard currentCard = getItem(position);
        holder.bind(currentCard);
    }

    class TermViewHolder extends RecyclerView.ViewHolder {
        private final TextView textQuestion;
        private final TextView textAnswer;

        public TermViewHolder(@NonNull View itemView) {
            super(itemView);
            textQuestion = itemView.findViewById(R.id.text_term_question);
            textAnswer = itemView.findViewById(R.id.text_term_answer);
        }

        public void bind(Flashcard card) {
            textQuestion.setText(card.question);

            String[] options = {card.option1, card.option2, card.option3, card.option4};
            String correctAnswerText = "N/A";
            if (card.answer >= 1 && card.answer <= 4) {
                correctAnswerText = options[card.answer - 1];
            }
            textAnswer.setText(correctAnswerText);
        }
    }
}