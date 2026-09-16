// Placeholder de Nicolás (REST/errores). Mantener los nombres de función si se reescribe.

const BASE_URL = '/api/boards';

export class ApiClientError extends Error {
    constructor(status, code, message) {
        super(message);
        this.status = status;
        this.code = code;
    }
}

async function handleResponse(response) {
    if (response.ok) {
        return response.json();
    }
    let body = null;
    try {
        body = await response.json();
    } catch {
        body = null;
    }
    throw new ApiClientError(
        response.status,
        body?.code ?? 'UNKNOWN_ERROR',
        body?.message ?? `La operación falló con estado ${response.status}`
    );
}

export async function createBoard(name) {
    const response = await fetch(BASE_URL, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ name }),
    });
    return handleResponse(response);
}

export async function loadBoard(boardId) {
    const response = await fetch(`${BASE_URL}/${boardId}`);
    return handleResponse(response);
}

export async function saveBoard(boardId, board) {
    const response = await fetch(`${BASE_URL}/${boardId}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ name: board.name, elements: board.elements }),
    });
    return handleResponse(response);
}
