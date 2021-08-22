package org.inu.universe.service.impl;

import lombok.RequiredArgsConstructor;
import org.inu.universe.domain.Account;
import org.inu.universe.domain.IdealType;
import org.inu.universe.exception.AccountException;
import org.inu.universe.exception.IdealTypeException;
import org.inu.universe.model.idealType.IdealTypeResponse;
import org.inu.universe.model.idealType.IdealTypeRequest;
import org.inu.universe.model.profile.ProfileResponse;
import org.inu.universe.repository.AccountRepository;
import org.inu.universe.repository.IdealTypeRepository;
import org.inu.universe.repository.ProfileRepository;
import org.inu.universe.service.IdealTypeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class IdealTypeServiceImpl implements IdealTypeService {

    private final IdealTypeRepository idealTypeRepository;
    private final AccountRepository accountRepository;
    private final ProfileRepository profileRepository;

    @Override
    @Transactional
    public IdealTypeResponse saveIdealType(Long accountId, IdealTypeRequest request) {
        Account findAccount = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountException("존재하지 않는 계정입니다."));

        IdealType idealType = IdealType.saveIdealType(request.getRegion(), request.getGender(), request.getAge1(), request.getAge2());
        IdealType savedIdealType = idealTypeRepository.save(idealType);

        findAccount.setIdealType(savedIdealType);

        return IdealTypeResponse.from(savedIdealType);
    }

    @Override
    @Transactional
    public IdealTypeResponse updateIdealType(Long idealTypeId, IdealTypeRequest request) {
        IdealType findIdealType = idealTypeRepository.findById(idealTypeId)
                .orElseThrow(() -> new IdealTypeException("존재하지 않는 IdealType입니다."));
        findIdealType.updateIdealType(request.getRegion(), request.getGender(), request.getAge1(), request.getAge2());

        return IdealTypeResponse.from(findIdealType);
    }

}
