# 📊 API DE RICK Y MORTY

## Estructura de la Respuesta JSON

```json
{
  "info": {
    "count": 826,
    "pages": 42,
    "next": "https://rickandmortyapi.com/api/character/?page=2",
    "prev": null
  },
  "results": [
    {
      "id": 1,
      "name": "Rick Sanchez",
      "status": "Alive",
      "species": "Human",
      "type": "",
      "gender": "Male",
      "origin": {
        "name": "Earth (C-137)",
        "url": "https://rickandmortyapi.com/api/location/1"
      },
      "location": {
        "name": "Citadel of Ricks",
        "url": "https://rickandmortyapi.com/api/location/3"
      },
      "image": "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
      "episode": [
        "https://rickandmortyapi.com/api/episode/1",
        "https://rickandmortyapi.com/api/episode/2"
      ],
      "url": "https://rickandmortyapi.com/api/character/1",
      "created": "2017-11-04T18:48:46.250Z"
    }
  ]
}
```

## Ciclo Petición/Respuesta con Rick y Morty API

```javascript
// 1. CLIENTE hace petición
fetch('https://rickandmortyapi.com/api/character/?name=rick')

// 2. SERVIDOR responde con JSON
{
  "info": { "count": 826 },
  "results": [ { "name": "Rick Sanchez", ... } ]
}

// 3. CLIENTE procesa y muestra
const data = await response.json();
displayCharacters(data.results);
```


# 🚀 EJECUTAR Y PROBAR

### Ejecutar index.html en el navegador

**Búsquedas Recomendadas:**

|Búsqueda	|Resultado Esperado|
|-----------|------------------|
|"rick"	|Personajes con "Rick" en el nombre|
|"morty"	|Personajes con "Morty" en el nombre|
|"summer"	|Summer Smith|
|"beth"	|Beth Smith|
|"birdperson"	|Birdperson|
|"pickle"	|Rick en forma de pepinillo|

-------
**Ver los Estados:**

1. Carga: La búsqueda muestra animación de carga
2. Datos: Lista de personajes con avatar y detalles
3. Vacío: Busca "xyzabc123" para ver mensaje
4. Error: Desconecta internet para ver error