package com.example.gymweb.service;

import com.example.gymweb.dto.Request.EjercicioDetalleRequest;
import com.example.gymweb.dto.Request.SerieRequest;
import com.example.gymweb.dto.Response.EjercicioDetalleResponse;
import com.example.gymweb.dto.Response.SerieResponse;
import com.example.gymweb.model.Ejercicio;
import com.example.gymweb.model.EjercicioDetalle;
import com.example.gymweb.model.RutinaDetalle;
import com.example.gymweb.model.Serie;
import com.example.gymweb.repository.EjercicioDetalleRepository;
import com.example.gymweb.repository.EjercicioRepository;
import com.example.gymweb.repository.RutinaDetalleRepository;
import com.example.gymweb.repository.SerieRepository;
import com.fasterxml.jackson.databind.annotation.JsonAppend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.print.DocFlavor;
import java.util.ArrayList;
import java.util.List;

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

    public EjercicioDetalleResponse convertirARespose(EjercicioDetalle detalle)
    {
        EjercicioDetalleResponse response = new EjercicioDetalleResponse();

        response.setEjercicio(detalle.getEjercicio().getNombre());
        response.setId(detalle.getId());

        List<SerieResponse> seriesResponse = new ArrayList<>();
        for(Serie s : detalle.getSeries())
        {
            SerieResponse serieResponse = new SerieResponse();
            serieResponse.setCarga(s.getCarga());
            serieResponse.setRepeticiones(s.getRepeticiones());
            serieResponse.setOrden(s.getOrden());

            seriesResponse.add(serieResponse);
        }

        response.setSeries(seriesResponse);
        return response;
    }

    public EjercicioDetalleResponse crearEjercicioDetalle(EjercicioDetalleRequest request)
    {
        EjercicioDetalle ejercicioDetalle = new EjercicioDetalle();

        Ejercicio ejercicio = ejercicioRepository.findById(request.getIdEjercicio())
                .orElseThrow( () -> new RuntimeException("El ejercicio no fue encontrado") );
        RutinaDetalle rutinaDetalle = rutinaDetalleRepository.findById(request.getIdRutinaDetalle())
                .orElseThrow( () -> new RuntimeException("La rutina no fue encontrada") );
        ejercicioDetalle.setEjercicio(ejercicio);
        ejercicioDetalle.setOrden(request.getOrden());
        ejercicioDetalle.setRutinaDetalle(rutinaDetalle);

        List<Serie> series = new ArrayList<>();
        for(SerieRequest s : request.getSeries())
        {
            Serie serie = new Serie();

            serie.setEjercicioDetalle(ejercicioDetalle);
            serie.setCarga(s.getCarga());
            serie.setOrden(s.getOrden());
            serie.setRepeticiones(s.getRepeticiones());

            series.add(serie);
        }
        ejercicioDetalle.setSeries(series);
        ejercicioDetalleRepository.save(ejercicioDetalle);

        return convertirARespose(ejercicioDetalle);
    }

    public void eliminarEjercicioDetalle(int id)
    {
        ejercicioDetalleRepository.deleteById(id);
    }

    public EjercicioDetalleResponse agregarSerie (int idDetalle, SerieRequest request)
    {
        EjercicioDetalle detalle = ejercicioDetalleRepository.findById(idDetalle)
                .orElseThrow( () -> new RuntimeException("El detalle no fue encontrado") );

        Serie serie = new Serie();
        serie.setRepeticiones(request.getRepeticiones());
        serie.setOrden(request.getOrden());
        serie.setCarga(request.getCarga());
        serie.setEjercicioDetalle(detalle);

        detalle.getSeries().add(serie);
        ejercicioDetalleRepository.save(detalle);

        return convertirARespose(detalle);
    }

    public SerieResponse modificarSerie(Integer idSerie, SerieRequest request) {
        Serie serie = serieRepository.findById(idSerie)
                .orElseThrow(() -> new RuntimeException("Serie no encontrada"));

        serie.setRepeticiones(request.getRepeticiones());
        serie.setCarga(request.getCarga());
        serie.setOrden(request.getOrden());

        serieRepository.save(serie);

        SerieResponse response = new SerieResponse();

        response.setOrden(serie.getOrden());
        response.setCarga(serie.getCarga());
        response.setRepeticiones(serie.getRepeticiones());
        response.setId(serie.getId());

        return  response;
    }
}
