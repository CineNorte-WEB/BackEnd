package com.tave.camchelin.domain.places.controller;

import com.tave.camchelin.domain.places.dto.PlaceDto;
import com.tave.camchelin.domain.places.service.PlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/places")
@RequiredArgsConstructor
public class PlaceController {

    private final PlaceService placeService;

    @GetMapping("/{id}")
    public ResponseEntity<PlaceDto> getPlaceById(@PathVariable Long id) {
        PlaceDto placeDto = placeService.getPlaceById(id);
        return ResponseEntity.ok(placeDto);
    }

    @GetMapping("/{name}")
    public ResponseEntity<PlaceDto> getPlaceByName(@PathVariable String name) {
        PlaceDto placeDto = placeService.getPlaceByName(name);
        return ResponseEntity.ok(placeDto);
    }
}
