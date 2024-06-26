package com.mazabel.literalura.repository;

import com.mazabel.literalura.entities.Libro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LibroRepository extends JpaRepository<Libro, Long> {

    Optional<Libro> findByTituloContainsIgnoreCase(String tituloLibro);

    List<Libro> findByIdioma(String idioma);


}
