package ru.itmo.db.dao;

import ru.itmo.db.base.ConnectionSettings;
import ru.itmo.db.base.Dao;
import ru.itmo.db.base.ValidatorBase;
import ru.itmo.db.orm_classses.Alpinist;
import ru.itmo.db.orm_classses.Mountain;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MountainDao implements Dao<Mountain, Integer> {

    private ValidatorBase<Mountain> validator;

    private final String db_table = "tb_mountains";


    public MountainDao(){
        validator = new ValidatorBase<>();
    }

    @Override
    public void createTable() {
        String create = "CREATE TABLE IF NOT EXISTS "+ db_table + " (" +
                "id SERIAL PRIMARY KEY," +
                "name VARCHAR(100) NOT NULL," +
                "country VARCHAR(200) NOT NULL," +
                "height INTEGER NOT NULL" +
                ");";

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        try (Connection connection = DriverManager.getConnection(
                ConnectionSettings.CONNECTION_STR,
                ConnectionSettings.LOGIN,
                ConnectionSettings.PASSWORD
        )){
            try (Statement statement = connection.createStatement()){
                statement.executeUpdate(create);
                System.out.println("Таблица "+ db_table +" создана или уже существует");
            }
        } catch (SQLException e) {
            System.out.println("Таблица "+ db_table +" не была создана " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void add(Mountain mountain) {
        if (!validator.validate(mountain)) {
            System.out.println("Данные в таблицу " + db_table + " не добавлены. Данные не прошли валидацию");
            return;
        }

        String insert = "INSERT INTO " + db_table +" (name, country, height) VALUES (?, ?, ?) RETURNING id";

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("org.postgresql.Driver не был загружен");
            throw new RuntimeException(e);
        }

        try (Connection connection = DriverManager.getConnection(
                ConnectionSettings.CONNECTION_STR,
                ConnectionSettings.LOGIN,
                ConnectionSettings.PASSWORD
        )){
            try (PreparedStatement statement = connection.prepareStatement(insert)){

                statement.setString(1, mountain.getName());
                statement.setString(2, mountain.getCountry());
                statement.setInt(3, mountain.getHeight());
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    System.out.println("Данные были добавлены, идентификатор горы " + id);
                    mountain.setId(id);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Mountain mountain) {
        if (!validator.validate(mountain)) {
            System.out.println("Данные горы не были изменены. Данные не прошли валидацию");
            return;
        }

        String update = "UPDATE "+ db_table +" SET name = ?, country = ?, height = ? WHERE id = ? RETURNING id";

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        try (Connection connection = DriverManager.getConnection(
                ConnectionSettings.CONNECTION_STR,
                ConnectionSettings.LOGIN,
                ConnectionSettings.PASSWORD
        )) {
            try (PreparedStatement statement = connection.prepareStatement(update)) {
                statement.setString(1, mountain.getName());
                statement.setString(2, mountain.getCountry());
                statement.setInt(3, mountain.getHeight());
                statement.setInt(4, mountain.getId());
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    System.out.println("Данные для горы с id = "+ id + " изменены");
                    mountain.setId(id);
                }
            }
        } catch (SQLException e) {
            System.out.println("Данные горы c id = " + mountain.getId() +
                    " в таблице "+ db_table +" не обновлены " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public Mountain delete(Integer integer) {
        String delete = "DELETE FROM "+ db_table +" WHERE id = ? RETURNING id";

        Mountain mountain = get(integer);
        if (mountain == null) {
            System.out.println("Гора с id = " + integer + " не найдена. Удаление невозможно");
            return null;
        }

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("org.postgresql.Driver не был загружен");
            throw new RuntimeException(e);
        }

        try (Connection connection = DriverManager.getConnection(
                ConnectionSettings.CONNECTION_STR,
                ConnectionSettings.LOGIN,
                ConnectionSettings.PASSWORD
        )){
            try (PreparedStatement statement = connection.prepareStatement(delete)){
                statement.setInt(1, integer);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    System.out.println("Гора c id = " + integer + " удалена из таблицы");
                    return mountain;
                }
            }
        } catch (SQLException e) {
            System.out.println("Данные не были удалены из таблицы " + e.getMessage());
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public Mountain get(Integer integer) {
        String select = "SELECT name, country, height FROM " + db_table + " WHERE id = ?";

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        try (Connection connection = DriverManager.getConnection(
                ConnectionSettings.CONNECTION_STR,
                ConnectionSettings.LOGIN,
                ConnectionSettings.PASSWORD
        )) {
            try (PreparedStatement statement = connection.prepareStatement(select)) {
                statement.setInt(1, integer);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    String name = resultSet.getString("name");
                    String country = resultSet.getString("country");
                    int height = resultSet.getInt("height");
                    Mountain mountain = new Mountain(name, country, height);
                    mountain.setId(integer);
                    return mountain;
                }
            }
        } catch (SQLException e) {
            System.out.println("Данные не были получены из таблицы " + e.getMessage());
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public List<Mountain> getAll() {
        List<Mountain> mountains = new ArrayList<>();
        String select = "SELECT * FROM "+ db_table;

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        try (Connection connection = DriverManager.getConnection(
                ConnectionSettings.CONNECTION_STR,
                ConnectionSettings.LOGIN,
                ConnectionSettings.PASSWORD
        )) {
            try (Statement statement = connection.createStatement()) {
                ResultSet resultSet = statement.executeQuery(select);
                while (resultSet.next()) {
                    String name = resultSet.getString("name");
                    String country = resultSet.getString("country");
                    int height = resultSet.getInt("height");
                    int id = resultSet.getInt("id");
                    Mountain mountain = new Mountain(name, country, height);
                    mountain.setId(id);
                    mountains.add(mountain);
                }
                return mountains;
            }
        } catch (SQLException e) {
            System.out.println("Не удалось получить список гор для восхождения " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
