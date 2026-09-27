package ru.yandex.practicum.gym;

public class CounterOfTrainings {

    //тренер
    private final Coach coach;
    //количество занятий в неделю
    private int count;

    public CounterOfTrainings(Coach coach) {
        this.coach = coach;
    }

    public void increment() {
        count++;
    }

    public Coach getCoach() {
        return coach;
    }

    public int getCount() {
        return count;
    }
}
