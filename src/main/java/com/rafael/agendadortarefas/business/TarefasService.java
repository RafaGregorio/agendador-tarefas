package com.rafael.agendadortarefas.business;

import com.rafael.agendadortarefas.business.dto.TarefasDTO;
import com.rafael.agendadortarefas.business.mapper.TarefasConverter;
import com.rafael.agendadortarefas.infrastructure.entity.TarefasEntity;
import com.rafael.agendadortarefas.infrastructure.enums.StatusNotificacaoEnum;
import com.rafael.agendadortarefas.infrastructure.repository.TarefasRepository;
import com.rafael.agendadortarefas.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TarefasService {

    private final TarefasRepository tarefasRepository;
    private final TarefasConverter tarefasConverter;
    private final JwtUtil jwtUtil;

    public TarefasDTO gravarTarefa(String token, TarefasDTO dto) {
        String email = jwtUtil.extrairEmailToken(token.substring(7));
        dto.setDataCriacao(LocalDateTime.now());
        dto.setStatus(StatusNotificacaoEnum.PENDENTE);
        dto.setEmailUsuario(email);
        TarefasEntity entity = tarefasConverter.paraTarefasEntity(dto);

        return tarefasConverter.paraTarefasDTO(tarefasRepository.save(entity));
    }
}
