DROP TABLE IF EXISTS `$table.name$`;

CREATE TABLE `$table.name$`  
(
	$table.columns:{s|`$s.name$` $s.type$ $if(s.lengthable)$($s.dataLength$$if(s.scalable)$,$s.scale$$endif$)$endif$ $if(!s.nullable)$ NOT NULL $endif$ $if(s.isPK)$ PRIMARY KEY $endif$ $if(s.autoEncrease)$ AUTO_INCREMENT $endif$$if(s.comment)$ COMMENT '$s.comment$' $endif$};separator=", \n"$
);
$if(table.comment)$ALTER TABLE $table.name$ COMMENT '$table.comment$'; $endif$