package com.example.prm392_v1.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.prm392_v1.data.entity.DocumentDetail;

import java.util.List;

@Dao
public interface DocumentDetailDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)

    void insert(DocumentDetail detail);

    @Query("SELECT * FROM document_details WHERE docId = :docId")
    List<DocumentDetail> getByDocId(int docId);
    @Query("DELETE FROM document_details WHERE docId = :docId")
    void deleteByDocId(int docId);

}

