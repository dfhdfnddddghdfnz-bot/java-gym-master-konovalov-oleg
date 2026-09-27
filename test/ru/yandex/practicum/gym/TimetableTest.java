package ru.yandex.practicum.gym;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TimetableTest {

    @Test
    void testGetTrainingSessionsForDaySingleSession() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        assertAll(
                //Проверить, что за понедельник вернулось одно занятие
                () -> assertEquals(List.of(singleTrainingSession),
                        timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY)),
                //Проверить, что за вторник не вернулось занятий
                () -> assertTrue(timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY).isEmpty())
        );
    }

    @Test
    void testGetTrainingSessionsForDayMultipleSessions() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");

        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);
        TrainingSession thursdayAdultTrainingSession = new TrainingSession(groupAdult, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(20, 0));

        timetable.addNewTrainingSession(thursdayAdultTrainingSession);

        Group groupChild = new Group("Акробатика для детей", Age.CHILD, 60);
        TrainingSession mondayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        TrainingSession thursdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(13, 0));
        TrainingSession saturdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.SATURDAY, new TimeOfDay(10, 0));

        timetable.addNewTrainingSession(mondayChildTrainingSession);
        timetable.addNewTrainingSession(thursdayChildTrainingSession);
        timetable.addNewTrainingSession(saturdayChildTrainingSession);

        assertAll(
                // Проверить, что за понедельник вернулось одно занятие
                () -> assertEquals(List.of(mondayChildTrainingSession),
                        timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY)),
                // Проверить, что за четверг вернулось два занятия в правильном порядке: сначала в 13:00, потом в 20:00
                () -> assertEquals(List.of(thursdayChildTrainingSession, thursdayAdultTrainingSession),
                        timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY)),
                // Проверить, что за вторник не вернулось занятий
                () -> assertTrue(timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY).isEmpty())
        );
    }

    @Test
    void testGetTrainingSessionsForDayAndTime() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        assertAll(
                //Проверить, что за понедельник в 13:00 вернулось одно занятие
                () -> assertEquals(List.of(singleTrainingSession),
                        timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(13, 0))),
                //Проверить, что за понедельник в 14:00 не вернулось занятий
                () -> assertTrue(timetable
                        .getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(14, 0)).isEmpty())
        );
    }

    @Test
    void testGetTrainingSessionsForDaySortedWhenAddedOutOfOrder() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        Group group = new Group("Акробатика для детей", Age.CHILD, 60);

        TrainingSession eveningTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.FRIDAY, new TimeOfDay(20, 0));
        TrainingSession earlyMorningTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.FRIDAY, new TimeOfDay(9, 30));
        TrainingSession lateMorningTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.FRIDAY, new TimeOfDay(9, 45));

        timetable.addNewTrainingSession(eveningTrainingSession);
        timetable.addNewTrainingSession(earlyMorningTrainingSession);
        timetable.addNewTrainingSession(lateMorningTrainingSession);

        assertEquals(
                List.of(earlyMorningTrainingSession, lateMorningTrainingSession, eveningTrainingSession),
                timetable.getTrainingSessionsForDay(DayOfWeek.FRIDAY));
    }

    @Test
    void testGetTrainingSessionsForDayAndTimeSeveralSessionsAtSameTime() {
        Timetable timetable = new Timetable();

        Group groupChild = new Group("Акробатика для детей", Age.CHILD, 60);
        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);
        Coach firstCoach = new Coach("Васильев", "Николай", "Сергеевич");
        Coach secondCoach = new Coach("Петрова", "Анна", "Ивановна");

        TrainingSession childTrainingSession = new TrainingSession(groupChild, firstCoach,
                DayOfWeek.WEDNESDAY, new TimeOfDay(18, 0));
        TrainingSession adultTrainingSession = new TrainingSession(groupAdult, secondCoach,
                DayOfWeek.WEDNESDAY, new TimeOfDay(18, 0));
        TrainingSession laterTrainingSession = new TrainingSession(groupAdult, secondCoach,
                DayOfWeek.WEDNESDAY, new TimeOfDay(19, 0));

        timetable.addNewTrainingSession(childTrainingSession);
        timetable.addNewTrainingSession(adultTrainingSession);
        timetable.addNewTrainingSession(laterTrainingSession);

        assertEquals(List.of(childTrainingSession, adultTrainingSession),
                timetable.getTrainingSessionsForDayAndTime(DayOfWeek.WEDNESDAY, new TimeOfDay(18, 0)));
    }

    @Test
    void testGetTrainingSessionsForEmptyTimetable() {
        Timetable timetable = new Timetable();

        assertAll(
                () -> assertTrue(timetable.getTrainingSessionsForDay(DayOfWeek.SUNDAY).isEmpty()),
                () -> assertTrue(timetable
                        .getTrainingSessionsForDayAndTime(DayOfWeek.SUNDAY, new TimeOfDay(10, 0)).isEmpty())
        );
    }

    @Test
    void testGetCountByCoachesEmptyTimetable() {
        Timetable timetable = new Timetable();

        assertTrue(timetable.getCountByCoaches().isEmpty());
    }

    @Test
    void testGetCountByCoachesSingleCoach() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");

        timetable.addNewTrainingSession(new TrainingSession(group, coach, DayOfWeek.MONDAY, new TimeOfDay(13, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach, DayOfWeek.THURSDAY, new TimeOfDay(13, 0)));

        List<CounterOfTrainings> countByCoaches = timetable.getCountByCoaches();

        assertAll(
                () -> assertEquals(1, countByCoaches.size()),
                () -> assertEquals(coach, countByCoaches.getFirst().getCoach()),
                () -> assertEquals(2, countByCoaches.getFirst().getCount())
        );
    }

    @Test
    void testGetCountByCoachesSortedByCountDesc() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach busiestCoach = new Coach("Васильев", "Николай", "Сергеевич");
        Coach middleCoach = new Coach("Петрова", "Анна", "Ивановна");
        Coach freeCoach = new Coach("Смирнов", "Петр", "Олегович");

        timetable.addNewTrainingSession(new TrainingSession(group, busiestCoach,
                DayOfWeek.MONDAY, new TimeOfDay(9, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, busiestCoach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, busiestCoach,
                DayOfWeek.FRIDAY, new TimeOfDay(20, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, middleCoach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, middleCoach,
                DayOfWeek.SATURDAY, new TimeOfDay(10, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, freeCoach,
                DayOfWeek.SUNDAY, new TimeOfDay(12, 0)));

        List<CounterOfTrainings> countByCoaches = timetable.getCountByCoaches();

        assertAll(
                () -> assertEquals(3, countByCoaches.size()),
                () -> assertEquals(busiestCoach, countByCoaches.getFirst().getCoach()),
                () -> assertEquals(3, countByCoaches.getFirst().getCount()),
                () -> assertEquals(middleCoach, countByCoaches.get(1).getCoach()),
                () -> assertEquals(2, countByCoaches.get(1).getCount()),
                () -> assertEquals(freeCoach, countByCoaches.get(2).getCoach()),
                () -> assertEquals(1, countByCoaches.get(2).getCount())
        );
    }

    @Test
    void testGetCountByCoachesSameFullNameIsOneCoach() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        Coach sameCoach = new Coach("Васильев", "Николай", "Сергеевич");

        timetable.addNewTrainingSession(new TrainingSession(group, coach, DayOfWeek.MONDAY, new TimeOfDay(13, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, sameCoach, DayOfWeek.TUESDAY, new TimeOfDay(13, 0)));

        assertAll(
                () -> assertEquals(1, timetable.getCountByCoaches().size()),
                () -> assertEquals(2, timetable.getCountByCoaches().getFirst().getCount())
        );
    }
}
