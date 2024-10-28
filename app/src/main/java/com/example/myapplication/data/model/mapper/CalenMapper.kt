package com.example.myapplication.data.model.mapper

import com.example.myapplication.data.model.DTO.CalendarDTO
import com.example.myapplication.data.model.entity.CalendarEvent

fun toEntity(dto: CalendarDTO): CalendarEvent {
    return CalendarEvent(
        id = dto.id,
        title = dto.title,
        description = dto.description,
        date = dto.date,
        time = dto.time
    )
}

// Entity -> DTO 변환
fun toDTO(entity: CalendarEvent): CalendarDTO {
    return CalendarDTO(
        id = entity.id,
        title = entity.title,
        description = entity.description,
        date = entity.date,
        time = entity.time
    )
}

//DTO는 데이터 전송에 사용, Entity는 데이터베이스와의 메핑에 사용
// Dto 와 Dao 변환
