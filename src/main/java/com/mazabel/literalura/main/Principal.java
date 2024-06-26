package com.mazabel.literalura.main;

import ch.qos.logback.core.encoder.JsonEscapeUtil;
import com.mazabel.literalura.entities.Autor;
import com.mazabel.literalura.entities.Libro;
import com.mazabel.literalura.model.Datos;
import com.mazabel.literalura.model.DatosAutor;
import com.mazabel.literalura.model.DatosLibro;
import com.mazabel.literalura.repository.AutorRepositorio;
import com.mazabel.literalura.repository.LibroRepository;
import com.mazabel.literalura.service.ConsumoAPI;
import com.mazabel.literalura.service.ConvertirDatos;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {

    private static final String URL_BASE = "https://gutendex.com/books/";
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConvertirDatos conversor = new ConvertirDatos();
    private Scanner scan = new Scanner(System.in);

    private List<DatosLibro> librosBuscados = new ArrayList<DatosLibro>();
    private List<DatosAutor> autoresBuscados = new ArrayList<DatosAutor>();
    private LibroRepository repositorioLibro;
    private AutorRepositorio repositorioAutor;

    public Principal(LibroRepository repository, AutorRepositorio repositoryAutor) {
        this.repositorioLibro = repository;
        this.repositorioAutor = repositoryAutor;
    }

    public void mostarMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    
                    1 - Buscar libro por titulo en la Web
                    2 - Buscar libro por titulo en libros buscados
                    3 - Mostrar todos los libros buscados
                    4 - Mostrar todos los autores de los libros buscados
                    5 - Buscar en la web autores vivos en determinado tiempo
                    6 - Mostrar libros por idioma
                                  
                    0 - Salir
                    """;
            System.out.println(menu);
            opcion = scan.nextInt();
            scan.nextLine();

            switch (opcion) {
                case 1:
                    buscarLibroPorNombre();
                    break;
                case 2:
                    buscarTitulo();
                    break;
                case 3:
                    mostrarLibrosGuardados();
                    break;
                case 4:
                    mostrarListaAutoresDeLibrosBuscados();
                    break;
                case 5:
                    buscarAutoresVivos();
                    break;
                case 6:
                    mostrarLibrosIdioma();
                    break;
                case 0:
                    System.out.println("¡Hasta luego!\n");
                    break;
                default:
                    System.out.println("Opción inválida\n");
            }
        }

    }

    private String busquedaAPI(String data) {
        return consumoAPI.obtenerDatos(URL_BASE + "?search=" + data.replace(" ", "+"));
    }
    private String busquedaAPIAñosAutores(int añoInicio, int añoFinal) {
        return consumoAPI.obtenerDatos(URL_BASE + "?author_year_start="+añoInicio+"&author_year_end="+añoFinal);
    }

    public void buscarLibroPorNombre() {
        System.out.println("Ingresa el nombre del Libro:");
        var tituloLibro = scan.nextLine();
        String resultado = busquedaAPI(tituloLibro);
        var busqueda = conversor.obtenerDatos(resultado, Datos.class);
        Optional<DatosLibro> libroBuscado = busqueda.resultados().stream()
                .filter(datosLibro -> datosLibro.titulo().toUpperCase().contains(tituloLibro.toUpperCase()))
                .findFirst();

        if (libroBuscado.isPresent()) {
            System.out.println("Se encontró el siguiente Libro: ");
            DatosLibro datosLibro = libroBuscado.get();
            DatosAutor primerAutor = datosLibro.datosAutor().get(0);
            Autor autor = new Autor(primerAutor);
            repositorioAutor.save(autor);
            Libro libro = new Libro(datosLibro, autor);
            repositorioLibro.save(libro);

        } else {
            System.out.println("No se encontró el Libro");
        }

    }

    public void mostrarLibrosGuardados(){
        System.out.println("Lista de libros guardados: ");
        List<Libro> listaLibros = repositorioLibro.findAll();
        listaLibros.forEach(System.out::println);
    }
    public void mostrarListaAutoresDeLibrosBuscados(){
        System.out.println("Lista de autores de los libros buscados: ");
        List<Autor> listaAutores = repositorioAutor.findAll();
        listaAutores.forEach(System.out::println);
    }


    private void buscarAutoresVivos() {
        System.out.println("Ingresa el rango de años entre los que deseas buscar");
        System.out.print("Año de inicio: ");
        var anhoInicio = Integer.valueOf(scan.nextLine());
        System.out.print("Año de fin: ");
        var anhoFinal = Integer.valueOf(scan.nextLine());
        System.out.println("Buscando autores vivos entre los años "+anhoInicio+ " y "+ anhoFinal);
        String resultado = busquedaAPIAñosAutores(anhoInicio, anhoFinal);
        var busqueda = conversor.obtenerDatos(resultado, Datos.class);
        List<DatosLibro> librosEncontrados = busqueda.resultados().stream().toList();
        //List<DatosAutor> autores = null;
        if (!librosEncontrados.isEmpty()) {
            for (DatosLibro libro: librosEncontrados){
                System.out.println(libro.datosAutor().get(0));
            }
        } else {
            System.out.println("No se encontró ningún autor");
        }

    }

    private void buscarTitulo(){
        System.out.println("Ingresa el titulo del Libro: ");
        var tituloLibro = scan.nextLine();
        Optional<Libro> libro = repositorioLibro.findByTituloContainsIgnoreCase(tituloLibro);

        if (libro.isPresent()){
            System.out.println("El libro buscado es: \n" + libro.get());
        }else {
            System.out.println("Libro no encontrado en la biblioteca");
        }
    }

    private void mostrarLibrosIdioma(){
        System.out.println("""
                Seleciona el idioma a buscar: 
                1 - en
                2 - es
                
                """);
        var op = scan.nextInt();

        switch (op){
            case 1:
                buscarPorIdioma("en");
                break;
            case 2:
                buscarPorIdioma("es");
                break;
            default:
                System.out.println("Opción inválida\n");
        }

    }

    private void buscarPorIdioma(String idioma){
        Optional<Libro> listaLibrosIdioma = repositorioLibro.findByIdioma(idioma);

        if (listaLibrosIdioma.isPresent()){
            System.out.println("Libros en idioma " + idioma.toUpperCase());
            List<Libro> listaLibros = listaLibrosIdioma.stream().collect(Collectors.toList());
            listaLibros.forEach(System.out::println);
        } else {
            System.out.println("No se encontraron libros en ese idioma");
        }
    }
    private Autor buscarAutor(Autor autor){
        List<Libro> listaLibros = repositorioLibro.findAll();
        //List<Autor> autoresRepositorio = listaLibros.stream().FlatMap(libro -> libro.getAutor().);
        return autor;
    }
}
