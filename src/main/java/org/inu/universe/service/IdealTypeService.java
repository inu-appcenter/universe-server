package org.inu.universe.service;


import org.inu.universe.model.idealType.IdealTypeResponse;
import org.inu.universe.model.idealType.IdealTypeRequest;
import org.inu.universe.model.profile.ProfileResponse;

import java.util.List;

public interface IdealTypeService {
    IdealTypeResponse saveIdealType(Long accountId, IdealTypeRequest request);

    IdealTypeResponse updateIdealType(Long idealTypeId, IdealTypeRequest request);
}
