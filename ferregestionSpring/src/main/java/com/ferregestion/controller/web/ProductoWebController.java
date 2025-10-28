package com.ferregestion.controller.web;

import com.ferregestion.dto.request.ProductoRequestDTO;
import com.ferregestion.dto.response.ProductoResponseDTO;
import com.ferregestion.service.GrupoService;
import com.ferregestion.service.ProductoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.math.BigDecimal;

@Controller
@RequestMapping("/web/productos")
public class ProductoWebController {

    private final ProductoService productoService;
    private final GrupoService grupoService;

    public ProductoWebController(ProductoService productoService,
                                 GrupoService grupoService) {
        this.productoService = productoService;
        this.grupoService = grupoService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("productos", productoService.listarTodos());
        return "productos/lista";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        ProductoRequestDTO productoDTO = ProductoRequestDTO.builder()
                .descripcion("")
                .codigoGrupo("")
                .iva(new BigDecimal("19.00"))
                .precioCompra(BigDecimal.ZERO)
                .precioVenta(BigDecimal.ZERO)
                .stock(0)
                .build();

        model.addAttribute("producto", productoDTO);
        model.addAttribute("grupos", grupoService.listarTodos());
        model.addAttribute("accion", "Nuevo");
        return "productos/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("producto") ProductoRequestDTO productoDTO,
                          BindingResult result,
                          RedirectAttributes redirectAttributes,
                          Model model) {

        if (productoDTO.getCodigoGrupo() != null && productoDTO.getCodigoGrupo().trim().isEmpty()) {
            productoDTO.setCodigoGrupo(null);
        }

        if (result.hasErrors()) {
            model.addAttribute("grupos", grupoService.listarTodos());
            model.addAttribute("accion", "Nuevo");
            return "productos/formulario";
        }

        try {
            productoService.guardar(productoDTO);
            redirectAttributes.addFlashAttribute("mensaje", "Producto creado exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
            return "redirect:/web/productos";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("grupos", grupoService.listarTodos());
            model.addAttribute("accion", "Nuevo");
            return "productos/formulario";
        }
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Integer id, Model model) {
        try {
            ProductoResponseDTO producto = productoService.buscarPorId(id);

            ProductoRequestDTO productoDTO = ProductoRequestDTO.builder()
                    .descripcion(producto.getDescripcion())
                    .codigoGrupo(producto.getGrupo() != null ? producto.getGrupo().getCodigoGrupo() : "")
                    .iva(producto.getIva() != null ? producto.getIva() : new BigDecimal("19.00"))
                    .precioCompra(producto.getPrecioCompra() != null ? producto.getPrecioCompra() : BigDecimal.ZERO)
                    .precioVenta(producto.getPrecioVenta() != null ? producto.getPrecioVenta() : BigDecimal.ZERO)
                    .stock(producto.getStock() != null ? producto.getStock() : 0)
                    .build();

            model.addAttribute("producto", productoDTO);
            model.addAttribute("productoId", id);
            model.addAttribute("grupos", grupoService.listarTodos());
            model.addAttribute("accion", "Editar");
            return "productos/formulario";
        } catch (Exception e) {
            return "redirect:/web/productos";
        }
    }

    @PostMapping("/actualizar/{id}")
    public String actualizar(@PathVariable Integer id,
                             @Valid @ModelAttribute("producto") ProductoRequestDTO productoDTO,
                             BindingResult result,
                             RedirectAttributes redirectAttributes,
                             Model model) {

        if (productoDTO.getCodigoGrupo() != null && productoDTO.getCodigoGrupo().trim().isEmpty()) {
            productoDTO.setCodigoGrupo(null);
        }

        if (result.hasErrors()) {
            model.addAttribute("productoId", id);
            model.addAttribute("grupos", grupoService.listarTodos());
            model.addAttribute("accion", "Editar");
            return "productos/formulario";
        }

        try {
            productoService.actualizar(id, productoDTO);
            redirectAttributes.addFlashAttribute("mensaje", "Producto actualizado exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
            return "redirect:/web/productos";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("productoId", id);
            model.addAttribute("grupos", grupoService.listarTodos());
            model.addAttribute("accion", "Editar");
            return "productos/formulario";
        }
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            productoService.eliminar(id);
            redirectAttributes.addFlashAttribute("mensaje", "Producto eliminado exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error al eliminar: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "danger");
        }
        return "redirect:/web/productos";
    }

    @GetMapping("/ver/{id}")
    public String ver(@PathVariable Integer id, Model model) {
        try {
            ProductoResponseDTO producto = productoService.buscarPorId(id);
            model.addAttribute("producto", producto);
            return "productos/detalle";
        } catch (Exception e) {
            return "redirect:/web/productos";
        }
    }
}