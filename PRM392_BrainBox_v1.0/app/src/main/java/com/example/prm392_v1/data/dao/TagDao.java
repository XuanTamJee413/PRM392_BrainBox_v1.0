package com.example.prm392_v1.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.prm392_v1.data.entity.DocumentTagCrossRef;
import com.example.prm392_v1.data.entity.Tag;

import java.util.List;

@Dao
public interface TagDao {
    @Insert
    void insert(Tag tag);
    @Query("SELECT * FROM tags")
    List<Tag> getAll();

    @Insert void insertCrossRef(DocumentTagCrossRef cross);
    @Query("SELECT tagId FROM document_tag_crossref WHERE docId = :docId")
    List<Integer> getTagsForDocument(int docId);
}
