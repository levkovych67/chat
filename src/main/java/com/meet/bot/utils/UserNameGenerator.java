package com.meet.bot.utils;

import java.util.List;
import java.util.Random;

public class UserNameGenerator {

    private static final List<String> FIRST_NAMES = List.of(
            "Андрій", "Артем", "Богдан", "Василь", "Віктор", "Дмитро", "Євген", "Іван", "Ігор", "Максим",
            "Микола", "Олег", "Роман", "Сергій", "Тарас", "Юрій", "Ярослав",
            "Анастасія", "Вікторія", "Галина", "Ірина", "Катерина", "Ксенія", "Марія", "Наталія",
            "Оксана", "Олена", "Софія", "Юлія", "Яна");


    private static final List<String> LAST_NAMES = List.of(
            "Квітка", "Соловейко", "Зоря", "Вітер", "Джерело", "Калина", "Дзвіночок", "Обрій", "Хмара", "Море",
            "Пиріжок", "Шкарпетка", "Дзиґа", "Курочка", "Капуста", "Носик", "Хвостик", "Огірочок", "Бджілка", "Мураха", "Скринька", "Смітник", "Пампушка", "Вареник", "Каша", "Котик", "Собачка", "Слоненя", "Жабеня", "Рибка",
            "Черепаха", "Банан", "Яблуко", "Груша", "Вишня", "Пінгвін", "Хлюпик", "Бульбашка", "Картопляний Чіп", "Бурундук", "Кіт-Кавалер", "Чупа-Чупс", "Тортеліні", "Кавунчик", "Лимонник"
    );


    public static String generateName() {
        Random random = new Random();
        String firstName = FIRST_NAMES.get(random.nextInt(FIRST_NAMES.size()));
        String lastName = LAST_NAMES.get(random.nextInt(LAST_NAMES.size()));
        return firstName + " " + lastName;
    }
}
