package com.aluracursos.screenmatch.controller;

import com.aluracursos.screenmatch.dto.SerieDTO;
import com.aluracursos.screenmatch.model.Serie;
import com.aluracursos.screenmatch.repository.SerieRepository;
import com.aluracursos.screenmatch.service.SerieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/series")
public class SerieController {

    @Autowired
    private SerieService servicio;

    @GetMapping()
    public List<SerieDTO> obtenerTodasLasSeries()
    {
        return servicio.obtenerTodasLasSeries();
    }

    @GetMapping("/top5")
    public List<SerieDTO> obtenerTop5()
    {
        return servicio.obtenerTop5();
    }

    @GetMapping("/lanzamientos")
    public List<SerieDTO> obtenerMasRecientes()
    {
        return servicio.obtenerMasRecientes();
    }


//    @GetMapping("/series")
//    public List<SerieDTO> obtenerTodasLasSeries()
//    {
//        return repository.findAll().stream()
//                .map(s -> new SerieDTO(s.getTitulo(), s.getTotalTemporadas(), s.getEvaluacion(), s.getPoster()
//                , s.getGenero(), s.getActores(), s.getSinopsis())).collect(Collectors.toList());
//    }

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
