package main.java.com.traderbobsemporium.dao.Loggers;

import main.java.com.traderbobsemporium.dao.DAO;
import main.java.com.traderbobsemporium.model.Logging.Announcement;
import main.java.com.traderbobsemporium.util.DatabaseUtil;
import main.java.com.traderbobsemporium.util.LoggingUtil;
import sun.dc.pr.PRError;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */
public class AnnouncementLogger extends DAO<Announcement> {

    public AnnouncementLogger() {
        super("announcement");
    }

    @Override
    public void updateAll(Announcement announcement, String[] params) throws SQLException {
        if (!params[0].isEmpty())
            announcement.setName(params[0]);
        if (!params[1].isEmpty())
            announcement.setTitle(params[1]);
        if (!params[2].isEmpty())
            announcement.setDialog(params[2]);
        if (!params[3].isEmpty())
            announcement.setDateTime(params[3]);
        update(announcement);
    }

    @Override
    public void update(Announcement updated) throws SQLException {
        try (Connection connection = DatabaseUtil.DATA_SOURCE.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("UPDATE announcement SET " +
                     "username = ?, title = ?, dialog = ?, date = ? WHERE id = ?")) {
            preparedStatement.setString(1, updated.getName());
            preparedStatement.setString(2, updated.getTitle());
            preparedStatement.setString(3, updated.getDialog());
            preparedStatement.setString(4, updated.getDateTime());
            preparedStatement.setInt(5, updated.getId());
            preparedStatement.execute();

        }
    }

    @Override
    public void add(Announcement announcement) throws SQLException {
        try (Connection connection = DatabaseUtil.DATA_SOURCE.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO announcement(id," +
                             "username, title, dialog, date) VALUES (?, ?, ?, ?, ?)")) {
            preparedStatement.setInt(1, announcement.getId());
            preparedStatement.setString(2, announcement.getName());
            preparedStatement.setString(3, announcement.getTitle());
            preparedStatement.setString(4, announcement.getDialog());
            preparedStatement.setString(5, announcement.getDateTime());
            preparedStatement.execute();
        }

    }
}
