package com.ferregestion.controller.web;

import com.ferregestion.dto.request.CreditoRequestDTO;
import com.ferregestion.dto.response.CreditoResponseDTO;
import com.ferregestion.service.CreditoService;
import com.ferregestion.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/web/creditos")
public class CreditoWebController {

    private final CreditoService creditoService;

    public CreditoWebController(CreditoService creditoService) {
        this.creditoService = creditoService;
    }

    @GetMapping
    public String listar(Model model) {
        List<CreditoResponseDTO> creditos = creditoService.listarTodos();
        model.addAttribute("creditos", creditos);
        return "creditos/lista";
    }

    @GetMapping("/ver/{id}")
    public String ver(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        try {
            CreditoResponseDTO credito = creditoService.buscarPorId(id);
            model.addAttribute("credito", credito);
            return "creditos/detalle";
        } catch (ResourceNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/web/creditos";
        }
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        try {
            CreditoResponseDTO credito = creditoService.buscarPorId(id);
            model.addAttribute("credito", credito);
            return "creditos/formulario";
        } catch (ResourceNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/web/creditos";
        }
    }

    @PostMapping("/actualizar/{id}")
    public String actualizar(@PathVariable Integer id,
                             @RequestParam(required = false) String nombre,
                             @RequestParam BigDecimal saldoPendiente,
                             @RequestParam String estado,
                             RedirectAttributes redirectAttributes) {
        try {
            // Obtener el crédito actual
            CreditoResponseDTO creditoActual = creditoService.buscarPorId(id);

            // Crear el DTO con los datos actualizados
            CreditoRequestDTO creditoDTO = CreditoRequestDTO.builder()
                    .idVenta(creditoActual.getIdVenta())  // ← CORREGIDO
                    .cedula(creditoActual.getIdCliente()) // ← CORREGIDO
                    .nombre(nombre)
                    .montoTotal(creditoActual.getMontoTotal())
                    .saldoPendiente(saldoPendiente)
                    .estado(estado)
                    .build();

            creditoService.actualizar(id, creditoDTO);

            redirectAttributes.addFlashAttribute("mensaje", "Crédito actualizado exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
            return "redirect:/web/creditos/ver/" + id;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar: " + e.getMessage());
            return "redirect:/web/creditos/editar/" + id;
        }
    }

    @GetMapping("/cliente/{cedula}")
    public String listarPorCliente(@PathVariable Integer cedula,
                                   @RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "10") int size,
                                   Model model) {
        Page<CreditoResponseDTO> creditos = creditoService.buscarPorCliente(cedula, page, size);
        model.addAttribute("creditos", creditos.getContent());
        model.addAttribute("cedula", cedula);
        return "creditos/lista-cliente";
    }

    @GetMapping("/activos")
    public String listarActivos(@RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "10") int size,
                                Model model) {
        Page<CreditoResponseDTO> creditos = creditoService.listarCreditosActivos(page, size);
        model.addAttribute("creditos", creditos.getContent());
        model.addAttribute("soloActivos", true);
        return "creditos/lista";
    }

    @GetMapping("/pagados")
    public String listarPagados(Model model) {
        List<CreditoResponseDTO> creditos = creditoService.listarTodos().stream()
                .filter(c -> "PAGADO".equals(c.getEstado()))
                .collect(Collectors.toList());
        model.addAttribute("creditos", creditos);
        model.addAttribute("soloPagados", true);
        return "creditos/lista";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            System.out.println("============================================");
            System.out.println("🗑️ WEB: Solicitud de eliminación recibida");
            System.out.println("    ID del crédito: " + id);
            System.out.println("============================================");

            // Obtener información antes de eliminar (para mostrar en el mensaje)
            CreditoResponseDTO credito = creditoService.buscarPorId(id);
            String nombreCliente = credito.getNombreCliente();

            // Eliminar el crédito
            creditoService.eliminar(id);

            System.out.println("✅ WEB: Crédito eliminado exitosamente");
            System.out.println("============================================");

            redirectAttributes.addFlashAttribute("mensaje",
                    "Crédito eliminado exitosamente. Cliente: " + nombreCliente);
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");

        } catch (Exception e) {
            System.err.println("❌ WEB: Error al eliminar crédito");
            System.err.println("    Error: " + e.getMessage());
            e.printStackTrace();

            redirectAttributes.addFlashAttribute("error",
                    "Error al eliminar el crédito: " + e.getMessage());
        }

        return "redirect:/web/creditos";
    }

    // Mostrar formulario de abonos
    @GetMapping("/{id}/abonos")
    public String verAbonos(@PathVariable Integer id, Model model) {
        CreditoResponseDTO credito = creditoService.buscarPorId(id);
        model.addAttribute("credito", credito);
        return "creditos/abonos";
    }

    // Procesar el abono
    @PostMapping("/{id}/abonar")
    public String procesarAbono(@PathVariable Integer id,
                                @RequestParam BigDecimal montoAbono,
                                @RequestParam(required = false) String observaciones,
                                RedirectAttributes redirectAttributes) {
        try {
            CreditoResponseDTO credito = creditoService.buscarPorId(id);

            // Validar que el monto no sea mayor al saldo pendiente
            if (montoAbono.compareTo(credito.getSaldoPendiente()) > 0) {
                redirectAttributes.addFlashAttribute("error",
                        "El monto del abono no puede ser mayor al saldo pendiente");
                return "redirect:/web/creditos/" + id + "/abonos";
            }

            // Calcular nuevo saldo
            BigDecimal nuevoSaldo = credito.getSaldoPendiente().subtract(montoAbono);
            String nuevoEstado = nuevoSaldo.compareTo(BigDecimal.ZERO) == 0 ? "PAGADO" : "ACTIVO";

            // Actualizar crédito
            CreditoRequestDTO creditoDTO = CreditoRequestDTO.builder()
                    .idVenta(credito.getIdVenta())
                    .cedula(credito.getIdCliente())
                    .nombre(credito.getNombre())
                    .montoTotal(credito.getMontoTotal())
                    .saldoPendiente(nuevoSaldo)
                    .estado(nuevoEstado)
                    .build();

            creditoService.actualizar(id, creditoDTO);

            // TODO: Aquí puedes registrar el abono en una tabla de historial de pagos

            String mensaje = nuevoSaldo.compareTo(BigDecimal.ZERO) == 0
                    ? "¡Crédito pagado completamente!"
                    : "Abono registrado exitosamente. Nuevo saldo: $" +
                    String.format("%,.2f", nuevoSaldo);

            redirectAttributes.addFlashAttribute("mensaje", mensaje);
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");

            return "redirect:/web/creditos/ver/" + id;

        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error",
                    "Error al procesar el abono: " + e.getMessage());
            return "redirect:/web/creditos/" + id + "/abonos";
        }
    }
}