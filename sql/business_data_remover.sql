--description: Upload business data csv
CREATE TEMP TABLE tt_business_remover
(
    business_name         VARCHAR
);

\copy tt_business_remover from 'businesses_to_be_removed.csv' delimiter ',' csv header;

DELETE 
  FROM tb_entity_favorites
 WHERE business in 
	(
		SELECT business
  		FROM tb_business
 		WHERE name in 
		(
			SELECT business_name
		  	FROM tt_business_remover
		)
	)
;

DELETE 
  FROM tb_business_category
 WHERE business in 
	(
		SELECT business
  		FROM tb_business
 		WHERE name in 
		(
			SELECT business_name
		  	FROM tt_business_remover
		)
	)
;

DELETE 
  FROM tb_business_subcategory
 WHERE business in 
	(
		SELECT business
  		FROM tb_business
 		WHERE name in 
		(
			SELECT business_name
		  	FROM tt_business_remover
		)
	)
;

DELETE 
  FROM tb_review
 WHERE business in 
	(
		SELECT business
  		FROM tb_business
 		WHERE name in 
		(
			SELECT business_name
		  	FROM tt_business_remover
		)
	)
;

DELETE 
  FROM tb_business
 WHERE name in 
	(
		SELECT business_name
		  FROM tt_business_remover
	)
;