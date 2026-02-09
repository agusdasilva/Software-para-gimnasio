package com.example.gymweb.Service;

import com.example.gymweb.Repository.RutinaDetalleRepository;
import com.example.gymweb.Repository.RutinaRepository;
import com.example.gymweb.Repository.UsuarioRepository;
import com.example.gymweb.dto.Request.ActualizarRutinaRequest;
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
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class RutinaService {
    @Autowired
    private RutinaRepository rutinaRepository;
    @Autowired
    private RutinaDetalleRepository rutinaDetalleRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;

    private Usuario currentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof Usuario)) {
            throw new RuntimeException("No hay usuario autenticado");
        }
        return (Usuario) auth.getPrincipal();
    }

    public RutinaResponse crearRutina(RutinaRequest request) {
        Usuario creador = (Usuario)this.usuarioRepository.findById(request.getIdCreador()).orElseThrow(() -> new RuntimeException("Creador no encontrado"));
        Rutina rutina = new Rutina();
        rutina.setNombre(request.getNombre());
        rutina.setCreador(creador);
        rutina.setEsGlobal(Boolean.TRUE.equals(request.getEsGlobal()));
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
        response.setEsGlobal(rutina.isEsGlobal());
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
        response.setEsGlobal(rutina.isEsGlobal());
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

    public List<RutinaResponse> listarMias() {
        Usuario usuario = currentUser();
        return this.rutinaRepository.findByCreadorId(usuario.getId()).stream()
                .map(this::convertirAResponse)
                .toList();
    }

    public List<RutinaResponse> listarGlobales() {
        return this.rutinaRepository.findByEsGlobalTrue().stream()
                .map(this::convertirAResponse)
                .toList();
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

    public RutinaResponse actualizarRutina(Integer idRutina, ActualizarRutinaRequest request) {
        Rutina rutina = rutinaRepository.findById(idRutina)
                .orElseThrow(() -> new RuntimeException("Rutina no encontrada"));

        if (request.getNombre() != null && !request.getNombre().isBlank()) {
            rutina.setNombre(request.getNombre());
        }
        if (request.getEsGlobal() != null) {
            rutina.setEsGlobal(request.getEsGlobal());
        }

        RutinaDetalle detalle = rutina.getRutinaDetalle();
        if (detalle == null) {
            throw new RuntimeException("La rutina no posee detalle asociado.");
        }

        if (request.getDescripcion() != null) {
            detalle.setDescripcion(request.getDescripcion());
        }
        if (request.getImagen() != null) {
            detalle.setImagen(request.getImagen());
        }
        if (request.getDescanso_seg() != null) {
            detalle.setDescanso_seg(request.getDescanso_seg());
        }

        rutinaRepository.save(rutina);
        rutinaDetalleRepository.save(detalle);

        return convertirAResponse(rutina);
    }

    public RutinaResponse suscribirRutina(Integer idRutina) {
        Usuario usuario = currentUser();
        Rutina original = rutinaRepository.findById(idRutina)
                .orElseThrow(() -> new RuntimeException("Rutina no encontrada"));

        if (!original.isEsGlobal() && (original.getCreador() == null || !Objects.equals(original.getCreador().getId(), usuario.getId()))) {
            throw new RuntimeException("No puedes suscribirte a una rutina privada");
        }

        if (original.getCreador() != null && Objects.equals(original.getCreador().getId(), usuario.getId())) {
            return convertirAResponse(original);
        }

        Rutina clon = clonarRutina(original, usuario);
        return convertirAResponse(clon);
    }

    public RutinaResponse clonarParaUsuario(int idRutina, int idUsuario) {
        Usuario usuario = this.usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Rutina original = rutinaRepository.findById(idRutina)
                .orElseThrow(() -> new RuntimeException("Rutina no encontrada"));
        Rutina clon = clonarRutina(original, usuario);
        return convertirAResponse(clon);
    }

    private Rutina clonarRutina(Rutina original, Usuario nuevoCreador) {
        Rutina nueva = new Rutina();
        nueva.setNombre(original.getNombre());
        nueva.setCreador(nuevoCreador);
        nueva.setEsGlobal(false);
        rutinaRepository.save(nueva);

        RutinaDetalle detalleOriginal = original.getRutinaDetalle();
        if (detalleOriginal == null) {
            return nueva;
        }

        RutinaDetalle nuevoDetalle = new RutinaDetalle();
        nuevoDetalle.setRutina(nueva);
        nuevoDetalle.setDescripcion(detalleOriginal.getDescripcion());
        nuevoDetalle.setImagen(detalleOriginal.getImagen());
        nuevoDetalle.setDescanso_seg(detalleOriginal.getDescanso_seg());

        List<EjercicioDetalle> nuevosEjercicios = new ArrayList<>();
        if (detalleOriginal.getEjercicios() != null) {
            for (EjercicioDetalle ej : detalleOriginal.getEjercicios()) {
                EjercicioDetalle nuevoEj = new EjercicioDetalle();
                nuevoEj.setRutinaDetalle(nuevoDetalle);
                nuevoEj.setEjercicio(ej.getEjercicio());
                nuevoEj.setOrden(ej.getOrden());

                List<Serie> nuevasSeries = new ArrayList<>();
                if (ej.getSeries() != null) {
                    for (Serie s : ej.getSeries()) {
                        Serie ns = new Serie();
                        ns.setEjercicioDetalle(nuevoEj);
                        ns.setCarga(s.getCarga());
                        ns.setRepeticiones(s.getRepeticiones());
                        ns.setOrden(s.getOrden());
                        nuevasSeries.add(ns);
                    }
                }
                nuevoEj.setSeries(nuevasSeries);
                nuevosEjercicios.add(nuevoEj);
            }
        }

        nuevoDetalle.setEjercicios(nuevosEjercicios);
        rutinaDetalleRepository.save(nuevoDetalle);
        nueva.setRutinaDetalle(nuevoDetalle);
        rutinaRepository.save(nueva);
        return nueva;
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
