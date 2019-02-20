--description: Upload business data csv
CREATE TEMP TABLE tt_business_importer
(
    business_name         VARCHAR,
    address               VARCHAR,
    city                  VARCHAR,
    state                 VARCHAR,
    zip                   VARCHAR,
    po_box                VARCHAR,
    contact               VARCHAR,
    phone_number          VARCHAR,
    email                 VARCHAR,
    website               VARCHAR,
    certifications        VARCHAR,
    category              VARCHAR,
    subcategories         VARCHAR,
    tags                  VARCHAR,
    description           VARCHAR,
    about                 VARCHAR
);

CREATE TEMP TABLE tt_subcategories
(
    business_name         VARCHAR,
    subcategory           VARCHAR,
    category              VARCHAR
);

\copy tt_business_importer from '~/Desktop/junior_design/sql/business_data.csv' delimiter ',' csv header;

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
        tt.business_name,
        tt.description,
        tt.about,
        tt.address,
        tt.phone_number,
        tt.po_box,
        tt.website,
        tt.zip,
        tt.city,
        s.state
FROM tt_business_importer tt
LEFT JOIN tb_state s
ON s.iso_code like tt.state
ON CONFLICT DO NOTHING;

INSERT INTO tb_business_category 
    (
        business,
        category
    )
SELECT b.business,
       c.category
FROM tt_business_importer tt
LEFT JOIN tb_business b
    ON  b.name = tt.business_name
LEFT JOIN tb_category c
    ON  c.description like tt.category;

INSERT INTO tt_subcategories ( business_name, subcategory, category )
SELECT business_name, LOWER( unnest( regexp_split_to_array( subcategories, ', ' ) ) ), category
FROM tt_business_importer
ON CONFLICT DO NOTHING;

INSERT INTO tb_subcategory ( name )
SELECT subcategory
FROM   tt_subcategories
ON CONFLICT DO NOTHING;

INSERT INTO tb_business_subcategory (business, category, subcategory)
SELECT b.business,
       c.category,
       s.subcategory
FROM tt_subcategories tt
LEFT JOIN tb_business b
    ON b.name = tt.business_name
LEFT JOIN tb_subcategory s
    ON s.name = tt.subcategory
LEFT JOIN tb_category c
    ON c.description = tt.category
ON CONFLICT DO NOTHING;

