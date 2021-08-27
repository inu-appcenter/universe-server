package org.inu.universe.service.impl;

import lombok.RequiredArgsConstructor;
import org.inu.universe.domain.Account;
import org.inu.universe.domain.IdealType;
import org.inu.universe.exception.AccountException;
import org.inu.universe.exception.IdealTypeException;
import org.inu.universe.model.idealType.IdealTypeResponse;
import org.inu.universe.model.idealType.IdealTypeRequest;
import org.inu.universe.repository.AccountRepository;
import org.inu.universe.repository.IdealTypeRepository;
import org.inu.universe.service.IdealTypeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class IdealTypeServiceImpl implements IdealTypeService {

    private final IdealTypeRepository idealTypeRepository;
    private final AccountRepository accountRepository;

    /*
    원하는 상대 설정
     */
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

    /*
    원하는 상대 수정
     */
    @Override
    @Transactional
    public IdealTypeResponse updateIdealType(Long accountId, IdealTypeRequest request) {

        Account findAccount = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountException("존재하지 않는 계정입니다."));

        IdealType findIdealType = idealTypeRepository.findById(findAccount.getIdealType().getId())
                .orElseThrow(() -> new IdealTypeException("존재하지 않는 IdealType입니다."));

        findIdealType.updateIdealType(request.getRegion(), request.getGender(), request.getAge1(), request.getAge2());

        return IdealTypeResponse.from(findIdealType);
    }

    /*
    원하는 상대 설정해둔 것 조회
     */
    @Override
    public IdealTypeResponse findIdealType(Long accountId) {
        Account findAccount = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountException("존재하지 않는 계정입니다."));

        IdealType findIdealType = idealTypeRepository.findById(findAccount.getIdealType().getId())
                .orElseThrow(() -> new IdealTypeException("존재하지 않는 IdealType입니다."));

        return IdealTypeResponse.from(findIdealType);
    }

}
