<?php
session_start();

// Verificar si se ha enviado una solicitud para reiniciar
if (isset($_GET['action']) && $_GET['action'] === 'reiniciar') {
    // Reiniciar la sesión
    session_unset();
    session_destroy();
    header("Location: indecPHP.php");
    exit();
}

// Función para obtener datos del Pokémon desde la PokeAPI
function obtenerPokemon($nombrePokemon) {
    $url = "https://pokeapi.co/api/v2/pokemon/" . urlencode(trim(strtolower($nombrePokemon)));

    // Usar cURL para hacer la solicitud a la PokeAPI
    $ch = curl_init($url);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    $response = curl_exec($ch);
    curl_close($ch);

    return json_decode($response, true);
}

// Manejar la búsqueda de Pokémon
if ($_SERVER['REQUEST_METHOD'] === 'POST' && isset($_POST['buscar'])) {
    $nombrePokemon = $_POST['nombrePokemon'];
    $pokemonData = obtenerPokemon($nombrePokemon);

    // Verificar que los datos del Pokémon no son nulos
    if ($pokemonData) {
        $habilidades = $pokemonData['abilities'];
        
        // Obtener la primera habilidad si existe
        if (!empty($habilidades)) {
            $habilidadPrincipal = $habilidades[0]['ability']['name'];
            $imagenPokemon = $pokemonData['sprites']['front_default'];

            // Guardar los datos en la sesión
            $_SESSION['nombrePokemon'] = $nombrePokemon;
            $_SESSION['habilidadPrincipal'] = $habilidadPrincipal;
            $_SESSION['imagenPokemon'] = $imagenPokemon;
            $_SESSION['message'] = null; // Limpiar mensaje

        } else {
            $_SESSION['message'] = "No se encontró el Pokémon '{$nombrePokemon}'. Por favor, intenta con otro.";
        }
    } else {
        $_SESSION['message'] = "No se encontró el Pokémon '{$nombrePokemon}'. Por favor, intenta con otro.";
    }
}

// Manejar la adivinanza de habilidad
if ($_SERVER['REQUEST_METHOD'] === 'POST' && isset($_POST['adivinar'])) {
    $nombrePokemon = $_SESSION['nombrePokemon'];
    $habilidadUsuario = $_POST['habilidad'];
    $habilidadPrincipal = $_SESSION['habilidadPrincipal'];
    $imagenPokemon = $_SESSION['imagenPokemon'];

    // Comprobar si la respuesta es correcta
    if (strcasecmp($habilidadUsuario, $habilidadPrincipal) === 0) {
        $_SESSION['message'] = "¡Correcto! La habilidad principal de {$nombrePokemon} es {$habilidadPrincipal}.";
    } else {
        $_SESSION['message'] = "Incorrecto. La habilidad principal de {$nombrePokemon} es {$habilidadPrincipal}.";
    }

    // Volver a guardar los datos en la sesión
    $_SESSION['nombrePokemon'] = $nombrePokemon;
    $_SESSION['habilidadPrincipal'] = $habilidadPrincipal;
    $_SESSION['imagenPokemon'] = $imagenPokemon;
}

// Variables para la vista
$nombrePokemon = $_SESSION['nombrePokemon'] ?? null;
$habilidadPrincipal = $_SESSION['habilidadPrincipal'] ?? null;
$imagenPokemon = $_SESSION['imagenPokemon'] ?? null;
$message = $_SESSION['message'] ?? null;

?>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Adivina la habilidad del Pokémon</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-gray-100 text-gray-800">

<div class="max-w-lg mx-auto mt-10 p-6 bg-white rounded-lg shadow-lg">
    <div class="flex items-center justify-center mb-4">
        <img src="https://raw.githubusercontent.com/PokeAPI/media/master/logo/pokeapi_256.png" alt="PokeAPI Logo">
    </div>
    <h1 class="text-2xl font-bold text-center">Adivina la habilidad del Pokémon</h1>
    <br>

    <!-- Formulario para preguntar el nombre del Pokémon, solo se muestra si no se ha buscado aún -->
    <?php if (is_null($nombrePokemon)): ?>
        <form action="" method="POST" class="mb-4">
            <div class="mb-4">
                <label for="nombrePokemon" class="block text-sm font-medium text-gray-700 mb-1">Introduce el nombre de un Pokémon:</label>
                <input type="text" id="nombrePokemon" name="nombrePokemon" class="w-full p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-indigo-500" required>
            </div>
            <button type="submit" name="buscar" class="w-full bg-blue-100 text-blue-800 py-2 rounded-lg hover:bg-blue-200 focus:outline-none focus:ring-2 focus:ring-blue-300">Buscar Pokémon</button>
        </form>
    <?php else: ?>
        <!-- Mostrar la imagen del Pokémon y el formulario de habilidad -->
        <div class="flex justify-center mb-4">
            <img src="<?php echo $imagenPokemon; ?>" alt="Imagen de <?php echo htmlspecialchars($nombrePokemon); ?>" class="w-32 h-32">
        </div>

        <form action="" method="POST" class="mb-4">
            <p class="text-lg text-center mb-2">Has elegido a <strong><?php echo htmlspecialchars($nombrePokemon); ?></strong>. ¿Cuál es su habilidad más importante?</p>
            <div class="mb-4">
                <label for="habilidad" class="block text-sm font-medium text-gray-700 mb-1">Introduce tu respuesta:</label>
                <input type="text" id="habilidad" name="habilidad" class="w-full p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-indigo-500" required>
            </div>
            <button type="submit" name="adivinar" class="w-full bg-blue-100 text-blue-800 py-2 rounded-lg hover:bg-blue-200 focus:outline-none focus:ring-2 focus:ring-blue-300">Adivinar habilidad</button>
        </form>
    <?php endif; ?>

    <!-- Mostrar el mensaje de resultado -->
    <?php if ($message): ?>
        <div class="<?php echo isset($habilidadPrincipal) && str_contains($message, '¡Correcto!') ? 'bg-green-100 border-l-4 border-green-500 text-green-700' : 'bg-red-100 border-l-4 border-red-500 text-red-700'; ?> p-4 mb-4" role="alert">
            <p><?php echo htmlspecialchars($message); ?></p>
        </div>

        <!-- Botón para reiniciar -->
        <form action="indecPHP.php" method="GET" class="mt-4">
            <input type="hidden" name="action" value="reiniciar">
            <button type="submit" class="inline-flex items-center rounded-md bg-gray-50 px-2 py-1 text-xs font-medium text-gray-600 ring-1 ring-inset ring-gray-500/10">Volver a empezar</button>
        </form>
    <?php endif; ?>
</div>

</body>
</html>

