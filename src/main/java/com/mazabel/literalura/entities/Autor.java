package com.mazabel.literalura.entities;

import com.mazabel.literalura.model.DatosAutor;
import jakarta.persistence.*;

import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "autores")
public class Autor {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    String nombre;
    String fechaDeNacimiento;
    String fechaDeFallecimiento;
    @OneToMany(mappedBy = "autor")
    List<Libro> libros;


    public Autor() {
    }

    public Autor(DatosAutor autor) {
        this.nombre = autor.nombre();
        this.fechaDeNacimiento = autor.fechaDeNacimiento();
        this.fechaDeFallecimiento = autor.fechaDeFallecimiento();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFechaDeNacimiento() {
        return fechaDeNacimiento;
    }

    public void setFechaDeNacimiento(String fechaDeNacimiento) {
        this.fechaDeNacimiento = fechaDeNacimiento;
    }

    public String getFechaDeFallecimiento() {
        return fechaDeFallecimiento;
    }

    public void setFechaDeFallecimiento(String fechaDeFallecimiento) {
        this.fechaDeFallecimiento = fechaDeFallecimiento;
    }

    public List<Libro> getLibros() {
        return libros;
    }

    public void setLibros(List<Libro> libros) {
        this.libros = libros;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        String librosTitulo = libros.stream()
                .map(libro -> libro.getTitulo())
                .collect(Collectors.joining(", "));
        return "---Autor---\n" +
                "Nombre= " + nombre + '\'' +
                "Fecha de nacimiento= " + fechaDeNacimiento + '\'' +
                "Fecha de fallecimiento= " + fechaDeFallecimiento + '\'' +
                "Libros= " + librosTitulo +
                "\n-----------";
    }
}
