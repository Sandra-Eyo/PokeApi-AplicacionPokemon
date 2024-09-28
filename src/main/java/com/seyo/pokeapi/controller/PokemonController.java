package com.seyo.pokeapi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Controller
public class PokemonController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/buscar")
    public String buscarPokemon(@RequestParam("nombrePokemon") String nombrePokemon, Model model) {
        // Convertir a minúsculas
        nombrePokemon = nombrePokemon.trim().toLowerCase();

        // Obtener datos del Pokémon desde la PokeAPI
        String url = "https://pokeapi.co/api/v2/pokemon/" + nombrePokemon;
        RestTemplate restTemplate = new RestTemplate();

        try {
            // Mapear la respuesta a un Map
            Map<String, Object> pokemonData = restTemplate.getForObject(url, Map.class);

            // Obtener la primera habilidad y la imagen del Pokémon
            if (pokemonData != null) {
                var abilities = (List<Map<String, Object>>) pokemonData.get("abilities");
                String habilidadPrincipal = ((Map<String, Object>) abilities.get(0).get("ability")).get("name").toString();
                String imagenPokemon = ((Map<String, Object>) ((Map<String, Object>) pokemonData.get("sprites")).get("front_default")).toString();

                // Pasar datos a la vista
                model.addAttribute("nombrePokemon", nombrePokemon);
                model.addAttribute("habilidadPrincipal", habilidadPrincipal);
                model.addAttribute("imagenPokemon", imagenPokemon);
            }
        } catch (Exception e) {
            model.addAttribute("message", "No se encontró el Pokémon '" + nombrePokemon + "'. Por favor, intenta con otro.");
        }

        return "index";
    }

    @PostMapping("/adivinar")
    public String adivinarHabilidad(@RequestParam("nombrePokemon") String nombrePokemon,
                                    @RequestParam("habilidad") String habilidadUsuario,
                                    @RequestParam("habilidadPrincipal") String habilidadPrincipal,
                                    Model model) {

        // Comprobamos si la respuesta es correcta
        if (habilidadUsuario.equalsIgnoreCase(habilidadPrincipal)) {
            model.addAttribute("message", "¡Correcto! La habilidad principal de " + nombrePokemon + " es " + habilidadPrincipal + ".");
        } else {
            model.addAttribute("message", "Incorrecto. La habilidad principal de " + nombrePokemon + " es " + habilidadPrincipal + ".");
        }

        // Pasar datos de vuelta a la vista
        model.addAttribute("nombrePokemon", nombrePokemon);
        model.addAttribute("habilidadPrincipal", habilidadPrincipal);

        return "index";
    }
}
