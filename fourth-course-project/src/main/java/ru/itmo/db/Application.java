package ru.itmo.db;

import ru.itmo.db.dao.AlpinistDao;
import ru.itmo.db.orm_classses.Alpinist;

import java.util.List;

public class Application {
    public static void main(String[] args) {
        AlpinistDao alpDao = new AlpinistDao();
        alpDao.createTable();
        Alpinist ivan = new Alpinist("Иван", "Россия", 38);
        Alpinist petr = new Alpinist("Петр", "Россия", 55);
        Alpinist sergey = new Alpinist("Сергей", "Россия", 23);
        Alpinist alex = new Alpinist("Alex", "Бразилия", 48);
        Alpinist leo = new Alpinist("Alex", "Аргентина", 20);
        Alpinist frank = new Alpinist("Frank", "Мексика", 19);
        Alpinist steven = new Alpinist("Steven", "Великобритания", 90);

        alpDao.add(ivan);

    }
}
