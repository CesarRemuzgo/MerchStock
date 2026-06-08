package com.merchstock.app.service.impl;

import com.google.common.base.Preconditions;
import com.merchstock.app.entity.Cliente;
import com.merchstock.app.exception.BusinessException;
import com.merchstock.app.exception.ResourceNotFoundException;
import com.merchstock.app.repository.ClienteRepository;
import com.merchstock.app.service.ClienteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementacion del servicio de Clientes
 *
 * Aplica librerias de la rubrica APF3:
 * - Google Guava: Preconditions
 * - Apache Commons Lang: StringUtils
 * - Logback: @Slf4j
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;

    @Override
    public List<Cliente> listarTodos() {
        log.debug("Listando todos los clientes");
        return clienteRepository.findAll();
    }

    @Override
    public List<Cliente> listarActivos() {
        log.debug("Listando clientes activos");
        return clienteRepository.findByActivoTrueOrderByNombreCompletoAsc();
    }

    @Override
    public Cliente buscarPorId(Long id) {
        Preconditions.checkNotNull(id, "El ID del cliente no puede ser nulo");
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente", "id", id));
    }

    @Override
    public Cliente buscarPorDocumento(String numeroDocumento) {
        Preconditions.checkArgument(StringUtils.isNotBlank(numeroDocumento),
                "El numero de documento no puede estar vacio");

        return clienteRepository.findByNumeroDocumento(numeroDocumento.trim())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cliente", "numeroDocumento", numeroDocumento));
    }

    @Override
    public List<Cliente> buscarPorNombre(String nombre) {
        if (StringUtils.isBlank(nombre)) {
            return listarActivos();
        }
        return clienteRepository.findByNombreCompletoContainingIgnoreCase(nombre.trim());
    }

    @Override
    @Transactional
    public Cliente crear(Cliente cliente) {
        log.info("Creando nuevo cliente: {}", cliente.getNombreCompleto());
        Preconditions.checkNotNull(cliente, "El cliente no puede ser nulo");
        Preconditions.checkArgument(StringUtils.isNotBlank(cliente.getNumeroDocumento()),
                "El numero de documento es obligatorio");

        // Validar duplicado
        if (clienteRepository.existsByNumeroDocumento(cliente.getNumeroDocumento())) {
            throw new BusinessException("Ya existe un cliente con documento: "
                    + cliente.getNumeroDocumento());
        }

        Cliente guardado = clienteRepository.save(cliente);
        log.info("Cliente creado con ID: {}", guardado.getId());
        return guardado;
    }

    @Override
    @Transactional
    public Cliente actualizar(Long id, Cliente clienteActualizado) {
        log.info("Actualizando cliente ID: {}", id);
        Cliente existente = buscarPorId(id);

        existente.setTipoDocumento(clienteActualizado.getTipoDocumento());
        existente.setNombreCompleto(clienteActualizado.getNombreCompleto());
        existente.setTelefono(clienteActualizado.getTelefono());
        existente.setEmail(clienteActualizado.getEmail());
        existente.setDireccion(clienteActualizado.getDireccion());

        // Solo actualiza numero documento si cambio (validar duplicado)
        if (!existente.getNumeroDocumento().equals(clienteActualizado.getNumeroDocumento())) {
            if (clienteRepository.existsByNumeroDocumento(clienteActualizado.getNumeroDocumento())) {
                throw new BusinessException("Ya existe otro cliente con documento: "
                        + clienteActualizado.getNumeroDocumento());
            }
            existente.setNumeroDocumento(clienteActualizado.getNumeroDocumento());
        }
        // Reactivar automaticamente al editar (en caso estuviera desactivado)
        if (Boolean.FALSE.equals(existente.getActivo())) {
            log.info("Reactivando cliente ID: {}", id);
            existente.setActivo(true);
        }
        return clienteRepository.save(existente);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        log.info("Desactivando cliente ID: {}", id);
        Cliente cliente = buscarPorId(id);
        cliente.setActivo(false);
        clienteRepository.save(cliente);
    }
}