package ru.itmo.db.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import ru.itmo.db.base.ConnectionSettings;
import ru.itmo.db.base.Dao;
import ru.itmo.db.base.ValidatorBase;
import ru.itmo.db.orm_classses.Group;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class GroupDao implements Dao<Group, Integer> {

    private final ValidatorBase<Group> validator;

    private final EntityManager manager;

    private final String db_table = "tb_groups";

    public GroupDao(EntityManager manager){
        validator = new ValidatorBase<>();
        this.manager = manager;
    }
    @Override
    public void createTable() {
        String create = "CREATE TABLE IF NOT EXISTS "+ db_table + " (" +
                "id SERIAL PRIMARY KEY," +
                "datetime NOT NULL," +
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
    public void add(Group group) {
        if (!validator.validate(group)) {
            System.out.println("Данные в таблицу " + db_table + " не добавлены. Данные не прошли валидацию");
            return;
        }
        manager.persist(group);
        manager.flush();
    }

    @Override
    public void update(Group group) {
        if (!validator.validate(group)) {
            System.out.println("Данные горы не были изменены. Данные не прошли валидацию");
            return;
        }
        manager.merge(group);
    }

    @Override
    public Group delete(Integer id) {
        Group group = get(id);
        manager.remove(group);
        return group;
    }

    @Override
    public Group get(Integer id) {
        return manager.find(Group.class, id);
    }

    @Override
    public List<Group> getAll() {
        TypedQuery<Group> getAllGroupsQuery = manager.createQuery("SELECT g FROM Group g", Group.class);
        return getAllGroupsQuery.getResultList();
    }
}
