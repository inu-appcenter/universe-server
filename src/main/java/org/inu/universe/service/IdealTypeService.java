package org.inu.universe.service;


import org.inu.universe.model.idealType.IdealTypeResponse;
import org.inu.universe.model.idealType.IdealTypeRequest;

public interface IdealTypeService {
    IdealTypeResponse saveIdealType(Long accountId, IdealTypeRequest request);

    IdealTypeResponse updateIdealType(Long accountId, IdealTypeRequest request);

    IdealTypeResponse findIdealType(Long accountId);
}
