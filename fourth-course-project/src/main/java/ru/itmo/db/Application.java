package ru.itmo.db;

import ru.itmo.db.dao.AlpinistDao;
import ru.itmo.db.dao.MountainDao;
import ru.itmo.db.orm_classses.Alpinist;
import ru.itmo.db.orm_classses.Mountain;

import java.util.List;

public class Application {
    public static void main(String[] args) {
        /*AlpinistDao alpDao = new AlpinistDao();
        alpDao.createTable();
        Alpinist ivan = new Alpinist("Иван", "Россия", 38);
        Alpinist petr = new Alpinist("Петр", "Россия", 55);
        Alpinist sergey = new Alpinist("Сергей", "Россия", 23);
        Alpinist alex = new Alpinist("Alex", "Бразилия", 48);
        Alpinist leo = new Alpinist("Alex", "Аргентина", 20);
        Alpinist frank = new Alpinist("Frank", "Мексика", 19);
        Alpinist steven = new Alpinist("Steven", "Великобритания", 90);
        alpDao.add(ivan);
        alpDao.add(petr);
        alpDao.add(sergey);
        alpDao.add(alex);
        alpDao.add(leo);
        alpDao.add(frank);
        alpDao.add(steven);*/

        MountainDao mountainDao = new MountainDao();
        mountainDao.createTable();
        Mountain everest = new Mountain("Эверест", "Гималаи", 8849);
        Mountain elbrus = new Mountain("Эльбрус", "Россия", 5642);
        Mountain kilimanjaro = new Mountain("Килиманджаро", "Танзания", 5885);
        mountainDao.add(everest);
        mountainDao.add(elbrus);
        mountainDao.add(kilimanjaro);


    }
}
