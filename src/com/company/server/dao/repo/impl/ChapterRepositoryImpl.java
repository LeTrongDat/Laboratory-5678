package com.company.server.dao.repo.impl;

import com.company.server.dao.repo.ChapterRepository;
import com.company.shared.entity.Chapter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ChapterRepositoryImpl implements ChapterRepository {
    private Connection con;

    public ChapterRepositoryImpl(Connection con) {
        this.con = con;
    }

    @Override
    public int add(Chapter chapter) throws SQLException {
        String SQL = "INSERT INTO chapter(name, marines_count) VALUES(?, ?) RETURNING chapter_id";

        PreparedStatement pst = con.prepareStatement(SQL);

        pst.setString(1, chapter.getName());

        pst.setLong(2, chapter.getMarinesCount());

        ResultSet rs = pst.executeQuery();

        rs.next();

        return rs.getInt("chapter_id");
    }

    @Override
    public int update(Chapter chapter, int chapterId) throws SQLException {
        String SQL = "UPDATE chapter SET " +
                "name = ?, " +
                "marines_count = ? " +
                "WHERE chapter_id = ? RETURNING chapter_id";

        PreparedStatement pst = con.prepareStatement(SQL);

        pst.setString(1, chapter.getName());

        pst.setLong(2, chapter.getMarinesCount());

        pst.setInt(3, chapterId);

        ResultSet rs = pst.executeQuery();

        rs.next();

        return rs.getInt("chapter_id");
    }

    @Override
    public Chapter findById(int id) throws SQLException {
        String SQL = String.format("SELECT * FROM chapter WHERE chapter_id = %d", id);

        ResultSet rs = con.createStatement().executeQuery(SQL);

        rs.next();

        return new Chapter(rs.getString("name"), rs.getLong("marines_count"));
    }
}
