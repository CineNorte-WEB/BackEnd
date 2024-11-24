package com.tave.camchelin.domain.univs.controller;

import com.tave.camchelin.domain.places.dto.PlaceDto;
import com.tave.camchelin.domain.univs.dto.UnivDto;
import com.tave.camchelin.domain.univs.service.UnivService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/univs")
@RequiredArgsConstructor
public class UnivController {
    private final UnivService univService;


    @GetMapping("/{id}") // Univ 조회 (id로 조회)
    public ResponseEntity<UnivDto> getUnivById(@PathVariable Long id) {
        UnivDto univDto = univService.getUnivById(id);
        return ResponseEntity.ok(univDto);
    }


    @GetMapping("/{name}") // Univ 조회 (이름으로 조회)
    public ResponseEntity<UnivDto> getUnivByName(@PathVariable String name) {
        UnivDto univDto = univService.getUnivByName(name);
        return ResponseEntity.ok(univDto);
    }


    @GetMapping // 모든 Univ 목록 조회
    public ResponseEntity<List<UnivDto>> getAllUnivs() {
        List<UnivDto> univDtos = univService.getAllUnivs();
        return ResponseEntity.ok(univDtos);
    }

    @GetMapping("/{univId}/places") // 학교에 등록된 맛집 조회
    public ResponseEntity<List<PlaceDto>> getPlacesByUnivId(@PathVariable Long univId) {
        List<PlaceDto> places = univService.getPlacesByUnivId(univId);
        return ResponseEntity.ok(places);
    }
}
