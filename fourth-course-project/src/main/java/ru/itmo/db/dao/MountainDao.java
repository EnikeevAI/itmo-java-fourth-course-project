package ru.itmo.db.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import ru.itmo.db.base.Dao;
import ru.itmo.db.base.ValidatorBase;
import ru.itmo.db.orm_classses.Mountain;


import java.util.List;

public class MountainDao implements Dao<Mountain, Integer> {

    private final ValidatorBase<Mountain> validator;

    private final EntityManager manager;

    private final String db_table = "tb_mountains";


    public MountainDao(EntityManager manager){
        validator = new ValidatorBase<>();
        this.manager = manager;
    }

    @Override
    public void add(Mountain mountain) {
        if (!validator.validate(mountain)) {
            System.out.println("Данные в таблицу " + db_table + " не добавлены. Данные не прошли валидацию");
            return;
        }
        manager.persist(mountain);
        manager.flush();
    }

    @Override
    public void update(Mountain mountain) {
        if (!validator.validate(mountain)) {
            System.out.println("Данные горы не были изменены. Данные не прошли валидацию");
            return;
        }
        manager.merge(mountain);
    }

    @Override
    public Mountain delete(Integer id) {
        Mountain mountain = get(id);
        manager.remove(mountain);
        return mountain;
    }

    @Override
    public Mountain get(Integer id) {
        return manager.find(Mountain.class, id);
    }

    @Override
    public List<Mountain> getAll() {
        TypedQuery<Mountain> getAllGroupsQuery = manager.createQuery("SELECT g FROM Mountain g", Mountain.class);
        return getAllGroupsQuery.getResultList();
    }
}
