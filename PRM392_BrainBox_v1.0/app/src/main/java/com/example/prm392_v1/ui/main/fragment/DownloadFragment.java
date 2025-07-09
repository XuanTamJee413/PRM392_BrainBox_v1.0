package com.example.prm392_v1.ui.main.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.prm392_v1.R;
import com.example.prm392_v1.data.BrainBoxDatabase;
import com.example.prm392_v1.data.entity.Document;
import com.example.prm392_v1.data.entity.Quiz;
import com.example.prm392_v1.data.model.DocumentDto;
import com.example.prm392_v1.data.model.UserDto;
import com.example.prm392_v1.ui.adapters.DocumentAdapter;
import com.example.prm392_v1.ui.adapters.QuizAdapter;

import java.util.ArrayList;
import java.util.List;

public class DownloadFragment extends Fragment {
    private RecyclerView recyclerDocuments, recyclerQuizzes;
    private DocumentAdapter documentAdapter;
    private QuizAdapter quizAdapter;
    public DownloadFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_download, container, false);

        recyclerDocuments = view.findViewById(R.id.recycler_downloaded_documents);
        recyclerQuizzes = view.findViewById(R.id.recycler_downloaded_quizzes);

        recyclerDocuments.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerQuizzes.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        documentAdapter = new DocumentAdapter();
        quizAdapter = new QuizAdapter();

        recyclerDocuments.setAdapter(documentAdapter);
        recyclerQuizzes.setAdapter(quizAdapter);

        loadDownloadedDocuments(view);
        loadDownloadedQuizzes(view);

        return view;
    }

    private void loadDownloadedDocuments(View view) {
        new Thread(() -> {
            List<Document> docs = BrainBoxDatabase.getInstance(requireContext()).documentDao().getAllPublic();
            List<DocumentDto> dtos = new ArrayList<>();

            for (Document d : docs) {
                DocumentDto dto = new DocumentDto();
                dto.DocId = d.docId;
                dto.Title = d.title;
                dto.Views = d.views;

                DocumentDto.Author author = new DocumentDto.Author();
                author.Username = "truy cập online để xem author";
                dto.Author = author;

                dtos.add(dto);
            }

            requireActivity().runOnUiThread(() -> {
                if (dtos.isEmpty()) {
                    recyclerDocuments.setVisibility(View.GONE);
                    view.findViewById(R.id.text_no_downloaded_documents).setVisibility(View.VISIBLE);
                } else {
                    recyclerDocuments.setVisibility(View.VISIBLE);
                    view.findViewById(R.id.text_no_downloaded_documents).setVisibility(View.GONE);
                    documentAdapter.submitList(dtos);
                }
            });
        }).start();
    }

    private void loadDownloadedQuizzes(View view) {
        BrainBoxDatabase.getInstance(requireContext())
                .quizDao()
                .getAllAsLiveData()
                .observe(getViewLifecycleOwner(), new Observer<List<com.example.prm392_v1.data.entity.Quiz>>() {
                    @Override
                    public void onChanged(List<com.example.prm392_v1.data.entity.Quiz> quizEntities) {
                        if (quizEntities == null || quizEntities.isEmpty()) {
                            recyclerQuizzes.setVisibility(View.GONE);
                            view.findViewById(R.id.text_no_downloaded_quizzes).setVisibility(View.VISIBLE);
                        } else {
                            recyclerQuizzes.setVisibility(View.VISIBLE);
                            view.findViewById(R.id.text_no_downloaded_quizzes).setVisibility(View.GONE);

                            // Parse
                            List<com.example.prm392_v1.data.model.Quiz> quizModels = new ArrayList<>();
                            for (com.example.prm392_v1.data.entity.Quiz q : quizEntities) {
                                com.example.prm392_v1.data.model.Quiz quiz = new com.example.prm392_v1.data.model.Quiz();
                                quiz.quizId = q.quizId;
                                quiz.quizName = q.quizName;
                                quiz.description = q.description;
                                quizModels.add(quiz);
                            }

                            quizAdapter.submitList(quizModels);
                        }
                    }
                });
    }
}