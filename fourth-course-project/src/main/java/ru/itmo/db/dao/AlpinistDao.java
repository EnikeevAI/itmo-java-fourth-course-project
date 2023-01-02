package ru.itmo.db.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import ru.itmo.db.base.Dao;
import ru.itmo.db.base.ValidatorBase;
import ru.itmo.db.orm_classses.Alpinist;

import java.util.List;

public class AlpinistDao implements Dao<Alpinist, Integer> {

    private final ValidatorBase<Alpinist> validator;

    private final EntityManager manager;

    private final String db_table = "tb_alpinists";

    public AlpinistDao(EntityManager manager) {
        validator = new ValidatorBase<>();
        this.manager = manager;
    }

    @Override
    public void add(Alpinist alpinist) {
        if (!validator.validate(alpinist)) {
            System.out.println("Данные в таблицу " + db_table + " не добавлены. Данные не прошли валидацию");
            return;
        }
        manager.persist(alpinist);
        manager.flush();
    }

    @Override
    public void update(Alpinist alpinist) {
        if (!validator.validate(alpinist)) {
            System.out.println("Данные альпиниста не были изменены. Данные не прошли валидацию");
            return;
        }
        manager.merge(alpinist);
    }

    @Override
    public Alpinist delete(Integer id) {
        Alpinist alpinist = get(id);
        manager.remove(alpinist);
        return alpinist;
    }

    @Override
    public Alpinist get(Integer id) {
        return manager.find(Alpinist.class, id);
    }

    @Override
    public List<Alpinist> getAll() {
        TypedQuery<Alpinist> getAllGroupsQuery = manager.createQuery("SELECT g FROM Alpinist g", Alpinist.class);
        return getAllGroupsQuery.getResultList();
    }
}
