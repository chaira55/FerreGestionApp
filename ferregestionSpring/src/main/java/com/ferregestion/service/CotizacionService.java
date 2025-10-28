package com.ferregestion.service;

import com.ferregestion.dto.request.CotizacionRequestDTO;
import com.ferregestion.dto.response.CotizacionResponseDTO;
import com.ferregestion.entity.Cotizacion;
import com.ferregestion.exception.ResourceNotFoundException;
import com.ferregestion.mapper.CotizacionMapper;
import com.ferregestion.repository.CotizacionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CotizacionService {

    private final CotizacionRepository cotizacionRepository;
    private final CotizacionMapper cotizacionMapper;

    public CotizacionService(CotizacionRepository cotizacionRepository, CotizacionMapper cotizacionMapper) {
        this.cotizacionRepository = cotizacionRepository;
        this.cotizacionMapper = cotizacionMapper;
    }

    public List<CotizacionResponseDTO> listarTodas() {
        return cotizacionRepository.findAll().stream()
                .map(cotizacionMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    // NUEVO: Paginación
    public Page<CotizacionResponseDTO> listarTodasPaginado(int page, int size, String sortBy, String direction) {
        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));

        return cotizacionRepository.findAll(pageable)
                .map(cotizacionMapper::toResponseDTO);
    }

    // NUEVO: Buscar por cliente
    public Page<CotizacionResponseDTO> buscarPorCliente(Integer cedula, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "fecha"));
        return cotizacionRepository.findByClienteCedula(cedula, pageable)
                .map(cotizacionMapper::toResponseDTO);
    }

    // NUEVO: Buscar por nombre del cliente
    public Page<CotizacionResponseDTO> buscarPorNombreCliente(String nombre, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "fecha"));
        return cotizacionRepository.findByNombreContainingIgnoreCase(nombre, pageable)
                .map(cotizacionMapper::toResponseDTO);
    }

    // NUEVO: Filtrar por rango de fechas
    public Page<CotizacionResponseDTO> filtrarPorRangoFechas(
            LocalDate fechaInicio,
            LocalDate fechaFin,
            int page,
            int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "fecha"));
        return cotizacionRepository.findByFechaBetween(fechaInicio, fechaFin, pageable)
                .map(cotizacionMapper::toResponseDTO);
    }

    // NUEVO: Filtrar por rango de total
    public Page<CotizacionResponseDTO> filtrarPorRangoTotal(
            BigDecimal min,
            BigDecimal max,
            int page,
            int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "total"));
        return cotizacionRepository.findByTotalBetween(min, max, pageable)
                .map(cotizacionMapper::toResponseDTO);
    }

    // NUEVO: Cotizaciones del día
    public List<CotizacionResponseDTO> cotizacionesDelDia(LocalDate fecha) {
        return cotizacionRepository.findByFecha(fecha).stream()
                .map(cotizacionMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    // NUEVO: Estadísticas
    public Map<String, Object> obtenerEstadisticas(LocalDate fechaInicio, LocalDate fechaFin) {
        BigDecimal total = cotizacionRepository.calcularTotalCotizacionesPorRango(fechaInicio, fechaFin);
        List<Object[]> cotizacionesPorCliente = cotizacionRepository.contarCotizacionesPorCliente();

        Map<String, Object> estadisticas = new HashMap<>();
        estadisticas.put("totalCotizaciones", total != null ? total : BigDecimal.ZERO);
        estadisticas.put("fechaInicio", fechaInicio);
        estadisticas.put("fechaFin", fechaFin);

        Map<String, Long> cotizacionesCliente = new HashMap<>();
        for (Object[] row : cotizacionesPorCliente) {
            cotizacionesCliente.put((String) row[0], (Long) row[1]);
        }
        estadisticas.put("cotizacionesPorCliente", cotizacionesCliente);

        return estadisticas;
    }

    public CotizacionResponseDTO buscarPorId(Integer id) {
        Cotizacion cotizacion = cotizacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cotización con ID " + id + " no encontrada"));
        return cotizacionMapper.toResponseDTO(cotizacion);
    }

    public CotizacionResponseDTO guardar(CotizacionRequestDTO cotizacionDTO) {
        Cotizacion cotizacion = cotizacionMapper.toEntity(cotizacionDTO);
        Cotizacion cotizacionGuardada = cotizacionRepository.save(cotizacion);
        return cotizacionMapper.toResponseDTO(cotizacionGuardada);
    }

    public CotizacionResponseDTO actualizar(Integer id, CotizacionRequestDTO cotizacionDTO) {
        Cotizacion cotizacionExistente = cotizacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cotización con ID " + id + " no encontrada"));

        cotizacionExistente.getDetalles().clear();

        Cotizacion cotizacionNueva = cotizacionMapper.toEntity(cotizacionDTO);
        cotizacionExistente.setCliente(cotizacionNueva.getCliente());
        cotizacionExistente.setNombre(cotizacionNueva.getNombre());
        cotizacionExistente.setFecha(cotizacionNueva.getFecha());
        cotizacionExistente.setTotal(cotizacionNueva.getTotal());

        cotizacionNueva.getDetalles().forEach(detalle -> {
            detalle.setCotizacion(cotizacionExistente);
            cotizacionExistente.getDetalles().add(detalle);
        });

        Cotizacion cotizacionActualizada = cotizacionRepository.save(cotizacionExistente);
        return cotizacionMapper.toResponseDTO(cotizacionActualizada);
    }

    public void eliminar(Integer id) {
        Cotizacion cotizacion = cotizacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cotización con ID " + id + " no encontrada"));
        cotizacionRepository.delete(cotizacion);
    }
}