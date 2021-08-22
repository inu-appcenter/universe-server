package org.inu.universe.controller;

import lombok.RequiredArgsConstructor;
import org.inu.universe.config.security.LoginAccount;
import org.inu.universe.domain.Profile;
import org.inu.universe.model.idealType.IdealTypeResponse;
import org.inu.universe.model.idealType.IdealTypeRequest;
import org.inu.universe.model.profile.ProfileResponse;
import org.inu.universe.repository.ProfileRepository;
import org.inu.universe.service.IdealTypeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class IdealTypeController {

    private final IdealTypeService idealTypeService;

    /*
    원하는 상대 설정
     */
    @PostMapping("/idealType")
    public ResponseEntity<IdealTypeResponse> saveIdealType(@LoginAccount Long accountId, @RequestBody @Valid IdealTypeRequest request) {
        return ResponseEntity.ok(idealTypeService.saveIdealType(accountId, request));
    }

    /*
    원하는 상대 수정
     */
    @PatchMapping("/idealType/{idealTypeId}")
    public ResponseEntity<IdealTypeResponse> updateIdealType(@PathVariable Long idealTypeId, @RequestBody @Valid IdealTypeRequest request) {
        return ResponseEntity.ok(idealTypeService.updateIdealType(idealTypeId, request));
    }
}
