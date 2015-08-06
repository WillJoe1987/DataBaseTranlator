CREATE OR REPLACE TABLE $table.name$  
(
	$table.columns:{s|$s.name$ $s.type$ $if(s.lengthable)$($s.dataLength$$if(s.scalable)$,$s.scale$$endif$)$endif$ $if(!s.nullable)$ NOT NULL $endif$ $if(s.isPK)$ PRIMARY KEY $endif$};separator=", \n"$
);
$if(table.comment)$COMMENT ON TABLE $table.name$ IS '$table.comment$'; $endif$

$table.columns:{s|$if(s.comment)$ COMMENT ON COLUMN $table.name$.$s.name$ IS '$s.comment$' $endif$};separator="; \n"$
