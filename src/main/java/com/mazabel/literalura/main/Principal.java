package com.mazabel.literalura.main;

import com.mazabel.literalura.entities.Autor;
import com.mazabel.literalura.entities.Libro;
import com.mazabel.literalura.model.Datos;
import com.mazabel.literalura.model.DatosAutor;
import com.mazabel.literalura.model.DatosLibro;
import com.mazabel.literalura.repository.LibroRepository;
import com.mazabel.literalura.service.ConsumoAPI;
import com.mazabel.literalura.service.ConvertirDatos;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Principal {

    private static final String URL_BASE = "https://gutendex.com/books/";
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConvertirDatos conversor = new ConvertirDatos();
    private Scanner scan = new Scanner(System.in);

    private List<DatosLibro> librosBuscados = new ArrayList<DatosLibro>();
    private List<DatosAutor> autoresBuscados = new ArrayList<DatosAutor>();
    private LibroRepository repositorio;

    public Principal(LibroRepository repository) {
        this.repositorio = repository;
    }

    public void mostarMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    
                    1 - Buscar Libro por titulo
                    2 - Lista de libros buscados
                    3 - Lista de autores de los libros buscados
                    4 - Buscar autores vivos en determinado tiempo
                                  
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
                    mostrarListaLibrosBuscados();
                    break;
                case 3:
                    mostrarListaAutoresDeLibrosBuscados();
                    break;
                case 4:
                    buscarAutoresVivos();
                    break;
                case 0:
                    System.out.println("¡Hasta luego!");
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        }

    }

    /*    public void mostarMenu(){
            var resultado = consumoAPI.obtenerDatos(URL_BASE);
            System.out.println(resultado);

            var datos = conversor.obtenerDatos(resultado, Datos.class);
            System.out.println(datos);
        }*/
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
            Libro libro = new Libro(datosLibro, autor);

            repositorio.save(libro);

        } else {
            System.out.println("No se encontró el Libro");
        }

    }

    public void mostrarListaLibrosBuscados(){
        System.out.println("Lista de libros buscados: ");
        List<Libro> listaLibros = repositorio.findAll();
        listaLibros.forEach(System.out::println);
    }
    public void mostrarListaAutoresDeLibrosBuscados(){
        System.out.println("Lista de autores de los libros buscados: ");
        autoresBuscados.forEach(System.out::println);
    }


    public void buscarAutoresVivos() {
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
}
