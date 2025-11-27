package com.example.gymweb.Service;

import com.example.gymweb.Repository.MembresiaRepository;
import com.example.gymweb.Repository.PagoRepository;
import com.example.gymweb.dto.Request.PagoRequest;
import com.example.gymweb.dto.Response.PagoResponse;
import com.example.gymweb.model.EstadoMembresia;
import com.example.gymweb.model.EstadoPago;
import com.example.gymweb.model.Membresia;
import com.example.gymweb.model.Pago;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PagoService {
    @Autowired
    private PagoRepository pagoRepository;
    @Autowired
    private MembresiaRepository membresiaRepository;

    private PagoResponse convertirAResponse(Pago p) {
        PagoResponse res = new PagoResponse();
        res.setId(p.getId());
        res.setIdMembresia(p.getMembresia().getId());
        res.setMonto(p.getMonto());
        res.setEstado(p.getEstado().name());
        res.setComprobanteUrl(p.getComprobante_url());
        res.setFecha(p.getFecha());
        return res;
    }

    public PagoResponse registrarPago(PagoRequest request) {
        Membresia membresia = (Membresia)this.membresiaRepository.findById(request.getIdMembresia()).orElseThrow(() -> new RuntimeException("Membresía no encontrada"));
        Pago pago = new Pago();
        pago.setMembresia(membresia);
        pago.setMonto(request.getMonto());
        pago.setComprobante_url(request.getComprobanteUrl());
        pago.setFecha(LocalDateTime.now());
        pago.setEstado(EstadoPago.COMPLETADO);
        this.pagoRepository.save(pago);
        membresia.setEstado(EstadoMembresia.ACTIVA);
        membresia.setFechaInicio(LocalDateTime.now());
        membresia.setFechaFin(LocalDateTime.now().plusMonths(1L));
        this.membresiaRepository.save(membresia);
        return this.convertirAResponse(pago);
    }

    public List<PagoResponse> listarPagosPorMembresia(int idMembresia) {
        return this.pagoRepository.findByMembresiaId(idMembresia).stream().map(this::convertirAResponse).toList();
    }

    public List<PagoResponse> listarPagosDeUsuario(int idUsuario) {
        return this.membresiaRepository.findFirstByUsuarioIdOrderByFechaFinDesc(idUsuario).stream().flatMap((m) -> this.pagoRepository.findByMembresiaId(m.getId()).stream()).map(this::convertirAResponse).toList();
    }

    public List<PagoResponse> listarTodos() {
        return this.pagoRepository.findAll().stream().map(this::convertirAResponse).toList();
    }
}
