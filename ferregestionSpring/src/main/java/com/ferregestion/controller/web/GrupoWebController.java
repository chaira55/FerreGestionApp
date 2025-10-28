package com.ferregestion.controller.web;

import com.ferregestion.dto.request.GrupoRequestDTO;
import com.ferregestion.dto.response.GrupoResponseDTO;
import com.ferregestion.service.GrupoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/web/grupos")
public class GrupoWebController {

    private final GrupoService grupoService;

    public GrupoWebController(GrupoService grupoService) {
        this.grupoService = grupoService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("grupos", grupoService.listarTodos());
        return "grupos/lista";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("grupo", new GrupoRequestDTO());
        model.addAttribute("accion", "Nuevo");
        return "grupos/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("grupo") GrupoRequestDTO grupoDTO,
                          BindingResult result,
                          RedirectAttributes redirectAttributes,
                          Model model) {

        if (result.hasErrors()) {
            model.addAttribute("accion", "Nuevo");
            return "grupos/formulario";
        }

        try {
            grupoService.guardar(grupoDTO);
            redirectAttributes.addFlashAttribute("mensaje", "Grupo creado exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
            return "redirect:/web/grupos";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("accion", "Nuevo");
            return "grupos/formulario";
        }
    }

    @GetMapping("/editar/{codigo}")
    public String mostrarFormularioEditar(@PathVariable String codigo, Model model) {
        try {
            GrupoResponseDTO grupo = grupoService.buscarPorId(codigo);

            GrupoRequestDTO grupoDTO = GrupoRequestDTO.builder()
                    .codigoGrupo(grupo.getCodigoGrupo())
                    .nombre(grupo.getNombre())
                    .iva(grupo.getIva())
                    .build();

            model.addAttribute("grupo", grupoDTO);
            model.addAttribute("accion", "Editar");
            return "grupos/formulario";
        } catch (Exception e) {
            return "redirect:/web/grupos";
        }
    }

    @PostMapping("/actualizar/{codigo}")
    public String actualizar(@PathVariable String codigo,
                             @Valid @ModelAttribute("grupo") GrupoRequestDTO grupoDTO,
                             BindingResult result,
                             RedirectAttributes redirectAttributes,
                             Model model) {

        if (result.hasErrors()) {
            model.addAttribute("accion", "Editar");
            return "grupos/formulario";
        }

        try {
            grupoService.actualizar(codigo, grupoDTO);
            redirectAttributes.addFlashAttribute("mensaje", "Grupo actualizado exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
            return "redirect:/web/grupos";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("accion", "Editar");
            return "grupos/formulario";
        }
    }

    @GetMapping("/eliminar/{codigo}")
    public String eliminar(@PathVariable String codigo, RedirectAttributes redirectAttributes) {
        try {
            grupoService.eliminar(codigo);
            redirectAttributes.addFlashAttribute("mensaje", "Grupo eliminado exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error al eliminar: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "danger");
        }
        return "redirect:/web/grupos";
    }
}