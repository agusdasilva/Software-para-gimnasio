package com.example.gymweb.service;

import com.example.gymweb.dto.Request.RutinaRequest;
import com.example.gymweb.dto.Response.RutinaDetalleResponse;
import com.example.gymweb.dto.Response.RutinaResponse;
import com.example.gymweb.model.Rutina;
import com.example.gymweb.model.RutinaDetalle;
import com.example.gymweb.model.Usuario;
import com.example.gymweb.repository.RutinaDetalleRepository;
import com.example.gymweb.repository.RutinaRepository;
import com.example.gymweb.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RutinaService {

    @Autowired
    private RutinaRepository rutinaRepository;

    @Autowired
    private RutinaDetalleRepository rutinaDetalleRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public RutinaResponse crearRutina(RutinaRequest request) {

        Usuario creador = usuarioRepository.findById(request.getIdCreador())
                .orElseThrow(() -> new RuntimeException("Creador no encontrado"));

        Rutina rutina = new Rutina();
        rutina.setNombre(request.getNombre());
        rutina.setCreador(creador);

        rutinaRepository.save(rutina);

        RutinaDetalle detalle = new RutinaDetalle();
        detalle.setRutina(rutina);
        detalle.setDescripcion(request.getDescripcion());
        detalle.setImagen(request.getImagen());
        detalle.setDescanso_seg(request.getDescanso_seg());
        detalle.setEjercicios(new ArrayList<>()); // lista VACÍA

        rutinaDetalleRepository.save(detalle);

        rutina.setRutinaDetalle(detalle);
        rutinaRepository.save(rutina);

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
        det.setEjercicios(new ArrayList<>());

        response.setDetalle(det);

        return response;
    }

    public void elimninarPorId(int idRutina)
    {
        rutinaRepository.deleteById(idRutina);
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
        detRes.setEjercicios(new ArrayList<>()); // Luego vos lo llenás cuando agregues ejerciciosDetalle

        response.setDetalle(detRes);

        return response;
    }

    public RutinaResponse buscarPorId(Integer id) {
        Rutina rutina = rutinaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rutina no encontrada"));

        return convertirAResponse(rutina);
    }

    public RutinaResponse buscarPorNombre(String nombre) {

        Rutina rutina = rutinaRepository.findByNombreIgnoreCase(nombre)
                .orElseThrow(() -> new RuntimeException("No existe una rutina con ese nombre"));


        return convertirAResponse(rutina);
    }

    public RutinaResponse modificarDescanso(Integer idRutina, int nuevoDescanso) {

        Rutina rutina = rutinaRepository.findById(idRutina)
                .orElseThrow(() -> new RuntimeException("Rutina no encontrada"));

        RutinaDetalle detalle = rutina.getRutinaDetalle();
        if (detalle == null) {
            throw new RuntimeException("La rutina no tiene detalle asociado");
        }

        detalle.setDescanso_seg(nuevoDescanso);
        rutinaDetalleRepository.save(detalle);

        return convertirAResponse(rutina);
    }

    public List<RutinaResponse> listarTodas() {
        return rutinaRepository.findAll()
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }
}