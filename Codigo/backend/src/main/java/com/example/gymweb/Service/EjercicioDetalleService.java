package com.example.gymweb.Service;

import com.example.gymweb.Repository.EjercicioDetalleRepository;
import com.example.gymweb.Repository.EjercicioRepository;
import com.example.gymweb.Repository.RutinaDetalleRepository;
import com.example.gymweb.Repository.SerieRepository;
import com.example.gymweb.dto.Request.EjercicioDetalleRequest;
import com.example.gymweb.dto.Request.SerieRequest;
import com.example.gymweb.dto.Response.EjercicioDetalleResponse;
import com.example.gymweb.dto.Response.SerieResponse;
import com.example.gymweb.model.Ejercicio;
import com.example.gymweb.model.EjercicioDetalle;
import com.example.gymweb.model.RutinaDetalle;
import com.example.gymweb.model.Serie;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EjercicioDetalleService {
    @Autowired
    private EjercicioDetalleRepository ejercicioDetalleRepository;
    @Autowired
    private EjercicioRepository ejercicioRepository;
    @Autowired
    private RutinaDetalleRepository rutinaDetalleRepository;
    @Autowired
    private SerieRepository serieRepository;

    public EjercicioDetalleService(EjercicioDetalleRepository ejercicioDetalleRepository, EjercicioRepository ejercicioRepository, RutinaDetalleRepository rutinaDetalleRepository, SerieRepository serieRepository) {
        this.ejercicioDetalleRepository = ejercicioDetalleRepository;
        this.ejercicioRepository = ejercicioRepository;
        this.rutinaDetalleRepository = rutinaDetalleRepository;
        this.serieRepository = serieRepository;
    }

    public EjercicioDetalleResponse convertirARespose(EjercicioDetalle detalle) {
        EjercicioDetalleResponse response = new EjercicioDetalleResponse();
        response.setEjercicio(detalle.getEjercicio().getNombre());
        response.setId(detalle.getId());
        List<SerieResponse> seriesResponse = new ArrayList();

        for(Serie s : detalle.getSeries()) {
            SerieResponse serieResponse = new SerieResponse();
            serieResponse.setCarga(s.getCarga());
            serieResponse.setRepeticiones(s.getRepeticiones());
            serieResponse.setOrden(s.getOrden());
            seriesResponse.add(serieResponse);
        }

        response.setSeries(seriesResponse);
        return response;
    }

    public EjercicioDetalleResponse crearEjercicioDetalle(EjercicioDetalleRequest request) {
        EjercicioDetalle ejercicioDetalle = new EjercicioDetalle();
        Ejercicio ejercicio = (Ejercicio)this.ejercicioRepository.findById(request.getIdEjercicio()).orElseThrow(() -> new RuntimeException("El ejercicio no fue encontrado"));
        RutinaDetalle rutinaDetalle = (RutinaDetalle)this.rutinaDetalleRepository.findById(request.getIdRutinaDetalle()).orElseThrow(() -> new RuntimeException("La rutina no fue encontrada"));
        ejercicioDetalle.setEjercicio(ejercicio);
        ejercicioDetalle.setOrden(request.getOrden());
        ejercicioDetalle.setRutinaDetalle(rutinaDetalle);
        List<Serie> series = new ArrayList();

        for(SerieRequest s : request.getSeries()) {
            Serie serie = new Serie();
            serie.setEjercicioDetalle(ejercicioDetalle);
            serie.setCarga(s.getCarga());
            serie.setOrden(s.getOrden());
            serie.setRepeticiones(s.getRepeticiones());
            series.add(serie);
        }

        ejercicioDetalle.setSeries(series);
        this.ejercicioDetalleRepository.save(ejercicioDetalle);
        return this.convertirARespose(ejercicioDetalle);
    }

    public void eliminarEjercicioDetalle(int id) {
        this.ejercicioDetalleRepository.deleteById(id);
    }

    public EjercicioDetalleResponse agregarSerie(int idDetalle, SerieRequest request) {
        EjercicioDetalle detalle = (EjercicioDetalle)this.ejercicioDetalleRepository.findById(idDetalle).orElseThrow(() -> new RuntimeException("El detalle no fue encontrado"));
        Serie serie = new Serie();
        serie.setRepeticiones(request.getRepeticiones());
        serie.setOrden(request.getOrden());
        serie.setCarga(request.getCarga());
        serie.setEjercicioDetalle(detalle);
        detalle.getSeries().add(serie);
        this.ejercicioDetalleRepository.save(detalle);
        return this.convertirARespose(detalle);
    }

    public SerieResponse modificarSerie(Integer idSerie, SerieRequest request) {
        Serie serie = (Serie)this.serieRepository.findById(idSerie).orElseThrow(() -> new RuntimeException("Serie no encontrada"));
        serie.setRepeticiones(request.getRepeticiones());
        serie.setCarga(request.getCarga());
        serie.setOrden(request.getOrden());
        this.serieRepository.save(serie);
        SerieResponse response = new SerieResponse();
        response.setOrden(serie.getOrden());
        response.setCarga(serie.getCarga());
        response.setRepeticiones(serie.getRepeticiones());
        response.setId(serie.getId());
        return response;
    }

    public void eliminarSerie(int idSerie) {
        Serie serie = serieRepository.findById(idSerie)
                .orElseThrow(() -> new RuntimeException("Serie no encontrada"));

        serieRepository.delete(serie);
    }

    public EjercicioDetalleResponse modificarOrden(int idDetalle, int nuevoOrden) {

        EjercicioDetalle detalle = ejercicioDetalleRepository.findById(idDetalle)
                .orElseThrow(() -> new RuntimeException("Detalle no encontrado"));

        // ⚠ Se obtiene la RutinaDetalle a donde pertenece
        RutinaDetalle rutinaDetalle = detalle.getRutinaDetalle();

        // ⚠ Traemos TODOS los ejercicios ordenados actuales
        List<EjercicioDetalle> ejercicios = rutinaDetalle.getEjercicios();

        // ⚠ Orden previo del ejercicio que vamos a mover
        int ordenAnterior = detalle.getOrden();

        if (nuevoOrden < 1 || nuevoOrden > ejercicios.size()) {
            throw new RuntimeException("Nuevo orden fuera de rango");
        }

        // ===== REORDENAR LISTA =====
        if (nuevoOrden < ordenAnterior) {
            // Subió → otros bajan
            for (EjercicioDetalle e : ejercicios) {
                if (e.getOrden() >= nuevoOrden && e.getOrden() < ordenAnterior) {
                    e.setOrden(e.getOrden() + 1);
                }
            }
        } else if (nuevoOrden > ordenAnterior) {
            // Bajó → otros suben
            for (EjercicioDetalle e : ejercicios) {
                if (e.getOrden() <= nuevoOrden && e.getOrden() > ordenAnterior) {
                    e.setOrden(e.getOrden() - 1);
                }
            }
        }

        // Finalmente actualizamos
        detalle.setOrden(nuevoOrden);

        ejercicioDetalleRepository.saveAll(ejercicios);

        return convertirARespose(detalle);
    }
}
