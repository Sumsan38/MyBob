CREATE TABLE IF NOT EXISTS bob_recipe_ingredients (
	detail_ingredient_id INT auto_increment NOT NULL COMMENT 'pk',
	recipe_id INT NOT NULL COMMENT '레시피 ID (FK)',
	ingredient_id INT NOT NULL COMMENT '재료 ID (FK)',
	reg_id varchar(100) NOT NULL COMMENT '글쓴이 이메일',
    reg_date datetime DEFAULT NULL COMMENT '등록 날짜',
    mod_id varchar(100) DEFAULT NULL COMMENT '수정자 이메일',
    mod_date datetime DEFAULT NULL COMMENT '수정 날짜',
	CONSTRAINT bob_recipe_ingredients_pk PRIMARY KEY (detail_ingredient_id),
	CONSTRAINT bob_recipe_ingredients_bob_recipe_FK FOREIGN KEY (recipe_id) REFERENCES bob_recipe(recipe_id),
	CONSTRAINT bob_recipe_ingredients_bob_ingredients_FK FOREIGN KEY (ingredient_id) REFERENCES bob_ingredients(ingredient_id)
) COMMENT='레시피 재료 상세';
