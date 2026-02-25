package com.example.gymweb.Service;

import com.example.gymweb.Repository.AptoMedicoRepository;
import com.example.gymweb.dto.Request.AptoAprobarRequest;
import com.example.gymweb.dto.Request.AptoUploadRequest;
import com.example.gymweb.dto.Response.AptoResponse;
import com.example.gymweb.model.AptoEstado;
import com.example.gymweb.model.AptoMedico;
import com.example.gymweb.model.Usuario;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;

@Service
public class AptoMedicoService {

    private final AptoMedicoRepository repo;

    public AptoMedicoService(AptoMedicoRepository repo) {
        this.repo = repo;
    }

    public AptoResponse subir(Usuario usuario, AptoUploadRequest request) {
        if (request.getBase64() == null || request.getBase64().isBlank()) {
            throw new RuntimeException("Archivo requerido");
        }
        AptoMedico apto = new AptoMedico();
        apto.setUsuario(usuario);
        apto.setNombreArchivo(request.getNombreArchivo());
        apto.setEstado(AptoEstado.PENDIENTE);
        apto.setFechaSubida(LocalDateTime.now());
        apto.setFechaVencimiento(null);
        apto.setArchivo(decodeBase64(request.getBase64()));
        repo.save(apto);
        return toResponse(apto);
    }

    public List<AptoResponse> listarMios(Integer usuarioId) {
        return repo.findByUsuario_IdOrderByFechaSubidaDesc(usuarioId).stream().map(this::toResponse).toList();
    }

    public List<AptoResponse> listarPendientes() {
        return repo.findByEstadoOrderByFechaSubidaAsc(AptoEstado.PENDIENTE).stream().map(this::toResponse).toList();
    }

    public List<AptoResponse> listarAprobadosVigentes() {
        return repo.findVigentes(AptoEstado.APROBADO, LocalDate.now()).stream().map(this::toResponse).toList();
    }

    public AptoResponse aprobar(Integer id, AptoAprobarRequest request) {
        AptoMedico apto = repo.findById(id).orElseThrow(() -> new RuntimeException("Apto no encontrado"));
        apto.setEstado(AptoEstado.APROBADO);
        apto.setFechaVencimiento(request.getFechaVencimiento());
        repo.save(apto);
        return toResponse(apto);
    }

    public AptoResponse rechazar(Integer id) {
        AptoMedico apto = repo.findById(id).orElseThrow(() -> new RuntimeException("Apto no encontrado"));
        apto.setEstado(AptoEstado.RECHAZADO);
        apto.setFechaVencimiento(null);
        repo.save(apto);
        return toResponse(apto);
    }

    public AptoResponse cancelar(Integer id) {
        AptoMedico apto = repo.findById(id).orElseThrow(() -> new RuntimeException("Apto no encontrado"));
        apto.setEstado(AptoEstado.CANCELADO);
        repo.save(apto);
        return toResponse(apto);
    }

    private byte[] decodeBase64(String value) {
        String cleaned = value.contains(",") ? value.substring(value.indexOf(',') + 1) : value;
        return Base64.getDecoder().decode(cleaned);
    }

    private AptoResponse toResponse(AptoMedico apto) {
        AptoResponse res = new AptoResponse();
        res.setId(apto.getId());
        res.setUsuarioId(apto.getUsuario().getId());
        res.setUsuarioNombre(apto.getUsuario().getNombre());
        res.setUsuarioEmail(apto.getUsuario().getEmail());
        res.setEstado(apto.getEstado().name());
        res.setFechaSubida(apto.getFechaSubida());
        res.setFechaVencimiento(apto.getFechaVencimiento());
        res.setNombreArchivo(apto.getNombreArchivo());
        return res;
    }
}
