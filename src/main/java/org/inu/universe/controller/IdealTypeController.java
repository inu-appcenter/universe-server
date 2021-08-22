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

    @PostMapping("/idealType")
    public ResponseEntity<IdealTypeResponse> saveIdealType(@LoginAccount Long accountId, @RequestBody @Valid IdealTypeRequest request) {
        return ResponseEntity.ok(idealTypeService.saveIdealType(accountId, request));
    }

    @PatchMapping("/idealType/{idealTypeId}")
    public ResponseEntity<IdealTypeResponse> updateIdealType(@PathVariable Long idealTypeId, @RequestBody @Valid IdealTypeRequest request) {
        return ResponseEntity.ok(idealTypeService.updateIdealType(idealTypeId, request));
    }
}
