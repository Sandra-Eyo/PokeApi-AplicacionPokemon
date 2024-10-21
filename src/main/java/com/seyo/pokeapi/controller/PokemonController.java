package com.seyo.pokeapi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Random;

@Controller
public class PokemonController {

    @GetMapping("/")
    public String index(Model model) {
        // Generar un número aleatorio entre 1 y 151 (Primera generación de Pokémon)
        Random random = new Random();
        int numeroPokemon = random.nextInt(151) + 1;  // +1 para que vaya de 1 a 151

        // URL de la PokeAPI para obtener los datos del Pokémon por su número
        String url = "https://pokeapi.co/api/v2/pokemon/" + numeroPokemon;
        RestTemplate restTemplate = new RestTemplate();

        try {
            // Mapear la respuesta de la API a un Map
            Map<String, Object> pokemonData = restTemplate.getForObject(url, Map.class);

            if (pokemonData != null) {
                // Obtener el nombre del Pokémon
                String nombrePokemon = (String) pokemonData.get("name");

                // Obtener la imagen del Pokémon
                Map<String, Object> sprites = (Map<String, Object>) pokemonData.get("sprites");
                String imagenPokemon = (String) sprites.get("front_default");

                // Pasar los datos al modelo para la vista
                model.addAttribute("nombrePokemon", nombrePokemon);
                model.addAttribute("imagenPokemon", imagenPokemon);
            }
        } catch (Exception e) {
            model.addAttribute("message", "Hubo un error al obtener los datos del Pokémon. Intenta nuevamente.");
        }

        return "index"; // Renderizar la página principal
    }

    @PostMapping("/adivinar")
    public String adivinarPokemon(@RequestParam("nombreUsuario") String nombreUsuario,
                                  @RequestParam("nombrePokemon") String nombrePokemon,
                                  @RequestParam("imagenPokemon") String imagenPokemon,
                                  Model model) {

        // Comprobamos si la respuesta es correcta
        if (nombreUsuario.trim().equalsIgnoreCase(nombrePokemon)) {
            model.addAttribute("message", "¡Correcto! El Pokémon es " + nombrePokemon + ".");
        } else {
            model.addAttribute("message", "Incorrecto. El Pokémon era " + nombrePokemon + ".");
        }

        // Mantener la imagen del Pokémon
        model.addAttribute("imagenPokemon", imagenPokemon);

        return "index"; // Renderizar la página con el resultado
    }

    @GetMapping("/reiniciar")
    public String reiniciar(Model model) {
        return "redirect:/"; // Reiniciar y seleccionar otro Pokémon
    }
}
