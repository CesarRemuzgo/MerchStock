package com.merchstock.app.repository;

import com.merchstock.app.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio DAO para la entidad Categoria
 * Spring Data JPA provee CRUD basico automaticamente
 */
@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    /**
     * Busca una categoria por su nombre exacto
     */
    Optional<Categoria> findByNombre(String nombre);

    /**
     * Verifica si existe una categoria con el nombre dado
     */
    boolean existsByNombre(String nombre);

    /**
     * Lista las categorias activas (no eliminadas logicamente)
     */
    List<Categoria> findByActivoTrueOrderByNombreAsc();
    /**
     * Busca una categoria activa por su nombre, ignorando mayusculas/minusculas.
     * Util para la importacion masiva desde CSV.
     */
    Optional<Categoria> findByNombreIgnoreCaseAndActivoTrue(String nombre);
}