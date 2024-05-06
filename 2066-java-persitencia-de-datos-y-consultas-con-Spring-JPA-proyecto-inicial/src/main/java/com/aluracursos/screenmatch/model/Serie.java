package com.aluracursos.screenmatch.model;

import com.aluracursos.screenmatch.service.ConsultaChatGPT;
import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.persistence.*;

import java.util.List;
import java.util.OptionalDouble;
@Entity
@Table(name = "series") //polémico. esta línea existe solo porque no quiero que mi tabla se llame "serie" en singular
public class Serie {
    @Id //aviso que el atributo que está abajo será la primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) //para que sea autoincremental
    private Long id; //agrego este atributo para poder tener el "id" en la tabla - pero ¿no debería ser id_serie?
    @Column(unique = true) // por default es false, aviso que quiero que no haya títulos repetidos
    private String titulo;
    private Integer totalTemporadas;
    private Double evaluacion;
    private String poster;
    @Enumerated(EnumType.STRING) //debo avisar que tengo enum, y que cada value lo traiga como string
    private Categoria genero;
    private String actores;
    private String sinopsis;
    @OneToMany(mappedBy = "serie") //serie se refiere al atributo en la clase Episodio
    private List<Episodio> listaEpisodios;

    public Serie(){}
    public Serie(DatosSerie datosSerie)
    {
        this.titulo = datosSerie.titulo();
        this.totalTemporadas = datosSerie.totalTemporadas();
        this.evaluacion = OptionalDouble.of(Double.valueOf(datosSerie.evaluacion())).orElse(0);
        this.poster = datosSerie.poster();
        this.genero = Categoria.fromString(datosSerie.genero().split(",")[0].trim());
        this.actores = datosSerie.actores();
        this.sinopsis = datosSerie.sinopsis(); //ConsultaChatGPT.obtenerTraduccion(datosSerie.sinopsis());
    }

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}
    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getTotalTemporadas() {
        return totalTemporadas;
    }

    public void setTotalTemporadas(Integer totalTemporadas) {
        this.totalTemporadas = totalTemporadas;
    }

    public Double getEvaluacion() {
        return evaluacion;
    }

    public void setEvaluacion(Double evaluacion) {
        this.evaluacion = evaluacion;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public Categoria getGenero() {
        return genero;
    }

    public void setGenero(Categoria genero) {
        this.genero = genero;
    }

    public String getActores() {
        return actores;
    }

    public void setActores(String actores) {
        this.actores = actores;
    }

    public String getSinopsis() {
        return sinopsis;
    }

    public void setSinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
    }

    @Override
    public String toString() {
        return "Serie{" +
                "Titulo='" + titulo + '\'' +
                ", Total de Temporadas=" + totalTemporadas +
                ", Evaluación=" + evaluacion +
                ", Poster='" + poster + '\'' +
                ", Género=" + genero +
                ", Actores='" + actores + '\'' +
                ", Sinopsis='" + sinopsis + '\'' +
                '}';
    }
}
