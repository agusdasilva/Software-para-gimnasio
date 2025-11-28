package com.example.gymweb.Service;

import com.example.gymweb.Repository.RutinaDetalleRepository;
import com.example.gymweb.Repository.RutinaRepository;
import com.example.gymweb.Repository.UsuarioRepository;
import com.example.gymweb.dto.Request.ModificarRutinaDetalleRequest;
import com.example.gymweb.dto.Request.RutinaRequest;
import com.example.gymweb.dto.Response.RutinaDetalleResponse;
import com.example.gymweb.dto.Response.RutinaResponse;
import com.example.gymweb.model.EjercicioDetalle;
import com.example.gymweb.model.Rutina;
import com.example.gymweb.model.RutinaDetalle;
import com.example.gymweb.model.Serie;
import com.example.gymweb.model.Usuario;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RutinaService {
    @Autowired
    private RutinaRepository rutinaRepository;
    @Autowired
    private RutinaDetalleRepository rutinaDetalleRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;

    public RutinaResponse crearRutina(RutinaRequest request) {
        Usuario creador = (Usuario)this.usuarioRepository.findById(request.getIdCreador()).orElseThrow(() -> new RuntimeException("Creador no encontrado"));
        Rutina rutina = new Rutina();
        rutina.setNombre(request.getNombre());
        rutina.setCreador(creador);
        this.rutinaRepository.save(rutina);
        RutinaDetalle detalle = new RutinaDetalle();
        detalle.setRutina(rutina);
        detalle.setDescripcion(request.getDescripcion());
        detalle.setImagen(request.getImagen());
        detalle.setDescanso_seg(request.getDescanso_seg());
        detalle.setEjercicios(new ArrayList());
        this.rutinaDetalleRepository.save(detalle);
        rutina.setRutinaDetalle(detalle);
        this.rutinaRepository.save(rutina);
        RutinaResponse response = new RutinaResponse();
        response.setId(rutina.getId());
        response.setNombre(rutina.getNombre());
        response.setCreador(rutina.getCreador().getNombre());
        RutinaDetalleResponse det = new RutinaDetalleResponse();
        det.setId(detalle.getId());
        det.setRutina(rutina.getNombre());
        det.setDescanso_seg(detalle.getDescanso_seg());
        det.setDescripcion(detalle.getDescripcion());
        det.setImagen(detalle.getImagen());
        det.setEjercicios(new ArrayList());
        response.setDetalle(det);
        return response;
    }

    public void elimninarPorId(int idRutina) {
        this.rutinaRepository.deleteById(idRutina);
    }

    private RutinaResponse convertirAResponse(Rutina rutina) {
        RutinaDetalle detalle = rutina.getRutinaDetalle();
        RutinaResponse response = new RutinaResponse();
        response.setId(rutina.getId());
        response.setNombre(rutina.getNombre());
        response.setCreador(rutina.getCreador().getNombre());
        RutinaDetalleResponse detRes = new RutinaDetalleResponse();
        detRes.setId(detalle.getId());
        detRes.setRutina(rutina.getNombre());
        detRes.setDescanso_seg(detalle.getDescanso_seg());
        detRes.setDescripcion(detalle.getDescripcion());
        detRes.setImagen(detalle.getImagen());
        detRes.setEjercicios(mapEjercicios(detalle));
        response.setDetalle(detRes);
        return response;
    }

    public RutinaResponse buscarPorId(Integer id) {
        Rutina rutina = (Rutina)this.rutinaRepository.findById(id).orElseThrow(() -> new RuntimeException("Rutina no encontrada"));
        return this.convertirAResponse(rutina);
    }

    public RutinaResponse buscarPorNombre(String nombre) {
        Rutina rutina = (Rutina)this.rutinaRepository.findByNombreIgnoreCase(nombre).orElseThrow(() -> new RuntimeException("No existe una rutina con ese nombre"));
        return this.convertirAResponse(rutina);
    }

    public RutinaResponse modificarDescanso(Integer idRutina, int nuevoDescanso) {
        Rutina rutina = (Rutina)this.rutinaRepository.findById(idRutina).orElseThrow(() -> new RuntimeException("Rutina no encontrada"));
        RutinaDetalle detalle = rutina.getRutinaDetalle();
        if (detalle == null) {
            throw new RuntimeException("La rutina no tiene detalle asociado");
        } else {
            detalle.setDescanso_seg(nuevoDescanso);
            this.rutinaDetalleRepository.save(detalle);
            return this.convertirAResponse(rutina);
        }
    }

    public List<RutinaResponse> listarTodas() {
        return this.rutinaRepository.findAll().stream().map(this::convertirAResponse).toList();
    }
    public RutinaResponse modificarDetalle(Integer idRutina, ModificarRutinaDetalleRequest request) {

        Rutina rutina = rutinaRepository.findById(idRutina)
                .orElseThrow(() -> new RuntimeException("Rutina no encontrada"));

        RutinaDetalle detalle = rutina.getRutinaDetalle();

        if (detalle == null) {
            throw new RuntimeException("La rutina no posee detalle asociado.");
        }

        if (request.getDescripcion() != null)
            detalle.setDescripcion(request.getDescripcion());

        if (request.getImagen() != null)
            detalle.setImagen(request.getImagen());

        if (request.getDescanso_seg() != null)
            detalle.setDescanso_seg(request.getDescanso_seg());

        rutinaDetalleRepository.save(detalle);

        return convertirAResponse(rutina);
    }

    private List<com.example.gymweb.dto.Response.EjercicioDetalleResponse> mapEjercicios(RutinaDetalle detalle) {
        if (detalle == null || detalle.getEjercicios() == null) {
            return new ArrayList<>();
        }
        return detalle.getEjercicios().stream()
                .sorted(Comparator.comparingInt(EjercicioDetalle::getOrden))
                .map(this::mapEjercicio)
                .toList();
    }

    private com.example.gymweb.dto.Response.EjercicioDetalleResponse mapEjercicio(EjercicioDetalle ej) {
        com.example.gymweb.dto.Response.EjercicioDetalleResponse res = new com.example.gymweb.dto.Response.EjercicioDetalleResponse();
        res.setId(ej.getId());
        res.setEjercicio(ej.getEjercicio().getNombre());
        res.setSeries(mapSeries(ej));
        return res;
    }

    private List<com.example.gymweb.dto.Response.SerieResponse> mapSeries(EjercicioDetalle ej) {
        List<Serie> series = ej.getSeries();
        if (series == null) {
            return new ArrayList<>();
        }
        return series.stream()
                .sorted(Comparator.comparingInt(Serie::getOrden))
                .map(s -> {
                    com.example.gymweb.dto.Response.SerieResponse sr = new com.example.gymweb.dto.Response.SerieResponse();
                    sr.setId(s.getId());
                    sr.setCarga(s.getCarga());
                    sr.setRepeticiones(s.getRepeticiones());
                    sr.setOrden(s.getOrden());
                    return sr;
                })
                .toList();
    }
}
