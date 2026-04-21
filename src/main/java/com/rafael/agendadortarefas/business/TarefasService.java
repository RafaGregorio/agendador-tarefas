package com.rafael.agendadortarefas.business;

import com.rafael.agendadortarefas.business.dto.TarefasDTO;
import com.rafael.agendadortarefas.business.mapper.TarefaUpdateConverter;
import com.rafael.agendadortarefas.business.mapper.TarefasConverter;
import com.rafael.agendadortarefas.infrastructure.entity.TarefasEntity;
import com.rafael.agendadortarefas.infrastructure.enums.StatusNotificacaoEnum;
import com.rafael.agendadortarefas.infrastructure.exceptions.ResourceNotFoundException;
import com.rafael.agendadortarefas.infrastructure.repository.TarefasRepository;
import com.rafael.agendadortarefas.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TarefasService {

    private final TarefasRepository tarefasRepository;
    private final TarefasConverter tarefasConverter;
    private final JwtUtil jwtUtil;
    private final TarefaUpdateConverter  tarefaUpdateConverter;


    // Post Metodos
    public TarefasDTO gravarTarefa(String token, TarefasDTO dto) {
        String email = jwtUtil.extrairEmailToken(token.substring(7));
        dto.setDataCriacao(LocalDateTime.now());
        dto.setStatus(StatusNotificacaoEnum.PENDENTE);
        dto.setEmailUsuario(email);
        TarefasEntity entity = tarefasConverter.paraTarefasEntity(dto);

        return tarefasConverter.paraTarefasDTO(tarefasRepository.save(entity));
    }

    // Get Metodos
    public List<TarefasDTO> buscaTarefasAgendadasPorPeriodo(LocalDateTime dataInicial, LocalDateTime dataFinal) {
     return tarefasConverter.paraListaTarefasDTO(
             tarefasRepository.findByDataEventoBetween(dataInicial, dataFinal));
    }

    public List<TarefasDTO> buscaTarefasPorEmail(String token) {
        String email = jwtUtil.extrairEmailToken(token.substring(7));
        List<TarefasEntity> list = tarefasRepository.findByEmailUsuario(email);

        return tarefasConverter.paraListaTarefasDTO(list);
    }

    // Update Metodos
    public TarefasDTO alteraStatus(StatusNotificacaoEnum status, String id) {
        try {
            TarefasEntity entity = tarefasRepository.findById(id).
                    orElseThrow(() -> new ResourceNotFoundException("Tarefa não encontrada" + id));
            entity.setStatus(status);
            return tarefasConverter.paraTarefasDTO(tarefasRepository.save(entity));
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Erro ao alterar status da tarefa" + e.getMessage());
        }
    }

    public TarefasDTO updateTarefas(TarefasDTO dto, String id) {
        try {
            TarefasEntity entity = tarefasRepository.findById(id).
                    orElseThrow(() -> new ResourceNotFoundException("Tarefa não encontrada" + id));
            tarefaUpdateConverter.updateTarefas(dto, entity);
            return tarefasConverter.paraTarefasDTO(tarefasRepository.save(entity));
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Erro ao alterar status da tarefa" + e.getMessage());
        }
    }

    // Delete Metodos
    public void deletaTarefaPorId(String id) {
        try {
            tarefasRepository.deleteById(id);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Erro ao deletar tarefa por id, id inexistente: " + id,
                    e.getCause());
        }
    }
}
