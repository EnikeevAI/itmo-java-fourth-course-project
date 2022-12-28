package ru.itmo.db.base;

import java.util.List;

public interface Dao<T, PK> {
    void createTable();

    // добавить информацию в таблицу
    void add(T t);

    // обновление данных
    void update(T t);

    // удаление данных по уникальному идентификатору
    T delete(PK pk);

    // получение по уникальному идентификатору
    T get(PK pk);

    // получение всех записей
    List<T> getAll();
}
