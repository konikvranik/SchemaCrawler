SELECT
  NULL AS PKTABLE_CAT,
  P.OWNER AS PKTABLE_SCHEM,
  P.TABLE_NAME AS PKTABLE_NAME,
  PC.COLUMN_NAME AS PKCOLUMN_NAME,
  NULL AS FKTABLE_CAT,
  F.OWNER AS FKTABLE_SCHEM,
  F.TABLE_NAME AS FKTABLE_NAME,
  FC.COLUMN_NAME AS FKCOLUMN_NAME,
  FC.POSITION AS KEY_SEQ,
  NULL AS UPDATE_RULE,
  DECODE(F.DELETE_RULE, 'CASCADE', 0, 'SET NULL', 2, 1) AS DELETE_RULE,
  F.CONSTRAINT_NAME AS FK_NAME,
  P.CONSTRAINT_NAME AS PK_NAME,
  DECODE(F.DEFERRABLE, 'DEFERRABLE', 5, 'NOT DEFERRABLE', 7, 'DEFERRED', 6) AS DEFERRABILITY
FROM
  ${catalogscope}_CONSTRAINTS P
  INNER JOIN ${catalogscope}_USERS USERS
    ON P.OWNER = USERS.USERNAME
  INNER JOIN ${catalogscope}_CONSTRAINTS F
    ON P.OWNER = F.R_OWNER
      AND P.CONSTRAINT_NAME = F.R_CONSTRAINT_NAME
  INNER JOIN ${catalogscope}_CONS_COLUMNS PC
    ON PC.OWNER = P.OWNER
      AND PC.CONSTRAINT_NAME = P.CONSTRAINT_NAME
      AND PC.TABLE_NAME = P.TABLE_NAME
  INNER JOIN ${catalogscope}_CONS_COLUMNS FC
    ON FC.OWNER = F.OWNER
      AND FC.CONSTRAINT_NAME = F.CONSTRAINT_NAME
      AND FC.TABLE_NAME = F.TABLE_NAME
      AND FC.POSITION = PC.POSITION
WHERE
  REGEXP_LIKE(P.OWNER, '${schema-inclusion-rule}')
  AND REGEXP_LIKE(P.OWNER || '.' || P.TABLE_NAME, '${table-inclusion-rule}')
  AND P.TABLE_NAME NOT LIKE 'BIN$%'
  AND NOT REGEXP_LIKE(P.TABLE_NAME, '^(SYS_IOT|MDOS|MDRS|MDRT|MDOT|MDXT)_.*$')
  AND USERS.ORACLE_MAINTAINED = 'N'
  AND NOT REGEXP_LIKE(USERS.USERNAME, '^APEX_[0-9]{6}$')
  AND NOT REGEXP_LIKE(USERS.USERNAME, '^FLOWS_[0-9]{5}$')
  AND P.CONSTRAINT_TYPE IN ('P', 'U')
  AND F.CONSTRAINT_TYPE = 'R'
ORDER BY
  PKTABLE_SCHEM,
  PKTABLE_NAME,
  KEY_SEQ
