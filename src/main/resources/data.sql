-- =============================================================================
-- data.sql — Datos de ejemplo
-- =============================================================================

-- Usuario de ejemplo
INSERT INTO users (id, name, email, password, created, modified, last_login,
                   token, is_active)
SELECT '00000000-0000-0000-0000-000000000001',
       'Juan Rodriguez',
       'demo.user@dominio.cl',
       'hunter2',
       '2026-10-07 00:00:00',
       '2026-10-07 00:00:00',
       '2026-10-07 00:00:00',
       'TOKEN_DE_DEMO_NO_ES_UN_JWT_VALIDO',
       TRUE
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'demo.user@dominio.cl');

-- Telefono del usuario demo
INSERT INTO phones (id, number, citycode, contrycode, user_id)
SELECT '00000000-0000-0000-0000-000000000101',
       '1234567',
       '1',
       '57',
       '00000000-0000-0000-0000-000000000001'
WHERE NOT EXISTS (SELECT 1 FROM phones WHERE id =
                  '00000000-0000-0000-0000-000000000101');

