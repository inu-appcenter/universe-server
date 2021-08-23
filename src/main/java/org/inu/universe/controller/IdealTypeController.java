package org.inu.universe.controller;

import lombok.RequiredArgsConstructor;
import org.inu.universe.config.security.LoginAccount;
import org.inu.universe.model.idealType.IdealTypeResponse;
import org.inu.universe.model.idealType.IdealTypeRequest;
import org.inu.universe.service.IdealTypeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


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
    @PatchMapping("/idealType")
    public ResponseEntity<IdealTypeResponse> updateIdealType(@LoginAccount Long accountId, @RequestBody @Valid IdealTypeRequest request) {
        return ResponseEntity.ok(idealTypeService.updateIdealType(accountId, request));
    }

    /*
    원하는 상대 조회 (설정해둔 것 조회)
     */
    @GetMapping("/idealType")
    public ResponseEntity<IdealTypeResponse> findIdealType(@LoginAccount Long accountId) {
        return ResponseEntity.ok(idealTypeService.findIdealType(accountId));
    }
}
