package ru.yandex.practicum.gym;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public class Timetable {

    private final Map<DayOfWeek, NavigableMap<TimeOfDay, List<TrainingSession>>> timetable = new HashMap<>();

    public void addNewTrainingSession(TrainingSession trainingSession) {
        timetable
                .computeIfAbsent(trainingSession.getDayOfWeek(), day -> new TreeMap<>())
                .computeIfAbsent(trainingSession.getTimeOfDay(), time -> new ArrayList<>())
                .add(trainingSession);
    }

    public List<TrainingSession> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        return sessionsOfDay(dayOfWeek)
                .values()
                .stream()
                .flatMap(List::stream)
                .toList();
    }

    public List<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        return List.copyOf(sessionsOfDay(dayOfWeek).getOrDefault(timeOfDay, List.of()));
    }

    public List<CounterOfTrainings> getCountByCoaches() {
        Map<Coach, CounterOfTrainings> counters = new HashMap<>();
        timetable
                .values()
                .stream()
                .flatMap(sessionsOfDay -> sessionsOfDay.values().stream())
                .flatMap(List::stream)
                .forEach(trainingSession -> counters
                        .computeIfAbsent(trainingSession.getCoach(), CounterOfTrainings::new)
                        .increment()
                );
        return counters
                .values()
                .stream()
                .sorted(Comparator.comparingInt(CounterOfTrainings::getCount).reversed())
                .toList();
    }

    private NavigableMap<TimeOfDay, List<TrainingSession>> sessionsOfDay(DayOfWeek dayOfWeek) {
        return timetable.getOrDefault(dayOfWeek, Collections.emptyNavigableMap());
    }
}
