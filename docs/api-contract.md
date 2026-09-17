# REST Contract — Lab 05

Base path: `/api/boards`

Todos los `Board` se serializan como:

```json
{
  "id": "string",
  "name": "string",
  "elements": [
    {
      "id": "string",
      "type": "RECTANGLE | TEXT | CONNECTOR",
      "x": 0,
      "y": 0,
      "width": 0,
      "height": 0,
      "text": "string",
      "sourceId": "string | null",
      "targetId": "string | null"
    }
  ]
}
```

## BoardElement

Cada elemento tiene un `id` y un `type`.

### RECTANGLE

Utiliza `x`, `y`, `width` y `height`. El campo `text` es opcional. `sourceId` y `targetId` no se utilizan.

```json
{
  "id": "rect-1",
  "type": "RECTANGLE",
  "x": 100,
  "y": 100,
  "width": 120,
  "height": 60,
  "text": "",
  "sourceId": null,
  "targetId": null
}
```

### TEXT

Utiliza `x`, `y` y `text`. `sourceId` y `targetId` no se utilizan.

```json
{
  "id": "text-1",
  "type": "TEXT",
  "x": 100,
  "y": 100,
  "width": 0,
  "height": 0,
  "text": "Example",
  "sourceId": null,
  "targetId": null
}
```

### CONNECTOR

Representa una conexión entre dos elementos existentes del mismo Board mediante `sourceId` y `targetId`.

```json
{
  "id": "connector-1",
  "type": "CONNECTOR",
  "x": 0,
  "y": 0,
  "width": 0,
  "height": 0,
  "text": "",
  "sourceId": "rect-1",
  "targetId": "rect-2"
}
```

Un `CONNECTOR` debe cumplir las siguientes invariantes:

- `sourceId` es obligatorio.
- `targetId` es obligatorio.
- `sourceId` y `targetId` deben ser diferentes.
- El elemento indicado por `sourceId` debe existir en el mismo Board.
- El elemento indicado por `targetId` debe existir en el mismo Board.

| Method | Resource | Request | Success response | Error cases |
|---|---|---|---|---|
| POST | `/api/boards` | `{"name": "string"}` | `201 Created` + `Board` (id generado por el servidor, `elements: []`) | `400 INVALID_REQUEST` si `name` es vacío/nulo |
| GET | `/api/boards/{boardId}` | - | `200 OK` + `Board` | `404 BOARD_NOT_FOUND` si el id no existe |
| PUT | `/api/boards/{boardId}` | `{"name": "string", "elements": [BoardElement...]}` | `200 OK` + `Board` reemplazado (conserva el `id` de la URL) | `404 BOARD_NOT_FOUND` si el Board no existe; `400 INVALID_REQUEST`/`INVALID_INPUT` si los datos violan el contrato o las invariantes |
 
## Error contract

Toda respuesta de error usa el mismo cuerpo `ApiError`, sin exponer stack traces ni mensajes internos de Java:

```json
{
  "timestamp": "2026-09-17T10:15:30Z",
  "status": 404,
  "code": "BOARD_NOT_FOUND",
  "message": "Board not found: <boardId>",
  "path": "/api/boards/<boardId>"
}
```

| Code | HTTP status | Cuándo ocurre |
|---|---|---|
| `BOARD_NOT_FOUND` | 404 | GET/PUT sobre un `boardId` que no existe |
| `INVALID_REQUEST` | 400 | Falla la validación del body de la petición |
| `INVALID_INPUT` | 400 | El dominio rechaza los datos de `Board` o `BoardElement`, incluyendo invariantes inválidas de `CONNECTOR` |
| `MALFORMED_REQUEST` | 400 | Body ausente o JSON mal formado |
| `INTERNAL_ERROR` | 500 | Error inesperado no mapeado a los casos anteriores |

El Lab 05 mantiene los endpoints definidos previamente. La evolución del contrato se realiza sobre `BoardElement` mediante la incorporación de `CONNECTOR`, `sourceId` y `targetId`, sin agregar endpoints específicos para la interfaz gráfica.