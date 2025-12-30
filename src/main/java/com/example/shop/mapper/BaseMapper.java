package com.example.shop.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public interface BaseMapper {
    @Named("dateToString")
    default String dateToString(LocalDateTime date) {
        return date == null ? null : date.toString();
    }
}
