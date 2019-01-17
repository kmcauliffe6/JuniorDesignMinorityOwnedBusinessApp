--description: update mlx line item tracking

CREATE TEMP TABLE tt_state_importer
(
    uppercase_state       VARCHAR,
    lowercase_state 	  VARCHAR,
    iso 				  VARCHAR
);

\copy tt_line_item_tracking from 'us_states.csv' delimiter ',' csv header;

INSERT INTO tb_state ( description, iso )
SELECT lowercase_state, iso
FROM tt_state_importer;
