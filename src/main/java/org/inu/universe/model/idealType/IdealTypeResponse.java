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

    private Integer age;

    private String college;


    public static IdealTypeResponse from(IdealType idealType) {
        IdealTypeResponse idealTypeResponse = new IdealTypeResponse();
        idealTypeResponse.id = idealType.getId();
        idealTypeResponse.region = idealType.getRegion();
        idealTypeResponse.gender = idealType.getGender();
        idealTypeResponse.age = idealType.getAge();
        idealTypeResponse.college = idealType.getCollege();
        return idealTypeResponse;
    }
}
