package com.aluracursos.screenmatch.repository;

import com.aluracursos.screenmatch.model.Categoria;
import com.aluracursos.screenmatch.model.Episodio;
import com.aluracursos.screenmatch.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SerieRepository extends JpaRepository<Serie, Long> {

    Optional<Serie> findByTituloContainsIgnoreCase(String nombreSerie);

    List<Serie> findTop5ByOrderByEvaluacionDesc();
    List<Serie> findByGenero(Categoria cate);

    List<Serie> findByTotalTemporadasLessThanEqualAndEvaluacionGreaterThanEqual(int totalTemporadas, double evaluacion);

    //anotación para indicar query sql nativa
    @Query (value = "SELECT * FROM series WHERE series.total_temporadas <= 6 AND series.evaluacion >= 7.5", nativeQuery = true)
    List<Serie> seriesPorTemporadaEvaluacionNativa();

    //anotacion JPQL
    @Query (value = "SELECT s FROM Serie s WHERE s.totalTemporadas <= :totalTemporadas AND s.evaluacion >= :evaluacion")
    List<Serie> seriesPorTemporadaEvaluacionJPQL(int totalTemporadas, Double evaluacion);

    @Query("SELECT e FROM Serie s JOIN s.listaEpisodios e WHERE e.titulo ILIKE %:nombreEpisodio%")
    List<Episodio> episodiosPorNombre (String nombreEpisodio);

    @Query("SELECT e FROM Serie s JOIN s.listaEpisodios e WHERE s = :serie ORDER BY e.evaluacion DESC LIMIT 5")
    List<Episodio> top5episodios(Serie serie);

    @Query("SELECT s from Serie s JOIN s.listaEpisodios e GROUP BY s ORDER BY MAX(e.fechaDeLanzamiento) DESC LIMIT 5")
    List<Serie> lanzamientosMasRecientes();
}
