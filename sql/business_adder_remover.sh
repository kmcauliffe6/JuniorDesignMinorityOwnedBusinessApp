psql -U benjaminYarmowich -h blackbusinessdb.c8mjkzcpmkqm.us-east-2.rds.amazonaws.com -d black_business -f business_data_remover.sql

psql -U benjaminYarmowich -h blackbusinessdb.c8mjkzcpmkqm.us-east-2.rds.amazonaws.com -d black_business -f business_data_importer.sql
