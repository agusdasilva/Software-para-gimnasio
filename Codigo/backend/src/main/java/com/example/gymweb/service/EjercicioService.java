package com.example.gymweb.service;

import com.example.gymweb.dto.Request.EjercicioRequest;
import com.example.gymweb.dto.Response.EjercicioResponse;
import com.example.gymweb.model.Ejercicio;
import com.example.gymweb.repository.EjercicioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EjercicioService {

    @Autowired
    private EjercicioRepository ejercicioRepository;

    public EjercicioService(EjercicioRepository ejercicioRepository) {
        this.ejercicioRepository = ejercicioRepository;
    }

    public EjercicioResponse convertirAResponse(Ejercicio ejercio)
    {
        EjercicioResponse response = new EjercicioResponse();

        response.setEquipamiento(ejercio.getEquipamiento());
        response.setId(ejercio.getId());
        response.setEs_global(ejercio.getEsGlobal());
        response.setGrupo_muscular(ejercio.getGrupoMuscular());
        response.setNombre(ejercio.getNombre());

        return response;
    }

    public EjercicioResponse crearEjercicio(EjercicioRequest request)
    {
        Ejercicio ejercicio = new Ejercicio();

        ejercicio.setEquipamiento(request.getEquipamiento());
        ejercicio.setEsGlobal(request.getEs_global());
        ejercicio.setNombre(request.getNombre());
        ejercicio.setGrupoMuscular(request.getGrupo_muscular());

        ejercicioRepository.save(ejercicio);

        return convertirAResponse(ejercicio);
    }

    public EjercicioResponse buscarPorId(int id)
    {
        return convertirAResponse(ejercicioRepository.findById(id).orElseThrow(
                () -> new RuntimeException("El ejercicio no fue encontrado")));
    }

    public EjercicioResponse buscarPorNombre(String nombre)
    {
            Ejercicio ejercicio = ejercicioRepository.findByNombreIgnoreCase(nombre).orElseThrow( () -> new RuntimeException("El ejercicio no fue encontrado"));

            return convertirAResponse(ejercicio);

    }


    public void eliminarEjercicio(int id)
    {
        ejercicioRepository.deleteById(id);
    }

    public List<EjercicioResponse> listar()
    {
        return ejercicioRepository.findAll().stream().map( p -> {
            return convertirAResponse(p);
        }).toList();
    }

    public List<EjercicioResponse> listarPorContenerNombre(String nombre)
    {
        List<Ejercicio> ejercicios = ejercicioRepository.findAllByNombreContainingIgnoreCase(nombre)
                .orElseThrow( () -> new RuntimeException("No se encontraron ejercicios para ese nombre"));

        return ejercicios.stream().map( p -> {
            return convertirAResponse(p);
        }).toList();

    }

    public List<EjercicioResponse> listarPorgrupo_muscular(String grupo_muscular)
    {
        List<Ejercicio> ejercicios = ejercicioRepository.findAllByGrupoMuscularIgnoreCase(grupo_muscular)
                .orElseThrow( () -> new RuntimeException("No se encontraron ejercicios para el grupo muscular"));

        return ejercicios.stream().map( p -> {
            return convertirAResponse(p);
        }).toList();
    }


}
