package org.inu.universe.model.idealType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.inu.universe.domain.IdealType;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IdealTypeResponse {

    private Long id;

    private String region;

    private String gender;

    private Integer age1;

    private Integer age2;

    public static IdealTypeResponse from(IdealType idealType) {
        IdealTypeResponse idealTypeResponse = new IdealTypeResponse();
        idealTypeResponse.id = idealType.getId();
        idealTypeResponse.region = idealType.getRegion();
        idealTypeResponse.gender = idealType.getGender();
        idealTypeResponse.age1 = idealType.getAge1();
        idealTypeResponse.age2 = idealType.getAge2();
        return idealTypeResponse;
    }
}
