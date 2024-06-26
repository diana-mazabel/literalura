package com.mazabel.literalura.repository;

import com.mazabel.literalura.entities.Libro;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibroRepository extends JpaRepository<Libro, Long> {
}
