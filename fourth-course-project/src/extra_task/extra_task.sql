-- 1. Создание таблицы Альпинист
CREATE TABLE IF NOT EXISTS tb_alpinists
(ID SERIAL PRIMARY KEY, COUNTRY VARCHAR(200) NOT NULL, AGE INTEGER NOT NULL, NAME VARCHAR(100) NOT NULL);

-- 2. Добавление данных в таблицу Альпинист
INSERT INTO tb_alpinists(name, age, country) VALUES
("Иван", 28, "Россия"),
("Роман", 48, "Россия"),
("Александр", 18, "Белоруссия"),
("Алексей", 30, "Чехия"),
("Alex", 42, "Бразилия"),
("Leo", 31, "Аргентина"),
("Frank", 50, "Мексика");

-- 3. Изменение имени альпиниста
UPDATE tb_alpinists SET name = 'Steven' WHERE name = 'Frank';

-- 4. Получение идентификаторов и имен альпинистов старше 30 и младше 50 лет
SELECT id, name FROM tb_alpinists WHERE age between 30 and 50;

-- 5. Получение названий гор, высота которых больше указанной (6000)
SELECT name FROM tb_mountains Where height > 6000;

-- 6. Получение страны, в которой расположена гора с определенным названием
SELECT country from tb_mountains where name = 'Эверест';

-- 7. Получение идентификаторов, которые совершали восхождения в прошлом
UPDATE tb_groups SET date = '2022-02-02' WHERE id = 2; -- меняем дату на дату из прошлого для примера
SELECT a.id as alpinist_id, a.name as alpinist_name, g.id as group_id, g.date as date
from tb_groups g
inner join tb_alpinists a ON a.tb_groups_id = g.id
where g.date < now();

-- 8. Получение идентификаторов групп, которые совершали восхождения на гору с определенным названием
SELECT g.id as group_id, m.name as mountain
from tb_groups g
inner join tb_mountains m ON g.mountain_id = m.id
where m.name = 'Эверест';

-- 9. Получение идентификатором и имен альпинистов, которые совершали восхождения на горы, высота которых от 8000 до 9000
SELECT a.id, a.name
FROM tb_alpinists a
INNER JOIN tb_groups g ON a.tb_groups_id = g.id
INNER JOIN tb_mountains m ON g.mountain_id = m.id
WHERE m.height between 8000 and 9000
