package com.merchstock.app.service.impl;

import com.google.common.base.Preconditions;
import com.merchstock.app.entity.Categoria;
import com.merchstock.app.exception.BusinessException;
import com.merchstock.app.exception.ResourceNotFoundException;
import com.merchstock.app.repository.CategoriaRepository;
import com.merchstock.app.service.CategoriaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementacion del servicio de Categorias
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class CategoriaServiceImpl implements CategoriaService {

    private final CategoriaRepository categoriaRepository;

    @Override
    public List<Categoria> listarActivas() {
        log.debug("Listando categorias activas");
        return categoriaRepository.findByActivoTrueOrderByNombreAsc();
    }

    @Override
    public List<Categoria> listarTodas() {
        return categoriaRepository.findAll();
    }

    @Override
    public Categoria buscarPorId(Long id) {
        Preconditions.checkNotNull(id, "El ID no puede ser nulo");
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria", "id", id));
    }

    @Override
    @Transactional
    public Categoria crear(Categoria categoria) {
        log.info("Creando nueva categoria: {}", categoria.getNombre());
        Preconditions.checkArgument(StringUtils.isNotBlank(categoria.getNombre()),
                "El nombre de la categoria es obligatorio");

        if (categoriaRepository.existsByNombre(categoria.getNombre())) {
            throw new BusinessException("Ya existe una categoria con nombre: " + categoria.getNombre());
        }

        return categoriaRepository.save(categoria);
    }

    @Override
    @Transactional
    public Categoria actualizar(Long id, Categoria categoriaActualizada) {
        Categoria existente = buscarPorId(id);
        existente.setNombre(categoriaActualizada.getNombre());
        existente.setDescripcion(categoriaActualizada.getDescripcion());
        return categoriaRepository.save(existente);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        Categoria categoria = buscarPorId(id);
        categoria.setActivo(false);
        categoriaRepository.save(categoria);
    }
}