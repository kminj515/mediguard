// res
package com.example.mediguard.domain.medication.dto.res;

import com.example.mediguard.domain.medication.entity.Medication;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MedicationResponseDto {
    private Long id;
    private String name;
    private String ingredient;
    private String efficacy;
    private String dosage;
    private String precaution;
    private String sideEffect;

    public static MedicationResponseDto from(Medication medication) {
        return MedicationResponseDto.builder()
                .id(medication.getId())
                .name(medication.getName())
                .ingredient(medication.getIngredient())
                .efficacy(medication.getEfficacy())
                .dosage(medication.getDosage())
                .precaution(medication.getPrecaution())
                .sideEffect(medication.getSideEffect())
                .build();
    }
}
