package ru.itmo.db;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import ru.itmo.db.dao.AlpinistDao;
import ru.itmo.db.dao.GroupDao;
import ru.itmo.db.dao.MountainDao;
import ru.itmo.db.orm_classses.Alpinist;
import ru.itmo.db.orm_classses.Group;
import ru.itmo.db.orm_classses.Mountain;

public class Application {
    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("fourthCourseProject");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        AlpinistDao alpDao = new AlpinistDao(em);
        GroupDao groupDao = new GroupDao(em);
        MountainDao mountainDao = new MountainDao(em);

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
        alpDao.add(steven);


        Mountain everest = new Mountain("Эверест", "Гималаи", 8849);
        Mountain elbrus = new Mountain("Эльбрус", "Россия", 5642);
        Mountain kilimanjaro = new Mountain("Килиманджаро", "Танзания", 5885);
        mountainDao.add(everest);
        mountainDao.add(elbrus);
        mountainDao.add(kilimanjaro);

        Group group1 = new Group(3, everest);
        Group group2 = new Group(4, everest);

        groupDao.add(group1);
        groupDao.add(group2);

        group1.addAlpinist(ivan);
        group1.addAlpinist(frank);
        group1.removeAlpinist(sergey);
        group1.removeAlpinist(ivan);

        group2.addAlpinist(ivan);
        group2.addAlpinist(petr);
        group2.addAlpinist(sergey);
        group2.addAlpinist(alex);


        System.out.println("IIIIIIVVVVAAAAAAAANNNNN " + ivan.getGroup().getId());
        System.out.println("GGGGRRROOOOPPPP2 " + group2.getId());

        em.getTransaction().commit();
        System.out.println("GGGGRRROOOOPPPP2 " + group2.getId());

        em.close();
        emf.close();


    }
}
