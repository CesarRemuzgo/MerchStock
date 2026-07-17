package com.merchstock.app.repository;

import com.merchstock.app.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio DAO para la entidad Cliente
 */
@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    /**
     * Busca un cliente por su numero de documento (DNI, RUC, etc)
     */
    Optional<Cliente> findByNumeroDocumento(String numeroDocumento);

    /**
     * Verifica si existe un cliente con ese documento
     */
    boolean existsByNumeroDocumento(String numeroDocumento);

    /**
     * Lista clientes activos
     */
    List<Cliente> findByActivoTrueOrderByNombreCompletoAsc();

    /**
     * Busca clientes cuyo nombre contenga el texto dado (insensitive)
     */
    List<Cliente> findByNombreCompletoContainingIgnoreCase(String nombre);
}