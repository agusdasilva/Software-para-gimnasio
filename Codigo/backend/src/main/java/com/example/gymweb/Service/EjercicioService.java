package com.example.gymweb.Service;

import com.example.gymweb.Repository.EjercicioRepository;
import com.example.gymweb.dto.Request.EjercicioRequest;
import com.example.gymweb.dto.Response.EjercicioResponse;
import com.example.gymweb.model.Ejercicio;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EjercicioService {
    @Autowired
    private EjercicioRepository ejercicioRepository;

    public EjercicioService(EjercicioRepository ejercicioRepository) {
        this.ejercicioRepository = ejercicioRepository;
    }

    public EjercicioResponse convertirAResponse(Ejercicio ejercio) {
        EjercicioResponse response = new EjercicioResponse();
        response.setEquipamiento(ejercio.getEquipamiento());
        response.setId(ejercio.getId());
        response.setEs_global(ejercio.getEsGlobal());
        response.setGrupo_muscular(ejercio.getGrupoMuscular());
        response.setNombre(ejercio.getNombre());
        return response;
    }

    public EjercicioResponse crearEjercicio(EjercicioRequest request) {
        Ejercicio ejercicio = new Ejercicio();
        ejercicio.setEquipamiento(request.getEquipamiento());
        ejercicio.setEsGlobal(request.getEs_global());
        ejercicio.setNombre(request.getNombre());
        ejercicio.setGrupoMuscular(request.getGrupo_muscular());
        this.ejercicioRepository.save(ejercicio);
        return this.convertirAResponse(ejercicio);
    }

    public EjercicioResponse buscarPorId(int id) {
        return this.convertirAResponse((Ejercicio)this.ejercicioRepository.findById(id).orElseThrow(() -> new RuntimeException("El ejercicio no fue encontrado")));
    }

    public EjercicioResponse buscarPorNombre(String nombre) {
        Ejercicio ejercicio = (Ejercicio)this.ejercicioRepository.findByNombreIgnoreCase(nombre).orElseThrow(() -> new RuntimeException("El ejercicio no fue encontrado"));
        return this.convertirAResponse(ejercicio);
    }

    public void eliminarEjercicio(int id) {
        this.ejercicioRepository.deleteById(id);
    }

    public List<EjercicioResponse> listar() {
        return this.ejercicioRepository.findAll().stream().map((p) -> this.convertirAResponse(p)).toList();
    }

    public List<EjercicioResponse> listarPorContenerNombre(String nombre) {
        List<Ejercicio> ejercicios = (List)this.ejercicioRepository.findAllByNombreContainingIgnoreCase(nombre).orElseThrow(() -> new RuntimeException("No se encontraron ejercicios para ese nombre"));
        return ejercicios.stream().map((p) -> this.convertirAResponse(p)).toList();
    }

    public List<EjercicioResponse> listarPorgrupo_muscular(String grupo_muscular) {
        List<Ejercicio> ejercicios = (List)this.ejercicioRepository.findAllByGrupoMuscularIgnoreCase(grupo_muscular).orElseThrow(() -> new RuntimeException("No se encontraron ejercicios para el grupo muscular"));
        return ejercicios.stream().map((p) -> this.convertirAResponse(p)).toList();
    }
}

