package com.example.prm392_v1.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.prm392_v1.data.entity.DocumentTagCrossRef;

import java.util.List;

@Dao
public interface DocumentTagCrossRefDao {
    @Insert
    void insert(DocumentTagCrossRef crossRef);

    @Query("SELECT * FROM document_tag_crossref WHERE docId = :docId")
    List<DocumentTagCrossRef> getTagsByDocument(int docId);
}
