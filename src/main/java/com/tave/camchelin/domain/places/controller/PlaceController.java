package com.tave.camchelin.domain.places.controller;

import com.tave.camchelin.domain.menus.entity.Menu;
import com.tave.camchelin.domain.menus.repository.MenuRepository;
import com.tave.camchelin.domain.places.dto.PlaceDto;
import com.tave.camchelin.domain.places.dto.SearchResponseDto;
import com.tave.camchelin.domain.places.entity.Place;
import com.tave.camchelin.domain.places.service.PlaceService;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/places")
@RequiredArgsConstructor
public class PlaceController {

    private final PlaceService placeService;

    @GetMapping("/id/{id}")
    public ResponseEntity<PlaceDto> getPlaceById(@PathVariable Long id) {
        PlaceDto placeDto = placeService.getPlaceById(id);
        return ResponseEntity.ok(placeDto);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<PlaceDto> getPlaceByName(@PathVariable String name) {
        PlaceDto placeDto = placeService.getPlaceByName(name);
        return ResponseEntity.ok(placeDto);
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchPlacesByName(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        System.out.println("page = " + page);
        Page<SearchResponseDto> response = placeService.searchPlacesByName(name, page, size);
        return ResponseEntity.ok(response);
    }
}
