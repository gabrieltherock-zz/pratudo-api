package br.com.pratudo.recipe.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MethodOfPreparation {

    @JsonProperty("totalMethodOfPreparationTime")
    private Time time;

    private List<Step> steps;

    @JsonIgnore
    public Long getTotalMinutesInMethodOfPreparation() {

        return steps.stream()
                .map(
                        step -> step.getMethodOfPreparationItem()
                                .stream()
                                .map(MethodOfPreparationItem::getTime)
                                .map(stepTime -> stepTime.getTimeUnit().toMinutes(stepTime.getValue()))
                                .reduce(0L, Long::sum)
                )
                .reduce(0L, Long::sum);
    }
}