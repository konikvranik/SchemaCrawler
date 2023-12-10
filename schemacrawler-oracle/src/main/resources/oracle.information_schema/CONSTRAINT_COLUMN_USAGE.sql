SELECT
  NULL AS CONSTRAINT_CATALOG,
  TABLE_CONTRAINTS.OWNER AS CONSTRAINT_SCHEMA,
  TABLE_CONTRAINTS.CONSTRAINT_NAME,
  NULL AS TABLE_CATALOG,
  TABLE_CONTRAINTS.OWNER AS TABLE_SCHEMA,
  TABLE_CONTRAINTS.TABLE_NAME,
  COLUMNS.COLUMN_NAME,
  COLUMNS.POSITION AS ORDINAL_POSITION
FROM
  ${catalogscope}_CONSTRAINTS TABLE_CONTRAINTS
  INNER JOIN ${catalogscope}_CONS_COLUMNS COLUMNS
    ON TABLE_CONTRAINTS.OWNER = COLUMNS.OWNER
      AND TABLE_CONTRAINTS.TABLE_NAME = COLUMNS.TABLE_NAME
      AND TABLE_CONTRAINTS.CONSTRAINT_NAME = COLUMNS.CONSTRAINT_NAME
  INNER JOIN ${catalogscope}_USERS USERS
    ON TABLE_CONTRAINTS.OWNER = USERS.USERNAME
      AND USERS.ORACLE_MAINTAINED = 'N'
      AND NOT REGEXP_LIKE(USERS.USERNAME, '^APEX_[0-9]{6}$')
      AND NOT REGEXP_LIKE(USERS.USERNAME, '^FLOWS_[0-9]{5}$')
WHERE
  REGEXP_LIKE(TABLE_CONTRAINTS.OWNER, '${schema-inclusion-rule}')
  AND REGEXP_LIKE(TABLE_CONTRAINTS.OWNER || '.' || TABLE_CONTRAINTS.TABLE_NAME, '${table-inclusion-rule}')
  AND TABLE_CONTRAINTS.TABLE_NAME NOT LIKE 'BIN$%'
  AND NOT REGEXP_LIKE(TABLE_CONTRAINTS.TABLE_NAME, '^(SYS_IOT|MDOS|MDRS|MDRT|MDOT|MDXT)_.*$')
  AND TABLE_CONTRAINTS.CONSTRAINT_TYPE IN ('C', 'U', 'P', 'R')
