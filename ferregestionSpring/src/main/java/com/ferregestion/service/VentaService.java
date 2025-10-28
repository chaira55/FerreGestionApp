package com.ferregestion.service;

import com.ferregestion.dto.request.VentaRequestDTO;
import com.ferregestion.dto.response.VentaResponseDTO;
import com.ferregestion.entity.Venta;
import com.ferregestion.exception.ResourceNotFoundException;
import com.ferregestion.mapper.VentaMapper;
import com.ferregestion.repository.VentaRepository;
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
public class VentaService {

    private final VentaRepository ventaRepository;
    private final VentaMapper ventaMapper;

    public VentaService(VentaRepository ventaRepository, VentaMapper ventaMapper) {
        this.ventaRepository = ventaRepository;
        this.ventaMapper = ventaMapper;
    }

    public List<VentaResponseDTO> listarTodos() {
        return ventaRepository.findAll().stream()
                .map(ventaMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    // NUEVO: Paginación
    public Page<VentaResponseDTO> listarTodosPaginado(int page, int size, String sortBy, String direction) {
        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));

        return ventaRepository.findAll(pageable)
                .map(ventaMapper::toResponseDTO);
    }

    // NUEVO: Buscar por cliente (cédula)
    public Page<VentaResponseDTO> buscarPorCliente(Integer cedula, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "fecha"));
        return ventaRepository.findByClienteCedula(cedula, pageable)
                .map(ventaMapper::toResponseDTO);
    }

    // NUEVO: Buscar por nombre del cliente
    public Page<VentaResponseDTO> buscarPorNombreCliente(String nombre, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "fecha"));
        return ventaRepository.findByNombreContainingIgnoreCase(nombre, pageable)
                .map(ventaMapper::toResponseDTO);
    }

    // NUEVO: Filtrar por tipo de pago
    public Page<VentaResponseDTO> filtrarPorTipoPago(String tipoPago, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "fecha"));
        return ventaRepository.findByTipoPago(tipoPago, pageable)
                .map(ventaMapper::toResponseDTO);
    }

    // NUEVO: Filtrar por rango de fechas
    public Page<VentaResponseDTO> filtrarPorRangoFechas(
            LocalDate fechaInicio,
            LocalDate fechaFin,
            int page,
            int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "fecha"));
        return ventaRepository.findByFechaBetween(fechaInicio, fechaFin, pageable)
                .map(ventaMapper::toResponseDTO);
    }

    // NUEVO: Filtrar por rango de total
    public Page<VentaResponseDTO> filtrarPorRangoTotal(
            BigDecimal min,
            BigDecimal max,
            int page,
            int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "total"));
        return ventaRepository.findByTotalBetween(min, max, pageable)
                .map(ventaMapper::toResponseDTO);
    }

    // NUEVO: Ventas del día
    public List<VentaResponseDTO> ventasDelDia(LocalDate fecha) {
        return ventaRepository.findByFecha(fecha).stream()
                .map(ventaMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    // NUEVO: Estadísticas de ventas por rango
    public Map<String, Object> obtenerEstadisticas(LocalDate fechaInicio, LocalDate fechaFin) {
        BigDecimal total = ventaRepository.calcularTotalVentasPorRango(fechaInicio, fechaFin);
        List<Object[]> ventasPorTipo = ventaRepository.contarVentasPorTipoPago();

        Map<String, Object> estadisticas = new HashMap<>();
        estadisticas.put("totalVentas", total != null ? total : BigDecimal.ZERO);
        estadisticas.put("fechaInicio", fechaInicio);
        estadisticas.put("fechaFin", fechaFin);

        Map<String, Long> ventasPorTipoPago = new HashMap<>();
        for (Object[] row : ventasPorTipo) {
            ventasPorTipoPago.put((String) row[0], (Long) row[1]);
        }
        estadisticas.put("ventasPorTipoPago", ventasPorTipoPago);

        return estadisticas;
    }

    public VentaResponseDTO buscarPorId(Integer idVenta) {
        Venta venta = ventaRepository.findById(idVenta)
                .orElseThrow(() -> new ResourceNotFoundException("Venta con ID " + idVenta + " no encontrada"));
        return ventaMapper.toResponseDTO(venta);
    }

    public VentaResponseDTO guardar(VentaRequestDTO ventaDTO) {
        Venta venta = ventaMapper.toEntity(ventaDTO);
        Venta ventaGuardada = ventaRepository.save(venta);
        return ventaMapper.toResponseDTO(ventaGuardada);
    }

    public VentaResponseDTO actualizar(Integer idVenta, VentaRequestDTO ventaDTO) {
        Venta ventaExistente = ventaRepository.findById(idVenta)
                .orElseThrow(() -> new ResourceNotFoundException("Venta con ID " + idVenta + " no encontrada"));

        ventaExistente.getDetalles().clear();

        Venta ventaNueva = ventaMapper.toEntity(ventaDTO);
        ventaExistente.setCliente(ventaNueva.getCliente());
        ventaExistente.setNombre(ventaNueva.getNombre());
        ventaExistente.setCotizacion(ventaNueva.getCotizacion());
        ventaExistente.setFecha(ventaNueva.getFecha());
        ventaExistente.setTotal(ventaNueva.getTotal());
        ventaExistente.setTipoPago(ventaNueva.getTipoPago());

        ventaNueva.getDetalles().forEach(detalle -> {
            detalle.setVenta(ventaExistente);
            ventaExistente.getDetalles().add(detalle);
        });

        Venta ventaActualizada = ventaRepository.save(ventaExistente);
        return ventaMapper.toResponseDTO(ventaActualizada);
    }

    public void eliminar(Integer idVenta) {
        Venta venta = ventaRepository.findById(idVenta)
                .orElseThrow(() -> new ResourceNotFoundException("Venta con ID " + idVenta + " no encontrada"));
        ventaRepository.delete(venta);
    }
}