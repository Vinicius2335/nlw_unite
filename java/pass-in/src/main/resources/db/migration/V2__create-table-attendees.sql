CREATE TABLE attendees (
    id INTEGER NOT NULL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email  VARCHAR(255) NOT NULL,
    event_id VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT attendees_event_id_fk FOREIGN KEY (event_id)
    REFERENCES events (id) ON DELETE RESTRICT ON UPDATE CASCADE
);

CREATE SEQUENCE sequence_attendees START WITH 1001 INCREMENT BY 1

-- ON DELETE RESTRICT -> caso alguem tente deletar um evento que tenha participantes, não será possivel
-- ON UPDATE CASCADE -> caso o evento sejá alterado, essa ação tambem vai refletir na tabela attendees