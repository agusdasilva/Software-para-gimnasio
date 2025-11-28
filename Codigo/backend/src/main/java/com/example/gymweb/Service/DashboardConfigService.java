package com.example.gymweb.Service;

import com.example.gymweb.Repository.ConfigDashboardRepository;
import com.example.gymweb.dto.Request.DashboardConfigUpdateRequest;
import com.example.gymweb.dto.Response.DashboardConfigResponse;
import com.example.gymweb.model.ConfigDashboard;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DashboardConfigService {

    private final ConfigDashboardRepository repo;
    private final NotificacionService notificacionService;

    public DashboardConfigService(ConfigDashboardRepository repo, NotificacionService notificacionService) {
        this.repo = repo;
        this.notificacionService = notificacionService;
    }

    public DashboardConfigResponse obtener() {
        ConfigDashboard cfg = repo.findAll().stream().findFirst().orElseGet(this::crearDefault);
        return map(cfg);
    }

    public DashboardConfigResponse actualizar(DashboardConfigUpdateRequest request) {
        ConfigDashboard cfg = repo.findAll().stream().findFirst().orElseGet(this::crearDefault);
        cfg.setNoticias(String.join("\n", safeList(request.getNoticias())));
        cfg.setRecordatorios(String.join("\n", safeList(request.getRecordatorios())));
        repo.save(cfg);
        notificacionService.crearParaTodos("Actualizamos noticias/recordatorios del gym. Dale un vistazo al dashboard.");
        return map(cfg);
    }

    private ConfigDashboard crearDefault() {
        ConfigDashboard cfg = new ConfigDashboard();
        cfg.setNoticias("Nuevas maquinas disponibles pronto");
        cfg.setRecordatorios("Llega 10 minutos antes");
        return repo.save(cfg);
    }

    private DashboardConfigResponse map(ConfigDashboard cfg) {
        DashboardConfigResponse response = new DashboardConfigResponse();
        response.setNoticias(splitLines(cfg.getNoticias()));
        response.setRecordatorios(splitLines(cfg.getRecordatorios()));
        return response;
    }

    private List<String> splitLines(String text) {
        if (text == null || text.isBlank()) return List.of();
        return Arrays.stream(text.split("\\n")).map(String::trim).filter(s -> !s.isEmpty()).collect(Collectors.toList());
    }

    private List<String> safeList(List<String> list) {
        if (list == null) return List.of();
        return list.stream().filter(s -> s != null && !s.isBlank()).map(String::trim).collect(Collectors.toList());
    }
}
