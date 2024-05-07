package com.aluracursos.screenmatch.principal;

import ch.qos.logback.core.encoder.JsonEscapeUtil;
import com.aluracursos.screenmatch.model.*;
import com.aluracursos.screenmatch.repository.SerieRepository;
import com.aluracursos.screenmatch.service.ApiKey;
import com.aluracursos.screenmatch.service.ConsumoAPI;
import com.aluracursos.screenmatch.service.ConvierteDatos;

import javax.swing.text.html.Option;
import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoApi = new ConsumoAPI();
    private final String URL_BASE = "https://www.omdbapi.com/?t=";
    private final String API_KEY_OMDB = ApiKey.getApiKeyOmdb();
    private final String API_KEY_CHATGPT = ApiKey.getApiKeyChatGpt();
    private ConvierteDatos conversor = new ConvierteDatos();
    private List<DatosSerie> listaDatosSerie = new ArrayList<>();
    private SerieRepository repositorio;
    private List<Serie> listaSeries = new ArrayList<>();

    public Principal(SerieRepository serieRepository) {
        this.repositorio = serieRepository;
    }

    public void muestraElMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    1 - Buscar series (nueva consulta via API)
                    2 - Buscar episodios
                    3 - Mostrar series buscadas
                    4 - Buscar serie por título (ya guardadas en DB)
                    5 - Top 5 de series en DB
                    6 - Buscar por categoría
                    7 - Filtrar series por temporada y evaluación
                                  
                    0 - Salir
                    """;
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion) {
                case 1:
                    buscarSerieWeb();
                    break;
                case 2:
                    buscarEpisodioPorSerie();
                    break;
                case 3:
                    mostrarSeriesBuscadas();
                    break;
                case 4:
                    buscarSeriesPorTitulo();
                    break;
                case 5:
                    listarTop5();
                    break;
                case 6:
                    buscarSeriesPorCategoria();
                    break;
                case 7:
                    filtrarSeriesPorTemporadaYEvaluacion();
                    break;
                case 0:
                    System.out.println("Cerrando la aplicación...");
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        }

    }

    private DatosSerie getDatosSerie() {
        System.out.println("Escribe el nombre de la serie que deseas buscar");
        var nombreSerie = teclado.nextLine();
        var json = consumoApi.obtenerDatos(URL_BASE + nombreSerie.replace(" ", "+") + "&apikey=" + API_KEY_OMDB);
        System.out.println(json);
        DatosSerie datos = conversor.obtenerDatos(json, DatosSerie.class);
        return datos;
    }
    private void buscarEpisodioPorSerie() {
        //DatosSerie datosSerie = getDatosSerie();
        mostrarSeriesBuscadas();
        System.out.println("Ingrese nombre de la serie de la cual desea buscar episodios");
        var nombreSerie = teclado.nextLine();

        Optional<Serie> serie = listaSeries.stream()
                .filter(s -> s.getTitulo().toLowerCase().contains(nombreSerie.toLowerCase()))
                .findFirst();

        if(serie.isPresent())
        {
            var serieEncontrada = serie.get();

            List<DatosTemporadas> temporadas = new ArrayList<>();

            for (int i = 1; i <= serieEncontrada.getTotalTemporadas(); i++) {
                var json = consumoApi.obtenerDatos(URL_BASE + serieEncontrada.getTitulo().replace(" ", "+") + "&season=" + i + "&apikey=" + API_KEY_OMDB);
                DatosTemporadas datosTemporada = conversor.obtenerDatos(json, DatosTemporadas.class);
                temporadas.add(datosTemporada);
            }
            temporadas.forEach(System.out::println);

            List<Episodio> episodios = temporadas.stream()
                    .flatMap(d -> d.episodios().stream()
                            .map(e -> new Episodio(d.numero(), e)))
                    .collect(Collectors.toList());
            serieEncontrada.setEpisodios(episodios);
            repositorio.save(serieEncontrada);
        }

    }
    private void buscarSerieWeb() {
        DatosSerie datos = getDatosSerie();
        Serie serie = new Serie(datos);
        repositorio.save(serie);
        //listaDatosSerie.add(datos);
        System.out.println(datos);
    }

    private void mostrarSeriesBuscadas()
    {
        //método anterior
        //List<Serie> listaSeries = new ArrayList<>();
        //listaSeries = listaDatosSerie.stream().map(d -> new Serie(d)).collect(Collectors.toList());
        listaSeries = repositorio.findAll();
        //mostrar agrupados por genero
        listaSeries.stream().sorted(Comparator.comparing(Serie::getGenero)).forEach(System.out::println);
    }

    private void buscarSeriesPorTitulo()
    {
        System.out.println("Ingrese nombre de la serie");
        var nombreSerie = teclado.nextLine();
        //contrastar input via derived query
        Optional<Serie> serieBuscada = repositorio.findByTituloContainsIgnoreCase(nombreSerie);
        //si hay match, agregar a series buscadas
        if(serieBuscada.isPresent())
        {
            System.out.println("Se encontró la serie " + serieBuscada.get());
        }
        else
        {
            System.out.println("No se encontró ninguna serie con ese nombre");
        }
    }

    private void listarTop5()
    {
        List<Serie> top5series = repositorio.findTop5ByOrderByEvaluacionDesc();
        top5series.forEach(s -> System.out.println("Serie: " + s.getTitulo() + ". Evaluación: " + s.getEvaluacion()));
    }

    private void buscarSeriesPorCategoria()
    {
        //ingresar categoria, haciendo un foreach del enum?
        System.out.println("Ingrese el género o categoría de serie");
        var genero = teclado.nextLine();
        var categoria = Categoria.fromCastellano(genero);
        List<Serie> seriesPorCategoria = repositorio.findByGenero(categoria);
        System.out.println("Series de " + categoria);
        seriesPorCategoria.forEach(System.out::println);
    }

    private void filtrarSeriesPorTemporadaYEvaluacion()
    {
        System.out.println("¿Filtrar series con cuántas temporadas? ");
        var totalTemporadas = teclado.nextInt();
        teclado.nextLine();
        System.out.println("¿A partir de que evaluación? ");
        var evaluacion = teclado.nextDouble();
        teclado.nextLine();
        List<Serie> filtroSeries = repositorio.findByTotalTemporadasLessThanEqualAndEvaluacionGreaterThanEqual(totalTemporadas, evaluacion);
        System.out.println("*** Series filtradas ***");
        filtroSeries.forEach(s ->
                System.out.println(s.getTitulo() + "  - evaluacion: " + s.getEvaluacion()));
    }
}

