package com.AppRH.AppRH.controllers;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.AppRH.AppRH.models.Candidato;
import com.AppRH.AppRH.models.Vaga;

import com.AppRH.AppRH.repository.CandidatoRepository;
import com.AppRH.AppRH.repository.VagaRepository;



@Controller
public class VagaController {

    private VagaRepository vagaRepository;
    private CandidatoRepository candidatoRepository;

    // Cadastrar Vaga
    @RequestMapping(value = "/cadastrarVaga", method = RequestMethod.GET)
    public String form() {
        return "vaga/formVaga";
    }
    
    @RequestMapping(value = "/cadastrarVaga", method = RequestMethod.POST)
    public String form(@Valid Vaga vaga, BindingResult result, RedirectAttributes attributes) {

        if (result.hasErrors()) {
            attributes.addFlashAttribute("mensagem", "Verifique os campos");
            return "redirect:/cadastrarVaga";
        }

        vagaRepository.save(vaga);
        attributes.addFlashAttribute("mensagem", "Vaga cadastrada com sucesso!");
        
        return "redirect:/cadastrarVaga";
    }

    // Listar Vagas
    @RequestMapping(value = "/vagas", method = RequestMethod.GET)
    public ModelAndView listarVagas() {
        ModelAndView modelAndView = new ModelAndView("vaga/listaVaga");

        Iterable<Vaga> vagas = vagaRepository.findAll();
        modelAndView.addObject("vagas", vagas);

        return modelAndView;
    }

    // Detalhar Vaga
    @RequestMapping(value = "/{codigo}", method = RequestMethod.GET)
    public ModelAndView detalharVaga(@PathVariable("codigo") long codigo) {
        
        Vaga vaga = vagaRepository.findByCodigo(codigo);

        ModelAndView modelAndView = new ModelAndView("vaga/detalhesVaga");

        modelAndView.addObject("vaga", vaga);

        Iterable<Candidato> candidatos = candidatoRepository.findByVaga(vaga);
        modelAndView.addObject("candidatos", candidatos);

        return modelAndView;
    }


    public String detalhesVagaPost(@PathVariable("codigo") long codigo, @Valid Candidato candidato,
            BindingResult result, RedirectAttributes attributes) {
        
        if (result.hasErrors()) {
            attributes.addFlashAttribute("mensagem", "Verifique os campos");
            
            return "redirect:/{codigo}";
        }

        // CPF duplicado
        if (candidatoRepository.findByCpf(candidato.getCPF()) != null) {
            attributes.addFlashAttribute("mensagem erro", "CPF duplicado");

            return "redirect:/{codigo}";
        }
        
        Vaga vaga = vagaRepository.findByCodigo(codigo);

        candidato.setVaga(vaga);
        candidatoRepository.save(candidato);
        attributes.addFlashAttribute("mensagem", "Candidato adicionado com sucesso!");

        return "redirect:/{codigo}";
    }

    // Deleta vaga
    @RequestMapping("/deletarVaga")
    public String deletarVaga(long codigo) {

        Vaga vaga = vagaRepository.findByCodigo(codigo);

        vagaRepository.delete(vaga);

        return "redirect:/vagas";
    }

    //Deleta candidato pelo CPF
    @RequestMapping("/deletarCandidato")
    public String deletarCandidato(String cpf) {
        
        Candidato candidato = candidatoRepository.findByCpf(cpf);
        Vaga vaga = candidato.getVaga();
        String codigoDaVaga = "" + vaga.getCodigo();

        candidatoRepository.delete(candidato);

        return "redirect:/" + codigoDaVaga;
    }



    // MÉTODOS QUE ATUALIZAM VAGA


    // formulario de edição de vaga (APENAS VIEW)
    @RequestMapping(value = "/editar-vaga", method = RequestMethod.GET)
    public ModelAndView editarVaga(long codigo) {
        
        Vaga vaga = vagaRepository.findByCodigo(codigo);

        ModelAndView modelAndView = new ModelAndView("vaga/update-vaga");

        modelAndView.addObject("vaga", vaga);

        return modelAndView;
    }

    @RequestMapping(value = "/editar-vaga", method = RequestMethod.POST)
    public String updateVaga(@Valid Vaga vaga, BindingResult result, RedirectAttributes attributes) {

        vagaRepository.save(vaga);

        attributes.addFlashAttribute("success", "vaga alterada com sucesso!");

        String codigo = "" + vaga.getCodigo();

        return "redirect:/" + codigo;
    }

}
