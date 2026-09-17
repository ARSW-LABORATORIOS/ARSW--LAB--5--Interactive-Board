package edu.eci.arsw.collabboard.application.service;

import edu.eci.arsw.collabboard.application.exception.BoardNotFoundException;
import edu.eci.arsw.collabboard.domain.model.Board;
import edu.eci.arsw.collabboard.domain.model.BoardElement;
import edu.eci.arsw.collabboard.domain.model.ElementType;
import edu.eci.arsw.collabboard.infrastructure.persistence.InMemoryBoardRepository;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BoardApplicationServiceTest {

    private final BoardApplicationService service =
            new BoardApplicationService(new InMemoryBoardRepository());

    @Test
    void shouldCreateAndReadBoard() {
        Board created = service.createBoard("Architecture Session");
        Board loaded = service.getBoard(created.id());

        assertEquals(created, loaded);
    }

    @Test
    void shouldFailWithConcreteExceptionWhenBoardDoesNotExist() {
        assertThrows(BoardNotFoundException.class,
                () -> service.getBoard("missing-board"));
    }

    @Test
    void shouldReplaceBoardSuccessfully() {
        Board created = service.createBoard("Initial Name");
        BoardElement element = new BoardElement("e1", ElementType.RECTANGLE, 0, 0, 100, 50, "");

        Board replaced = service.replaceBoard(created.id(), "Updated Name", List.of(element));

        assertEquals(created.id(), replaced.id());
        assertEquals("Updated Name", replaced.name());
        assertEquals(1, replaced.elements().size());
    }

    @Test
    void shouldFailReplacingNonExistentBoard() {
        assertThrows(BoardNotFoundException.class,
                () -> service.replaceBoard("ghost-id", "Name", List.of()));
    }

    @Test
    void shouldGenerateUniqueIdsForEachBoard() {
        Board b1 = service.createBoard("Board One");
        Board b2 = service.createBoard("Board Two");

        assertNotEquals(b1.id(), b2.id());
    }

    @Test
    void shouldReplaceBoardWithValidConnector() {
        Board created = service.createBoard("Connector Board");

        BoardElement source = new BoardElement(
                "e1", ElementType.RECTANGLE, 10, 10, 100, 50, ""
        );

        BoardElement target = new BoardElement(
                "e2", ElementType.RECTANGLE, 200, 10, 100, 50, ""
        );

        BoardElement connector = new BoardElement(
                "c1", ElementType.CONNECTOR, 0, 0, 0, 0, "",
                "e1", "e2"
        );

        Board replaced = service.replaceBoard(
                created.id(),
                "Connector Board",
                List.of(source, target, connector)
        );

        assertEquals(3, replaced.elements().size());
        assertEquals(ElementType.CONNECTOR, replaced.elements().get(2).type());
        assertEquals("e1", replaced.elements().get(2).sourceId());
        assertEquals("e2", replaced.elements().get(2).targetId());
    }

    @Test
    void shouldRejectConnectorWithMissingSource() {
        Board created = service.createBoard("Connector Board");

        BoardElement target = new BoardElement(
                "e2", ElementType.RECTANGLE, 200, 10, 100, 50, ""
        );

        BoardElement connector = new BoardElement(
                "c1", ElementType.CONNECTOR, 0, 0, 0, 0, "",
                "missing", "e2"
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> service.replaceBoard(
                        created.id(),
                        "Connector Board",
                        List.of(target, connector)
                )
        );
    }

    @Test
    void shouldRejectConnectorWithMissingTarget() {
        Board created = service.createBoard("Connector Board");

        BoardElement source = new BoardElement(
                "e1", ElementType.RECTANGLE, 10, 10, 100, 50, ""
        );

        BoardElement connector = new BoardElement(
                "c1", ElementType.CONNECTOR, 0, 0, 0, 0, "",
                "e1", "missing"
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> service.replaceBoard(
                        created.id(),
                        "Connector Board",
                        List.of(source, connector)
                )
        );
    }

    @Test
    void shouldRejectConnectorWithSameSourceAndTarget() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new BoardElement(
                        "c1", ElementType.CONNECTOR, 0, 0, 0, 0, "",
                        "e1", "e1"
                )
        );
    }

    @Test
    void shouldRejectConnectorWithoutSource() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new BoardElement(
                        "c1", ElementType.CONNECTOR, 0, 0, 0, 0, "",
                        null, "e2"
                )
        );
    }

    @Test
    void shouldRejectConnectorWithoutTarget() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new BoardElement(
                        "c1", ElementType.CONNECTOR, 0, 0, 0, 0, "",
                        "e1", null
                )
        );
    }
}
