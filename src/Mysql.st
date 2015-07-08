CREATE TABLE $table.name$  (
	$ table.columns:{s|$s.name$ $s.type$};  separator=", \n"$
)

$if(table.comment)$ COMMENT IS  $table.comment$ $endif$