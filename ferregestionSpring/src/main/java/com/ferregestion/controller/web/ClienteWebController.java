package com.ferregestion.controller.web;

import com.ferregestion.dto.request.ClienteRequestDTO;
import com.ferregestion.dto.response.ClienteResponseDTO;
import com.ferregestion.service.ClienteService;
import com.ferregestion.service.VentaService;
import com.ferregestion.service.CreditoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.math.BigDecimal;

@Controller
@RequestMapping("/web/clientes")
public class ClienteWebController {

    private final ClienteService clienteService;
    private final VentaService ventaService;
    private final CreditoService creditoService;

    public ClienteWebController(ClienteService clienteService,
                                VentaService ventaService,
                                CreditoService creditoService) {
        this.clienteService = clienteService;
        this.ventaService = ventaService;
        this.creditoService = creditoService;
    }

    // Listar todos los clientes
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("clientes", clienteService.listarTodos());
        return "clientes/lista";
    }

    // Mostrar formulario de nuevo cliente
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("cliente", new ClienteRequestDTO());
        model.addAttribute("accion", "Nuevo");
        return "clientes/formulario";
    }

    // Guardar nuevo cliente
    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("cliente") ClienteRequestDTO clienteDTO,
                          BindingResult result,
                          RedirectAttributes redirectAttributes,
                          Model model) {

        if (result.hasErrors()) {
            model.addAttribute("accion", "Nuevo");
            return "clientes/formulario";
        }

        try {
            clienteService.guardar(clienteDTO);
            redirectAttributes.addFlashAttribute("mensaje", "Cliente creado exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
            return "redirect:/web/clientes";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("accion", "Nuevo");
            return "clientes/formulario";
        }
    }

    // Mostrar formulario de edición
    @GetMapping("/editar/{cedula}")
    public String mostrarFormularioEditar(@PathVariable Integer cedula, Model model) {
        try {
            ClienteResponseDTO cliente = clienteService.buscarPorCedula(cedula);

            // Convertir Response a Request DTO
            ClienteRequestDTO clienteDTO = ClienteRequestDTO.builder()
                    .cedula(cliente.getCedula())
                    .nombre(cliente.getNombre())
                    .celular(cliente.getCelular())
                    .direccion(cliente.getDireccion())
                    .correo(cliente.getCorreo())
                    .build();

            model.addAttribute("cliente", clienteDTO);
            model.addAttribute("accion", "Editar");
            return "clientes/formulario";
        } catch (Exception e) {
            return "redirect:/web/clientes";
        }
    }

    // Actualizar cliente
    @PostMapping("/actualizar/{cedula}")
    public String actualizar(@PathVariable Integer cedula,
                             @Valid @ModelAttribute("cliente") ClienteRequestDTO clienteDTO,
                             BindingResult result,
                             RedirectAttributes redirectAttributes,
                             Model model) {

        if (result.hasErrors()) {
            model.addAttribute("accion", "Editar");
            return "clientes/formulario";
        }

        try {
            clienteService.actualizar(cedula, clienteDTO);
            redirectAttributes.addFlashAttribute("mensaje", "Cliente actualizado exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
            return "redirect:/web/clientes";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("accion", "Editar");
            return "clientes/formulario";
        }
    }

    // Eliminar cliente
    @GetMapping("/eliminar/{cedula}")
    public String eliminar(@PathVariable Integer cedula, RedirectAttributes redirectAttributes) {
        try {
            clienteService.eliminar(cedula);
            redirectAttributes.addFlashAttribute("mensaje", "Cliente eliminado exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error al eliminar: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "danger");
        }
        return "redirect:/web/clientes";
    }

    // Ver detalle de cliente CON ESTADÍSTICAS CORREGIDAS
    @GetMapping("/ver/{cedula}")
    public String ver(@PathVariable Integer cedula, Model model) {
        try {
            ClienteResponseDTO cliente = clienteService.buscarPorCedula(cedula);
            model.addAttribute("cliente", cliente);

            // CALCULAR ESTADÍSTICAS CORRECTAMENTE

            // Total de compras (ventas del cliente)
            long totalCompras = ventaService.listarTodos().stream()
                    .filter(v -> {
                        if (v.getCliente() == null) return false;
                        Integer ventaCedula = v.getCliente().getCedula();
                        return ventaCedula != null && ventaCedula.equals(cedula);
                    })
                    .count();

            // Total gastado por el cliente
            BigDecimal totalGastado = ventaService.listarTodos().stream()
                    .filter(v -> {
                        if (v.getCliente() == null) return false;
                        Integer ventaCedula = v.getCliente().getCedula();
                        return ventaCedula != null && ventaCedula.equals(cedula);
                    })
                    .map(v -> v.getTotal() != null ? v.getTotal() : BigDecimal.ZERO)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            // Créditos activos (con saldo pendiente)
            long creditosActivos = creditoService.listarTodos().stream()
                    .filter(c -> {
                        if (c.getCliente() == null) return false;
                        Integer creditoCedula = c.getCliente().getCedula();
                        if (creditoCedula == null || !creditoCedula.equals(cedula)) return false;
                        BigDecimal saldo = c.getSaldoPendiente();
                        return saldo != null && saldo.compareTo(BigDecimal.ZERO) > 0;
                    })
                    .count();

            // DEBUG: Imprimir en consola para verificar
            System.out.println("=== ESTADÍSTICAS CLIENTE " + cedula + " ===");
            System.out.println("Total Compras: " + totalCompras);
            System.out.println("Total Gastado: " + totalGastado);
            System.out.println("Créditos Activos: " + creditosActivos);

            // Obtener lista de ventas del cliente para mostrar
            var ventasCliente = ventaService.listarTodos().stream()
                    .filter(v -> {
                        if (v.getCliente() == null) return false;
                        Integer ventaCedula = v.getCliente().getCedula();
                        return ventaCedula != null && ventaCedula.equals(cedula);
                    })
                    .collect(java.util.stream.Collectors.toList());

            model.addAttribute("totalCompras", totalCompras);
            model.addAttribute("totalGastado", totalGastado);
            model.addAttribute("creditosActivos", creditosActivos);
            model.addAttribute("ventasCliente", ventasCliente);

            return "clientes/detalle";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/web/clientes";
        }
    }
}