###
###
###
1、OracleDataSource 指定ORACLE数据源；
2、OracleBeanBuilder 运行可导出Mysql版本的建表语句，仅限table；main函数中可指定导出文件路径；
3、DataExporter 运行可导出所有table的数据；main函数中可指定导出文件路径；
4、导出为sql文件后，在mysql中执行sql文件即可。

目前针对特殊情况的处理：
	1、某些关键字，在mysql中，不允许做为列名使用，则在该列名前加“_”;见：MysqlConvertorImpl.errorNames;
	2、某些表数据上存在问题，暂不导出为脚本；见：DataExporter.exludeTables;
	3、部分处理逻辑尚未总结；
	
本机编译环境：
	JDK1.6.0.25
	lib：/antlr-3.1.1.jar
		 /stringtemplate-2.3b6.jar
		 /ojdbc14-10.2.0.3.0.jar

