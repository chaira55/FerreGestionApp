package com.ferregestion.controller.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ferregestion.dto.request.VentaRequestDTO;
import com.ferregestion.dto.request.DetalleVentaRequestDTO;
import com.ferregestion.dto.response.VentaResponseDTO;
import com.ferregestion.dto.response.ClienteResponseDTO;
import com.ferregestion.service.ClienteService;
import com.ferregestion.service.ProductoService;
import com.ferregestion.service.VentaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/web/ventas")
public class VentaWebController {

    private final VentaService ventaService;
    private final ClienteService clienteService;
    private final ProductoService productoService;
    private final ObjectMapper objectMapper;

    public VentaWebController(VentaService ventaService,
                              ClienteService clienteService,
                              ProductoService productoService,
                              ObjectMapper objectMapper) {
        this.ventaService = ventaService;
        this.clienteService = clienteService;
        this.productoService = productoService;
        this.objectMapper = objectMapper;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("ventas", ventaService.listarTodos());
        return "ventas/lista";
    }

    @GetMapping("/nueva")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("venta", new VentaRequestDTO());

        // Pasar clientes como JSON
        List<ClienteResponseDTO> clientes = clienteService.listarTodos();
        try {
            String clientesJson = objectMapper.writeValueAsString(clientes);
            model.addAttribute("clientesJson", clientesJson);
        } catch (Exception e) {
            System.err.println("Error al serializar clientes: " + e.getMessage());
            model.addAttribute("clientesJson", "[]");
        }

        model.addAttribute("productos", productoService.listarTodos());
        return "ventas/formulario.html";
    }

    @PostMapping("/guardar")
    public String guardar(@RequestParam("cedula") Integer cedula,
                          @RequestParam("fecha") String fechaStr,
                          @RequestParam("tipoPago") String tipoPago,
                          @RequestParam("total") String totalStr,
                          @RequestParam Map<String, String> allParams,
                          RedirectAttributes redirectAttributes,
                          Model model) {

        System.out.println("=== GUARDANDO VENTA ===");
        System.out.println("Cliente (Cédula): " + cedula);
        System.out.println("Fecha: " + fechaStr);
        System.out.println("Tipo Pago: " + tipoPago);
        System.out.println("Total: " + totalStr);
        System.out.println("Parámetros recibidos: " + allParams.keySet());

        try {
            // Parsear fecha y total
            LocalDate fecha = LocalDate.parse(fechaStr);
            BigDecimal total = new BigDecimal(totalStr);

            // Construir DTO manualmente
            VentaRequestDTO ventaDTO = new VentaRequestDTO();
            ventaDTO.setCedula(cedula);
            ventaDTO.setFecha(fecha);
            ventaDTO.setTipoPago(tipoPago);
            ventaDTO.setTotal(total);

            // Extraer detalles de los parámetros
            List<DetalleVentaRequestDTO> detalles = new ArrayList<>();
            int index = 0;

            while (allParams.containsKey("detalles[" + index + "].idProducto")) {
                try {
                    String productoIdStr = allParams.get("detalles[" + index + "].idProducto");
                    String cantidadStr = allParams.get("detalles[" + index + "].cantidad");
                    String precioStr = allParams.get("detalles[" + index + "].precioUnitario");

                    System.out.println("Detalle " + index + ": productoId=" + productoIdStr +
                            ", cantidad=" + cantidadStr +
                            ", precio=" + precioStr);

                    if (productoIdStr != null && cantidadStr != null && precioStr != null) {
                        DetalleVentaRequestDTO detalle = new DetalleVentaRequestDTO();
                        detalle.setIdProducto(Integer.parseInt(productoIdStr.trim()));
                        detalle.setCantidad(Integer.parseInt(cantidadStr.trim()));
                        detalle.setPrecioUnitario(new BigDecimal(precioStr.trim()));
                        detalles.add(detalle);
                    }

                    index++;
                } catch (NumberFormatException e) {
                    System.err.println("Error parseando detalle " + index + ": " + e.getMessage());
                    index++;
                }
            }

            ventaDTO.setDetalles(detalles);

            System.out.println("✅ Detalles construidos: " + detalles.size());

            // Validaciones
            if (detalles.isEmpty()) {
                model.addAttribute("error", "Debe agregar al menos un producto");

                // Recargar datos
                List<ClienteResponseDTO> clientes = clienteService.listarTodos();
                try {
                    String clientesJson = objectMapper.writeValueAsString(clientes);
                    model.addAttribute("clientesJson", clientesJson);
                } catch (Exception ex) {
                    model.addAttribute("clientesJson", "[]");
                }

                model.addAttribute("productos", productoService.listarTodos());
                return "ventas/formulario.html";
            }

            // Guardar venta
            VentaResponseDTO ventaGuardada = ventaService.guardar(ventaDTO);
            System.out.println("✅ Venta guardada con ID: " + ventaGuardada.getIdVenta());

            redirectAttributes.addFlashAttribute("mensaje", "Venta registrada exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
            return "redirect:/web/ventas";

        } catch (RuntimeException e) {
            System.err.println("❌ Error al guardar: " + e.getMessage());
            e.printStackTrace();

            model.addAttribute("error", e.getMessage());

            // Recargar datos
            List<ClienteResponseDTO> clientes = clienteService.listarTodos();
            try {
                String clientesJson = objectMapper.writeValueAsString(clientes);
                model.addAttribute("clientesJson", clientesJson);
            } catch (Exception ex) {
                model.addAttribute("clientesJson", "[]");
            }

            model.addAttribute("productos", productoService.listarTodos());
            return "ventas/formulario.html";

        } catch (Exception e) {
            System.err.println("❌ Error inesperado: " + e.getMessage());
            e.printStackTrace();

            model.addAttribute("error", "Error al guardar: " + e.getMessage());

            // Recargar datos
            List<ClienteResponseDTO> clientes = clienteService.listarTodos();
            try {
                String clientesJson = objectMapper.writeValueAsString(clientes);
                model.addAttribute("clientesJson", clientesJson);
            } catch (Exception ex) {
                model.addAttribute("clientesJson", "[]");
            }

            model.addAttribute("productos", productoService.listarTodos());
            return "ventas/formulario.html";
        }
    }

    @GetMapping("/ver/{id}")
    public String ver(@PathVariable Integer id, Model model) {
        try {
            VentaResponseDTO venta = ventaService.buscarPorId(id);
            model.addAttribute("venta", venta);
            return "ventas/detalle";
        } catch (Exception e) {
            return "redirect:/web/ventas";
        }
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            ventaService.eliminar(id);
            redirectAttributes.addFlashAttribute("mensaje", "Venta eliminada exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error al eliminar: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "danger");
        }
        return "redirect:/web/ventas";
    }
}