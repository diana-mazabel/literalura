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
    Integer fechaDeNacimiento;
    Integer fechaDeFallecimiento;
    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    List<Libro> libros;


    public Autor() {
    }

    public Autor(DatosAutor autor) {
        this.nombre = autor.nombre();
        this.fechaDeNacimiento = Integer.valueOf(autor.fechaDeNacimiento());
        this.fechaDeFallecimiento = Integer.valueOf(autor.fechaDeFallecimiento());
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getFechaDeNacimiento() {
        return fechaDeNacimiento;
    }

    public void setFechaDeNacimiento(Integer fechaDeNacimiento) {
        this.fechaDeNacimiento = fechaDeNacimiento;
    }

    public Integer getFechaDeFallecimiento() {
        return fechaDeFallecimiento;
    }

    public void setFechaDeFallecimiento(Integer fechaDeFallecimiento) {
        this.fechaDeFallecimiento = fechaDeFallecimiento;
    }

    public List<Libro> getLibros() {
        return libros;
    }

    public void setLibros(List<Libro> libros) {
        libros.forEach(libro -> libro.setAutor(this));
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
        String titulos = libros.stream().map(l-> l.getTitulo()).collect(Collectors.joining(", "));
        return "------Autor------\n" +
                "ID: "  + id + "\n" +
                "Nombre= " + nombre + "\n" +
                "Fecha de nacimiento= " + fechaDeNacimiento + "\n" +
                "Fecha de fallecimiento= " + fechaDeFallecimiento + "\n" +
                "Libros= " + titulos +
                "\n--------------------";
    }
}
