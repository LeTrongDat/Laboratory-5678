package com.company.server.dao.repo;

import com.company.shared.entity.Chapter;

import java.sql.SQLException;

public interface ChapterRepository extends Repository {
    int add(Chapter chapter) throws SQLException;
    int update(Chapter chapter, int chapterId) throws SQLException;
    Chapter findById(int id) throws SQLException;
}
