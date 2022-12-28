package ru.itmo.db.dao;

import ru.itmo.db.base.ConnectionSettings;
import ru.itmo.db.base.Dao;
import ru.itmo.db.base.ValidatorBase;
import ru.itmo.db.orm_classses.Alpinist;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AlpinistDao implements Dao<Alpinist, Integer> {

    private ValidatorBase<Alpinist> validator;

    private final String db_table = "tb_alpinists";


    public AlpinistDao(){
        validator = new ValidatorBase<>();
    }

    @Override
    public void createTable() {
        String create = "CREATE TABLE IF NOT EXISTS "+ db_table + " (" +
                "id SERIAL PRIMARY KEY," +
                "name VARCHAR(100) NOT NULL," +
                "address VARCHAR(200) NOT NULL," +
                "age INTEGER NOT NULL" +
                ");";

        System.out.println(create);

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
    public void add(Alpinist alpinist) {

        if (!validator.validate(alpinist)) {
            System.out.println("Альпинист не добавлен. Данные не прошли валидацию");
            return;
        }

        String insert = "INSERT INTO " + db_table +" (name, address, age) VALUES (?, ?, ?) RETURNING id";

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

                statement.setString(1, alpinist.getName());
                statement.setString(2, alpinist.getAddress());
                statement.setInt(3, alpinist.getAge());
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    System.out.println("Данные были добавлены, идентификатор альпиниста " + id);
                    alpinist.setId(id);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Alpinist alpinist) {
        if (!validator.validate(alpinist)) {
            System.out.println("Данные альпиниста не были изменены. Данные не прошли валидацию");
            return;
        }

        String update = "UPDATE "+ db_table +" SET name = ?, address = ?, age = ? WHERE id = ?";

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
                statement.setString(1, alpinist.getName());
                statement.setString(2, alpinist.getAddress());
                statement.setInt(3, alpinist.getAge());
                statement.setInt(4, alpinist.getId());
                int result = statement.executeUpdate();
                if (result == 1) {
                    System.out.println("Данные альпиниста c id = " + alpinist.getId() +
                            " в таблице "+ db_table +" обновлены");
                }
            }
        } catch (SQLException e) {
            System.out.println("Данные альпиниста c id = " + alpinist.getId() +
                    " в таблице "+ db_table +" не обновлены " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public Alpinist delete(Integer integer) {
        String delete = "DELETE FROM "+ db_table +" WHERE id = ?";

        Alpinist alpinist = get(integer);

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
                int result = statement.executeUpdate();
                if (result == 1) {
                    System.out.println("Альпинист c id = " + integer + " удален");
                    return alpinist;
                }
            }
        } catch (SQLException e) {
            System.out.println("Данные не были добавлены в таблицу " + e.getMessage());
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public Alpinist get(Integer integer) {
        String select = "SELECT name, address, age FROM " + db_table + " WHERE id = ?";

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
                    String address = resultSet.getString("address");
                    int age = resultSet.getInt("age");
                    Alpinist alpinist = new Alpinist(name, address, age);
                    alpinist.setId(integer);
                    return alpinist;
                }
            }
        } catch (SQLException e) {
            System.out.println("Данные не были получены из таблицы " + e.getMessage());
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public List<Alpinist> getAll() {
        List<Alpinist> alpinists = new ArrayList<>();
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
                    String address = resultSet.getString("address");
                    int age = resultSet.getInt("age");
                    int id = resultSet.getInt("id");
                    Alpinist alpinist = new Alpinist(name, address, age);
                    alpinist.setId(id);
                    alpinists.add(alpinist);
                }
                return alpinists;
            }
        } catch (SQLException e) {
            System.out.println("Не удалось получить список альпинистов " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
