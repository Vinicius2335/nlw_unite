CREATE UNIQUE INDEX events_slug_key ON events (slug);
-- para não expor o ID do evento usamos o slug (uma url do evento) como uma chave pública, por isso ela não pode repetir

CREATE UNIQUE INDEX attendees_event_id_email_key ON attendees (event_id, email);
-- atraves desse index controlamos que o participante não se inscreva no mesmo evento mais de uma vez

CREATE UNIQUE INDEX check_ins_attendee_id_key ON check_ins (attendee_id)
-- atraves desse index controlamos que o participante realiza o check in apenas uma vez

-- INDEXES vão otimizar as consultas, estabecem regras para manter a consistência dos nossos dados
-- essas regras vão atender nossas regras de negócio
-- resumindo: estamos declarando nossas regras de negócio no banco tambem
