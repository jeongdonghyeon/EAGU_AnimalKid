package com.example.myapplication.data.model.mapper

import com.example.myapplication.data.model.DTO.CalendarDTO
import com.example.myapplication.data.model.entity.CalendarEvent

fun toEntity(dto: CalendarDTO): CalendarEvent {
    return CalendarEvent(
        Calendarid = dto.Calendarid,
        Calendartitle = dto.Calendartitle,
        Calendardescription = dto.Calendardescription,
        Calendardate = dto.Calendardate,
        Calendartime = dto.Calendartime
    )
}

// Entity -> DTO 변환
fun toDTO(entity: CalendarEvent): CalendarDTO {
    return CalendarDTO(
        Calendarid = entity.Calendarid,
        Calendartitle = entity.Calendartitle,
        Calendardescription = entity.Calendardescription,
        Calendardate = entity.Calendardate,
        Calendartime = entity.Calendartime
    )
}

//DTO는 데이터 전송에 사용, Entity는 데이터베이스와의 메핑에 사용
// Dto 와 Dao 변환
