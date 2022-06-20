package de.hirola.runningplanbuilder.model;

import de.hirola.runningplanbuilder.util.ApplicationResources;
import de.hirola.sportsapplications.model.MovementType;
import de.hirola.sportsapplications.model.RunningPlanEntry;
import de.hirola.sportsapplications.model.RunningUnit;
import de.hirola.sportsapplications.model.UUID;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Copyright 2022 by Michael Schmidt, Hirola Consulting
 * This software us licensed under the AGPL-3.0 or later.
 *
 * A helper class to view an entry of a running plan in table view.
 *
 * @author Michael Schmidt (Hirola)
 * @since v0.1
 */
public class RunningPlanEntryTableObject {
    private final ApplicationResources applicationResources;
    private final UUID uuid;
    private String dayString;
    private String weekString;
    private String durationString;
    private String distanceString;
    private String remarksString;
    private String runningUnitsString;


    public RunningPlanEntryTableObject(@NotNull RunningPlanEntry entry) {
        uuid = entry.getUUID();
        applicationResources = ApplicationResources.getInstance();
        remarksString = entry.getRemarks()
                .orElse(applicationResources.getString("runningplanentry.remarks.default"));
        dayString = getWeekDayString(entry.getDay());
        weekString = String.valueOf(entry.getWeek());
        durationString = entry.getDuration() + " min";
        distanceString = entry.getDistance() + " km";
        runningUnitsString = buildRunningUnitsString(entry.getRunningUnits());
    }

    public String getRemarksString() {
        return remarksString;
    }

    public String getDayString() {
        return dayString;
    }

    public String getWeekString() {
        return weekString;
    }

    public String getDurationString() {
        return durationString;
    }

    public String getDistanceString() {
        return distanceString;
    }

    public String getRunningUnitsString() {
        return runningUnitsString;
    }

    public String getWeekDayString(int forDay) {
        String weekDayString = "";
        switch (forDay) {
            case 1: return applicationResources.getString("monday");
            case 2: return applicationResources.getString("tuesday");
            case 3: return applicationResources.getString("wednesday");
            case 4: return applicationResources.getString("thursday");
            case 5: return applicationResources.getString("friday");
            case 6: return applicationResources.getString("saturday");
            case 7: return applicationResources.getString("sunday");
        }
        return weekDayString;
    }

    public void update(@NotNull RunningPlanEntry entry) {
        if (entry.getUUID().equals(uuid)) {
            remarksString = entry.getRemarks()
                    .orElse(applicationResources.getString("runningplanentry.remarks.default"));
            dayString = getWeekDayString(entry.getDay());
            weekString = String.valueOf(entry.getWeek());
            durationString = entry.getDuration() + " min";
            distanceString = entry.getDistance() + " km";
            runningUnitsString = buildRunningUnitsString(entry.getRunningUnits());
        }
    }

    private String buildRunningUnitsString(List<RunningUnit> runningUnits) {
        StringBuilder runningUnitsString = new StringBuilder();
        int count = 0;
        for (RunningUnit runningUnit: runningUnits) {
            count++;
            String movementTypeName = applicationResources.getString("movementType.name.default");
            Optional<MovementType> movementType = runningUnit.getMovementType();
            if (movementType.isPresent()) {
                movementTypeName = movementType.get().getName();
            }
            runningUnitsString
                    .append(runningUnit.getDuration())
                    .append(" min ")
                    .append(movementTypeName);
            if (count < runningUnits.size()) {
                runningUnitsString.append(", ");
            }
        }
        return runningUnitsString.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RunningPlanEntryTableObject that = (RunningPlanEntryTableObject) o;
        return uuid.equals(that.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }
}
