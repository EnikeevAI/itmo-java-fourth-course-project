package ru.itmo.db;

import ru.itmo.db.dao.AlpinistDao;
import ru.itmo.db.orm_classses.Alpinist;

import java.util.List;

public class Application {
    public static void main(String[] args) {
        AlpinistDao alpDao = new AlpinistDao();
        alpDao.createTable();
        Alpinist ivan = new Alpinist("Иван", "Россия", 120);
        alpDao.add(ivan);
        ivan.setName("Артем");
        alpDao.update(ivan);
        Alpinist alpinist = alpDao.delete(ivan.getId());
        System.out.println(alpinist.getName());
        List<Alpinist> alpinists = alpDao.getAll();
        System.out.println(alpinists);

    }
}
