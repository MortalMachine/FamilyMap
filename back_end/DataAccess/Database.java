package DataAccess;

import java.sql.*;
import java.util.*;

public class Database
{
    static
    {
        try
        {
            final String driver = "org.sqlite.JDBC";
            Class.forName(driver);
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        bootup();
    }

    private Connection conn;

    private static void bootup()
    {
        try
        {
            Database db = new Database();
            db.openConnection();
            db.createTables();
            db.closeConnection(true);
        }
        catch (SQLException e)
        {
            System.out.println(e.getLocalizedMessage());
        }
    }

    public void openConnection() throws SQLException
    {
        try
        {
            final String CONNECTION_URL = "jdbc:sqlite:familymap.sqlite";

            conn = DriverManager.getConnection(CONNECTION_URL);
            conn.setAutoCommit(false);
        }
        catch (SQLException e)
        {
            throw new SQLException("openConnection failed", e);
        }
    }

    public void closeConnection(boolean commit) throws SQLException
    {
        try
        {
            if (commit)
            {
                conn.commit();
            }
            else
            {
                conn.rollback();
            }

            conn.close();
            conn = null;
        }
        catch (SQLException e)
        {
            throw new SQLException("closeConnection failed", e);
        }
    }

    public void createTables() throws SQLException
    {
        try
        {
            Statement stmt = null;
            try
            {
                stmt = conn.createStatement();
                //stmt.executeUpdate("drop table if exists person");
                //stmt.executeUpdate("drop table if exists user");
                //stmt.executeUpdate("drop table if exists event");
                //stmt.executeUpdate("drop table if exists auth_token");
                stmt.executeUpdate("create table person " +
                        "( id integer not null primary key autoincrement,\n" +
                        "        person_id varchar(255) not null,\n" +
                        "        descendant varchar(255) not null,\n" +
                        "        first_name varchar(255) not null,\n" +
                        "        last_name varchar(255) not null,\n" +
                        "        gender varchar(128) not null,\n" +
                        "        father varchar(255) null,\n" +
                        "        mother varchar(255) null,\n" +
                        "        spouse varchar(255) null );");
                stmt.executeUpdate("create table user " +
                        "( id integer not null primary key autoincrement,\n" +
                        "        user_name varchar(255) not null,\n" +
                        "        password varchar(255) not null,\n" +
                        "        email_address varchar(255) not null,\n" +
                        "        first_name varchar(255) not null,\n" +
                        "        last_name varchar(255) not null,\n" +
                        "        gender varchar(128) not null,\n" +
                        "        person_id varchar(255) not null );");
                stmt.executeUpdate("create table event " +
                        "( id integer not null primary key autoincrement,\n" +
                        "        event_id varchar(255) not null,\n" +
                        "        descendant varchar(255) not null,\n" +
                        "        person_id varchar(255) not null,\n" +
                        "        latitude integer,\n" +
                        "        longitude integer,\n" +
                        "        country varchar(255) not null,\n" +
                        "        city varchar(255) not null,\n" +
                        "        event_type varchar(255) not null,\n" +
                        "        year integer );");
                stmt.executeUpdate("create table auth_token " +
                        "( id integer not null primary key autoincrement,\n" +
                        "        user_name varchar(255) not null,\n" +
                        "        auth_token varchar(255) not null );");
            }
            finally
            {
                if (stmt != null)
                {
                    stmt.close();
                }
            }
        }
        catch (SQLException e)
        {
            throw new SQLException(e.getMessage());
        }
    }
    public Connection getConnection()
    {
        return conn;
    }
}
