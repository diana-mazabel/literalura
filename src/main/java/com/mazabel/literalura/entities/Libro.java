package com.mazabel.literalura.entities;

import com.mazabel.literalura.model.DatosLibro;
import jakarta.persistence.*;

import java.util.List;


@Entity
@Table(name= "libros")
public class Libro {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String titulo;

    @ManyToOne
    @JoinColumn(name = "autor_id")
    private Autor autor;
    private String idioma;
    private Double numDescargas;


    public Libro() {
    }

    public Libro(DatosLibro libro, Autor autor) {
        this.titulo = libro.titulo();
        this.autor = autor;
        this.idioma = libro.idiomas().get(0);
        this.numDescargas = libro.numDescargas();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        List<Libro> libros = autor.getLibros();
        libros.add(this);
        autor.setLibros(libros);
        this.autor = autor;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public Double getNumDescargas() {
        return numDescargas;
    }

    public void setNumDescargas(Double numDescargas) {
        this.numDescargas = numDescargas;
    }

    @Override
    public String toString() {
        return "-------Libro-------\n" +
                "ID: "  + id + "\n" +
                "Titulo= " + titulo + "\n" +
                "Autor= " + autor.getNombre() + "\n" +
                "Idioma= " + idioma + "\n" +
                "No. de Descargas= " + numDescargas +
                "\n------------------";
    }
}
