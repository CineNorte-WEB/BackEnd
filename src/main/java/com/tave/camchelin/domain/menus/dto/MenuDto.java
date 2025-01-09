package com.tave.camchelin.domain.menus.dto;

import com.tave.camchelin.domain.menus.entity.Menu;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuDto {
    private Long id;
    private String name;
    private Integer price;
    private String description;

    // Menu 엔티티 -> MenuDto로 변환
    public static MenuDto fromEntity(Menu menu) {
        return MenuDto.builder()
                .id(menu.getId())
                .name(menu.getName())
                .price(menu.getPrice())
                .description(menu.getDescription())
                .build();
    }
}