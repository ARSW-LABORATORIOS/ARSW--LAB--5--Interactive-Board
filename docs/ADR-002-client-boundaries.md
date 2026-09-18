# ADR-002: Client Boundaries

## Context

Lab 05 evolves the Collaborative Board into an interactive web application. The client must communicate with the existing REST API, maintain the current Board state, render its elements using SVG, and support user interactions such as adding, selecting, moving, connecting, deleting, saving, and loading elements.

Keeping these responsibilities separated reduces coupling between HTTP communication, application state, and visual rendering.

## Decision

The web client is divided into four main modules:

- `BoardApp` in `app.js` coordinates the application flow and user actions.
- `BoardApiClient` in `board-api-client.js` is responsible for all HTTP communication with the REST API.
- `BoardState` in `board-state.js` maintains the current Board, selected element, remote operation state, and local element modifications.
- `BoardView` in `board-view.js` renders the Board using SVG and handles visual interactions.

All `fetch` operations are kept inside `BoardApiClient`. `BoardView` does not communicate directly with the backend and does not contain persistence rules. `BoardState` does not perform HTTP requests.

The backend REST API remains responsible for validating the domain model before persisting a Board. This includes the invariants required by `CONNECTOR`.

## Consequences

The client has clear boundaries between communication, state, rendering, and orchestration.

Changes to the REST communication can be isolated in `BoardApiClient`, while visual changes can be made in `BoardView` without modifying persistence logic.

The application state can evolve independently from the SVG representation.

The backend remains the final authority for domain validation when a Board is saved.

## Trade-off

This separation introduces more JavaScript modules and requires coordination between them through `BoardApp`.

For a small application, placing all behavior in a single JavaScript file could require less initial code. However, that approach would increase coupling and make future evolution more difficult.

The modular approach is preferred because the Collaborative Board is expected to continue evolving in later laboratories.

## Evidence

The implementation contains the following client modules:

- `src/main/resources/static/js/app.js`
- `src/main/resources/static/js/api/board-api-client.js`
- `src/main/resources/static/js/state/board-state.js`
- `src/main/resources/static/js/ui/board-view.js`

The backend keeps the existing layered structure:

- `BoardRestController`
- `BoardApplicationService`
- `BoardRepository`
- `InMemoryBoardRepository`

The application was validated with the following functional flow:

1. Create a Board through the REST API.
2. Add and move Board elements.
3. Create a `CONNECTOR` between two existing elements.
4. Save the complete Board state.
5. Reload the Board through the REST API.
6. Verify that the elements and `CONNECTOR` are preserved.
7. Delete a connected element and verify that its associated connector is removed.
8. Save and reload the Board and verify that the deleted state is preserved.

The automated test suite contains 18 passing tests, including tests for valid and invalid `CONNECTOR` invariants.