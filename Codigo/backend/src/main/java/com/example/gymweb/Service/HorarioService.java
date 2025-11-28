package com.example.gymweb.Service;

import com.example.gymweb.Repository.HorarioRepository;
import com.example.gymweb.dto.Request.HorarioUpdateRequest;
import com.example.gymweb.dto.Response.HorarioDiaResponse;
import com.example.gymweb.model.Horario;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class HorarioService {

    private final HorarioRepository horarioRepository;
    private final NotificacionService notificacionService;

    public HorarioService(HorarioRepository horarioRepository, NotificacionService notificacionService) {
        this.horarioRepository = horarioRepository;
        this.notificacionService = notificacionService;
    }

    public List<HorarioDiaResponse> obtener() {
        ensureDefaults();
        return horarioRepository.findAll().stream()
                .sorted(Comparator.comparing(Horario::getId))
                .map(this::map)
                .collect(Collectors.toList());
    }

    public List<HorarioDiaResponse> actualizar(HorarioUpdateRequest request) {
        if (request.getDias() == null || request.getDias().isEmpty()) {
            throw new RuntimeException("Horario invalido");
        }
        ensureDefaults();
        request.getDias().forEach(d -> {
            if (d.getHoraApertura() == null || d.getHoraCierre() == null || d.getDia() == null) {
                throw new RuntimeException("Horario invalido");
            }
            Horario horario = horarioRepository.findAll().stream()
                    .filter(h -> h.getDia().equalsIgnoreCase(d.getDia()))
                    .findFirst()
                    .orElseGet(() -> {
                        Horario nuevo = new Horario();
                        nuevo.setDia(d.getDia());
                        return nuevo;
                    });
            horario.setHoraApertura(d.getHoraApertura().trim());
            horario.setHoraCierre(d.getHoraCierre().trim());
            horarioRepository.save(horario);
        });

        String mensaje = "Se actualizo el horario del gimnasio. Revisa los nuevos horarios por dia.";
        notificacionService.crearParaTodos(mensaje);

        return obtener();
    }

    private void ensureDefaults() {
        if (horarioRepository.count() == 0) {
            List<String> dias = List.of("Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado");
            dias.forEach(d -> {
                Horario h = new Horario();
                h.setDia(d);
                h.setHoraApertura("08:00");
                h.setHoraCierre("22:00");
                horarioRepository.save(h);
            });
        }
    }

    private HorarioDiaResponse map(Horario horario) {
        HorarioDiaResponse response = new HorarioDiaResponse();
        response.setId(horario.getId());
        response.setDia(horario.getDia());
        response.setHoraApertura(horario.getHoraApertura());
        response.setHoraCierre(horario.getHoraCierre());
        return response;
    }
}
