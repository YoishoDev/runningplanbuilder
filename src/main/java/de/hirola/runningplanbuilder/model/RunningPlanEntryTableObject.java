package de.hirola.runningplanbuilder.model;

import de.hirola.sportsapplications.model.MovementType;
import de.hirola.sportsapplications.model.RunningPlanEntry;
import de.hirola.sportsapplications.model.RunningUnit;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Copyright 2022 by Michael Schmidt, Hirola Consulting
 * This software us licensed under the AGPL-3.0 or later.
 *
 * A helper class to view an entry of a running plan in table view.
 *
 * @author Michael Schmidt (Hirola)
 * @since v.0.1
 */
public class RunningPlanEntryTableObject {

    private final String dayString;
    private final String weekString;
    private final String durationString;
    private final String runningUnitsString;


    public RunningPlanEntryTableObject(@NotNull RunningPlanEntry runningPlanEntry) {
        dayString = String.valueOf(runningPlanEntry.getDay());
        weekString = String.valueOf(runningPlanEntry.getWeek());
        durationString = String.valueOf(runningPlanEntry.getDuration());
        runningUnitsString = buildRunningUnitsString(runningPlanEntry.getRunningUnits());
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

    public String getRunningUnitsString() {
        return runningUnitsString;
    }

    private String buildRunningUnitsString(List<RunningUnit> runningUnits) {
        StringBuilder runningUnitsString = new StringBuilder();
        int count = 0;
        for (RunningUnit runningUnit: runningUnits) {
            MovementType movementType = runningUnit.getMovementType();
            runningUnitsString
                    .append(runningUnit.getDuration())
                    .append(" min ")
                    .append(movementType.getName());
            if (count < runningUnits.size()) {
                runningUnitsString.append(", ");
            }
            count++;
        }
        return runningUnitsString.toString();
    }
}
