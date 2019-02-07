CREATE OR REPLACE FUNCTION fn_insert_new_rating ( rating integer, curr_business integer )
RETURNS integer as $_$
DECLARE 
	my_init_rating 	  numeric;
	my_num_of_ratings numeric;
BEGIN
	SELECT avg_rating INTO my_init_rating
	  FROM tb_rating
	 WHERE business = curr_business;

	SELECT num_ratings INTO my_num_of_ratings
	  FROM tb_rating
	 WHERE business = curr_business;
	 
	UPDATE tb_rating
	   SET avg_rating = my_init_rating * my_num_of_ratings + rating,
	       num_ratings = my_num_of_ratings + 1;

	RETURN my_init_rating * my_num_of_ratings + rating;
END;
 $_$
LANGUAGE plpgsql;