IF NOT EXISTS(SELECT 1 FROM  information_schema.COLUMNS WHERE TABLE_SCHEMA='auto_code' AND table_name='pro_fun' AND COLUMN_NAME='gen_service') THEN
     ALTER TABLE pro_fun ADD COLUMN gen_service VARCHAR(10) varchar(10) NOT NULL DEFAULT 'yes' COMMENT 'yes;no';
END IF;