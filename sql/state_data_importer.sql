--description: update mlx line item tracking

CREATE TEMP TABLE tt_state_importer
(
    uppercase_state       VARCHAR,
    lowercase_state 	  VARCHAR,
    iso 				  VARCHAR
);

\copy tt_state_importer from '~/Desktop/junior_design/sql/us_states.csv' delimiter ',' csv;

INSERT INTO tb_state ( description, iso_code )
SELECT lowercase_state, iso
FROM tt_state_importer;
