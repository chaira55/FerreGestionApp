package com.ferregestion.controller.web;

import com.ferregestion.service.ClienteService;
import com.ferregestion.service.ProductoService;
import com.ferregestion.service.VentaService;
import com.ferregestion.service.CreditoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.time.LocalDate;

@Controller
public class DashboardController {

    private final ClienteService clienteService;
    private final ProductoService productoService;
    private final VentaService ventaService;
    private final CreditoService creditoService;

    public DashboardController(ClienteService clienteService,
                               ProductoService productoService,
                               VentaService ventaService,
                               CreditoService creditoService) {
        this.clienteService = clienteService;
        this.productoService = productoService;
        this.ventaService = ventaService;
        this.creditoService = creditoService;
    }

    @GetMapping("/")
    public String dashboard(Model model) {

        // ESTADÍSTICAS BÁSICAS

        // Total de clientes
        long totalClientes = clienteService.listarTodos().size();

        // Total de productos
        long totalProductos = productoService.listarTodos().size();

        // Ventas del mes actual
        LocalDate inicioMes = LocalDate.now().withDayOfMonth(1);
        LocalDate finMes = LocalDate.now();

        BigDecimal totalVentasMes = ventaService.listarTodos().stream()
                .filter(v -> !v.getFecha().isBefore(inicioMes) && !v.getFecha().isAfter(finMes))
                .map(v -> v.getTotal())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Créditos activos (con saldo pendiente)
        long totalCreditosActivos = creditoService.listarTodos().stream()
                .filter(c -> c.getSaldoPendiente() != null && c.getSaldoPendiente().compareTo(BigDecimal.ZERO) > 0)
                .count();

        // Productos con stock bajo (menos de 10 unidades)
        var productosStockBajo = productoService.listarTodos().stream()
                .filter(p -> p.getStock() != null && p.getStock() < 10)
                .limit(10) // Máximo 10 productos
                .toList();

        // Agregar atributos al modelo
        model.addAttribute("totalClientes", totalClientes);
        model.addAttribute("totalProductos", totalProductos);
        model.addAttribute("totalVentasMes", totalVentasMes);
        model.addAttribute("totalCreditosActivos", totalCreditosActivos);
        model.addAttribute("productosStockBajo", productosStockBajo);

        return "index";
    }
}