--description: Upload business data csv
CREATE TEMP TABLE tt_business_importer
(
    business_name         VARCHAR,
    address               VARCHAR,
    city                  VARCHAR,
    state                 VARCHAR,
    po_box                VARCHAR,
    contact               VARCHAR,
    phone_number          VARCHAR,
    email                 VARCHAR,
    website               VARCHAR,
    certifications        VARCHAR,
    category              VARCHAR,
    subcategories         VARCHAR,
    description           VARCHAR,
    about                 VARCHAR
);

\copy tt_state_importer from '~/Desktop/junior_design/sql/us_states.csv' delimiter ',' csv;

INSERT INTO tb_business 
    ( 
        contact,
        email,
        name,
        description,
        about,
        address_line_one,
        phone_number,
        po_box,
        website,
        zip_code,
        city,
        state
    )
SELECT  tt.contact,
        tt.email,
        tt.name,
        tt.description,
        tt.about,
        tt.address,
        tt.phone_number,
        tt.po_box,
        tt.website,
        tt.zip_code,
        tt.city,
        s.state
FROM tt_business_importer
LEFT JOIN tb_state s
WHERE s.iso_code = tt.state
ON CONFLICT DO NOTHING;
