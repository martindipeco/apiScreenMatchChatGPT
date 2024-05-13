package com.aluracursos.screenmatch.service;

import com.aluracursos.screenmatch.dto.SerieDTO;
import com.aluracursos.screenmatch.model.Serie;
import com.aluracursos.screenmatch.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SerieService {

    @Autowired //inyecto dependencia
    private SerieRepository repository;
    public List<SerieDTO> obtenerTodasLasSeries()
    {
        return convierteDatos(repository.findAll());
    }

    public List<SerieDTO> obtenerTop5()
    {
        return convierteDatos(repository.findTop5ByOrderByEvaluacionDesc());
    }

    public List<SerieDTO> obtenerMasRecientes()
    {
        return convierteDatos(repository.lanzamientosMasRecientes());
    }

    public SerieDTO obtenerPorId(Long id)
    {
        Optional<Serie> serie = repository.findById(id);
        if (serie.isPresent())
        {
            Serie serieConfirmada = serie.get();
            return new SerieDTO(serieConfirmada.getId(), serieConfirmada.getTitulo(), serieConfirmada.getTotalTemporadas(),
                    serieConfirmada.getEvaluacion(), serieConfirmada.getPoster()
                    , serieConfirmada.getGenero(), serieConfirmada.getActores(), serieConfirmada.getSinopsis());
        }
        return null;
    }

    public List<SerieDTO> convierteDatos(List<Serie> serie)
    {
        return serie.stream()
                .map(s -> new SerieDTO(s.getId(), s.getTitulo(), s.getTotalTemporadas(), s.getEvaluacion(), s.getPoster()
                        , s.getGenero(), s.getActores(), s.getSinopsis())).collect(Collectors.toList());
    }

}
