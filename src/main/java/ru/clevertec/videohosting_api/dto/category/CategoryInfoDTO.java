package ru.clevertec.videohosting_api.dto.category;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class CategoryInfoDTO {
    private Long id;
    private String name;
}
