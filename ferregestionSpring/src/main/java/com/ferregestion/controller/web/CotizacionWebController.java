package com.ferregestion.controller.web;

import com.ferregestion.dto.request.CotizacionRequestDTO;
import com.ferregestion.dto.response.CotizacionResponseDTO;
import com.ferregestion.exception.ResourceNotFoundException;
import com.ferregestion.service.ClienteService;
import com.ferregestion.service.CotizacionService;
import com.ferregestion.service.ProductoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/web/cotizaciones")
public class CotizacionWebController {

    private final CotizacionService cotizacionService;
    private final ClienteService clienteService;
    private final ProductoService productoService;
    private final ObjectMapper objectMapper;

    public CotizacionWebController(CotizacionService cotizacionService,
                                   ClienteService clienteService,
                                   ProductoService productoService,
                                   ObjectMapper objectMapper) {
        this.cotizacionService = cotizacionService;
        this.clienteService = clienteService;
        this.productoService = productoService;
        this.objectMapper = objectMapper;
    }

    @GetMapping
    public String listar(Model model) {
        List<CotizacionResponseDTO> cotizaciones = cotizacionService.listarTodas();
        model.addAttribute("cotizaciones", cotizaciones);
        return "cotizaciones/lista";
    }

    @GetMapping("/nueva")
    public String nueva(Model model) {
        try {
            model.addAttribute("clientes", clienteService.listarTodos());
            model.addAttribute("productos", productoService.listarTodos());

            // Convertir productos a JSON para JavaScript
            String productosJson = objectMapper.writeValueAsString(productoService.listarTodos());
            model.addAttribute("productosJson", productosJson);

            return "cotizaciones/formulario.html";
        } catch (JsonProcessingException e) {
            model.addAttribute("error", "Error al cargar los datos");
            return "redirect:/web/cotizaciones";
        }
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute CotizacionRequestDTO cotizacionDTO,
                          BindingResult result,
                          RedirectAttributes redirectAttributes,
                          Model model) {

        // Log para debug
        System.out.println("=== GUARDANDO COTIZACIÓN ===");
        System.out.println("Cliente cedula: " + cotizacionDTO.getCedula());
        System.out.println("Nombre: " + cotizacionDTO.getNombre());
        System.out.println("Fecha: " + cotizacionDTO.getFecha());
        System.out.println("Total: " + cotizacionDTO.getTotal());
        System.out.println("Detalles: " + (cotizacionDTO.getDetalles() != null ? cotizacionDTO.getDetalles().size() : 0));

        if (cotizacionDTO.getDetalles() != null) {
            cotizacionDTO.getDetalles().forEach(d ->
                    System.out.println("  - Producto: " + d.getIdProducto() +
                            ", Cantidad: " + d.getCantidad() +
                            ", Precio: " + d.getPrecioUnitario())
            );
        }

        if (result.hasErrors()) {
            System.out.println("Errores de validación:");
            result.getAllErrors().forEach(error -> System.out.println("  - " + error.getDefaultMessage()));

            try {
                model.addAttribute("clientes", clienteService.listarTodos());
                model.addAttribute("productos", productoService.listarTodos());
                String productosJson = objectMapper.writeValueAsString(productoService.listarTodos());
                model.addAttribute("productosJson", productosJson);
            } catch (JsonProcessingException e) {
                // Ignorar
            }
            return "cotizaciones/formulario";
        }

        try {
            CotizacionResponseDTO cotizacionGuardada = cotizacionService.guardar(cotizacionDTO);
            System.out.println("Cotización guardada con ID: " + cotizacionGuardada.getIdCotizacion());

            redirectAttributes.addFlashAttribute("mensaje", "Cotización creada exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
            return "redirect:/web/cotizaciones";
        } catch (Exception e) {
            System.out.println("ERROR al guardar: " + e.getMessage());
            e.printStackTrace();

            redirectAttributes.addFlashAttribute("error", "Error al crear la cotización: " + e.getMessage());
            return "redirect:/web/cotizaciones/nueva";
        }
    }

    @GetMapping("/ver/{id}")
    public String ver(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        try {
            CotizacionResponseDTO cotizacion = cotizacionService.buscarPorId(id);
            model.addAttribute("cotizacion", cotizacion);
            return "cotizaciones/detalle";
        } catch (ResourceNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/web/cotizaciones";
        }
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            cotizacionService.eliminar(id);
            redirectAttributes.addFlashAttribute("mensaje", "Cotización eliminada exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar: " + e.getMessage());
        }

        return "redirect:/web/cotizaciones";
    }
}