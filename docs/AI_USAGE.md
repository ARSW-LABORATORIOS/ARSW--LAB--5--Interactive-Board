# AI Usage Declaration

Declaring AI use does not reduce the grade. You must be able to explain and validate every submitted decision.

| Tool      | Activity          | Prompt / purpose                                               | How I validated the result                                                  | What I changed / rejected                                         |
|-----------|-------------------|----------------------------------------------------------------|-----------------------------------------------------------------------------|-------------------------------------------------------------------|
| ChatGPT   | Documentation     | Support for the ADR, README and architecture diagrams.         | Compared the suggestions with the implemented code and tests.               | Adapted the suggestions to the final project.                     |
| Claude    | Code and analysis | Support with code and understanding the exercise requirements. | We reviewed the code and compared it with the requirements of each section. | We adapted the suggestions according to the project requirements. |
| Claude    | Frontend (my part: board-view.js) | Lo usé como guía para armar mi parte (board-view.js): renderizar RECTANGLE/TEXT/CONNECTOR en SVG. | Levanté el backend local y probé todo a mano en el navegador: cree un board, agregué rectángulo y texto, los moví, los conecté, los borré y guardé/recargué para confirmar que quedaba. | Le pedí que le quitara los prompt()/alert() del navegador y los cambiara por inputs normales del toolbar, y le cambié los colores a mi gusto. |