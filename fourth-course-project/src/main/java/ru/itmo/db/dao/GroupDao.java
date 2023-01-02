package ru.itmo.db.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import ru.itmo.db.base.Dao;
import ru.itmo.db.base.ValidatorBase;
import ru.itmo.db.orm_classses.Group;

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
            System.out.println("Данные группы не были изменены. Данные не прошли валидацию");
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
