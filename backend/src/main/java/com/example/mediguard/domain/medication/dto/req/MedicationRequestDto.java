// req
package com.example.mediguard.domain.medication.dto.req;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicationRequestDto {
    private String name;
    private String ingredient;
    private String efficacy;
    private String dosage;
    private String precaution;
    private String sideEffect;
}
