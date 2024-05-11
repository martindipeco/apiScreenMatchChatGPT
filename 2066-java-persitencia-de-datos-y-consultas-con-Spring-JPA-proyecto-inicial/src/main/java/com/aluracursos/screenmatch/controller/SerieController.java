package com.aluracursos.screenmatch.controller;

import com.aluracursos.screenmatch.dto.SerieDTO;
import com.aluracursos.screenmatch.model.Serie;
import com.aluracursos.screenmatch.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class SerieController {

    @Autowired //inyecto dependencia
    private SerieRepository repository;
    @GetMapping("/series")
    public List<SerieDTO> obtenerTodasLasSeries()
    {

        return repository.findAll().stream()
                .map(s -> new SerieDTO(s.getTitulo(), s.getTotalTemporadas(), s.getEvaluacion(), s.getPoster()
                , s.getGenero(), s.getActores(), s.getSinopsis())).collect(Collectors.toList());
    }

    @GetMapping("/inicio")
    public String muestraMensaje()
    {
        return "Probando Live Reload a ver si cambia de verdad";
    }
//    public String mostrarMensaje()
//    {
//        return "Hola mundo Spring";
//    }
}
