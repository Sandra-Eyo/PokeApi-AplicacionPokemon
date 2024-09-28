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

        // URL de la PokeAPI
        String url = "https://pokeapi.co/api/v2/pokemon/" + nombrePokemon;
        RestTemplate restTemplate = new RestTemplate();

        try {
            // Mapear la respuesta a un Map
            Map<String, Object> pokemonData = restTemplate.getForObject(url, Map.class);

            // Verificar que los datos del Pokémon no son nulos
            if (pokemonData != null) {
                // Obtener la lista de habilidades
                List<Map<String, Object>> abilities = (List<Map<String, Object>>) pokemonData.get("abilities");

                // Obtener la primera habilidad si existe
                if (!abilities.isEmpty()) {
                    String habilidadPrincipal = ((Map<String, Object>) abilities.get(0).get("ability")).get("name").toString();

                    // Obtener la imagen del Pokémon
                    Map<String, Object> sprites = (Map<String, Object>) pokemonData.get("sprites");
                    String imagenPokemon = sprites.get("front_default").toString();

                    // Pasar datos al modelo para ser usados en la vista
                    model.addAttribute("nombrePokemon", nombrePokemon);
                    model.addAttribute("habilidadPrincipal", habilidadPrincipal);
                    model.addAttribute("imagenPokemon", imagenPokemon);
                    return "index"; // Renderizar la página principal con los datos del Pokémon
                }
            }

            // Si no se encuentran los datos, enviar mensaje de error
            model.addAttribute("message", "No se encontró el Pokémon '" + nombrePokemon + "'. Por favor, intenta con otro.");
        } catch (Exception e) {
            // Manejar posibles excepciones (por ejemplo, si el Pokémon no existe)
            model.addAttribute("message", "No se encontró el Pokémon '" + nombrePokemon + "'. Por favor, intenta con otro.");
        }

        return "index"; // Renderizar la página principal, incluso si hay error
    }

    @PostMapping("/adivinar")
    public String adivinarHabilidad(@RequestParam("nombrePokemon") String nombrePokemon,
                                    @RequestParam("habilidad") String habilidadUsuario,
                                    @RequestParam("habilidadPrincipal") String habilidadPrincipal,
                                    Model model) {

        // Comprobamos si la respuesta es correcta o erronea
        if (habilidadUsuario.equalsIgnoreCase(habilidadPrincipal)) {
            model.addAttribute("message", "¡Correcto! La habilidad principal de " + nombrePokemon + " es " + habilidadPrincipal + ".");
            model.addAttribute("messageType", "success"); // Mensaje de éxito
        } else {
            model.addAttribute("message", "Incorrecto. La habilidad principal de " + nombrePokemon + " es " + habilidadPrincipal + ".");
            model.addAttribute("messageType", "error"); // Mensaje de error
        }

        // Pasar datos de vuelta a la vista
        model.addAttribute("nombrePokemon", nombrePokemon);
        model.addAttribute("habilidadPrincipal", habilidadPrincipal);

        return "index"; // Renderizar la página principal con el resultado
    }
}
