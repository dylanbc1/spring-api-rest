package taller2.entrega.taller2.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import taller2.entrega.taller2.persistence.models.Libro;
import taller2.entrega.taller2.service.IProjectService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import java.util.List;

@RestController
@RequestMapping("/")
public class ProjectController {
    private IProjectService projectService;
    public ProjectController(IProjectService projectService){
        this.projectService = projectService;
        projectService.create(new Libro("Mil años de soledad", "01", "Norma",
                "Gabriel García Marquez", 199, 1000));
    }

    @GetMapping("/autores")
    public String listAllAuthors(){
        this.projectService
    }

    @GetMapping()
    public String getHome(){
        return "home";
    }

    @GetMapping("/libros")
    public String listAll(Model model){
        model.addAttribute("libros", projectService.listLibro());
        return "list_books";
    }

    @GetMapping("/libros/{id}")
    public String showDetails(@PathVariable String id, Model model) {
        Libro libro = this.projectService.search(id).orElse(null);
        if (libro == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity not found");
        }
        model.addAttribute("libro", libro);
        return "book_details";
    }

    @GetMapping("/libros/create")
    public String mostrarFormularioCreacion() {
    return "create_book";
}
    //
    @PostMapping("/libros/create")
    public String handleSubmit(Model model, @ModelAttribute Libro newLibro) {
        Libro created = this.projectService.create(newLibro);

        if (created != null){
            model.addAttribute("confirmation", 1);
        } else {
            model.addAttribute("confirmation", 0);
        }
        return "alert";
    }

    // edit
    @GetMapping("/libros/{id}/edit")
    public String editForm(Model model, @PathVariable String id){
        if (this.projectService.search(id).isPresent()) {
            model.addAttribute("libro", this.projectService.search(id).get());
            return "edit";
        } else {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "entity not found"
            );
        }
    }

    @PostMapping("/libros/edit")
    public String handleEdit(Model model, @ModelAttribute Libro newLibro) {
        Libro edited = this.projectService.edit(newLibro);

        if (edited != null){
            model.addAttribute("confirmation", 1);
        } else {
            model.addAttribute("confirmation", 0);
        }
        return "alert";
    }

    @GetMapping("/libros/{id}/delete")
    public String delete(Model model, @PathVariable String id){
        boolean deleted = projectService.delete(id);

        if (deleted){
            model.addAttribute("confirmation", 1);
        } else {
            model.addAttribute("confirmation", 0);
        }
        return "alert";
    }
}