package com.rafael.agendadortarefas.business.mapper;

import com.rafael.agendadortarefas.business.dto.TarefasDTO;
import com.rafael.agendadortarefas.infrastructure.entity.TarefasEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TarefasConverter {

    TarefasEntity paraTarefasEntity(TarefasDTO tarefasDTO);
    TarefasDTO paraTarefasDTO(TarefasEntity tarefasEntity);
}
